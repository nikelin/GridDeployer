package com.api.deployer.ui.components.security;

import com.redshape.ui.components.AbstractComponent;
import com.redshape.ui.components.actions.ComponentAction;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.ui.components.security
 */
public class SecurityComponent extends AbstractComponent {

	public SecurityComponent() {
		super("security", "Security");
	}

	@Override
	public void init() {
		this.addAction( new ComponentAction("Users", this ) );
		this.addAction( new ComponentAction("Permissions", this ) );
	}
}
