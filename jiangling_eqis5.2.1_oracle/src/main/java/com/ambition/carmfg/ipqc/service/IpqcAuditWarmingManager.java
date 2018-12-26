package com.ambition.carmfg.ipqc.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.IpqcAuditWarming;
import com.ambition.carmfg.ipqc.dao.IpqcAuditWarmingDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class IpqcAuditWarmingManager
{

	  @Autowired
	  private IpqcAuditWarmingDao ipqcAuditWarmingDao;
	
	  public IpqcAuditWarming getIpqcAuditWarming(Long id)
	  {
	    return (IpqcAuditWarming)this.ipqcAuditWarmingDao.get(id);
	  }
	
	  public void deleteIpqcAuditWarming(IpqcAuditWarming ipqcAuditWarming) {
	    this.ipqcAuditWarmingDao.delete(ipqcAuditWarming);
	  }
	
	  public Page<IpqcAuditWarming> search(Page<IpqcAuditWarming> page) {
	    return this.ipqcAuditWarmingDao.search(page);
	  }
	
	  public List<IpqcAuditWarming> listAll() {
	    return this.ipqcAuditWarmingDao.getAllIpqcAuditWarming();
	  }
	
	  public void deleteIpqcAuditWarming(Long id) {
	    this.ipqcAuditWarmingDao.delete(id);
	  }
	  public void deleteIpqcAuditWarming(String ids) {
	    String[] deleteIds = ids.split(",");
	    for (String id : deleteIds) {
	      IpqcAuditWarming ipqcAuditWarming = ipqcAuditWarmingDao.get(Long.valueOf(id));
	      if (ipqcAuditWarming.getId() != null)
	        this.ipqcAuditWarmingDao.delete(ipqcAuditWarming);
	    }
	  }
	
	  public void saveIpqcAuditWarming(IpqcAuditWarming ipqcAuditWarming) {
	    if (StringUtils.isEmpty(ipqcAuditWarming.getBusinessUnitName())) {
	      throw new RuntimeException("事业部不能为空!");
	    }
	    if (StringUtils.isEmpty(ipqcAuditWarming.getStation())) {
	      throw new RuntimeException("站别不能为空!");
	    }
	    if (StringUtils.isEmpty(ipqcAuditWarming.getProblemDegree())) {
	      throw new RuntimeException("问题严重度不能为空!");
	    }
	    if (StringUtils.isEmpty(ipqcAuditWarming.getMissingItems())) {
	      throw new RuntimeException("缺失项目不能为空!");
	    }
	    if (isExistAuditWarming(ipqcAuditWarming.getId(), ipqcAuditWarming.getBusinessUnitName(), ipqcAuditWarming.getStation(), ipqcAuditWarming.getProblemDegree(), ipqcAuditWarming.getMissingItems())) {
	      throw new RuntimeException("已存在相同的预警信息!");
	    }
	    this.ipqcAuditWarmingDao.save(ipqcAuditWarming);
	  }
	
	  private boolean isExistAuditWarming(Long id, String businessUnitName, String station, String problemDegree, String missingItems)
	  {
	    String hql = "select count(*) from IpqcAuditWarming d where d.companyId = ? and d.businessUnitName = ? and d.station =? and d.problemDegree =? and d.missingItems =?";
	    List<Object> params = new ArrayList<Object>();
	    params.add(ContextUtils.getCompanyId());
	    params.add(businessUnitName);
	    params.add(station);
	    params.add(problemDegree);
	    params.add(missingItems);
	    if (id != null) {
	      hql = hql + "and d.id <> ?";
	      params.add(id);
	    }
	    Query query = this.ipqcAuditWarmingDao.getSession().createQuery(hql);
	    for (int i = 0; i < params.size(); i++) {
	      query.setParameter(i, params.get(i));
	    }
	
	    List<?> list = query.list();
	    if (Integer.valueOf(list.get(0).toString()).intValue() > 0) {
	      return true;
	    }
	    return false;
	  }
	
	  public IpqcAuditWarming serachWarming(String businessUnitName, String station, String problemDegree, String missingItems) {
	    return this.ipqcAuditWarmingDao.serachWarming(businessUnitName, station, problemDegree, missingItems);
	  }
	}