package com.ambition.supplier.archives.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.supplier.admittance.service.AdmittanceFlowConfigureManager;
import com.ambition.supplier.admittance.service.SublotsAppraisalReportManager;
import com.ambition.supplier.archives.dao.CertificateDao;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.archives.dao.SupplyProductDao;
import com.ambition.supplier.entity.AdmittanceFlowConfigure;
import com.ambition.supplier.entity.AppraisalReport;
import com.ambition.supplier.entity.Certificate;
import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.InspectionReport;
import com.ambition.supplier.entity.ProductAdmittanceRecord;
import com.ambition.supplier.entity.ProductExploitationRecord;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierLinkMan;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.manager.service.ProductExploitationRecordManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.wf.engine.client.ExtendField;

@Service
@Transactional
public class SupplierManager {
	@Autowired
	private SupplierDao supplierDao;
	
	@Autowired
	private SupplyProductDao supplyProductDao;
	
	@Autowired
	private CertificateDao certificateDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	@Autowired
	private UseFileManager useFileManager;
	
	@Autowired
 	private ProductExploitationRecordManager productExploitationRecordManager;
	
	@Autowired
	private AdmittanceFlowConfigureManager flowConfigureManager;
	
	@Autowired
	private SublotsAppraisalReportManager appraisalReportManager;
	@Autowired
	private ProductBomManager bomManager;
	private Map<String,String> compareMap = new HashMap<String, String>();
	private Map<String,String> targetMap = new HashMap<String, String>();
	
	public SupplierManager(){
		//初始化比较字符串
		compareMap.put("equals","=");
		compareMap.put("ge",">=");
		compareMap.put("gt",">");
		compareMap.put("le","<=");
		compareMap.put("lt","<");
		compareMap.put("like","like");
		
		//初始化检查记录表的字符串对应字段
		targetMap.put("product", "s.supplyProducts");
		targetMap.put("certificate", "s.certificates");
		targetMap.put("linkMan", "s.supplierLinkMans");
	}
	
	/**
	 * 根据供应商名称查询供应商
	 * @param supplierName
	 * @return
	 */
	public Supplier getSupplierByName(String supplierName){
		return supplierDao.getSupplierByName(supplierName);
	}
	
