package com.api.deployer.ui.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public final class Settings {
	private static final Logger log = Logger.getLogger( Settings.class );
	public static String DEFAULT_FILE_PATH = ".settings";
	
	protected static class InstanceHandler {
		private final static Settings instance = new Settings();
	}

	public static Settings getInstance() {
		return InstanceHandler.instance;
	}
	
	public static class Setting {
		private String code;
		
		protected Setting( String code ) {
			this.code = code;
		}
		
		public static final Setting BackupDirectory = new Setting("app.backup.directory");
		public static final Setting Theme = new Setting("app.theme");
		public static final Setting Locale = new Setting("app.locale");
		
		private static final Setting[] SETTINGS = new Setting[] {
			BackupDirectory, Theme, Locale
		};
		
		public String name() {
			return this.code;
		}
		
		public static Setting valueOf( String id ) {
			for ( Setting s : SETTINGS ) {
				if ( s.code.equals(s) ) {
					return s;
				}
			}
			
			return null;
		}
	}
	
	private Map<Setting, String> settings = new HashMap<Setting, String>();
	private String settingsFilePath;
	
	private Settings() {
		if ( this.settingsFilePath == null ) {
			this.settingsFilePath = DEFAULT_FILE_PATH;
		}
		
		this.load();
	}
	
	public void save() throws IOException {
		File file = new File( this.settingsFilePath );
		if ( !file.exists() ) {
			file.createNewFile();
		}
		
		StringBuffer buff = new StringBuffer();
		for ( Setting s : this.settings.keySet() ) {
			buff.append( s.name() )
				.append("=")
				.append( this.settings.get(s) )
				.append( "\n" );
		}
		
		FileWriter writer = new FileWriter(file);
		writer.write( buff.toString() );
		writer.close();
	}
	
	private void load() {
		try {
			File file = new File( this.settingsFilePath );
			if ( file.length() == 0 ) {
				return;
			}
			
			FileReader reader = new FileReader(file);
			CharBuffer data = CharBuffer.allocate( (int) file.length() );
			reader.read( data );
			
			int pos = 0;
			StringBuffer name = new StringBuffer();
			StringBuffer value = new StringBuffer();
			boolean nameContext = true;
			while( pos++ != data.length() ) {
				char c = data.charAt(pos);
				switch ( c ) {
				case '\n':
					nameContext = true;
					Setting setting = Setting.valueOf( name.toString() );
					if ( setting == null ) {
						throw new IllegalArgumentException("Unsupported property: " + name.toString() );
					}
					
					this.set( setting, value.toString() );
				break;
				case '=':
					nameContext = false;
				break;
				case ' ':
					if ( nameContext ) {
						continue;
					}
				default:
					if ( nameContext ) {
						name.append( c );
					} else {
						value.append(c);
					}
				}
			}
		} catch ( IOException e  ) {
			log.info("Settings reading exception");
		}
	}
	
	public void setSettingsFile( String path ) {
		this.settingsFilePath = path;
		this.load();
	}
	
	public void set( Setting id, String value ) {
		this.settings.put(id, value);
	}
	
	public String get( Setting id ) {
		return this.settings.get(id);
	}
	
}
