/**   
 * @Title: ChartAction.java 
 * @Package com.ambition.util.chart.web 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2014-5-20 下午4:31:44 
 * @version V1.0   
 */ 
package com.ambition.util.chart.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.ambition.util.chart.service.SendPictureMailUtils;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名: ChartAction 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  刘承斌
 * @version 1.00 2014-5-20 下午4:31:44  发布
 */
@Namespace("/util/common/chart")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "util/common/chart", type = "redirectAction") })
public class ChartAction {
	Logger log = Logger.getLogger(this.getClass());

	@Action("send-message")
	public String sendMessage() throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = null;
		File file = null;
		OutputStream outputStream = null;
		try {
			String svg = Struts2Utils.getRequest().getParameter("svg");
			String tos = Struts2Utils.getRequest().getParameter("tos");
			List<User> toUsers = new ArrayList<User>();
			for(String to : tos.split(",")){
				if(StringUtils.isNotEmpty(to)){
					toUsers.add(ApiFactory.getAcsService().getUserByLoginName(to));
				}
			}
			if(toUsers.isEmpty()){
				throw new AmbFrameException("收件人为空!");
			}
			String subject = Struts2Utils.getRequest().getParameter("subject");
			String content = Struts2Utils.getRequest().getParameter("content");
//			String data=Struts2Utils.getRequest().getParameter("data");
//			String label=Struts2Utils.getRequest().getParameter("label");
//			String width=Struts2Utils.getRequest().getParameter("width");
//			String height=Struts2Utils.getRequest().getParameter("height");
			if (StringUtils.isNotEmpty(svg)) {
			    svg = svg.replaceAll(":rect", "rect");
			    Transcoder t = new JPEGTranscoder();
		    	byteArrayOutputStream = new ByteArrayOutputStream();
				TranscoderInput input = new TranscoderInput(new StringReader(svg));
				TranscoderOutput output = new TranscoderOutput(byteArrayOutputStream);
				t.transcode(input, output);
				file = new File(System.currentTimeMillis() + ".jpg");
				outputStream = new FileOutputStream(file);
				outputStream.write(byteArrayOutputStream.toByteArray());
				outputStream.close();
				outputStream = null;
				SendPictureMailUtils.sendFiles(toUsers,subject,content,file);
				renderText("{}");
			}else{
				throw new AmbFrameException("图片不存在!");
			}
		} catch (Exception e) {
			createErrorMessage("发送邮件失败," + e.getMessage());
			log.error("发送邮件失败!",e);
		}finally{
			if(outputStream != null){
				outputStream.close();
			}
			if(file != null){
				file.delete();
			}
			if(byteArrayOutputStream != null){
				byteArrayOutputStream.close();
			}
		}
		return null;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	
    protected String renderText(String text)
    {
        return render(text, "text/plain;charset=UTF-8");
    }
    
	/**
	 * 绕过Template,直接输出内容的简便函数. 
	 */
	protected String render(String text, String contentType) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