	/**
	 * 查询供应商
	 * @param page
	 * @return
	 */
	public Page<Supplier> search(Page<Supplier> page,JSONObject params){
		params = convertJsonObject(params);
		StringBuffer hqlBuffer = new StringBuffer("select distinct s from Supplier s");
		Map<String,String> innerMap = new HashMap<String, String>(); 
		StringBuffer searchBuffer = new StringBuffer(" where s.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		
		//绑定查询条件
		for(Object key : params.keySet()){
			String[] properties = key.toString().split("_");
			if(properties.length<3){
				continue;
			}
			if(!"s".equals(properties[0])&&!targetMap.containsKey(properties[0])){
				continue;
			}
			if(!compareMap.containsKey(properties[2])){
				continue;
			}
			Object value = params.get(key);
			if(value==null){
				continue;
			}
			if(!"s".equals(properties[0])){
				innerMap.put(properties[0],targetMap.get(properties[0]));
			}
			if(properties.length > 3){
				value = convertObject(properties[3],value);
			}
			if("like".equals(properties[2])){
				searchBuffer.append(" and " + properties[0] + "." + properties[1] + " like ? ");
				searchParams.add("%" + value + "%");
			}else{
				searchBuffer.append(" and " + properties[0] + "." + properties[1] + " " + compareMap.get(properties[2]) + " ? ");
				searchParams.add(value);
			}
		}
		
		for(String key : innerMap.keySet()){
			hqlBuffer.append(" inner join " + innerMap.get(key) + " " + key);
		}
		hqlBuffer.append(searchBuffer);
		return supplierDao.findPage(page,hqlBuffer.toString(),searchParams);
	}
	/**
	 * 查询供应商
	 * @param page
	 * @return
	 */
	public Page<Supplier> searchByPage(Page<Supplier> page,String state){
		StringBuffer sb = new StringBuffer("from Supplier s where s.companyId = ? and s.deleted=? ");
		com.norteksoft.product.api.entity.Department d = ApiFactory.getAcsService().getDepartmentById(ContextUtils.getDepartmentId());
		if("供应商".equals(d.getName())){
			sb.append(" and s.code=" + ContextUtils.getLoginName());
		}
		if(StringUtils.isNotEmpty(state)){
			return supplierDao.searchPageByHql(page,sb.append(" and s.state = ?").toString(),ContextUtils.getCompanyId(),0,state);
		}else{
			return supplierDao.searchPageByHql(page,sb.toString(),ContextUtils.getCompanyId(),0);
		}
	}
	public Page<Supplier> searchByPage(Page<Supplier> page,JSONObject params,String supplierState){
		StringBuffer sb = new StringBuffer("from Supplier s where 1=1");
		//params = convertJsonObject(params);
		if(supplierState!=null && !supplierState.equals("")){
			params.put("state", supplierState);
		}
		//查询条件
		List<Object> searchParams = new ArrayList<Object>();
		if(!params.isEmpty()){
			if(params.containsKey("code")){
				sb.append(" and s.code like ?");
				searchParams.add("%" + params.getString("code") + "%");
			}
			if(params.containsKey("name")){
				sb.append(" and s.name like ?");
				searchParams.add("%" + params.getString("name") + "%");
			}
			if(params.containsKey("region")){
				sb.append(" and s.region = ?");
				searchParams.add(params.getString("region"));
			}
			if(params.containsKey("enterpriseProperty")){
				sb.append(" and s.enterpriseProperty = ?");
				searchParams.add(params.getString("enterpriseProperty"));
			}
			if(params.containsKey("importance")){
				sb.append(" and s.importance = ?");
				searchParams.add(params.getString("importance"));
			}
			if(params.containsKey("state")){
				sb.append(" and s.state = ?");
				searchParams.add(params.getString("state"));
			}
		}
		return supplierDao.searchPageByHql(page, sb.toString(),searchParams.toArray());
	}
	/**
	 * 查询供应商供应的产品
	 * @param id
	 * @return
	 */
	public String searchSupplyProducts(Long id){
		Supplier supplier = supplierDao.get(id);
		if(supplier != null){
			Page<SupplyProduct> page = new Page<SupplyProduct>();
			page.setResult(supplier.getSupplyProducts());
			return PageUtils.pageToJson(page);
		}else{
			return null;
		}
	}
	/**
	 * 查询供应商的体系证书
	 * @param id
	 * @return
	 */
	public String searchCertificates(Long id){
		Supplier supplier = supplierDao.get(id);
		if(supplier != null){
			Page<Certificate> page = new Page<Certificate>();
			page.setResult(supplier.getCertificates());
			return PageUtils.pageToJson(page);
		}else{
			return null;
		}
	}
	/**
	 * 检查是否存在相同名称的供应商
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistSupplier(Long id,String businessUnitCode,String code,String name){
		String hql = "select count(*) from Supplier s where s.companyId = ?  and (s.code = ? or s.name=?)";
//		String hql = "select count(*) from Supplier s where s.companyId = ? and s.businessUnitCode = ? and (s.code = ? or s.name=?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
//		params.add(businessUnitCode);
		params.add(code);
		params.add(name);
		if(id != null){
			hql += " and s.id <> ?";
			params.add(id);
		}
		Query query = supplierDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	
	public Supplier getSupplier(Long id){
		return supplierDao.get(id);
	}
	
	public Supplier getSupplier(String supplierCode){
		String hql = "from Supplier s where  s.code = ?";
		List<Supplier> suppliers = supplierDao.find(hql,supplierCode);
		if(suppliers.isEmpty()){
			return null;
		}else{
			return suppliers.get(0);
		}
	}
	
	
	public Supplier getSupplerByRandom(){
		return supplierDao.getSupplerByRandom();
	}
	/**
	 * 保存供应商
	 * @param supplier
	 * @throws Exception 
	 */
	public void saveSupplier(Supplier supplier) throws Exception{
		if(StringUtils.isEmpty(supplier.getCode())){
			throw new RuntimeException("编码不能为空!");
		}
		if(StringUtils.isEmpty(supplier.getName())){
			throw new RuntimeException("名称不能为空!");
		}
		if(isExistSupplier(supplier.getId(),supplier.getBusinessUnitCode(),supplier.getCode(),supplier.getName())){
			throw new RuntimeException("已存在相同的编码或名称!");
		}
		String linkMans = Struts2Utils.getParameter("linkMans");
		/*if(linkMans != null){
			JSONArray jsonArray = JSONArray.fromObject(linkMans);
			if(supplier.getSupplierLinkMans()==null){
				supplier.setSupplierLinkMans(new ArrayList<SupplierLinkMan>());
			}else{
				supplier.getSupplierLinkMans().clear();
			}
			for(int i=0;i<jsonArray.size();i++){
				JSONObject json = jsonArray.getJSONObject(i);
				SupplierLinkMan supplierLinkMan = new SupplierLinkMan();
				supplierLinkMan.setSupplier(supplier);
				supplierLinkMan.setCreatedTime(new Date());
				supplierLinkMan.setCompanyId(ContextUtils.getCompanyId());
				supplierLinkMan.setCreator(ContextUtils.getUserName());
				supplierLinkMan.setLastModifiedTime(new Date());
				supplierLinkMan.setLastModifier(ContextUtils.getUserName());
				for(Object pro : json.keySet()){
					setProperty(supplierLinkMan, pro.toString(),json.getString(pro.toString()));
					if(i==0){
						setProperty(supplier, pro.toString(),json.getString(pro.toString()));
					}
				}
				supplier.getSupplierLinkMans().add(supplierLinkMan);
			}
		}else{
			String linkMan = Struts2Utils.getParameter("linkMan");
			String linkPhone = Struts2Utils.getParameter("linkPhone");
			if(supplier.getSupplierLinkMans().isEmpty()){
				if(StringUtils.isNotEmpty(linkMan)||StringUtils.isNotEmpty(linkPhone)){
					SupplierLinkMan supplierLinkMan = new SupplierLinkMan();
					supplierLinkMan.setSupplier(supplier);
					supplierLinkMan.setCreatedTime(new Date());
					supplierLinkMan.setCompanyId(ContextUtils.getCompanyId());
					supplierLinkMan.setCreator(ContextUtils.getUserName());
					supplierLinkMan.setLastModifiedTime(new Date());
					supplierLinkMan.setLastModifier(ContextUtils.getUserName());
					supplierLinkMan.setLinkMan(linkMan);
					supplierLinkMan.setLinkPhone(linkPhone);
					supplier.getSupplierLinkMans().add(supplierLinkMan);
				}
			}else{
				supplier.getSupplierLinkMans().get(0).setLinkMan(linkMan);
				supplier.getSupplierLinkMans().get(0).setLinkPhone(linkPhone);
			}
		}*/
		supplierDao.save(supplier);
		Long hisEstimateModelId = (Long) Struts2Utils.getRequest().getAttribute("hisEstimateModelId");
		String hisStr = hisEstimateModelId==null?"":hisEstimateModelId.toString();
		String newId = supplier.getEstimateModelId()==null?"":supplier.getEstimateModelId().toString();
//		if(!newId.equals(hisStr)){
//			evaluateManager.calculateRealPointsBySupplier(supplier.getBusinessUnitCode(),supplier.getCode(),null,null);
//		}
	}
	
	/**
	 * 删除供应商
	 * @param deleteIds
	 */
	public void deleteSupplier(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			Supplier supplier = supplierDao.get(Long.valueOf(id));
			if(supplier != null){
				logUtilDao.debugLog("删除", supplier.toString());
				supplierDao.delete(supplier);
			}
		}
	}
	
