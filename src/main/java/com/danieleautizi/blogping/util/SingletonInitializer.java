package com.danieleautizi.blogping.util;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * This class instantiates a static object following
 * singleton pattern to manage properties file access 
 * with only one instance. The purpose is to avoid
 * repeated file access during execution.
 * 
 * @author d.autizi
 * @version 1.0
 */
public class SingletonInitializer {

	private static SingletonInitializer instance = null;
	
	private HashMap<String, Object> properties = null;
	private String file = Constraint.PROPERTIES_FILE;
	
	private List<String> blacklist = null;
	private String blacklistFile = Constraint.BLACKLIST_FILE;
	
	public synchronized static SingletonInitializer getInstance() {
		if (instance == null) {
            instance = new SingletonInitializer();
		}
        return instance;
    }
	
	/*
	 * PRIVATE TO AVOID EXTERNAL INSTANTIATION
	 * SINGLETON BASE RULE
	 */
	private SingletonInitializer() {
		if (!StringUtils.isEmpty(file)) {
			Properties prop = PropertiesUtil.load(file);
			properties = PropertiesUtil.setConfigurationProperties(prop);
		}
		
		if (!StringUtils.isEmpty(blacklistFile)) {
			blacklist = PropertiesUtil.loadList(blacklistFile);
		}
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public List<String> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}

}
