package com.contentwire.ui.display.dashboard.components.pages;

import com.contentwire.ui.display.dashboard.DashboardUI;

import javax.swing.*;
import java.awt.*;

/**
 * Styled page for dashboards
 */
public class Page extends JPanel {
    protected JPanel content = new JPanel();
    private final JPanel titleContainer = new JPanel();
    String title;
    DashboardUI window;

    /**
     * Initializes layout and UI.
     * @param title title of the page
     * @param window parent window
     */

    public Page(String title, DashboardUI window) {
        super();
        this.window = window;
        this.title = title;

        this.content.setAlignmentY(Component.TOP_ALIGNMENT);

        initComponents(title);
        setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
    }

    /**
     * Initializes styled components
     * @param title title of the page
     */

    private void initComponents(String title) {

        JLabel titleText = new JLabel(title);
        titleText.putClientProperty( "FlatLaf.styleClass", "h1" );

        titleContainer.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleContainer.add(titleText);
        this.add(titleContainer);
        this.add(content);
    }

    /**
     * Add component to the content field
     * @param content component to add
     */

    public void addContent(JComponent content) {
        this.content.add(content);
    }

    public String getTitle() {
        return title;
    }
}
