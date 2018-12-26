package com.ambition.util.erp.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="message")
public class UntreatedMessage {
	private String category;//类型,必填
	private String content;//内容描述,必填
	private String sender;//发起人登录名(工号)
	private String receiver;//接收人登录名(工号)
	private String url;//链接地址,当为提示消息时链接地址为空,其他办理地址 
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	  * 方法名: 创建返回值的消息
	  * <p>功能说明：</p>
	  * @param category 类型,必填
	  * @param content 内容描述,必填
	  * @param sender
	  * @param receiver
	  * @param url
	  * @return
	 */
	public static UntreatedMessage creaMessage(String category,String content,String sender,String receiver,String url){
		UntreatedMessage message = new UntreatedMessage();
		message.setCategory(category);
		message.setContent(content);
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setUrl(url);
		return message;
	}
}
