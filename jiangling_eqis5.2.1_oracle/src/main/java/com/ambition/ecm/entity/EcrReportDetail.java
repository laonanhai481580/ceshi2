package com.ambition.ecm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 类名:工程变更明细
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-11-9 发布
 */
@Entity
@Table(name="ECM_ECR_REPORT_DETAIL")
public class EcrReportDetail extends IdEntity {
	
	private static final long serialVersionUID = 1L;
	private String beforeCode;
	private String beforeName;
	private String afterCode;
	private String afterName;
	private String describe;//描述
	@ManyToOne
	@JoinColumn(name="FK_ECR_REPORT")
	@JsonIgnore()
	private EcrReport ecrReport;
	
	public String getBeforeCode() {
		return beforeCode;
	}
	public void setBeforeCode(String beforeCode) {
		this.beforeCode = beforeCode;
	}
	public String getBeforeName() {
		return beforeName;
	}
	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}
	public String getAfterCode() {
		return afterCode;
	}
	public void setAfterCode(String afterCode) {
		this.afterCode = afterCode;
	}
	public String getAfterName() {
		return afterName;
	}
	public void setAfterName(String afterName) {
		this.afterName = afterName;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public EcrReport getEcrReport() {
		return ecrReport;
	}
	public void setEcrReport(EcrReport ecrReport) {
		this.ecrReport = ecrReport;
	}
	
	
}
