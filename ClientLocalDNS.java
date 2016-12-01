import java.net.*;

public class ClientLocalDNS implements Runnable
{
	private DatagramSocket localDomainUDP1;
	private DatagramSocket localDomainUDP2;
	private DatagramSocket localDomainUDP3;
	
	public ClientLocalDNS(InetAddress addr, int port) throws Exception
	{
		localDomainUDP1 = new DatagramSocket(port,addr);
		localDomainUDP2 = new DatagramSocket(port,addr);
		localDomainUDP3 = new DatagramSocket(port,addr);
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}

}