package Client;

import Utils.KnockUtils;

public class ClientApp
{
    private static int[] ports = null;
    private static String serverAddress = null;
    private static String messageToServer = null;

    private static AuthenticationClient authenticationClient;

    public static void main(String[] args)
    {
        if(args.length == 1 && args[0].equals("--help"))
        {
            System.out.println("-s <[server address]>" + "\n" +
                    "-p <[ports to knock...]>" + "\n" +
                    "-m <[message to server(optional)]>");
            System.exit(0);
        }
        checkArgumentCorrectness(args);

        if(messageToServer != null)
            authenticationClient = new AuthenticationClient(serverAddress, messageToServer, ports);
        else
            authenticationClient = new AuthenticationClient(serverAddress, ports);

        System.out.println("-Client was started");
        System.out.println("-Such message will be send to server: " + "\"" + authenticationClient.getMessageToServer() + "\"");
        System.out.println("-Client will be knocking in such internet socket addresses:");
        for(int i = 0; i < ports.length; i++)
            System.out.println(i + " -> " + serverAddress + ":" + ports[i]);
        System.out.println();

        authenticationClient.startClient();

        System.out.println("Server responded with " + "\"" + authenticationClient.getMessageFromServer() + "\"");
    }

    private static void checkArgumentCorrectness(String[] args)
    {
        int indexOfServerAddress = KnockUtils.indexOf(args, "-s");
        int indexOfPorts = KnockUtils.indexOf(args, "-p");
        int indexOfMessage = KnockUtils.indexOf(args, "-m");
        int firstBiggerIndexThanPort;
        if(indexOfServerAddress > indexOfPorts && indexOfMessage > indexOfPorts)
            firstBiggerIndexThanPort = Math.min(indexOfMessage, indexOfServerAddress);
        else
            firstBiggerIndexThanPort = Math.max(indexOfMessage, indexOfServerAddress);

        if (indexOfMessage != -1)
            messageToServer = args[indexOfMessage + 1];
        if (indexOfServerAddress != -1)
            serverAddress = args[indexOfServerAddress + 1];
        else
        {
            System.err.println("Server address is not specified (-s)");
            System.err.println("Try to execute --help");
            System.exit(-1);
        }

        if (indexOfPorts != -1)
        {
            if (firstBiggerIndexThanPort > indexOfPorts)
            {
                ports = new int[firstBiggerIndexThanPort - indexOfPorts - 1];

                int portIter = 0;
                for (int i = indexOfPorts + 1; i < firstBiggerIndexThanPort; i++)
                {
                    ports[portIter] = Integer.parseInt(args[i]);
                    portIter++;
                }
            }
            else
            {
                ports = new int[args.length - indexOfPorts - 1];

                int portIter = 0;
                for (int i = indexOfPorts + 1; i < args.length; i++)
                {
                    ports[portIter] = Integer.parseInt(args[i]);
                    portIter++;
                }
            }
        }
        else
        {
            System.err.println("Ports are not specified (-p)");
            System.err.println("Try to execute --help");
            System.exit(-1);
        }
    }
}
