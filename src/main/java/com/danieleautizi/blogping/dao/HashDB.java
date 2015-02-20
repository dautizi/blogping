package com.danieleautizi.blogping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danieleautizi.blogping.model.Weblog;


/**
 * This class simulates a DB with concurrent data access.
 * It consists of methods to store(add) and ask(get) data. 
 * ConcurrentHashMap ensures the concurrent access to HashDB
 * data.
 * 
 * @author d.autizi
 * @version 1.0
 */
public class HashDB implements WeblogDAO {
	
	protected final static Log logger = LogFactory.getLog(HashDB.class);
	
	private static HashDB instance = null;
	
	/*
	 * ConcurrentHashMap allows thread-safe access to your data 
	 * without locking. That means you can add/remove values in 
	 * one thread and at the same time get values out in another 
	 * thread without running into an exception.
	 */
	private ConcurrentHashMap<String, Weblog> weblogMap = null;
	
	private int counter = 0;
	
	private HashDB() {
		if (weblogMap == null) {
			weblogMap = new ConcurrentHashMap<String, Weblog>();
		}
	}
	
	/*
	 * Singleton to avoid multiple HashDB instances
	 */
	public static synchronized HashDB getConcurrentHashMapInstance() {
		if (instance == null) {
			instance = new HashDB();
		}
        return instance;
    }
	
	public int getCounter() throws DaoException {
		return counter;
	}
	
	public void clear() throws DaoException {
		if (getConcurrentHashMapInstance().weblogMap.size() > 0) {
			weblogMap.clear();
			counter = 0;
		}
	}
	
	public void addWeblog(Weblog weblog) throws DaoException {
		if (getConcurrentHashMapInstance().weblogMap.size() > 10000) {
			weblogMap.clear();
			counter = 0;
		}
		
		// CHECK IF ALREADY EXISTS
		Weblog wbFound = getWeblog(weblog.getName());
		if (wbFound == null) {
			// CHECK DATE/WHEN
			
			// IF WHEN IS OK PUT
			getConcurrentHashMapInstance().weblogMap.put(weblog.getName(), weblog);
			counter++;
		}
	}
	
	public void removeWeblog(Weblog weblog) throws DaoException {
		if (weblogMap.size() > 0) {
			getConcurrentHashMapInstance().weblogMap.remove(weblog.getName());
		}
	}
	
	public Weblog getWeblog(String name) throws DaoException {
		Weblog weblog = getConcurrentHashMapInstance().weblogMap.get(name);
		
		return weblog;
	}
	
	public List<Weblog> getWeblogs() throws DaoException {
		Collection<Weblog> weblogCollection = getConcurrentHashMapInstance().weblogMap.values();
		List<Weblog> weblogList = new ArrayList<>(weblogCollection);
		
		return weblogList;
	}
		
}
