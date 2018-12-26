package com.ambition.supplier.admittance.service;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.supplier.admittance.dao.InspectionGradeDao;
import com.ambition.supplier.admittance.dao.InspectionGradeTypeDao;
import com.ambition.supplier.admittance.dao.InspectionReportDao;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.archives.dao.SupplyProductDao;
import com.ambition.supplier.entity.CheckGrade;
import com.ambition.supplier.entity.CheckGradeType;
import com.ambition.supplier.entity.InspectionGrade;
import com.ambition.supplier.entity.InspectionGradeType;
import com.ambition.supplier.entity.InspectionReport;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.manager.service.ProductExploitationRecordManager;
import com.ambition.supplier.supervision.service.CheckGradeTypeManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;
import com.norteksoft.wf.engine.client.OnExecutingTransation;

@Service
@Transactional
public class InspectionReportManager implements FormFlowableDeleteInterface,OnExecutingTransation{
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	@Autowired
	private InspectionReportDao inspectionReportDao;
	
	@Autowired
	private InspectionGradeTypeDao inspectionGradeTypeDao;
	
	@Autowired
	private InspectionGradeDao inspectionGradeDao;
	
	@SuppressWarnings("unused")
	@Autowired
	private SupplyProductDao supplyProductDao;

	@Autowired
	private SupplierDao supplierDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	@Autowired
 	private CheckGradeTypeManager checkGradeTypeManager;
	
	@Autowired
	private ProductExploitationRecordManager productExploitationRecordManager;
	
	/**
	 * 查询供应商考察记录
	 * @param page
	 * @return
	 */
	public Page<InspectionReport> search(Page<InspectionReport> page){
		return inspectionReportDao.search(page);
	}
	
