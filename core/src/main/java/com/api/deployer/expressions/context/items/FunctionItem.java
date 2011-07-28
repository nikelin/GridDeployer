package com.api.deployer.expressions.context.items;

import java.lang.reflect.Method;

import com.api.commons.IFunction;
import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.context.IEvaluationContextItem;

public class FunctionItem implements IEvaluationContextItem {
	private IFunction<?,?> function;
	
	public FunctionItem( IFunction<?,?> function ) {
		this.function = function;
	}

	@Override
	public Method getMethod(String name, int argumentsCount, Class<?>[] types )
			throws EvaluationException {
		return this.function.toMethod();
	}

	@Override
	public <V> V getValue(String name) throws EvaluationException {
		throw new EvaluationException("Method not supported on function item");
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue() throws EvaluationException {
		return (V) this.function;
	}
	
}
