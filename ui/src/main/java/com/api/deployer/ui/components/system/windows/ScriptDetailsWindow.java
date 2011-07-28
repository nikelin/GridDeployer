package com.api.deployer.ui.components.system.windows;

import com.api.deployer.ui.data.scripts.Script;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.panels.FormPanel;

import javax.swing.*;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.components.system.windows
 */
public class ScriptDetailsWindow extends JFrame {
    private Script script;

    public ScriptDetailsWindow( Script script ) {
        super();

        this.script = script;

        this.buildUI();
        this.configUI();
    }

    protected void buildUI() {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
        this.add( this.createDetailsPane() );
        this.add( this.createButtonsPane() );
    }

    protected JComponent createDetailsPane() {
        FormPanel panel = new FormPanel();
        panel.addField("name", "Name", new JLabel( this.script.getName() ) );
        panel.addField("description", "Description", new JLabel( this.script.getDescription() ) );
        panel.addField("category", "Category", new JLabel( this.script.getCategory().getName() ) );
        panel.addField("declaration", "Declaration", new JLabel( this.script.getDeclaration()) );
        return panel;
    }

    protected JComponent createButtonsPane() {
        Box box = Box.createHorizontalBox();
        box.add( new JButton(
            new InteractionAction(
                "Close",
                new WindowCloseHandler(this)
            )
        ) );
        return box;
    }

    protected void configUI() {
        this.setSize( 400, 150 );
        this.setTitle("Script details");
    }

}
