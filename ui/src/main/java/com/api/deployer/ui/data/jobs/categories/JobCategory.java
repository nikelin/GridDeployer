package com.api.deployer.ui.data.jobs.categories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.api.deployer.ui.data.jobs.Job;
import com.redshape.ui.data.AbstractModelData;

public class JobCategory extends AbstractModelData {

	public JobCategory() {
		super();
		
		this.set( JobCategoryModel.CHILDS, new ArrayList<JobCategory>() );
		this.set( JobCategoryModel.JOBS, new ArrayList<JobCategory>() );
	}

	public List<Job> getJobs() {
		return this.get( JobCategoryModel.JOBS );
	}

	public void addJob( Job job ) {
		job.setCategory(this);
		this.<List<Job>>get( JobCategoryModel.JOBS ).add( job );
	}

	public void setJobs( List<Job> jobs ) {
		for ( Job job : jobs ) {
			this.addJob(job);
		}
	}
	
	public void setName( String name ) {
		this.set( JobCategoryModel.NAME, name );
	}
	
	public String getName() {
		return this.get( JobCategoryModel.NAME );
	}
	
	protected void setParent( JobCategory parent ) {
		this.set( JobCategoryModel.PARENT, parent );
	}

	public void setChilds( List<JobCategory> childs ) {
		this.set( JobCategoryModel.CHILDS, childs );
	}
	
	public void addChild( JobCategory child ) {
		this.<List<JobCategory>>get( JobCategoryModel.CHILDS ).add( child );
		child.setParent( this );
	}
	
	public List<JobCategory> getChilds() {
		return this.get( JobCategoryModel.CHILDS );
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
}
