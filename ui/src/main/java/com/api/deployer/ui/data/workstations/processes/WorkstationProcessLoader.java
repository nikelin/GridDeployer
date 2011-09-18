package com.api.deployer.ui.data.workstations.processes;

import com.api.deployer.jobs.JobScope;
import com.api.deployer.jobs.requests.ProcessesRequestJob;
import com.api.deployer.system.processes.system.IMonitoredProcess;
import com.api.deployer.ui.connector.DeployAgentConnector;
import com.api.deployer.ui.data.workstations.Workstation;
import com.redshape.daemon.jobs.result.IJobResult;
import com.redshape.ui.data.loaders.AbstractDataLoader;
import com.redshape.ui.data.loaders.LoaderException;
import com.redshape.ui.utils.UIRegistry;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkstationProcessLoader extends AbstractDataLoader<WorkstationProcess> {
    private Workstation workstation;

    public WorkstationProcessLoader( Workstation workstation ) {
        super();

        this.workstation = workstation;
    }

    protected DeployAgentConnector getConnector() {
        return UIRegistry.getContext().getBean( DeployAgentConnector.class );
    }

    @Override
    protected Collection<WorkstationProcess> doLoad() throws LoaderException {
        try {
            IJobResult result = this.getConnector()
							.executeJob(JobScope.AGENT,
									this.workstation.getId(), new ProcessesRequestJob());

            Collection<WorkstationProcess> list = new HashSet<WorkstationProcess>();

            Collection<IMonitoredProcess> processes = result.getAttribute( ProcessesRequestJob.Attribute.LIST );
            for ( IMonitoredProcess process : processes ) {
                WorkstationProcess listItem = new WorkstationProcess();
                listItem.setCpuUsage( process.getCpuUsage() );
                listItem.setMemoryUsage( process.getPhysUsage() );
                listItem.setVirtMemoryUsage( process.getVirtUsage() );
                listItem.setPID( process.getPid() );
                listItem.setName( process.getCommand() );
                listItem.setUser( process.getUser() );
                listItem.setRelatedObject( process );
                list.add( listItem );
            }

            return list;
        } catch ( RemoteException e ) {
            throw new LoaderException( e.getMessage(), e );
        }
    }
}
