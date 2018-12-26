package com.ambition.aftersales.baseinfo.web;

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

import com.ambition.aftersales.baseinfo.service.LarTargetManager;
import com.ambition.aftersales.entity.LarTarget;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
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
 * 类名:Lar目标值维护action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Namespace("/aftersales/base-info/lar-target")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/base-info/lar-target", type = "redirectAction") })
public class LarTargetAction extends BaseAction<LarTarget>{

	private static final long serialVersionUID = 1L;
	private File myFile;
	private LarTarget larTarget;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<LarTarget> page;
	@Autowired
	private LarTargetManager larTargetManager;
	@Override
	public LarTarget getModel() {
		return larTarget;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="删除数据")
	public String delete() throws Exception {
		try {
			larTargetManager.deleteLarTarget(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除Lar目标值维护，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除Lar目标值维护信息失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建Lar目标值维护信息")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			larTarget=new LarTarget();
			larTarget.setCompanyId(ContextUtils.getCompanyId());
			larTarget.setCreatedTime(new Date());
			larTarget.setCreator(ContextUtils.getUserName());
			larTarget.setModifiedTime(new Date());
			larTarget.setModifier(ContextUtils.getLoginName());
			larTarget.setModifierName(ContextUtils.getUserName());
			larTarget.setBusinessUnitName(ContextUtils.getSubCompanyName());
			larTarget.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			larTarget=larTargetManager.getLarTarget(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存Lar目标值维护信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			larTarget.setModifiedTime(new Date());
			larTarget.setModifier(ContextUtils.getLoginName());
			larTarget.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", larTarget.toString());
		}else{
			logUtilDao.debugLog("保存", larTarget.toString());
		}
		try {
			larTargetManager.saveLarTarget(larTarget);
			this.renderText(JsonParser.object2Json(larTarget));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存Lar目标值维护信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = larTargetManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "Lar目标值维护信息");
		} catch (Exception e) {
			log.error("查询Lar目标值维护信息失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="Lar目标值维护")
	public String export() throws Exception {
		try {
			Page<LarTarget> page = new Page<LarTarget>(65535);
			page = larTargetManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"Lar目标值维护台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出Lar目标值维护信息失败",e);
		}
		return null;
	}
	
	/*	@Action("export")
	@LogInfo(optType="导出", message="LAR数据")
	public String export() throws Exception {
		try {
			Page<LarData> page = new Page<LarData>(65535);
			page = larDataManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"LAR数据台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出LAR数据信息失败",e);
		}
		return null;
	}*/
	
	  @Action("import")
	  public String imports() throws Exception
	  {
	    return SUCCESS;
	  }
	  
	  @Action("import-datas")
	  @LogInfo(optType="导入", message="导入LAR数据")
	  public String importDatas() throws Exception {
		  System.out.println("1111");
	    try {
	      if (this.myFile != null) {
	        String businessUnit = Struts2Utils.getParameter("businessUnit");
	        renderHtml(this.larTargetManager.importDatas(this.myFile, businessUnit));
	      }
	    } catch (Exception e) {
	      renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
	    }
	    return null;
	  }
	
	  @Action("download-template")
	  @LogInfo(optType="下载", message="下载LAR目标值维护数据导入模板")
	  public String downloadTemplate()
	    throws Exception
	  {
	    InputStream inputStream = null;
	    try {
	      inputStream = getClass().getClassLoader().getResourceAsStream("template/report/afs-lartaget-template.xlsx");
	      Workbook book = WorkbookFactory.create(inputStream);
	      String fileName = "LAR目标值维护数据导入模板.xls";
	      byte[] byname = fileName.getBytes("gbk");
	      fileName = new String(byname, "8859_1");
	      HttpServletResponse response = Struts2Utils.getResponse();
	      response.reset();
	      response.setContentType("application/vnd.ms-excel");
	      response.setHeader("Content-Disposition", 
	        "attachment; filename=\"" + fileName + "\"");

	      book.write(response.getOutputStream());
	    } catch (Exception e) {
	      this.log.error("下载LAR目标值维护数据导入模板失败!", e);
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
	public Page<LarTarget> getPage() {
		return page;
	}
	public void setPage(Page<LarTarget> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
}
