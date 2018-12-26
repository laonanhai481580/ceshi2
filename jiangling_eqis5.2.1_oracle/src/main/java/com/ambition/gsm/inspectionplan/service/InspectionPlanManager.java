package com.ambition.gsm.inspectionplan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gsm.base.service.CheckStandardManager;
import com.ambition.gsm.entity.CheckReportDetail;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmForeignReport;
import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.ambition.gsm.entity.GsmMailSetting;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.entity.InspectionWarnUser;
import com.ambition.gsm.equipment.dao.GsmEquipmentDao;
import com.ambition.gsm.equipment.dao.GsmMailSettingsDao;
import com.ambition.gsm.inspectionplan.dao.GsmInnerCheckReportDao;
import com.ambition.gsm.inspectionplan.dao.InspectionPlanDao;
import com.ambition.gsm.inspectionplan.dao.InspectionWarnUserDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 校准计划(SERVICE)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class InspectionPlanManager {
	@Autowired
	private LogUtilDao logUtilDao;	
	@Autowired
	private InspectionPlanDao inspectionPlanDao;
	@Autowired
	private GsmEquipmentDao gsmEquipmentDao;
 	@Autowired
	private UseFileManager useFileManager;
 	@Autowired
	private GsmMailSettingsDao gsmMailSettingsDao;
 	@Autowired
	private InspectionWarnUserDao inspectionWarnUserDao;
 	@Autowired
	private FormCodeGenerated formCodeGenerated;
 	@Autowired
	private GsmInnerCheckReportManager gsmInnerCheckReportManager;
	@Autowired
	private CheckStandardManager checkStandardManager;
	@Autowired
	private GsmInnerCheckReportDao gsmInnerCheckReportDao;
 	/**
 	 * 获取对象
 	 * @param id
 	 * @return
 	 */
	public InspectionPlan getInspectionPlan(Long id){
		return inspectionPlanDao.get(id);
	}

	public List<InspectionPlan> getImportInspectionPlan(Long id){
		return inspectionPlanDao.getImportInspectionPlan(id);
	}
	public List<InspectionPlan> getInspectionPlanByGsm(GsmEquipment gsmEquipment){
		return inspectionPlanDao.getInspectionPlanByGsm(gsmEquipment);
	}
	/**
	 * 保存对象
	 * @param inspectionPlan
	 * @throws Exception
	 */
	public void saveInspectionPlan(InspectionPlan inspectionPlan) throws Exception{
		inspectionPlanDao.save(inspectionPlan);
	}
	 
	/**
	 * 删除对象
	 * @param inspectionPlan
	 */
	public void deleteInspectionPlan(InspectionPlan inspectionPlan){
		inspectionPlanDao.delete(inspectionPlan);
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),inspectionPlan.getAttachment());
	}
	
	/**
	 * 删除对象
	 * @param deleteIds
	 */
	public void deleteInspectionPlan(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			InspectionPlan inspectionPlan = inspectionPlanDao.get(Long.valueOf(id));
			if(inspectionPlan != null){
				GsmEquipment gsmEquipment = inspectionPlan.getGsmEquipment();
				if(gsmEquipment!= null){
				//修改生成校验计划的状态
	//				gsmEquipment.setGenerateState(null); //generateStage=>null
					gsmEquipmentDao.save(gsmEquipment);
				}
				inspectionPlanDao.delete(inspectionPlan);
				useFileManager.useAndCancelUseFiles(inspectionPlan.getAttachment(),null);
			}
		}
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @param inspectionState
	 * @param operator
	 * @return
	 */
	public Page<InspectionPlan> getPage(Page<InspectionPlan>page ,String inspectionState,String operator){
		return inspectionPlanDao.getPage(page, inspectionState, operator);
	}

	/**
	 * 送校
	 * @param ids
	 * @throws Exception
	 */
	public void test(String ids,String bd) throws Exception {
		for(String id : ids.split(",")){
			InspectionPlan inspectionPlan = inspectionPlanDao.get(Long.valueOf(id));
			
			if(!(InspectionPlan.STATE_CHECk_WAIT.equals(inspectionPlan.getInspectionState()))){
				throw new AmbFrameException("校验状态【"+inspectionPlan.getInspectionState()+"】不能重复送校");
			}
			if(!(GsmEquipment.STATE_DEFAULT_INSTOCK.equals(inspectionPlan.getGsmEquipment().getMeasurementState()) || GsmEquipment.STATE_INUSE.equals(inspectionPlan.getGsmEquipment().getMeasurementState()))){
				throw new AmbFrameException("量检具状态【"+inspectionPlan.getGsmEquipment().getMeasurementState()+"】不能送校"); 
			}
			inspectionPlan.setModifiedTime(new Date());
			inspectionPlan.setModifier(ContextUtils.getLoginName());
			inspectionPlan.setModifierName(ContextUtils.getUserName());
			inspectionPlan.setInspectionState(InspectionPlan.STATE_CHECk_IN);
			/*inspectionPlan.setActualInspectionDate(new Date());*/
			inspectionPlanDao.save(inspectionPlan);
			//相对更新量检具校验状态
			GsmEquipment gsmEquipment = inspectionPlan.getGsmEquipment();
			gsmEquipment.setMeasurementState(GsmEquipment.STATE_CHECK);
			//生成校验报告
			if("N".equals(bd)){
				GsmInnerCheckReport gsmInnerCheckReport=new GsmInnerCheckReport();
				gsmInnerCheckReport.setFormNo(formCodeGenerated.generateGsmInnerCheckReportFormNo());
				gsmInnerCheckReport.setCompanyId(ContextUtils.getCompanyId());
				gsmInnerCheckReport.setCreatedTime(new Date());
				gsmInnerCheckReport.setCreator(ContextUtils.getUserName());
				gsmInnerCheckReport.setModifiedTime(new Date());
				gsmInnerCheckReport.setModifier(ContextUtils.getLoginName());
				gsmInnerCheckReport.setModifierName(ContextUtils.getLoginName());
				gsmInnerCheckReport.setDepartmentId(ContextUtils.getDepartmentId());
				gsmInnerCheckReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
				gsmInnerCheckReport.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
				gsmInnerCheckReport.setMeasurementName(gsmEquipment.getEquipmentName());
				gsmInnerCheckReport.setMeasurementSpecification(gsmEquipment.getEquipmentModel());
				gsmInnerCheckReport.setManufacturer(gsmEquipment.getManufacturer());
				gsmInnerCheckReport.setManagementNo(gsmEquipment.getManagerAssets());
				gsmInnerCheckReport.setDepartMent(gsmEquipment.getDevName());
				gsmInnerCheckReport.setFrequency(gsmEquipment.getFrequency());
				gsmInnerCheckReport.setInstallPlace(gsmEquipment.getAddress());
				gsmInnerCheckReport.setDutyMan(gsmEquipment.getDutyMan());//责任人
				gsmInnerCheckReport.setDutyLoginMan(gsmEquipment.getDutyLoginMan());//责任人登入名
				gsmInnerCheckReport.setCopyMan(gsmEquipment.getCopyMan());//抄送人
				gsmInnerCheckReport.setCopyLoginMan(gsmEquipment.getCopyLoginMan());//抄送人登入名
				gsmInnerCheckReport.setPlanId(inspectionPlan.getId().toString());
				gsmInnerCheckReport.setGsmState(inspectionPlan.getGsmState());
	/*			if(gsmEquipment.getEquipmentName()!=null&&gsmEquipment.getEquipmentModel()!=null&&gsmEquipment.getManufacturer()!=null){
					CheckStandard checkStandard=checkStandardManager.getCheckStandard(gsmEquipment.getEquipmentName(),gsmEquipment.getEquipmentModel(),gsmEquipment.getManufacturer());
					
					 List<CheckStandardDetail> checkStandardDetails=checkStandard.getCheckItems();
						for (CheckStandardDetail checkStandardDetail : checkStandardDetails) {
							CheckReportDetail detail  = new CheckReportDetail();
							detail.setItemName(checkStandardDetail.getItemName());
							detail.setAllowableError(checkStandardDetail.getAllowableError());
							detail.setStandardValue(checkStandardDetail.getStandardValue());
							gsmInnerCheckReport.getCheckReportDetails().add(detail);
						}
					}else{
						CheckReportDetail detail  = new CheckReportDetail();
						gsmInnerCheckReport.getCheckReportDetails().add(detail);
					}*/
				gsmInnerCheckReportManager.saveEntity(gsmInnerCheckReport);
			}
			if("Y".equals(bd)){
				GsmForeignReport gsmForeignReport = new GsmForeignReport();
//				gsmForeignReport.setFormNo(formCodeGenerated.generateGsmEntrustNo());
				gsmForeignReport.setCompanyId(ContextUtils.getCompanyId());
				gsmForeignReport.setCreatedTime(new Date());
				gsmForeignReport.setCreator(ContextUtils.getUserName());
				gsmForeignReport.setModifiedTime(new Date());
				gsmForeignReport.setModifier(ContextUtils.getLoginName());
				gsmForeignReport.setModifierName(ContextUtils.getLoginName());
				gsmForeignReport.setDepartmentId(ContextUtils.getDepartmentId());
				gsmForeignReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
				gsmForeignReport.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
				gsmForeignReport.setEquipmentName(gsmEquipment.getEquipmentName());//设备名称
				gsmForeignReport.setEquipmentModel(gsmEquipment.getEquipmentModel());//设备型号
				gsmForeignReport.setManufacturer(gsmEquipment.getManufacturer());//厂商
				gsmForeignReport.setManagementNo(gsmEquipment.getManagerAssets());//管理编号
				gsmForeignReport.setDepartMent(gsmEquipment.getDevName());//使用部门
				gsmForeignReport.setInstallPlace(gsmEquipment.getAddress());//安装地点
				gsmForeignReport.setPlanId(inspectionPlan.getId().toString());
				gsmForeignReport.setGsmState(inspectionPlan.getGsmState());
				gsmMailSettingsDao.getSession().save(gsmForeignReport);
			}
			inspectionPlanDao.getSession().save(gsmEquipment);
		}
	}
	/**
	 * 送校
	 * @param session 
	 * @param ids
	 * @throws Exception
	 */
	public void testZd(InspectionPlan inspectionPlan,String bd) throws Exception {
		if(InspectionPlan.STATE_CHECk_WAIT.equals(inspectionPlan.getInspectionState())){
			inspectionPlan.setModifiedTime(new Date());
			inspectionPlan.setInspectionState(InspectionPlan.STATE_CHECk_IN);
			inspectionPlanDao.save(inspectionPlan);
			//相对更新量检具校验状态
			GsmEquipment gsmEquipment = inspectionPlan.getGsmEquipment();
//			gsmEquipment.setMeasurementState(GsmEquipment.STATE_CHECK);
			
			//生成校验报告
			if("内校".equals(bd)){
				GsmInnerCheckReport gsmInnerCheckReport=new GsmInnerCheckReport();
				gsmInnerCheckReport.setFormNo(formCodeGenerated.generateGsmInnerCheckReportFormNo());
				gsmInnerCheckReport.setCompanyId(gsmEquipment.getCompanyId());
				gsmInnerCheckReport.setCreatedTime(new Date());
				gsmInnerCheckReport.setCreator(gsmEquipment.getCreator());
				gsmInnerCheckReport.setModifiedTime(new Date());
				gsmInnerCheckReport.setModifier(gsmEquipment.getModifier());
				gsmInnerCheckReport.setModifierName(gsmEquipment.getModifierName());
				gsmInnerCheckReport.setDepartmentId(gsmEquipment.getDepartmentId());
				gsmInnerCheckReport.setMeasurementName(gsmEquipment.getEquipmentName());
				gsmInnerCheckReport.setMeasurementSpecification(gsmEquipment.getEquipmentModel());
				gsmInnerCheckReport.setManufacturer(gsmEquipment.getManufacturer());
				gsmInnerCheckReport.setManagementNo(gsmEquipment.getManagerAssets());
				gsmInnerCheckReport.setDepartMent(gsmEquipment.getDevName());
				gsmInnerCheckReport.setFrequency(gsmEquipment.getFrequency());
				gsmInnerCheckReport.setInstallPlace(gsmEquipment.getAddress());
				gsmInnerCheckReport.setDutyMan(gsmEquipment.getDutyMan());//责任人
				gsmInnerCheckReport.setDutyLoginMan(gsmEquipment.getDutyLoginMan());//责任人登入名
				gsmInnerCheckReport.setPlanId(inspectionPlan.getId().toString());
				gsmInnerCheckReport.setGsmState(inspectionPlan.getGsmState());
				gsmInnerCheckReportDao.getSession().save(gsmInnerCheckReport);
			}
			if("外校".equals(bd)){
				GsmForeignReport gsmForeignReport = new GsmForeignReport();
				gsmForeignReport.setCompanyId(gsmEquipment.getCompanyId());
				gsmForeignReport.setCreatedTime(new Date());
				gsmForeignReport.setCreator(gsmEquipment.getCreator());
				gsmForeignReport.setModifiedTime(new Date());
				gsmForeignReport.setModifier(gsmEquipment.getModifier());
				gsmForeignReport.setModifierName(gsmEquipment.getModifierName());
				gsmForeignReport.setDepartmentId(gsmEquipment.getDepartmentId());
				gsmForeignReport.setEquipmentName(gsmEquipment.getEquipmentName());//设备名称
				gsmForeignReport.setEquipmentModel(gsmEquipment.getEquipmentModel());//设备型号
				gsmForeignReport.setManufacturer(gsmEquipment.getManufacturer());//厂商
				gsmForeignReport.setManagementNo(gsmEquipment.getManagerAssets());//管理编号
				gsmForeignReport.setDepartMent(gsmEquipment.getDevName());//使用部门
				gsmForeignReport.setInstallPlace(gsmEquipment.getAddress());//安装地点
				gsmForeignReport.setPlanId(inspectionPlan.getId().toString());
				gsmForeignReport.setGsmState(inspectionPlan.getGsmState());
				gsmMailSettingsDao.getSession().save(gsmForeignReport);
			}
		}
	}
	public GsmMailSettings getGsmMailSettingsByBusinessCode(String businessCode) {
		return gsmMailSettingsDao.getByBusinessCode(businessCode);
	} 
	public Page<InspectionPlan> list(Page<InspectionPlan>page){
		return inspectionPlanDao.list(page);
	}
	/**
	 * 方法名: saveGsmMailSettings 
	 * <p>功能说明：保存检定邮件提醒规则</p>
	 * @return void
	 * @throws
	 */
	public String saveGsmMailSettings(GsmMailSetting gsmMailSetting,String selFlag,Page<InspectionPlan> page){
		
		String resultIds="";
		List<InspectionPlan> inspectionPlans = null;
		if(selFlag!=null && selFlag.equals("select")){
			inspectionPlans = new ArrayList<InspectionPlan>();
			String[] ids = Struts2Utils.getParameter("ids").split(",");
			for(String id:ids){
				if(StringUtils.isNotEmpty(id)){
					inspectionPlans.add(inspectionPlanDao.get(Long.valueOf(id)));
				}
			}
		}else{
			page.setPageNo(1);
			page.setPageSize(Integer.MAX_VALUE);
			page = list(page);
			inspectionPlans = page.getResult();
		}
		
		for(InspectionPlan inspectionPlan :inspectionPlans){
			
			if(Struts2Utils.getParameter("personStrs")!=null && !Struts2Utils.getParameter("personStrs").equals("")){
				String personStrs = Struts2Utils.getParameter("personStrs");
				JSONArray personArray = null;
				if(StringUtils.isNotEmpty(personStrs)){
					personArray = JSONArray.fromObject(personStrs);
					if(personArray.size()!=0){
						//删除历史的预警人员数据
						for(InspectionWarnUser inspectionWarnUser:inspectionPlan.getInspectionWarnUsers()){
							inspectionWarnUserDao.delete(inspectionWarnUser);
						}
						
						inspectionPlan.setInspectionWarnUsers(new ArrayList<InspectionWarnUser>());
						for(int i = 0;i<personArray.size();i++){
							InspectionWarnUser user = new InspectionWarnUser();
							user.setCompanyId(ContextUtils.getCompanyId());
							user.setCreatedTime(new Date());
							user.setCreator(ContextUtils.getUserName());
							user.setModifiedTime(new Date());
							user.setModifier(ContextUtils.getLoginName());
							user.setModifierName(ContextUtils.getUserName());
							user.setName(personArray.getJSONObject(i).get("name").toString());
							user.setUserId(personArray.getJSONObject(i).get("userId").toString());
							user.setInspectionPlan(inspectionPlan);
							inspectionPlan.getInspectionWarnUsers().add(user);
						}
					}else{
						inspectionPlan.getInspectionWarnUsers().clear();
					}
				}
			}
			
			
			inspectionPlan.setGsmMailSetting(gsmMailSetting);
			inspectionPlanDao.save(inspectionPlan);
			resultIds += inspectionPlan.getId().toString()+",";
		}
		
		return resultIds;
	}
	public Page<InspectionPlan> listState(Page<InspectionPlan> page,String state){
		String hql = " from InspectionPlan e where e.gsmState=? and (e.inspectionState='待校验' or e.inspectionState='校验中')";
		return inspectionPlanDao.searchPageByHql(page, hql, state);
	}
	public void gsmHide(String id,String type){
		GsmEquipment gsmEquipment = gsmEquipmentDao.get(Long.valueOf(id));
		if("Y".equals(type)){
			gsmEquipment.setGsmState("N");
		}else{
			gsmEquipment.setGsmState("Y");
		}
		gsmEquipmentDao.save(gsmEquipment);
	}
}