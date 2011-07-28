package com.api.deployer.expressions.context.items;

import java.lang.reflect.Method;
import java.util.Map;

import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.context.IEvaluationContextItem;

public class MapItem implements IEvaluationContextItem {
	private Map<String,?> map;
	
	public MapItem( Map<String,?> map ) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue(String name) throws EvaluationException {
		return (V) this.map.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue() throws EvaluationException {
		return (V) this.map;
	}
	
	@Override
	public Method getMethod( String name, int argumentsCount, Class<?>[] types ) throws EvaluationException {
		throw new IllegalArgumentException("Restricted operation on map");
	}
	
}
