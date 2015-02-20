package com.danieleautizi.blogping.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.danieleautizi.blogping.model.Weblog;

public class HashDBTest {

	@Test
    public void save_get_same_weblog() throws DaoException {
		String name = "DanieleAutiziBlog";
        String url = "http://www.danieleautizi.com";

        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        
        HashDB hashDB = HashDB.getConcurrentHashMapInstance();
        hashDB.addWeblog(wbInput);
        
        hashDB.getWeblog(name);
        
        Assert.assertEquals(wbInput.getUrl(), hashDB.getWeblog(name).getUrl());
    }
	
	@Test
    public void remove_get_same_weblog() throws DaoException {
		String name = "DanieleAutiziBlog";
        String url = "http://www.danieleautizi.com";
        Weblog nullWeblog = null;
        
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
        HashDB hashDB = HashDB.getConcurrentHashMapInstance();
        hashDB.addWeblog(wbInput);
        
        Weblog notNullWeblog = hashDB.getWeblog(name);
        if (notNullWeblog != null) {
        	hashDB.removeWeblog(wbInput);
        	nullWeblog = hashDB.getWeblog(name);
        }
        
        Assert.assertNull(nullWeblog);
    }
	
	@Test
    public void weblog_counter_clear() throws DaoException {
		String name = "DanieleAutiziBlog";
        String url = "http://www.danieleautizi.com";
        
        Weblog wbInput = new Weblog();
        wbInput.setName(name);
        wbInput.setUrl(url);
        wbInput.setInsertDate(new Date());
        
        HashDB hashDB = HashDB.getConcurrentHashMapInstance();
        hashDB.addWeblog(wbInput);
        
        hashDB.clear();
        
        Assert.assertEquals("Weblog expected: ", 0, hashDB.getCounter());
    }
	
	@Test
    public void weblog_counter_check() throws DaoException {
		HashDB hashDB = HashDB.getConcurrentHashMapInstance();
        hashDB.clear();
        
		String name1 = "DanieleAutiziBlog1";
        String url1 = "http://www.danieleautizi.com";
        Weblog wbInput1 = new Weblog();
        wbInput1.setName(name1);
        wbInput1.setUrl(url1);
        wbInput1.setInsertDate(new Date());
        hashDB.addWeblog(wbInput1);
        
        String name2 = "DanieleAutiziBlog2";
        String url2 = "http://www.danieleautizi.com/bis";
        Weblog wbInput2 = new Weblog();
        wbInput2.setName(name2);
        wbInput2.setUrl(url2);
        wbInput2.setInsertDate(new Date());
        hashDB.addWeblog(wbInput2);
        
        Assert.assertEquals("Weblog expected: ", 2, hashDB.getCounter());
    }
	
	@Test
    public void get_weblog_list() throws DaoException {
		HashDB hashDB = HashDB.getConcurrentHashMapInstance();
        hashDB.clear();
        
		String name1 = "DanieleAutiziBlog1";
        String url1 = "http://www.danieleautizi.com";
        Weblog wbInput1 = new Weblog();
        wbInput1.setName(name1);
        wbInput1.setUrl(url1);
        wbInput1.setInsertDate(new Date());
        hashDB.addWeblog(wbInput1);
        
        String name2 = "DanieleAutiziBlog2";
        String url2 = "http://www.danieleautizi.com/bis";
        Weblog wbInput2 = new Weblog();
        wbInput2.setName(name2);
        wbInput2.setUrl(url2);
        wbInput2.setInsertDate(new Date());
        hashDB.addWeblog(wbInput2);
        
        String name3 = "DanieleAutiziBlog3";
        String url3 = "http://www.danieleautizi.com/tris";
        Weblog wbInput3 = new Weblog();
        wbInput3.setName(name3);
        wbInput3.setUrl(url3);
        wbInput3.setInsertDate(new Date());
        hashDB.addWeblog(wbInput3);
        
        List<Weblog> list = hashDB.getWeblogs();
        
        Assert.assertEquals("Weblog expected from list: ", 3, list.size());
    }
	
	@Test
    public void get_wanted_weblog_from_list() throws DaoException {
		HashDB hashDB = HashDB.getConcurrentHashMapInstance();
        hashDB.clear();
        
		String name1 = "DanieleAutiziBlog1";
        String url1 = "http://www.danieleautizi.com";
        Weblog wbInput1 = new Weblog();
        wbInput1.setName(name1);
        wbInput1.setUrl(url1);
        wbInput1.setInsertDate(new Date());
        hashDB.addWeblog(wbInput1);
        
        String name2 = "DanieleAutiziBlog2";
        String url2 = "http://www.danieleautizi.com/bis";
        Weblog wbInput2 = new Weblog();
        wbInput2.setName(name2);
        wbInput2.setUrl(url2);
        wbInput2.setInsertDate(new Date());
        hashDB.addWeblog(wbInput2);
        
        String name3 = "DanieleAutiziBlog3";
        String url3 = "http://www.danieleautizi.com/tris";
        Weblog wbInput3 = new Weblog();
        wbInput3.setName(name3);
        wbInput3.setUrl(url3);
        wbInput3.setInsertDate(new Date());
        hashDB.addWeblog(wbInput3);
        
        Map<String, Weblog> map = new HashMap<String, Weblog>();
        List<Weblog> list = hashDB.getWeblogs();
        for (int i = 0; i < list.size(); i++) {
        	Weblog iWb = list.get(i);
        	map.put(iWb.getName(), iWb);
        }

        Assert.assertTrue("DanieleAutiziBlog2 is weblog inside map! ", map.containsKey(name2));
        Assert.assertSame("Weblog name expected from list: ", wbInput2, map.get(name2));
    }

}
