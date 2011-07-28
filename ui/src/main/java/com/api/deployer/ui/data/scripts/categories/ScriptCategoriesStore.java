package com.api.deployer.ui.data.scripts.categories;

import com.redshape.ui.data.IModelType;
import com.redshape.ui.data.stores.ListStore;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.data.scripts.categories
 */
public class ScriptCategoriesStore extends ListStore<ScriptCategory> {

	public ScriptCategoriesStore() {
		super( new ScriptCategoryModel() );
	}
}
