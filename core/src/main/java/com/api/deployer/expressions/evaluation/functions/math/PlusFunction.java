package com.api.deployer.expressions.evaluation.functions.math;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions
 */
public class PlusFunction extends Function<Object, Double> {

	private IEvaluator evaluator;

	public PlusFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Double invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 2 );
		this.assertArgumentsType( arguments, Number.class );

        return this.convertValue( arguments[0] ) + this.convertValue ( arguments[1] );
	}

    protected Double convertValue( Object value ) {
        return ( (Number) value ).doubleValue();
    }

}
