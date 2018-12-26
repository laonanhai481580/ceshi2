package com.ambition.carmfg.oqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcInspectionReport;
import com.ambition.carmfg.entity.OqcInspectionReport;
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
public class OqcInspectionReportDao extends HibernateDao<OqcInspectionReport,Long>{
	public Page<OqcInspectionReport> list(Page<OqcInspectionReport> page){
		return findPage(page, "from OqcInspectionReport d");
	}
	
	public List<OqcInspectionReport> getAllOqcInspectionReport(){
		return find("from OqcInspectionReport d");
	}

    public Page<OqcInspectionReport> search(Page<OqcInspectionReport> page) {
        return searchPageByHql(page, "from OqcInspectionReport d ");
    }
}
