package com.ambition.improve.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.improve.baseinfo.dao.ProblemDescribleDao;
import com.ambition.improve.entity.ProblemDescrible;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class ProblemDescribleManager {
	@Autowired
	private ProblemDescribleDao problemDescribleDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public ProblemDescrible getProblemDescrible(Long id){
		return problemDescribleDao.get(id);
	}
	//验证并保存记录
	public boolean isExistProblemDescrible(Long id, String name,String businessUnit){
		String hql = "select count(*) from ProblemDescrible d where d.companyId =? and d.businessUnitName=? and d.defectionType = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(businessUnit);
		params.add(name);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = problemDescribleDao.getSession().createQuery(hql);
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
	public void saveProblemDescrible(ProblemDescrible problemDescrible){
		if(StringUtils.isEmpty(problemDescrible.getDefectionType())){
			throw new RuntimeException("不良类型不能为空!");
		}

		if(isExistProblemDescrible(problemDescrible.getId(), problemDescrible.getDefectionType(),problemDescrible.getBusinessUnitName())){
			throw new RuntimeException("该事业部已存在相同不良类型!");
		}
		problemDescribleDao.save(problemDescrible);
	}
	public void saveExcelProblemDescrible(ProblemDescrible problemDescrible){
		problemDescribleDao.save(problemDescrible);
	}
	
	//删除记录
	public void deleteProblemDescrible(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(problemDescribleDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", problemDescribleDao.get(Long.valueOf(id)).toString());
			}
			problemDescribleDao.delete(Long.valueOf(id));
		}
	}
	
	//删除对象
	public void deleteProblemDescrible(ProblemDescrible problemDescrible){
		logUtilDao.debugLog("删除", problemDescrible.toString());
		problemDescribleDao.delete(problemDescrible);
	}
	
	//返回页面对象列表
	public Page<ProblemDescrible> list(Page<ProblemDescrible> page){
		return problemDescribleDao.list(page);
	}
	
	//返回所有对象列表
	public List<ProblemDescrible> listAll(){
		return problemDescribleDao.getAllProblemDescrible();
	}
	public ProblemDescrible getProblemDescribleByCode(String code){
		return problemDescribleDao.getProblemDescribleByCode(code);
	}
	public List<ProblemDescrible> getProblemDescribleByBusinessUnit(String businessUnit){
		return problemDescribleDao.getProblemDescribleByBusinessUnit(businessUnit);
	}
	/**
	 * 获取所有的不良类别
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<ProblemDescrible> problemDescribles = listAll();
		List<Option> options = new ArrayList<Option>();
		for(ProblemDescrible problemDescrible : problemDescribles){
			Option option = new Option();
			option.setName(problemDescrible.getDefectionType());
			option.setValue(problemDescrible.getDefectionType());
			options.add(option);
		}
		return options;
	}
}
