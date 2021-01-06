package Server;

import Utils.KnockUtils;

import java.util.Arrays;

public class ServerApp
{
    private static AuthenticationServer authenticationServer;

    private static int[] customUserPorts = null;
    private static int numberAuthenticationPorts = -1;
    private static String messageToClients = null;

    public static void main(String[] args) throws InterruptedException
    {
        checkArgumentCorrectness(args);

        //customPorts + message
        if (customUserPorts != null && messageToClients != null && numberAuthenticationPorts == -1)
            authenticationServer = new AuthenticationServer(messageToClients, customUserPorts);
        //customPorts
        else if (customUserPorts != null && messageToClients == null && numberAuthenticationPorts == -1 )
            authenticationServer = new AuthenticationServer(customUserPorts);
        //numberOfPorts + message
        else if (customUserPorts == null && messageToClients != null && numberAuthenticationPorts > 0)
            authenticationServer = new AuthenticationServer(messageToClients, numberAuthenticationPorts);
            //numberOfPorts
        else if (customUserPorts == null && messageToClients == null && numberAuthenticationPorts > 0)
            authenticationServer = new AuthenticationServer(numberAuthenticationPorts);
        else
        {
            System.err.println("Unknown format for arguments");
            System.exit(-1);
        }

        synchronized (authenticationServer)
        {
            authenticationServer.startServer();
            System.out.println("-Server was started");
            System.out.println("-Such message will be send to all clients that passed authentication: " + "\"" + authenticationServer.getMessageToClients() + "\"");
            System.out.println("-Server will be waiting client to knock in such order of authentication ports:");
            for (int i = 0; i < authenticationServer.getAuthenticationPorts().length; i++)
                System.out.println(i + " -> " + authenticationServer.getAuthenticationPorts()[i]);
            System.out.println();

            authenticationServer.wait();
        }
    }

    private static void checkArgumentCorrectness(String[] args)
    {
        if (Arrays.stream(args).filter(s -> s.equals("-m")).count() > 1)
        {
            System.err.println("Error: There can not be more than 1 message");
            System.exit(-1);
        }
        if (Arrays.stream(args).filter(s -> s.equals("-n")).count() > 1)
        {
            System.err.println("Error: There can not be more than 1 arguments that defines number of ports to be open");
            System.exit(-1);
        }

        int indexOfMessage = KnockUtils.indexOf(args, "-m");
        int indexOfPortsNumber = KnockUtils.indexOf(args, "-n");
        int indexOfCustomPorts = KnockUtils.indexOf(args, "-p");

        if(indexOfPortsNumber != -1 && indexOfCustomPorts != -1)
        {
            System.err.println("You can not ask server to open random number of ports (-n) and set your custom ports (-p) at the same time");
            System.exit(-1);
        }
        else
        {
            if(indexOfMessage != -1)
                messageToClients = args[indexOfMessage + 1];
            if(indexOfPortsNumber != -1)
                numberAuthenticationPorts = Integer.parseInt(args[indexOfPortsNumber + 1]);
            if(indexOfCustomPorts != -1)
            {
                if (indexOfMessage > indexOfCustomPorts)
                {
                    customUserPorts = new int[indexOfMessage - indexOfCustomPorts - 1];

                    int customPortIter = 0;
                    for (int i = indexOfCustomPorts + 1; i < indexOfMessage; i++)
                    {
                        customUserPorts[customPortIter] = Integer.parseInt(args[i]);
                        customPortIter++;
                    }
                }
                else
                {
                    customUserPorts = new int[args.length - indexOfCustomPorts - 1];

                    int customPortIter = 0;
                    for (int i = indexOfCustomPorts + 1; i < args.length; i++)
                    {
                        customUserPorts[customPortIter] = Integer.parseInt(args[i]);
                        customPortIter++;
                    }
                }
            }
        }

    }
}
