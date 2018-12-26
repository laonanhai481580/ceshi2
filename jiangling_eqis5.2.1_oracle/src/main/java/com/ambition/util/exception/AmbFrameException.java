package com.ambition.util.exception;
/**
 * 类名:检查的异常
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：需要检查的异常类</p>
 * @author  赵骏
 * @version 1.00 2013-4-19 发布
 */
public class AmbFrameException extends RuntimeException {
	private static final long serialVersionUID = -637074693203352523L;
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public AmbFrameException(String message){
		super(message);
	}
	public AmbFrameException(String message,Throwable ex){
		super(message,ex);
	}
}
