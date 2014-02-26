import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by elizabethengelman on 2/26/14.
 * This server, as it is now, is just what Telnet is doing. It takes something from the user, sends it to the Server,
 * and prints out what the server gives it
 */
public class ChatServer {
    public static void main (String[] args) throws Exception {
        if (args.length != 1){
            System.err.println("Usage: java ChatServer <port number>");
            System.exit(1); //what is exit code 1?
        }

        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Server started");
        try(
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);//when to use different types
                // of readers/writers?
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            )
        {
            System.out.println("Client connected on port: " + portNumber);
            System.out.println(input);
            String inputLine;
            while ((inputLine = input.readLine()) != null){
                System.out.println(inputLine);
                output.println(inputLine);
            }
        }
        catch(IOException ie){
            System.out.println("Exception caught when trying to listen on port " + portNumber);
            System.out.println(ie.getMessage());

        }
    }
}
