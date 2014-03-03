import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);//writing out from the client
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));//reading into the client
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));//getting input from the user via the keyboard
        ) {
            System.out.println("Welcome to my awesome chat server!");
            ServerPollingThread thread = new ServerPollingThread(input);
            thread.start();

            String userInput;
            while (true) {
                if ((userInput = stdIn.readLine()) != null) {
                    output.println(userInput);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    public static class ServerPollingThread extends Thread {
        private BufferedReader serverInput;

        public ServerPollingThread(BufferedReader input) {
            this.serverInput = input;
        }

        public void run() {
            try {
                while (true) {
                    String inputLine = serverInput.readLine();
                    if (inputLine != null) {
                        System.out.println(inputLine);
                    }

                }
            } catch (IOException ie) {
                System.out.println("Exception caught in the run method");
                System.out.println(ie.getMessage());
            }
        }
    }
}
