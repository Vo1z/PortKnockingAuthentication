package Server;

import Utils.Constants;
import Utils.KnockUtils;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AuthenticationServer
{
    public final int numberOfAuthenticationSockets;

    private AuthenticationSocket[] authenticationSockets;
    private volatile Set<String>[] authenticationAddresses;
    private boolean isWorking = false;

    public AuthenticationServer(int numberOfAuthenticationSockets)
    {
        this.numberOfAuthenticationSockets = numberOfAuthenticationSockets;
    }

    public void startServer()
    {
        //todo replace
        System.out.println("server started");
        if (!this.isWorking)
        {
            this.isWorking = true;
            initializeServer();

            Arrays.stream(this.authenticationSockets)
                    .forEach(Thread::start);
        }
    }

    public void stopServer()
    {
        //todo replace
        System.out.println("server stopped");
        if(this.isWorking)
        {
            this.isWorking = false;
            Arrays.stream(this.authenticationSockets)
                    .forEach(AuthenticationSocket::stopListening);

            this.authenticationAddresses = null;
            this.authenticationSockets = null;
        }
    }

    private void initializeServer()
    {
        try
        {
            this.authenticationSockets = new AuthenticationSocket[this.numberOfAuthenticationSockets];
            this.authenticationAddresses = new HashSet[this.numberOfAuthenticationSockets];
            for (int i = 0; i < this.authenticationSockets.length; i++)
            {
                this.authenticationSockets[i] = new AuthenticationSocket(this, i);
                this.authenticationAddresses[i] = new HashSet<>();
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    public void openSocketForSuchAddress(String remoteAddress, int remotePort)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(0);
            serverSocket.setSoTimeout(Constants.CONNECTION_TIMEOUT);
            //Todo remove
            System.out.println("Message: " + Constants.IPV4_ADDRESS + ":" + serverSocket.getLocalPort());
            KnockUtils.sendDatagramMessage(serverSocket.getInetAddress().getHostName() + ":" + serverSocket.getLocalPort(), remoteAddress, remotePort);

            Socket openedSocket = serverSocket.accept();

            //Checks if received socket address is corresponding to address that has passed authentication
            if (((InetSocketAddress)openedSocket.getRemoteSocketAddress()).getHostName().equals(remoteAddress))
                (new ServerProcessing(serverSocket.accept())).start();
            else
                openedSocket.close();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public synchronized boolean checkAuthentication(String addressOfRequester, int authenticationSocketNumber)
    {
        if (this.authenticationAddresses[authenticationSocketNumber].contains(addressOfRequester))
            return false;

        for(int i = 0; i < authenticationSocketNumber; i++)
            if (!this.authenticationAddresses[i].contains(addressOfRequester))
                return false;

        return true;
    }

    public synchronized void addAddressToAuthenticationList(String address, int socketAuthenticationNumber)
    {
        this.authenticationAddresses[socketAuthenticationNumber].add(address);
    }

    public synchronized void removeAddressFromAuthenticationList(String address, int socketAuthenticationNumber)
    {
        for(int i = 0; i <= socketAuthenticationNumber; i++)
            this.authenticationAddresses[i].remove(address);
    }

    //Getters
    public boolean isWorking()
    {
        return this.isWorking;
    }

    public int[] getAuthenticationPorts()
    {
        if(!this.isWorking)
            throw new RuntimeException("Server was not started yet");
        if(this.authenticationSockets == null)
            return null;


        return Arrays.stream(this.authenticationSockets)
                .filter(Objects::nonNull)
                .mapToInt(AuthenticationSocket::getPort)
                .toArray();
    }

    public Set<String>[] getAuthenticationAddresses()
    {
        return this.authenticationAddresses;
    }
}