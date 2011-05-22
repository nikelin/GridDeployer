package com.api.deployer.system.linux.scanners;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.api.commons.IHasher;
import com.api.commons.hashers.MD5;
import com.api.deployer.system.devices.storage.IStorageDevicePartition;
import com.api.deployer.system.devices.storage.IStorageDriveDevice;
import com.api.deployer.system.devices.storage.PartitionFlag;
import com.api.deployer.system.devices.storage.PartitionType;
import com.api.deployer.system.devices.storage.StorageDiskDrive;
import com.api.deployer.system.devices.storage.StorageDiskPartition;
import com.api.deployer.system.devices.storage.StorageFilesystem;
import com.api.deployer.system.scanners.IDeviceScanner;
import com.api.deployer.system.scanners.ScannerException;
import com.api.deployer.system.ISystemFacade;
import com.api.deployer.system.processes.ISystemProcess;
import com.api.deployer.system.scripts.IScriptExecutionHandler;
import com.api.deployer.system.scripts.IScriptExecutor;

/**
 * @author nikelin
 */
public class LinuxFSScanner implements IDeviceScanner<IStorageDriveDevice> {
	private final static Logger log = Logger.getLogger( LinuxFSScanner.class );
	
	private ISystemFacade facade;
	// fixme, please!!
	private Collection<IStorageDriveDevice> drivesCache;
	
    final static Pattern NON_PRINTABLE = Pattern.compile("[^\t\n\r\u0020-\u007E\u0085\u00A0-\uD7FF\uE000-\uFFFC]");
	private Pattern partprobePattern = Pattern.compile("(.+?): (.+?)partitions");
	private Pattern diskModelPattern = Pattern.compile("^Model:\\s(.+)\n");
	private Pattern diskSizePattern = Pattern.compile("Disk.*:\\s(.+)");
	private Pattern sectorSizePattern = Pattern.compile("size.*:\\s(.+)\\/");
	private Pattern partitionUUIDPattern = Pattern.compile("UUID=\"(.+?)\"");
	
	public LinuxFSScanner() {
		this(null);
	}
	
	public LinuxFSScanner( ISystemFacade facade ) {
		this.facade = facade;
	}
	
	public void setFacade( ISystemFacade facade ) {
		this.facade = facade;
	}
	
	protected ISystemFacade getFacade() {
		return this.facade;
	}
	
	protected IHasher getHasher() {
		return new MD5();
	}
	
	public Collection<IStorageDriveDevice> scan() throws ScannerException {
		return this.findDrives();
	}
	
	public Collection<IStorageDriveDevice> findDrives() throws ScannerException {
		try {
			if ( this.drivesCache != null ) {
				return this.drivesCache;
			}
			
			Collection<IStorageDriveDevice> result = this.drivesCache = new HashSet<IStorageDriveDevice>();
		
			IScriptExecutor process = this.getFacade()
										  .getConsole()
										  	.createExecutor("partprobe")
										  	.addUnnamedParameter("-s");
			process.setHandler( new IScriptExecutionHandler() {
				
				@Override
				public boolean onExit(String output) {
					if ( output.isEmpty() ) {
						return false;
					}
					
					return true;
				}
				
				@Override
				public void onError(String output) {
					log.info("Partprobe exection error");
				}

				@Override
				public Integer onProgressRequested(ISystemProcess process) throws IOException {
					return process.exitValue() == 1 ? 100 : 0;
				}
			});
			
			String output = process.execute();
			log.info(output);
			if ( !process.isSuccess() ) {
				throw new ScannerException();
			}

			StringTokenizer tokenizer = new StringTokenizer( output, "\n");
			while ( tokenizer.hasMoreElements() ) {
				String splittedItem = tokenizer.nextToken();
				Matcher matcher = partprobePattern.matcher( splittedItem );
				if ( matcher.find() ) {
					IStorageDriveDevice device = this.createDevice();
					
					String path = output.substring( matcher.start(1), matcher.end(1) );
					device.setPath( path );
					// fixme
					device.setUUID( UUID.randomUUID() );
					device.setPartitionsType( splittedItem.substring( matcher.start(2), matcher.end(2) ) ); 
					device.setName( path.replace("/dev/", "") );
					
					Collection<IStorageDevicePartition> partitions = this.findPartitions(device);
					for ( IStorageDevicePartition partition : partitions ) {
						device.addPartition(partition);
					}
					
					result.add(device);
				}
			}
			
			process.kill();
		
			return result;
		} catch ( IOException e ) {
			throw new ScannerException();
		}
	}
	
