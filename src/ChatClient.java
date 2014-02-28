import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by elizabethengelman on 2/26/14.
 */
public class ChatClient {
    public static void main (String[] args) throws IOException {
//        if (args.length != 2){
//            System.err.println("Usage: java EchoClient <host name> <port number>");
//            System.exit(1);
//        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
             Socket socket = new Socket(hostName, portNumber);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);//writing out from the client
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));//reading into the client
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));//getting input from the user via the keyboard
            )
        {
        System.out.println("Welcome to my awesome chat server!");
        ServerPollingThread thread = new ServerPollingThread(input);
        thread.start();

        String userInput;
        while (true){
            if ((userInput = stdIn.readLine()) != null) {
                output.println(userInput);//println can only be used with PrintWriter? not OutputStreamWriter?
            }
        }

        }
        catch(UnknownHostException e){
            System.err.println("Unknown host: " + hostName);
            System.exit(1);
        }
        catch (IOException e){
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    public static class ServerPollingThread extends Thread{
        private BufferedReader serverInput;

        //new Thread constructor, which takes in a new socket
        public ServerPollingThread(BufferedReader input){
            this.serverInput = input;
        }

        public void run(){
            try
            {
                System.out.println("A new client is connected!");
                while (true){
                    String inputLine = serverInput.readLine();
                    if (inputLine != null){
                        System.out.println(inputLine);
                    }

                }
            }
            catch(IOException ie){
                System.out.println("Exception caught in the run method");
                System.out.println(ie.getMessage());
            }
        }
    }
}
