package com.api.deployer.ui.components.help.data;

import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.components.help.data
 */
public class HelpDataLoader extends AbstractDataLoader {

	@Override
	protected Collection doLoad() throws LoaderException {
		return new HashSet();
	}
}
