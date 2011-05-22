package com.api.deployer.expressions.language.ast;


public class FunctionTreeNode extends AbstractComposedSyntaxTreeNode {
	private static final Class<? extends IScopeTreeNode>[] RELATED
				= new Class[] { FunctionScopeTreeNode.class };
	private String name;

	public FunctionTreeNode( String name ) {
		this.name = name;
	}

	@Override
	public Class<? extends IScopeTreeNode>[] getRelatedScopes() {
		return RELATED;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean isComposed() {
		return true;
	}

	@Override
	public String toString() {
		return "{ Function with name='" + this.getName() + "' Arguments:" + String.valueOf( this.getArguments() ) + " }";
	}
	
}
