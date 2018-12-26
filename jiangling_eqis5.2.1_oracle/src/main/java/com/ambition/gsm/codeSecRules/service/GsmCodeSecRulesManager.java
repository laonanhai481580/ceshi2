package com.ambition.gsm.codeSecRules.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.codeSecRules.dao.GsmCodeSecRulesDao;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmCodeSecRules;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;

/**
 * 计量编号规则二级分类(SERVICE)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class GsmCodeSecRulesManager {
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmCodeSecRulesDao gsmCodeSecRulesDao;
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public GsmCodeSecRules getGsmCodeSecRules(Long id) { 
		return gsmCodeSecRulesDao.get(id);
	}  

	/**
	 * 保存对象
	 * @param gsmCodeSecRules
	 */
	public void saveGsmCodeSecRules(GsmCodeSecRules gsmCodeSecRules) {
		if(!this.canGsmCodeSecRules(gsmCodeSecRules)){
			throw new AmbFrameException("该类别已经存在!");
		}
		gsmCodeSecRulesDao.save(gsmCodeSecRules);
	} 

	/**
	 * 删除多对象
	 * @param ids
	 */
	public void deleteGsmSecCodeRules(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			logUtilDao.debugLog("删除", gsmCodeSecRulesDao.get(Long.valueOf(id)).toString());
			gsmCodeSecRulesDao.delete(Long.valueOf(id));
		}
	}
 
	/**
	 * 删除对象
	 * @param gsmCodeSecRules
	 */
	public void deleteGsmCodeSecRules(GsmCodeSecRules gsmCodeSecRules){
		logUtilDao.debugLog("删除", gsmCodeSecRules.toString());
		gsmCodeSecRulesDao.delete(gsmCodeSecRules);
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @param gsmCodeRules
	 * @return
	 */
	public Page<GsmCodeSecRules> getPage(Page<GsmCodeSecRules> page) {
		return gsmCodeSecRulesDao.getPage(page);
	}
	
	/**
	 * 根据一级类别获取分页对象
	 * @param page
	 * @param gsmCodeRules
	 * @return
	 */
	public Page<GsmCodeSecRules> getPage(Page<GsmCodeSecRules> page,GsmCodeRules gsmCodeRules) {
		return gsmCodeSecRulesDao.getPage(page, gsmCodeRules);
	}
	
	/**
	 * 根据一级类别获取对象
	 * @param gsmCodeRules
	 * @return
	 */
	public List<GsmCodeSecRules> getGsmCodeSecRules(GsmCodeRules gsmCodeRules) {
		return gsmCodeSecRulesDao.getGsmCodeSecRules(gsmCodeRules);
	}
	
	/**
	 * 根据对象获取对象
	 * @param gsmCodeSecRules
	 * @return
	 */
	public GsmCodeSecRules getGsmCodeSecRules(GsmCodeSecRules gsmCodeSecRules) {
		List<GsmCodeSecRules> list = gsmCodeSecRulesDao.getGsmCodeSecRules(gsmCodeSecRules);
		return list != null && list.size() > 0?list.get(0):null;
	}
	
	/**
	 * 是否可以保存
	 * @return
	 */
	public Boolean canGsmCodeSecRules(GsmCodeSecRules gsmCodeSecRules){
		List<GsmCodeSecRules> list = gsmCodeSecRulesDao.getGsmCodeSecRules(gsmCodeSecRules);
		return list != null && list.size() > 0?false:true;
	}
}