	public InspectionReport getInspectionReport(Long id){
		return inspectionReportDao.get(id);
	}
	public InspectionReport getInspectionReportById(Long id){
		List<InspectionReport> reports = inspectionGradeDao.find("from InspectionReport i where i.id = ?", id);
		if(reports.isEmpty()){
			return null;
		}else{
			return reports.get(0);
		}
	}
	/**
	 * 保存考察报告
	 * @param inspectionReport
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public void saveInspectionReport(InspectionReport inspectionReport,JSONArray jsonArray,String idTarget){
		if(inspectionReport.getId()==null){
//			inspectionReport.setCode(formCodeGenerated.generateInspectionReportCode());
		}else{
			idTarget = "inspection";
		}
		//设置会签人员字符串
		if(inspectionReport.getEvaluationMemberLoginNames() != null){
			String[] names = inspectionReport.getEvaluationMemberLoginNames().split(";");
			StringBuffer sb = new StringBuffer("");
			for(String name : names){
				if(StringUtils.isNotEmpty(name)){
					if(sb.length()>0){
						sb.append(",");
					}
					if(name.indexOf("=")>0){
						sb.append(name.split("=")[1]);
					}else{
						sb.append(name);
					}
				}
			}
			inspectionReport.setEvaluationMemberLoginNames(sb.toString());
		}
		if(inspectionReport.getInspectionManLoginName() != null && inspectionReport.getInspectionManLoginName().indexOf("=")>0){
			inspectionReport.setInspectionManLoginName(inspectionReport.getInspectionManLoginName().split("=")[1]);
		}
		if(jsonArray != null){
			if(inspectionReport.getId()!=null){
				String hql = "update InspectionGradeType i set reviewer = null,reviewerLoginName=null,weight=null where i.inspectionReport.id = ?";
				inspectionGradeTypeDao.createQuery(hql,inspectionReport.getId()).executeUpdate();
				StringBuffer reviewerLoginNames = new StringBuffer(","),reviewers = new StringBuffer(",");
				for(int i=0;i<jsonArray.size();i++){
					JSONObject json = jsonArray.getJSONObject(i);
					InspectionGradeType inspectionGradeType = inspectionGradeTypeDao.get(json.getLong("id"));
					inspectionGradeType.setReviewer(json.getString("reviewer"));
					String loginName = json.getString("reviewerLoginName");
					if(loginName.indexOf("=")>0){
						loginName = loginName.split("=")[1];
					}
					inspectionGradeType.setReviewerLoginName(loginName);
					if(reviewerLoginNames.indexOf(loginName+",")==-1){
						reviewerLoginNames.append(loginName+",");
						reviewers.append(json.getString("reviewer")+",");
					}
					if(StringUtils.isNotEmpty(json.getString("weight"))){
						inspectionGradeType.setWeight(json.getDouble("weight"));
					}
					inspectionGradeTypeDao.save(inspectionGradeType);
				}
				if(reviewers.length()>1){
					reviewers.deleteCharAt(0).deleteCharAt(reviewers.length()-1);
					reviewerLoginNames.deleteCharAt(0).deleteCharAt(reviewerLoginNames.length()-1);
				}
				inspectionReport.setReviewers(reviewers.toString());
				inspectionReport.setReviewerLoginNames(reviewerLoginNames.toString());
				inspectionReport.setLastModifiedTime(new Date());
				inspectionReport.setLastModifier(ContextUtils.getUserName());
				inspectionReportDao.save(inspectionReport);
			}else{
				Map<String,JSONObject> reviewMap = new HashMap<String, JSONObject>();
				StringBuffer reviewerLoginNames = new StringBuffer(","),reviewers = new StringBuffer(",");
				for(int i=0;i<jsonArray.size();i++){
					JSONObject json = jsonArray.getJSONObject(i);
					reviewMap.put(json.getString("id"),json);
					String loginName = json.getString("reviewerLoginName");
					if(loginName.indexOf("=")>0){
						loginName = loginName.split("=")[1];
					}
					if(reviewerLoginNames.indexOf(loginName+",")==-1){
						reviewerLoginNames.append(loginName+",");
						reviewers.append(json.getString("reviewer")+",");
					}
				}
				if(reviewers.length()>1){
					reviewers.deleteCharAt(0).deleteCharAt(reviewers.length()-1);
					reviewerLoginNames.deleteCharAt(0).deleteCharAt(reviewerLoginNames.length()-1);
				}
				inspectionReport.setReviewers(reviewers.toString());
				inspectionReport.setReviewerLoginNames(reviewerLoginNames.toString());
				inspectionReportDao.save(inspectionReport);
				
				Map<String,CheckGradeType> targetMap = new HashMap<String,CheckGradeType>();
				String hql = "from CheckGradeType c where c.companyId = ? and c.type = ?";
				List<CheckGradeType> list = inspectionReportDao.find(hql,ContextUtils.getCompanyId(),CheckGradeType.TYPE_SURVEY);
				for(CheckGradeType checkGradeType : list){
					targetMap.put(checkGradeType.getId().toString(),checkGradeType);
				}
				if(inspectionReport.getInspectionGradeTypes()==null){
					inspectionReport.setInspectionGradeTypes(new ArrayList<InspectionGradeType>());
				}else{
					inspectionReport.getInspectionGradeTypes().clear();
				}
				hql = "select distinct c.checkGradeType from CheckGrade c where c.companyId = ? and  c.type = ?";
				List<CheckGradeType> checkGradeTypes = inspectionReportDao.createQuery(hql,ContextUtils.getCompanyId(),CheckGradeType.TYPE_SURVEY).list();
				Map<String,InspectionGradeType> existMap = new HashMap<String, InspectionGradeType>();
				for(CheckGradeType checkGradeType : checkGradeTypes){
					InspectionGradeType inspectionGradeType = copyInspectionGradeType(checkGradeType, inspectionReport, existMap,reviewMap);
					for(CheckGrade  checkGrade : checkGradeType.getCheckGrades()){
						InspectionGrade inspectionGrade = new InspectionGrade();
						inspectionGrade.setCompanyId(ContextUtils.getCompanyId());
						inspectionGrade.setCreatedTime(new Date());
						inspectionGrade.setCreator(ContextUtils.getUserName());
						inspectionGrade.setLastModifiedTime(new Date());
						inspectionGrade.setLastModifier(ContextUtils.getUserName());
						inspectionGrade.setInspectionGradeType(inspectionGradeType);
						inspectionGrade.setName(checkGrade.getName());
						inspectionGrade.setWeight(checkGrade.getWeight());
						inspectionGrade.setOrderNum(checkGrade.getOrderNum());
//						if(checkGrade.getId().toString().equals(selectGrade)){
//							inspectionGrade.setIsSelect(true);
//							inspectionGradeType.setRealFee(inspectionGrade.getWeight());
//						}
						inspectionGradeDao.save(inspectionGrade);
					}
					inspectionGradeTypeDao.save(inspectionGradeType);
				}
			}
//			List<InspectionGradeType> inspectionGradeTypes = inspectionGradeTypeDao.find("from InspectionGradeType i where i.inspectionReport.id = ?",inspectionReport.getId());
//			StringBuffer sb = new StringBuffer(",");
//			for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
//				getReviewers(inspectionGradeType,sb);
//				if(sb.length()>1){
//					inspectionGradeType.setReviewers("");
//				}else{
//					inspectionGradeType.setReviewers(sb.deleteCharAt(0).deleteCharAt(sb.length()-1).toString());
//				}
//				inspectionGradeTypeDao.save(inspectionGradeType);
//				sb.delete(0,sb.length()-1).append(",");
//			}
		}else{
			inspectionReport.setLastModifiedTime(new Date());
			inspectionReport.setLastModifier(ContextUtils.getUserName());
			inspectionReportDao.save(inspectionReport);
		}
		productExploitationRecordManager.updateApplyStateBySupplier(inspectionReport.getSupplier());
		logUtilDao.debugLog("保存", inspectionReport.toString());
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
	/**
	 * 保存考察报告的评分项目
	 * @param inspectionReport
	 * @param params
	 */
	public void saveInspectionReportInspectionGradeTypes(InspectionReport inspectionReport,JSONArray jsonArray){
		if(jsonArray != null){
			List<InspectionGradeType> topList = new ArrayList<InspectionGradeType>();
			Map<Long,InspectionGradeType> existMap = new HashMap<Long, InspectionGradeType>();
			for(int i=0;i<jsonArray.size();i++){
				JSONObject json = jsonArray.getJSONObject(i);
				InspectionGradeType inspectionGradeType = inspectionGradeTypeDao.get(json.getLong("id"));
				String selectGrade = json.getString("selectId");
				for(InspectionGrade inspectionGrade : inspectionGradeType.getInspectionGrades()){
					if(inspectionGrade.getId().toString().equals(selectGrade)){
						if(!inspectionGrade.getIsSelect()){
							inspectionGrade.setIsSelect(true);
							inspectionGradeType.setRealFee(inspectionGrade.getWeight());
							inspectionGradeDao.save(inspectionGrade);
						}
					}else if(inspectionGrade.getIsSelect()){
						inspectionGrade.setIsSelect(false);
						inspectionGradeDao.save(inspectionGrade);
					}
				}
				if(json.containsKey("remark")){
					inspectionGradeType.setRemark(json.getString("remark"));
				}
				inspectionGradeTypeDao.save(inspectionGradeType);
				InspectionGradeType top = getTop(inspectionGradeType,existMap);
				if(!topList.contains(top)){
					topList.add(top);
				}
			}
			for(InspectionGradeType inspectionGradeType : topList){
				inspectionGradeType.setRealFee(calculateRealFee(inspectionGradeType));
				inspectionGradeTypeDao.save(inspectionGradeType);
			}
			Double realFee = 0.0;
			for(InspectionGradeType inspectionGradeType : inspectionReport.getInspectionGradeTypes()){
				if(inspectionGradeType.getRealFee() != null){
					realFee += inspectionGradeType.getRealFee();
				}
			}
			inspectionReport.setRealFee(realFee);
			inspectionReportDao.save(inspectionReport);
		}
		logUtilDao.debugLog("保存", inspectionReport.toString());
	}
	
