package com.ambition.iqc.inspectionbase.web;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.TestFrequency;
import com.ambition.iqc.inspectionbase.service.TestFrequencyManager;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;


@Namespace("/iqc/inspection-base/test-frequency")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/inspection-base/test-frequency", type = "redirectAction") })
public class TestFrequencyAction extends BaseAction<TestFrequency> {

	private static final long serialVersionUID = 1L;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;//删除的BOM编号
	private Page<TestFrequency> page;			
	private File myFile;
	
	private TestFrequency testFrequency;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
 	@Autowired
	private TestFrequencyManager testFrequencyManager;
 	
	@Autowired
	private IncomingInspectionActionsReportManager incomingInspectionActionsReportManager;
	public Page<TestFrequency> getPage() {
		return page;
	}

	public void setPage(Page<TestFrequency> page) {
		this.page = page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TestFrequency getTestFrequency() {
		return testFrequency;
	}
	public void setTestFrequency(TestFrequency testFrequency) {
		this.testFrequency = testFrequency;
	}
	public TestFrequency getModel() {
		return testFrequency;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			testFrequency = new TestFrequency();
			testFrequency.setCreatedTime(new Date());
			testFrequency.setCompanyId(ContextUtils.getCompanyId());
			testFrequency.setCreator(ContextUtils.getUserName());
			testFrequency.setLastModifiedTime(new Date());
			testFrequency.setLastModifier(ContextUtils.getUserName());
			testFrequency.setBusinessUnitName(ContextUtils.getSubCompanyName());
			testFrequency.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			testFrequency = testFrequencyManager.getTestFrequency(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@LogInfo(optType="保存",message="测试频率维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			testFrequency.setModifiedTime(new Date());
			testFrequency.setModifier(ContextUtils.getLoginName());
			testFrequency.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", testFrequency.toString());
		}else{
			logUtilDao.debugLog("保存", testFrequency.toString());
		}
		try {
			testFrequencyManager.saveTestFrequency(testFrequency);
			this.renderText(JsonParser.object2Json(testFrequency));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存测试频率维护信息失败  ",e);
		}		
		return null;
	}

	@Action("save-old")
	@LogInfo(optType="同步",message="同步以往数据")
	public String saveOld() throws Exception {
		JSONObject result = new JSONObject();
		@SuppressWarnings("rawtypes")
		List list=incomingInspectionActionsReportManager.listSupplierAndCode();
		for(Object obj : list){			
			Object objs[] = (Object[])obj;			
			String supplierName=null;
			if(objs[0]!=null){
				supplierName=objs[0].toString();
			}
			String supplierCode=null;
			if(objs[1]!=null){
				supplierCode=objs[1].toString();
			}
			String checkBomName=null;
			if(objs[2]!=null){
				checkBomName=objs[2].toString();
			}	
			String checkBomCode=null;
			if(objs[3]!=null){
				checkBomCode=objs[3].toString();
			}	
			if(!testFrequencyManager.isExistTestFrequency(null, supplierCode,checkBomCode)){
				TestFrequency testFrequency=new TestFrequency();
				testFrequency.setCreatedTime(new Date());
				testFrequency.setCompanyId(ContextUtils.getCompanyId());
				testFrequency.setCreator(ContextUtils.getUserName());
				testFrequency.setLastModifiedTime(new Date());
				testFrequency.setLastModifier(ContextUtils.getUserName());
				testFrequency.setBusinessUnitName(ContextUtils.getSubCompanyName());
				testFrequency.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
				testFrequency.setSupplierCode(supplierCode);
				testFrequency.setCheckBomCode(checkBomCode);
				testFrequency.setSupplierName(supplierName);
				testFrequency.setCheckBomName(checkBomName);
				testFrequency.setIsErp("是");
				testFrequencyManager.saveTestFrequency(testFrequency);
				
			}
		}
		try{			
			result.put("message", "同步成功!");
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", e.getMessage());
			e.printStackTrace();
		}
		renderText(result.toString());
		return null;
	}

	
	@Action("delete")
	@LogInfo(optType="删除",message="测试频率维护")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				testFrequencyManager.deleteTestFrequency(deleteIds);				
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}		
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = testFrequencyManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "测试频率维护");
		} catch (Exception e) {
			log.error("查询测试频率维护信息失败  ",e);
		}		
		return null;
	}

	@Action("export")
	@LogInfo(optType="导出",message="测试频率维护")
	public String exports() throws Exception {
		try {
			Page<TestFrequency> page = new Page<TestFrequency>(65535);
			page = testFrequencyManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"测试频率维护"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出测试频率维护信息失败",e);
		}
		return null;
	}
	@Action("check-item-select")
	public String checkItemSelect() throws Exception {
		return SUCCESS;
	}	
	
