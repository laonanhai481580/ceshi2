package com.ambition.spc.entity;
 
import java.util.Date; 
import org.springframework.stereotype.Component;


/**
 *	KLIPPEL整机性能测试采集,和spc结合，关注质量特性，没有在数据库中生成表
 */

@Component
public class KlippelCollections{  
	private Long id;   //编号，自动增长
	private String productNo;//产品系列
	private String machineNo;    //设备编号 
	private Date detectDate;//采集时间 
	private String paramName;//参数名称，质量特性编号
	private String paramValue;//  参数值
	private String processId; //型号编号
	private String featureId; //特性编号
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	public Date getDetectDate() {
		return detectDate;
	}
	public void setDetectDate(Date detectDate) {
		this.detectDate = detectDate;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setValues(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	

}
