import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

//This will function as the Main class for this project
public class ClientApplication
{
	static String indexContent = "";
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
		
		//Creating the network sockets for the Client Application
		ServerSocket clientTCPSocket = new ServerSocket(40430, 10, InetAddress.getByName("localhost"));
		DatagramSocket clientToLocalDomainUDP = new DatagramSocket(40431, InetAddress.getByName("localhost"));
		
		//Creating the network sockets for the Local DNS
		ClientLocalDNS clientDNS = new ClientLocalDNS(InetAddress.getByName("localhost"), 40432, 40433, 40434);
		
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
		connectToHisCinema();
		
		Scanner getVideo = new Scanner(System.in);
		
		System.out.println("Please enter a number for the video you wish to request: Please choose a number between 1 and 5 inclusively");
		
		int clientRequest = getVideo.nextInt();
		getVideo.close();

		switch(clientRequest)
		{
		case 1: System.out.println("Client has chosen video 1"); break;
		case 2: System.out.println("Client has chosen video 2"); break;
		case 3: System.out.println("Client has chosen video 3"); break;
		case 4: System.out.println("Client has chosen video 4"); break;
		case 5: System.out.println("Client has chosen video 5"); break;
			default: System.out.println("That is not a valid selection"); break;
		}
		
		
		

	}
	
	public static void connectToHisCinema() throws UnknownHostException, IOException
	{
		Socket sendSocket = new Socket(InetAddress.getByName("localhost"), 40437);
			
		DataOutputStream toServer = new DataOutputStream(sendSocket.getOutputStream());	
		
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
		
		toServer.writeBytes("GET /index.html HTTP/1.1\r\nHost: www.hiscinema.com\r\nto IP: " + 
				sendSocket.getInetAddress() + " on Port: " + sendSocket.getPort());
		
		String serverReply = "";
		
		try
		{
			while(serverReply!=null)
			{
				System.out.println(serverReply);
				serverReply = inFromServer.readLine();
				indexContent+=serverReply;
			}
		}
		catch(Exception e)
		{
			System.out.println("bad doot");
		}
		sendSocket.close();	
	}
}