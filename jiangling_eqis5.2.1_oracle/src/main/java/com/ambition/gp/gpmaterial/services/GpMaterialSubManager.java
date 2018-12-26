package com.ambition.gp.gpmaterial.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gp.entity.GpMaterialSub;
import com.ambition.gp.gpmaterial.dao.GpMaterialSubDao;
import com.ambition.gsm.inspectionplan.dao.InspectionPlanDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class GpMaterialSubManager {
	@Autowired
	private GpMaterialSubDao gpMaterialSubDao;
	@Autowired
	private InspectionPlanDao inspectionPlanDao;
	
	public GpMaterialSub getGpMaterialSub(Long id){
		return gpMaterialSubDao.get(id);
	}
	public void saveGpMaterialSub(GpMaterialSub gpMaterialSub){
		gpMaterialSubDao.save(gpMaterialSub);
	}
	
	public Page<GpMaterialSub> list(Page<GpMaterialSub> page){
		return gpMaterialSubDao.list(page);
	}
	
	public List<GpMaterialSub> listAll(){
		return gpMaterialSubDao.getGpMaterialSub();
	}
	
	
	public void deleteGpMaterialSub(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
			GpMaterialSub gpMaterialSub=gpMaterialSubDao.get(Long.valueOf(id));
			if(gpMaterialSub.getId() != null){
				gpMaterialSubDao.delete(gpMaterialSub);
			}
		}
	}
	public Page<GpMaterialSub> listState(Page<GpMaterialSub> page,String code){
		String hql = " from GpMaterialSub e where e.gpMaterial.code=?";
		return gpMaterialSubDao.searchPageByHql(page, hql, code);
	}
	public Page<GpMaterialSub> search(Page<GpMaterialSub> page){
		return gpMaterialSubDao.search(page);
	}
	public Page<GpMaterialSub> listPageByParams(Page<GpMaterialSub> page) {
		// TODO Auto-generated method stub
		String hql = " from GpMaterialSub s where s.isHarmful = ?";
		return gpMaterialSubDao.searchPageByHql(page, hql,"1");
	}
	public void harmful(String eid,String type){
		GpMaterialSub gpMaterialSub = gpMaterialSubDao.get(Long.valueOf(eid));
		if(type!=null){
			gpMaterialSub.setIsHarmful("1");
		}else{
			gpMaterialSub.setIsHarmful("0");
		}
		gpMaterialSubDao.save(gpMaterialSub);
	}
	public List<GpMaterialSub> selectAverageInput(String id){
		return gpMaterialSubDao.selectGpMaterialSub(id);
	}
}
