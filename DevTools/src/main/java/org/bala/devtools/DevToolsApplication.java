package org.bala.devtools;

import org.bala.devtools.resources.ActionResource;
import org.bala.devtools.resources.DevToolsExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DevToolsApplication extends Application<DevToolsConfiguration>  {

	private static final Logger logger = LoggerFactory.getLogger(DevToolsApplication.class);

	public static void main(String[] args) throws Exception {
		new DevToolsApplication().run(args);
	}

	@Override
	public String getName(){
		return "DevTools";
	}

    @Override
    public void initialize(Bootstrap<DevToolsConfiguration> bootstrap) {
        // nothing to do yet
    	super.initialize(bootstrap);
    	bootstrap.addBundle(new AssetsBundle("/html", "/", "devtools.html", "html"));
    	bootstrap.addBundle(new AssetsBundle("/scripts", "/scripts", "", "scripts"));
    }	
    
	@Override
	public void run(DevToolsConfiguration config, Environment environment)throws Exception {
		final ActionResource resource = new ActionResource();
		
		environment.jersey().register(resource);
		environment.jersey().register(new DevToolsExceptionMapper());
	}
	
	
	

}
