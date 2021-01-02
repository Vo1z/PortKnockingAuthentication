package Client;

import Server.AuthenticationServer;
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
        this.server.startServer();
        this.serverPorts = this.server.getAuthenticationPorts();
    }

    @After
    public void after()
    {
        this.server.stopServer();
    }

    @Test
    public void startClient() throws UnknownHostException, InterruptedException
    {
        AuthenticationClient client = new AuthenticationClient(InetAddress.getByName("localhost").getHostAddress(), this.serverPorts);
        client.startClient();

        Thread.sleep(5000);
        Assert.fail();
    }
}
