import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Her_CDN_DNS {
	InetAddress THIS_PC_IP;
	int THIS_PC_PORT = 40436;
	static DatagramSocket herDNSSocket;
	static InetAddress LOCAL_DNS_IP;
	static int LOCAL_DNS_PORT;
	static InetAddress HER_CDN_IP;
	static int HER_CDN_PORT = 40438;
	public Her_CDN_DNS() throws UnknownHostException, SocketException{
		HER_CDN_IP = InetAddress.getByName("localhost");
		THIS_PC_IP = InetAddress.getByName("localhost");
		herDNSSocket = new DatagramSocket(THIS_PC_PORT, THIS_PC_IP);
	}
	public static void main(String[] args) throws UnknownHostException, SocketException{
		Her_CDN_DNS herCDNDNS = new Her_CDN_DNS();
		waitForQuery();
	}
	public static void waitForQuery(){
		try
		{
			byte[] receiveData = new byte[1024];
			DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
			System.out.println("trying to receive data from localDNS in Her_CDN_DNS");
			herDNSSocket.receive(rcvPkt);
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
				byte[] outputData = analyzeMessage(data);
				replyToLocalDNS(outputData);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void replyToLocalDNS(byte[] data){
		herDNSSocket.connect(LOCAL_DNS_IP, LOCAL_DNS_PORT);
		
		System.out.println("herCDN is replying local DNS at IP address: " + LOCAL_DNS_IP + " on Port: " + LOCAL_DNS_PORT +"\n");
		
		DatagramPacket sndPkt = new DatagramPacket(data, data.length, LOCAL_DNS_IP, LOCAL_DNS_PORT);
		
		try
		{
			System.out.println("replying to local dns from hercdn");
			herDNSSocket.send(sndPkt);
			
			
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
        
        outputData = ("(herCDN.com, "+HER_CDN_IP+":"+HER_CDN_PORT+", A)").getBytes();
        //outputData = ("(herCDN.com,"+HER_CDN_IP+", A)").getBytes();
        return outputData;
    }
}