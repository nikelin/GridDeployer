package com.api.deployer.expressions.evaluation.functions.math;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.math
 */
public class SumFunction extends Function<Object, Double> {

	private IEvaluator evaluator;

	public SumFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Double invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsType( arguments, Number.class );

		Double result = 0D;
		for ( Object arg : arguments ) {
			result += (Double) arg;
		}

		return result;
	}

}
