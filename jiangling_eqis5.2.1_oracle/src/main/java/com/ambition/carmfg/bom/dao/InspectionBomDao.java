package com.ambition.carmfg.bom.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.ProductBomInspection;
import com.ambition.si.entity.SiDefectionCode;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class InspectionBomDao extends HibernateDao<ProductBomInspection, Long> {
	public Page<ProductBomInspection> list(Page<ProductBomInspection> page){
		return findPage(page, "from ProductBomInspection d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<ProductBomInspection> getAllInspectionBom(){
		return find("from ProductBomInspection d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<ProductBomInspection> search(Page<ProductBomInspection> page) {
        return searchPageByHql(page, "from ProductBomInspection d where d.companyId=?",ContextUtils.getCompanyId());
    }
    
	public  ProductBomInspection getProductBomInspection(String code) {
		String hql = "from ProductBomInspection d where d.materielCode = ?";
		List<ProductBomInspection> inspectionBoms = this.find(hql,code);
		if(inspectionBoms.size()>0){
			return inspectionBoms.get(0);
		}else{
			return null;
		}
	}   
}