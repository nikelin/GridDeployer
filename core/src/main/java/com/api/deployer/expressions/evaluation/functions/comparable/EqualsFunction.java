package com.api.deployer.expressions.evaluation.functions.comparable;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.comparable
 */
public class EqualsFunction extends Function<Object, Boolean> {

	private IEvaluator evaluator;

	public EqualsFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Boolean invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 2 );

		return arguments[0].equals( arguments[1] );
	}
}
