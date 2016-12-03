import java.net.*;

public class HerCDN_DNS implements Runnable
{
	private static DatagramSocket herContentDomainUDP;
	
	public HerCDN_DNS(InetAddress addr, int port) throws Exception
	{
		herContentDomainUDP= new DatagramSocket(port,addr);
	}
	
	static public DatagramSocket getUDPSocket()
	{
		return herContentDomainUDP;
	}
	
	@Override
	public void run() 
	{
		try
		{
					byte[] receiveData = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
					this.herContentDomainUDP.receive(rcvPkt);
					
					try
					{	
						int receivePort = ClientLocalDNS.getUDPSocket().getLocalPort();
						InetAddress receiveIP = rcvPkt.getAddress();
						
						byte[] data = rcvPkt.getData();
						byte[] output = analyzeMessage(data);
                        
                        DatagramPacket sndPkt = new DatagramPacket(output, output.length, receiveIP, receivePort);
                        
                        System.out.println("herCDN.com DNS is replying to Client Local DNS with the type A request on IP address: " + sndPkt.getAddress() + " on Port: " + sndPkt.getPort());
                        herContentDomainUDP.connect(sndPkt.getSocketAddress());
                        herContentDomainUDP.send(sndPkt);
                       
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
            outputData = (dataParts[0][0]+" , " + HerCDNWebServer.getTCPSocket().getInetAddress() + ", A)").getBytes();
        }
        return outputData;
    }
}