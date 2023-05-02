import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {
         Socket client = new Socket("localhost", 8080);
        System.out.println("Connected to server at " + client.getInetAddress().getHostAddress());

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        while (true) {
            String message = console.readLine();
            out.println(message);
            String response = in.readLine();
            System.out.println(response);
        }
    }
}