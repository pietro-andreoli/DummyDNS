import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;


public class Main
{
	public static void main(String[] args) throws Exception
	{
		
		
		//Declaring an array to hold the 4 IP Addresses and a Scanner to read IPs from stdin
		InetAddress[] ip_list = new InetAddress[4];
		Scanner ip_get = new Scanner(System.in);
		int ip_num=1;
		
		//Take input from keyboard to initialize 4 IP Addresses
		while(ip_num < 5)
		{
			System.out.println("Enter IP "+ip_num);
			ip_list[ip_num-1] = InetAddress.getByName(ip_get.nextLine());
			ip_num++;
		}
		
		//Close the scanner once all IP's have been initialized
		ip_get.close();
		
		//Create the dummy web server for herCDN.com
		HerCDNWebServer her_server = new HerCDNWebServer(ip_list[0],90 );
		her_server.run();
		
		
		
		//ServerSocket his_cinema1 = new ServerSocket(40430, 100, ip_list[0]);
		//ServerSocket his_cinema2 = new ServerSocket(40431, 100, ip_list[0]);
		//ServerSocket his_cinema1 = new ServerSocket(90);
		//ServerSocket his_cinema2 = new ServerSocket(90);
		
		
	}


}