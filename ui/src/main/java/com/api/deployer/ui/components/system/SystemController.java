package com.api.deployer.ui.components.system;

import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.IEvaluator;
import com.api.deployer.ui.components.system.windows.ScriptingConsoleWindow;
import com.api.deployer.ui.data.scripts.Script;
import com.redshape.ui.AbstractController;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.UnhandledUIException;
import com.redshape.ui.annotations.Action;
import com.redshape.ui.events.AppEvent;
import com.redshape.ui.state.UIStateEvent;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.ui.windows.swing.ISwingWindowsManager;

import javax.swing.*;
import java.util.Map;

/**
 * @author root
 * @date 07/04/11
 * @package com.api.deployer.ui.components.system
 */
public class SystemController extends AbstractController {
	@Override
	protected void initEvents() {
        this.registerEvent( SystemComponent.Events.Action.ExecuteScript );
		this.registerEvent( UIStateEvent.Result.Restored );
		this.registerEvent( UIStateEvent.Result.Saved );
		this.registerEvent( SystemComponent.Events.Views.Console );
	}

	@Override
	protected void initViews() {
	}

    @Action( eventType = "SystemComponent.Events.Action.ExecuteScript")
    public void executeScriptAction( AppEvent event ) {
        IEvaluator evaluator = UIRegistry.getContext().getBean( IEvaluator.class );
        if ( evaluator == null ) {
            UIRegistry.getNotificationsManager().warn("Script evaluator not initialized!");
            return;
        }

        Script script = event.getArg(0);
        if ( script == null ) {
            return;
        }

        Map<String, String> attributes = event.getArg(1);

        String declaration = script.getDeclaration();
        for ( String key : attributes.keySet() ) {
            declaration = declaration.replace("${" + key + "}", attributes.get(key) );
        }

        try {
            evaluator.evaluate( declaration );
        } catch ( EvaluationException e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }

	@Action( eventType = "SystemComponent.Events.Views.Console" )
	public void scriptingConsoleView( AppEvent event ) {
		UIRegistry.<ISwingWindowsManager>getWindowsManager().open(ScriptingConsoleWindow.class);
	}

	@Action( eventType = "UIStateEvent.Result.Restored")
	public void afterRestoreAction( AppEvent event ) {
		JOptionPane.showMessageDialog(UIRegistry.<JFrame>getRootContext(), "State successfully " +
				"restored to revision " + event.getArg(0) + "!");
	}

	@Action( eventType = "UIStateEvent.Result.Saved" )
	public void afterSaveAction( AppEvent event ) {
		JOptionPane.showMessageDialog(UIRegistry.<JFrame>getRootContext(), "State successfully saved!");
	}
}
