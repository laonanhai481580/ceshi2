package com.ambition.improve.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.improve.entity.ProblemDescrible;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class ProblemDescribleDao extends HibernateDao<ProblemDescrible,Long>{
	
	public Page<ProblemDescrible> list(Page<ProblemDescrible> page){
		return searchPageByHql(page,"from ProblemDescrible d where d.companyId=?  ",ContextUtils.getCompanyId());
	}
	public List<ProblemDescrible> getAllProblemDescrible(){
		return find("from ProblemDescrible d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<ProblemDescrible> getProblemDescribleByBusinessUnit(String businessUnit){
		return find("from ProblemDescrible d where d.companyId = ? and d.businessUnitName = ? ", ContextUtils.getCompanyId(),businessUnit);
	}
	public String getDefectionTypeById(Long id) {
		String hql = "from ProblemDescrible d where d.id = ?";
		List<ProblemDescrible> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getDefectionType();
		}		
	}
	public ProblemDescrible getProblemDescribleByCode(String code) {
		String hql = "from ProblemDescrible d where d.defectionTypeNo = ?";
		List<ProblemDescrible> list = this.find(hql,code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}
