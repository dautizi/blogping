package com.danieleautizi.blogping.dao;

/**
 * This class is the DAO entry point developed starting by
 * Factory design pattern. It instantiates the correct DAO 
 * that caller wants.
 * 
 * @author d.autizi
 * @version 1.0
 */
public class DAOFactory {

	public static WeblogDAO getWeblogDAO(String type) { 
        if (type.equalsIgnoreCase("hashdb")) {
            return HashDB.getConcurrentHashMapInstance();
        } else {
        	// OTHER DAO IMPLEMENTATION
        	
        	/*
        	 * Considering for example a SQL DB as MySQL
        	 * we have to define a connections pool management
        	 * with a right connections number to avoid 
        	 * congestion or request Timeout...the best way
        	 * is surely my lovely Spring!
        	 */
        	
        	return null;
        }
    }

}
