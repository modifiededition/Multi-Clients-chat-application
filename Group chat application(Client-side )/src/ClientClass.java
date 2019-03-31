import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientClass {

// a client is going to send a writing text to the server and ther server is going to recive the text
// and going to send it to the textbox of every client.

     JTextArea incoming;
     JTextField outgoing;
   Socket socket;
   BufferedReader reader;
   PrintWriter writer;



    public static void main(String[] args) {
        ClientClass clientClass = new ClientClass();
        clientClass.go();
    }

    public void go(){
        JFrame frame = new JFrame("Clint App");
        JPanel panel = new JPanel();

        incoming = new JTextArea(15,50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(incoming);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("send");
        sendButton.addActionListener(new SendButtonListner());

        panel.add(scrollPane);
        panel.add(outgoing);
        panel.add(sendButton);
        setUptheNetworking();

        // calling another stack to get input from the server and di[lay it on the incoming textbox.

        Thread readerThread = new Thread(new Incomingreader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.setSize(400,500);
        frame.setVisible(true);

    }



    public void setUptheNetworking(){
        try {
            socket = new Socket("10.7.10.158", 5000);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inputStreamReader);
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("Network Established");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class SendButtonListner implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            try {
                System.out.println(outgoing.getText());
                writer.println(outgoing.getText());
                writer.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class Incomingreader implements Runnable{

        @Override
        public void run(){
            String message;
            try {

            while((message=reader.readLine())!=null) {
                System.out.println(message);
                incoming.append(message + "\n");
            }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
