package com.api.deployer.expressions.evaluation.functions;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions
 */
public class BeginFunction extends Function<Object, Object> {

	private IEvaluator evaluator;

	public BeginFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Object invoke(Object... arguments) throws InvocationTargetException {
		return arguments.length != 0 ? arguments[arguments.length-1] : null;
	}

}