package Client;

import Utils.Constants;
import Utils.KnockUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class AuthenticationClient
{
    public final String authenticationServerAddress;
    public final int[] authenticationServerPorts;

    private String messageToServer = "Hello server!", messageFromServer = "";

    public AuthenticationClient(String authenticationServerAddress, int... authenticationServerPorts)
    {
        this.authenticationServerAddress = authenticationServerAddress;
        this.authenticationServerPorts = authenticationServerPorts;
    }

    public AuthenticationClient(String authenticationServerAddress, String messageToServer, int... authenticationServerPorts)
    {
        this.authenticationServerAddress = authenticationServerAddress;
        this.authenticationServerPorts = authenticationServerPorts;
        this.messageToServer = messageToServer;
    }

    public void startClient()
    {
        try
        {
            startKnocking();
            listenToServer();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    private void startKnocking()
    {
        for (int authenticationServerPort : this.authenticationServerPorts)
            KnockUtils.sendDatagramMessage(this.messageToServer, this.authenticationServerAddress, authenticationServerPort);
    }

    private void listenToServer() throws IOException
    {
        byte[] buffer = new byte[Constants.MAX_DATAGRAM_SIZE];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        DatagramSocket datagramSocket = new DatagramSocket(0);

        while (true)
        {
            datagramSocket.receive(datagramPacket);

            if(datagramPacket.getSocketAddress().toString().replaceAll(":\\d+|/", "").equals(this.authenticationServerAddress))
            {
                String socketInetAddress = new String(datagramPacket.getData(), StandardCharsets.UTF_8);
                String address = socketInetAddress.replaceAll(":\\d+|/", "").trim();
                int port = Integer.parseInt(socketInetAddress.replaceAll(".+&!(:\\d+)", "").trim());

                connectToServerSocket(address, port);

                break;
            }
        }
    }

    private void connectToServerSocket(String address, int port) throws IOException
    {
        Socket socket = new Socket(address, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;

        out.println(this.messageToServer);
        out.flush();

        while ((line = in.readLine()) != null)
        {
            this.messageFromServer += line;
        }

        socket.close();
    }

    public String getMessageFromServer()
    {
        return this.messageFromServer;
    }
}