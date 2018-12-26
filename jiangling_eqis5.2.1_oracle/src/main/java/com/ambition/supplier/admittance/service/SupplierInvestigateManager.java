package com.ambition.supplier.admittance.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.product.base.IdEntity;
import com.ambition.supplier.admittance.dao.SupplierInvestigateDao;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.entity.Certificate;
import com.ambition.supplier.entity.InspectionGradeType;
import com.ambition.supplier.entity.InspectionReport;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierInvestigate;
import com.ambition.supplier.entity.SupplierInvestigateContact;
import com.ambition.supplier.entity.SupplierInvestigateEvaluateRecord;
import com.ambition.supplier.entity.SupplierInvestigateStockProduct;
import com.ambition.supplier.entity.SupplierInvestigateTestingDevice;
import com.ambition.supplier.entity.SupplierLinkMan;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.base.enumeration.ProcessState;
import com.norteksoft.wf.engine.client.EndInstanceInterface;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;

/**
 * 类名:供应商调查表业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-4-28 发布
 */
@Service
@Transactional
public class SupplierInvestigateManager implements FormFlowableDeleteInterface, EndInstanceInterface{
	private static final String WORKFLOW_DEFINITION_CODE = "supplier_investigate";
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	@Autowired
	private SupplierInvestigateDao investigateDao;
	
	@Autowired
	private UseFileManager useFileManager;
	
	@Autowired
	private ProductBomManager bomManager;
	
	@Autowired
	private SupplierDao supplierDao;
	/**
	  * 方法名: 供应商调查记录
	  * <p>功能说明：分页查询供应商调查记录</p>
	  * @param page
	  * @return
	 */
	public Page<SupplierInvestigate> search(Page<SupplierInvestigate> page){
		return investigateDao.search(page);
	}
	
