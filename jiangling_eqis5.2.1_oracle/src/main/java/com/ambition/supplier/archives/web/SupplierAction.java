package com.ambition.supplier.archives.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.archives.service.SupplyProductManager;
import com.ambition.supplier.develop.services.SupplierDevelopManager;
import com.ambition.supplier.entity.Certificate;
import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.Evaluate;
import com.ambition.supplier.entity.ProductExploitationRecord;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierDevelop;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.evaluate.service.EvaluateManager;
import com.ambition.supplier.manager.service.ProductExploitationRecordManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.annotations.Conversion;

@Namespace("/supplier/archives")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/archives", type = "redirectAction") })
@Conversion
public class SupplierAction extends CrudActionSupport<Supplier> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<Supplier> page;
	private Page<SupplyProduct> productPage;
	private Supplier supplier;
	@Autowired
	private SupplyProductManager supplyProductManager;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierDevelopManager supplierDevelopManager;
	private File myFile;
	
	private Boolean multiselect = false;
	
	private JSONObject params;//查询参数
	
	private String state;//状态
	
 	@Autowired
	private SupplierManager supplierManager;
 	
 	@Autowired
 	private EstimateModelManager estimateModelManager;

 	@Autowired
 	private ProductExploitationRecordManager productExploitationRecordManager;
 	
 	
 	private GridColumnInfo supplyProductGridColumnInfo;
 	
 	private GridColumnInfo certificateGridColumnInfo;

 	private List<DynamicColumnDefinition> dynamicColumnDefinitions = new ArrayList<DynamicColumnDefinition>();

	public List<DynamicColumnDefinition> getDynamicColumnDefinitions() {
		return dynamicColumnDefinitions;
	}

	public void setDynamicColumnDefinitions(
			List<DynamicColumnDefinition> dynamicColumnDefinitions) {
		this.dynamicColumnDefinitions = dynamicColumnDefinitions;
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

	public Page<SupplyProduct> getProductPage() {
		return productPage;
	}

	public void setProductPage(Page<SupplyProduct> productPage) {
		this.productPage = productPage;
	}

	public Page<Supplier> getPage() {
		return page;
	}

	public void setPage(Page<Supplier> page) {
		this.page = page;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Supplier getModel() {
		return supplier;
	}
	
	public GridColumnInfo getSupplyProductGridColumnInfo() {
		return supplyProductGridColumnInfo;
	}

	public void setSupplyProductGridColumnInfo(
			GridColumnInfo supplyProductGridColumnInfo) {
		this.supplyProductGridColumnInfo = supplyProductGridColumnInfo;
	}

	public GridColumnInfo getCertificateGridColumnInfo() {
		return certificateGridColumnInfo;
	}

	public void setCertificateGridColumnInfo(
			GridColumnInfo certificateGridColumnInfo) {
		this.certificateGridColumnInfo = certificateGridColumnInfo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplier = new Supplier();
			supplier.setCreatedTime(new Date());
			supplier.setCompanyId(ContextUtils.getCompanyId());
			supplier.setCreatorName(ContextUtils.getUserName());
			supplier.setCreator(ContextUtils.getLoginName());
			supplier.setLastModifiedTime(new Date());
			supplier.setLastModifier(ContextUtils.getUserName());
			supplier.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplier.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			supplier = supplierManager.getSupplier(id);
			Struts2Utils.getRequest().setAttribute("hisEstimateModelId",supplier.getEstimateModelId());
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		try {
			supplyProductGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SUPPLIER_SUPPLY_PRODUCT");
			JSONArray jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColModel());
			jsonArray.add(0, "{name:'act',index:'act', width:50,align:\"center\",sortable:false}");
			supplyProductGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColNames());
			jsonArray.add(0,"操作");
			supplyProductGridColumnInfo.setColNames(jsonArray.toString());
			
			certificateGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SUPPLIER_CERTIFICATE");
			StringBuffer models = new StringBuffer(certificateGridColumnInfo.getColModel());
			models.insert(1,"{name:'act',index:'act', width:50,align:\"center\",sortable:false},");
			certificateGridColumnInfo.setColModel(models.toString());
			StringBuffer colNames = new StringBuffer(certificateGridColumnInfo.getColNames());
			colNames.insert(1,"'操作',");
			certificateGridColumnInfo.setColNames(colNames.toString());
			
			//地区
			List<Option> regions = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_region");
			ActionContext.getContext().put("regions",regions);
			ActionContext.getContext().put("parentId",id);
			
			SupplierDevelop supplierDevelop = supplierDevelopManager.getSupplierDevelopByName(supplier.getName());
			ActionContext.getContext().put("evaluate",supplierDevelop);
		} catch (Exception e) {
			log.error(e);
		}
		return SUCCESS;
	}
	@Action("base-info")
	public String baseInfo() throws Exception {
		try {
			supplier = supplierManager.getSupplier(id);
		} catch (Exception e) {
			log.error(e);
		}
		ActionContext.getContext().put("supplier",supplier);

		/*supplyProductGridColumnInfo = mmsUtil.getGridColumnInfo("SUPPLIER_SUPPLY_PRODUCT");
		JSONArray jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColModel());
		jsonArray.add(0, "{name:'act',index:'act', width:50,align:\"center\",sortable:false}");
		supplyProductGridColumnInfo.setColModel(jsonArray.toString());
		jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColNames());
		jsonArray.add(0,"操作");
		supplyProductGridColumnInfo.setColNames(jsonArray.toString());
		
		certificateGridColumnInfo = mmsUtil.getGridColumnInfo("SUPPLIER_CERTIFICATE");
		try {
			jsonArray = JSONArray.fromObject(certificateGridColumnInfo.getColModel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jsonArray.add(0, "{name:'act',index:'act', width:50,align:\"center\",sortable:false}");
		certificateGridColumnInfo.setColModel(jsonArray.toString());
		jsonArray = JSONArray.fromObject(certificateGridColumnInfo.getColNames());
		jsonArray.add(0,"操作");
		certificateGridColumnInfo.setColNames(jsonArray.toString());*/
		
		supplyProductGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SUPPLIER_SUPPLY_PRODUCT");
		JSONArray jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColModel());
		jsonArray.add(0, "{name:'act',index:'act', width:50,align:\"center\",sortable:false}");
		supplyProductGridColumnInfo.setColModel(jsonArray.toString());
		jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColNames());
		jsonArray.add(0,"操作");
		supplyProductGridColumnInfo.setColNames(jsonArray.toString());
		
		certificateGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SUPPLIER_CERTIFICATE");
		StringBuffer models = new StringBuffer(certificateGridColumnInfo.getColModel());
		models.insert(1,"{name:'act',index:'act', width:50,align:\"center\",sortable:false},");
		certificateGridColumnInfo.setColModel(models.toString());
		StringBuffer colNames = new StringBuffer(certificateGridColumnInfo.getColNames());
		colNames.insert(1,"'操作',");
		certificateGridColumnInfo.setColNames(colNames.toString());
		
		//企业性质
		List<Option> enterprisePropertys = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_enterpriseProperty");
		ActionContext.getContext().put("enterprisePropertys",enterprisePropertys);
		
		return SUCCESS;
	}
	
	@Action("enterprise-info")
	public String enterpriseInfo() throws Exception {
		try {
			supplier = supplierManager.getSupplier(id);
		} catch (Exception e) {
			log.error(e);
		}
		ActionContext.getContext().put("supplier",supplier);

//		supplyProductGridColumnInfo = mmsUtil.getGridColumnInfo("SUPPLIER_SUPPLY_PRODUCT");
//		JSONArray jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColModel());
//		jsonArray.add(0, "{name:'act',index:'act', width:50,align:\"center\",sortable:false}");
//		supplyProductGridColumnInfo.setColModel(jsonArray.toString());
//		jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColNames());
//		jsonArray.add(0,"操作");
//		supplyProductGridColumnInfo.setColNames(jsonArray.toString());
//		
//		certificateGridColumnInfo = mmsUtil.getGridColumnInfo("SUPPLIER_CERTIFICATE");
//		try {
//			jsonArray = JSONArray.fromObject(certificateGridColumnInfo.getColModel());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		jsonArray.add(0, "{name:'act',index:'act', width:50,align:\"center\",sortable:false}");
//		certificateGridColumnInfo.setColModel(jsonArray.toString());
//		jsonArray = JSONArray.fromObject(certificateGridColumnInfo.getColNames());
//		jsonArray.add(0,"操作");
//		certificateGridColumnInfo.setColNames(jsonArray.toString());
		
		supplyProductGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SUPPLIER_SUPPLY_PRODUCT");
		JSONArray jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColModel());
		jsonArray.add(0, "{name:'act',index:'act', width:50,align:\"center\",sortable:false}");
		supplyProductGridColumnInfo.setColModel(jsonArray.toString());
		jsonArray = JSONArray.fromObject(supplyProductGridColumnInfo.getColNames());
		jsonArray.add(0,"操作");
		supplyProductGridColumnInfo.setColNames(jsonArray.toString());
		
		certificateGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SUPPLIER_CERTIFICATE");
		StringBuffer models = new StringBuffer(certificateGridColumnInfo.getColModel());
		models.insert(1,"{name:'act',index:'act', width:50,align:\"center\",sortable:false},");
		certificateGridColumnInfo.setColModel(models.toString());
		StringBuffer colNames = new StringBuffer(certificateGridColumnInfo.getColNames());
		colNames.insert(1,"'操作',");
		certificateGridColumnInfo.setColNames(colNames.toString());
		
		//物料类别
		List<Option> materialTypes = ApiFactory.getSettingService().getOptionsByGroupCode("supply_materialType");
		ActionContext.getContext().put("materialOptions",convertToOptions(materialTypes));
				
		return SUCCESS;
	}
	
	@Action("view-info")
	public String viewInfo() throws Exception {
		try {
			supplier = supplierManager.getSupplier(id);
			ActionContext.getContext().getValueStack().push(supplier);
		} catch (Exception e) {
			log.error(e);
		}
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
//		if(supplier.getEstimateModelName() != null){
//			EstimateModel estimateModel = estimateModelManager.getEstimateModelByName(supplier.getEstimateModelName(),null,EvaluatingIndicator.TYPE_QUARTER);
//			if(estimateModel == null){
//				createErrorMessage("保存失败,评价模型不存在!");
//				return null;
//			}else{
//				supplier.setEstimateModelId(estimateModel.getId());
//				supplier.setEstimateModelName(estimateModel.getName());
//			}
//		}
		try {
			if(id == null){
				supplierManager.saveSupplier(supplier);
				logUtilDao.debugLog("保存", supplier.toString());
			}else{
				if(supplier != null){
					supplier.setLastModifiedTime(new Date());
					supplier.setLastModifier(ContextUtils.getUserName());
					supplierManager.saveSupplier(supplier);
					logUtilDao.debugLog("修改", supplier.toString());
				}else{
					throw new RuntimeException("供应商为空!");
				}
			}
			this.renderText(JsonParser.getRowValue(supplier));
		} catch (Exception e) {
			log.error("保存失败",e);
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				supplierManager.deleteSupplier(deleteIds);
			} catch (Exception e) {
				log.error(e);
				renderText("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	@LogInfo(optType="供应商台帐页面",message="供应商列表")
	public String list() throws Exception {
		//物料类别
		List<Option> materialTypes = ApiFactory.getSettingService().getOptionsByGroupCode("supply_materialType");
		ActionContext.getContext().put("materialTypes",materialTypes);
		
		//重要度
		List<Option> importances = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_importance");
		ActionContext.getContext().put("importances",importances);
		ActionContext.getContext().put("importanceOptions",convertToOptions(importances));
		
		//地区
		List<Option> regions = ApiFactory.getSettingService().getOptionsByGroupCode("region");
		ActionContext.getContext().put("regionOptions",convertToOptions(regions));
		
		//企业性质
		List<Option> enterprisePropertys = ApiFactory.getSettingService().getOptionsByGroupCode("enterpriseProperty");
		ActionContext.getContext().put("enterprisePropertyOptions",convertToOptions(enterprisePropertys));
				
		//评价模型
		List<EstimateModel> estimateModels = estimateModelManager.getTopEstimateModels();
		StringBuffer sb = new StringBuffer("");
		for(EstimateModel estimateModel : estimateModels){
			if(sb.length() > 0){
				sb.append(";");
			}
			sb.append(estimateModel.getName() + ":" + estimateModel.getName());
		}
		ActionContext.getContext().put("estimateModelOptions",sb.toString());

		return SUCCESS;
	}
	
	@Action("list-datas")
	@LogInfo(optType="查询",message="供应商台帐查询")
	public String getSuppliers() throws Exception {
		try{
			page = supplierManager.searchByPage(page,state);
			renderText(PageUtils.pageToJson(page,"SUPPLIER_SUPPLIER"));
		}catch (Exception e) {
			log.error(e);
		}
//		logUtilDao.debugLog("查询", "供应商质量管理：供应商台帐");
		return null;
	}
	//潜在供应商
	@Action("potential-list-datas")
	public String getPotentialSuppliers() throws Exception {
		state = Supplier.STATE_POTENTIAL;
		try{
			page = supplierManager.searchByPage(page,state);
			
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		logUtilDao.debugLog("查询", "供应商质量管理：潜在供应商名录");
		return null;
	}
	//准供应商
	@Action("allow-list-datas")
	public String getAllowSuppliers() throws Exception {
		state = Supplier.STATE_ALLOW;
		try{
			page = supplierManager.searchByPage(page,state);
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		logUtilDao.debugLog("查询", "供应商质量管理：准供应商名录");
		return null;
	}
	//合格供应商
	@Action("qualified-list-datas")
	public String getQualifiedSuppliers() throws Exception {
		state = Supplier.STATE_QUALIFIED;
		try{
			page = supplierManager.searchByPage(page,state);
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		logUtilDao.debugLog("查询", "供应商质量管理：合格供应商名录");
		return null;
	}
	//已淘汰供应商
	@Action("eliminated-list-datas")
	public String getEliminatedSuppliers() throws Exception {
		state = Supplier.STATE_ELIMINATED;
		try{
			page = supplierManager.searchByPage(page,state);
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		logUtilDao.debugLog("查询", "供应商质量管理：已淘汰供应商名录");
		return null;
	}
	//淘汰供应商
	@Action("wash-out-supplier")
	public String washOutSupplier() throws Exception {
		try {
			supplierManager.washOutSupplier(deleteIds);
			renderText("");
		} catch (Exception e) {
			log.error(e);
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	//恢复供应商为合格
	@Action("resume-supplier")
	public String resumeSupplier() throws Exception {
		try {
			supplierManager.resumeSupplier(deleteIds);
			renderText("");
		} catch (Exception e) {
			log.error(e);
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	/**
	 * 导出
	 * @return
	 * @throws Exception 
	 */
	@Action("exports")
	public String exports() throws Exception {
		page = supplierManager.searchByPage(new Page<Supplier>(Integer.MAX_VALUE),state);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "SUPPLIER_SUPPLIER_QUALIFIED"),"供应商台帐"));
		return null;
	}
	/**
	 * 供应的产品
	 * @return
	 * @throws Exception
	 */
	@Action("product-datas")
	public String getProductDatas() throws Exception {
		Supplier supplier = supplierManager.getSupplier(id);
		final Supplier s = supplier;
		JSONObject jsonPage = new JSONObject();
		if(supplier != null){
			List<Object> result = new ArrayList<Object>();
			for(SupplyProduct supplyProduct : supplier.getSupplyProducts()){
				String applyState = null;
				JSONObject jsonObject = JSONObject.fromObject(JsonParser.object2Json(supplyProduct));

//				ProductExploitationRecord productExploitationRecord = productExploitationRecordManager.getProductApplyStateBySupplierAndProductBom(s,supplyProduct.getCode());
//				if(productExploitationRecord==null){
//					jsonObject.put("applyState",SupplyProduct.APPLYSTATE_DEFAULT);
//				}else if(supplyProduct.getCode().equals(productExploitationRecord.getCode())){
//					jsonObject.put("applyState",productExploitationRecord.getApplyState());
//				}
			
//				if(supplyProduct.getAdmissionDate() != null){
//					jsonObject.put("admissionDate",sdf.format(supplyProduct.getAdmissionDate()));
//				}
				result.add(jsonObject);
			}
			jsonPage.put("rows",result);
		}
		renderText(jsonPage.toString());
		return null;
	}
	
	/**
	 * 保存供应的产品
	 * @return
	 * @throws Exception
	 */
	@Action("save-product")
	public String saveProductDatas() throws Exception {
		Supplier supplier = supplierManager.getSupplier(id);
		if(supplier == null){
			createErrorMessage("供应商不存在,可能已经被删除!");
		}else{
			try{
				Map<String,Object> result = supplierManager.saveSupplyProduct(supplier,params);
				result.put("message","保存成功！");
				renderText(JSONObject.fromObject(result).toString());
			}catch(Exception e){
				log.error(e);
				createErrorMessage("保存失败：" + e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * 体系证书
	 * @return
	 * @throws Exception
	 */
	@Action("certificate-datas")
	public String getCertificateDatas() throws Exception {
		Supplier supplier = supplierManager.getSupplier(id);
		JSONObject jsonPage = new JSONObject();
		if(supplier != null){
			List<Object> result = new ArrayList<Object>();
			for(Certificate certificate : supplier.getCertificates()){
				JSONObject jsonObject = JSONObject.fromObject(JsonParser.object2Json(certificate));
				if(certificate.getCertificationDate() != null){
					jsonObject.put("certificationDate",DateUtil.formateDateStr(certificate.getCertificationDate()));
				}
				if(certificate.getInvalidationDate() != null){
					jsonObject.put("invalidationDate",DateUtil.formateDateStr(certificate.getInvalidationDate()));
				}
				result.add(jsonObject);
			}
			jsonPage.put("rows",result);
		}
		renderText(jsonPage.toString());
		return null;
	}
	
	/**
	 * 保存体系证书
	 * @return
	 * @throws Exception
	 */
	@Action("save-certificate")
	public String saveCertificateDatas() throws Exception {
		Supplier supplier = supplierManager.getSupplier(id);
		if(supplier == null){
			createErrorMessage("供应商不存在,可能已经被删除!");
		}else{
			try{
				Map<String,Object> result = supplierManager.saveCertificate(supplier,params);
				result.put("message","保存成功！");
				renderText(JSONObject.fromObject(result).toString());
			}catch(Exception e){
				log.error(e);
				createErrorMessage("保存失败：" + e.getMessage());
			}
		}
		return null;
	}
	
	@Action("select-supplier")
	public String selectSupplier() throws Exception {
		@SuppressWarnings("rawtypes")
		List options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_region");
		ActionContext.getContext().put("regions",options);
		options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_enterpriseProperty");
		ActionContext.getContext().put("properties",options);
		options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_importance");
		ActionContext.getContext().put("importances",options);
		return SUCCESS;
	}
	
	@Action("select-supplier-datas")
	public String selectSupplierDatas() throws Exception {
		try{
			params = CommonUtil.convertJsonObject(params);
			page = supplierManager.searchByPage(page,params,state);
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		logUtilDao.debugLog("选择供应商", "供应商质量管理：查询供应商");
		return null;
	}
	
	@Action("import-supplier-material-form")
	public String importSupplierMaterialForm() throws Exception {
		return SUCCESS;
	}
	@Action("import-supplier-material")
	public String importSuppliersMaterial() throws Exception {
		try {
			if(myFile != null){
				renderHtml(supplierManager.importSuppliersMaterial(myFile));
			}
		} catch (Exception e) {
			log.error(e);
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	@Action("download-supplier-template")
	public String downloadSupplierTemplate() throws Exception {
		supplierManager.createSupplierTemplate();
		return null;
	}
	
	@Action("select-supply-products")
	public String selectSupplyProducts() throws Exception {
		DynamicColumnDefinition dynamicColumnDefinition = new DynamicColumnDefinition();
		dynamicColumnDefinition.setColName("开发状态");
		dynamicColumnDefinition.setColWidth("90");
		dynamicColumnDefinition.setEditable(false);
		dynamicColumnDefinition.setName("applyState");
		dynamicColumnDefinition.setVisible(true);
		dynamicColumnDefinitions.add(dynamicColumnDefinition);
		dynamicColumnDefinition = new DynamicColumnDefinition();
		dynamicColumnDefinition.setColName("错误提示");
		dynamicColumnDefinition.setColWidth("90");
		dynamicColumnDefinition.setEditable(false);
		dynamicColumnDefinition.setName("errorMessage");
		dynamicColumnDefinition.setVisible(false);
		dynamicColumnDefinitions.add(dynamicColumnDefinition);
		dynamicColumnDefinition = new DynamicColumnDefinition();
		dynamicColumnDefinition.setColName("　");
		dynamicColumnDefinition.setColWidth("240");
		dynamicColumnDefinition.setEditable(false);
		dynamicColumnDefinition.setName("showErrorMessage");
		dynamicColumnDefinition.setVisible(true);
		dynamicColumnDefinitions.add(dynamicColumnDefinition);
		ActionContext.getContext().put("currentNode",Struts2Utils.getParameter("currentNode"));
		return SUCCESS;
	}
	
	/**
	 * 供应的产品
	 * @return
	 * @throws Exception
	 */
	@Action("select-supply-products-datas")
	public String selectSupplyProductsDatas() throws Exception {
		Supplier supplier = supplierManager.getSupplier(id);
		final Supplier s = supplier;
		Page<SupplyProduct> supplyProductPage = new Page<SupplyProduct>(Integer.MAX_VALUE);
		supplyProductPage.setResult(supplier.getSupplyProducts());
		supplyProductPage.setTotalCount(supplier.getSupplyProducts().size());
		supplyProductPage.setPageNo(1);
		try {
			renderText(PageUtils.dynamicPageToJson(supplyProductPage,new DynamicColumnValues(){
				public void addValuesTo(List<Map<String, Object>> result) {
					String currentNode = Struts2Utils.getParameter("currentNode");
					for(Map<String, Object> map:result){
						supplierManager.setSupplyProductState(s,currentNode,map);
					}
				}
			}));
		} catch (Exception e) {
			log.error("选择供应商物料失败!",e);
		}
		return null;
	}
	//停用供应商
	@Action("update-stop")
	public String updateStop() throws Exception {
		try {
			supplierManager.updateStop(deleteIds);
			renderText("");
		} catch (Exception e) {
			log.error(e);
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	//禁用供应商
	@Action("update-disable")
	public String updateDisable() throws Exception {
		try {
			supplierManager.updateDisable(deleteIds);
			renderText("");
		} catch (Exception e) {
			log.error(e);
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	//恢复供应商
	@Action("update-restore")
	public String updateRestore() throws Exception {
		try {
			supplierManager.updateRestore(deleteIds);
			renderText("");
		} catch (Exception e) {
			log.error(e);
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	/*
	 *模糊查询供应商的名称
	 */
	@SuppressWarnings("unchecked")
	@Action("read-supplier")
	public String readSupplier() throws Exception{ 
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}else{
			params = new JSONObject();
		}
		List<Object[]> searchList=(List<Object[]>) supplierManager.searchSupplier(params);
		StringBuilder sb=new StringBuilder();
//		sb.append(",{\"value\":\"1\",\"label\":\"label1\",\"id\":\"01\"},{\"value\":\"2\",\"label\":\"label2\",\"id\":\"02\"}");
		for(Object[] obj:searchList){
			sb.append(",{\"value\":\""+obj[1]+"\",\"label\":\""+obj[1]+"\",\"id\":\""+obj[2]+"\"}");
		}
		sb.replace(0, 1, "[").append("]"); 
		renderText(sb.toString());
		return null;
	}
	/*
	 *模糊查询供应商的名称
	 */
	@SuppressWarnings("unchecked")
	@Action("read-supplier-by-name")
	public String readSupplierByName() throws Exception{ 
		List<Object[]> searchList=(List<Object[]>) supplierManager.searchSupplier(params);
		StringBuilder sb=new StringBuilder();
//		sb.append(",{\"value\":\"1\",\"label\":\"label1\",\"id\":\"01\"},{\"value\":\"2\",\"label\":\"label2\",\"id\":\"02\"}");
		for(Object[] obj:searchList){
			sb.append(",{\"value\":\""+obj[1]+"\",\"label\":\""+obj[1]+"\",\"id\":\""+obj[2]+"\"}");
		}
		sb.replace(0, 1, "[").append("]"); 
		renderText(sb.toString());
		return null;
	}
	
	/*
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 把map转换为页面的编辑select字符串
	 * @param map
	 * @return
	 */
	private String convertToOptions(List<Option> options){
		StringBuffer sb = new StringBuffer("");
		for(Option option : options){
			if(sb.length() > 0){
				sb.append(";");
			}
			sb.append(option.getName() + ":" + option.getValue());
		}
		return sb.toString();
	}
	/**
	 * 取对应供应商已准入的产品
	 * @param map
	 * @return
	 */
	@Action("get-supplier-products")
	public String  getSupplierApplyProducts()throws Exception{
		String supplierId = Struts2Utils.getParameter("supplierId");
		Supplier supplier = supplierManager.getSupplier(Long.valueOf(supplierId));
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Object> list1 = new ArrayList();
		if(supplier!=null){
//			List<ProductExploitationRecord> list = supplier.getProductExploitationRecords();
//			
//			for(ProductExploitationRecord productExploitationRecord:list){
//				if(productExploitationRecord.getApplyState().equals("已准入")){
//					Map<String,String> map = new HashMap<String, String>();
//					map.put("code",productExploitationRecord.getCode());
//					map.put("name", productExploitationRecord.getName());
//					map.put("materialType", productExploitationRecord.getMaterialType());
//					list1.add(map);
//				}
//			}
			renderText(JsonParser.object2Json(list1).toString());
		}
		return null;
	}
	
	@Action("get-supplier-productsByCode")
	public String  getSupplierApplyProductsByCode()throws Exception{
		String supplierId = Struts2Utils.getParameter("supplierId");
		String bomCode = Struts2Utils.getParameter("bomCode"); 
		Supplier supplier = supplierManager.getSupplier(Long.valueOf(supplierId));
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Object> list1 = new ArrayList();
		List<ProductExploitationRecord> list = productExploitationRecordManager.getProductApplyStateBySupplierAndProductBomList(supplier, bomCode); 
    	for(ProductExploitationRecord productExploitationRecord:list){ 
					Map<String,String> map = new HashMap<String, String>();
					map.put("value",productExploitationRecord.getCode());
					map.put("label", productExploitationRecord.getCode());
					map.put("name", productExploitationRecord.getName());
					map.put("materialType", productExploitationRecord.getMaterialType());
					list1.add(map); 
			}
			renderText(JsonParser.object2Json(list1).toString());
		return null;
	}
	
	
	@Action("import-estimate")
	public String importEstimate() throws Exception {
		try {
			renderHtml(supplierManager.importEstimate(myFile));
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	@Action("import-supplier")
	public String importSuppliers() throws Exception {
		try {
			renderHtml(supplierManager.importSuppliers(myFile));
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	@Action("import-form")
	public String importSupplierForm() throws Exception {
		return SUCCESS;
	}
	
	/**
	  * 方法名:导出导入模板 
	  * <p>功能说明：</p>
	  * @throws IOException
	 */
	@Action("download-excel")
	@LogInfo(optType="导出",message="导出导入模板")
	public void exportReport() throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/supplier.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "供应商资料模板.xlsx";
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
	}
}
