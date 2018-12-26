package com.ambition.carmfg.checkinspection.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.checkinspection.dao.MfgCheckInspectionReportDao;
import com.ambition.carmfg.checkinspection.dao.MfgPatrolItemDao;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgCheckItem;
import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.MfgPatrolItem;
import com.ambition.carmfg.entity.MfgPlantParameterItem;
import com.ambition.carmfg.entity.PlantParameter;
import com.ambition.carmfg.entity.PlantParameterDetail;
import com.ambition.carmfg.plantparameter.dao.PlantParameterDao;
import com.ambition.iqc.entity.AcceptanceQualityLimit;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.product.EscColumnToBean;
import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.abnormal.util.BaseUtil;
import com.ambition.spc.dataacquisition.dao.SpcSgSampleDao;
import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.dataacquisition.service.SpcMfgInterface;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSgTag;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.layertype.dao.LayerTypeDao;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;

/**    
* 检验报告SERVICE
* @authorBy wlongfeng
*
*/
@Service
@Transactional
public class MfgCheckInspectionReportManager {
	@Autowired
	private  MfgCheckInspectionReportDao mfgCheckInspectionReportDao;
	
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	@Autowired
	private UseFileManager useFileManager;
	
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	
	@Autowired
	private SpcMfgInterface spcMfgInterface;
	
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;
	
	@Autowired
	private SpcSgSampleDao spcSgSampleDao;
	@Autowired
	private PlantParameterDao plantParameterDao;
	@Autowired
	private LayerTypeDao layerTypeDao;
//	
//	@Autowired
//	private ProductBomManager productBomManager;
	@Autowired
	private MfgPatrolItemDao mfgPatrolItemDao;
	
	public MfgCheckInspectionReport getMfgCheckInspectionReport(Long id){
		return mfgCheckInspectionReportDao.get(id);
	}
	
	public MfgCheckInspectionReport getMfgCheckInspectionReportByBatchNo(String batchNo){
		List<MfgCheckInspectionReport> incomingInspectionActionsReportList=mfgCheckInspectionReportDao.getIncomingInspectionActionsReportByBatchNo(batchNo);
		MfgCheckInspectionReport incomingInspectionActionsReport=new MfgCheckInspectionReport();
		if(incomingInspectionActionsReportList.size()>0){
			incomingInspectionActionsReport=incomingInspectionActionsReportList.get(0);
		}
		return incomingInspectionActionsReport;
	}
	
	public MfgCheckInspectionReport getMfgCheckInspectionReportByInspectionNo(String inspectionNo){
		List<MfgCheckInspectionReport> incomingInspectionActionsReportList=mfgCheckInspectionReportDao.getIncomingInspectionActionsReportByInspectionNo(inspectionNo);
		if(incomingInspectionActionsReportList.isEmpty()){
			return null;
		}else{
			return incomingInspectionActionsReportList.get(0);
		}
	}
	
	public List<MfgPlantParameterItem> getMfgPlantParameterItem(String workProcedure,String machineNo){
		PlantParameter pp=plantParameterDao.getPlantParameter(workProcedure, machineNo);
		List<MfgPlantParameterItem> mfgPlantItems=null;
		if(pp!=null&&!pp.getCheckItems().isEmpty()){
			mfgPlantItems= new ArrayList<MfgPlantParameterItem>();
			for(PlantParameterDetail detail:pp.getCheckItems()){
				MfgPlantParameterItem item= new MfgPlantParameterItem();
				item.setItemName(detail.getPlantName());
				item.setParameterName(detail.getParameterName());
				item.setParameterSpc(detail.getParameterStandard());
				item.setParameterMax(detail.getLevela());
				item.setParameterMin(detail.getLevelb());
				mfgPlantItems.add(item);
			}
		}
		return mfgPlantItems;
	}
	
