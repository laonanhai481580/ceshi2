package com.ambition.cost.partsloss.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.ambition.cost.entity.PartsLoss;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;


@Repository
public class PartsLossDao extends HibernateDao<PartsLoss, Long> {
		
	public Page<PartsLoss> list(Page<PartsLoss> page){
		return findPage(page, "from PartsLoss t");
	}
	
	public List<PartsLoss> getAllPartsLoss(){
		return find("from PartsLoss t");
	}
	public List<PartsLoss> getPartsLoss(){
		String hql="from PartsLoss t where t.lossType !=? and t.partImageNo is null";
		List<PartsLoss> li =this.find(hql,new Object[]{"龙头整组"});
		return li;
	}
	
	public List<PartsLoss> getAllPartsLossByMainCode(String mainCode){
        return find("select i from PartsLoss i where i.mainCode=? ",mainCode);
    }
    public Page<PartsLoss> search(Page<PartsLoss> page) {
        return searchPageByHql(page, "from PartsLoss t ");
    }
}
