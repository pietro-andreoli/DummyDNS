import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class ClientTest {
	public static void main (String[] args) throws Exception{
		Socket listener = new Socket(InetAddress.getByName("localhost"), 90);
		int x = 1;
		while(x == 1){
			
			try{
				int client_num = 0;
				
			
				PrintWriter pw = new PrintWriter(listener.getOutputStream());
				pw.println("GET / HTTP/1.1");
				pw.println("Host: localhost");
				pw.flush();
				pw.close();
				BufferedReader reader = new BufferedReader(new InputStreamReader(listener.getInputStream()));
				String line = reader.readLine();
				while(line != null){
						System.out.println(line);
						line = reader.readLine();
				}
				reader.close();
			}finally{
				//x=0;
				
			}
		}
		System.out.println("doot");
		listener.close();
	}

}
