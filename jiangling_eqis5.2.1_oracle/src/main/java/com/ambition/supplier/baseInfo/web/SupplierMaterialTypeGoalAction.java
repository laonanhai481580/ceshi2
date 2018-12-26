package com.ambition.supplier.baseInfo.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.baseInfo.services.SupplierMaterialTypeGoalManager;
import com.ambition.supplier.entity.SupplierMaterialTypeGoal;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:物料类别目标维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月9日 发布
 */
@Namespace("/supplier/base-info/material-type-goal")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/base-info/material-type-goal", type = "redirectAction") })
public class SupplierMaterialTypeGoalAction extends CrudActionSupport<SupplierMaterialTypeGoal>{

	
		/**
		  *SupplierMaterialTypeGoalAction.java2016年10月9日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<SupplierMaterialTypeGoal> page;
	private JSONObject params;//查询参数
	private SupplierMaterialTypeGoal supplierMaterialTypeGoal;
	private String saveSucc;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierMaterialTypeGoalManager supplierMaterialTypeGoalManager;
	@Override
	public SupplierMaterialTypeGoal getModel() {
		// TODO Auto-generated method stub
		return supplierMaterialTypeGoal;
	}

	@Override
	@Action("delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		supplierMaterialTypeGoalManager.delete(deleteIds);
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
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

	public Page<SupplierMaterialTypeGoal> getPage() {
		return page;
	}

	public void setPage(Page<SupplierMaterialTypeGoal> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public SupplierMaterialTypeGoal getSupplierMaterialTypeGoal() {
		return supplierMaterialTypeGoal;
	}

	public void setSupplierMaterialTypeGoal(
			SupplierMaterialTypeGoal supplierMaterialTypeGoal) {
		this.supplierMaterialTypeGoal = supplierMaterialTypeGoal;
	}

	public String getSaveSucc() {
		return saveSucc;
	}

	public void setSaveSucc(String saveSucc) {
		this.saveSucc = saveSucc;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
	@Override
	@Action("list")
	public String list() throws Exception {
		return "SUCCESS";
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		// TODO Auto-generated method stub
		page = supplierMaterialTypeGoalManager.listDatas(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id==null){
			supplierMaterialTypeGoal = new SupplierMaterialTypeGoal();
			supplierMaterialTypeGoal.setCreatedTime(new Date());
			supplierMaterialTypeGoal.setCompanyId(ContextUtils.getCompanyId());
			supplierMaterialTypeGoal.setCreatorName(ContextUtils.getUserName());
			supplierMaterialTypeGoal.setCreator(ContextUtils.getLoginName());
			supplierMaterialTypeGoal.setLastModifiedTime(new Date());
			supplierMaterialTypeGoal.setLastModifier(ContextUtils.getUserName());
			supplierMaterialTypeGoal.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierMaterialTypeGoal.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
//			supplierLevelScore.setBusinessUnitName("");//事业部
		}else {
			supplierMaterialTypeGoal = supplierMaterialTypeGoalManager.getSupplierMaterialTypeGoal(id);
		}
	}

	@Override
	@Action("save")
	public String save() throws Exception {
		JSONObject result = new JSONObject();
		if(id != null){
			supplierMaterialTypeGoal.setLastModifiedTime(new Date());
			supplierMaterialTypeGoal.setLastModifier(ContextUtils.getUserName());
		}
		try {
			supplierMaterialTypeGoalManager.saveSupplierMaterialTypeGoal(supplierMaterialTypeGoal);
			renderText(JsonParser.getRowValue(supplierMaterialTypeGoal));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("select-material-type")
	public String findMaterialType() throws Exception {
		String materialType  = Struts2Utils.getParameter("materialType");
		JSONObject result = new JSONObject();
		String type = Struts2Utils.getParameter("type");
		try {
			supplierMaterialTypeGoal = supplierMaterialTypeGoalManager.findMaterialType(materialType);
			result.put("error", false);
			if("check".equals(type)){
				result.put("value", supplierMaterialTypeGoal.getComingLowerLimit()/100);
			}else{
				result.put("value", supplierMaterialTypeGoal.getLineUpperLimit()/100);
			}
			
			
		} catch (Exception e) {
			log.error("查询失败:"+ e.getMessage());
			result.put("error", true);
			result.put("message", "未匹配到该物料类别");
			e.printStackTrace();
		}
		renderText(result.toString());
		return null;
		
	}

}
