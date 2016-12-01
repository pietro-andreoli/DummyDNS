import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HisCinemaWebServer implements Runnable {
	ServerSocket my_sock ;
	public HisCinemaWebServer(InetAddress addr, int port) throws Exception{
		my_sock= new ServerSocket(port, 100, addr);
	}
	@Override
	public void run() {
		try{
			try{
				while(true){
					Socket client = this.my_sock.accept();
					try{
						//PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
						//pw.println(new Date().toString());
						BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String line = reader.readLine();
						String recieved_message = "";
						while(line != null){
							//System.out.println(line);
							recieved_message+=line;
							line = reader.readLine();
						}
						System.out.println(recieved_message);
						if("index.txt".equals(recieved_message.split(":")[1])){
							System.out.println("ayylmao");
						}
						reader.close();
					}finally{
						client.close();
					}
				}
			}finally{
				my_sock.close();
			}
		}catch(Exception e){
			
		}
		
	}
}
