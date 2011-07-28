package com.api.deployer.ui.data.help;

import com.api.deployer.ui.data.help.categories.HelpContentCategory;
import com.redshape.ui.data.AbstractModelData;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.data.help
 */
public class HelpContent extends AbstractModelData {

	public HelpContent() {
		super();
	}

	public String getTitle() {
		return this.get( HelpContentModel.TITLE );
	}

	public void setTitle( String title ) {
		this.set( HelpContentModel.TITLE, title );
	}

	public void setContents( String contents ) {
		this.set( HelpContentModel.CONTENTS, contents );
	}

	public String getContents() {
		return this.get( HelpContentModel.CONTENTS );
	}

	public HelpContentCategory getCategory() {
		return this.get( HelpContentModel.CATEGORY );
	}

	public void setCategory( HelpContentCategory category ) {
		this.set( HelpContentModel.CATEGORY, category );
	}

}
