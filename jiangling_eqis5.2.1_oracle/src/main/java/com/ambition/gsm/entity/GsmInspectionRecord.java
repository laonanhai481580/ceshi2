package com.ambition.gsm.entity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.product.orm.IdEntity;

/**
 * 校验记录(ENTITY)
 * @author 张顺治
 *
 */
@SuppressWarnings("unused")
@Entity
@Table(name="GSM_INSPECTION_RECORD")
public class GsmInspectionRecord extends IdEntity{
	private static final long serialVersionUID = 1L;
	private static final String STATE_COMPLETE_ON_TIME = "按时完成";
	private static final String STATE_COMPLETE_EXCEED_TIME = "超期完成";
	private static final String STATE_NOT_COMPLETE_EXCEED_TIME = "超期未完成";
	private static final String STATE_ON_GOING = "进行中";
	
	@Column(length=50,name="user_name")
	private String user;//使用人
	private String checkState;//校验状态
	private String resultState;//结果状态 ：1.按时完成 2.超期完成 3.超期未完成 4.进行中
	private String surveyor;//鉴定员
	private Boolean isPlan;//是否为计划检定
	private String moth;//检定月份
	@JsonIgnore
	private Date planDate;// 计划
	@JsonIgnore
	private Date actualDate;// 实际
	
	@ManyToOne
	@JoinColumn(name="FK_INSPECTION_PLAN_ID")
	private InspectionPlan inspectionPlan;//检定计划
	
	@OneToMany(mappedBy="gsmIspectionRecord",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<InspectionRecordAttach> InspectionRecordAttachs;//检查项目
	
	@OneToMany(mappedBy="gsmInspectionRecord",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<GsmMailSendContent> gsmMailSendContents;//发送邮件
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCheckState() {
		return checkState;
	}
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	public String getSurveyor() {
		return surveyor;
	}
	public void setSurveyor(String surveyor) {
		this.surveyor = surveyor;
	}

	public String getResultState() {
		return resultState;
	}
	public void setResultState(String resultState) {
		this.resultState = resultState;
	}
	public Boolean getIsPlan() {
		return isPlan;
	}
	public void setIsPlan(Boolean isPlan) {
		this.isPlan = isPlan;
	}
	public InspectionPlan getInspectionPlan() {
		return inspectionPlan;
	}
	public void setInspectionPlan(InspectionPlan inspectionPlan) {
		this.inspectionPlan = inspectionPlan;
	}
	public String getMoth() {
		return moth;
	}
	public void setMoth(String moth) {
		this.moth = moth;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	public List<InspectionRecordAttach> getInspectionRecordAttachs() {
		return InspectionRecordAttachs;
	}
	public void setInspectionRecordAttachs(
			List<InspectionRecordAttach> inspectionRecordAttachs) {
		InspectionRecordAttachs = inspectionRecordAttachs;
	}
	
	public List<GsmMailSendContent> getGsmMailSendContents() {
		return gsmMailSendContents;
	}
	public void setGsmMailSendContents(List<GsmMailSendContent> gsmMailSendContents) {
		this.gsmMailSendContents = gsmMailSendContents;
	}
	public Object clone(){
		ByteArrayOutputStream bout = null;
		ObjectOutputStream out = null;
		ByteArrayInputStream bin = null;
		try 
		{
			bout  =   new  ByteArrayOutputStream();
		    out  =   new  ObjectOutputStream(bout);
		    out.writeObject( this );
		    bin  =   new  ByteArrayInputStream(bout
		    		.toByteArray());
		    ObjectInputStream in  =   new  ObjectInputStream(bin);
		    return in.readObject();
		} catch (IOException e) {
			throw new RuntimeException("对象克隆失败!",e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("对象克隆失败!",e);
		}finally{
			if(bout != null){
				try {
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bin != null){
				try {
					bin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}  
	}
}