	/**
	 * 保存供应的产品
	 * @param supplier
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> saveSupplyProduct(Supplier supplier,JSONObject params) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,String> updateDetailMap = new HashMap<String, String>();
		params = convertJsonObject(params);
		//删除历史的数据
		String deleteIds[] = params.getString("deletes").split(",");
		for(String id : deleteIds){
			if(StringUtils.isNotEmpty(id)){
				supplyProductDao.delete(Long.valueOf(id));
			}
		}
		//添加或更新新的数据
		JSONObject createPropertyJson = new JSONObject(),updatePropertyJson = new JSONObject();
		createPropertyJson.put("companyId_long",ContextUtils.getCompanyId());
		createPropertyJson.put("creator",ContextUtils.getUserName());
		createPropertyJson.put("createdTime_utildate",new Date(System.currentTimeMillis()));
		createPropertyJson.put("lastModifier",ContextUtils.getUserName());
		createPropertyJson.put("lastModifiedTime_utildate",new Date(System.currentTimeMillis()));
		
		updatePropertyJson.put("lastModifier",ContextUtils.getUserName());
		updatePropertyJson.put("lastModifiedTime_utildate",new Date(System.currentTimeMillis()));

		JSONArray jsonArray = params.getJSONArray("updates");
		for(int i=0;i<jsonArray.size();i++){
			JSONObject json = jsonArray.getJSONObject(i);
			SupplyProduct supplyProduct = (SupplyProduct) convertObject(SupplyProduct.class,json,createPropertyJson,updatePropertyJson);
			if(supplyProduct != null){
				if(supplyProduct.getId() < 0){
					Long id = supplyProduct.getId();
					supplyProduct.setId(null);
					supplyProduct.setSupplier(supplier);
					supplyProductDao.save(supplyProduct);
					updateDetailMap.put(id.toString(),supplyProduct.getId().toString());
				}else{
					supplyProductDao.save(supplyProduct);
				}
			}
		}
		result.put("updateDetails",updateDetailMap);
		return result;
	}
	
	/**
	 * 保存体系证书
	 * @param supplier
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> saveCertificate(Supplier supplier,JSONObject params) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,String> updateDetailMap = new HashMap<String, String>();
		params = convertJsonObject(params);
		//删除历史的数据
		String deleteIds[] = params.getString("deletes").split(",");
		for(String id : deleteIds){
			if(StringUtils.isNotEmpty(id)){
				Certificate certificate = certificateDao.get(Long.valueOf(id));
				certificateDao.delete(certificate);
				useFileManager.useAndCancelUseFiles(certificate.getCertificationFiles(),null);
			}
		}
		//添加或更新新的数据
		JSONObject createPropertyJson = new JSONObject(),updatePropertyJson = new JSONObject();
		createPropertyJson.put("companyId_long",ContextUtils.getCompanyId());
		createPropertyJson.put("creator",ContextUtils.getUserName());
		createPropertyJson.put("createdTime_utildate",new Date(System.currentTimeMillis()));
		createPropertyJson.put("lastModifier",ContextUtils.getUserName());
		createPropertyJson.put("lastModifiedTime_utildate",new Date(System.currentTimeMillis()));
		
		updatePropertyJson.put("lastModifier",ContextUtils.getUserName());
		updatePropertyJson.put("lastModifiedTime_utildate",new Date(System.currentTimeMillis()));

		JSONArray jsonArray = params.getJSONArray("updates");
		for(int i=0;i<jsonArray.size();i++){
			JSONObject json = jsonArray.getJSONObject(i);
			Long id = json.getLong("id");
			if(id>0){
				useFileManager.useAndCancelUseFiles(certificateDao.get(id).getCertificationFiles(), null);
			}
			Certificate certificate = (Certificate) convertObject(Certificate.class,json,createPropertyJson,updatePropertyJson);
			if(certificate != null){
				if(certificate.getId() < 0){
					id = certificate.getId();
					certificate.setId(null);
					certificate.setSupplier(supplier);
					certificateDao.save(certificate);
					updateDetailMap.put(id.toString(),certificate.getId().toString());
				}else{
					certificateDao.save(certificate);
				}
				useFileManager.useAndCancelUseFiles(null,certificate.getCertificationFiles());
			}
		}
		result.put("updateDetails",updateDetailMap);
		return result;
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
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
	
	/**
	 * 转换对象
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertObject(String type,Object value){
		if(value == null){
			return null;
		}else if("date".equals(type)){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			try {
				return new Date(sdf.parse(value.toString()).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}else if("long".equals(type)){
			return Long.valueOf(value.toString());
		}else if("double".equals(type)){
			return Double.valueOf(value.toString());
		}else{
			return value;
		}
	}
	
	/**
	 * 转换对象
	 * @param className
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Object convertObject(Class className,JSONObject jsonObject,JSONObject createPropertyJson,JSONObject updatePropertyJson) throws Exception{
		try {
			Long id = jsonObject.getLong("id");
			Object obj = null;
			if(id < 0){
				obj = className.newInstance();
				for(Object key : createPropertyJson.keySet()){
					setProperty(obj, key,createPropertyJson);
				}
				PropertyUtils.setProperty(obj,"id",id);
			}else{
				obj = supplierDao.getSession().get(className, id);
				if(obj == null){
					return obj;
				}
				for(Object key : updatePropertyJson.keySet()){
					setProperty(obj, key,updatePropertyJson);
				}
			}
			jsonObject.remove("id");
			ExtendField extendField = null;
			for(Object key : jsonObject.keySet()){
				if(key.toString().startsWith("extendField.")){
					if(extendField == null){
						extendField = new ExtendField();
					}
					String fieldName = key.toString().split("\\.")[1];
					setProperty(extendField, fieldName,jsonObject.get(key.toString()));
				}else{
					setProperty(obj, key,jsonObject);
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void setProperty(Object obj,Object key,JSONObject json) throws Exception{
		String[] args = key.toString().split("_");
		if(args.length == 1){
			Class<?> type = PropertyUtils.getPropertyType(obj,key.toString()); 
			if(type != null){
				if(StringUtils.isEmpty(json.get(key).toString())){
					PropertyUtils.setProperty(obj,key.toString(),null);
				}else{
					if(Date.class.equals(type)){
						PropertyUtils.setProperty(obj,key.toString(),DateUtil.parseDate(json.get(key).toString()));
					}else{
						PropertyUtils.setProperty(obj,key.toString(),json.get(key));
					}
				}
			}
		}else if(args.length == 2){
			if("long".equals(args[1])){
				if(PropertyUtils.getPropertyType(obj,args[0]) != null){
					PropertyUtils.setProperty(obj,args[0],json.getLong(key.toString()));
				}
			}else if("utildate".equals(args[1])){
				if(PropertyUtils.getPropertyType(obj,args[0]) != null){
					JSONObject dateJson = json.getJSONObject(key.toString());
					PropertyUtils.setProperty(obj,args[0],new Date(dateJson.getLong("time")));
				}
			}
		}
	}

	private void setProperty(Object obj,String fieldName,Object val) throws Exception{
		Class<?> classType = PropertyUtils.getPropertyType(obj, fieldName);
		if(classType != null){
			if(val==null||StringUtils.isEmpty(val.toString())){
				PropertyUtils.setProperty(obj,fieldName,null);
			}else{
				if(Date.class.getName().equals(classType.getName())){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					PropertyUtils.setProperty(obj,fieldName,sdf.parse(val.toString()));
				}else if(Double.class.getName().equals(classType.getName())){
					PropertyUtils.setProperty(obj,fieldName,Double.valueOf(val.toString()));
				}else{
					PropertyUtils.setProperty(obj,fieldName,val);
				}
			}
		}
	}
	
	/**
	 * 查询所有供应商
	 * @return
	 */
	public List<Supplier> listAll() {
		return supplierDao.find("from Supplier s where s.companyId = ? and s.state = ?",new Object[]{ContextUtils.getCompanyId(),Supplier.STATE_QUALIFIED});
	}

