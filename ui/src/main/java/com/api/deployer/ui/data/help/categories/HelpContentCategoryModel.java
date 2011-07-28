package com.api.deployer.ui.data.help.categories;

import com.redshape.ui.data.AbstractModelType;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.data.help.categories
 */
public class HelpContentCategoryModel extends AbstractModelType {
	public static final String NAME = "name";
	public static final String ITEMS = "items";
	public static final String CHILDREN = "childs";
	public static final String PARENT = "parent";

	public HelpContentCategoryModel() {
		super( HelpContentCategory.class );

		this.addField(NAME);
	}

	@Override
	public HelpContentCategory createRecord() {
		return new HelpContentCategory();
	}

}
