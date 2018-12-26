package com.ambition.aftersales.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.CustomerPlaceDao;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.CustomerPlace;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class CustomerPlaceManager {
	@Autowired
	private CustomerPlaceDao customerPlaceDao;
	@Autowired
	private CustomerListManager customerListManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public CustomerPlace getCustomerPlace(Long id){
		return customerPlaceDao.get(id);
	}
	public void saveCustomerPlace(CustomerPlace customerPlace){
		if(StringUtils.isEmpty(customerPlace.getCustomerPlace())){
			throw new RuntimeException("客户场地不能为空!");
		}
		customerPlaceDao.save(customerPlace);
	}	
	public void saveExcelCustomerPlace(CustomerPlace customerPlace){
		customerPlaceDao.save(customerPlace);
	}	
	//删除记录
	public void deleteCustomerPlace(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(customerPlaceDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", customerPlaceDao.get(Long.valueOf(id)).toString());
			}
			customerPlaceDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteCustomerPlace(CustomerPlace customerPlace){
		logUtilDao.debugLog("删除", customerPlace.toString());
		customerPlaceDao.delete(customerPlace);
	}
	
	//返回页面对象列表
	public Page<CustomerPlace> list(Page<CustomerPlace> page,CustomerList customerList){
		return customerPlaceDao.list(page, customerList);
	}
	
	public Page<CustomerPlace> listByParent(Page<CustomerPlace> page,Long parentId){
		return customerPlaceDao.listByParent(page, parentId);
	}
	
	public Page<CustomerPlace> getCodeByParams(Page<CustomerPlace> page, JSONObject params){
		return customerPlaceDao.getCodeByParams(page, params);
	}
	
	public Page<CustomerPlace> list(Page<CustomerPlace> page,String code){
		return customerPlaceDao.list(page, code);
	}
	//返回所有对象列表
	public List<CustomerPlace> listAll(CustomerList customerList){
		return customerPlaceDao.getAllCustomerPlaces(customerList);
	}	
//	返回页面对象列表
	public Page<CustomerPlace> listByParams(Page<CustomerPlace> page ,String customerName){
		return customerPlaceDao.listByParams(page,customerName);
	}
	public List<CustomerPlace> listCustomerPlace(String code){
		return customerPlaceDao.getCustomerPlace(code);
	}
	public List<CustomerPlace> listPlace() {
		String hql = "from CustomerPlace c where  c.companyId = ? and c.customerList is not null";
		List<CustomerPlace> customerPlaces = customerPlaceDao.find(hql,ContextUtils.getCompanyId());
		return customerPlaces;
	}
	public List<CustomerPlace> getPlaces(){
		List<CustomerPlace> customerPlaces = customerPlaceDao.getPlaces();
		return customerPlaces;
	}	
    /**
     * 方法名: 
     * <p>功能说明：</p>
     * @param calendar
     * @return
    */
	public List<Option> converThirdLevelToList(List<CustomerPlace> list){
	   List<Option> options = new ArrayList<Option>();
	   for(CustomerPlace customerPlace : list){
	       Option option = new Option();
	       String name = customerPlace.getCustomerPlace().toString();
	       String value = customerPlace.getCustomerPlace().toString();
	       option.setName(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
	       option.setValue(value==null?"":value.replaceAll("\n","").replaceAll(",","，"));
	       options.add(option);
	   }
	   return options;
	}  	
}
