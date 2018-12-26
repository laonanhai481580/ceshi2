/**   
 * @Title: MailSendContent.java 
 * @Package com.ambition.gsm.entity 
 * @Description: (邮件发送人，日期，内容。邮件发送引擎的实体) 
 * @author 刘承斌   
 * @date 2014-5-29 下午4:28:36 
 * @version V1.0   
 */ 
package com.ambition.util.mail.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.norteksoft.product.orm.IdEntity;

/**
 * 类名: MailSendContent 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：邮件发送人，日期，内容。邮件发送引擎的实体</p>
 * @author  刘承斌
 * @version 1.00  2015-3-15 下午1:57:29  发布
 */
@Entity
@Table(name="AMB_MAIL_SEND_CONTENT")
public class MailSendContent extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String tos;//发送对象，发送者登录名，以逗号隔开
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendDate;//发送时间
	private String subject;//主题
	private String contents;//内容
	@Column(length=1)
	private String deleted="0";//0未删除,1已删除(已发送)
	
	public String getTos() {
		return tos;
	}
	public void setTos(String tos) {
		this.tos = tos;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
}
