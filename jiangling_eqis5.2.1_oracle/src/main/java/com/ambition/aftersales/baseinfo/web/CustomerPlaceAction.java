package com.ambition.aftersales.baseinfo.web;

import java.io.File;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.baseinfo.service.CustomerPlaceManager;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.CustomerPlace;
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
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/aftersales/base-info/customer")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/base-info/customer", type = "redirectAction") })
public class CustomerPlaceAction extends BaseAction<CustomerPlace>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private CustomerPlace customerPlace;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private CustomerPlaceManager customerPlaceManager;
	private Page<CustomerPlace> page;
	private List<CustomerPlace> list;
	private File myFile;
 	//不良类别Id
	private Long customerListId;
	@Autowired
	private CustomerListManager customerListManager;
	private CustomerList customerList;
	private String customerName;
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

	public CustomerPlace getCustomerPlace() {
		return customerPlace;
	}

	public void setCustomerPlace(CustomerPlace customerPlace) {
		this.customerPlace = customerPlace;
	}

	public Page<CustomerPlace> getPage() {
		return page;
	}

	public void setPage(Page<CustomerPlace> page) {
		this.page = page;
	}

	public List<CustomerPlace> getList() {
		return list;
	}

	public void setList(List<CustomerPlace> list) {
		this.list = list;
	}

	public Long getCustomerListId() {
		return customerListId;
	}

	public void setCustomerListId(Long customerListId) {
		this.customerListId = customerListId;
	}

	public CustomerList getCustomerList() {
		return customerList;
	}

	public void setCustomerList(CustomerList customerList) {
		this.customerList = customerList;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	public CustomerPlace getModel() {
		return customerPlace;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			customerPlace = new CustomerPlace();
			if(customerListId != null && customerListId != 0){
				customerList = customerListManager.getCustomerList(customerListId);
			}else{
				customerList = null;
			}
			customerPlace.setCompanyId(ContextUtils.getCompanyId());
			customerPlace.setCreatedTime(new Date());
			customerPlace.setCreator(ContextUtils.getUserName());
			customerPlace.setCustomerList(customerList);
			customerPlace.setLastModifiedTime(new Date());
			customerPlace.setLastModifier(ContextUtils.getUserName());
			customerPlace.setBusinessUnitName(ContextUtils.getSubCompanyName());
			customerPlace.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			customerPlace = customerPlaceManager.getCustomerPlace(id);
		}
	}
	
	@Action("place-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("place-save")
	@LogInfo(optType="保存",message="客户场地维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			customerPlace.setLastModifiedTime(new Date());
			customerPlace.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", customerPlace.toString());
		}else{
			logUtilDao.debugLog("保存", customerPlace.toString());
		}
		try {
			customerPlaceManager.saveCustomerPlace(customerPlace);
			renderText(JsonParser.object2Json(customerPlace));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("place-delete")
	@LogInfo(optType="删除",message="客户场地维护")
	@Override
	public String delete() throws Exception {
		customerPlaceManager.deleteCustomerPlace(deleteIds);
		return null;
	}

	@Action("place-delete-subs")
	@LogInfo(optType="删除级联",message="客户场地维护")
	public String deleteSubs() throws Exception {
		if(customerListId != null && customerListId != 0){
			customerList = customerListManager.getCustomerList(customerListId);
		}else{
			customerList = null;
		}
		list = customerPlaceManager.listAll(customerList);
		for(CustomerPlace customerPlace: list){
			customerPlaceManager.deleteCustomerPlace(customerPlace);
		}
		return null;
	}
	
	@Action("place-list")
	@Override
	public String list() throws Exception {
		renderMenu();
		if(customerListId != null && customerListId != 0){
			customerList = customerListManager.getCustomerList(customerListId);
		}else{
			customerList = null;
		}
		return SUCCESS;
	}
	
	@Action("place-list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("customerListId");
		if(myId != null && !myId.equals("")){
			customerListId = Long.valueOf(myId);
			customerList = customerListManager.getCustomerList(customerListId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("customerPlace");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = customerPlaceManager.list(page, code);
		}else{
			page = customerPlaceManager.list(page, customerList);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "售后质量管理：基础设置-客户场地维护");
		return null;
	}
	@Action("exportCode3")
	@LogInfo(optType="导出",message="客户场地")
	public String export() throws Exception {
		Page<CustomerPlace> page = new Page<CustomerPlace>(100000);
		String myId = Struts2Utils.getParameter("customerListId");
		if(myId != null && !myId.equals("")){
			customerListId = Long.valueOf(myId);
			customerList = customerListManager.getCustomerList(customerListId);
		}
		page = customerPlaceManager.list(page, customerList);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"AFS_CUSTOMER_PLACE"),"customerPlace"));
		logUtilDao.debugLog("导出", "售后质量管理：基础设置-客户场地维护");
		return null;
	}
    @Action("place-select")
    public String selectByProcess() throws Exception {
    	JSONObject result = new JSONObject();
        try{
            String customerName = Struts2Utils.getParameter("customerName");
            List<CustomerPlace> customerPlaces = customerPlaceManager.listPlace();
            String places = "";
            for (CustomerPlace customerPlace : customerPlaces) {
            	if(customerPlace.getCustomerList().getCustomerName().equals(customerName)){
	            	if(places.length()==0){
	            		places = customerPlace.getCustomerPlace();
					}else{
						places += "," +  customerPlace.getCustomerPlace();
					}
			}
            }
            result.put("places",  places);
            result.put("error", false);
        }catch(Exception e){
            result.put("error", true);
            result.put("message", "查找客户场地失败");
        }
        this.renderText(JsonParser.object2Json(result));
        return null;
    }	
}
