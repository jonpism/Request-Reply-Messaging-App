import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MailClient {
    final static int ServerPort = 4444;

    public MailClient() {
    }

    public static void main(String args[]) throws IOException {

        Scanner scn = new Scanner(System.in);

        // establish the connection
        Socket s = new Socket("localhost", ServerPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();

                    try {

                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //readMessage thread
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    // read the message sent to this client
                    String received = null;
                    try {
                        received = dis.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(received);




                }
            }
        });

        sendMessage.start();
        readMessage.start();


    }

}
