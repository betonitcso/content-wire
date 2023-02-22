package com.contentwire;

import com.formdev.flatlaf.FlatDarkLaf;
import com.contentwire.service.repository.service.ConnectionService;
import com.contentwire.service.resource.management.UserManagementService;
import com.contentwire.ui.display.auth.AuthenticationUI;
import com.contentwire.ui.display.dashboard.DashboardUI;

import javax.naming.AuthenticationException;
import javax.swing.*;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, AuthenticationException, InterruptedException {
        FlatDarkLaf.setup();

        System.out.println("[INFO] Application starting...");

        ConnectionService.setConnectionURI("jdbc:postgresql://localhost:5432/cw");

        AuthenticationUI authenticationUI = new AuthenticationUI();
        DashboardUI dashboard = new DashboardUI();

        ConnectionService.initTables();

        authenticationUI.dispatchAuthentication();

        SwingUtilities.invokeLater(() -> {
            while(true) {
                if(UserManagementService.getCurrentUser() != null) {
                    break;
                }
            }
        });

        dashboard.display();
    }
}
