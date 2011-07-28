package com.api.deployer.expressions.evaluation.functions.math;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions
 */
public class MinusFunction extends Function<Object, Double> {

	private IEvaluator evaluator;

	public MinusFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Double invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 2 );
		this.assertArgumentsType( arguments, Number.class );

		return (Double) arguments[0] - (Double) arguments[1];
	}

}
