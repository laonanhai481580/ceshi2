package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:体系维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Entity                                  
@Table(name = "QSM_SYSTEM_MAINTENANCE")//QSM_SYSTEM_MAINTENANCE
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SystemMaintenance extends IdEntity{

		/**
		  *SystemCertificate.java2016年9月26日
		 */
	private static final long serialVersionUID = 1L;
	private String enterpriseGroup;//事业群
	private String systemType;//体系类型
	private String fileType;//类别
	private String version;//版本
	private String attachment;//附件
	private String uploadMan;//上传人
	private Date uploadDate;//上传时间
	private String remark;//备注
	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}
	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getUploadMan() {
		return uploadMan;
	}
	public void setUploadMan(String uploadMan) {
		this.uploadMan = uploadMan;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