	public Collection<IStorageDevicePartition> findPartitions( 
			IStorageDriveDevice device ) throws ScannerException {
		try {
			Collection<IStorageDevicePartition> partitions = new HashSet<IStorageDevicePartition>();
			 
			IScriptExecutor process = this.getFacade()
										  .getConsole()
										  	.createExecutor("parted")
										  	.addUnnamedParameter( device.getPath() )
										  	.addUnnamedParameter( "print" );
			
			String output = process.execute();
			if ( !process.isSuccess() ) {
				throw new ScannerException();
			}
			
			Matcher result;
			result = diskModelPattern.matcher(output);
			result.find();
			device.setModel( output.substring( result.start(1), result.end(1) ) );
			
			result = sectorSizePattern.matcher(output);
			result.find();
			String sectorSize = output.substring( result.start(1), result.end(1) ).replace("B", "");
			device.setSectorSize( Integer.valueOf( sectorSize ) );
			
			result = diskSizePattern.matcher(output);
			result.find();
			device.setSize( this.parseSize( output.substring( result.start(1), result.end(1) ) ) );
			
			StringTokenizer tokenizer = new StringTokenizer( output, "\n");
			int offset = 0;
			while ( tokenizer.hasMoreElements() ) {
				String token = tokenizer.nextToken();
				if ( offset++ <= 4 ) {
					continue;
				}
				
				int pos = 0;
				int tokenNum = 0;
				StringBuilder buff = new StringBuilder();
				String cells[] = new String[7];
				while ( pos++ <= token.length() ) {
					if ( pos == token.length() ) {
						cells[tokenNum++] = buff.toString();
						break;
					}
					
					String substr = token.substring( pos, pos + 1 );
					if ( substr.isEmpty() || substr.equals(" ") || NON_PRINTABLE.matcher( substr ).find() ) {
						if ( 0 != buff.length() ) {
							cells[tokenNum++] = buff.toString();
							buff.delete( 0, buff.length() );
						}
						continue;
					} 
					
					buff.append( substr );
				}
				
				IStorageDevicePartition partition = new StorageDiskPartition(device);
				partition.setNumber( Integer.valueOf( cells[0] ) );
				partition.setStart( this.parseSize( cells[1] ) );
				partition.setEnd( this.parseSize( cells[2] ) );
				partition.setSize( this.parseSize( cells[3] ) );
				partition.setType( this.getPartitionType( cells[4] ) );
				partition.setName( device.getName() + partition.getNumber() );
				partition.setPath( device.getPath() + String.valueOf( partition.getNumber() ) );
				partition.setUUID( this.detectPartitionUUID( partition.getPath() ) );
				
				if ( cells[5] != null ) {
					partition.setFilesystem( this.getFilesystem( cells[5] ) );
				}
				
				if ( cells[6] != null && cells[6].contains("boot") ) {
					partition.setFlag( PartitionFlag.BOOT, true);
				}
				
				partitions.add(partition);
			}
			
			return partitions;
		} catch ( Throwable e ) {
			throw new ScannerException( e.getMessage(), e );
		}
	}
	
	protected UUID detectPartitionUUID( String path ) throws IOException {
		IScriptExecutor executor = this.getFacade().getConsole().createExecutor("blkid").addUnnamedParameter( path );
		String executionResult = executor.execute();
		
		Matcher matcher = partitionUUIDPattern.matcher( executionResult );
		if ( !matcher.find() ) {
			return null;
		}
		
		try {
			return UUID.fromString( executionResult.substring( matcher.start(1), matcher.end(1) ) );
		} catch ( IllegalArgumentException e  ) {
			return null;
		}
	}
	
	protected StorageFilesystem getFilesystem( String value ) {
		if ( value.equals( "ext2" ) ) {
			return StorageFilesystem.EXT2;
		} else if ( value.equals( "ext3" ) ) {
			return StorageFilesystem.EXT3;
		} else if ( value.equals( "ext4" ) ) {
			return StorageFilesystem.EXT4;
		} else if ( value.equals( "ntfs" ) ) {
			return StorageFilesystem.NTFS;
		} else if ( value.equals( "linux-swap" ) || value.equals("swap") ) {
			return StorageFilesystem.SWAP;
		} else if ( value.equals( "vfat" ) ) {
			return StorageFilesystem.VFAT;
		} else {
			return StorageFilesystem.GENERIC;
		}
	}
	
	protected Long parseSize( String value ) {
		value = value.trim();
		if ( value.endsWith("GB") ) {
			return Long.valueOf( (long) ( Math.pow( 10, 9 ) * Double.valueOf( value.replace("GB", "") ) ) );
		} else if ( value.endsWith("MB") ) {
			return Long.valueOf( (long) ( Math.pow( 10, 6 ) * Double.valueOf( value.replace("MB", "" ) ) ) );
		} else if ( value.endsWith("kB") ) {
			return Long.valueOf( (long) ( Math.pow( 10, 3 ) * Double.valueOf( value.replace("kB", "") ) ) ); 
		} else if ( value.endsWith("B") ) {
			return Long.valueOf( value.replace("B", "") );
		}
		
		throw new IllegalArgumentException();
	}
	
	protected IStorageDevicePartition createDevicePartition( IStorageDriveDevice device ) {
		return new StorageDiskPartition( device );
	}
	
	protected IStorageDriveDevice createDevice() {
		return new StorageDiskDrive();
	}
	
	protected PartitionType getPartitionType( String type ) {
		if ( type.equals("logical") ) {
			return PartitionType.LOGICAL;
		} else if ( type.equals("primary") ) {
			return PartitionType.PRIMARY;
		} else if ( type.equals("extended") ) {
			return PartitionType.EXTENDED;
		}
		
		return PartitionType.UNKNOWN;
	}
	
}
