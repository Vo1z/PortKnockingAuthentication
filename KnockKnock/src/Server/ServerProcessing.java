package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerProcessing extends Thread
{
    private final Socket socket;
    private String messageFromClient, messageToClient = "Hello client!";

    public ServerProcessing(Socket socket)
    {
        this.socket = socket;
    }

    public ServerProcessing(Socket socket, String messageToClient)
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

        out.print(messageToClient);
        out.flush();

        //TODO check for correctness
        //this.socket.close();
    }

    @Override
    public void run()
    {
        try { this.processClient(); }
        catch (IOException ioException) { ioException.printStackTrace(); }
    }

    public String getMessageFromClient()
    {
        return this.messageFromClient;
    }
}