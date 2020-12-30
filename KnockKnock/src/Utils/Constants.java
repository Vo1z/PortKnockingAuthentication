package Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants
{
    private static final int MIN_MTU = 576;
    private static final int MAX_IP_HEADER_SIZE = 60;
    private static final int UDP_HEADER_SIZE = 8;
    public static final int MAX_DATAGRAM_SIZE = MIN_MTU - MAX_IP_HEADER_SIZE - UDP_HEADER_SIZE;
    public static final int CONNECTION_TIMEOUT = 3000;
    public static final int AUTHENTICATION_SOCKET_TIMEOUT = 3000;
    public static final int MESSAGE_DELAY_MILLISECONDS = 30;
    public static String IPV4_ADDRESS;

    static
    {
        try
        {
            IPV4_ADDRESS = InetAddress.getByName("localhost").getHostAddress();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }
}