package com.ambition.carmfg.checkinspection.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 检验报告DAO
 * @author 赵骏
 *
 */
@Repository
public class MfgCheckInspectionReportDao extends HibernateDao<MfgCheckInspectionReport, Long> {
		
	public Page<MfgCheckInspectionReport> list(Page<MfgCheckInspectionReport> page,String workshop,String workProcedure,InspectionPointTypeEnum typeEnum){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ? and o.workshop=? and o.workProcedure = ? and o.inspectionPointType = ?";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),workshop,workProcedure,typeEnum);
	}
	public Page<MfgCheckInspectionReport> listCompletePatrol(Page<MfgCheckInspectionReport> page,String workshop,InspectionPointTypeEnum typeEnum){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ? and o.workshop=? and o.patrolState = ? and o.inspectionPointType = ?";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),workshop,MfgCheckInspectionReport.PATROL_STATE_COMPLETE,typeEnum);
	}
	
	public Page<MfgCheckInspectionReport> list(Page<MfgCheckInspectionReport> page,String checkItemName,InspectionPointTypeEnum typeEnum){
		String hql = "select distinct i.mfgCheckInspectionReport from MfgCheckItem i where i.companyId = ? and i.checkItemName = ? and i.inspectionPointType = ?";
		  return searchPageByHql(page, hql,ContextUtils.getCompanyId(),checkItemName,typeEnum);
	}
	public Page<MfgCheckInspectionReport> list(Page<MfgCheckInspectionReport> page,InspectionPointTypeEnum typeEnum){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ? and o.inspectionPointType = ? and ( o.reportState=? or o.reportState=? )";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),typeEnum,MfgCheckInspectionReport.STATE_AUDIT,MfgCheckInspectionReport.STATE_DEFAULT);
	}
	public Page<MfgCheckInspectionReport> waitAuditList(Page<MfgCheckInspectionReport> page){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ? and  o.reportState=?  ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),MfgCheckInspectionReport.STATE_AUDIT);
	}	
	public Page<MfgCheckInspectionReport> recheckList(Page<MfgCheckInspectionReport> page){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ? and ( o.reportState=? or o.reportState=? ) and o.workflowInfo.workflowId is not null and o.workflowInfo.currentActivityName=?  ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),MfgCheckInspectionReport.STATE_DEFAULT,MfgCheckInspectionReport.STATE_AUDIT,"填写检验报告");
	}		
	
	public Page<MfgCheckInspectionReport> listAll(Page<MfgCheckInspectionReport> page,InspectionPointTypeEnum typeEnum){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ? and o.inspectionPointType = ? and  o.reportState=? ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),typeEnum,MfgCheckInspectionReport.STATE_COMPLETE);
	}	
	
	public Page<MfgCheckInspectionReport> listAllProduct(Page<MfgCheckInspectionReport> page){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ?";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}	
	public Page<MfgCheckInspectionReport> listUnqualifieds(Page<MfgCheckInspectionReport> page){
		String hql = "from MfgCheckInspectionReport o where o.companyId = ? and o.inspectionConclusion=?";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),"NG");
	}
	public Page<MfgCheckInspectionReport> unlist(Page<MfgCheckInspectionReport> page){
		String hql = "select distinct o from MfgCheckInspectionReport o left outer join o.checkItems checkItems where o.inspectionConclusion=?";
		 return searchPageByHql(page, hql,"NG");
	}
	
	public List<MfgCheckInspectionReport> getAllIncomingInspectionActionsReport(){
		String hql = "from MfgCheckInspectionReport i where companyId=?";
		return find(hql, ContextUtils.getCompanyId());
	}
	
	public List<MfgCheckInspectionReport> getAllSupplier(){
		String hql = "select distinct incomingInspectionActionsReport.supplierName from MfgCheckInspectionReport incomingInspectionActionsReport where companyId=?";
		return find(hql, ContextUtils.getCompanyId());
	}
	
	public List<MfgCheckInspectionReport> getAllMaterial(){
		String hql = "select distinct incomingInspectionActionsReport.checkBomCode from MfgCheckInspectionReport incomingInspectionActionsReport where companyId=?";
		return find(hql, ContextUtils.getCompanyId());
	}
	
	public List<MfgCheckInspectionReport> getIncomingInspectionActionsReportByBatchNo(String batchNo) {
		String hql = "from MfgCheckInspectionReport incomingInspectionActionsReport where batchNo=?";
        return find(hql,batchNo);
    }
	
	public Page<MfgCheckInspectionReport> unsearch(Page<MfgCheckInspectionReport> page) {
		String hql = "from MfgCheckInspectionReport incomingInspectionActionsReport where inspectionConclusion=?";
        return searchPageByHql(page, hql,"NG");
    }
	
	public Page<MfgCheckInspectionReport> getQualifiedIncomingInspectionActionsReport(Page<MfgCheckInspectionReport> page,Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql = "select i from MfgCheckInspectionReport i,Supplier s  where i.supplierId=s.id and  i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate!=null){
			searchParams.add(new java.util.Date(startDate.getTime()));
		}
		if(endDate!=null){
			searchParams.add(new java.util.Date(endDate.getTime()));
		}
		searchParams.add(state);
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql=hql+" and i.supplierName=?";
			searchParams.add(dutySupplier);
		}	
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}	
		return findPage(page, hql, searchParams.toArray());
	}
	
	public Page<MfgCheckInspectionReport> getUnQualifiedIncomingInspectionActionsReport(Page<MfgCheckInspectionReport> page,Date startDate,Date endDate,String state,String processingResult,String inspector){
		String hql = "from MfgCheckInspectionReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate!=null){
			searchParams.add(new java.util.Date(startDate.getTime()));
		}
		if(endDate!=null){
			searchParams.add(new java.util.Date(endDate.getTime()));
		}
		searchParams.add(state);
		if(!processingResult.equals("")){
			hql=hql+" and processingResult=?";
			searchParams.add(processingResult);
		}
		if(!inspector.equals("")){
			hql=hql+" and inspector=?";
			searchParams.add(inspector);
		}	
		return findPage(page, hql, searchParams.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<MfgCheckInspectionReport> getQualifiedIncomingInspectionActionsReport(Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql = "select s from MfgCheckInspectionReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<? and i.inspectionConclusion=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate!=null){
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}
		if(endDate!=null){
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		searchParams.add(state);
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql=hql+" and i.supplierName=?";
			searchParams.add(dutySupplier);
		}
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}
		Query query = this.getSession().createQuery(hql.toString());
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();	
		return list;
	}
	
	public List<MfgCheckInspectionReport> getQualifiedIncomingInspectionActionsReport(Date startDate,Date endDate,String state){
		String hql = "from MfgCheckInspectionReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=?";
		return find(hql, ContextUtils.getCompanyId(),startDate,endDate,state);
	}
	
	public List<MfgCheckInspectionReport> getIncomingInspectionActionsReportByInspectionNo(String inspectionNo){
		String hql = "from MfgCheckInspectionReport incomingInspectionActionsReport where companyId=? and inspectionNo=?";
		return find(hql, ContextUtils.getCompanyId(),inspectionNo);
	}
	
	public Page<MfgCheckInspectionReport> getAllIncomingInspectionActionsReport(Page<MfgCheckInspectionReport> page,Date startDate,Date endDate,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql = "select i from MfgCheckInspectionReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate!=null){
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}
		if(endDate!=null){
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!dutySupplier.equals("")){
			hql=hql+" and i.supplierName=?";
			searchParams.add(dutySupplier);
		}	
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}	
		return page=findPage(page, hql, searchParams.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<MfgCheckInspectionReport> getAllIncomingInspectionActionsReport(Date startDate,Date endDate,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		String hql = "from MfgCheckInspectionReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate!=null){
			searchParams.add(new java.sql.Date(startDate.getTime()));
		}
		if(endDate!=null){
			searchParams.add(new java.sql.Date(endDate.getTime()));
		}
		if(!materiel.equals("")){
			hql=hql+" and i.checkBomCode=?";
			searchParams.add(materiel);
		}
		if(!checkBomMaterialType.equals("")){
			hql=hql+" and i.checkBomMaterialType=?";
			searchParams.add(checkBomMaterialType);
		}
		if(!importance.equals("")){
			hql=hql+" and s.importance=?";
			searchParams.add(importance);
		}
		Query query=null; 
		try {
			 query = this.getSession().createQuery(hql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		return list;
	}
	
	
	public List<MfgCheckInspectionReport> getAllIncomingInspectionActionsReport(Date startDate,Date endDate){
		String hql = "from MfgCheckInspectionReport i where companyId=? and inspectionDate>=? and inspectionDate<?";
		return find(hql, ContextUtils.getCompanyId(),startDate,endDate);
	}
	
	//发起预警
	public List<MfgCheckInspectionReport> getAllIncomingInspectionActionsReportByItems(Date startDate,Date endDate,String supplierName,String checkBomName,String checkItemName){
		String hql = "select i from MfgCheckInspectionReport i left join i.checkItems item on item.conclusion=? where where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<=? and i.supplierName=? and i.checkBomName=? and item.checkItemName=? and i.inspectionConclusion=?";
		return find(hql, "NG",ContextUtils.getCompanyId(),startDate,endDate,supplierName,checkBomName,checkItemName,"NG");
	}
	
	//发起改进
	public List<MfgCheckInspectionReport> getAllIncomingInspectionActionsReportByOrder(Date startDate,Date endDate,String supplierName,String checkBomName){
		String hql = "from MfgCheckInspectionReport i where i.companyId=? and i.inspectionDate>=? and i.inspectionDate<=? and i.supplierName=? and i.checkBomName=? and i.inspectionConclusion=?";
		return find(hql, ContextUtils.getCompanyId(),startDate,endDate,supplierName,checkBomName,"NG");
	}
	
	public void decreaseIndex(Integer start, Integer end) {
        createQuery("update GoalItem goalItem set goalItem.displayIndex=goalItem.displayIndex-1 where goalItem.displayIndex>? and goalItem.displayIndex<=?",
            start,end).executeUpdate();
	}

	public void increaseIndex(Integer start, Integer end) {
	    createQuery("update GoalItem goalItem set goalItem.displayIndex=goalItem.displayIndex+1 where goalItem.displayIndex>=? and goalItem.displayIndex<?",
	            start,end).executeUpdate();
	}
	
	public void updateIndex(Integer originalIndex, Integer newIndex) {
	    createQuery("update GoalItem goalItem set goalItem.displayIndex=? where goalItem.displayIndex=?",
	            newIndex,originalIndex).executeUpdate();
	}
	
	public Page<MfgCheckInspectionReport> queryIinspectionReportDetail(Page<MfgCheckInspectionReport> page, JSONObject params) {
		String type = (String)params.get("type");
		String myType = (String)params.get("myType");
		String inspectionPoint="";
		if(type!=null){
			inspectionPoint=(String)params.get("type");
		}else{
			inspectionPoint=(String)params.get("inspectionPoint");
		}
		String inspectionDate=(String)params.get("inspectionDate");
		String searchname=(String)params.get("searchname");
		String materiel=(String)params.get("itemdutyPart_equals");
		String dutySupplier=(String)params.get("dutySupplier");
		String checkBomMaterialType=(String)params.get("checkBomMaterialType");
		String importance=(String)params.get("importance");
		String otherstartDateString=(String)params.get("startDate_ge_date");
		String otherendDateString=(String)params.get("endDate_le_date");
		String destartDateString=(String)params.get("startDate_ge_date");
		String deendDateString=(String)params.get("endDate_le_date");
		if(materiel==null){
			materiel="";
		}
		if(dutySupplier==null){
			dutySupplier="";
		}
		if(checkBomMaterialType==null){
			checkBomMaterialType="";
		}
		if(importance==null){
			importance="";
		}
		String state=(String)params.get("state");
		if(state==null){
			state="limit";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		Date startDate=new Date();
		Date otherstartDate=new Date();
		Date otherendDate=new Date();
		Date destartDate=new Date();
		Date deendDate=new Date();
		try {
			if(inspectionDate!=null){
				startDate = sdf.parse(inspectionDate);
			}
			if(otherstartDateString!=null){
				otherstartDate = sdf.parse(otherstartDateString);
			}
			if(otherendDateString!=null){
				otherendDate = sdf.parse(otherendDateString);
			}
			if(destartDateString!=null){
				destartDate = sdf.parse(destartDateString);
			}
			if(deendDateString!=null){
				deendDate = sdf.parse(deendDateString);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		startCal.setTime(startDate);
		startCal.add(Calendar.DAY_OF_YEAR, 1);
		Date endDate = startCal.getTime();
		if(myType!=null){
			if(!myType.equals("date")){
				endCal.setTime(startDate);
				endCal.add(Calendar.MONTH,1);
				endCal.add(Calendar.DATE,-1);
				endDate = endCal.getTime();
			}
		}
		@SuppressWarnings("unused")
		List<MfgCheckInspectionReport> incomingInspectionActionsReport = new ArrayList<MfgCheckInspectionReport>();
		if(inspectionPoint.equals("检查批数")&&state.equals("one")){
			page= findPage(page,"from MfgCheckInspectionReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? ", ContextUtils.getCompanyId(),startDate,endDate);
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("one")){
			page= findPage(page,"from MfgCheckInspectionReport incomingInspectionActionsReport where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=? ", ContextUtils.getCompanyId(),startDate,endDate,"OK");	
		}
		if(inspectionPoint.equals("检验批数")&&state.equals("all")){
			try {
				page= getAllIncomingInspectionActionsReport(page,startDate,endDate,materiel,dutySupplier,checkBomMaterialType,importance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("all")){
			page= getQualifiedIncomingInspectionActionsReport(page,startDate,endDate,"OK",materiel,dutySupplier,checkBomMaterialType,importance);
		}
		if(inspectionPoint.equals("检查批数")&&state.equals("supplier")){
			page= getAllIncomingInspectionActionsReport(page,otherstartDate,otherendDate,materiel,dutySupplier,"","");	
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("supplier")){
			page= getQualifiedIncomingInspectionActionsReport(page,otherstartDate,otherendDate,"OK",materiel,dutySupplier,"","");
		}
		if(inspectionPoint.equals("检查批数")&&state.equals("material")){
			page= getAllIncomingInspectionActionsReport(page,otherstartDate,otherendDate,materiel,dutySupplier,"","");	
		}
		if(inspectionPoint.equals("合格批数")&&state.equals("material")){
			page= getQualifiedIncomingInspectionActionsReport(page,otherstartDate,otherendDate,"OK",materiel,dutySupplier,"","");
		}
		//不合格
		if(inspectionPoint.equals("materiel")){
			page= getQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,"NG",searchname,"","","");
		}
		if(inspectionPoint.equals("supplier")){
			page= getQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,"NG","",searchname,"","");
		}
		if(inspectionPoint.equals("result")){
			page= getUnQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,"NG",searchname,"");
		}
		if(inspectionPoint.equals("inspector")){
			page= getUnQualifiedIncomingInspectionActionsReport(page,destartDate,deendDate,"NG","",searchname);
		}
		return page;
	}
	
	public Page<MfgCheckInspectionReport> search(Page<MfgCheckInspectionReport> page) {
		String hql = "from MfgCheckInspectionReport i";
	    return searchPageByHql(page,hql);
	}

	public List<MfgCheckInspectionReport> getQualifiedIncomingInspectionActionsReportByResult(Date startDate, Date endDate, String state, String processingResult) {
		String hql = "from MfgCheckInspectionReport i where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=? and processingResult=?";
		return find(hql, ContextUtils.getCompanyId(),startDate,endDate,state,processingResult);
	}
	
	public List<MfgCheckInspectionReport> getQualifiedIncomingInspectionActionsReportByInspector(Date startDate, Date endDate, String state, String inspector) {
		String hql = "from MfgCheckInspectionReport i where companyId=? and inspectionDate>=? and inspectionDate<? and inspectionConclusion=? and inspector=?";
		return find(hql, ContextUtils.getCompanyId(),startDate,endDate,state,inspector);
	}
	public List<MfgCheckInspectionReport> getListByInspectingItemIndicator(
			Date startDate, Date endDate,
			MfgItemIndicator inspectingItemIndicator) {
		String hql = "from MfgCheckInspectionReport a where a.companyId = ? and a.machineNo=? and a.inspectionDate >= ? and a.inspectionDate < ?  and a.workflowInfo.currentActivityName=? and a.workProcedure=? ";
		MfgInspectingIndicator inda = inspectingItemIndicator.getMfgInspectingIndicator();
		endDate.setDate(endDate.getDate()+1);
		String businessUnitName = inda.getBusinessUnitName();
		List<Option> countTypeOptions = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_business_unit_key");
		String companyLike = "";
		for (Option option : countTypeOptions) {
			if(businessUnitName.indexOf(option.getValue())>-1){
				companyLike=option.getValue();
			}
		}
		if(companyLike!=null&&!"".equals(companyLike)){
			hql += " and a.processSection like '%"+companyLike+"%'";
		}
		hql += " order by a.inspectionDate asc";
		return this.find(hql, new Object[]{ContextUtils.getCompanyId(),inspectingItemIndicator.getMfgInspectingIndicator().getModel(),  startDate, endDate,"流程结束",inspectingItemIndicator.getMfgInspectingIndicator().getWorkingProcedure()});
	}
	
	public List<MfgCheckInspectionReport> getListByInspectingItemIndicator(Date startDate, Date endDate) {
		String hql = "from MfgCheckInspectionReport a where a.companyId = ?  and a.workflowInfo.endTime >= ? and a.workflowInfo.endTime < ?  and a.workflowInfo.currentActivityName=?  ";
		endDate.setDate(endDate.getDate()+1);
		hql += " order by a.workflowInfo.endTime asc";
		return this.find(hql, new Object[]{ContextUtils.getCompanyId(), startDate, endDate,"流程结束"});
	}	
}
	
