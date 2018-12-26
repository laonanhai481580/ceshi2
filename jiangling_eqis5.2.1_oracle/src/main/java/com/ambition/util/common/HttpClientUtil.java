package com.ambition.util.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:httpclient类
 * <p>UAAPSECAGENT</p>
 * <p>建行信息技术管理部厦门开发中心</p>
 * <p>功能说明：httpclient类实现kmc高可用</p>
 * @author  赵骏
 * @version 1.00 2013-1-4 发布
 */
public class HttpClientUtil {
	static Log log = LogFactory.getLog(HttpClientUtil.class);
	private static ThreadSafeClientConnManager clientConnectionManager = null;
	static{
		clientConnectionManager = new ThreadSafeClientConnManager();
		clientConnectionManager.setMaxTotal(800);
		clientConnectionManager.setDefaultMaxPerRoute(20);
	}
	
	/**
	  * 方法名:产生HttpClient对象 
	  * @return
	 */
	private static HttpClient getHttpClient(){
		HttpClient client = new DefaultHttpClient(clientConnectionManager);
			//设置超时时间
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
		return client;
	}
	
	/**
	  * 方法名: 执行远端调用
	  * <p>功能说明：根据请求参数和地址调用远端应用</p>
	  * @param entity
	  * @return
	 */
	public static byte[] executeByUrl(List<NameValuePair> nameValuePairs,String url){
		HttpPost httpPost = null;
		InputStream in = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			//method.addRequestHeader("Content-type" , "");
//			httpPost.setHeader("Content-type","text/html; charset=GB2312");
			HttpResponse response = getHttpClient().execute(httpPost);
			if(response.getEntity() != null){
				in = response.getEntity().getContent();
			}
			if(response.getStatusLine().getStatusCode() == 200){
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				if(result==null||result.length==0){
					throw new AmbFrameException("调用【" + url + "】返回格式为空!");
				}
				return result;
			}else{
				throw new AmbFrameException("调用【" + url + "】出错,返回状态【"+response.getStatusLine().getStatusCode()+"】!");
			}
		} catch (Throwable e) {
			throw new AmbFrameException("调用【" + url + "】出错,"+e.getMessage()+"!");
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					throw new AmbFrameException("关闭连接时出错,"+e.getMessage()+"!");
				}
			}
		}
	}
	/**
	  * 方法名: 执行远端调用
	  * <p>功能说明：根据请求参数和地址调用远端应用</p>
	  * @param entity
	  * @return
	 */
	public static byte[] execute(List<NameValuePair> nameValuePairs,String methodName){
		String url = PropUtils.getProp("erp.webservice.url") + "/" + methodName;
		return executeByUrl(nameValuePairs,url);
	}
	/**
	  * 方法名: 执行远程调用
	  * <p>功能说明：执行远程调用,并把远程调用的结果转换为集合</p>
	  * @param entity
	  * @param url
	  * @return
	 */
	public static List<Map<String,String>> executeForList(List<NameValuePair> nameValuePairs,String methodName){
		return executeForList(nameValuePairs,methodName,"NewDataSet");
	}
	
	/**
	  * 方法名: 执行远程调用
	  * <p>功能说明：执行远程调用,并把远程调用的结果转换为集合</p>
	  * @param entity
	  * @param url
	  * @param dataSetName
	  * @return
	 */
	public static List<Map<String,String>> executeForList(List<NameValuePair> nameValuePairs,String methodName,String dataSetName){
		byte[] bytes = execute(nameValuePairs, methodName);
		try {
			return getArrayList(bytes,dataSetName);
		} catch (JDOMException e) {
			throw new AmbFrameException("调用远程服务失败!",e);
		} catch (IOException e) {
			throw new AmbFrameException("调用远程服务失败!",e);
		}
	}
	
	/**
	  * 方法名: 转换XML格式的字节数组为集合
	  * @param bytes
	  * @param dataSetNodeName
	  * @return
	  * @throws JDOMException
	  * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static List<Map<String,String>> getArrayList(byte[] bytes,String dataSetNodeName) throws JDOMException, IOException{
		SAXBuilder saxBuilder = new SAXBuilder();
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(bytes);
			Document document = saxBuilder.build(in);
			Element target = getElement(document.getRootElement(),dataSetNodeName);
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			if(target != null){
				List<Element> elements = target.getChildren();
				Map<String,String> objMap = new HashMap<String, String>();
				for(Element element : elements){
					objMap.put(element.getName(),element.getValue());
				}
				list.add(objMap);
			}
			return list;
		}finally{
			if(in != null){
				in.close();
			}
		}
	}
	/**
	  * 方法名: 获取提定的节点
	  * @param parent
	  * @param qName
	  * @return
	 */
	private static Element getElement(Element parent,String qName){
		if(parent.getName().equals(qName)){
			return parent;
		}else{
			@SuppressWarnings("unchecked")
			List<Element> elements = parent.getChildren();
			for(Element element : elements){
				Element target = getElement(element, qName);
				if(target != null){
					return target;
				}
			}
			return null;
		}
	}
}
