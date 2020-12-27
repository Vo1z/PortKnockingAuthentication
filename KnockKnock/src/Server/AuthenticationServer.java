package Server;

import Utils.KnockUtils;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AuthenticationServer
{
    public final int numberOfAuthenticationSockets;
    private DatagramSocket[] openedPorts;
    private volatile HashSet<String>[] incomeAddresses;

    public AuthenticationServer(int numberOfAuthenticationSockets)
    {
        this.numberOfAuthenticationSockets = numberOfAuthenticationSockets;
        this.openedPorts = new DatagramSocket[numberOfAuthenticationSockets];
        this.incomeAddresses = new HashSet[numberOfAuthenticationSockets];
    }

    public int[] generateNewOrderOfPorts()
    {
        this.openedPorts = KnockUtils.shuffleArray(openedPorts);
        return getOpenedPorts();
    }

    private void openPorts() throws SocketException
    {
        for(int i = 0; i < this.openedPorts.length; i++)
            this.openedPorts[i] = new DatagramSocket();
    }

    public synchronized void addAddress(String addressOfRequester, int authenticationSocketId)
    {
        this.incomeAddresses[authenticationSocketId].add(addressOfRequester);
    }

    public boolean checkAuthentication(String addressOfRequester, int authenticationSocketId)
    {
        for(int i = 0; i < authenticationSocketId; i++)
            if (!incomeAddresses[i].contains(addressOfRequester))
                return false;

        return true;
    }

    //Getters
    public int[] getOpenedPorts()
    {
        return Arrays.stream(this.openedPorts)
                .mapToInt(DatagramSocket::getPort)
                .toArray();
    }
}