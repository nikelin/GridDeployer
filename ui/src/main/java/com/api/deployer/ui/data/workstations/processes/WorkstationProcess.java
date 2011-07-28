package com.api.deployer.ui.data.workstations.processes;

import com.redshape.ui.data.AbstractModelData;

/**
 * @author nikelin
 */
public class WorkstationProcess extends AbstractModelData {

    public WorkstationProcess() {
        super();
    }

    public void setUser( String user ) {
        this.set( WorkstationProcessModel.USER, user );
    }

    public String getUser() {
        return this.get( WorkstationProcessModel.USER );
    }

    public void setPID( Integer value ) {
        this.set( WorkstationProcessModel.PID, value );
    }

    public Integer getPID() {
        return this.get( WorkstationProcessModel.PID );
    }

    public String getName() {
        return this.get( WorkstationProcessModel.NAME );
    }

    public void setName( String name ) {
        this.set( WorkstationProcessModel.NAME, name );
    }

    public Integer getMemoryUsage() {
        return this.get( WorkstationProcessModel.PHYS_MEMORY_USAGE );
    }

    public void setMemoryUsage( Integer value ) {
        this.set( WorkstationProcessModel.PHYS_MEMORY_USAGE, value );
    }

    public void setVirtMemoryUsage( Integer value ) {
        this.set( WorkstationProcessModel.VIRT_MEMORY_USAGE, value );
    }

    public Integer getVirtMemoryUsage() {
        return this.get( WorkstationProcessModel.VIRT_MEMORY_USAGE );
    }

    public void setCpuUsage( Integer value ) {
        this.set( WorkstationProcessModel.CPU_USAGE, value );
    }

    public Integer getCpuUsage() {
        return this.get( WorkstationProcessModel.CPU_USAGE );
    }
}
