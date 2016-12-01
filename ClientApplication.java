import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

//This will function as the Main class for this project
public class ClientApplication
{
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
		
		ip_get.close();
		
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
		
		//Creating the network socket for herCDN.com Web Server
		HerCDNWebServer herCNDWeb = new HerCDNWebServer(InetAddress.getByName("localhost"), 40438);
		
		//Eventually all of the above should look something like these
		/*
		Socket clientToHisCinemaTCP = new Socket(ip_list[0] , 40430);
		Socket clientToHerContentTCP = new Socket(ip_list[0] , 40431);
		DatagramSocket clientToLocalDomainUDP = new DatagramSocket(40432 , ip_list[0]);
		*/
		connectToHisCinema();

	}
	
	public static void connectToHisCinema() throws UnknownHostException, IOException{
		
		Socket sendSocket = new Socket(InetAddress.getByName("localhost"), 40435);
		int x = 1;
			
			
		PrintWriter pw = new PrintWriter(sendSocket.getOutputStream());
		pw.print("GET /index.html HTTP/1.1\r\n");
		pw.print("Host: www.hiscinema.com\r\n");
		pw.print("");
		pw.flush();
		
		
			BufferedReader reader = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
			String line = reader.readLine();
			while(line != null){
					System.out.println(line);
					line = reader.readLine();
			}
			reader.close();
			pw.close();
		
			
	}
}