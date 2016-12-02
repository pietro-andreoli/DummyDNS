import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ClientLocalDNS implements Runnable
{ 
	private static DatagramSocket localDomainUDP;
	
	public ClientLocalDNS(InetAddress addr, int port) throws Exception
	{
		localDomainUDP = new DatagramSocket(port, addr);
	}
	
	@Override
	public void run() 
	{
		try
		{
					byte[] receiveData = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
					this.localDomainUDP.receive(rcvPkt);
					try
					{	
						byte[] data = rcvPkt.getData();
						System.out.println(new String(data));	
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
	
	public InetAddress queryCinemaDNS(String DNS_Query, InetAddress address, int port) throws SocketException, UnknownHostException
	{
		String msg = DNS_Query;
		DatagramSocket toServerSocket = new DatagramSocket(40439,InetAddress.getByName("localhost"));
		toServerSocket.connect(address, port);
		
		System.out.println("Querying hiscinema.com DNS at IP address: " + toServerSocket.getInetAddress() + " on Port: " + toServerSocket.getPort());
		System.out.println(msg);
		DatagramPacket sndPkt = new DatagramPacket(msg.getBytes(), msg.length(), toServerSocket.getInetAddress(), toServerSocket.getPort());
		
		try
		{
		toServerSocket.send(sndPkt);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return localDomainUDP.getInetAddress();
	}

	public InetAddress queryContentDNS(InetAddress queryCinemaDNS) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public void analyzeMessage(byte[] data){
		String dataS = new String(data);
		//String[] dataChunks = dataS.split("\n");
		String[] dataChunks = dataS.split(",");
		String[][] dataParts = new String[dataChunks.length][];
		if(dataChunks[2].contains("A")){
			//then we recieved the ip of the place we want to go.
		}else if(dataChunks[2].contains("")){
			
		}
		/*for(int i = 0; i < data_chunks.length; i++){
			data_parts[i] = data_chunks[i].split(",");
		}
		if(data_parts[0][2].contains("A")
		for(int i = 0; i < data_parts.length; i++){
			for(int j = 0; j < data_parts[i].length; j++){
				//if(data_parts[i])
			}
		}*/
	}
}