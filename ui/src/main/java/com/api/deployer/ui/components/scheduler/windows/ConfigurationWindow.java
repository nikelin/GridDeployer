package com.api.deployer.ui.components.scheduler.windows;

import com.api.deployer.jobs.activation.ActivationAttribute;
import com.api.deployer.jobs.activation.JobActivationProfile;
import com.api.deployer.jobs.activation.JobActivationType;
import com.api.deployer.ui.components.scheduler.SchedulerComponent;
import com.api.deployer.ui.components.scheduler.panels.JobsTree;
import com.api.deployer.ui.data.jobs.Job;
import com.api.deployer.ui.data.jobs.categories.JobCategoryStore;
import com.api.deployer.ui.data.jobs.configurations.JobConfiguration;
import com.api.deployer.ui.data.jobs.configurations.JobConfigurationsStore;
import com.api.deployer.ui.data.workstations.StationsStore;
import com.api.deployer.ui.data.workstations.groups.StationGroupsStore;
import com.redshape.ui.Dispatcher;
import com.redshape.ui.application.UnhandledUIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.application.events.UIEvents;
import com.redshape.ui.application.events.handlers.WindowCloseHandler;
import com.redshape.ui.application.notifications.INotificationsManager;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.panels.FormPanel;
import com.redshape.ui.utils.UIRegistry;
import org.apache.commons.lang.UnhandledException;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;

/**
 * @author root
 * @date 12/04/11
 * @package com.api.deployer.ui.components.scheduler.windows
 */
public class ConfigurationWindow extends JFrame {
	private static final Logger log = Logger.getLogger(ConfigurationWindow.class);

	private JobCategoryStore jobStore;
	private JobsTree tree;
    private JobActivationProfile profile;
	private FormPanel activationPolicyPanel;
	private JComboBox activationTypeSelector;

	public ConfigurationWindow() {
		super();

		this.init();

		this.buildUI();
		this.configUI();
	}

