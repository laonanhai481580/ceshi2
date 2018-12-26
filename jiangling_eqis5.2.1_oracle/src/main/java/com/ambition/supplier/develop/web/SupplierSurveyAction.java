package com.ambition.supplier.develop.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.develop.services.SupplierSurveyManager;
import com.ambition.supplier.entity.SupplierSurvey;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;


/**
 * 类名:供应商调查表台账
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月10日 发布
 */
@Namespace("/supplier/develop/survey")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/develop/survey", type = "redirectAction") })
public class SupplierSurveyAction extends CrudActionSupport<SupplierSurvey>{

		/**
		  *SupplierSurveyAction.java2016年10月10日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<SupplierSurvey> page;
	private JSONObject params;//查询参数
	private SupplierSurvey supplierSurvey;
	private String saveSucc;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierSurveyManager supplierSurveyManager;
	
	
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

	public Page<SupplierSurvey> getPage() {
		return page;
	}

	public void setPage(Page<SupplierSurvey> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public SupplierSurvey getSupplierSurvey() {
		return supplierSurvey;
	}

	public void setSupplierSurvey(SupplierSurvey supplierSurvey) {
		this.supplierSurvey = supplierSurvey;
	}

	public String getSaveSucc() {
		return saveSucc;
	}

	public void setSaveSucc(String saveSucc) {
		this.saveSucc = saveSucc;
	}

	@Override
	public SupplierSurvey getModel() {
		// TODO Auto-generated method stub
		return supplierSurvey;
	}

	@Override
	@Action("delete")
	public String delete() throws Exception {
		supplierSurveyManager.delete(deleteIds);
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Action("list")
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return "SUCCESS";
	}

	@Action("list-datas")
	public String listDatas() throws Exception {
		// TODO Auto-generated method stub
		page = supplierSurveyManager.listDatas(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id==null){
			supplierSurvey = new SupplierSurvey();
			supplierSurvey.setCreatedTime(new Date());
			supplierSurvey.setCompanyId(ContextUtils.getCompanyId());
			supplierSurvey.setCreatorName(ContextUtils.getUserName());
			supplierSurvey.setCreator(ContextUtils.getLoginName());
			supplierSurvey.setLastModifiedTime(new Date());
			supplierSurvey.setLastModifier(ContextUtils.getUserName());
			supplierSurvey.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierSurvey.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			supplierSurvey = supplierSurveyManager.getSupplierSurvey(id);
		}
	}

	@Override
	@Action("save")
	public String save() throws Exception {
		JSONObject result = new JSONObject();
		if(id != null){
			supplierSurvey.setLastModifiedTime(new Date());
			supplierSurvey.setLastModifier(ContextUtils.getUserName());
		}
		try {
			supplierSurveyManager.saveSupplierSurvey(supplierSurvey);
			renderText(JsonParser.getRowValue(supplierSurvey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
