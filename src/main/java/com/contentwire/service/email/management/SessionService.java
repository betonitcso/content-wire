package com.contentwire.service.email.management;

import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class SessionService {

    private Authenticator authenticator;

    private final Properties properties;

    /**
     * Sets up system properties for secure SMTP messaging.
     * @param host string containing the host address/domain name
     * @param port object containing the port number
     */

    public SessionService(String host, Object port) {
        properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port.toString());
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
    }

    /**
     * Creates an SMTP Auth session and authenticates with user credentials
     * @param senderUsername SMTP username
     * @param senderPassword SMTP pass
     */

    public void authenticate(String senderUsername, String senderPassword) {
        this.authenticator = new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderUsername, senderPassword);
            }
        };
    }
    public Session createSession() {
        return Session.getInstance(properties, authenticator);
    }
}
