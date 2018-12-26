package com.ambition.aftersales.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.CustomerListDao;
import com.ambition.aftersales.entity.CustomerList;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class CustomerListManager {
	@Autowired
	private CustomerListDao customerListDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public CustomerList getCustomerList(Long id){
		return customerListDao.get(id);
	}
	//验证并保存记录
	public boolean isExistCustomerList(Long id, String no){
		String hql = "select count(*) from CustomerList d where d.companyId =?  and d.customerNo = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(no);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = customerListDao.getSession().createQuery(hql);
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
	public void saveCustomerList(CustomerList customerList){
		if(StringUtils.isEmpty(customerList.getCustomerNo())){
			throw new RuntimeException("客户代码不能为空!");
		}
		if(StringUtils.isEmpty(customerList.getCustomerName())){
			throw new RuntimeException("客户名称不能为空!");
		}
		if(isExistCustomerList(customerList.getId(), customerList.getCustomerNo())){
			throw new RuntimeException("已存在相同客户信息!");
		}
		customerListDao.save(customerList);
	}
	public void saveExcelCustomerList(CustomerList customerList){
		customerListDao.save(customerList);
	}
	
	//删除记录
	public void deleteCustomerList(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(customerListDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", customerListDao.get(Long.valueOf(id)).toString());
			}
			customerListDao.delete(Long.valueOf(id));
		}
	}
	
	//删除对象
	public void deleteCustomerList(CustomerList customerList){
		logUtilDao.debugLog("删除", customerList.toString());
		customerListDao.delete(customerList);
	}
	
	//返回页面对象列表
	public Page<CustomerList> list(Page<CustomerList> page){
		return customerListDao.list(page);
	}
//	返回页面对象列表
	public CustomerList listByCustomerName(String customerName){
		return customerListDao.listByCustomerName(customerName);
	}
	public List<CustomerList> listAll(){
		return customerListDao.getAllCustomerList();
	}
	public CustomerList getCustomerListByCode(String code){
		return customerListDao.getCustomerListByCode(code);
	}
	public List<CustomerList> getCustomerListByBusinessUnit(String businessUnit){
		return customerListDao.getCustomerListByBusinessUnit(businessUnit);
	}
    /**
     * 方法名: 
     * <p>功能说明：</p>
     * @param calendar
     * @return
    */
public List<Option> converExceptionLevelToList(List<CustomerList> customerLists){
   List<Option> options = new ArrayList<Option>();
   for(CustomerList customerList : customerLists){
       Option option = new Option();
       String name = customerList.getCustomerName().toString();
       String value = customerList.getCustomerName().toString();
       option.setName(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
       option.setValue(value==null?"":value.replaceAll("\n","").replaceAll(",","，"));
       options.add(option);
   }
   return options;
}
	/**
	 * 获取所有的不良类别
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<CustomerList> customerLists = listAll();
		List<Option> options = new ArrayList<Option>();
		for(CustomerList customerList : customerLists){
			Option option = new Option();
			option.setName(customerList.getCustomerName());
			option.setValue(customerList.getCustomerName());
			options.add(option);
		}
		return options;
	}
}
