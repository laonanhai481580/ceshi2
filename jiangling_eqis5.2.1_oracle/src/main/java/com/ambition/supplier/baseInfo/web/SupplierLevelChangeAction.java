package com.ambition.supplier.baseInfo.web;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.baseInfo.services.SupplierLevelChangeManager;
import com.ambition.supplier.entity.SupplierLevelChange;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:供应商等级变更关系维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月9日 发布
 */
@Namespace("/supplier/base-info/level-change")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/base-info/level-change", type = "redirectAction") })
public class SupplierLevelChangeAction extends CrudActionSupport<SupplierLevelChange>{
		/**
		  *SupplierLevelChangeAction.java2016年10月9日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private JSONObject params;//查询参数
	private SupplierLevelChange supplierLevelChange;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierLevelChangeManager supplierLevelChangeManager;
	
	
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

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public SupplierLevelChange getSupplierLevelChange() {
		return supplierLevelChange;
	}

	public void setSupplierLevelChange(SupplierLevelChange supplierLevelChange) {
		this.supplierLevelChange = supplierLevelChange;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	@Override
	public SupplierLevelChange getModel() {
		// TODO Auto-generated method stub
		return supplierLevelChange;
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

	@Override
	@Action("list")
	public String list() throws Exception {
		// TODO Auto-generated method stub
		List<SupplierLevelChange> supplierLevelChanges = supplierLevelChangeManager.searchlist();
		ActionContext.getContext().put("supplierLevelChanges",supplierLevelChanges);
		ApiFactory.getBussinessLogService().log("等级关系变更", "查看列表", ContextUtils.getSystemId("supplier"));
		return "SUCCESS";
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Action("save")
	public String save() throws Exception {
		// TODO Auto-generated method stub
		String params = Struts2Utils.getParameter("saveParams");
		supplierLevelChangeManager.saveSupplierLevelChange(params);
		List<SupplierLevelChange> supplierLevelChanges = supplierLevelChangeManager.searchlist();
		ActionContext.getContext().put("supplierLevelChanges",supplierLevelChanges);
		logUtilDao.debugLog("保存", params,ContextUtils.getSystemId("supplier"));
		return "list";
	}

}
