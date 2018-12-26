/**   
 * @Title: GsmMailSetting.java 
 * @Package com.ambition.gsm.entity 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2014-5-28 下午9:28:19 
 * @version V1.0   
 */ 
package com.ambition.gsm.entity;


/**
 * 类名: GsmMailSetting 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 */
public class GsmMailSetting {
	
	private Boolean advanceRemindSwitch;//是否鉴定前提醒
	private Float advanceRemindTime;//提前提醒时间
	private Boolean delayRemindSwitch;//是否检定超期提醒
	private Float delayRemindTime;//检定超期提醒时间
	public Boolean getAdvanceRemindSwitch() {
		return advanceRemindSwitch;
	}
	public void setAdvanceRemindSwitch(Boolean advanceRemindSwitch) {
		this.advanceRemindSwitch = advanceRemindSwitch;
	}
	public Float getAdvanceRemindTime() {
		return advanceRemindTime;
	}
	public void setAdvanceRemindTime(Float advanceRemindTime) {
		this.advanceRemindTime = advanceRemindTime;
	}
	public Boolean getDelayRemindSwitch() {
		return delayRemindSwitch;
	}
	public void setDelayRemindSwitch(Boolean delayRemindSwitch) {
		this.delayRemindSwitch = delayRemindSwitch;
	}
	public Float getDelayRemindTime() {
		return delayRemindTime;
	}
	public void setDelayRemindTime(Float delayRemindTime) {
		this.delayRemindTime = delayRemindTime;
	}
	
	

}
