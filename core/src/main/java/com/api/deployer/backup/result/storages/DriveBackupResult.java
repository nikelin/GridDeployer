package com.api.deployer.backup.result.storages;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.api.deployer.backup.result.IBackupResult;

public class DriveBackupResult implements IDriveBackupResult {
	private String finalName;
	private IBackupResult grubResult;
	private Collection<IPartitionBackupResult> partitionsResult = new HashSet<IPartitionBackupResult>();
	private IMBRBackupResult mbrResult;
	private UUID uuid;
	private String name;
	private String path;
	private String partitionsType;
	private String model;
	private String imageName;
	private String imageDescription;
	
	public DriveBackupResult( String finalName ) {
		this.finalName = finalName;
	}
	
	@Override
	public void setUUID( UUID uuid ) {
		this.uuid = uuid;
	}
	
	@Override
	public UUID getUUID() {
		return this.uuid;
	}
	
	@Override
	public String getFinalName() {
		return this.finalName;
	}

	@Override
	public void setGrubResult( IBackupResult result ) {
		this.grubResult = result;
	}
	
	@Override
	public IBackupResult getGrubResult() {
		return this.grubResult;
	}

	@Override
	public void addPartitionResult( IPartitionBackupResult result ) {
		this.partitionsResult.add(result);
	}
	
	@Override
	public Collection<IPartitionBackupResult> getPartitionsResult() {
		return this.partitionsResult;
	}

	@Override
	public IMBRBackupResult getMBRResult() {
		return this.mbrResult;
	}

	@Override
	public void setMBRResult(IMBRBackupResult result) {
		this.mbrResult = result;
	}

	@Override
	public void setPartitionsResult(Collection<IPartitionBackupResult> result) {
		this.partitionsResult = result;
	}

	@Override
	public void setPartitionsType(String type) {
		this.partitionsType = type;
	}

	@Override
	public String getPartitionsType() {
		return this.partitionsType;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String getModel() {
		return this.model;
	}
	
	@Override
	public void setImageName( String name ) {
		this.imageName = name;
	}
	
	@Override
	public String getImageName() {
		return this.imageName;
	}
	
	@Override
	public void setImageDescription( String description ) {
		this.imageDescription = description;
	}

	@Override
	public String getImageDescription() {
		return this.imageDescription;
	}

}
