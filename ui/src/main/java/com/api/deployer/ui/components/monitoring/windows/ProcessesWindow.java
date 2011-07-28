package com.api.deployer.ui.components.monitoring.windows;

import com.api.deployer.ui.data.workstations.StationsStore;
import com.api.deployer.ui.data.workstations.Workstation;
import com.api.deployer.ui.data.workstations.processes.WorkstationProcessLoader;
import com.api.deployer.ui.data.workstations.processes.WorkstationProcessStore;
import com.redshape.ui.application.UIException;
import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.IEventHandler;
import com.redshape.ui.components.InteractionAction;
import com.redshape.ui.data.adapters.swing.ComboBoxAdapter;
import com.redshape.ui.data.adapters.swing.TableAdapter;
import com.redshape.ui.data.loaders.policies.RefreshPolicy;
import com.redshape.ui.utils.UIRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.api.deployer.ui.components.monitoring.windows
 */
public class ProcessesWindow extends JFrame {
    private Workstation workstation;
    private WorkstationProcessStore processesStore;
    private StationsStore workstationsStore;
    private JComboBox workstationSelector;

    private JTable processesTable;

    public ProcessesWindow() throws UIException {
        this(null);
    }

    public ProcessesWindow( Workstation workstation ) throws UIException {
        super();

        this.workstation = workstation;

        this.init();
        this.buildUI();
        this.configUI();
    }

    protected void init() throws UIException {
        try {
            this.processesStore = UIRegistry.getStoresManager().getStore( WorkstationProcessStore.class );
            this.workstationsStore = UIRegistry.getStoresManager().getStore( StationsStore.class );

            if ( this.workstation != null ) {
                this.bindLoader();
            }
        } catch ( InstantiationException e ) {
            throw new UIException("Store init exception...");
        }
    }

    protected void bindLoader() {
        this.processesStore.setLoader( new RefreshPolicy( new WorkstationProcessLoader( this.workstation ), 4000 ) );
    }

    public void setWorkstation( Workstation record ) {
        this.workstation = record;
        this.bindLoader();
    }

    protected Workstation getWorkstation() {
        if ( this.workstation != null ) {
            return this.workstation;
        }

        return (Workstation) this.workstationSelector.getSelectedItem();
    }

    protected void buildUI() throws UIException {
        this.setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );

        if ( this.workstation == null ) {
            this.add( this.createWorkstationSelector() );
        }

        this.add( this.createProcessesTable() );
        this.add( this.createControlPanel() );

    }

    public JComponent createProcessesTable() {
        return this.processesTable = new TableAdapter( this.processesStore );
    }

    public JComponent createControlPanel() {
        Box box = Box.createHorizontalBox();
        box.add( new JButton(
            new InteractionAction(
                "Stop",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {

                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "End",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {

                    }
                }
            )
        ) );

        box.add( new JButton(
            new InteractionAction(
                "Kill",
                new IEventHandler() {
                    @Override
                    public void handle(AppEvent event) {

                    }
                }
            )
        ) );

        return box;
    }

    protected void onStationChanged() {

    }

    public JComponent createWorkstationSelector() {
        this.workstationSelector = new ComboBoxAdapter( this.workstationsStore );
        this.workstationSelector.addItemListener( new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                ProcessesWindow.this.onStationChanged();
            }
        });

        this.workstationSelector.setRenderer( new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
                return new JLabel( ( (Workstation) o ).getName() );
            }
        });

        return this.workstationSelector;
    }

    protected void configUI() {
        this.setSize( 500, 400 );
        this.setTitle("Workstation processes");
    }

}
