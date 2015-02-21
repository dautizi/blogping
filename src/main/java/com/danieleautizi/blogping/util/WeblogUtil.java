package com.danieleautizi.blogping.util;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.UrlValidator;

import com.danieleautizi.blogping.controller.WeblogException;
import com.danieleautizi.blogping.model.Weblog;
import com.danieleautizi.blogping.model.WeblogResponse;


/**
 * This class instantiates static utility methods
 * to validate input Weblog data and convert Date
 * in different formats.
 * 
 * @author d.autizi
 * @version 1.0
 */
public class WeblogUtil implements Constraint {

	protected final static Log logger = LogFactory.getLog(WeblogUtil.class);
	
	public enum ActionType {
        SAVE("save", "save or update blog"),
        REMOVE("remove", "remove blog"),
        GET("get", "get a specific blog");

        private String actionName;

        private String actionDescription;

        ActionType(String actionName, String actionDescription) {
            this.actionName = actionName;
            this.actionDescription = actionDescription;
        }

        public String getActionName() {
            return actionName;
        }
        
        public String getActionDescription() {
            return actionDescription;
        }
    }
	
	public static String DATE_TIME_PATTERN_SS = "dd/MM/yyyy HH:mm:ss";

    public static String UPDATES_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static Locale DEFAULT_LOCALE = Locale.ENGLISH;
    
