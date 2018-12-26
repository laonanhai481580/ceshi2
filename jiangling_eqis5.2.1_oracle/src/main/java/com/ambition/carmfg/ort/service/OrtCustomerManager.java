package com.ambition.carmfg.ort.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.OrtCustomer;
import com.ambition.carmfg.ort.dao.OrtCustomerDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class OrtCustomerManager {
	@Autowired
	private OrtCustomerDao ortCustomerDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public OrtCustomer getOrtCustomer(Long id){
		return ortCustomerDao.get(id);
	}
	//验证并保存记录
	public boolean isExistOrtCustomer(Long id, String no, String name){
		String hql = "select count(*) from OrtCustomer d where d.companyId =?  and (d.customerNo = ? or d.customerName = ?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(no);
		params.add(name);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = ortCustomerDao.getSession().createQuery(hql);
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
	public void saveOrtCustomer(OrtCustomer ortCustomer){
		if(StringUtils.isEmpty(ortCustomer.getCustomerNo())){
			throw new RuntimeException("客户编码不能为空!");
		}
		if(StringUtils.isEmpty(ortCustomer.getCustomerName())){
			throw new RuntimeException("客户名称不能为空!");
		}
		if(isExistOrtCustomer(ortCustomer.getId(), ortCustomer.getCustomerNo(), ortCustomer.getCustomerName())){
			throw new RuntimeException("已存在相同客户编码或客户名称!");
		}
		ortCustomerDao.save(ortCustomer);
	}
	public void saveExcelOrtCustomer(OrtCustomer ortCustomer){
		ortCustomerDao.save(ortCustomer);
	}
	
	//删除记录
	public void deleteOrtCustomer(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(ortCustomerDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", ortCustomerDao.get(Long.valueOf(id)).toString());
			}
			ortCustomerDao.delete(Long.valueOf(id));
		}
	}
	
	//删除对象
	public void deleteOrtCustomer(OrtCustomer ortCustomer){
		logUtilDao.debugLog("删除", ortCustomer.toString());
		ortCustomerDao.delete(ortCustomer);
	}
	
	//返回页面对象列表
	public Page<OrtCustomer> listByProcessSection(Page<OrtCustomer> page,String processSection){
		return ortCustomerDao.listByProcessSection(page,processSection);
	}
	public Page<OrtCustomer> list(Page<OrtCustomer> page){
		return ortCustomerDao.list(page);
	}	
	//返回所有对象列表
	public List<OrtCustomer> listAll(){
		return ortCustomerDao.getAllOrtCustomer();
	}
	public OrtCustomer getOrtCustomerByCode(String code){
		return ortCustomerDao.getOrtCustomerByCode(code);
	}
	/**
	 * 获取所有的不良类别
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<OrtCustomer> ortCustomers = listAll();
		List<Option> options = new ArrayList<Option>();
		for(OrtCustomer ortCustomer : ortCustomers){
			Option option = new Option();
			option.setName(ortCustomer.getCustomerName());
			option.setValue(ortCustomer.getCustomerName());
			options.add(option);
		}
		return options;
	}
}
