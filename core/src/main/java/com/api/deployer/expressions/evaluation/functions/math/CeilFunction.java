package com.api.deployer.expressions.evaluation.functions.math;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.expressions.evaluation.functions.math
 */
public class CeilFunction extends Function<Object, Integer> {
    private IEvaluator evaluator;

    public CeilFunction( IEvaluator evaluator ) {
        this.evaluator = evaluator;
    }

    @Override
    public Integer invoke( Object... args ) {
        this.assertArgumentsCount( args, 1 );
        this.assertArgumentsType( args, Number.class );

        return ( (Number) args[0] ).intValue();
    }

}
