package com.ambition.carmfg.ipqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcInspectionReport;
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
public class IpqcInspectionReportDao extends HibernateDao<IpqcInspectionReport,Long>{
	public Page<IpqcInspectionReport> list(Page<IpqcInspectionReport> page){
		return findPage(page, "from IpqcInspectionReport d");
	}
	
	public List<IpqcInspectionReport> getAllIpqcInspectionReport(){
		return find("from IpqcInspectionReport d");
	}

    public Page<IpqcInspectionReport> search(Page<IpqcInspectionReport> page) {
        return searchPageByHql(page, "from IpqcInspectionReport d ");
    }
}
