package com.api.deployer.expressions.language.ast;

public class FunctionScopeTreeNode extends AbstractSyntaxTreeNode
								   implements  IScopeTreeNode {

	@Override
	public boolean isComposed() {
		return false;
	}

	@Override
	public String toString() {
		return "{Function End}";
	}
	
}
