import java.net.*;

public class HisCinemaDNS implements Runnable
{
	private DatagramSocket hisCinemaDomainSocket;
	
	public HisCinemaDNS(InetAddress addr, int port) throws Exception
	{
		hisCinemaDomainSocket= new DatagramSocket(port,addr);
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}

}