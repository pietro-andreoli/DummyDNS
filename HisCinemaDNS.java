import java.net.*;

public class HisCinemaDNS implements Runnable
{
	private DatagramSocket hisCinemaDomainUDP;
	
	public HisCinemaDNS(InetAddress addr, int port) throws Exception
	{
		hisCinemaDomainUDP= new DatagramSocket(port,addr);
	}
	
	public DatagramSocket getUDPSocket()
	{
		return hisCinemaDomainUDP;
	}
	@Override
	public void run() 
	{
		try
		{
					byte[] receiveData = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
					this.hisCinemaDomainUDP.receive(rcvPkt);
					
					try
					{	
						int receivePort = ClientLocalDNS.getUDPSocket().getLocalPort();
						InetAddress receiveIP = rcvPkt.getAddress();
						
						byte[] data = rcvPkt.getData();
						byte[] output = analyzeMessage(data);
                        
                        DatagramPacket sndPkt = new DatagramPacket(output, output.length, receiveIP, receivePort);
                        
                        System.out.println("hiscinema.com DNS is replying to Client Local DNS with the type NS request on IP address: " + sndPkt.getAddress() + " on Port: " + sndPkt.getPort());
                        hisCinemaDomainUDP.connect(sndPkt.getSocketAddress());
                        hisCinemaDomainUDP.send(sndPkt);
                       
					}
					catch(Exception e)
					{
						System.out.println(e);
					}		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public byte[] analyzeMessage(byte[] data)
	{
        byte[] outputData = null;
        String dataS = new String(data);
        String[] dataChunks = dataS.split("\n");
        //String[] dataChunks = dataS.split(",");
        String[][] dataParts = new String[dataChunks.length][];

        for(int i = 0; i < dataChunks.length; i++)
        {
            dataParts[i] = dataChunks[i].split(",");
        }
        
        String recordType = dataParts[0][2];
        
        if(recordType.contains("A"))
        {
            //A type
        }
        else if(recordType.contains("V"))
        {
            outputData = (dataParts[0][0]+", dns.herCDN.com, NS)\n (herCDN.com, "+ HerCDN_DNS.getUDPSocket().getLocalAddress() + ", A)").getBytes();
        }
        return outputData;
    }
}