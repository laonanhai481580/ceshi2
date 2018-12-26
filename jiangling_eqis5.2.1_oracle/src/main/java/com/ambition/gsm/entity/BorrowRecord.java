package com.ambition.gsm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="GSM_BORROW_RECORD")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class BorrowRecord extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "GSM_BORROW_RECORD";//实体编码
	public static final String ENTITY_LIST_NAME = "仪器借调申请单";//实体名称
	private String formNo;// 表单编号
	private String businessDivision;// 所属事业部
	private Date borrowDate;// 借用时间
	private Date returnDate;// 预定归还时间
	private String borrower;// 借用人
	private String borrowerDept;// 借用部门
	private String depository;// 保管确认
	private String preserver;// 原保管人
	private String preserverDept;// 保管部门
	private Date preserverDate;// 保管日期
	private String laboratory;//实验室确认
	private String confirmor;// 确认人
	private String confirmorDept;// 确认部门
	private Date confirmorDate;// 确认日期
	private String preserverLogin;// 保管人登入名
	private String confirmorLogin;// 确认人登入名
	
	
	@OneToMany(mappedBy = "borrowRecord",cascade={CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<BorrowRecordSublist> borrowRecordSublists;
	
//	@ManyToOne
//	@JoinColumn(name="GSM_SUBLIST_ID")
//  @JsonIgnore()
//	private BorrowRecordSublist brs;
	
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public Date getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public String getBorrowerDept() {
		return borrowerDept;
	}
	public void setBorrowerDept(String borrowerDept) {
		this.borrowerDept = borrowerDept;
	}
	public String getDepository() {
		return depository;
	}
	public void setDepository(String depository) {
		this.depository = depository;
	}
	public String getPreserver() {
		return preserver;
	}
	public void setPreserver(String preserver) {
		this.preserver = preserver;
	}
	public String getPreserverDept() {
		return preserverDept;
	}
	public void setPreserverDept(String preserverDept) {
		this.preserverDept = preserverDept;
	}
	public Date getPreserverDate() {
		return preserverDate;
	}
	public void setPreserverDate(Date preserverDate) {
		this.preserverDate = preserverDate;
	}
	public String getLaboratory() {
		return laboratory;
	}
	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}
	public String getConfirmor() {
		return confirmor;
	}
	public void setConfirmor(String confirmor) {
		this.confirmor = confirmor;
	}
	public String getConfirmorDept() {
		return confirmorDept;
	}
	public void setConfirmorDept(String confirmorDept) {
		this.confirmorDept = confirmorDept;
	}
	public Date getConfirmorDate() {
		return confirmorDate;
	}
	public void setConfirmorDate(Date confirmorDate) {
		this.confirmorDate = confirmorDate;
	}
	public String getPreserverLogin() {
		return preserverLogin;
	}
	public void setPreserverLogin(String preserverLogin) {
		this.preserverLogin = preserverLogin;
	}
	public String getConfirmorLogin() {
		return confirmorLogin;
	}
	public void setConfirmorLogin(String confirmorLogin) {
		this.confirmorLogin = confirmorLogin;
	}
	public List<BorrowRecordSublist> getBorrowRecordSublists() {
		return borrowRecordSublists;
	}
	public void setBorrowRecordSublists(
			List<BorrowRecordSublist> borrowRecordSublists) {
		this.borrowRecordSublists = borrowRecordSublists;
	}
	public String getBusinessDivision() {
		return businessDivision;
	}
	public void setBusinessDivision(String businessDivision) {
		this.businessDivision = businessDivision;
	}
//	public BorrowRecordSublist getBrs() {
//		return brs;
//	}
//	public void setBrs(BorrowRecordSublist brs) {
//		this.brs = brs;
//	}
	
}
