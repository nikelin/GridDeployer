package com.api.deployer.expressions.evaluation.functions.strings;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.expressions.evaluation.functions.strings
 */
public class ConcatFunction extends Function {
    private IEvaluator evaluator;

    public ConcatFunction( IEvaluator evaluator ) {
        this.evaluator = evaluator;
    }

    @Override
    public String invoke( Object... args ) {
        this.assertArgumentsType( args, String.class );

        StringBuilder result = new StringBuilder();
        for ( Object arg : args ) {
            result.append( String.valueOf( arg ) );
        }

        return result.toString();
    }

}
