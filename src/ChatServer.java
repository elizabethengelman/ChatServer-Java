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
//                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);//when to use different types
//                // of readers/writers?
//                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            while(true){
            ChatClientThread thread = new ChatClientThread(serverSocket.accept());
            thread.start();
            }
        }

        catch(IOException ie){
            System.out.println("Exception caught when trying to listen on port " + portNumber);
            System.out.println(ie.getMessage());
        }
    }

    public static class ChatClientThread extends Thread{
        private Socket socket;

        //new Thread constructor, which takes in a new socket
        public ChatClientThread(Socket socket){
            this.socket = socket;
        }

        public void run(){
            try(
                    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                )
            {
                System.out.println("A new client is connected!");
                String inputLine;
                while ((inputLine = input.readLine()) != null){
                    System.out.println(inputLine);
                    output.println(inputLine);
                }
            }
            catch(IOException ie){
                System.out.println("Exception caught in the run method");
                System.out.println(ie.getMessage());
            }
            finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                try {
                    System.out.println("A client is going down, closing its socket!");
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
