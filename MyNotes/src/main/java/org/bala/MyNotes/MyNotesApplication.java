package org.bala.MyNotes;

import org.bala.MyNotes.configuration.MyNotesConfiguration;
import org.bala.MyNotes.filters.MyNotesAuthFilter;
import org.bala.MyNotes.filters.MyNotesAuthenticator;
import org.bala.MyNotes.filters.SessionRefreshFilter;
import org.bala.MyNotes.resources.NotesResource;
import org.bala.Mynotes.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class MyNotesApplication extends Application<MyNotesConfiguration> {

	private static final Logger logger = LoggerFactory.getLogger(MyNotesApplication.class);
	
	public static void main(String[] args) throws Exception {
		
		new MyNotesApplication().run(args);
	}

   @Override
    public String getName() {
        return "MyNotes";
    }

    @Override
    public void initialize(Bootstrap<MyNotesConfiguration> bootstrap) {
        
    	super.initialize(bootstrap);
    	//for request coming under path /html look under /html/ directory
    	bootstrap.addBundle(new AssetsBundle("/html", "/html", "mynotes.html", "html"));
    	bootstrap.addBundle(new ViewBundle<MyNotesConfiguration>());
    }
	    
	@Override
	public void run(MyNotesConfiguration configuration, Environment environment)
			throws Exception {
	
		final NotesResource notes = new NotesResource(configuration);
		
		//environment.jersey().setUrlPattern("/api/*");		
		environment.jersey().register(notes);
		
		
		environment.jersey().register(new AuthDynamicFeature(
	            new MyNotesAuthFilter.Builder<User>()
	                .setAuthenticator(new MyNotesAuthenticator())	           
	                .setRealm("SUPER SECRET STUFF")
	                .buildAuthFilter()));
	   
	    //If you want to use @Auth to inject a custom Principal type into your resource
	    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
	   
	    environment.jersey().register(SessionRefreshFilter.class);
	}
	
	

}
