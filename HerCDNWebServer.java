import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HerCDNWebServer {
	InetAddress THIS_PC_IP;
	int THIS_PC_PORT = 40438;
	InetAddress CLIENT_IP;
	int CLIENT_PORT;
	static ServerSocket herCDNTCP;
	public HerCDNWebServer() throws IOException{
		THIS_PC_IP = InetAddress.getByName("127.0.0.1");
		herCDNTCP = new ServerSocket(THIS_PC_PORT, 100, THIS_PC_IP);
	}
	public static void main(String[] args) throws IOException{
		HerCDNWebServer thisPCServer = new HerCDNWebServer();
		waitForQuery();
	}
	public static void waitForQuery() throws IOException{
		while(true){
			Socket receivingSocket = herCDNTCP.accept();
			try
			{	
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(receivingSocket.getInputStream()));
				
				DataOutputStream outToClient = new DataOutputStream(receivingSocket.getOutputStream());
				
			
				
				String clientMessage = "";
				String fullMessage = "";
				try
				{
					while(clientMessage != null)
					{
						
						if(inFromClient.ready())
						{
							clientMessage = inFromClient.readLine();
							fullMessage += clientMessage;
							System.out.println(clientMessage);
							if(clientMessage.equals("")) clientMessage = null;
							//break;
						}
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				
				outToClient.writeBytes("Request received, sending index.txt to IP: " + receivingSocket.getInetAddress() + " on Port: " + receivingSocket.getPort()+"\n");
				String[] analyzedMessage = analyzeMessage(fullMessage);
				if(analyzedMessage[0].contains("V"))
				{
					File videoFile = new File(System.getProperty("user.dir")+"\\src\\"+analyzedMessage[1]);
					byte[] fileByteArray = new byte[(int)videoFile.length()];
					FileInputStream fis = new FileInputStream(videoFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(fileByteArray, 0, fileByteArray.length);
					OutputStream os = receivingSocket.getOutputStream();
					os.write(fileByteArray, 0, fileByteArray.length);
					//System.out.println(fileByteArray.toString());
					os.flush();
					fis.close();
					bis.close();
					os.close();
				}
				inFromClient.close();
				outToClient.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			receivingSocket.close();
		}
	}
	public static String[] analyzeMessage(String data)
	{
        String[] outputData = null;
        String[] dataChunks = data.split("\n");
        //String[] dataChunks = dataS.split(",");
        String[][] dataParts = new String[dataChunks.length][];

        for(int i = 0; i < dataChunks.length; i++)
        {
            dataParts[i] = dataChunks[i].split(",");
        }
        
        String recordType = dataParts[0][2];
        String video = dataParts[0][0];
        if(recordType.contains("V")){
        	outputData[0] = recordType;
        	outputData[1] = video;
        }
        return outputData;
    }
}
