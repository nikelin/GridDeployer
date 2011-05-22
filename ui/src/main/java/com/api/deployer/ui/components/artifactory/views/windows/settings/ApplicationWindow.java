package com.api.deployer.ui.components.artifactory.views.windows.settings;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationWindow extends JFrame {

    public ApplicationWindow() {
        super();

        this.configUI();
        this.buildUI();
    }

    protected void buildUI() {

    }

    protected void configUI() {
        this.setTitle("Environment settings");
        this.setSize( 550, 400 );
    }

}
