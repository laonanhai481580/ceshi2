package com.ambition.gsm.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:检验项目明细
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-24 发布
 */
@Entity
@Table(name = "GSM_CHECK_REPORT_DETAIL")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CheckReportDetail  extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String itemName;//项目名称
	private String standardValue;//标准值
	private String indicatedValue;//指示值
	private String errorValue;//误差值
	private String passOrFail;//passOrFail
	private String allowableError;//允许误差		
	private String remark;//备注
    @ManyToOne
	@JoinColumn(name = "FK_CHECK_REPORT_ID")
    private GsmInnerCheckReport gsmInnerCheckReport;//內校报告
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}
	public String getIndicatedValue() {
		return indicatedValue;
	}
	public void setIndicatedValue(String indicatedValue) {
		this.indicatedValue = indicatedValue;
	}
	public String getErrorValue() {
		return errorValue;
	}
	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}
	public String getPassOrFail() {
		return passOrFail;
	}
	public void setPassOrFail(String passOrFail) {
		this.passOrFail = passOrFail;
	}
	public String getAllowableError() {
		return allowableError;
	}
	public void setAllowableError(String allowableError) {
		this.allowableError = allowableError;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public GsmInnerCheckReport getGsmInnerCheckReport() {
		return gsmInnerCheckReport;
	}
	public void setGsmInnerCheckReport(GsmInnerCheckReport gsmInnerCheckReport) {
		this.gsmInnerCheckReport = gsmInnerCheckReport;
	}



	
    
    
}