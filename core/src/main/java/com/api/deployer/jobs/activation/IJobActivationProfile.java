package com.api.deployer.jobs.activation;

import java.io.Serializable;
import java.util.Map;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.jobs.activation
 */
public interface IJobActivationProfile extends Serializable {

	public <V> V getAttribute( ActivationAttribute name );

	public void setAttribute( ActivationAttribute name, Object value );

	public Map<ActivationAttribute, Object> getAttributes();

	public JobActivationType getActivationType();

}
