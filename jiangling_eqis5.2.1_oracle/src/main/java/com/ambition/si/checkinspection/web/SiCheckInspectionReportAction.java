package com.ambition.si.checkinspection.web;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.si.checkinspection.service.SiCheckInspectionReportManager;
import com.ambition.si.entity.SiCheckInspectionReport;
import com.ambition.si.entity.SiCheckItem;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * 现场检验报告ACTION
 * @authorBy 赵骏
 *
 */
@Namespace("/si/check-inspection")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "si/check-inspection", type = "redirectAction") })
public class SiCheckInspectionReportAction extends CrudActionSupport<SiCheckInspectionReport> {
	private Logger log=Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SiCheckInspectionReport siCheckInspectionReport;
	private JSONObject params;
	private File myFile;
	private boolean canEdit=true;
	@Autowired
	private SiCheckInspectionReportManager siCheckInspectionReportManager;

	private Page<SiCheckInspectionReport> page;
	
	private List<SiCheckInspectionReport> list;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;	
	public List<SiCheckInspectionReport> getList() {
		return list;
	}

	public void setList(List<SiCheckInspectionReport> list) {
		this.list = list;
	}

	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
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

	public void setPage(Page<SiCheckInspectionReport> page) {
		this.page = page;
	}
	
	public Page<SiCheckInspectionReport> getPage() {
		return page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public SiCheckInspectionReport getSiCheckInspectionReport() {
		return siCheckInspectionReport;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public void setSiCheckInspectionReport(
			SiCheckInspectionReport siCheckInspectionReport) {
		this.siCheckInspectionReport = siCheckInspectionReport;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){			
			siCheckInspectionReport=new SiCheckInspectionReport();
			siCheckInspectionReport.setFormNo(formCodeGenerated.generateSiCheckInspectionReportFormNo());
			siCheckInspectionReport.setCreatedTime(new Date());
			siCheckInspectionReport.setCompanyId(ContextUtils.getCompanyId());
			siCheckInspectionReport.setCreator(ContextUtils.getLoginName());
			siCheckInspectionReport.setCreatorName(ContextUtils.getUserName());
			siCheckInspectionReport.setLastModifiedTime(new Date());
			siCheckInspectionReport.setLastModifier(ContextUtils.getUserName());
			siCheckInspectionReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			siCheckInspectionReport.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {			
			siCheckInspectionReport=siCheckInspectionReportManager.getSiCheckInspectionReport(id);
			if(!siCheckInspectionReport.getCreator().equals(ContextUtils.getLoginName())){
				canEdit=false;
			}
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
			page = siCheckInspectionReportManager.list(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "供应商检验：现场检验报告台帐");
		return null;
	}
	@Action("input")
	@Override
	public String input() throws Exception {
		try {
			List<SiCheckItem> siCheckItems=siCheckInspectionReport.getCheckItems();
			if (siCheckItems == null||siCheckItems.size()==0) {
				siCheckItems = new ArrayList<SiCheckItem>();
				SiCheckItem item = new SiCheckItem();
				siCheckItems.add(item);
			}
			ActionContext.getContext().put("_siCheckItems", siCheckItems);
			List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
			ActionContext.getContext().put("businessUnits", businessUnits);
			List<Option> conclusions = ApiFactory.getSettingService().getOptionsByGroupCode("si-conclusion");
			ActionContext.getContext().put("conclusions", conclusions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="现场检验报告")
	@Override
	public String delete() throws Exception {
		try{
			String[] ids = deleteIds.split(",");
			int allCount=ids.length;
			int delCount=siCheckInspectionReportManager.deleteSiCheckInspectionReport(deleteIds);
			createMessage("操作成功!共删除了"+delCount + "条数据，"+(allCount-delCount)+"条数据没有权限删除！");
		}catch (Exception e) {
			log.error("删除失败!",e);
			createMessage("操作失败:" + e.getMessage());
		}
		return null;
	}	

	/**
	 * 现场检验报告导出
	 */
	@Action("export")
	@LogInfo(optType="导出",message="现场检验报告")
	public String export() throws Exception {
		Page<SiCheckInspectionReport> page = new Page<SiCheckInspectionReport>(100000);
		page = siCheckInspectionReportManager.list(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SI_CHECK_INSPECTION_REPORT"),"现场检验报告"));
		logUtilDao.debugLog("导出", "供应商检验：现场检验报告");
		return null;		
	}
	@Override
	public SiCheckInspectionReport getModel() {
		return siCheckInspectionReport;
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存现场检验报告 ")
	@Override
	public String save() throws Exception {
		try{
			String zb = Struts2Utils.getParameter("zibiao");
			SiCheckInspectionReport report = siCheckInspectionReportManager.save(siCheckInspectionReport, zb);
			String reportId = Struts2Utils.getParameter("id");
			if(report.getId() == null && StringUtils.isEmpty(reportId)){
				logUtilDao.debugLog("保存", report.toString());
			}else{
				report.setId(Long.valueOf(reportId));
				report.setModifiedTime(new Date());
				report.setModifier(ContextUtils.getLoginName());
				report.setModifierName(ContextUtils.getUserName());
				logUtilDao.debugLog("修改", report.toString());
			}
			siCheckInspectionReportManager.saveSiCheckInspectionReport(report);
			addActionMessage("保存成功" );
	}catch(Exception e){
		log.error("保存现场检验报告 失败",e);
		addActionMessage("保存失败");
	}
	input();
	return "input";
	}
	/**
    * 方法名:导出Excel文件
    * <p>
    * 功能说明：
    * </p>
    * @throws IOException
    */
   @Action("export-form")
   @LogInfo(optType = "导出", message = "导出现场检验报告")
   public void exportReport() throws Exception {
       try {
           prepareModel();
           siCheckInspectionReportManager.exportReport(siCheckInspectionReport);
       } catch (Exception e) {
           e.printStackTrace();
           renderText("导出失败:" + e.getMessage());
       }
   }
  
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}	
}
