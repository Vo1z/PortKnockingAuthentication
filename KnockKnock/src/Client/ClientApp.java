package Client;

import Utils.Constants;

import java.util.Arrays;

public class ClientApp
{
    private static int[] ports = null;
    private static String serverAddress = null;
    private static String messageToServer = null;

    private static AuthenticationClient authenticationClient;

    public static void main(String[] args)
    {
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
        if (args == null || args.length == 0)
        {
            System.err.println("Missing arguments");
            System.exit(-1);
        }

        if (args.length == 2)
        {
            if (args[0].matches(Constants.ADDRESS_REGEX_ON_ARGUMENTS)
                    && Arrays.stream(args).skip(1).allMatch(str -> str.matches("\\d+")))
            {
                serverAddress = args[0];
                ports = Arrays.stream(args)
                        .skip(1)
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
            else
            {
                System.err.println("Input for server address or ports was given incorrectly");
                System.exit(-1);
            }
        }

        if (args.length >= 3)
        {
            if (args[0].matches(Constants.ADDRESS_REGEX_ON_ARGUMENTS)
                    && args[1].matches(".+")
                    && Arrays.stream(args).skip(2).allMatch(str -> str.matches("\\d+")))
            {
                serverAddress = args[0];
                messageToServer = args[1];
                ports = Arrays.stream(args)
                        .skip(2)
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
            else
            {
                System.err.println("Input for server address, message to server or ports was given incorrectly");
                System.exit(-1);
            }
        }
    }
}
