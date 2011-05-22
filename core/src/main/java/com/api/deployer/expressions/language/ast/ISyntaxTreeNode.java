package com.api.deployer.expressions.language.ast;

public interface ISyntaxTreeNode {

	public boolean isList();

	public boolean isMap();

	public boolean isDeclaration();

	public boolean isComposed();

	public IComposedSyntaxTreeNode asComposedNode();
	
}
