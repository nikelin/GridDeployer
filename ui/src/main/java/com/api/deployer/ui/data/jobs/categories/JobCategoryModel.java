package com.api.deployer.ui.data.jobs.categories;

import com.redshape.ui.data.AbstractModelType;
import com.redshape.ui.data.IModelData;

public class JobCategoryModel extends AbstractModelType {
	public static final String NAME = "name";
	public static final String CHILDS = "childs";
	public static final String PARENT = "parent";
	public static final String JOBS = "jobs";
	
	public JobCategoryModel() {
		super( JobCategory.class );

		this.addField( JOBS );
		this.addField( NAME );
		this.addField( CHILDS );
		this.addField( PARENT );
	}
	
	@Override
	public IModelData createRecord() {
		return new JobCategory();
	}
	
}
