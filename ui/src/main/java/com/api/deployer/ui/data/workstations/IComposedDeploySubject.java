package com.api.deployer.ui.data.workstations;

import java.util.Collection;

/**
 * @author nikelin
 * @date 15/04/11
 * @package com.api.deployer.ui.data.workstations
 */
public interface IComposedDeploySubject extends IDeploySubject {

	public <T extends IDeploySubject> Collection<T> getChildren();

}
