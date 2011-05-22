package com.api.deployer.expressions.language.impl;

import com.api.deployer.expressions.language.ILexer;
import com.api.deployer.expressions.language.ast.FunctionTreeNode;
import org.quartz.impl.matchers.StringMatcher;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 5/2/11
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionMatcher implements IMatcher<FunctionTreeNode> {
    public static String START_TOKEN = "(";
    public static String END_TOKEN = ")";

    @Override
    public int getLookAheadSize() {
        return 2;
    }

    @Override
    public boolean hasChildContext() {
        return true;
    }

    @Override
    public boolean matchStart( String value ) {
        return value.equals( START_TOKEN );
    }

    @Override
    public boolean matchEnd( String value ) {
        return value.equals( END_TOKEN );
    }
}
