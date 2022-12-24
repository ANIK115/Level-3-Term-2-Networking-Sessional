package network.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int PORT = 5115;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

        while(true)
        {
            //accept client request
            Socket client = serverSocket.accept();
            //this function opens a new thread to serve the client request and again go to listening state
            serve(client);
        }
    }

    private static void serve(Socket client) throws IOException {
         //it serves the clients
        new ServerThread(client);
    }
}
