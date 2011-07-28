package com.api.deployer.ui.data.scripts;

import com.redshape.ui.data.AbstractModelType;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.data.scripts
 */
public class ScriptModel extends AbstractModelType {
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CATEGORY = "category";
	public static final String DECLARATION = "declaration";
	public static final String PARAMETERS = "parameters";

	public ScriptModel() {
		super( Script.class );

	}

	@Override
	public Script createRecord() {
		return new Script();
	}

}
