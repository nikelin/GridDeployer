package com.api.deployer.expressions.language;

public class GrammarRule implements IRule<TokenType> {
	private TokenType from;
	private TokenType to;
	
	public GrammarRule( TokenType from, TokenType to ) {
		this.from = from;
		this.to = to;
	}
	
	public TokenType getFrom() {
		return this.from;
	}
	
	public TokenType getTo() {
		return this.to;
	}
	
	@Override
	public String toString() {
		return this.from.toString() + "->" + this.to.toString();
	}
	
}
