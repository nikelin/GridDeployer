package com.api.deployer.system.linux.scripts.bash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.api.deployer.system.scripts.IScriptExecutor;
import com.api.deployer.system.scripts.IScriptListExecutor;
import com.api.deployer.system.scripts.ScriptListStyle;

public class BashScriptListExecutor extends BashScriptExecutor 
									implements IScriptListExecutor<BashScriptExecutor> {
	private List<BashScriptExecutor> scripts = new ArrayList<BashScriptExecutor>();
	private ScriptListStyle style;
	
	public BashScriptListExecutor() {
		this.style = ScriptListStyle.EXCLUSIVE;
	}
	
	@Override
	public String getExecCommand() {
		StringBuilder builder = new StringBuilder();
		
		int counter = 0;
		for ( BashScriptExecutor executor : this.scripts ) {
			builder//.append( "(" )
				   .append( executor.getExecCommand() )
				   //.append( ")" )
				   ;
			
			if ( counter++ != ( this.scripts.size() - 1 ) ) {
				switch ( this.style ) {
				case EXCLUSIVE:
					builder.append(" && ");
				break;
				case INCLUSIVE:
					builder.append(" | ");
				break;
				}
			}
		}
		
		return builder.toString();
	}

	@Override
	public void addScript(BashScriptExecutor script) {
		this.scripts.add(script);
	}

	@Override
	public Collection<BashScriptExecutor> getScripts() {
		return this.scripts;
	}
	
	@Override
	public void setListStyle( ScriptListStyle style ) {
		this.style = style;
	}
	
	@Override
	public void kill() {
		for ( IScriptExecutor executor : this.getScripts() ) {
			executor.kill();
		}
	}

}
