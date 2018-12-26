package com.ambition.epm.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.baseinfo.dao.EpmOrtIndicatorDao;
import com.ambition.epm.entity.EpmOrtIndicator;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class EpmOrtIndicatorManager {
	@Autowired
	private EpmOrtIndicatorDao ortIndicatorDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public EpmOrtIndicator getOrtIndicator(Long id){
		return ortIndicatorDao.get(id);
	}
	//验证并保存记录
	public boolean isExistOrtIndicator(Long id, String customerNo, String model,String samplType){
		String hql = "select count(*) from EpmOrtIndicator d where d.companyId =? and d.customerNo=? and d.model = ? and d.samplType = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(customerNo);
		params.add(model);
		params.add(samplType);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = ortIndicatorDao.getSession().createQuery(hql);
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
	public void saveOrtIndicator(EpmOrtIndicator ortIndicator){
		if(StringUtils.isEmpty(ortIndicator.getCustomerNo())){
			throw new RuntimeException("客户编号不能为空!");
		}
		if(StringUtils.isEmpty(ortIndicator.getModel())){
			throw new RuntimeException("model不能为空!");
		}
		if(StringUtils.isEmpty(ortIndicator.getSamplType())){
			throw new RuntimeException("样品类型不能为空!");
		}
//		if(isExistOrtIndicator(ortIndicator.getId(), ortIndicator.getCustomerNo(), ortIndicator.getModel(),ortIndicator.getSamplType())){
//			throw new RuntimeException("已存在相同的测试标准!");
//		}
		ortIndicatorDao.save(ortIndicator);
	}
	public void saveExcelOrtIndicator(EpmOrtIndicator ortIndicator){
		ortIndicatorDao.save(ortIndicator);
	}
	
	//删除记录
	public void deleteOrtIndicator(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(ortIndicatorDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", ortIndicatorDao.get(Long.valueOf(id)).toString());
			}
			ortIndicatorDao.delete(Long.valueOf(id));
		}
	}
	
	//删除对象
	public void deleteOrtIndicator(EpmOrtIndicator ortIndicator){
		logUtilDao.debugLog("删除", ortIndicator.toString());
		ortIndicatorDao.delete(ortIndicator);
	}
	public Page<EpmOrtIndicator> list(Page<EpmOrtIndicator> page){
		return ortIndicatorDao.list(page);
	}	
	//返回所有对象列表
	public List<EpmOrtIndicator> listAll(){
		return ortIndicatorDao.getAllOrtIndicator();
	}
	public EpmOrtIndicator getOrtIndicatorBySth(String customerNo,String model,String samplType){
		return ortIndicatorDao.getOrtIndicatorBySth(customerNo,model,samplType);
	}
}
