package com.api.deployer.expressions.context;

import java.lang.reflect.Method;

import com.api.deployer.expressions.EvaluationException;

public interface IEvaluationContextItem {

	public Method getMethod( String name, int argumentsCount, Class<?>[] types ) throws EvaluationException ;
	
	public <V> V getValue(String name) throws EvaluationException;
	
	public <V> V getValue() throws EvaluationException;
	
}
