package com.api.deployer.expressions.evaluation.functions.binary;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.binary
 */
public class BinaryNotFunction extends Function<Object, Integer> {

	private IEvaluator evaluator;

	public BinaryNotFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	public Integer invoke( Object... args ) {
		this.assertArgumentsCount( args, 1 );
		this.assertArgumentType( args[0], Integer.class );

		return ~ ( (Integer) args[1] );
	}

}
