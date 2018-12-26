package com.ambition.carmfg.oqc.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.SizeInspection;
import com.ambition.carmfg.oqc.service.SizeInspectionManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
/**
 * 
 * 类名:尺寸检验数据action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年7月7日 发布
 */
@Namespace("/carmfg/oqc")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/oqc", type = "redirectAction") })
public class SizeInspectionAction extends BaseAction<SizeInspection>{

	private static final long serialVersionUID = 1L;
	private SizeInspection sizeInspection;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<SizeInspection> page;
	private File myFile;
	private String businessUnit;//所属事业部
	@Autowired
	private SizeInspectionManager sizeInspectionManager;
	@Override
	public SizeInspection getModel() {
		return sizeInspection;
	}
	@Action("size-delete")
	@LogInfo(optType="删除",message="尺寸检验数据")
	public String delete() throws Exception {
		try {
			sizeInspectionManager.deleteSizeInspection(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除尺寸检验数据，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除尺寸检验数据失败",e);
		}
		return null;
	}	
	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("size-list")
	public String list() throws Exception {
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			sizeInspection=new SizeInspection();
			sizeInspection.setCompanyId(ContextUtils.getCompanyId());
			sizeInspection.setCreatedTime(new Date());
			sizeInspection.setCreator(ContextUtils.getUserName());
			sizeInspection.setModifiedTime(new Date());
			sizeInspection.setModifier(ContextUtils.getLoginName());
			sizeInspection.setModifierName(ContextUtils.getUserName());
			sizeInspection.setBusinessUnitName(ContextUtils.getSubCompanyName());
			sizeInspection.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			sizeInspection=sizeInspectionManager.getSizeInspection(id);
		}
		
	}
	
	@Action("size-save")
	@LogInfo(optType="保存",message="保存尺寸检验数据")
	public String save() throws Exception {
		if(id != null && id != 0){
			sizeInspection.setModifiedTime(new Date());
			sizeInspection.setModifier(ContextUtils.getLoginName());
			sizeInspection.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", sizeInspection.toString());
		}else{
			logUtilDao.debugLog("保存", sizeInspection.toString());
		}
		try {
			String zb1 = Struts2Utils.getParameter("attachmentFiles");
			sizeInspection.setAttachment(zb1);
			sizeInspection.setBusinessUnitName(businessUnit);
			sizeInspectionManager.saveSizeInspection(sizeInspection);
			this.renderText(JsonParser.object2Json(sizeInspection));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存尺寸检验数据失败  ",e);
		}		
		return null;
	}
	@Action("size-list-datas")
	public String listDatas() throws Exception {
		try {
			page = sizeInspectionManager.search(page,businessUnit);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "尺寸检验数据");
		} catch (Exception e) {
			log.error("查询尺寸检验数据失败  ",e);
		}		
		return null;
	}
	@Action("size-export")
	public String export() throws Exception {
		try {
			Page<SizeInspection> page = new Page<SizeInspection>(65535);
			page = sizeInspectionManager.search(page,businessUnit);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"尺寸检验数据台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出尺寸检验数据失败",e);
		}
		return null;
	}
	@Action("size-import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("size-import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(sizeInspectionManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	public void prepareEditSave() throws Exception{
		prepareModel();
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
	public Page<SizeInspection> getPage() {
		return page;
	}
	public void setPage(Page<SizeInspection> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}		
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}	
}
