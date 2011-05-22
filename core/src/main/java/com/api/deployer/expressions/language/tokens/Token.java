package com.api.deployer.expressions.language.tokens;

import com.api.deployer.expressions.language.IToken;
import com.api.deployer.expressions.language.TokenType;

public class Token implements IToken {
	
	private TokenType type;
	private Object value;
	
	public Token( TokenType type ) {
		this(type, null);
	}
	
	public Token( TokenType type, Object value ) {
		this.type = type;
		this.value = value;
	}
	
	@Override
	public TokenType getType() {
		return this.type;
	}
	
	@Override
	public Object getValue() {
		return this.value;
	}

}
