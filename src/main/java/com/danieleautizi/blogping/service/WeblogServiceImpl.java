package com.danieleautizi.blogping.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danieleautizi.blogping.controller.WeblogException;
import com.danieleautizi.blogping.dao.DAOFactory;
import com.danieleautizi.blogping.dao.DaoException;
import com.danieleautizi.blogping.dao.WeblogDAO;
import com.danieleautizi.blogping.model.Weblog;
import com.danieleautizi.blogping.model.WeblogResponse;
import com.danieleautizi.blogping.model.WeblogUpdates;
import com.danieleautizi.blogping.util.Constraint;
import com.danieleautizi.blogping.util.SingletonInitializer;
import com.danieleautizi.blogping.util.WeblogUtil;
import com.danieleautizi.blogping.util.WeblogUtil.ActionType;

/**
 * This class consists of methods to validate input data
 * and to "wrap" persistence layer. 
 * In this way configuring different kind of persistence 
 * units (DB/NoSQL) is possible for each method.
 * Furthermore it provides the logic to choose the 
 * right response.
 * 
 * @author d.autizi
 * @version 1.0
 */
public class WeblogServiceImpl implements WeblogService {

	protected final static Log logger = LogFactory.getLog(WeblogServiceImpl.class);
	
	public WeblogResponse addWeblog(Weblog weblog) throws WeblogException {
		WeblogResponse wResponse = null;

    	// GET PROPERTIES
    	HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
    	wResponse = WeblogUtil.validate(weblog, ActionType.SAVE, prop);
		
    	if (wResponse.isBlacklisted()) {
    		// BLACKLISTED
    		throw new WeblogException(Response.Status.FORBIDDEN, wResponse);
		} else if (Boolean.parseBoolean(wResponse.getSuccess())) {
    		try {
    			// HERE BECAUSE YOU CAN CHOOSE A DIFFERENT DAO IMPL FOR EVERY OPERATION
	    		WeblogDAO wDAO = DAOFactory.getWeblogDAO((String) prop.get(Constraint.PERSISTENCE_PROPERTY));
	    		wDAO.addWeblog(weblog);
    		} catch(DaoException dex) {
    			logger.error("Weblog save FAILED: " + dex, dex);
    			
    			// UPDATE XML RESPONSE
    			wResponse.setSuccess("false");
    			wResponse.setMessage((String) prop.get(Constraint.DOWN_OF_SERVICE));
    			wResponse.setStatus(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
    			
    			throw new WeblogException(Response.Status.SERVICE_UNAVAILABLE, wResponse);
    		}
    	} else {
    		throw new WeblogException(Response.Status.BAD_REQUEST, wResponse);
    	}
    	
		return wResponse;
	}
	
	public WeblogResponse removeWeblog(Weblog weblog) throws WeblogException {
		WeblogResponse wResponse = null;

    	HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
    	wResponse = WeblogUtil.validate(weblog, ActionType.REMOVE, prop);
		
    	if (Boolean.parseBoolean(wResponse.getSuccess())) {
    		try {
	    		WeblogDAO wDAO = DAOFactory.getWeblogDAO((String) prop.get(Constraint.PERSISTENCE_PROPERTY));
	    		wDAO.removeWeblog(weblog);
    		} catch(DaoException dex) {
    			logger.error("Weblog remove FAILED: " + dex, dex);
    			
    			// UPDATE XML RESPONSE
    			wResponse.setSuccess("false");
    			wResponse.setMessage((String) prop.get(Constraint.DOWN_OF_SERVICE));
    			wResponse.setStatus(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
    			
    			throw new WeblogException(Response.Status.SERVICE_UNAVAILABLE, wResponse);
    		}
    	} else {
    		throw new WeblogException(Response.Status.BAD_REQUEST, wResponse);
    	}
    	
		return wResponse;
	}
	
	public Weblog getWeblog(String name) throws WeblogException {
		Weblog weblog = null;
		
		HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
    	
    	if (!StringUtils.isEmpty(name)) {
    		try {
	    		WeblogDAO wDAO = DAOFactory.getWeblogDAO((String) prop.get(Constraint.PERSISTENCE_PROPERTY));
	    		weblog = wDAO.getWeblog(name);
    		} catch(DaoException dex) {
    			logger.error("Weblog get '" + name + "' FAILED: " + dex, dex);
    			
    			// SET XML RESPONSE
    			WeblogResponse wResponse = new WeblogResponse();
    			wResponse.setAction(ActionType.GET.getActionName());
    			wResponse.setSuccess("false");
    			wResponse.setMessage((String) prop.get(Constraint.DOWN_OF_SERVICE));
    			wResponse.setStatus(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
    			wResponse.setCode((String) prop.get(Constraint.DOWN_OF_SERVICE_CODE));
    			
    			throw new WeblogException(Response.Status.SERVICE_UNAVAILABLE, wResponse);
    		}
    	} else {
    		throw new WeblogException(Response.Status.BAD_REQUEST, (String) prop.get(Constraint.NAME_MISS));
    	}
    	
		return weblog;
	}
	
	public WeblogUpdates getWeblogUpdates() throws WeblogException {
		WeblogUpdates wUpdates = null;
		HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
    	
    	try {
    		WeblogDAO wDAO = DAOFactory.getWeblogDAO((String) prop.get(Constraint.PERSISTENCE_PROPERTY));
    		List<Weblog> weblogs = wDAO.getWeblogs();
    		
    		if (weblogs != null) {
    			// RESULT WEBLOG LIST TO PUSH IN WEBLOGS UPDATES
	    		List<Weblog> wbList = new ArrayList<>();
    			wUpdates = new WeblogUpdates();
    		
	    		// DATE FORMAT
	    		Date updatesAskDT = new Date();
	    		String updated = WeblogUtil.getDateTime(WeblogUtil.UPDATES_FORMAT, updatesAskDT, WeblogUtil.DEFAULT_LOCALE);
    			wUpdates.setUpdated(updated);
	    		
    			// VERSION
    			wUpdates.setVersion("1");
    			
    			// FILTER AND ENRICH WEBLOG WITH PING DATETIME (IN SECONDS)
	    		// ITERATOR INSTANCE
    			ListIterator<Weblog> iter = weblogs.listIterator();
    			int weblogSize = weblogs.size();
    			
    			while (iter.hasNext()) {
    				Weblog wb = iter.next();
    				// GET INSERT DATE
	    			Date insertDate = wb.getInsertDate();
	    			int seconds = WeblogUtil.secondsBetween(updatesAskDT, insertDate);
	    			
	    			// FILTER FOR 60 MINUTES MAX 
	    			if (seconds > 3600) {
	    				iter.remove();
	    				weblogSize--;
	    			} else {
	    				wb.setWhen(seconds);
	    				wbList.add(wb);
	    			}
	    		}
	    		
	    		wUpdates.setWeblogs(wbList);
	    		wUpdates.setCount(weblogSize);
    		}
    	} catch(DaoException dex) {
			logger.error("WeblogUpdates for changes.xml FAILED: " + dex);
			
			// SET XML RESPONSE
			WeblogResponse wResponse = new WeblogResponse();
			wResponse.setAction(ActionType.GET.getActionName());
			wResponse.setSuccess("false");
			wResponse.setMessage((String) prop.get(Constraint.DOWN_OF_SERVICE));
			wResponse.setStatus(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
			wResponse.setCode((String) prop.get(Constraint.DOWN_OF_SERVICE_CODE));
			
			throw new WeblogException(Response.Status.SERVICE_UNAVAILABLE, wResponse);
		}

		return wUpdates;
	}

}
