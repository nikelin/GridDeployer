package com.api.deployer.expressions.evaluation.functions.arrays;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.arrays
 */
public class CARFunction extends Function<Object, Object> {
	private IEvaluator evaluator;

	public CARFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Object invoke( Object... args ) throws InvocationTargetException {
		try {
			this.assertArgumentsCount( args, 1 );
			this.assertArgumentType( args[0], List.class );

			return ( (List) args[0] ).get(0);
		} catch ( Throwable e ) {
			throw new InvocationTargetException( e );
		}
	}

}
