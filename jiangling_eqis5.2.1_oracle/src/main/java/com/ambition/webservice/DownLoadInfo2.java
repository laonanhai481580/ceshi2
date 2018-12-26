package com.ambition.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;



/**
 * 类名:HelloWorld.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-9-9 下午5:26:11
 * </p>
 */
@WebService
public interface DownLoadInfo2{
	String Download_Info(@WebParam(name="xml")String text);  
 }
