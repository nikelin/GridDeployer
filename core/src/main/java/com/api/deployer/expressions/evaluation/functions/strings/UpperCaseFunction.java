package com.api.deployer.expressions.evaluation.functions.strings;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.expressions.evaluation.functions.strings
 */
public class UpperCaseFunction extends Function<Object, String> {
    private IEvaluator evaluator;

    public UpperCaseFunction( IEvaluator evaluator ) {
        this.evaluator = evaluator;
    }

    @Override
    public String invoke( Object... args ) throws InvocationTargetException {
        this.assertArgumentsCount( args, 1 );
        this.assertArgumentsType( args, String.class );

        return ( (String) args[0] ).toUpperCase();
    }

}
