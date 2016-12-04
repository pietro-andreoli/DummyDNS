import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HisCinemaDNS {
	InetAddress THIS_PC_IP;
	int THIS_PC_PORT = 40435;
	static DatagramSocket hisCinemaDNSSocket;
	private static InetAddress LOCAL_DNS_IP;
	private static int LOCAL_DNS_PORT;
	public HisCinemaDNS() throws UnknownHostException, SocketException{
		THIS_PC_IP = InetAddress.getByName("localhost");
		hisCinemaDNSSocket = new DatagramSocket(THIS_PC_PORT, THIS_PC_IP);
	}
	public static void main(String[] args) throws UnknownHostException, SocketException{
		HisCinemaDNS thisDNS = new HisCinemaDNS();
		waitForQuery();
	}
	
	public static void waitForQuery(){
		try
		{
			byte[] receiveData = new byte[1024];
			DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
			System.out.println("trying to receive data from localDNS in Her_CDN_DNS");
			hisCinemaDNSSocket.receive(rcvPkt);
			System.out.println("Data recieved in her cdn dns");
			LOCAL_DNS_IP = InetAddress.getByName(rcvPkt.getSocketAddress().toString().split(":")[0].split("/")[1]);
			LOCAL_DNS_PORT = Integer.parseInt((rcvPkt.getSocketAddress().toString().split(":")[1]));
			byte[] data = null;
			try
			{	
				data = rcvPkt.getData();
				System.out.println(new String(data));	
			}
			catch(Exception e)
			{
				System.out.println(e);
			}		
			
			if(new String(data).contains("hiscinema.com/video")){
				replyToLocalDNS(analyzeMessage(data));
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void replyToLocalDNS(byte[] data){
		hisCinemaDNSSocket.connect(LOCAL_DNS_IP, LOCAL_DNS_PORT);
		
		System.out.println("his cinema dns is replying to local DNS at IP address: " + LOCAL_DNS_IP + " on Port: " + LOCAL_DNS_PORT +"\n");
		
		DatagramPacket sndPkt = new DatagramPacket(data, data.length, LOCAL_DNS_IP, LOCAL_DNS_PORT);
		
		try
		{
			System.out.println("replying to local dns from hiscinema dns");
			hisCinemaDNSSocket.send(sndPkt);
			
			
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
        
       
        outputData = (dataParts[0][0]+", dns.herCDN.com, NS)\n(herCDN.com, localhost, A)").getBytes();
        return outputData;
    }
}