    /**
     * Calculate difference in seconds between 2 datetime
     * instances
     * @param newer date
     * @param older date
     * @return seconds
     */
    public static int secondsBetween(Date newer, Date older) {
    	int difference = 0;
    	
    	long seconds = (newer.getTime() - older.getTime()) / 1000;
    	difference = new BigDecimal(seconds).intValueExact();
    	
    	return difference;
    }
    
    
    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     *
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate, Locale locale) {
        SimpleDateFormat sdf = null;
        String returnValue = "";

        if (aDate != null) {
            sdf = new SimpleDateFormat(aMask, locale);
            returnValue = sdf.format(aDate);
        }

        return returnValue;
    }
    
    public static String convertDateStringFormat(String date, String inPattern, String outPattern){
		String formattedDate = null;
		SimpleDateFormat outgoingDateFormat = new SimpleDateFormat(outPattern);
		
		try {
			formattedDate = outgoingDateFormat.format(convertStringToDate(inPattern,date));
		} catch (ParseException e) {
			throw new RuntimeException("Expected date format ["+date+"]. Input format "+inPattern);
		}
		
		return formattedDate;
	}
    
    public static String convertDateStringFormat(String date, String inPattern, String outPattern,Locale local){
		String formattedDate = null;
		SimpleDateFormat outgoingDateFormat = new SimpleDateFormat(outPattern,local);
		
		try {
			formattedDate = outgoingDateFormat.format(convertStringToDate(inPattern,date,local));
		} catch (ParseException e) {
			throw new RuntimeException("Expected date format ["+date+"]. Input format "+inPattern);
		}
		
		return formattedDate;
	}
    
    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }
    
    public static final Date convertStringToDate(String aMask, String strDate, Locale local) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask,local);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }
    
	
	/**
	 * Returns a WeblogResponse after validate Weblog input. 
	 * The url must be valid considering configured max characters 
	 * no. and only allowed parameters. 
	 * The name must be valid considering configured max characters no. 
	 *
	 * @param 	weblog			weblog bean with name and url set
	 * @param 	actionType 		type of requested action 
	 * @param 	configuration	properties 
	 * @return  response bean
	 */
    public static WeblogResponse validate(Weblog weblog, ActionType actionType, HashMap<String, Object> configuration) {
    	WeblogResponse wresponse = null;
    	
    	boolean nameValidation = false, // NAME IS NOT BLANK OR NULL
    			urlValidation  = false, // URL IS VALID
    			blacklisted    = false;	// IN BLACKLIST
    	
    	String name = weblog.getName();
    	String url = weblog.getUrl();
    	String action = actionType.getActionName();
    	    	
    	if (configuration == null) {
    		configuration = SingletonInitializer.getInstance().getProperties();
    	}
    	
    	// name= <Name of Blog> (limited to 1024 characters)
		int blogNameLimit = Integer.parseInt(configuration.get(Constraint.NAME_MAX_LENGTH).toString());
		if (!StringUtils.isEmpty(name) && name.length() <= blogNameLimit) {
			nameValidation = true;
		}
		
		// url= <URL of Blog> (limited to 255 characters)
		int blogUrlLimit = (int) Integer.parseInt(configuration.get(Constraint.URL_MAX_LENGTH).toString());
		if (!StringUtils.isEmpty(url) && url.length() <= blogUrlLimit) {
			// CHECK URL VALIDATION
			urlValidation = validateUrl(url);
		}
		
		// CHECK BLACKLIST
		if (urlValidation) {
			List<String> blacklist = SingletonInitializer.getInstance().getBlacklist();
			blacklisted = blacklistedHost(url, blacklist);
		}
		
		// RESPONSE SET CHOICE
		if (nameValidation && urlValidation) {
			// SUCCESS
			wresponse = new WeblogResponse();
			wresponse.setAction(action);
			
			if (blacklisted) {
				wresponse.setCode(configuration.get(HOST_BLACKLISTED_CODE).toString());
				wresponse.setMessage(configuration.get(HOST_BLACKLISTED).toString());
				wresponse.setSuccess(Boolean.FALSE.toString());
				wresponse.setStatus(Response.Status.FORBIDDEN.getStatusCode());
			} else {
				wresponse.setCode(configuration.get(WEBLOG_SAVE_CODE).toString());
				wresponse.setMessage(configuration.get(WEBLOG_SAVE).toString());
				wresponse.setSuccess(Boolean.TRUE.toString());
				wresponse.setStatus(Response.Status.OK.getStatusCode());
			}
		} else {
			// WRONG CASE
			wresponse = new WeblogResponse();
			wresponse.setAction(action);
			wresponse.setSuccess(Boolean.FALSE.toString());
			wresponse.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
			
			// BOTH NAME AND URL WRONG
			if (!nameValidation && !urlValidation) {
				// INVALID URL AND NAME
				wresponse.setCode(configuration.get(NAME_URL_INVALID_CODE).toString());
				wresponse.setMessage(configuration.get(Constraint.NAME_URL_INVALID).toString());
			} else if (!nameValidation) {
				// WRONG NAME
				if (StringUtils.isEmpty(name)) {
					// NAME BLANK OR NULL
					wresponse.setCode(configuration.get(NAME_MISS_CODE).toString());
					wresponse.setMessage(configuration.get(Constraint.NAME_MISS).toString());
				} else {
					wresponse.setCode(configuration.get(NAME_OVERSIZED_CODE).toString());
					wresponse.setMessage(configuration.get(Constraint.NAME_OVERSIZED).toString());
				}
			} else if (!urlValidation) {
				// WRONG URL
				if (StringUtils.isEmpty(url)) {
					// URL BLANK OR NULL
					wresponse.setCode(configuration.get(URL_MISS_CODE).toString());
					wresponse.setMessage(configuration.get(Constraint.URL_MISS).toString());
				} else {
					// URL OVERSIZED OR NOT VALID SYNTAX
					wresponse.setCode(configuration.get(URL_NOT_VALID_CODE).toString());
					wresponse.setMessage(configuration.get(Constraint.URL_NOT_VALID).toString());
				}
			}
		}
		
    	return wresponse;
    }
	
    
    /**
     * Checks if submitted url's host is blacklisted
     * @param url to check
     * @param blacklistedList blacklist load to startup
     * @return boolean validation
     */
    public static boolean blacklistedHost(String url, List<String> blacklistedList) {
    	boolean isBlackListed = false;
    	URL aURL = null;
		String host = null;
    	
    	try {
			aURL = new URL(url);
			host = aURL.getHost();
			
			logger.info("BLACKLIST HOSTS: " + blacklistedList.toString());
			
			if (blacklistedList != null && blacklistedList.contains(host)) {
				isBlackListed = true;
				logger.info("Host " + host + " is blacklisted");
			} else {
				logger.info("Host " + host + " not in blacklist");
			}
		} catch (MalformedURLException e) {
			logger.debug("blacklistedHost control: " + e);
		}		
    	
    	return isBlackListed;
    }
    
    /**
     * Validates submitted url
     * @param url submitted url
     * @return boolean validation
     */
	public static boolean validateUrl(String url) {
		boolean isValid = false;
		String URL_REGEX = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
		
		if (!StringUtils.isEmpty(url)) {
			// FIRST CONTROL THROUGH APACHE URL VALIDATOR
			UrlValidator urlValidator = new UrlValidator();
			boolean firstCheck = urlValidator.isValid(url);
			
			// SECOND BY REG EXP
			Pattern p = Pattern.compile(URL_REGEX);
			Matcher m = p.matcher(url);
			
			if (firstCheck && m.find()) {
				url = m.group();
				isValid = true;
			}
			
			// THIRD LEVEL 
			/*
				try {
				    URL url = new URL("http://www.danieleautizi.com/");
				    URLConnection conn = url.openConnection();
				    conn.connect();
				} catch (MalformedURLException e) {
				    // the URL is not in a valid form
				} catch (IOException e) {
				    // the connection couldn't be established
				}
			 */
		}
		return isValid;
	}
	
	/**
	 * Decodes url using URIUtil class
	 * @param url to decode 
	 * @return decoded url
	 * @throws URIException
	 */
	public static String decodeUrl(String url) {
		HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
		
		try {
			return URIUtil.decode(url);
		} catch (URIException uie) {
			logger.error("url decoding error: " + uie);
			
			// SET XML RESPONSE
			WeblogResponse wResponse = new WeblogResponse();
			wResponse.setAction(ActionType.GET.getActionName());
			wResponse.setSuccess("false");
			wResponse.setMessage((String) prop.get(Constraint.URL_WRONG_ENCODING));
			wResponse.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
			wResponse.setCode((String) prop.get(Constraint.URL_WRONG_ENCODING_CODE));
			
			throw new WeblogException(Response.Status.BAD_REQUEST, wResponse);
		}
	}
	
	/**
	 * Validates request parameters
	 * @param parameters map from request
	 * @param accepted parameters from configuration
	 * @return boolean validation
	 */
	public static boolean paramNotAllowedExistence(MultivaluedMap<String, String> parameters, List<String> accepted) {
		boolean isValid = false;
		
		if (parameters != null) {
			Iterator<String> it = parameters.keySet().iterator();
			
			while(it.hasNext()) {
				String key = (String) it.next();
				if (!accepted.contains(key)) {
					logger.error("NOT ACCEPTED key: " + key + ", value: " + parameters.getFirst(key));
					isValid = true;
					break;
				}
			}
		}
		
		return isValid;
	}
	
	
	public static Map<String, String> getQueryMap(String url) {
		Map<String, String> map = null;
	    
		if (!StringUtils.isEmpty(url)) {
			map = new HashMap<String, String>();
			String[] params = url.split("&");
		    
			if (params != null && params.length > 0) {
			    for (String param : params) {
			    	String name = param.split("=")[0];
			    	String value = null;
			    	
			    	if (param.split("=").length > 1) {
			    		value = param.split("=")[1];
			    	}
			    	
			    	map.put(name, value);
			    }
			}
	    }
	    return map;
	}
	
	private static boolean acceptParameters(String url, String[] accepted) {
		boolean isValid = true;
		
		// MAKE QUERY MAP
		Map<String, String> queryMap = getQueryMap(url);
		
		// CHECK ACCEPTABLE
		if (queryMap != null && accepted != null && queryMap.size() >= accepted.length) {
			for (String param : accepted) {
				
				// KEY EXISTENCE
				if (!queryMap.containsKey(param)) { 
					// DO NOT CHECK UPPER/LOWER CASE
					isValid = false;
				} else {
					// CHECK VALUE BLANK OR NULL
					if (StringUtils.isEmpty(queryMap.get(param))) {
						isValid = false;
					}
				}
			}
		} else {
			isValid = false;
		}
		return isValid;
	}
	
}
