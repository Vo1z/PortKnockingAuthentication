package Server;

import Utils.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class AuthenticationSocket extends Thread
{
    final int authenticationNumber;
    final AuthenticationServer server;
    private boolean isWorking = false;

    AuthenticationSocket(int authenticationNumber, AuthenticationServer server)
    {
        this.authenticationNumber = authenticationNumber;
        this.server = server;
    }

    public void stopListening()
    {
        this.isWorking = false;
    }

    @Override
    public void run()
    {
        this.isWorking = true;

        try
        {
            byte[] buffer = new byte[UDP.MAX_DATAGRAM_SIZE];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            DatagramSocket udpSocket = new DatagramSocket();

            while (this.isWorking)
            {
                udpSocket.receive(datagramPacket);

                if(this.server.checkAuthentication(datagramPacket.getAddress().getHostAddress(), authenticationNumber))
                {
                    if (this.server.numberOfAuthenticationSockets - 1 == this.authenticationNumber)
                    {
                        //TODO implement and add openPortForSuchAddress() in AuthenticationServer class
                    }
                    else
                    {
                        //TODO implement and addAddress() in AuthenticationServer class
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}