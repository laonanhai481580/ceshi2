package com.ambition.gsm.inspectionrecord.dao;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmInspectionRecord;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名: GsmInspectionRecordDao 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：检定计划dao</p>
 * @author  刘承斌
 * @version 1.00 2014-5-21 下午2:46:11  发布
 */

@Repository
public class GsmInspectionRecordDao extends HibernateDao<GsmInspectionRecord, Long> {

	public List<GsmInspectionRecord> list() {
		return find("from GsmInspectionRecord gsmInspectionRecord where companyId=? and isPlan is ?",ContextUtils.getCompanyId(),false);
	}
	
	public List<GsmInspectionRecord> getGsmInspectionRecordByTimeAndMeano(String measurementNo,String inspectionYear,Date nextTime){
		return find("from GsmInspectionRecord g where g.inspectionPlan.measurementNo=? and g.inspectionPlan.inspectionYear=? and g.planDate=?", measurementNo,inspectionYear,nextTime);
	}
		
}
