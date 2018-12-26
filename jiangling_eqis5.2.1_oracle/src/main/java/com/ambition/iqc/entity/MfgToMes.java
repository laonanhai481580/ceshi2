package com.ambition.iqc.entity;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * 类名:传送给MES 信息的xml entity
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016-10-9 发布
 */
@XmlRootElement(namespace="http://tempuri.org/")
public class MfgToMes {
//	@XmlElement(name="ProcessCard")
	private String ProcessCard;//流程卡
//	@XmlElement(name="WorkProcedure")
	private String WorkProcedure;//工序
//	private String objId;//接口名称
//	private String operate;//操作类型
//	private String godownEntryNumber;//收料单号
//	private String entryId;//分录号
//	private String entryCodeId;//分录号ID
//	private Integer badNum;//外观不良数
//	private String checkResult;//检验结果
	public String getProcessCard() {
		return ProcessCard;
	}
	public void setProcessCard(String processCard) {
		ProcessCard = processCard;
	}
	public String getWorkProcedure() {
		return WorkProcedure;
	}
	public void setWorkProcedure(String workProcedure) {
		WorkProcedure = workProcedure;
	}
	
}
