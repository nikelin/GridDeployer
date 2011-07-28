package com.api.deployer.expressions.evaluation.functions.system;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.expressions.evaluation.functions.system
 */
public class SearchPathFunction extends Function<Object, Object> {

	private IEvaluator evaluator;

	public SearchPathFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Object invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount(arguments, 1);

		Object argument = arguments[0];
		if ( argument instanceof Collection) {
			for ( Object path : (Collection) argument ) {
				this.evaluator.getLoader().addSearchPath( String.valueOf( path ) );
			}
		} else {
			this.evaluator.getLoader().addSearchPath( String.valueOf( arguments[0] ) );
		}

		return null;
	}

}