	public SupplierInvestigate getSupplierInvestigate(Long id){
		return investigateDao.get(id);
	}
	public SupplierInvestigate getSupplierInvestigateById(Long id){
		List<SupplierInvestigate> list = investigateDao.find("from SupplierInvestigate a where a.id = ?", id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	/**
	  * 方法名:检查是否已经相同的供应商 
	  * <p>功能说明：</p>
	  * @param investigate
	 */
	private void isExistInvestigate(SupplierInvestigate investigate){
		String hql = "from SupplierInvestigate s where s.enterpriameseName = ? and s.code <> ? and (s.workflowInfo.endTime is null or s.investigateResult = ?)";
		List<SupplierInvestigate> supplierInvestigates = investigateDao.find(hql,new Object[]{investigate.getEnterpriameseName(),investigate.getCode(),SupplierInvestigate.RESULT_PASS});
		if(!supplierInvestigates.isEmpty()){
			SupplierInvestigate s = supplierInvestigates.get(0);
			if(s.getWorkflowInfo()==null||s.getWorkflowInfo().getEndTime()==null){
				throw new AmbFrameException("【"+investigate.getEnterpriameseName()+"】正在调查!");
			}else{
				throw new AmbFrameException("【"+investigate.getEnterpriameseName()+"】的调查已经通过!");
			}
		}
		//检查供应商是否已经存在
		hql = "select count(s.id) from Supplier s where s.name = ?";
		long count = investigateDao.countHqlResult(hql, investigate.getEnterpriameseName());
		if(count >0){
			throw new AmbFrameException("【"+investigate.getEnterpriameseName()+"】的在供应商台帐中已经存在!");
		}
	}
	/**
	 * 保存考察报告
	 * @param inspectionReport
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public void saveInvestigate(SupplierInvestigate investigate){
		if(investigate.getId()==null){
//			investigate.setCode(formCodeGenerated.generateSupplierInvestigateCode());
		}else{
			investigate.setLastModifiedTime(new Date());
			investigate.setLastModifier(ContextUtils.getUserName());
		}
		//判断非空项
		if(StringUtils.isEmpty(investigate.getEnterpriameseName())){
			throw new AmbFrameException("企业名称不能为空!");
		}else if(StringUtils.isEmpty(investigate.getEnterpriseAddress())){
			throw new AmbFrameException("企业地址不能为空!");
		}else if(StringUtils.isEmpty(investigate.getEvaluateMan())){
			throw new AmbFrameException("评价人员不能为空!");
		}else if(investigate.getEvaluateDate()==null){
			throw new AmbFrameException("评价日期不能为空!");
		}
		//判断惟一
		isExistInvestigate(investigate);
		if(investigate.getEvaluateMan() != null && investigate.getEvaluateMan().indexOf("=")>0){
			investigate.setEvaluateMan(investigate.getEvaluateMan().split("=")[1]);
		}
		String evaluateRecordStrs = Struts2Utils.getParameter("evaluateRecordStrs");
		if(evaluateRecordStrs != null){
			investigate.getEvaluateRecords().clear();
			JSONArray jsonArray = JSONArray.fromObject(evaluateRecordStrs);
			for(Object obj : jsonArray){
				SupplierInvestigateEvaluateRecord evaluateRecord = getObj((Map<String,Object>)obj, SupplierInvestigateEvaluateRecord.class);
				if(evaluateRecord != null){
					evaluateRecord.setSupplierInvestigate(investigate);
					investigate.getEvaluateRecords().add(evaluateRecord);
				}
			}
		}
		String contactStrs = Struts2Utils.getParameter("contactStrs");
		if(contactStrs != null){
			investigate.getContacts().clear();
			JSONArray jsonArray = JSONArray.fromObject(contactStrs);
			for(Object obj : jsonArray){
				SupplierInvestigateContact contact = getObj((JSONObject)obj, SupplierInvestigateContact.class);
				if(contact != null){
					contact.setSupplierInvestigate(investigate);
					investigate.getContacts().add(contact);
				}
			}
		}
		String stockProductStrs = Struts2Utils.getParameter("stockProductStrs");
		if(stockProductStrs != null){
			investigate.getStockProducts().clear();
			JSONArray jsonArray = JSONArray.fromObject(stockProductStrs);
			for(Object obj : jsonArray){
				SupplierInvestigateStockProduct product = getObj((JSONObject)obj, SupplierInvestigateStockProduct.class);
				if(product != null){
					product.setSupplierInvestigate(investigate);
					investigate.getStockProducts().add(product);
				}
			}
		}
		String testingDeviceStrs = Struts2Utils.getParameter("testingDeviceStrs");
		if(testingDeviceStrs != null){
			investigate.getTestingDevices().clear();
			JSONArray jsonArray = JSONArray.fromObject(testingDeviceStrs);
			for(Object obj : jsonArray){
				SupplierInvestigateTestingDevice testingDevice = getObj((JSONObject)obj, SupplierInvestigateTestingDevice.class);
				if(testingDevice != null){
					testingDevice.setSupplierInvestigate(investigate);
					investigate.getTestingDevices().add(testingDevice);
				}
			}
		}
		investigateDao.save(investigate);
		if(Struts2Utils.getParameter("hisSituationAttachment") != null){
			useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisSituationAttachment"),investigate.getSituationAttachment());
		}
	}
	
	private <T> T getObj(Map<String,Object> map,Class<T> cla){
		boolean isEmpty = Boolean.TRUE;
		for(Object obj : map.keySet()){
			Object val = map.get(obj);
			if(val != null && StringUtils.isNotEmpty(val.toString())){
				isEmpty = Boolean.FALSE;
				break;
			}
		}
		if(isEmpty){
			return null;
		}else{
			try {
				T obj = cla.newInstance();
				for(Object proObj : map.keySet()){
					String pro = proObj.toString();
					Class<?> proType = PropertyUtils.getPropertyType(obj,pro);
					if(proType == null){
						throw new AmbFrameException("【"+ cla.getName() + "】不包含名称为【" + pro + "】的字段!");
					}
					Object value = map.get(proObj);
					if(value == null || StringUtils.isEmpty(value.toString())){
						continue;
					}
					if(proType.getName().equals(Date.class.getName())){
						if(!(value instanceof Date)){
							Date temp = DateUtil.parseDate(value.toString().trim());
							if(temp == null){
								throw new AmbFrameException("字符串【" + value + "】不是有效的日期格式!标准格式为2013-05-01");
							}
							value = temp;
						}
					}else if(proType.getName().equals(Integer.class.getName())){
						if(value instanceof Double){
							value = ((Double)value).intValue();
						}else if(!(value instanceof Integer)){
							try {
								value = Integer.valueOf(value.toString().trim());
							} catch (NumberFormatException e) {
								throw new AmbFrameException("字符串【" + value + "】不是有效的数字!");
							}
						}
					}else if(proType.getName().equals(Double.class.getName())){
						if(!(value instanceof Double)){
							try {
								value = Double.valueOf(value.toString().trim());
							} catch (NumberFormatException e) {
								throw new AmbFrameException("字符串【" + value + "】不是有效的双精度数字!");
							}
						}
					}else if(proType.getName().equals(String.class.getName())){
						if(!(value instanceof String)){
							value = value.toString().trim();
						}
					}
					PropertyUtils.setProperty(obj, proObj.toString(), value);
				}
				if(obj instanceof IdEntity){
					IdEntity id = (IdEntity)obj;
					id.setCompanyId(ContextUtils.getCompanyId());
					id.setCreatedTime(new Date());
					id.setCreator(ContextUtils.getUserName());
					id.setLastModifiedTime(new Date());
					id.setLastModifier(ContextUtils.getUserName());
				}
				return obj;
			} catch (Exception e) {
				if(e instanceof AmbFrameException){
					throw (AmbFrameException)e;
				}else{
					throw new AmbFrameException("转换对象失败！",e);
				}
			}
		}
	}
	@SuppressWarnings("unused")
	private void getReviewers(InspectionGradeType inspectionGradeType,StringBuffer reviewers){
		if(StringUtils.isNotEmpty(inspectionGradeType.getReviewer())){
			if(reviewers.indexOf("," + inspectionGradeType.getReviewer() + ",")==-1){
				reviewers.append(inspectionGradeType.getReviewer() + ",");
			}
		}
		for(InspectionGradeType child : inspectionGradeType.getChildren()){
			getReviewers(child,reviewers);
		}
	}
	
	public void createQuestionReport(SupplierInvestigate investigate) throws Exception{
		InputStream inputStream = getClass().getResourceAsStream("/template/report/inspection-question-report.xls");
		Workbook workbook = WorkbookFactory.create(inputStream);
		inputStream.close();
		Sheet sheet = workbook.getSheetAt(0);
		//Issued by:		
		sheet.getRow(3).getCell(3).setCellValue(ContextUtils.getUserName());
		//Issue Date:		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		sheet.getRow(4).getCell(3).setCellValue(sdf.format(new Date()));
	    
		//标题
		String fileName = (investigate.getEnterpriameseName()==null?"":investigate.getEnterpriameseName()) + "稽核问题点报告.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		workbook.write(response.getOutputStream());
	}
	/**
	  * 方法名: 删除供应商调查表
	  * <p>功能说明：</p>
	  * @param deleteIds
	 */
	public void deleteInvestigates(String deleteIds){
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				SupplierInvestigate investigate = getSupplierInvestigateById(Long.valueOf(id));
				if(investigate != null){
					ApiFactory.getInstanceService().deleteInstance(investigate);
				}
			}
		}
	}
	
