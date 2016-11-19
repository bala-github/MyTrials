package org.bala.MyNotes.resources;

import io.dropwizard.auth.Auth;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bala.MyNotes.Utils.Utils;
import org.bala.MyNotes.configuration.MyNotesConfiguration;
import org.bala.MyNotes.configuration.MyNotesConstant;
import org.bala.MyNotes.handlers.LoginHandler;
import org.bala.MyNotes.views.LogoutView;
import org.bala.MyNotes.views.UserView;
import org.bala.Mynotes.models.Note;
import org.bala.Mynotes.models.MyNotesResponse;
import org.bala.Mynotes.models.User;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.fasterxml.jackson.databind.ObjectMapper;



@Path("/")
public class NotesResource {

	private static final Logger logger = LoggerFactory.getLogger(NotesResource.class);
	
	private LoginHandler loginHandler;
	
	private DbxAppInfo appInfo;	
	
	private  DbxRequestConfig requestConfig;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public NotesResource(MyNotesConfiguration configuration) {
		
		requestConfig = new DbxRequestConfig("MyScrapBook/v1");
		appInfo = new DbxAppInfo(MyNotesConstant.clientId, MyNotesConstant.clientSecret);
		loginHandler = new LoginHandler(appInfo, requestConfig);
	}
	
	@Path("/list")
	@GET
	@Produces("application/json")
	public ListFolderResult getNotes(@Auth User user, @DefaultValue("") @QueryParam("path") String path) {
		
		
		logger.info("Request [/list] for user [" + user.getName() + "]" + "Path[" + path + "]");
		
		DbxClientV2 client = new DbxClientV2(requestConfig, user.getAccessToken());
		
		
		try {
	
			if(path.equalsIgnoreCase("/")) {
				path = ""; 
			}
			ListFolderResult result = client.files().listFolderBuilder(path).withIncludeMediaInfo(true).start();
			logger.info("List Folder Result:" + mapper.writeValueAsString(result));
			return result;
			
		} catch (Exception  e) {
			logger.error("Exception in fetching list." + e.getMessage());
			e.printStackTrace();
			throw new MyNotesException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
		}

	}
	
	@Path("/add")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public FileMetadata addNote(@Auth User user, Note note) {
	
		logger.info("Request [/add] for user [" + user.getName() + "]");
		
		DbxClientV2 client = new DbxClientV2(requestConfig, user.getAccessToken());
		
		
		try {
			
			if(note.getFolder().equalsIgnoreCase("/")) {
				note.setFolder("");
			}
			
			if(!note.getFolder().endsWith("/")) {
				note.setFolder(note.getFolder() + "/");
			}
			
			FileMetadata filemetadata = client.files().uploadBuilder(note.getFolder()  + note.getTitle() +".json")
				.withMode(WriteMode.OVERWRITE)
				.withAutorename(false)
				.withMute(true)
				.uploadAndFinish(new ByteArrayInputStream(mapper.writeValueAsBytes(note)));
			
			logger.debug("Response:" + mapper.writeValueAsString(filemetadata));
			return filemetadata;
		} catch (Exception e) {
			logger.error("Exception in fetching list." + e.getMessage());
			e.printStackTrace();
			throw new MyNotesException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
	}
	
	@Path("/remove")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response removeNote(@Auth User user, Note note) {
	
		logger.info("Request [/remove] for user [" + user.getName() + "]");
		DbxClientV2 client = new DbxClientV2(requestConfig, user.getAccessToken());
		
		try {
		
			if(note.getFolder().equalsIgnoreCase("/")) {
				note.setFolder("");
			}
		
			if(!note.getFolder().endsWith("/")) {
				note.setFolder(note.getFolder() + "/");
			}
			
			client.files().delete(note.getFolder()  + note.getTitle() +".json");
			
			return Response.ok().build();
			
		} catch (Exception e) {
			logger.error("Exception in fetching list." + e.getMessage());
			e.printStackTrace();
			throw new MyNotesException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		
	}	
	
	@Path("/details")
	@POST
	@Produces("application/json")
	public GetTemporaryLinkResult getDetails(@Auth User user, Note note) {
	
		logger.info("Request [/details] for user [" + user.getName() + "]" + "[" + note.getFolder() + "]" + "[" + note.getTitle() + "]");
		DbxClientV2 client = new DbxClientV2(requestConfig, user.getAccessToken());
		
		try {
			
			if(note.getFolder().equalsIgnoreCase("/")) {
				note.setFolder("");
			}
		
			if(!note.getFolder().endsWith("/")) {
				note.setFolder(note.getFolder() + "/");
			}
			return client.files().getTemporaryLink(note.getFolder()  + note.getTitle() +".json");
			
		} catch (DbxException e) {
			logger.error("Exception in fetching list." + e.getMessage() + "-" + e.getMessage());
			e.printStackTrace();
			throw new MyNotesException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		
	}
	
	@GET
	@Path("/login")
	public javax.ws.rs.core.Response login(@QueryParam("code") String code) {
		
		return loginHandler.handleLoginWithOauthCode(code, "https://mynotes.io/login");
	}

	@GET
	@Path("/user")
	@Produces("application/json")
	public User getUserDetails(@Auth User user) {
		logger.info("Request [/user] for user [" + user.getName() + "]");
		
		try {
			
			return user;
		} catch(Exception e) {
			
			logger.error("Exception in fetching list." + e.getMessage() + "-" + e.getMessage());
			e.printStackTrace();
			throw new MyNotesException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		
	}
	
	@GET
	@Path("/access_token")
	@Produces("application/json")
	public Response getUserDetails(@QueryParam("code") String code) {
		logger.info("Request [/user] for user [" + code + "]");
		
		try {
			
			return loginHandler.handleLoginWithOauthCode(code, "");
		} catch(Exception e) {
			
			logger.error("Exception in fetching list." + e.getMessage() + "-" + e.getMessage());
			e.printStackTrace();
			throw new MyNotesException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		
	}	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public UserView welcome(@Auth User user) {
		
		return new UserView(user);
	}
	
	@GET
	@Path("/signout")
	@Produces(MediaType.TEXT_HTML)
	public LogoutView signout(@Auth User user) {
		
		return new LogoutView(user);
	}
	
	@GET
	@Path("/test")
	public List<Note> getNotes2() {
		
		System.out.println("getNotes2");
		return null;
	}
}
