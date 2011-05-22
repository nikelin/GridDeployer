package com.api.deployer.expressions.evaluation.functions.logical;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.conditional
 */
public class NotFunction extends Function<Object, Boolean> {

	private IEvaluator evaluator;

	public NotFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Boolean invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 1 );
		this.assertArgumentType( arguments[0], Boolean.class );

		return ! ( (Boolean) arguments[0] );
	}

}
