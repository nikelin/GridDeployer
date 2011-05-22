package com.api.deployer.expressions.evaluation.functions.comparable;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.conditional
 */
public class GreatThanFunction extends Function<Object, Boolean> {

	private IEvaluator evaluator;

	public GreatThanFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Boolean invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsType( arguments, Comparable.class );
		this.assertArgumentsCount( arguments, 2 );

		return 1 == ( ( (Comparable) arguments[0] ).compareTo( (Comparable) arguments[1] ) );
	}

}