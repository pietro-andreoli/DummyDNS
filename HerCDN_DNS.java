import java.net.*;

public class HerCDN_DNS implements Runnable
{
	private DatagramSocket herContentDomainUDP;
	
	public HerCDN_DNS(InetAddress addr, int port) throws Exception
	{
		herContentDomainUDP= new DatagramSocket(port,addr);
	}
	
	public DatagramSocket getUDPSocket()
	{
		return herContentDomainUDP;
	}
	
	@Override
	public void run() 
	{
		try
		{
					byte[] receiveData = new byte[1024];
					DatagramPacket rcvPkt = new DatagramPacket(receiveData, receiveData.length);
					this.herContentDomainUDP.receive(rcvPkt);
					try
					{	
						byte[] data = rcvPkt.getData();
						System.out.println(new String(data));	
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