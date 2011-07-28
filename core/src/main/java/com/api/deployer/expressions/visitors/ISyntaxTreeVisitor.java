package com.api.deployer.expressions.visitors;

import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.IEvaluator;
import com.api.deployer.expressions.language.ast.*;

public interface ISyntaxTreeVisitor {

	public void setEvaluatorContext( IEvaluator evaluator );

	public <T> T visit( ISyntaxTree tree ) throws EvaluationException;

	public <T> T visit( ScopeTreeNode node ) throws EvaluationException;
	
	public <T> T visit( FunctionTreeNode node ) throws EvaluationException;
	
	public <T> T visit( DeclarationTreeNode node ) throws EvaluationException;
	
	public Object visit( VariableTreeNode node ) throws EvaluationException;
	
	public Object visit( LiteralTreeNode node ) throws EvaluationException;
	
}
