package com.ambition.util.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ambition.iqc.entity.MfgToMes;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:WebserviceHttpClientUtil类
 * <p>UAAPSECAGENT</p>
 * <p>建行信息技术管理部厦门开发中心</p>
 * <p>功能说明：httpclient类实现kmc高可用</p>
 * @author  赵骏
 * @version 1.00 2013-1-4 发布
 */
public class WebserviceHttpClientUtil {
	static Log log = LogFactory.getLog(WebserviceHttpClientUtil.class);
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
	public static HttpClient getHttpClient(){
		HttpClient client = new DefaultHttpClient(clientConnectionManager);
		//设置超时时间
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
//		client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
		return client;
	}
	
	/**
	  * 方法名: 执行远端调用
	  * <p>功能说明：根据请求参数和地址调用远端应用</p>
	  * @param entity
	  * @return
	 */
	public static HttpResponse executeForResponse(String url,List<NameValuePair> nameValuePairs){
		return executeForResponse(url, nameValuePairs,null);
	}
	/**
	  * 方法名: 执行远端调用
	  * <p>功能说明：根据请求参数和地址调用远端应用</p>
	  * @param entity
	  * @return
	 */
	public static HttpResponse executeForResponse(String url,List<NameValuePair> nameValuePairs,String cookieStr){
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			//连接完后由服务端主动关闭链接
//			httpPost.addHeader("Connection","close");
			if(nameValuePairs != null){
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			}
			if(StringUtils.isNotEmpty(cookieStr)){
				httpPost.addHeader("Cookie",cookieStr);
			}
			return getHttpClient().execute(httpPost);
		} catch (Throwable e) {
			throw new AmbFrameException("调用【" + url + "】出错,"+e.getMessage()+"!");
		}
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
	public static byte[] executeForXmlByUrl(String xmlStr,String mimeType,String url){
		HttpPost httpPost = null;
		InputStream in = null;
		try {
			httpPost = new HttpPost(url);
			HttpEntity entity = new StringEntity(xmlStr, mimeType,HTTP.UTF_8);
			httpPost.setEntity(entity);
			httpPost.setHeader("SOAPAction","http://tempuri.org/QISIPQCBASEDATE");
//			httpPost.setHeader("Accept-Encoding","gzip,deflate");
			httpPost.setHeader("Content-Type","text/xml;charset=UTF-8");
			HttpResponse response = getHttpClient().execute(httpPost);
			if(response.getEntity() != null){
				in = response.getEntity().getContent();
			}
			if(response.getStatusLine().getStatusCode() == 200){
//				String str=getResponseBodyAsString(response.getEntity() );
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				if(result==null||result.length==0){
					throw new AmbFrameException("调用【" + url + "】返回格式为空!");
				}
				return result;
			}else{
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				String str = new String(result);
				throw new AmbFrameException("调用【" + url + "】出错,返回状态【"+response.getStatusLine().getStatusCode()+"】!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
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
	  public static String getResponseBodyAsString(HttpEntity entity ) throws IOException {  
	        GZIPInputStream gzin = null;  
            InputStream is = entity.getContent();
            gzin = new GZIPInputStream(is);  
            StringBuffer sb = new StringBuffer();  
            List<byte[]> arr =new ArrayList<byte[]>();
            try {
            byte[] by= new byte[1024];
            int actlength=0;
            while((actlength=gzin.read(by))!=-1){
                byte[] b=Arrays.copyOf(by, actlength);;
                arr.add(b);
            }
            }
            catch (java.io.EOFException e) {
                // TODO Auto-generated catch block
           //     e.printStackTrace();
            } 
            int actlength=0;
            
            //   List<byte[]> arr =new ArrayList<byte[]>();
               actlength = 0;
               for(byte[] b : arr){
                   actlength+=b.length;
               }
               byte[] by1= new byte[actlength];
               actlength = 0;
               for(byte[] b : arr){
                   System.arraycopy(b,0,by1,actlength,b.length);
                   actlength+=b.length;
               }
               String s= new String(by1,"utf8");
               
               sb.append(s);
            return sb.toString();
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
	
	/**
	  * 方法名: 获取提定的节点
	  * @param parent
	  * @param qName
	  * @return
	 */
	private static Node getNode(Node parent,String qName){
		if(parent.getNodeName().equals(qName)){
			return parent;
		}else{
			NodeList nodeList = parent.getChildNodes();
			int len = nodeList.getLength();
			for(int i=0;i<len;i++){
				Node node = nodeList.item(i);
				Node target = getNode(node, qName);
				if(target != null){
					return target;
				}
			}
			return null;
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
	public static List<Map<String,String>> getArrayList(org.w3c.dom.Document document,String dataSetNodeName){
		Node node = getNode(document, dataSetNodeName);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(node != null){
			NodeList nodeList = node.getChildNodes();
			Map<String,String> objMap = new HashMap<String, String>();
			int len = nodeList.getLength();
			for(int i=0;i<len;i++){
				Node child = nodeList.item(i);
				objMap.put(child.getNodeName(),child.getFirstChild().getNodeValue());
			}
			list.add(objMap);
		}
		return list;
	}
	
	/**
	 * 方法名: converToMapForErp
	 * <p>功能说明：解析ERP传递过来xml</p>
	 * 创建人:wuxuming 日期： 2015-9-21 version 1.0
	 * @param 
	 * @return
	 */
	public static Map<String,Object> converToMapForErp(String xmlStr) throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		org.dom4j.Document document = DocumentHelper.parseText(xmlStr);  
        org.dom4j.Element root = document.getRootElement(); 
        List<org.dom4j.Element> elements = root.elements();
        String objectId=null;
        String itemName="";
        String itemValue="";
        List<Map<String,String>> list=new  ArrayList<Map<String,String>>();
        List<org.dom4j.Element> threeEnts=null;
        Map<String,String> map= new HashMap<String,String>();
        //遍历根元素
        for (Iterator<org.dom4j.Element> it = elements.iterator(); it.hasNext();) {  
            org.dom4j.Element element = it.next();
            //获取所以的子元素，遍历
            List<org.dom4j.Element> els =element.elements();
            List<org.dom4j.Element> elsNext = els.get(0).elements();
            List<org.dom4j.Element> elsNextNext = elsNext.get(0).elements();
            List<org.dom4j.Element> elsNextNextNext = elsNextNext.get(0).elements();
            List<org.dom4j.Element> elsNextNextNextNext = elsNextNextNext.get(0).elements();
            for(org.dom4j.Element nt:elsNextNextNextNext){
            	if("ObjectID".equals(nt.getName())){
            		objectId=nt.getText();
            	}else if("Data".equals(nt.getName())){
            		List<org.dom4j.Element>  childElements=nt.elements();
            		threeEnts=childElements;
            	}
            }
//            for(org.dom4j.Element nt:elsNextNextNext){
//            	if("ObjectID".equals(nt.getName())){
//            		objectId=nt.getText();
//            	}else if("Data".equals(nt.getName())){
//            		List<org.dom4j.Element>  childElements=nt.elements();
//            		threeEnts=childElements;
//            	}
//            }
        }
        for(org.dom4j.Element threeEnt:threeEnts){
        	System.out.println(threeEnt.getName());
        	Attribute attribute=threeEnt.attribute("Field");
			itemName=attribute.getValue();
			System.out.println(itemName);
        	String [] itemNames=itemName.split("!@");
        	List<org.dom4j.Element>  childElements=threeEnt.elements();
        	for(org.dom4j.Element dataEnt:childElements){
        		Map<String,String> nameToValue=new HashMap<String,String>();
        		System.out.println(threeEnt.getName());
        		Attribute bute=dataEnt.attribute("Data");
        		itemValue=bute.getValue();
        		String [] itemValues =itemValue.split("!@");
        		for(int i=0;i<itemValues.length&&i<itemNames.length;i++){
        			nameToValue.put(itemNames[i], itemValues[i]);
    			}
        		nameToValue.put("ObjectID", objectId);
        		nameToValue.put("fieldRootName", threeEnt.getName());
        		list.add(nameToValue);
        	}
        }
        resultMap.put("objectId", objectId);
        resultMap.put("dataList", list);
		return resultMap;
	}
	/**
	 * 方法名: converToMapForBarCode
	 * <p>功能说明：解析条码传递过来的xml</p>
	 * 创建人:wuxuming 日期： 2015-9-21 version 1.0
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String,String>> converToMapForBarCode(String xmlStr) throws Exception{
		List<Map<String,String>> mapList=new ArrayList<Map<String,String>>();
		org.dom4j.Document document = DocumentHelper.parseText(xmlStr);  
        org.dom4j.Element root = document.getRootElement(); 
        List<org.dom4j.Element> elements = root.elements();
        List<org.dom4j.Element> columnsList=null;
        List<org.dom4j.Element> valuesList=null;
        //遍历根元素
        for (Iterator<org.dom4j.Element> it = elements.iterator(); it.hasNext();) {  
            org.dom4j.Element element = it.next();
            //获取所以的子元素，遍历
            org.dom4j.Element columns=(org.dom4j.Element) element.element("Columns");
            columnsList=(List<org.dom4j.Element>) columns.elements();
            valuesList =(List<org.dom4j.Element>) element.elements("Values");
        }
        for(org.dom4j.Element child:valuesList){
        	List<org.dom4j.Element> threeElements= child.elements();
        	Map<String,String> map= new HashMap<String,String>();
        	for(int i=0;i<columnsList.size()&&i<threeElements.size();i++){
        		Attribute bute=columnsList.get(i).attribute("Name");
        		org.dom4j.Element valueElement=threeElements.get(i);
        		String mapKey=bute.getValue();
        		String mapValue=valueElement.getText();
        		map.put(mapKey, mapValue);
        	}
        	mapList.add(map);
        }
		return mapList;
	}
	
	public static  String bulidXmlByObj(MfgToMes mfgToMes){
		String xml="";
		  try {  
	            JAXBContext context = JAXBContext.newInstance(MfgToMes.class);  
	            Marshaller marshaller = context.createMarshaller();
	            StringWriter sw = new StringWriter();
	            marshaller.marshal(mfgToMes, sw); 
	            xml=sw.toString();
	        } catch (JAXBException e) {  
	            e.printStackTrace();  
	        }  
		return xml;
	}
	
	
	/**
	 * 方法名: 
	 * <p>功能说明：</p>
	 * 创建人:wuxuming 日期： 2015-10-20 version 1.0
	 * @param inspectionRecord 检验记录信息
	 * @param reportResult 不合格品处理信息
	 * @param reportResult 传递对象路径
	 * @return
	 */
	@SuppressWarnings({"unused" })
	public static String buildXmlDoc(String rootNode,Object reportResult,String classPath) throws Exception{
		org.dom4j.Document doc = DocumentHelper.createDocument();
		org.dom4j.Element rootNodeDoc = doc.addElement(rootNode);//soapEnvelope
		if(reportResult!=null){
			return bulidXmlByEntity(doc,rootNodeDoc,reportResult,classPath);
//			return null;
		}else{
			return "不满足生成XML文件的条件";
		}
	}
	
	public static String bulidXmlByEntity(org.dom4j.Document doc,org.dom4j.Element element,Object reportResult,String classPath) throws Exception{
		StringWriter writer = new StringWriter();
		element.addNamespace("xmlns", "http://tempuri.org/");
	    Method[] methods = reportResult.getClass().getDeclaredMethods();
	    for(java.lang.reflect.Method m:methods){
	    	if(m.getName().startsWith("get")){//获取get方法
	    		String objName=m.getName();
	    		String elementName=objName.substring(3);
	    		Object o = m.invoke(reportResult);//执行
	    		element.addElement(elementName).setText(o==null?"":o.toString());
	    	}
	    }
	    OutputFormat format = OutputFormat.createCompactFormat(); //createPrettyPrint() 层次格式化
	    XMLWriter output = new XMLWriter(writer, format);
	    output.write(doc);
	    writer.close();
	    output.close();
		return writer.toString();
	}
	
	/**
	  * 方法名: 欧菲光回写XML文件
	  * <p>功能说明：</p>
	  * @return
	 */
	public static String bulidXmlDocByObj(org.dom4j.Document doc,org.dom4j.Element element,Object reportResult,String classPath) throws Exception{
		StringWriter writer = new StringWriter();
        org.dom4j.Element STD_IN = element.addElement("STD_IN");
        org.dom4j.Element Service = element.addElement("Service");
        Service.addAttribute("name","SetData");
        org.dom4j.Element Operate = Service.addElement("Operate");
        org.dom4j.Element ObjectID = Service.addElement("ObjectID");
        org.dom4j.Element DataSet = Service.addElement("DataSet");
        org.dom4j.Element Row = DataSet.addElement("Row");
        Field[] fields = reportResult.getClass().getDeclaredFields();
        List<String> objPropertys=new ArrayList<String>();
        Class<?> classObj=Class.forName(classPath);
        Method[] methods = reportResult.getClass().getSuperclass().getDeclaredMethods();
        String attributStr=null;
        for(java.lang.reflect.Field f:fields){
 		   String objName=f.getName();
 		   objPropertys.add(objName.replaceFirst(objName.substring(0, 1),objName.substring(0, 1).toUpperCase()));
 		   attributStr+=objName.replaceFirst(objName.substring(0, 1),objName.substring(0, 1).toUpperCase())+"!@";
 		}
        DataSet.addAttribute("Field",attributStr);
        String valueStr=null;
		for(java.lang.reflect.Method m:methods){
		   if(m.getName().startsWith("get")){//获取get方法
			 String key=m.getName().substring(3,m.getName().length());
		     Object o = m.invoke(reportResult);//执行
		     valueStr+=(o==null?"":o.toString())+"!@";
		   }
		}
		Row.addAttribute("Data",attributStr);
		OutputFormat format = OutputFormat.createCompactFormat(); //createPrettyPrint() 层次格式化
        XMLWriter output = new XMLWriter(writer, format);
        output.write(doc);
        writer.close();
        output.close();
		return writer.toString();
	}
	
	/**
	 * 方法名: 生成对象不为null的XML文件
	 * <p>功能说明：</p>
	 * 创建人:wuxuming 日期： 2015-10-20 version 1.0
	 * @param 
	 * @return
	 */
//	public static String buildXmlDocByObj(org.dom4j.Document doc,org.dom4j.Element element,Object reportResult,String classPath) throws Exception{
//		StringWriter writer = new StringWriter();
//        org.dom4j.Element DataInfo = element.addElement("DataInfo");
//        org.dom4j.Element columns = DataInfo.addElement("Columns");
//		Field[] fields = reportResult.getClass().getDeclaredFields();
//		List<String> objPropertys=new ArrayList<String>();
//		//遍历父类的方法集合
//		Class<?> classObj=Class.forName(classPath);
//		if(classObj.getGenericSuperclass()!=null){
//			Field[] fieldsPartent = reportResult.getClass().getSuperclass().getDeclaredFields();
//			for(java.lang.reflect.Field f:fieldsPartent){
//			   String objName=f.getName();
//			   objPropertys.add(objName.replaceFirst(objName.substring(0, 1),objName.substring(0, 1).toUpperCase()));
//			   columns.addElement("Column").addAttribute("Name", objName.replaceFirst(objName.substring(0, 1),objName.substring(0, 1).toUpperCase())).setText("");
//			}
//		}
//		for(java.lang.reflect.Field f:fields){
//		   String objName=f.getName();
//		   objPropertys.add(objName.replaceFirst(objName.substring(0, 1),objName.substring(0, 1).toUpperCase()));
//		   columns.addElement("Column").addAttribute("Name", objName.replaceFirst(objName.substring(0, 1),objName.substring(0, 1).toUpperCase())).setText("");
//		}
//		Map<String,String> map=new HashMap<String,String>();
//		org.dom4j.Element Values = DataInfo.addElement("Values");
//		//遍历父类的value
//		if(classObj.getGenericSuperclass()!=null){
//			Method[] methodParents= reportResult.getClass().getDeclaredMethods();
//			for(Method mPartent:methodParents){
//				if(mPartent.getName().startsWith("get")){//获取get方法
//					 String key=mPartent.getName().substring(3,mPartent.getName().length());
//					 System.out.println(key);
//				     Object o = mPartent.invoke(reportResult);//执行
//				     map.put(key, o==null?"":o.toString());
//				}
//			}
//		}
//		Method[] methods = reportResult.getClass().getSuperclass().getDeclaredMethods();
//		for(java.lang.reflect.Method m:methods){
//		   if(m.getName().startsWith("get")){//获取get方法
//			 String key=m.getName().substring(3,m.getName().length());
//			 System.out.println(key);
//		     Object o = m.invoke(reportResult);//执行
//		     map.put(key, o==null?"":o.toString());
//		   }
//		}
//		int i=0;
//		for(String objProperty:objPropertys){
//			i++;
//			if(map.containsKey(objProperty)){
//				Values.addElement("Value").setText(map.get(objProperty));
//			}else{
//				throw new AmbFrameException("属性不对应，生产XML文件失败");
//			}
//		}
//		OutputFormat format = OutputFormat.createCompactFormat(); //createPrettyPrint() 层次格式化
//        XMLWriter output = new XMLWriter(writer, format);
//        output.write(doc);
//        writer.close();
//        output.close();
//		return writer.toString();
//	}
}
