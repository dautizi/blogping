package com.danieleautizi.blogping.controller;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.danieleautizi.blogping.model.WeblogResponse;

public class WeblogException extends WebApplicationException {

	private static final long serialVersionUID = -7765770754818419211L;
	
	public WeblogException() {
		super();
	}

	public WeblogException(String message) {
		super(Response.status(Response.Status.BAD_REQUEST)
				.entity(message).type(MediaType.APPLICATION_XML).build());
	}
	
	public WeblogException(Status status, String message) {
		super(Response.status(status)
				.entity(message).type(MediaType.TEXT_HTML).build());
	}
	
	public WeblogException(Status status, WeblogResponse weblogResponse) {
		super(Response.status(status)
				.entity(weblogResponse).type(MediaType.APPLICATION_XML).build());
	}
		
	public WeblogException(Throwable cause) {
		super(cause);
	}
 
}
