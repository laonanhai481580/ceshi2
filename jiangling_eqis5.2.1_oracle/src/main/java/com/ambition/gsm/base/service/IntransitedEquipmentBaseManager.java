package com.ambition.gsm.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.base.dao.IntransitedEquipmentBaseDao;
import com.ambition.gsm.entity.IntransitedEquipmentBase;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.orm.Page;

/**
 * 在途量检具基类(SERVICE)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class IntransitedEquipmentBaseManager {
	@Autowired
	private IntransitedEquipmentBaseDao intransitedEquipmentBaseDao;

	public IntransitedEquipmentBase getIntransitedEquipmentBase(Long id){
		return intransitedEquipmentBaseDao.get(id);
	}

	public void saveGsmEquipmentBase(IntransitedEquipmentBase intransitedEquipmentBase){
		intransitedEquipmentBaseDao.save(intransitedEquipmentBase);
	}
	
	public void deleteIntransitedEquipmentBase(IntransitedEquipmentBase intransitedEquipmentBase){
		intransitedEquipmentBaseDao.delete(intransitedEquipmentBase);
	}

	public Page<IntransitedEquipmentBase> search(Page<IntransitedEquipmentBase>page){
		return intransitedEquipmentBaseDao.search(page);
	}
	
	public Page<IntransitedEquipmentBase> list(Page<IntransitedEquipmentBase>page){
		return intransitedEquipmentBaseDao.list(page);
	}

	public List<IntransitedEquipmentBase> listAll(){
		return intransitedEquipmentBaseDao.getAllIntransitedEquipmentBase();
	}
		
	public void deleteIntransitedEquipmentBase(Long id){
		intransitedEquipmentBaseDao.delete(id);
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteIntransitedEquipmentBase(String ids) {
		String[] deleteIds = ids.split(",");
		int deleteNum=0;
		int failNum=0;
		for (String id : deleteIds) {
			IntransitedEquipmentBase  intransitedEquipmentBase = intransitedEquipmentBaseDao.get(Long.valueOf(id));
			intransitedEquipmentBaseDao.delete(intransitedEquipmentBase);
			deleteNum++;
		}
		return deleteNum+"条数据没有权限删除！";
	}
	
	/**
	 * 基础设置删除
	 * 
	 * @param deleteIds
	 */
	public void deleteGsm(String deleteIds) throws Exception {
		String[] ids = deleteIds.split(",");
		for (String id : ids) {
			IntransitedEquipmentBase  intransitedEquipmentBase = intransitedEquipmentBaseDao.get(Long.valueOf(id));
			if (null !=  intransitedEquipmentBase) {
				 intransitedEquipmentBaseDao.delete( intransitedEquipmentBase);
			}
		}
	}
	
	/**
	 * 保存对象
	 * 
	 */
	public  void save( IntransitedEquipmentBase  intransitedEquipmentBase) throws Exception {
		 intransitedEquipmentBaseDao.save(intransitedEquipmentBase);
	}
	
	/**
	 * 导出售后投诉信息台帐
	 * 
	 * @param page
	 * @param complaint
	 * @return
	 */
	public Page<IntransitedEquipmentBase> searchByPage(Page<IntransitedEquipmentBase> page){
		return intransitedEquipmentBaseDao.list(page);
	} 
	
	public IntransitedEquipmentBase getIntransitedEquipmentBaseNo(String measurementNo){
		return intransitedEquipmentBaseDao.getIntransitedEquipmentBaseNo(measurementNo);
	}

}
