package Utils;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KnockUtils
{
    public static<T> T[] shuffleArray(T[] arrayToMix)
    {
        List<T> shuffledList = Arrays.asList(arrayToMix);
        Collections.shuffle(shuffledList);

        return (T[])shuffledList.toArray();
    }

    public static void sendDatagramMessage(String message, String destinationAddress, int destinationPort)
    {
        try
        {
            byte[] buffer = message.getBytes();
            DatagramSocket datagramSocket = new DatagramSocket(0);
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(destinationAddress), destinationPort);

            datagramSocket.send(datagramPacket);

            Thread.sleep(Constants.MESSAGE_DELAY_MILLISECONDS);
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}