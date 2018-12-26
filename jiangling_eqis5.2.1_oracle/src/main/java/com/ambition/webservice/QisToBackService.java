package com.ambition.webservice;

import java.util.List;

import org.apache.http.protocol.HTTP;
import org.dom4j.DocumentHelper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.madeinspection.dao.MadeInspectionDao;
import com.ambition.carmfg.madeinspection.service.MadeInspectionManager;
import com.ambition.iqc.entity.MfgToMes;
import com.ambition.util.common.WebserviceHttpClientUtil;
import com.ambition.util.exception.AmbFrameException;

/**
 * 类名:QisToBackService.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-10-13 下午2:47:14
 * </p>
 */
@Service
@Transactional
public class QisToBackService {
	@Autowired
	private MadeInspectionDao madeInspectionDao;
	//IPQC推送 并返回数据数据
	public String mfgToBarch(MfgToMes mfgToMes) throws Exception{
			String xmlStr= "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
						   "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">"+
						   "<soapenv:Header/>"+
						   "<soapenv:Body><tem:QISIPQCBASEDATE><tem:RunCardSN>"+mfgToMes.getProcessCard()+"</tem:RunCardSN>"+
						   "<tem:ProcessName>"+mfgToMes.getWorkProcedure()+"</tem:ProcessName><tem:FFlag>null</tem:FFlag></tem:QISIPQCBASEDATE>"+
						   "</soapenv:Body></soapenv:Envelope>";
			String webserviceUrl="http://192.168.0.33:7200/service.asmx";
			byte[] bytes=WebserviceHttpClientUtil.executeForXmlByUrl(xmlStr, "text/xml", webserviceUrl);
			String bytesXml=new String(bytes,HTTP.UTF_8);
			DownLoadInfo2Impl impl= new DownLoadInfo2Impl();
			String message=impl.Download_Info(bytesXml);//解析并保存数据
//			if(message.indexOf("<Status>")==-1){
//				return message;
//			}else{
//				return message;
//			}
			return message;
//			String message=QisToBackService.responseXml(bytesXml);
//			System.out.println(message);
//			return message;
	}
	
	private static String responseXml(String xml) throws Exception{
		xml=xml.replace("[&lt;", "<");
		xml=xml.replace("&gt;]", ">");
		org.dom4j.Document document=null;
		document = DocumentHelper.parseText(xml);
		org.dom4j.Element root = document.getRootElement();
		org.dom4j.Element Service = root.element("Service");
		List<org.dom4j.Element>  childElements=Service.elements();
		org.dom4j.Element status = childElements.get(1);
		org.dom4j.Element error = childElements.get(2);
		String statusVal=status.getText();
		String errorMessage=error.getText();
		if("1".equals(statusVal)){
			throw new AmbFrameException("推送数据失败:"+errorMessage); 
		}else{
			return "推送成功";
		}
	}
	
	/**
	  * 方法名: 测试能否正常连接
	  * <p>功能说明：</p>
	  * @return
	 */
	public static void main(String [] args){
		MfgToMes mfgToMes=new MfgToMes();
		mfgToMes.setProcessCard("20161107SQQ000010");
		mfgToMes.setWorkProcedure("1st 压膜");
		QisToBackService qis=new QisToBackService();
		try {
//			qis.mfgToBarch(mfgToMes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
