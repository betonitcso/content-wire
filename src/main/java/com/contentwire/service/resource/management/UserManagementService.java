package com.contentwire.service.resource.management;

import com.contentwire.ui.window.router.exceptions.WrongCredentialsException;
import com.contentwire.model.CampaignManager;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Singleton responsible for generating user passwords, authenticating Campaign Managers and managing the current user for the ContentWIRE application.
 */
public class UserManagementService {
    private static CampaignManager currentUser;

    /**
     * Hashes a given text with BCrypt.
     * @param password input text
     * @return hashed and salted password
     */
    public static String genHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    /**
     * Checks password match for given string and a Campaign Manager's password hash
     * @param cm Campaign Manager to authenticate
     * @param password input string
     * @throws WrongCredentialsException the password wasn't correct
     */
    public static void authenticate(CampaignManager cm, String password) throws WrongCredentialsException {
        if(cm.isAuthenticated()) return;
        if (BCrypt.checkpw(password, cm.getPasswordHash())) {
            cm.setAuthenticated(true);
        } else {
            throw new WrongCredentialsException("You've entered incorrect credentials");
        }
    }

    public static void setCurrentUser(CampaignManager campaignManager) {
        currentUser = campaignManager;
    }

    public static CampaignManager getCurrentUser() {
        return currentUser;
    }
}
