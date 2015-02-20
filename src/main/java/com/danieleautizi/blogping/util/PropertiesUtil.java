package com.danieleautizi.blogping.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class instantiates static methods for
 * processing application properties.
 * 
 * @author d.autizi
 * @version 1.0
 */
public class PropertiesUtil {

	private final static Log logger = LogFactory.getLog(PropertiesUtil.class);
	
	/**
	 * This method loads properties file starting by
	 * file name
	 * @param properties fileName to load
	 * @return Properties entity
	 */
	public static Properties load(String fileName) {
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
			// input = new FileInputStream(fileName);
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			input = loader.getResourceAsStream(fileName);
			
			// LOAD A PROPERTIES FILE
			prop.load(input);
		} catch (IOException ex) {
			logger.error("loading file exception: " + ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("closing file error: " + e);
				}
			}
		}
		return prop;
	}
	
	/**
	 * Loads properties into a List of strings starting by
	 * file name
	 * @param properties fileName to load
	 * @return list of strings
	 */
	public static List<String> loadList(String fileName) {
		ArrayList<String> list = null;
		InputStream input = null;
		BufferedReader br = null;
		
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			input = loader.getResourceAsStream(fileName);
			br = new BufferedReader(new InputStreamReader(input));
			
			list = new ArrayList<String>();
			
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException ex) {
			logger.error("loading file exception: " + ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	/**
	 * Gets property from properties starting by
	 * key string
	 * @param properties
	 * @param key to get
	 * @return property found in string
	 */
	public static String getProperty(Properties prop, String key) {
		String property = null;
		
		if (prop != null) {
			Object obj = prop.get(key);
			
			if (obj != null) {
				property = (String) obj;
			}
		}
		
		return property;
	}
	
	/**
	 * Sets configuration map by key and related ogject
	 * @param properties file
	 * @return properties map 
	 */
	public static HashMap<String,Object> setConfigurationProperties(Properties prop) {
		HashMap<String,Object> confMap = null;
		
		try {
			confMap = new HashMap<String,Object>();
			
			for (String name: prop.stringPropertyNames()) {
			    confMap.put(name, prop.getProperty(name));
				logger.debug("property loaded: " + name);
			}
		} catch(Exception ex) {
			logger.info("Error loading properties: " + ex);
		}
		
		return confMap;
	}
}
