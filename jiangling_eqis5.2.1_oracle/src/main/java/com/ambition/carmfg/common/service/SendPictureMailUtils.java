package com.ambition.carmfg.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.Assert;

import com.ambition.carmfg.entity.CustomMailInfo;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.organization.MailDeploy;
import com.norteksoft.acs.service.organization.CompanyManager;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:自定义发送带图片的邮件
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-1-20 发布
 */
public class SendPictureMailUtils {
	/**
	  * 方法名: 发送图片的邮件
	  * <p>功能说明：</p>
	  * @param to
	  * @param subject
	  * @param content
	  * @param file
	  * @return
	  * @throws AddressException
	  * @throws MessagingException
	 */
	public static void sendFiles(List<User> toUsers,String subject,String content,File file) throws AddressException, MessagingException{
		Map<String,List<String>> pathMap = new HashMap<String, List<String>>();
		Map<String,CustomMailInfo> mailInfoMap = new HashMap<String, CustomMailInfo>();
		for(User toUser : toUsers){
			if(StringUtils.isEmpty(toUser.getEmail())){
				throw new AmbFrameException("用户【"+toUser.getName()+"】的收件地址为空!");
			}
			CustomMailInfo info = getMailInfo(toUser);
//			CustomMailInfo info = new CustomMailInfo(true,"smtp","", port, user, password, from)
			if(info != null){
				if(!pathMap.containsKey(info.getFrom())){
					pathMap.put(info.getFrom(),new ArrayList<String>());
					mailInfoMap.put(info.getFrom(),info);
				}
				pathMap.get(info.getFrom()).add(toUser.getEmail());
			}
		}
		Map<String,String> fileMap = new HashMap<String, String>();
		fileMap.put(file.getName(),file.getAbsolutePath());
		for(String from : mailInfoMap.keySet()){
			CustomMailInfo info = mailInfoMap.get(from);
			sendMail(Boolean.valueOf(info.isAutheticate()), info.getProtocol(), info.getHost(), info.getPort(), info.getUser(), info.getPassword(), info.getFrom(),pathMap.get(from), subject, content, fileMap);
		}
	}
	
