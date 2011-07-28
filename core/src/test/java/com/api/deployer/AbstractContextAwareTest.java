package com.api.deployer;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class AbstractContextAwareTest<T> extends AbstractTest<T> {
	private ApplicationContext context;
	
	protected AbstractContextAwareTest( String path ) {
		this.context = this.loadContext( path );
	}
	
	protected ApplicationContext getContext() {
		return this.context;
	}
	
    protected ApplicationContext loadContext( String contextPath )  {
        File file = new File(contextPath);
        if (file.exists()) {
            return new FileSystemXmlApplicationContext(contextPath);
        } else {
            return new ClassPathXmlApplicationContext(contextPath);
        }
    }
    
}
