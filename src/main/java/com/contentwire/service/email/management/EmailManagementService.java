package com.contentwire.service.email.management;

import com.contentwire.model.Audience;
import com.contentwire.model.AudienceMember;
import com.contentwire.model.Message;
import com.contentwire.service.repository.service.ConnectionService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;

/**
 * A Singleton class that can be used to send MIME messages through the SMTP protocol for a given Audience.
 */
public class EmailManagementService {

    /**
     *
     * The method configures settings for Javax Mail, creates a MIME message from the subject, body and session, then sets the recipients to members of a given audience and sends the E-Mail.
     *
     * @param message The message to send. Contains Subject and Body. The method creates a MIME message from its contents.
     * @param sender E-Mail address of the sender.
     * @param session The Session used to transport messages through SMTP.
     * @param audience Target Audience. The method sends the mail for each of its members.
     */
    public static void sendForAudience(Message message, String sender, Session session, Audience audience) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        msg.setSubject(message.getSubject());
        String body = message.getText();
        if(body.contains("<html>") && body.contains("</html>")) {
            msg.setContent(body, "text/html");
        } else {
            msg.setText(body);
        }


        try {
            for(AudienceMember am: ConnectionService.getMembersOfAudience(audience.getName())) {
                msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(am.getEmail()));
            }
            Transport.send(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
