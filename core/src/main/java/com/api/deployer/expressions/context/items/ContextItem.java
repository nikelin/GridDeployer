package com.api.deployer.expressions.context.items;

import java.lang.reflect.Method;

import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.context.IEvaluationContext;
import com.api.deployer.expressions.context.IEvaluationContextItem;

public class ContextItem implements IEvaluationContextItem {
	private IEvaluationContext context;
	
	public ContextItem( IEvaluationContext context ) {
		this.context = context;
	}

	@Override
	public <V> V getValue(String name) throws EvaluationException {
		return (V) this.context.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue() throws EvaluationException {
		return (V) this.context;
	}

	@Override
	public Method getMethod(String name, int argumentsCount, Class<?>[] types ) throws EvaluationException {
		return this.context.resolveFunction( name, argumentsCount, types).toMethod();
	}
	
}
