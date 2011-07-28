package com.api.deployer.expressions.evaluation.functions.system;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.expressions.evaluation.functions.system
 */
public class RootPathFunction extends Function<Object, Object> {
	private IEvaluator evaluator;

	public RootPathFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Object invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 1 );
		this.assertArgumentType( arguments[0], String.class );

		this.evaluator.getLoader().setRootDirectory( String.valueOf( arguments[0] ) );

		return null;
	}

}

