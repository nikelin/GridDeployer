package com.api.deployer.backup.artifactory;

public class ArtifactoryVersion {
	private String version;
	
	public static final ArtifactoryVersion A10 = new ArtifactoryVersion("1.0");
	public static final ArtifactoryVersion Current = ArtifactoryVersion.A10;
	
	public static ArtifactoryVersion[] VERSIONS = new ArtifactoryVersion[] { A10 };
	
	public ArtifactoryVersion( String version ) {
		this.version = version;
	}
	
	public String version() {
		return this.version;
	}
	
	public static ArtifactoryVersion valueOf( String version ) {
		for ( ArtifactoryVersion registryVersion : VERSIONS ) {
			if ( registryVersion.version().equals(version) ) {
				return registryVersion;
			}
		}
		
		return null;
	}
	
}