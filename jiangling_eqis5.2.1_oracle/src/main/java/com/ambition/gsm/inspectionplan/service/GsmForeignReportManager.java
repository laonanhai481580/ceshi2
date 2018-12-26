package com.ambition.gsm.inspectionplan.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmForeignReport;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.inspectionplan.dao.GsmForeignReportDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.erp.service.GsmWarmingService;
import com.ibm.icu.util.Calendar;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class GsmForeignReportManager {
	@Autowired
	private GsmForeignReportDao gsmForeignReportDao;
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;
	@Autowired
	private InspectionPlanManager inspectionPlanManager;
	public GsmForeignReport getGsmForeignReport(Long id){
		return gsmForeignReportDao.get(id);
	}
	public void saveGsmForeignReport(GsmForeignReport gsmForeignReport){
		gsmForeignReportDao.save(gsmForeignReport);
	}
	
	public Page<GsmForeignReport> list(Page<GsmForeignReport> page){
		return gsmForeignReportDao.list(page);
	}
	
	public List<GsmForeignReport> listAll(){
		return gsmForeignReportDao.getGsmForeignReport();
	}
	
	
	public void deleteMaintain(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
 			GsmForeignReport gsmForeignReport=gsmForeignReportDao.get(Long.valueOf(id));
			if(gsmForeignReport.getId() != null){
				gsmForeignReportDao.delete(gsmForeignReport);
			}
		}
	}
	
	public Page<GsmForeignReport> search(Page<GsmForeignReport> page){
		return gsmForeignReportDao.search(page);
	}
	public void sendGsmForeignReport(GsmForeignReport gsmForeignReport) {
		//回写计划日期
		if(gsmForeignReport.getCheckDate()!=null&&gsmForeignReport.getPlanId()!=null&&!gsmForeignReport.getPlanId().equals("")){
			InspectionPlan	inspectionPlan=inspectionPlanManager.getInspectionPlan(Long.valueOf(gsmForeignReport.getPlanId()));
			try {
				inspectionPlan.setActualInspectionDate(gsmForeignReport.getCheckDate());
				inspectionPlan.setInspectionState(InspectionPlan.STATE_CHECk_OUT);
				inspectionPlan.setCheckResult(gsmForeignReport.getCheckResult());
				inspectionPlan.setInspectionPeople(gsmForeignReport.getCheckMan());
				if(gsmForeignReport.getCheckDate().getTime()<=inspectionPlan.getInspectionPlanDate().getTime()){
					inspectionPlan.setIsIntime("是");
				}else{
					inspectionPlan.setIsIntime("否");
				}
				 inspectionPlanManager.saveInspectionPlan(inspectionPlan);
				  //修改原来的量检具信息状态
				  GsmEquipment gsmEquipment = inspectionPlan.getGsmEquipment();
				  gsmEquipment.setMeasurementState(GsmEquipment.STATE_INUSE);
				  gsmEquipment.setProofTime(gsmForeignReport.getCheckDate());
				  Date doDate = inspectionPlan.getActualInspectionDate();//实际校检日期
				  Calendar c = Calendar.getInstance();//获得一个日历的实例
			       c.setTime(doDate);//设置日历时间
			       Integer frequency=Integer.valueOf(inspectionPlan.getFrequency());
			       if(frequency!=null){
			    	   c.add(Calendar.MONTH,frequency);//在日历的月份上增加频率(月)
			    	   c.add(5, -1);
			    	   Date inspectionPlanDateNext=c.getTime();
				       gsmEquipment.setNextProofTime(inspectionPlanDateNext);
			       }
				//生成下一条计划
/*				  if(gsmForeignReport.getHasNext()==null||!gsmForeignReport.getHasNext().equals("是")){
					  InspectionPlan inspectionPlanNew = new InspectionPlan();
					  inspectionPlanNew.setCompanyId(ContextUtils.getCompanyId());
					  inspectionPlanNew.setCreatedTime(new Date());
					  inspectionPlanNew.setCreator(ContextUtils.getLoginName());
					  inspectionPlanNew.setCreatorName(ContextUtils.getUserName());
					  inspectionPlanNew.setModifiedTime(new Date());
					  inspectionPlanNew.setModifier(ContextUtils.getLoginName());
					  inspectionPlanNew.setModifierName(ContextUtils.getUserName());
					  inspectionPlanNew.setBusinessUnitName(ContextUtils.getSubCompanyName());
					  inspectionPlanNew.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
					  inspectionPlanNew.setYesOrNo(true);
					  inspectionPlanNew.setAddress(gsmEquipment.getAddress());
					  inspectionPlanNew.setProcessSection(gsmEquipment.getProcessSection());
					  inspectionPlanNew.setEquipmentName(gsmEquipment.getEquipmentName());
					  inspectionPlanNew.setEquipmentModel(gsmEquipment.getEquipmentModel());
					  inspectionPlanNew.setCompanyMain(gsmEquipment.getCompanyMain());
					  inspectionPlanNew.setDevName(gsmEquipment.getDevName());
					  inspectionPlanNew.setWorkProducre(gsmEquipment.getWorkProducre());
					  inspectionPlanNew.setDutyMan(gsmEquipment.getDutyMan());
					  inspectionPlanNew.setDutyLoginMan(gsmEquipment.getDutyLoginMan());
					  inspectionPlanNew.setManagerAssets(gsmEquipment.getManagerAssets());
					  inspectionPlanNew.setCheckMethod(gsmEquipment.getCheckMethod());
					  inspectionPlanNew.setFrequency(gsmEquipment.getFrequency());
					  Date doDate = inspectionPlan.getActualInspectionDate();//实际校检日期
					  inspectionPlanNew.setInspectionState(InspectionPlan.STATE_CHECk_WAIT);//校验状态
					  inspectionPlanNew.setInspectionLastedDate(doDate);//上次校检日期
					  inspectionPlanNew.setGsmEquipment(gsmEquipment);
					  inspectionPlanNew.setGsmState("N");
					   Calendar c = Calendar.getInstance();//获得一个日历的实例
				       c.setTime(doDate);//设置日历时间
				       Integer frequency=Integer.valueOf(inspectionPlan.getFrequency());
				       if(frequency!=null){
				    	   c.add(Calendar.MONTH,frequency);//在日历的月份上增加频率(月)	
				    	   Date inspectionPlanDateNext=c.getTime();
					       inspectionPlanNew.setInspectionPlanDate(inspectionPlanDateNext);
					       gsmEquipment.setNextProofTime(inspectionPlanDateNext);
					       Long nowDates=new Date().getTime();
					       Long warnDates=inspectionPlanDateNext.getTime();
					       if(nowDates>warnDates){
							   gsmEquipment.setIsWarm(1);
						   }else{
							   Long intervalMilli = warnDates - nowDates;
							   int 	days=(int)(intervalMilli/(24 * 60 * 60 * 1000));
							   if(days<=10){
								   gsmEquipment.setIsWarm(1); 
							   }else{
								   gsmEquipment.setIsWarm(0);
							   }
						   }	
				       }	
				      gsmEquipmentManager.saveGsmEquipment(gsmEquipment);
					  inspectionPlanManager.saveInspectionPlan(inspectionPlanNew);//生成下次校验计划
					  gsmForeignReport.setHasNext("是");
				  }*/
				  } catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public Page<GsmForeignReport> listState(Page<GsmForeignReport> page,String state){
		String hql = " from GsmForeignReport e where e.gsmState=?";
		return gsmForeignReportDao.searchPageByHql(page, hql, state);
	}
}
