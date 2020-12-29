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
            DatagramSocket datagramSocket = new DatagramSocket();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(destinationAddress), destinationPort);

            datagramSocket.send(datagramPacket);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}