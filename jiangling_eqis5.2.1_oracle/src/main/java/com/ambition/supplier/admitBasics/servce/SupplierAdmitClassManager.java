package com.ambition.supplier.admitBasics.servce;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.admitBasics.dao.SupplierAdmitClassDao;
import com.ambition.supplier.entity.SupplierAdmitClass;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class SupplierAdmitClassManager {
	@Autowired
	private SupplierAdmitClassDao admitClassDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public SupplierAdmitClass getSupplierAdmitClass(Long id){
		return admitClassDao.get(id);
	}
	//验证并保存记录
	public boolean isExistSupplierAdmitClass(Long id, String name,String businessUnit){
		String hql = "select count(*) from SupplierAdmitClass d where d.companyId =? and d.businessUnitName=? and d.supplierAdmitClass = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(businessUnit);
		params.add(name);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = admitClassDao.getSession().createQuery(hql);
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
	public void saveSupplierAdmitClass(SupplierAdmitClass admitClass){
		if(StringUtils.isEmpty(admitClass.getSupplierAdmitClass())){
			throw new RuntimeException("材料类别不能为空!");
		}
		if(isExistSupplierAdmitClass(admitClass.getId(), admitClass.getSupplierAdmitClass(),admitClass.getBusinessUnitName())){
			throw new RuntimeException("该事业部已存在相同材料类别!");
		}
		admitClassDao.save(admitClass);
	}
	public void saveExcelSupplierAdmitClass(SupplierAdmitClass admitClass){
		admitClassDao.save(admitClass);
	}
	
	//删除记录
	public String deleteSupplierAdmitClass(String ids){
		StringBuilder sb = new StringBuilder("");
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(admitClassDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", admitClassDao.get(Long.valueOf(id)).toString());
			}
			admitClassDao.delete(Long.valueOf(id));
			sb.append(admitClassDao.get(Long.valueOf(id)).getSupplierAdmitClass() + ",");
		}
		return sb.toString();
	}
	
	//删除对象
	public void deleteSupplierAdmitClass(SupplierAdmitClass admitClass){
		logUtilDao.debugLog("删除", admitClass.toString());
		admitClassDao.delete(admitClass);
	}
	
	//返回页面对象列表
	public Page<SupplierAdmitClass> list(Page<SupplierAdmitClass> page,String businessUnit){
		return admitClassDao.list(page,businessUnit);
	}
	
	//返回所有对象列表
	public List<SupplierAdmitClass> listAll(){
		return admitClassDao.getAllSupplierAdmitClass();
	}
	public SupplierAdmitClass getSupplierAdmitClassByCode(String materialSort){
		return admitClassDao.getSupplierAdmitClassByCode(materialSort);
	}
	public List<SupplierAdmitClass> getSupplierAdmitClassByBusinessUnit(String businessUnit){
		return admitClassDao.getSupplierAdmitClassByBusinessUnit(businessUnit);
	}
	/**
	 * 获取所有的不良类别
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<SupplierAdmitClass> admitClasss = listAll();
		List<Option> options = new ArrayList<Option>();
		for(SupplierAdmitClass admitClass : admitClasss){
			Option option = new Option();
			option.setName(admitClass.getSupplierAdmitClass());
			option.setValue(admitClass.getSupplierAdmitClass());
			options.add(option);
		}
		return options;
	}
}
