package com.contentwire.ui.display.dashboard.components.elements.buttons;

import javax.swing.*;
import java.awt.*;

/**
 * Button for easier and more universalcallback implementations
 */

public class QuickActionButton extends JButton {

    public QuickActionButton(String name, ButtonCallback callback) {
        super(name);

        this.initStyle();

        this.addActionListener(e -> callback.onClick());
    }

    public QuickActionButton(String name, String iconUrl, ButtonCallback callback) {
        super(name);

        this.initCustomIcon(iconUrl);

        this.addActionListener(e -> callback.onClick());
    }


    /**
     * Initialize Swing elements.
     */

    private void initStyle() {
        Image icon = new ImageIcon("src/main/resources/assets/icons/arrow-right.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        this.setIcon(new ImageIcon(icon));
        this.setIconTextGap(10);
        this.putClientProperty( "FlatLaf.styleClass", "h2" );
    }

    /**
     * Add custom icon for button.
     * @param url URI of the icon
     */

    public void initCustomIcon(String url) {
        Image icon = new ImageIcon(url).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        this.setIcon(new ImageIcon(icon));
        this.setIconTextGap(10);
        this.putClientProperty( "FlatLaf.styleClass", "h2" );
    }
}
