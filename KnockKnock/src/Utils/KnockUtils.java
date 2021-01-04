package Utils;

import java.io.IOException;
import java.net.*;
import java.util.*;

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

    public static void sendDatagramMessageFromBoundedSocket(String message, String destinationAddress, int destinationPort, DatagramSocket socket)
    {
        try
        {
            byte[] buffer = message.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(destinationAddress), destinationPort);

            socket.send(datagramPacket);

            Thread.sleep(Constants.MESSAGE_DELAY_MILLISECONDS);
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean checkIfSuchNetworkInterfaceExists(String address)
    {
        if (address == null)
            return false;

        StringBuilder allAddresses = new StringBuilder();

        try
        {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while(networkInterfaces.hasMoreElements())
            {
                NetworkInterface ni = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();

                while (inetAddresses.hasMoreElements())
                {
                    InetAddress ia = inetAddresses.nextElement();

                    allAddresses.append(ia.toString().replace("/", "")).append("\n");
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        return allAddresses.toString().contains(address);
    }

    public static List<String> getAllAvailableInetInetInterfaces()
    {
        LinkedList<String> addresses = new LinkedList<>();

        try
        {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements())
            {
                NetworkInterface ni = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();

                while (inetAddresses.hasMoreElements())
                {
                    InetAddress ia = inetAddresses.nextElement();

                    String address = ia.toString().replace("/", "");
                    addresses.add(address);
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        return addresses;
    }
}