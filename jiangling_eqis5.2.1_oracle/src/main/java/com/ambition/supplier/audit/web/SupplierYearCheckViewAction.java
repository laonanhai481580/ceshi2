package com.ambition.supplier.audit.web;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.audit.services.SupplierYearCheckViewManager;
import com.ambition.supplier.entity.SupplierYearCheckView;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
@Namespace("/supplier/audit/year-view")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/audit/year-view", type = "redirectAction") })
public class SupplierYearCheckViewAction extends CrudActionSupport<SupplierYearCheckView>{
		/**
		  *SupplierYearCheckViewAction.java2016年10月27日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private SupplierYearCheckView supplierYearCheckView;
    @Autowired
    private SupplierYearCheckViewManager supplierYearCheckViewManager;
    private Page<SupplierYearCheckView> page;
    private JSONObject params;
    @Autowired
    private LogUtilDao logUtilDao;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SupplierYearCheckView getSupplierYearCheckView() {
		return supplierYearCheckView;
	}

	public void setSupplierYearCheckView(SupplierYearCheckView supplierYearCheckView) {
		this.supplierYearCheckView = supplierYearCheckView;
	}

	public Page<SupplierYearCheckView> getPage() {
		return page;
	}

	public void setPage(Page<SupplierYearCheckView> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	public SupplierYearCheckView getModel() {
		// TODO Auto-generated method stub
		return supplierYearCheckView;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
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
		ActionContext.getContext().put("auditYears",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_audit_year"));
        return SUCCESS;
    }
    
    @Action("list-datas")
    public String listdatas() throws Exception{
        try {
            page = supplierYearCheckViewManager.getSupplierYearCheckViewPage(page);
            renderText(PageUtils.pageToJson(page));
        } catch (Exception e) {
            logUtilDao.debugLog("查询失败", "查询失败"+e.getMessage());
        }
        return null;
    }

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
