package com.api.deployer.ui.data.help.categories;

import com.redshape.ui.data.stores.ListStore;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.data.help.categories
 */
public class HelpCategoriesStore extends ListStore<HelpContentCategory> {

	public HelpCategoriesStore() {
		super( new HelpContentCategoryModel() );
	}

}
