package com.ambition.epm.entrustHsf.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entrustHsf.dao.EntrustHsfSublistDao;
import com.ambition.epm.exception.dao.ExceptionDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class EntrustHsfSublistManager {
	@Autowired
	private EntrustHsfSublistDao entrustHsfSublistDao;
	@Autowired
	private ExceptionDao exceptionDao;
	
	public EntrustHsfSublist getEntrustHsfSublist(Long id){
		return entrustHsfSublistDao.get(id);
	}
	public List<EntrustHsfSublist>  getByHsfId(Long id){
		return entrustHsfSublistDao.getByHsfId(id);
	}
	public void saveEntrustHsfSublist(EntrustHsfSublist entrustHsfSublist){
		entrustHsfSublistDao.save(entrustHsfSublist);
	}
	public Page<EntrustHsfSublist> list(Page<EntrustHsfSublist> page){
		return entrustHsfSublistDao.list(page);
	}
	
	public List<EntrustHsfSublist> listAll(){
		return entrustHsfSublistDao.getEntrustHsfSublist();
	}
	
	public void deleteEntrustHsfSublist(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
 			EntrustHsfSublist entrustHsfSublist=entrustHsfSublistDao.get(Long.valueOf(id));
			if(entrustHsfSublist.getId() != null){
				entrustHsfSublistDao.delete(entrustHsfSublist);
			}
		}
	}
	
	public Page<EntrustHsfSublist> search(Page<EntrustHsfSublist> page){
		return entrustHsfSublistDao.search(page);
	}
	public void alterEntrustHsfSublist(Long id){
		EntrustHsfSublist entrustHsfSublist =entrustHsfSublistDao.get(Long.valueOf(id));
		entrustHsfSublist.setTestAfter("OK");
		entrustHsfSublistDao.save(entrustHsfSublist);
	} 
	public Page<EntrustHsfSublist> listState(Page<EntrustHsfSublist> page,String state ,String str){
		String hql = " from EntrustHsfSublist e where e.hiddenState=? and e.companyId=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		searchParams.add(ContextUtils.getCompanyId());
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		return entrustHsfSublistDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public List<EntrustHsfSublist>listOutOrIn(String subName){
		return entrustHsfSublistDao.listOutOrIn(subName);
	}
}
