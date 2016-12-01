import java.net.*;

public class HerCDN_DNS implements Runnable
{
	private DatagramSocket herContentDomainSocket;
	
	public HerCDN_DNS(InetAddress addr, int port) throws Exception
	{
		herContentDomainSocket= new DatagramSocket(port,addr);
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
	}
}