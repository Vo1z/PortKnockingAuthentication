import Server.AuthenticationServer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AuthenticationServerTest
{
    @Test
    public void startServer() throws InterruptedException
    {
        int numberOfOpenedSockets = 10;
        AuthenticationServer server = new AuthenticationServer(numberOfOpenedSockets);
        server.startServer();

        int[] ports = Arrays.stream(server.getAuthenticationPorts())
                .distinct()
                .toArray();

        Assert.assertEquals(server.getAuthenticationPorts().length, numberOfOpenedSockets);
        Assert.assertArrayEquals(ports, server.getAuthenticationPorts());

        server.stopServer();
    }

    @Test
    public void stopServer()
    {
        int numberOfOpenedSockets = 10;
        AuthenticationServer server = new AuthenticationServer(numberOfOpenedSockets);
        server.startServer();
        server.stopServer();

        Assert.assertNull(server.getAuthenticationPorts());
        Assert.assertTrue(!server.isWorking());
    }
}
