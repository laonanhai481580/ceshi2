/**   
 * @Title: InspectionPlanThreadManager.java 
 * @Package com.ambition.gsm.inspectionplan.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2014-5-29 下午3:30:02 
 * @version V1.0   
 */ 
package com.ambition.gsm.inspectionplan.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.norteksoft.product.util.ParameterUtils;
import com.norteksoft.product.util.ThreadParameters;


/**
 * 类名: InspectionPlanThreadManager 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  刘承斌
 * @version 1.00 2014-5-29 下午3:30:02  发布
 */
public class InspectionPlanThread implements Runnable {
	
	private InspectionPlanManager inspectionPlanManager;
	private String ids;
	private Long companyId;
	private GsmEquipmentManager gsmEquipmentManager;
	private final static SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm:ss:SSS");
	
	public GsmEquipmentManager getGsmEquipmentManager() {
		return gsmEquipmentManager;
	}

	public void setGsmEquipmentManager(GsmEquipmentManager gsmEquipmentManager) {
		this.gsmEquipmentManager = gsmEquipmentManager;
	}

	public String getIds() {
		return ids;
	}
	
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public InspectionPlanManager getInspectionPlanManager() {
		return inspectionPlanManager;
	}

	public void setInspectionPlanManager(InspectionPlanManager inspectionPlanManager) {
		this.inspectionPlanManager = inspectionPlanManager;
	}

	public InspectionPlanThread(String ids,Long companyId,InspectionPlanManager inspectionPlanManager){
		this.ids = ids;
		this.companyId = companyId;
		this.inspectionPlanManager = inspectionPlanManager;
	}
	public InspectionPlanThread(String resultIds, Long companyId2,GsmEquipmentManager gsmEquipmentManager) {
		this.ids = resultIds;
		this.companyId = companyId2;
		this.gsmEquipmentManager = gsmEquipmentManager;
	}

	/**
	 * 方法名: run 
	 * <p>功能说明：(这里用一句话描述这个方法的作用)</p>
	 * @throws
	 * @see java.lang.Runnable#run() 
	 */
	@Override
	public void run() {
		System.out.println("运行时间："+ timeFormater.format(new Date())); 
		System.out.println("当前线程名称："+ Thread.currentThread().getName());
		
		ThreadParameters para =  new ThreadParameters();
		para.setCompanyId(companyId);
		para.setUserName("system");
		ParameterUtils.setParameters(para);
//		inspectionPlanManager.checkInspectionPlan(ids);
		
		System.out.println("完成时间："+ timeFormater.format(new Date()));
	}

}
