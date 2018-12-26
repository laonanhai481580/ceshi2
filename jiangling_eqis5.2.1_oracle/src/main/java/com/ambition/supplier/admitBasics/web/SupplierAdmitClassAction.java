package com.ambition.supplier.admitBasics.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.BaseAction;
import com.ambition.supplier.admitBasics.servce.SupplierAdmitClassManager;
import com.ambition.supplier.entity.SupplierAdmitClass;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/supplier/base-info/admit-basics")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/base-info/admit-basics", type = "redirectAction") })
public class SupplierAdmitClassAction extends BaseAction<SupplierAdmitClass>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String businessUnit;//所属事业部
	private SupplierAdmitClass admitClass;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierAdmitClassManager admitClassManager;
	private Page<SupplierAdmitClass> page;
	
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

	public SupplierAdmitClass getSupplierAdmitClass() {
		return admitClass;
	}
	public void setSupplierAdmitClass(SupplierAdmitClass admitClass) {
		this.admitClass = admitClass;
	}
	public Page<SupplierAdmitClass> getPage() {
		return page;
	}
	public void setPage(Page<SupplierAdmitClass> page) {
		this.page = page;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	@Override
	public SupplierAdmitClass getModel() {
		return admitClass;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			admitClass = new SupplierAdmitClass();
			admitClass.setCompanyId(ContextUtils.getCompanyId());
			admitClass.setCreatedTime(new Date());
			admitClass.setCreator(ContextUtils.getUserName());
			admitClass.setLastModifiedTime(new Date());
			admitClass.setLastModifier(ContextUtils.getUserName());
			admitClass.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			admitClass = admitClassManager.getSupplierAdmitClass(id);
		}
	}
	@Action("admit-class-delete")
	@LogInfo(optType="删除",message="材料类别维护")
	@Override
	public String delete() throws Exception {
		try{
			String deleteNos=admitClassManager.deleteSupplierAdmitClass(deleteIds);
			//日志消息,方便跟踪删除记录
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除材料类别台帐,材料类别为【" + deleteNos + "】!");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("admit-class-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("admit-class-list")
	@Override
	public String list() throws Exception {
		renderMenu();
		//所属事业部
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
	@Action("admit-class-save")
	@LogInfo(optType="保存",message="材料类别维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			admitClass.setLastModifiedTime(new Date());
			admitClass.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", admitClass.toString());
		}else{
			logUtilDao.debugLog("保存", admitClass.toString());
		}
		try{
			admitClass.setBusinessUnitName(businessUnit);
			admitClassManager.saveSupplierAdmitClass(admitClass);
			this.renderText(JsonParser.object2Json(admitClass));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("admit-class-list-datas")
	public String getListDatas() throws Exception {
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		try {
			page = admitClassManager.list(page,businessUnit);
			this.renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "供应商管理：基础设置-材料类别维护");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="材料类别维护")
	public String export() throws Exception {
		Page<SupplierAdmitClass> page = new Page<SupplierAdmitClass>(100000);
		page = admitClassManager.list(page,businessUnit);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_ADMIT_CLASS"),"admitClass"));
		logUtilDao.debugLog("导出", "供应商管理：基础设置-材料类别维护");
		return null;
	}
}
