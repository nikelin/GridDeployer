package com.api.deployer.expressions.language.ast;

import com.api.deployer.expressions.language.DeclarationType;

/**
 * @author nikelin
 * @date 19/04/11
 * @package com.api.deployer.expressions.language.ast
 */
public abstract class AbstractSyntaxTreeNode implements ISyntaxTreeNode {

	@Override
	public boolean isComposed() {
		return this instanceof IComposedSyntaxTreeNode;
	}

	@Override
	public boolean isList() {
		return this.isDeclaration()
				&& ( (DeclarationTreeNode) this ).getType().equals( DeclarationType.LIST );
	}

	@Override
	public boolean isMap() {
		return this.isDeclaration()
				&& ( (DeclarationTreeNode) this ).getType().equals( DeclarationType.MAP );
	}

	@Override
	public boolean isDeclaration() {
		return this instanceof DeclarationTreeNode;
	}

	@Override
	public IComposedSyntaxTreeNode asComposedNode() {
		if ( !this.isComposed() ) {
			throw new UnsupportedOperationException("There is no conversion to composed node");
		}

		return (IComposedSyntaxTreeNode) this;
	}
}
