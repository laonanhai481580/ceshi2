package com.ambition.gp.gpSurvey.web;

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

import com.ambition.gp.entity.GpSurvey;
import com.ambition.gp.gpSurvey.services.GpSurveyManager;
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
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;


@Namespace("/gp/gpmaterial/gpSurvey")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gp/gpmaterial/gpSurvey", type = "redirectAction") })
public class GpSurveyAction extends CrudActionSupport<GpSurvey>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GpSurvey gpSurvey;
	private File myFile;
	private Page<GpSurvey> page;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private GpSurveyManager gpSurveyManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
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

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public GpSurvey getGpSurvey() {
		return gpSurvey;
	}

	public void setGpSurvey(GpSurvey gpSurvey) {
		this.gpSurvey = gpSurvey;
	}

	public Page<GpSurvey> getPage() {
		return page;
	}

	public void setPage(Page<GpSurvey> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

		@Override
		public GpSurvey getModel() {
			// TODO Auto-generated method stub
			return gpSurvey;
		}
		
		@Action("delete")
		@LogInfo(optType="删除")
		@Override
		public String delete() throws Exception {
			try {
				gpSurveyManager.deleteGpSurvey(deleteIds);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:供应商调查表");
			} catch (Exception e) {
				// TODO: handle exception
				renderText("删除失败:" + e.getMessage());
				log.error("删除数据信息失败",e);
			}
			return null;
		}
		@Override
		public String input() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Action("list")
		@Override
		public String list() throws Exception {
			// TODO Auto-generated method stub
			return SUCCESS;
		}
		
		@Action("list-datas")
		@LogInfo(optType="查询",message="查询数据")
		public String listDates(){
			try {
				page = gpSurveyManager.search(page);
				renderText(PageUtils.pageToJson(page));
			} catch (Exception e) {
				// TODO: handle exception
				log.error("台账获取例表失败", e);
			}
			return null;
		}

		@Override
		protected void prepareModel() throws Exception {
			// TODO Auto-generated method stub
			if(id == null){
				gpSurvey = new GpSurvey();
				gpSurvey.setCompanyId(ContextUtils.getCompanyId());
				gpSurvey.setCreatedTime(new Date());
				gpSurvey.setCreator(ContextUtils.getUserName());
				gpSurvey.setModifiedTime(new Date());
				gpSurvey.setModifier(ContextUtils.getUserName());
				gpSurvey.setBusinessUnitName(ContextUtils.getSubCompanyName());
				gpSurvey.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			}else {
				gpSurvey = gpSurveyManager.getGpSurvey(id);
			}
		}

		
		@Action("save")
		@LogInfo(optType="保存",message="保存供应商调查表数据")
		@Override
		public String save() throws Exception {
			try {
				String fj = Struts2Utils.getParameter("attachmentFiles");
				String fj1 = Struts2Utils.getParameter("attachmentFiles2");
				String fj2 = Struts2Utils.getParameter("attachmentFiles3");
				if(fj!=null){
					gpSurvey.setGuaranteeFile(fj);;
				}
				if(fj1!=null){
					gpSurvey.setSurveyFile(fj1);
				}
				if(fj2!=null){
					gpSurvey.setSignatureFile(fj2);
				}
				gpSurveyManager.saveGpSurvey(gpSurvey);
				renderText(JsonParser.getRowValue(gpSurvey));
				logUtilDao.debugLog("保存",gpSurvey.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}

		@Action("export")
		@LogInfo(optType="导出",message="供应商调查表台帐")
		public String export() throws Exception {
			Page<GpSurvey> page = new Page<GpSurvey>(100000);
			page = gpSurveyManager.list(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GP_SURVEY"),"供应商调查表台帐"));
			logUtilDao.debugLog("导出", "供应商调查表台帐");
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
					renderHtml(gpSurveyManager.importDatas(myFile));
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
		@LogInfo(optType="下载",message="下载供应商调查表模版")
		public String downloadTemplate() throws Exception {
			InputStream inputStream = null;
			try {
				inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/gp-gpSurvey.xls");
				Workbook book = WorkbookFactory.create(inputStream);
				String fileName = "供应商调查表台帐.xls";
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
		//创建返回消息
		public void createErrorMessage(String message){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("error",true);
			map.put("message",message);
			renderText(JSONObject.fromObject(map).toString());
		}
}
