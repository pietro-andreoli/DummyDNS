
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFrame;


//The Main class will function as the Client Application node
public class ClientApplication
{
	//40430
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
		
		Socket clientToHisCinemaTCP = new Socket(InetAddress.getByName("localhost"), 40430);
		Socket clientToHerContentTCP = new Socket(InetAddress.getByName("localhost"), 40431);
		DatagramSocket clientToLocalDomainUDP = new DatagramSocket(40432, InetAddress.getByName("localhost"));
		
		
		/*
		Socket clientToHisCinemaTCP = new Socket(ip_list[0] , 40430);
		Socket clientToHerContentTCP = new Socket(ip_list[0] , 40431);
		DatagramSocket clientToLocalDomainUDP = new DatagramSocket(40432 , ip_list[0]);
		*/
		
		
		//Not sure what's gonna happen to these lines
		HerCDNWebServer her_server = new HerCDNWebServer(InetAddress.getByName("localhost"),40430 );
		
		HisCinemaWebServer his_server = new HisCinemaWebServer(InetAddress.getByName("localhost"),40430 );
		his_server.run();
		
		
		
		
	}


}