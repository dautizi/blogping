package com.danieleautizi.blogping.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.danieleautizi.blogping.model.Weblog;
import com.danieleautizi.blogping.model.WeblogResponse;
import com.danieleautizi.blogping.util.WeblogUtil.ActionType;


public class WeblogUtilTest {

	@Test
    public void get_query_map_from_querystring() {
		String queryString = "param1=ciao&param2=&param3=xanti"; // VALID URL
        
		Map<String, String> paramMap = WeblogUtil.getQueryMap(queryString);
		
		Assert.assertEquals("Query param1 expected: ", "ciao", paramMap.get("param1"));
        Assert.assertNull("Query param2 expected: blank or null ", paramMap.get("param2"));
        Assert.assertEquals("Query param3 expected: ", "xanti", paramMap.get("param3"));
    }
	
	@Test
    public void validate_valid_url() {
		String name = "DanieleAutiziBlog";
		String url = "http://www.danieleautizi.com/rest?param1=ciao&param2=&param3=xanti"; // VALID URL
        
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
		
		HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
		WeblogResponse wbResponse = WeblogUtil.validate(wbInput, ActionType.SAVE, prop);
		
		Assert.assertEquals("Action expected: ", "save", wbResponse.getAction());
        Assert.assertEquals("Success expected: ", "true", wbResponse.getSuccess());
        Assert.assertEquals("Result code expected: ", "11", wbResponse.getCode());
    }
	
	@Test
    public void validate_notvalid_url() {
		String name = "DanieleAutiziBlog";
		String url = "http://.com/"; // VALID URL
        
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
		HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
		WeblogResponse wbResponse = WeblogUtil.validate(wbInput, ActionType.SAVE, prop);
				
		Assert.assertEquals("Action expected: ", "save", wbResponse.getAction());
        Assert.assertEquals("Success expected: ", "false", wbResponse.getSuccess());
        Assert.assertEquals("Result code expected: ", "13", wbResponse.getCode());
    }
	
	@Test
    public void format_datetime() throws ParseException {
		String dateToCheck = "Mon, 16 Feb 2015 07:38:55 CET";
		Date predefined = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz").parse("2015-02-16 07:38:55 CET");
		
		String updated = WeblogUtil.getDateTime(WeblogUtil.UPDATES_FORMAT, predefined, WeblogUtil.DEFAULT_LOCALE);
				
		Assert.assertEquals("Datetime expected: ", dateToCheck, updated);
    }
	
	@Test
    public void seconds_between() throws ParseException {
		int differenceInSeconds = 27;
		Date newer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-02-16 07:39:22");
		Date older = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-02-16 07:38:55");
		
		int seconds = WeblogUtil.secondsBetween(newer, older);
				
		Assert.assertEquals("difference in seconds expected: ", differenceInSeconds, seconds);
    }

    @Test
    public void decode_url_with_parameters() throws URIException {
		String name = "DanieleAutiziBlog7";
		String url = "http://www.danieleautizi.com/marta?blogtv=12&streaming=true"; // VALID URL
        
		url = URIUtil.encodeWithinQuery(url);
		url = WeblogUtil.decodeUrl(url);
		
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
		HashMap<String, Object> prop = SingletonInitializer.getInstance().getProperties();
		WeblogResponse wbResponse = WeblogUtil.validate(wbInput, ActionType.SAVE, prop);
				
		Assert.assertEquals("Action expected: ", "save", wbResponse.getAction());
        Assert.assertEquals("Success expected: ", "true", wbResponse.getSuccess());
    }
}
