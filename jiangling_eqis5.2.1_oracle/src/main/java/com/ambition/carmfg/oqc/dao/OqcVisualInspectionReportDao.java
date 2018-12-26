package com.ambition.carmfg.oqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcInspectionReport;
import com.ambition.carmfg.entity.OqcInspectionReport;
import com.ambition.carmfg.entity.OqcVisualInspectionReport;
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
public class OqcVisualInspectionReportDao extends HibernateDao<OqcVisualInspectionReport,Long>{
	public Page<OqcVisualInspectionReport> list(Page<OqcVisualInspectionReport> page){
		return findPage(page, "from OqcVisualInspectionReport d");
	}
	
	public List<OqcVisualInspectionReport> getAllOqcVisualInspectionReport(){
		return find("from OqcVisualInspectionReport d");
	}

    public Page<OqcVisualInspectionReport> search(Page<OqcVisualInspectionReport> page) {
        return searchPageByHql(page, "from OqcVisualInspectionReport d ");
    }
}