	private static void sendMail(Boolean isAutheticate, String protocol, String host, Integer port, String user, String password, String from, List<String> tos, 
            String subject, String content, Map<String,String> filePathMap)
        throws AddressException, MessagingException
    {
        Properties p = new Properties();
        p.put("mail.smtp.auth", isAutheticate.toString());
        p.put("mail.transport.protocol", protocol);
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.port", port);
        Session session = Session.getInstance(p);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = new InternetAddress[tos.size()];
        for(int i=0;i<tos.size();i++){
        	address[i] = new InternetAddress(tos.get(i));
        }
        msg.setRecipients(javax.mail.Message.RecipientType.TO,address);
        msg.setSentDate(new Date());
        msg.setSubject(subject);
        Multipart mulp = new MimeMultipart();
        String fileFullPath = null;
        List<String> pictures = new ArrayList<String>();
        if(filePathMap != null)
        {
        	int index = 0;
        	for(String fileName : filePathMap.keySet()){
        		fileFullPath = filePathMap.get(fileName);
                if(StringUtils.isNotEmpty(fileName)&&StringUtils.isNotEmpty(fileFullPath))
                {
                    File f = new File(fileFullPath);
                    if(f.exists())
                    {
                    	MimeBodyPart filePart = new MimeBodyPart();
                    	DataSource ds3 = new FileDataSource(fileFullPath);
                    	DataHandler dataHandler3 = new DataHandler(ds3);
                    	filePart.setDataHandler(dataHandler3);
                    	filePart.setFileName(fileName);
                    	String contentId = "picture" + index++;
                    	filePart.setContentID(contentId);
                        mulp.addBodyPart(filePart);
                        pictures.add(contentId);
                    }
                }
        	}
        }
        MimeBodyPart remarkPart = new MimeBodyPart();
        if(content == null){
        	content = "";
        }
        content = content.replaceAll("\\n","<br/>").replaceAll(" ","&nbsp;&nbsp;");
        StringBuilder contentSB = new StringBuilder("<table style='width:100%;'><tr><td style='padding-bottom:4px;'>" + content + "</td></tr>");
        for(String partId : pictures){
        	contentSB.append("<tr><td><img src='cid:" + partId +"'/><td></tr>");
        }
        remarkPart.setContent(contentSB.toString(),"text/html;charset=gbk");
        mulp.addBodyPart(remarkPart);
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setText(content);
        mulp.addBodyPart(mbp);
        
        msg.setContent(mulp);
        Transport tran = session.getTransport(protocol);
        tran.connect(host, user, password);
        tran.sendMessage(msg, msg.getAllRecipients());
    }
	private static CustomMailInfo getMailInfo(User user)
    {
        CompanyManager companyManager = (CompanyManager)ContextUtils.getBean("companyManager");
        MailDeploy mailDeploy = companyManager.getMailDeployByCompanyId();

        String protocol = "smtp";
        Integer port = Integer.valueOf(25);
        Boolean isAutheticate;
        String host;
        String userName;
        String password;
        String from;
        if(MailboxDeploy.INSIDE.equals(user.getMailboxDeploy()))
        {
            isAutheticate = getSmtpAuth(mailDeploy.getSmtpAuthInside());
            if(StringUtils.isNotEmpty(mailDeploy.getTransportProtocolInside()))
                protocol = mailDeploy.getTransportProtocolInside();
            Assert.notNull(mailDeploy.getSmtpHostInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u90AE\u4EF6\u670D\u52A1\u5668\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            host = mailDeploy.getSmtpHostInside();
            if(StringUtils.isNotEmpty(mailDeploy.getSmtpPortInside()))
                port = Integer.valueOf(NumberUtils.toInt(mailDeploy.getSmtpPortInside(), 25));
            Assert.notNull(mailDeploy.getHostUserInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7AEF\u7528\u6237\u540D]\u4E0D\u80FD\u4E3A\u7A7A  ");
            userName = mailDeploy.getHostUserInside();
            Assert.notNull(mailDeploy.getHostUserPasswordInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7528\u6237\u5BC6\u7801]\u4E0D\u80FD\u4E3A\u7A7A  ");
            password = mailDeploy.getHostUserPasswordInside();
            Assert.notNull(mailDeploy.getHostUserFromInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u4E3B\u673A\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            from = mailDeploy.getHostUserFromInside();
        } else
        {
            isAutheticate = getSmtpAuth(mailDeploy.getSmtpAuthExterior());
            if(StringUtils.isNotEmpty(mailDeploy.getTransportProtocolExterior()))
                protocol = mailDeploy.getTransportProtocolExterior();
            Assert.notNull(mailDeploy.getSmtpHostExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u90AE\u4EF6\u670D\u52A1\u5668\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            host = mailDeploy.getSmtpHostExterior();
            if(StringUtils.isNotEmpty(mailDeploy.getSmtpPortExterior()))
                port = Integer.valueOf(NumberUtils.toInt(mailDeploy.getSmtpPortExterior(), 25));
            Assert.notNull(mailDeploy.getHostUserExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7AEF\u7528\u6237\u540D]\u4E0D\u80FD\u4E3A\u7A7A  ");
            userName = mailDeploy.getHostUserExterior();
            Assert.notNull(mailDeploy.getHostUserPasswordExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7528\u6237\u5BC6\u7801]\u4E0D\u80FD\u4E3A\u7A7A  ");
            password = mailDeploy.getHostUserPasswordExterior();
            Assert.notNull(mailDeploy.getHostUserFromExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u4E3B\u673A\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            from = mailDeploy.getHostUserFromExterior();
        }
        return new CustomMailInfo(isAutheticate.booleanValue(), protocol, host, port, userName, password, from);
    }
	
	private static Boolean getSmtpAuth(String smtpAuth)
    {
        Boolean isAutheticate;
        if(StringUtils.isEmpty(smtpAuth))
            isAutheticate = Boolean.valueOf(true);
        else
            isAutheticate = Boolean.valueOf(smtpAuth);
        return isAutheticate;
    }
}
