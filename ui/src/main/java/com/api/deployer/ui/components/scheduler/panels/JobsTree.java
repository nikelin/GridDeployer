package com.api.deployer.ui.components.scheduler.panels;

import com.api.deployer.ui.components.scheduler.SchedulerComponent;
import com.api.deployer.ui.data.jobs.Job;
import com.api.deployer.ui.data.jobs.categories.JobCategory;
import com.api.deployer.ui.data.jobs.categories.JobCategoryStore;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.AbstractDataTree;
import com.redshape.utils.IFilter;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Collection;

/**
 * @author nikelin
 * @date 12/04/11
 * @package com.api.deployer.ui.components.scheduler.panels
 */
public class JobsTree extends AbstractDataTree<JobCategory, JobCategoryStore> {
	private static final Logger log = Logger.getLogger(JobsTree.class);

	public JobsTree( JobCategoryStore store ) {
		super( store );

		this.configUI();
	}

	@Override
	protected void init() {
		super.init();
	}



	@Override
	protected JPopupMenu createContextMenu(DefaultMutableTreeNode path) {
		return new TreeContextMenu( this, path );
	}

	protected void invalidateRecord( JobCategory category ) {
		DefaultMutableTreeNode node = this.findNode(category);
		if ( node == null ) {
			this.addRecord(category);
		}

		for ( Job job : category.getJobs() ) {
			DefaultMutableTreeNode jobNode = this.findNode( node, job );
			if ( jobNode != null ) {
				continue;
			}

			this.insertNode( node, jobNode = this.createNode(job) );

			if ( job.getJob() != null ) {
				this.insertNode( jobNode, this.createNode( job.getJob() ) );
			}
		}
	}

	public Collection<Job> getJobs() {
		return this.collect( new IFilter<DefaultMutableTreeNode>() {
				@Override
				public boolean filter( DefaultMutableTreeNode node ) {
					if ( node.getUserObject() instanceof Job ) {
						return true;
					}

					return false;
				}
			}
		);
	}

    @Override
	public DefaultMutableTreeNode addRecord( JobCategory category ) {
		DefaultMutableTreeNode categoryNode;
        if ( null != ( categoryNode = this.findNode(category ) ) ) {
            return categoryNode;
        }

		this.insertNode(categoryNode = this.createNode(category));

		this.processNode( categoryNode, category );

        return categoryNode;
	}

	public void addJobRecord( Job job ) {
		DefaultMutableTreeNode categoryNode = this.findNode(job.getCategory());
		if ( categoryNode == null ) {
			this.insertNode( categoryNode = this.createNode( job.getCategory() ) );
		}

		DefaultMutableTreeNode jobNode = this.createNode( job );
		this.insertNode( categoryNode, jobNode );

		if ( job.getJob() != null ) {
			this.insertNode( jobNode, this.createNode( job.getJob() ) );
		}
	}

	protected void processNode( DefaultMutableTreeNode categoryNode, JobCategory category ) {
		for ( JobCategory childCategory : category.getChilds() ) {
			DefaultMutableTreeNode childCategoryNode;
			this.insertNode( categoryNode, childCategoryNode = this.createNode( childCategory ) );
			this.processNode( childCategoryNode, childCategory );
		}

		for ( Job job : category.getJobs() ) {
			this.insertNode( categoryNode, this.createNode(job) );
		}
	}


	public void removeRecord( JobCategory category ) {
		this.removeNode( this.findNode(category) );
	}

	protected void configUI() {
		this.setPreferredSize( new Dimension(250, 120) );
	}

	public class TreeContextMenu extends JPopupMenu {
		private static final long serialVersionUID = 7672229708189643272L;

		private DefaultMutableTreeNode nodeContext;
		private JobsTree context;

		public TreeContextMenu( JobsTree context, DefaultMutableTreeNode nodeContext ) {
			super();

			this.context = context;
			this.nodeContext = nodeContext;

			this.init();
		}

		protected void init() {
			if ( this.nodeContext == null ) {
				this.addGlobalItems();
			} else {
				this.addNodeItems();
			}
		}

		private void addNodeItems() {
            if ( this.nodeContext.getUserObject() instanceof Job ) {
                this.add( new JMenuItem(
                   new InteractionAction(
                       "Description",
                       new IEventHandler() {
                           @Override
                           public void handle(AppEvent event) {
                               Dispatcher.get().forwardEvent(
                                   SchedulerComponent.Events.Views.Job.Details,
                                   (Job) TreeContextMenu.this.nodeContext.getUserObject() );
                           }
                       }
                   )
                ) );
            }

			this.add( new JMenuItem(
                new InteractionAction(
                    "Remove",
                    new IEventHandler() {
                        @Override
                        public void handle(AppEvent event) {
                            TreeContextMenu.this.context.removeNode(
                                TreeContextMenu.this.nodeContext
                            );
                        }
                    }
                )
			) );
		}

		private void addGlobalItems() {
			this.add( new JMenuItem(
				new InteractionAction(
					"Create job",
					SchedulerComponent.Events.Views.Job.New
				)
			) );
		}
	}

}
