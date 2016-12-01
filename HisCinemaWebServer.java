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
			try
			{
				while(true)
				{
					Socket client = this.hisCinemaWebSocket.accept();
					try
					{
						System.out.println("ayy");
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
						System.out.println("ayy");
						if(recieved_message.contains("GET /index.html")){
							File index = new File(System.getProperty("user.dir")+"\\src\\index.txt");
							byte[] fileByteArray = new byte[(int)index.length()];
							FileInputStream fis = new FileInputStream(index);
							BufferedInputStream bis = new BufferedInputStream(fis);
							bis.read(fileByteArray, 0, fileByteArray.length);
							OutputStream os = client.getOutputStream();
							os.write(fileByteArray, 0, fileByteArray.length);
							os.flush();
							fis.close();
							bis.close();
							os.close();
						}
						
						reader.close();
					}catch(Exception e){
						System.out.println(e);
					}finally
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