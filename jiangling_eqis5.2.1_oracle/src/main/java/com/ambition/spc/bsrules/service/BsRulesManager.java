package com.ambition.spc.bsrules.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.bsrules.dao.BsRulesDao;
import com.ambition.spc.entity.BsRules;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;

/**
 * SPC判断规则service
 * @author wlongfeng
 *
 */


@Service
@Transactional
public class BsRulesManager {
	@Autowired
	private BsRulesDao bsRulesDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public BsRules getBsRules(Long id){
		return bsRulesDao.get(id);
	}
	
	public void saveBsRules(BsRules bsRules){
		bsRulesDao.save(bsRules);
	}
	
	public void deleteBsRules(BsRules bsRules){
		bsRulesDao.delete(bsRules);
	}
	
	public void deleteBsRulesDao(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			logUtilDao.debugLog("删除", bsRulesDao.get(Long.valueOf(id)).toString());
			bsRulesDao.delete(Long.valueOf(id));
		}
	}
	public Page<BsRules> list(Page<BsRules>page){
		return bsRulesDao.list(page);
	}
	
	public List<BsRules> listAll(){
		return bsRulesDao.getBsRules();
	}
	public Page<BsRules> search(Page<BsRules> page) {
		return bsRulesDao.search(page);
	}
}
