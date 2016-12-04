import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class LocalDNS {
	static InetAddress THIS_PC_IP;
	static int THIS_PC_PORT = 40432;	
	static DatagramSocket localDNSSocket;
	static InetAddress HER_CDN_IP;
	static int HER_CDN_PORT;
	public LocalDNS() throws SocketException, UnknownHostException{
		THIS_PC_IP = InetAddress.getByName("localhost");
		localDNSSocket = new DatagramSocket(THIS_PC_PORT, THIS_PC_IP);
	}
	public static void main(String[] args) throws SocketException, UnknownHostException{
		LocalDNS thisLocalDNS = new LocalDNS();
		waitForQuery();
	}
	public static void waitForQuery(){
		try
		{
					byte[] receiveData = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
					localDNSSocket.receive(rcvPkt);
					String rcvData = "";
					byte[] data = null;
					try
					{	
						data = rcvPkt.getData();
						rcvData = new String(data);
						System.out.println(new String(data));	
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
					byte[] outputData = analyzeMessage(data);
					if(new String(outputData).contains("dns.herCDN.com")){
						queryHerCDN(outputData);
					}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void queryHerCDN(byte[] data){
		
		localDNSSocket.connect(HER_CDN_IP, HER_CDN_PORT);
		
		System.out.println("Client Local DNS is querying herCDN.com DNS at IP address: " + HER_CDN_IP + " on Port: " + HER_CDN_PORT +"\n");
		
		DatagramPacket sndPkt = new DatagramPacket(data, data.length, HER_CDN_IP, HER_CDN_PORT);
		
		try
		{
			System.out.println("Querying HERCDN from LocalDNS");
			localDNSSocket.send(sndPkt);
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static byte[] analyzeMessage(byte[] data)
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
            outputData = (dataParts[0][0]+", dns.herCDN.com, NS)\n(herCDN.com, localhost, A)").getBytes();
        }
        return outputData;
    }
}
