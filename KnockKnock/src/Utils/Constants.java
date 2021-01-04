package Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants
{
    private static final int MIN_MTU = 576;
    private static final int MAX_IP_HEADER_SIZE = 60;
    private static final int UDP_HEADER_SIZE = 8;
    public static final int MAX_DATAGRAM_SIZE = MIN_MTU - MAX_IP_HEADER_SIZE - UDP_HEADER_SIZE;
    public static final int SERVER_SOCKET_CONNECTION_TIMEOUT = 0;
    public static final int AUTHENTICATION_SOCKET_TIMEOUT = 0;
    public static final int MESSAGE_DELAY_MILLISECONDS = 30;
    public static final String PORT_REGEX = ":\\d+|/";
    public static final String ADDRESS_REGEX = "(\\d+(\\.|:)+)|/";
    public static final String HOST_ADDRESS_REGEX = "(\\d+\\.)+(\\d+)";
    public static boolean IS_RUNTIME_IN_DEBUG_MODE = false;

    public static String IPV4_ADDRESS = "25.102.204.33"; //On which address socket will be open for
    //todo replace
//    static
//    {
//        try
//        {
//            IPV4_ADDRESS = InetAddress.getByName("localhost").getHostAddress();
//        }
//        catch (UnknownHostException e)
//        {
//            e.printStackTrace();
//        }
//    }
}