package com.api.deployer.ui.components.system.windows;

import com.api.commons.StringUtils;
import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.IEvaluator;
import com.api.deployer.ui.components.system.SystemComponent;
import com.api.deployer.ui.components.system.panels.ScriptCategoriesTree;
import com.api.deployer.ui.data.scripts.Script;
import com.api.deployer.ui.data.scripts.categories.ScriptCategoriesStore;
import com.api.deployer.ui.data.scripts.categories.ScriptCategory;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.ui.components.system.windows
 */
public class ScriptingConsoleWindow extends JFrame {
	private JTextArea consoleAreaComponent;
	private JTextField commandLineComponent;
	private JButton executeButton;
    private ScriptCategoriesTree tree;
    private IStore<ScriptCategory> scriptsStore;
    private FormPanel executeDetailsPanel;

	private boolean turnedOff;
    private boolean isConfigured;

	private Queue<String> prev = new LinkedBlockingQueue<String>();
	private Queue<String> next = new LinkedBlockingQueue<String>();

	public ScriptingConsoleWindow() {
		super();

		this.buildUI();
		this.configUI();
	}

	protected IEvaluator getEvaluator() {
		return UIRegistry.getContext().getBean( IEvaluator.class );
	}

	@SuppressWarnings("deprecation")
	protected void appendConsoleMessage( String message ) {
		this.consoleAreaComponent.append( new Date().toLocaleString() );
		this.consoleAreaComponent.append(" - ");
		this.consoleAreaComponent.append(message);
		this.consoleAreaComponent.append("\n");
	}

	protected void turnOff() {
		this.turnedOff = true;
		this.executeButton.setEnabled(false);
	}

	protected boolean isTurnedOff() {
		return this.turnedOff;
	}

	protected void buildUI() {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );

        this.scriptsStore = UIRegistry.getContext().getBean(ScriptCategoriesStore.class);