	private Double calculateRealFee(InspectionGradeType inspectionGradeType){
		if(inspectionGradeType.getChildren().isEmpty()){
			return inspectionGradeType.getRealFee();
		}else{
			Double realFee = 0.0;
			for(InspectionGradeType child : inspectionGradeType.getChildren()){
				Double val = calculateRealFee(child);
				if(val != null){
					realFee += val;
				}
			}
			inspectionGradeType.setRealFee(realFee);
			inspectionGradeTypeDao.save(inspectionGradeType);
			return realFee;
		}
	}
	
	private InspectionGradeType copyInspectionGradeType(CheckGradeType checkGradeType,InspectionReport inspectionReport,Map<String,InspectionGradeType> existMap,Map<String,JSONObject> reviewMap){
		if(checkGradeType == null){
			return null;
		}else if(existMap.containsKey(checkGradeType.getId().toString())){
			return existMap.get(checkGradeType.getId().toString());
		}else{
			InspectionGradeType inspectionGradeType = new InspectionGradeType();
			inspectionGradeType.setCompanyId(inspectionGradeType.getCompanyId());
			inspectionGradeType.setCreatedTime(new Date());
			inspectionGradeType.setLastModifier(ContextUtils.getUserName());
			inspectionGradeType.setLastModifiedTime(new Date());
			inspectionGradeType.setName(checkGradeType.getName());
			inspectionGradeType.setLevel(checkGradeType.getLevel());
			inspectionGradeType.setOrderNum(checkGradeType.getOrderNum());
			inspectionGradeType.setTotalFee(checkGradeType.getTotalFee());
			inspectionGradeType.setParent(copyInspectionGradeType(checkGradeType.getParent(),inspectionReport,existMap,reviewMap));
			if(inspectionGradeType.getParent()==null){
				inspectionGradeType.setInspectionReport(inspectionReport);
			}
			if(reviewMap.containsKey(checkGradeType.getId().toString())){
				JSONObject json = reviewMap.get(checkGradeType.getId().toString());
				inspectionGradeType.setReviewer(json.getString("reviewer"));
				String loginName = json.getString("reviewerLoginName");
				if(loginName.indexOf("=")>0){
					loginName = loginName.split("=")[1];
				}
				inspectionGradeType.setReviewerLoginName(loginName);
				if(StringUtils.isNotEmpty(json.getString("weight"))){
					inspectionGradeType.setWeight(json.getDouble("weight"));
				}
			}
			inspectionGradeTypeDao.save(inspectionGradeType);
			existMap.put(checkGradeType.getId().toString(),inspectionGradeType);
			return inspectionGradeType;
		}
	}
	
