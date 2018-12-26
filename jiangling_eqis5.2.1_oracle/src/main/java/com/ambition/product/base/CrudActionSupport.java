package com.ambition.product.base;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * ACTION(BASE)
 * @author wangChen
 *
 * @param <E>
 */
public class CrudActionSupport<E> extends com.norteksoft.product.web.struts2.CrudActionSupport<E>{
	private static final long serialVersionUID = 1L;

	@Override
	public E getModel() {
		
		return null;
	}

	@Override
	public String delete() throws Exception {
		
		return null;
	}

	@Override
	public String input() throws Exception {
		
		return null;
	}

	@Override
	public String list() throws Exception {
		
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void map(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息及obj
	 * @param message
	 * @param obj
	 */
	public void createMessage(String message,Object obj){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		map.put("obj",obj);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createIdMessage(Long id,Boolean isError,String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",id);
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
