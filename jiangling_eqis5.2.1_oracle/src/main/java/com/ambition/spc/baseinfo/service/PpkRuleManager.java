package com.ambition.spc.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.baseinfo.dao.PpkRuleDao;
import com.ambition.spc.entity.PpkRule;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;

/**
 * 类名:PPK规则表(com.ambition.spc.baseinfo.service)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2017年5月2日 发布
 */
@Service
@Transactional
public class PpkRuleManager {
	@Autowired
	private PpkRuleDao ppkRuleDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public PpkRule getPpkRule(Long id){
		return ppkRuleDao.get(id);
	}
	public Page<PpkRule> list(Page<PpkRule>page){
		return ppkRuleDao.list(page);
	}
	/**
	 * 检查是否存在相同名称的数据
	 * @param student
	 * @return
	 */
	private Boolean isExistName(PpkRule ppkRule) throws Exception{
		StringBuilder hql = new StringBuilder("select count(*) from PpkRule t where name = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(ppkRule.getName());
		//判断是修改update还是添加Add
		if(ppkRule.getId()!=null){
			hql.append(" and t.id <> ?");
			params.add(ppkRule.getId());
		}
		Query query = ppkRuleDao.getSession().createQuery(hql.toString());
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
	private Boolean isExist(PpkRule ppkRule) throws Exception{
		StringBuilder hql = new StringBuilder("select count(*) from PpkRule t where 1<2");
		List<Object> params = new ArrayList<Object>();
		Double upLimit = ppkRule.getUpLimit();
		Double belowLimit = ppkRule.getBelowLimit();
		Double upLimitMin = Double.parseDouble((""+Integer.MAX_VALUE).toString());
		Double belowLimitMax = Double.parseDouble((""+Integer.MIN_VALUE).toString());
		if(upLimit == null){
			upLimit = upLimitMin;
		}
		if(belowLimit == null){
			belowLimit = belowLimitMax;
		}
		hql.append(" and (");
		hql.append("nvl(t.upLimit,?) <= ? and nvl(t.belowLimit,?) >= ? ");
		params.add(upLimitMin);
		params.add(upLimit);
		params.add(belowLimitMax);
		params.add(upLimit);
		
		hql.append(" or nvl(t.upLimit,?) <= ? and nvl(t.belowLimit,?) >= ? ");
		params.add(upLimitMin);
		params.add(belowLimit);
		params.add(belowLimitMax);
		params.add(belowLimit);
		
		hql.append(" or ? <= nvl(t.upLimit,?) and ? >= nvl(t.upLimit,?)");
		params.add(upLimit);
		params.add(upLimitMin);
		params.add(belowLimit);
		params.add(upLimitMin);
		
		hql.append(" or ? <= nvl(t.belowLimit,?) and ? >= nvl(t.belowLimit,?)");
		params.add(upLimit);
		params.add(belowLimitMax);
		params.add(belowLimit);
		params.add(belowLimitMax);
		hql.append(" )");
		//判断是修改update还是添加Add
		if(ppkRule.getId()!=null){
			hql.append(" and t.id <> ?");
			params.add(ppkRule.getId());
		}
		Query query = ppkRuleDao.getSession().createQuery(hql.toString());
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
	  * 方法名:保存PPK准则
	  * <p>功能说明：</p>
	  * @return
	 */
	public void savePpkRule(PpkRule ppkRule) throws Exception{
		if(StringUtils.isEmpty(ppkRule.getName())){
			throw new RuntimeException("名称不能为空！");
		}
		if(this.isExistName(ppkRule)){
			throw new AmbFrameException("名称不能重复!");
		}
		if(ppkRule.getUpLimit()==null&&ppkRule.getBelowLimit()==null){
			throw new RuntimeException("上限、下限必须填一个！");
		}
		if(ppkRule.getUpLimit()!=null&&ppkRule.getBelowLimit()!=null&&ppkRule.getUpLimit()<ppkRule.getBelowLimit()){
			throw new RuntimeException("[上限值]不能小于[下限值]！");
		}
		if(this.isExist(ppkRule)){
			throw new AmbFrameException("该范围已统计过，请重新选择!");
		}
		ppkRuleDao.save(ppkRule);
	}
	
	/**
	 * 刪除FPA目标值配置数据
	 * @param ids
	 */
	public void deleteBsRulesDao(String ids) throws Exception{
		String[] deleteIds = ids.split(",");
		for(String id :deleteIds){
			logUtilDao.debugLog("删除", ppkRuleDao.get(Long.valueOf(id)).toString());
			ppkRuleDao.delete(Long.valueOf(id));
		}
	}
	
	public List<PpkRule> getAllList(){
//		return ppkRuleDao.getAll();
		return ppkRuleDao.find("from PpkRule c order by c.name asc");
	}
	
}
