package com.ambition.carmfg.ort.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.OrtPlan;
import com.ambition.carmfg.ort.dao.OrtPlanDao;
import com.norteksoft.product.orm.Page;
/**
 * 
 * 类名:ORT计划Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPFs
 * @version 1.00 2016年8月31日 发布
 */
@Service
@Transactional
public class OrtPlanManager {
	@Autowired
	private OrtPlanDao ortPlanDao;
	public OrtPlan getOrtPlan(Long id){
		return ortPlanDao.get(id);
	}
	
	public void deleteOrtPlan(OrtPlan ortPlan){
		ortPlanDao.delete(ortPlan);
	}

	public Page<OrtPlan> search(Page<OrtPlan>page){
		return ortPlanDao.search(page);
	}

	public List<OrtPlan> listAll(){
		return ortPlanDao.getAllOrtPlan();
	}
		
	public void deleteOrtPlan(Long id){
		ortPlanDao.delete(id);
	}
	public void deleteOrtPlan(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			OrtPlan  ortPlan = ortPlanDao.get(Long.valueOf(id));
			if(ortPlan.getId() != null){
				ortPlanDao.delete(ortPlan);
			}
		}
	}
	public void saveOrtPlan(OrtPlan ortPlan){
		ortPlanDao.save(ortPlan);
	}


}
