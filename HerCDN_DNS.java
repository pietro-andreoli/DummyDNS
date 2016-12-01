import java.net.*;

public class HerCDN_DNS implements Runnable
{
	private ServerSocket herContentDomainSocket;
	
	public HerCDN_DNS(InetAddress addr, int port) throws Exception
	{
		herContentDomainSocket= new ServerSocket(port, 100, addr);
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}

}