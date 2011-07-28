package com.api.deployer.ui.data.scripts;

import com.api.deployer.ui.data.scripts.categories.ScriptCategory;
import com.redshape.ui.data.AbstractModelData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.ui.data.scripts
 */
public class Script extends AbstractModelData {

	public Script() {
		super();

		this.set( ScriptModel.PARAMETERS, new HashSet<String>() );
	}

	public void setParameters( Collection<String> parameters ) {
		this.set( ScriptModel.PARAMETERS, parameters );
	}

	public Collection<String> getParameters() {
		return this.get( ScriptModel.PARAMETERS );
	}

	public void addParameter( String name ) {
		this.getParameters().add(name);
	}

	public String getName() {
		return this.get( ScriptModel.NAME );
	}

	public void setName( String name ) {
		this.set( ScriptModel.NAME, name );
	}

	public void setCategory( ScriptCategory category ) {
		this.set( ScriptModel.CATEGORY, category );
	}

	public ScriptCategory getCategory() {
		return this.get( ScriptModel.CATEGORY );
	}

	public void setDescription( String value ) {
		this.set( ScriptModel.DESCRIPTION, value );
	}

	public String getDescription() {
		return this.get( ScriptModel.DESCRIPTION );
	}

	public void setDeclaration( String value ) {
		this.set( ScriptModel.DECLARATION, value );
	}

	public String getDeclaration() {
		return this.get( ScriptModel.DECLARATION );
	}

    @Override
    public String toString() {
        return this.getName();
    }

}
