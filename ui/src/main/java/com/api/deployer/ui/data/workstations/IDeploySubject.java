package com.api.deployer.ui.data.workstations;

import com.redshape.ui.data.IModelData;

import java.util.UUID;

/**
 * @author nikelin
 * @date 15/04/11
 * @package com.api.deployer.ui.data.workstations
 */
public interface IDeploySubject extends IModelData {

	public UUID getId();

	public boolean isComposed();

	public IComposedDeploySubject asComposed();

	public String getName();

}
