package com.api.deployer.expressions.evaluation.functions.arrays;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.util.List;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.arrays
 */
public class EmptyFunction extends Function<Object, Boolean> {

	private IEvaluator evaluator;

	public EmptyFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Boolean invoke( Object... args ) {
		this.assertArgumentsCount(args, 1);
		this.assertArgumentType(args[0], List.class);

		return ( (List) args[0] ).isEmpty();
	}

}
