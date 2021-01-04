package Server;

import Utils.Constants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AuthenticationSocketTest
{
    private final int numberOfSockets = 8;
    private AuthenticationServer server = new AuthenticationServer(8);
    private int[] ports;

    @Before
    public void before()
    {
        Constants.IS_RUNTIME_IN_DEBUG_MODE = true;

        this.server.startServer();
        this.ports = this.server.getAuthenticationPorts();
    }

    @After
    public void after()
    {
        this.server.stopServer();

        Constants.IS_RUNTIME_IN_DEBUG_MODE = false;
    }

    @Test
    public void run() throws IOException, InterruptedException
    {
        int numberOfRequests = 4;
        String message = "knock";
        byte[] buffer = message.getBytes();
        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket datagramPacket;

        for (int i = 0; i < numberOfRequests; i++)
        {
            datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), ports[i]);
            datagramSocket.send(datagramPacket);
            Thread.sleep(30);
        }

        for (int i = 0; i < numberOfRequests; i++)
            Assert.assertTrue(this.server.getAuthenticationAddresses()[i].contains(InetAddress.getByName("localhost").getHostAddress()));

        for (int i = 0; i < numberOfRequests; i++)
        {
            datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), ports[i]);
            datagramSocket.send(datagramPacket);
            Thread.sleep(30);
        }
        datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), ports[numberOfRequests + 2]);
        datagramSocket.send(datagramPacket);

        for (int i = 0; i < numberOfRequests + 2; i++)
            Assert.assertFalse(this.server.getAuthenticationAddresses()[i].contains(InetAddress.getByName("localhost").getHostAddress()));

        for (int i = 0; i < this.numberOfSockets; i++)
        {
            datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), ports[i]);
            datagramSocket.send(datagramPacket);
            Thread.sleep(30);
        }

        for (int i = 0; i < this.numberOfSockets; i++)
            Assert.assertFalse(this.server.getAuthenticationAddresses()[i].contains(InetAddress.getByName("localhost").getHostAddress()));
    }
}