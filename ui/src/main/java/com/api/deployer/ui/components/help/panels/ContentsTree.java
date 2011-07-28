package com.api.deployer.ui.components.help.panels;

import com.api.deployer.ui.data.help.categories.HelpCategoriesStore;
import com.api.deployer.ui.data.help.categories.HelpContentCategory;
import com.redshape.ui.data.adapters.swing.AbstractDataTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.components.help.panels
 */
public class ContentsTree extends AbstractDataTree<HelpContentCategory, HelpCategoriesStore> {

	public ContentsTree( HelpCategoriesStore store ) {
		super(store);
	}

	@Override
	public DefaultMutableTreeNode addRecord(HelpContentCategory record) {
        DefaultMutableTreeNode node;
		if ( null != ( node = this.findNode(record) ) ) {
			return node;
		}

		this.insertNode( node = this.createNode(record) );

        return node;
	}

	@Override
	public void removeRecord(HelpContentCategory record) {
		DefaultMutableTreeNode node = this.findNode(record);
		DefaultMutableTreeNode parent = node.getParent() == null
				? this.getRoot() : (DefaultMutableTreeNode) node.getParent();
		this.removeNode( parent, node );
	}

	@Override
	protected void invalidateRecord(HelpContentCategory record) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected JPopupMenu createContextMenu(DefaultMutableTreeNode path) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
