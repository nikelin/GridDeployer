package com.api.deployer.ui.data.scripts.categories;

import com.api.deployer.ui.data.scripts.Script;
import com.redshape.ui.data.AbstractModelData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.data.scripts.categories
 */
public class ScriptCategory extends AbstractModelData {

	public ScriptCategory() {
		super();

		this.set( ScriptCategoryModel.CHILDREN, new ArrayList<ScriptCategory>() );
        this.set( ScriptCategoryModel.SCRIPTS, new ArrayList<ScriptCategory>() );
	}

	public void setParent( ScriptCategory parent ) {
		this.set( ScriptCategoryModel.PARENT, parent );
	}

	public ScriptCategory getParent() {
		return this.get( ScriptCategoryModel.PARENT );
	}

	public void setScripts( List<Script> scripts ) {
		for ( Script script : scripts ) {
			this.addScript(script);
		}
	}

	public List<Script> getScripts() {
		return this.get( ScriptCategoryModel.SCRIPTS );
	}

	public void addScript( Script script ) {
		script.setCategory(this);
		this.getScripts().add( script );
	}

	public List<ScriptCategory> getChildren() {
		return this.get( ScriptCategoryModel.CHILDREN );
	}

	public void setChildren( List<ScriptCategory> children ) {
		for ( ScriptCategory category : children ) {
			this.addChild( category );
		}
	}

	public void addChild( ScriptCategory category ) {
		category.setParent(this);
		this.getChildren().add( category );
	}

	public void setName( String name ) {
		this.set( ScriptCategoryModel.NAME, name );
	}

	public String getName() {
		return this.get( ScriptCategoryModel.NAME );
	}

    @Override
    public String toString() {
        return this.getName();
    }

}
