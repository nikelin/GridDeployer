package com.api.deployer.system.processes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: semichevskiy
 * Date: 2/17/11
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISystemProcess extends Serializable {

    public String readStdError() throws IOException;
    
    public String readStdInput() throws IOException;

    public InputStream getInputStream();

    public InputStream getErrorStream();

    public OutputStream getOutputStream();
    
    public boolean isSuccessful() throws IOException;
    
    public int waitFor() throws InterruptedException;

    public int exitValue();

    public void destroy();

    public int getPID();
}
