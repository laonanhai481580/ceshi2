package com.ambition.qsm.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.qsm.entity.SystemMaintenance;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:体系维护DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Repository
public class SystemMaintenanceDao extends HibernateDao<SystemMaintenance,Long>{
	public Page<SystemMaintenance> list(Page<SystemMaintenance> page){
		return findPage(page, "from SystemMaintenance d");
	}
	
	public List<SystemMaintenance> getAllSystemMaintenance(){
		return find("from SystemMaintenance d");
	}

    public Page<SystemMaintenance> search(Page<SystemMaintenance> page) {
        return searchPageByHql(page, "from SystemMaintenance d ");
    }
}
