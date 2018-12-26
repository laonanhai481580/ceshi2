package com.ambition.gsm.intransitedequipment.dao;
 
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmEquipment;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 在途量检具管理(DAO)
 * @author 张顺治
 *
 */
@Repository
public class IntransitedEquipmentDao extends HibernateDao<GsmEquipment, Long> {
	
	/**
	 * 根据对象获取对象
	 * @param measurementType
	 * @return
	 */
	public List<GsmEquipment> getIntransitedEquipments(GsmEquipment intransitedEquipment){
		StringBuffer sbHql = new StringBuffer("from GsmEquipment i where i.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
//		if(StringUtils.isNotEmpty(intransitedEquipment.getMeasurementSerialNo())){
//			sbHql.append(" and i.measurementSerialNo like ? ");
//			searchParams.add(intransitedEquipment.getMeasurementSerialNo()+"%");
//		}
//		if(StringUtils.isNotEmpty(intransitedEquipment.getMeasurementType())){
//			sbHql.append(" and i.measurementType like ? ");
//			searchParams.add(intransitedEquipment.getMeasurementType()+"%");
//		}
		sbHql.append("order by i.id desc");
		return this.find(sbHql.toString(),searchParams.toArray());
	}
	
	/**
	 * 根据计量编号规则一级和二级检索最大的序列号
	 * @param measurementType
	 * @return
	 */
	public Integer getMaxSerialNoByGsmCodeRules(String gsmCodeRules){
		String hql = "select max(i.serialNo) from IntransitedEquipment i where i.companyId = ? and i.gsmCodeRules = ?";
		List<Integer> list = this.find(hql,ContextUtils.getCompanyId(),gsmCodeRules);
		return list != null && list.size() > 0 && list.get(0) != null?list.get(0):0;
	}
}
