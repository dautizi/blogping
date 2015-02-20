package com.danieleautizi.blogping.dao;

/**
 * Dao Exception
 */
public class DaoException extends Exception {

	private static final long serialVersionUID = -7365770754819419111L;
		
    public DaoException() {}

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DaoException(Throwable cause) {
        super(cause);
    }

}
