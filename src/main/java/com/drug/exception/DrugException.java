package com.drug.exception;

/**
 * 异常定义类
 * 
 * @author andongdong
 * 
 */
public class DrugException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8355468430947759921L;

	public DrugException(String message) {
		super(message);
	}

	public DrugException(String message, Throwable t) {
		super(message, t);
	}

}
