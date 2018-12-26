package com.ambition.carmfg.oqc.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OqcOtherInspectionReport;
import com.ambition.carmfg.oqc.service.OqcOtherInspectionReportManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:体系维护action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Namespace("/carmfg/oqc/oqcother-inspection")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/oqc/oqcother-inspection", type = "redirectAction") })
public class OqcOtherInspectionReportAction extends BaseAction<OqcOtherInspectionReport>{

	private static final long serialVersionUID = 1L;
	private OqcOtherInspectionReport oqcOtherInspectionReport;
	@Autowired
	private LogUtilDao logUtilDao;

	private File myFile;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	@Autowired
	private AcsUtils acsUtils;
	private Page<OqcOtherInspectionReport> page;
	@Autowired
	private OqcOtherInspectionReportManager oqcOtherInspectionReportManager;
	@Override
	public OqcOtherInspectionReport getModel() {
		return oqcOtherInspectionReport;
	}
	
	
	@Action("isHarmful")
	@LogInfo(optType="变更")
	public String harmful(){
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		try {
			oqcOtherInspectionReportManager.isHarmfulDate(eid);
			oqcOtherInspectionReportManager.harmful(eid,type);
			if("Y".equals(type)){
			oqcOtherInspectionReport=oqcOtherInspectionReportManager.getOqcOtherInspectionReport(id);
			}
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "变更数据状态，编号："+eid);
		} catch (Exception e) {
			createErrorMessage("提交失败："+e.getMessage());
			return null;
		}
		return renderText("操作成功");
	}
	
	
	
	@Action("delete")
	@LogInfo(optType="删除",message="OQC其他检验")
	public String delete() throws Exception {
		try {
			oqcOtherInspectionReportManager.deleteOqcOtherInspectionReport(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除OQC其他检验，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除OQC其他检验失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建OQC其他检验")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			oqcOtherInspectionReport=new OqcOtherInspectionReport();
			oqcOtherInspectionReport.setCompanyId(ContextUtils.getCompanyId());
			oqcOtherInspectionReport.setCreatedTime(new Date());
			oqcOtherInspectionReport.setCreator(ContextUtils.getUserName());
			oqcOtherInspectionReport.setModifiedTime(new Date());
			oqcOtherInspectionReport.setModifier(ContextUtils.getLoginName());
			oqcOtherInspectionReport.setModifierName(ContextUtils.getUserName());
			oqcOtherInspectionReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			oqcOtherInspectionReport.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			oqcOtherInspectionReport=oqcOtherInspectionReportManager.getOqcOtherInspectionReport(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存OQC其他检验")
	public String save() throws Exception {
		if(id != null && id != 0){
			oqcOtherInspectionReport.setModifiedTime(new Date());
			oqcOtherInspectionReport.setModifier(ContextUtils.getLoginName());
			oqcOtherInspectionReport.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", oqcOtherInspectionReport.toString());
		}else{
			logUtilDao.debugLog("保存", oqcOtherInspectionReport.toString());
		}
		try {
			String zb1 = Struts2Utils.getParameter("attachmentFiles");
			oqcOtherInspectionReport.setAttachment(zb1);
			oqcOtherInspectionReportManager.saveOqcOtherInspectionReport(oqcOtherInspectionReport);
			this.renderText(JsonParser.object2Json(oqcOtherInspectionReport));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存OQC其他检验失败  ",e);
		}		
		return null;
	}

	@Action("list-datas")
	@LogInfo(optType="查询",message="查询数据")
	public String listDates(){
		String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
		//String userName = ContextUtils.getCompanyName();
		String code = ContextUtils.getLoginName();
		String type = Struts2Utils.getParameter("type");
	//	factorySupply = Struts2Utils.getParameter("factorySupply");
//		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
//		String subName=user.getSubCompanyName();
		try {
//			if("开发部".equals(dept)){
//				page = gpAverageMaterialManager.listState(page,type,null,null,null);
//				renderText(PageUtils.pageToJson(page));
//			}else
				if("供应商".equals(dept)){
				page = oqcOtherInspectionReportManager.listState(page,type,code,null);
				renderText(PageUtils.pageToJson(page));
			}else{
				page = oqcOtherInspectionReportManager.listState(page,type,null,null);//,factorySupply
				renderText(PageUtils.pageToJson(page));
			}
		} catch (Exception e) {
			log.error("台账获取例表失败", e);
		}
		return null;
	}
	
	@Action("export")
	@LogInfo(optType="导出", message="OQC其他检验")
	public String export() throws Exception {
		try {
			Page<OqcOtherInspectionReport> page = new Page<OqcOtherInspectionReport>(65535);
			page = oqcOtherInspectionReportManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"OQC其他检验报告建立库台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("OQC其他检验建立库台账",e);
		}
		return null;
	}
	
	
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(this.myFile != null){
				renderHtml(oqcOtherInspectionReportManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	  @Action("download-template")
	  @LogInfo(optType="下载", message="下载OQC外观检验数据导入模板")
	  public String downloadTemplate()
	    throws Exception
	  {
	    InputStream inputStream = null;
	    try {
	      inputStream = getClass().getClassLoader().getResourceAsStream("template/report/oqcother-inspection-report.xlsx");
	      Workbook book = WorkbookFactory.create(inputStream);
	      String fileName = "OQC其他检验数据导入模板.xls";
	      byte[] byname = fileName.getBytes("gbk");
	      fileName = new String(byname, "8859_1");
	      HttpServletResponse response = Struts2Utils.getResponse();
	      response.reset();
	      response.setContentType("application/vnd.ms-excel");
	      response.setHeader("Content-Disposition", 
	        "attachment; filename=\"" + fileName + "\"");
	      book.write(response.getOutputStream());
	    } catch (Exception e) {
	      this.log.error("下载OQC其他检验数据导入模板失败!", e);
	    } finally {
	      if (inputStream != null) {
	        inputStream.close();
	      }
	    }
	    return null;
	  }
	
	/**
	 * 
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
	public Page<OqcOtherInspectionReport> getPage() {
		return page;
	}
	public void setPage(Page<OqcOtherInspectionReport> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
}
