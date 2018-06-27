package blog.services;

public class EmailMessage {
    private String receiver;
    private String subject;
    private String content;

    public EmailMessage(String receiver, String subject, String content) {
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setTo(String receiver) {
        this.receiver = receiver;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
