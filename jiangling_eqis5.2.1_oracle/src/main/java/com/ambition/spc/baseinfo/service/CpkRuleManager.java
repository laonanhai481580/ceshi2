package com.ambition.spc.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.baseinfo.dao.CpkRuleDao;
import com.ambition.spc.entity.CpkRule;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:CPK规则表(com.ambition.spc.baseinfo.service)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年7月5日 发布
 */
@Service
public class CpkRuleManager {
	@Autowired
	private CpkRuleDao cpkRuleDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public CpkRule getCpkRule(Long id){
		return cpkRuleDao.get(id);
	}
	public Page<CpkRule> list(Page<CpkRule>page){
		return cpkRuleDao.list(page);
	}
	/**
	 * 检查是否存在相同名称的数据
	 * @param student
	 * @return
	 */
	private Boolean isExistName(CpkRule cpkRule) throws Exception{
		StringBuilder hql = new StringBuilder("select count(*) from CpkRule t where name = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(cpkRule.getName());
		//判断是修改update还是添加Add
		if(cpkRule.getId()!=null){
			hql.append(" and t.id <> ?");
			params.add(cpkRule.getId());
		}
		Query query = cpkRuleDao.getSession().createQuery(hql.toString());
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List<?> list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 检查是否存在相同交差范围的数据
	 * @param student
	 * @return
	 */
	private Boolean isExist(CpkRule cpkRule) throws Exception{
		StringBuilder hql = new StringBuilder("select count(*) from CpkRule t where 1<2");
		List<Object> params = new ArrayList<Object>();
		Double upLimit = cpkRule.getUpLimit();
		Double belowLimit = cpkRule.getBelowLimit();
		Double upLimitMin = Double.valueOf(Integer.MAX_VALUE);
		Double belowLimitMax = Double.valueOf(Integer.MIN_VALUE);
		if(upLimit == null){
			upLimit = upLimitMin;
		}
		if(belowLimit == null){
			belowLimit = belowLimitMax;
		}
		//比较时，>=下限 <上限
		hql.append(" and (");
		hql.append("t.upLimit is not null and t.belowLimit is not null and (t.upLimit > ? and t.belowLimit < ? ");
		hql.append(" or t.upLimit > ? and t.belowLimit <= ? or t.upLimit <= ? and t.belowLimit >= ?) ");
		params.add(upLimit);
		params.add(upLimit);
		params.add(belowLimit);
		params.add(belowLimit);
		params.add(upLimit);
		params.add(belowLimit);
		hql.append(" or t.upLimit is null and t.belowLimit <= ? ");
		params.add(belowLimit);
		hql.append(" or t.belowLimit is null and t.upLimit > ? ");
		params.add(upLimit);
		hql.append(" )");
		//判断是修改update还是添加Add
		if(cpkRule.getId()!=null){
			hql.append(" and t.id <> ?");
			params.add(cpkRule.getId());
		}
		Query query = cpkRuleDao.getSession().createQuery(hql.toString());
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List<?> list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	  * 方法名:保存CPK准则
	  * <p>功能说明：</p>
	  * @return
	 */
	@Transactional
	public void saveCpkRule(CpkRule cpkRule) throws Exception{
		if(StringUtils.isEmpty(cpkRule.getName())){
			throw new RuntimeException("名称不能为空！");
		}
		if(this.isExistName(cpkRule)){
			throw new AmbFrameException("名称不能重复!");
		}
		if(cpkRule.getUpLimit()==null&&cpkRule.getBelowLimit()==null){
			throw new AmbFrameException("上限、下限必须填一个！");
		}
		if(cpkRule.getUpLimit()!=null&&cpkRule.getBelowLimit()!=null&&cpkRule.getUpLimit()<cpkRule.getBelowLimit()){
			throw new AmbFrameException("[上限值]不能小于[下限值]！");
		}
		if(this.isExist(cpkRule)){
			throw new AmbFrameException("该范围已统计过，请重新选择!");
		}
		cpkRuleDao.save(cpkRule);
	}
	
	/**
	 * 刪除FPA目标值配置数据
	 * @param ids
	 */
	@Transactional
	public void deleteBsRulesDao(String ids) throws Exception{
		String[] deleteIds = ids.split(",");
		for(String id :deleteIds){
			logUtilDao.debugLog("删除", cpkRuleDao.get(Long.valueOf(id)).toString());
			cpkRuleDao.delete(Long.valueOf(id));
		}
	}
	
	public List<CpkRule> getAllList(){
//		return cpkRuleDao.getAll();
		return cpkRuleDao.find("from CpkRule c ");
	}
	
}
