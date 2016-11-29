import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class ServerTest {
	public static void main (String[] args) throws Exception{
		int client_num = 0;
		Socket listener = new Socket(InetAddress.getByName("localhost"), 90);
		BufferedReader reader = new BufferedReader(new InputStreamReader(listener.getInputStream()));
		String line = reader.readLine();
		while(line != null){
				System.out.println(line);
				line = reader.readLine();
		}
		
	}
}
