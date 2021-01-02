package Server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProcessingTest
{
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Before
    public void before() throws Exception
    {
        this.serverSocket = new ServerSocket(0);
        this.socket = new Socket(InetAddress.getByName("localhost"), this.serverSocket.getLocalPort());
        this.out = new PrintWriter(this.socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Test
    public void processClient() throws IOException, InterruptedException
    {
        String messageToClient = "Hello client", messageToServerProcessing = "Hello ServerProcessing";
        StringBuffer received = new StringBuffer();
        ServerProcessing serverProcessing = new ServerProcessing(this.serverSocket.accept(), messageToClient);
        serverProcessing.start();

        this.out.println(messageToServerProcessing);
        this.out.flush();

        received.append(this.in.readLine());

        this.socket.close();
        Thread.sleep(1000);

        Assert.assertEquals(messageToServerProcessing, serverProcessing.getMessageFromClient());
        Assert.assertEquals(messageToClient, received.toString());
    }
}
