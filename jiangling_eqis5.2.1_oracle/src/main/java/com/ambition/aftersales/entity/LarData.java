package com.ambition.aftersales.entity;

import java.util.Date;
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
 * 类名:LAR批次合格率
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Entity
@Table(name = "AFS_LAR_DATA")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class LarData extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String workingProcedure;//新增工序
	private Date larDate;//日期
	//private String businessUnit;//事业部
	private String customerCode;//客户编码
	private String customer;//客户名称
	private String customerFactory;//  新增 客户场地
	private String ofilmModel;//欧菲机种
	private String customerModel;//客户端机种
	
	private Integer inputBatchNum;//入料批数
	private Integer inspectionBatchNum;//检验批数
	private Integer qualifiedBatchNum;//合格批数
	private String batchQualificationRate;//批合格率
	private Integer inputCount;//    入料数
	private Integer inspectionCount;//抽检数
	private Integer qualifiedCount;//良数
	private Integer unqualifiedCount;//不良数
	private String qualifiedRate;//良率
	private String unqualifiedRate;//不良率
	
	private String unqualifiedDppm;//不良Dppm UnqualifiedDppm
	private String badItem;//不良项
	
	@OneToMany(mappedBy = "larData",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<LarDefectiveItem> larDefectiveItems;//不良明细
	public Date getLarDate() {
		return larDate;
	}
	public void setLarDate(Date larDate) {
		this.larDate = larDate;
	}

	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerModel() {
		return customerModel;
	}
	public void setCustomerModel(String customerModel) {
		this.customerModel = customerModel;
	}
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public Integer getInspectionCount() {
		return inspectionCount;
	}
	public void setInspectionCount(Integer inspectionCount) {
		this.inspectionCount = inspectionCount;
	}
	public Integer getQualifiedCount() {
		return qualifiedCount;
	}
	public void setQualifiedCount(Integer qualifiedCount) {
		this.qualifiedCount = qualifiedCount;
	}

	public Integer getInputBatchNum() {
		return inputBatchNum;
	}
	public void setInputBatchNum(Integer inputBatchNum) {
		this.inputBatchNum = inputBatchNum;
	}
	public Integer getInspectionBatchNum() {
		return inspectionBatchNum;
	}
	public void setInspectionBatchNum(Integer inspectionBatchNum) {
		this.inspectionBatchNum = inspectionBatchNum;
	}
	public Integer getQualifiedBatchNum() {
		return qualifiedBatchNum;
	}
	public void setQualifiedBatchNum(Integer qualifiedBatchNum) {
		this.qualifiedBatchNum = qualifiedBatchNum;
	}
	public Integer getInputCount() {
		return inputCount;
	}
	public void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}
	public Integer getUnqualifiedCount() {
		return unqualifiedCount;
	}
	public void setUnqualifiedCount(Integer unqualifiedCount) {
		this.unqualifiedCount = unqualifiedCount;
	}
	public String getBadItem() {
		return badItem;
	}
	public void setBadItem(String badItem) {
		this.badItem = badItem;
	}
	public String getWorkingProcedure() {
		return workingProcedure;
	}
	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}
	public String getCustomerFactory() {
		return customerFactory;
	}
	public void setCustomerFactory(String customerFactory) {
		this.customerFactory = customerFactory;
	}


	public String getUnqualifiedRate() {
		return unqualifiedRate;
	}
	public void setUnqualifiedRate(String unqualifiedRate) {
		this.unqualifiedRate = unqualifiedRate;
	}

/*	public List<VlrrDefectiveItem> getVlrrDefectiveItems() {
		return vlrrDefectiveItems;
	}
	public void setVlrrDefectiveItems(List<VlrrDefectiveItem> vlrrDefectiveItems) {
		this.vlrrDefectiveItems = vlrrDefectiveItems;
	}*/
	
	public String getQualifiedRate() {
		return qualifiedRate;
	}

	public List<LarDefectiveItem> getLarDefectiveItems() {
		return larDefectiveItems;
	}
	public void setLarDefectiveItems(List<LarDefectiveItem> larDefectiveItems) {
		this.larDefectiveItems = larDefectiveItems;
	}
	public void setQualifiedRate(String qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}

	public String getBatchQualificationRate() {
		return batchQualificationRate;
	}
	public void setBatchQualificationRate(String batchQualificationRate) {
		this.batchQualificationRate = batchQualificationRate;
	}
	public String getUnqualifiedDppm() {
		return unqualifiedDppm;
	}
	public void setUnqualifiedDppm(String unqualifiedDppm) {
		this.unqualifiedDppm = unqualifiedDppm;
	}
	
	
}
