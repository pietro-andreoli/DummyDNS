import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

//This will function as the Main class for this project
public class ClientApplication
{
	static String indexContent = "";
	//Creating the network sockets for the Client Application
	static ServerSocket clientTCPSocket;
	static DatagramSocket clientToLocalDomainUDP;
	public static void main(String[] args) throws Exception
	{
		InetAddress[] ip_list = new InetAddress[4];
		Scanner ip_get = new Scanner(System.in);
		int ip_num=1;
		
		//While loop to gather the IP's to be initialized
		while(ip_num < 5)
		{
			System.out.println("Enter IP "+ip_num);
			ip_list[ip_num-1] = InetAddress.getByName(ip_get.nextLine());
			ip_num++;
		}
		
		//Creating the Client's sockets for communication
		clientTCPSocket = new ServerSocket(40430, 10, InetAddress.getByName("localhost"));
		clientToLocalDomainUDP = new DatagramSocket(40431, InetAddress.getByName("localhost"));
		
		//Creating the network sockets for the Local DNS
		ClientLocalDNS clientDNS1 = new ClientLocalDNS(InetAddress.getByName("localhost"), 40432);
		new Thread(clientDNS1).start();
		
		ClientLocalDNS clientDNS2 = new ClientLocalDNS(InetAddress.getByName("localhost"), 40433);
		new Thread(clientDNS2).start();
		
		ClientLocalDNS clientDNS3 = new ClientLocalDNS(InetAddress.getByName("localhost"), 40434);
		new Thread(clientDNS3).start();
		
		//Creating the network socket for hiscinema.com DNS
		HisCinemaDNS hisCinemaDNS = new HisCinemaDNS(InetAddress.getByName("localhost"), 40435);
		
		//Creating the network socket for herCDN.com DNS
		HerCDN_DNS herContentDomain = new HerCDN_DNS(InetAddress.getByName("localhost"), 40436);
		
		//Creating the network socket for hiscinema.com Web Server
		HisCinemaWebServer hisCinemaWeb = new HisCinemaWebServer(InetAddress.getByName("localhost"), 40437);
		
		
		//new Thread(new ConnectionHandler()).start();
		//Creating the network socket for herCDN.com Web Server
		HerCDNWebServer herCDNWeb = new HerCDNWebServer(InetAddress.getByName("localhost"), 40438);
		
		//Eventually all of the above should look something like these
		/*
		Socket clientToHisCinemaTCP = new Socket(ip_list[0] , 40430);
		Socket clientToHerContentTCP = new Socket(ip_list[0] , 40431);
		DatagramSocket clientToLocalDomainUDP = new DatagramSocket(40432 , ip_list[0]);
		*/
		
		new Thread(hisCinemaWeb).start();
		ArrayList<String> fileContents = connectToHisCinema();
		while(true){
			
			
			Scanner getVideo = new Scanner(System.in);
			
			System.out.println("Please enter a number for the video you wish to request: Please choose a number between 1 and "+fileContents.size()+"inclusively");
			
			int clientRequest = getVideo.nextInt();
			getVideo.close();
			if(!(clientRequest > fileContents.size()) && !(clientRequest < 1)){
				System.out.println("User Chose: "+ fileContents.get(clientRequest-1));
				break;
			}
			/*switch(clientRequest)
			{
			case 1: System.out.println("Client has chosen video 1"); queryLocalDNS("www.hiscinema.com/video1"); break;
			case 2: System.out.println("Client has chosen video 2"); queryLocalDNS("www.hiscinema.com/video2"); break;
			case 3: System.out.println("Client has chosen video 3"); queryLocalDNS("www.hiscinema.com/video3"); break;
			case 4: System.out.println("Client has chosen video 4"); queryLocalDNS("www.hiscinema.com/video4"); break;
			case 5: System.out.println("Client has chosen video 5"); queryLocalDNS("www.hiscinema.com/video5"); break;
				default: System.out.println("That is not a valid selection"); break;
			}*/
		
		}
		

	}
	
	public static ArrayList<String> connectToHisCinema() throws UnknownHostException, IOException
	{
		Socket sendSocket = new Socket(InetAddress.getByName("localhost"), 40437);
		ArrayList<String> fileContents = new ArrayList<String>();
		DataOutputStream toServer = new DataOutputStream(sendSocket.getOutputStream());	
		
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
		
		InetAddress ip = sendSocket.getInetAddress();
		int port = sendSocket.getPort();
		
		toServer.writeBytes("GET /index.html HTTP/1.1\r\n Host: www.hiscinema.com\r\n to IP: " + ip + "\r\n on Port: " + port + "\r\n");
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
				if(serverReply.contains("Request received")){
					ack = true;
					break;
				}
			}
			if(ack == true){
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
	
	public static void queryLocalDNS(String videoURL) throws SocketException, UnknownHostException
	{
		DatagramSocket toServerSocket = new DatagramSocket(40439,InetAddress.getByName("localhost"));
		toServerSocket.connect(InetAddress.getByName("localhost"), 40432);
		String msg = videoURL;
		DatagramPacket sndPkt = new DatagramPacket(msg.getBytes(), msg.length(), toServerSocket.getInetAddress(), toServerSocket.getPort());
		
		try
		{
		toServerSocket.send(sndPkt);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		toServerSocket.close();
	}
}

	