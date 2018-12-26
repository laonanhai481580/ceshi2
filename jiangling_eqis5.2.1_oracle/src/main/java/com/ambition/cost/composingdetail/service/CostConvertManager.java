package com.ambition.cost.composingdetail.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.cost.composingdetail.dao.CostConvertDao;
import com.ambition.cost.entity.CostConvert;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:成本转换规则业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-12-8 发布
 */
@Service
@Transactional
public class CostConvertManager {
	@Autowired
	private CostConvertDao costConvertDao;
	public CostConvert getCostConvert(Long id){
		return costConvertDao.get(id);
	}
	public void saveCostConvert(CostConvert costConvert){
		//相同相同的部门,相同的职务不能重复
		String hql = "select count(*) from CostConvert c where c.deptName = ? and c.dutyName = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(costConvert.getDeptName());
		params.add(costConvert.getDutyName());
		if(costConvert.getId() != null){
			hql += " and c.id <> ?";
			params.add(costConvert.getId());
		}
		List<?> objs = costConvertDao.find(hql,params.toArray());
		if(Integer.valueOf(objs.get(0).toString())>0){
			throw new AmbFrameException("已经录入了相同的转换规则!");
		}
		String levelTwoName = Struts2Utils.getParameter("levelTwoName");
		if(StringUtils.isNotEmpty(levelTwoName)){
			costConvert.setLevelTwoName(levelTwoName); 
		}
		costConvert.setModifiedTime(new Date());
		costConvert.setModifier(ContextUtils.getLoginName());
		costConvert.setModifierName(ContextUtils.getUserName());
		costConvertDao.save(costConvert);
		clearCache();
	}
	
	public void deleteCostConvert(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			costConvertDao.delete(Long.valueOf(id));
		}
		clearCache();
	}
	public Page<CostConvert> getListDatas(Page<CostConvert> page,Integer year){
		String hql = "from CostConvert g where g.companyId = ? and g.year = ?";
		return costConvertDao.searchPageByHql(page, hql, new Object[]{ContextUtils.getCompanyId(),year});
	}
	public Page<CostConvert> list(Page<CostConvert> page){
		return costConvertDao.list(page);
	}
	private static List<CostConvert> costConverts = new ArrayList<CostConvert>();
	private static Integer cacheFlag = 0;
	
	public static Map<String,CostConvert> getCacheConvertMap(Session session){
		synchronized (cacheFlag) {
			if(cacheFlag==0){
				String hql = "from CostConvert g";
				@SuppressWarnings("unchecked")
				List<CostConvert> converts = session.createQuery(hql).list();
				costConverts.addAll(converts);
				cacheFlag = 1;
			}
		}
		Map<String,CostConvert> convertMap = new HashMap<String, CostConvert>();
		for(CostConvert convert : costConverts){
			convertMap.put(convert.getDeptName()+convert.getDutyName(),convert);
		}
		return convertMap;
	}
	/**
	  * 方法名: 清除缓存的转换规则
	  * <p>功能说明：</p>
	 */
	public static void clearCache(){
		synchronized (cacheFlag) {
			cacheFlag = 0;
		}
	}
}
