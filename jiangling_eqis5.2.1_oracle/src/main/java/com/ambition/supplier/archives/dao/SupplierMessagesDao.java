package com.ambition.supplier.archives.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierMessages;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月26日 发布
 */
@Repository
public class SupplierMessagesDao extends  HibernateDao<SupplierMessages,Long>{

	public List<SupplierMessages> searchByFileName(String fileName, Long id) {
		if(null!=id&&!"".equals(id)){
        	return find("from SupplierMessages t where t.fileName=? and t.id != ? and t.companyId=? ",fileName,id,ContextUtils.getCompanyId());
    	}
    	return find("from SupplierMessages t where t.fileName=? and t.companyId=? ",fileName,ContextUtils.getCompanyId());
	}

}
