public class Email {


    private boolean isNew; //new email
    private String sender;
    private String receiver;
    private String subject;
    private String mainbody;


    public Email(boolean isNew,String sender,String receiver,String subject,String mainbody)
    {
        this.isNew = isNew;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainbody = mainbody;

    }

    public void setisNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMainbody(String mainbody) {
        this.mainbody = mainbody;
    }



    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getMainbody() {
        return mainbody;
    }


    public String isNew() {
        if(isNew)

            return "NEW";
        else
            return " ";
    }

}
