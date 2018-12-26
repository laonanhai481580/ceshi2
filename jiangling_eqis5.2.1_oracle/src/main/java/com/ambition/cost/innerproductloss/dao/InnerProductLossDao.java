package com.ambition.cost.innerproductloss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.cost.entity.InnerProductLoss;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
@Repository
public class InnerProductLossDao  extends HibernateDao<InnerProductLoss, Long>{
	public Page<InnerProductLoss> list(Page<InnerProductLoss> page){
		return findPage(page, "from InnerProductLoss t");
	}
	
	public List<InnerProductLoss> getAllInnerProductLoss(){
		return find("from InnerProductLoss t");
	}

    public Page<InnerProductLoss> search(Page<InnerProductLoss> page) {
        return searchPageByHql(page, "from InnerProductLoss t ");
    }
    public List<InnerProductLoss> getAllInnerProductLossByCode(String code){
        return find("select i from InnerProductLoss i where i.code=? ",code);
    }

}
