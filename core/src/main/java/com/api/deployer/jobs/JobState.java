package com.api.deployer.jobs;

/**
 * @author nikelin
 * @date 26/04/11
 * @package com.api.deployer.jobs
 */
public enum JobState {
    WAITING("Waiting"),
    PROCESSING("Processing"),
    PAUSED("Paused"),
    FAILED("Failed");

    private String title;

    private JobState( String title ) {
        this.title = title;
    }

    public String title() {
        return this.title;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
