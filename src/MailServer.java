import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class MailServer {
    public static Account mclient;
    public static int index=0;

    static Vector<ClientHandler> clients = new Vector<>();
    static int i = 0;
    static boolean connectedClient =false;//Checks whether the client is connected

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4444);
        Socket s;

        while (true) {
            s = ss.accept();
            connectedClient =true;

            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            ClientHandler mtch = new ClientHandler(s,String.valueOf(i), din, dout);

            sendMessages(String.valueOf(i),"Mail Server: ");
            sendMessages(String.valueOf(i),"------------");
            sendMessages(String.valueOf(i),"You connected as a guest");
            sendMessages(String.valueOf(i),"------------");
            sendMessages(String.valueOf(i),"Type the option you want to do");
            sendMessages(String.valueOf(i),"------------");
            sendMessages(String.valueOf(i),"Options:");
            sendMessages(String.valueOf(i),"LogIn");
            sendMessages(String.valueOf(i),"SignIn");
            sendMessages(String.valueOf(i),"Exit");

            Thread t = new Thread(mtch);

            clients.add(mtch);
            t.start();
            i++;

        }
    }

    // ClientHandler class
    static class ClientHandler implements Runnable {
        private Account account;
        private String username;
        final DataInputStream din;
        final DataOutputStream dout;
        Socket s;

        // constructor
        public ClientHandler(Socket s,String username,DataInputStream din, DataOutputStream dout) throws IOException {
            this.din = din;
            this.dout = dout;
            this.username =username;
            this.s = s;
        }

        @Override
        public void run() {
            String username = null;
            String received;

            while (true) {
                try {
                    if (connectedClient)
                    {
                        connectedClient =false;
                        sendMessages(this.username,"Mail Server: ");
                        sendMessages(this.username,"------------");
                        sendMessages(this.username,"You connected as a guest");
                        sendMessages(this.username,"------------");
                        sendMessages(this.username,"Type the option you want to do");
                        sendMessages(this.username,"------------");
                        sendMessages(this.username,"Options:");
                        sendMessages(this.username,"LogIn");
                        sendMessages(this.username,"SignIn");
                        sendMessages(this.username,"Exit");
                    }

                    // receive the string
                    received = din.readUTF().toLowerCase();
                    System.out.println(received);

                    //for every option the user types , calls the functions that does those options


                    if (received.equals("exit")) {
                        this.s.close();
                        break;
                    }


                    if(received.equals("signin"))
                    {
                        sendMessages(this.username,"Please type your username");
                        username = din.readUTF();
                        while(!register(username)){
                            //if the username is invalid or used
                            sendMessages(this.username,"Invalid Username try another one");
                            username = din.readUTF();
                        }
                        sendMessages(this.username,"Please type your password");
                        String password = din.readUTF();
                        account=new Account(username,password);
                        mclient=account;
                        sendMessages(this.username,"Welcome "+username+"! Type login to log onto your account!");
                        this.username=username;

                    }


                    if(received.equals("login")) {
                        username=logIn();
                        account.setUsername(username);
                        account.setPassword(account.getPasswords().get(index) );
                        mclient=account;
                    }

                    if(received.equals("showemails"))
                    {
                        showemails();
                    }

                    if(received.equals("reademail"))
                    {
                        readEmail();
                    }


                    if(received.equals("deleteemail"))
                    {
                        deleteEmail();
                    }

                    if(received.equals("newemail")) {
                        newEmail();
                    }
                    if(received.equals("logout"))
                    {
                        connectedClient =true;
                        username = null;
                    }
                    if(!received.equals("signin")) {//if you sign in you have to login first
                        sendMessages(this.username, " ");
                        sendMessages(this.username, "Welcome back " + username + "!");
                        sendMessages(this.username, "==========================");
                        sendMessages(this.username, "NewEmail");
                        sendMessages(this.username, "ShowEmails");
                        sendMessages(this.username, "ReadEmail");
                        sendMessages(this.username, "DeleteEmail");
                        sendMessages(this.username, "Exit");
                        sendMessages(this.username, "==========================");
                        sendMessages(this.username, " ");
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            try {
                // closing resources
                this.din.close();
                this.dout.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public String logIn() throws IOException {
            //checks if the email is already used.
            //and asks for another
            sendMessages(this.username,"Please type your email");
            username = din.readUTF();
            account = new Account();
            while (!account.getUsernames().contains(username)) {
                sendMessages(this.username,"The email is not correct");
                username = din.readUTF();
                //check if the user wants to exit
                if (username.equals("exit")) {
                    this.s.close();
                    break;
                }
            }
            index=account.getUsernames().indexOf(username);
            account.setUsername(username);
            account.setPassword(account.getPasswords().get(index) );
            sendMessages(this.username,"Type your password");
            String password = din.readUTF();
            while(!account.getPassword().equals(password))
            {
                sendMessages(this.username,"Wrong password. Try again");
                password = din.readUTF();
                if(password.equals("exit")) {
                    this.s.close();
                    break;
                }
            }
            this.username=username;
            return username;
        }

        //shows the mainbody of an email and set it as seen
        public void readEmail() throws IOException {
            boolean noemails=true;
            ArrayList<Email> mailbox= mclient.mailboxForUser(mclient.getUsername());
            for (int i = 0; i < mailbox.size(); i++)
            {
                if(mailbox.get(i).isNew() == "NEW") {
                    sendMessages(mclient.getUsername(), "ID            " + "FROM              " + "SUBJECT");
                    sendMessages(this.username,(i + 1) + "          " +
                            mailbox.get(i).getSender() + "          " +
                            mailbox.get(i).getSubject()
                            + "          " +
                            mailbox.get(i).getMainbody());
                    noemails=false;
                    mailbox.get(i).setisNew(false);//if a mail is read set as not new
                }
            }
            if(noemails)
                sendMessages(this.username,"No new emails.");
        }

        //create a new email
        public void newEmail() throws IOException {
            sendMessages(this.username, "Receiver:");
            String receiver = din.readUTF();
            sendMessages(this.username, "Subject:");
            String subject = din.readUTF();
            sendMessages(this.username, "Main body:");
            String mainbody = din.readUTF();

            if (!account.usernames.contains(receiver))//checking if the receiver is invalid
                sendMessages(this.username, "Wrong receiver!");
            else {//add the new email to the mailbox
                sendMessages(this.username, "the email was sent successfully!");
                Email email = new Email(true, this.username, receiver, subject, mainbody);
                mclient.mailbox.add(email);
            }
        }

        //ask the id number of an email and then delete it
        public void deleteEmail() throws IOException {
            ArrayList<Email> mailbox= mclient.mailboxForUser(mclient.getUsername());
            if(mailbox.isEmpty())
                sendMessages(mclient.getUsername(), "There are no emails");
            else{
                showemails();
                sendMessages(this.username, "Type which email you wish to delete.");
                Integer deleleteItem = Integer.valueOf(din.readUTF());
                for(int i=0; i< mclient.getMailbox().size();i++)
                    if(mailbox.get(deleleteItem-1).equals(mclient.getMailbox().get(i)))
                    {
                        mclient.getMailbox().remove(i);
                        break;
                    }
                sendMessages(this.username, "The Email was successfully deleted.");
                showemails();
            }
        }
    }


    public static void sendMessages(String name,String text) throws IOException {
        for (ClientHandler mc : MailServer.clients) {
            // if the recipient is found, write on its
            // output stream
            if (mc.username.equals(name)) {
                mc.dout.writeUTF(text);
                break;
            }
        }
    }

    public static void showemails() throws IOException {
        //show emails of a client
        ArrayList<Email> mailbox= mclient.mailboxForUser(mclient.getUsername());
        if(mailbox.isEmpty())
            sendMessages(mclient.getUsername(), "You have no emails");
        else{
            sendMessages(mclient.getUsername(), "No.            " + "Sender              " + "Subject");
            for (int i = 0; i < mailbox.size(); i++)
            {
                sendMessages(mclient.getUsername(), (i + 1) + " " + mailbox.get(i).isNew() + "          " +
                        mailbox.get(i).getSender() + "          " +
                        mailbox.get(i).getSubject());
            }
        }
    }

    public static boolean register(String toCheck) //checking if a username is invalid
    {
        Account a = new Account();
        boolean reg = a.getUsernames().contains(toCheck);
        if(reg)
            return false;
        return true;
    }
}




