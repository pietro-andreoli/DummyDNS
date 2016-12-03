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
	private DatagramSocket localDNSSend;
	private static DatagramSocket localDNSReceive;
	
	public ClientLocalDNS(InetAddress addr, int sendPort, int receivePort) throws Exception
	{
		localDNSSend = new DatagramSocket(sendPort, addr);
		localDNSReceive = new DatagramSocket(receivePort, addr);
	}
	
	public static DatagramSocket getReceiveSocket()
	{
		return localDNSReceive;
	}
	
	@Override
	public void run() 
	{
		try
		{
					byte[] receiveData = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
					this.localDNSReceive.receive(rcvPkt);
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
		
		localDNSSend.connect(address, port);
		
		System.out.println("Client Local DNS is querying hiscinema.com DNS at IP address: " + localDNSSend.getInetAddress() + " on Port: " + localDNSSend.getPort()+"\n");
		DatagramPacket sndPkt = new DatagramPacket(msg.getBytes(), msg.length(), localDNSSend.getInetAddress(), localDNSSend.getPort());
		
		try
		{
			localDNSSend.send(sndPkt);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return localDNSReceive.getInetAddress();
	}

	public InetAddress queryContentDNS(String DNS_Query, InetAddress CDNaddress, int port) throws SocketException, UnknownHostException 
	{
		String msg = DNS_Query;
		localDNSSend.connect(InetAddress.getByName("localhost"), port);
		
		System.out.println("Client Local DNS is querying herCDN.com DNS at IP address: " + localDNSSend.getInetAddress() + " on Port: " + localDNSSend.getPort() +"\n");
		
		DatagramPacket sndPkt = new DatagramPacket(msg.getBytes(), msg.length(), localDNSSend.getInetAddress(), localDNSSend.getPort());
		
		try
		{
			localDNSSend.send(sndPkt);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return localDNSReceive.getInetAddress();
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
            outputData = (dataParts[0][0]+", dns.herCDN.com, NS)\n(herCDN.com, localhost, A)").getBytes();
        }
        return outputData;
    }
	
}