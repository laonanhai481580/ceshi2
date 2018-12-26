package com.ambition.gsm.inspectionmsaplan.dao;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.InspectionMsaplan;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * MSA校验计划(DAO)
 * @author 张顺治
 *
 */
@Repository
public class InspectionMsaplanDao extends HibernateDao<InspectionMsaplan, Long> { 
	
	/**
	 * 分页对象
	 * @param page
	 * @param msaState
	 * @param operator
	 * @return
	 */
	public Page<InspectionMsaplan> getPage(Page<InspectionMsaplan> page,String msaState,String operator){
		StringBuilder sbHql = new StringBuilder("from InspectionMsaplan i where i.companyId= ?");
		List<Object> searchParames = new ArrayList<Object>();
		searchParames.add(ContextUtils.getCompanyId());
		if(StringUtils.isNotEmpty(msaState) && StringUtils.isNotEmpty(operator)){
			if("IN".equals(operator.toUpperCase())){
				sbHql.append(" and i.msaState in('"+(msaState.contains(",")?msaState.replaceAll(",", "','"):msaState)+"') ");
			}else if("NOTIN".equals(operator.toUpperCase())){
				sbHql.append(" and i.msaState not in('"+(msaState.contains(",")?msaState.replaceAll(",", "','"):msaState)+"') ");
			}
		}
		return searchPageByHql(page, sbHql.toString(), searchParames.toArray());
	}
	/**
	 * 分页对象
	 * @param page
	 * @param msaState
	 * @param operator
	 * @return
	 */
	public Page<InspectionMsaplan> getPage(Page<InspectionMsaplan> page){
		StringBuilder sbHql = new StringBuilder("from InspectionMsaplan i where i.companyId= ?");
		return searchPageByHql(page, sbHql.toString(), ContextUtils.getCompanyId());
	}
}

