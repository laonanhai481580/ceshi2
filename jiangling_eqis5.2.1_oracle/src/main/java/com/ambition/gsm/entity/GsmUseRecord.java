package com.ambition.gsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;


/**
 * 计量器具用户使用记录(ENTITY)
 * @author 张顺治
 *
 */
@Entity
@Table(name = "GSM_USE_RECORD")
public class GsmUseRecord extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String measurementNo;// 器具编号/统一编号
	private String measurementSerialNo;//
	private String measurementName;// 器具名称
	private String measurementSpecification;// 器具型号/规格
	private String useDept;
	private String borrower;// 领用人
	private Integer amount;// 领用数量
	@Temporal(TemporalType.TIMESTAMP)
	private Date borrowDate;// 领用时间
	private String returnDept;// 归还部门
	private String returner;// 归还人
	@Temporal(TemporalType.TIMESTAMP)
	private Date returnDate;// 预定归还时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date realReturnDate;// 实际归还时间
	private String borrowedConfirmPeople;// 借还确认人
	private String borrowedRemark;// 借还备
	private String distributionPeople;// 发放人
	private String light;// 红警灯
	
	@ManyToOne
	@JoinColumn(name = "FK_MEASUREMENT_USE_RECORD_ID")
	private GsmEquipment gsmEquipment;

	public String getMeasurementSerialNo() {
		return measurementSerialNo;
	}

	public void setMeasurementSerialNo(String measurementSerialNo) {
		this.measurementSerialNo = measurementSerialNo;
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		this.light = light;
	}

	public String getDistributionPeople() {
		return distributionPeople;
	}

	public void setDistributionPeople(String distributionPeople) {
		this.distributionPeople = distributionPeople;
	}

	public String getMeasurementNo() {
		return measurementNo;
	}

	public void setMeasurementNo(String measurementNo) {
		this.measurementNo = measurementNo;
	}

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

	public String getUseDept() {
		return useDept;
	}

	public void setUseDept(String useDept) {
		this.useDept = useDept;
	}

	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}

	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}

	public String getBorrowedConfirmPeople() {
		return borrowedConfirmPeople;
	}

	public void setBorrowedConfirmPeople(String borrowedConfirmPeople) {
		this.borrowedConfirmPeople = borrowedConfirmPeople;
	}

	public String getBorrowedRemark() {
		return borrowedRemark;
	}

	public void setBorrowedRemark(String borrowedRemark) {
		this.borrowedRemark = borrowedRemark;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public String getReturnDept() {
		return returnDept;
	}

	public void setReturnDept(String returnDept) {
		this.returnDept = returnDept;
	}

	public String getReturner() {
		return returner;
	}

	public void setReturner(String returner) {
		this.returner = returner;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getRealReturnDate() {
		return realReturnDate;
	}

	public void setRealReturnDate(Date realReturnDate) {
		this.realReturnDate = realReturnDate;
	}

}
