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
	static InetAddress HIS_CINEMA_DNS_IP;
	static int HIS_CINEMA_DNS_PORT = 40435;
	static InetAddress CLIENT_IP;
	static int CLIENT_PORT;
	static int numConnects = 0;
	public LocalDNS() throws SocketException, UnknownHostException{
		THIS_PC_IP = InetAddress.getByName("127.0.0.1");
		HIS_CINEMA_DNS_IP = InetAddress.getByName("127.0.0.1");
		HER_CDN_IP = InetAddress.getByName("127.0.0.1");
		localDNSSocket = new DatagramSocket(THIS_PC_PORT, THIS_PC_IP);
	}
	public static void main(String[] args) throws SocketException, UnknownHostException{
		LocalDNS thisLocalDNS = new LocalDNS();
		waitForQuery();
	}
	public static void waitForQuery(){
		while(true){
			try
			{
				byte[] receiveData = new byte[1024];
				DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
				localDNSSocket.receive(rcvPkt);
				if(numConnects == 0){
					numConnects++;
					System.out.println(rcvPkt.getSocketAddress().toString().split(":")[0]);
					System.out.println(Integer.parseInt((rcvPkt.getSocketAddress().toString().split(":")[1])));
					CLIENT_IP = InetAddress.getByName(rcvPkt.getSocketAddress().toString().split(":")[0].split("/")[1]);
					CLIENT_PORT = Integer.parseInt((rcvPkt.getSocketAddress().toString().split(":")[1]));
				}                        
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
				String temp = new String(data);
				if(temp.split(", ")[2].equals("A")){
					replyToClient(data);
				}
				if(new String(data).contains("dns.hiscinema.com")){
					queryHisCinemaDNS(data);
				}else if(new String(data).contains("dns.herCDN.com")){
					queryHerCDN(data);
				}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

	public static void queryHisCinemaDNS(byte[] data){
		localDNSSocket.connect(HIS_CINEMA_DNS_IP, HIS_CINEMA_DNS_PORT);
		
		System.out.println("Client Local DNS is querying hiscinema DNS at IP address: " + HIS_CINEMA_DNS_IP + " on Port: " + HIS_CINEMA_DNS_PORT +"\n");
		
		DatagramPacket sndPkt = new DatagramPacket(data, data.length, HIS_CINEMA_DNS_IP, HIS_CINEMA_DNS_PORT);
		
		try
		{
			System.out.println("Querying hiscinema dns from LocalDNS");
			localDNSSocket.send(sndPkt);
			localDNSSocket.disconnect();
			
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
	
	public static void replyToClient(byte[] data){
		localDNSSocket.connect(CLIENT_IP, CLIENT_PORT);
		
		System.out.println("Client Local DNS is replying to client at IP address: " + CLIENT_IP + " on Port: " + CLIENT_PORT +"\n");
		
		DatagramPacket sndPkt = new DatagramPacket(data, data.length, CLIENT_IP, CLIENT_PORT);
		
		try
		{
			System.out.println("Querying CLIENT from LocalDNS");
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
        
        String recordType = dataParts[0][1];
        
        if(recordType.contains("dns.hiscinema.com")){
        	//do nothing
        }
        else if(recordType.contains("dns.herCDN.com"))
        {
            outputData = (dataParts[0][0]+", dns.herCDN.com, NS)\n(herCDN.com, localhost, A)").getBytes();
        }
        return outputData;
    }
}
