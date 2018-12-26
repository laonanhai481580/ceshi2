package com.ambition.gp.gpmaterial.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.GpMaterial;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class GpMaterialDao extends HibernateDao<GpMaterial,Long>{
	public List<GpMaterial> searchByApproveId(Long approvalId){
		String hql = "from GpMaterial o where o.companyId=? and o.approvalId=?  ";
		return find(hql, ContextUtils.getCompanyId(),approvalId.toString());
	}
}