	/**
	 * 根据供应商,物料编码,检验数查询检验项目
	 * @param supplierId
	 * @param checkBomCode
	 * @param stockAmount
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Map<String,Object> getCheckItems(String workProcedure,String checkBomCode,Integer stockAmount,Date inspectionDate,List<JSONObject> existCheckItems,String inspectionPointType) throws 
	IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String,JSONObject> existItemMap = new HashMap<String, JSONObject>();
		if(existCheckItems != null){
			for(JSONObject json : existCheckItems){
				if(json.containsKey("checkItemName")){
					existItemMap.put(json.getString("checkItemName"),json);
				}
			}
		}
		List<MfgCheckItem> checkItems = new ArrayList<MfgCheckItem>();
		Pattern pattern = Pattern.compile("[0-9]*|[0-9]*\\.[0-9]*");
		//查询所有的检验项目
		String hql = "select i from MfgItemIndicator i where i.companyId = ? and i.mfgInspectingIndicator.workingProcedure = ? and i.mfgInspectingIndicator.model = ? ";
		List<MfgItemIndicator> itemIndicators = null;
		if(ContextUtils.getSubCompanyName()!=null){
			hql += " and i.mfgInspectingIndicator.businessUnitName= ?";
			itemIndicators = mfgCheckInspectionReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode,ContextUtils.getSubCompanyName());
		}else{
			itemIndicators = mfgCheckInspectionReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode);
		}
		if(itemIndicators==null||itemIndicators.size()==0){
			hql = "select i from MfgItemIndicator i where i.companyId = ? and i.mfgInspectingIndicator.workingProcedure = ? and i.mfgInspectingIndicator.model = ? ";
			itemIndicators = mfgCheckInspectionReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode);
		}
		//itemIndicators = mfgCheckInspectionReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode);
		Collections.sort(itemIndicators,new Comparator<MfgItemIndicator>() {
			@Override
			public int compare(MfgItemIndicator o1, MfgItemIndicator o2) {
				Integer o1OrderNum = o1.getOrderNum();
				Integer o2OrderNum = o2.getOrderNum();
				if(o1OrderNum == null || o2OrderNum == null){
					o1OrderNum = o1.getId().intValue();
					o2OrderNum = o2.getId().intValue();
				}
				if(o1OrderNum<o2OrderNum){
					return -1;
				}else if(o1OrderNum>o2OrderNum){
					return 1;
				}else{
					return 0;
				}
			}
		});
		List<MfgInspectingItem> parentInspectingItems = new ArrayList<MfgInspectingItem>();
		Map<Long,List<MfgItemIndicator>> parentItemIndicatorMap = new HashMap<Long, List<MfgItemIndicator>>();
		for(MfgItemIndicator itemIndicator : itemIndicators){
			MfgInspectingItem inspectingItem = itemIndicator.getMfgInspectingItem();
			MfgInspectingItem parentInspectionItem = inspectingItem.getItemParent();
			if(parentInspectionItem==null){
				parentInspectionItem = inspectingItem;
			}
			if(parentItemIndicatorMap.containsKey(parentInspectionItem.getId())){
				parentItemIndicatorMap.get(parentInspectionItem.getId()).add(itemIndicator);
			}else{
				List<MfgItemIndicator> indicators = new ArrayList<MfgItemIndicator>();
				indicators.add(itemIndicator);
				parentItemIndicatorMap.put(parentInspectionItem.getId(),indicators);
				parentInspectingItems.add(parentInspectionItem);
			}
		}
		for(MfgInspectingItem parentInspectingItem : parentInspectingItems){
			List<MfgItemIndicator> indicators = parentItemIndicatorMap.get(parentInspectingItem.getId());
			//检查项目排序
//			Collections.sort(indicators,new Comparator<MfgItemIndicator>() {
//				@Override
//				public int compare(MfgItemIndicator o1, MfgItemIndicator o2) {
//					if(o1.getMfgInspectingItem().getCreatedTime().getTime()<o2.getMfgInspectingItem().getCreatedTime().getTime()){
//						return 0;
//					}else{
//						return 1;
//					}
//				}
//			});
			boolean isRowSpan = false;
			for(MfgItemIndicator itemIndicator : indicators){
				if(itemIndicator.getIsInEquipment()!=null&&itemIndicator.getIsInEquipment().equals("是")){//如果集成则过滤 
					continue;
				}
				if(inspectionPointType.equals(InspectionPointTypeEnum.PATROLINSPECTION.toString())){
					if(itemIndicator.getIsJnUnit()!=null&&itemIndicator.getIsJnUnit().equals("否")){//过滤掉巡检不要的检验项目
						continue;
					}
					if(itemIndicator.getInAmountPatrol()==null){
						continue;
					}
				}
				if(inspectionPointType.equals(InspectionPointTypeEnum.COMPLETEINSPECTION.toString())){//末检，数量为空代表不检验
					if(itemIndicator.getInAmountEnd()==null){
						continue;
					}
				}
				MfgCheckItem checkItem = new MfgCheckItem();
				if(!isRowSpan){
					isRowSpan = true;
					checkItem.setParentItemName(parentInspectingItem.getItemName());
					checkItem.setParentRowSpan(indicators.size());
				}
				if(itemIndicator.getIsJnUnit()!=null){
					checkItem.setIsJnUnit(itemIndicator.getIsJnUnit());
				}else{
					checkItem.setIsJnUnit("否");
				}				
				checkItem.setCheckMethod(itemIndicator.getMethod());
				checkItem.setInspectionType(itemIndicator.getMfgInspectingItem().getTopParent().getItemName());
				checkItem.setSpecifications(itemIndicator.getSpecifications());
				String level = itemIndicator.getInspectionLevel();
				checkItem.setInspectionLevel(level);
				checkItem.setRemark(itemIndicator.getRemark());
				checkItem.setCheckItemName(itemIndicator.getMfgInspectingItem().getItemName());
				checkItem.setCountType(itemIndicator.getCountType());
				checkItem.setMaxlimit(itemIndicator.getLevela());
				checkItem.setMinlimit(itemIndicator.getLevelb());
				checkItem.setInspectionAmount(itemIndicator.getInspectionAmount());
				if(itemIndicator.getFeatureId()!=null && !itemIndicator.getFeatureId().equals("")){
					checkItem.setFeatureId(itemIndicator.getFeatureId());
				}else{
					checkItem.setFeatureId("");
				}
				checkItem.setResults("");
				checkItem.setUnit(itemIndicator.getUnit());
				if("FIRSTINSPECTION".equals(inspectionPointType)){
					checkItem.setInspectionAmount(itemIndicator.getInAmountFir());
				}else if("PATROLINSPECTION".equals(inspectionPointType)){
					checkItem.setInspectionAmount(itemIndicator.getInAmountPatrol());
				}else{
					checkItem.setInspectionAmount(itemIndicator.getInAmountEnd());
				}
				/*checkItem.setIsJnUnit(itemIndicator.getIsJnUnit());
				checkItem.setIsInEquipment(itemIndicator.getIsJnUnit());*/
				if(existItemMap.containsKey(checkItem.getCheckItemName())){
					JSONObject json = existItemMap.get(checkItem.getCheckItemName());
					if(MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
						if(json.containsKey("results")){
							checkItem.setResults(json.getString("results"));
						}
					}else{
						for(int i=1;i<=30;i++){
							String fieldName = "result" + i;
							if(json.containsKey(fieldName)){
								String value = json.getString(fieldName);
								if(StringUtils.isNotEmpty(value)&&pattern.matcher(value).matches()){
									PropertyUtils.setProperty(checkItem,fieldName,Double.valueOf(value));
								}
							}
						}
					}
					if(json.containsKey("qualifiedAmount")){
						String value = json.getString("qualifiedAmount");
						if(StringUtils.isNotEmpty(value)&&StringUtils.isNumeric(value)){
							checkItem.setQualifiedAmount(Integer.valueOf(value));
						}
					}
					if(json.containsKey("unqualifiedAmount")){
						String value = json.getString("unqualifiedAmount");
						if(StringUtils.isNotEmpty(value)&&StringUtils.isNumeric(value)){
							checkItem.setUnqualifiedAmount(Integer.valueOf(value));
						}
					}
				}
				if(checkItem.getUnqualifiedAmount()!=null&&checkItem.getUnqualifiedAmount()>0){
					checkItem.setConclusion("NG");
				}else{
					checkItem.setConclusion("OK");
				}
				checkItems.add(checkItem);
			}
		}
		resultMap.put("checkItems",checkItems);
		return resultMap;
	}
	/**
	 * 根据供应商,物料编码,检验数查询检验项目
	 * @param supplierId
	 * @param checkBomCode
	 * @param stockAmount
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Map<String,Object> getPatrolItems(String workProcedure,String checkBomCode,Integer stockAmount,JSONArray existCheckItems) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String,Object> returnMap = new HashMap<String, Object>();
		Map<String,JSONObject> existItemMap = new HashMap<String, JSONObject>();
		if(existCheckItems != null){
			for(int i=0;i<existCheckItems.size();i++){
				JSONObject json = existCheckItems.getJSONObject(i);
				if(json.containsKey("checkItemName")){
					existItemMap.put(json.getString("checkItemName"),json);
				}
			}
		}
		List<MfgCheckItem> checkItems = new ArrayList<MfgCheckItem>();

		String baseType = sampleSchemeManager.getUseBaseType().getBaseType();
		Map<String,String> codeLetterMap = new HashMap<String, String>();
//		Map<String,AcceptanceQualityLimit> qualityLimitMap = new HashMap<String, AcceptanceQualityLimit>();
//		Pattern pattern = Pattern.compile("[0-9]*|[0-9]*\\.[0-9]*");
		//查询所有的检验项目
		String hql = "from MfgItemIndicator i where i.companyId = ? and i.mfgInspectingIndicator.workingProcedure = ? and i.mfgInspectingIndicator.materielCode = ? and i.mfgInspectingIndicator.isMax = ?";
		List<MfgItemIndicator> itemIndicators = mfgCheckInspectionReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode,true);
		List<MfgInspectingItem> parentInspectingItems = new ArrayList<MfgInspectingItem>();
		Map<Long,List<MfgItemIndicator>> parentItemIndicatorMap = new HashMap<Long, List<MfgItemIndicator>>();
		for(MfgItemIndicator itemIndicator : itemIndicators){
			if(!returnMap.containsKey("imgFileId")){
				List<Map<String,String>> files = useFileManager.parseFilesByString(itemIndicator.getMfgInspectingIndicator().getImgFileName());
				if(!files.isEmpty()){
					returnMap.put("imgFileId",files.get(0).get("id"));
				}
				returnMap.put("patrolSettings", itemIndicator.getMfgInspectingIndicator().getPatrolSettings());
				returnMap.put("standardVersion",itemIndicator.getMfgInspectingIndicator().getStandardVersion());
				returnMap.put("is3C",itemIndicator.getMfgInspectingIndicator().getIs3C());
				returnMap.put("isStandard",itemIndicator.getMfgInspectingIndicator().getIsStandard());
				returnMap.put("iskeyComponent",itemIndicator.getMfgInspectingIndicator().getIskeyComponent());
			}
			MfgInspectingItem inspectingItem = itemIndicator.getMfgInspectingItem();
			MfgInspectingItem parentInspectionItem = inspectingItem.getItemParent();
			if(parentInspectionItem==null){
				parentInspectionItem = inspectingItem;
			}
			if(parentItemIndicatorMap.containsKey(parentInspectionItem.getId())){
				parentItemIndicatorMap.get(parentInspectionItem.getId()).add(itemIndicator);
			}else{
				List<MfgItemIndicator> indicators = new ArrayList<MfgItemIndicator>();
				indicators.add(itemIndicator);
				parentItemIndicatorMap.put(parentInspectionItem.getId(),indicators);
				parentInspectingItems.add(parentInspectionItem);
			}
		}
		//父类项目排序
		Collections.sort(parentInspectingItems,new Comparator<MfgInspectingItem>() {
			@Override
			public int compare(MfgInspectingItem o1, MfgInspectingItem o2) {
				if(o1.getCreatedTime().getTime()<o2.getCreatedTime().getTime()){
					return 0;
				}else{
					return 1;
				}
			}
		});
		for(MfgInspectingItem parentInspectingItem : parentInspectingItems){
			List<MfgItemIndicator> indicators = parentItemIndicatorMap.get(parentInspectingItem.getId());
			//检查项目排序
			Collections.sort(indicators,new Comparator<MfgItemIndicator>() {
				@Override
				public int compare(MfgItemIndicator o1, MfgItemIndicator o2) {
					if(o1.getMfgInspectingItem().getCreatedTime().getTime()<o2.getMfgInspectingItem().getCreatedTime().getTime()){
						return 0;
					}else{
						return 1;
					}
				}
			});
			boolean isRowSpan = false;
			for(MfgItemIndicator itemIndicator : indicators){
				MfgCheckItem checkItem = new MfgCheckItem();
				if(!isRowSpan){
					isRowSpan = true;
					checkItem.setParentItemName(parentInspectingItem.getItemName());
					checkItem.setParentRowSpan(indicators.size());
				}
				checkItem.setInspectionPointType(InspectionPointTypeEnum.PATROLINSPECTION);
				checkItem.setCheckMethod(itemIndicator.getMfgInspectingItem().getMethod());
				checkItem.setInspectionType(itemIndicator.getMfgInspectingItem().getTopParent().getItemName());
				checkItem.setSpecifications(itemIndicator.getSpecifications());
				checkItem.setAttachmentFiles(itemIndicator.getMfgInspectingItem().getAttachmentFiles());
				String level = itemIndicator.getInspectionLevel();
				checkItem.setInspectionLevel(level);
				
				checkItem.setCheckItemName(itemIndicator.getMfgInspectingItem().getItemName());
				checkItem.setCountType(itemIndicator.getMfgInspectingItem().getCountType());
				checkItem.setMaxlimit(itemIndicator.getLevela());
				checkItem.setMinlimit(itemIndicator.getLevelb());
				if(itemIndicator.getFeatureId()!=null && !itemIndicator.getFeatureId().equals("")){
					checkItem.setFeatureId(itemIndicator.getFeatureId());
				}else{
					checkItem.setFeatureId("");
				}
				checkItem.setResults("");
				checkItem.setUnit(itemIndicator.getMfgInspectingItem().getUnit());
				//如果是设置的检验数量,则直接按照规定的检验数量检验
				if(itemIndicator.getInspectionAmount() != null && itemIndicator.getInspectionAmount()>0){
					checkItem.setInspectionLevel("n=" + itemIndicator.getInspectionAmount());
					if(itemIndicator.getInspectionAmount()>stockAmount){
						checkItem.setInspectionAmount(stockAmount);
					}else{
						checkItem.setInspectionAmount(itemIndicator.getInspectionAmount());
					}
					checkItem.setAqlAc(0);
					checkItem.setAqlRe(1);
				}else{
					//根据检验级别获取样本量字码
					if(!codeLetterMap.containsKey(level)){
						codeLetterMap.put(level,sampleCodeLetterManager.queryCodeLetter(baseType,level,stockAmount));
					}
					checkItem.setCodeLetter(codeLetterMap.get(level));
					//获取AQL标准
					if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
						checkItem.setAql(level);
					}else{
						checkItem.setAql(itemIndicator.getAqlStandard());
					}
//					String codeLetter = checkItem.getCodeLetter();
//					codeLetter = codeLetter==null?"":codeLetter;
//					String aql = checkItem.getAql()==null?"":checkItem.getAql();
//					String type = SampleScheme.ORDINARY_TYPE;
//					String key = type + "_" + codeLetter + "_" + aql;
//					if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
//						key += "_" + checkItem.getCountType();
//					}
//					if(!qualityLimitMap.containsKey(key)){
//						if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
//							qualityLimitMap.put(key,sampleSchemeManager.queryQualityLimit(codeLetter,aql,baseType,type,checkItem.getCountType()));
//						}else{
//							qualityLimitMap.put(key,sampleSchemeManager.queryQualityLimit(codeLetter,aql,baseType,type));
//						}
//					}
//					AcceptanceQualityLimit acceptanceQualityLimit = qualityLimitMap.get(key);
//					if(acceptanceQualityLimit!=null){
//						checkItem.setAqlAc(acceptanceQualityLimit.getAc());
//						checkItem.setAqlRe(acceptanceQualityLimit.getRe());
//						checkItem.setInspectionAmount(acceptanceQualityLimit.getAmount());
//						if(checkItem.getInspectionAmount() != null){
//							if(checkItem.getInspectionAmount()>stockAmount){
//								checkItem.setInspectionAmount(stockAmount);
//							}
//							checkItem.setQualifiedAmount(checkItem.getInspectionAmount());
//							checkItem.setUnqualifiedAmount(0);
//						}else{
//							checkItem.setQualifiedAmount(0);
//							checkItem.setUnqualifiedAmount(0);
//						}
//					}else{
//						checkItem.setQualifiedAmount(0);
//						checkItem.setUnqualifiedAmount(0);
//					}
					if(checkItem.getAqlAc()==null){
						checkItem.setAqlAc(0);
					}
					if(checkItem.getAqlRe()==null){
						checkItem.setAqlRe(1);
					}
				}
				
//				if(existItemMap.containsKey(checkItem.getCheckItemName())){
//					JSONObject json = existItemMap.get(checkItem.getCheckItemName());
//					if(MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
//						if(json.containsKey("results")){
//							checkItem.setResults(json.getString("results"));
//						}
//					}else{
//						for(int i=1;i<=30;i++){
//							String fieldName = "result" + i;
//							if(json.containsKey(fieldName)){
//								String value = json.getString(fieldName);
//								if(StringUtils.isNotEmpty(value)&&pattern.matcher(value).matches()){
//									PropertyUtils.setProperty(checkItem,fieldName,Double.valueOf(value));
//								}
//							}
//						}
//					}
//					if(json.containsKey("qualifiedAmount")){
//						String value = json.getString("qualifiedAmount");
//						if(StringUtils.isNotEmpty(value)&&StringUtils.isNumeric(value)){
//							checkItem.setQualifiedAmount(Integer.valueOf(value));
//						}
//					}
//					if(json.containsKey("unqualifiedAmount")){
//						String value = json.getString("unqualifiedAmount");
//						if(StringUtils.isNotEmpty(value)&&StringUtils.isNumeric(value)){
//							checkItem.setUnqualifiedAmount(Integer.valueOf(value));
//						}
//					}
//				}
				if(checkItem.getUnqualifiedAmount()!=null&&checkItem.getUnqualifiedAmount()>0){
					checkItem.setConclusion("NG");
				}else{
					checkItem.setConclusion("OK");
				}
				checkItems.add(checkItem);
			}
		}
		returnMap.put("checkItems",checkItems);
		return returnMap;
	}
	public List<MfgCheckInspectionReport> getIncomingInspectionActionsReportListByBatchNo(String batchNo){
		return mfgCheckInspectionReportDao.getIncomingInspectionActionsReportByBatchNo(batchNo);
	}
	
	public void saveIncomingInspectionActionsReport(MfgCheckInspectionReport incomingInspectionActionsReport){
		mfgCheckInspectionReportDao.save(incomingInspectionActionsReport);
	}
	/**
	 * 方法名: getMaterielLevelByCodeNo
	 * <p>功能说明：根据物料编码获取物料级别数据</p>
	 * 创建人:wuxuming 日期： 2015-3-2 version 1.0
	 * @param 
	 * @return
	 */
	public MfgInspectingIndicator getMaterielLevelByCodeNo(String checkedBomCode,String workProcedure) throws Exception{
		String hql=" from MfgInspectingIndicator l where l.materielCode=? and l.companyId=? and l.workingProcedure=? and l.isMax=? ";
		List<MfgInspectingIndicator> listObj=mfgCheckInspectionReportDao.find(hql, checkedBomCode,ContextUtils.getCompanyId(),workProcedure,true);
		if(!listObj.isEmpty()){
			if(listObj.size()>1){
				throw new  AmbFrameException("该品号获取的物料级别不具备唯一性，请联系相关人员!!");
			}else{
				MfgInspectingIndicator inspectingIndicator=listObj.get(0);
				return inspectingIndicator;
			}
		}else{
			return null;
		}
	}
	/**
	 * 方法名:getIncomingInspectionActionsReport 
	 * <p>功能说明：根据品号获取单据的物料级别数据</p>
	 * 创建人:wuxuming 日期： 2015-3-2 version 1.0
	 * @param 
	 * @return
	 */
	private MfgCheckInspectionReport getMfgCheckInspectionReport(MfgCheckInspectionReport mfgCheckInspectionReport) throws Exception{
		MfgInspectingIndicator inspectingIndicator=getMaterielLevelByCodeNo(mfgCheckInspectionReport.getCheckBomCode(),mfgCheckInspectionReport.getWorkProcedure());
		return mfgCheckInspectionReport;
	}
	/**
	 * 保存进货检验记录
	 * @param incomingInspectionActionsReport
	 * @param checkItemArray
	 * @throws Exception 
	 */
	public void saveIncomingInspectionActionsReport(MfgCheckInspectionReport incomingInspectionActionsReport,List<JSONObject> checkItemArray,JSONObject badItems,String method,InspectionPointTypeEnum typeEnum) throws Exception{
		if(!(MfgCheckInspectionReport.STATE_DEFAULT.equals(incomingInspectionActionsReport.getReportState())
			||MfgCheckInspectionReport.STATE_RECHECK.equals(incomingInspectionActionsReport.getReportState()))){
			throw new AmbFrameException("只能保存状态为【"+MfgCheckInspectionReport.STATE_DEFAULT+"】" +
					"或【"+MfgCheckInspectionReport.STATE_RECHECK+"】的检验报告!");
		}
		if(incomingInspectionActionsReport.getId() == null){
			incomingInspectionActionsReport.setCheckItems(new ArrayList<MfgCheckItem>());
			if(InspectionPointTypeEnum.PATROLINSPECTION.getCode().equals(typeEnum.getCode())){
//				incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateMfgPatrolInspectionReportCode());
			}else if(InspectionPointTypeEnum.FIRSTINSPECTION.getCode().equals(typeEnum.getCode())){
//				incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateFirstInspectionReportCode());
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(typeEnum.getCode())){
//				incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateStorageInspectionReportCode());
			}else if(InspectionPointTypeEnum.DELIVERINSPECTION.getCode().equals(typeEnum.getCode())){
//				incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateDeliverInspectionReportCode());
			}else{
//				incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateMfgCheckInspectionReportCode());
			}
		}else{
			incomingInspectionActionsReport.setLastModifiedTime(new Date());
			incomingInspectionActionsReport.setLastModifier(ContextUtils.getUserName());
		}
		//获取物料级别
		incomingInspectionActionsReport=getMfgCheckInspectionReport(incomingInspectionActionsReport);
		if(checkItemArray != null){
			incomingInspectionActionsReport.getCheckItems().clear();
			
			String allHisPatrolSpcSampleIds = incomingInspectionActionsReport.getSpcSampleIds()==null?"":incomingInspectionActionsReport.getSpcSampleIds();
			String allPatrolSpcSampleIds = ",";
			Map<String,QualityFeature> featureMap = new HashMap<String, QualityFeature>();
			for(JSONObject json : checkItemArray){
				MfgCheckItem checkItem = new MfgCheckItem();
				checkItem.setCompanyId(ContextUtils.getCompanyId());
				checkItem.setCreatedTime(new Date());
				checkItem.setCreator(ContextUtils.getUserName());
				checkItem.setLastModifiedTime(new Date());
				checkItem.setLastModifier(ContextUtils.getUserName());
				String values = "";
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());
					setProperty(checkItem, key.toString(),value);
					if(!"results".equals(key.toString())&& key.toString().indexOf("result")>=0){
						if(value != null && !value.equals("")){
							values += value + ",";
						}
					}
				}
				String featureId = null,countType = null;
				featureId = checkItem.getFeatureId();
				countType = checkItem.getCountType();
				checkItem.setCheckBomCode(incomingInspectionActionsReport.getCheckBomCode());
				checkItem.setCheckBomName(incomingInspectionActionsReport.getCheckBomName());
				checkItem.setWorkProcedure(incomingInspectionActionsReport.getWorkProcedure());
				checkItem.setInspectionDate(incomingInspectionActionsReport.getInspectionDate());
				checkItem.setMfgCheckInspectionReport(incomingInspectionActionsReport);
				incomingInspectionActionsReport.getCheckItems().add(checkItem);
				
				//保存SPC数据
				if("on".equals(method)){
					//System.out.println("method:on,countType:" + countType + ",featureId");
					//处理spc数据
					String spcSampleIds = "";
					if(MfgInspectingItem.COUNTTYPE_METERING.equals(countType) && StringUtils.isNotEmpty(featureId)){
						//spcSubGroupManager.saveGroupFromMfg(featureId,datas);
						if(StringUtils.isNotEmpty(values)){
							//System.out.println("featureId:" + featureId + ",values:" + values);
							if(!featureMap.containsKey(checkItem.getFeatureId())){
								List<QualityFeature> features = qualityFeatureDao.find("from QualityFeature q where q.id = ?",Long.valueOf(checkItem.getFeatureId()));
				    			if(features.isEmpty()){
				    				featureMap.put(checkItem.getFeatureId(),null);
				    			}else{
				    				featureMap.put(checkItem.getFeatureId(),features.get(0));
				    			}
							}
							QualityFeature feature = featureMap.get(checkItem.getFeatureId());
							if(feature != null){
								String hisSampleIds = checkItem.getSpcSampleIds();
								if(StringUtils.isNotEmpty(hisSampleIds)){
									allHisPatrolSpcSampleIds = allHisPatrolSpcSampleIds.replaceAll("," + hisSampleIds,",");
								}
								spcSampleIds = insertSpcByResults(feature,checkItem.getCreatedTime(),hisSampleIds,values,incomingInspectionActionsReport.getInspectionPointType(),incomingInspectionActionsReport.getWorkGroupType(),incomingInspectionActionsReport,checkItem.getEquipmentNo());
							}
							allPatrolSpcSampleIds += spcSampleIds;
						}
					
					}
					checkItem.setSpcSampleIds(spcSampleIds);
				}
			}
			//删除不存在采集数据的子组
			String[] ids = allHisPatrolSpcSampleIds.split(",");
			for(String id : ids){
				id = id.trim();
				if(StringUtils.isNotEmpty(id)){
					List<SpcSgSample> list = spcSgSampleDao.find("from SpcSgSample s where s.id = ?",Long.valueOf(id));
					if(!list.isEmpty()){
						SpcSgSample sample = list.get(0);
						spcSubGroupDao.delete(sample.getSpcSubGroup());
					}
				}
			}
			//设置最新的采集数据ID
			incomingInspectionActionsReport.setSpcSampleIds(allPatrolSpcSampleIds);
			
			if(method != null && method.equals("off")){
				if(incomingInspectionActionsReport.getId()==null && incomingInspectionActionsReport.getInspectionDatas() != null){
					//离线检验数据文件
					String id = incomingInspectionActionsReport.getInspectionDatas().split("\\|~\\|")[0];
					com.ambition.util.useFile.entity.UseFile useFile = useFileManager.getUseFile(Long.valueOf(id));
					InputStream in =null;
					try{
						in = (InputStream) useFile.getBlobValue().getBinaryStream();
					}catch(Exception e){
						e.printStackTrace();
					}
					
					//featureIds
					String featureIds = "",countType = null; 
					//最大检验数量
					int maxMount = 0;
					if(incomingInspectionActionsReport.getInspectionAmount() != null){
						maxMount = incomingInspectionActionsReport.getInspectionAmount();
					}
					
					for(JSONObject json : checkItemArray){
						countType = json.getString("countType");
						if(MfgInspectingItem.COUNTTYPE_METERING.equals(countType)){
							featureIds += json.getString("featureId")+",";
							if(json.getString("inspectionAmount")!=null){
								try {
									if(maxMount < json.getInt("inspectionAmount")){
										maxMount = json.getInt("inspectionAmount");
									}
								} catch (Exception e) {
									throw new AmbFrameException("SPC取值出错!",e);
								}
								
							}
						}
					}
					if(featureIds.length()!=0){
				    	featureIds.trim();
				    	featureIds = featureIds.substring(0,featureIds.length()-1);
				    	String[] qualityIds = featureIds.split(",");
				    	//附属信息
				    	Map<String,String> layersMap = new LinkedHashMap<String, String>();
					    for(String qid:qualityIds){
					    	if(qid != null && !qid.equals("")){
					    		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(qid));
						    	if(qualityFeature != null){
						    		if(qualityFeature.getFeatureLayers()!=null && qualityFeature.getFeatureLayers().size()!=0){
					    				for(int i = 0;i<qualityFeature.getFeatureLayers().size();i++){
					    					layersMap.put(qualityFeature.getFeatureLayers().get(i).getDetailName(), qualityFeature.getFeatureLayers().get(i).getDetailCode());
						    			}
						    		}
						    	}
					    	}
					    }
					    if(maxMount==0){
					    	throw new AmbFrameException("检验项目中没有设置质量参数!");
					    }else{
					    	spcMfgInterface.importMfgExcelDatas(qualityIds, maxMount, layersMap, in);
					    }
				    }
				}
			}
		}
		mfgCheckInspectionReportDao.save(incomingInspectionActionsReport);
		//更新文件附件的状态,不使用的文件,并设置新的文件为使用
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"), incomingInspectionActionsReport.getAttachmentFiles());
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisInspectionDatas"), incomingInspectionActionsReport.getInspectionDatas());
	}
	@Autowired
	private PatrolSettingsManager patrolSettingsManager;
	
	public void saveMfgPatrolReport(MfgCheckInspectionReport incomingInspectionActionsReport,JSONArray checkItemArray,JSONArray patrolItemArray) throws Exception{
		if(incomingInspectionActionsReport.getId() == null){
//			incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateMfgPatrolInspectionReportCode());
		}
		//获取物料级别
		incomingInspectionActionsReport=getMfgCheckInspectionReport(incomingInspectionActionsReport);
		//保存检验项目
		Map<String,String> featureMap = new HashMap<String, String>();
		if(checkItemArray != null){
			if(incomingInspectionActionsReport.getId()==null){
				incomingInspectionActionsReport.setCheckItems(new ArrayList<MfgCheckItem>());
			}else{
				incomingInspectionActionsReport.getCheckItems().clear();
			}
			for(int i=0;i<checkItemArray.size();i++){
				JSONObject json = checkItemArray.getJSONObject(i);
				MfgCheckItem checkItem = new MfgCheckItem();
				checkItem.setCompanyId(ContextUtils.getCompanyId());
				checkItem.setCreatedTime(new Date());
				checkItem.setCreator(ContextUtils.getUserName());
				checkItem.setLastModifiedTime(new Date());
				checkItem.setLastModifier(ContextUtils.getUserName());
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());
					setProperty(checkItem, key.toString(),value);
				}
				checkItem.setCheckBomCode(incomingInspectionActionsReport.getCheckBomCode());
				checkItem.setCheckBomName(incomingInspectionActionsReport.getCheckBomName());
				checkItem.setWorkProcedure(incomingInspectionActionsReport.getWorkProcedure());
				checkItem.setInspectionDate(incomingInspectionActionsReport.getInspectionDate());
				checkItem.setMfgCheckInspectionReport(incomingInspectionActionsReport);
				incomingInspectionActionsReport.getCheckItems().add(checkItem);
				if(StringUtils.isNotEmpty(checkItem.getFeatureId())&&MfgInspectingItem.COUNTTYPE_METERING.equals(checkItem.getCountType())){
					featureMap.put(checkItem.getCheckItemName(),checkItem.getFeatureId());
				}
			}
		}
		
		//保存巡检记录
		if(patrolItemArray != null){
			String allHisPatrolSpcSampleIds = "," + (incomingInspectionActionsReport.getSpcSampleIds()==null?"":incomingInspectionActionsReport.getSpcSampleIds())+",";
			String allPatrolSpcSampleIds = ",";
			if(incomingInspectionActionsReport.getId()==null){
				incomingInspectionActionsReport.setPatrolItems(new ArrayList<MfgPatrolItem>());
			}else if(MfgCheckInspectionReport.SAVE_MODE_HISTORY.equals(incomingInspectionActionsReport.getSaveMode())){
				incomingInspectionActionsReport.getPatrolItems().clear();
			}
			Map<String,QualityFeature> featureEntityMap = new HashMap<String, QualityFeature>();
			List<MfgPatrolItem> addItems = new ArrayList<MfgPatrolItem>();
			for(int i=0;i<patrolItemArray.size();i++){
				JSONObject json = patrolItemArray.getJSONObject(i);
				MfgPatrolItem patrolItem = null;
				if(MfgCheckInspectionReport.SAVE_MODE_PATROL.equals(incomingInspectionActionsReport.getSaveMode())
					&&json.containsKey("id")){
					String idStr = json.getString("id");
					for(MfgPatrolItem item : incomingInspectionActionsReport.getPatrolItems()){
						if(idStr.equals(item.getId().toString())){
							patrolItem = item;
							break;
						}
					}
				}
				json.remove("id");
				if(patrolItem != null){
					patrolItem.setLastModifiedTime(new Date());
					patrolItem.setLastModifier(ContextUtils.getUserName());
				}else{
					patrolItem = new MfgPatrolItem();
					patrolItem.setCompanyId(ContextUtils.getCompanyId());
					patrolItem.setCreatedTime(new Date());
					patrolItem.setCreator(ContextUtils.getUserName());
					patrolItem.setLastModifiedTime(new Date());
					patrolItem.setLastModifier(ContextUtils.getUserName());
				}
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());
					setProperty(patrolItem, key.toString(),value);
				}
				patrolItem.setMfgCheckInspectionReport(incomingInspectionActionsReport);
				
				//添加SPC数据
				if(featureMap.containsKey(patrolItem.getCheckItemName())&&StringUtils.isNotEmpty(patrolItem.getResult())){
					String featureId = featureMap.get(patrolItem.getCheckItemName());
					if(!featureEntityMap.containsKey(featureId)){
						List<QualityFeature> features = qualityFeatureDao.find("from QualityFeature q where q.id = ?",Long.valueOf(featureId));
		    			if(features.isEmpty()){
		    				featureEntityMap.put(featureId,null);
		    			}else{
		    				featureEntityMap.put(featureId,features.get(0));
		    			}
					}
					QualityFeature feature = featureEntityMap.get(featureId);
					String spcSampleIds = "";
					if(feature != null){
						String hisSampleIds = patrolItem.getSpcSampleIds();
						if(StringUtils.isNotEmpty(hisSampleIds)){
							allHisPatrolSpcSampleIds = allHisPatrolSpcSampleIds.replaceAll("," + hisSampleIds,",");
						}
						spcSampleIds = insertSpcByResults(feature,patrolItem.getInspectionDate(),hisSampleIds,patrolItem.getResult(),incomingInspectionActionsReport.getInspectionPointType(),incomingInspectionActionsReport.getWorkGroupType(),incomingInspectionActionsReport,null);
					}
					allPatrolSpcSampleIds += spcSampleIds;
					patrolItem.setSpcSampleIds(spcSampleIds);
				}
				//添加入明细
				if(patrolItem.getId() == null){
					addItems.add(patrolItem);
				}
			}
			incomingInspectionActionsReport.getPatrolItems().addAll(addItems);
			//删除不存在采集数据的子组
			String[] ids = allHisPatrolSpcSampleIds.split(",");
			for(String id : ids){
				if(StringUtils.isNotEmpty(id)){
					List<SpcSgSample> list = spcSgSampleDao.find("from SpcSgSample s where s.id = ?",Long.valueOf(id));
					if(!list.isEmpty()){
						SpcSgSample sample = list.get(0);
						spcSubGroupDao.delete(sample.getSpcSubGroup());
					}
				}
			}
			//设置最新的采集数据ID
			incomingInspectionActionsReport.setSpcSampleIds(allPatrolSpcSampleIds);
		}
		if("true".equals(Struts2Utils.getParameter("isSubmit"))){
			incomingInspectionActionsReport.setPatrolState(MfgCheckInspectionReport.PATROL_STATE_COMPLETE);
		}
		//设置产品类型,产品型号
