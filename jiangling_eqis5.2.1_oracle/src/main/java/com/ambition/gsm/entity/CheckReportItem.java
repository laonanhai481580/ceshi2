package com.ambition.gsm.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:校验标准件
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-24 发布
 */
@Entity
@Table(name = "GSM_CHECK_REPORT_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CheckReportItem extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String standardName;//标准件名称
	private String standardNo;//标准件编号
	private Date validityDate;//有效期至
	private String certificateNo;//证书编号	
    @ManyToOne
	@JoinColumn(name = "FK_CHECK_REPORT_ID")
    private GsmInnerCheckReport gsmInnerCheckReport;//內校报告
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public String getStandardNo() {
		return standardNo;
	}
	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}
	public Date getValidityDate() {
		return validityDate;
	}
	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}
	public String getCertificateNo() {
		return certificateNo;
	}
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	public GsmInnerCheckReport getGsmInnerCheckReport() {
		return gsmInnerCheckReport;
	}
	public void setGsmInnerCheckReport(GsmInnerCheckReport gsmInnerCheckReport) {
		this.gsmInnerCheckReport = gsmInnerCheckReport;
	}

	
    
    
}