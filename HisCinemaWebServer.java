import java.io.*;
import java.net.*;
import java.util.Date;

public class HisCinemaWebServer implements Runnable 
{
	ServerSocket hisCinemaWebSocket;
	
	public HisCinemaWebServer(InetAddress addr, int port) throws Exception
	{
		hisCinemaWebSocket= new ServerSocket(port, 100, addr);
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
					Socket client = this.hisCinemaWebSocket.accept();
					try
					{
						//PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
						//pw.println(new Date().toString());
						BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String line = reader.readLine();
						String recieved_message = "";
						
						while(line != null)
						{
							//System.out.println(line);
							recieved_message+=line;
							line = reader.readLine();
						}
						
						System.out.println(recieved_message);
						
						if("index.txt".equals(recieved_message.split(":")[1]))
						{
							System.out.println("ayylmao");
						}
						reader.close();
					}
					finally
					{
						client.close();
					}
				}
			}
			finally
			{
				hisCinemaWebSocket.close();
			}
		}
		catch(Exception e)
		{
			
		}
		
	}
}