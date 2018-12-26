package com.ambition.supplier.baseInfo.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.baseInfo.dao.SupplierLevelChangeDao;
import com.ambition.supplier.baseInfo.dao.SupplierLevelScoreDao;
import com.ambition.supplier.entity.SupplierLevelChange;
import com.ambition.supplier.entity.SupplierLevelScore;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月9日 发布
 */
@Service
@Transactional
public class SupplierLevelChangeManager {
	@Autowired
	private SupplierLevelChangeDao supplierLevelChangeDao;
	@Autowired
	private LogUtilDao logUtilDao;

	public SupplierLevelChange getSupplierLevelScore(Long id) {
		// TODO Auto-generated method stub
		return supplierLevelChangeDao.get(id);
	}


	public void saveSupplierLevelChange(String params) {
		// TODO Auto-generated method stub
		JSONArray itemStrArray = null;
		if (!params.isEmpty()) {
			itemStrArray = JSONArray.fromObject(params);
		}
		String hql = "delete from supplier_Level_Change";
		supplierLevelChangeDao.getSession().createSQLQuery(hql).executeUpdate();
		if (!itemStrArray.isEmpty()) {
			for (int i = 0; i < itemStrArray.size(); i++) {
				JSONObject jso = itemStrArray.getJSONObject(i);
				SupplierLevelChange supplierLevelChange = new SupplierLevelChange();
				supplierLevelChange.setCompanyId(ContextUtils.getCompanyId());
				supplierLevelChange.setCreatedTime(new Date());
				supplierLevelChange.setCreatorName(ContextUtils.getUserName());
				supplierLevelChange.setCreator(ContextUtils.getLoginName());
				supplierLevelChange.setModifiedTime(new Date());
				for (Object key : jso.keySet()) {
					String value = jso.getString(key.toString());
					try {
						setProperty(supplierLevelChange, key.toString(), value);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				supplierLevelChangeDao.save(supplierLevelChange);
			}
		}
	}
	 private void setProperty(Object obj,String property,Object value) throws Exception{
	        Class<?> type = PropertyUtils.getPropertyType(obj,property);
	        if(type != null){
	            if(value==null||StringUtils.isEmpty(value.toString())){
	                PropertyUtils.setProperty(obj,property,null);
	            }else{
	                 if(String.class.getName().equals(type.getName())){
	                    PropertyUtils.setProperty(obj,property,value.toString());
	                }else if(Integer.class.getName().equals(type.getName())){
	                    PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
	                }else if(Double.class.getName().equals(type.getName())){
	                    PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
	                }else if(Float.class.getName().equals(type.getName())){
	                    PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
	                }else if(Boolean.class.getName().equals(type.getName())){
	                    PropertyUtils.setProperty(obj,property,Boolean.valueOf(value.toString()));
	                }else if(Date.class.getName().equals(type.getName())){
	                    if(Date.class.getName().equals(value.getClass().getName())){
	                        PropertyUtils.setProperty(obj,property,value);
	                    }else if(String.class.getName().equals(value.getClass().getName())&&value.toString().length()==10){
	                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                        PropertyUtils.setProperty(obj,property,sdf.parse(value.toString()));
	                    }
	                }else{
	                    PropertyUtils.setProperty(obj,property,value);
	                }
	            }
	        }
	    }


	public List<SupplierLevelChange> searchlist() {
		// TODO Auto-generated method stub
		String hql = " from SupplierLevelChange s where s.companyId=? ";
		return supplierLevelChangeDao.find(hql,ContextUtils.getCompanyId());
	}
}
