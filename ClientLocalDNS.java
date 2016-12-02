import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ClientLocalDNS implements Runnable
{
	private DatagramSocket localDomainUDP1;
	private DatagramSocket localDomainUDP2;
	private DatagramSocket localDomainUDP3;
	private DatagramSocket localDomainUDP;
	
	public ClientLocalDNS(InetAddress addr, int port) throws Exception
	{
		localDomainUDP = new DatagramSocket(port, addr);
	}
	
	@Override
	public void run() 
	{
		try
		{
				while(true)
				{
					byte[] recvBuff = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(recvBuff, 1024);
					this.localDomainUDP.receive(rcvPkt);
					try
					{	
						//BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
						
						//DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
						
						String clientMessage = "";
						
						try
						{
							while(clientMessage!=null)
							{
								System.out.println(clientMessage);
								clientMessage = new String(rcvPkt.getData(), 0, rcvPkt.getLength());
								
							}
						}
						catch(Exception e)
						{
							System.out.println("bad doot");
						}
						
						/*outToClient.writeBytes("Request received, sending index.txt to IP: " + client.getInetAddress() + " on Port: " + client.getPort() + "\n");
						outToClient.flush();
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
						*/
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
					//client.close();
					
				}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}