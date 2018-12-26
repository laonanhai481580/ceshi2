package com.ambition.qsm.inneraudit.web;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
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
import com.ambition.qsm.entity.AuditorLibrary;
import com.ambition.qsm.inneraudit.service.AuditorLibraryManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.service.organization.UserManager;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:内审员信息action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Namespace("/qsm/inner-audit/auditor-library")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/inner-audit/auditor-library", type = "redirectAction") })
public class AuditorLibraryAction extends BaseAction<AuditorLibrary>{

	private static final long serialVersionUID = 1L;
	private AuditorLibrary auditorLibrary;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private UserManager userManager;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private File myFile;
	private Page<AuditorLibrary> page;
	@Autowired
	private AuditorLibraryManager auditorLibraryManager;
	@Override
	public AuditorLibrary getModel() {
		return auditorLibrary;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="内审员信息")
	public String delete() throws Exception {
		try {
			auditorLibraryManager.deleteAuditorLibrary(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除内审员信息，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除内审员信息失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建内审员信息")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			auditorLibrary=new AuditorLibrary();
			auditorLibrary.setCompanyId(ContextUtils.getCompanyId());
			auditorLibrary.setCreatedTime(new Date());
			auditorLibrary.setCreator(ContextUtils.getUserName());
			auditorLibrary.setModifiedTime(new Date());
			auditorLibrary.setModifier(ContextUtils.getLoginName());
			auditorLibrary.setModifierName(ContextUtils.getUserName());
			auditorLibrary.setBusinessUnitName(ContextUtils.getSubCompanyName());
			auditorLibrary.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			auditorLibrary=auditorLibraryManager.getAuditorLibrary(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存内审员信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			auditorLibrary.setModifiedTime(new Date());
			auditorLibrary.setModifier(ContextUtils.getLoginName());
			auditorLibrary.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", auditorLibrary.toString());
		}else{
			logUtilDao.debugLog("保存", auditorLibrary.toString());
		}
		try {
			String zb1 = Struts2Utils.getParameter("attachmentFiles");
			auditorLibrary.setCertificate(zb1);
			/*User user=userManager.getCompanyUserByLoginName(auditorLibrary.getWorkNumber());
			auditorLibrary.setName(user.getName());
			auditorLibrary.setBusinessUnitName(user.getSubCompanyName());
			auditorLibrary.setDepartment(user.getMainDepartmentName());
			//auditorLibrary.setPosition(user.get);
			auditorLibrary.setEducation(user.getUserInfo().getEducationGrade());
			Calendar a=Calendar.getInstance();
			Integer currentYear= a.get(Calendar.YEAR);//得到年
			String date=user.getUserInfo().getHireDate();
			Integer hireYear=null;
			if(!date.equals("")){
				hireYear=Integer.valueOf(date.substring(0,date.indexOf('-')));				
			}	
			Integer wokeAge=currentYear-hireYear;
			auditorLibrary.setWorkAge(wokeAge.toString());*/
			auditorLibraryManager.saveAuditorLibrary(auditorLibrary);
			this.renderText(JsonParser.object2Json(auditorLibrary));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存内审员信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = auditorLibraryManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "内审员信息");
		} catch (Exception e) {
			log.error("查询内审员信息失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="内审员信息")
	public String export() throws Exception {
		try {
			Page<AuditorLibrary> page = new Page<AuditorLibrary>(65535);
			page = auditorLibraryManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"内审员信息台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出内审员信息失败",e);
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
				renderHtml(auditorLibraryManager.importDatas(myFile));
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
	@LogInfo(optType="下载",message="下载内审员信息模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/qsm-auditor-library.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "内审员信息模板.xls";
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
	public Page<AuditorLibrary> getPage() {
		return page;
	}
	public void setPage(Page<AuditorLibrary> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
}
