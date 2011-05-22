package com.api.deployer.expressions.evaluation.functions.language;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.expressions.evaluation.functions.language
 */
public class NotDeclaredFunction extends Function<Object, Object> {
	private IEvaluator evaluator;

	public NotDeclaredFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Object invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 1 );
		this.assertArgumentType( arguments[0], String.class );

	   return this.evaluator.getRootContext().get( String.valueOf( arguments[0] ) );
	}

}
