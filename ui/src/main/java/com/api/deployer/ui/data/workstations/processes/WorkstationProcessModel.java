package com.api.deployer.ui.data.workstations.processes;

import com.redshape.ui.data.AbstractModelType;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkstationProcessModel extends AbstractModelType {

    public static final String USER = "user";
    public static final String NAME = "name";
    public static final String PHYS_MEMORY_USAGE = "physMemoryUsage";
    public static final String VIRT_MEMORY_USAGE = "virtMemoryUsage";
    public static final String CPU_USAGE = "cpuUsage";
    public static final String PID = "pid";

    public WorkstationProcessModel() {
        super();

        this.addField(PID)
            .setTitle("PID");

        this.addField(USER)
            .setTitle("User");

        this.addField( NAME )
            .setTitle("Name");

        this.addField( PHYS_MEMORY_USAGE )
            .setTitle("Physical Memory");

        this.addField( VIRT_MEMORY_USAGE )
            .setTitle("Virtual memory");

        this.addField( CPU_USAGE )
            .setTitle("CPU usage");
    }

    @Override
    public WorkstationProcess createRecord() {
        return new WorkstationProcess();
    }

}
