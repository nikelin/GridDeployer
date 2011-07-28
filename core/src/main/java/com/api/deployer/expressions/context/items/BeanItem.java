package com.api.deployer.expressions.context.items;

import java.lang.reflect.Method;

import com.api.commons.beans.PropertyUtils;
import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.context.IEvaluationContextItem;
import com.redshape.utils.ReflectionUtils;

public class BeanItem implements IEvaluationContextItem {
	private Class<?> clazz;
	private Object instance;
	
	public BeanItem( Class<?> clazz, Object instance ) {
		this.clazz = clazz;
		this.instance = instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue(String name) throws EvaluationException {
		try {
			if ( name != null ) {
				return (V) PropertyUtils.getInstance().getProperty( this.clazz, name).get(this.instance);
			} else {
				return (V) this;
			}
		} catch ( Throwable e ) {
			throw new EvaluationException("Requested object or field is inaccessible");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue() {
		return (V) this.instance;
	}

	@Override
	public Method getMethod(String name, int argumentsCount, Class<?>[] types ) throws EvaluationException {
		try {
			for ( Method m : this.clazz.getMethods() ) {
				if ( m.getName().equals( name ) && m.getParameterTypes().length == argumentsCount
						&& ReflectionUtils.compareTypeLists( m.getParameterTypes(), types ) ) {
					return m;
				}
			}
			
			throw new EvaluationException("Requested method ( " + name + " " + argumentsCount + "-arged ) not found");
		} catch ( Throwable e ) {
			throw new EvaluationException( e.getMessage(), e );
		}
	}

}
