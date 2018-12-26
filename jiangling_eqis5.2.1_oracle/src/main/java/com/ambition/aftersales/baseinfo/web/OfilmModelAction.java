package com.ambition.aftersales.baseinfo.web;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.baseinfo.service.CustomerPlaceManager;
import com.ambition.aftersales.baseinfo.service.OfilmModelManager;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.CustomerPlace;
import com.ambition.aftersales.entity.OfilmModel;
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
public class OfilmModelAction extends BaseAction<OfilmModel>{

	private static final long serialVersionUID = 1L;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private OfilmModel ofilmModel;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private OfilmModelManager ofilmModelManager;
	@Autowired
	private CustomerPlaceManager customerPlaceManager;
	private Page<OfilmModel> page;
	private List<OfilmModel> list;
	private List<CustomerPlace> listPlace;
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

	public OfilmModel getOfilmModel() {
		return ofilmModel;
	}

	public void setOfilmModel(OfilmModel ofilmModel) {
		this.ofilmModel = ofilmModel;
	}

	public Page<OfilmModel> getPage() {
		return page;
	}

	public void setPage(Page<OfilmModel> page) {
		this.page = page;
	}

	public List<OfilmModel> getList() {
		return list;
	}

	public void setList(List<OfilmModel> list) {
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

	public List<CustomerPlace> getListPlace() {
		return listPlace;
	}

	public void setListPlace(List<CustomerPlace> listPlace) {
		this.listPlace = listPlace;
	}

	@Override
	public OfilmModel getModel() {
		return ofilmModel;
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
			ofilmModel = new OfilmModel();
			if(customerListId != null && customerListId != 0){
				customerList = customerListManager.getCustomerList(customerListId);
			}else{
				customerList = null;
			}
			ofilmModel.setCompanyId(ContextUtils.getCompanyId());
			ofilmModel.setCreatedTime(new Date());
			ofilmModel.setCreator(ContextUtils.getUserName());
			ofilmModel.setCustomerList(customerList);
			ofilmModel.setLastModifiedTime(new Date());
			ofilmModel.setLastModifier(ContextUtils.getUserName());
			ofilmModel.setBusinessUnitName(ContextUtils.getSubCompanyName());
			ofilmModel.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ofilmModel = ofilmModelManager.getOfilmModel(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="不良类别维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			ofilmModel.setLastModifiedTime(new Date());
			ofilmModel.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", ofilmModel.toString());
		}else{
			logUtilDao.debugLog("保存", ofilmModel.toString());
		}
		try {
			ofilmModelManager.saveOfilmModel(ofilmModel);
			renderText(JsonParser.object2Json(ofilmModel));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="客户清单维护")
	@Override
	public String delete() throws Exception {
		ofilmModelManager.deleteOfilmModel(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除级联",message="客户清单维护")
	public String deleteSubs() throws Exception {
		if(customerListId != null && customerListId != 0){
			customerList = customerListManager.getCustomerList(customerListId);
		}else{
			customerList = null;
		}
		list = ofilmModelManager.listAll(customerList);
		for(OfilmModel ofilmModel: list){
			ofilmModelManager.deleteOfilmModel(ofilmModel);
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(customerListId != null && customerListId != 0){
			customerList = customerListManager.getCustomerList(customerListId);
		}else{
			customerList = null;
		}
		list = ofilmModelManager.listAll(customerList);
		listPlace = customerPlaceManager.listAll(customerList);
		if(list.size() >= 1||listPlace.size() >= 1){
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
		if(customerListId != null && customerListId != 0){
			customerList = customerListManager.getCustomerList(customerListId);
		}else{
			customerList = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("customerListId");
		if(myId != null && !myId.equals("")){
			customerListId = Long.valueOf(myId);
			customerList = customerListManager.getCustomerList(customerListId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("ofilmModel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = ofilmModelManager.list(page, code);
		}else{
			page = ofilmModelManager.list(page, customerList);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "售后质量管理：基础设置-欧菲机种维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="欧菲机种维护")
	public String export() throws Exception {
		Page<OfilmModel> page = new Page<OfilmModel>(100000);
		String myId = Struts2Utils.getParameter("customerListId");
		if(myId != null && !myId.equals("")){
			customerListId = Long.valueOf(myId);
			customerList = customerListManager.getCustomerList(customerListId);
		}
		page = ofilmModelManager.list(page, customerList);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"AFS_OFILM_MODEL"),"ofilmModel"));
		logUtilDao.debugLog("导出", "售后质量管理：基础设置-欧菲机种维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		customerListId = Long.valueOf(Struts2Utils.getParameter("customerListId"));
		return "imports";
	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入",message="欧菲机种维护")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(ofilmModelManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	/**
	  * 方法名: 下载欧菲机种导入模板
	  * <p>功能说明：下载欧菲机种导入模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载欧菲机种导入模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/afs-ofilm-model-template.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "欧菲机种导入模板.xls";
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
	/**
	 * 问题点弹框选择
	 * @param component
	 * @return
	 */
	@Action("model-select")
	public String modelSelect() throws Exception {
		String customerName=Struts2Utils.getParameter("customerName");
		this.setCustomerName(customerName);
		return SUCCESS;
	}
	/**
	 * 问题点弹框选择
	 * @param component
	 * @return
	 */
	@Action("model-select-ofg")
	public String ofgModelSelect() throws Exception {
		String customerName=Struts2Utils.getParameter("customerName");
		this.setCustomerName(customerName);
		return SUCCESS;
	}
	@Action("model-select-list-datas")
	public String getPositionCodeByParent() throws Exception {		
		try {
			params = convertJsonObject(params);
			String customerName=Struts2Utils.getParameter("customerName");
			page = ofilmModelManager.listByParams(page,customerName);
			this.renderText(PageUtils.pageToJson(page));
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
