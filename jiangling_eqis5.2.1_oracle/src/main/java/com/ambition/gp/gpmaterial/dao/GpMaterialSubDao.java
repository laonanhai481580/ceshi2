package com.ambition.gp.gpmaterial.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.GpMaterialSub;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class GpMaterialSubDao extends HibernateDao<GpMaterialSub,Long>{
	public Page<GpMaterialSub> list(Page<GpMaterialSub> page){
		return findPage(page,"from GpMaterialSub m");
	}
	public List<GpMaterialSub> getGpMaterialSub(){
		return find("from GpMaterialSub m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<GpMaterialSub> search(Page<GpMaterialSub> page){
		return searchPageByHql(page,"from GpMaterialSub m");
	}
	public List<GpMaterialSub> selectGpMaterialSub(String id){
		return find("from GpMaterialSub m where m.companyId=? and m.averageId=?", ContextUtils.getCompanyId(),id);
	}
}
