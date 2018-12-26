package com.ambition.carmfg.plantparameter.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.PlantItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 
 * 类名:设备信息Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月3日 发布
 */
@Repository
public class PlantItemDao extends HibernateDao<PlantItem, Long>{
	public Page<PlantItem> list(Page<PlantItem> page){
		return findPage(page, "from PlantItem p where p.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<PlantItem> getAllPlantItem(){
		return find("from PlantItem p where p.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<PlantItem> search(Page<PlantItem> page) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from PlantItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and i.plantName like ?");
			params.add("%" + customSearch.trim() + "%");
		}
		return searchPageByHql(page, hql.toString(), params.toArray());
    }
}
