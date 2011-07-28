package com.api.deployer.expressions.language.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.expressions.language.ast
 */
public abstract class AbstractComposedSyntaxTreeNode
			extends AbstractSyntaxTreeNode
			implements IComposedSyntaxTreeNode {
	private List<ISyntaxTreeNode> arguments = new ArrayList<ISyntaxTreeNode>();

	@Override
	public ISyntaxTreeNode shiftArgument() {
		ISyntaxTreeNode result = this.arguments.get(0);
		this.arguments = this.arguments.subList( 1, this.arguments.size() );
		return result;
	}

	@Override
	public void removeArgument( ISyntaxTreeNode node ) {
		this.arguments.remove(node);
	}

	@Override
	public void addArgument(ISyntaxTreeNode node) {
		this.arguments.add(node);
	}

	@Override
	public List<ISyntaxTreeNode> getArguments() {
		return this.arguments;
	}

}
