package Client;

import Server.AuthenticationServer;
import Utils.Constants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AuthenticationClientTest
{
    private final int numberOfOpenedSockets = 10;
    private final AuthenticationServer server = new AuthenticationServer(this.numberOfOpenedSockets);
    private int[] serverPorts;

    @Before
    public void before()
    {
        Constants.IS_RUNTIME_IN_DEBUG_MODE = true;

        this.server.startServer();
        this.serverPorts = this.server.getAuthenticationPorts();
    }

    @After
    public void after()
    {
        this.server.stopServer();

        Constants.IS_RUNTIME_IN_DEBUG_MODE = false;
    }

    @Test
    public void startClient() throws UnknownHostException, InterruptedException
    {
        String messageToServer = "Hello Server!";
        AuthenticationClient client = new AuthenticationClient(InetAddress.getByName("localhost").getHostAddress(),
                messageToServer, this.serverPorts);
        client.startClient();

        Thread.sleep(500);

        this.server.getMessagesFromAuthorisedClients().keySet()
                .forEach(key -> Assert.assertEquals(this.server.getMessagesFromAuthorisedClients().get(key), messageToServer));
    }
}