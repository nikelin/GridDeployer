package com.api.deployer.expressions.evaluation.functions.binary;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.binary
 */
public class LeftShiftFunction extends Function<Object, Integer> {

	private IEvaluator evaluator;

	public LeftShiftFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Integer invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 2 );
		this.assertArgumentType( arguments[0], Integer.class );
		this.assertArgumentType( arguments[1], Integer.class );

		return (Integer) arguments[0] << (Integer) arguments[1];
	}
}
