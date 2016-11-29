import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HerCDNWebServer implements Runnable{
	ServerSocket my_sock ;
	public HerCDNWebServer(InetAddress addr, int port) throws Exception{
		my_sock= new ServerSocket(90, 100, addr);
	}
	@Override
	public void run() {
		try{
			try{
				while(true){
					Socket client = this.my_sock.accept();
					
					try{
						PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
						pw.println(new Date().toString());
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