	protected void init() {
		try {
			this.jobStore = UIRegistry.getStoresManager().getStore(this, JobCategoryStore.class);

			Dispatcher.get().addListener(
				SchedulerComponent.Events.Views.Job.Added,
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						ConfigurationWindow.this.onJobAdded(event);
					}
				}
			);
		} catch ( InstantiationException e ) {
			throw new UnhandledUIException("Cannot initialize jobs storage");
		}
	}

	protected StationsStore getStationsStore() {
		try {
			return UIRegistry.getStoresManager().getStore( StationsStore.class );
		} catch ( InstantiationException e ) {
			throw new UnhandledException( e.getMessage(), e );
		}
	}

	protected StationGroupsStore getGroupsStore() {
		try {
			return UIRegistry.getStoresManager().getStore( StationGroupsStore.class );
		} catch ( InstantiationException e ) {
			throw new UnhandledUIException( e.getMessage(), e );
		}
	}

	protected void buildUI() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
		panel.add( this.createTitle() );
		panel.add( this.createTree() );
		panel.add( this.createDetailsPanel() );
		panel.add( this.createActivationPolicyPanel() );

        this.onActivationTypeChanged();

		panel.add( this.createButtonsPane() );
		this.add( panel );
	}

	protected JComponent createActivationPolicyPanel() {
		this.activationPolicyPanel = new FormPanel();
		this.activationPolicyPanel.setVisible(false);
		this.activationPolicyPanel.setBorder( BorderFactory.createTitledBorder("Policy details") );

		return this.activationPolicyPanel;
	}

	protected JComponent createDetailsPanel() {
		FormPanel panel = new FormPanel();
		panel.addField("type", "Activation type", this.createActivationTypeSelector() );
		return panel;
	}

    protected Vector<JobActivationType> getSupportedActivationPolicies() {
        Vector<JobActivationType> result = new Vector<JobActivationType>();
        result.add( JobActivationType.SINGLE );
        result.add( JobActivationType.DATE );
        result.add( JobActivationType.TIMER );

        return result;
    }

	protected JComboBox createActivationTypeSelector() {
		JComboBox selector = this.activationTypeSelector = new JComboBox(
                this.getSupportedActivationPolicies() );

		selector.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				ConfigurationWindow.this.onActivationTypeChanged();
			}
		});

		selector.setRenderer( new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
				return new JLabel( ( (JobActivationType) o ).type() );
			}
		});

		return selector;
	}

	protected void onActivationTypeChanged() {
	   	this.activationPolicyPanel.removeFields();

		switch ( (JobActivationType) this.activationTypeSelector.getSelectedItem() ) {
			case SINGLE:
				this.activationPolicyPanel.addField("delay", "Delay before start", new JTextField() );
			break;
			case DATE:
				this.activationPolicyPanel.addField("date", "Enter date (2011.01.25 18:10:05):", new JTextField() );
			break;
			case TIMER:
				this.activationPolicyPanel.addField("interval", "Invoke interval", new JTextField() );
				this.activationPolicyPanel.addField("ticks", "Ticks count", new JTextField() );
				this.activationPolicyPanel.addField("unlimited", "Unlimited", new JCheckBox() );
				this.activationPolicyPanel.addField("delay", "Before first run delay", new JTextField() );
			break;
			case TRIGGER:
			break;
		}

        this.activationPolicyPanel.setVisible(true);
		Dispatcher.get().forwardEvent( UIEvents.Core.Repaint, this.activationPolicyPanel );
	}

    protected JobActivationProfile createActivationProfile() {
        JobActivationType type = (JobActivationType) this.activationTypeSelector.getSelectedItem();
        INotificationsManager notificator = UIRegistry.getNotificationsManager();

        JobActivationProfile profile = this.profile = new JobActivationProfile(type);
        switch ( type ) {
			case SINGLE:
				String delay = this.activationPolicyPanel.<String>getField("delay").getValue();
				if ( delay != null && !delay.isEmpty() ) {
					profile.setAttribute( ActivationAttribute.Single.Delay, Integer.valueOf(delay) );
				}
			break;
			case DATE:
				String date = this.activationPolicyPanel.<String>getField("date").getValue();
				if ( date == null || date.isEmpty() ) {
					notificator.error("Date must not be null!");
					return null;
				}

				try {
					profile.setAttribute( ActivationAttribute.Date.Point, new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse( date ) );
				} catch ( ParseException e ) {
					notificator.error("Wrong date value provided!");
					return null;
				}
			break;
			case TIMER:
                String timerDelay = null;
                if (  this.activationPolicyPanel.<String>getField("delay").getValue() != null ) {
                    timerDelay = this.activationPolicyPanel.<String>getField("delay").getValue();
                }

				if ( timerDelay == null || timerDelay.isEmpty() ) {
					profile.setAttribute( ActivationAttribute.Timer.Delay, 0 );
				} else {
					try {
						profile.setAttribute( ActivationAttribute.Timer.Delay, Integer.valueOf( timerDelay ) );
					} catch ( NumberFormatException e ) {
						notificator.error("Wrong timer delay value!");
						return null;
					}
				}

				String timerInterval = this.activationPolicyPanel.<String>getField("interval").getValue();
				if ( timerInterval == null || timerInterval.isEmpty() ) {
					notificator.error("Timer interval value must be provided!");
					return null;
				} else {
					try {
						profile.setAttribute( ActivationAttribute.Timer.Interval, Integer.valueOf( timerInterval ) );
					} catch ( NumberFormatException e ) {
						notificator.error("Wrong timer interval value provided!");
						return null;
					}
				}

				Boolean isUnlimited = this.activationPolicyPanel.<Boolean>getField("unlimited").getValue();
				if ( isUnlimited ) {
					profile.setAttribute( ActivationAttribute.Timer.Unlimited, true );
				} else {
					String timerTicks = this.activationPolicyPanel.<String>getField("ticks").getValue();
					if ( timerTicks == null || timerTicks.isEmpty() ) {
						notificator.error("Either timer ticks value or enabled unlimited option must be provided!");
						return null;
					}

					try {
						profile.setAttribute( ActivationAttribute.Timer.Interval, Integer.valueOf( timerTicks ) );
					} catch ( NumberFormatException e ) {
						notificator.error("Wrong timer interval value provided!");
						return null;
					}
				}
			break;
		}

        return profile;
    }

	protected void onProcess() {
		Collection<Job> jobs = ConfigurationWindow.this.tree.getJobs();
		if ( jobs.isEmpty() ) {
			UIRegistry.getNotificationsManager().warn("Select at least one job first!");
			return;
		}

        JobActivationProfile profile = this.createActivationProfile();
        if ( profile == null ) {
            return;
        }

		Dispatcher.get().forwardEvent( SchedulerComponent.Events.Action.ProcessJobs,
			jobs, profile );

        UIRegistry.getNotificationsManager().info( jobs.size() + " jobs successfully scheduled!");
        UIRegistry.getNotificationsManager().ask(
            "Do you want continue scheduling?",
            null,
            new IEventHandler() {
                @Override
                public void handle(AppEvent event) {
                    ConfigurationWindow.this.onExit();
                }
            }
        );
	}

    protected void onConfigurationSave( AppEvent event ) {
        JobConfigurationsStore store;
        try {
            store = UIRegistry.getStoresManager().getStore(JobConfigurationsStore.class);
        } catch ( InstantiationException e ) {
            throw new UnhandledUIException("Configuration save failed!");
        }

        INotificationsManager notifier = UIRegistry.getNotificationsManager();

        boolean successful = true;
        String name;
        do {
            name = notifier.request("Enter name for a new configuration");
            if ( name.isEmpty() ) {
                notifier.error("Name must not be empty!");
                continue;
            }

            if ( store.findByName(name) != null ) {
                notifier.error("Configuration with such name alredy exists!");
            }

            successful = true;
        } while ( !successful );

        JobConfiguration configuration = new JobConfiguration( name );
        for ( Job job : this.tree.getJobs() ) {
            configuration.addItem( job );
        }

        configuration.setActivationProfile( this.profile );


        store.add( configuration );


        new WindowCloseHandler(this).handle(event);
    }

    protected void onExit() {
        if ( this.profile != null ) {
            UIRegistry.getNotificationsManager().ask(
                "Save current configuration?",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {
                        ConfigurationWindow.this.onConfigurationSave(event);
                    }
                },
                new WindowCloseHandler(this)
            );

            this.profile = null;
        }
    }

	protected JComponent createButtonsPane() {
		Box box = Box.createHorizontalBox();

		box.add( new JButton(
			new InteractionAction(
				"Process",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
						ConfigurationWindow.this.onProcess();
					}
				}
			)
		) );

		box.add( new JButton(
			new InteractionAction(
				"Cancel",
				new WindowCloseHandler(this)
			)
		) );

		return box;
	}

	protected JComponent createTitle() {
		Box box = Box.createHorizontalBox();
		box.add( new JLabel("Configuration") );
		return box;
	}

	protected JComponent createTree() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );

		panel.add( new JScrollPane( this.tree = new JobsTree( this.jobStore ) ) );

		Box box = Box.createHorizontalBox();

		box.add( new JButton(
			new InteractionAction(
				"Add",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
					  	Dispatcher.get().forwardEvent(SchedulerComponent.Events.Views.Job.New);
					}
				}
			)
		) );

		box.add( new JButton(
			new InteractionAction(
				"Remove",
				new IEventHandler() {
					@Override
					public void handle(AppEvent event) {
					}
				}
			)
		) );

		panel.add( box );

		return panel;
	}

	protected void onJobAdded( AppEvent event ) {
		this.tree.addJobRecord( event.<Job>getArg(0) );
		Dispatcher.get().forwardEvent(UIEvents.Core.Repaint, this);
	}

	protected void configUI() {
		this.setTitle("Scheduling");
		this.setSize( 450, 500 );
	}

}
