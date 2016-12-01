import java.net.InetAddress;
import java.net.ServerSocket;

public class ClientLocalDNS implements Runnable{
	private ServerSocket my_sock;
	public ClientLocalDNS(InetAddress addr, int port) throws Exception{
		my_sock= new ServerSocket(port, 100, addr);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