/*	@Action("tes")
	public String updateSupplier() throws Exception {
		JSONObject result = new JSONObject();
		Calendar cal = Calendar.getInstance();
		String supplierCode=Struts2Utils.getParameter("supplierCode");
		String checkBomCode=Struts2Utils.getParameter("checkBomCode");
		String productStage=Struts2Utils.getParameter("productStage");
		String hisText=Struts2Utils.getParameter("hisText");
		String ordText=Struts2Utils.getParameter("ordText");
		TestFrequency ortTestFrequency=testFrequencyManager.searchFrequency(supplierCode, checkBomCode, productStage, ordText,"ort");
		TestFrequency hsfTestFrequency=testFrequencyManager.searchFrequency(supplierCode, checkBomCode, productStage, hisText,"hsf");	
		Date nowDate=new Date();			
		cal.setTime(nowDate);
		Long nowDateLong=cal.getTimeInMillis();
		if(ortTestFrequency!=null){
			Date  lastOrtTestDate=ortTestFrequency.getLastOrtTestDate();
			String lastOrtReportId=ortTestFrequency.getLastOrtReportId();
			String lastOrtReportNo=ortTestFrequency.getLastOrtReportNo();
			Integer testCycle=ortTestFrequency.getTestCycle();
			if(lastOrtTestDate==null){
				result.put("ortNeed", true);				
				IncomingInspectionActionsReport report=incomingInspectionActionsReportManager.searchReport(supplierCode, checkBomCode, productStage, ordText, "ort");
				if(report!=null){
					result.put("lastOrtReportId", report.getOrdReportId());
					result.put("lastOrtReportNo", report.getOrdReportNo());
					result.put("ortFirst", false);
					ortTestFrequency.setLastOrtReportId(report.getOrdReportId());
					ortTestFrequency.setLastOrtReportNo(report.getOrdReportNo());
					testFrequencyManager.saveTestFrequency(ortTestFrequency);
				}else{
					result.put("ortFirst", true);
				}
			}else{
				cal.setTime(lastOrtTestDate);
				cal.add(Calendar.MONTH,testCycle);
				Long endDate=cal.getTimeInMillis();
				if(nowDateLong<endDate){
					result.put("ortNeed", false);
					result.put("ortFirst", false);
					result.put("lastOrtReportId", lastOrtReportId);
					result.put("lastOrtReportNo", lastOrtReportNo);
				}else{
					result.put("ortNeed", true);
					result.put("ortFirst", false);
				}
			}
		}else{
			result.put("ortNeed", false);
			result.put("ortFirst", false);
			result.put("ortEmpty", true);
		}		
		if(hsfTestFrequency!=null){
			Date  lastHsfTestDate=hsfTestFrequency.getLastHsfTestDate();
			String lastHsfReportId=hsfTestFrequency.getLastHsfReportId();
			String lastHsfReportNo=hsfTestFrequency.getLastHsfReportNo();
			Integer testCycle=hsfTestFrequency.getTestCycle();
			if(lastHsfTestDate==null){
				result.put("hsfNeed", true);
				IncomingInspectionActionsReport report=incomingInspectionActionsReportManager.searchReport(supplierCode, checkBomCode, productStage, hisText, "hsf");
				if(report!=null){
					result.put("lastHsfReportId", report.getHisReportId());
					result.put("lastHsfReportNo", report.getHisReportNo());
					result.put("hsfFirst", false);
					hsfTestFrequency.setLastHsfReportNo(report.getHisReportId());
					hsfTestFrequency.setLastHsfReportNo(report.getHisReportNo());
					testFrequencyManager.saveTestFrequency(hsfTestFrequency);
				}else{
					result.put("hsfFirst", true);
				}
			}else{
				cal.setTime(lastHsfTestDate);
				cal.add(Calendar.MONTH,testCycle);
				Long endDate=cal.getTimeInMillis();
				if(nowDateLong<endDate){
					result.put("hsfNeed", false);
					result.put("hsfFirst", false);
					result.put("lastHsfReportId", lastHsfReportId);
					result.put("lastHsfReportNo", lastHsfReportNo);
				}else{
					result.put("hsfNeed", true);
					result.put("hsfFirst", false);
				}
			}
		}else{
			result.put("hsfNeed", false);
			result.put("hsfFirst", false);
			result.put("hsfEmpty", true);
		}
		renderText(result.toString());
		return null;
	}*/
	
	
	@Action("test-validate")
	public String testValidate() throws Exception {
		JSONObject result = new JSONObject();
		Calendar cal = Calendar.getInstance();
		String supplierCode=Struts2Utils.getParameter("supplierCode");
		String checkBomCode=Struts2Utils.getParameter("checkBomCode");
		TestFrequency testFrequency=testFrequencyManager.searchFrequency(supplierCode, checkBomCode);
		Date nowDate=new Date();			
		cal.setTime(nowDate);
		Long nowDateLong=cal.getTimeInMillis();
		if(testFrequency!=null){
			
			Date  lastOrtTestDate=testFrequency.getLastOrtTestDate();
			String lastOrtReportId=testFrequency.getLastOrtReportId();
			String lastOrtReportNo=testFrequency.getLastOrtReportNo();
			Date  lastHsfTestDate=testFrequency.getLastHsfTestDate();
			String lastHsfReportId=testFrequency.getLastHsfReportId();
			String lastHsfReportNo=testFrequency.getLastHsfReportNo();
			String isHsf=testFrequency.getIsHsf();
			String isOrt=testFrequency.getIsOrt();
			Integer ortTestCycle=testFrequency.getOrtTestCycle();
			Integer hsfTestCycle=testFrequency.getHsfTestCycle();
			if(!"否".equals(isHsf)&&!"否".equals(isOrt)&&ortTestCycle==null&&hsfTestCycle==null){
				result.put("testFrequency", false);
			}else{
				result.put("testFrequency", true);
			}
			if(ortTestCycle!=null&&isOrt!=null&&isOrt.equals("是")){
				result.put("isOrt", true);
				result.put("ortEmpty", false);
				if(lastOrtTestDate==null){									
					IncomingInspectionActionsReport report=incomingInspectionActionsReportManager.searchReport(supplierCode, checkBomCode,"ort");
					if(report!=null){
						testFrequency.setLastOrtTestDate(report.getInspectionDate());
						testFrequency.setLastOrtReportId(report.getOrdReportId());
						testFrequency.setLastOrtReportNo(report.getOrdReportNo());
						cal.setTime(report.getInspectionDate());
						cal.add(Calendar.MONTH,ortTestCycle);
						Long endDate=cal.getTimeInMillis();
						if(nowDateLong<endDate){
							result.put("ortNeed", false);
							result.put("ortFirst", false);
							result.put("lastOrtReportId", report.getOrdReportId());
							result.put("lastOrtReportNo", report.getOrdReportNo());
						}else{
							result.put("ortNeed", true);
							result.put("ortFirst", false);
						}
					}else{
						result.put("ortNeed", true);
						result.put("ortFirst", true);
					}
				}else{
					cal.setTime(lastOrtTestDate);
					cal.add(Calendar.MONTH,ortTestCycle);
					Long endDate=cal.getTimeInMillis();
					if(nowDateLong<endDate){
						result.put("ortNeed", false);
						result.put("ortFirst", false);
						result.put("lastOrtReportId", lastOrtReportId);
						result.put("lastOrtReportNo", lastOrtReportNo);
					}else{
						result.put("ortNeed", true);
						result.put("ortFirst", false);
					}
				}
			}else{
				if(isOrt!=null&&isOrt.equals("否")){
					result.put("isOrt", false);
				}else{
					result.put("isOrt", true);
				}
				result.put("ortEmpty", true);
			}
			if(hsfTestCycle!=null&&isHsf!=null&&isHsf.equals("是")){
				result.put("hsfEmpty", false);
				if(lastHsfTestDate==null){									
					IncomingInspectionActionsReport report=incomingInspectionActionsReportManager.searchReport(supplierCode, checkBomCode,"hsf");
					if(report!=null){
						testFrequency.setLastHsfTestDate(report.getInspectionDate());
						testFrequency.setLastHsfReportId(report.getHisReportId());
						testFrequency.setLastHsfReportNo(report.getHisReportNo());
						cal.setTime(report.getInspectionDate());
						cal.add(Calendar.MONTH,hsfTestCycle);
						Long endDate=cal.getTimeInMillis();
						if(nowDateLong<endDate){
							result.put("hsfNeed", false);
							result.put("hsfFirst", false);
							result.put("lastHsfReportId", report.getHisReportId());
							result.put("lastHsfReportNo", report.getHisReportNo());
						}else{
							result.put("hsfNeed", true);
							result.put("hsfFirst", false);
						}
					}else{
						result.put("hsfNeed", true);
						result.put("hsfFirst", true);
					}
				}else{
					cal.setTime(lastHsfTestDate);
					cal.add(Calendar.MONTH,hsfTestCycle);
					Long endDate=cal.getTimeInMillis();
					if(nowDateLong<endDate){
						result.put("hsfNeed", false);
						result.put("hsfFirst", false);
						result.put("lastHsfReportId", lastHsfReportId);
						result.put("lastHsfReportNo", lastHsfReportNo);
					}else{
						result.put("hsfNeed", true);
						result.put("hsfFirst", false);
					}
				}
			}else{
				if(isHsf!=null&&isHsf.equals("否")){
					result.put("isHsf", false);
				}else{
					result.put("isHsf", true);
				}
				result.put("hsfEmpty", true);
			}	
			testFrequencyManager.saveTestFrequency(testFrequency);
		}else{
			result.put("testFrequency", false);
		}		
		renderText(result.toString());
		return null;
	}		
	
	
}
