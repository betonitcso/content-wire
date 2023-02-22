package com.contentwire.ui.display.dashboard.components.menu;

import com.contentwire.model.CampaignManager;
import com.contentwire.model.DisplayMode;
import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.elements.buttons.LogoutButton;
import com.contentwire.ui.display.dashboard.components.pages.*;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.*;
import com.contentwire.ui.display.dashboard.components.pages.responsible.pages.ResponsibleDashboard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton to initialize and modify menus, and route between pages.
 */

public class MenuController {

    static List<Page> pages = new ArrayList<>();

    /**
     * When called, it changes the tab to the desired one
     * @param window window to set the page for
     * @param tabName identifier of the page
     */

    public static void dispatchTabChange(DashboardUI window, String tabName) {
        for (Page p : pages) {
            if (p.getTitle().equals(tabName)) {
                window.setPage(p);
                break;
            }
        }
    }


    /**
     * Generate menu bar for window
     * @param window window to generate the menu for
     * @param user Campaign Manager using the application
     * @return DashboardMenubar with contents based on role in organization
     */
    public static DashboardMenuBar generateMenuBar(DashboardUI window, CampaignManager user) {
        DashboardMenuBar menuBar = new DashboardMenuBar();
        if(user.getDisplayMode().equals(DisplayMode.ORCHESTRATOR)) {
            addOrchestratorTabs(menuBar, window);
        } else {
            addResponsibleTabs(menuBar, window);
        }
        return menuBar;
    }

    /**
     * Adds pages for Campaign Responsibles
     * @param menuBar custom JmenuBar
     * @param window window to which the tabs should be added
     */

    public static void addResponsibleTabs(DashboardMenuBar menuBar, DashboardUI window) {
        ResponsibleDashboard responsibleDashboard = new ResponsibleDashboard("Send E-Mail", window);
        menuBar.addMenuTab(new MenuTab(window, "Send E-Mail", responsibleDashboard));
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new LogoutButton(window));
        window.setPage(responsibleDashboard);
    }

    /**
     * Adds pages for Campaign Orchestrators
     * @param menuBar custom JmenuBar
     * @param window window to which the tabs should be added
     */
    public static void addOrchestratorTabs(DashboardMenuBar menuBar, DashboardUI window) {
        Dashboard dashboard = new Dashboard("Dashboard", window);
        menuBar.addMenuTab(new MenuTab(window, "Dashboard", dashboard));
        menuBar.addMenuTab(new MenuTab(window, "Campaigns", new Campaigns("Campaigns", window)));
        menuBar.addMenuTab(new MenuTab(window, "Audiences", new Audiences("Audiences", window)));
        menuBar.addMenuTab(new MenuTab(window, "Audience Members", new AudienceMembers("Audience Members", window)));
        menuBar.addMenuTab(new MenuTab(window, "Organization Members", new Organization("Organization Members", window)));
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new LogoutButton(window));
        window.setPage(dashboard);
    }

    public static void addPage(Page p) {
        pages.add(p);
    }
}
