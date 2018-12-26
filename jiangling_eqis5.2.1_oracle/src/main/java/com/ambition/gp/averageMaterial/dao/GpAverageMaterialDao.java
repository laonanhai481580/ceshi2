package com.ambition.gp.averageMaterial.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.GpAverageMaterial;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class GpAverageMaterialDao extends HibernateDao<GpAverageMaterial,Long>{
	public Page<GpAverageMaterial> list(Page<GpAverageMaterial> page){
		return findPage(page,"from GpAverageMaterial m");
	}
	public List<GpAverageMaterial> getGpAverageMaterial(){
		return find("from GpAverageMaterial m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<GpAverageMaterial> search(Page<GpAverageMaterial> page){
		return searchPageByHql(page,"from GpAverageMaterial m");
	}
}
