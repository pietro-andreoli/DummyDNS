import java.io.*;
import java.net.*;
import java.util.Date;

public class HisCinemaWebServer implements Runnable 
{
	ServerSocket hisCinemaWebSocket;
	
	public HisCinemaWebServer(InetAddress addr, int port) throws Exception
	{
		this.hisCinemaWebSocket= new ServerSocket(port, 100, addr);
		
	}
	
	@Override
	public void run() 
	{
		try
		{
				while(true)
				{
					Socket client = this.hisCinemaWebSocket.accept();
					try
					{	
						BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
						
						DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
						
						String clientMessage = "";
						
						try
						{
							while(clientMessage!=null)
							{
								clientMessage = inFromClient.readLine();
								System.out.println(clientMessage);
								if(!inFromClient.ready())
								{
									break;
								}
							}
						}
						catch(Exception e)
						{
							System.out.println(e);
						}
						
						outToClient.writeBytes("Request received, sending index.txt to IP: " + client.getInetAddress() + " on Port: " + client.getPort() + "\n");
					
						if(clientMessage.contains("GET /index.html"))
						{
							File index = new File(System.getProperty("user.dir")+"\\src\\index.txt");
							byte[] fileByteArray = new byte[(int)index.length()];
							FileInputStream fis = new FileInputStream(index);
							BufferedInputStream bis = new BufferedInputStream(fis);
							bis.read(fileByteArray, 0, fileByteArray.length);
							OutputStream os = client.getOutputStream();
							os.write(fileByteArray, 0, fileByteArray.length);
							//System.out.println(fileByteArray.toString());
							os.flush();
							fis.close();
							bis.close();
							os.close();
						}
						inFromClient.close();
						outToClient.close();
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
					client.close();
					
				}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}