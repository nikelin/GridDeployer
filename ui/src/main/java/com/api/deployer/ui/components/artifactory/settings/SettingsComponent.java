package com.api.deployer.ui.components.artifactory.settings;

import com.api.deployer.ui.components.artifactory.ArtifactoryComponent;
import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsComponent extends AbstractComponent {

    public SettingsComponent() {
        super( "artifactory_settings", "Settings" );
    }

    @Override
    public void init() {
        this.addAction( new ComponentAction("Connection", this, ArtifactoryComponent.Events.Views.Settings.Connection ) );
        this.addAction( new ComponentAction("Environment", this, ArtifactoryComponent.Events.Views.Settings.Application ) );
    }
}
