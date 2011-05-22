package com.api.deployer.expressions.evaluation.functions.arrays;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;
import com.sun.syndication.feed.impl.ObjectBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.arrays
 */
public class LengthFunction extends Function<Object, Integer> {

	private IEvaluator evaluator;

	public LengthFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Integer invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 1 );
		this.assertArgumentType( arguments[0], List.class );

		return ( (List) arguments[0] ).size();
	}
}
