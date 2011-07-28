package com.api.deployer.expressions.language.ast;

public class LiteralTreeNode extends AbstractSyntaxTreeNode {
	private Object value;
	
	public LiteralTreeNode( Object value ) {
		this.value = value;
	}
	
	@Override
	public boolean isComposed() {
		return false;
	}

	public boolean isBoolean() {
		return this.value instanceof Boolean;
	}

	public boolean isNumeric() {
		return this.value instanceof Number;
	}

	public boolean isString() {
		return this.value instanceof Number;
	}

	public Object getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "{Literal: " + this.getValue() + "}";
	}
	
}
