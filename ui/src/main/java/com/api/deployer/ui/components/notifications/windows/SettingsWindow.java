package com.api.deployer.ui.components.notifications.windows;

import com.api.deployer.notifications.NotificationType;
import com.redshape.ui.FormPanel;
import com.redshape.ui.actions.InteractionAction;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.events.IEventHandler;
import com.redshape.ui.events.handlers.WindowCloseHandler;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.utils.config.IWritableConfig;

import javax.swing.*;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.ui.components.notifications.windows
 */
public class SettingsWindow extends JFrame {
    private FormPanel form;
    private JComboBox levelSelector;

    public SettingsWindow() {
        super();

        this.buildUI();
        this.configUI();
    }

    protected void buildUI() {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
        this.add( this.createSettingsPane() );
        this.add( this.createButtonsPane() );
    }

    protected void onSave() {
        IWritableConfig settingsNode = this.getSettings();
        settingsNode.attribute("level",
                String.valueOf(
                    ( (NotificationType) this.levelSelector.getSelectedItem() ).level() ) );
        settingsNode.attribute("enable",
                String.valueOf( this.form.<Boolean>getField("enable").getValue() ) );
        settingsNode.attribute("saveHistory",
                String.valueOf( this.form.<Boolean>getField("history").getValue() ) );

        UIRegistry.getNotificationsManager().info("Notifications settings saved successfully!");
    }

    protected IWritableConfig getSettings() {
        return UIRegistry.getSettings().createChild("notifications");
    }

    protected Object getSetting( String name, Object defaultValue ) {
        Object value = this.getSettings().attribute(name);
        if ( value == null ) {
            return defaultValue;
        }

        return value;
    }

    protected JComboBox createLevelSelector() {
        return this.levelSelector = new JComboBox( NotificationType.values() );
    }

    protected JComponent createButtonsPane() {
        Box box = Box.createHorizontalBox();

        box.add( new JButton(
            new InteractionAction(
                "Save",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        SettingsWindow.this.onSave();
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Cancel",
                new WindowCloseHandler(this)
            )
        ) );

        return box;
    }

    protected JComponent createSettingsPane() {
        FormPanel panel = this.form = new FormPanel();
        panel.addField("enable", "Enable events notifications", new JCheckBox() )
             .setValue( this.getSetting("enable", true) );
        panel.addField("level", "Notifications level", this.createLevelSelector() )
             .setValue( this.getSetting("level", NotificationType.INFO ) );
        panel.addField("history", "Save notifications history", new JCheckBox() )
             .setValue( this.getSetting("save", true) );

        return panel;
    }

    protected void configUI() {
        this.setTitle("Notifications settings");
        this.setSize( 400, 200 );
    }

}