	/**
	 * 复制考察评分标准为评分项目
	 * @param checkGradeType
	 * @param parent
	 * @return
	 */
	private InspectionGradeType copyCheckGradeType(CheckGradeType checkGradeType,InspectionGradeType parent){
		InspectionGradeType inspectionGradeType = new InspectionGradeType();
		inspectionGradeType.setId(checkGradeType.getId());
		inspectionGradeType.setParent(parent);
		inspectionGradeType.setCompanyId(checkGradeType.getCompanyId());
		inspectionGradeType.setLevel(checkGradeType.getLevel());
		inspectionGradeType.setName(checkGradeType.getName());
		inspectionGradeType.setTotalFee(checkGradeType.getTotalFee());
		inspectionGradeType.setOrderNum(checkGradeType.getOrderNum());
		List<InspectionGradeType> children = new ArrayList<InspectionGradeType>();
		if(checkGradeType.getChildren().size()>0){
			Double realTotalFee = 0.0;
			for(CheckGradeType child : checkGradeType.getChildren()){
				InspectionGradeType childType = copyCheckGradeType(child,inspectionGradeType);
				realTotalFee += childType.getRealFee();
				children.add(childType);
			}
			inspectionGradeType.setRealFee(realTotalFee);
		}else{
			List<InspectionGrade> inspectionGrades = new ArrayList<InspectionGrade>();
			for(CheckGrade checkGrade : checkGradeType.getCheckGrades()){
				InspectionGrade inspectionGrade = new InspectionGrade();
				inspectionGrade.setId(checkGrade.getId());
				inspectionGrade.setInspectionGradeType(inspectionGradeType);
				inspectionGrade.setCompanyId(inspectionGradeType.getCompanyId());
				inspectionGrade.setName(checkGrade.getName());
				inspectionGrade.setWeight(checkGrade.getWeight());
				inspectionGrades.add(inspectionGrade);
			}
			if(inspectionGrades.size()>0){
				inspectionGrades.get(0).setIsSelect(true);
				inspectionGradeType.setRealFee(inspectionGrades.get(0).getWeight());
			}
			inspectionGradeType.setInspectionGrades(inspectionGrades);
		}
		inspectionGradeType.setChildren(children);
		return inspectionGradeType;
	}
	
