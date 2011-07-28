package com.api.deployer.jobs.activation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.jobs.activation
 */
public class JobActivationProfile implements IJobActivationProfile {
	private Map<ActivationAttribute, Object> attributes = new HashMap<ActivationAttribute, Object>();
	private JobActivationType type;

	public JobActivationProfile() {
		this( JobActivationType.SINGLE );
	}

	public JobActivationProfile( JobActivationType type ) {
		this.type = type;
	}

	@Override
	public <V> V getAttribute( ActivationAttribute attribute ) {
		return (V) this.attributes.get(attribute);
	}

	@Override
	public void setAttribute( ActivationAttribute attribute, Object value ) {
		this.attributes.put( attribute, value );
	}

	@Override
	public Map<ActivationAttribute, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public JobActivationType getActivationType() {
		return this.type;
	}

    @Override
    public String toString() {
        return this.type.name();
    }
}
