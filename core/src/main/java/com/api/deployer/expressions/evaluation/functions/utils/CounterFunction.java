package com.api.deployer.expressions.evaluation.functions.utils;

import com.api.commons.Function;
import com.api.deployer.expressions.IEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nikelin
 * @date 25/04/11
 * @package com.api.deployer.expressions.evaluation.functions.utils
 */
public class CounterFunction extends Function<Object, Integer> {
    private IEvaluator evaluator;

    private static Map<String, Integer> counters = new HashMap<String, Integer>();
    private static Map<String, Integer> limits = new HashMap<String, Integer>();

    public CounterFunction( IEvaluator evaluator ) {
        this.evaluator = evaluator;
    }

    @Override
    public Integer invoke( Object... args ) {
        this.assertArgumentType( args[0], String.class );

        boolean created = false;

        final String counterName = (String) args[0];
        if ( !counters.containsKey(counterName) ) {
            counters.put( counterName, 0 );
            created = true;
        }

        final Integer limit;
        if ( args.length > 1 ) {
            this.assertArgumentType( args[1], Double.class );
            this.limits.put( counterName,  limit = ( (Double) args[1] ).intValue() );
        } else {
            limit = this.limits.get( counterName );
        }

        if ( created ) {
            return 0;
        }

        if ( limit != null && counters.get(counterName) >= limit ) {
            counters.remove(counterName);
            return limit;
        }

        Integer currentValue;
        counters.put( counterName, currentValue = counters.get(counterName) + 1 );

        return currentValue;
    }

}
