package com.ambition.gsm.base.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmCheckItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 
 * 类名:检验项目Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年11月24日 发布
 */
@Repository
public class GsmCheckItemDao extends HibernateDao<GsmCheckItem, Long>{
	public Page<GsmCheckItem> list(Page<GsmCheckItem> page){
		return findPage(page, "from GsmCheckItem p ");
	}
	
	public List<GsmCheckItem> getAllGsmCheckItem(){
		return find("from GsmCheckItem p ");
	}

    public Page<GsmCheckItem> search(Page<GsmCheckItem> page) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from GsmCheckItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and i.plantName like ?");
			params.add("%" + customSearch.trim() + "%");
		}
		return searchPageByHql(page, hql.toString(), params.toArray());
    }
}
