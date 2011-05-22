package com.api.deployer.notifications;

import com.api.deployer.io.transport.IDestination;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public interface IEndPointHandler {

	public void onEndPoint( IDestination endPoint );

}
