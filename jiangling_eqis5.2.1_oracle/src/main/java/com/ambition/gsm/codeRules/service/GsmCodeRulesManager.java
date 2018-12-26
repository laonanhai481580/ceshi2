package com.ambition.gsm.codeRules.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.codeRules.dao.GsmCodeRulesDao;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;

/**
 * 计量编号规则一级类别(SERVICE)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class GsmCodeRulesManager {
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmCodeRulesDao gsmCodeRulesDao;
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public GsmCodeRules getGsmCodeRules(Long id) {
		return gsmCodeRulesDao.get(id);
	}

	/**
	 * 保存对象
	 * @param gsmCodeRules
	 */
	public void saveGsmCodeRules(GsmCodeRules gsmCodeRules) {
		if(!this.canSaveGsmCodeRules(gsmCodeRules)){
			throw new AmbFrameException("该类别已经存在");
		}
		gsmCodeRulesDao.save(gsmCodeRules);
	}
	
	/**
	 * 删除对象
	 */
	public void deleteGsmCodeRules(GsmCodeRules gsmCodeRules) {
		gsmCodeRulesDao.delete(gsmCodeRules);
	}
	
	/**
	 * 删除多对象
	 * @param deleteIds
	 */
	public String deleteGsmCodeRules(String deleteIds) {
		String[] ids = deleteIds.split(",");
		int length = ids.length;
		for(String id: ids){
			GsmCodeRules gsmCodeRules = this.getGsmCodeRules(Long.valueOf(id));
			if(gsmCodeRules.getGsmCodeSecRuless() == null || gsmCodeRules.getGsmCodeSecRuless().size() == 0){
				this.deleteGsmCodeRules(gsmCodeRules);
				logUtilDao.debugLog("删除", gsmCodeRulesDao.get(Long.valueOf(id)).toString());
				length--;
			}
		}
		return "共"+ids.length+"条 其中"+(ids.length-length)+"条删除成功"+length+"条删除失败!";
	}

	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmCodeRules> getPage(Page<GsmCodeRules> page) { 
		return gsmCodeRulesDao.getPage(page);
	}
	
	/**
	 * 获取所有对象
	 * @return
	 */
	public List<GsmCodeRules> getGsmCodeRules() {
		return gsmCodeRulesDao.getAll();
	}

	/**
	 * 根据对象获取对象
	 * @param type
	 * @return
	 */
	public GsmCodeRules getGsmCodeRules(GsmCodeRules gsmCodeRules) {
		List<GsmCodeRules> list = gsmCodeRulesDao.getGsmCodeRules(gsmCodeRules);
		return list != null && list.size() > 0?list.get(0):null;
	}
	
	/**
	 * 是否可以保存
	 * @return
	 */
	public Boolean canSaveGsmCodeRules(GsmCodeRules gsmCodeRules){
		List<GsmCodeRules> list = gsmCodeRulesDao.getGsmCodeRules(gsmCodeRules);
		return list != null && list.size() > 0?false:true;
	}
}
