import java.net.*;

public class HisCinemaDNS implements Runnable
{
	private DatagramSocket hisCinemaDomainUDP;
	
	public HisCinemaDNS(InetAddress addr, int port) throws Exception
	{
		hisCinemaDomainUDP= new DatagramSocket(port,addr);
	}
	
	public DatagramSocket getUDPSocket()
	{
		return hisCinemaDomainUDP;
	}
	@Override
	public void run() 
	{
		try
		{
					byte[] receiveData = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
					
					this.hisCinemaDomainUDP.receive(rcvPkt);
					
					try
					{	
						byte[] data = rcvPkt.getData();
						System.out.println(new String(data) +"\n");	
					}
					catch(Exception e)
					{
						System.out.println(e);
					}		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}