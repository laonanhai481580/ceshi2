package com.ambition.cost.composingdetail.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.cost.entity.Composing;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class ComposingDao extends HibernateDao<Composing, Long> {
	/**
	 * 获取质量成本一级科目
	 * @return
	 */
	public List<Composing> getTopComposing(){
		return find("from Composing c where c.companyId=? and c.parent is null order by orderNum",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据级别查询质量成本
	 * @param level
	 * @return
	 */
	public List<Composing> getComposingByLevel(Integer level){
		return find("from Composing c where c.companyId=? and c.dengji = ?",new Object[]{ContextUtils.getCompanyId(),level});
	}
}
