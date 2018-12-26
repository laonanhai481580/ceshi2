package com.ambition.epm.sample.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entity.SampleSublist;
import com.ambition.epm.sample.dao.SampleSublistDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class SampleSublistManager {
	@Autowired
	private SampleSublistDao sampleSublistDao;
	
	public SampleSublist getSampleSublist(Long id){
		return sampleSublistDao.get(id);
	}
	public List<SampleSublist>  getByHsfId(Long id){
		return sampleSublistDao.getByHsfId(id);
	}
	public void saveSampleSublist(SampleSublist sampleSublist){
		sampleSublistDao.save(sampleSublist);
	}
	public Page<SampleSublist> list(Page<SampleSublist> page){
		return sampleSublistDao.list(page);
	}
	public List<SampleSublist> listAll(){
		return sampleSublistDao.getSampleSublist();
	}
	public Page<SampleSublist> search(Page<SampleSublist> page){
		return sampleSublistDao.search(page);
	}
	public void deleteSampleSublist(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
 			SampleSublist sampleSublist=sampleSublistDao.get(Long.valueOf(id));
			if(sampleSublist.getId() != null){
				sampleSublistDao.delete(sampleSublist);
			}
		}
	}
}
