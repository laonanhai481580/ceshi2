/**   
 * @Title: GsmMailSendContent.java 
 * @Package com.ambition.gsm.entity 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2014-5-29 下午4:28:36 
 * @version V1.0   
 */ 
package com.ambition.gsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.norteksoft.product.orm.IdEntity;


/**
 * 类名: GsmMailSendContent 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  张顺志
 * 
 */
@Entity
@Table(name="GSM_MAIL_SEND_CONTENT")
public class GsmMailSendContent extends IdEntity {
	private static final long serialVersionUID = 1L;
	public static final String ADVANCE_SUBJECT="提前提醒邮件";
	public static final String DELAY_SUBJECT="超期提醒邮件";

	private String tos;
	private Date warnDate;
	private String subject;
	private String contents;
	
	@ManyToOne
	@JoinColumn(name="FK_INSPECTION_RECORD")
	private GsmInspectionRecord gsmInspectionRecord;
	
	
	public String getTos() {
		return tos;
	}
	public void setTos(String tos) {
		this.tos = tos;
	}
	public Date getWarnDate() {
		return warnDate;
	}
	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
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
	public GsmInspectionRecord getGsmInspectionRecord() {
		return gsmInspectionRecord;
	}
	public void setGsmInspectionRecord(GsmInspectionRecord gsmInspectionRecord) {
		this.gsmInspectionRecord = gsmInspectionRecord;
	}
	
}
