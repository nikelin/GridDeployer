package com.api.deployer.system.scripts;

import static org.junit.Assert.*;
import org.junit.Test;

import com.api.deployer.system.console.Console;
import com.api.deployer.system.console.IConsole;

public class ScriptExecutorsTest {
	
	@Test
	public void testMain() {
		IConsole console = this.getConsole();
		
		IScriptExecutor executor = console.createExecutor("dd");
		executor.setParameter("if", "/dev/sda3");
		executor.setParameter("of", "/dev/sda1");
		executor.declareOutputSource("/tmp/dd-result.txt");
		
		assertTrue( executor.hasDeclaredOutputSource() );
		assertEquals( 
			"dd if=/dev/sda3 of=/dev/sda1  > /tmp/dd-result.txt",
			executor.getExecCommand()
		);
		
		IScriptListExecutor<IScriptExecutor> listExecutor = console.createListExecutor();
		listExecutor.addScript( executor );
		listExecutor.addScript( executor );
		
		assertEquals(
			"(" + executor.getExecCommand() + ") && (" + executor.getExecCommand() + ")",
			listExecutor.getExecCommand()
		);
	}
	
	// TODO: rewrite with spring context
	protected IConsole getConsole() {
		return new Console();
	}
	
}
