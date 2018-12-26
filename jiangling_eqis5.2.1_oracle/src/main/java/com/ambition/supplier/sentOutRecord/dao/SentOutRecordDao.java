package com.ambition.supplier.sentOutRecord.dao;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.SentOutRecord;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class SentOutRecordDao extends HibernateDao<SentOutRecord,Long>{
	public Page<SentOutRecord> search(Page<SentOutRecord> page){
		return searchPageByHql(page,"from SentOutRecord s");
	}
}
