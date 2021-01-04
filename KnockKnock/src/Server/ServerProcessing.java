package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerProcessing extends Thread
{
    private final Socket socket;
    private final AuthenticationServer server;
    private final String messageToClient;
    private String messageFromClient;

    public ServerProcessing(Socket socket, AuthenticationServer server)
    {
        this.socket = socket;
        this.server = server;
        this.messageToClient = server.getMessageToClients();
    }

    private void processClient() throws IOException
    {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        this.messageFromClient = in.readLine();

        out.println(this.messageToClient);
        out.flush();

        this.server.addMessage(this.socket.getInetAddress().getHostAddress(), this.messageFromClient);
        this.socket.close();
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