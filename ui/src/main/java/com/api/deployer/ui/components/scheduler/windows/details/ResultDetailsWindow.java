package com.api.deployer.ui.components.scheduler.windows.details;

import com.api.deployer.ui.data.jobs.results.JobResult;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.TableModelMapAdapter;
import com.redshape.ui.panels.FormPanel;

import javax.swing.*;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.components.scheduler.windows.details
 */
public class ResultDetailsWindow extends JFrame {
    private JobResult result;

    public ResultDetailsWindow( JobResult result ) {
        super();

        this.result = result;

        this.buildUI();
        this.configUI();
    }

    protected JobResult getResult() {
        return this.result;
    }

    protected void buildUI() {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
        this.add( this.createInfoPane() );
        this.add( this.createDetailsPane() );
        this.add( this.createButtonsPane() );
    }

    protected JComponent createInfoPane() {
        FormPanel panel = new FormPanel();
        panel.addField("id", "Result ID", new JLabel( this.result.getId().toString() ) );
        panel.addField("jobId", "Related job ID", new JLabel( this.result.getJobId().toString() ) );
        panel.addField("date", "Completion date", new JLabel( this.result.getCompletionDate().toString() ) );
        return panel;
    }

    protected JComponent createDetailsPane() {
        JTable table = new JTable(
                new TableModelMapAdapter( this.getResult().getAttributes() ) );
        JScrollPane pane = new JScrollPane( table );
        table.setFillsViewportHeight(true);
        return pane;
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
    }

}