	/**
	  * 方法名: 开始供应商调查表的流程
	  * <p>功能说明：</p>
	  * @param investigate
	  * @param jsonArray
	  * @param idTarget
	 */
	public void submitProcess(SupplierInvestigate investigate){
		saveInvestigate(investigate);
		Long processId=ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(WORKFLOW_DEFINITION_CODE).get(0).getId();
		ApiFactory.getInstanceService().submitInstance(processId,
				investigate);
	}
	
	/**
	  * 方法名: 完成任务
	  * <p>功能说明：</p>
	  * @param investigate
	  * @param taskId
	  * @param taskTransact
	 */
	public void completeTask(SupplierInvestigate investigate, Long taskId,
			TaskProcessingResult taskTransact) {
		saveInvestigate(investigate);
		ApiFactory.getTaskService().completeWorkflowTask(taskId,
				taskTransact);
		String opinion = Struts2Utils.getParameter("opinion");
		if(opinion == null){
			if(TaskProcessingResult.SUBMIT.equals(taskTransact.getKey())){
				opinion = "【"+investigate.getLastModifier()+"】在【"+DateUtil.formateTimeStr(investigate.getLastModifiedTime())+"】对调查报告进行了修改!";
			}
		}
		if(opinion != null){
			Opinion opinionParameter = new Opinion();
			if(TaskProcessingResult.SUBMIT.equals(taskTransact.getKey())){
				opinionParameter.setCustomField("修改供应商调查报告");
			}else{
				opinionParameter.setCustomField(Struts2Utils.getParameter("operateName"));
			}
			opinionParameter.setOpinion(opinion);
			opinionParameter.setTaskId(taskId);
			opinionParameter.setAddOpinionDate(new Date());
			ApiFactory.getOpinionService().saveOpinion(opinionParameter);
		}
	}
	
