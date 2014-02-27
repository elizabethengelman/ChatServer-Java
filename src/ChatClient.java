import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by elizabethengelman on 2/26/14.
 */
public class ChatClient {

    BufferedReader input;
    PrintWriter output;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);

    public ChatClient(){
        textField.setEditable(true);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) {
                output.println(textField.getText());
                textField.setText("");
            }
        });
    }

    public void run(String hostName, int portNumber) throws IOException {
                Socket socket = new Socket(hostName, portNumber);
                output = new PrintWriter(socket.getOutputStream(), true);//writing out from the client
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));//reading into the client
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));//getting input from the user via the keyboard
            String userInput;
//            while ((userInput = stdIn.readLine()) != null) {
        while(true){
                userInput = input.readLine();

//                output.println(userInput);//println can only be used with PrintWriter? not OutputStreamWriter?

                System.out.println("echo: " + userInput);
                messageArea.append(userInput);
            }
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run(hostName, portNumber);
    }
}
