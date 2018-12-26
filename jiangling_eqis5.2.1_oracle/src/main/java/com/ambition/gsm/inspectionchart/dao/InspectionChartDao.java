package com.ambition.gsm.inspectionchart.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.InspectionPlan;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 量检具统计分析(DAO)
 * @author 刘承斌
 *
 */
@Repository
public class InspectionChartDao extends HibernateDao<InspectionPlan, Long> {

	/**
	 * 所有对象
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<InspectionPlan> getPlanInspectionPlan(Date startDate,Date endDate) {
		return find("from InspectionPlan i where i.companyId=? and i.inspectionPlanDate>=? and i.inspectionPlanDate<? ",	ContextUtils.getCompanyId(), startDate, endDate);
	}

	/**
	 * 所有对象
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<InspectionPlan> getActualInspectionPlan(Date startDate,	Date endDate) {
		return find("from InspectionPlan i where i.companyId=? and i.actualInspectionDate>=? and i.actualInspectionDate<?  ",	ContextUtils.getCompanyId(), startDate, endDate);
	}

	/**
	 * 分页对象
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Page<InspectionPlan> getActualInspectionPlan(Page<InspectionPlan> page, Date startDate, Date endDate) {
		String hql="from InspectionPlan i where i.companyId=? and i.actualInspectionDate >= ? and i.actualInspectionDate < ? ";// and yesOrNo is ? ";
		return this.findPage(page,hql,ContextUtils.getCompanyId(), startDate, endDate);
	} 
	
	/**
	 * 分页对象
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Page<InspectionPlan> getActualInspectionRecord(Page<InspectionPlan> page, Date startDate, Date endDate) {
		return findPage(page,"from InspectionPlan i where companyId=? and actualInspectionDate>=? and actualInspectionDate<? and yesOrNo is ? ",ContextUtils.getCompanyId(), startDate, endDate, true);
	}

	/**
	 * 分页对象
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Page<InspectionPlan> getPlanInspectionRecord(Page<InspectionPlan> page, Date startDate,Date endDate) { 
		return findPage(page,"from InspectionPlan i where companyId=? and inspectionPlanDate>=? and inspectionPlanDate<? ",ContextUtils.getCompanyId(), startDate, endDate);
	}
	
	/**
	 * 所有对象
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<InspectionPlan> getQualifiedActualInspectionRecord(Date startDate, Date endDate) {
		return find("from InspectionPlan i where i.companyId=? and i.actualInspectionDate>=? and i.actualInspectionDate<? and i.checkState=? ",ContextUtils.getCompanyId(), startDate, endDate, "合格");
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Page<InspectionPlan> getQualifiedActualInspectionRecord(Page<InspectionPlan> page,Date startDate, Date endDate) {
		return findPage(page,"from InspectionPlan i where i.companyId=? and i.actualInspectionDate>=? and i.actualInspectionDate<?  and i.checkState=? ",ContextUtils.getCompanyId(), startDate, endDate, "合格");
	}
}