	/**
	 * 获得任务是否已完成，已取消，已指派，他人已领取状态
	 * 
	 * @param task
	 * @return
	 */
	private boolean isTaskComplete(WorkflowTask task) {
		return task.getActive().equals(2) || task.getActive().equals(3)
		|| task.getActive().equals(5) || task.getActive().equals(7);
	}

	public String getFieldPermission(Long taskId) {
		if (taskId == null) {
			Long processId = ApiFactory.getDefinitionService()
			.getWorkflowDefinitionsByCode(WORKFLOW_DEFINITION_CODE).get(0)
			.getId();
			return ApiFactory.getFormService()
			.getFieldPermissionNotStarted(
					processId);
		} else {
			WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
			if (isTaskComplete(task)) {
				return ApiFactory.getFormService()
				.getFieldPermission(false);
			} else {
				return ApiFactory.getFormService().getFieldPermission(
						taskId);
			}
		}
	}

	/**
	 * 根据任务获取实例
	 * @param taskId
	 * @return
	 */
	public SupplierInvestigate getInvestigateByTaskId(Long taskId) {
		return getSupplierInvestigate(ApiFactory.getFormService()
				.getFormFlowableIdByTask(taskId));
	}

	/**
	 * 获得loginName用户的该实例的当前任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getMyTask(SupplierInvestigate investigate, String loginName) {
		return ApiFactory.getTaskService().getActiveTaskByLoginName(investigate,
				loginName);
	}

	/**
	 * 获得任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getWorkflowTask(Long taskId) {
		return ApiFactory.getTaskService().getTask(taskId);
	}

	/*
	 * 删除流程实例时的回调方法（在流程参数中配置了beanName）
	 * 
	 * @see com.norteksoft.wf.engine.client.FormFlowableDeleteInterface#
	 * deleteFormFlowable(java.lang.Long)
	 */
	@Override
	public void deleteFormFlowable(Long id) {
		SupplierInvestigate investigate = getSupplierInvestigateById(id);
		if(investigate != null){
			investigateDao.delete(investigate);
			useFileManager.useAndCancelUseFiles(investigate.getSituationAttachment(),null);
		}
	}
	public void deleteInvestigateByTaskId(Long taskId) {
		WorkflowTask task = getWorkflowTask(taskId);
		if(task != null){
			ApiFactory.getInstanceService().deleteInstance(task.getProcessInstanceId());
		}
	}
	/**
	  * 方法名: 导入供应商调查表
	  * <p>功能说明：导入供应商调查表</p>
	  * @param file
	  * @return
	  * @throws Exception
	 */
	public void importInvestigate(File file) throws Exception{
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = book.getSheetAt(0);
		Map<String,String> fieldMap = getInvestigateTemplateMap();
		Map<String,Object> objMap = new HashMap<String, Object>();
		for(String name : fieldMap.keySet()){
			//包含下划线的字段不处理
			if(name.indexOf("_")>-1){
				continue;
			}
			Object value = getValueByExcelFlag(sheet,fieldMap.get(name));
			objMap.put(name,value);
		}
		SupplierInvestigate investigate = getObj(objMap, SupplierInvestigate.class);
		if(StringUtils.isEmpty(investigate.getEvaluateMan())){
			investigate.setEvaluateMan(ContextUtils.getUserName());
		}
		if(investigate.getEvaluateDate()==null){
			investigate.setEvaluateDate(new Date());
		}
		//处理集合
		//加工检测设备
		List<SupplierInvestigateTestingDevice> testingDevices = getListByExecelFlag(sheet,
				new String[]{"F29","J29","L29"},6,
				new String[]{"cots","model","vendor"},SupplierInvestigateTestingDevice.class,investigate);
		investigate.setTestingDevices(testingDevices);
		//生产的产品
		List<SupplierInvestigateStockProduct> stockProducts = getListByExecelFlag(sheet,
				new String[]{"A44","B44","D44","G44","I44","K44","M44"},7,
				new String[]{"productCode","productName","technics","auxiliaryProduct","auxiliaryProductNum","auxiliaryDate"
				,"productCapacity"},
		SupplierInvestigateStockProduct.class,investigate);
		investigate.setStockProducts(stockProducts);
		//评价项目
		List<SupplierInvestigateEvaluateRecord> evaluateRecords = getListByExecelFlag(sheet,
				new String[]{"A61","F61","J61"},6,
				new String[]{
				"itemName",
				"selfAssessment",
				"auditContent"},
				SupplierInvestigateEvaluateRecord.class,investigate);
		investigate.setEvaluateRecords(evaluateRecords);
		//联系人信息
		Map<String,Object> contactMap = new HashMap<String, Object>();
		contactMap.put("department",getValueByExcelFlag(sheet,fieldMap.get("contact_department")));
		contactMap.put("job",getValueByExcelFlag(sheet,fieldMap.get("contact_job")));
		contactMap.put("contact",getValueByExcelFlag(sheet,fieldMap.get("contact_contact")));
		contactMap.put("contactPhone",getValueByExcelFlag(sheet,fieldMap.get("contact_contactPhone")));
		contactMap.put("contactFax",getValueByExcelFlag(sheet,fieldMap.get("contact_contactFax")));
		contactMap.put("email",getValueByExcelFlag(sheet,fieldMap.get("contact_email")));
		SupplierInvestigateContact contact = getObj(contactMap,SupplierInvestigateContact.class);
		if(contact != null){
			investigate.setContacts(new ArrayList<SupplierInvestigateContact>());
			contact.setSupplierInvestigate(investigate);
			investigate.getContacts().add(contact);
		}
		saveInvestigate(investigate);
	}
	