	/**
	 * 导入供应商
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String importSuppliers(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String,String> fieldMap = getSupplierFieldMap();
		
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if(row == null){
			throw new RuntimeException("第一行不能为空!");
		}
		LinkedHashMap<String,Integer> columnMap = new LinkedHashMap<String,Integer>();
//		Map<String,Integer> columnMap = new HashMap<String,Integer>();
		for(int i=0;;i++){
			Cell cell = row.getCell(i);
			if(cell==null){
				break;
			}
			String value = cell.getStringCellValue();
			if(fieldMap.containsKey(value)){
				columnMap.put(value,i);
			}
		}
		if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new RuntimeException("资料格式不正确!&nbsp;&nbsp;<br/><a href='/supplier/archives/download-supplier-template.htm'>下载供应商资料模板>></a>");
		}
		DecimalFormat df = new DecimalFormat("#.##############");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 1;
		while(rows.hasNext()){
			row = rows.next();
			Supplier supplier = null;
			for(String columnName : columnMap.keySet()){
				Cell cell = row.getCell(columnMap.get(columnName));
				if("供应商编号".equals(columnName)){
					supplier = getSupplier(cell.getStringCellValue());
					if(supplier==null){
						supplier = new Supplier();
						supplier.setCreatedTime(new Date());
						supplier.setCompanyId(ContextUtils.getCompanyId());
						supplier.setCreator(ContextUtils.getUserName());
						supplier.setLastModifiedTime(new Date());
						supplier.setLastModifier(ContextUtils.getUserName());
					}
				}
				if(cell != null){
					if(Cell.CELL_TYPE_STRING == cell.getCellType()){
						if( columnName.contains("登录日期")){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							if(StringUtils.isEmpty(cell.getStringCellValue().toString())){
								continue;
							}
							Date date = sdf.parse(cell.getStringCellValue().toString());
							supplier.setEnterDate(date);
						}else{
							setExcelProperty(supplier, fieldMap.get(columnName),cell.getStringCellValue());
						}
					}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
						setExcelProperty(supplier, fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
					}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
						setExcelProperty(supplier, fieldMap.get(columnName),cell.getCellFormula());
					}
				}
			}
			try {
				saveSupplier(supplier);
				sb.append("第" + (i+1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("第" + (i+1) + "行保存失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		if(i == 1){
			sb.append("数据为空!");
		}
		return sb.toString();
	}
	
	/**
	 * 导入供应商准入物料
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String importSuppliersMaterial(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		try{
			
			
			Workbook book = WorkbookFactory.create(new FileInputStream(file));
			
			Sheet sheet = book.getSheetAt(0);
			Row row = sheet.getRow(0);
			if(row == null){
				throw new RuntimeException("第一行不能为空!");
			}
			
			Iterator<Row> rows = sheet.rowIterator();
			rows.next();//标题行
			int i = 1;
			while(rows.hasNext()){
				row = rows.next();
				try{
					String code11= row.getCell(0).getStringCellValue();
					System.out.println("------------------"+code11);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
				
				Supplier supplier = supplierDao.getSupplierByCode(row.getCell(0).getStringCellValue().trim());//根据第一列供方编号取供方
				if(supplier==null){
					supplier = new Supplier();
					List<SupplyProduct> supplyProducts = new ArrayList<SupplyProduct>();
					List<ProductAdmittanceRecord> productAdmittanceRecords = new ArrayList<ProductAdmittanceRecord>();
					List<ProductExploitationRecord> productExploitationRecords = new ArrayList<ProductExploitationRecord>();
					
					supplier.setCode(row.getCell(0).getStringCellValue().trim());//供应商编号
					supplier.setName(row.getCell(1).getStringCellValue().trim());//供应商名称
					supplier.setSupplyProducts(supplyProducts);
//					supplier.setProductAdmittanceRecords(productAdmittanceRecords);
//					supplier.setProductExploitationRecords(productExploitationRecords);
				}
					String code = row.getCell(2).getStringCellValue();
					if(StringUtils.isEmpty(code)){
						continue;
					}
					boolean hasExist = false;
					for(SupplyProduct product : supplier.getSupplyProducts()){
						if(code.equals(product.getCode())){
							hasExist = true;
							break;
						}
					}
					if(hasExist){
						continue;
					}
//					String name = row.getCell(3).getStringCellValue();
//					String materialType = row.getCell(4).getStringCellValue();
//					String importance = row.getCell(5).getStringCellValue();
					ProductBom bom = bomManager.getProductBomByBomCode(code);
					if(bom==null){
						continue;
					}
					//供应的产品
					SupplyProduct supplyProduct = new SupplyProduct();
					supplyProduct.setCode(bom.getMaterielCode());
					supplyProduct.setName(bom.getMaterielName());
					supplyProduct.setMaterialType(bom.getMaterialType());
					supplyProduct.setModel(bom.getMaterielModel());
					supplyProduct.setImportance(bom.getImportance());
					supplyProduct.setCreatedTime(new Date());
					supplyProduct.setCompanyId(ContextUtils.getCompanyId());
					supplyProduct.setCreator(ContextUtils.getUserName());
					supplyProduct.setLastModifiedTime(new Date());
					supplyProduct.setLastModifier(ContextUtils.getUserName());
					supplyProduct.setSupplier(supplier);
					//
					ProductExploitationRecord productExploitationRecord = new ProductExploitationRecord();
					productExploitationRecord.setCode(code);
					productExploitationRecord.setName(bom.getMaterielName());
					productExploitationRecord.setApplyState("已准入");
					productExploitationRecord.setExploitationPhase("已准入");
					productExploitationRecord.setImportance(bom.getImportance());
					productExploitationRecord.setMaterialType(bom.getMaterialType());
					productExploitationRecord.setCreatedTime(new Date());
					productExploitationRecord.setCompanyId(ContextUtils.getCompanyId());
					productExploitationRecord.setCreator(ContextUtils.getUserName());
					productExploitationRecord.setLastModifiedTime(new Date());
					productExploitationRecord.setLastModifier(ContextUtils.getUserName());
					productExploitationRecord.setSupplier(supplier);
					
					ProductAdmittanceRecord productAdmittanceRecord = new ProductAdmittanceRecord();
					productAdmittanceRecord.setCode(code);
					productAdmittanceRecord.setName(bom.getMaterielName());
					productAdmittanceRecord.setImportance(bom.getImportance());
					productAdmittanceRecord.setMaterialType(bom.getMaterialType());
					productAdmittanceRecord.setSupplier(supplier);
					productAdmittanceRecord.setCreatedTime(new Date());
					productAdmittanceRecord.setCompanyId(ContextUtils.getCompanyId());
					productAdmittanceRecord.setCreator(ContextUtils.getUserName());
					productAdmittanceRecord.setLastModifiedTime(new Date());
					productAdmittanceRecord.setLastModifier(ContextUtils.getUserName());
					
					supplier.getSupplyProducts().add(supplyProduct);
//					supplier.getProductAdmittanceRecords().add(productAdmittanceRecord);
//					supplier.getProductExploitationRecords().add(productExploitationRecord);
					supplier.setState(Supplier.STATE_QUALIFIED);
					try {
						saveSupplier(supplier);
						sb.append("第" + (i+1) + "行保存成功!<br/>");
					} catch (Exception e) {
						sb.append("第" + (i+1) + "行保存失败:<font color=red>" + e.getMessage() + "</font><br/>");
					}
				
				
				
				i++;
			}
			file.delete();
			if(i == 1){
				sb.append("数据为空!");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	private Map<String,String> getSupplierFieldMap(){
		Map<String,String> fieldMap = new HashMap<String, String>();
		fieldMap.put("供应商编号","code");
		fieldMap.put("供应商名称","name");
		fieldMap.put("供应物料","supplyMaterial");
		fieldMap.put("材料类别","materialType");
		fieldMap.put("联系人","linkMan");
		fieldMap.put("联系电话","linkPhone");
		fieldMap.put("状态","state");
		fieldMap.put("供应商地址","address");
		fieldMap.put("登录日期","enterDate");
		fieldMap.put("备注","remark");
		return fieldMap;
	}
	/**
	 * 创建供应商资料模板
	 * @throws Exception 
	 */
	public void createSupplierTemplate() throws Exception{
		Workbook wb = new HSSFWorkbook();
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setColor(IndexedColors.RED.getIndex());
		font.setBoldweight((short) 700);
		style.setFont(font);
		style.setAlignment((short) 2);
		style.setVerticalAlignment((short) 0);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern((short) 1);
		style.setBorderTop((short) 1);
		style.setTopBorderColor(IndexedColors.BLUE.getIndex());
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		Sheet sheet = wb.createSheet("Sheet1");
		Row row = sheet.createRow(0);
		Cell cell = null;
		int colIndex = 0;
		Map<String,String> fieldMap = getSupplierFieldMap();
		for(String columnName : fieldMap.keySet()){
			cell = row.createCell(colIndex++);
			cell.setCellStyle(style);
			cell.setCellValue(columnName);
		}
		String fileName = "供应商资料模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		wb.write(response.getOutputStream());
	}
	
