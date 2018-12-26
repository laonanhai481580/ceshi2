package com.ambition.util.erp.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {
	private String status;//状态
	private String msg;//消息
	private String idStr;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
	/**
	  * 方法名: 添加成功的消息
	  * <p>功能说明：</p>
	  * @param msg
	 */
	public void addSuccessMessage(String msg){
		this.status = "0";
		this.msg = msg;
	}
	/**
	  * 方法名:添加失败的消息 
	  * <p>功能说明：</p>
	  * @param msg
	 */
	public void addErrorMessage(String msg){
		this.status = "1";
		this.msg = msg;
	}
	
	/**
	  * 方法名: 添加执行成功的返回值
	  * <p>功能说明：</p>
	  * @param msg
	  * @return
	 */
	public static Result createSuccessResult(String msg){
		Result result = new Result();
		result.addSuccessMessage(msg);
		return result;
	}
	
	/**
	  * 方法名: 添加执行失败的返回值
	  * <p>功能说明：</p>
	  * @param msg
	  * @return
	 */
	public static Result createErrorResult(String msg){
		Result result = new Result();
		result.addErrorMessage(msg);
		return result;
	}
}
