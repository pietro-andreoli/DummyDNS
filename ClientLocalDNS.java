import java.net.*;

public class ClientLocalDNS implements Runnable
{
	private DatagramSocket localDomainUDP1;
	private DatagramSocket localDomainUDP2;
	private DatagramSocket localDomainUDP3;
	
	public ClientLocalDNS(InetAddress addr, int port1, int port2, int port3) throws Exception
	{
		localDomainUDP1 = new DatagramSocket(port1,addr);
		localDomainUDP2 = new DatagramSocket(port2,addr);
		localDomainUDP3 = new DatagramSocket(port3,addr);
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}
}