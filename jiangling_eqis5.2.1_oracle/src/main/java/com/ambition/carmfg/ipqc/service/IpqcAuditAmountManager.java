package com.ambition.carmfg.ipqc.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.IpqcAuditAmount;
import com.ambition.carmfg.ipqc.dao.IpqcAuditAmountDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class IpqcAuditAmountManager{

	  @Autowired
	  private IpqcAuditAmountDao ipqcAuditAmountDao;
	
	  public IpqcAuditAmount getIpqcAuditAmount(Long id)
	  {
	    return (IpqcAuditAmount)this.ipqcAuditAmountDao.get(id);
	  }
	
	  public void deleteIpqcAuditAmount(IpqcAuditAmount ipqcAuditAmount) {
	    this.ipqcAuditAmountDao.delete(ipqcAuditAmount);
	  }
	
	  public Page<IpqcAuditAmount> search(Page<IpqcAuditAmount> page) {
	    return this.ipqcAuditAmountDao.search(page);
	  }
	
	  public List<IpqcAuditAmount> listAll() {
	    return this.ipqcAuditAmountDao.getAllIpqcAuditAmount();
	  }
	
	  public void deleteIpqcAuditAmount(Long id) {
	    this.ipqcAuditAmountDao.delete(id);
	  }
	  public void deleteIpqcAuditAmount(String ids) {
	    String[] deleteIds = ids.split(",");
	    for (String id : deleteIds) {
	      IpqcAuditAmount ipqcAuditAmount = ipqcAuditAmountDao.get(Long.valueOf(id));
	      if (ipqcAuditAmount.getId() != null)
	        this.ipqcAuditAmountDao.delete(ipqcAuditAmount);
	    }
	  }
	
	  public void saveIpqcAuditAmount(IpqcAuditAmount ipqcAuditAmount) {
	    if (StringUtils.isEmpty(ipqcAuditAmount.getBusinessUnitName())) {
	      throw new RuntimeException("厂区不能为空!");
	    }	    
	    if (StringUtils.isEmpty(ipqcAuditAmount.getFactory())) {	      
	    	throw new RuntimeException("工厂不能为空!");
	    }
	    if (StringUtils.isEmpty(ipqcAuditAmount.getStation())) {	      
	    	throw new RuntimeException("工序不能为空!");
	    }
	    if (ipqcAuditAmount.getAuditAmount()==null) {
		      throw new RuntimeException("稽核件数不能为空!");
		    }
	    if (isExistAuditAmount(ipqcAuditAmount.getId(), ipqcAuditAmount.getBusinessUnitName(),ipqcAuditAmount.getFactory(), ipqcAuditAmount.getStation())) {
	      throw new RuntimeException("已存在相同的信息!");
	    }
	    this.ipqcAuditAmountDao.save(ipqcAuditAmount);
	  }
	
	  private boolean isExistAuditAmount(Long id, String businessUnitName,String factory, String station)
	  {
	    String hql = "select count(*) from IpqcAuditAmount d where d.companyId = ? and d.businessUnitName = ? and d.factory = ? and d.station=? ";
	    List<Object> params = new ArrayList<Object>();
	    params.add(ContextUtils.getCompanyId());
	    params.add(businessUnitName);
	    params.add(factory);
	    params.add(station);	   
	    if (id != null) {
	      hql = hql + "and d.id <> ?";
	      params.add(id);
	    }
	    Query query = this.ipqcAuditAmountDao.getSession().createQuery(hql);
	    for (int i = 0; i < params.size(); i++) {
	      query.setParameter(i, params.get(i));
	    }
	
	    List<?> list = query.list();
	    if (Integer.valueOf(list.get(0).toString()).intValue() > 0) {
	      return true;
	    }
	    return false;
	  }
	  
		public IpqcAuditAmount searchIpqcAuditAmount(String businessUnitName,String factory,String station){
			String hql = "select i from IpqcAuditAmount i where i.companyId = ? and i.businessUnitName = ? and i.factory=? and i.station=?  ";
			List<Object> searchParams = new ArrayList<Object>();
			searchParams.add(ContextUtils.getCompanyId());
			searchParams.add(businessUnitName);
			searchParams.add(factory);
			searchParams.add(station);
			List<IpqcAuditAmount> list = ipqcAuditAmountDao.find(hql,searchParams.toArray());
			if(list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}	  
	  
	}