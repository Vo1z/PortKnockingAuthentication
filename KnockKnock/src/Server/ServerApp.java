package Server;

public class ServerApp
{
    private static AuthenticationServer authenticationServer;

    private static String address = null;
    private static int numberAuthenticationPorts = -1;
    private static String messageToClients = null;

    public static void main(String[] args) throws InterruptedException
    {
        checkArgumentCorrectness(args);

        if (messageToClients == null)
            authenticationServer = new AuthenticationServer(numberAuthenticationPorts);
        else
            authenticationServer = new AuthenticationServer(numberAuthenticationPorts, messageToClients);

        synchronized (authenticationServer)
        {
            authenticationServer.startServer();
            System.out.println("Server was started");
            System.out.println("Such message will be send to all clients that passed authentication: " + "\"" + authenticationServer.getMessageToClients() + "\"");
            System.out.println("Server will be waiting client to knock in such order of authentication ports:");
            for (int i = 0; i < numberAuthenticationPorts; i++)
                System.out.print(authenticationServer.getAuthenticationPorts()[i] + " ");
            System.out.println();
            for (int i = 0; i < numberAuthenticationPorts; i++)
                System.out.println(i + " -> " + authenticationServer.getAuthenticationPorts()[i]);
            System.out.println();

            authenticationServer.wait();
        }
    }

    private static void checkArgumentCorrectness(String[] args)
    {
        if ( args == null || args.length == 0)
        {
            System.err.println("Missing arguments");
            System.exit(-1);
        }

        if (args.length == 1)
        {
            checkCorrectnessForOneArgument(args);
        }

        if (args.length == 2)
        {
            checkCorrectnessForTwoArguments(args);
        }

        if(args.length > 3)
        {
            System.err.println("No such format for arguments");
            System.err.println("There is a lot of arguments");
            System.exit(-1);
        }
    }

    private static void checkCorrectnessForOneArgument(String[] args)
    {
        if (args[0].matches("\\d+"))
        {
            numberAuthenticationPorts = Integer.parseInt(args[0]);
        }
        else
        {
            System.err.println("No such format for arguments");
            System.exit(-1);
        }
    }

    private static void checkCorrectnessForTwoArguments(String[] args)
    {
        if (args[0].matches("\\d+"))
            numberAuthenticationPorts = Integer.parseInt(args[0]);
        else
        {
            System.err.println("No such format for arguments");
            System.exit(-1);
        }

        messageToClients = args[1].replace("'", "");
    }
}
