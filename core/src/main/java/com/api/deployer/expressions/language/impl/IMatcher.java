package com.api.deployer.expressions.language.impl;

import com.api.deployer.expressions.language.ILexer;
import com.api.deployer.expressions.language.ITokenizer;
import com.api.deployer.expressions.language.ast.ISyntaxTreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 5/2/11
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMatcher<T extends ISyntaxTreeNode> {

    public boolean hasChildContext();

    public int getLookAheadSize();

    public boolean matchStart( String value );

    public String matchName();

    public boolean matchEnd( String value );

}
