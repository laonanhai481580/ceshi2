package com.ambition.qsm.customeraudit.web;

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

import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.qsm.customeraudit.service.CustomerAuditManager;
import com.ambition.qsm.entity.CustomerAudit;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:客户审核履历action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Namespace("/qsm/customer-audit")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/customer-audit", type = "redirectAction") })
public class CustomerAuditAction extends BaseAction<CustomerAudit>{

	private static final long serialVersionUID = 1L;
	private CustomerAudit customerAudit;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private File myFile;
	private Page<CustomerAudit> page;
	@Autowired
	private CustomerAuditManager customerAuditManager;
	@Override
	public CustomerAudit getModel() {
		return customerAudit;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="客户审核履历")
	public String delete() throws Exception {
		try {
			customerAuditManager.deleteCustomerAudit(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除客户审核履历，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除客户审核履历失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建客户审核履历")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			customerAudit=new CustomerAudit();
			customerAudit.setCompanyId(ContextUtils.getCompanyId());
			customerAudit.setCreatedTime(new Date());
			customerAudit.setCreator(ContextUtils.getUserName());
			customerAudit.setModifiedTime(new Date());
			customerAudit.setModifier(ContextUtils.getLoginName());
			customerAudit.setModifierName(ContextUtils.getUserName());
			customerAudit.setBusinessUnitName(ContextUtils.getSubCompanyName());
			customerAudit.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			customerAudit=customerAuditManager.getCustomerAudit(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存客户审核履历")
	public String save() throws Exception {
		if(id != null && id != 0){
			customerAudit.setModifiedTime(new Date());
			customerAudit.setModifier(ContextUtils.getLoginName());
			customerAudit.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", customerAudit.toString());
		}else{
			logUtilDao.debugLog("保存", customerAudit.toString());
		}
		try {
			String customerCheck = Struts2Utils.getParameter("attachmentFiles");
			String issuesList = Struts2Utils.getParameter("attachmentFiles2");
			customerAudit.setCustomerCheck(customerCheck);
			customerAudit.setIssuesList(issuesList);
			customerAuditManager.saveCustomerAudit(customerAudit);
			this.renderText(JsonParser.object2Json(customerAudit));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存客户审核履历失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = customerAuditManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "客户审核履历");
		} catch (Exception e) {
			log.error("查询客户审核履历失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="客户审核履历")
	public String export() throws Exception {
		try {
			Page<CustomerAudit> page = new Page<CustomerAudit>(65535);
			page = customerAuditManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"客户审核履历台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出客户审核履历失败",e);
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
			if(myFile != null){
				renderHtml(customerAuditManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载客户审核履历模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/qsm-customer-audit.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "客户审核履历模板.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
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
	public Page<CustomerAudit> getPage() {
		return page;
	}
	public void setPage(Page<CustomerAudit> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
}
