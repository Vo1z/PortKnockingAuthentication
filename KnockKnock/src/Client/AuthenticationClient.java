package Client;

import Utils.Constants;
import Utils.KnockUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class AuthenticationClient
{
    public final String authenticationServerAddress;
    public final int[] authenticationServerPorts;

    private DatagramSocket clientKnocker;

    private final String messageToServer;
    private String messageFromServer;

    public AuthenticationClient(String authenticationServerAddress, String messageToServer, int... authenticationServerPorts)
    {
        this.authenticationServerAddress = authenticationServerAddress;
        this.authenticationServerPorts = authenticationServerPorts;
        this.messageToServer = messageToServer;
        try
        {
            this.clientKnocker = new DatagramSocket(0);
            this.clientKnocker.setSoTimeout(Constants.CLIENT_SOCKET_TIMEOUT);
        }
        catch (SocketException e) { e.printStackTrace(); }
    }

    public AuthenticationClient(String authenticationServerAddress, int... authenticationServerPorts)
    {
        this.authenticationServerAddress = authenticationServerAddress;
        this.authenticationServerPorts = authenticationServerPorts;
        this.messageToServer = "Hello server!";
        try
        {
            this.clientKnocker = new DatagramSocket(0);
            this.clientKnocker.setSoTimeout(Constants.CLIENT_SOCKET_TIMEOUT);
        }
        catch (SocketException e) { e.printStackTrace(); }
    }

    public void startClient()
    {
        startKnocking();
        try
        {
            listenToServer();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public void startKnocking()
    {
        for (int authenticationServerPort : this.authenticationServerPorts)
            KnockUtils.sendDatagramMessageFromBoundedSocket(this.messageToServer, this.authenticationServerAddress, authenticationServerPort, this.clientKnocker);
    }

    public void listenToServer() throws IOException
    {
        byte[] buffer = new byte[Constants.MAX_DATAGRAM_SIZE];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

        if (Constants.IS_CLIENT_HAS_INFINITE_LIFETIME)
        {
            while (true)
            {
                this.clientKnocker.receive(datagramPacket);

                if (datagramPacket.getSocketAddress().toString().replaceAll(Constants.PORT_REGEX, "").equals(this.authenticationServerAddress))
                {
                    String messageFromServer = new String(datagramPacket.getData(), StandardCharsets.UTF_8);
                    int port = Integer.parseInt(messageFromServer.replaceAll(Constants.ADDRESS_REGEX, "").trim());

                    connectToServerSocket(this.authenticationServerAddress, port);

                    break;
                }
            }
        }
        else
        {
            try
            {
                this.clientKnocker.receive(datagramPacket);

                if (datagramPacket.getSocketAddress().toString().replaceAll(Constants.PORT_REGEX, "").equals(this.authenticationServerAddress))
                {
                    String messageFromServer = new String(datagramPacket.getData(), StandardCharsets.UTF_8);
                    int port = Integer.parseInt(messageFromServer.replaceAll(Constants.ADDRESS_REGEX, "").trim());

                    connectToServerSocket(this.authenticationServerAddress, port);

                    return;
                }
            }
            catch (SocketTimeoutException ex)
            {
                System.err.println("ERROR: There was no response from the server before timeout");
                System.err.println("Terminating...");
                System.exit(-1);
            }
        }
    }

    public void connectToServerSocket(String address, int port) throws IOException
    {
        Socket socket = new Socket(address, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder received = new StringBuilder();

        out.println(this.messageToServer);
        out.flush();

        received.append(in.readLine());
        this.messageFromServer = received.toString();

        socket.close();
    }

    public String getMessageFromServer()
    {
        return this.messageFromServer;
    }

    public String getMessageToServer()
    {
        return  this.messageToServer;
    }
}