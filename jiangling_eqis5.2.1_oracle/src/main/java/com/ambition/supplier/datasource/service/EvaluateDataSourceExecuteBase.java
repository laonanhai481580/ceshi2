package com.ambition.supplier.datasource.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.api.entity.Department;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名: 业绩规则评价执行父类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：业绩规则评价计算的父类</p>
 * @author  赵骏
 * @version 1.00 2013-4-19 发布
 */
public abstract class EvaluateDataSourceExecuteBase {
	/**
	  * 方法名:获取公司编号 
	  * <p>功能说明：获取公司编号</p>
	  * @return
	 */
	public Long getCompanyId(){
		return ContextUtils.getCompanyId();
	}
	public String getCompanyName(){
		return ContextUtils.getCompanyName();
	}
	public String getBusinessUnitCode(){
		return CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName());
	}
	/**
	  * 方法名:获取系统ID 
	  * <p>功能说明：获取系统ID</p>
	  * @return
	 */
	public Long getSystemId(){
		return ContextUtils.getSystemId();
	}
	/**
	  * 方法名:获取部门 
	  * <p>功能说明：获取当前用户所在的部门ID,多个部门之间用逗号隔开</p>
	  * @return
	 */
	public String getDepartMentIds(){
		List<Department> departments = ApiFactory.getAcsService().getDepartmentsByUserId(ContextUtils.getUserId());
		StringBuffer sb = new StringBuffer();
		for(Department d : departments){
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append(d.getId());
		}
		return sb.toString();
	}
	/**
	  * 方法名: 执行sql语句
	  * <p>功能说明：</p>
	  * @param sql
	  * @param values
	  * @return
	 */
	public List<Object> executeSql(String sql,Object... values){
		String beanName = EvaluateDataSourceManager.class.getSimpleName();
		beanName = beanName.substring(0,1).toLowerCase() + beanName.substring(1);
		EvaluateDataSourceManager gradeRuleManager = (EvaluateDataSourceManager)ContextUtils.getBean(beanName);
		List<Object> list = gradeRuleManager.executeSql(sql, values);
		return list;
	}
	
	/**
	  * 方法名:业绩计算 
	  * <p>功能说明：业绩计算</p>
	  * @param supplierId 供应商编号
	  * @param startDate  开始时间
	  * @param endDate	结束时间
	  * @return
	  * @throws Exception
	 */
	public abstract Map<String,Object> execute(String supplierCode,Date startDate,Date endDate,String materialType) throws Exception;
}