//		ProductBom bom = productBomManager.getModelSpecificationByBomCode(incomingInspectionActionsReport.getCheckBomCode());
//		if(bom != null){
//			ProductBom model = productBomManager.getProductModelByBom(bom);
//			incomingInspectionActionsReport.setProductModel(model==null?null:model.getName());
//		}
		mfgCheckInspectionReportDao.save(incomingInspectionActionsReport);
		
		//更新文件附件的状态,不使用的文件,并设置新的文件为使用
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"), incomingInspectionActionsReport.getAttachmentFiles());
		//useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisInspectionDatas"), incomingInspectionActionsReport.getInspectionDatas());
		//巡检到期监控
		Date nexePatrolDate = patrolSettingsManager.monitorPatrolRecord(incomingInspectionActionsReport);
		incomingInspectionActionsReport.setNextPatrolDate(nexePatrolDate);
		mfgCheckInspectionReportDao.save(incomingInspectionActionsReport);
	}
	
	public List<MfgPatrolItem> queryPatrolItemByReport(Long reportId){
		String hql = "from MfgPatrolItem m where m.mfgCheckInspectionReport.id = ? order by m.inspectionDate";
		return mfgPatrolItemDao.find(hql,reportId);
	}
	public void deleteIncomingInspectionActionsReport(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			MfgCheckInspectionReport report = mfgCheckInspectionReportDao.get(Long.valueOf(id));
			deleteIncomingInspectionActionsReport(report);
		}
	}
	
	public void deleteIncomingInspectionActionsReport(MfgCheckInspectionReport incomingInspectionActionsReport){
		mfgCheckInspectionReportDao.delete(incomingInspectionActionsReport);
		patrolSettingsManager.deleteMonitor(incomingInspectionActionsReport.getId());
	}
	/**
	  * 方法名: 重置巡检状态
	  * <p>功能说明：</p>
	  * @param deleteIds
	 */
	public void rePatrol(String recIds){
		String[] ids = recIds.split(",");
		for(String id: ids){
			MfgCheckInspectionReport report = mfgCheckInspectionReportDao.get(Long.valueOf(id));
			report.setPatrolState(MfgCheckInspectionReport.PATROL_STATE_DEFAULT);
			Date nextPatrolDate = patrolSettingsManager.monitorPatrolRecord(report);
			report.setNextPatrolDate(nextPatrolDate);
			mfgCheckInspectionReportDao.save(report);
		}
	}
	
	public Page<MfgCheckInspectionReport> list(Page<MfgCheckInspectionReport> page,String workshop,String workProcedure,InspectionPointTypeEnum typeEnum){
		return mfgCheckInspectionReportDao.list(page,workshop,workProcedure,typeEnum);
	}
	public Page<MfgCheckInspectionReport> list(Page<MfgCheckInspectionReport> page,InspectionPointTypeEnum typeEnum){
		return mfgCheckInspectionReportDao.list(page,typeEnum);
	}
	public Page<MfgCheckInspectionReport> waitAuditList(Page<MfgCheckInspectionReport> page){
		return mfgCheckInspectionReportDao.waitAuditList(page);
	}	
	public Page<MfgCheckInspectionReport> recheckList(Page<MfgCheckInspectionReport> page){
		return mfgCheckInspectionReportDao.recheckList(page);
	}		
	
	public Page<MfgCheckInspectionReport> listAll(Page<MfgCheckInspectionReport> page,InspectionPointTypeEnum typeEnum){
		return mfgCheckInspectionReportDao.listAll(page,typeEnum);
	}
	public Page<MfgCheckInspectionReport> listAllProduct(Page<MfgCheckInspectionReport> page){
		/*return mfgCheckInspectionReportDao.listAllProduct(page);*/
		 String searchParameters = Struts2Utils.getParameter("searchParameters");
		String sql = "select machine_no from ( select distinct(machine_no) as machine_no from MFG_CHECK_INSPECTION_REPORT o where o.business_unit_name is not null ";
		Session session = mfgCheckInspectionReportDao.getSession();
		List<Object> searchParamsFault = new ArrayList<Object>();
		String appendSql = "";
		 if(StringUtils.isNotEmpty(searchParameters)){
	            JSONArray array = JSONArray.fromObject(searchParameters);
	            for(int i=0;i<array.size();i++){
	                JSONObject json = array.getJSONObject(i);
	                String propName = json.getString("propName");
	                Object propValue = json.getString("propValue");
	                String optSign = json.getString("optSign");
	                String dataType = json.getString("dataType");
	                if("like".equals(optSign)){
	                	appendSql += " and o.machine_no like ?";
	                    searchParamsFault.add("%" + propValue + "%");
	                }
	            }
	        }
		SQLQuery query = session.createSQLQuery("select count(distinct(machine_no)) from MFG_CHECK_INSPECTION_REPORT o where o.business_unit_name is not null "+appendSql);
       for(int i=0;i<searchParamsFault.size();i++){
           query.setParameter(i,searchParamsFault.get(i));
       }
       int totalCount = Integer.valueOf(query.list().get(0).toString());
       page.setTotalCount(totalCount);
       query = session.createSQLQuery(sql+ ""+appendSql+")");
       for(int i=0;i<searchParamsFault.size();i++){
           query.setParameter(i,searchParamsFault.get(i));
       }
//       System.out.println(sqlFault);
       query.setFirstResult((page.getPageNo()-1)*page.getPageSize());
       query.setMaxResults(page.getPageSize());
       //获取字段映射
		Field fs[] = MfgCheckInspectionReport.class.getDeclaredFields();
		Map<String,String> sqlFieldMap = new HashMap<String, String>();
		StringBuilder str = new StringBuilder();
		for(Field f : fs){
			str.delete(0,str.length());
			String name = f.getName();
			for(int i=0;i<name.length();i++){
				String s = name.substring(i,i+1);
				if(StringUtils.isAllUpperCase(s)){
					str.append("_" + s);
				}else{
					str.append(s);
				}
			}
			sqlFieldMap.put(str.toString().toUpperCase(),name);
		}
		sqlFieldMap.put("ID","id");
       query.setResultTransformer(new EscColumnToBean(MfgCheckInspectionReport.class,sqlFieldMap));
       @SuppressWarnings("unchecked")
       List<MfgCheckInspectionReport> results = query.list();
       page.setResult(results);
		return page;
	}
	/**
	  * 方法名: 过程检验不合格
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<MfgCheckInspectionReport> listUnqualifieds(Page<MfgCheckInspectionReport> page){
		return mfgCheckInspectionReportDao.listUnqualifieds(page);
	}
	public Page<MfgCheckInspectionReport> listCompletePatrol(Page<MfgCheckInspectionReport> page,String workshop,InspectionPointTypeEnum typeEnum){
		return mfgCheckInspectionReportDao.listCompletePatrol(page,workshop,typeEnum);
	}
	
	public Page<MfgCheckInspectionReport> listTemporaryPatrol(Page<MfgCheckInspectionReport> page,String workshop,String checkBomCode,String checkBomName){
		String hql = "from MfgCheckInspectionReport i where i.inspectionPointType = ? and i.companyId = ? and i.workshop = ? and i.patrolState = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(InspectionPointTypeEnum.PATROLINSPECTION);
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(workshop);
		searchParams.add(MfgCheckInspectionReport.PATROL_STATE_DEFAULT);
		if(StringUtils.isNotEmpty(checkBomCode)){
			hql += " and i.checkBomCode = ? and i.checkBomName = ?";
			searchParams.add(checkBomCode);
			searchParams.add(checkBomName);
		}
		return mfgCheckInspectionReportDao.findPage(page,hql,searchParams.toArray());
	}
	
	public Page<MfgCheckInspectionReport> listTemporaryPatrol(Page<MfgCheckInspectionReport> page){
		String hql = "from MfgCheckInspectionReport i where i.inspectionPointType = ? and i.patrolState = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(InspectionPointTypeEnum.PATROLINSPECTION);
		searchParams.add(MfgCheckInspectionReport.PATROL_STATE_DEFAULT);
//		if(StringUtils.isNotEmpty(checkBomCode)){
//			hql += " and i.checkBomCode = ? and i.checkBomName = ?";
//			searchParams.add(checkBomCode);
//			searchParams.add(checkBomName);
//		}
		return mfgCheckInspectionReportDao.findPage(page,hql,searchParams.toArray());
	}
	
	public Page<MfgCheckInspectionReport> list(Page<MfgCheckInspectionReport> page,JSONObject params,InspectionPointTypeEnum typeEnum){
		StringBuilder hql = new StringBuilder("select distinct m.mfgCheckInspectionReport from MfgPatrolItem m where m.companyId = ? and m.conclusion = ? and m.mfgCheckInspectionReport.inspectionPointType = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add("NG");
		searchParams.add(typeEnum);
		//绑定查询条件
		return mfgCheckInspectionReportDao.searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<MfgCheckInspectionReport> unlist(Page<MfgCheckInspectionReport>page){
		return mfgCheckInspectionReportDao.unlist(page);
	}
	
	public List<MfgCheckInspectionReport> listAll(){
		return mfgCheckInspectionReportDao.getAllIncomingInspectionActionsReport();
	}
	public List<MfgCheckInspectionReport> listAllSuplier(){
		return mfgCheckInspectionReportDao.getAllSupplier();
	}
	public List<MfgCheckInspectionReport> listAllMaterial(){
		return mfgCheckInspectionReportDao.getAllMaterial();
	}
	public Page<MfgCheckInspectionReport> unsearch(Page<MfgCheckInspectionReport> page){
		return mfgCheckInspectionReportDao.unsearch(page);
	}
	
	public List<MfgCheckInspectionReport> listAll(Date startDate,Date endDate,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		return mfgCheckInspectionReportDao.getAllIncomingInspectionActionsReport(startDate,endDate,materiel,dutySupplier,checkBomMaterialType,importance);
	}
	public Page<MfgCheckInspectionReport> listAll(Page<MfgCheckInspectionReport> page,Date startDate,Date endDate,String materiel,String dutySupplier){
		return mfgCheckInspectionReportDao.getAllIncomingInspectionActionsReport(page,startDate,endDate,materiel,dutySupplier,"","");
	}
	
	public List<MfgCheckInspectionReport> listAll(Date startDate,Date endDate){
		return mfgCheckInspectionReportDao.getAllIncomingInspectionActionsReport(startDate,endDate);
	}
	//用来查找一段时间内供应商物料连续几批不合格
	public List<MfgCheckInspectionReport> listAllByOrder(Date startDate,Date endDate,String supplierName,String checkBomName){
		return mfgCheckInspectionReportDao.getAllIncomingInspectionActionsReportByOrder(startDate,endDate,supplierName,checkBomName);
	}
	//查找一段时间内供应商物料同一检验项目的不合格报告
	public List<MfgCheckInspectionReport> listAllByItems(Date startDate,Date endDate,String supplierName,String checkBomName,String checkItemName){
		return mfgCheckInspectionReportDao.getAllIncomingInspectionActionsReportByItems(startDate,endDate,supplierName,checkBomName,checkItemName);
	}
	public Page<MfgCheckInspectionReport> listQualified(Page<MfgCheckInspectionReport>page,Date startDate,Date endDate,String state,String materiel,String dutySupplier){
		return mfgCheckInspectionReportDao.getQualifiedIncomingInspectionActionsReport(page,startDate,endDate,state,materiel,dutySupplier,"","");
	}
	public List<MfgCheckInspectionReport> listQualified(Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		return mfgCheckInspectionReportDao.getQualifiedIncomingInspectionActionsReport(startDate,endDate,state,materiel,dutySupplier,checkBomMaterialType,importance);
	}
	public List<MfgCheckInspectionReport> listQualifiedByProcessingResult(Date startDate,Date endDate,String state,String processingResult){
		return mfgCheckInspectionReportDao.getQualifiedIncomingInspectionActionsReportByResult(startDate,endDate,state,processingResult);
	}
	public List<MfgCheckInspectionReport> listQualifiedByInspector(Date startDate,Date endDate,String state,String inspector){
		return mfgCheckInspectionReportDao.getQualifiedIncomingInspectionActionsReportByInspector(startDate,endDate,state,inspector);
	}
	public List<MfgCheckInspectionReport> listQualified(Date startDate,Date endDate,String state){
		return mfgCheckInspectionReportDao.getQualifiedIncomingInspectionActionsReport(startDate,endDate,state);
	}
	public List<MfgCheckInspectionReport> listByInspectionNo(String inspectionNo){
		return mfgCheckInspectionReportDao.getIncomingInspectionActionsReportByInspectionNo(inspectionNo);
	}
	
	public void saveItemOrder(Integer originalIndex, Integer newIndex) {
		mfgCheckInspectionReportDao.updateIndex(originalIndex, Integer.MAX_VALUE);
        if (originalIndex < newIndex) {// 从上往下移动 两者之间的displayIndex要自减
        	mfgCheckInspectionReportDao.decreaseIndex(originalIndex,newIndex);
        } else {// 从下往上移动 两者之间的displayIndex要自增
        	mfgCheckInspectionReportDao.increaseIndex(newIndex,originalIndex);
        }
        mfgCheckInspectionReportDao.updateIndex(Integer.MAX_VALUE, newIndex);
    }

	public Page<MfgCheckInspectionReport> queryIinspectionReportDetail(Page<MfgCheckInspectionReport> page, JSONObject params) {
		return mfgCheckInspectionReportDao.queryIinspectionReportDetail(page, params);
	}
	
	public Page<MfgCheckInspectionReport> search(Page<MfgCheckInspectionReport> page) {
		return mfgCheckInspectionReportDao.search(page);
	}

	//封装不良细项结果数据集的JSON格式
	public String getResultJson(Page<MfgCheckInspectionReport> page,String countNamePrex) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(MfgCheckInspectionReport cp : page.getResult()){
			JSONObject json = JSONObject.fromObject(JsonParser.object2Json(cp));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(cp.getInspectionDate()!=null){
				String inspectionDate=sdf.format(cp.getInspectionDate());
				json.put("inspectionDate", inspectionDate);
			}
			list.add(json);
		}
		// 添加jqGrid所需的页信息
		StringBuilder json = new StringBuilder();
		json.append("{\"page\":\"");
		json.append(page.getPageNo());
		json.append("\",\"total\":");
		json.append(page.getTotalPages());
		json.append(",\"records\":\"");
		json.append(page.getTotalCount());
		json.append("\",\"rows\":");
		json.append(JSONArray.fromObject(list).toString());
		json.append("}");
		return json.toString();
}

	// 封装保存后的信息(处理保存后日期为空)
	public String getOneResultJson(MfgCheckInspectionReport incomingInspectionActionsReport) {
		HashMap<String, Object> hs = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (incomingInspectionActionsReport.getInspectionDate() != null) {
			String inspectionDate = sdf.format(incomingInspectionActionsReport.getInspectionDate());
			hs.put("inspectionDate", inspectionDate);
		}
		StringBuffer sb = new StringBuffer();
		sb.append(JsonParser.object2Json(incomingInspectionActionsReport));
		sb.delete(sb.length() - 1, sb.length());
		sb.append(",");
		sb.append(JsonParser.object2Json(hs).substring(1, JsonParser.object2Json(hs).length()));
		return sb.toString();
	}

	/**
	 * 导入产品
	 * 
	 * @param file
	 * @throws Exception
	 */
	public String importFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("检验报告编号", "inspectionNo");
		fieldMap.put("批次号", "batchNo");
		fieldMap.put("检验日期", "inspectionDate");
		fieldMap.put("到货日期", "enterDate");
		fieldMap.put("检验员", "inspector");
		fieldMap.put("物料名称", "materialName");
		fieldMap.put("来料数", "stockAmount");
		fieldMap.put("供方", "dutySupplier");
		fieldMap.put("检验数", "inspectionAmount");
		fieldMap.put("合格数", "qualifiedAmount");
		fieldMap.put("不良数", "unqualifiedAmount");
		fieldMap.put("检验判定", "inspectionConclusion");
		fieldMap.put("不良类别", "unqualifiedType");
		fieldMap.put("合格率", "qualifiedRate");
		fieldMap.put("不良等级", "unqualifiedGrade");
		fieldMap.put("发生次数", "occurrenceNum");
		fieldMap.put("处理结果", "processingResult");
		fieldMap.put("完成日期", "measuresDate");
		fieldMap.put("备注", "remark");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if (row == null) {
			throw new RuntimeException("第一行不能为空!");
		}

		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0;; i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap.containsKey(value)) {
				columnMap.put(value, i);
			}
		}
		if (columnMap.keySet().size() != fieldMap.keySet().size()) {
			throw new RuntimeException("模板格式不正确!");
		}
		Iterator<Row> rows = sheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();// 标题行
		int i = 1;
		while (rows.hasNext()) {
			row = rows.next();
			MfgCheckInspectionReport incomingInspectionActionsReport = new MfgCheckInspectionReport();
			incomingInspectionActionsReport.setCreatedTime(new Date());
			incomingInspectionActionsReport.setCompanyId(ContextUtils.getCompanyId());
			incomingInspectionActionsReport.setCreator(ContextUtils.getUserName());
			incomingInspectionActionsReport.setLastModifiedTime(new Date());
			incomingInspectionActionsReport.setLastModifier(ContextUtils.getUserName());
			for (String columnName : columnMap.keySet()) {
				Cell cell = row.getCell(columnMap.get(columnName));
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						setProperty(incomingInspectionActionsReport,fieldMap.get(columnName),cell.getStringCellValue());
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(incomingInspectionActionsReport,fieldMap.get(columnName), date);
						} else {
							setProperty(incomingInspectionActionsReport,fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(incomingInspectionActionsReport,fieldMap.get(columnName), cell.getCellFormula());
					}
				}
			}
			try {
				saveIncomingInspectionActionsReport(incomingInspectionActionsReport);
				sb.append("第" + (i + 1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("第" + (i + 1) + "行保存失败:<font color=red>"
						+ e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}

	private void setProperty(Object obj, String property, Object value) throws Exception {
		String fieldName = property,customType = null;
		if(property.indexOf("_")>0){
			String[] strs = property.split("_");
			fieldName = strs[0];
			customType = strs[1];
		}
		Class<?> type = PropertyUtils.getPropertyType(obj, fieldName);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, fieldName, null);
			} else {
				if("timestamp".equals(customType)){
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value.toString()));
				}else if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Boolean.valueOf(value.toString()));
				} else if (Date.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value+""));
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	
	public void submitProcess(MfgCheckInspectionReport incomingInspectionActionsReport) {
		saveIncomingInspectionActionsReport(incomingInspectionActionsReport);
		Long processId=ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode("sample-appraisal-report").get(0).getId();
		ApiFactory.getInstanceService().submitInstance(processId,
				incomingInspectionActionsReport);
	}

	public void completeTask(MfgCheckInspectionReport incomingInspectionActionsReport, Long taskId,
			TaskProcessingResult taskTransact) {
		saveIncomingInspectionActionsReport(incomingInspectionActionsReport);
		ApiFactory.getTaskService().completeWorkflowTask(taskId,
				taskTransact);
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
			.getWorkflowDefinitionsByCode("expense-report").get(0)
			.getId();
			return ApiFactory.getFormService()
			.getFieldPermissionNotStarted(
					processId);
		} else {
			com.norteksoft.product.api.entity.WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
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
	public MfgCheckInspectionReport getIncomingInspectionActionsReportByTaskId(Long taskId) {
		return getMfgCheckInspectionReport(ApiFactory.getFormService()
				.getFormFlowableIdByTask(taskId));
	}

	/**
	 * 获得loginName用户的该实例的当前任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getMyTask(MfgCheckInspectionReport incomingInspectionActionsReport, String loginName) {
		return ApiFactory.getTaskService().getActiveTaskByLoginName(incomingInspectionActionsReport,
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
	public void deleteFormFlowable(Long id) {
		mfgCheckInspectionReportDao.delete(id);
	}
	//发起预警
	public void launchWarning(MfgCheckInspectionReport incomingInspectionActionsReport){
//		List<AlarmSetting> iqcAlarmSetting=alarmSettingManager.getAlarmSettingBytype("进货检验");
//		AlarmSetting alarmSetting=iqcAlarmSetting.size()>0?iqcAlarmSetting.get(0):null;
//		if(alarmSetting.getState() != null && AlarmSetting.USE_STATE.equals(alarmSetting.getState())){
//			int hour=alarmSetting==null?1:alarmSetting.getHour();
//			int number=alarmSetting==null?1:alarmSetting.getNumber();
//			//获取天数
//			Date endDate=incomingInspectionActionsReport.getInspectionDate();
//			Calendar startCal = Calendar.getInstance();
//			startCal.setTime(endDate);
//			startCal.set(Calendar.DAY_OF_YEAR, startCal.get(Calendar.DAY_OF_YEAR) - hour);
//			Date beginDate=startCal.getTime();
//			
//			List<MfgCheckItem> itemList = new ArrayList<MfgCheckItem>();
//			if(!incomingInspectionActionsReport.getCheckItems().isEmpty()){
//				for(MfgCheckItem item:incomingInspectionActionsReport.getCheckItems()){
//					if(item.getConclusion().equals("NG")){
//						itemList.add(item);
//					}
//				}
//			}
//			
//			if(!itemList.isEmpty()){
//				String reportsHql = "select c.incomingInspectionActionsReport from CheckItem c where c.supplierId = ? and c.checkBomCode = ? and c.checkItemName = ? and c.inspectionDate between ? and ? and c.inspectionDate > ?";
//				Query reportsQuery = incomingInspectionActionsReportDao.createQuery(reportsHql);
//				reportsQuery.setParameter(0,incomingInspectionActionsReport.getSupplierId());
//				reportsQuery.setParameter(1,incomingInspectionActionsReport.getCheckBomCode());
//				reportsQuery.setMaxResults(number);
//				
//				String hql = "select u from com.ambition.improve.entity.UnqualityItem u where u.companyId = ? and u.unqualityItemName = ? and u.correctionPrecaution.supplierCode = ? and u.correctionPrecaution.materielCode = ? order by u.createdTime desc";
//				Query query = incomingInspectionActionsReportDao.createQuery(hql);
//				query.setParameter(0,ContextUtils.getCompanyId());
//				query.setParameter(2,incomingInspectionActionsReport.getSupplierCode());
//				query.setParameter(3,incomingInspectionActionsReport.getCheckBomCode());
//				query.setMaxResults(1);
//
//				StringBuffer ids = new StringBuffer();
//				
//				for(CheckItem item:itemList){
//					query.setParameter(1,item.getCheckItemName());
//					List<com.ambition.improve.entity.UnqualityItem> unqualityItems = query.list();
//					//lastTime
//					Calendar checkCalendar = Calendar.getInstance();
//					if(!unqualityItems.isEmpty()){
//						checkCalendar.setTime(unqualityItems.get(0).getCreatedTime());
//					}else{
//						checkCalendar.set(Calendar.YEAR,1900);
//					}
//					if(endDate.getTime()>checkCalendar.getTimeInMillis()){
//						reportsQuery.setParameter(2,item.getCheckItemName());
//						reportsQuery.setParameter(3,beginDate);
//						reportsQuery.setParameter(4,endDate);
//						reportsQuery.setParameter(5,checkCalendar.getTime());
//						List<IncomingInspectionActionsReport> reportsList = reportsQuery.list();
//						if(reportsList.size()>=number){
//							CorrectionPrecaution correctionPrecaution = new CorrectionPrecaution();
//							correctionPrecaution.setCompanyId(ContextUtils.getCompanyId());
//							correctionPrecaution.setCreatedTime(new Date());
//							correctionPrecaution.setCreator(ContextUtils.getUserName());
//							correctionPrecaution.setLastModifiedTime(new Date());
//							correctionPrecaution.setLastModifier(ContextUtils.getUserName());
//							correctionPrecaution.setInfoSource("进货检验");
//							correctionPrecaution.setReportTime(new Date());
//							correctionPrecaution.setSourceNo(incomingInspectionActionsReport.getInspectionNo());
//							correctionPrecaution.setMaterielName(incomingInspectionActionsReport.getCheckBomName());
//							correctionPrecaution.setMaterielCode(incomingInspectionActionsReport.getCheckBomCode());
//							correctionPrecaution.setSupplierName(incomingInspectionActionsReport.getSupplierName());
//							correctionPrecaution.setSupplierCode(supplierManager.getSupplier(incomingInspectionActionsReport.getSupplierId()).getCode());
//							correctionPrecaution.setSupplierId(incomingInspectionActionsReport.getSupplierId());
//							correctionPrecaution.setNoticeState("0");//发起预警
//							correctionPrecaution.setUnqualityPhenomenon("*供应商："+incomingInspectionActionsReport.getSupplierName()+"，物料："+incomingInspectionActionsReport.getCheckBomName()+"，检验项目："+item.getCheckItemName()+"，连续"+hour+" 天发生"+number+"批不合格。");
//
//							for(IncomingInspectionActionsReport report : reportsList){
//								ids.append(report.getId()+",");
//							}
//							correctionPrecaution.setIdString(ids.toString());
//							
//							UnqualityItem unqualityItem = new UnqualityItem();
//							unqualityItem.setCompanyId(ContextUtils.getCompanyId());
//							unqualityItem.setCreatedTime(new Date());
//							unqualityItem.setCreator(ContextUtils.getUserName());
//							unqualityItem.setLastModifiedTime(new Date());
//							unqualityItem.setLastModifier(ContextUtils.getUserName());
//							unqualityItem.setUnqualityItemName(item.getCheckItemName());
//							unqualityItem.setUnqualityAmount(item.getUnqualifiedAmount());
//							unqualityItem.setCorrectionPrecaution(correctionPrecaution);
//							List<UnqualityItem> unqualityLists = new ArrayList<UnqualityItem>();
//							unqualityLists.add(unqualityItem);
//							correctionPrecaution.setUnqualityItems(unqualityLists);
//							correctionPrecautionManager.iqcsaveCorrectionPrecation(correctionPrecaution);
//						}
//					}
//				}
//			}
//		}
	}
	//发起改进
	public void launchImprove(MfgCheckInspectionReport incomingInspectionActionsReport){
//		if(correctionPrecautionManager.listAllBySupplierName(incomingInspectionActionsReport.getSupplierName()).size()==0){
//		List<AlarmSetting> iqcAlarmSetting=alarmSettingManager.getAlarmSettingBytype("进货检验");
//		AlarmSetting alarmSetting=new AlarmSetting();
//		if(iqcAlarmSetting.size()>0){
//			alarmSetting=iqcAlarmSetting.get(0);
//		}
//		int hour=alarmSetting.getHour();
//		int number=alarmSetting.getNumber();
//		Date endDate=incomingInspectionActionsReport.getInspectionDate();
//		//获取天数
//		Calendar startCal = Calendar.getInstance();
//		startCal.setTime(endDate);
//		startCal.set(Calendar.DAY_OF_YEAR, startCal.get(Calendar.DAY_OF_YEAR) - hour);
//		Date beginDate=startCal.getTime();
//		List<IncomingInspectionActionsReport> incomingInspectionActionsReportList=this.listAllByOrder(beginDate,endDate,incomingInspectionActionsReport.getSupplierName(),incomingInspectionActionsReport.getCheckBomName());
//		if(incomingInspectionActionsReportList.size()>=number){
//			CorrectionPrecaution correctionPrecaution=new CorrectionPrecaution();
//			correctionPrecaution.setCreatedTime(new Date());
//			correctionPrecaution.setCompanyId(ContextUtils.getCompanyId());
//			correctionPrecaution.setCreator(ContextUtils.getUserName());
//			correctionPrecaution.setLastModifiedTime(new Date());
//			correctionPrecaution.setLastModifier(ContextUtils.getUserName());
//			correctionPrecaution.setInfoSource("进货检验");
//			correctionPrecaution.setSourceNo(incomingInspectionActionsReport.getInspectionNo());
//			correctionPrecaution.setProductName(incomingInspectionActionsReport.getCheckBomName());
//			correctionPrecaution.setSupplierName(incomingInspectionActionsReport.getSupplierName());
//			correctionPrecaution.setSupplierCode(supplierManager.getSupplier(incomingInspectionActionsReport.getSupplierId()).getCode());
//			correctionPrecaution.setNoticeState("0");//发起预警
//			correctionPrecautionManager.iqcsaveCorrectionPrecation(correctionPrecaution);
//		}else{
//			System.out.println("不发起改进");
//		}
//		}
	}
	public String insertSpcByResults(QualityFeature feature,Date inspectionDate,String hisSampleIds,String results,InspectionPointTypeEnum inspectionPointType, String workGroupType, MfgCheckInspectionReport mfgCheckInspectionReport,String equipmentNo) throws NumberFormatException, Exception{
		List<SpcSgSample> samples = new ArrayList<SpcSgSample>();
		if(StringUtils.isNotEmpty(hisSampleIds)){
			String[] sampleIds = hisSampleIds.split(",");
			for(String id : sampleIds){
				if(StringUtils.isNotEmpty(id)){
					List<SpcSgSample> list = spcSgSampleDao.find("from SpcSgSample s where s.id = ?",Long.valueOf(id));
					if(!list.isEmpty()){
						SpcSgSample sample = list.get(0);
						//如果历史的特性与现在的特性不一致,直接跳过
						if(!sample.getSpcSubGroup().getQualityFeature().getId().equals(feature.getId())){
							break;
						}
						samples.add(sample);
					}
				}
			}
		}
		String[] vals = results.split(",");
		int count=0;
		String spcSampleIds = "";
		for(String val : vals){
			//判断是否数字
			if(CommonUtil1.isNumber(val)){
				SpcSgSample sample = null;
				if(count<samples.size()){
					sample = samples.get(count);
					if(val.indexOf(".")==-1){
						val += ".0";
					}
					if(!val.equals(sample.getSamValue())){
						sample.setSamValue(Double.valueOf(val));
						spcSgSampleDao.save(sample);
						//更新值
						updateSpcSgGroup(sample.getSpcSubGroup(),ContextUtils.getCompanyId(),ContextUtils.getUserName(),mfgCheckInspectionReport);
					}
				}else{
					sample = insertSpc(feature,inspectionDate,Double.valueOf(val),ContextUtils.getCompanyId(),ContextUtils.getUserName(),inspectionPointType,workGroupType,mfgCheckInspectionReport,equipmentNo);
				}
				spcSampleIds += sample.getId() + ",";
				count++;
			}
		}
		if(count<samples.size()){
			for(;count<samples.size();count++){
				spcSubGroupDao.delete(samples.get(count).getSpcSubGroup());
			}
		}
		return spcSampleIds;
	}
	public String insertSpcByResultsNew(QualityFeature feature,Date inspectionDate,String hisSampleIds,String results,InspectionPointTypeEnum inspectionPointType, String workGroupType, MfgCheckInspectionReport mfgCheckInspectionReport,String equipmentNo,Session session) throws NumberFormatException, Exception{
		List<SpcSgSample> samples = new ArrayList<SpcSgSample>();
		if(StringUtils.isNotEmpty(hisSampleIds)){
			String[] sampleIds = hisSampleIds.split(",");
			for(String id : sampleIds){
				if(StringUtils.isNotEmpty(id)){
					Query query = session.createQuery("from SpcSgSample s where s.id = ?").setParameter(0, Long.valueOf(id));
					@SuppressWarnings("unchecked")
					List<SpcSgSample> list = query.list();
					if(!list.isEmpty()){
						SpcSgSample sample = list.get(0);
						//如果历史的特性与现在的特性不一致,直接跳过
						if(!sample.getSpcSubGroup().getQualityFeature().getId().equals(feature.getId())){
							break;
						}
						samples.add(sample);
					}
				}
			}
		}
		String[] vals = results.split(",");
		int count=0;
		String spcSampleIds = "";
		for(String val : vals){
			//判断是否数字
			if(CommonUtil1.isNumber(val)){
				SpcSgSample sample = null;
				if(count<samples.size()){
					sample = samples.get(count);
					if(val.indexOf(".")==-1){
						val += ".0";
					}
					if(!val.equals(sample.getSamValue())){
						sample.setSamValue(Double.valueOf(val));
						session.save(sample);
						//更新值
						updateSpcSgGroupNew(sample.getSpcSubGroup(),ContextUtils.getCompanyId(),ContextUtils.getUserName(),mfgCheckInspectionReport,session);
					}
				}else{
					sample = insertSpcNew(feature,inspectionDate,Double.valueOf(val),ContextUtils.getCompanyId(),ContextUtils.getUserName(),inspectionPointType,workGroupType,mfgCheckInspectionReport,equipmentNo,session);
				}
				spcSampleIds += sample.getId() + ",";
				count++;
			}
		}
		if(count<samples.size()){
			for(;count<samples.size();count++){
				session.delete(samples.get(count).getSpcSubGroup());
			}
		}
		return spcSampleIds;
	}
	private Integer getMaxGroupOrderNum(QualityFeature feature,Long companyId){
		List<Object> objs = qualityFeatureDao.find("select max(s.subGroupOrderNum) from SpcSubGroup s where s.qualityFeature = ? and s.companyId = ?",feature,companyId);
		if(objs.get(0)==null){
			return 1;
		}else{
			return Integer.valueOf(objs.get(0).toString())+1;
		}
	}
	
	public void updateSpcSgGroup(SpcSubGroup spcSubGroup,Long companyid,String userName, MfgCheckInspectionReport mfgCheckInspectionReport){
		spcSubGroup.setLastModifiedTime(new Date());
		spcSubGroup.setLastModifier(userName);
		String hql = "select max(s.samValue),min(s.samValue),sum(s.samValue),count(id) from SpcSgSample s where s.spcSubGroup = ? and s.companyId = ?";
		Object[] objs = (Object[])spcSubGroupDao.find(hql,spcSubGroup,companyid).get(0);
		Double max = null,min=null,sum=0.0;
		if(objs[0] != null){
			max = Double.valueOf(objs[0].toString());
		}
		if(objs[1] != null){
			min = Double.valueOf(objs[1].toString());
		}
		if(objs[2] != null){
			sum  += Double.valueOf(objs[2].toString());
		}
		if(max==null){
			max = 0.0;
		}
		if(min==null){
			min=0.0;
		}
		int sampleSize = spcSubGroup.getActualSmapleNum();
		if(objs[3] != null){
			sampleSize = Integer.valueOf(objs[3].toString());
		}
		Double sigma = sum/sampleSize;
		Double randDiff = max - min;
		spcSubGroup.setMaxValue(max);
		spcSubGroup.setMinValue(min);
		spcSubGroup.setSigma(sigma);
		spcSubGroup.setCreator(mfgCheckInspectionReport.getInspector());
		spcSubGroup.setRangeDiff(randDiff);
		spcSubGroupDao.save(spcSubGroup);
	}
	public void updateSpcSgGroupNew(SpcSubGroup spcSubGroup,Long companyid,String userName, MfgCheckInspectionReport mfgCheckInspectionReport,Session session){
		spcSubGroup.setLastModifiedTime(new Date());
		spcSubGroup.setLastModifier(userName);
		String hql = "select max(s.sam_Value),min(s.sam_Value),sum(s.sam_Value),count(id) from SPC_SG_SAMPLE s where s.FK_SUB_GROUP_ID = ? and s.company_Id = ?";
		Query query = session.createSQLQuery(hql).setParameter(0, spcSubGroup.getId()).setParameter(1, companyid);
		Object[] objs = (Object[])query.list().get(0);
		Double max = null,min=null,sum=0.0;
		if(objs[0] != null){
			max = Double.valueOf(objs[0].toString());
		}
		if(objs[1] != null){
			min = Double.valueOf(objs[1].toString());
		}
		if(objs[2] != null){
			sum  += Double.valueOf(objs[2].toString());
		}
		if(max==null){
			max = 0.0;
		}
		if(min==null){
			min=0.0;
		}
		int sampleSize = spcSubGroup.getActualSmapleNum();
		if(objs[3] != null){
			sampleSize = Integer.valueOf(objs[3].toString());
		}
		Double sigma = sum/sampleSize;
		Double randDiff = max - min;
		spcSubGroup.setMaxValue(max);
		spcSubGroup.setMinValue(min);
		spcSubGroup.setSigma(sigma);
		spcSubGroup.setCreator(mfgCheckInspectionReport.getInspector());
		spcSubGroup.setRangeDiff(randDiff);
		session.save(spcSubGroup);
	}
	 
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	
	/**
	  * 方法名:添加数据时插入监控 
	  * @param feature
	  * @param detectDate
	  * @param paramValue
	  * @param companyid
	  * @param userName
	 * @param workGroupType 
	 * @param mfgCheckInspectionReport 
	  * @return
	 * @throws Exception 
	 */
	public SpcSgSample insertSpc(QualityFeature feature,Date detectDate,Double paramValue,Long companyid,String userName,InspectionPointTypeEnum inspectionPointType, String workGroupType, MfgCheckInspectionReport mfgCheckInspectionReport,String equipmentNo) throws Exception{
		String inspectionType = "";
		if(InspectionPointTypeEnum.FIRSTINSPECTION.equals(inspectionPointType)){
			inspectionType = "首检";
		}else if(InspectionPointTypeEnum.PATROLINSPECTION.equals(inspectionPointType)){
			inspectionType = "巡检";
		}else if(InspectionPointTypeEnum.COMPLETEINSPECTION.equals(inspectionPointType)){
			inspectionType = "末检";
		}
		SpcSubGroup spcSubGroup = spcSubGroupManager.findLastSubGroup(feature.getId(),feature.getSampleCapacity(),companyid,userName);
		int sampleSize = 0;
		ProcessPoint point = feature.getProcessPoint();
		String pointParentName = "";
		if(point!=null){
			if(point.getParent()!=null){
				if( point.getParent().getName().contains("CCM")){
					pointParentName=point.getName();
				}else{
					pointParentName = point.getParent().getName();
				}
			}else{
				pointParentName = point.getName();
			}
		}
		if(spcSubGroup == null){
		    spcSubGroup = new SpcSubGroup();
			spcSubGroup.setProsessPointName(pointParentName);
			spcSubGroup.setCompanyId(companyid);
			spcSubGroup.setCreatedTime(detectDate);
			spcSubGroup.setCreator(mfgCheckInspectionReport.getInspector());
			spcSubGroup.setLastModifiedTime(new Date());
			spcSubGroup.setLastModifier(userName);
			spcSubGroup.setActualSmapleNum(1);
			spcSubGroup.setQualityFeature(feature);
			spcSubGroup.setSubGroupOrderNum(getMaxGroupOrderNum(feature,companyid));
			spcSubGroup.setSubGroupSize(feature.getSampleCapacity());
			spcSubGroup.setRangeDiff(0.0);
			spcSubGroup.setSigma(paramValue);
			spcSubGroup.setMaxValue(paramValue);
			spcSubGroup.setMinValue(paramValue);
			spcSubGroup.setInspectionType(inspectionType);
			if("D".equals(workGroupType)||"N".equals(workGroupType)){
				String setColumn = null;
				String inpectionTypeSetColumn = null;
				String inspector = null;
				String machine = null;
				String hql = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlInspectionType = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlInspector = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlMachine = " from LayerType t where t.typeName=? and t.companyId=?";
				List<LayerType> types = layerTypeDao.find(hql,"班组",ContextUtils.getCompanyId());
				List<LayerType> inspectionTypesLayer = layerTypeDao.find(hqlInspectionType,"检验",ContextUtils.getCompanyId());
				List<LayerType> inspecorTypesLayer = layerTypeDao.find(hqlInspector,"检验员",ContextUtils.getCompanyId());
				List<LayerType> machineTypesLayer = layerTypeDao.find(hqlMachine,"机台",ContextUtils.getCompanyId());
                if(types.size()>0){
                	setColumn = types.get(0).getTypeCode();
                	inpectionTypeSetColumn = inspectionTypesLayer.get(0).getTypeCode();
                	inspector = inspecorTypesLayer.get(0).getTypeCode();
                	machine = machineTypesLayer.get(0).getTypeCode();
                	if(setColumn!=null){
                    	SpcSgTag spcSgTag = new SpcSgTag();
        				spcSgTag.setCreatedTime(spcSubGroup.getCreatedTime());
        				spcSgTag.setCompanyId(ContextUtils.getCompanyId());
        				spcSgTag.setCreator(mfgCheckInspectionReport.getInspector());
        				spcSgTag.setModifiedTime(new Date());
        				spcSgTag.setModifier(ContextUtils.getUserName());
        				spcSgTag.setTagName(types.get(0).getTypeName());
        				spcSgTag.setTagCode(setColumn);
        				if("D".equals(workGroupType)){
        					spcSgTag.setTagValue("SHIFT_D");
        					setProperty(spcSubGroup, setColumn, "SHIFT_D");
        				}else if("N".equals(workGroupType)){
        					spcSgTag.setTagValue("SHIFT_N");
        					setProperty(spcSubGroup, setColumn, "SHIFT_N");
        				}
        				spcSgTag.setSpcSubGroup(spcSubGroup);
        				
        				SpcSgTag spcSgTagInspectionType = new SpcSgTag();
        				spcSgTagInspectionType.setCreatedTime(spcSubGroup.getCreatedTime());
        				spcSgTagInspectionType.setCompanyId(ContextUtils.getCompanyId());
        				spcSgTagInspectionType.setCreator(mfgCheckInspectionReport.getInspector());
        				spcSgTagInspectionType.setModifiedTime(new Date());
        				spcSgTagInspectionType.setModifier(ContextUtils.getUserName());
        				spcSgTagInspectionType.setTagName(inspectionTypesLayer.get(0).getTypeName());
        				spcSgTagInspectionType.setTagCode(inpectionTypeSetColumn);
        				spcSgTagInspectionType.setTagValue(inspectionType);
    					setProperty(spcSubGroup, inpectionTypeSetColumn, inspectionType);
        				spcSgTagInspectionType.setSpcSubGroup(spcSubGroup);
        				
        				SpcSgTag spcSgTagInspector = new SpcSgTag();
        				spcSgTagInspector.setCreatedTime(spcSubGroup.getCreatedTime());
        				spcSgTagInspector.setCompanyId(ContextUtils.getCompanyId());
        				spcSgTagInspector.setCreator(mfgCheckInspectionReport.getInspector());
        				spcSgTagInspector.setModifiedTime(new Date());
        				spcSgTagInspector.setModifier(ContextUtils.getUserName());
        				spcSgTagInspector.setTagName(inspecorTypesLayer.get(0).getTypeName());
        				spcSgTagInspector.setTagCode(inspector);
        				spcSgTagInspector.setTagValue(mfgCheckInspectionReport.getInspector());
    					setProperty(spcSubGroup, inspector, mfgCheckInspectionReport.getInspector());
    					spcSgTagInspector.setSpcSubGroup(spcSubGroup);
    					
    					SpcSgTag spcSgTagMachine = new SpcSgTag();
    					spcSgTagMachine.setCreatedTime(spcSubGroup.getCreatedTime());
    					spcSgTagMachine.setCompanyId(ContextUtils.getCompanyId());
    					spcSgTagMachine.setCreator(mfgCheckInspectionReport.getInspector());
    					spcSgTagMachine.setModifiedTime(new Date());
        				spcSgTagInspector.setModifier(ContextUtils.getUserName());
        				spcSgTagMachine.setTagName(machineTypesLayer.get(0).getTypeName());
        				spcSgTagMachine.setTagCode(machine);
        				spcSgTagMachine.setTagValue(equipmentNo);
    					setProperty(spcSubGroup, machine, equipmentNo);
    					spcSgTagMachine.setSpcSubGroup(spcSubGroup);
    					
        				spcSubGroupDao.getSession().save(spcSgTag);
        				spcSubGroupDao.getSession().save(spcSgTagInspectionType);
        				spcSubGroupDao.getSession().save(spcSgTagInspector);
        				spcSubGroupDao.getSession().save(spcSgTagMachine);
    				}
				}
			}
			
		}else{
			spcSubGroup.setLastModifiedTime(new Date());
			spcSubGroup.setLastModifier(userName);
			String hql = "select max(s.samValue),min(s.samValue),sum(s.samValue),count(id) from SpcSgSample s where s.spcSubGroup = ? and s.companyId = ?";
			Object[] objs = (Object[])spcSubGroupDao.find(hql,spcSubGroup,companyid).get(0);
			Double max = paramValue,min=paramValue,sum=paramValue;
			if(objs[0] != null){
				Double maxVal = Double.valueOf(objs[0].toString());
				if(maxVal>max){
					max = maxVal;
				}
			}
			if(objs[1] != null){
				Double minVal = Double.valueOf(objs[1].toString());
				if(minVal < min){
					min = minVal;
				}
			}
			if(objs[2] != null){
				sum  += Double.valueOf(objs[2].toString());
			}
			if(objs[3] != null){
				sampleSize = Integer.valueOf(objs[3].toString());
			}
			Double sigma = sum/(sampleSize+1);
			Double randDiff = max - min;
			spcSubGroup.setMaxValue(max);
			spcSubGroup.setMinValue(min);
			spcSubGroup.setSigma(sigma);
			spcSubGroup.setRangeDiff(randDiff);
			spcSubGroup.setActualSmapleNum(sampleSize+1);
		}
		spcSubGroupDao.save(spcSubGroup);
		
		SpcSgSample spcSgSample = new SpcSgSample();
		spcSgSample.setCreatedTime(detectDate);
		spcSgSample.setCompanyId(companyid);
		spcSgSample.setCreator(mfgCheckInspectionReport.getInspector());
		spcSgSample.setLastModifiedTime(new Date());
		spcSgSample.setLastModifier(mfgCheckInspectionReport.getInspector());
		spcSgSample.setSampleNo("X" + (sampleSize + 1));
		spcSgSample.setSampleOrderNum(sampleSize + 1+"");
		spcSgSample.setSamValue(paramValue);
		spcSgSample.setSpcSubGroup(spcSubGroup);
		spcSgSampleDao.save(spcSgSample);
		//如果采集的数据够一组时,检查监控条件
		if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())&&equipmentNo!=null){
			if(spcSubGroup.getSubGroupSize() != null && spcSubGroup.getSubGroupSize().equals(sampleSize+1)){
				//根据条件查询采集的数据
				String hql = "select s.spcSubGroup from SpcSgTag s where s.companyId = ?  and s.spcSubGroup.qualityFeature = ? and s.tagValue=?";
				List<Object> searchParams = new ArrayList<Object>();
				searchParams.add(spcSubGroup.getCompanyId());
				searchParams.add(feature);
				searchParams.add(equipmentNo);
				//10天以前的数据
				hql = hql + " and s.spcSubGroup.createdTime between ? and ?";
				Calendar calendar = Calendar.getInstance();
				searchParams.add(calendar.getTime());
				calendar.add(Calendar.DATE, -10);
				searchParams.add(searchParams.size()-1, calendar.getTime());
				List<SpcSubGroup> list = spcSubGroupDao.find(hql,searchParams.toArray());
				BaseUtil.setSpcSgSampleDao(spcSgSampleDao);
				//根据规则检测所查询的数据  异常报警
				try{
					abnormalInfoManager.lanchAbnormalCCM(spcSubGroup.getSubGroupOrderNum()+"", feature, list,null,equipmentNo);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				BaseUtil.setSpcSgSampleDao(null);
				
				spcSubGroup.setJudgeState(1);
				spcSubGroupDao.save(spcSubGroup);
			}
		}else{
			if(spcSubGroup.getSubGroupSize() != null && spcSubGroup.getSubGroupSize().equals(sampleSize+1)){
				//根据条件查询采集的数据
				String hql = "from SpcSubGroup s where s.companyId = ?  and s.qualityFeature = ?";
				List<Object> searchParams = new ArrayList<Object>();
				searchParams.add(spcSubGroup.getCompanyId());
				searchParams.add(feature);
				//10天以前的数据
				hql = hql + " and s.createdTime between ? and ?";
				Calendar calendar = Calendar.getInstance();
				searchParams.add(calendar.getTime());
				calendar.add(Calendar.DATE, -10);
				searchParams.add(searchParams.size()-1, calendar.getTime());
				List<SpcSubGroup> list = spcSubGroupDao.find(hql,searchParams.toArray());
				BaseUtil.setSpcSgSampleDao(spcSgSampleDao);
				//根据规则检测所查询的数据  异常报警
				try{
					abnormalInfoManager.lanchAbnormal(spcSubGroup.getSubGroupOrderNum()+"", feature, list,null);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				BaseUtil.setSpcSgSampleDao(null);
				
				spcSubGroup.setJudgeState(1);
				spcSubGroupDao.save(spcSubGroup);
			}
		}
		
		return spcSgSample;
	} 
	/**
	  * 方法名:添加数据时插入监控 
	  * @param feature
	  * @param detectDate
	  * @param paramValue
	  * @param companyid
	  * @param userName
	 * @param workGroupType 
	 * @param mfgCheckInspectionReport 
	  * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public SpcSgSample insertSpcNew(QualityFeature feature,Date detectDate,Double paramValue,Long companyid,String userName,InspectionPointTypeEnum inspectionPointType, String workGroupType, MfgCheckInspectionReport mfgCheckInspectionReport,String equipmentNo,Session session) throws Exception{
		String inspectionType = "";
		if(InspectionPointTypeEnum.FIRSTINSPECTION.equals(inspectionPointType)){
			inspectionType = "首检";
		}else if(InspectionPointTypeEnum.PATROLINSPECTION.equals(inspectionPointType)){
			inspectionType = "巡检";
		}else if(InspectionPointTypeEnum.COMPLETEINSPECTION.equals(inspectionPointType)){
			inspectionType = "末检";
		}
		SpcSubGroup spcSubGroup = spcSubGroupManager.findLastSubGroupNew(feature.getId(),feature.getSampleCapacity(),companyid,userName,session);
		int sampleSize = 0;
		ProcessPoint point = feature.getProcessPoint();
		String pointParentName = "";
		if(point!=null){
			if(point.getParent()!=null){
				if( point.getParent().getName().contains("CCM")){
					pointParentName=point.getName();
				}else{
					pointParentName = point.getParent().getName();
				}
			}else{
				pointParentName = point.getName();
			}
		}
		if(spcSubGroup == null){
		    spcSubGroup = new SpcSubGroup();
			spcSubGroup.setProsessPointName(pointParentName);
			spcSubGroup.setCompanyId(companyid);
			spcSubGroup.setCreatedTime(detectDate);
			spcSubGroup.setCreator(mfgCheckInspectionReport.getInspector());
			spcSubGroup.setLastModifiedTime(new Date());
			spcSubGroup.setLastModifier(userName);
			spcSubGroup.setActualSmapleNum(1);
			spcSubGroup.setQualityFeature(feature);
			spcSubGroup.setSubGroupOrderNum(getMaxGroupOrderNum(feature,companyid));
			spcSubGroup.setSubGroupSize(feature.getSampleCapacity());
			spcSubGroup.setRangeDiff(0.0);
			spcSubGroup.setSigma(paramValue);
			spcSubGroup.setMaxValue(paramValue);
			spcSubGroup.setMinValue(paramValue);
			spcSubGroup.setInspectionType(inspectionType);
			if("D".equals(workGroupType)||"N".equals(workGroupType)){
				String setColumn = null;
				String inpectionTypeSetColumn = null;
				String inspector = null;
				String machine = null;
				String hql = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlInspectionType = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlInspector = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlMachine = " from LayerType t where t.typeName=? and t.companyId=?";
				List<LayerType> types = new ArrayList<LayerType>();
				Query query = session.createQuery(hql).setParameter(0, "班组").setParameter(1, ContextUtils.getCompanyId());
				types=query.list();
				List<LayerType> inspectionTypesLayer = new ArrayList<LayerType>();
				query = session.createQuery(hqlInspectionType).setParameter(0, "检验").setParameter(1, ContextUtils.getCompanyId());
				inspectionTypesLayer = query.list();
				List<LayerType> inspecorTypesLayer = new ArrayList<LayerType>();
				query = session.createQuery(hqlInspector).setParameter(0, "检验员").setParameter(1, ContextUtils.getCompanyId());
				inspecorTypesLayer = query.list();
				List<LayerType> machineTypesLayer  = new ArrayList<LayerType>();		
				query = session.createQuery(hqlMachine).setParameter(0, "机台").setParameter(1, ContextUtils.getCompanyId());
				machineTypesLayer = query.list();
              if(types.size()>0){
              	setColumn = types.get(0).getTypeCode();
              	inpectionTypeSetColumn = inspectionTypesLayer.get(0).getTypeCode();
              	inspector = inspecorTypesLayer.get(0).getTypeCode();
              	machine = machineTypesLayer.get(0).getTypeCode();
              	if(setColumn!=null){
                  	SpcSgTag spcSgTag = new SpcSgTag();
      				spcSgTag.setCreatedTime(spcSubGroup.getCreatedTime());
      				spcSgTag.setCompanyId(ContextUtils.getCompanyId());
      				spcSgTag.setCreator(mfgCheckInspectionReport.getInspector());
      				spcSgTag.setModifiedTime(new Date());
      				spcSgTag.setModifier(ContextUtils.getUserName());
      				spcSgTag.setTagName(types.get(0).getTypeName());
      				spcSgTag.setTagCode(setColumn);
      				if("D".equals(workGroupType)){
      					spcSgTag.setTagValue("SHIFT_D");
      					setProperty(spcSubGroup, setColumn, "SHIFT_D");
      				}else if("N".equals(workGroupType)){
      					spcSgTag.setTagValue("SHIFT_N");
      					setProperty(spcSubGroup, setColumn, "SHIFT_N");
      				}
      				spcSgTag.setSpcSubGroup(spcSubGroup);
      				
      				SpcSgTag spcSgTagInspectionType = new SpcSgTag();
      				spcSgTagInspectionType.setCreatedTime(spcSubGroup.getCreatedTime());
      				spcSgTagInspectionType.setCompanyId(ContextUtils.getCompanyId());
      				spcSgTagInspectionType.setCreator(mfgCheckInspectionReport.getInspector());
      				spcSgTagInspectionType.setModifiedTime(new Date());
      				spcSgTagInspectionType.setModifier(ContextUtils.getUserName());
      				spcSgTagInspectionType.setTagName(inspectionTypesLayer.get(0).getTypeName());
      				spcSgTagInspectionType.setTagCode(inpectionTypeSetColumn);
      				spcSgTagInspectionType.setTagValue(inspectionType);
  					setProperty(spcSubGroup, inpectionTypeSetColumn, inspectionType);
      				spcSgTagInspectionType.setSpcSubGroup(spcSubGroup);
      				
      				SpcSgTag spcSgTagInspector = new SpcSgTag();
      				spcSgTagInspector.setCreatedTime(spcSubGroup.getCreatedTime());
      				spcSgTagInspector.setCompanyId(ContextUtils.getCompanyId());
      				spcSgTagInspector.setCreator(mfgCheckInspectionReport.getInspector());
      				spcSgTagInspector.setModifiedTime(new Date());
      				spcSgTagInspector.setModifier(ContextUtils.getUserName());
      				spcSgTagInspector.setTagName(inspecorTypesLayer.get(0).getTypeName());
      				spcSgTagInspector.setTagCode(inspector);
      				spcSgTagInspector.setTagValue(mfgCheckInspectionReport.getInspector());
  					setProperty(spcSubGroup, inspector, mfgCheckInspectionReport.getInspector());
  					spcSgTagInspector.setSpcSubGroup(spcSubGroup);
  					
  					SpcSgTag spcSgTagMachine = new SpcSgTag();
  					spcSgTagMachine.setCreatedTime(spcSubGroup.getCreatedTime());
  					spcSgTagMachine.setCompanyId(ContextUtils.getCompanyId());
  					spcSgTagMachine.setCreator(mfgCheckInspectionReport.getInspector());
  					spcSgTagMachine.setModifiedTime(new Date());
      				spcSgTagInspector.setModifier(ContextUtils.getUserName());
      				spcSgTagMachine.setTagName(machineTypesLayer.get(0).getTypeName());
      				spcSgTagMachine.setTagCode(machine);
      				spcSgTagMachine.setTagValue(equipmentNo);
  					setProperty(spcSubGroup, machine, equipmentNo);
  					spcSgTagMachine.setSpcSubGroup(spcSubGroup);
  					
      				session.save(spcSgTag);
      				session.save(spcSgTagInspectionType);
      				session.save(spcSgTagInspector);
      				session.save(spcSgTagMachine);
  				}
				}
			}
			
		}else{
			spcSubGroup.setLastModifiedTime(new Date());
			spcSubGroup.setLastModifier(userName);
			String hql = "select max(s.samValue),min(s.samValue),sum(s.samValue),count(id) from SpcSgSample s where s.spcSubGroup = ? and s.companyId = ?";
			Object[] objs = (Object[])spcSubGroupDao.find(hql,spcSubGroup,companyid).get(0);
			Double max = paramValue,min=paramValue,sum=paramValue;
			if(objs[0] != null){
				Double maxVal = Double.valueOf(objs[0].toString());
				if(maxVal>max){
					max = maxVal;
				}
			}
			if(objs[1] != null){
				Double minVal = Double.valueOf(objs[1].toString());
				if(minVal < min){
					min = minVal;
				}
			}
			if(objs[2] != null){
				sum  += Double.valueOf(objs[2].toString());
			}
			if(objs[3] != null){
				sampleSize = Integer.valueOf(objs[3].toString());
			}
			Double sigma = sum/(sampleSize+1);
			Double randDiff = max - min;
			spcSubGroup.setMaxValue(max);
			spcSubGroup.setMinValue(min);
			spcSubGroup.setSigma(sigma);
			spcSubGroup.setRangeDiff(randDiff);
			spcSubGroup.setActualSmapleNum(sampleSize+1);
		}
		spcSubGroupDao.save(spcSubGroup);
		
		SpcSgSample spcSgSample = new SpcSgSample();
		spcSgSample.setCreatedTime(detectDate);
		spcSgSample.setCompanyId(companyid);
		spcSgSample.setCreator(mfgCheckInspectionReport.getInspector());
		spcSgSample.setLastModifiedTime(new Date());
		spcSgSample.setLastModifier(mfgCheckInspectionReport.getInspector());
		spcSgSample.setSampleNo("X" + (sampleSize + 1));
		spcSgSample.setSampleOrderNum(sampleSize + 1+"");
		spcSgSample.setSamValue(paramValue);
		spcSgSample.setSpcSubGroup(spcSubGroup);
		spcSgSampleDao.save(spcSgSample);
		//如果采集的数据够一组时,检查监控条件
		if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())&&equipmentNo!=null){
			if(spcSubGroup.getSubGroupSize() != null && spcSubGroup.getSubGroupSize().equals(sampleSize+1)){
				//根据条件查询采集的数据
				String hql = "select s.spcSubGroup from SpcSgTag s where s.companyId = ?  and s.spcSubGroup.qualityFeature = ? and s.tagValue=?";
				List<Object> searchParams = new ArrayList<Object>();
				searchParams.add(spcSubGroup.getCompanyId());
				searchParams.add(feature);
				searchParams.add(equipmentNo);
				//10天以前的数据
				hql = hql + " and s.spcSubGroup.createdTime between ? and ?";
				Calendar calendar = Calendar.getInstance();
				searchParams.add(calendar.getTime());
				calendar.add(Calendar.DATE, -10);
				searchParams.add(searchParams.size()-1, calendar.getTime());
				List<SpcSubGroup> list = spcSubGroupDao.find(hql,searchParams.toArray());
				BaseUtil.setSpcSgSampleDao(spcSgSampleDao);
				//根据规则检测所查询的数据  异常报警
				try{
					abnormalInfoManager.lanchAbnormalCCM(spcSubGroup.getSubGroupOrderNum()+"", feature, list,null,equipmentNo);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				BaseUtil.setSpcSgSampleDao(null);
				
				spcSubGroup.setJudgeState(1);
				spcSubGroupDao.save(spcSubGroup);
			}
		}else{
			if(spcSubGroup.getSubGroupSize() != null && spcSubGroup.getSubGroupSize().equals(sampleSize+1)){
				//根据条件查询采集的数据
				String hql = "from SpcSubGroup s where s.companyId = ?  and s.qualityFeature = ?";
				List<Object> searchParams = new ArrayList<Object>();
				searchParams.add(spcSubGroup.getCompanyId());
				searchParams.add(feature);
				//10天以前的数据
				hql = hql + " and s.createdTime between ? and ?";
				Calendar calendar = Calendar.getInstance();
				searchParams.add(calendar.getTime());
				calendar.add(Calendar.DATE, -10);
				searchParams.add(searchParams.size()-1, calendar.getTime());
				List<SpcSubGroup> list = spcSubGroupDao.find(hql,searchParams.toArray());
				BaseUtil.setSpcSgSampleDao(spcSgSampleDao);
				//根据规则检测所查询的数据  异常报警
				try{
					abnormalInfoManager.lanchAbnormal(spcSubGroup.getSubGroupOrderNum()+"", feature, list,null);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				BaseUtil.setSpcSgSampleDao(null);
				
				spcSubGroup.setJudgeState(1);
				spcSubGroupDao.save(spcSubGroup);
			}
		}
		
		return spcSgSample;
	} 
	
	/**
	  * 方法名: 根据车间名称查询暂存区的巡检产品
	  * <p>功能说明：</p>
	  * @param workshop
	  * @return
	 */
	public List<Object> queryTemporaryProducts(String workshop){
		String hql = "select distinct i.checkBomCode,i.checkBomName from MfgCheckInspectionReport i where i.patrolState = ? and i.workshop = ? and i.inspectionPointType=? order by i.checkBomName";
		return mfgCheckInspectionReportDao.find(hql,MfgCheckInspectionReport.PATROL_STATE_DEFAULT,workshop,InspectionPointTypeEnum.PATROLINSPECTION);
	}
	
	/**
	  * 方法名:根据物料和用户查询拥有的权限工序 
	  * <p>功能说明：</p>
	  * @param checkBomCode
	  * @param userId
	  * @return
	 */
	public List<Option> queryPatrolWorkProcedureByUser(String checkBomCode,Long userId){
		List<Option> permissionOptions = new ArrayList<Option>();
		List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_procedure");
		if(!options.isEmpty()){
			//查询有权限的工序
			String hql = "select distinct i.workProcedure from InspectionPoint i inner join i.inspectors s where i.listType = ? and s.userId = ?";
			List<String> workProcedures = mfgCheckInspectionReportDao.find(hql,InspectionPointTypeEnum.PATROLINSPECTION,userId);
			Map<String,Boolean> hasMap = new HashMap<String, Boolean>();
			for(String procedure : workProcedures){
				hasMap.put(procedure, true);
			}
			//查询有绑定产品标准的工序
			hql = "select distinct i.workingProcedure from MfgInspectingIndicator i where i.materielCode = ?";
			workProcedures = mfgCheckInspectionReportDao.find(hql,checkBomCode);
			Map<String,Boolean> standardMap = new HashMap<String, Boolean>();
			for(String procedure : workProcedures){
				standardMap.put(procedure, true);
			}
			for(Option option : options){
				if(hasMap.containsKey(option.getValue())
					&&standardMap.containsKey(option.getValue())){
					permissionOptions.add(option);
				}
			}
		}
		return permissionOptions;
	}
	
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param id
	  * @throws IOException
	 */
	public void exportReport(Long id) throws IOException{
		InputStream inputStream = null;
		try {
			MfgCheckInspectionReport report = mfgCheckInspectionReportDao.get(id);
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-inspection-report.xls");
			Workbook book = WorkbookFactory.create(inputStream);
			Sheet sheet = book.getSheetAt(0);
			String title = "首检报告";
//			if(InspectionPointTypeEnum.DELIVERINSPECTION.getCode().equals(report.getInspectionPointType().getCode())){
//				title = "制程抽检报告";
//			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(report.getInspectionPointType().getCode())){
//				title = "成品检验报告";
//			}
			ExcelUtil.getCell(sheet,"a1").setCellValue(title);
			ExcelUtil.getCell(sheet,"a2").setCellValue("编号:" + report.getInspectionNo());
			ExcelUtil.getCell(sheet,"h3").setCellValue(report.getSection()==null?"":report.getSection());
			ExcelUtil.getCell(sheet,"m3").setCellValue(report.getProductionLine()==null?"":report.getProductionLine());
			ExcelUtil.getCell(sheet,"d4").setCellValue(report.getWorkGroupType()==null?"":report.getWorkGroupType());
			ExcelUtil.getCell(sheet,"h4").setCellValue(report.getWorkProcedure()==null?"":report.getWorkProcedure());
			ExcelUtil.getCell(sheet,"m4").setCellValue(report.getCheckBomCode()==null?"":report.getCheckBomCode());
			ExcelUtil.getCell(sheet,"d5").setCellValue(report.getCheckBomModel()==null?"":report.getCheckBomModel());
			ExcelUtil.getCell(sheet,"h5").setCellValue(report.getCheckBomName()==null?"":report.getCheckBomName());
			ExcelUtil.getCell(sheet,"m5").setCellValue(report.getStockAmount()==null?"":report.getStockAmount().toString());
			ExcelUtil.getCell(sheet,"d6").setCellValue(report.getInspectionDate()==null?"":DateUtil.formateDateStr(report.getInspectionDate()));
			ExcelUtil.getCell(sheet,"m8").setCellValue(report.getStandardVersion()==null?"":report.getStandardVersion());
			
			//保存明细
			int index = 0;
			CellStyle cellStyle = ExcelUtil.getCellBorderStyle(book);
			for(MfgCheckItem checkItem : report.getCheckItems()){
				if(index > 0){
					int insertRowNum = index + 9;
					sheet.shiftRows(insertRowNum, sheet.getLastRowNum(),1,true,false);
					sheet.createRow(insertRowNum);
					Row row = sheet.getRow(insertRowNum);
					row.setHeightInPoints(29);
					for(int j=0;j<=14;j++){
						row.createCell(j).setCellStyle(cellStyle);
					}
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 1,2));
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 3,4));
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 9,12));
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 9,12));
				}
				//序号
				Cell cell = ExcelUtil.getCell(sheet,"a10",index);
				cell.setCellValue(index+1);
				//分类
				cell = ExcelUtil.getCell(sheet,"b10",index);
				cell.setCellValue(checkItem.getParentItemName()==null?"":checkItem.getParentItemName());
				//检查项目
				cell = ExcelUtil.getCell(sheet,"d10",index);
				cell.setCellValue(checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName());
				//检查方法
				cell = ExcelUtil.getCell(sheet,"f10",index);
				cell.setCellValue(checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod());
				//检查类别
				cell = ExcelUtil.getCell(sheet,"g10",index);
				cell.setCellValue(checkItem.getInspectionType()==null?"":checkItem.getInspectionType());
				//抽检数量
				cell = ExcelUtil.getCell(sheet,"h10",index);
				cell.setCellValue(checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount().toString());
				//规格
				cell = ExcelUtil.getCell(sheet,"i10",index);
				cell.setCellValue(checkItem.getSpecifications()==null?"":checkItem.getSpecifications());
				//检验记录
				cell = ExcelUtil.getCell(sheet,"j10",index);
				String str = "";
				if(MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					str = checkItem.getResults();
				}else{
					for(int i=1;i<=80;i++){
						Object val = PropertyUtils.getProperty(checkItem,"result" + i);
						if(val != null){
							str += "," + val;
						}
					}
				}
				cell.setCellValue(StringUtils.isEmpty(str)?"":str.substring(1));
				//合格数
				cell = ExcelUtil.getCell(sheet,"n10",index);
				cell.setCellValue(checkItem.getQualifiedAmount()==null?"":checkItem.getQualifiedAmount().toString());
				//不良数
				cell = ExcelUtil.getCell(sheet,"o10",index);
				cell.setCellValue(checkItem.getUnqualifiedAmount()==null?"":checkItem.getUnqualifiedAmount().toString());
				//结论
				cell = ExcelUtil.getCell(sheet,"p10",index);
				cell.setCellValue(checkItem.getConclusion()==null?"":checkItem.getConclusion().toString());
				index++;
			}
			index--;
			//底部的内容
			ExcelUtil.getCell(sheet,"d14",index).setCellValue(report.getInspectionAmount()==null?"":report.getInspectionAmount().toString());
			ExcelUtil.getCell(sheet,"d15",index).setCellValue(report.getQualifiedAmount()==null?"":report.getQualifiedAmount().toString());
			ExcelUtil.getCell(sheet,"h15",index).setCellValue(report.getUnqualifiedAmount()==null?"":report.getUnqualifiedAmount().toString());
			ExcelUtil.getCell(sheet,"m15",index).setCellValue(report.getQualifiedRate()==null?"":(report.getQualifiedRate()*100 + "%"));
			ExcelUtil.getCell(sheet,"d16",index).setCellValue(report.getInspectionConclusion()==null?"":("OK".equals(report.getInspectionConclusion())?"合格":"不合格"));
			ExcelUtil.getCell(sheet,"h16",index).setCellValue(report.getAuditMan()==null?"":report.getAuditMan());
			ExcelUtil.getCell(sheet,"m16",index).setCellValue(report.getInspector()==null?"":report.getInspector());
			String fileName = title + ".xls";
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
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append("error.xls").append("\"")
					.toString());
			response.getOutputStream().write(("服务器错误:" + e.getMessage()).getBytes());
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
	}
	
	/**
	 * 方法名:重新检验 
	  * <p>功能说明：</p>
	  * @param deleteIds
	 */
	public void reCheck(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				MfgCheckInspectionReport report = mfgCheckInspectionReportDao.get(Long.valueOf(id));
				if(report != null){
					if(MfgCheckInspectionReport.STATE_DEFECTIVE_PROCESS.equals(report.getReportState())){
						throw new AmbFrameException("编号为【"+report.getInspectionNo()+"】的检验报告的状态为【"+report.getReportState()+"】,不能重新检验!");
					}
					if(MfgCheckInspectionReport.STATE_DEFAULT.equals(report.getReportState())
						||MfgCheckInspectionReport.STATE_RECHECK.equals(report.getReportState())){
						continue;
					}
					report.setReportState(MfgCheckInspectionReport.STATE_RECHECK);
					report.setAuditMan(ContextUtils.getUserName());
					report.setAuditRemark(Struts2Utils.getParameter("opinion"));
					mfgCheckInspectionReportDao.save(report);
				}
			}
		}
	}
	
	/**
	  * 方法名: 审核检验报告
	  * <p>功能说明：</p>
	  * @param ids
	 */
	public void auditReport(String ids){
		for(String id : ids.split(",")){
			if(StringUtils.isNotEmpty(id)){
				MfgCheckInspectionReport report = mfgCheckInspectionReportDao.get(Long.valueOf(id));
				if(report != null){
					if(!MfgCheckInspectionReport.STATE_AUDIT.equals(report.getReportState())){
						throw new AmbFrameException("只能审核状态为【"+MfgCheckInspectionReport.STATE_AUDIT+"】的检验单!");
					}
					report.setReportState(MfgCheckInspectionReport.STATE_COMPLETE);
					report.setAuditMan(ContextUtils.getUserName());
					report.setAuditRemark(Struts2Utils.getParameter("opinion"));
					mfgCheckInspectionReportDao.save(report);
				}
			}
		}
	}

	public List<MfgCheckInspectionReport> getListByInspectingItemIndicator(
			Date startDate, Date endDate,
			MfgItemIndicator inspectingItemIndicator) {
		// TODO Auto-generated method stub
		return mfgCheckInspectionReportDao.getListByInspectingItemIndicator(startDate, endDate, inspectingItemIndicator);
	}
	
	@SuppressWarnings("unchecked")
	public List<MfgCheckInspectionReport> getListByEndTime(
			Date startDate, Date endDate, Session session) {
		String hql = "from MfgCheckInspectionReport a where a.companyId = ?  and a.inspectionDate >= ? and a.inspectionDate <= ?  and a.workflowInfo.currentActivityName=? and a.hasSynced=?  ";
		hql += " order by a.inspectionDate asc";
		Query query = session.createQuery(hql).setParameter(0, ContextUtils.getCompanyId())
				.setParameter(1, startDate).setParameter(2, endDate).setParameter(3, "流程结束").setParameter(4, "否");
		return query.list();
//		return mfgCheckInspectionReportDao.getListByInspectingItemIndicator(startDate, endDate);
	}	
}
