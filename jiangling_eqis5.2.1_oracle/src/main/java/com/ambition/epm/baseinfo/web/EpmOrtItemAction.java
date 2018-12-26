package com.ambition.epm.baseinfo.web;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.epm.baseinfo.service.EpmOrtIndicatorManager;
import com.ambition.epm.baseinfo.service.EpmOrtItemManager;
import com.ambition.epm.entity.EpmOrtIndicator;
import com.ambition.epm.entity.EpmOrtItem;
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
import com.opensymphony.xwork2.ActionContext;

@Namespace("/epm/base-info/ort-item")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/epm/base-info/ort-item", type = "redirectAction") })
public class EpmOrtItemAction extends BaseAction<EpmOrtItem>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private EpmOrtItem ortItem;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private EpmOrtItemManager ortItemManager;
	private Page<EpmOrtItem> page;
	private List<EpmOrtItem> list;
	private File myFile;
 	//不良类别Id
	private Long ortIndicatorId;
	@Autowired
	private EpmOrtIndicatorManager ortIndicatorManager;
	private EpmOrtIndicator ortIndicator;
	private String customerNo;//客户编号
	private String productNo;//机种
	private String sampleType;//样品类别
	private Logger log = Logger.getLogger(this.getClass());
	
	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
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

	public EpmOrtItem getOrtItem() {
		return ortItem;
	}

	public void setOrtItem(EpmOrtItem ortItem) {
		this.ortItem = ortItem;
	}

	public Page<EpmOrtItem> getPage() {
		return page;
	}

	public void setPage(Page<EpmOrtItem> page) {
		this.page = page;
	}

	public List<EpmOrtItem> getList() {
		return list;
	}

	public void setList(List<EpmOrtItem> list) {
		this.list = list;
	}

	public Long getOrtIndicatorId() {
		return ortIndicatorId;
	}

	public void setOrtIndicatorId(Long ortIndicatorId) {
		this.ortIndicatorId = ortIndicatorId;
	}

	public EpmOrtIndicator getOrtIndicator() {
		return ortIndicator;
	}

	public void setOrtIndicator(EpmOrtIndicator ortIndicator) {
		this.ortIndicator = ortIndicator;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	public EpmOrtItem getModel() {
		return ortItem;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			ortItem = new EpmOrtItem();
			if(ortIndicatorId != null && ortIndicatorId != 0){
				ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
			}else{
				ortIndicator = null;
			}
			ortItem.setCompanyId(ContextUtils.getCompanyId());
			ortItem.setCreatedTime(new Date());
			ortItem.setCreator(ContextUtils.getUserName());
			ortItem.setOrtIndicator(ortIndicator);
			ortItem.setLastModifiedTime(new Date());
			ortItem.setLastModifier(ContextUtils.getUserName());
			ortItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			ortItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ortItem = ortItemManager.getOrtItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存ORT测试项目")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			ortItem.setLastModifiedTime(new Date());
			ortItem.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", ortItem.toString());
		}else{
			logUtilDao.debugLog("保存", ortItem.toString());
		}
		try {
			ortItemManager.saveOrtItem(ortItem);
			renderText(JsonParser.getRowValue(ortItem));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="ORT测试项目")
	@Override
	public String delete() throws Exception {
		ortItemManager.deleteOrtItem(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除子级",message="ORT测试项目")
	public String deleteSubs() throws Exception {
		if(ortIndicatorId != null && ortIndicatorId != 0){
			ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
		}else{
			ortIndicator = null;
		}
		list = ortItemManager.listAll(ortIndicator);
		for(EpmOrtItem ortItem: list){
			ortItemManager.deleteOrtItem(ortItem);
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(ortIndicatorId != null && ortIndicatorId != 0){
			ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
		}else{
			ortIndicator = null;
		}
		list = ortItemManager.listAll(ortIndicator);
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
		renderMenu();
		if(ortIndicatorId != null && ortIndicatorId != 0){
			ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
		}else{
			ortIndicator = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("ortIndicatorId");
		if(myId != null && !myId.equals("")){
			ortIndicatorId = Long.valueOf(myId);
			ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("ortItem");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = ortItemManager.list(page, code);
		}else{
			page = ortItemManager.list(page, ortIndicator);
		}		
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("select-list")
	public String selectList() throws Exception {
		customerNo = Struts2Utils.getParameter("customerNo");
		productNo =  Struts2Utils.getParameter("productNo");
		sampleType =  Struts2Utils.getParameter("sampleType");
		return SUCCESS;
	}
	@Action("list-select-datas")
	public String getListSelectDatas() throws Exception{
		customerNo = Struts2Utils.getParameter("customerNo");
		productNo =  Struts2Utils.getParameter("productNo");
		sampleType =  Struts2Utils.getParameter("sampleType");
		
		try {
			page = ortItemManager.listPageByParams(page, customerNo,productNo,sampleType);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查找测试项目失败:"+e.getMessage());
		}
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出",message="ORT测试项目")
	public String export() throws Exception {
		Page<EpmOrtItem> page = new Page<EpmOrtItem>(100000);
		String myId = Struts2Utils.getParameter("ortIndicatorId");
		if(myId != null && !myId.equals("")){
			ortIndicatorId = Long.valueOf(myId);
			ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
		}
		page = ortItemManager.list(page, ortIndicator);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"EPM_ORT_ITEM"),"ortItem"));
		logUtilDao.debugLog("导出", "ORT测试项目维护");
		return null;
	}
//	@Action("imports")
//	public String imports() throws Exception {
//		return "imports";
//	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入",message="ORT测试项目")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(ortItemManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	@Action("ort-item-multi-select")
	public String codeBomMultiSelect() throws Exception {
		List<EpmOrtIndicator> ortIndicators = ortIndicatorManager.listAll();
		List<Map<String,Object>> ortIndicatorMaps = new ArrayList<Map<String,Object>>();
		for(EpmOrtIndicator ortIndicator : ortIndicators){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("isLeaf",true);
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",ortIndicator.getId());
			attrMap.put("customerNo",ortIndicator.getCustomerNo());
			attrMap.put("model",ortIndicator.getModel());
			attrMap.put("samplType",ortIndicator.getSamplType());
			map.put("attr",attrMap);
			ortIndicatorMaps.add(map);
		}
		ActionContext.getContext().put("ortIndicatorMaps",JSONArray.fromObject(ortIndicatorMaps).toString());	
		return SUCCESS;
	}
	
	@Action("item-list-datas")
	public String getCodeListByParent() throws Exception {		
		if(ortIndicatorId != null && ortIndicatorId != 0){
			ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
		}else{
			ortIndicator = null;
		}
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		if(params!=null){
			page = ortItemManager.getCodeByParams(page, params,ortIndicator);
		}else{
			page = ortItemManager.list(page, ortIndicator);
		}
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(EpmOrtItem ortItem : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",ortItem.getId());
				map.put("itemName",ortItem.getItemName());
				map.put("condition",ortItem.getCondition());
				map.put("count",ortItem.getCount());
				map.put("standardGreen",ortItem.getStandardGreen());
				map.put("standardYellow",ortItem.getStandardYellow());
				map.put("standardRed",ortItem.getStandardRed());
				map.put("customerNo", ortItem.getOrtIndicator().getCustomerNo());
				map.put("model",ortItem.getOrtIndicator().getModel());
				map.put("samplType",ortItem.getOrtIndicator().getSamplType());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("ortItem-import")
	public String imports() throws Exception {
		ortIndicatorId = Long.valueOf(Struts2Utils.getParameter("ortIndicatorId"));
		return SUCCESS;
	}
	
	@Action("ortItem-import-datas")
	public String importDatas1() throws Exception {
		try {
			if(myFile != null){
				renderHtml(ortItemManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载测试项目模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/epm-ortItem.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "测试项目模板.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return null;
	}	
}
