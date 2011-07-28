package com.api.deployer.jobs.result;

import com.api.commons.IEnum;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 4/24/11
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class JobResultAttribute implements IEnum {
    private String code;

    protected JobResultAttribute( String code ) {
        this.code = code;
    }

    public String name() {
        return this.code;
    }

}
