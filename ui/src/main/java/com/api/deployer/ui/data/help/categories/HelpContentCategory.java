package com.api.deployer.ui.data.help.categories;

import com.api.deployer.ui.data.help.HelpContent;
import com.redshape.ui.data.AbstractModelData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.data.help.categories
 */
public class HelpContentCategory extends AbstractModelData {

	public HelpContentCategory() {
		super();

		this.set( HelpContentCategoryModel.CHILDREN, new ArrayList<HelpContentCategory>() );
		this.set( HelpContentCategoryModel.ITEMS, new ArrayList<HelpContent>() );
	}

	public List<HelpContentCategory> getChildren() {
		return this.get( HelpContentCategoryModel.CHILDREN );
	}

    public void addChild( HelpContentCategory child ) {
		this.getChildren().add( child );
	}

	public List<HelpContent> getItems() {
		return this.get( HelpContentCategoryModel.ITEMS );
	}

	public HelpContentCategory getParent() {
		return this.get( HelpContentCategoryModel.PARENT );
	}

	public void setParent( HelpContentCategory item ) {
		this.set( HelpContentCategoryModel.PARENT, item );
	}

	public void addItem( HelpContent item ) {
		this.getItems().add(item);
	}

	public void setName( String name ) {
		this.set( HelpContentCategoryModel.NAME, name );
	}

	public String getName() {
		return this.get( HelpContentCategoryModel.NAME );
	}

}
