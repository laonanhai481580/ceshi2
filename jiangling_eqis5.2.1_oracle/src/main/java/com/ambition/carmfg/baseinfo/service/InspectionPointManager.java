package com.ambition.carmfg.baseinfo.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.dao.InspectionPointDao;
import com.ambition.carmfg.entity.InspectionPoint;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class InspectionPointManager {
	@Autowired
	private InspectionPointDao inspectionPointDao;
	@Autowired
	private LogUtilDao logUtilDao;

	public InspectionPoint getInspectionPoint(Long id){
		return inspectionPointDao.get(id);
	}
	
	public void saveInspectionPoint(InspectionPoint inspectionPoint){
		if(StringUtils.isEmpty(inspectionPoint.getListCode())&&inspectionPoint.getListType() != null){
			if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(inspectionPoint.getListType().getCode())){
				inspectionPoint.setListCode("");
			}
		}
		inspectionPointDao.save(inspectionPoint);
	}
	
	public void deleteInspectionPoint(Long id){
		if(inspectionPointDao.get(id)!=null){
			logUtilDao.debugLog("删除", inspectionPointDao.get(id).toString());
		}
		inspectionPointDao.delete(id);
	}
	
	public void deleteInspectionPoint(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			deleteInspectionPoint(Long.valueOf(id));
		}
	}
	
	public void deleteInspectionPoint(InspectionPoint inspectionPoint){
		logUtilDao.debugLog("删除", inspectionPoint.toString());
		inspectionPointDao.delete(inspectionPoint);
	}
	
	public Page<InspectionPoint> list(Page<InspectionPoint>page){
		return inspectionPointDao.search(page);
	}
	
	public List<InspectionPoint> listAll(){
		return inspectionPointDao.getAllInspectionPoint();
	}
	
	public List<InspectionPoint> getInspectionPointByWorkshop(String workshop){
		return inspectionPointDao.getInspectionPointByWorkshop(workshop);
	}
	
	public List<InspectionPoint> getInspectionPointByWorkshop(String workshop,Long inspectorId){
		return inspectionPointDao.getInspectionPointByWorkshop(workshop,inspectorId);
	}
	
	public List<InspectionPoint> getInspectionPointByInspector(Long inspectorId){
		return inspectionPointDao.getInspectionPointByInspector(inspectorId);
	}
	public List<InspectionPoint> getInspectionPointByInspector(Long inspectorId,InspectionPointTypeEnum typeNum){
		return inspectionPointDao.getInspectionPointByInspector(inspectorId,typeNum);
	}
	public List<Object> getInspectionPointWorkshops(Long inspectorId){
		return inspectionPointDao.getInspectionPointWorkshops(inspectorId);
	}
	public List<InspectionPoint> getInspectionPointByName(String name){
		return inspectionPointDao.getInspectionPointByName(name);
	}
	
	public InspectionPoint getOnlyInspectionPointByName(String name){
		InspectionPoint inspectionPoint = null;
		List<InspectionPoint>  list = inspectionPointDao.getInspectionPointByName(name);
		if(list!=null&&list.size()>0){
			inspectionPoint = list.get(0);
		}
		return inspectionPoint;
	}
	
	public boolean isSingleInspectionPoint(String inspectionPointName,Long id){
		boolean isSingle = true;
		List<InspectionPoint>  list = inspectionPointDao.getInspectionPointExcludeSelf(inspectionPointName,id);
		if(list!=null&&list.size()>0){
			isSingle = false;
		}
		return isSingle;
		
	}
	
	public List<InspectionPoint> getInspectionPointByWorkshopAndType(String workshop,InspectionPointTypeEnum listType){
		String hql = "from InspectionPoint i where i.companyId = ? and i.workshop = ? and i.listType = ?";
		return inspectionPointDao.find(hql,ContextUtils.getCompanyId(),workshop,listType);
	}
	
	/**
	  * 方法名: 根据工厂和工序获取采集点
	  * <p>功能说明：</p>
	  * @param workshop
	  * @param workprocedure
	  * @param listType
	  * @return
	 */
	public InspectionPoint getInspectionPointByWorkshopAndWorkprocedure(String workshop,String workprocedure,InspectionPointTypeEnum listType){
		String hql = "from InspectionPoint i where i.companyId = ? and i.workshop = ? and i.workProcedure = ? and i.listType = ?";
		List<InspectionPoint> points = inspectionPointDao.find(hql,ContextUtils.getCompanyId(),workshop,workprocedure,listType);
		if(points.isEmpty()){
			return null;
		}else{
			return points.get(0);
		}
	}
}
