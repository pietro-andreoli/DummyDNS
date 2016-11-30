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

import javax.swing.JFrame;
public class Main{
	//40430
	public static void main(String[] args) throws Exception{
		//TCP port for connection with dummy server www.hiscinema.com
		//TCP port for connection with dummy server www.hercdn.com
		InetAddress[] ip_list = new InetAddress[4];
		Scanner ip_get = new Scanner(System.in);
		int ip_num=1;
		while(ip_num < 5){
			System.out.println("Enter IP "+ip_num);
			ip_list[ip_num-1] = InetAddress.getByName(ip_get.nextLine());
			ip_num++;
		}
		//HerCDNWebServer her_server = new HerCDNWebServer(ip_list[0],90 );
		HerCDNWebServer her_server = new HerCDNWebServer(InetAddress.getByName("localhost"),40430 );
		her_server.run();
		//HisCinemaWebServer his_server = new HisCinemaWebServer(ip_list[0],90 );
		HisCinemaWebServer his_server = new HisCinemaWebServer(InetAddress.getByName("localhost"),90 );
		his_server.run();
		//ServerSocket his_cinema1 = new ServerSocket(40430, 100, ip_list[0]);
		//ServerSocket his_cinema2 = new ServerSocket(40431, 100, ip_list[0]);
		//ServerSocket his_cinema1 = new ServerSocket(90);
		//ServerSocket his_cinema2 = new ServerSocket(90);
		
		ip_get.close();
	}


}
