import java.net.*;

public class HisCinemaDNS implements Runnable
{
	private ServerSocket hisCinemaDomainSocket;
	
	public HisCinemaDNS(InetAddress addr, int port) throws Exception
	{
		hisCinemaDomainSocket= new ServerSocket(port, 100, addr);
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}

}