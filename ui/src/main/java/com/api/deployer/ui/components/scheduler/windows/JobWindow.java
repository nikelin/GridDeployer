package com.api.deployer.ui.components.scheduler.windows;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.INetworkDevice;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.ui.components.artifactory.data.Artifact;
import com.api.deployer.ui.components.artifactory.data.ArtifactsStore;
import com.api.deployer.ui.components.artifactory.data.loaders.ArtifactsLoader;
import com.api.deployer.ui.components.scheduler.SchedulerComponent;
import com.api.deployer.ui.components.scheduler.panels.JobsTree;
import com.api.deployer.ui.data.devices.network.NetworkDevice;
import com.api.deployer.ui.data.devices.network.NetworkDevicesStore;
import com.api.deployer.ui.data.devices.storage.StorageDevice;
import com.api.deployer.ui.data.devices.storage.StorageDevicesStore;
import com.api.deployer.ui.data.devices.storage.partitions.StoragePartition;
import com.api.deployer.ui.data.devices.storage.partitions.StoragePartitionsStore;
import com.api.deployer.ui.data.jobs.Job;
import com.api.deployer.ui.data.jobs.categories.JobCategoryStore;
import com.api.deployer.ui.data.workstations.IDeploySubject;
import com.api.deployer.ui.data.workstations.StationsStore;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.IEvaluator;
import com.redshape.daemon.jobs.IJob;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UIException;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.IStore;
import com.redshape.ui.data.adapters.swing.ComboBoxAdapter;
import com.redshape.ui.data.bindings.render.ISwingRenderer;
import com.redshape.ui.data.bindings.render.components.ObjectUI;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.IDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.data.providers.IProvidersFactory;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import com.redshape.utils.Function;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author nikelin
 * @date 12/04/11
 * @package com.api.deployer.ui.components.scheduler.windows
 */
public class JobWindow extends JFrame {
	private static final Logger log = Logger.getLogger(JobWindow.class);

	private JComboBox targetSelector;
	private JComponent body;
	private JobsTree jobsTree;
	private ObjectUI jobUI;
	private Job currentJob;
	private FormPanel targetsForm;
	private IDataLoader<StorageDevice> loader;
    private IEvaluator evaluator;

	public JobWindow() {
		super();
		this.buildUI();
		this.configUI();

        this.evaluator = UIRegistry.getContext().getBean( IEvaluator.class );
	}

    protected IEvaluator getEvaluator() {
        return this.evaluator;
    }

	protected JobCategoryStore getJobsStore() throws UIException {
		try {
			try {
				return UIRegistry.getContext().getBean( JobCategoryStore.class );
			} catch ( Throwable e ) {
				return UIRegistry.getStoresManager().getStore( JobCategoryStore.class );
			}
		} catch ( InstantiationException e ) {
			throw new UIException("Store creation exception", e);
		}
	}

    protected void reset() {
        try {
            this.body.removeAll();
            this.currentJob = null;
            this.evaluator.reset();
        } catch ( Throwable e ) {
            throw new UnhandledUIException( e.getMessage(), e );
        }
    }

	protected void buildUI() {
		try {
			this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
			this.add(this.createTitle());

			JPanel selectPanel = new JPanel();
			selectPanel.setLayout( new BoxLayout(selectPanel, BoxLayout.Y_AXIS ) );
			selectPanel.add( this.createJobSelectPanel() );
			selectPanel.setMinimumSize( new Dimension( 500, 100 ) );
			this.add( selectPanel );
			this.add( this.createTargetsPanel() );
			this.add( new JScrollPane( this.createBody() ) );

			this.add(this.createButtonsPanel() );

		} catch ( Throwable e ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
	}

    protected void onCreate() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        this.jobsTree.getSelectionPath().getLastPathComponent();
        if (!(node.getUserObject() instanceof Job)) {
            return;
        }

        if ( null != this.currentJob && this.currentJob.equals( node.getUserObject() ) ) {
            return;
        }

        this.currentJob = (Job) node.getUserObject();

        try {
            this.displayJobConfiguration((Job) node.getUserObject());
        } catch (UIException e) {
            throw new UnhandledUIException(e.getMessage(), e);
        }
    }

