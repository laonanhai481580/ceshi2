package com.ambition.gsm.useRecord.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmUseRecord;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 计量器具用户使用记录(DAO)
 * @author 张顺治
 *
 */
@Repository
public class GsmUseRecordDao extends HibernateDao<GsmUseRecord, Long> {
	
	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmUseRecord> getPage(Page<GsmUseRecord> page){
		return searchPageByHql(page, "from GsmUseRecord g where g.companyId = ?",ContextUtils.getCompanyId());
	}
	
	/**
	 * 一个量具多次被借
	 * @param no
	 * @return
	 */
	public GsmUseRecord getGsmUseRecordByNo(String no) {
		List<GsmUseRecord > list = this.find("from GsmUseRecord g where g.companyId = ? and g.measurementSerialNo = ? and g.realReturnDate is null order by g.createdTime desc", ContextUtils.getCompanyId(),no);
		return list != null && list.size()>0?list.get(0):null;
	}
}
