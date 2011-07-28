package com.api.deployer.system.linux;

import java.io.IOException;

import com.api.deployer.system.devices.IDevice;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageFilesystem;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.scanners.filters.StorageDriveDeviceFilter;
import com.api.deployer.system.configurers.IPartitionsEditor;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.console.IConsole;
import com.api.deployer.system.scripts.IScriptExecutor;

public class PartedPartitionsEditor implements IPartitionsEditor {
	private IStorageDriveDevice device;
	private ISystemFacade facade;
	
	public PartedPartitionsEditor( ISystemFacade facade, IStorageDriveDevice device ) {
		this.device = device;
		this.facade = facade;
	}
	
	protected ISystemFacade getSystem() {
		return this.facade;
	}
	
	protected IConsole getConsole() {
		return this.getSystem().getConsole();
	}
	
	protected IStorageDriveDevice getContext() {
		return this.device;
	}
	
	@Override
	public void unmountPartition( IStorageDevicePartition partition ) throws IOException {
		IScriptExecutor executor = this.getConsole().createExecutor("umount");
		executor.addUnnamedParameter( partition.getPath() );
		
		String output = executor.execute();
		
		if ( !executor.isSuccess() ) {
			throw new IOException("Unmount of partition `" + partition.getPath() + "` failed: " + output );
		}
	}
	
	@Override
	public IStorageDevicePartition createPartition(Integer number, StorageFilesystem fs, 
			PartitionType type, Long start, Long end) 
		throws IOException {
		IScriptExecutor executor = this.getConsole().createExecutor("parted");
		executor.addUnnamedParameter( this.getContext().getPath() );
		executor.addUnnamedParameter( "mkpart" );
		executor.addUnnamedParameter( type.name().toLowerCase() );
		
		String fsType = this.getFSType(fs);
		executor.addUnnamedParameter( fsType == null ? "ext2" : fsType );
		executor.addUnnamedParameter( start );
		executor.addUnnamedParameter( end );
		 
		executor.execute();
		
		IStorageDevicePartition partition = this.getContext().createPartition();
		partition.setStart(start);
		partition.setEnd(end);
		partition.setPath( device.getPath() + number );
		partition.setName( device.getName() + number );
		partition.setSize( end - start );
		partition.setNumber(number);
		partition.setFilesystem( fs );
		partition.setType( type );
		
		return partition;
	}

	public String getFSType( StorageFilesystem filesystem ) {
		if ( filesystem.equals( StorageFilesystem.EXT2 ) ) {
			return "ext2";
		} else if ( filesystem.equals( StorageFilesystem.EXT3 ) ) {
			return "ext3";
		} else if ( filesystem.equals( StorageFilesystem.EXT4 ) ) {
			return "ext4";
		} else if ( filesystem.equals( StorageFilesystem.NTFS ) ) {
			return "ntfs";
		} else if ( filesystem.equals( StorageFilesystem.SWAP ) ) {
			return "swap";
		} else if ( filesystem.equals( StorageFilesystem.VFAT ) ) {
			return "vfat";
		} else if ( filesystem.equals( StorageFilesystem.GENERIC ) ) {
			return null;
		}
		
		throw new IllegalArgumentException();
	}
	
	@Override
	public void deletePartition(Integer number) 
		throws IOException {
		IScriptExecutor executor = this.getConsole().createExecutor("parted");
		executor.addUnnamedParameter( "rm" );
		executor.addUnnamedParameter( number );
		
		executor.execute();
	}
	
	@Override
	public void deletePartitions() throws IOException {
		try {
			IStorageDriveDevice device = (IStorageDriveDevice) this.getSystem()
				.getDevice( new StorageDriveDeviceFilter() {
					private static final long serialVersionUID = 3844674809188131511L;

					public boolean filter( IDevice device ) {
						return super.filter(device) && device.getPath().equals( device.getPath() );
					}
				});
			
			for ( IStorageDevicePartition partition : device.getPartitions() ) {
				this.deletePartition( partition.getNumber() );
			}
		} catch ( ScannerException e ) {
			throw new IOException( e.getMessage(), e );
		}
	}

	@Override
	public void makeFilesystem(Integer number, StorageFilesystem format)
		throws IOException {
		IScriptExecutor executor = this.getConsole().createExecutor("parted");
		executor.addUnnamedParameter( "mkfs" );
		executor.addUnnamedParameter( number );
		executor.addUnnamedParameter( format.getCode() );
		
		executor.execute();
	}

}
