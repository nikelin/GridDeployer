package com.api.deployer.ui.components.system.panels;

import com.api.deployer.ui.components.system.SystemComponent;
import com.api.deployer.ui.data.scripts.Script;
import com.api.deployer.ui.data.scripts.categories.ScriptCategory;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.AbstractDataTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author nikelin
 * @date 27/04/11
 * @package com.api.deployer.ui.components.system.panels
 */
public class ScriptCategoriesTree extends AbstractDataTree<ScriptCategory, IStore<ScriptCategory>> {

    public ScriptCategoriesTree( IStore<ScriptCategory> store ) {
        super(store);
    }

    @Override
    public DefaultMutableTreeNode addRecord(ScriptCategory record) {
        return this.addRecord(null, record);
    }

    public DefaultMutableTreeNode addRecord(ScriptCategory record, ScriptCategory child ) {
        DefaultMutableTreeNode parentNode = record == null ? this.getRoot() : this.findNode(record);
        if ( parentNode == null ) {
            throw new IllegalArgumentException("parent not exists");
        }

        DefaultMutableTreeNode node = this.findNode(parentNode, record);
        if ( node == null ) {
            this.insertNode( parentNode, node = this.createNode(child) );
        }

        for ( ScriptCategory childCategory : child.getChildren() ) {
            this.addRecord( child, childCategory );
        }

        for (Script script : child.getScripts() ) {
            this.insertNode( node, this.createNode(script) );
        }

        return node;
    }

    @Override
    public void removeRecord(ScriptCategory record) {
        DefaultMutableTreeNode node = this.findNode(record);
        if ( node == null ) {
            throw new IllegalArgumentException("record not exists");
        }

        this.removeNode(node);
    }

    @Override
    protected void invalidateRecord(ScriptCategory record) {
        DefaultMutableTreeNode node = this.findNode(record);
        if ( node == null ) {
            return;
        }

        for ( Script script : record.getScripts() ) {
            DefaultMutableTreeNode scriptNode = this.findNode( node, script );
            if ( scriptNode == null ) {
                this.insertNode( node, this.createNode(script) );
            }
        }

        for ( ScriptCategory child : record.getChildren() ) {
            DefaultMutableTreeNode childNode = this.findNode( node, child );
            if ( childNode == null ) {
                this.insertNode( node, this.createNode( child ) );
            }
        }
    }

    @Override
    protected JPopupMenu createContextMenu(DefaultMutableTreeNode path) {
        return new TreeContextMenu(path);
    }

    public static class TreeContextMenu extends JPopupMenu {
        private DefaultMutableTreeNode context;

        public TreeContextMenu( DefaultMutableTreeNode context ) {
            this.context = context;

            this.init();
        }

        protected void init() {
            if ( this.context.getUserObject() instanceof Script ) {
                this.initScriptContext();
            }
        }

        protected void initScriptContext() {
            this.add( new JMenuItem(
                new InteractionAction(
                    "Execute",
                    new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            Dispatcher.get().forwardEvent(
                                SystemComponent.Events.Action.ExecuteScript,
                                TreeContextMenu.this.context.getUserObject()
                            );
                        }
                    }
                )
            ) );
        }

    }
}
