package com.ambition.gsm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:基础维护-检验标准
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-24 发布
 */
@Entity
@Table(name = "GSM_CHECK_STANDARD")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CheckStandard  extends IdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "GSM_CHECK_STANDARD";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "检验标准";//实体_列表_名称
	private String measurementName;//仪器名称
	private String measurementSpecification;//规格型号
	private String manufacturer;//制造厂商 
	private String attachment;//附件
	private String remark;//备注
	private Long standardAttachId;//标准文件的ID
	@OneToMany(mappedBy="checkStandard",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<CheckStandardDetail> checkItems;//检验项目明细
	public String getMeasurementName() {
		return measurementName;
	}
	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}
	public String getMeasurementSpecification() {
		return measurementSpecification;
	}
	public void setMeasurementSpecification(String measurementSpecification) {
		this.measurementSpecification = measurementSpecification;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<CheckStandardDetail> getCheckItems() {
		return checkItems;
	}
	public void setCheckItems(List<CheckStandardDetail> checkItems) {
		this.checkItems = checkItems;
	}
	public Long getStandardAttachId() {
		return standardAttachId;
	}
	public void setStandardAttachId(Long standardAttachId) {
		this.standardAttachId = standardAttachId;
	}
	
	
	
	
}
