import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Account {

    private String username;
    private String password;
    //a list of all emails from all users
    public static ArrayList<Email> mailbox= new ArrayList<>(Arrays.asList(
            new Email(true, "CHRISTODOULOS", "GEORGE", "HEY", "HEY MAN!"),
            new Email(true, "CHRISTODOULOS", "GEORGE", "OKAY?", "ARE YOU OKAY?"),
            new Email(true, "GEORGE", "CHRISTODOULOS", "hey", "hey man"),
            new Email(true, "GEORGE", "CHRISTODOULOS", "good", "am good you?"),
            new Email(true, "CHRISTODOULOS", "GEORGE", "GAME!", "WANT TO PLAY A GAME?"),
            new Email(true, "GEORGE","CHRISTODOULOS" , "no", "sorry i cant.")
    ));
    //passwords
    public static ArrayList<String> passwords = new ArrayList<String>(Arrays.asList("#guest","PASSWORD1","PASSWORD2","MARIASPASSWORD"));
    //USERNAMES
    public static ArrayList<String> usernames = new ArrayList<String>(Arrays.asList("#guest","CHRISTODOULOS","GEORGE","MARIA1996"));

    public Account(){}
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.usernames.add(username);
        this.passwords.add(password);

    }

    public String getUsername() {
        return username;
    }

    //find which emails belong to a specific user
    public ArrayList<Email> mailboxForUser(String user){
        ArrayList<Email> mailboxOfUser = new ArrayList<>();
        for(int i =0; i < mailbox.size(); i++)
        {
            if(user.equals(mailbox.get(i).getReceiver()))
                mailboxOfUser.add(mailbox.get(i));
        }
        return mailboxOfUser;
    }

    public String getPassword() {
        return password;
    }

    public List<Email> getMailbox() {
        return this.mailbox;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public ArrayList<String> getPasswords() {
        return passwords;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
