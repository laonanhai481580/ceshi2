package com.ambition.supplier.admittance.web;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.supplier.admittance.service.SupplierInvestigateManager;
import com.ambition.supplier.entity.SupplierInvestigate;
import com.ambition.supplier.entity.SupplierInvestigateContact;
import com.ambition.supplier.entity.SupplierInvestigateEvaluateRecord;
import com.ambition.supplier.entity.SupplierInvestigateStockProduct;
import com.ambition.supplier.entity.SupplierInvestigateTestingDevice;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:供应商调查action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：供应商调查增,删,改,查,流程</p>
 * @author  赵骏
 * @version 1.00 2013-5-1 发布
 */
@Namespace("/supplier/admittance/investigate")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "supplier/admittance/investigate", type = "redirectAction") })
public class SupplierInvestigateAction extends CrudActionSupport<SupplierInvestigate> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Long nodeid;
	// 工作流任务id
	private Long taskId;
	// 点击的按钮
	private TaskProcessingResult taskTransact;
	// 字段权限
	private String fieldPermission;
	
	private JSONObject params;
	
	private WorkflowTask task;

	private SupplierInvestigate supplierInvestigate;
	
	@Autowired
	private SupplierInvestigateManager investigateManager;

	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	private Page<SupplierInvestigate> page;

	private File myFile;
	
	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public TaskProcessingResult getTaskTransact() {
		return taskTransact;
	}

	public void setTaskTransact(TaskProcessingResult taskTransact) {
		this.taskTransact = taskTransact;
	}

	public String getFieldPermission() {
		return fieldPermission;
	}

	public WorkflowTask getTask() {
		return task;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public SupplierInvestigate getSupplierInvestigate() {
		return supplierInvestigate;
	}

	public void setSupplierInvestigate(SupplierInvestigate supplierInvestigate) {
		this.supplierInvestigate = supplierInvestigate;
	}

	public Page<SupplierInvestigate> getPage() {
		return page;
	}

	public void setPage(Page<SupplierInvestigate> page) {
		this.page = page;
	}

	@Override
	public SupplierInvestigate getModel() {
		return supplierInvestigate;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplierInvestigate=new SupplierInvestigate();
			supplierInvestigate.setCompanyId(ContextUtils.getCompanyId());
			supplierInvestigate.setCreatedTime(new Date());
			supplierInvestigate.setCreator(ContextUtils.getUserName());
			supplierInvestigate.setContacts(new ArrayList<SupplierInvestigateContact>());
			supplierInvestigate.setTestingDevices(new ArrayList<SupplierInvestigateTestingDevice>());
			supplierInvestigate.setStockProducts(new ArrayList<SupplierInvestigateStockProduct>());
			supplierInvestigate.setEvaluateRecords(new ArrayList<SupplierInvestigateEvaluateRecord>());
			supplierInvestigate.setLastModifier(ContextUtils.getUserName());
			supplierInvestigate.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierInvestigate.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			supplierInvestigate = investigateManager.getSupplierInvestigate(id);
			supplierInvestigate.getContacts();
			supplierInvestigate.getTestingDevices();
			supplierInvestigate.getStockProducts();
			supplierInvestigate.getEvaluateRecords();
		}
	}
	
	private void initInput(){
		if(supplierInvestigate.getContacts().isEmpty()){
			List<SupplierInvestigateContact> contacts = new ArrayList<SupplierInvestigateContact>();
			contacts.add(new SupplierInvestigateContact());
			ActionContext.getContext().put("investigateContacts", contacts);
		}else{
			ActionContext.getContext().put("investigateContacts", supplierInvestigate.getContacts());
		}
		if(supplierInvestigate.getTestingDevices().isEmpty()){
			List<SupplierInvestigateTestingDevice> testingDevices = new ArrayList<SupplierInvestigateTestingDevice>();
			testingDevices.add(new SupplierInvestigateTestingDevice());
			testingDevices.add(new SupplierInvestigateTestingDevice());
			testingDevices.add(new SupplierInvestigateTestingDevice());
			ActionContext.getContext().put("investigateTestingDevices", testingDevices);
		}else{
			ActionContext.getContext().put("investigateTestingDevices", supplierInvestigate.getTestingDevices());
		}
		if(supplierInvestigate.getStockProducts().isEmpty()){
			List<SupplierInvestigateStockProduct> stockProducts = new ArrayList<SupplierInvestigateStockProduct>();
			stockProducts.add(new SupplierInvestigateStockProduct());
			stockProducts.add(new SupplierInvestigateStockProduct());
			stockProducts.add(new SupplierInvestigateStockProduct());
			ActionContext.getContext().put("investigateStockProducts", stockProducts);
		}else{
			ActionContext.getContext().put("investigateStockProducts", supplierInvestigate.getStockProducts());
		}
		if(supplierInvestigate.getEvaluateRecords().isEmpty()){
			List<SupplierInvestigateEvaluateRecord> evaluateRecords = new ArrayList<SupplierInvestigateEvaluateRecord>();
			String strs[] = new String[]{
					"技术文件完整、正确、统一、清晰，有关场所都得到和使用有效版本",
					"加工设备机型和精度能满足产品质量要求，有维护保养制度",
					"工装、模具制造有验收制度，使用前经过验证，制度满足产品要求",
					"产品原材料定点采购，有质保单和进厂检验记录，材料有标识",
					"半成品在制品摆放整齐",
					"产品、搬运、储存、包装和交付能保证产品不受损坏和锈蚀"};
			for(String str : strs){
				SupplierInvestigateEvaluateRecord evaluateRecord = new SupplierInvestigateEvaluateRecord();
				evaluateRecord.setItemName(str);
				evaluateRecords.add(evaluateRecord);
			}
			ActionContext.getContext().put("investigateEvaluateRecords", evaluateRecords);
		}else{
			ActionContext.getContext().put("investigateEvaluateRecords", supplierInvestigate.getEvaluateRecords());
		}
		//企业类型
		List<Option> enterpriseTypes = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_enterpriseProperty");
		ActionContext.getContext().put("enterpriseTypes",enterpriseTypes);
		//工装模具设计方式
		List<Option> mouldDesigns = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_mould_design");
		ActionContext.getContext().put("mouldDesigns",mouldDesigns);
		//工装模具制造方式
		List<Option> mouldMaufactures = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_mould_maufacture");
		ActionContext.getContext().put("mouldMaufactures",mouldMaufactures);
	}
	@Action("input")
	@Override
	public String input() throws Exception {
		if(id == null){
//			supplierInvestigate.setCode(formCodeGenerated.generateSupplierInvestigateCode());
			supplierInvestigate.setEvaluateMan(ContextUtils.getLoginName());
			supplierInvestigate.setEvaluateDate(new Date());
		}
		initInput();
		return SUCCESS;
	}

	@Action("view")
	public String view() throws Exception {
		if(id != null){
			supplierInvestigate = investigateManager.getSupplierInvestigate(id);
			supplierInvestigate.getContacts();
			supplierInvestigate.getTestingDevices();
			supplierInvestigate.getStockProducts();
			supplierInvestigate.getEvaluateRecords();
		}
		initInput();
		ActionContext.getContext().getValueStack().push(supplierInvestigate);
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			investigateManager.saveInvestigate(supplierInvestigate);
			id = supplierInvestigate.getId();
			addActionMessage("保存成功!");
		} catch (Exception e) {
			log.error("保存供应商调查表失败!",e);
			addActionMessage("保存失败:" + e.getMessage());
			supplierInvestigate.setId(id);
		}
		initInput();
		return "input";
	} 

	public void prepareProcessTask() throws Exception {
		if (id != null) {
			supplierInvestigate = investigateManager.getSupplierInvestigateById(id);
		} else if (taskId != null) {
			supplierInvestigate = investigateManager.getInvestigateByTaskId(taskId);
		}
		// 需我办理的任务
		if (taskId == null) {
			task = investigateManager.getMyTask(supplierInvestigate,
					ContextUtils.getLoginName());
			taskId = task.getId();
		} else {
			task = investigateManager.getWorkflowTask(taskId);
		}
	}

	// 任务办理界面
	@Action("process-task")
	public String processTask() throws Exception {
		if(supplierInvestigate != null){
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(supplierInvestigate);
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			if(taskId != null){
				investigateManager.deleteInvestigateByTaskId(taskId);
			}
			supplierInvestigate = new SupplierInvestigate();
		}
		initInput();
		return SUCCESS;
	}

	public void prepareCompleteTask() throws Exception {
		prepareProcessTask();
	}

	// 完成任务
	@Action("complete-task")
	public String completeTask() throws Exception {
		prepareProcessTask();
		investigateManager.completeTask(supplierInvestigate, taskId, taskTransact);
		ActionContext.getContext().put("message","操作成功！");
		addActionMessage("操作成功！");
		//历史意见
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(supplierInvestigate);
		Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		initInput();
		return "process-task";
	}
	public void prepareSubmitProcess() throws Exception {
		prepareModel();
	}

	// 第一次提交
	@Action("submit-process")
	public String submitProcess() throws Exception {
		investigateManager.submitProcess(supplierInvestigate);
		//得到task控制页面上显示取回按钮
		task = investigateManager.getWorkflowTask(supplierInvestigate
				.getWorkflowInfo().getFirstTaskId());
		//控制页面上的字段都不能编辑
		fieldPermission = investigateManager.getFieldPermission(supplierInvestigate
				.getWorkflowInfo().getFirstTaskId());
		ActionContext.getContext().put("message","提交成功！");
		addActionMessage("提交成功！");
		initInput();
		return "input";
	}

	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			investigateManager.deleteInvestigates(deleteIds);
		} catch (Exception e) {
			log.error(e);
			renderText("删除失败:" + e.getMessage());
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = investigateManager.search(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			log.error("查询供应商调查表信息失败",e);
		}
		return null;
	}
	public void prepareHistory() throws Exception{
		supplierInvestigate=investigateManager.getSupplierInvestigate(id);
	}
	@Action("history")
	public String history() throws Exception{
		return "history";
	}
	@Action("create-investigate")
	public String createInvestigate() throws Exception {
		try {
			if(id != null){
				supplierInvestigate = investigateManager.getSupplierInvestigate(id);
				investigateManager.createQuestionReport(supplierInvestigate);
			}
		} catch (Exception e) {
			log.error("生成供应商调查报告失败",e);
			renderText("生成失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("import-form")
	public String importForm() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-investigate")
	public String importInvestigate() throws Exception {
		try {
			investigateManager.importInvestigate(myFile);
			addActionMessage("导入成功!");
		} catch (Exception e) {
			log.error("导入供应商调查表失败",e);
			addActionMessage("导入失败:" + e.getMessage() + "!");
		}finally{
			if(myFile != null){
				myFile.delete();
			}
		}
		return "import-form";
	}
	
	@Action("export")
	public String export() throws Exception {
		Page<SupplierInvestigate> page = new Page<SupplierInvestigate>(Integer.MAX_VALUE);
		page = investigateManager.search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_INVESTIGATE"),"供应商调查表"));
		return null;
	}
	
	@Action("download-template")
	public String downloadTemplate() throws Exception {
		String fileName = "供应商调查表.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/supplier-investigate.xls");
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			response.getOutputStream().write(bytes);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return null;
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	
}
