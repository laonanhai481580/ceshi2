package com.ambition.util.aop;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.BusinessSystem;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

public class LogAspect {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private LogAspectManager logAspectManager;
	//请求地址和系统的映射关系
	private Map<String,String> defaultUrlMap;
	private Map<String,String> highUrlMap;
	//系统编码和应用系统的映射
	private Map<String,BusinessSystem> systemMap;
	public LogAspect(){
		highUrlMap = new HashMap<String, String>();
		defaultUrlMap = new HashMap<String, String>();
		String fileName = "systemUrlMapping1.properties";
		Set<Object> props = PropUtils.getPropertyKeys(fileName);
		for(Object keyObj : props){
			String key = (keyObj+"").trim();
			String value = PropUtils.getProp(fileName, keyObj+"").trim();
			if(StringUtils.isNotEmpty(value)){
				if(key.startsWith("high.")){
					key = key.substring("high.".length());
					highUrlMap.put(key,value);
				}else if(key.startsWith("default.")){
					key = key.substring("default.".length());
					defaultUrlMap.put(key,value);
				}
			}
		}
		//初始化系统编码
		systemMap = new HashMap<String, BusinessSystem>();
	}
	/**
	  * 方法名:操作日志记录 
	  * <p>功能说明：记录用户的操作日志,采用aop方式,需要在记录点添加自定义注解@LogInfo</p>
	  * @param joinPoint 方法执行点信息
	  * @param log 记录内容
	 */
	public void log(JoinPoint joinPoint,LogInfo log){
		try {
			String message = log.message();
			if(StringUtils.isEmpty(message)){
				Object messageObj = Struts2Utils.getRequest().getAttribute(LogInfo.MESSAGE_ATTRIBUTE);
				message = messageObj==null?"":messageObj.toString();
			}
			logAspectManager.debugLog(log.optType(),message,getSystemId());
			logger.info("记录日志成功!");
		} catch (Exception e) {
			logger.error("记录操作日志时出错!",e);
		}
	}
	/**
	  * 方法名: 获取业务系统ID
	  * <p>功能说明：根据请求地址映射的业务系统编码获取业务系统的ID</p>
	  * @return 业务系统的ID,没有对应的编码为空
	 */
	private Long getSystemId(){
		HttpServletRequest request = Struts2Utils.getRequest();
		String url=request.getRequestURI().substring(request.getContextPath().length());
		for(String key : highUrlMap.keySet()){
			if(url.startsWith("/" + key + "/")){
				return getSystemId(highUrlMap.get(key));
			}
		}
		for(String key : defaultUrlMap.keySet()){
			if(url.startsWith("/" + key + "/")){
				return getSystemId(defaultUrlMap.get(key));
			}
		}
		return null;
	}
	
	/**
	  * 方法名: 获取业务系统ID
	  * <p>功能说明：根据业务系统编码获取业务系统的ID</p>
	  * @return 业务系统的ID,没有对应的编码为空
	 */
	private Long getSystemId(String systemCode){
		if(!systemMap.containsKey(systemCode)){
			systemMap.put(systemCode, ApiFactory.getAcsService().getSystemByCode(systemCode));
		}
		BusinessSystem system = systemMap.get(systemCode);
		if(system == null){
			return null;
		}else{
			return system.getId();
		}
	}
	
	/**
	  * 方法名: Action出错后记录方法
	  * @param joinPoint
	  * @param ex
	 */
	public void debug(JoinPoint joinPoint,Throwable ex){
		logger.error("Action出错了!",ex);
	}
}
