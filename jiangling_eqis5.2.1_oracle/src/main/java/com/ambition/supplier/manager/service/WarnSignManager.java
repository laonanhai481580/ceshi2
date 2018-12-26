package com.ambition.supplier.manager.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.SupplierGoal;
import com.ambition.supplier.entity.WarnSign;
import com.ambition.supplier.manager.dao.WarnSignDao;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * WarnSignManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class WarnSignManager {
	@Autowired
	private WarnSignDao warnSignDao;
	@Autowired
	private LogUtilDao logUtilDao;
//	@Autowired
//	private EvaluateManager evaluateManager;
	@SuppressWarnings("rawtypes")
	private Boolean isExistWarnSign(Long id,String businessUnitCode,String degree){
		String hql = "select count(*) from WarnSign w where w.companyId = ? and w.businessUnitCode = ? and w.estimateDegree = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(businessUnitCode);
		params.add(degree);
		if(id != null){
			hql += " and w.id <> ?";
			params.add(id);
		}
		Query query = warnSignDao.createQuery(hql,params.toArray());
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	public WarnSign getWarnSign(Long id) {
		return warnSignDao.get(id);
	}
	public void deleteWarnSign(String deleteIds) {
		StringBuffer names = new StringBuffer("");
		StringBuffer businessUnitCodes = new StringBuffer("");
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			WarnSign warnSign = warnSignDao.get(Long.valueOf(id));
			if(warnSign != null){
				logUtilDao.debugLog("删除", warnSign.toString());
				warnSignDao.delete(warnSign);
				if(names.length()>0){
					names.append(",");
				}
				names.append("'" + warnSign.getEstimateDegree() + "'");
				if(businessUnitCodes.length()>0){
					businessUnitCodes.append("'" + warnSign.getBusinessUnitCode() + "'");
				}
			}
		}
		//更新相关的为其他
		if(names.length()>0){
			String hql = "update SupplierGoal s set s.redYellowCard=null,s.evaluateGrade = '其他' where s.companyId = ? and s.evaluateGrade in (" + names.toString() + ") and s.businessUnitCode in (" + businessUnitCodes + ") and s.evaluateYear = ?";
			Query query = warnSignDao.getSession().createQuery(hql);
			query.setParameter(0,ContextUtils.getCompanyId());
			query.setParameter(1,Calendar.getInstance().get(Calendar.YEAR));
			query.executeUpdate();
		}
	}
	public void saveWarnSign(WarnSign warnSign) {
		if(StringUtils.isEmpty(warnSign.getBusinessUnitCode())){
			throw new AmbFrameException("事业部不能为空!");
		}
		if(StringUtils.isEmpty(warnSign.getEstimateDegree())){
			throw new RuntimeException("评价等级不能为空!");
		}
		if(warnSign.getGoal1()==null && warnSign.getGoal2()==null){
			throw new RuntimeException("得分范围不能为空!");
		}
		if(warnSign.getGoal1()==null && warnSign.getGoal2()!=null){
			warnSign.setGoalRange("0.0 ≤【分】< "+warnSign.getGoal2().toString());
		}
		if(warnSign.getGoal1()!=null && warnSign.getGoal2()==null){
			warnSign.setGoalRange(warnSign.getGoal1().toString()+" ≤【分】≤ 100.0");
		}
		if(warnSign.getGoal1()!=null && warnSign.getGoal2()!=null){
			if(warnSign.getGoal1()>warnSign.getGoal2()){
				throw new RuntimeException("前得分不能大于后得分!");
			}
			if(warnSign.getGoal2()==100){
				warnSign.setGoalRange(warnSign.getGoal1().toString()+" ≤【分】 ≤ "+warnSign.getGoal2().toString());
			}else{
				warnSign.setGoalRange(warnSign.getGoal1().toString()+" ≤【分】< "+warnSign.getGoal2().toString());
			}
		}
		if(isExistWarnSign(warnSign.getId(),warnSign.getBusinessUnitCode(),warnSign.getEstimateDegree())){
			throw new RuntimeException("已存在相同的评价等级!");
		}
		warnSignDao.save(warnSign);
		//更新供应商的评分等级
		String hql = "from SupplierGoal s where s.companyId = ? and s.evaluateYear = ?";
		List<SupplierGoal> supplierGoals = warnSignDao.find(hql,ContextUtils.getCompanyId(),Calendar.getInstance().get(Calendar.YEAR));
//		evaluateManager.calculateEvaluateGrade(supplierGoals);
		
	}
	public Page<WarnSign> list(Page<WarnSign> page){
		return warnSignDao.list(page);
	}
	public  List<WarnSign> list(){
		return warnSignDao.getAllWarSing();
	}
}
