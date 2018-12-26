package com.ambition.carmfg.ipqc.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.IpqcProblemScore;
import com.ambition.carmfg.ipqc.dao.IpqcProblemScoreDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class IpqcProblemScoreManager
{

	  @Autowired
	  private IpqcProblemScoreDao ipqcProblemScoreDao;
	
	  public IpqcProblemScore getIpqcProblemScore(Long id)
	  {
	    return (IpqcProblemScore)this.ipqcProblemScoreDao.get(id);
	  }
	
	  public void deleteIpqcProblemScore(IpqcProblemScore ipqcProblemScore) {
	    this.ipqcProblemScoreDao.delete(ipqcProblemScore);
	  }
	
	  public Page<IpqcProblemScore> search(Page<IpqcProblemScore> page) {
	    return this.ipqcProblemScoreDao.search(page);
	  }
	
	  public List<IpqcProblemScore> listAll() {
	    return this.ipqcProblemScoreDao.getAllIpqcProblemScore();
	  }
	
	  public void deleteIpqcProblemScore(Long id) {
	    this.ipqcProblemScoreDao.delete(id);
	  }
	  public void deleteIpqcProblemScore(String ids) {
	    String[] deleteIds = ids.split(",");
	    for (String id : deleteIds) {
	      IpqcProblemScore ipqcProblemScore = ipqcProblemScoreDao.get(Long.valueOf(id));
	      if (ipqcProblemScore.getId() != null)
	        this.ipqcProblemScoreDao.delete(ipqcProblemScore);
	    }
	  }
	
	  public void saveIpqcProblemScore(IpqcProblemScore ipqcProblemScore) {
	    if (StringUtils.isEmpty(ipqcProblemScore.getProblemDegree())) {
		      throw new RuntimeException("问题严重度不能为空!");
		}
	    if (isExistAuditWarming(ipqcProblemScore.getId(),ipqcProblemScore.getProblemDegree())) {
	      throw new RuntimeException("已存在相同的信息!");
	    }
	    this.ipqcProblemScoreDao.save(ipqcProblemScore);
	  }
	
	  private boolean isExistAuditWarming(Long id,String problemDegree)
	  {
	    String hql = "select count(*) from IpqcProblemScore d where d.companyId = ? and d.problemDegree = ? ";
	    List<Object> params = new ArrayList<Object>();
	    params.add(ContextUtils.getCompanyId());
	    params.add(problemDegree);
	    if (id != null) {
	      hql = hql + "and d.id <> ?";
	      params.add(id);
	    }
	    Query query = this.ipqcProblemScoreDao.getSession().createQuery(hql);
	    for (int i = 0; i < params.size(); i++) {
	      query.setParameter(i, params.get(i));
	    }
	
	    List<?> list = query.list();
	    if (Integer.valueOf(list.get(0).toString()).intValue() > 0) {
	      return true;
	    }
	    return false;
	  }
	  
	  
	  public IpqcProblemScore serach(String problemDegree){
		return ipqcProblemScoreDao.serach(problemDegree);		  		  
	  }
	}
