package com.api.deployer.jobs.activation;

public enum JobActivationType {
	TRIGGER("Trigger"),
	TIMER("Timer"),
	DATE("On specified date"),
	SINGLE("Now");

	private String type;

	private JobActivationType( String type ) {
		this.type = type;
	}

	public String type() {
		return this.type;
	}
}
