package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "QSM_SIGN_MEASURE_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SignMeasureItem extends IdEntity{

	private static final long serialVersionUID = 1L;

	private String nodeName;//节点名称
	private String attachment;//附件
	private String signContent;//意见
	private String signMan;//会签人
	private String signManLogin;//会签人登录名
	private Date signDate;//时间
	@ManyToOne
	@JoinColumn(name = "QSM_CORRECT_MEASURES_ID")
	@JsonIgnore()
	private CorrectMeasures correctMeasures;
	

	public CorrectMeasures getCorrectMeasures() {
		return correctMeasures;
	}
	public void setCorrectMeasures(CorrectMeasures correctMeasures) {
		this.correctMeasures = correctMeasures;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getSignContent() {
		return signContent;
	}
	public void setSignContent(String signContent) {
		this.signContent = signContent;
	}
	public String getSignMan() {
		return signMan;
	}
	public void setSignMan(String signMan) {
		this.signMan = signMan;
	}
	public String getSignManLogin() {
		return signManLogin;
	}
	public void setSignManLogin(String signManLogin) {
		this.signManLogin = signManLogin;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	
	
}