		JTabbedPane pane = new JTabbedPane();
        pane.add( "Console", this.createConsolePane() );
        pane.add( "Library", this.createLibraryPane() );
        this.add(pane);
	}

    protected JComponent createLibraryPane() {
        JPanel panel = new JPanel();
        panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
        panel.add( this.createScriptsTree() );
        panel.add( this.createScriptsControlPane() );
        panel.add( this.createExecuteDetailsPanel() );
        return panel;
    }

    protected JComponent createExecuteDetailsPanel() {
        this.executeDetailsPanel = new FormPanel();
        this.executeDetailsPanel.setBorder(
            BorderFactory.createTitledBorder("Execute details")
        );
        return this.executeDetailsPanel;
    }

    protected JComponent createScriptsTree() {
        this.tree = new ScriptCategoriesTree( this.scriptsStore );
        JScrollPane pane = new JScrollPane( this.tree );
        return pane;
    }

    protected void onScriptDetails() {
        Object record = this.tree.getSelectedNode().getUserObject();
        if ( !( record instanceof Script ) ) {
            return;
        }

        ScriptDetailsWindow window = new ScriptDetailsWindow( (Script) record );
        window.setVisible(true);
    }

    protected void executeScript( Script script, Map<String, String> attributes ) {
        Dispatcher.get().forwardEvent( SystemComponent.Events.Action.ExecuteScript, script, attributes );
    }

    protected void onExecuteRequested() {
        Object object = this.tree.getSelectedNode().getUserObject();
        if ( !( object instanceof Script ) ) {
            return;
        }

        final Script scriptObject = (Script) object;
        if ( scriptObject.getParameters().size() == 0 || this.isConfigured ) {
            Map<String, String> values = new HashMap<String, String>();
            for ( String parameter : scriptObject.getParameters() ) {
                values.put(
                    parameter,
                    this.executeDetailsPanel.<String>getField(parameter).getValue() );
            }

            this.executeScript( scriptObject, values );
            this.isConfigured = false;
        } else {
            this.showExecuteDetailsPanel( (Script) object );
        }
    }

    protected void showExecuteDetailsPanel( Script script ) {
        this.executeDetailsPanel.removeFields();

        for ( String parameter : script.getParameters() ) {
            this.executeDetailsPanel.addField( parameter, StringUtils.ucfirst(parameter), new JTextField() );
        }

        this.executeDetailsPanel.setVisible(true);
        Dispatcher.get().forwardEvent( UIEvents.Core.Repaint, this.executeDetailsPanel );

        this.isConfigured = true;
    }

    protected JComponent createScriptsControlPane() {
        Box box = Box.createHorizontalBox();
        box.add( new JButton(
            new InteractionAction(
                "Execute",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ScriptingConsoleWindow.this.onExecuteRequested();
                    }
                }
            )
        ) );
        box.add( new JButton(
            new InteractionAction(
                "Details",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ScriptingConsoleWindow.this.onScriptDetails();
                    }
                }
            )
        ) );

        return box;
    }

    protected JComponent createConsolePane() {
        JPanel component = new JPanel();
		component.setSize(500, 400);
		component.setLayout( new BoxLayout(component, BoxLayout.Y_AXIS ) );

		component.add( new JLabel("API Script Console") );

		this.consoleAreaComponent = new JTextArea(10, 25);
		this.consoleAreaComponent.setEditable(false);
		JScrollPane consoleAreaWrapper = new JScrollPane(this.consoleAreaComponent);
		component.add( consoleAreaWrapper );

		JPanel commandLinePanel = new JPanel();
		commandLinePanel.add( this.commandLineComponent = new JTextField(45) );

		this.commandLineComponent.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				switch ( keyEvent.getKeyCode() ) {
					case KeyEvent.VK_ENTER:
						ScriptingConsoleWindow.this.onExecute();
					break;
					case KeyEvent.VK_UP:
						ScriptingConsoleWindow.this.onPrevRequest();
					break;
					case KeyEvent.VK_DOWN:
						ScriptingConsoleWindow.this.onNextRequest();
					break;
				}
			}
		});

		commandLinePanel.add( this.executeButton =  new JButton(
			new InteractionAction(
				"Execute",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
					 	ScriptingConsoleWindow.this.onExecute();
					}
				}
			)
		));

		commandLinePanel.add( new JButton(
			new InteractionAction(
				"Reset",
				new IEventHandler() {
					@Override
					public void handle( AppEvent event ) {
						try {
							ScriptingConsoleWindow.this.onReset();
						} catch ( EvaluationException e ) {
							ScriptingConsoleWindow.this.turnOff();
							ScriptingConsoleWindow.this.appendConsoleMessage( "Error: " + e.getMessage() );
						}
					}
				}
			)
		));

		component.add(commandLinePanel);
		return component;
    }

	protected void onReset() throws EvaluationException {
		int count = this.getEvaluator().getRootContext().getObjectsCount();
		this.appendConsoleMessage("Cleared " + count + " objects.");
		this.getEvaluator().reset();
	}

	protected void onPrevRequest() {
		if ( this.prev.isEmpty() ) {
			this.prev.addAll( this.next );
			this.next.clear();
		}

		String command = this.prev.poll();
		this.next.add( command );
		this.commandLineComponent.setText(command);

		Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.commandLineComponent);
	}

	protected void onNextRequest() {
		if ( this.next.isEmpty() ) {
			this.next.addAll( this.prev );
			this.prev.clear();
		}

		String command = this.next.poll();
		this.prev.add( command );

		this.commandLineComponent.setText(command);

		Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.commandLineComponent);
	}

	protected void onExecute() {
		if ( this.isTurnedOff() ) {
			return;
		}

		String eval = this.commandLineComponent.getText();
		this.appendConsoleMessage( eval );
		this.commandLineComponent.setText("");

		this.prev.add( eval );

		try {
			this.appendConsoleMessage( String.valueOf(this.getEvaluator().evaluate(eval)) );
		} catch ( Throwable e ) {
			this.appendConsoleMessage( "Error: " + e.getMessage() );
		}

		Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this.commandLineComponent);
	}

	protected void configUI() {
		this.setSize( 500, 400 );
		this.setTitle("Scripting console");
	}

}
