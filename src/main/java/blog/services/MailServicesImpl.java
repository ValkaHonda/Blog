package blog.services;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
@Service
public class MailServicesImpl implements MailServices{
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String USERNAME = "mailservices321@gmail.com";//
    private static final String PASSWORD = "blog123blog";

    @Override
    public boolean sendEmailMessage(EmailMessage emailMessage) {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        try{
            Session session = Session.getDefaultInstance(props,
                    new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(USERNAME, PASSWORD);
                        }});

            // -- Create a new message --
            Message msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(USERNAME));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailMessage.getReceiver(),false));
            msg.setSubject(emailMessage.getSubject());
           // msg.setText(emailMessage.getContent());
            msg.setContent("<h1>Welcome, sir "+emailMessage.getSubject()+"<script>\n" +
                    "</h1>" +
                    "<button onclick=\"redirect()\">Confirm JS fix</button>"+
                    "        <h2>Please press confirm if this is your email</h2>" +
                    "        <a href=\"https://www.wikipedia.org/\">Confirm</a>", "text/html");
            msg.setSentDate(new Date());
            Transport.send(msg);
        }catch (MessagingException e){
            return false;
        }
        return true;
    }
}
