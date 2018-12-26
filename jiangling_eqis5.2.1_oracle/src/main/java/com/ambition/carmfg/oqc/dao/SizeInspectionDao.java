package com.ambition.carmfg.oqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.SizeInspection;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:尺寸检验数据DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年7月7日 发布
 */
@Repository
public class SizeInspectionDao extends HibernateDao<SizeInspection,Long>{
	public Page<SizeInspection> list(Page<SizeInspection> page){
		return findPage(page, "from SizeInspection d");
	}
	
	public List<SizeInspection> getAllSizeInspection(){
		return find("from SizeInspection d");
	}

    public Page<SizeInspection> search(Page<SizeInspection> page,String businessUnit) {
        return searchPageByHql(page, "from SizeInspection d where d.companyId=? and d.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
    }
}
