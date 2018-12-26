package com.ambition.carmfg.ort.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OrtCustomer;
import com.ambition.carmfg.entity.OrtInspectionItem;
import com.ambition.carmfg.ort.service.OrtCustomerManager;
import com.ambition.carmfg.ort.service.OrtInspectionItemManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:ORT检验项目action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Namespace("/carmfg/ort/ort-base")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/ort/ort-base", type = "redirectAction") })
public class OrtInspectionItemAction extends BaseAction<OrtInspectionItem>{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private OrtInspectionItem inspectionItem;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private OrtInspectionItemManager inspectionItemManager;
	private Page<OrtInspectionItem> page;
	private List<OrtInspectionItem> list;
	private File myFile;
 	//客户信息Id
	private Long customerId;
	private String customerNo;
	private JSONObject params;
	@Autowired
	private OrtCustomerManager customerManager;
	private OrtCustomer customer;
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

	public OrtInspectionItem getInspectionItem() {
		return inspectionItem;
	}

	public void setInspectionItem(OrtInspectionItem inspectionItem) {
		this.inspectionItem = inspectionItem;
	}

	public Page<OrtInspectionItem> getPage() {
		return page;
	}

	public void setPage(Page<OrtInspectionItem> page) {
		this.page = page;
	}

	public List<OrtInspectionItem> getList() {
		return list;
	}

	public void setList(List<OrtInspectionItem> list) {
		this.list = list;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public OrtCustomer getDefectionType() {
		return customer;
	}

	public void setDefectionType(OrtCustomer customer) {
		this.customer = customer;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	public OrtInspectionItem getModel() {
		return inspectionItem;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			inspectionItem = new OrtInspectionItem();
			if(customerId != null && customerId != 0){
				customer = customerManager.getOrtCustomer(customerId);
			}else{
				customer = null;
			}
			inspectionItem.setCompanyId(ContextUtils.getCompanyId());
			inspectionItem.setCreatedTime(new Date());
			inspectionItem.setCreator(ContextUtils.getUserName());
			inspectionItem.setOrtCustomer(customer);
			inspectionItem.setLastModifiedTime(new Date());
			inspectionItem.setLastModifier(ContextUtils.getUserName());
			inspectionItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			inspectionItem = inspectionItemManager.getInspectionItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="ORT检验项目")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			inspectionItem.setLastModifiedTime(new Date());
			inspectionItem.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", inspectionItem.toString());
		}else{
			logUtilDao.debugLog("保存", inspectionItem.toString());
		}
		try {
			inspectionItemManager.saveInspectionItem(inspectionItem);
			renderText(JsonParser.object2Json(inspectionItem));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="ORT检验项目")
	@Override
	public String delete() throws Exception {
		inspectionItemManager.deleteInspectionItem(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除子级",message="ORT检验项目")
	public String deleteSubs() throws Exception {
		if(customerId != null && customerId != 0){
			customer = customerManager.getOrtCustomer(customerId);
		}else{
			customer = null;
		}
		list = inspectionItemManager.listAll(customer);
		for(OrtInspectionItem inspectionItem: list){
			inspectionItemManager.deleteInspectionItem(inspectionItem);
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(customerId != null && customerId != 0){
			customer = customerManager.getOrtCustomer(customerId);
		}else{
			customer = null;
		}
		list = inspectionItemManager.listAll(customer);
		if(list.size() >= 1){
			renderText("have");
		}else{
			renderText("no");
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		if(customerId != null && customerId != 0){
			customer = customerManager.getOrtCustomer(customerId);
		}else{
			customer = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("customerId");
		if(myId != null && !myId.equals("")){
			customerId = Long.valueOf(myId);
			customer = customerManager.getOrtCustomer(customerId);
		}

		page = inspectionItemManager.list(page, customer);		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-ORT检验项目维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="ORT检验项目")
	public String export() throws Exception {
		Page<OrtInspectionItem> page = new Page<OrtInspectionItem>(100000);
		String myId = Struts2Utils.getParameter("customerId");
		if(myId != null && !myId.equals("")){
			customerId = Long.valueOf(myId);
			customer = customerManager.getOrtCustomer(customerId);
		}
		page = inspectionItemManager.list(page, customer);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_ORT_INSPECTION_ITEM"),"inspectionItem"));
		logUtilDao.debugLog("导出", "制造质量管理：基础设置-ORT检验项目维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入",message="ORT检验项目")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(inspectionItemManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}	
	@Action("code-list-datas")
	public String getCodeListByParent() throws Exception {		
		if(customerId != null && customerId != 0){
			customer = customerManager.getOrtCustomer(customerId);
		}else{
			customer = null;
		}
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		if(params!=null){
			page = inspectionItemManager.getCodeByParams(page, params,customer);
		}else{
			page = inspectionItemManager.list(page, customer);
		}
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(OrtInspectionItem inspectionItem : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",inspectionItem.getId());
				map.put("testItem",inspectionItem.getTestItem());
				map.put("testCondition",inspectionItem.getTestCondition());
				map.put("value", inspectionItem.getValue());
				map.put("judgeStandard",inspectionItem.getJudgeStandard());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 测试项目弹框选择
	 * @param component
	 * @return
	 */
	@Action("inspection-item-select")
	public String postionBomSelect() throws Exception {
		String customerNo=Struts2Utils.getParameter("customerNo");
		this.setCustomerNo(customerNo);
		return SUCCESS;
	}
	@Action("inspection-item-select-datas")
	public String getPositionCodeByParent() throws Exception {
		params = convertJsonObject(params);
		String customerNo=Struts2Utils.getParameter("customerNo");
		page = inspectionItemManager.listByParams(page,customerNo);
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(OrtInspectionItem inspectionItem : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",inspectionItem.getId());
				map.put("testItem",inspectionItem.getTestItem());
				map.put("testCondition",inspectionItem.getTestCondition());
				map.put("value",inspectionItem.getValue());
				map.put("judgeStandard",inspectionItem.getJudgeStandard());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	/**
     * 转换json格式
     */
    private JSONObject convertJsonObject(JSONObject params){
        JSONObject resultJson = new JSONObject();
        if(params == null){
            return resultJson;
        }else{
            for(Object key : params.keySet()){
                resultJson.put(key,params.getJSONArray(key.toString()).get(0));
            }
            return resultJson;
        }
    }
}