	private void setExcelProperty(Object obj,String property,Object value) throws Exception{
		Class<?> type = PropertyUtils.getPropertyType(obj,property);
		if(type != null){
			if(value==null||StringUtils.isEmpty(value.toString())){
				PropertyUtils.setProperty(obj,property,null);
			}else{
				if(Date.class.getName().equals(type.getName())){
					
				}else if(String.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,value.toString());
				}else if(Integer.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
				}else if(Double.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
				}else if(Float.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
				}
			}
		}
	}
	
	/**
	 * 淘汰供应商
	 * @param delteIds
	 */
	public void washOutSupplier(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				Supplier supplier = supplierDao.get(Long.valueOf(id));
				if(supplier!=null){
					supplier.setState(Supplier.STATE_ELIMINATED);
					supplierDao.save(supplier);
				}
			}
		}
	}
	
	/**
	 * 恢复供应商
	 * @param ids
	 */
	public void resumeSupplier(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				Supplier supplier = supplierDao.get(Long.valueOf(id));
				if(supplier!=null){
					supplier.setState(Supplier.STATE_QUALIFIED);
					supplierDao.save(supplier);
				}
			}
		}
	}

	/**
	 * 停用供应商
	 * @param delteIds
	 */
	public void updateStop(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				Supplier supplier = supplierDao.get(Long.valueOf(id));
				if(supplier!=null){
					supplier.setUseState(Supplier.USESTATE_STOP);
					supplier.setLastModifiedTime(new Date());
					supplier.setLastModifier(ContextUtils.getUserName());
					supplierDao.save(supplier);
				}
			}
		}
	}
	
