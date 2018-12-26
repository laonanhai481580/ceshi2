package com.ambition.iqc.inspectionreport.web;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.checkinspection.web.MfgCheckInspectionReportAction;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.InspectingIndicator;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.inspectionbase.service.InspectingIndicatorManager;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionReportManager;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.opensymphony.xwork2.ActionContext;

/**    
 * 检验报告ACTION
 * @authorBy wlongfeng
 *
 */
@Namespace("/iqc/inspection-report")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/inspection-report", type = "redirectAction") })
public class IncomingInspectionActionsReportAction extends CrudActionSupport<IncomingInspectionActionsReport> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String packingFir;
	private String packingSec;
	private Boolean canImprove=false;//
	private IncomingInspectionActionsReport incomingInspectionActionsReport;
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	private JSONObject params;
	private File myFile;
	@Autowired
	private IncomingInspectionActionsReportManager incomingInspectionActionsReportManager;
	@Autowired
	private IncomingInspectionReportManager reportManager;
	@Autowired
	private InspectingIndicatorManager indicatorManager;
	private String businessUnitsCode;
	private Page<IncomingInspectionActionsReport> page;
	private List<IncomingInspectionActionsReport> list;
	private List<Option> listOption;
	@Autowired
	private LogUtilDao logUtilDao;
	private ProductBom productBom;	
	@Autowired
	private ProductBomManager productBomManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private InspectingIndicatorManager inspectingIndicatorManager;

	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	//组装列表
	private String colNames;//列名
	private String colModel;//列信息
	private String colCode;//列代号
	private String code;//物料代码
	private Integer totalYear;
	private Integer startMonth;
	private Integer endMonth;
	private String changeview;//新建
	private Boolean isAudit=false;//
	private Boolean isLastAudit=false;//
	public String getBusinessUnitsCode() {
		return businessUnitsCode;
	}

	public void setBusinessUnitsCode(String businessUnitsCode) {
		this.businessUnitsCode = businessUnitsCode;
	}

	public List<IncomingInspectionActionsReport> getList() {
		return list;
	}

	public void setList(List<IncomingInspectionActionsReport> list) {
		this.list = list;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public Boolean getCanImprove() {
		return canImprove;
	}

	public void setCanImprove(Boolean canImprove) {
		this.canImprove = canImprove;
	}

	public Boolean getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Boolean isAudit) {
		this.isAudit = isAudit;
	}

	public Boolean getIsLastAudit() {
		return isLastAudit;
	}

	public void setIsLastAudit(Boolean isLastAudit) {
		this.isLastAudit = isLastAudit;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public String getColNames() {
		return colNames;
	}

	public void setColNames(String colNames) {
		this.colNames = colNames;
	}

	public String getColModel() {
		return colModel;
	}

	public void setColModel(String colModel) {
		this.colModel = colModel;
	}

	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	public List<Option> getListOption() {
		return listOption;
	}

	public void setListOption(List<Option> listOption) {
		this.listOption = listOption;
	}

	public void setPage(Page<IncomingInspectionActionsReport> page){
		this.page = page;
	}
	
	public Page<IncomingInspectionActionsReport> getPage() {
		return page;
	}
	
	public IncomingInspectionActionsReport getModel() {
		return incomingInspectionActionsReport;
	}
	
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public ProductBom getProductBom() {
		return productBom;
	}

	public void setProductBom(ProductBom productBom) {
		this.productBom = productBom;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getTotalYear() {
		return totalYear;
	}

	public void setTotalYear(Integer totalYear) {
		this.totalYear = totalYear;
	}

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}
	
	public String getChangeview() {
		return changeview;
	}

	public void setChangeview(String changeview) {
		this.changeview = changeview;
	}

	public String getPackingFir() {
		return packingFir;
	}

	public void setPackingFir(String packingFir) {
		packingFir=",";
		String [] packingFirArray=Struts2Utils.getRequest().getParameterValues("packingFir");
		for(String fir:packingFirArray){
			packingFir+=fir+",";
		}
		this.packingFir = packingFir.substring(1, packingFir.length()-1);
	}

	public String getPackingSec() {
		return packingSec;
	}

	public void setPackingSec(String packingSec) {
		this.packingFir=Struts2Utils.getParameter("packingSec");
		this.packingSec = packingSec;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			incomingInspectionActionsReport=new IncomingInspectionActionsReport();
			incomingInspectionActionsReport.setCreatedTime(new Date());
			incomingInspectionActionsReport.setCompanyId(ContextUtils.getCompanyId());
			incomingInspectionActionsReport.setCreator(ContextUtils.getUserName());
			incomingInspectionActionsReport.setLastModifiedTime(new Date());
			incomingInspectionActionsReport.setLastModifier(ContextUtils.getUserName());
			incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateIqcCode());
			incomingInspectionActionsReport.setInspectionDate(new Date());
			incomingInspectionActionsReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			incomingInspectionActionsReport.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(id);
			if(incomingInspectionActionsReport.getInspectionConclusion().equals("NG")&&incomingInspectionActionsReport.getInspectionState()!=IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT&&incomingInspectionActionsReport.getInspectionState()!=IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT){
				canImprove=true;
			}
			if(incomingInspectionActionsReport.getInspectionDate()==null){
			   incomingInspectionActionsReport.setInspectionDate(new Date());
			}
		}
	}
	/**
	  * 方法名: 渲染处理结果
	 */
	private void renderProcessResult(){
		List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_processingResult");
		JSONArray array = new JSONArray();
		for(Option option : options){
			JSONObject obj = new JSONObject();
			obj.put("name", option.getName());
			obj.put("value",option.getValue());
			array.add(obj);
		}
		ActionContext.getContext().put("processingResults", array);
	}
	@Action("list")
	@Override
	@LogInfo(optType="页面",message="进货检验台帐")
	public String list() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		renderProcessResult();
		return SUCCESS;
	}
	private String disposeSpecialCharacter(String json)
    {
		json = json.replaceAll("\\\\","\\\\\\\\");
		return PageUtils.disposeSpecialCharacter(json);
    }
	@Action("list-datas")
	@LogInfo(optType="查询",message="进货检验数据")
	public String getListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.list(page,null);
		} catch (Exception e) {
			log.error("获取进货检验数据失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	@Action("list-hid")
	@LogInfo(optType="查询",message="进货检验数据")
	public String getListHid() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.listHid(page);
		} catch (Exception e) {
			log.error("获取进货检验数据失败!",e);
		}
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("un-check")
	@LogInfo(optType="页面",message="检验未完成台帐")
	public String unCheckList() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		renderProcessResult();
		return SUCCESS;
	}
	
	@Action("un-check-list-datas")
	@LogInfo(optType="查询",message="检验未完成数据")
	public String getUnCheckListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.listUnCheck(page,null);
		} catch (Exception e) {
			log.error("获取检验未完成数据失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	@Action("recheck-list")
	@LogInfo(optType="页面",message="重检台帐")
	public String reCheckList() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		renderProcessResult();
		return SUCCESS;
	}
	
	@Action("recheck-list-datas")
	@LogInfo(optType="查询",message="重检数据")
	public String getReCheckListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.listReCheck(page,null);
		} catch (Exception e) {
			log.error("获取重检数据失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	@Action("checked")
	@LogInfo(optType="页面",message="检验完成台帐")
	public String checkedList() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		renderProcessResult();
		return SUCCESS;
	}
	
	@Action("checked-list-datas")
	@LogInfo(optType="查询",message="检验完成数据")
	public String getCheckedListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.listChecked(page,null);
		} catch (Exception e) {
			log.error("获取检验完成数据失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	@Action("wait-audit")
	@LogInfo(optType="页面",message="检验待审核台帐")
	public String waitAuditList() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		renderProcessResult();
		return SUCCESS;
	}
	
	/**
	  * 方法名: 上级带审核台账
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("last-wait-audit")
	@LogInfo(optType="页面",message="检验待审核检验台帐")
	public String lastWaitAuditList() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		renderProcessResult();
		return SUCCESS;
	}
	
	@Action("wait-audit-list-datas")
	@LogInfo(optType="查询",message="检验待审核数据")
	public String getWaitAuditListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.listWaitAudit(page,null);
		} catch (Exception e) {
			log.error("获取检验待审核数据失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	/**
	  * 方法名: 上级带审核台账数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("wait-last-audit-list-datas")
	@LogInfo(optType="查询",message="上级待审核检验数据")
	public String getWaitLastAuditListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.listLstWaitAudit(page,null);
		} catch (Exception e) {
			log.error("获取检验待审核数据失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	
	@Action("complete")
	@LogInfo(optType="页面",message="检验已完成台帐")
	public String completeList() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		renderProcessResult();
		return SUCCESS;
	}
	
	@Action("complete-list-datas")
	@LogInfo(optType="查询",message="检验已完成数据")
	public String getCompleteListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.listComplete(page,null);
		}catch(Exception e){
			log.error("获取检验已完成数据失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	
	@Action("get-inspection-no")
	public String getInspectionNO() throws Exception {
//		renderText(formCodeGenerated.generateIncomingInspectionReportCode());
		return null;
	}
	
	/**
	 * 显示检验标准的图片
	 * @return
	 * @throws Exception
	 */
	@Action("show-indicator-picture")
	public String showIndicatorPicture() throws Exception{
		String picturePath;
		String checkBomCode=Struts2Utils.getParameter("mid");
		InspectingIndicator indicator=inspectingIndicatorManager.getAllInspectingIndicatorsByProductBom(checkBomCode);
		if(indicator.getAttachmentFiles()!=null){
			picturePath=indicator.getAttachmentFiles();
		}else{
			picturePath="";
		}
		Struts2Utils.getRequest().setAttribute("path",picturePath);
		Struts2Utils.getRequest().setAttribute("name",checkBomCode);
		return "showPicture";
	}
	/**
	  * 方法名:输入页面初始化 
	  * <p>功能说明：</p>
	 */
	private void renderInput(){
		List<Option> inspectionLevels = sampleCodeLetterManager.getInspectionLevelOptions(SampleCodeLetter.GB_TYPE);
		inspectionLevels.addAll(sampleCodeLetterManager.getInspectionLevelOptions(SampleCodeLetter.MIL_TYPE));
		inspectionLevels.addAll(sampleCodeLetterManager.getInspectionLevelOptions(SampleCodeLetter.MIL1051_TYPE));
		Struts2Utils.getRequest().setAttribute("inspectionLevels",inspectionLevels);
//		businessUnitsCode = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits").get(0).getValue();
//		incomingInspectionActionsReport.setBusinessUnitCode(businessUnitsCode);
		ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("processSections"));
		ActionContext.getContext().put("packing1",ApiFactory.getSettingService().getOptionsByGroupCode("packing1"));
		ActionContext.getContext().put("sampleSchemeTypes",ApiFactory.getSettingService().getOptionsByGroupCode("sample_scheme_types"));
		ActionContext.getContext().put("packing2",ApiFactory.getSettingService().getOptionsByGroupCode("packing2"));
		ActionContext.getContext().put("packing3",ApiFactory.getSettingService().getOptionsByGroupCode("packing3"));
		ActionContext.getContext().put("iqc_okorng",ApiFactory.getSettingService().getOptionsByGroupCode("iqc_okorng"));
		ActionContext.getContext().put("equipment_numbers",ApiFactory.getSettingService().getOptionsByGroupCode("equipment_numbers"));
		ActionContext.getContext().put("iqc_product_stage",ApiFactory.getSettingService().getOptionsByGroupCode("iqc_product_stage"));
		ActionContext.getContext().put("isOks",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_isok"));
		if(IncomingInspectionActionsReport.FORM_TYPE_BACK.equals(incomingInspectionActionsReport.getFormType())){
			ActionContext.getContext().put("processingResults",ApiFactory.getSettingService().getOptionsByGroupCode("iqc_backProcessingResult"));
		}else{
			ActionContext.getContext().put("processingResults",new ArrayList<Option>());
		}
		InspectingIndicator indicator = indicatorManager.getInspectingIndicator(incomingInspectionActionsReport.getCheckBomCode());
		if(indicator != null){
			//检验标准的附件
			ActionContext.getContext().put("indicatorAttachmentFiles",indicator.getAttachmentFiles());
		}
	}
	
	@Action("input")
	@Override
	@LogInfo(optType="页面",message="进货检验单页面")
	public String input() throws Exception {
		try {
			if(incomingInspectionActionsReport.getId() == null){
				incomingInspectionActionsReport.setInspectionDate(new Date());
				incomingInspectionActionsReport.setEnterDate(incomingInspectionActionsReport.getInspectionDate());
//				incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateIncomingInspectionReportCode());
			}
			String batchNo=Struts2Utils.getParameter("batchNo");
			if(incomingInspectionActionsReportManager.getIncomingInspectionActionsReportListByBatchNo(batchNo).size()==0&&batchNo!=null){
				Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
				addActionMessage("批次号不存在！");
			}else{
				List<CheckItem> checkItems = null;
				if(batchNo!=null){
					incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReportByBatchNo(batchNo);	
					checkItems = incomingInspectionActionsReport.getCheckItems();
					ActionContext.getContext().put("incomingInspectionActionsReport", incomingInspectionActionsReport);
				}
				if(id!=null){
					incomingInspectionActionsReport = incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(id);
					checkItems = incomingInspectionActionsReport.getCheckItems();
					if(incomingInspectionActionsReport.getInspectionDate()==null){
						incomingInspectionActionsReport.setInspectionDate(new Date());
					}
					if(checkItems.size()==0){
						Map<String,Object> resultMap = incomingInspectionActionsReportManager.getCheckItems(incomingInspectionActionsReport.getProcessSection(),incomingInspectionActionsReport.getSupplierCode(), incomingInspectionActionsReport.getCheckBomCode(), incomingInspectionActionsReport.getStockAmount(),incomingInspectionActionsReport.getInspectionDate(),null);
						checkItems = (List<CheckItem>)resultMap.get("checkItems");
						if(checkItems.size()!=0){
							Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
						}else{
							Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
						}
						for(String key : resultMap.keySet()){
							if(!"checkItems".equals(key)){
								ActionContext.getContext().put(key,resultMap.get(key));
							}
						}
					}
//					checkItems = incomingInspectionActionsReport.getCheckItems();
					ActionContext.getContext().put("incomingInspectionActionsReport", incomingInspectionActionsReport);
				}else{
					checkItems = new ArrayList<CheckItem>();
				}
				/*Collections.sort(checkItems,new Comparator<CheckItem>() {
					@Override
					public int compare(CheckItem o1, CheckItem o2) {
						if(o1.getId()<o2.getId()){
							return Integer.valueOf(String.valueOf(o1.getId()-o2.getId()));
						}else{
						    return Integer.valueOf(String.valueOf(o2.getId()-o1.getId()));
						}
					}
				});*/
				//获取物料级别
				Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
			}
			ActionContext.getContext().put("materialType",incomingInspectionActionsReport.getCheckBomMaterialType());
			ActionContext.getContext().put("standardVersion",incomingInspectionActionsReport.getStandardVersion());
			ActionContext.getContext().put("changeView",changeview);
			//来源任务ID
			String sourceTaskId = Struts2Utils.getParameter("sourceTaskId");
			if(sourceTaskId != null && CommonUtil1.isInteger(sourceTaskId)){
				ActionContext.getContext().put("sourceTaskId",sourceTaskId);
			}
			renderAudit();
			//初始化页面
			renderInput();
			//处理结果
			renderProcessResult();
		} catch (Exception e) {
			log.error("创建进货检验表单失败!",e);
		}
		return SUCCESS;
	}
	private void renderAudit(){
		if(incomingInspectionActionsReport.getInspectionState().equals("待审核")){
			String name=incomingInspectionActionsReport.getAuditLoginMan();
			String loginName=ContextUtils.getLoginName();
			if(name!=null&&name.equals(loginName)){
				isAudit=true;
			}
		}
		if(incomingInspectionActionsReport.getInspectionState().equals("上级待审核")){
			String name=incomingInspectionActionsReport.getLastStateLoginMan();
			String loginName=ContextUtils.getLoginName();
			if(name!=null&&name.equals(loginName)){
				isLastAudit=true;
			}
		}
	}
	
	/**
	  * 方法名: 自动带出客户代码
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("set-customer-code")
	public String setCustomerCode() throws Exception {
		JSONObject obj = new JSONObject();
		Date inspectionDate = DateUtil.parseDateTime(Struts2Utils.getParameter("inspectionDate"));
		String checkBomCode=Struts2Utils.getParameter("checkBomCode");
		String businessUnitName=Struts2Utils.getParameter("processSection");
		String supplierCode=Struts2Utils.getParameter("supplierCode");
		String targetRule=incomingInspectionActionsReportManager.serachTargetRule(supplierCode, checkBomCode, businessUnitName,inspectionDate);
		obj.put("targetRule", targetRule);
		this.renderText(obj.toString());
		return null;	
	}	
	
	
	@SuppressWarnings("unchecked")
	@Action("check-items")
	public String getCheckItems() throws Exception {
		try {
			Date inspectionDate = DateUtil.parseDateTime(Struts2Utils.getParameter("inspectionDate"));
			String processSection = Struts2Utils.getParameter("processSection");
			String businessUnitCode = Struts2Utils.getParameter("businessUnitCode");
			String supplierCode = Struts2Utils.getParameter("supplierCode");
			String checkBomCode = Struts2Utils.getParameter("checkBomCode");
			String checkBomMaterialType = Struts2Utils.getParameter("checkBomMaterialType");
			Double stockAmount = StringUtils.isNotEmpty(Struts2Utils.getParameter("stockAmount"))?Double.valueOf(Struts2Utils.getParameter("stockAmount")):null;
			List<JSONObject> checkItemArrays = MfgCheckInspectionReportAction.getRequestCheckItems();
			Map<String,Object> resultMap = incomingInspectionActionsReportManager.getCheckItems(processSection,supplierCode, checkBomCode, stockAmount,inspectionDate,checkItemArrays);
			List<CheckItem> checkItems = (List<CheckItem>)resultMap.get("checkItems");
			if(checkItems.size()!=0){
				Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
			}else{
				Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
			}
			for(String key : resultMap.keySet()){
				if(!"checkItems".equals(key)){
					ActionContext.getContext().put(key,resultMap.get(key));
				}
			}
			ActionContext.getContext().put("equipment_numbers",ApiFactory.getSettingService().getOptionsByGroupCode("equipment_numbers"));
//			ActionContext.getContext().put("is3C", resultMap.get("is3C"));
//			ActionContext.getContext().put("isStandard", resultMap.get("isStandard"));
//			ActionContext.getContext().put("iskeyComponent", resultMap.get("iskeyComponent"));
//			ActionContext.getContext().put("materialType", resultMap.get("materialType"));
//			ActionContext.getContext().put("workingProcedure", resultMap.get("workingProcedure"));
			//检验标准的附件
			ActionContext.getContext().put("indicatorAttachmentFiles",resultMap.get("indicatorAttachmentFiles"));
			Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
		} catch (Exception e) {
			log.error("获取检验单项目失败!",e);
		}
		return SUCCESS;
	}
	
	/**
	  * 方法名: 待检验的项目
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("wait-checked-items")
	public String getWaitCheckedItems() throws Exception{
		List<CheckItem> checkItems=null;
//		if(checkItems==null){
			String businessUnitCode = Struts2Utils.getParameter("businessUnitCode");
			String supplierCode = Struts2Utils.getParameter("supplierCode");
			String checkBomCode = Struts2Utils.getParameter("checkBomCode");
			Double stockAmount = StringUtils.isNotEmpty(Struts2Utils.getParameter("stockAmount"))?Double.valueOf(Struts2Utils.getParameter("stockAmount")):null;
			Date inspectionDate = DateUtil.parseDateTime(Struts2Utils.getParameter("inspectionDate"));
			
			List<JSONObject> checkItemArrays = MfgCheckInspectionReportAction.getRequestCheckItems();
			Map<String,Object> resultMap = incomingInspectionActionsReportManager.getCheckItems(businessUnitCode,supplierCode, checkBomCode, stockAmount,inspectionDate,checkItemArrays);
			checkItems = (List<CheckItem>)resultMap.get("checkItems");
//			Collections.sort(checkItems,new Comparator<CheckItem>() {
//				@Override
//				public int compare(CheckItem o1, CheckItem o2) {
//					if(o1.getCreatedTime().getTime()<o2.getCreatedTime().getTime()){
//						return Integer.valueOf(String.valueOf(o1.getCreatedTime().getTime()-o2.getCreatedTime().getTime()));
//					}else{
//					    return Integer.valueOf(String.valueOf(o2.getCreatedTime().getTime()-o1.getCreatedTime().getTime()));
//					}
//				}
//			});
//		}
		if(checkItems.size()!=0){
			Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
		}else{
			Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
		}
		return SUCCESS;
	}
	
	@Action("view-info")
	@LogInfo(optType="页面",message="查看进货检验单页面")
	public String view() throws Exception {
		try {
			String batchNo=Struts2Utils.getParameter("batchNo");
			String sourceNo=Struts2Utils.getParameter("sourceNo");
			if(incomingInspectionActionsReportManager.getIncomingInspectionActionsReportListByBatchNo(batchNo).size()==0&&batchNo!=null){
				Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
				addActionMessage("批次号不存在！");
			}else{
				List<CheckItem> checkItems = null;
				if(id!=null){
					incomingInspectionActionsReport = incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(id);	
					checkItems = incomingInspectionActionsReport.getCheckItems();
					ActionContext.getContext().put("incomingInspectionActionsReport", incomingInspectionActionsReport);
				}else{
					checkItems = new ArrayList<CheckItem>();
				}
				if(batchNo!=null){
					incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReportByBatchNo(batchNo);	
					checkItems = incomingInspectionActionsReport.getCheckItems();
					ActionContext.getContext().put("incomingInspectionActionsReport", incomingInspectionActionsReport);
				}
				if(sourceNo!=null&&!sourceNo.equals("undefined")){
					incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReportByInspectionNo(sourceNo);	
					if(incomingInspectionActionsReport.getId()==null){
						ActionContext.getContext().put("error",true);
					}else{
						ActionContext.getContext().put("error",false);
					}
					checkItems = incomingInspectionActionsReport.getCheckItems();
					ActionContext.getContext().put("incomingInspectionActionsReport", incomingInspectionActionsReport);
				}
				if(checkItems!=null && checkItems.size()!=0){
					Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
				}else{
					Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
				}
				List<Option> inspectionLevels = sampleCodeLetterManager.getInspectionLevelOptions(SampleCodeLetter.GB_TYPE);
				inspectionLevels.addAll(sampleCodeLetterManager.getInspectionLevelOptions(SampleCodeLetter.MIL_TYPE));
				Struts2Utils.getRequest().setAttribute("inspectionLevels",inspectionLevels);
			}
		} catch (Exception e) {
			log.error("查看进货检详情失败!",e);
		}
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	@LogInfo(optType="页面",message="保存进货检验单")
	public String save() throws Exception {
		//转换时间
		incomingInspectionActionsReport.setInspectionDate(DateUtil.parseDateTime(Struts2Utils.getParameter("inspectionDate")));
		incomingInspectionActionsReport.getProcessSection();
		List<JSONObject> checkItems = MfgCheckInspectionReportAction.getRequestCheckItems();
		if(id != null){
			incomingInspectionActionsReport.setLastModifiedTime(new Date());
			incomingInspectionActionsReport.setLastModifier(ContextUtils.getUserName());
			incomingInspectionActionsReport.setInspector(ContextUtils.getUserName());
			incomingInspectionActionsReport.setInspectorLoginName(ContextUtils.getLoginName());
		}else{
			incomingInspectionActionsReport.setCheckItems(new ArrayList<CheckItem>());
		}
		try {
			incomingInspectionActionsReportManager.saveIncomingInspectionActionsReport(incomingInspectionActionsReport,checkItems);
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText(JsonParser.getRowValue(incomingInspectionActionsReport));
				return null;
			}else{
				addActionMessage("保存成功!");
				//初始化页面
				renderInput();
				//处理结果
				renderProcessResult();
				renderAudit();
				return "input";
			}
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("保存进货检验信息失败!",e);
			}
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText("{'error':true,'message':'保存失败:"+e.getMessage()+"'}");
				return null;
			}else{
				addActionMessage("保存失败," + e.getMessage());
				ActionContext.getContext().put("failflag", "failed");
				ActionContext.getContext().put("failmessage", "保存失败," + e.getMessage());
				if(id != null){
					incomingInspectionActionsReport = incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(id);
//					for(CheckItem checkItem : incomingInspectionActionsReport.getCheckItems()){
//						checkItem.getCheckItemName();
//					}
				}
				if(checkItems != null){
					if(incomingInspectionActionsReport.getId()==null){
						incomingInspectionActionsReport.setCheckItems(new ArrayList<CheckItem>());
					}else{
						incomingInspectionActionsReport.getCheckItems().clear();
					}
					int cr=0,ma=0,mi=0;
					for(JSONObject json : checkItems){
						CheckItem checkItem = new CheckItem();
						checkItem.setCompanyId(ContextUtils.getCompanyId());
						checkItem.setCreatedTime(new Date());
						checkItem.setCreator(ContextUtils.getUserName());
						checkItem.setLastModifiedTime(new Date());
						checkItem.setLastModifier(ContextUtils.getUserName());
						for(Object key : json.keySet()){
							String value = json.getString(key.toString());
							setProperty(checkItem, key.toString(),value);
						}
						checkItem.setIiar(incomingInspectionActionsReport);
						incomingInspectionActionsReport.getCheckItems().add(checkItem);	
					}
				}
				
				
				Struts2Utils.getRequest().setAttribute("checkItems",incomingInspectionActionsReport.getCheckItems());
				incomingInspectionActionsReport.setId(id);
//				if(incomingInspectionActionsReport.getCheckItems() != null){
//					for(CheckItem checkItem : incomingInspectionActionsReport.getCheckItems()){
//						checkItem.getCheckItemName();
//					}
//				}
				//渲染输入页面
				renderInput();
				//处理结果
				renderProcessResult();
				return "input";
			}
		}
		
	}
	
	@Action("delete")
	@Override
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			String deleteNos = incomingInspectionActionsReportManager.deleteIncomingInspectionActionsReport(deleteIds);
			//日志消息,方便跟踪删除记录
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除进货检验台帐,请检单号和分录号为【" + deleteNos + "】!");
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("删除进货检验台帐失败!",e);
			}
			renderText(e.getMessage());
		}
		return null;
	}
	
	@Action("re-check")
	@LogInfo(optType="修改",message="重置进货检验报告处理状态")
	public String reCheck() throws Exception {
		JSONObject result = new JSONObject();
		try {
			String recheckText=Struts2Utils.getParameter("recheckText");
			String id=Struts2Utils.getParameter("id");
			if(id!=null){
					incomingInspectionActionsReportManager.reCheck(id,recheckText);
				}
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("重置进货检验报告状态失败!",e);
			}
			result.put("error",true);
			result.put("message",e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	/**
	 * 原料供应商合格率页面
	 */
	@Action("inspection-chart") 
	@LogInfo(optType="页面",message="原料供应商合格率页面")
	public String chart() throws Exception {
		List<Option> productType = productBomManager.getModelSpecificationToOptions();
		ActionContext.getContext().put("productTypes", productType);
		List<Option> importance = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_importance");
		ActionContext.getContext().put("importances", importance);
		List<Option> materialType = ApiFactory.getSettingService().getOptionsByGroupCode("supply_materialType");
		ActionContext.getContext().put("materialTypes", materialType);
		return SUCCESS;
	}
	
	/**
	 * 原料供应商合格率数据
	 */
	@Action("inspection-chart-datas")
	@LogInfo(optType="数据",message="原料供应商合格率数据")
	public String getchartDatas() throws Exception {
		params = convertJsonObject(params);
		String materiel="", dutySupplier="",checkBomMaterialType="",importance="";
		if(params.get("myType") != null && "date".equals(params.get("myType").toString())){
			//按检验日期统计
			if(params.containsKey("itemdutyPartCode_equals")){
				materiel=params.get("itemdutyPartCode_equals").toString();
			}
			if(params.containsKey("dutySupplierCode")){
				dutySupplier=params.get("dutySupplierCode").toString();
			}
			if(params.containsKey("checkBomMaterialType")){
				checkBomMaterialType=params.get("checkBomMaterialType").toString();
			}
			if(params.containsKey("importance")){
				importance=params.get("importance").toString();
			}
			Date startDate=new Date();
			Date endDate=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(params.get("endDate_le_date")!=null){
				startDate=sdf.parse(params.get("startDate_ge_date").toString()+" 00:00:00");
				endDate=sdf.parse(params.get("endDate_le_date").toString()+" 23:59:59");
			}
			//获取天数
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			startCal.setTime(startDate);
			endCal.setTime(endDate);
			Map<String,Object> result = new HashMap<String, Object>();
			if(dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", "合格批数推移图");
			}else if(!dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", dutySupplier+"_"+"合格批数推移图");
			}else if(dutySupplier.isEmpty()&&!materiel.isEmpty()){
				result.put("title", materiel+"_"+"合格批数推移图");
			}else{
				result.put("title", dutySupplier+"_"+materiel+"_"+"合格批数推移图");
			}
			result.put("ppmtitle", materiel+"_"+"合格批数推移图");
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			result.put("subtitle","(" + sdf.format(startDate) + "-" + sdf.format(endDate) + ")");
			List<Integer> categories = new ArrayList<Integer>();
			result.put("categories", categories);
			result.put("yAxisTitle1","批<br/>次<br/>数");
			result.put("yAxisTitle2","合<br/>格<br/>率");

			List<Integer> data = new ArrayList<Integer>();
			//检验批数
			Map<String,Object> series1 = new HashMap<String, Object>();
			series1.put("name", "检验批数");
			List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
			
			//合格批数
			Map<String,Object> series2 = new HashMap<String, Object>();
			series2.put("name", "合格批数");
			List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
			
			//批次合格率
			Map<String,Object> series3 = new HashMap<String, Object>();
			series3.put("name", "合格率");
			List<Double> data3 = new ArrayList<Double>();
			
			List<IncomingInspectionActionsReport> incomingInspectionActionsReport = new ArrayList<IncomingInspectionActionsReport>();
			double jianyan = 0, hege = 0;
			Calendar startCal1 = Calendar.getInstance();
			startCal1.setTime(startDate);
			while(startCal1.getTimeInMillis() <= endDate.getTime()){
				startCal1.set(Calendar.HOUR_OF_DAY, 0);
				startCal1.set(Calendar.MINUTE, 0);
				startCal1.set(Calendar.SECOND, 0);
				Date startdate = startCal1.getTime();
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("date", sdf.format(startdate));
				//横坐标天数的参数
				categories.add(startCal1.get(Calendar.DAY_OF_MONTH));
				startCal1.add(Calendar.DAY_OF_YEAR, 1);
				startCal1.set(Calendar.HOUR_OF_DAY, 0);
				startCal1.set(Calendar.MINUTE, 0);
				startCal1.set(Calendar.SECOND, 0);
				Date enddate = startCal1.getTime();
				
				incomingInspectionActionsReport = incomingInspectionActionsReportManager.listAll(startdate,enddate,materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","检验批数");
				map.put("y", incomingInspectionActionsReport.size());
				data1.add(map);
				data.add(incomingInspectionActionsReport.size());
				jianyan = incomingInspectionActionsReport.size();
				
				data = new ArrayList<Integer>();
				map = new HashMap<String, Object>();
				map.put("date", sdf.format(startdate));
				incomingInspectionActionsReport = incomingInspectionActionsReportManager.listQualified(startdate, enddate,"OK",materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","合格批数");
				map.put("in","合格批数");
				map.put("y", incomingInspectionActionsReport.size());
				data2.add(map);
				data.add(incomingInspectionActionsReport.size());
				hege = incomingInspectionActionsReport.size();
				
				double rate = 0.00;
				if(jianyan != 0){
					rate = (hege/jianyan)*100;
				}
				data3.add(new Double(new DecimalFormat( "0.00" ).format(rate)));
			}
			ActionContext.getContext().put("categorieslist", categories);
			result.put("tableHeaderList", categories);
			result.put("firstColName","日期");
			series1.put("data",data1);
			result.put("series1", series1);
			
			series2.put("data",data2);
			result.put("series2", series2);
			
			series3.put("data",data3);
			result.put("series3", series3);
			
			result.put("max", 100);
			this.renderText(JSONObject.fromObject(result).toString());
		}else if(params.get("myType") != null && "month".equals(params.get("myType").toString())){
			//按检验月份统计
			if(params.containsKey("itemdutyPartCode_equals")){
				 materiel=params.get("itemdutyPartCode_equals").toString();
			}
			if(params.containsKey("dutySupplierCode")){
				 dutySupplier=params.get("dutySupplierCode").toString();
			}
			if(params.containsKey("checkBomMaterialType")){
				checkBomMaterialType=params.get("checkBomMaterialType").toString();
			}
			if(params.containsKey("importance")){
				importance=params.get("importance").toString();
			}
			
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			int startyear = 0, startmonth = 0, endyear = 0, endmonth = 0, endMonthNumber = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(params.get("endDate_le_date")!=null && params.get("startDate_ge_date")!=null){
				startyear =  Integer.valueOf(params.get("startDate_ge_date").toString().substring(0, 4));
				startmonth = Integer.valueOf(params.get("startDate_ge_date").toString().substring(5, 7));
				endyear = Integer.valueOf(params.get("endDate_le_date").toString().substring(0, 4));
				endmonth = Integer.valueOf(params.get("endDate_le_date").toString().substring(5, 7));
			}
			
			startCal.set(Calendar.YEAR,startyear);
			startCal.set(Calendar.MONTH,startmonth-1);
			startCal.set(Calendar.DATE,1);
			
			endCal.set(Calendar.YEAR,endyear);
			endCal.set(Calendar.MONTH,endmonth);
			endCal.set(Calendar.DATE,1);
			endCal.add(Calendar.DATE,-1);
			endMonthNumber = incomingInspectionActionsReportManager.getYearAndMonthNumber(endCal);
			
			Map<String,Object> result = new HashMap<String, Object>();
			if(dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", "合格批数推移图");
			}else if(!dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", dutySupplier+"_"+"合格批数推移图");
			}else if(dutySupplier.isEmpty()&&!materiel.isEmpty()){
				result.put("title", materiel+"_"+"合格批数推移图");
			}else{
				result.put("title", dutySupplier+"_"+materiel+"_"+"合格批数推移图");
			}
			result.put("ppmtitle", materiel+"_"+"合格批数推移图");
			result.put("subtitle","(" + startyear + "-" + startmonth + " 至 " + endyear + "-" + endmonth + ")");
			List<Integer> categories = new ArrayList<Integer>();
			result.put("categories", categories);
			result.put("yAxisTitle1","批<br/>次<br/>数");
			result.put("yAxisTitle2","合<br/>格<br/>率");

			List<Integer> data = new ArrayList<Integer>();
			//检验批数
			Map<String,Object> series1 = new HashMap<String, Object>();
			series1.put("name", "检验批数");
			List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
			
			//合格批数
			Map<String,Object> series2 = new HashMap<String, Object>();
			series2.put("name", "合格批数");
			List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
			
			//批次合格率
			Map<String,Object> series3 = new HashMap<String, Object>();
			series3.put("name", "合格率");
			List<Double> data3 = new ArrayList<Double>();
			
			List<IncomingInspectionActionsReport> incomingInspectionActionsReport = new ArrayList<IncomingInspectionActionsReport>();
			double total =0, qualified = 0;
			while(incomingInspectionActionsReportManager.getYearAndMonthNumber(startCal) <= endMonthNumber){
				//横坐标月份的参数
				categories.add(startCal.get(Calendar.MONTH)+1);
				startCal.set(Calendar.HOUR_OF_DAY,0);
				startCal.set(Calendar.MINUTE,0);
				startCal.set(Calendar.SECOND,0);
				Date startdate1 = startCal.getTime();
				
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(startdate1);
				endCalendar.add(Calendar.MONTH, 1);
				endCalendar.add(Calendar.DATE, -1);
				endCalendar.set(Calendar.HOUR_OF_DAY,23);
				endCalendar.set(Calendar.MINUTE,59);
				endCalendar.set(Calendar.SECOND,59);
				Date enddate1 = endCalendar.getTime();
				
				Map<String,Object> map = new HashMap<String, Object>();
				sdf = new SimpleDateFormat("yyyy-MM");
				map.put("date", sdf.format(startdate1));
				incomingInspectionActionsReport = incomingInspectionActionsReportManager.listAll(startdate1,enddate1,materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","检验批数");
				map.put("y", incomingInspectionActionsReport.size());
				data1.add(map);
				data.add(incomingInspectionActionsReport.size());
				total = incomingInspectionActionsReport.size();
				
				data = new ArrayList<Integer>();
				map = new HashMap<String, Object>();
				map.put("date", sdf.format(startdate1));
				incomingInspectionActionsReport = incomingInspectionActionsReportManager.listQualified(startdate1, enddate1,"OK",materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","合格批数");
				map.put("in","合格批数");
				map.put("y", incomingInspectionActionsReport.size());
				data2.add(map);
				data.add(incomingInspectionActionsReport.size());
				qualified = incomingInspectionActionsReport.size();
				
				double rate=0.00;
				if(total!=0){
					rate=(qualified/total)*100;
				}
				data3.add(new Double(new DecimalFormat( "0.00" ).format(rate)));
				
				startCal.add(Calendar.MONTH, 1);
			}
			ActionContext.getContext().put("categorieslist", categories);
			result.put("tableHeaderList", categories);
			result.put("firstColName","月份");
			
			series1.put("data",data1);
			result.put("series1", series1);
			
			series2.put("data",data2);
			result.put("series2", series2);
			
			series3.put("data",data3);
			result.put("series3", series3);
			
			result.put("max", 100);
			this.renderText(JSONObject.fromObject(result).toString());
		}else{
			//按周统计
			if(params.containsKey("itemdutyPartCode_equals")){
				 materiel=params.get("itemdutyPartCode_equals").toString();
			}
			if(params.containsKey("dutySupplierCode")){
				 dutySupplier=params.get("dutySupplierCode").toString();
			}
			if(params.containsKey("checkBomMaterialType")){
				checkBomMaterialType=params.get("checkBomMaterialType").toString();
			}
			if(params.containsKey("importance")){
				importance=params.get("importance").toString();
			}
			
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			int startyear = 0, startweek = 0, endyear = 0, endweek = 0, endWeekNumber = 0;
			startyear = Integer.valueOf(params.get("year_ge").toString());
			endyear = Integer.valueOf(params.get("year_le").toString());
			if(params.get("week_ge")!=null){
				startweek = Integer.valueOf(params.get("week_ge").toString());
			}
			if(params.get("week_le")!=null){
				endweek = Integer.valueOf(params.get("week_le").toString());
			}
			
			startCal.set(Calendar.YEAR,startyear);     
			startCal.set(Calendar.WEEK_OF_YEAR,startweek);
			startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			startCal.setFirstDayOfWeek(Calendar.MONDAY);
			
			endCal.set(Calendar.YEAR,endyear);
			endCal.set(Calendar.WEEK_OF_YEAR,endweek);
			endCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			endCal.setFirstDayOfWeek(Calendar.MONDAY);   
			
			endWeekNumber = incomingInspectionActionsReportManager.getYearAndWeekNumber(endCal);
			
			Map<String,Object> result = new HashMap<String, Object>();
			if(dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", "合格批数推移图");
			}else if(!dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", dutySupplier+"_"+"合格批数推移图");
			}else if(dutySupplier.isEmpty()&&!materiel.isEmpty()){
				result.put("title", materiel+"_"+"合格批数推移图");
			}else{
				result.put("title", dutySupplier+"_"+materiel+"_"+"合格批数推移图");
			}
			result.put("ppmtitle", materiel+"_"+"合格批数推移图");
			result.put("subtitle","(" + startyear +"年"+ startweek + "周 - " + endyear + "年"+ endweek +"周)");
			List<Integer> categories = new ArrayList<Integer>();
			result.put("categories", categories);
			result.put("yAxisTitle1","批<br/>次<br/>数");
			result.put("yAxisTitle2","合<br/>格<br/>率");

			List<Integer> data = new ArrayList<Integer>();
			//检验批数
			Map<String,Object> series1 = new HashMap<String, Object>();
			series1.put("name", "检验批数");
			List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
			
			//合格批数
			Map<String,Object> series2 = new HashMap<String, Object>();
			series2.put("name", "合格批数");
			List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
			
			//批次合格率
			Map<String,Object> series3 = new HashMap<String, Object>();
			series3.put("name", "合格率");
			List<Double> data3 = new ArrayList<Double>();
			
			List<IncomingInspectionActionsReport> incomingInspectionActionsReport = new ArrayList<IncomingInspectionActionsReport>();
			double total =0, qualified = 0;
			while(incomingInspectionActionsReportManager.getYearAndWeekNumber(startCal) <= endWeekNumber){
				//横坐标周期的参数
				String name = String.valueOf(incomingInspectionActionsReportManager.getYearAndWeekNumber(startCal));
				categories.add(Integer.valueOf(name.substring(4)));
				startCal.set(Calendar.HOUR_OF_DAY,0);
				startCal.set(Calendar.MINUTE,0);
				startCal.set(Calendar.SECOND,0);
				Date startdate1 = startCal.getTime();
				
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(startdate1);
				endCalendar.add(Calendar.DATE, 6);
				endCalendar.set(Calendar.HOUR_OF_DAY,23);
				endCalendar.set(Calendar.MINUTE,59);
				endCalendar.set(Calendar.SECOND,59);
				Date enddate1 = endCalendar.getTime();
				
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("date", name);
				incomingInspectionActionsReport = incomingInspectionActionsReportManager.listAll(startdate1,enddate1,materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","检验批数");
				map.put("y", incomingInspectionActionsReport.size());
				data1.add(map);
				data.add(incomingInspectionActionsReport.size());
				total = incomingInspectionActionsReport.size();
				
				data = new ArrayList<Integer>();
				map = new HashMap<String, Object>();
				map.put("date", name);
				incomingInspectionActionsReport = incomingInspectionActionsReportManager.listQualified(startdate1, enddate1,"OK",materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","合格批数");
				map.put("in","合格批数");
				map.put("y", incomingInspectionActionsReport.size());
				data2.add(map);
				data.add(incomingInspectionActionsReport.size());
				qualified = incomingInspectionActionsReport.size();
				
				double rate=0.00;
				if(total!=0){
					rate=(qualified/total)*100;
				}
				data3.add(new Double(new DecimalFormat( "0.00" ).format(rate)));
				
				startCal.add(Calendar.DATE, 7);
			}
			ActionContext.getContext().put("categorieslist", categories);
			result.put("tableHeaderList", categories);
			result.put("firstColName","周期");
			
			series1.put("data",data1);
			result.put("series1", series1);
			
			series2.put("data",data2);
			result.put("series2", series2);
			
			series3.put("data",data3);
			result.put("series3", series3);
			
			result.put("max", 100);
			this.renderText(JSONObject.fromObject(result).toString());
		}
		return null;	
	}
	
	/**
	 * 原料供应商合格率图表链接页面
	 */
	@Action("inspection-report-detail")
	@LogInfo(optType="页面",message="原料供应商合格率详情页面")
	public String getchartDatasDetail() throws Exception {
		params= convertJsonObject(params);
		return SUCCESS;
	}
	
	/**
	 * 原料供应商合格率图表和进货检验一次合格率链接页面数据
	 */
	@Action("inspection-report-detail-datas")
	@LogInfo(optType="数据",message="原料供应商合格率图表和进货检验一次合格率数据")
	public String getchartDatasDetailDatas() throws Exception {
		String searchStrs = Struts2Utils.getParameter("searchStrs");
		if(StringUtils.isNotEmpty(searchStrs)){
			params = JSONObject.fromObject(searchStrs);
		}
		page = incomingInspectionActionsReportManager.queryIinspectionReportDetail(page, params);
		String result = PageUtils.pageToJson(page);
		this.renderText(JSONObject.fromObject(result).toString());
		return null;
	}
	
	/**
	 * 进货检验一次合格率图表页面
	 */
	@Action("inspection-one-chart")
	@LogInfo(optType="页面",message="进货检验一次合格率图表页面")
	public String onechart() throws Exception {
		return SUCCESS;
	}

	
	
	/**
	 * 进货检验一次合格率图表页面数据
	 */
	@Action("inspection-one-chart-datas")
	@LogInfo(optType="数据",message="进货检验一次合格率图表数据")
	public String getonechartDatas() throws Exception {
		try {
			params = convertJsonObject(params);
			if(params.get("myType") != null && params.get("myType").toString().equals("date")){
				this.renderText(JSONObject.fromObject(incomingInspectionActionsReportManager.getOneChartDatas(params)).toString());
			}else if(params.get("myType") != null && params.get("myType").toString().equals("month")){
				this.renderText(JSONObject.fromObject(incomingInspectionActionsReportManager.getOneChartDatasByMonths(params)).toString());
			}else{
				this.renderText(JSONObject.fromObject(incomingInspectionActionsReportManager.getOneChartDatasByWeeks(params)).toString());
			}
		} catch (Exception e) {
			log.error("查询出错!",e);
		}
		return null;	
	}
	
	
	/**
	  * 方法名: 验证项目状态
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("validate-item-status")
	public String getItemStatus(){
		String items=Struts2Utils.getParameter("items");
		String  itemNameStr="";
		String reportId=Struts2Utils.getParameter("reportId");
		id=reportId==""?null:Long.valueOf(reportId);
		if(id!=null){
			incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(id);
			if(incomingInspectionActionsReport!=null&&incomingInspectionActionsReport.getCheckItems()!=null){
				for(CheckItem item:incomingInspectionActionsReport.getCheckItems()){
					if(item.getCheckItemName().indexOf(items)>=0){
						itemNameStr+=item.getCheckItemName()+",";
					}
				}
			}
		}
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("itemNameStr", itemNameStr);
		renderText(jsonObj.toString());
		return null;
	}
	
	/**
	 * 进货检验不合格页面
	 */
	@Action("unlist")
	@LogInfo(optType="页面",message="进货检验不合格台帐")
	public String unlist() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_processingResult");
		JSONArray array = new JSONArray();
		for(Option o : listOptions){
			JSONObject obj = new JSONObject();
			obj.put("name",o.getName());
			obj.put("value",o.getValue());
			array.add(obj);
		}
		ActionContext.getContext().put("processingResults",array);
		return SUCCESS;
	}
	/**
	 * 进货检验合格页面
	 */
	@Action("oklist")
	@LogInfo(optType="页面",message="进货检验合格台帐")
	public String oklist() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
		listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_processingResult");
		JSONArray array = new JSONArray();
		for(Option o : listOptions){
			JSONObject obj = new JSONObject();
			obj.put("name",o.getName());
			obj.put("value",o.getValue());
			array.add(obj);
		}
		ActionContext.getContext().put("processingResults",array);
		return SUCCESS;
	}
	/**
	 * 进货检验不合格数据
	 */
	@Action("unlist-datas")
	@LogInfo(optType="数据",message="进货检验不合格台帐数据")
	public String getUnListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.unlist(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	/**
	 * 进货检验合格数据
	 */
	@Action("oklist-datas")
	@LogInfo(optType="数据",message="进货检验不合格台帐数据")
	public String getOkListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.oklist(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
		return null;
	}
	/**
	  * 方法名: 快速检索页面
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("select-report-datas")
	@LogInfo(optType="数据",message="快速检索台帐数据")
	public String getSelectReportListDatas() throws Exception {
		try {
			page = incomingInspectionActionsReportManager.quickSelectList(page);
		}catch (Exception e) {
			log.error("快速检索台帐查询失败!",e);
		}
		String json = PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code"));
		this.renderText(disposeSpecialCharacter(json));
//		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	/**
	 * 进货检验检验报告导出
	 */
	@Action("export")
	@LogInfo(optType="导出",message="进货检验管理：检验报告-导出检验报告台帐")
	public String export() throws Exception {
		System.out.println("可以了");
		try {
			Page<IncomingInspectionActionsReport> page = new Page<IncomingInspectionActionsReport>(100000);
			String checkItem=Struts2Utils.getParameter("checkItem");
			String state = Struts2Utils.getParameter("state");
			if("waitAudit".equals(state)){
				page = incomingInspectionActionsReportManager.listWaitAudit(page,checkItem);
			}else if("unCheck".equals(state)){
				page = incomingInspectionActionsReportManager.listUnCheck(page,checkItem);
			}else if("complete".equals(state)){
				page = incomingInspectionActionsReportManager.listComplete(page,checkItem);
			}else if("checked".equals(state)){
				page = incomingInspectionActionsReportManager.listChecked(page,checkItem);
			}else if("reCheck".equals(state)){
				page = incomingInspectionActionsReportManager.listReCheck(page,checkItem);
			}else{
				page = incomingInspectionActionsReportManager.list(page,checkItem);
			}
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_IIAR"),"进货检验报告台账"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导出进货检验台帐失败!",e);
		}
		return null;
	}
	@Action("export-item-is-null")
	@LogInfo(optType="导出",message="进货检验管理：检验报告-导出检验报告台帐")	
	public String exportItemIsNull() throws Exception {
		Page<IncomingInspectionActionsReport> page = new Page<IncomingInspectionActionsReport>(100000);
		String startStr=Struts2Utils.getParameter("startStr");
		String endStr=Struts2Utils.getParameter("endStr");
		try {
			page = incomingInspectionActionsReportManager.itemIsNullList(page,startStr,endStr);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_IIAR"),"进货检验报告台账"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导出进货检验台帐失败!",e);
		}
		return null;
	}
	
	
	/**
	 * 进货检验不合格检验报告导出
	 */
	@Action("unexport")
	@LogInfo(optType="导出",message="进货检验不合格品台帐导出")
	public String unexport() throws Exception {
		Page<IncomingInspectionActionsReport> page = new Page<IncomingInspectionActionsReport>(100000);
		page = incomingInspectionActionsReportManager.unlist(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_UNIIR"),"检验报告不合格台帐"));
		logUtilDao.debugLog("导出", "进货检验管理：检验报告-导出检验报告不合格台帐");
		return null;
	}
	/**
	 * 进货检验合格检验报告导出
	 */
	@Action("okexport")
	@LogInfo(optType="导出",message="进货检验合格品台帐导出")
	public String okexport() throws Exception {
		Page<IncomingInspectionActionsReport> page = new Page<IncomingInspectionActionsReport>(100000);
		page = incomingInspectionActionsReportManager.oklist(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_UNIIR"),"检验报告合格台帐"));
		logUtilDao.debugLog("导出", "进货检验管理：检验报告-导出检验报告合格台帐");
		return null;
	}	
	/**
	 * 进货检验检验报告导入页面
	 */
	@Action("imports")
	@LogInfo(optType="导入",message="进货检验报告导入页面")
	public String imports() throws Exception {
		return "imports";
	}
	
	/**
	 * 进货检验检验报告导入数据处理
	 */
	@Action("import-datas")
	@LogInfo(optType="导入",message="进货检验报告导入")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(incomingInspectionActionsReportManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	
	/**
	 * 供应商合格率对比页面
	 */
	@Action("supplier-rate-chart")
	@LogInfo(optType="图表",message="供应商合格率对比")
	public String supplierRateChart() throws Exception {
		List<Option> productType = productBomManager.getModelSpecificationToOptions();
		ActionContext.getContext().put("productTypes", productType);
		return SUCCESS;
	}
	
	/**
	 * 供应商合格率对比数据
	 */
	@Action("supplier-rate-chart-datas")
	@LogInfo(optType="数据",message="供应商合格率对比")
	public String supplierRateChartDatas() throws Exception {
		params = convertJsonObject(params);
		try {
			renderText(JSONObject.fromObject(reportManager.supplierRateChartDatas(params)).toString());
		} catch (Exception e) {
			log.error("供应商合格率对比查询失败!",e);
		}
		return null;	
	}
	
	/**
	 * 物料合格率对比页面
	 */
	@Action("material-rate-chart")
	@LogInfo(optType="页面",message="物料合格率对比")
	public String materialRateChart() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 物料合格率对比数据
	 */
	@Action("material-rate-chart-datas")
	@LogInfo(optType="数据",message="物料合格率对比")
	public String materialRateChartDatas() throws Exception {
		params = convertJsonObject(params);
		try {
	this.renderText(JSONObject.fromObject(reportManager.materialRateChartDatas(params)).toString());
		} catch (Exception e) {
			log.error("物料合格率对比分析失败!",e);
		}
		return null;	
	}
	
	/**
	 * 检验项目不良分析
	 */
	@Action("check-type-chart")
	@LogInfo(optType="页面",message="检验项目不良分析")
	public String checkTypeChart() throws Exception {
		List<Option> productType = productBomManager.getModelSpecificationToOptions();
		ActionContext.getContext().put("productTypes", productType);
		return SUCCESS;
	}
	
	/**
	 * 检验项目不良分析数据
	 */
	@Action("check-type-chart-datas")
	@LogInfo(optType="数据",message="检验项目不良分析数据")
	public String getCheckTypeChartDatas() throws Exception {
		params = convertJsonObject(params);
		try {
			this.renderText(JSONObject.fromObject(reportManager.getCheckTypeChartDatas(params)).toString());
		} catch (Exception e) {
			log.error("检验不良分析失败!",e);
		}
		return null;	
	}
	
	@Action("inspection-no-list")
	public String inspectionNoList(){
		return SUCCESS;
	}
	/**
	  * 方法名: 手动更新同步状态
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("update-request-check-state")
	@LogInfo(optType="修改",message="更新进货检验报告同步状态")
	public String updateRequestCheckState() throws Exception {
		JSONObject obj = new JSONObject();
//		String result = incomingStateInterationService.updateRequestCheckState(id);
		String result = "";
		if("".equals(result)){
			obj.put("message","操作成功!");
		}else{
			obj.put("error",true);
			obj.put("message","同步失败," + result);
		}
		this.renderText(obj.toString());
		return null;	
	}
	
	/**
	  * 方法名: 审核
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("audit")
	@LogInfo(optType="修改",message="审核进货检验报告")
	public String audit() throws Exception {
		JSONObject obj = new JSONObject();
		try {
			incomingInspectionActionsReportManager.auditIncomingInspectionActionsReport(id,
					Struts2Utils.getParameter("auditText"),
					Struts2Utils.getParameter("lastStateText"),
					null,
					null,
					null);
			obj.put("message","");
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("审核进货检验台帐失败!",e);
			}
			obj.put("error",true);
			obj.put("message",e.getMessage());
		}
		//检查是否从待办任务处发起的
		try {
			String sourceTaskId = Struts2Utils.getParameter("sourceTaskId");
			if(CommonUtil1.isInteger(sourceTaskId)){
				ApiFactory.getTaskService().completeWorkflowTask(Long.valueOf(sourceTaskId),TaskProcessingResult.READED);
			}
		} catch (Exception e) {
			obj.put("error",true);
			obj.put("message","审核成功,但更新任务状态失败!");
			log.error("审核进货检验台帐后完成已阅失败!",e);
		}
		this.renderText(obj.toString());
		return null;	
	}
	
	@Action("download-excel-format")
	public String downloadExcelFormat(){
		try{
			String fileName = "进货检验报告删除模板.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/iqc-inspection-delete-excel.xls");
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			response.getOutputStream().write(bytes);
			inputStream.close();
		}catch(Exception e){
			log.error("下载进货检验报告删除模板附件失败!",e);
		}
		return null;
	}
	
	@Action("import-form")
	public String importForm() throws Exception {
		return SUCCESS;
	}
	
	@Action("imports-report-excel")
	public String importsReportExcel() throws Exception {
		try {
			if(myFile != null){
				renderHtml(incomingInspectionActionsReportManager.importReportExcel(myFile));
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	@Action("processing-result")
	@LogInfo(optType="修改",message="处理不合格品处理结果")
	public String processingResult() throws Exception {
		JSONObject result = new JSONObject();
		try {
			incomingInspectionActionsReportManager.processingResult(deleteIds);
		} catch (Exception e) {
			result.put("error",true);
			result.put("message",e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	/**
	 * 方法名: 
	 * <p>功能说明：统计检验及时完成率页面</p>
	 * 创建人:wuxuming 日期： 2015-3-7 version 1.0
	 * @param 
	 * @return
	 */
	@Action("statistical-finish-report-rate")
	public String dudateStatisticalFinishReportRate(){
		return SUCCESS;
	}
	
	/**
	 * 方法名: 
	 * <p>功能说明：统计检验及时完成率数据</p>
	 * 创建人:wuxuming 日期： 2015-3-7 version 1.0
	 * @param 
	 * @return
	 */
	@Action("statistical-finish-report-rate-datas")
	public String dudateStatisticalFinishReportRateDatas(){
		try{
			params = convertJsonObject(params);
			incomingInspectionActionsReportManager.dudateStatisticalFinishReportRateDatas(params);
		}catch(Exception e){
			JSONObject obj = new JSONObject();
			obj.put("error",true);
			obj.put("message","审核成功,但更新任务状态失败!");
			log.error("审核进货检验台帐后完成已阅失败!",e);
		}
		return SUCCESS;
	}
	
	/**
	  * 方法名: 导出报告
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("export-report")
	public String exportReport() throws Exception{
		try {
			incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(id);
			incomingInspectionActionsReportManager.exportReport(incomingInspectionActionsReport);
		} catch (Exception e) {
			e.printStackTrace();
			renderText("导出失败:" + e.getMessage());
		}
		return null;
	}
	/**
	  * 方法名: 测试接口
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("test-jiekou")
	public String testJiekou() throws Exception{
		String shuliang=Struts2Utils.getParameter("shuliang");
		incomingInspectionActionsReportManager.testSaveErpResult(shuliang);
		return null;
	} 
	
	/**
	  * 方法名: 测试接口1
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("test-jiekou1")
	public String testJiekou1() throws Exception{
		String shuliang=Struts2Utils.getParameter("shuliang");
//		Test.testMethod(shuliang);
//		incomingInspectionActionsReportManager.testSaveErpResult1(shuliang);
		return null;
	} 
	
	/**
	  * 方法名: 审核
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("re-write")
	public String reWrite() throws Exception {
		JSONObject obj = new JSONObject();
		try {
			List<IncomingInspectionActionsReport> reports = incomingInspectionActionsReportManager.queryByDate(Struts2Utils.getParameter("updateDate"));
			log.error("size:" + reports.size());
			int i = 0;
			for(IncomingInspectionActionsReport report : reports){
				log.error("write:" + ++i + "," + report.getInspectionNo());
				incomingInspectionActionsReportManager.updateWriteState(report);
			}
			obj.put("message","");
		} catch (Exception e) {
			log.error("检验台帐失败!",e);
			obj.put("error",true);
			obj.put("message",e.getMessage());
		}
		this.renderText(obj.toString());
		return null;	
	}
	@Action("input-spc")
	public String inputSpc() throws Exception {
		JSONObject result = new JSONObject();
	    String spcSampleId = Struts2Utils.getParameter("spcSampleId");
	    try{
	    	 String reportId = incomingInspectionActionsReportManager.getReportIdBySpcSmapleId(spcSampleId);
	    	 result.put("error", false);
	    	 result.put("reportId", reportId);
	    }catch(Exception e){
	    	result.put("error", true);
	    	result.put("message", "找不到对应表单");
	    }
	    renderText(result.toString());
        return null;
	}
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	private void setProperty(Object obj, String property, Object value) throws Exception {
		String fieldName = property,customType = null;
		if(property.indexOf("_")>0){
			String[] strs = property.split("_");
			fieldName = strs[0];
			customType = strs[1];
		}
		Class<?> type = PropertyUtils.getPropertyType(obj, fieldName);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, fieldName, null);
			} else {
				if("timestamp".equals(customType)){
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value.toString()));
				}else if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Boolean.valueOf(value.toString()));
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	@Action("hiddenState")
	public String hiddenState(){
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		incomingInspectionActionsReportManager.hiddenState(eid,type);
		return null;
	}
}
