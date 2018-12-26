package com.ambition.aftersales.baseinfo.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/aftersales/base-info/customer")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/base-info/customer", type = "redirectAction") })
public class CustomerListAction extends BaseAction<CustomerList>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private CustomerList customerList;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private CustomerListManager customerListManager;
	private Page<CustomerList> page;
	private JSONObject params;
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

	public CustomerList getCustomerList() {
		return customerList;
	}
	public void setCustomerList(CustomerList customerList) {
		this.customerList = customerList;
	}
	public Page<CustomerList> getPage() {
		return page;
	}
	public void setPage(Page<CustomerList> page) {
		this.page = page;
	}

	@Override
	public CustomerList getModel() {
		return customerList;
	}
	
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	@Override
	protected void prepareModel() throws Exception{
		if(id == null){
			customerList = new CustomerList();
			customerList.setCompanyId(ContextUtils.getCompanyId());
			customerList.setCreatedTime(new Date());
			customerList.setCreator(ContextUtils.getUserName());
			customerList.setLastModifiedTime(new Date());
			customerList.setLastModifier(ContextUtils.getUserName());
			customerList.setBusinessUnitName(ContextUtils.getSubCompanyName());
			customerList.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			customerList = customerListManager.getCustomerList(id);
		}
	}
	@Action("customer-delete")
	@LogInfo(optType="删除",message="客户清单维护")
	@Override
	public String delete() throws Exception {
		try{
			customerListManager.deleteCustomerList(deleteIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("customer-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("customer-list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}
	@Action("customer-save")
	@LogInfo(optType="保存",message="客户清单维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			customerList.setLastModifiedTime(new Date());
			customerList.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", customerList.toString());
		}else{
			logUtilDao.debugLog("保存", customerList.toString());
		}
		try{
			customerListManager.saveCustomerList(customerList);
			this.renderText(JsonParser.object2Json(customerList));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("customer-list-datas")
	public String getListDatas() throws Exception {
		try {
			page = customerListManager.list(page);
			this.renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "售后质量管理：基础设置-客户清单维护");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="客户清单维护")
	public String export() throws Exception {
		Page<CustomerList> page = new Page<CustomerList>(100000);
		page = customerListManager.list(page);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"AFS_CUSTOMER_LIST"),"customerList"));
		logUtilDao.debugLog("导出", "售后质量管理：基础设置-客户清单维护");
		return null;
	}
	


}