//	@Autowired
//	private DisableSupplierStateInterationService stateInterationService;
	/**
	 * 禁用供应商
	 * @param delteIds
	 */
	public void updateDisable(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				Supplier supplier = supplierDao.get(Long.valueOf(id));
				if(supplier!=null){
					if(Supplier.USESTATE_DISABLE.equals(supplier.getUseState())){
						continue;
					}
					supplier.setUseState(Supplier.USESTATE_DISABLE);
					supplier.setLastModifiedTime(new Date());
					supplier.setLastModifier(ContextUtils.getUserName());
					supplierDao.save(supplier);
//					String result = stateInterationService.updateRequestCheckState(ContextUtils.getCompanyId(),ContextUtils.getLoginName(),supplier.getCode());
//					if(!"".equals(result)){
//						throw new AmbFrameException("禁用供应商失败!" + result);
//					}
				}
			}
		}
	}
	
	/**
	 * 恢复供应商
	 * @param delteIds
	 */
	public void updateRestore(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				Supplier supplier = supplierDao.get(Long.valueOf(id));
				if(supplier!=null){
					supplier.setUseState(Supplier.USESTATE_QUALIFIED);
					supplier.setLastModifiedTime(new Date());
					supplier.setLastModifier(ContextUtils.getUserName());
					supplierDao.save(supplier);
				}
			}
		}
	}
	
	/**
	 * 获取供应商的状态列表
	 * @return
	 */
	public List<Option> getSupplierStateOptions(){
		List<Option> options = new ArrayList<Option>();
		Option option = new Option();
		option.setName(Supplier.STATE_POTENTIAL);
		option.setValue(Supplier.STATE_POTENTIAL);
		options.add(option);
		
		option = new Option();
		option.setName(Supplier.STATE_ALLOW);
		option.setValue(Supplier.STATE_ALLOW);
		options.add(option);
		
		option = new Option();
		option.setName(Supplier.STATE_QUALIFIED);
		option.setValue(Supplier.STATE_QUALIFIED);
		options.add(option);
		
		option = new Option();
		option.setName(Supplier.STATE_ELIMINATED);
		option.setValue(Supplier.STATE_ELIMINATED);
		options.add(option);
		
		return options;
	}
	/**
	 * 根据编号/名称模糊检索供应商
	 * @return
	 */
	public List<?> searchSupplier(JSONObject params){
		return supplierDao.searchSupplier(params);
	}
	
	/**
	  * 方法名: 设置物料的选择状态
	  * <p>功能说明：根据物料和当前的环节确定可以选定的物料</p>
	  * @param map
	  * @param currentNode
	 */
	public void setSupplyProductState(Supplier supplier,String materialType,String bomCode,String currentNode,Map<String,Object> map){
		boolean isNewSupplier = false;
		if(!Supplier.STATE_QUALIFIED.equals(supplier.getState())){
			isNewSupplier = true;
		}
		AdmittanceFlowConfigure flowConfigure = flowConfigureManager.getAdmittanceFlowConfigure(isNewSupplier,materialType);
		Integer minSampleAppraisal = flowConfigure.getMinSampleAppraisal()==null?1:flowConfigure.getMinSampleAppraisal();
		Integer maxSampleAppraisal = flowConfigure.getMaxSampleAppraisal()==null?Integer.MAX_VALUE:flowConfigure.getMaxSampleAppraisal();
		Integer maxSublotsAppraisal = flowConfigure.getMaxSublotsAppraisal()==null?Integer.MAX_VALUE:flowConfigure.getMaxSublotsAppraisal();
		Integer minSublotsAppraisal = flowConfigure.getMinSublotsAppraisal()==null?1:flowConfigure.getMinSublotsAppraisal();
		ProductExploitationRecord productExploitationRecord = productExploitationRecordManager.getProductApplyStateBySupplierAndProductBom(supplier,bomCode);
		String applyState = "";
		if(productExploitationRecord==null){
			applyState = SupplyProduct.APPLYSTATE_DEFAULT;
		}else if(bomCode.equals(productExploitationRecord.getCode())){
			applyState = productExploitationRecord.getApplyState();
		}/*else{
			List<InspectionReport> inspectionReports = supplier.getInspectionReports();
			if(inspectionReports.isEmpty()){
				applyState = SupplyProduct.APPLYSTATE_DEFAULT;
			}else{
				InspectionReport inspectionReport = inspectionReports.get(0);
				if(InspectionReport.STATE_PASS.equals(inspectionReport.getInspectionState())){
					if(InspectionReport.RESULT_FAIL.equals(inspectionReport.getInspectionResult())){
						applyState = SupplyProduct.APPLYSTATE_INSPECTFAIL;
					}else{
						applyState = SupplyProduct.APPLYSTATE_INSPECTPASS;
					}
				}else if(InspectionReport.STATE_FAIL.equals(inspectionReport.getInspectionState())){
					applyState = SupplyProduct.APPLYSTATE_INSPECTFAIL;
				}else{
					applyState = SupplyProduct.APPLYSTATE_INSPECT;
				}
			}
		}*/
		map.put("applyState",applyState);
		String errorMessage = "";
		//对应的阶段
		int currentPhase = 0;
		Map<String,Integer> phaseMap = new HashMap<String, Integer>();
		phaseMap.put(SupplyProduct.APPLYSTATE_DEFAULT,0);
		phaseMap.put(SupplyProduct.APPLYSTATE_INSPECT,1);
		phaseMap.put(SupplyProduct.APPLYSTATE_INSPECTPASS,1);
		phaseMap.put(SupplyProduct.APPLYSTATE_INSPECTFAIL,1);
		phaseMap.put(SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLE,2);
		phaseMap.put(SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEFAIL,2);
		phaseMap.put(SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEPASS,2);
		phaseMap.put(SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTS,3);
		phaseMap.put(SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSFAIL,3);
		phaseMap.put(SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSPASS,3);
		phaseMap.put(SupplyProduct.APPLYSTATE_ADMITTANCE,4);
		if("sample-appraisal".equals(currentNode)){
			currentPhase = 2;
		}else if("sublots-appraisal".equals(currentNode)){
			currentPhase = 3;
		}else if("admittance".equals(currentNode)){
			currentPhase = 4;
		}
		int productPhase = 0;
		if(phaseMap.containsKey(applyState)){
			productPhase = phaseMap.get(applyState);
		}
		if(productPhase>currentPhase){
			errorMessage = "状态为【"+applyState+"】";
		}else if(SupplyProduct.APPLYSTATE_INSPECT.equals(applyState)
			||SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLE.equals(applyState)
			||SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTS.equals(applyState)
			||SupplyProduct.APPLYSTATE_ADMITTANCE.equals(applyState)){
			errorMessage = "状态为【"+applyState+"】";
		}else{
			if("sample-appraisal".equals(currentNode)){
				//如果现场考察不通过,
				if(flowConfigure.getNeedInspectionReport()){
					getIsCanSelect(productPhase, currentPhase, SupplyProduct.APPLYSTATE_INSPECTPASS, applyState, supplier.getId(), bomCode,AppraisalReport.TYPE_SAMPLE,maxSampleAppraisal,minSampleAppraisal,map);
				}
				//如果没有错,判断上限次数
				if(!map.containsKey("errorMessage")&&flowConfigure.getNeedSampleAppraisal()){
					int timeOfPhase = appraisalReportManager.getTimeOfPhase(supplier.getId(),bomCode,AppraisalReport.TYPE_SAMPLE)-1;
					if(timeOfPhase>=maxSampleAppraisal){
						map.put("errorMessage","最多只能进行【"+maxSampleAppraisal+"】次样件鉴定!");
					}
				}
			}else if("sublots-appraisal".equals(currentNode)){
				boolean concessionPass = false;
				if(flowConfigure.getNeedSampleAppraisal()){
					if(SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEFAIL.equals(applyState)&&productExploitationRecord.getIsSampleConcessionPass()){
						concessionPass = true;
					}else{
						getIsCanSelect(productPhase, currentPhase, SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEPASS, applyState, supplier.getId(), bomCode,AppraisalReport.TYPE_SUBLOTS,maxSublotsAppraisal,minSampleAppraisal,map);
					}
				}else if(flowConfigure.getNeedInspectionReport()){
					if(productPhase<=2){
						getIsCanSelect(productPhase, currentPhase, SupplyProduct.APPLYSTATE_INSPECTPASS, applyState, supplier.getId(), bomCode,AppraisalReport.TYPE_SAMPLE,maxSampleAppraisal,minSampleAppraisal,map);
					}
				}
				//如果没有错,判断上限次数
				if(!concessionPass&&!map.containsKey("errorMessage")&&flowConfigure.getNeedSampleAppraisal()){
					int timeOfPhase = appraisalReportManager.getTimeOfPhase(supplier.getId(),bomCode,AppraisalReport.TYPE_SUBLOTS)-1;
					if(timeOfPhase>=maxSublotsAppraisal){
						map.put("errorMessage","最多只能进行【"+maxSampleAppraisal+"】次小批鉴定!");
					}
				}
			}else if("admittance".equals(currentNode)){
				boolean concessionPass = false;
				if(flowConfigure.getNeedSublotsAppraisal()){
					if(SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSFAIL.equals(applyState)){
						if(productExploitationRecord.getThirdBatchId() != null){
							concessionPass = productExploitationRecord.getIsThirdBatchConcessionPass();
						}else if(productExploitationRecord.getSecondBatchId() != null){
							concessionPass = productExploitationRecord.getIsSecondBatchConcessionPass();
						}else if(productExploitationRecord.getFirstBatchId() != null){
							concessionPass = productExploitationRecord.getIsFirstBatchConcessionPass();
						}
					}
					if(!concessionPass){
						getIsCanSelect(productPhase, currentPhase, SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSPASS, applyState, supplier.getId(), bomCode,AppraisalReport.TYPE_SUBLOTS,maxSublotsAppraisal,minSublotsAppraisal,map);
					}
				}else if(flowConfigure.getNeedSampleAppraisal()){
					if(SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEFAIL.equals(applyState)&&productExploitationRecord.getIsSampleConcessionPass()){
						concessionPass = true;
					}else{
						getIsCanSelect(productPhase, currentPhase, SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEPASS, applyState, supplier.getId(), bomCode,AppraisalReport.TYPE_SUBLOTS,maxSublotsAppraisal,minSublotsAppraisal,map);
					}
				}else{
					errorMessage = "必须要样件鉴定或小批鉴定!";
				}
			}
		}
		if(!map.containsKey("errorMessage")){
			map.put("errorMessage",errorMessage);
		}
		errorMessage = map.get("errorMessage").toString();
		if(StringUtils.isNotEmpty(errorMessage)){
			map.put("showErrorMessage","<font color=red>不可选,"+errorMessage + "</font>");
		}
	}
	/**
	  * 方法名: 设置物料的选择状态
	  * <p>功能说明：根据物料和当前的环节确定可以选定的物料</p>
	  * @param map
	  * @param currentNode
	 */
	public void setSupplyProductState(Supplier supplier,String currentNode,Map<String,Object> map){
		setSupplyProductState(supplier, map.get("importance")+"",map.get("code")+"", currentNode, map);
	}
	
	private void getIsCanSelect(int productPhase,int currentPhase,String lastSuccessState,String applyState,long supplierId,String bomCode,String appraisalType,int max,int min,Map<String,Object> map){
		if(productPhase<currentPhase){
			if(!lastSuccessState.equals(applyState)){
				map.put("errorMessage","状态为【"+applyState+"】,必须【"+lastSuccessState+"】");
			}else{
				//如果是样件鉴定,判断是否达到最少的成功次数
				if(SupplyProduct.APPLYSTATE_APPRAISAL_SAMPLEPASS.equals(lastSuccessState)){
					int timeOfPhase = appraisalReportManager.getTimeOfPhase(supplierId,bomCode,AppraisalReport.TYPE_SAMPLE)-1;
					if(timeOfPhase<min){
						map.put("errorMessage","最少必须先经过【"+min+"】次样件鉴定!");
					}
				}else if(SupplyProduct.APPLYSTATE_APPRAISAL_SUBLOTSPASS.equals(lastSuccessState)){
					int timeOfPhase = appraisalReportManager.getTimeOfPhase(supplierId,bomCode,AppraisalReport.TYPE_SUBLOTS)-1;
					if(timeOfPhase<min){
						map.put("errorMessage","最少必须先经过【"+min+"】次小批鉴定!");
					}
				}
			}
		}else{
			//判断次数
			int timeOfPhase = appraisalReportManager.getTimeOfPhase(supplierId,bomCode,appraisalType)-1;
			if(timeOfPhase >= max){
				map.put("isCanSelect",false);
				map.put("errorMessage","最多只允许鉴定【"+max+"】次");
			}
		}
	}
	@Autowired
	private EstimateModelManager estimateModelManager;
	/**
	 * 导入评价模型
	 * @param file
	 * @param parent
	 * @param productStructure
	 * @throws Exception
	 */
	public String importEstimate(File file) throws Exception{
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if(row == null){
			throw new RuntimeException("第一行不能为空!");
		}
		Map<String,EstimateModel> estimateModelMap = new HashMap<String, EstimateModel>();
		List<EstimateModel> estimateModels = estimateModelManager.getTopEstimateModels();
		for(EstimateModel estimateModel : estimateModels){
			if(StringUtils.isNotEmpty(estimateModel.getName())){
				String[] strs = estimateModel.getName().split("-");
				estimateModelMap.put(strs[0],estimateModel);
			}
		}
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		Map<String,EstimateModel> supplierMap = new HashMap<String, EstimateModel>();
		while(rows.hasNext()){
			row = rows.next();
			String code = (ExcelUtil.getCellValue(row.getCell(0))+"").trim();
			String type = (ExcelUtil.getCellValue(row.getCell(6))+"").trim();
			if(StringUtils.isEmpty(code)){
				continue;
			}
			supplierMap.put(code,estimateModelMap.get(type));
		}
		String hql = "from Supplier s";
		List<Supplier> suppliers = supplierDao.find(hql);
		for(Supplier supplier : suppliers){
			EstimateModel model = supplierMap.get(supplier.getCode());
			if(model != null){
				if(supplier.getEstimateModelId() == null){
					supplier.setEstimateModelId(model.getId());
					supplierDao.save(supplier);
				}
			}
		}
		file.delete();
		return "导入成功!";
	}

}
