package com.api.deployer.ui.components.workstations.windows;

import com.api.deployer.jobs.deploy.AgentSetupJob;
import com.api.deployer.ui.components.workstations.WorkstationComponent;
import com.redshape.ui.Dispatcher;

import com.redshape.ui.application.UIException;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.bindings.render.ISwingRenderer;
import com.redshape.ui.data.bindings.render.components.ObjectUI;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;
import java.awt.*;
/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.components.workstations.windows
 */
public class DeployWindow extends JFrame {
    private ObjectUI jobUI;

    public DeployWindow() throws UIException {
        super();

        this.buildUI();
        this.configUI();
    }

    protected void buildUI() throws UIException {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
        this.createJobUI();
        this.add( this.createButtonsPanel() );
    }

    protected void createJobUI() throws UIException  {
        try {
            ISwingRenderer renderer = UIRegistry.<ISwingRenderer>getViewRendererFacade()
                                                .createRenderer(AgentSetupJob.class);

            this.jobUI = renderer.render( this.getContentPane(), AgentSetupJob.class );
            this.jobUI.setPreferredSize( new Dimension( 500, 300 ) );
        } catch ( InstantiationException e ) {
            throw new UIException( e.getMessage(), e );
        }
    }

    protected JComponent createButtonsPanel() {
        Box box = Box.createHorizontalBox();

        box.add(new JButton(
            new InteractionAction(
                "Setup",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        try {
                            DeployWindow.this.onSetup();
                        } catch ( UIException e ) {
                            throw new UnhandledUIException( e.getMessage(), e );
                        }
                    }
                }
            )
        ) );

        box.add(new JButton(
            new InteractionAction(
                "Cancel",
                new WindowCloseHandler(this)
            )
        ) );

        return box;
    }

    protected void onSetup() throws UIException {
        try {
            if ( this.jobUI == null ) {
                return;
            }

            Dispatcher.get().forwardEvent(
                new AppEvent(
                    WorkstationComponent.Events.Action.Deploy,
                    this.jobUI.<AgentSetupJob>createInstance()
                )
            );
        } catch ( Throwable e ) {
            throw new UIException( e.getMessage(), e );
        }
    }

    protected void configUI() {
        this.setSize( 500, 250 );
        this.setTitle("Workstation setup");
    }

}
