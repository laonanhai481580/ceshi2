package com.ambition.carmfg.entity;
/**
 * 
 * 类名:巡检周期设置
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-9-5 发布
 */
public class PatrolSettings{
	private String timeIntervalType;//巡检周期类型
	private String timeIntervalValue;//巡检周期值
	private Boolean remindSwitch;//是否到期未巡检提醒
	private String remindTimeType;//到期未巡检判断类型
	private String remindTimeValue;//到期未巡检判断值
	private String triggerType;//触发类型
	private String triggerValue;//触发的时间值
	private String receiveTypes;//到期未巡检接收消息类型
	private String receiveUserIds;//到期未巡检接收人员ID
	private String receiveUserNames;//到期未巡检接收人员名称
	public String getTimeIntervalType() {
		return timeIntervalType;
	}
	public void setTimeIntervalType(String timeIntervalType) {
		this.timeIntervalType = timeIntervalType;
	}
	public String getTimeIntervalValue() {
		return timeIntervalValue;
	}
	public void setTimeIntervalValue(String timeIntervalValue) {
		this.timeIntervalValue = timeIntervalValue;
	}
	public Boolean getRemindSwitch() {
		return remindSwitch;
	}
	public void setRemindSwitch(Boolean remindSwitch) {
		this.remindSwitch = remindSwitch;
	}
	public String getRemindTimeType() {
		return remindTimeType;
	}
	public void setRemindTimeType(String remindTimeType) {
		this.remindTimeType = remindTimeType;
	}
	public String getRemindTimeValue() {
		return remindTimeValue;
	}
	public void setRemindTimeValue(String remindTimeValue) {
		this.remindTimeValue = remindTimeValue;
	}
	public String getReceiveTypes() {
		return receiveTypes;
	}
	public void setReceiveTypes(String receiveTypes) {
		this.receiveTypes = receiveTypes;
	}
	public String getReceiveUserIds() {
		return receiveUserIds;
	}
	public void setReceiveUserIds(String receiveUserIds) {
		this.receiveUserIds = receiveUserIds;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public String getTriggerValue() {
		return triggerValue;
	}
	public void setTriggerValue(String triggerValue) {
		this.triggerValue = triggerValue;
	}
	public String getReceiveUserNames() {
		return receiveUserNames;
	}
	public void setReceiveUserNames(String receiveUserNames) {
		this.receiveUserNames = receiveUserNames;
	}
}
