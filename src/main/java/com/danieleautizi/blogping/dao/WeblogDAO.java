package com.danieleautizi.blogping.dao;

import java.util.List;

import com.danieleautizi.blogping.model.Weblog;

/**
 * The DAO layer is the unit needed to store and get 
 * Weblogs.
 * 
 * @author d.autizi
 * @version 1.0
 */
public interface WeblogDAO {

	public void addWeblog(Weblog weblog) throws DaoException;
	
	public void removeWeblog(Weblog weblog) throws DaoException;
	
	public Weblog getWeblog(String url) throws DaoException;
	
	public List<Weblog> getWeblogs() throws DaoException;
	
}
