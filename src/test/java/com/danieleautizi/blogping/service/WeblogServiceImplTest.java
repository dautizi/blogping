package com.danieleautizi.blogping.service;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import com.danieleautizi.blogping.controller.WeblogException;
import com.danieleautizi.blogping.model.Weblog;
import com.danieleautizi.blogping.model.WeblogResponse;
import com.danieleautizi.blogping.model.WeblogUpdates;

public class WeblogServiceImplTest {

	@Test
    public void check_save_valid_weblog_response() throws WeblogException {
		String name = "DanieleAutiziBlog";
        String url = "http://www.danieleautizi.com";
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
        WeblogServiceImpl wsi = new WeblogServiceImpl();
        WeblogResponse wResponse = wsi.addWeblog(wbInput);
    	
        boolean response = Boolean.parseBoolean(wResponse.getSuccess());
        
        Assert.assertTrue("Validation result for a valid weblog: ", response);
    }
	
	@Test
    public void check_save_notvalid_weblog_response() throws WeblogException {
		String name = "DanieleAutiziBlog";
        String url = "http://com"; // NOT VALID URL
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());

    	WeblogServiceImpl wsi = new WeblogServiceImpl();
        try {
        	wsi.addWeblog(wbInput);
        } catch(WeblogException ex) {
        	WeblogResponse wResponse = (WeblogResponse) ex.getResponse().getEntity();
        	boolean result = Boolean.parseBoolean(wResponse.getSuccess());
        	Assert.assertFalse("Validation result for a not valid weblog: ", result);
        }
    }

	@Test
    public void get_weblog() throws WeblogException {
		String name = "DanieleAutiziBlog";
        String url = "http://www.danieleautizi.com";
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());

        WeblogServiceImpl wsi = new WeblogServiceImpl();
        wsi.addWeblog(wbInput);
    	
        Weblog wbOutput = wsi.getWeblog(name);
        
        Assert.assertEquals("Get Weblog expected ", url, wbOutput.getUrl());
    }
	
	@Test
    public void get_not_existing_weblog() throws WeblogException {
		String name = "DanieleAutiziBlog";
		String wrongName = "AutiziDaniele";
        String url = "http://www.danieleautizi.com";
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());

        WeblogServiceImpl wsi = new WeblogServiceImpl();
        wsi.addWeblog(wbInput);
    	
        Weblog wbOutput = wsi.getWeblog(wrongName);
    	
        Assert.assertNull(wbOutput);
    }

	@Test
    public void save_notvalid_name_weblog() throws WeblogException {
		int oversizeCharNumber = 1025;
		String name = RandomStringUtils.randomAlphabetic(oversizeCharNumber); // NOT VALID NAME
		String url = "http://www.danieleautizi.com";
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
        WeblogServiceImpl wsi = new WeblogServiceImpl();
        try {
        	wsi.addWeblog(wbInput);
        } catch(WeblogException ex) {
        	int status = ex.getResponse().getStatus();
        	Assert.assertEquals(400, status);
        }
    }
	
	@Test
    public void save_notvalid_url_weblog() throws WeblogException {
		int oversizeCharNumber = 240;
		String urlExt = RandomStringUtils.randomAlphabetic(oversizeCharNumber);
		String name = "DanieleAutiziBlog";
		String url = "http://www.danieleautizi.com/" + urlExt; // NOT VALID URL
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
        WeblogServiceImpl wsi = new WeblogServiceImpl();
        try {
        	wsi.addWeblog(wbInput);
        } catch(WeblogException ex) {
        	int status = ex.getResponse().getStatus();
        	Assert.assertEquals(400, status);
        	
        	WeblogResponse wResponse = (WeblogResponse) ex.getResponse().getEntity();
        	int code = Integer.parseInt(wResponse.getCode());
        	Assert.assertEquals("Custom error code expected: 13 ", 13, code);
        }
    }
	
	@Test
    public void remove_valid_weblog() throws WeblogException {
		String name = "DanieleAutiziBlog";
		String url = "http://www.danieleautizi.com/";
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
        WeblogServiceImpl wsi = new WeblogServiceImpl();
        wsi.removeWeblog(wbInput);
    	
        Weblog wbOutput = wsi.getWeblog(name);
    	
        Assert.assertNull(wbOutput);
    }
	
	@Test
    public void get_weblog_updates() throws WeblogException {
		WeblogServiceImpl wsi = new WeblogServiceImpl();
		String name1 = "DanieleAutiziBlog1";
        String url1 = "http://www.danieleautizi.com";
        Weblog wbInput1 = new Weblog();
        wbInput1.setName(name1);
        wbInput1.setUrl(url1);
        wbInput1.setInsertDate(new Date());
        wsi.addWeblog(wbInput1);
        
        String name2 = "DanieleAutiziBlog2";
        String url2 = "http://www.danieleautizi.com/bis";
        Weblog wbInput2 = new Weblog();
        wbInput2.setName(name2);
        wbInput2.setUrl(url2);
        wbInput2.setInsertDate(new Date());
        wsi.addWeblog(wbInput2);
        
        String name3 = "DanieleAutiziBlog3";
        String url3 = "http://www.danieleautizi.com/tris";
        Weblog wbInput3 = new Weblog();
        wbInput3.setName(name3);
        wbInput3.setUrl(url3);
        wbInput3.setInsertDate(new Date());
        wsi.addWeblog(wbInput3);
        
        WeblogUpdates list = wsi.getWeblogUpdates();
    	
        Assert.assertEquals("WeblogUpdates elements: ", 3, (int)list.getCount());
    }
	
	@Test
    public void set_outofdate_ping_and_verify_updates_count() throws WeblogException {
		WeblogServiceImpl wsi = new WeblogServiceImpl();
		int count = 0;
		if (wsi.getWeblogUpdates() != null) {
			count = wsi.getWeblogUpdates().getCount();
		}
		
		int expectedCount = count + 2;
		
		String name1 = "DanieleAutiziBlog4";
        String url1 = "http://www.danieleautizi.com";
        Weblog wbInput1 = new Weblog();
        wbInput1.setName(name1);
        wbInput1.setUrl(url1);
        
        Date now = new Date();
        // OUT OF DATETIME PING
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, -2);
        Date outOfDatetime = cal.getTime();
        wbInput1.setInsertDate(outOfDatetime);
        wsi.addWeblog(wbInput1);
                
        String name2 = "DanieleAutiziBlog5";
        String url2 = "http://www.danieleautizi.com/bis";
        Weblog wbInput2 = new Weblog();
        wbInput2.setName(name2);
        wbInput2.setUrl(url2);
        wbInput2.setInsertDate(new Date());
        wsi.addWeblog(wbInput2);
        
        String name3 = "DanieleAutiziBlog6";
        String url3 = "http://www.danieleautizi.com/tris";
        Weblog wbInput3 = new Weblog();
        wbInput3.setName(name3);
        wbInput3.setUrl(url3);
        wbInput3.setInsertDate(new Date());
        wsi.addWeblog(wbInput3);
    	
        WeblogUpdates list = wsi.getWeblogUpdates();
    	count = list.getCount();
    	
        Assert.assertEquals("WeblogUpdates elements: " + expectedCount, expectedCount, count);
    }

}
