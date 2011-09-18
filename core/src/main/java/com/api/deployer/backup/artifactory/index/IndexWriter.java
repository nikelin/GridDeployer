package com.api.deployer.backup.artifactory.index;

import com.api.deployer.backup.artifactory.IArtifactoryFacade;
import com.api.deployer.backup.artifactory.WriterException;
import com.api.deployer.backup.artifactory.artifacts.IArtifact;
import com.api.deployer.backup.artifactory.artifacts.writers.IArtifactWriter;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.config.IWritableConfig;
import com.redshape.utils.config.XMLConfig;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class IndexWriter implements IIndexWriter {
	private IArtifactoryFacade artifactoryFacade;
	
	protected static final class LockHandler {
		private static Collection<String> allocated = new HashSet<String>();
		private static final int WAIT_LIMIT = 5000;
		
		private String path;
		
		private LockHandler(String path) {
			if ( path == null ) {
				throw new IllegalArgumentException("null");
			}
			
			this.path = path;
		}
		
		public synchronized static LockHandler create( String path ) throws LockedException {
			if ( allocated.contains(path) ) {
				throw new LockedException("Needs to close previous lock before open another");
			}
			
			LockHandler handler = new LockHandler(path);
			allocated.add(path);
			return handler;
		}
		
		private void check() throws InterruptedException {
			if ( !allocated.contains( this.path ) ) {
				throw new InterruptedException("Handler is closed");
			}
		}
		
		private File getFile() {
			return new File( this.path + File.separator + ".lock");
		}
		
		public boolean waitUnlock() throws InterruptedException {
			check();
			
			File file = this.getFile();
			long started = new Date().getTime();
			while ( !file.exists() || ( new Date().getTime() - started ) > WAIT_LIMIT ) {
				Thread.sleep(50);
			}
			
			if ( !file.exists() ) {
				throw new InterruptedException("Unable to get lock");
			}
			
			return true;
		}
		
		public boolean checkLock() {
			return new File( this.path + File.separator + ".lock").exists();
		}
		
		public void lock() throws IOException, InterruptedException {
			check();
			
			File file = this.getFile();
			file.createNewFile();
			file.setReadOnly();
		}
		
		public void unlock() throws IOException, InterruptedException {
			check();
			
			this.getFile().delete();
		}
		
		public void close() throws InterruptedException {
			check();
			
			allocated.remove( this.path );
		}	
	}
	
	public IndexWriter() {
		this(null);
	}
	
	public IndexWriter( IArtifactoryFacade facade ) {
		this.artifactoryFacade = facade;
	}
	
	@Override
	public void setArtifactoryFacade( IArtifactoryFacade facade ) {
		this.artifactoryFacade = facade;
	}
	
	protected IArtifactoryFacade getArtifactoryFacade() {
		return this.artifactoryFacade;
	}
	
	@Override
	public IConfig writeIndex(IArtifactoryIndex index) throws WriterException {
		try {
			IWritableConfig config = XMLConfig.createEmpty("meta");
			
			config.createChild("lastUpdate").set( String.valueOf( index.getLastUpdate() ) );
			config.createChild("version").set( index.getVersion().version() );
			
			IWritableConfig artifactsHolder = config.createChild("artifacts");
			for ( IArtifact artifact : index.getArtifacts() ) {
				IArtifactWriter writer = this.getArtifactoryFacade().createArtifactWriter(artifact);
				artifactsHolder.append( writer.flush() );
			}
			
			return config;
		} catch ( ConfigException e  ) {
			throw new WriterException("Index serializing exception", e);
		} catch ( InstantiationException e  ) {
			throw new WriterException( e.getMessage(), e );
		}
	}

	@Override
	public synchronized void flushIndex( String path, IArtifactoryIndex index) 
		throws WriterException, LockedException {
		try {
			LockHandler handler = LockHandler.create(path);
			if ( handler.waitUnlock() ) {
				handler.lock();
				XMLConfig.writeConfig( new File(path), (XMLConfig) this.writeIndex(index) );
				handler.unlock();
			}
			handler.close();
		} catch ( ConfigException e  ) {
			throw new WriterException( "Index serialization exception", e );
		} catch ( IOException e  ) {
			throw new WriterException( "I/O exception while being flushing index to disk drive", e);
		} catch ( InterruptedException e  ) {
			throw new WriterException( "Unable to get control access on index", e );
		}
	} 

}
