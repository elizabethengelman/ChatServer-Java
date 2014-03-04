import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private static Set<PrintWriter> printWriters = new HashSet<PrintWriter>();

    public static void main(String[] args) throws Exception {

        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Server started");
        ServerSocket serverSocket = new ServerSocket(portNumber);
        try {
            while (true) {
                ChatClientThread thread = new ChatClientThread(serverSocket.accept());
                thread.start();
            }
        } finally {
            serverSocket.close();
        }
    }

    public static class ChatClientThread extends Thread {
        private Socket socket;

        public ChatClientThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                printWriters.add(output);

                System.out.println("A new client is connected!");
                String userName = "";
                while (userName.length() < 1) {
                    output.println("Please enter your preferred user name. It must be at least 1 character.");
                    userName = input.readLine();
                }
                output.println("Your name has been accepted, it is: " + userName);
                while (true) {
                    String inputLine = input.readLine();
                    if (inputLine != null) {
                        System.out.println("from client: " + inputLine);
                        notifyClient(userName + ": " + inputLine);
                    }

                }
            } catch (IOException ie) {
                System.out.println("Exception caught in the run method");
                System.out.println(ie.getMessage());
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                try {
                    System.out.println("A client is going down, closing its socket!");
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void notifyClient(String inputLine) {
            for (PrintWriter printWriter : printWriters) {
                printWriter.println(inputLine);
            }
        }
    }
}
