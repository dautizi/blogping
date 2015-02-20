package com.danieleautizi.blogping.service;

import com.danieleautizi.blogping.controller.WeblogException;
import com.danieleautizi.blogping.model.Weblog;
import com.danieleautizi.blogping.model.WeblogResponse;
import com.danieleautizi.blogping.model.WeblogUpdates;


/**
 * The service layer is the unit that validates and processes
 * input Weblog data, hiding the processing way to ws methods.
 * In this way application ensures decoupling between ws definition
 * and implementative logic.
 * 
 * @author d.autizi
 * @version 1.0
 */
public interface WeblogService {

	public WeblogResponse addWeblog(Weblog weblog) throws WeblogException;
	
	public WeblogResponse removeWeblog(Weblog weblog) throws WeblogException;
	
	public Weblog getWeblog(String name) throws WeblogException;
	
	public WeblogUpdates getWeblogUpdates() throws WeblogException;
	
}
