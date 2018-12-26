package com.ambition.supplier.mrbcode.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.entity.SupplierMrbCode;
import com.ambition.supplier.mrbcode.services.SupplierMrbCodeManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/supplier/supplierMrbCode")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/supplier/supplierMrbCode", type = "redirectAction") })
public class SupplierMrbCodeAction extends CrudActionSupport<SupplierMrbCode>{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<SupplierMrbCode> page;
	private Logger log = Logger.getLogger(this.getClass());
	private SupplierMrbCode supplierMrbCode;
	@Autowired
	private SupplierMrbCodeManager supplierMrbCodeManager;
	@Autowired
	private LogUtilDao logUtilDao;
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

	public Page<SupplierMrbCode> getPage() {
		return page;
	}

	public void setPage(Page<SupplierMrbCode> page) {
		this.page = page;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public SupplierMrbCode getSupplierMrbCode() {
		return supplierMrbCode;
	}

	public void setSupplierMrbCode(SupplierMrbCode supplierMrbCode) {
		this.supplierMrbCode = supplierMrbCode;
	}
	
	@Override
	public SupplierMrbCode getModel() {
		// TODO Auto-generated method stub
		return supplierMrbCode;
	}

	@Action("save")
	@LogInfo(optType="保存",message="保存成本数据")
	@Override
	public String save() throws Exception {
		try {
			// TODO Auto-generated method stub
			supplierMrbCodeManager.saveSupplierMrbCode(supplierMrbCode);
		} catch (Exception e) {
			// TODO: handle exception
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			// TODO Auto-generated method stub
			supplierMrbCodeManager.deleteSupplierMrbCode(deleteIds);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	
	@Action("list-datas")
	@LogInfo(optType="数据",message="成本数据")
	public String listDates(){
		try {
			page = supplierMrbCodeManager.search(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("台账获取例表失败", e);
		}
		return null;
	}
	
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
			supplierMrbCode = new SupplierMrbCode();
			supplierMrbCode.setCompanyId(ContextUtils.getCompanyId());
			supplierMrbCode.setCreatedTime(new Date());
			supplierMrbCode.setCreator(ContextUtils.getUserName());
			supplierMrbCode.setModifiedTime(new Date());
			supplierMrbCode.setModifier(ContextUtils.getUserName());
			supplierMrbCode.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierMrbCode.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			supplierMrbCode = supplierMrbCodeManager.getSupplierMrbCode(id);
		}
		
	}
	//创建返回消息
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}

	
	
}
