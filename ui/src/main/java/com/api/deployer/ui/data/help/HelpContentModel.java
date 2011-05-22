package com.api.deployer.ui.data.help;

import com.redshape.ui.data.AbstractModelType;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.data.help
 */
public class HelpContentModel extends AbstractModelType {

	public static final String TITLE = "title";
	public static final String CONTENTS = "contents";
	public static final String CATEGORY = "category";

	public HelpContentModel() {
		super();
	}

	@Override
	public HelpContent createRecord() {
		return new HelpContent();
	}

}
