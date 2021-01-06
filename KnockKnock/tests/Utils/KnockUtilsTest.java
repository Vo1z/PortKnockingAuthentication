package Utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class KnockUtilsTest
{
    @Test
    public void shuffleArray()
    {
        String[] array = new String[]{"a", "b", "c", "d", "e", "f", "g"};
        String[] arrayToShuffle = Arrays.copyOf(array, array.length);
        KnockUtils.shuffleArray(arrayToShuffle);

        Assert.assertFalse(Arrays.equals(array, arrayToShuffle));
    }

    @Test
    public void sendDatagramMessage() throws IOException
    {
        String message = "Test message";
        byte[] buffer = new byte[message.getBytes().length];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        DatagramSocket datagramSocket = new DatagramSocket();

        KnockUtils.sendDatagramMessage(message, "localhost", datagramSocket.getLocalPort());
        datagramSocket.receive(datagramPacket);

        Assert.assertArrayEquals(datagramPacket.getData(), message.getBytes());
    }

    @Test
    public void sendDatagramMessageFromBoundedSocket() throws IOException
    {
        String message = "Test message";
        byte[] buffer = new byte[message.getBytes().length];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        DatagramSocket receiver = new DatagramSocket();
        DatagramSocket sender = new DatagramSocket(0);

        KnockUtils.sendDatagramMessageFromBoundedSocket(message, "localhost", receiver.getLocalPort(), sender);

        receiver.receive(datagramPacket);

        Assert.assertArrayEquals(datagramPacket.getData(), message.getBytes());

        int expectedPort = Integer.parseInt(datagramPacket.getSocketAddress().toString().replaceAll(Constants.ADDRESS_REGEX, ""));
        Assert.assertEquals(sender.getLocalPort(), expectedPort);
    }

    @Test
    public void checkIfSuchNetworkInterfaceExists() throws UnknownHostException
    {
        String loopback = InetAddress.getLoopbackAddress().getHostAddress();
        String localHost = InetAddress.getLocalHost().getHostAddress();

        Assert.assertTrue(KnockUtils.checkIfSuchNetworkInterfaceExists(loopback));
        Assert.assertTrue(KnockUtils.checkIfSuchNetworkInterfaceExists(localHost));
    }

    @Test
    public void getAllAvailableInetInetInterfaces() throws UnknownHostException
    {
        String loopback = InetAddress.getLoopbackAddress().getHostAddress();
        String localHost = InetAddress.getLocalHost().getHostAddress();

        Assert.assertTrue(KnockUtils.getAllAvailableInetInetInterfaces().contains(loopback));
        Assert.assertTrue(KnockUtils.getAllAvailableInetInetInterfaces().contains(localHost));
    }

    @Test
    public void indexOf()
    {
        String[] array = new String[]{"00","x","sd","-n","a","d","8","5","3","2"};
        int indexOfElement = 3;
        String element = array[indexOfElement];

        Assert.assertEquals(indexOfElement, KnockUtils.indexOf(array, element));
        Assert.assertEquals(-1, KnockUtils.indexOf(array, "NotExistingElement"));
    }
}