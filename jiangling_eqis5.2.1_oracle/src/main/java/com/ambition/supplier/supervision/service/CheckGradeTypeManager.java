package com.ambition.supplier.supervision.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.CheckGradeType;
import com.ambition.supplier.supervision.dao.CheckGradeTypeDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class CheckGradeTypeManager {
	
	@Autowired
	private CheckGradeTypeDao checkGradeTypeDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	/**
	 * 获取稽查评分项目类型
	 * @return
	 */
	public CheckGradeType getCheckGradeType(Long id){
		return checkGradeTypeDao.get(id);
	}
	
	/**
	 * 查询顶级稽查评分项目类型
	 * @return
	 */
	public List<CheckGradeType> getTopCheckGradeTypes(String type){
		return checkGradeTypeDao.getTopCheckGradeTypes(type);
	}
	
	/**
	 * 检查是否存在相同的项目名称
	 * @param name
	 * @param id
	 * @return
	 */
	private Boolean isExistCheckGradeType(String name,Long id,String type){
		String hql = "select count(id) from CheckGradeType c where c.companyId = ? and c.name = ? and c.type = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(name);
		params.add(type);
		if(id != null){
			hql += " and c.id <> ?";
			params.add(id);
		}
		List<?> list = checkGradeTypeDao.find(hql,params.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 保存评价项目名称
	 * @param checkGradeType
	 */
	public void storeCheckGradeType(CheckGradeType checkGradeType){
		if(StringUtils.isEmpty(checkGradeType.getName())){
			throw new RuntimeException("名称不能为空!");
		}else{
			if(isExistCheckGradeType(checkGradeType.getName(),checkGradeType.getId(),checkGradeType.getType())){
				throw new RuntimeException("已经存在了相同的名称!");
			}else{
				boolean isNew = false;
				if(checkGradeType.getId() == null){
					isNew = true;
					//获取最大的排序号
					String hql = "select max(orderNum) from CheckGradeType c where c.companyId = ? and c.type = ?";
					List<Object> params = new ArrayList<Object>();
					params.add(ContextUtils.getCompanyId());
					params.add(checkGradeType.getType());
					if(checkGradeType.getParent() == null){
						hql += " and c.parent is null";
					}else{
						hql += " and c.parent = ?";
						params.add(checkGradeType.getParent());
					}
					List<?> list = checkGradeTypeDao.find(hql,params.toArray());
					if(list.isEmpty()||list.get(0) == null){
						checkGradeType.setOrderNum(0);
					}else{
						checkGradeType.setOrderNum(Integer.valueOf(list.get(0).toString()) + 1);
					}
				}
				checkGradeTypeDao.save(checkGradeType);
				logUtilDao.debugLog("保存", checkGradeType.toString());
				if(isNew){
					updateMaxFee(checkGradeType.getParent());
				}
			}
		}
	}

	/**
	 * 删除稽查评分项目
	 * @param deleteIds
	 */
	public void deleteCheckGradeType(String deleteIds) {
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				CheckGradeType checkGradeType = checkGradeTypeDao.get(Long.valueOf(id));
				logUtilDao.debugLog("删除", checkGradeType.toString());
				checkGradeTypeDao.delete(checkGradeType);
				updateMaxFee(checkGradeType.getParent());
			}
		}
	}
	
	/**
	 * 保存评价项目名称
	 * @param checkGradeType
	 */
	public void moveCheckGradeType(CheckGradeType checkGradeType,CheckGradeType newParent,String position,Integer orderNum){
		if("last".equals(position)){
			orderNum = 0;
			String hql = "select orderNum from CheckGradeType c where c.companyId = ? and c.type = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(ContextUtils.getCompanyId());
			params.add(checkGradeType.getType());
			if(newParent != null){
				hql += " and c.parent = ?";
				params.add(newParent);
			}else{
				hql += " and c.parent is null";
			}
			hql += " order by c.orderNum desc";
			Query query = checkGradeTypeDao.getSession().createQuery(hql);
			for(int i=0;i<params.size();i++){
				query.setParameter(i,params.get(i));
			}
			List<?> list = query.list();
			if(!list.isEmpty()&&list.get(0)!=null){
				orderNum = Integer.valueOf(list.get(0).toString());
			}
			orderNum++;
		}else if("before".equals(position)){
			String hql = "update CheckGradeType c set c.orderNum = c.orderNum + 1 where c.companyId = ? and c.orderNum >= ? and c.type = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(ContextUtils.getCompanyId());
			params.add(orderNum);
			params.add(checkGradeType.getType());
			if(newParent != null){
				hql += " and c.parent = ?";
				params.add(newParent);
			}else{
				hql += " and c.parent is null";
			}
			Query query = checkGradeTypeDao.getSession().createQuery(hql);
			for(int i=0;i<params.size();i++){
				query.setParameter(i,params.get(i));
			}
			query.executeUpdate();
		}else{
			String hql = "update CheckGradeType c set c.orderNum = c.orderNum - 1 where c.companyId = ? and c.orderNum <= ? and c.type = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(ContextUtils.getCompanyId());
			params.add(orderNum);
			params.add(checkGradeType.getType());
			if(newParent != null){
				hql += " and c.parent = ?";
				params.add(newParent);
			}else{
				hql += " and c.parent is null";
			}
			Query query = checkGradeTypeDao.getSession().createQuery(hql);
			for(int i=0;i<params.size();i++){
				query.setParameter(i,params.get(i));
			}
			query.executeUpdate();
		}
		CheckGradeType hisParent = checkGradeType.getParent();
		checkGradeType.setParent(newParent);
		if(newParent == null){
			checkGradeType.setLevel(1);
		}else{
			checkGradeType.setLevel(newParent.getLevel()+1);
		}
		checkGradeType.setOrderNum(orderNum);
		checkGradeTypeDao.save(checkGradeType);
		checkGradeTypeDao.getSession().flush();
		//更新总分
		if(hisParent == null || newParent == null){
			if(hisParent != null){
				updateMaxFee(hisParent);
			}else{
				updateMaxFee(newParent);
			}
		}else if(hisParent != newParent){
			if(hisParent.getLevel() > newParent.getLevel()){
				updateMaxFee(hisParent);
				updateMaxFee(newParent);
			}else{
				updateMaxFee(newParent);
				updateMaxFee(hisParent);
			}
		}
	}
	/**
	 * 更新总分
	 * @param checkGradeType
	 */
	public void updateMaxFee(CheckGradeType checkGradeType){
		if(checkGradeType == null){
			return;
		}
		if(checkGradeType.getChildren().isEmpty()){
			if(CheckGradeType.TYPE_CHECK.equals(checkGradeType.getType())){//稽查评分,计算总的部分
				String hql = "select sum(c.weight) from CheckGrade c where c.checkGradeType = ?";
				List<?> list = checkGradeTypeDao.find(hql,checkGradeType);
				if(list.isEmpty()||list.get(0)==null){
					checkGradeType.setTotalFee(0.0);
				}else{
					checkGradeType.setTotalFee(Double.valueOf(list.get(0).toString()));
				}
			}else{//获取最高分
				String hql = "select max(c.weight) from CheckGrade c where c.checkGradeType = ?";
				List<?> list = checkGradeTypeDao.find(hql,checkGradeType);
				if(list.isEmpty()||list.get(0)==null){
					checkGradeType.setTotalFee(0.0);
				}else{
					checkGradeType.setTotalFee(Double.valueOf(list.get(0).toString()));
				}
			}
			checkGradeTypeDao.save(checkGradeType);
		}else{
			double totalFee = 0.0;
			for(CheckGradeType child : checkGradeType.getChildren()){
				if(child.getTotalFee() != null){
					totalFee += child.getTotalFee();
				}
			}
			checkGradeType.setTotalFee(totalFee);
			checkGradeTypeDao.save(checkGradeType);
		}
		if(checkGradeType.getParent() != null){
			CheckGradeType parent =checkGradeType.getParent();
			double totalFee = 0.0;
			for(CheckGradeType child : parent.getChildren()){
				if(child.getTotalFee() != null){
					totalFee += child.getTotalFee();
				}
			}
			parent.setTotalFee(totalFee);
			checkGradeTypeDao.save(parent);
			updateMaxFee(parent);
		}
	}
	/**
	 * 设置权重
	 * @param checkGradeType
	 * @param weightStr
	 */
	public void setCheckGradeTypeWeight(CheckGradeType checkGradeType,String weightStr){
		if(StringUtils.isEmpty(weightStr)){
			checkGradeType.setWeight(null);
		}else{
			checkGradeType.setWeight(Double.valueOf(weightStr));
		}
		checkGradeTypeDao.save(checkGradeType);
	}
}
