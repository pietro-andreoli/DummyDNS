import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HisCinemaWebServer {
	static ServerSocket hisCinemaWebSocket;
	static InetAddress THIS_PC_IP;
	static int THIS_PC_PORT = 40437;
	static InetAddress HIS_CINEMA_DNS_IP;
	static int HIS_CINEMA_DNS_PORT = 40437;
	/*
	 * CHANGE IP HERE
	 */
	public HisCinemaWebServer() throws IOException{
		THIS_PC_IP = InetAddress.getByName("127.0.0.1");
		hisCinemaWebSocket = new ServerSocket(THIS_PC_PORT, 100, THIS_PC_IP);
	}
	public static void main(String[] args) throws IOException{
		HisCinemaWebServer hisCinemaServer = new HisCinemaWebServer();
		waitForQuery();
	}
	public static void waitForQuery() throws IOException{
		while(true){
			Socket receivingSocket = hisCinemaWebSocket.accept();
			try
			{	
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(receivingSocket.getInputStream()));
				
				DataOutputStream outToClient = new DataOutputStream(receivingSocket.getOutputStream());
				
			
				
				String clientMessage = "";
				String fullMessage = "";
				try
				{
					while(clientMessage != null)
					{
						
						if(inFromClient.ready())
						{
							clientMessage = inFromClient.readLine();
							fullMessage += clientMessage;
							System.out.println(clientMessage);
							if(clientMessage.equals("")) clientMessage = null;
							//break;
						}
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				
				outToClient.writeBytes("Request received, sending index.txt to IP: " + receivingSocket.getInetAddress() + " on Port: " + receivingSocket.getPort()+"\n");
			
				if(fullMessage.contains("GET /index.html"))
				{
					File index = new File(System.getProperty("user.dir")+"\\src\\index.txt");
					byte[] fileByteArray = new byte[(int)index.length()];
					FileInputStream fis = new FileInputStream(index);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(fileByteArray, 0, fileByteArray.length);
					OutputStream os = receivingSocket.getOutputStream();
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
			receivingSocket.close();
		}
	}
}
