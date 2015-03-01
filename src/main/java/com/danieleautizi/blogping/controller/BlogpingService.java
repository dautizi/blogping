package com.danieleautizi.blogping.controller;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danieleautizi.blogping.model.Weblog;
import com.danieleautizi.blogping.model.WeblogResponse;
import com.danieleautizi.blogping.model.WeblogUpdates;
import com.danieleautizi.blogping.service.WeblogService;
import com.danieleautizi.blogping.service.WeblogServiceImpl;
import com.danieleautizi.blogping.util.Constraint;
import com.danieleautizi.blogping.util.SingletonInitializer;
import com.danieleautizi.blogping.util.WeblogUtil;
import com.sun.jersey.spi.resource.Singleton;


/**
 * This class is the rest WS entry point and consists 
 * of GET/POST methods that external consumers can call. 
 * The constructor instantiates properties are used to 
 * set and call the service layer to control/process 
 * weblog beans.
 * 
 * @author d.autizi
 * @version 1.0
 */
@Path("/blogping")
@Singleton
public class BlogpingService implements Constraint {

    	private final static Log logger = LogFactory.getLog(BlogpingService.class);
    
    	HashMap<String, Object> properties = null;
    	String persistenceSource = DEFAULT_PERSISTENCE_SOURCE;

	public BlogpingService(@Context ServletContext context) throws WeblogException {
		super();
		SingletonInitializer sa = SingletonInitializer.getInstance();
		properties = sa.getProperties();
		persistenceSource = (String) properties.get(PERSISTENCE_PROPERTY);
	}


    	@GET
    	@Path("/changes.xml")
    	@Produces(MediaType.APPLICATION_XML)
	public WeblogUpdates doGetChanges() throws WeblogException {
		logger.info("get changes.xml service");
		
		// SERVICE INSTANCE
		WeblogService service = new WeblogServiceImpl();
		WeblogUpdates changes = service.getWeblogUpdates();
		
		if (changes == null) {
			String errorDescription = (String) SingletonInitializer.getInstance().getProperties().get(DOWN_OF_SERVICE);
			throw new WeblogException(Response.Status.INTERNAL_SERVER_ERROR, errorDescription);
		}

		return changes;
	}


    	@GET
    	@Path("/pingSiteForm")
    	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	public Response doGetAddWeblog(@QueryParam("name") String name, 
			@QueryParam("url") String url,
			@Context UriInfo ui) throws WeblogException {
		
		logger.info("pingSiteForm query param: Name="+name+", url="+url);
		
		// DECODE URL
		url = WeblogUtil.decodeUrl(url);

		// CHECK EXTRA PARAM EXISTENCE IN THE REQUEST
		MultivaluedMap<String, String> parameters = ui.getQueryParameters();
		boolean extraParamExistence = WeblogUtil.paramNotAllowedExistence(parameters, Constraint.ALLOWED_PARAMS);
		
		if (!extraParamExistence) {
			Weblog wb = new Weblog();
			wb.setName(name);
			wb.setUrl(url);
			wb.setInsertDate(new Date());
			
			// ADD WEBLOG THROUGH SERVICE
			WeblogService service = new WeblogServiceImpl();
			// IF YOU WANT A SPECIFIC XML RESPONSE TYPE
			// WeblogResponse response = service.addWeblog(wb);
			service.addWeblog(wb);
		} else {
			String errorDescription = (String) SingletonInitializer.getInstance().getProperties().get(UNKNOWN_PARAMETERS);
			throw new WeblogException(Response.Status.BAD_REQUEST, errorDescription);
		}

		return Response.status(Status.OK).entity("Thanks for the ping.").build();
	}
	
	
    	@POST
	@Path("/pingSiteForm")
    	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    	@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
	public Response doPostWeblogForm(@FormParam("name") String name, 
			@FormParam("url") String url) throws WeblogException {
		
		logger.info("pingSiteForm post params: Name=" + name + ", url=" + url);
		
		// DECODE URL
		url = WeblogUtil.decodeUrl(url);
		
		Weblog wb = new Weblog();
		wb.setName(name);
		wb.setUrl(url);
		wb.setInsertDate(new Date());
			
		// ADD WEBLOG THROUGH SERVICE
		WeblogService service = new WeblogServiceImpl();
		// IF YOU WANT A SPECIFIC XML RESPONSE TYPE
		WeblogResponse response = service.addWeblog(wb);
		
		return Response.status(Status.OK).entity("Thanks for the ping.").build();
	}

}
