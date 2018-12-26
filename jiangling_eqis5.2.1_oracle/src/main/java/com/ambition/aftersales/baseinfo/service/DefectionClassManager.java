package com.ambition.aftersales.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.DefectionClassDao;
import com.ambition.aftersales.entity.DefectionClass;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class DefectionClassManager {
	@Autowired
	private DefectionClassDao defectionTypeDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public DefectionClass getDefectionClass(Long id){
		return defectionTypeDao.get(id);
	}
	//验证并保存记录
	public boolean isExistDefectionClass(Long id, String name,String businessUnit){
		String hql = "select count(*) from DefectionClass d where d.companyId =? and d.businessUnitName=? and d.defectionClass = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(businessUnit);
		params.add(name);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = defectionTypeDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size(); i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString()) > 0){
			return true;
		}else{
			return false;
		}		
	}
	public void saveDefectionClass(DefectionClass defectionType){
		if(StringUtils.isEmpty(defectionType.getDefectionClass())){
			throw new RuntimeException("不良类别不能为空!");
		}
		if(isExistDefectionClass(defectionType.getId(), defectionType.getDefectionClass(),defectionType.getBusinessUnitName())){
			throw new RuntimeException("该事业部已存在相同不良类别!");
		}
		defectionTypeDao.save(defectionType);
	}
	public void saveExcelDefectionClass(DefectionClass defectionType){
		defectionTypeDao.save(defectionType);
	}
	
	//删除记录
	public void deleteDefectionClass(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(defectionTypeDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", defectionTypeDao.get(Long.valueOf(id)).toString());
			}
			defectionTypeDao.delete(Long.valueOf(id));
		}
	}
	
	//删除对象
	public void deleteDefectionClass(DefectionClass defectionType){
		logUtilDao.debugLog("删除", defectionType.toString());
		defectionTypeDao.delete(defectionType);
	}
	
	//返回页面对象列表
	public Page<DefectionClass> list(Page<DefectionClass> page,String businessUnit){
		return defectionTypeDao.list(page,businessUnit);
	}
	
	//返回所有对象列表
	public List<DefectionClass> listAll(){
		return defectionTypeDao.getAllDefectionClass();
	}
	public DefectionClass getDefectionClassByCode(String code){
		return defectionTypeDao.getDefectionClassByCode(code);
	}
	public List<DefectionClass> getDefectionClassByBusinessUnit(String businessUnit){
		return defectionTypeDao.getDefectionClassByBusinessUnit(businessUnit);
	}
	/**
	 * 获取所有的不良类别
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<DefectionClass> defectionTypes = listAll();
		List<Option> options = new ArrayList<Option>();
		for(DefectionClass defectionType : defectionTypes){
			Option option = new Option();
			option.setName(defectionType.getDefectionClass());
			option.setValue(defectionType.getDefectionClass());
			options.add(option);
		}
		return options;
	}
}
