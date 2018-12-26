package com.ambition.supplier.supervision.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.CheckGrade;
import com.ambition.supplier.entity.CheckGradeType;
import com.ambition.supplier.supervision.dao.CheckGradeDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 稽查评分项目明细
 * @author 赵骏
 *
 */
@Service
@Transactional
public class CheckGradeManager {
	@Autowired
	private CheckGradeDao checkGradeDao;
	
	@Autowired
	private LogUtilDao logUtilDao;

	@Autowired
	private CheckGradeTypeManager checkGradeTypeManager;
	/**
	 * 获取稽查评分项目明细
	 * @return
	 */
	public CheckGrade getCheckGrade(Long id){
		return checkGradeDao.get(id);
	}
	
	public List<CheckGrade> queryAllCheckGrades(String type){
		String hql = "from CheckGrade c where c.companyId = ? and c.type = ? order by c.orderNum";
		return checkGradeDao.find(hql,ContextUtils.getCompanyId(),type);
	}
	/**
	 * 检查是否存在相同的项目名称
	 * @param name
	 * @param id
	 * @return
	 */
	private Boolean isExistCheckGrade(String name,Long id,String type,CheckGradeType checkGradeType){
		String hql = "select count(id) from CheckGrade c where c.companyId = ? and c.checkGradeType = ? and c.name = ? and c.type = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(checkGradeType);
		params.add(name);
		params.add(type);
		if(id != null){
			hql += " and c.id <> ?";
			params.add(id);
		}
		List<?> list = checkGradeDao.find(hql,params.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 保存评价项目
	 * @param checkGrade
	 */
	public void storeCheckGrade(CheckGrade checkGrade){
		if(StringUtils.isEmpty(checkGrade.getName())){
			throw new RuntimeException("名称不能为空!");
		}else{
			if(isExistCheckGrade(checkGrade.getName(),checkGrade.getId(),checkGrade.getType(),checkGrade.getCheckGradeType())){
				throw new RuntimeException("已经存在了相同的名称!");
			}else{
				if(checkGrade.getId() == null){
					//获取最大的排序号
					String hql = "select max(orderNum) from CheckGrade c where c.companyId = ? and c.checkGradeType = ? and c.type = ?";
					List<?> list = checkGradeDao.find(hql,ContextUtils.getCompanyId(),checkGrade.getCheckGradeType(),checkGrade.getType());
					if(list.isEmpty()||list.get(0) == null){
						checkGrade.setOrderNum(0);
					}else{
						checkGrade.setOrderNum(Integer.valueOf(list.get(0).toString()) + 1);
					}
				}
				checkGradeDao.save(checkGrade);
				logUtilDao.debugLog("保存", checkGrade.toString());
				checkGradeTypeManager.updateMaxFee(checkGrade.getCheckGradeType());
			}
		}
	}

	/**
	 * 删除稽查评分项目
	 * @param deleteIds
	 */
	public void deleteCheckGrade(String deleteIds) {
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				CheckGrade checkGrade = checkGradeDao.get(Long.valueOf(id));
				logUtilDao.debugLog("删除", checkGrade.toString());
				checkGradeDao.delete(checkGrade);
				checkGradeTypeManager.updateMaxFee(checkGrade.getCheckGradeType());
			}
		}
	}
	
	/**
	 * 移动评价项目名称
	 * @param checkGrade
	 * @param position
	 * @param orderNum
	 */
	public void moveCheckGrade(CheckGrade checkGrade,String position,Integer orderNum){
		if("last".equals(position)){
			orderNum = 0;
			String hql = "select max(orderNum) from CheckGrade c where c.companyId = ? and c.checkGradeType = ? and c.type = ?";
			List<?> list = checkGradeDao.find(hql,ContextUtils.getCompanyId(),checkGrade.getCheckGradeType(),checkGrade.getType());
			if(!list.isEmpty()&&list.get(0)!=null){
				orderNum = Integer.valueOf(list.get(0).toString());
			}
			orderNum++;
		}else if("before".equals(position)){
			String hql = "update CheckGrade c set c.orderNum = c.orderNum + 1 where c.companyId = ? and c.orderNum >= ? and c.checkGradeType = ? and c.type = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(ContextUtils.getCompanyId());
			params.add(orderNum);
			params.add(checkGrade.getCheckGradeType());
			params.add(checkGrade.getType());
			Query query = checkGradeDao.getSession().createQuery(hql);
			for(int i=0;i<params.size();i++){
				query.setParameter(i,params.get(i));
			}
			query.executeUpdate();
		}
		checkGrade.setOrderNum(orderNum);
		checkGradeDao.save(checkGrade);
		logUtilDao.debugLog("移动评价项目", checkGrade.toString());
	}
}
