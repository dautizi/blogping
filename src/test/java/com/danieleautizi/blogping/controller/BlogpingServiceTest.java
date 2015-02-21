package com.danieleautizi.blogping.controller;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class BlogpingServiceTest {
	
	private HttpClient client = new HttpClient();
	private String wsBaseUrl = "http://localhost:8079/";
	
    @Test
    public void send_update_and_verify_response() throws IOException {
        String name = "DanieleAutiziBlogEncoded";
        String url = "http://www.danieleautizi.com/marta?webtv=4&streaming=true";

        GetMethod ping = new GetMethod(wsBaseUrl + "blogping/pingSiteForm?name=" + name
            + "&url=" + URIUtil.encodeWithinQuery(url));

        try {
            client.executeMethod(ping);
            Assert.assertEquals("Thanks for the ping.", ping.getResponseBodyAsString());
        } finally {
            ping.releaseConnection();
        }

        GetMethod changes = new GetMethod(wsBaseUrl + "blogping/changes.xml");

        try {
            client.executeMethod(changes);
            Assert.assertTrue(changes.getResponseBodyAsString().contains(name));
        } finally {
            changes.releaseConnection();
        }
    }
    
    @Test
    public void send_notvalid_url_param() throws IOException {
        String name = "DanieleAutiziBlog";
        String url 	= "http://danieleautizi";

        GetMethod ping = new GetMethod(wsBaseUrl + "blogping/pingSiteForm?name=" + name
            + "&url=" + URIUtil.encodeWithinQuery(url));

        try {
            client.executeMethod(ping);
            Assert.assertTrue(ping.getResponseBodyAsString().contains("400"));
            Assert.assertTrue(ping.getResponseBodyAsString().contains("<code>13</code>"));
        } finally {
            ping.releaseConnection();
        }
    }
    
    @Test
    public void send_notvalid_name_param() throws IOException {
    	int oversizeCharNumber = 1025;
		String name = RandomStringUtils.randomAlphabetic(oversizeCharNumber); // NOT VALID NAME
        String url = "http://www.danieleautizi.com";

        GetMethod ping = new GetMethod(wsBaseUrl + "blogping/pingSiteForm?name=" + name
            + "&url=" + URIUtil.encodeWithinQuery(url));

        try {
            client.executeMethod(ping);
            Assert.assertTrue(ping.getResponseBodyAsString().contains("Name parameter length is out of limit"));
            Assert.assertTrue(ping.getResponseBodyAsString().contains("<code>2</code>"));
        } finally {
            ping.releaseConnection();
        }
    }
    
    @Test
    public void send_without_name() throws IOException {
    	String name = "";
        String url = "http://www.danieleautizi.com";

        GetMethod ping = new GetMethod(wsBaseUrl + "blogping/pingSiteForm?name=" + name
            + "&url=" + URIUtil.encodeWithinQuery(url));

        try {
            client.executeMethod(ping);
            Assert.assertTrue(ping.getResponseBodyAsString().contains("Name parameter miss"));
            Assert.assertTrue(ping.getResponseBodyAsString().contains("<code>3</code>"));
        } finally {
            ping.releaseConnection();
        }
    }
    
    @Test
    public void send_blacklisted_host() throws IOException {
        String name = "DanieleAutiziBlog";
        String url = "http://google.com";

        GetMethod ping = new GetMethod(wsBaseUrl + "blogping/pingSiteForm?name=" + name
            + "&url=" + URIUtil.encodeWithinQuery(url));

        try {
            client.executeMethod(ping);
            Assert.assertTrue(ping.getResponseBodyAsString().contains("Host defined is in blacklist"));
            Assert.assertTrue(ping.getResponseBodyAsString().contains("<code>6</code>"));
        } finally {
            ping.releaseConnection();
        }
    }
    
    @Test
    public void send_post_and_verify_response() throws IOException {
        String name = "DanieleAutiziBlog";
        String url = "http://www.danieleautizi.com";

        PostMethod ping = new PostMethod(wsBaseUrl + "blogping/pingSiteForm");
        ping.setParameter("name", name);
        ping.setParameter("url", url);
        
        try {
            int code = client.executeMethod(ping);
            Assert.assertEquals("Save code: 200", 200, code);
            Assert.assertEquals("Thanks for the ping.", ping.getResponseBodyAsString());
        } finally {
            ping.releaseConnection();
        }
        
        GetMethod changes = new GetMethod(wsBaseUrl + "blogping/changes.xml");

        try {
            client.executeMethod(changes);
            Assert.assertTrue(changes.getResponseBodyAsString().contains(name));
            Assert.assertTrue(changes.getResponseBodyAsString().contains(url));
        } finally {
            changes.releaseConnection();
        }
    }
    
    @Test
    public void get_changes_and_verify_response() throws IOException {
        GetMethod ping = new GetMethod(wsBaseUrl + "blogping/changes.xml");
        
        try {
            int code = client.executeMethod(ping);
            Assert.assertEquals("Changes.xml get response: 200", 200, code);
            Assert.assertNotNull(ping.getResponseBodyAsString());
        } finally {
            ping.releaseConnection();
        }
    }

}
