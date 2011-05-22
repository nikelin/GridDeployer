package com.api.deployer.expressions.context.items;

import com.api.commons.ReflectionUtils;
import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.context.IEvaluationContextItem;

import java.lang.reflect.Method;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.context.items
 */
public class ClassContext implements IEvaluationContextItem {
	private Class<?> clazz;

	public ClassContext( Class<?> clazz ) {
		this.clazz = clazz;
	}

	@Override
	public Method getMethod(String name, int argumentsCount, Class<?>[] types ) throws EvaluationException {
		for ( Method method : this.clazz.getMethods() ) {
			if ( method.getName().equals(name) && method.getParameterTypes().length == argumentsCount
					&& ReflectionUtils.compareTypeLists( method.getParameterTypes(), types ) ) {
				return method;
			}
		}

		return null;
	}

	@Override
	public <V> V getValue(String name) throws EvaluationException {
		try {
			return (V) this.clazz.getField(name).get( this.clazz );
		} catch ( Throwable e ) {
			throw new EvaluationException( e.getMessage(), e );
		}
	}

	@Override
	public <V> V getValue() throws EvaluationException {
		return (V) this.clazz;
	}
}