	/**
	  * 方法名:获取集合 
	  * <p>功能说明：根据起始单元格和行数获取集合的值</p>
	  * @param firstExcelFlag
	  * @param count
	  * @param fieldNames
	  * @param cla
	  * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	private <T> List<T> getListByExecelFlag(Sheet sheet,String[] excelFlags,int count,String[] fieldNames,Class<T> cla,SupplierInvestigate investigate) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<T> objs = new ArrayList<T>();
		int firstRowIndex  = Integer.valueOf(excelFlags[0].substring(1))-1;
		for(int i=0;i<count;i++){
			Map<String, Object> objMap = new HashMap<String, Object>();
			for(int j = 0;j<fieldNames.length;j++){
				String excelFlag = excelFlags[j].substring(0,1) + (firstRowIndex+i+1);
				Object val = getValueByExcelFlag(sheet,excelFlag);
				if(val != null && StringUtils.isNotEmpty(val.toString())){
					objMap.put(fieldNames[j],val);
				}
			}
			if(objMap.isEmpty()){
				continue;
			}
			T obj = getObj(objMap, cla);
			if(obj != null){
				PropertyUtils.setProperty(obj,"supplierInvestigate",investigate);
				objs.add(obj);
			}
		}
		return objs;
	}
	/**
	  * 方法名:获取值
	  * <p>功能说明：根据单元格的位置字符串获取对应的值</p>
	  * @param excelFlag
	  * @param flagMap
	  * @return
	 */
	private Object getValueByExcelFlag(Sheet sheet,String excelFlag){
		int columnIndex = excelFlag.charAt(0)-65;
		int rowIndex  = Integer.valueOf(excelFlag.substring(1))-1;
		return getValueByExcelFlag(sheet,rowIndex,columnIndex);
	}
	