	public void setInspectionGradeTypes(InspectionReport inspectionReport){
		if(inspectionReport.getId() == null){
			List<CheckGradeType> firstLevelTypes = checkGradeTypeManager.getTopCheckGradeTypes(CheckGradeType.TYPE_SURVEY);
			List<InspectionGradeType> inspectionGradeTypes = new ArrayList<InspectionGradeType>();
			Double realTotalFee = 0.0,totalFee = 0.0;
			for(CheckGradeType checkGradeType : firstLevelTypes){
				InspectionGradeType inspectionGradeType = copyCheckGradeType(checkGradeType,null);
				inspectionGradeTypes.add(inspectionGradeType);
				realTotalFee += inspectionGradeType.getRealFee();
				if(inspectionGradeType.getTotalFee() != null){
					totalFee += inspectionGradeType.getTotalFee();
				}
			}
			inspectionReport.setTotalFee(totalFee);
			inspectionReport.setRealFee(100.0);
			inspectionReport.setInspectionGradeTypes(inspectionGradeTypes);
		}
	}
	private InspectionGradeType getTop(InspectionGradeType inspectionGradeType,Map<Long,InspectionGradeType> existMap){
		if(inspectionGradeType.getParent() == null){
			return inspectionGradeType;
		}else if(existMap.containsKey(inspectionGradeType.getParent().getId())){
			return existMap.get(inspectionGradeType.getParent().getId());
		}else{
			existMap.put(inspectionGradeType.getParent().getId(),inspectionGradeType.getParent());
			return getTop(inspectionGradeType.getParent(), existMap);
		}
	}
	/**
	 * 复制考察评分标准为评分项目
	 * @param checkGradeType
	 * @param parent
	 * @return
	 */
	private InspectionGradeType copyInspectionGradeType(InspectionGradeType inspectionGradeType,InspectionGradeType parent,Map<Long,InspectionGradeType> existMap){
		if(existMap.containsKey(inspectionGradeType.getId())){
			InspectionGradeType target = new InspectionGradeType();
			target.setId(inspectionGradeType.getId());
			target.setParent(parent);
			target.setCompanyId(inspectionGradeType.getCompanyId());
			target.setLevel(inspectionGradeType.getLevel());
			target.setName(inspectionGradeType.getName());
			target.setTotalFee(inspectionGradeType.getTotalFee());
			target.setOrderNum(inspectionGradeType.getOrderNum());
			target.setRemark(inspectionGradeType.getRemark());
			List<InspectionGradeType> children = new ArrayList<InspectionGradeType>();
			if(inspectionGradeType.getChildren().size()>0){
				Double realTotalFee = 0.0;
				for(InspectionGradeType child : inspectionGradeType.getChildren()){
					if(existMap.containsKey(child.getId())){
						InspectionGradeType childType = copyInspectionGradeType(child,inspectionGradeType,existMap);
						realTotalFee += childType.getRealFee();
						children.add(childType);
					}
				}
				target.setRealFee(realTotalFee);
			}else{
				List<InspectionGrade> inspectionGrades = new ArrayList<InspectionGrade>();
				InspectionGrade select = null;
				for(InspectionGrade inspectionGrade : inspectionGradeType.getInspectionGrades()){
					InspectionGrade targetGrade = new InspectionGrade();
					targetGrade.setId(inspectionGrade.getId());
					targetGrade.setInspectionGradeType(target);
					targetGrade.setCompanyId(inspectionGrade.getCompanyId());
					targetGrade.setName(inspectionGrade.getName());
					targetGrade.setWeight(inspectionGrade.getWeight());
					targetGrade.setIsSelect(inspectionGrade.getIsSelect());
					inspectionGrades.add(targetGrade);
					if(inspectionGrade.getIsSelect()){
						select = inspectionGrade;
					}
				}
				if(select != null){
					target.setRealFee(select.getWeight());
				}else if(inspectionGrades.size()>0){
					inspectionGrades.get(0).setIsSelect(true);
					target.setRealFee(inspectionGrades.get(0).getWeight());
				}
				target.setInspectionGrades(inspectionGrades);
			}
			target.setChildren(children);
			return target;
		}else{
			return null;
		}
	}
	private List<InspectionGradeType> getSelfInspectionGradeType(InspectionGradeType inspectionGradeType,String loginName){
		List<InspectionGradeType> inspectionGradeTypes = new ArrayList<InspectionGradeType>();
		if(loginName.equals(inspectionGradeType.getReviewerLoginName())){
			inspectionGradeTypes.add(inspectionGradeType);
		}else{
			for(InspectionGradeType child : inspectionGradeType.getChildren()){
				inspectionGradeTypes.addAll(getSelfInspectionGradeType(child, loginName));
			}
		}
		return inspectionGradeTypes;
	}
	public List<InspectionGradeType> getInspectionGradeTypesByUser(InspectionReport inspectionReport,String loginName){
		List<InspectionGradeType> allTypes = inspectionReport.getInspectionGradeTypes();
		List<InspectionGradeType> inspectionGradeTypes = new ArrayList<InspectionGradeType>();
		for(InspectionGradeType inspectionGradeType : allTypes){
			inspectionGradeTypes.addAll(getSelfInspectionGradeType(inspectionGradeType,loginName));
		}
		List<InspectionGradeType> result = new ArrayList<InspectionGradeType>();
		Map<Long,InspectionGradeType> existMap = new HashMap<Long, InspectionGradeType>();
		List<InspectionGradeType> topList = new ArrayList<InspectionGradeType>();
		
		for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
			existMap.put(inspectionGradeType.getId(),inspectionGradeType);
			InspectionGradeType top = getTop(inspectionGradeType,existMap);
			if(!topList.contains(top)){
				topList.add(top);
			}
			List<InspectionGradeType> allChildren = inspectionGradeType.getAllChildren();
			for(InspectionGradeType child : allChildren){
				if(child.getReviewerLoginName()==null||loginName.equals(child.getReviewerLoginName())){
					existMap.put(child.getId(),child);
				}
			}
		}
		for(InspectionGradeType inspectionGradeType : topList){
			result.add(copyInspectionGradeType(inspectionGradeType,null,existMap));
		}
		return result;
	}
	/**
	 * 根据评分项目获取评分的表格html
	 * @param inspectionReport
	 * @return
	 */
	public String getInspectionGradeHtml(InspectionReport inspectionReport){
		int compare = 70;
		String idTarget = "inspection";
		if(inspectionReport.getId() == null){
			List<CheckGradeType> firstLevelTypes = checkGradeTypeManager.getTopCheckGradeTypes(CheckGradeType.TYPE_SURVEY);
			List<InspectionGradeType> inspectionGradeTypes = new ArrayList<InspectionGradeType>();
			Double realTotalFee = 0.0,totalFee = 0.0;
			for(CheckGradeType checkGradeType : firstLevelTypes){
				InspectionGradeType inspectionGradeType = copyCheckGradeType(checkGradeType,null);
				inspectionGradeTypes.add(inspectionGradeType);
				realTotalFee += inspectionGradeType.getRealFee();
				if(inspectionGradeType.getTotalFee() != null){
					totalFee += inspectionGradeType.getTotalFee();
				}
			}
			inspectionReport.setTotalFee(totalFee);
			inspectionReport.setRealFee(100.0);
			inspectionReport.setInspectionGradeTypes(inspectionGradeTypes);
			idTarget = "check";
		}
		List<InspectionGradeType> firstLevelTypes = inspectionReport.getInspectionGradeTypes();
		int rowNumbers = 0;
		StringBuffer tableStr = new StringBuffer("");
		for(InspectionGradeType inspectionGradeType : firstLevelTypes){
			if(!hasInspectionGrades(inspectionGradeType)){
				continue;
			}
			rowNumbers++;
			createCheckTypeTr(tableStr, inspectionGradeType, "",rowNumbers);
		}
		double totalFee=0.0;
		if(inspectionReport.getTotalFee()!=null){
			totalFee=inspectionReport.getTotalFee();
		}
		DecimalFormat df = new DecimalFormat("0.#");
		tableStr.append("<tr>")
		.append("<td colspan=2><input type='hidden' name='params.idTarget' value='"+idTarget+"'></input>供应商综合评价得分（原始得分/总分×100）&nbsp;&nbsp;&nbsp;&nbsp;S=<input size=\"12\" name=\"realFee\" id='realFee' readonly='readonly' value='"+inspectionReport.getRealFee()+"'/>(分)</td>")
		.append("</tr>");
		tableStr.append("<tr>")
		.append("<td colspan=2 style=\"font-style: italic;\"><input type='hidden' id='compare' value='"+compare+"'></input>备注</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td colspan=2 style=\"font-style: italic;\">1.总共分为"+rowNumbers+"大项，总分为"+df.format(totalFee)+"分；</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td colspan=2 style=\"font-style: italic;\"><input type='hidden' id='allTotalFee' name='totalFee' value='"+df.format(totalFee)+"'></input>2.供应商评价得分（原始得分/总分（"+df.format(totalFee)+"）×100），S大于等于"+compare+"分时，此供应商列入进一步考察对象。若低于"+compare+"分时，此供应商判为不合格。</td>")
		.append("</tr>");
		return tableStr.toString();
	}
	
	private boolean hasInspectionGrades(InspectionGradeType inspectionGradeType){
		if(!inspectionGradeType.getChildren().isEmpty()){
			for(InspectionGradeType child : inspectionGradeType.getChildren()){
				if(hasInspectionGrades(child)){
					return true;
				}
			}
			return false;
		}else{
			return !inspectionGradeType.getInspectionGrades().isEmpty();
		}
	}
	private void createCheckTypeTr(StringBuffer sb,InspectionGradeType inspectionGradeType,String parentIndex,int index){
		String indexStr = getIndexStr(inspectionGradeType.getLevel(),parentIndex,index);
		sb.append("<tr>")
		.append("<td colspan=2><b>" + indexStr + "</b>" +inspectionGradeType.getName()+"</td>")
		.append("</tr>");
		if(!inspectionGradeType.getChildren().isEmpty()){
			int i=1;
			for(InspectionGradeType child : inspectionGradeType.getChildren()){
				createCheckTypeTr(sb,child,indexStr,i);
				i++;
			}
		}else{
			DecimalFormat df = new DecimalFormat("0.#");
			sb.append("<tr><td><ul style='list-style:none;margin:0px;'>");
			for(InspectionGrade inspectionGrade : inspectionGradeType.getInspectionGrades()){
				sb.append("<li style='float:left;width:260px;line-height:24px;'><input type=\"radio\" name=\"params.a"+inspectionGradeType.getId()+"\" value='"+inspectionGrade.getId()+"' "+(inspectionGrade.getIsSelect()?"checked=\"checked\"":"")+" title='"+inspectionGrade.getWeight()+"'>"+inspectionGrade.getName()+"（"+df.format(inspectionGrade.getWeight())+"）</input></li>");
			}
			sb.append("</ul></td><td style='width:30%;'><textarea style='width:100%;' rows=2 name=\"params.a"+inspectionGradeType.getId()+"_remark\">"+(inspectionGradeType.getRemark()==null?"":inspectionGradeType.getRemark())+"</textarea></td></tr>");
		}
	}
	
	private String getIndexStr(int level,String parentIndex,int index){
		StringBuffer sb = new StringBuffer("");
		for(int i=1;i<level+1;i++){
			sb.append("&nbsp;");
		}
		sb.append(parentIndex + index + ".");
		return sb.toString();
	}
	/**
	 * 根据考察报告查询其他的考察记录
	 * @param inspectionReport
	 * @return
	 */
	public List<InspectionReport> queryOtherInspectionReport(InspectionReport inspectionReport){
		String hql = "from InspectionReport i where i.companyId = ? and i.id <> ? and i.supplier = ?";
		List<InspectionReport> inspectionReports = inspectionReportDao.find(hql,ContextUtils.getCompanyId(),inspectionReport.getId(),inspectionReport.getSupplier());
		return inspectionReports;
	}
	private int createQuestionList(Sheet sheet,List<InspectionGradeType> inspectionGradeTypes,int rowNumber,CellStyle style){
		for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
			if(inspectionGradeType.getChildren().isEmpty()){
				if(inspectionGradeType.getTotalFee()==null||inspectionGradeType.getRealFee()==null){
					continue;
				}else if(inspectionGradeType.getTotalFee().equals(inspectionGradeType.getRealFee())){
					continue;
				}
				Row row = sheet.createRow(rowNumber);
				row.setHeightInPoints(28);
				for(int i=0;i<37;i++){
					row.createCell(i).setCellStyle(style);
				}
				row.getCell(0).setCellValue(rowNumber-11);
				sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,1,12));
				row.getCell(1).setCellValue(inspectionGradeType.getRemark()==null?"":inspectionGradeType.getRemark());
				//改善对策
				sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,13,27));
				//责任人
				sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,28,30));
				//完成日
				sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,31,33));
				//追踪
				sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,34,36));
				rowNumber++;
			}else{
				rowNumber = createQuestionList(sheet, inspectionGradeType.getChildren(),rowNumber,style);
			}
		}
		return rowNumber;
	}
	public void createQuestionReport(InspectionReport inspectionReport) throws Exception{
		InputStream inputStream = getClass().getResourceAsStream("/template/report/inspection-question-report.xls");
		Workbook workbook = WorkbookFactory.create(inputStream);
		inputStream.close();
		Sheet sheet = workbook.getSheetAt(0);
		//Issued by:		
		sheet.getRow(3).getCell(3).setCellValue(ContextUtils.getUserName());
		//Issue Date:		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		sheet.getRow(4).getCell(3).setCellValue(sdf.format(new Date()));
		//表单编号:
		sheet.getRow(2).getCell(24).setCellValue("表单编号:" + inspectionReport.getCode() + " 版本:" + inspectionReport.getReportVersion());
		//供应商名称
		sheet.getRow(5).getCell(7).setCellValue(inspectionReport.getSupplier()==null?"":inspectionReport.getSupplier().getName());
		//供应商地址
		sheet.getRow(5).getCell(15).setCellValue(inspectionReport.getSupplier()==null?"":inspectionReport.getSupplier().getAddress());
		//访问日期
		sheet.getRow(5).getCell(24).setCellValue(inspectionReport.getInspectionDate()==null?"":sdf.format(inspectionReport.getInspectionDate()));
		//会签人员
		sheet.getRow(6).getCell(7).setCellValue(inspectionReport.getEvaluationMembers()==null?"":inspectionReport.getEvaluationMembers());
		//审核人员
		sheet.getRow(7).getCell(7).setCellValue(inspectionReport.getAuditMans()==null?"":inspectionReport.getAuditMans());
		//标题
		sheet.getRow(8).getCell(7).setCellValue("供应商现场审核");
		//评价内容
		sheet.getRow(9).getCell(7).setCellValue(inspectionReport.getComprehensiveEvaluation()==null?"":inspectionReport.getComprehensiveEvaluation());
		//问题列表
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short)10);
		style.setFont(font);
		style.setWrapText(true);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderTop((short) 1);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight((short) 1);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		int rowNumber = createQuestionList(sheet,inspectionReport.getInspectionGradeTypes(),12,style);
		//最后的备注
		Row row = sheet.createRow(rowNumber + 2);
		for(int i=0;i<37;i++){
			row.createCell(i);
		}
		sheet.addMergedRegion(new CellRangeAddress(rowNumber + 2,rowNumber + 2,0,36));
		row.getCell(0).setCellValue("注：SQE需在稽核完成后的两个工作日内将稽核问题汇总在此份报告中发给厂商，并要求厂商在五个工作日之类回复相关改善措施。");
		style = workbook.createCellStyle();
	    font = workbook.createFont();
	    font.setFontHeightInPoints((short)12);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    style.setFont(font);
	    row.getCell(0).setCellStyle(style);
	   
