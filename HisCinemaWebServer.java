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
		System.out.println("Doot");
		try
		{
				while(true)
				{
					Socket client = this.hisCinemaWebSocket.accept();
					try
					{
						System.out.println("ayy1");
						
						//PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
						//pw.println(new Date().toString());
						
						BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
						
						DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
						
						String clientMessage = inFromClient.readLine();
						
						
						System.out.println(clientMessage);
						
						outToClient.writeBytes("Request received, sending index.txt to IP: " + client.getInetAddress() + " on Port: " + client.getPort() + "\n");
				
						System.out.println("ayy2");
						
						
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
			
		}
	}
}