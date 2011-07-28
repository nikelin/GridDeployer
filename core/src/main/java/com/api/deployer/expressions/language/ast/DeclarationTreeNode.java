package com.api.deployer.expressions.language.ast;

import com.api.deployer.expressions.language.DeclarationType;

public class DeclarationTreeNode extends AbstractComposedSyntaxTreeNode implements ISyntaxTreeNode {
	private static final Class<? extends IScopeTreeNode>[] RELATED
			= new Class[] { DeclarationScopeTreeNode.class, FunctionScopeTreeNode.class };
	private DeclarationType type;
	private String name;

	public DeclarationTreeNode( DeclarationType type ) {
		this.name = type.name();
		this.type = type;
	}

	@Override
	public Class<? extends IScopeTreeNode>[] getRelatedScopes() {
		return RELATED;
	}

	public String getName() {
		return this.name;
	}
	
	public DeclarationType getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return "{ Declaration type='" + this.getType() + "' Arguments:" + String.valueOf( this.getArguments() ) + " }";
	}
	
}
