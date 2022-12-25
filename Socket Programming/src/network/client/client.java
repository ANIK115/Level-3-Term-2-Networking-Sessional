package network.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class client {
    static final int PORT = 5115;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            Socket clientSocket = new Socket("localhost", PORT);
            System.out.println("Enter a file name: ");
            String filename = scanner.next();
            new ClientThread(clientSocket, filename);
        }
    }
}
