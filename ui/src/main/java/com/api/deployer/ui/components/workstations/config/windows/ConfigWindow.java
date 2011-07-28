package com.api.deployer.ui.components.workstations.config.windows;

import com.api.deployer.jobs.configuration.network.NetworkConfigurationJob;
import com.api.deployer.ui.components.workstations.config.panels.PartitionsEditorPanel;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.bindings.render.ISwingRenderer;
import com.redshape.ui.data.bindings.render.components.ObjectUI;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;

public class ConfigWindow extends JFrame {
	private static final long serialVersionUID = 248431473962567342L;
	private Workstation record;
    private ObjectUI jobUI;
	
	public ConfigWindow() {
		this(null);
	}
	
	public ConfigWindow( Workstation record ) {
		super();
		
		this.buildUI();
		this.configUI();
	}
	
	public void setRecord( Workstation record ) {
		this.record = record;
	}
	
	protected Workstation getRecord() {
		return this.record;
	}
	
	protected void configUI() {
		this.setSize( 600, 400 );
		this.setTitle("Workstation configuration");
	}
	
	protected void buildUI() {
		this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );

        JTabbedPane pane = new JTabbedPane();
        pane.add( "Network configuration", this.createNetworkConfigurationPane() );
        pane.add( "Partitions editor", this.createPartitionsEditorPane() );
        this.add( pane );

        this.add( this.createButtonsPane() );
	}

    protected void onSave() {

    }

    protected JComponent createButtonsPane() {
        Box box = Box.createHorizontalBox();

        box.add( new JButton(
            new InteractionAction(
                "Save",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ConfigWindow.this.onSave();
                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Close",
                new WindowCloseHandler(this)
            )
        ) );

        return box;
    }

    protected JComponent createNetworkConfigurationPane() {
        try {
            ISwingRenderer renderer = UIRegistry.<ISwingRenderer>getViewRendererFacade()
                                            .createRenderer(NetworkConfigurationJob.class);

            this.jobUI = renderer.render( this, NetworkConfigurationJob.class );

            return this.jobUI;
        } catch ( Throwable e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }

    protected JComponent createPartitionsEditorPane() {
        return new PartitionsEditorPanel( this.getRecord() );
    }
	
}
