package com.ambition.gsm.common.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmEquipment;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 计量器具编码(DAO)
 * @author 张顺治
 *
 */
@Repository
public class GsmBomDao extends HibernateDao<GsmEquipment, Long> {
	
	public Page<GsmEquipment> getPage(Page<GsmEquipment> page){
		return this.searchPageByHql(page, "from GsmEquipment g where g.companyId = ?", ContextUtils.getCompanyId());
	}
	
	public Page<GsmEquipment> getPageByParams(Page<GsmEquipment> page,JSONObject params){
		StringBuffer sbHql = new StringBuffer("from GsmEquipment g where g.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				sbHql.append(" and g." + pro + " like ? ");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		sbHql.append(" order by g.measurementNo");
		return this.findPage(page,sbHql.toString(),searchParams.toArray());
	}
	
	public Page<GsmEquipment> getPageByGsmCodeRules(Page<GsmEquipment> page,GsmCodeRules gsmCodeRules){
		StringBuilder sbHql = new StringBuilder("from GsmEquipment g where g.companyId = ? and (g.measurementState = ? or g.measurementState = ?)");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(GsmEquipment.STATE_DEFAULT_INSTOCK);
		searchParams.add(GsmEquipment.STATE_INUSE);
		if(gsmCodeRules != null){
			sbHql.append(" and g.measurementType = ? ");
			searchParams.add(gsmCodeRules.getId().toString());
		}
		return this.searchPageByHql(page, sbHql.toString(), searchParams.toArray());
	}
}
