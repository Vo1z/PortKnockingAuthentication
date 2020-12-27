package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerProcessing implements Runnable
{
    private final Socket socket;
    private String messageFromClient, messageToClient;

    ServerProcessing(Socket socket, String messageToClient)
    {
        this.socket = socket;
        this.messageToClient = messageToClient;
    }

    private void processClient() throws IOException
    {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        String line;

        while ((line = in.readLine()) != null)
        {
            this.messageFromClient += line;
        }

        out.print(messageFromClient);

        socket.close();
    }

    @Override
    public void run()
    {
        try { this.processClient(); }
        catch (IOException ioException) { ioException.printStackTrace(); }
    }
}
