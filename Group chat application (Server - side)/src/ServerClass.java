import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerClass {
    ArrayList clientPrintList;

    public static void main(String[] args) {
        new ServerClass().go();
    }

    public void go() {
        clientPrintList = new ArrayList();
        try {
            ServerSocket socket = new ServerSocket(5000);

            while (true) {
                Socket clientSocket = socket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientPrintList.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public  class  ClientHandler implements Runnable {

        BufferedReader bufferedReader;
        Socket sock;

        public  ClientHandler(Socket socket) {
            try {
                sock = socket;
                InputStreamReader streamReadrer = new InputStreamReader(sock.getInputStream());
                bufferedReader = new BufferedReader(streamReadrer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public  void run() {
            String message;
            try {
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println(message);
                    sendToEveryone(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

        public void sendToEveryone(String message) {
            Iterator it = clientPrintList.iterator();

            while (it.hasNext()) {
                try {

                    PrintWriter writer = (PrintWriter) it.next();
                    writer.println(message);
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }