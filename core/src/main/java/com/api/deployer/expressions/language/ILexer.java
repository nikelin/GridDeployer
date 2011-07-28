package com.api.deployer.expressions.language;

import com.api.deployer.expressions.language.ast.ISyntaxTree;
import com.api.deployer.expressions.language.impl.LexerException;

public interface ILexer {

	public ISyntaxTree process() throws LexerException;
	
}
