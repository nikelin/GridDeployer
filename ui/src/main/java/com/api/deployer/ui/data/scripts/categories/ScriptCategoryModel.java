package com.api.deployer.ui.data.scripts.categories;

import com.redshape.ui.data.AbstractModelType;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.data.scripts.categories
 */
public class ScriptCategoryModel extends AbstractModelType {
	public static final String NAME = "name";
	public static final String PARENT = "parent";
	public static final String CHILDREN = "childs";
	public static final String SCRIPTS = "scripts";

	public ScriptCategoryModel() {
		super();
	}

	@Override
	public ScriptCategory createRecord() {
		return new ScriptCategory();
	}

}
