package com.drug.ibatis;

/**
 * 数据库操作异常类
 * 
 * @author andongdong
 * 
 */
public class DaoException extends RuntimeException {

	private static final long serialVersionUID = 6921332072906346653L;

	public DaoException(String message) {
		super(message);
	}

}
