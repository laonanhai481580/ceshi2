package com.ambition.iqc.inspectionreport.web;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.SampleTransferRecord;
import com.ambition.iqc.inspectionreport.service.SampleTransferRecordManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;



/**
 * 检验状态变更
 * @author 赵骏
 *
 */
@Namespace("/iqc/inspection-report/sample-transfer")
@ParentPackage("default")
public class SampleTransferAction extends CrudActionSupport<SampleTransferRecord> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	
	private SampleTransferRecord sampleTransferRecord;
	
	@Autowired
	private SampleTransferRecordManager sampleTransferRecordManager;
	
	private Page<SampleTransferRecord> page;
	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	public SampleTransferRecordManager getSampleTransferRecordManager() {
		return sampleTransferRecordManager;
	}

	public void setSampleTransferRecordManager(
			SampleTransferRecordManager sampleTransferRecordManager) {
		this.sampleTransferRecordManager = sampleTransferRecordManager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Page<SampleTransferRecord> getPage() {
		return page;
	}

	public void setPage(Page<SampleTransferRecord> page) {
		this.page = page;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			sampleTransferRecord=new SampleTransferRecord();
			sampleTransferRecord.setCreatedTime(new Date());
			sampleTransferRecord.setCompanyId(ContextUtils.getCompanyId());
			sampleTransferRecord.setCreator(ContextUtils.getUserName());
			sampleTransferRecord.setLastModifiedTime(new Date());
			sampleTransferRecord.setLastModifier(ContextUtils.getUserName());
			sampleTransferRecord.setBusinessUnitName(ContextUtils.getSubCompanyName());
			sampleTransferRecord.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			sampleTransferRecord=sampleTransferRecordManager.getSampleTransferRecord(id);
		}
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = sampleTransferRecordManager.search(page);
			for(SampleTransferRecord sampleTransferRecord : page.getResult()){
				sampleTransferRecord.setCheckBomName(sampleTransferRecord.getCheckBomName().replaceAll("\n",""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "进货检验管理：检验报告-检验方案变更确认");
		return null;
	}
	
	@Action("input")
	public String input() throws Exception {
		if(id == null){
			sampleTransferRecord.setAuditTime(new Date());
			sampleTransferRecord.setAuditMan(ContextUtils.getUserName());
			sampleTransferRecord.setAuditState(SampleTransferRecord.AUDITSTATE_PASS);			
		}
		ActionContext.getContext().put("iqc_product_stage",ApiFactory.getSettingService().getOptionsByGroupCode("iqc_product_stage"));
		return SUCCESS;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="检验状态变更")
	public String delete() throws Exception {
		try {
			sampleTransferRecordManager.delete(deleteIds);
		} catch (Exception e) {
			createErrorMessage("删除失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SampleTransferRecord getModel() {
		return sampleTransferRecord;
	}
	@Action("audit")
	public String audit() throws Exception {
		try {
			sampleTransferRecordManager.auditSampleTransfer(Struts2Utils.getParameter("ids"),Struts2Utils.getParameter("state"),Struts2Utils.getParameter("auditText"));
			createMessage("操作成功!");
		} catch (Exception e) {
			createErrorMessage("操作失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="检验状态变更")
	public String save() throws Exception {
		try {
			sampleTransferRecordManager.addSampleTransferRecord(sampleTransferRecord);
			createMessage("操作成功!");
		} catch (Exception e) {
			createErrorMessage("操作失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="检验状态变更")
	public String export() throws Exception {
		try {
			Page<SampleTransferRecord> page = new Page<SampleTransferRecord>(65535);
			page = sampleTransferRecordManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"检验状态变更"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
