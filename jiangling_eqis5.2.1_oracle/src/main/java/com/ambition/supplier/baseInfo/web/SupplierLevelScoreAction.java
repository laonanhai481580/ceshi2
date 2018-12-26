package com.ambition.supplier.baseInfo.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.ProductBom;
import com.ambition.supplier.baseInfo.services.SupplierLevelScoreManager;
import com.ambition.supplier.entity.SupplierLevelScore;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:供应商得分考核关系
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年9月27日 发布
 */
@Namespace("/supplier/base-info/level-score")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/base-info/level-score", type = "redirectAction") })
public class SupplierLevelScoreAction extends CrudActionSupport<SupplierLevelScore> {

		/**
		  *SupplierLevelScoreAction.java2016年9月27日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<SupplierLevelScore> page;
	private JSONObject params;//查询参数
	private SupplierLevelScore supplierLevelScore;
	private String saveSucc;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierLevelScoreManager supplierLevelScoreManager;
	
	
	public String getSaveSucc() {
		return saveSucc;
	}

	public void setSaveSucc(String saveSucc) {
		this.saveSucc = saveSucc;
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

	public Page<SupplierLevelScore> getPage() {
		return page;
	}

	public void setPage(Page<SupplierLevelScore> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public SupplierLevelScore getSupplierLevelScore() {
		return supplierLevelScore;
	}

	public void setSupplierLevelScore(SupplierLevelScore supplierLevelScore) {
		this.supplierLevelScore = supplierLevelScore;
	}

	@Override
	public SupplierLevelScore getModel() {
		// TODO Auto-generated method stub
		return supplierLevelScore;
	}

	@Override
	@Action("delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		supplierLevelScoreManager.delete(deleteIds);
		return null;
	}

	@Override
	@Action("input")
	public String input() throws Exception {
		// TODO Auto-generated method stub
		ActionContext.getContext().put("auditLevels",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_audit_level"));
		return SUCCESS;
	}

	@Override
	@Action("list")
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}

	@Action("list-datas")
	public String listDatas() throws Exception {
		// TODO Auto-generated method stub
		page = supplierLevelScoreManager.listDatas(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id==null){
			supplierLevelScore = new SupplierLevelScore();
			supplierLevelScore.setCreatedTime(new Date());
			supplierLevelScore.setCompanyId(ContextUtils.getCompanyId());
			supplierLevelScore.setCreatorName(ContextUtils.getUserName());
			supplierLevelScore.setCreator(ContextUtils.getLoginName());
			supplierLevelScore.setLastModifiedTime(new Date());
			supplierLevelScore.setLastModifier(ContextUtils.getUserName());
			supplierLevelScore.setBusinessUnitName(ContextUtils.getCompanyName());
			supplierLevelScore.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
//			supplierLevelScore.setBusinessUnitName("");//事业部
		}else {
			supplierLevelScore = supplierLevelScoreManager.getSupplierLevelScore(id);
		}
	}

	@Override
	@Action("save")
	public String save() throws Exception {
		JSONObject result = new JSONObject();
		try {
			System.out.println(ServletActionContext.getRequest().getRequestURI());
			supplierLevelScoreManager.saveSupplierLevelScore(supplierLevelScore);
			saveSucc="true";
		} catch (Exception e) {
			saveSucc="false";
			log.error("保存失败!",e);
			addActionMessage("操作失败!" + e.getMessage());
		}
		ActionContext.getContext().put("auditLevels",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_audit_level"));
		return "input";
	}

}
