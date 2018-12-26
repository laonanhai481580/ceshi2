package com.ambition.gsm.inspectionplan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.InspectionWarnUser;
import com.ambition.gsm.inspectionplan.dao.InspectionWarnUserDao;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class InspectionWarnUserManager {
	@Autowired
	private InspectionWarnUserDao inspectionWarnUserDao;
	

	public InspectionWarnUser getInspectionWarnUser(Long id){
		return inspectionWarnUserDao.get(id);
	}

	public void saveInspectionWarnUser(InspectionWarnUser inspectionWarnUser){
		inspectionWarnUserDao.save(inspectionWarnUser);
	}

	
	
	public void deleteInspectionWarnUser(InspectionWarnUser inspectionWarnUser){
		inspectionWarnUserDao.delete(inspectionWarnUser);
	}

	public Page<InspectionWarnUser> search(Page<InspectionWarnUser>page){
		return inspectionWarnUserDao.search(page);
	}

	public List<InspectionWarnUser> listAll(){
		return inspectionWarnUserDao.getAllInspectionWarnUser();
	}
		
	public void deleteInspectionWarnUser(Long id){
		inspectionWarnUserDao.delete(id);
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteInspectionWarnUser(String ids) {
		String[] deleteIds = ids.split(",");
		int deleteNum=0;
		int failNum=0;
		for (String id : deleteIds) {
			InspectionWarnUser  inspectionWarnUser = inspectionWarnUserDao.get(Long.valueOf(id));
				inspectionWarnUserDao.delete(inspectionWarnUser);
				deleteNum++;
		}
		return deleteNum+"条数据成功删除，"+failNum+"条数据没有权限删除！";
	}

	
}
