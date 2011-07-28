package com.api.deployer.ui.components.scheduler.windows.details;

import com.api.deployer.ui.data.jobs.Job;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.panels.FormPanel;

import javax.swing.*;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.ui.components.scheduler.windows
 */
public class JobDetailsWindow extends JFrame {
    private Job job;

    public JobDetailsWindow( Job job ) {
        super();

        this.job = job;

        this.buildUI();
        this.configUI();
    }
    protected void buildUI() {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
        this.add(this.createInfoPane());
        this.add( this.createButtonsPane() );
    }

    protected JComponent createInfoPane() {
        FormPanel panel = new FormPanel();
        panel.addField("name", "Name", new JLabel( this.job.getName() ) );
        panel.addField("category", "Category", new JLabel( this.job.getCategory().getName() ) );
        panel.addField("scope", "Execution scope", new JLabel( this.job.getScope().name() ) );
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText( this.job.getDescription() );
        panel.add( descriptionArea );
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
        this.setSize( 400, 200 );
        this.setTitle("Job details");
    }

}
