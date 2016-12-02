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
		System.out.println("doot");
		localDomainUDP.connect(address,port);
		System.out.println("doot");
		String msg = DNS_Query;
		System.out.println("doot");
		System.out.println(DNS_Query);
		System.out.println("doot");
		
		DatagramSocket toServerSocket = new DatagramSocket(40439,InetAddress.getByName("localhost"));
		toServerSocket.connect(InetAddress.getByName("localhost"), 40432);
		System.out.println("Querying DNS at IP address: " + toServerSocket.getInetAddress() + " on Port: " + toServerSocket.getPort());
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
}