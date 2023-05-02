import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static ArrayList<Socket> clients = new ArrayList<>();
    private static ArrayList<String> clientNames = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Server started on port 8080");

        while (true) {
            Socket client = server.accept();
            clients.add(client);
            String name = "Client-" + (clients.size() - 1);
            clientNames.add(name);
            System.out.println(name + " connected from " + client.getInetAddress().getHostAddress());

            Thread t = new Thread(new ClientHandler(client, name));
            t.start();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket client;
        private String name;

        public ClientHandler(Socket client, String name) {
            this.client = client;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    System.out.println(name + ": " + message);
                    for (Socket c : clients) {
                        if (c != client) {
                            PrintWriter out = new PrintWriter(c.getOutputStream(), true);
                            out.println(name + ": " + message);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error handling client " + name + ": " + e);
            } finally {
                try {
                    client.close();
                    clients.remove(client);
                    clientNames.remove(name);
                    System.out.println(name + " disconnected.");
                } catch (IOException e) {
                    System.err.println("Error closing client " + name + ": " + e);
                }
            }
        }
    }
}