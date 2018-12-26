package com.ambition.gsm.inspectionmsachart.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.InspectionMsaplan;
import com.ambition.gsm.entity.InspectionPlan;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名: InspectionChartDao
 * <p>
 * amb
 * </p>
 * <p>
 * 厦门安必兴信息科技有限公司
 * </p>
 * <p>
 * 功能说明：设备设施dao
 * </p>
 * 
 * @author 刘承斌
 * @version 1.00 2014-5-21 下午2:45:29 发布
 */
@Repository
public class InspectionMsaChartDao extends HibernateDao<InspectionMsaplan, Long> {

	public List<InspectionMsaplan> getPlanInspectionPlan(Date startDate,Date endDate) {
		return find("from InspectionMsaplan i where i.companyId=? and i.msaPlanDate>=? and i.msaPlanDate<? ",	ContextUtils.getCompanyId(), startDate, endDate);
	}

	public List<InspectionMsaplan> getActualInspectionPlan(Date startDate,	Date endDate) {
		return find("from InspectionMsaplan i where i.companyId=? and i.actualInspectionMsaDate>=? and i.actualInspectionMsaDate<?  ",	ContextUtils.getCompanyId(), startDate, endDate);
	}

	public Page<InspectionMsaplan> getActualInspectionPlan(Page<InspectionMsaplan> page, Date startDate, Date endDate) {
		String hql="from InspectionMsaplan i where i.companyId=? and i.actualInspectionMsaDate >= ? and i.actualInspectionMsaDate < ? ";// and yesOrNo is ? ";
		return this.findPage(page,hql,ContextUtils.getCompanyId(), startDate, endDate);
	} 
	public Page<InspectionMsaplan> getActualInspectionRecord(Page<InspectionMsaplan> page, Date startDate, Date endDate) {
		return findPage(page,"from InspectionMsaplan i where companyId=? and actualInspectionDate>=? and actualInspectionDate<? and yesOrNo is ? ",ContextUtils.getCompanyId(), startDate, endDate, true);
	}

	public Page<InspectionMsaplan> getPlanInspectionRecord(Page<InspectionMsaplan> page, Date startDate,Date endDate) { 
		return findPage(page,"from InspectionMsaplan i where companyId=? and actualInspectionMsaDate>=? and actualInspectionMsaDate<? and checkState=?",ContextUtils.getCompanyId(), startDate, endDate,"合格");

	}
	
	public List<InspectionMsaplan> getQualifiedActualInspectionRecord(Date startDate, Date endDate) {
		return find("from InspectionMsaplan i where i.companyId=? and i.actualInspectionMsaDate>=? and i.actualInspectionMsaDate<? and i.checkState=? ",ContextUtils.getCompanyId(), startDate, endDate, "合格");
	}
	public Page<InspectionMsaplan> getQualifiedActualInspectionRecord(Page<InspectionPlan> page,Date startDate, Date endDate) {
		return findPage(page,"from InspectionMsaplan i where i.companyId=? and i.actualInspectionDate>=? and i.actualInspectionDate<?  and i.checkState=? ",ContextUtils.getCompanyId(), startDate, endDate, "合格");
	}

	private Page<InspectionMsaplan> findPage(Page<InspectionPlan> page,String string, Long companyId, Date startDate, Date endDate,String string2) { 
		return null;
	}
}