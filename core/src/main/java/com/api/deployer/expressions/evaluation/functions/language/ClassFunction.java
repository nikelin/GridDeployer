package com.api.deployer.expressions.evaluation.functions.language;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.expressions.evaluation.functions.language
 */
public class ClassFunction extends Function<Object, Class<?>> {
    private IEvaluator evaluator;

    public ClassFunction( IEvaluator evaluator ) {
        this.evaluator = evaluator;
    }

    @Override
    public Class<?> invoke( Object... args ) throws InvocationTargetException {
        this.assertArgumentsCount( args, 1 );
        this.assertArgumentsType( args, String.class );

        try {
            return Class.forName( String.valueOf( args[0] ) );
        } catch ( Throwable e ) {
            throw new InvocationTargetException( e );
        }
    }

}