//	    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream(); 
//	    BufferedImage bufferImg = ImageIO.read(new File("c:/test.JPG")); 
//      ImageIO.write(bufferImg,"jpg",byteArrayOut); 
//
//	    HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,512,255,(short) 1,1,(short)10,20);
//	    Drawing drawing = sheet.createDrawingPatriarch();
//	    drawing.createPicture(anchor,workbook.addPicture(byteArrayOut.toByteArray(),Workbook.PICTURE_TYPE_JPEG));
	    
		//标题
		String fileName = (inspectionReport.getSupplier()==null?"":inspectionReport.getSupplier().getName()) + "稽核问题点报告.xls";
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
	//根据评分项目获取评分的表格html结束///////////////////////////////////
	public void deleteInspectionReports(String deleteIds){
		try {
			for(String id : deleteIds.split(",")){
				if(StringUtils.isNotEmpty(id)){
					InspectionReport inspectionReport = getInspectionReportById(Long.valueOf(id));
					if(inspectionReport != null){
						ApiFactory.getInstanceService().deleteInstance(inspectionReport);
						productExploitationRecordManager.updateApplyStateBySupplier(inspectionReport.getSupplier());
					}
				}
			}
			logUtilDao.debugLog("删除",deleteIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void submitProcess(InspectionReport inspectionReport,JSONArray jsonArray,String idTarget) throws Exception {
		try {
			saveInspectionReport(inspectionReport,jsonArray,idTarget);
			Long processId=ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode("supplier_inspectionreport").get(0).getId();
			ApiFactory.getInstanceService().submitInstance(processId,
					inspectionReport);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void completeTaskGrade(InspectionReport inspectionReport,JSONArray jsonArray, Long taskId) {
		try {
			saveInspectionReportInspectionGradeTypes(inspectionReport,jsonArray);
			ApiFactory.getTaskService().completeWorkflowTask(taskId,
					TaskProcessingResult.AGREEMENT);
			String opinion = Struts2Utils.getParameter("opinion");
			Opinion opinionParameter = new Opinion();
			opinionParameter.setCustomField("提交");
			opinionParameter.setOpinion(opinion);
			opinionParameter.setTaskId(taskId);
			opinionParameter.setAddOpinionDate(new Date());
			ApiFactory.getOpinionService().saveOpinion(opinionParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void completeEvaluateTask(InspectionReport inspectionReport, Long taskId,
			TaskProcessingResult taskTransact) {
		inspectionReportDao.save(inspectionReport);
		completeTask(inspectionReport,taskId, taskTransact);
	}
	public void completeTask(InspectionReport inspectionReport, Long taskId,
			TaskProcessingResult taskTransact) {
		try {
			inspectionReportDao.save(inspectionReport);
			ApiFactory.getTaskService().completeWorkflowTask(taskId,
					taskTransact);
			String opinion = Struts2Utils.getParameter("opinion");
			Opinion opinionParameter = new Opinion();
			opinionParameter.setCustomField(Struts2Utils.getParameter("operateName"));
			opinionParameter.setOpinion(opinion);
			opinionParameter.setTaskId(taskId);
			opinionParameter.setAddOpinionDate(new Date());
			ApiFactory.getOpinionService().saveOpinion(opinionParameter);
			
			if(ApiFactory.getInstanceService().isInstanceComplete(inspectionReport)){//结果完成
				if(InspectionReport.RESULT_PASS.equals(inspectionReport.getInspectionResult())){//通过
					if(InspectionReport.STATE_PASS.equals(inspectionReport.getInspectionState())){
						Supplier supplier = inspectionReport.getSupplier();
						if(Supplier.STATE_ELIMINATED.equals(supplier.getState())||Supplier.STATE_POTENTIAL.equals(supplier.getState())){
							supplier.setState(Supplier.STATE_ALLOW);
							supplierDao.save(supplier);
						}
					}
				}
				//更新开发状态
				productExploitationRecordManager.updateApplyStateBySupplier(inspectionReport.getSupplier());
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			.getWorkflowDefinitionsByCode("supplier_inspectionreport").get(0)
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
	public InspectionReport getInpectionReportByTaskId(Long taskId) {
		return getInspectionReport(ApiFactory.getFormService()
				.getFormFlowableIdByTask(taskId));
	}

	/**
	 * 获得loginName用户的该实例的当前任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getMyTask(InspectionReport inspectionReport, String loginName) {
		return ApiFactory.getTaskService().getActiveTaskByLoginName(inspectionReport,
				loginName);
	}

	public void deleteInspectionReortByTaskId(Long taskId){
		WorkflowTask task = getWorkflowTask(taskId);
		if(task != null){
			ApiFactory.getInstanceService().deleteInstance(task.getProcessInstanceId());
		}
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
		InspectionReport report = getInspectionReportById(id);
		if(report != null){
			inspectionReportDao.delete(report);
		}
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
	
	public void execute(Long taskId) {
	}
	
}
