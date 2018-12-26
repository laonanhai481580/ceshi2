package com.ambition.gp.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;





import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**    
 * 质量公告
 * @authorBy wlongfeng
 *
 */
@Entity
@Table(name="GP_QUALITY_ANNOUNCEMENT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class QualityAnnouncement extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String announcementType;//公告类型
	private String territorial;//属地,集团、DBG、B1、B2、B3…...B10 、BMDT、TM1
	private String contentClassification;//内容分类,社会新闻、内部
	private String title;//标题
	private String  content;//正文,内容
	private String  contentHtml;//正文html
	private Long contentHtmlFileId;//正文内容附件ID
	/**
	 * 仅供显示的内容,不保存到数据库
	 */
	@Transient
	private String showContentHtml;//显示的内容HTML
	private String coverImg;//封面图片
	private String attachFile;//附件
	private String publishOrganization;//发布组织
	private String publisher;//发布人
	@Temporal(TemporalType.TIMESTAMP)
	private Date  releaseTime;//发布时间
	private String releaseStatus;//发布状态
	private String sendMail;//邮件发送人
	private String sendMailLogin;//邮件发送人
	private String isRelease="0";//是否发布 0:否，1：是 
	private Long topFlag=0l;//置顶
	
	public String getTerritorial() {
		return territorial;
	}
	public void setTerritorial(String territorial) {
		this.territorial = territorial;
	}
	public String getContentClassification() {
		return contentClassification;
	}
	public void setContentClassification(String contentClassification) {
		this.contentClassification = contentClassification;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}
	public String getPublishOrganization() {
		return publishOrganization;
	}
	public void setPublishOrganization(String publishOrganization) {
		this.publishOrganization = publishOrganization;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Date getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	public String getAnnouncementType() {
		return announcementType;
	}
	public void setAnnouncementType(String announcementType) {
		this.announcementType = announcementType;
	}
	public String getSendMail() {
		return sendMail;
	}
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}
	public String getCoverImg() {
		return coverImg;
	}
	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}
	public String getIsRelease() {
		return isRelease;
	}
	public void setIsRelease(String isRelease) {
		this.isRelease = isRelease;
	}
	public Long getTopFlag() {
		return topFlag;
	}
	public void setTopFlag(Long topFlag) {
		this.topFlag = topFlag;
	}
	public String getReleaseStatus() {
		return releaseStatus;
	}
	public void setReleaseStatus(String releaseStatus) {
		this.releaseStatus = releaseStatus;
	}
	public Long getContentHtmlFileId() {
		return contentHtmlFileId;
	}
	public void setContentHtmlFileId(Long contentHtmlFileId) {
		this.contentHtmlFileId = contentHtmlFileId;
	}
	public String getShowContentHtml() {
		return showContentHtml;
	}
	public void setShowContentHtml(String showContentHtml) {
		this.showContentHtml = showContentHtml;
	}
	public String getSendMailLogin() {
		return sendMailLogin;
	}
	public void setSendMailLogin(String sendMailLogin) {
		this.sendMailLogin = sendMailLogin;
	}
	
}
