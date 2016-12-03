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
		
		System.out.println("Client Local DNS is querying hiscinema.com DNS at IP address: " + toServerSocket.getInetAddress() + " on Port: " + toServerSocket.getPort()+"\n");
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

	public InetAddress queryContentDNS(String DNS_Query, InetAddress CDNaddress, int port) throws SocketException, UnknownHostException 
	{
		String msg = DNS_Query;
		DatagramSocket toServerSocket = new DatagramSocket(40438,InetAddress.getByName("localhost"));
		toServerSocket.connect(InetAddress.getByName("localhost"), port);
		
		System.out.println("Client Local DNS is querying herCDN.com DNS at IP address: " + toServerSocket.getInetAddress() + " on Port: " + toServerSocket.getPort() +"\n");
		
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
            outputData = ("("+dataParts[0][0]+", dns.herCDN.com, NS)\n(herCDN.com, localhost, A)").getBytes();
        }
        return outputData;
    }
	
}