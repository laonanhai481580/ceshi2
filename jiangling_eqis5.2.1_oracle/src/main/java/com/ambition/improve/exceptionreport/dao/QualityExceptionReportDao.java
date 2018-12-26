package com.ambition.improve.exceptionreport.dao;

import org.springframework.stereotype.Repository;

import com.ambition.improve.entity.QualityExceptionReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:品质异常联络单Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年4月22日 发布
 */
@Repository
public class QualityExceptionReportDao extends HibernateDao<QualityExceptionReport, Long>{

	public Page<QualityExceptionReport> searchPageProduct(
			Page<QualityExceptionReport> page) {
		return searchPageByHql(page, "from QualityExceptionReport d where d.exceptionBelong='生产' ");
	}
	public Page<QualityExceptionReport> searchPageQuality(
			Page<QualityExceptionReport> page) {
		return searchPageByHql(page, "from QualityExceptionReport d where d.exceptionBelong='品质' ");
	}

}
