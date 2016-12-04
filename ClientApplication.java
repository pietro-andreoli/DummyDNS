import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientApplication {
	static String indexContent = "";
	static byte[] videoContent;
	static InetAddress THIS_PC_IP;
	static int THIS_PC_PORT = 40430;
	static InetAddress HIS_CINEMA_WEB_SERVER_IP;
	static int HIS_CINEMA_WEB_SERVER_PORT = 40437;
	static InetAddress LOCAL_DNS_IP;
	static int LOCAL_DNS_PORT = 40432;
	static DatagramSocket thisPCSocket;
	//static DatagramSocket thisPCSocket;
	public static void main(String[] args) throws UnknownHostException, IOException{
		/*
		 * MODIFY IPS
		 * **************************************************
		 */
		THIS_PC_IP = InetAddress.getByName("localhost");
		HIS_CINEMA_WEB_SERVER_IP =  InetAddress.getByName("localhost");
		LOCAL_DNS_IP = InetAddress.getByName("localhost");
		ArrayList<String> fileContents = connectToHisCinema();
		int clientRequest;
		while(true)
		{
			Scanner getVideo = new Scanner(System.in);
			
			System.out.println("Please enter a number for the video you wish to request: Please choose a number between 1 and "+fileContents.size()+" inclusively");
			
			clientRequest = getVideo.nextInt();
			getVideo.close();
			if(!(clientRequest > fileContents.size()) && !(clientRequest < 1))
			{
				System.out.println("User Chose: "+ fileContents.get(clientRequest-1));
				break;
			}
			
			
		
		}
		InetAddress herCDNip = queryLocalDNS(fileContents.get(clientRequest-1));
		queryHerCDN(herCDNip);
	}
	
	
	
	private static void queryHerCDN(InetAddress herCDNip) throws IOException 
	{
		Socket sendSocket = new Socket(HIS_CINEMA_WEB_SERVER_IP, HIS_CINEMA_WEB_SERVER_PORT, THIS_PC_IP, THIS_PC_PORT);
		
		byte[] serverReply = null;
		InputStream is = sendSocket.getInputStream();
		FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir"));
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		DataOutputStream toServer = new DataOutputStream(sendSocket.getOutputStream());	
		
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
		
		toServer.writeBytes("(www.herCDN.com/videox, dns.herCND.com, V, 86400)\n(herCDN.com, "+herCDNip+", A)");
		toServer.flush();
		
		
		try
		{
			boolean ack = false;
			while(serverReply!=null)
			{
					ack = true;
			}
			
			if(ack == true)
			{
				
				while(serverReply!=null)
				{
					int bytesRead =  is.read(serverReply, 0, serverReply.length);
					bos.write(serverReply, 0, bytesRead);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		sendSocket.close();	

	}



	public static ArrayList<String> connectToHisCinema() throws UnknownHostException, IOException
	{
		Socket sendSocket = new Socket(HIS_CINEMA_WEB_SERVER_IP, HIS_CINEMA_WEB_SERVER_PORT, THIS_PC_IP, THIS_PC_PORT);
		ArrayList<String> fileContents = new ArrayList<String>();
		DataOutputStream toServer = new DataOutputStream(sendSocket.getOutputStream());	
		
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
		
		//InetAddress ip = sendSocket.getInetAddress();
		//int port = sendSocket.getPort();
		
		
		toServer.writeBytes("GET /index.html HTTP/1.1\r\n Host: www.hiscinema.com\r\n to IP: " + HIS_CINEMA_WEB_SERVER_IP.getHostAddress() 
			+ "\r\n on Port: " + HIS_CINEMA_WEB_SERVER_PORT + "\r\n\r");
		toServer.flush();
		String serverReply = "";
		
		try
		{
			boolean ack = false;
			while(serverReply!=null)
			{
				
				serverReply = inFromServer.readLine();
				System.out.println(serverReply);
				indexContent+=serverReply;
				if(serverReply.contains("Request received"))
				{
					ack = true;
					break;
				}
			}
			if(ack == true)
			{
				serverReply = "";
				while(serverReply!=null)
				{
					
					serverReply = inFromServer.readLine();
					System.out.println(":"+serverReply);
					if(serverReply != null)
						fileContents.add(serverReply);
					indexContent+=serverReply;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		sendSocket.close();	
		return fileContents;
	}
	
	public static InetAddress queryLocalDNS(String videoURL) throws SocketException, UnknownHostException
	{
		thisPCSocket = new DatagramSocket(THIS_PC_PORT, THIS_PC_IP);
		thisPCSocket.connect(LOCAL_DNS_IP, LOCAL_DNS_PORT);
		
		String msg = "(" + videoURL + ", dns.hiscinema.com, V, 86400) \n(hiscinema.com, " + LOCAL_DNS_IP + ", A)";
		System.out.println("Querying Local DNS at IP address: " + LOCAL_DNS_IP + " on Port: " +LOCAL_DNS_PORT+"\n");
		
		System.out.println(msg+"\n");
		
		DatagramPacket sndPkt = new DatagramPacket(msg.getBytes(), msg.length(), LOCAL_DNS_IP, LOCAL_DNS_PORT);
		
	//	InetAddress cinemaDNSIP = hisCinemaDNS.getUDPSocket().getLocalAddress();
	//	int cinemaDNSPort = hisCinemaDNS.getUDPSocket().getLocalPort();
	//	int contentDNSPort = herContentDomain.getUDPSocket().getLocalPort();
		
		try
		{
			System.out.println("Querying the localDNS");
			thisPCSocket.send(sndPkt);
			thisPCSocket.disconnect();
			byte[] rcvData = new byte[1024];
			DatagramPacket rcvPkt = new DatagramPacket(rcvData, rcvData.length);
			System.out.println("Recieving response from localDNS");
			thisPCSocket.receive(rcvPkt);
			System.out.println("Response recieved");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		//InetAddress cinemaDNSip = clientDNS.queryCinemaDNS(msg, cinemaDNSIP , cinemaDNSPort );
		//InetAddress herCDNIP = clientDNS.queryContentDNS( msg , cinemaDNSip , contentDNSPort );
		
		return null;
	}
}