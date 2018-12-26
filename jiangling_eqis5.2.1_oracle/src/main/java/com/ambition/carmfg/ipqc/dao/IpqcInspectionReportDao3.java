package com.ambition.carmfg.ipqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.GpAverageMaterial;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class IpqcInspectionReportDao3 extends HibernateDao<GpAverageMaterial,Long>{
	public Page<GpAverageMaterial> list(Page<GpAverageMaterial> page){
		return findPage(page,"from IpqcInspectionReport m");
	}
	public List<GpAverageMaterial> getGpAverageMaterial(){
		return find("from IpqcInspectionReport m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<GpAverageMaterial> search(Page<GpAverageMaterial> page){
		return searchPageByHql(page,"from IpqcInspectionReport m");
	}
}
