package com.ambition.gsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 量检具维修管理(ENTITY)
 * @author 张顺志
 *
 */
@Entity
@Table(name = "GSM_EQUIPMENT_MAINTENANCE")
public class GsmEquipmentMaintenance  extends IdEntity { 
	private static final long serialVersionUID = 1L;
	public final static String STATE_MAINTENANCE_WAIT = "待维修";
	public final static String STATE_MAINTENANCE_IN = "维修中";
	public final static String STATE_MAINTENANCE_OUT = "维修完";
	
	@JsonIgnore
	private Date lastedMaintenanceDate;//上次维修日期
	private Date maintenanceDate;//送修日期
	private Date anticipatedFixedDate;//预计修好日期
	private int sendTimes=0;//送修次数
	private String maintenanceState;//送修状态   //维修中，维修完成
	private String remark;//备注
	@JsonIgnore
	private Date repairDate;//维修日期
	private String  repairPeople;//维修人
	private String attachment;//附件
	
	@ManyToOne
	@JoinColumn(name="FK_GSM_EQUIPMENT_ID")
	private GsmEquipment gsmEquipment;
	
	public String getRepairPeople() {
		return repairPeople;
	}
	public void setRepairPeople(String repairPeople) {
		this.repairPeople = repairPeople;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public Date getRepairDate() {
		return repairDate;
	}
	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}
	public Date getLastedMaintenanceDate() {
		return lastedMaintenanceDate;
	}
	public void setLastedMaintenanceDate(Date lastedMaintenanceDate) {
		this.lastedMaintenanceDate = lastedMaintenanceDate;
	}
	public Date getMaintenanceDate() {
		return maintenanceDate;
	}
	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	public Date getAnticipatedFixedDate() {
		return anticipatedFixedDate;
	}
	public void setAnticipatedFixedDate(Date anticipatedFixedDate) {
		this.anticipatedFixedDate = anticipatedFixedDate;
	}
	public int getSendTimes() {
		return sendTimes;
	}
	public void setSendTimes(int sendTimes) {
		this.sendTimes = sendTimes;
	}
	public String getMaintenanceState() {
		return maintenanceState;
	}
	public void setMaintenanceState(String maintenanceState) {
		this.maintenanceState = maintenanceState;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}
	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}
	
} 