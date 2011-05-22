package com.api.deployer.expressions.language;

public class LexicalRule implements IRule<LexicalType> {
	private LexicalType from;
	private LexicalType to;
	
	public LexicalRule( LexicalType from, LexicalType to ) {
		this.from = from;
		this.to = to;
	}
	
	public LexicalType getFrom() {
		return this.from;
	}
	
	public LexicalType getTo() {
		return this.to;
	}
	
	@Override
	public String toString() {
		return this.from.toString() + "->" + this.to.toString();
	}

}
