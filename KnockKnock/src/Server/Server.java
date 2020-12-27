package Server;

import Utils.KnockUtils;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class Server
{
    private DatagramSocket[] openedPorts;

    public Server(int portNumber)
    {
        this.openedPorts = new DatagramSocket[portNumber];
    }

    public int[] generateNewOrderOfPorts()
    {
        openedPorts = KnockUtils.shuffleArray(openedPorts);
        return getOpenedPorts();
    }

    private void openPorts() throws SocketException
    {
        for(int i = 0; i < openedPorts.length; i++)
        {
            openedPorts[i] = new DatagramSocket();
        }
    }

    //Getters
    public int[] getOpenedPorts()
    {
        return Arrays.stream(openedPorts)
                .mapToInt(DatagramSocket::getPort)
                .toArray();
    }
}