	/**
	  * 方法名:获取值
	  * <p>功能说明：根据行和列的索引获取对应的值</p>
	  * @param excelFlag
	  * @param flagMap
	  * @return
	 */
	private Object getValueByExcelFlag(Sheet sheet,int rowIndex,int columnIndex){
		Row row = sheet.getRow(rowIndex);
		if(row == null){
			return null;
		}
		Cell cell = row.getCell(columnIndex);
		if(cell == null){
			return null;
		}
		if(Cell.CELL_TYPE_STRING == cell.getCellType()){
			return cell.getStringCellValue();
		}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				return cell.getDateCellValue();
			}else{
				return cell.getNumericCellValue();
			}
		}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
			return cell.getCellFormula();
		}else{
			return cell.getStringCellValue();
		}
	}
	/**
	  * 方法名: 导入供应商调查报告的字段和行列的对应
	  * <p>功能说明：</p>
	  * @return
	 */
	private Map<String,String> getInvestigateTemplateMap(){
		Map<String,String> templateMap = new HashMap<String, String>();
		templateMap.put("enterpriameseName","C3");//企业名称
		templateMap.put("enterpriseAddress","C4");//企业地址
		templateMap.put("depositBank","C5");//开户银行
		templateMap.put("bankAccount","C6");//银行帐号
		templateMap.put("enterpriseType","C8");//企业类型
		templateMap.put("isEssereCertificato","J9");//是否通过体系认证
		templateMap.put("certificatoName","C10");//认证名称
		templateMap.put("certificatoPassDate","H10");//认证通过日期
		templateMap.put("isHavePermit","L10");//有无生产许可证
		templateMap.put("partner","C11");//合作伙伴
		templateMap.put("matchMethod","C12");//合作方式
		templateMap.put("employeeNumber","F14");//员工总人数
		templateMap.put("seniorEngineerNumber","F15");//高级工程师人数
		templateMap.put("engineerNumber","F16");//工程师人数
		templateMap.put("inspectionNumber","F17");//产品质检人数
		templateMap.put("managerNumber","F18");//管理人员人数
		templateMap.put("workNumber","F19");//工人人数
		templateMap.put("yearlyCapacity","F20");//年生产能力
		templateMap.put("productProgram","F21");//生产纲领
		templateMap.put("lastYearIncome","C22");//去年销售收入
		templateMap.put("yearIncome","H22");//年度销售收入
		templateMap.put("nextYearIncome","L22");//预计明年销售收入
		templateMap.put("adoptSoftware","F23");//采用何种软件
		templateMap.put("developeSoftware","F24");//开发过何种软件 
		templateMap.put("applySkill","F25");//目前应用的技术 
		templateMap.put("haveMouldDesign","F26");//工装模具设计方式
		templateMap.put("haveMouldMaufacture","F27");//工装模具制造方式(自行)
		templateMap.put("planning","B35");//企业发展规划
		templateMap.put("supportAndTechnique","F36");//资金和技术支持情况
		templateMap.put("program","F37");//产品规划 
		templateMap.put("newSkill","F38");//引进的新技术
		templateMap.put("mouldManufacture","F39");//工装模具设计制造方式 
		templateMap.put("detectionMeans","F40");//引进的新试验设备和检测手段
		templateMap.put("otherDirection","F41");//其他说明 
		templateMap.put("productTypeNum","C42");//现生产产品种数
		templateMap.put("majorTypeNum","F42");//主要产品种数
		templateMap.put("otherIntroduction","A52");//其他 方面的介绍
		templateMap.put("situation","A55");//公司 情况 
		templateMap.put("evaluateMan","I69");//评价人员
		templateMap.put("evaluateDate","L69");//评价日期
		templateMap.put("evaluateConclusion","A68");//综合评价及结论
		templateMap.put("stockProduct_first","A44");//产品的第一个单元格
		templateMap.put("evaluateRecord_first","A61");//评价项目的第一个单元格
		templateMap.put("testingDevice_first","F29");//加工检测设备的第一个单元格
		//联系人信息
		templateMap.put("contact_department","C57");//部门
		templateMap.put("contact_job","J57");//职务
		templateMap.put("contact_contact","C58");//联系人
		templateMap.put("contact_contactPhone","J58");//联系电话
		templateMap.put("contact_contactFax","C59");//传真
		templateMap.put("contact_email","J59");//邮箱地址
		return templateMap;
	}
	
	/**
	 * 获取考察报告的状态列表
	 * @return
	 */
	public List<Option> getInspectionReportInspectionStateOptions(){
		List<Option> options = new ArrayList<Option>();
		Option option = new Option();
		option.setName(InspectionReport.STATE_DEFAULT);
		option.setValue(InspectionReport.STATE_DEFAULT);
		options.add(option);
		
		option = new Option();
		option.setName(InspectionReport.STATE_COUNTERSIGN);
		option.setValue(InspectionReport.STATE_COUNTERSIGN);
		options.add(option);
		
		option = new Option();
		option.setName(InspectionReport.STATE_AUDIT);
		option.setValue(InspectionReport.STATE_AUDIT);
		options.add(option);
		
		option = new Option();
		option.setName(InspectionReport.STATE_PASS);
		option.setValue(InspectionReport.STATE_PASS);
		options.add(option);
		
		option = new Option();
		option.setName(InspectionReport.STATE_FAIL);
		option.setValue(InspectionReport.STATE_FAIL);
		options.add(option);
		return options;
	}
	/**
	 * 转移供应商调查表到供应商台帐
	 */
	@Override
	public void endInstanceExecute(Long long1) {
		SupplierInvestigate investigate = investigateDao.get(long1);
		if(investigate.getWorkflowInfo() == null
			||!(investigate.getWorkflowInfo().getProcessState() == ProcessState.END || investigate.getWorkflowInfo().getProcessState() == ProcessState.MANUAL_END)){
			return;
		}
		if(!"已审核".equals(investigate.getInvestigateState())){
			return;
		}
		if(!SupplierInvestigate.RESULT_PASS.equals(investigate.getInvestigateResult())){
			return;
		}
		Supplier supplier = new Supplier();
//		supplier.setCode(formCodeGenerated.generateSupplierCode());
		supplier.setName(investigate.getEnterpriameseName());
		supplier.setAddress(investigate.getEnterpriseAddress());
		supplier.setBuildAbility(investigate.getYearlyCapacity());
		supplier.setCertificationOf3c(false);
		if("是".equals(investigate.getIsEssereCertificato())){
			supplier.setCertificationOfTS16949(true);
		}else{
			supplier.setCertificationOfTS16949(false);
		}
		supplier.setCompanyId(ContextUtils.getCompanyId());
		supplier.setCreateDate(new Date());
		supplier.setCreatedTime(new Date());
		supplier.setCreator(ContextUtils.getUserName());
		if(!investigate.getContacts().isEmpty()){
			SupplierInvestigateContact contact = investigate.getContacts().get(0);
			supplier.setEmail(contact.getEmail());
			supplier.setFax(contact.getContactFax());
			supplier.setLinkMan(contact.getContact());
			supplier.setLinkManDepartment(contact.getDepartment());
			supplier.setLinkManDuty(contact.getJob());
			supplier.setLinkPhone(contact.getContactPhone());
		}
		supplier.setEnterpriseDescription(investigate.getSituation());
		supplier.setEnterpriseProperty(investigate.getEnterpriseType());
		if(!investigate.getTestingDevices().isEmpty()){
			StringBuffer sb = new StringBuffer();
			for(SupplierInvestigateTestingDevice testingDevice : investigate.getTestingDevices()){
				if(sb.length()>0){
					sb.append(",");
				}
				if(StringUtils.isNotEmpty(testingDevice.getVendor())){
					sb.append(testingDevice.getVendor() + "生产的");
				}
				if(StringUtils.isNotEmpty(testingDevice.getModel())){
					sb.append("型号为" + testingDevice.getModel() + "的");
				}
				if(StringUtils.isNotEmpty(testingDevice.getCots())){
					sb.append(testingDevice.getCots());
				}
			}
			supplier.setEquipmentDescription(sb.toString());
		}
		supplier.setHeadcount(investigate.getEmployeeNumber());
		supplier.setLastModifiedTime(new Date());
		supplier.setLastModifier(ContextUtils.getUserName());
		supplier.setManagerCount(investigate.getManagerNumber());
		supplier.setOtherCertification(investigate.getCertificatoName());
		supplier.setQualityAssurance(investigate.getOtherDirection());
		supplier.setQualityControlCount(investigate.getInspectionNumber());
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotEmpty(investigate.getAdoptSoftware())){
			sb.append("使用的软件:" + investigate.getAdoptSoftware());
		}
		if(StringUtils.isNotEmpty(investigate.getApplySkill())){
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append("目前应用的技术:" + investigate.getApplySkill());
		}
		supplier.setResearchAbility(sb.toString());
		supplier.setState(Supplier.STATE_POTENTIAL);
		if(investigate.getSeniorEngineerNumber()!=null||investigate.getEngineerNumber() != null){
			int num = investigate.getSeniorEngineerNumber()==null?0:investigate.getSeniorEngineerNumber();
			num += investigate.getEngineerNumber()==null?0:investigate.getEngineerNumber();
			supplier.setTechnologyCount(num);
		}
		supplier.setUseState(Supplier.USESTATE_QUALIFIED);
		//体系证书
		if(StringUtils.isNotEmpty(investigate.getCertificatoName())){
			supplier.setCertificates(new ArrayList<Certificate>());
			Map<String,Object> objMap = new HashMap<String, Object>();
			objMap.put("name",investigate.getCertificatoName());
			if(investigate.getCertificatoPassDate() != null){
				objMap.put("certificationDate",investigate.getCertificatoPassDate());
			}
			Certificate certificate = getObj(objMap,Certificate.class);
			if(certificate != null){
				certificate.setSupplier(supplier);
				supplier.getCertificates().add(certificate);
			}
		}
		//供应的产品
		if(!investigate.getStockProducts().isEmpty()){
			supplier.setSupplyProducts(new ArrayList<SupplyProduct>());
			for(SupplierInvestigateStockProduct stockProduct : investigate.getStockProducts()){
				Map<String,Object> objMap = new HashMap<String, Object>();
				objMap.put("code",stockProduct.getProductCode());
				objMap.put("name",stockProduct.getProductName());
				ProductBom bom = bomManager.getProductBomByBomCode(stockProduct.getProductCode());
				if(bom != null){
					objMap.put("materialType",bom.getMaterialType());
					objMap.put("importance",bom.getImportance());
				}
				SupplyProduct supplyProduct = getObj(objMap,SupplyProduct.class);
				if(supplyProduct != null){
					supplyProduct.setSupplier(supplier);
					supplier.getSupplyProducts().add(supplyProduct);
				}
			}
		}
		//联系人信息
		/*if(!investigate.getContacts().isEmpty()){
			supplier.setSupplierLinkMans(new ArrayList<SupplierLinkMan>());
			for(SupplierInvestigateContact contact : investigate.getContacts()){
				Map<String,Object> objMap = new HashMap<String, Object>();
				objMap.put("linkMan",contact.getContact());
				objMap.put("linkPhone",contact.getContactPhone());
				objMap.put("email",contact.getEmail());
				objMap.put("linkManDepartment",contact.getDepartment());
				objMap.put("linkManDuty",contact.getJob());
				SupplierLinkMan linkMan = getObj(objMap,SupplierLinkMan.class);
				if(linkMan != null){
					linkMan.setSupplier(supplier);
					supplier.getSupplierLinkMans().add(linkMan);
				}
			}
		}*/
		supplierDao.save(supplier);
	}
}