    protected void onFinished() {
        try {
            if (JobWindow.this.jobUI == null) {
                UIRegistry.getNotificationsManager().error("First select and " +
                        "configure job you want to schedule.");
                return;
            }

            final IDeploySubject target = (IDeploySubject) JobWindow.this.targetSelector.getSelectedItem();
            if ( target == null ) {
                UIRegistry.getNotificationsManager().error("Select target to process job!");
                return;
            }

            final Job job = JobWindow.this.currentJob;
            job.setJob(JobWindow.this.jobUI.<IJob>createInstance());
            job.setTarget( target );

            Dispatcher.get().forwardEvent(
                    SchedulerComponent.Events.Views.Job.Added,
                    JobWindow.this.currentJob);

            UIRegistry.getNotificationsManager().ask(
                "Do you want to schedule another job?",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        JobWindow.this.reset();
                    }
                },
                new WindowCloseHandler(this) );
        } catch (Throwable e) {
            throw new UnhandledUIException(e.getMessage(), e);
        }
    }

	protected JComponent createButtonsPanel() {
		Box box = Box.createHorizontalBox();

		box.add(new JButton(
			new InteractionAction(
				"Create",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						JobWindow.this.onCreate();
					}
				}
			)
		));

		box.add(new JButton(
			new InteractionAction(
				"Finish",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						JobWindow.this.onFinished();
					}
				}
			)
		));

		box.add( new JButton(
			new InteractionAction(
				"Cancel",
				new WindowCloseHandler(this)
			)
		) );

		return box;
	}

	protected JComponent createTitle() {
		JPanel panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder("New job configuration") );
		return panel;
	}

	protected JComponent createJobSelectPanel() throws UIException {
		JPanel panel = new JPanel();
        panel.setBorder( BorderFactory.createTitledBorder("Select job type to create:") );
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );

        JScrollPane pane = new JScrollPane( this.jobsTree = new JobsTree( this.getJobsStore() ) );
        pane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        pane.setPreferredSize( new Dimension( 500, 250 ) );
		panel.add( pane);

        this.jobsTree.addTreeSelectionListener( new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                JobWindow.this.onCreate();
            }
        });

		return panel;
	}

	protected JComponent createBody() {
		this.body = new JPanel();
		this.body.setLayout(new BoxLayout(this.body, BoxLayout.Y_AXIS));
		this.body.setMinimumSize(new Dimension(500, 100));
		return this.body;
	}

	protected JComponent createTargetsPanel() throws UIException {
		this.targetsForm = new FormPanel();
		this.targetsForm.addField("target", "Target", this.createTargetSelector());
		return this.targetsForm;
	}

	protected JComboBox createTargetSelector() throws UIException {
		try {
			this.targetSelector = new ComboBoxAdapter( UIRegistry.getStoresManager().getStore(StationsStore.class) );
            if ( targetSelector.getItemCount() == 0 ) {
                this.targetSelector.setEnabled(false);
            }

			this.targetSelector.setRenderer( new ListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
					return new JLabel( String.valueOf( o ) );
				}
			});

			this.targetSelector.addItemListener( new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent itemEvent) {
                    try {
                        JobWindow.this.onTargetSelected( (Workstation) itemEvent.getItem() );
                    } catch ( LoaderException e ) {
                        throw new UnhandledUIException( e.getMessage(), e );
                    }
				}
			});

			return this.targetSelector;
		} catch ( InstantiationException e ) {
			throw new UIException( e.getMessage(), e );
		}
	}

    protected void onTargetSelected( Workstation station ) throws LoaderException {
        if ( this.loader == null ) {
            this.loader = this.createStorageDevicesLoader();
        }

        this.loader.load();
    }

    protected IDataLoader<NetworkDevice> createNetworkDevicesLoader() {
        return new AbstractDataLoader<NetworkDevice>() {
            @Override
            protected Collection<NetworkDevice> doLoad() throws LoaderException {
                Collection<NetworkDevice> devices = new HashSet<NetworkDevice>();

                Workstation station = (Workstation) JobWindow.this.targetSelector.getSelectedItem();
                for ( IDevice device : station.getDevices() ) {
                    final INetworkDevice networkDevice = (INetworkDevice) device;
                    if ( device instanceof INetworkDevice ) {
                        NetworkDevice deviceData = new NetworkDevice();
                        deviceData.setAddress( networkDevice.getAddress() );
                        deviceData.setGateway( networkDevice.getGateway() );
                        deviceData.setBroadcast( networkDevice.getBroadcast() );
                        deviceData.setNetmask( networkDevice.getNetmask() );
                        deviceData.setRelatedObject( networkDevice );
                        devices.add( deviceData );
                    }
                }

                return devices;
            }
        };
    }

    protected IDataLoader<Artifact> createArtifactsLoader() {
        return new RefreshPolicy( new ArtifactsLoader(), 4000 );
    }

	protected IDataLoader<StorageDevice> createStorageDevicesLoader() {
		return this.loader = new AbstractDataLoader<StorageDevice>() {
			@Override
			protected Collection<StorageDevice> doLoad() throws LoaderException {
				Collection<StorageDevice> devices = new HashSet<StorageDevice>();

				Collection<IDevice> deviceObjects = ( (Workstation) JobWindow.this.targetSelector.getSelectedItem() ).getDevices();
				for ( IDevice deviceObject : deviceObjects ) {
					StorageDevice device = new StorageDevice(deviceObject);
					device.setPath( deviceObject.getPath() );
					device.setUUID( deviceObject.getUUID() );
					devices.add(device);
				}

				return devices;
			}
		};
	}

    protected IDataLoader<StoragePartition> createStoragePartitionsLoader( final IProvidersFactory factory ) {
        return new AbstractDataLoader<StoragePartition>() {
            @Override
            protected Collection<StoragePartition> doLoad() throws LoaderException {
                try {
                    List<StoragePartition> partitions = new ArrayList<StoragePartition>();

                    IStore<StorageDevice> store = factory.provide( IStorageDriveDevice.class );
                    store.load();

                    for ( StorageDevice device : store.getList() ) {
                        for ( IStorageDevicePartition partition :  ( (IStorageDriveDevice) device.getRelatedObject() ).getPartitions() ) {
                            StoragePartition item = new StoragePartition();
                            item.setRelatedObject( partition );
                            item.setPath( partition.getPath() );
                            partitions.add( item );
                        }
                    }

                    return partitions;
                } catch ( InstantiationException e ) {
                    throw new LoaderException( e.getMessage(), e );
                }
            }
        };
    }

	protected void displayJobConfiguration( Job job ) throws UIException {
		try {
			if ( job.getJobClass() == null ) {
				throw new UnhandledUIException("Internal exception: wrong job type");
			}

			this.body.removeAll();

			this.body.setBorder( BorderFactory.createTitledBorder( job.getName() ) );

			ISwingRenderer renderer =
					  UIRegistry.<ISwingRenderer>getViewRendererFacade()
					  .createRenderer(job.getJobClass());

			IProvidersFactory providers = UIRegistry.getProvidersFactory();
			providers.registerLoader(renderer, ArtifactsStore.class, new RefreshPolicy( this.createArtifactsLoader(), 5000 ) );
            providers.registerLoader(renderer, NetworkDevicesStore.class, new RefreshPolicy( this.createNetworkDevicesLoader(), 5000 ) );
			providers.registerLoader(renderer, StorageDevicesStore.class, new RefreshPolicy( this.createStorageDevicesLoader(), 5000 ) );
            providers.registerLoader(renderer, StoragePartitionsStore.class,
                    new RefreshPolicy( this.createStoragePartitionsLoader(providers), 50000 ) );

			this.jobUI = renderer.render(this.body, job.getJobClass());
            this.jobUI.addPreInstantiateHandler(new Function<Object, Object>() {
                @Override
                public Object invoke(Object... args) throws InvocationTargetException {
                    if ( !( args[1] instanceof String ) ) {
                        return args[1];
                    }

                    try {
                        return JobWindow.this.preProcessFieldValue( (String) args[1] );
                    } catch ( EvaluationException e ) {
                        throw new InvocationTargetException( e );
                    }
                }
            });

			Dispatcher.get().forwardEvent( UIEvents.Core.Repaint, this.body );
		} catch ( Throwable e ) {
			throw new UIException("View building exception", e);
		}
	}

    protected String preProcessFieldValue( String value ) throws EvaluationException {
        return this.getEvaluator().processEmbed(value);
    }

	protected void configUI() {
		this.setTitle("Job configuration");
		this.setSize( new Dimension( 500, 400 ) );
	}


}
