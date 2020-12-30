package Utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class KnockUtilsTest
{
    @Test
    public void shuffleArray()
    {
        String[] array = new String[]{"a", "b", "c", "d"};
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
}