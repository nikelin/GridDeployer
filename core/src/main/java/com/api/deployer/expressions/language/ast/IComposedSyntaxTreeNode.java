package com.api.deployer.expressions.language.ast;

import java.util.Collection;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.expressions.language.ast
 */
public interface IComposedSyntaxTreeNode extends ISyntaxTreeNode {

	public Class<? extends IScopeTreeNode>[] getRelatedScopes();

	public void addArgument( ISyntaxTreeNode node );

	public void removeArgument( ISyntaxTreeNode node );

	public ISyntaxTreeNode shiftArgument();

	public Collection<ISyntaxTreeNode> getArguments();

}
