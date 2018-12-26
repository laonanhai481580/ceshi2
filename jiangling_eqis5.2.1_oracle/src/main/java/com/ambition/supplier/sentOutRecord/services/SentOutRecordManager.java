package com.ambition.supplier.sentOutRecord.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.SentOutRecord;
import com.ambition.supplier.sentOutRecord.dao.SentOutRecordDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class SentOutRecordManager {
	@Autowired
	private SentOutRecordDao sentOutRecordDao;
	public SentOutRecord getSentOutRecord(Long id){
		return sentOutRecordDao.get(id);
	}
	public void saveSentOutRecord(SentOutRecord sentOutRecord){
		sentOutRecordDao.save(sentOutRecord);
	}
	public void delete(String deleteIds){
		String[] Ids = deleteIds.split(",");
		for(String id : Ids){
			SentOutRecord sentOutRecord = sentOutRecordDao.get(Long.valueOf(id));
			if(sentOutRecord.getId() != null){
				sentOutRecordDao.delete(sentOutRecord);
			}
		}
	}
	public Page<SentOutRecord> search(Page<SentOutRecord> page){
		return sentOutRecordDao.search(page);
	}
}
