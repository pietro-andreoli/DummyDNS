import java.io.PrintWriter;
import java.net.*;
import java.util.Date;

public class HerCDNWebServer implements Runnable
{
	ServerSocket herContentWebSocket;
	
	public HerCDNWebServer(InetAddress addr, int port) throws Exception
	{
		herContentWebSocket= new ServerSocket(port, 100, addr);
	}
	
	@Override
	public void run() 
	{
		try
		{
			try
			{
				while(true)
				{
					Socket client = this.herContentWebSocket.accept();
					try
					{
						PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
						pw.println(new Date().toString());
					}
					finally
					{
						client.close();
					}
				}
			}
			finally
			{
				herContentWebSocket.close();
			}
		}
		catch(Exception e)
		{
			
		}
	}
}