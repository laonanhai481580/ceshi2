package com.ambition.iqc.inspectionreport.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.iqc.entity.AcceptanceQualityLimit;
import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.entity.ErpRequestRecord;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.InspectingIndicator;
import com.ambition.iqc.entity.InspectingItem;
import com.ambition.iqc.entity.ItemIndicator;
import com.ambition.iqc.entity.MaterielTypeLevel;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.entity.SampleTransferRecord;
import com.ambition.iqc.entity.SampleTransitionRule;
import com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao;
import com.ambition.iqc.samplestandard.service.SampleC1051CodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.iqc.samplestandard.service.SampleTransitionRuleManager;
import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.abnormal.util.BaseUtil;
import com.ambition.spc.dataacquisition.dao.SpcSgSampleDao;
import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.dataacquisition.service.SpcMfgInterface;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.opensymphony.xwork2.ActionContext;

import edu.emory.mathcs.backport.java.util.LinkedList;
/**    
* 检验报告SERVICE
* @authorBy wlongfeng
*
*/
@Service
@Transactional
public class IncomingInspectionActionsReportManager{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private  IncomingInspectionActionsReportDao incomingInspectionActionsReportDao;
	@Resource(name="ordbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SampleTransferRecordManager sampleTransferRecordManager;
	
	@Autowired
	private SampleTransitionRuleManager sampleTransitionRuleManager;
	
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	
	@Autowired
	private FormCodeGenerated formCodeGenerated;
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
	private UseFileManager useFileManager;
	
	
	@Autowired
	private SampleC1051CodeLetterManager sampleC1051CodeLetterManager;
	
//	@Autowired
//	private WarnRuleManager warnRuleManager;
	
	public IncomingInspectionActionsReport getIncomingInspectionActionsReport(Long id){
		return incomingInspectionActionsReportDao.get(id);
	}
	
	public IncomingInspectionActionsReport getIncomingInspectionActionsReportByBatchNo(String batchNo){
		List<IncomingInspectionActionsReport> incomingInspectionActionsReportList=incomingInspectionActionsReportDao.getIncomingInspectionActionsReportByBatchNo(batchNo);
		IncomingInspectionActionsReport incomingInspectionActionsReport=new IncomingInspectionActionsReport();
		if(incomingInspectionActionsReportList.size()>0){
			incomingInspectionActionsReport=(IncomingInspectionActionsReport) incomingInspectionActionsReportList.get(0);
		}
		return incomingInspectionActionsReport;
	}
	
	public Page<IncomingInspectionActionsReport> itemIsNullList(Page<IncomingInspectionActionsReport>page,String startStr,String endStr){
		return  incomingInspectionActionsReportDao.itemIsNullList(page,startStr,endStr);
	}
	
	public IncomingInspectionActionsReport getIncomingInspectionActionsReportByInspectionNo(String inspectionNo){
		List<IncomingInspectionActionsReport> incomingInspectionActionsReportList=incomingInspectionActionsReportDao.getIncomingInspectionActionsReportByInspectionNo(inspectionNo);
		if(incomingInspectionActionsReportList.isEmpty()){
			return null;
		}else{
			return incomingInspectionActionsReportList.get(0);
		}
//		IncomingInspectionActionsReport incomingInspectionActionsReport=new IncomingInspectionActionsReport();
//		if(incomingInspectionActionsReportList.size()>0){
//			incomingInspectionActionsReport=(IncomingInspectionActionsReport) incomingInspectionActionsReportList.get(0);
//		}
//		return incomingInspectionActionsReport;
	}
	
	public IncomingInspectionActionsReport getIncomingInspectionActionsReportBySupplierAndMaterial(String supplierCode,String checkBomCode,Date inspectionDate){
		return getIncomingInspectionActionsReportBySupplierAndMaterial(supplierCode,checkBomCode,inspectionDate,ContextUtils.getCompanyId());
		
	}
	public IncomingInspectionActionsReport getIncomingInspectionActionsReportBySupplierAndMaterial(String supplierCode,String checkBomCode,Date inspectionDate,Long companyId){
		String hql = "from IncomingInspectionActionsReport i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode = ? and i.inspectionDate < ? and i.inspectionConclusion='NG' order by i.createdTime desc";
		List<IncomingInspectionActionsReport> list = incomingInspectionActionsReportDao.find(hql,companyId,supplierCode,checkBomCode,inspectionDate);
		if(list!=null && list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
		
	}
	public Boolean checkIsNewMatriel(String supplierCode,String productBomCode,Date inspectionDate){
		String hql = "select count(i.id) from IncomingInspectionActionsReport i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode = ? and i.inspectionDate < ?";
		List<?> list = incomingInspectionActionsReportDao.find(hql,ContextUtils.getCompanyId(),supplierCode,productBomCode,inspectionDate);
		if(Integer.valueOf(list.get(0).toString())>0){
			return false;
		}else{
			return true;
		}
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
	public Map<String,Object> getCheckItems(String processSection,String supplierCode,String checkBomCode,Double stockAmount,Date inspectionDate,List<JSONObject> existCheckItems) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return getCheckItems(processSection,supplierCode, checkBomCode, stockAmount, inspectionDate, existCheckItems,ContextUtils.getCompanyId());
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
	@SuppressWarnings("unused")
	public Map<String,Object> getCheckItems(String processSection,String supplierCode,String checkBomCode,Double stockAmount,Date inspectionDate,List<JSONObject> existCheckItems,Long companyId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String,JSONObject> existItemMap = new HashMap<String, JSONObject>();
		if(existCheckItems != null){
			for(int i=0;i<existCheckItems.size();i++){
				JSONObject json = existCheckItems.get(i);
				if(json.containsKey("checkItemName")){
					existItemMap.put(json.getString("checkItemName"),json);
				}
			}
		}
		List<CheckItem> checkItems = new ArrayList<CheckItem>();
		SampleTransferRecord sampleTransferRecord = sampleTransferRecordManager.getSampleTransferRecord(processSection,supplierCode, checkBomCode,inspectionDate,companyId);
		if(sampleTransferRecord==null||!SampleScheme.EXEMPTION_TYPE.equals(sampleTransferRecord.getTargetRule())){
			String baseType = sampleSchemeManager.getUseBaseType(companyId).getBaseType();
			Map<String,String> codeLetterMap = new HashMap<String, String>();
			Map<String,AcceptanceQualityLimit> qualityLimitMap = new HashMap<String, AcceptanceQualityLimit>();
			Pattern pattern = Pattern.compile("[0-9]*|[0-9]*\\.[0-9]*");
			List<ItemIndicator> itemIndicators = null;
			String hql = "";
			//查询所有的检验项目
			String companyName = ContextUtils.getCompanyName();
//			if("欧菲科技-CCM".equals(companyName)){
//				String checkBomMaterialType = Struts2Utils.getParameter("checkBomMaterialType");
//				hql = "from ItemIndicator i where i.companyId = ? and i.inspectingIndicator.materielType = ? and i.inspectingIndicator.isMax=?";
//				itemIndicators = incomingInspectionActionsReportDao.find(hql,companyId,checkBomMaterialType,true);
//			}else{
//				hql = "from ItemIndicator i where i.companyId = ? and i.inspectingIndicator.materielCode = ? and i.inspectingIndicator.isMax=?";
//				itemIndicators = incomingInspectionActionsReportDao.find(hql,companyId,checkBomCode,true);
//			}
			hql = "from ItemIndicator i where i.companyId = ? and i.inspectingIndicator.materielCode = ? and i.inspectingIndicator.isMax=?";
			itemIndicators = incomingInspectionActionsReportDao.find(hql,companyId,checkBomCode,true);
			Collections.sort(itemIndicators,new Comparator<ItemIndicator>() {
				@Override
				public int compare(ItemIndicator o1, ItemIndicator o2) {
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
			List<InspectingItem> parentInspectingItems = new ArrayList<InspectingItem>();
			Map<Long,List<ItemIndicator>> parentItemIndicatorMap = new HashMap<Long, List<ItemIndicator>>();
			for(ItemIndicator itemIndicator : itemIndicators){
//				if(resultMap.isEmpty()){
//					InspectingIndicator indicator = itemIndicator.getInspectingIndicator();
//					resultMap.put("is3C",indicator.getIs3C());
//					resultMap.put("isStandard",indicator.getIsStandard());
//					resultMap.put("iskeyComponent",indicator.getIskeyComponent());
//					resultMap.put("indicatorAttachmentFiles",indicator.getAttachmentFiles());
//					resultMap.put("materialType",indicator.getMaterielType());
//					resultMap.put("workingProcedure",indicator.getWorkingProcedure());
//					resultMap.put("standardVersion",indicator.getStandardVersion());
//				}
				InspectingItem inspectingItem = itemIndicator.getInspectingItem();
				InspectingItem parentInspectionItem = inspectingItem.getItemParent();
				if(parentInspectionItem==null){
					parentInspectionItem = inspectingItem;
				}
				if(parentItemIndicatorMap.containsKey(parentInspectionItem.getId())){
					parentItemIndicatorMap.get(parentInspectionItem.getId()).add(itemIndicator);
				}else{
					List<ItemIndicator> indicators = new ArrayList<ItemIndicator>();
					indicators.add(itemIndicator);
					parentItemIndicatorMap.put(parentInspectionItem.getId(),indicators);
					parentInspectingItems.add(parentInspectionItem);
				}
			}
			for(InspectingItem parentInspectingItem : parentInspectingItems){
				List<ItemIndicator> indicators = parentItemIndicatorMap.get(parentInspectingItem.getId());
				boolean isRowSpan = false;
				for(ItemIndicator itemIndicator : indicators){
					if(itemIndicator.getIsInEquipment()!=null&&itemIndicator.getIsInEquipment().equals("是")){
						continue;
					}
					CheckItem checkItem = new CheckItem();
					if(!isRowSpan){
						isRowSpan = true;
						checkItem.setParentItemName(parentInspectingItem.getItemName());
						checkItem.setParentRowSpan(indicators.size());
					}
					checkItem.setParentName(parentInspectingItem.getItemName());
					//获取最新的抽样方案,默认正常抽检
					if(sampleTransferRecord != null){
						checkItem.setSampleSchemeType(sampleTransferRecord.getTargetRule());
						checkItem.setSchemeStartDate(sampleTransferRecord.getAuditTime());
					}else{
						checkItem.setSampleSchemeType(SampleScheme.ORDINARY_TYPE);
					}
					checkItem.setCreatedTime(new Date());
					checkItem.setCheckMethod(itemIndicator.getIndicatorMethod());
					checkItem.setEquipmentNumber(itemIndicator.getIndicatorMethod());
					checkItem.setClassification(itemIndicator.getInspectingItem().getClassification());
					checkItem.setInspectionType(itemIndicator.getInspectingItem().getTopParent().getItemName());
					checkItem.setSpecifications(itemIndicator.getSpecifications());
					String level = itemIndicator.getInspectionLevel();
					//1916标准特殊加严,从右到左
					String tempType = checkItem.getSampleSchemeType();
					String aql = itemIndicator.getAqlStandard();
					String aqls[]= null;
					if(SampleCodeLetter.MIL1051_TYPE.equals(baseType)){
						aqls = SampleScheme.getMit1051AQLs(companyId);
					}
					if(level != null && SampleCodeLetter.MIL_TYPE.equals(baseType)){
						String prefix = "validateLevel";
						if(SampleScheme.TIGHTEN_TYPE.equals(checkItem.getSampleSchemeType())){//加严
							if(level.startsWith(prefix)
									&&StringUtils.isNumeric(level.substring(prefix.length()))){
								int l = Integer.valueOf(level.substring(prefix.length()));
								if(l<7){
									l++;
									tempType = SampleScheme.ORDINARY_TYPE;
								}
								level = prefix + l;
							}
						}else if(SampleScheme.RELAX_TYPE.equals(checkItem.getSampleSchemeType())){//减量
							if(level.startsWith(prefix)
									&&StringUtils.isNumeric(level.substring(prefix.length()))){
								int l = Integer.valueOf(level.substring(prefix.length()));
								if(l>1){
									l--;
									tempType = SampleScheme.ORDINARY_TYPE;
								}
								level = prefix + l;
							}
						}
					}
					if(StringUtils.isNotEmpty(aql) && SampleCodeLetter.MIL1051_TYPE.equals(baseType)){
						int index = -1;
						for(int i=0;i<aqls.length;i++){
							if(aqls[i].equals(aql)){
								index = i;
								break;
							}
						}
						if(index>-1){
							if(SampleScheme.TIGHTEN_TYPE.equals(checkItem.getSampleSchemeType())){//加严
								if(index > 0){
									aql = aqls[--index];
									tempType = SampleScheme.TIGHTEN_TYPE;
								}else{
									tempType = SampleScheme.ORDINARY_TYPE;
								}
							}else if(SampleScheme.RELAX_TYPE.equals(checkItem.getSampleSchemeType())){
								if(index<aqls.length-1){
									aql = aqls[++index];
									tempType = SampleScheme.RELAX_TYPE;
								}else{
									tempType = SampleScheme.ORDINARY_TYPE;
								}
							}
						}
					}
					checkItem.setAql(aql);
					checkItem.setInspectionLevel(level);
					
					checkItem.setCheckItemName(itemIndicator.getInspectingItem().getItemName());
					checkItem.setCountType(itemIndicator.getCountType());
					checkItem.setMaxlimit(itemIndicator.getLevela());
					checkItem.setMinlimit(itemIndicator.getLevelb());
					checkItem.setResults("");
					checkItem.setUnit(itemIndicator.getIndicatorUnit());
					checkItem.setStandardRemark(itemIndicator.getRemark());
					//如果是设置的检验数量,则直接按照规定的检验数量检验
					if(itemIndicator.getInspectionAmount() != null){
						checkItem.setInspectionLevel("n=" + itemIndicator.getInspectionAmount());
						String amountStr = itemIndicator.getInspectionAmount();
						int inspectionAmount = 0;
						if(amountStr.endsWith("%")){
							String val = amountStr.substring(0,amountStr.lastIndexOf("%"));
							inspectionAmount = (int)(Double.valueOf(val)/100 * stockAmount);
						}else{
							inspectionAmount = Double.valueOf(amountStr).intValue();
						}
						if(inspectionAmount>stockAmount){
							checkItem.setInspectionAmount(stockAmount.intValue());
						}else{
							checkItem.setInspectionAmount(inspectionAmount);
						}
						checkItem.setQualifiedAmount(checkItem.getInspectionAmount());
						checkItem.setUnqualifiedAmount(0);
						checkItem.setAqlAc(0);
						checkItem.setAqlRe(1);
					}else{
						//根据检验级别获取样本量字码
						if(!codeLetterMap.containsKey(level)){
							codeLetterMap.put(level,sampleCodeLetterManager.queryCodeLetter(baseType,level,stockAmount.intValue(),companyId));
						}
						checkItem.setCodeLetter(codeLetterMap.get(level));
						//获取AQL标准
						if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
							checkItem.setAql(level);
						}else if(SampleCodeLetter.MIL1051_TYPE.equals(baseType)){
							checkItem.setAql(aql);
							checkItem.setInspectionLevel(aql);
						}else{
							checkItem.setAql(itemIndicator.getAqlStandard());
						}
						String codeLetter = checkItem.getCodeLetter();
						codeLetter = codeLetter==null?"":codeLetter;
						aql = checkItem.getAql()==null?"":checkItem.getAql();
						String type = checkItem.getSampleSchemeType();
						String key = type + "_" + codeLetter + "_" + aql;
						if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
							key += "_" + checkItem.getCountType();
						}
						if(!qualityLimitMap.containsKey(key)){
							if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
								qualityLimitMap.put(key,sampleSchemeManager.queryQualityLimit(codeLetter,aql,baseType,tempType,checkItem.getCountType(),companyId));
							}if(SampleCodeLetter.MIL1051_TYPE.equals(baseType)){
								qualityLimitMap.put(key,sampleC1051CodeLetterManager.queryQualityLimit(stockAmount.intValue(),aql,companyId));
							}else{
								qualityLimitMap.put(key,sampleSchemeManager.queryQualityLimit(codeLetter,aql,baseType,type,companyId));
							}
						}
						AcceptanceQualityLimit acceptanceQualityLimit = qualityLimitMap.get(key);
						if(acceptanceQualityLimit!=null){
							checkItem.setAqlAc(acceptanceQualityLimit.getAc());
							checkItem.setAqlRe(acceptanceQualityLimit.getRe());
							checkItem.setInspectionAmount(acceptanceQualityLimit.getAmount());
							if(checkItem.getInspectionAmount() != null){
								if(checkItem.getInspectionAmount()>stockAmount){
									checkItem.setInspectionAmount(stockAmount.intValue());
								}
								checkItem.setQualifiedAmount(checkItem.getInspectionAmount());
								checkItem.setUnqualifiedAmount(0);
							}else{
								checkItem.setQualifiedAmount(0);
								checkItem.setUnqualifiedAmount(0);
							}
						}else{
							checkItem.setQualifiedAmount(0);
							checkItem.setUnqualifiedAmount(0);
						}
						if(checkItem.getAqlAc()==null){
							checkItem.setAqlAc(0);
						}
						if(checkItem.getAqlRe()==null){
							checkItem.setAqlRe(1);
						}
					}
					
					if(existItemMap.containsKey(checkItem.getCheckItemName())){
						JSONObject json = existItemMap.get(checkItem.getCheckItemName());
						if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
							if(json.containsKey("results")){
								checkItem.setResults(json.getString("results"));
							}
						}else{
							for(int i=1;i<=80;i++){
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
					if(checkItem.getAqlRe()==null||checkItem.getUnqualifiedAmount()==null){
						checkItem.setConclusion("NG");
					}else{
						if(checkItem.getUnqualifiedAmount()<checkItem.getAqlRe()){
							checkItem.setConclusion("OK");
						}else{
							checkItem.setConclusion("NG");
						}
					}
					//上批检验物料单
					IncomingInspectionActionsReport report = this.getIncomingInspectionActionsReportBySupplierAndMaterial(supplierCode,checkBomCode,inspectionDate,companyId);
					if(report!=null){
						if(report.getCheckItems()!=null && report.getCheckItems().size()!=0){
							for(CheckItem item:report.getCheckItems()){
								if(item!=null && item.getConclusion()!=null && item.getConclusion().equals("NG")){
									if(item.getInspectionType().equals(checkItem.getInspectionType()) && item.getCheckItemName().equals(checkItem.getCheckItemName())){
										checkItem.setFlag("1");
									}
								}
							}
						}
					}
					checkItems.add(checkItem);
				}
			}
		}else{
			CheckItem checkItem = new CheckItem();
			checkItem.setSampleSchemeType(sampleTransferRecord.getTargetRule());
			checkItem.setSchemeStartDate(sampleTransferRecord.getAuditTime());
			checkItems.add(checkItem);
		}
		resultMap.put("checkItems",checkItems);
		return resultMap;
	}
	
	/**
	 * 根据供应商,图号,检验数查询检验项目
	 * @param report
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("unchecked")
	public List<CheckItem> getCheckItems(IncomingInspectionActionsReport report,List<JSONObject> existCheckItems) throws NumberFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String supplierCode = report.getSupplierCode(),checkBomCode = report.getCheckBomCode();
		Date inspectionDate = report.getInspectionDate();
		Double stockAmount = report.getStockAmount();
		Map<String,Object> resultMap = getCheckItems(report.getBusinessUnitCode(),supplierCode, checkBomCode, stockAmount, inspectionDate, existCheckItems,report.getCompanyId());
		report.setIs3C(resultMap.get("is3C")==null?null:resultMap.get("is3C").toString());
		report.setIsStandard(resultMap.get("isStandard")==null?null:resultMap.get("isStandard").toString());
		report.setIskeyComponent(resultMap.get("iskeyComponent")==null?null:resultMap.get("iskeyComponent").toString());
		report.setCheckBomMaterialType(resultMap.get("materialType")==null?null:resultMap.get("materialType").toString());
		report.setWorkingProcedure(resultMap.get("workingProcedure")==null?null:resultMap.get("workingProcedure").toString());
		report.setStandardVersion(resultMap.get("standardVersion")==null?null:resultMap.get("standardVersion").toString());
		return (List<CheckItem>) resultMap.get("checkItems");
	}
	
	public List<IncomingInspectionActionsReport> getIncomingInspectionActionsReportListByBatchNo(String batchNo){
		return incomingInspectionActionsReportDao.getIncomingInspectionActionsReportByBatchNo(batchNo);
	}
	
	public void saveIncomingInspectionActionsReport(IncomingInspectionActionsReport incomingInspectionActionsReport){
		incomingInspectionActionsReportDao.save(incomingInspectionActionsReport);
	}
	
	/**
	 * 方法名: getMaterielLevelByCodeNo
	 * <p>功能说明：根据物料编码获取物料级别数据</p>
	 * 创建人:wuxuming 日期： 2015-3-2 version 1.0
	 * @param 
	 * @return
	 */
	public InspectingIndicator getMaterielLevelByCodeNo(String checkedBomCode) throws Exception{
		String hql=" from InspectingIndicator l where l.materielCode=? and l.companyId=? and l.isMax=? ";
		List<InspectingIndicator> listObj=incomingInspectionActionsReportDao.find(hql, checkedBomCode,ContextUtils.getCompanyId(),true);
		if(!listObj.isEmpty()){
			if(listObj.size()>1){
				throw new  AmbFrameException("该品号获取的物料级别不具备唯一性，请联系相关人员!!");
			}else{
				InspectingIndicator inspectingIndicator=listObj.get(0);
				return inspectingIndicator;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 保存进货检验记录
	 * @param incomingInspectionActionsReport
	 * @param checkItemArray
	 * @throws Exception 
	 */
	public void saveIncomingInspectionActionsReport(IncomingInspectionActionsReport incomingInspectionActionsReport,List<JSONObject> checkItemArray) throws Exception{
		if(!(IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT.equals(incomingInspectionActionsReport.getInspectionState())
				||IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK.equals(incomingInspectionActionsReport.getInspectionState()))){
			throw new AmbFrameException("只能保存检验状态为【"+IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT+"】或为【"+IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK+"】的检验单!");
		}
		String schemeStartDateStr = Struts2Utils.getParameter("schemeStartDate");
		if(schemeStartDateStr != null){
			incomingInspectionActionsReport.setSchemeStartDate(DateUtil.parseDateTime(schemeStartDateStr));
		}
		boolean isOver=true;
		if(checkItemArray != null){
			if(incomingInspectionActionsReport.getId()==null){
				incomingInspectionActionsReport.setCheckItems(new ArrayList<CheckItem>());
			}else{
				incomingInspectionActionsReport.getCheckItems().clear();
			}
			for(JSONObject json : checkItemArray){
				CheckItem checkItem = new CheckItem();
				checkItem.setCompanyId(ContextUtils.getCompanyId());
				checkItem.setCreatedTime(new Date());
				checkItem.setCreator(ContextUtils.getUserName());
				checkItem.setLastModifiedTime(new Date());
				checkItem.setLastModifier(ContextUtils.getUserName());
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());
					setProperty(checkItem, key.toString(),value);
				}
				if(!checkItem.getItemStatus().equals("已领取")&&!"欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
					isOver=false;
//					checkItem.setInspectionMan(ContextUtils.getUserName());
				}
				checkItem.setIiar(incomingInspectionActionsReport);
				incomingInspectionActionsReport.getCheckItems().add(checkItem);	
			}
		}
		double qualifiedAmount = incomingInspectionActionsReport.getQualifiedAmount()==null?0:incomingInspectionActionsReport.getQualifiedAmount();
		double inspectionAmount = incomingInspectionActionsReport.getInspectionAmount()==null?0:incomingInspectionActionsReport.getInspectionAmount();
/*		if(inspectionAmount==0){
			incomingInspectionActionsReport.setQualifiedRate(null);
		}else{
			incomingInspectionActionsReport.setQualifiedRate((float) (qualifiedAmount/inspectionAmount));
		}*/
		String isSubmit = Struts2Utils.getParameter("isSubmit");
		
		if("true".equals(isSubmit)&&isOver){
//			if("OK".equals(incomingInspectionActionsReport.getInspectionConclusion())){
//				incomingInspectionActionsReport.setInspectionState(IncomingInspectionActionsReport.INPECTION_STATE_AUDIT);
//				incomingInspectionActionsReport.setProcessingReceiveQty(incomingInspectionActionsReport.getStockAmount());
//				incomingInspectionActionsReport.setProcessingBadQty(0.0d);
//				incomingInspectionActionsReport.setProcessingResult("合格");
//			}else{
				incomingInspectionActionsReport.setInspectionState(IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT);
//			}
		}
		incomingInspectionActionsReport.getInspectionState();
		incomingInspectionActionsReportDao.save(incomingInspectionActionsReport);
		if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
			if(incomingInspectionActionsReport.getProductStage().equals("LC")||incomingInspectionActionsReport.getProductStage().equals("量产")){
				checkSampleTransitionRule(incomingInspectionActionsReport);
			}				
		}else{
			checkSampleTransitionRule(incomingInspectionActionsReport);
		}		
		//更新文件附件的状态,不使用的文件,并设置新的文件为使用
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"), incomingInspectionActionsReport.getAttachmentFiles());
		//检验判定为合格并且提交时调用同步接口
		if(IncomingInspectionActionsReport.INPECTION_STATE_AUDIT.equals(incomingInspectionActionsReport.getInspectionState())){
			if("OK".equals(incomingInspectionActionsReport.getInspectionConclusion())
				||!IncomingInspectionActionsReport.FORM_TYPE_INPUT.equals(incomingInspectionActionsReport.getFormType())){
//				saveErpResult(incomingInspectionActionsReport);
			}
		}
	}
	
//	private void saveErpResult(IncomingInspectionActionsReport report){
//		//如果检验信息来源于ERP，则回写ERP，否则不回写
//		String inspcetionSource=report.getInspcetionSource();
//		if(!"addbyqis".equals(inspcetionSource)){
////			String message = incomingInspectionService.updateRequestCheckState(report);
////			if(StringUtils.isNotEmpty(message)){
////				throw new AmbFrameException("检验结果回写到信息系统失败," + message);
////			}
//		}
//	}
	
	
	public void testSaveErpResult(String shuliang){
		//如果检验信息来源于ERP，则回写ERP，否则不回写
//			String message = incomingInspectionService.testuUpdateRequestCheckState(shuliang);
		}
	
	public String serachTargetRule(String supplierCode,String checkBomCode,String businessUnitName,Date inspectionDate){
		String hql = "select s.targetRule from SampleTransferRecord s where s.companyId = ? and s.supplierCode = ? and s.checkBomCode = ? and s.auditState = ? and s.businessUnitName=? and s.auditTime < ? order by s.auditTime desc";
		List<?> list = incomingInspectionActionsReportDao.find(hql,ContextUtils.getCompanyId(),supplierCode,checkBomCode,SampleTransferRecord.AUDITSTATE_PASS,businessUnitName,inspectionDate);		
		String targetRule="正常";
		if(list.size()>0&&list.get(0)!=null){
			targetRule=list.get(0).toString();
		}
		return targetRule;
	}		
	/**
	 * 检查并生成转换规则
	 * @param checkItems
	 */
	private void checkSampleTransitionRule(IncomingInspectionActionsReport incomingInspectionActionsReport){
		if(SampleScheme.EXEMPTION_TYPE.equals(incomingInspectionActionsReport.getSampleSchemeType())){
			return;
		}
		List<SampleTransitionRule> sampleTransitionRules = sampleTransitionRuleManager.list();
		Map<String,SampleTransitionRule> flowWayMap = new HashMap<String, SampleTransitionRule>();
		for(SampleTransitionRule sampleTransitionRule : sampleTransitionRules){
			if(SampleTransitionRule.STATE_USE.equals(sampleTransitionRule.getState())){
				flowWayMap.put(sampleTransitionRule.getSourceRule() + "_" + sampleTransitionRule.getFlowWay(),sampleTransitionRule);
			}
		}
		//首先检查供应商,物料还有没有没有审核的
		String hql = "select count(s.id) from SampleTransferRecord s where s.companyId = ? and s.supplierCode = ? and s.checkBomCode = ? and s.auditState = ? and s.businessUnitName=? ";
		List<?> list = incomingInspectionActionsReportDao.find(hql,ContextUtils.getCompanyId(),incomingInspectionActionsReport.getSupplierCode(),incomingInspectionActionsReport.getCheckBomCode(),SampleTransferRecord.AUDITSTATE_NORMAL,incomingInspectionActionsReport.getProcessSection());
		if(Integer.valueOf(list.get(0).toString())>0){
			return;
		}
		//查询供应商，物料已审核方案的目前规则
		String targetRule=serachTargetRule(incomingInspectionActionsReport.getSupplierCode(), incomingInspectionActionsReport.getCheckBomCode(), incomingInspectionActionsReport.getProcessSection(),incomingInspectionActionsReport.getInspectionDate());
		SampleTransferRecord sampleTransferRecord = null;
		//如果判定结果为OK,则检查是否有机会放宽检验规则,flowWay为向下(down)
		if("OK".equals(incomingInspectionActionsReport.getInspectionConclusion())){
			SampleTransitionRule sampleTransitionRule = flowWayMap.get(incomingInspectionActionsReport.getSampleSchemeType()+"_down");
			if(targetRule!=null&&targetRule.equals("放宽")){
				return;
			}
			if(sampleTransitionRule != null){
				List<Object> params = new ArrayList<Object>();
				params.add(incomingInspectionActionsReport.getProcessSection());
				params.add(incomingInspectionActionsReport.getSupplierCode());
				params.add(incomingInspectionActionsReport.getCheckBomCode());
				params.add(SampleScheme.EXEMPTION_TYPE);
				hql = "select i.inspectionConclusion from IncomingInspectionActionsReport i where i.processSection = ? and i.supplierCode = ? and i.checkBomCode = ? and i.sampleSchemeType != ? ";			
				if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
					hql += " and ( i.productStage='量产' or i.productStage='LC') ";
				}
				if(incomingInspectionActionsReport.getSchemeStartDate() != null){
					hql += " and i.inspectionDate > ? ";
					params.add(incomingInspectionActionsReport.getSchemeStartDate());
				}
				hql += " and i.inspectionDate <= ? order by i.inspectionDate desc";
				params.add(incomingInspectionActionsReport.getInspectionDate());
				
				Query query = incomingInspectionActionsReportDao.createQuery(hql,params.toArray());
				query.setMaxResults(sampleTransitionRule.getAmount());
				List<?> conclusions = query.list();
				int total = 0;
				for(Object conclusion : conclusions){
					if("OK".equals(conclusion+"")){
						total++;
					}
				}
				if(total >= sampleTransitionRule.getAmount()){
					//添加转换规则,放松
					sampleTransferRecord = new SampleTransferRecord();
					sampleTransferRecord.setCompanyId(ContextUtils.getCompanyId());
					sampleTransferRecord.setCreator(ContextUtils.getUserName());
					sampleTransferRecord.setCreatedTime(new Date());
					sampleTransferRecord.setLastModifiedTime(sampleTransferRecord.getCreatedTime());
					sampleTransferRecord.setLastModifier(ContextUtils.getUserName());
					sampleTransferRecord.setBusinessUnitCode(incomingInspectionActionsReport.getBusinessUnitCode());
					sampleTransferRecord.setBusinessUnitName(incomingInspectionActionsReport.getProcessSection());
					sampleTransferRecord.setSupplierCode(incomingInspectionActionsReport.getSupplierCode());
					sampleTransferRecord.setSupplierName(incomingInspectionActionsReport.getSupplierName());
					sampleTransferRecord.setCheckBomCode(incomingInspectionActionsReport.getCheckBomCode());
					sampleTransferRecord.setCheckBomName(incomingInspectionActionsReport.getCheckBomName());
					sampleTransferRecord.setInspectionDate(incomingInspectionActionsReport.getInspectionDate());
					sampleTransferRecord.setSchemeStartDate(incomingInspectionActionsReport.getSchemeStartDate());
					sampleTransferRecord.setSourceRule(sampleTransitionRule.getSourceRule());
					sampleTransferRecord.setTargetRule(sampleTransitionRule.getTargetRule());
				}
			}
		}else{//为NG则检查是否需要加强检验规则
			SampleTransitionRule sampleTransitionRule = flowWayMap.get(incomingInspectionActionsReport.getSampleSchemeType()+"_up");
			if(targetRule!=null&&targetRule.equals("加严")){
				return;
			}
			if(sampleTransitionRule != null){
				List<Object> params = new ArrayList<Object>();
				params.add(incomingInspectionActionsReport.getProcessSection());
				params.add(incomingInspectionActionsReport.getSupplierCode());
				params.add(incomingInspectionActionsReport.getCheckBomCode());
				hql = "select i.inspectionConclusion from IncomingInspectionActionsReport i where i.processSection = ? and i.supplierCode = ? and i.checkBomCode = ? ";
				if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
					hql += " and ( i.productStage='量产' or i.productStage='LC') ";
				}
				if(incomingInspectionActionsReport.getSchemeStartDate()!=null){
					hql += " and i.inspectionDate > ? ";
					params.add(incomingInspectionActionsReport.getSchemeStartDate());
				}
				hql += " and i.inspectionDate <= ? order by i.inspectionDate desc";
				params.add(incomingInspectionActionsReport.getInspectionDate());
				
				Query query = incomingInspectionActionsReportDao.createQuery(hql,params.toArray());
				query.setMaxResults(sampleTransitionRule.getTotalRange());
				List<?> conclusions = query.list();
				int total = 0;
				for(Object conclusion : conclusions){
					if("NG".equals(conclusion+"")){
						total++;
					}
				}
				if(total >= sampleTransitionRule.getAmount()){
					//添加转换规则,加紧
					sampleTransferRecord = new SampleTransferRecord();
					sampleTransferRecord.setCompanyId(ContextUtils.getCompanyId());
					sampleTransferRecord.setCreator(ContextUtils.getUserName());
					sampleTransferRecord.setCreatedTime(new Date());
					sampleTransferRecord.setLastModifiedTime(sampleTransferRecord.getCreatedTime());
					sampleTransferRecord.setLastModifier(ContextUtils.getUserName());
					sampleTransferRecord.setBusinessUnitCode(incomingInspectionActionsReport.getBusinessUnitCode());
					sampleTransferRecord.setBusinessUnitName(incomingInspectionActionsReport.getProcessSection());
					sampleTransferRecord.setSupplierCode(incomingInspectionActionsReport.getSupplierCode());
					sampleTransferRecord.setSupplierName(incomingInspectionActionsReport.getSupplierName());
					sampleTransferRecord.setCheckBomCode(incomingInspectionActionsReport.getCheckBomCode());
					sampleTransferRecord.setCheckBomName(incomingInspectionActionsReport.getCheckBomName());
					sampleTransferRecord.setInspectionDate(incomingInspectionActionsReport.getInspectionDate());
					sampleTransferRecord.setSchemeStartDate(incomingInspectionActionsReport.getSchemeStartDate());
					sampleTransferRecord.setSourceRule(sampleTransitionRule.getSourceRule());
					sampleTransferRecord.setTargetRule(sampleTransitionRule.getTargetRule());
				}
			}
		}
		if(sampleTransferRecord != null){
			sampleTransferRecordManager.saveSampleTransferRecord(sampleTransferRecord);
		}
	}
	
	/**
	 * 检查并生成转换规则
	 * @param checkItems
	 */
	@SuppressWarnings("unused")
	private void checkSampleTransitionRule(List<CheckItem> checkItems){
		List<SampleTransitionRule> sampleTransitionRules = sampleTransitionRuleManager.list();
		Map<String,SampleTransitionRule> flowWayMap = new HashMap<String, SampleTransitionRule>();
		for(SampleTransitionRule sampleTransitionRule : sampleTransitionRules){
			if(SampleTransitionRule.STATE_USE.equals(sampleTransitionRule.getState())){
				flowWayMap.put(sampleTransitionRule.getSourceRule() + "_" + sampleTransitionRule.getFlowWay(),sampleTransitionRule);
			}
		}
		for(CheckItem checkItem : checkItems){
			//首先检查供应商,物料,检验项目还有没有没有审核的
			String hql = "select count(s.id) from SampleTransferRecord s where s.companyId = ? and s.supplierCode = ? and s.checkBomCode = ? and s.checkItemName = ? and s.auditState = ?";
			List<?> list = incomingInspectionActionsReportDao.find(hql,ContextUtils.getCompanyId(),checkItem.getSupplierCode(),checkItem.getCheckBomCode(),checkItem.getCheckItemName(),SampleTransferRecord.AUDITSTATE_NORMAL);
			if(Integer.valueOf(list.get(0).toString())>0){
				continue;
			}
			SampleTransferRecord sampleTransferRecord = null;
			//如果判定结果为OK,则检查是否有机会放宽检验规则,flowWay为向下(down)
			if("OK".equals(checkItem.getConclusion())){
				SampleTransitionRule sampleTransitionRule = flowWayMap.get(checkItem.getSampleSchemeType()+"_down");
				if(sampleTransitionRule != null){
					List<Object> params = new ArrayList<Object>();
					params.add(checkItem.getSupplierCode());
					params.add(checkItem.getCheckBomCode());
					params.add(checkItem.getCheckItemName());
					hql = "select c.conclusion from CheckItem c where c.supplierCode = ? and c.checkBomCode = ? and c.checkItemName = ?";
					if(checkItem.getSchemeStartDate() != null){
						hql += " and c.inspectionDate > ? ";
						params.add(checkItem.getSchemeStartDate());
					}
					hql += " and c.inspectionDate <= ? order by c.inspectionDate desc";
					params.add(checkItem.getInspectionDate());
					
					Query query = incomingInspectionActionsReportDao.createQuery(hql,params.toArray());
					query.setMaxResults(sampleTransitionRule.getAmount());
					List<?> conclusions = query.list();
					int total = 0;
					for(Object conclusion : conclusions){
						if("OK".equals(conclusion+"")){
							total++;
						}
					}
					if(total >= sampleTransitionRule.getAmount()){
						//添加转换规则,放松
						sampleTransferRecord = new SampleTransferRecord();
						sampleTransferRecord.setCompanyId(ContextUtils.getCompanyId());
						sampleTransferRecord.setCreator(ContextUtils.getUserName());
						sampleTransferRecord.setCreatedTime(new Date());
						sampleTransferRecord.setLastModifiedTime(sampleTransferRecord.getCreatedTime());
						sampleTransferRecord.setLastModifier(ContextUtils.getUserName());
						sampleTransferRecord.setSupplierCode(checkItem.getSupplierCode());
						sampleTransferRecord.setSupplierName(checkItem.getSupplierName());
						sampleTransferRecord.setCheckBomCode(checkItem.getCheckBomCode());
						sampleTransferRecord.setCheckBomName(checkItem.getCheckBomName());
						sampleTransferRecord.setCheckItemName(checkItem.getCheckItemName());
						sampleTransferRecord.setSourceRule(sampleTransitionRule.getSourceRule());
						sampleTransferRecord.setTargetRule(sampleTransitionRule.getTargetRule());
					}
				}
			//为NG则检查是否需要加强检验规则
			}else{
				SampleTransitionRule sampleTransitionRule = flowWayMap.get(checkItem.getSampleSchemeType()+"_up");
				if(sampleTransitionRule != null){
					List<Object> params = new ArrayList<Object>();
					params.add(checkItem.getSupplierCode());
					params.add(checkItem.getCheckBomCode());
					params.add(checkItem.getCheckItemName());
					hql = "select c.conclusion from CheckItem c where c.supplierCode = ? and c.checkBomCode = ? and c.checkItemName = ?";
					if(checkItem.getSchemeStartDate()!=null){
						hql += " and c.inspectionDate > ? ";
						params.add(checkItem.getSchemeStartDate());
					}
					hql += " and c.inspectionDate <= ? order by c.inspectionDate desc";
					params.add(checkItem.getInspectionDate());
					
					Query query = incomingInspectionActionsReportDao.createQuery(hql,params.toArray());
					query.setMaxResults(sampleTransitionRule.getTotalRange());
					List<?> conclusions = query.list();
					int total = 0;
					for(Object conclusion : conclusions){
						if("NG".equals(conclusion+"")){
							total++;
						}
					}
					if(total >= sampleTransitionRule.getAmount()){
						//添加转换规则,加紧
						sampleTransferRecord = new SampleTransferRecord();
						sampleTransferRecord.setCompanyId(ContextUtils.getCompanyId());
						sampleTransferRecord.setCreator(ContextUtils.getUserName());
						sampleTransferRecord.setCreatedTime(new Date());
						sampleTransferRecord.setLastModifiedTime(sampleTransferRecord.getCreatedTime());
						sampleTransferRecord.setLastModifier(ContextUtils.getUserName());
						sampleTransferRecord.setSupplierCode(checkItem.getSupplierCode());
						sampleTransferRecord.setSupplierName(checkItem.getSupplierName());
						sampleTransferRecord.setCheckBomCode(checkItem.getCheckBomCode());
						sampleTransferRecord.setCheckBomName(checkItem.getCheckBomName());
						sampleTransferRecord.setCheckItemName(checkItem.getCheckItemName());
						sampleTransferRecord.setSourceRule(sampleTransitionRule.getSourceRule());
						sampleTransferRecord.setTargetRule(sampleTransitionRule.getTargetRule());
					}
				}
			}
			if(sampleTransferRecord != null){
				sampleTransferRecordManager.saveSampleTransferRecord(sampleTransferRecord);
			}
		}
	}
	
	/**
	  * 方法名: 不良数量
	  * <p>功能说明：</p>
	  * @param id 
	  * @param processingResult 处置方式
	  * @param receiveQty 接收数量
	  * @param badQty 拒收数量
	  * @param cutRate 折价率
	  * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public void auditIncomingInspectionActionsReport(Long id,String auditText,String lastAuditText,
				Double receiveQty,Double badQty,Double cutRate) throws Exception{
		IncomingInspectionActionsReport report = incomingInspectionActionsReportDao.get(id);
		if(report != null){
			if(!IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT.equals(report.getInspectionState())&&!IncomingInspectionActionsReport.INPECTION_STATE_LAST_SUBMIT.equals(report.getInspectionState())){
				throw new AmbFrameException("只能审核状态为【"+IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT+"】的检验单!");
			}
			//调用接口回写检验结果
			
			String isLast=Struts2Utils.getParameter("islast");
			if(isLast!=null&&!"".equals(isLast)){
				report.setLastStateText(lastAuditText);
				report.setLastStateTime(new Date());
				report.setInspectionState(IncomingInspectionActionsReport.INPECTION_STATE_AUDIT);
			}else{
				report.setAuditText(auditText);
				report.setAuditTime(new Date());
				report.setInspectionState(IncomingInspectionActionsReport.INPECTION_STATE_LAST_SUBMIT);
//				ApiFactory.getPortalService().addMessage("iqc", "systemAdmin", "ofilm.systemAdmin", report.getLastStateLoginMan(), "进货检验管理", report.getCheckBomCode()+"检验单需要审核", "/inspection-report/input.htm?id="+report.getId());
				
			}
			incomingInspectionActionsReportDao.save(report);
			if(report.getAcceptNo()!=null){
				String[] acceptNoArr = report.getAcceptNo().split("/");
				for(int i=0;i<acceptNoArr.length;i++){
					String sql1 = "select * from apps.XXQIS_IQC_result_t where line_id=?";
					List<?> resultList = jdbcTemplate.queryForList(sql1,new Object[]{Long.valueOf(acceptNoArr[i])});
					String sql = "";
					if(resultList.size()>0){
						sql = "update apps.XXQIS_IQC_result_t set CHECKRESULT=? ,CREATE_DATE=? where line_id=? ";
						jdbcTemplate.update(sql, new Object[]{report.getInspectionConclusion(),new Date(), Long.valueOf(acceptNoArr[i])});
					}else{
						sql = "insert into apps.XXQIS_IQC_result_t (line_id,CHECKRESULT,FLAG,CREATE_DATE) values("+Long.valueOf(acceptNoArr[i])+",'"+report.getInspectionConclusion()+"','',sysdate)";
						jdbcTemplate.execute(sql);
					}
				}
			}
			
//			saveErpResult(report);
		}
	}
	
	public String deleteIncomingInspectionActionsReport(String deleteIds){
		String[] ids = deleteIds.split(",");
		StringBuilder sb = new StringBuilder("");
		for(String id: ids){
			IncomingInspectionActionsReport report = incomingInspectionActionsReportDao.get(Long.valueOf(id));
			if(report != null){
				incomingInspectionActionsReportDao.delete(report);
				sb.append(report.getRequestCheckNo() + "|" + report.getEntryId() + ",");
			}
		}
		return sb.toString();
	}
	
//	public void deleteIncomingInspectionActionsReport(IncomingInspectionActionsReport incomingInspectionActionsReport){
//		if(StringUtils.isNotEmpty(incomingInspectionActionsReport.getRequestCheckNo())&&StringUtils.isNotEmpty(incomingInspectionActionsReport.getEntryId())){
//			throw new AmbFrameException("不能删除根据erp请检单创建的来料检验单!");
//		}
//		//日志内容
//		logUtilDao.debugLog("删除", incomingInspectionActionsReport.toString());
//		incomingInspectionActionsReportDao.delete(incomingInspectionActionsReport);
//	}
	
	public void deleteErpRequestIncomingInspectionActionsReport(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			IncomingInspectionActionsReport report = incomingInspectionActionsReportDao.get(Long.valueOf(id));
			if(report != null){
				incomingInspectionActionsReportDao.delete(report);
			}
		}
	}
	//查询隐藏数据
	public Page<IncomingInspectionActionsReport> listHid(Page<IncomingInspectionActionsReport>page){
		return incomingInspectionActionsReportDao.listHid(page);
	}
	
	public Page<IncomingInspectionActionsReport> list(Page<IncomingInspectionActionsReport>page,String checkItem){
		return incomingInspectionActionsReportDao.list(page, checkItem);
	}
	
	public Page<IncomingInspectionActionsReport> listUnCheck(Page<IncomingInspectionActionsReport>page,String checkItem){
		return incomingInspectionActionsReportDao.listUnCheck(page, checkItem);
	}
	public Page<IncomingInspectionActionsReport> listReCheck(Page<IncomingInspectionActionsReport>page,String checkItem){
		return incomingInspectionActionsReportDao.listReCheck(page, checkItem);
	}
	public Page<IncomingInspectionActionsReport> listChecked(Page<IncomingInspectionActionsReport>page,String checkItem){
		return incomingInspectionActionsReportDao.listChecked(page, checkItem);
	}
	public Page<IncomingInspectionActionsReport> listWaitAudit(Page<IncomingInspectionActionsReport>page,String checkItem){
		return incomingInspectionActionsReportDao.listWaitAudit(page,checkItem);
	}
	public Page<IncomingInspectionActionsReport> listLstWaitAudit(Page<IncomingInspectionActionsReport>page,String checkItem){
		return incomingInspectionActionsReportDao.listLastWaitAudit(page,checkItem);
	}
	public Page<IncomingInspectionActionsReport> listComplete(Page<IncomingInspectionActionsReport>page,String checkItem){
		return incomingInspectionActionsReportDao.listComplete(page, checkItem);
	}
	public Page<IncomingInspectionActionsReport> listDiscards(Page<IncomingInspectionActionsReport>page){
		return incomingInspectionActionsReportDao.listDiscards(page);
	}
	
	public Page<IncomingInspectionActionsReport> unlist(Page<IncomingInspectionActionsReport>page){
		return incomingInspectionActionsReportDao.unlist(page);
	}
	public Page<IncomingInspectionActionsReport> oklist(Page<IncomingInspectionActionsReport>page){
		return incomingInspectionActionsReportDao.oklist(page);
	}
	/**
	  * 方法名: 快速检索查询
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<IncomingInspectionActionsReport> quickSelectList(Page<IncomingInspectionActionsReport>page){
		return incomingInspectionActionsReportDao.quickSelectList(page);
	}
	
	public List<IncomingInspectionActionsReport> listAll(){
		return incomingInspectionActionsReportDao.getAllIncomingInspectionActionsReport();
	}
	public List<IncomingInspectionActionsReport> listAllSuplier(){
		return incomingInspectionActionsReportDao.getAllSupplier();
	}
	public List<IncomingInspectionActionsReport> listAllSuplierName(){
		return incomingInspectionActionsReportDao.getAllSupplierName();
	}
	public List<IncomingInspectionActionsReport> listSupplierAndCode(){
		return incomingInspectionActionsReportDao.getSupplierAndCode();
	}	
	public Object supplierRateChartDatas(JSONObject params){
		String materiel="";
		String dutySupplier="";
		if(params.containsKey("materielCode")){
			 materiel=params.get("materielCode").toString();
		}
		if(params.containsKey("dutySupplier")){
			 dutySupplier=params.get("dutySupplier").toString();
			 dutySupplier = "'" + dutySupplier.replaceAll(",","','") + "'";
		}
		Date startDate=new Date();
		Date endDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(params.get("endDate_le_date")!=null){
			startDate=DateUtil.parseDate(params.getString("startDate_ge_date"));
			endDate=DateUtil.parseDate(params.getString("endDate_le_date"));
		}
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","supplierCode");
		modelJson.put("label","供应商名称");
		modelJson.put("index","supplierCode");
		modelJson.put("width","250");
		colModels.add(modelJson);
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","inspection");
		modelJson1.put("label","检查批数");
		modelJson1.put("index","inspection");
		colModels.add(modelJson1);
		JSONObject modelJson2 = new JSONObject();
		modelJson2.put("name","quality");
		modelJson2.put("label","合格批数");
		modelJson2.put("index","quality");
		colModels.add(modelJson2);
		JSONObject modelJson3 = new JSONObject();
		modelJson3.put("name","rate");
		modelJson3.put("label","合格率");
		modelJson3.put("index","rate");
		colModels.add(modelJson3);

		
		result.put("title", "供应商合格率对比图");
		result.put("subtitle","(" + sdf.format(startDate) + "-" + sdf.format(endDate) + ")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Integer> data = new ArrayList<Integer>();
		List<Map<String,Object>> data5 = new ArrayList<Map<String,Object>>();
		String hql = "select i.supplierName,count(*) from IncomingInspectionActionsReport i where i.companyId = ? and i.inspectionDate between ? and ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(StringUtils.isNotEmpty(materiel)){
			hql += " and i.checkBomCode = ?";
			searchParams.add(materiel);
		}
		if(StringUtils.isNotEmpty(dutySupplier)){
			hql += " and i.supplierName in (" + dutySupplier + ")";
		}
		String okHql = hql + " and i.inspectionConclusion = 'OK' group by i.supplierName";
		hql += " group by i.supplierName";
		List<Object> allList = incomingInspectionActionsReportDao.find(hql,searchParams.toArray());
		List<Object> okList = incomingInspectionActionsReportDao.find(okHql,searchParams.toArray());
		Map<String,Object[]> okMaps = new HashMap<String, Object[]>();
		for(Object obj : okList){
			Object[] objs = (Object[])obj;
			okMaps.put(objs[0]+"",objs);
		}
		List<Map<String,Object>> rateList = new ArrayList<Map<String,Object>>();
		int i=0;
		for(Object obj : allList){
			Object[] objs = (Object[])obj;
			Object[] okObjs = okMaps.get(objs[0]+"");
			Integer inspectionAmount = 0,okAmount = 0;
			if(objs[0] != null){
				inspectionAmount = Integer.valueOf(objs[1].toString());
			}
			if(okObjs != null && okObjs[1] != null){
				okAmount = Integer.valueOf(okObjs[1].toString());
			}
			double rate=0;
			if(inspectionAmount>0){
				rate=(okAmount*1.0/inspectionAmount)*100;
			}
			Map<String,Object> rateMap = new HashMap<String, Object>();
			rateMap.put("index",i++);
			rateMap.put("supplierName",objs[0]+"");
			rateMap.put("inspectionAmount",inspectionAmount);
			rateMap.put("okAmount",okAmount);
			rateMap.put("rate",rate);
			rateList.add(rateMap);
		}
		Collections.sort(rateList,new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if((Double)o1.get("rate") < (Double)o2.get("rate")){
					return 0;
				}else{
					return 1;
				}
			}
		});
		List<Integer> data2 = new ArrayList<Integer>();
		List<Map<String,Object>> data6 = new ArrayList<Map<String,Object>>();
		List<Double> data3 = new ArrayList<Double>();
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		DecimalFormat  df=new DecimalFormat("0.00");
		int count = 0;
		for(Map<String,Object> rateMap : rateList){
			Map<String,Object> map = new HashMap<String, Object>();
			String supplierName = rateMap.get("supplierName")+"";
			//横坐标天数的参数
			categories.add(supplierName);
			map.put("dutySupplier", supplierName);
			map.put("name","检查批数");
			map.put("y",rateMap.get("inspectionAmount"));
			data5.add(map);
			data.add((Integer) rateMap.get("inspectionAmount"));
			//合格批数
			Map<String,Object> okMap = new HashMap<String, Object>();
			okMap.put("dutySupplier", supplierName);
			okMap.put("name","合格批数");
			okMap.put("in","合格批数");
			okMap.put("y", rateMap.get("okAmount"));
			data6.add(okMap);
			data2.add((Integer) rateMap.get("okAmount"));
			//批次合格率
			Double rate = (Double) rateMap.get("rate");
			data3.add(rate);
			//表格数据
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("supplierCode",supplierName);
			dataMap.put("inspection",rateMap.get("inspectionAmount"));
			dataMap.put("quality",rateMap.get("okAmount"));
			dataMap.put("rate", df.format(rate));
			tabledata.add(dataMap);
			count++;
			if(count==15){
				break;
			}
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		series1.put("name", "检验批数");
		series1.put("data",data5);
		result.put("series1", series1);
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		series2.put("data",data6);
		result.put("series2", series2);
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		series3.put("data",data3);
		result.put("series3", series3);

		result.put("max", 100);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		return result;
	}
	public Object materialRateChartDatas(JSONObject params){
		String materiel="";
		String dutySupplier="";
		if(params.containsKey("materiel")){
			materiel="'" + params.getString("materiel").replaceAll(",","','")+"'";
		}
		if(params.containsKey("dutySupplierCode")){
			dutySupplier=params.get("dutySupplierCode").toString();
		}
//		if(!dutySupplier.equals("")){
//			if(!materiel.equals("")){
//				String[] materielArray=materiel.split(",");
//				for(int i=0;i<materielArray.length;i++){
//					materiellist.add(materielArray[i]);
//				}
//			}else{
//				//供应商所提供的物料
//				materiellist=incomingInspectionActionsReportManager.getAllMaterialBySupplier(dutySupplier);
//			}
//		}else{
//			if(!materiel.equals("")){
//				String[] materielArray=materiel.split(",");
//				for(int i=0;i<materielArray.length;i++){
//					materiellist.add(materielArray[i]);
//				}
//			}else{
//				materiellist=incomingInspectionActionsReportManager.listAllMaterial();
//			}
//		}
	
		Date startDate=new Date();
		Date endDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(params.get("endDate_le_date")!=null){
			startDate=DateUtil.parseDate(params.getString("startDate_ge_date"));
			endDate=DateUtil.parseDate(params.getString("endDate_le_date"));
		}
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","checkBomCode");
		modelJson.put("label","物料");
		modelJson.put("index","checkBomCode");
		colModels.add(modelJson);
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","inspection");
		modelJson1.put("label","检查批数");
		modelJson1.put("index","inspection");
		colModels.add(modelJson1);
		JSONObject modelJson2 = new JSONObject();
		modelJson2.put("name","quality");
		modelJson2.put("label","合格批数");
		modelJson2.put("index","quality");
		colModels.add(modelJson2);
		JSONObject modelJson3 = new JSONObject();
		modelJson3.put("name","rate");
		modelJson3.put("label","合格率");
		modelJson3.put("index","rate");
		colModels.add(modelJson3);
		
		result.put("title", "物料合格率对比图");
		result.put("subtitle","(" + sdf.format(startDate) + "-" + sdf.format(endDate) + ")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		//查询语
		String hql = "select i.checkBomName,count(*) from IncomingInspectionActionsReport i where i.companyId = ? and i.inspectionDate between ? and ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(StringUtils.isNotEmpty(dutySupplier)){
			hql += " and i.supplierCode = ?";
			searchParams.add(dutySupplier);
		}
		if(StringUtils.isNotEmpty(materiel)){
			hql += " and i.checkBomCode in (" + materiel + ")";
		}
		String okHql = hql + " and i.inspectionConclusion = 'OK' group by i.checkBomName";
		hql += " group by i.checkBomName";
		List<Object> allList = incomingInspectionActionsReportDao.find(hql,searchParams.toArray());
		List<Object> okList = incomingInspectionActionsReportDao.find(okHql,searchParams.toArray());
		Map<String,Object[]> okMaps = new HashMap<String, Object[]>();
		for(Object obj : okList){
			Object[] objs = (Object[])obj;
			okMaps.put(objs[0]+"",objs);
		}
		List<Map<String,Object>> rateList = new ArrayList<Map<String,Object>>();
		int i=0;
		for(Object obj : allList){
			Object[] objs = (Object[])obj;
			Object[] okObjs = okMaps.get(objs[0]+"");
			Integer inspectionAmount = 0,okAmount = 0;
			if(objs[0] != null){
				inspectionAmount = Integer.valueOf(objs[1].toString());
			}
			if(okObjs != null && okObjs[1] != null){
				okAmount = Integer.valueOf(okObjs[1].toString());
			}
			double rate=0;
			if(inspectionAmount>0){
				rate=(okAmount*1.0/inspectionAmount)*100;
			}
			Map<String,Object> rateMap = new HashMap<String, Object>();
			rateMap.put("index",i++);
			rateMap.put("checkBomName",objs[0]+"");
			rateMap.put("inspectionAmount",inspectionAmount);
			rateMap.put("okAmount",okAmount);
			rateMap.put("rate",rate);
			rateList.add(rateMap);
		}
		Collections.sort(rateList,new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if((Double)o1.get("rate") < (Double)o2.get("rate")){
					return 0;
				}else{
					return 1;
				}
			}
		});
		
		List<Integer> data = new ArrayList<Integer>();
		List<Map<String,Object>> data5 = new ArrayList<Map<String,Object>>();
		
		List<Map<String,Object>> data6 = new ArrayList<Map<String,Object>>();
		List<Integer> data2 = new ArrayList<Integer>();
		
		List<Double> data3 = new ArrayList<Double>();
		DecimalFormat  df=new DecimalFormat("0.00");
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		int count = 0;
		for(Map<String,Object> rateMap : rateList){
			Map<String,Object> map = new HashMap<String, Object>();
			String checkBomName = rateMap.get("checkBomName")+"";
			//横坐标天数的参数
			categories.add(checkBomName);
			map.put("materiel", checkBomName);
			map.put("name","检查批数");
			map.put("y",rateMap.get("inspectionAmount"));
			data5.add(map);
			data.add((Integer) rateMap.get("inspectionAmount"));
			//合格批数
			Map<String,Object> okMap = new HashMap<String, Object>();
			okMap.put("materiel", checkBomName);
			okMap.put("name","合格批数");
			okMap.put("in","合格批数");
			okMap.put("y", rateMap.get("okAmount"));
			data6.add(okMap);
			data2.add((Integer) rateMap.get("okAmount"));
			//批次合格率
			Double rate = (Double) rateMap.get("rate");
			data3.add(rate);
			//表格数据
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("checkBomCode",checkBomName);
			dataMap.put("inspection",rateMap.get("inspectionAmount"));
			dataMap.put("quality",rateMap.get("okAmount"));
			dataMap.put("rate",df.format(rate));
			tabledata.add(dataMap);
			count++;
			if(count==15){
				break;
			}
		}
		
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		series1.put("data",data5);
		result.put("series1", series1);
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		series2.put("data",data6);
		result.put("series2", series2);
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		series3.put("data",data3);
		result.put("series3", series3);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		result.put("max", 100);
		return result;
	}
	public List<IncomingInspectionActionsReport> listAllMaterial(){
		return incomingInspectionActionsReportDao.getAllMaterial();
	}
	public List<IncomingInspectionActionsReport> getAllMaterialBySupplier(String dutySupplier){
		return incomingInspectionActionsReportDao.getAllMaterialBySupplier(dutySupplier);
	}
	public List<IncomingInspectionActionsReport> getAllSupplierNameByMaterial(String material){
		return incomingInspectionActionsReportDao.getAllSupplierByMaterial(material);
	}
	public List<IncomingInspectionActionsReport> getAllSupplierByMaterial(String material){
		return incomingInspectionActionsReportDao.getAllSupplierByMaterial(material);
	}
	public Page<IncomingInspectionActionsReport> unsearch(Page<IncomingInspectionActionsReport> page){
		return incomingInspectionActionsReportDao.unsearch(page);
	}
	public List<IncomingInspectionActionsReport> listAll(Date startDate,Date endDate,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		return incomingInspectionActionsReportDao.getAllIncomingInspectionActionsReport(startDate,endDate,materiel,dutySupplier,checkBomMaterialType,importance);
	}
	public List<IncomingInspectionActionsReport> listAllByName(Date startDate,Date endDate,String materiel,String dutySupplierName,String checkBomMaterialType,String importance){
		return incomingInspectionActionsReportDao.getAllIncomingInspectionActionsReportBySupplierName(startDate,endDate,materiel,dutySupplierName,checkBomMaterialType,importance);
	}
	public Page<IncomingInspectionActionsReport> listAll(Page<IncomingInspectionActionsReport> page,Date startDate,Date endDate,String materiel,String dutySupplier){
		return incomingInspectionActionsReportDao.getAllIncomingInspectionActionsReport(page,startDate,endDate,materiel,dutySupplier,"","");
	}
	public List<Object> getAllCheckType(Date startDate,Date endDate){
		return incomingInspectionActionsReportDao.getAllCheckType(startDate, endDate);
	}
	public List<IncomingInspectionActionsReport> listAll(Date startDate,Date endDate){
		return incomingInspectionActionsReportDao.getAllIncomingInspectionActionsReport(startDate,endDate);
	}
	//用来查找一段时间内供应商物料连续几批不合格
	public List<IncomingInspectionActionsReport> listAllByOrder(Date startDate,Date endDate,String supplierName,String checkBomName){
		return incomingInspectionActionsReportDao.getAllIncomingInspectionActionsReportByOrder(startDate,endDate,supplierName,checkBomName);
	}
	//查找一段时间内供应商物料同一检验项目的不合格报告
	public List<IncomingInspectionActionsReport> listAllByItems(Date startDate,Date endDate,String supplierName,String checkBomName,String checkItemName){
		return incomingInspectionActionsReportDao.getAllIncomingInspectionActionsReportByItems(startDate,endDate,supplierName,checkBomName,checkItemName);
	}
	public Page<IncomingInspectionActionsReport> listQualified(Page<IncomingInspectionActionsReport>page,Date startDate,Date endDate,String state,String materiel,String dutySupplier){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReport(page,startDate,endDate,state,materiel,dutySupplier,"","");
	}
	public List<IncomingInspectionActionsReport> listQualified(Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReport(startDate,endDate,state,materiel,dutySupplier,checkBomMaterialType,importance);
	}
	public List<IncomingInspectionActionsReport> listQualifiedByName(Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReportBySupplierName(startDate,endDate,state,materiel,dutySupplier,checkBomMaterialType,importance);
	}
	public List<IncomingInspectionActionsReport> listQualified(Date startDate,Date endDate,String state,String materiel,String dutySupplier,String checkBomMaterialType,String importance,String classification){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReport(startDate,endDate,state,materiel,dutySupplier,checkBomMaterialType,importance,classification);
	}
	public List<IncomingInspectionActionsReport> listQualifiedByProcessingResult(Date startDate,Date endDate,String state,String processingResult){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReportByResult(startDate,endDate,state,processingResult);
	}
	public List<IncomingInspectionActionsReport> listQualifiedByProcessingResult(Date startDate,Date endDate,String state,String processingResult,String classification){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReportByResult(startDate,endDate,state,processingResult,classification);
	}
	public List<IncomingInspectionActionsReport> listQualifiedByInspector(Date startDate,Date endDate,String state,String inspector){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReportByInspector(startDate,endDate,state,inspector);
	}
	public List<IncomingInspectionActionsReport> listQualifiedByInspector(Date startDate,Date endDate,String state,String inspector,String classification){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReportByInspector(startDate,endDate,state,inspector,classification);
	}
	public List<IncomingInspectionActionsReport> listQualified(Date startDate,Date endDate,String classification,String state){
		return incomingInspectionActionsReportDao.getQualifiedIncomingInspectionActionsReport(startDate,endDate,classification,state);
	}
	public List<IncomingInspectionActionsReport> listByInspectionNo(String inspectionNo){
		return incomingInspectionActionsReportDao.getIncomingInspectionActionsReportByInspectionNo(inspectionNo);
	}
	
	public void saveItemOrder(Integer originalIndex, Integer newIndex) {
		incomingInspectionActionsReportDao.updateIndex(originalIndex, Integer.MAX_VALUE);
        if (originalIndex < newIndex) {// 从上往下移动 两者之间的displayIndex要自减
        	incomingInspectionActionsReportDao.decreaseIndex(originalIndex,newIndex);
        } else {// 从下往上移动 两者之间的displayIndex要自增
        	incomingInspectionActionsReportDao.increaseIndex(newIndex,originalIndex);
        }
        incomingInspectionActionsReportDao.updateIndex(Integer.MAX_VALUE, newIndex);
    }

	public void processingResult(String deleteIds){
		String result = Struts2Utils.getParameter("result");
		String[] ids = deleteIds.split(",");
		
		for(String id: ids){
			IncomingInspectionActionsReport report = incomingInspectionActionsReportDao.get(Long.valueOf(id));
			if(report != null){
//				if(!"已审核".equals(report.getInspectionState())){
//					throw new AmbFrameException("只能处理状态为【已审核】的检验数据!");
//				}
				if(!"NG".equals(report.getInspectionConclusion())){
					throw new AmbFrameException("只能检验判定为【不合格】的检验数据!");
				}
				report.setProcessingResult(result);
				report.setProcessingMan(ContextUtils.getUserName());
				report.setProcessingDate(new Date());
				incomingInspectionActionsReportDao.save(report);
			}
		}
	}
	
	
	public String importReportExcel(File file) throws Exception{
			InputStream inputStream=null;
			inputStream = new FileInputStream(file);
			StringBuffer sb = new StringBuffer("");
			Workbook book = WorkbookFactory.create(inputStream);
			int totalSheets = book.getNumberOfSheets();
			try{
			for(int k=0;k<totalSheets;k++){
				Sheet sheet = book.getSheetAt(k);
				Row row = sheet.getRow(0);
				if(row == null){
					continue;
				}
				//检验单编号
				String inspetionNo = (ExcelUtil.getCellValue(sheet,"a1")+"").trim();
				if(StringUtils.isEmpty(inspetionNo)){
					throw new AmbFrameException("SHEET"+k+"<font color=red>EXCEL格式不正确!</font>&nbsp;&nbsp;</br>");
				}
				String supplierName = (ExcelUtil.getCellValue(sheet,"b1")+"").trim();
				if(StringUtils.isEmpty(supplierName)){
					throw new AmbFrameException("SHEET"+k+"<font color=red>EXCEL格式不正确!</font>&nbsp;&nbsp;</br>");
				}
				String bomCode = (ExcelUtil.getCellValue(sheet,"c1")+"").trim();
				if(StringUtils.isEmpty(bomCode)){
					throw new AmbFrameException("SHEET"+k+"<font color=red>EXCEL格式不正确!</font>&nbsp;&nbsp;</br>");
				}
				String bussinessUnitName = (ExcelUtil.getCellValue(sheet,"d1")+"").trim();
				if(StringUtils.isEmpty(bussinessUnitName)){
					throw new AmbFrameException("SHEET"+k+"<font color=red>EXCEL格式不正确!</font>&nbsp;&nbsp;</br>");
				}
				row = sheet.getRow(2);//列头
				Iterator<Row> rows = sheet.rowIterator();
				row = rows.next();
				int orderNum = 0;
				while(rows.hasNext()){
					orderNum++;
					row = rows.next();
					Cell cell = row.getCell(0);
					inspetionNo = (ExcelUtil.getCellValue(cell)+"").trim();
					if(inspetionNo==null){
						throw new AmbFrameException("EXCEL文件中的第【"+orderNum+"】，进货检验报告编号不能为空值。");
					}
					IncomingInspectionActionsReport iqcReport =this.getIncomingInspectionActionsReportByInspectionNo(inspetionNo);
					if(iqcReport==null){
						 throw new AmbFrameException("EXCEL文件中的第【"+orderNum+"】，不存在报告编号为【"+inspetionNo+"】的检验记录。");
					}else{
						incomingInspectionActionsReportDao.delete(iqcReport);
					}
				}
				sb.append("导入的报告删除成功!<br/>");
			}
			return sb.toString();
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
			file.delete();
		}
	}
	
	
	/**
	 * 方法名:重新检验 
	  * <p>功能说明：</p>
	  * @param deleteIds
	 */
	public void reCheck(String idStr,String recheckText){
		IncomingInspectionActionsReport report = incomingInspectionActionsReportDao.get(Long.valueOf(idStr));
		if(report != null){
//			if(IncomingInspectionActionsReport.INPECTION_STATE_AUDIT.equals(report.getInspectionState())){
//				String resultStr = incomingInspectionService.checkUpdateGodownEntryResult(report);
//				if(StringUtils.isNotEmpty(resultStr)){
//					throw new AmbFrameException(resultStr);
//				}
//			}
			
			report.setInspectionState(IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK);
			report.setProcessingResult(null);
			report.setApprovedBy(null);
			report.setApprovedTime(null);
			report.setProcessingBadQty(null);
			report.setProcessingReceiveQty(null);
			report.setRecheckText(recheckText);
			report.setRecheckTime(new Date());
			incomingInspectionActionsReportDao.save(report);
		}
	}
	
	public Integer getYearAndMonthNumber(Calendar calendar){
		StringBuffer sb = new StringBuffer("");
		sb.append(calendar.get(Calendar.YEAR));
		if(calendar.get(Calendar.MONTH)+1<10){
			sb.append("0" + (calendar.get(Calendar.MONTH)+1));
		}else{
			sb.append(calendar.get(Calendar.MONTH)+1);
		}
		return Integer.valueOf(sb.toString());
	}
	
	public Integer getYearAndWeekNumber(Calendar calendar){
		StringBuffer sb = new StringBuffer("");
		sb.append(calendar.get(Calendar.YEAR));
		if(calendar.get(Calendar.WEEK_OF_YEAR)<10){
			sb.append("0"+calendar.get(Calendar.WEEK_OF_YEAR));
		}else{
			sb.append(calendar.get(Calendar.WEEK_OF_YEAR));
		}
		return Integer.valueOf(sb.toString());
	}
	
	/**
	 * 进货检验一次合格率按日期统计
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getOneChartDatas(JSONObject params) throws Exception{
		Date startDate=DateUtil.parseDate(params.getString("startDate_ge_date"));
		Date endDate=DateUtil.parseDate(params.getString("endDate_le_date"));
		//获取天数
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(startDate);
		endCal.setTime(endDate);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("title", "进货检验一次合格率");
		result.put("subtitle","(" + params.getString("startDate_ge_date") + "-" + params.getString("endDate_le_date") + ")");
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		
		StringBuilder hql = new StringBuilder("select yearMonthAndDate,count(*) from IncomingInspectionActionsReport i where i.companyId = ? and i.yearMonthAndDate between ? and ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(Integer.valueOf(params.getString("startDate_ge_date").replaceAll("-","")));
		searchParams.add(Integer.valueOf(params.getString("endDate_le_date").replaceAll("-","")));
		String oneMaterials="";
		String secondMaterials="";
		String threeMaterials="";
		for(Object key : params.keySet()){
			if("checkBomMaterialLevels".equals(key)){
				String [] leveIds=params.get(key).toString().split(",");
				for(String leveId:leveIds){
					String hqlLevel=" from MaterielTypeLevel leve where leve.id=? and leve.companyId=?";
					List<MaterielTypeLevel> levels=incomingInspectionActionsReportDao.find(hqlLevel,Long.valueOf(leveId),ContextUtils.getCompanyId());
					if(!levels.isEmpty()){
						MaterielTypeLevel level=levels.get(0);
						String levelName=level.getMaterielTypeName();
						if(level.getMaterielLevel()==1){
							oneMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==2){
							secondMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==3){
							threeMaterials+=levelName+",";
						}
					}
				}
				break;
			}
		}
		CommonUtil1.buildHql(params, hql, searchParams,getFieldMap(),"i");
		//全部
		List<?> list = incomingInspectionActionsReportDao.find(hql + " group by yearMonthAndDate",searchParams.toArray());
		Map<String,Integer> allMap = new HashMap<String, Integer>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null){
				continue;
			}
			allMap.put(objs[0].toString(),Integer.valueOf(objs[1].toString()));
		}
		//合格
		hql.append(" and i.inspectionConclusion = ?");
		searchParams.add("OK");
		//全部
		if(!"".equals(oneMaterials)){
			oneMaterials=oneMaterials.substring(0, oneMaterials.length()-1);
			hql.append(" and i.oneLevelMateriel in ('"+oneMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(secondMaterials)){
			secondMaterials=secondMaterials.substring(0, secondMaterials.length()-1);
			hql.append(" and i.secondLevelMateriel in ('"+secondMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(threeMaterials)){
			threeMaterials=threeMaterials.substring(0, threeMaterials.length()-1);
			hql.append(" and i.threeLevelMateriel in ('"+threeMaterials.toString().replaceAll(",","','")+"')");
		}
		list = incomingInspectionActionsReportDao.find(hql + " group by yearMonthAndDate",searchParams.toArray());
		Map<String,Integer> qualifieldMap = new HashMap<String, Integer>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null){
				continue;
			}
			qualifieldMap.put(objs[0].toString(),Integer.valueOf(objs[1].toString()));
		}
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		List<Double> data3 = new ArrayList<Double>();
		
		List<Integer> data = new ArrayList<Integer>();
		while(CommonUtil1.getYearMonthAndDate(startCal)<= CommonUtil1.getYearMonthAndDate(endCal)){
			//横坐标天数的参数
			categories.add(startCal.get(Calendar.DAY_OF_MONTH));
			
			Map<String,Object> map = new HashMap<String, Object>();
			String dateStr = DateUtil.formateDateStr(startCal);
			Integer index = CommonUtil1.getYearMonthAndDate(startCal);
			map.put("date",dateStr);
			
			Integer all = allMap.get(index.toString());
			if(all == null){
				all = 0;
			}
			map.put("name","检查批数");
			map.put("y", all);
			data1.add(map);
			data.add(all);
			
			data = new ArrayList<Integer>();
			map = new HashMap<String, Object>();
			map.put("date",dateStr);
			Integer qualifiedCount = qualifieldMap.get(index.toString());
			if(qualifiedCount == null){
				qualifiedCount = 0;
			}
			map.put("name","合格批数");
			map.put("in","合格批数");
			map.put("y",qualifiedCount);
			data2.add(map);
			data.add(qualifiedCount);
			
			double rate = 100.0;
			if(all>0){
				rate = (qualifiedCount*1.0/all)*100;
			}
			data3.add(rate);
			startCal.add(Calendar.DATE,1);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName","日期");
		series1.put("data",data1);
		result.put("series1", series1);
		series2.put("data",data2);
		result.put("series2", series2);
		series3.put("data",data3);
		result.put("series3", series3);
		result.put("max", 100);
		
		return result;
	}
	public static Map<String,String> getFieldMap(){
		Map<String,String> fieldMap  = new HashMap<String, String>();
		fieldMap.put("businessUnitCode","business_unit_code");
		fieldMap.put("formType","form_type");
		fieldMap.put("inspectionConclusion","inspection_conclusion");
		fieldMap.put("supplierCode","supplier_code");
		fieldMap.put("supplierName","supplier_name");
		fieldMap.put("checkBomCode","check_bom_code");
		fieldMap.put("checkBomName","check_bom_name");
		fieldMap.put("checkBomMaterialType","check_bom_material_type");
		fieldMap.put("iteminspectionType","inspection_type");
		fieldMap.put("itemconclusion","conclusion");
		
		fieldMap.put("oneLevelMateriel","one_level_materiel");
		fieldMap.put("secondLevelMateriel","second_level_materiel");
		fieldMap.put("threeLevelMateriel","three_level_materiel");
		return fieldMap;
	}
	/**
	 * 进货检验一次合格率按月统计
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getOneChartDatasByMonths(JSONObject params) throws Exception{
		Date startDate = DateUtil.parseDate(params.getString("startDate_ge_date"),"yyyy-MM");
		Date endDate = DateUtil.parseDate(params.getString("startDate_ge_date"),"yyyy-MM");
		
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("title", "进货检验一次合格率");
		result.put("subtitle","(" + params.get("startDate_ge_date").toString() + "-" + params.get("endDate_le_date").toString() + ")");
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		List<Double> data3 = new ArrayList<Double>();
		
		StringBuilder hql = new StringBuilder("select yearAndMonth,count(*) from IncomingInspectionActionsReport i where i.companyId = ? and i.yearAndMonth between ? and ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(Integer.valueOf(params.getString("startDate_ge_date").replaceAll("-","")));
		searchParams.add(Integer.valueOf(params.getString("endDate_le_date").replaceAll("-","")));
		String oneMaterials="";
		String secondMaterials="";
		String threeMaterials="";
		for(Object key : params.keySet()){
			if("checkBomMaterialLevels".equals(key)){
				String [] leveIds=params.get(key).toString().split(",");
				for(String leveId:leveIds){
					String hqlLevel=" from MaterielTypeLevel leve where leve.id=? and leve.companyId=?";
					List<MaterielTypeLevel> levels=incomingInspectionActionsReportDao.find(hqlLevel,Long.valueOf(leveId),ContextUtils.getCompanyId());
					if(!levels.isEmpty()){
						MaterielTypeLevel level=levels.get(0);
						String levelName=level.getMaterielTypeName();
						if(level.getMaterielLevel()==1){
							oneMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==2){
							secondMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==3){
							threeMaterials+=levelName+",";
						}
					}
				}
				break;
			}
		}
		CommonUtil1.buildHql(params, hql, searchParams,getFieldMap(),"i");
		//全部
		if(!"".equals(oneMaterials)){
			oneMaterials=oneMaterials.substring(0, oneMaterials.length()-1);
			hql.append(" and i.oneLevelMateriel in ('"+oneMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(secondMaterials)){
			secondMaterials=secondMaterials.substring(0, secondMaterials.length()-1);
			hql.append(" and i.secondLevelMateriel in ('"+secondMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(threeMaterials)){
			threeMaterials=threeMaterials.substring(0, threeMaterials.length()-1);
			hql.append(" and i.threeLevelMateriel in ('"+threeMaterials.toString().replaceAll(",","','")+"')");
		}
		List<?> list = incomingInspectionActionsReportDao.find(hql + " group by yearAndMonth",searchParams.toArray());
		Map<String,Integer> allMap = new HashMap<String, Integer>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null){
				continue;
			}
			allMap.put(objs[0].toString(),Integer.valueOf(objs[1].toString()));
		}
		//合格
		hql.append(" and i.inspectionConclusion = ?");
		searchParams.add("OK");
		list = incomingInspectionActionsReportDao.find(hql + " group by yearAndMonth",searchParams.toArray());
		Map<String,Integer> qualifieldMap = new HashMap<String, Integer>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null){
				continue;
			}
			qualifieldMap.put(objs[0].toString(),Integer.valueOf(objs[1].toString()));
		}
		
		List<Integer> data = new ArrayList<Integer>();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(endDate);
		while(CommonUtil1.getYearAndMonth(startCal)<=CommonUtil1.getYearAndMonth(endCal)){
			Integer startMonth = CommonUtil1.getYearAndMonth(startCal);
			//横坐标月份的参数
			categories.add(startMonth);
			
			Map<String,Object> map = new HashMap<String, Object>();
			Integer all = allMap.get(startMonth.toString());
			if(all == null){
				all = 0;
			}
			map.put("date",startMonth);
			map.put("name","检查批数");
			map.put("y",all);
			data1.add(map);
			data.add(all);
			
			data = new ArrayList<Integer>();
			map = new HashMap<String, Object>();
			map.put("date",startMonth);
			Integer qualifiedCount = qualifieldMap.get(startMonth.toString());
			if(qualifiedCount == null){
				qualifiedCount = 0;
			}
			map.put("name","合格批数");
			map.put("in","合格批数");
			map.put("y", qualifiedCount);
			data2.add(map);
			data.add(qualifiedCount);
			
			double rate = 100.0;
			if(all>0){
				rate = (qualifiedCount*1.0/all)*100;
			}
			data3.add(rate);
			
			startCal.add(Calendar.MONTH,1);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName","月份");
		series1.put("data", data1);
		result.put("series1", series1);
		series2.put("data", data2);
		result.put("series2", series2);
		series3.put("data", data3);
		result.put("series3", series3);
		result.put("max", 100);
		return result;
	}
	
	/**
	 * 进货检验一次合格率按周统计
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getOneChartDatasByWeeks(JSONObject params) throws Exception{
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		int startyear = 0, startweek = 0, endyear = 0, endweek = 0, endWeekNumber = 0;
		startyear = Integer.valueOf(params.get("year_ge").toString());
		endyear = Integer.valueOf(params.get("year_le").toString());
		if(params.get("week_ge")!=null){
			startweek = Integer.valueOf(params.get("week_ge").toString());
		}
		if(params.get("week_le")!=null){
			endweek = Integer.valueOf(params.get("week_le").toString());
		}
		
		startCal.set(Calendar.YEAR,startyear);     
		startCal.set(Calendar.WEEK_OF_YEAR,startweek);
		startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		startCal.setFirstDayOfWeek(Calendar.MONDAY);
		
		endCal.set(Calendar.YEAR,endyear);
		endCal.set(Calendar.WEEK_OF_YEAR,endweek);
		endCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		endCal.setFirstDayOfWeek(Calendar.MONDAY);   
		
		endWeekNumber = getYearAndWeekNumber(endCal);
		
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("title", "进货检验一次合格率");
		result.put("subtitle","(" + startyear +"年"+ startweek + "周 - " + endyear + "年"+ endweek +"周)");
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		List<Double> data3 = new ArrayList<Double>();
		
		List<Integer> data = new ArrayList<Integer>();
		double total =0, qualified = 0;
		while(getYearAndWeekNumber(startCal) <= endWeekNumber){
			//横坐标周期的参数
			String name = String.valueOf(getYearAndWeekNumber(startCal));
			categories.add(Integer.valueOf(name.substring(4)));
			startCal.set(Calendar.HOUR_OF_DAY,0);
			startCal.set(Calendar.MINUTE,0);
			startCal.set(Calendar.SECOND,0);
			Date startdate1 = startCal.getTime();
			
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(startdate1);
			endCalendar.add(Calendar.DATE, 6);
			endCalendar.set(Calendar.HOUR_OF_DAY,23);
			endCalendar.set(Calendar.MINUTE,59);
			endCalendar.set(Calendar.SECOND,59);
			Date enddate1 = endCalendar.getTime();

			Map<String,Object> map = new HashMap<String, Object>();
			List<IncomingInspectionActionsReport> incomingInspectionActionsReport = this.listAll(startdate1,enddate1);
			map.put("date",name);
			map.put("name","检查批数");
			map.put("y",incomingInspectionActionsReport.size());
			data1.add(map);
			data.add(incomingInspectionActionsReport.size());
			total = incomingInspectionActionsReport.size();
			
			data = new ArrayList<Integer>();
			map = new HashMap<String, Object>();
			map.put("date", name);
			incomingInspectionActionsReport = this.listQualified(startdate1, enddate1, null, "OK");
			map.put("name","合格批数");
			map.put("in","合格批数");
			map.put("y", incomingInspectionActionsReport.size());
			data2.add(map);
			data.add(incomingInspectionActionsReport.size());
			qualified = incomingInspectionActionsReport.size();
			
			double rate = 0;
			if(total != 0){
				rate = (qualified/total)*100;
			}
			data3.add(rate);
			
			startCal.add(Calendar.DATE, 7);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName","周期");
		series1.put("data", data1);
		result.put("series1", series1);
		series2.put("data", data2);
		result.put("series2", series2);
		series3.put("data", data3);
		result.put("series3", series3);
		result.put("max", 100);
		return result;
	}
	
	public Object caculateUnqualitedReport(JSONObject params) throws Exception{
		Date startDate=new Date();
		Date endDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(params.get("startDate_ge_date")!=null && params.get("endDate_le_date")!=null){
			String starttime=params.get("startDate_ge_date").toString();
			String endtime=params.get("endDate_le_date").toString();
			ActionContext.getContext().put("starttime", starttime);
			ActionContext.getContext().put("endtime", endtime);
		}
		
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		int startyear = 0, startmonth = 0, endyear = 0, endmonth = 0;
		int startweek = 0, endweek = 0;
		if(params.get("myType")!=null && "month".equals(params.get("myType").toString())){
			startyear =  Integer.valueOf(params.get("startDate_ge_date").toString().substring(0, 4));
			startmonth = Integer.valueOf(params.get("startDate_ge_date").toString().substring(5, 7));
			endyear = Integer.valueOf(params.get("endDate_le_date").toString().substring(0, 4));
			endmonth = Integer.valueOf(params.get("endDate_le_date").toString().substring(5, 7));
			
			startCal.set(Calendar.YEAR,startyear);
			startCal.set(Calendar.MONTH,startmonth-1);
			startCal.set(Calendar.DATE,1);
			startCal.set(Calendar.HOUR_OF_DAY,0);
			startCal.set(Calendar.MINUTE,0);
			startCal.set(Calendar.SECOND,0);
			
			endCal.set(Calendar.YEAR,endyear);
			endCal.set(Calendar.MONTH,endmonth);
			endCal.set(Calendar.DATE,1);
			endCal.add(Calendar.DATE,-1);
			endCal.set(Calendar.HOUR_OF_DAY,23);
			endCal.set(Calendar.MINUTE,59);
			endCal.set(Calendar.SECOND,59);
		}else if(params.get("myType")!=null && "week".equals(params.get("myType").toString())){
			startyear = Integer.valueOf(params.get("year_ge").toString());
			endyear = Integer.valueOf(params.get("year_le").toString());
			if(params.get("week_ge")!=null){
				startweek = Integer.valueOf(params.get("week_ge").toString());
			}
			if(params.get("week_le")!=null){
				endweek = Integer.valueOf(params.get("week_le").toString());
			}
			
			startCal.set(Calendar.YEAR,startyear);
			startCal.set(Calendar.WEEK_OF_YEAR, startweek);
			startCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			startCal.set(Calendar.HOUR_OF_DAY,0);
			startCal.set(Calendar.MINUTE,0);
			startCal.set(Calendar.SECOND,0);
			
			endCal.set(Calendar.YEAR,endyear);
			endCal.set(Calendar.WEEK_OF_YEAR, endweek);
			endCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			endCal.add(Calendar.DATE, 6);
			endCal.set(Calendar.HOUR_OF_DAY,23);
			endCal.set(Calendar.MINUTE,59);
			endCal.set(Calendar.SECOND,59);
		}else{
			startDate=sdf.parse(params.get("startDate_ge_date").toString()+" 00:00:00");
			endDate=sdf.parse(params.get("endDate_le_date").toString()+" 23:59:59");
		}
		
		Map<String,Object> result = new HashMap<String, Object>();
		if(params.get("myType")!=null && "month".equals(params.get("myType").toString())){
			startDate = startCal.getTime();
			endDate = endCal.getTime();
			result.put("subtitle","(" + startyear + "年" + startmonth + "月 至 " + endyear + "年" + endmonth + "月)");
		}else if(params.get("myType")!=null && "date".equals(params.get("myType").toString())){
			result.put("subtitle","(" + DateUtil.formateDateStr(startDate) + " 至" + DateUtil.formateDateStr(endDate) + ")");
		}else{
			startDate = startCal.getTime();
			endDate = endCal.getTime();
			result.put("subtitle","(" + startyear + "年" + startweek + "周 至" + endyear + "年" + endweek + "周)");
		}
		String classification = null;
		if(params.get("classification")!=null && !params.get("classification").equals("")){
			if(params.get("classification").equals("致命缺陷(CR)")){
				classification = "CR";
			}
			if(params.get("classification").equals("主要缺陷(MA)")){
				classification = "MA";
			}
			if(params.get("classification").equals("次要缺陷(MI)")){
				classification = "MI";
			}
		}
		String groupName = "";
		//不合格物料
		if(params.get("type").toString().equals("materiel")){
			groupName = "checkBomCode";
		//不合格供应商
		}else if(params.get("type").toString().equals("supplier")){
			groupName = "supplierName";
		//不合格类别
		}else if(params.get("type").toString().equals("type")){
			groupName = "supplierName";
		//检验员
		}else if(params.get("type").toString().equals("inspector")){
			groupName = "inspector";
		//处理结果
		}else if(params.get("type").toString().equals("result")){
			groupName = "processingResult";
		}else{
			groupName = params.getString("type");
		}
		List<Object> searchParams = new ArrayList<Object>();
		String hql = "select "+groupName+",count(*) as total from IncomingInspectionActionsReport i where i.companyId = ? and i.inspectionDate between ? and ? and i.inspectionConclusion = 'NG'";
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(StringUtils.isNotEmpty(classification)){
			hql+= " and i.defectLevel = ?";
			searchParams.add(classification);
		}
		hql += " group by " + groupName + " order by count(*) desc";
		Query query = incomingInspectionActionsReportDao.createQuery(hql);
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
		@SuppressWarnings("unchecked")
		List<Object> list = query.list();
		List<String> categories = new ArrayList<String>();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		int other = 1;
		for(Object o : list){
			Object[] objs = (Object[])o;
			String searchName = objs[0]==null?"":objs[0].toString();
			searchName = "".equals(searchName)?"其他" + other++:searchName;
			Integer y = Integer.valueOf(objs[1].toString());
			categories.add(searchName);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("searchname",searchName);
			map.put("y",y);
			data.add(map);
		}
		String label = Struts2Utils.getParameter("groupLabel");
		result.put("title", label+"不合格批数");
		result.put("categories", categories);
		result.put("tableHeaderList", categories);
		result.put("yAxisTitle1","不<br/>合<br/>格<br/>批<br/>数");
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name",label);
		series1.put("data",data);
		result.put("series1", series1);
		result.put("max", 99.9);
		return result;
	}
	
	public Page<IncomingInspectionActionsReport> queryIinspectionReportDetail(Page<IncomingInspectionActionsReport> page, JSONObject params) {
		StringBuilder hql = new StringBuilder("select distinct i from IncomingInspectionActionsReport i  where i.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if("date".equals(params.getString("myType"))){
			hql.append(" and i.yearMonthAndDate between ? and ?");
		}else{
			hql.append(" and i.yearAndMonth between ? and ?");
		}
		searchParams.add(Integer.valueOf(params.getString("startDate_ge_date").replaceAll("-","")));
		searchParams.add(Integer.valueOf(params.getString("endDate_le_date").replaceAll("-","")));
		CommonUtil1.buildHql(params, hql, searchParams,getFieldMap(),"i");
		String oneMaterials="";
		String secondMaterials="";
		String threeMaterials="";
		for(Object key : params.keySet()){
			if("checkBomMaterialLevels".equals(key)){
				String [] leveIds=params.get(key).toString().split(",");
				for(String leveId:leveIds){
					String hqlLevel=" from MaterielTypeLevel leve where leve.id=? and leve.companyId=?";
					List<MaterielTypeLevel> levels=incomingInspectionActionsReportDao.find(hqlLevel,Long.valueOf(leveId),ContextUtils.getCompanyId());
					if(!levels.isEmpty()){
						MaterielTypeLevel level=levels.get(0);
						String levelName=level.getMaterielTypeName();
						if(level.getMaterielLevel()==1){
							oneMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==2){
							secondMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==3){
							threeMaterials+=levelName+",";
						}
					}
				}
				break;
			}
		}
		//全部
		if(!"".equals(oneMaterials)){
			oneMaterials=oneMaterials.substring(0, oneMaterials.length()-1);
			hql.append(" and i.oneLevelMateriel in ('"+oneMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(secondMaterials)){
			secondMaterials=secondMaterials.substring(0, secondMaterials.length()-1);
			hql.append(" and i.secondLevelMateriel in ('"+secondMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(threeMaterials)){
			threeMaterials=threeMaterials.substring(0, threeMaterials.length()-1);
			hql.append(" and i.threeLevelMateriel in ('"+threeMaterials.toString().replaceAll(",","','")+"')");
		}
		return incomingInspectionActionsReportDao.searchPageByHql(page, hql.toString(),searchParams.toArray());
	}
	
	public Page<IncomingInspectionActionsReport> search(Page<IncomingInspectionActionsReport> page) {
		return incomingInspectionActionsReportDao.search(page);
	}

	//封装不良细项结果数据集的JSON格式
	public String getResultJson(Page<IncomingInspectionActionsReport> page,String countNamePrex) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(IncomingInspectionActionsReport cp : page.getResult()){
			JSONObject json = JSONObject.fromObject(JsonParser.object2Json(cp));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(cp.getInspectionDate()!=null){
				String inspectionDate=sdf.format(cp.getInspectionDate());
				json.put("inspectionDate", inspectionDate);
			}
			if(cp.getQualifiedRate()!=null){
				String qualifiedRate=String.valueOf(cp.getQualifiedRate()*100)+"%";
				json.put("qualifiedRate", qualifiedRate);
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
	public String getOneResultJson(IncomingInspectionActionsReport incomingInspectionActionsReport) {
		HashMap<String, Object> hs = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (incomingInspectionActionsReport.getInspectionDate() != null) {
			String inspectionDate = sdf.format(incomingInspectionActionsReport.getInspectionDate());
			hs.put("inspectionDate", inspectionDate);
		}
		if (incomingInspectionActionsReport.getEnterDate() != null) {
			String enterDate = sdf.format(incomingInspectionActionsReport.getEnterDate());
			hs.put("enterDate", enterDate);
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
			IncomingInspectionActionsReport incomingInspectionActionsReport = new IncomingInspectionActionsReport();
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
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	
	public void submitProcess(IncomingInspectionActionsReport incomingInspectionActionsReport) {
		saveIncomingInspectionActionsReport(incomingInspectionActionsReport);
		Long processId=ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode("sample-appraisal-report").get(0).getId();
		ApiFactory.getInstanceService().submitInstance(processId,
				incomingInspectionActionsReport);
	}

	public void completeTask(IncomingInspectionActionsReport incomingInspectionActionsReport, Long taskId,
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
	public IncomingInspectionActionsReport getIncomingInspectionActionsReportByTaskId(Long taskId) {
		return getIncomingInspectionActionsReport(ApiFactory.getFormService()
				.getFormFlowableIdByTask(taskId));
	}

	/**
	 * 获得loginName用户的该实例的当前任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getMyTask(IncomingInspectionActionsReport incomingInspectionActionsReport, String loginName) {
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
		incomingInspectionActionsReportDao.delete(id);
	}
	/**
	  * 方法名: 判断是否第一次来料
	  * @param page
	  * @param params
	  * @return
	 */
	public Boolean checkIsNewIQCBySupplier(String supplierCode, String supplierName){
		String hql = "select count(*) from IncomingInspectionActionsReport i where i.companyId = ? and i.supplierCode = ? and i.supplierName = ?";
		List<?> list = incomingInspectionActionsReportDao.find(hql,ContextUtils.getCompanyId(),supplierCode,supplierName);
		if(Integer.valueOf(list.get(0).toString())>0){
			return false;
		}else{
			return true;
		}
	}
	
	
	/**
	  * 方法名:增加/修改采购物料入库信息明细
	  * @param storageType 入库类别
	  * @param godownEntryNumber
	  * @param businessUnitCode
	  * @param businessUnitName
	  * @param entryId
	  * @param supplierCode
	  * @param supplierName
	  * @param materialCode
	  * @param materialName
	  * @param materialModel
	  * @param planIncomingDate
	  * @param enterDate
	  * @param amount
	  * @param companyId
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NumberFormatException 
	 */
	public void saveCheckItem(String formType,String storageType,String godownEntryNumber,
			String businessUnitCode,
			String businessUnitName,
			String entryId,
			String supplierCode,
			String supplierName,
			String materialCode,
			String materialName,
			String materialModel,
			Date planIncomingDate,
			Date enterDate,
			Double amount,
			Long companyId,
			String orderNo,
			String remark) throws NumberFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(StringUtils.isEmpty(formType)){
			throw new AmbFrameException("单据类型不能为空!");
		}
		if(StringUtils.isEmpty(storageType)){
			throw new AmbFrameException("入库类别不能为空!");
		}
		if(StringUtils.isEmpty(godownEntryNumber)){
			throw new AmbFrameException("入库单号不能为空!");
		}
		if(StringUtils.isEmpty(businessUnitCode)){
			throw new AmbFrameException("事业部编码不能为空!");
		}
		if(StringUtils.isEmpty(businessUnitName)){
			throw new AmbFrameException("事业部名称不能为空!");
		}
		if(StringUtils.isEmpty(entryId)){
			throw new AmbFrameException("分录号不能为空!");
		}
		if(StringUtils.isEmpty(supplierCode)){
			throw new AmbFrameException("供应商编码不能为空!");
		}
		if(StringUtils.isEmpty(supplierName)){
			throw new AmbFrameException("供应商名称不能为空!");
		}
		if(StringUtils.isEmpty(materialCode)){
			throw new AmbFrameException("物料编码不能为空!");
		}
		if(StringUtils.isEmpty(materialName)){
			throw new AmbFrameException("物料名称不能为空!");
		}
		if(StringUtils.isEmpty(materialModel)){
			throw new AmbFrameException("物料型号不能为空!");
		}
		if(enterDate==null){
			throw new AmbFrameException("到货日期不能为空!");
		}
		if(planIncomingDate==null){
			planIncomingDate = enterDate;
		}
		if(amount==null){
			throw new AmbFrameException("来料数量不能为空!");
		}
		IncomingInspectionActionsReport report = null;
		List<JSONObject> existArray = null;
		String hql = "from IncomingInspectionActionsReport i where i.businessUnitCode = ? and i.requestCheckNo = ? and i.entryId = ? and i.companyId = ? and i.formType = ?";
		List<IncomingInspectionActionsReport> reports = incomingInspectionActionsReportDao.find(hql,businessUnitCode,godownEntryNumber,entryId,companyId,formType);
		String operate = "新增";
		if(reports.isEmpty()){
			report = new IncomingInspectionActionsReport();
			report.setCompanyId(companyId);
			report.setCreatedTime(new Date());
			report.setCreator(ScheduleJob.getScheduleUserName());
			report.setFormType(formType);
			report.setStorageType(storageType);
			report.setRequestCheckNo(godownEntryNumber);
			report.setEntryId(entryId);
//			report.setInspectionNo(formCodeGenerated.generateIncomingInspectionReportCode(companyId));
			report.setCheckItems(new ArrayList<CheckItem>());
		}else{
			report = reports.get(0);
			existArray = new ArrayList<JSONObject>();
			//exist items
			for(CheckItem checkItem : report.getCheckItems()){
				JSONObject obj = new JSONObject();
				obj.put("checkItemName",checkItem.getCheckItemName());
				if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					obj.put("results",checkItem.getResults()==null?"":checkItem.getResults());
				}else{
					for(int i=1;i<=30;i++){
						String fieldName = "result" + i;
						Object val = PropertyUtils.getProperty(checkItem,fieldName);
						if(val != null){
							obj.put(fieldName,val);
						}
					}
				}
				if(checkItem.getQualifiedAmount() != null){
					obj.put("qualifiedAmount",checkItem.getQualifiedAmount());
				}
				if(checkItem.getUnqualifiedAmount() != null){
					obj.put("unqualifiedAmount",checkItem.getUnqualifiedAmount());
				}
				existArray.add(obj);
			}
			report.getCheckItems().clear();
		}
		report.setLastModifiedTime(new Date());
		report.setLastModifier(ScheduleJob.getScheduleUserName());
		report.setBusinessUnitCode(businessUnitCode);
		report.setBusinessUnitName(businessUnitName);
		report.setSupplierCode(supplierCode);
		report.setSupplierName(supplierName);
		report.setEnterDate(enterDate);
		report.setCheckBomCode(materialCode);
		report.setCheckBomName(materialName);
		report.setModelSpecification(materialModel);
		report.setInspectionDate(enterDate);
		report.setStockAmount(amount);
		report.setErpStockAmount(amount);
		report.setPlanIncomingDate(planIncomingDate);
		report.setOrderNo(orderNo);
		report.setRemark(remark);
		//生成检验项目明细
		List<CheckItem> items = getCheckItems(report,existArray);
		report.getCheckItems().addAll(items);
		report.setQualifiedRate(1.0f);
		report.setQualifiedAmount(0.0d);
		incomingInspectionActionsReportDao.save(report);
		//保存接口操作痕迹
		ErpRequestRecord erpRequestRecord = new ErpRequestRecord();
		erpRequestRecord.setCreator("system");
		erpRequestRecord.setCreatedTime(new Date());
		erpRequestRecord.setCollectionName(operate + "进货检验单");
		erpRequestRecord.setCompanyId(companyId);
		erpRequestRecord.setDetails(getRemarkDetail(report));
		incomingInspectionActionsReportDao.getSession().save(erpRequestRecord);
	}
	
	/**
	  * 方法名: 删除进货检验单
	  * <p>功能说明：</p>
	  * @param godownEntryNumber 入库单号,必填
	  * @param businessUnitCode 事业部门编码(统计归属),必填
	  * @param entryId 分录号 可为空,为空时删除整张入库单
	 */
	@SuppressWarnings("unchecked")
	public void deleteCheckItem(String formType,String godownEntryNumber,String businessUnitCode,
			String entryId,Long companyId){
		if(StringUtils.isEmpty(formType)){
			throw new AmbFrameException("单据类型不能为空!");
		}
		if(StringUtils.isEmpty(godownEntryNumber)){
			throw new AmbFrameException("入库单号不能为空!");
		}
		if(StringUtils.isEmpty(businessUnitCode)){
			throw new AmbFrameException("事业部门编码不能为空!");
		}
		List<String> details = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isEmpty(entryId)){
			String hql = "from IncomingInspectionActionsReport i where i.businessUnitCode = ? and i.requestCheckNo = ? and i.companyId = ? and i.formType = ?";
			List<IncomingInspectionActionsReport> reports = incomingInspectionActionsReportDao.createQuery(hql,businessUnitCode,godownEntryNumber,companyId,formType).list();
			for(IncomingInspectionActionsReport report : reports){
				if(!(IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT.equals(report.getInspectionState())
						||IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK.equals(report.getInspectionState()))){
					throw new AmbFrameException("检验状态为【"+report.getInspectionState()+"】,不能删除!");
				}
				incomingInspectionActionsReportDao.delete(report);
				if(sb.length()>1500){
					details.add(sb.toString());
					sb.delete(0,sb.length());
				}
				if(sb.length()>0){
					sb.append(";");
				}
				sb.append(getRemarkDetail(report));
			}
		}else{
			String hql = "from IncomingInspectionActionsReport i where i.businessUnitCode = ? and i.requestCheckNo = ? and i.entryId = ? and i.companyId = ? and i.formType = ?";
			List<IncomingInspectionActionsReport> reports = incomingInspectionActionsReportDao.createQuery(hql,businessUnitCode,godownEntryNumber,entryId,companyId,formType).list();
			for(IncomingInspectionActionsReport report : reports){
				if(!(IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT.equals(report.getInspectionState())
						||IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK.equals(report.getInspectionState()))){
					throw new AmbFrameException("检验状态为【"+report.getInspectionState()+"】,不能删除!");
				}
				incomingInspectionActionsReportDao.delete(report);
				if(sb.length()>1500){
					details.add(sb.toString());
					sb.delete(0,sb.length());
				}
				if(sb.length()>0){
					sb.append(";");
				}
				sb.append(getRemarkDetail(report));
			}
		}
		if(sb.length()>0){
			details.add(sb.toString());
		}
		for(String detail : details){
			ErpRequestRecord erpRequestRecord = new ErpRequestRecord();
			erpRequestRecord.setCreator("system");
			erpRequestRecord.setCreatedTime(new Date());
			erpRequestRecord.setCollectionName("删除进货检验单");
			erpRequestRecord.setCompanyId(companyId);
			erpRequestRecord.setDetails(detail);
			incomingInspectionActionsReportDao.getSession().save(erpRequestRecord);
		}
	}
	
	/**
	  * 方法名: 获取接口操作备忘
	  * <p>功能说明：</p>
	  * @param report
	  * @return
	 */
	private static String getRemarkDetail(IncomingInspectionActionsReport report){
		return "inspectionNo:" + report.getInspectionNo() + ",code:" + report.getCheckBomCode() + ",name:" + report.getCheckBomName() + ",requestCheckNo:" + report.getRequestCheckNo() + ",entryId:" + report.getEntryId() +",inspectionDate:" + DateUtil.formateDateStr(report.getInspectionDate());
	}
	
	/**
	  * 方法名: 分拆进货检验单,回写检验单后如果发现单号不对,分拆检验单
	  * <p>功能说明：</p>
	  * @param report
	  * @param newBillNo
	  * @param newEntryId
	  * @throws NumberFormatException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	public void splitIncomingReport(IncomingInspectionActionsReport report,String newBillNo,String newEntryId) throws NumberFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(StringUtils.isEmpty(newBillNo)){
			throw new AmbFrameException("新单号不能为空!");
		}
		if(StringUtils.isEmpty(newEntryId)){
			throw new AmbFrameException("新分录号不能为空!");
		}
		if(newBillNo.equals(report.getRequestCheckNo())){
			return;
		}
		
		//计算出拆分的数量
		Double splitVal = report.getStockAmount() - report.getProcessingReceiveQty();
		if(splitVal<=0){
			return;
		}
		
		report.setProcessingResult("合格");
		report.setStockAmount(report.getProcessingReceiveQty());
		report.setProcessingBadQty(0.0);
//		String splitRequestCheckNo = StringUtils.isEmpty(report.getRequestCheckNo())?(report.getRequestCheckNo() + "," + newBillNo):(report.getSplitRequestCheckNo() + "," + newBillNo);
//		report.setSplitRequestCheckNo(splitRequestCheckNo);
		report.setQualifiedAmount(report.getInspectionAmount());
		report.setUnqualifiedAmount(0.0);
		report.setQualifiedRate(1.0f);
		report.setInspectionConclusion("OK");
		
		//新建一个检验项目
		IncomingInspectionActionsReport splitReport = new IncomingInspectionActionsReport();
		//复制属性值
		List<Field> fieldList = new ArrayList<Field>();
		Field[] fields = IncomingInspectionActionsReport.class.getDeclaredFields();
		for(Field field : fields){
			fieldList.add(field);
		}
		fields = IncomingInspectionActionsReport.class.getSuperclass().getDeclaredFields();
		for(Field field : fields){
			fieldList.add(field);
		}
		for(Field field : fieldList){
			if(field.getType().getName().equals(List.class.getName()) 
					|| "id".equals(field.getName())
					|| field.getModifiers() != 2){
				continue;
			}
			PropertyUtils.setProperty(splitReport,field.getName(),
					PropertyUtils.getProperty(report, field.getName()));
		}
		List<CheckItem> newCheckItems = new ArrayList<CheckItem>();
		for(CheckItem checkItem : report.getCheckItems()){
			//复制属性值
			fields = CheckItem.class.getDeclaredFields();
			fieldList.clear();
			for(Field field : fields){
				fieldList.add(field);
			}
			fields = CheckItem.class.getSuperclass().getDeclaredFields();
			for(Field field : fields){
				fieldList.add(field);
			}
			CheckItem newCheckItem = new CheckItem();
			for(Field field : fieldList){
				if(field.getType().getName().equals(IncomingInspectionActionsReport.class.getName()) 
						|| "id".equals(field.getName())
						|| field.getModifiers() != 2){
					continue;
				}
				PropertyUtils.setProperty(newCheckItem,field.getName(),
						PropertyUtils.getProperty(checkItem, field.getName()));
			}
			newCheckItem.setIiar(splitReport);
			if(newCheckItem.getInspectionAmount()!=null&&newCheckItem.getInspectionAmount()>splitVal){
				newCheckItem.setInspectionAmount(splitVal.intValue());
				if(newCheckItem.getQualifiedAmount()!=null&&newCheckItem.getQualifiedAmount()>newCheckItem.getInspectionAmount()){
					newCheckItem.setQualifiedAmount(newCheckItem.getInspectionAmount());
					newCheckItem.setUnqualifiedAmount(newCheckItem.getInspectionAmount()-newCheckItem.getQualifiedAmount());
				}
			}
			newCheckItems.add(newCheckItem);
		}
//		splitReport.setInspectionNo(formCodeGenerated.generateIncomingInspectionReportCode(report.getCompanyId()));
		splitReport.setRequestCheckNo(newBillNo);
		splitReport.setEntryId(newEntryId);
		splitReport.setCheckItems(newCheckItems);
		splitReport.setStockAmount(splitVal);
		splitReport.setInspectionAmount(splitVal);
		splitReport.setQualifiedAmount(0.0d);
		splitReport.setUnqualifiedAmount(splitVal);
		splitReport.setProcessingReceiveQty(0.0d);
		splitReport.setProcessingBadQty(splitVal);
		splitReport.setInspectionConclusion("NG");
		splitReport.setQualifiedRate(0.0f);
		splitReport.setInspectionState(IncomingInspectionActionsReport.INPECTION_STATE_SUBMIT);
		incomingInspectionActionsReportDao.save(splitReport);
		
		//
		for(CheckItem checkItem : report.getCheckItems()){
			if(checkItem.getInspectionAmount() != null && checkItem.getInspectionAmount()>report.getStockAmount()){
				checkItem.setInspectionAmount(report.getStockAmount().intValue());
				if(checkItem.getQualifiedAmount()!=null&&checkItem.getQualifiedAmount()>checkItem.getInspectionAmount()){
					checkItem.setQualifiedAmount(checkItem.getInspectionAmount());
					checkItem.setUnqualifiedAmount(checkItem.getInspectionAmount()-checkItem.getQualifiedAmount());
				}
			}
		}
		incomingInspectionActionsReportDao.save(report);
		
		//保存拆分出来的检验报告记录
		ErpRequestRecord erpRequestRecord = new ErpRequestRecord();
		erpRequestRecord.setCreator("system");
		erpRequestRecord.setCreatedTime(new Date());
		erpRequestRecord.setCollectionName("新增进货检验单(拆分)");
		erpRequestRecord.setCompanyId(report.getCompanyId());
		erpRequestRecord.setDetails(getRemarkDetail(splitReport));
		incomingInspectionActionsReportDao.getSession().save(erpRequestRecord);
	}
	
	public void saveExecuteErpParams(IncomingInspectionActionsReport report,List<NameValuePair> nameValuePairs){
		StringBuilder detail = new StringBuilder();
		detail.append("inspectionNo:" + report.getInspectionNo());
		for(NameValuePair nameValuePair : nameValuePairs){
			detail.append("," + nameValuePair.getName() + ":" + nameValuePair.getValue());
		}
		ErpRequestRecord erpRequestRecord = new ErpRequestRecord();
		erpRequestRecord.setCreator("system");
		erpRequestRecord.setCreatedTime(new Date());
		erpRequestRecord.setCollectionName("回写进货检验结果");
		erpRequestRecord.setCompanyId(report.getCompanyId());
		erpRequestRecord.setDetails(detail.toString());
		incomingInspectionActionsReportDao.getSession().save(erpRequestRecord);
	}
	/**
	 * 方法名: 
	 * <p>功能说明：统计检验员检验及时率</p>
	 * 创建人:wuxuming 日期： 2015-3-9 version 1.0
	 * @param 
	 * @return
	 */
	public List<Map<String,List<Object>>> dudateStatisticalFinishReportRateDatas(JSONObject params) throws Exception{
		Integer startDate = Integer.valueOf(params.getString("startDate").replaceAll("-",""));
		Integer endDate = Integer.valueOf(params.getString("endDate").replaceAll("-",""));
		Integer dutyDate = Integer.valueOf(params.getString("dutyDate"));//接受时间 与 检验时间差
		String business=params.getString("businessUnitCode").toString();
		String bomCode=null;
		if(params.containsKey("checkBomCode")){
			bomCode=params.getString("checkBomCode").toString();
		}
		String businessUnitCodeHql="";
		Map<String,List<String>> mapBus= new HashMap<String,List<String>>();//事业部与检验员
		Map<String,Integer> mapInspectorRate= new HashMap<String,Integer>();//（事业部+检验员）与 总检验数
		Map<String,Integer> mapInspectorFinishRate= new HashMap<String,Integer>();//（事业部+检验员）与 按时完成数
		Map<String,List<Object>> mapReportInformation= new HashMap<String,List<Object>>();//（事业部+检验员）与检验报告基础信息
		Map<String,List<IncomingInspectionActionsReport>> mapLiOBj= new HashMap<String,List<IncomingInspectionActionsReport>>();//检验员的检验报告信息
		String hql="from IncomingInspectionActionsReport iiar where   iiar.enterYearMonthAndDate between ? and ? and iiar.inspectionState=? and iiar.checkBomCode=? ";
		hql+=businessUnitCodeHql;
		if(business!=null&&!"".equals(business)){
			hql+=" and iiar.businessUnitCode in('" +business.replaceAll(",","','") + "')";
		}
		List<IncomingInspectionActionsReport> li= incomingInspectionActionsReportDao.find(hql, startDate,endDate,IncomingInspectionActionsReport.INPECTION_STATE_AUDIT,bomCode);
		String businessUnit="";
		for(IncomingInspectionActionsReport iiar:li){
			businessUnit = iiar.getBusinessUnitName();
			if(businessUnit==null){
				if(iiar.getBusinessUnitCode()==null){
					continue;
				}
			}
			String inspector=iiar.getInspector();
			String busInspector=businessUnit+"-"+inspector;
			if(mapInspectorRate.containsKey(busInspector)){ //存储检验总数
				int batchNum=mapInspectorRate.get(busInspector);
				batchNum++;
				mapInspectorRate.put(busInspector, batchNum);
			}else{
				mapInspectorRate.put(busInspector, 1);
			}
			
		}
		String checkedHq="from IncomingInspectionActionsReport iiar where  iiar.yearMonthAndDate between ? and ? and iiar.inspectionState=? and iiar.checkBomCode=? ";
		checkedHq+=businessUnitCodeHql;
		if(business!=null&&!"".equals(business)){
			checkedHq+=" and iiar.businessUnitCode in('" +business.replaceAll(",","','") + "')";
		}
		List<IncomingInspectionActionsReport> liReport= incomingInspectionActionsReportDao.find(checkedHq, startDate+dutyDate,endDate+dutyDate,IncomingInspectionActionsReport.INPECTION_STATE_AUDIT,bomCode);
		@SuppressWarnings("unchecked")
		List<String> tempLinkList=new LinkedList();
		for(IncomingInspectionActionsReport iiar:liReport){
			businessUnit = iiar.getBusinessUnitName();
			if(businessUnit==null){
				if(iiar.getBusinessUnitCode()==null){
					continue;
				}
			}
			String inspector=iiar.getInspector().trim();
			String busInspector=businessUnit+"-"+inspector;
			if(mapBus.containsKey(businessUnit)){
				if(!tempLinkList.contains(inspector)){
					List<String> liInspector=mapBus.get(businessUnit);
					liInspector.add(inspector);
					tempLinkList.add(inspector);
					mapBus.put(businessUnit, liInspector);
				}
			}else{
				List<String> liInspector=new ArrayList<String>();
				liInspector.add(inspector);
				tempLinkList.add(inspector);
				mapBus.put(businessUnit, liInspector);
			}
			if(mapReportInformation.containsKey(busInspector)){//存储检验报告基础信息
				List<IncomingInspectionActionsReport>  liReportBus= mapLiOBj.get(busInspector);
				liReportBus.add(iiar);
				mapLiOBj.put(busInspector, liReportBus);
			}else{
				List<IncomingInspectionActionsReport>  liReportBus= new ArrayList<IncomingInspectionActionsReport>();
				liReportBus.add(iiar);
				mapLiOBj.put(busInspector, liReportBus);
			}
			if(mapInspectorFinishRate.containsKey(busInspector)){ //存储检验总数
				int batchNum=mapInspectorFinishRate.get(busInspector);
				batchNum++;
				mapInspectorFinishRate.put(busInspector, batchNum);
			}else{
				mapInspectorFinishRate.put(busInspector, 1);
			}
		}
		ActionContext.getContext().put("mapBus", mapBus);
		ActionContext.getContext().put("mapInspectorRate", mapInspectorRate);
		ActionContext.getContext().put("mapLiOBj", mapLiOBj);
		ActionContext.getContext().put("mapInspectorFinishRate", mapInspectorFinishRate);
		
		return null;
	}
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param incomingInspectionActionsReport
	  * @throws IOException
	 */
	public void exportReport(IncomingInspectionActionsReport incomingInspectionActionsReport) throws IOException{
		InputStream inputStream = null;//ff
		try {
			IncomingInspectionActionsReport report = incomingInspectionActionsReport;
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/iqc-inspection-report.xls");
			Map<String,ExportExcelFormatter> formatterMap = new HashMap<String, ExportExcelFormatter>();
			//检验单号格式化
			formatterMap.put("inspectionNo",new ExportExcelFormatter() {
				public String format(Object value, int rowNum, String fieldName,Cell cell) {
					IncomingInspectionActionsReport report = (IncomingInspectionActionsReport)value;
					return "检验单编号:" + report.getInspectionNo();
				}

			});
			//检验判定格式化
			ExportExcelFormatter conclusionFormatter = new ExportExcelFormatter() {
				public String format(Object value, int rowNum, String fieldName,Cell cell) {
					String conclusion = null;
					if("inspectionConclusion".equals(fieldName)){
						IncomingInspectionActionsReport report = (IncomingInspectionActionsReport)value;
						conclusion = report.getInspectionConclusion();
					}else if("checkItems.conclusion".equals(fieldName)){
						CheckItem item = (CheckItem)value;
						conclusion = item.getConclusion();
					}
					if(StringUtils.isEmpty(conclusion)){
						return "";
					}else if("OK".equals(conclusion)){
						return "合格";
					}else if("NG".equals(conclusion)){
						return "不合格";
					}else{
						return conclusion;
					}
				}
			};
			formatterMap.put("inspectionConclusion",conclusionFormatter);
			formatterMap.put("checkItems.conclusion",conclusionFormatter);
			//检验水平格式化
			formatterMap.put("checkItems.inspectionLevel",new ExportExcelFormatter() {
				public String format(Object value, int rowNum, String fieldName,Cell cell) {
					String level = "special1,special2,special3,special4,ordinary1,ordinary2,ordinary3";
					CheckItem item = (CheckItem)value;
					String myLevel = null;
					switch (level.indexOf(item.getInspectionLevel())) {
						case 0:
							myLevel = "特殊S-1";
							break;
						case 9:
							myLevel = "特殊S-2";
							break;
						case 18:
							myLevel = "特殊S-3";
							break;
						case 27:
							myLevel = "特殊S-4";
							break;
						case 36:
							myLevel = "一般I";
							break;
						case 46:
							myLevel = "一般II";
							break;
						case 56:
							myLevel = "一般III";
							break;
						default:
							myLevel = item.getInspectionLevel();
							break;
					}
					return myLevel;
				}
			});
			//检验记录格式化
			formatterMap.put("checkItems.results",new ExportExcelFormatter() {
				public String format(Object value, int rowNum, String fieldName,Cell cell){
					String result = "";
					CheckItem item = (CheckItem)value;
					if("计数".equals(item.getCountType())){
						return item.getResults();
					}else{
						Field[] fields = item.getClass().getDeclaredFields();
						try {
							for (int j = 0; j < fields.length; j++) {
								fields[j].setAccessible(true);
								if(fields[j].getName().contains("result") && !"results".equals(fields[j].getName())){
									String firstLetter = fields[j].getName().substring(0, 1).toUpperCase();
									String getMethodName = "get" + firstLetter + fields[j].getName().substring(1);
									Method getMethod = item.getClass().getMethod(getMethodName, new Class[] {});
									Object val = getMethod.invoke(item, new Object[] {});
									if(val != null){
										result += val+",";
									}
								}
							}
							if(result.length()>=1){
								result = result.substring(0, result.length()-1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
						return result;
					}
				}
			});
			//合格率格式化
			formatterMap.put("qualifiedRate",new ExportExcelFormatter() {
				public String format(Object value, int rowNum, String fieldName,Cell cell) {
					IncomingInspectionActionsReport report = (IncomingInspectionActionsReport)value;
					if(report.getQualifiedRate()==null){
						return "";
					}
					return report.getQualifiedRate()*100 + "%";
				}
			});
			String exportFileName = "进货检验报告";
			ExcelUtil.exportToExcel(inputStream, exportFileName, report, formatterMap);
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
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
			IncomingInspectionActionsReport report = incomingInspectionActionsReportDao.get(id);
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/iqc-inspection-report.xls");
			Workbook book = WorkbookFactory.create(inputStream);
			Sheet sheet = book.getSheetAt(0);
			String tableName = "";
			if(StringUtils.isNotEmpty(report.getCheckBomMaterialType())){
				tableName = report.getCheckBomMaterialType();
			}else if(StringUtils.isNotEmpty(report.getStorageType())){
				tableName = report.getStorageType();
			}
			if(tableName.endsWith("检验")){
				tableName = tableName.substring(0,tableName.length()-2);
			}
			String title = tableName + "检验报告";
			ExcelUtil.getCell(sheet,"a1").setCellValue(title);
			ExcelUtil.getCell(sheet,"a2").setCellValue("编号:" + report.getInspectionNo());
			ExcelUtil.getCell(sheet,"d3").setCellValue(report.getRequestCheckNo()==null?"":report.getRequestCheckNo());
			ExcelUtil.getCell(sheet,"h3").setCellValue(report.getFormType()==null?"":report.getFormType());
			ExcelUtil.getCell(sheet,"m3").setCellValue(report.getStorageType()==null?"":report.getStorageType());
			ExcelUtil.getCell(sheet,"d4").setCellValue(report.getBusinessUnitName()==null?"":report.getBusinessUnitName());
			ExcelUtil.getCell(sheet,"h4").setCellValue(report.getSupplierCode()==null?"":report.getSupplierCode());
			ExcelUtil.getCell(sheet,"m4").setCellValue(report.getSupplierName()==null?"":report.getSupplierName());
			ExcelUtil.getCell(sheet,"d5").setCellValue(report.getCheckBomCode()==null?"":report.getCheckBomCode());
			ExcelUtil.getCell(sheet,"h5").setCellValue(report.getCheckBomName()==null?"":report.getCheckBomName());
			ExcelUtil.getCell(sheet,"m5").setCellValue(report.getModelSpecification()==null?"":report.getModelSpecification());
			ExcelUtil.getCell(sheet,"d6").setCellValue(report.getCheckBomMaterialType()==null?"":report.getCheckBomMaterialType());
			ExcelUtil.getCell(sheet,"h6").setCellValue(DateUtil.formateDateStr(report.getPlanIncomingDate()));
			ExcelUtil.getCell(sheet,"m6").setCellValue(DateUtil.formateDateStr(report.getEnterDate()));
			ExcelUtil.getCell(sheet,"d8").setCellValue(report.getIs3C()==null?"":report.getIs3C());
			ExcelUtil.getCell(sheet,"h8").setCellValue(report.getIsStandard()==null?"":report.getIsStandard());
			ExcelUtil.getCell(sheet,"m8").setCellValue(report.getIskeyComponent()==null?"":report.getIskeyComponent());
			ExcelUtil.getCell(sheet,"d9").setCellValue(report.getStockAmount()==null?"":report.getStockAmount().toString());
			ExcelUtil.getCell(sheet,"h9").setCellValue(report.getBatchNo()==null?"":report.getBatchNo());
			ExcelUtil.getCell(sheet,"m9").setCellValue(DateUtil.formateTimeStr(report.getInspectionDate()));
			ExcelUtil.getCell(sheet,"d10").setCellValue(report.getFireControlNo()==null?"":report.getFireControlNo());
			ExcelUtil.getCell(sheet,"h10").setCellValue(report.getStandardVersion()==null?"":report.getStandardVersion());
			ExcelUtil.getCell(sheet,"m10").setCellValue("加严".equals(report.getSampleSchemeType())?"加严":(report.getSampleSchemeType()==null?"正常":report.getSampleSchemeType()));
			ExcelUtil.getCell(sheet,"d11").setCellValue(report.getRemark()==null?"":report.getRemark());
			
			//保存明细
			int index = 0;
			CellStyle cellStyle = ExcelUtil.getCellBorderStyle(book);
			for(CheckItem checkItem : report.getCheckItems()){
				if(index > 0){
					int insertRowNum = index + 12;
					sheet.shiftRows(insertRowNum, sheet.getLastRowNum(),1,true,false);
					sheet.createRow(insertRowNum);
					Row row = sheet.getRow(insertRowNum);
					row.setHeightInPoints(29);
					for(int j=0;j<=15;j++){
						row.createCell(j).setCellStyle(cellStyle);
					}
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 1,2));
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 3,4));
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 9,12));
					sheet.addMergedRegion(new CellRangeAddress(insertRowNum, insertRowNum, 9,12));
				}
				//序号
				Cell cell = ExcelUtil.getCell(sheet,"a13",index);
				cell.setCellValue(index+1);
				//分类
				cell = ExcelUtil.getCell(sheet,"b13",index);
				cell.setCellValue(checkItem.getParentItemName()==null?"":checkItem.getParentItemName());
				//检查项目
				cell = ExcelUtil.getCell(sheet,"d13",index);
				cell.setCellValue(checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName());
				//检查方法
				cell = ExcelUtil.getCell(sheet,"f13",index);
				cell.setCellValue(checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod());
				//检查类别
				cell = ExcelUtil.getCell(sheet,"g13",index);
				cell.setCellValue(checkItem.getInspectionType()==null?"":checkItem.getInspectionType());
				//抽检数量
				cell = ExcelUtil.getCell(sheet,"h13",index);
				cell.setCellValue(checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount().toString());
				//规格
				cell = ExcelUtil.getCell(sheet,"i13",index);
				cell.setCellValue(checkItem.getSpecifications()==null?"":checkItem.getSpecifications());
				//检验记录
				cell = ExcelUtil.getCell(sheet,"j13",index);
				String str = "";
				if(MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					str = checkItem.getResults();
				}else{
					for(int i=1;i<=30;i++){
						Object val = PropertyUtils.getProperty(checkItem,"result" + i);
						if(val != null){
							str += "," + val;
						}
					}
				}
				cell.setCellValue(StringUtils.isEmpty(str)?"":str.substring(1));
				//合格数
				cell = ExcelUtil.getCell(sheet,"n13",index);
				cell.setCellValue(checkItem.getQualifiedAmount()==null?"":checkItem.getQualifiedAmount().toString());
				//不良数
				cell = ExcelUtil.getCell(sheet,"o13",index);
				cell.setCellValue(checkItem.getUnqualifiedAmount()==null?"":checkItem.getUnqualifiedAmount().toString());
				//结论
				cell = ExcelUtil.getCell(sheet,"p13",index);
				cell.setCellValue(checkItem.getConclusion()==null?"":("OK".equals(checkItem.getConclusion())?"合格":"不合格"));
				
				cell = ExcelUtil.getCell(sheet,"p13",index);
				cell.setCellValue(checkItem.getStandardRemark()==null?"":checkItem.getStandardRemark());
				
				index++;
			}
			if(report.getCheckItems().size()>0){
				index--;
			}
			
			//底部的内容
			ExcelUtil.getCell(sheet,"d14",index).setCellValue(report.getInspectionAmount()==null?"":report.getInspectionAmount().toString());
			ExcelUtil.getCell(sheet,"m14",index).setCellValue(report.getInspectionConclusion()==null?"":report.getInspectionConclusion());
			ExcelUtil.getCell(sheet,"d15",index).setCellValue(report.getQualifiedAmount()==null?"":report.getQualifiedAmount().toString());
			ExcelUtil.getCell(sheet,"h15",index).setCellValue(report.getUnqualifiedAmount()==null?"":report.getUnqualifiedAmount().toString());
			ExcelUtil.getCell(sheet,"m15",index).setCellValue(report.getQualifiedRate()==null?"":(report.getQualifiedRate()*100 + "%"));
			ExcelUtil.getCell(sheet,"d17",index).setCellValue(report.getProcessingResult()==null?"":report.getProcessingResult());
			ExcelUtil.getCell(sheet,"m17",index).setCellValue(report.getInspector()==null?"":report.getInspector());
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
	public List<IncomingInspectionActionsReport> queryByDate(String dateStr){
		String sql = "from IncomingInspectionActionsReport i where i.inspectionState = ? and i.createdTime > ? ";
		return incomingInspectionActionsReportDao
										.createQuery(sql.toString(),"已完成",DateUtil.parseDate(dateStr))
										.list();
	}
	public void updateWriteState(IncomingInspectionActionsReport report){
		String hqlDetails = "select count(*) from ErpRequestRecord e where collectionName = ? and details like ?";
		Query query = incomingInspectionActionsReportDao.createQuery(hqlDetails, "回写进货检验结果","%" + report.getInspectionNo() + "%");
		List<Object> details = query.list();
		if(!(Integer.valueOf(details.get(0).toString())>0)){
//			incomingInspectionService.updateRequestCheckState(report);
		}
	}
	public static void main(String[] args) {
		IncomingInspectionActionsReport splitReport = new IncomingInspectionActionsReport();
		Field[] fields = IncomingInspectionActionsReport.class.getSuperclass()
				.getDeclaredFields();
		for(Field field : fields){
			if(field.getType().getName().equals(List.class.getName()) 
					|| "id".equals(field.getName()) || field.getModifiers() != 2){
				continue;
			}
		}
	}
	
	public List<IncomingInspectionActionsReport> getListByInspectingItemIndicator(
			Date startDate, Date endDate,
			ItemIndicator inspectingItemIndicator) {
		// TODO Auto-generated method stub
		return incomingInspectionActionsReportDao.getListByInspectingItemIndicator(startDate, endDate, inspectingItemIndicator);
	}
	public String insertSpcByResults(QualityFeature feature,Date inspectionDate,String hisSampleIds,String results){
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
						updateSpcSgGroup(sample.getSpcSubGroup(),ContextUtils.getCompanyId(),ContextUtils.getUserName());
					}
				}else{
					sample = insertSpc(feature,inspectionDate,Double.valueOf(val),ContextUtils.getCompanyId(),ContextUtils.getUserName());
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
	/**
	  * 方法名:添加数据时插入监控 
	  * @param feature
	  * @param detectDate
	  * @param paramValue
	  * @param companyid
	  * @param userName
	  * @return
	 */
	public SpcSgSample insertSpc(QualityFeature feature,Date detectDate,Double paramValue,Long companyid,String userName){
		SpcSubGroup spcSubGroup = spcSubGroupManager.findLastSubGroup(feature.getId(),feature.getSampleCapacity(),companyid,userName);
		int sampleSize = 0;
		if(spcSubGroup == null){
			spcSubGroup = new SpcSubGroup();
			spcSubGroup.setCompanyId(companyid);
			spcSubGroup.setCreatedTime(detectDate);
			spcSubGroup.setCreator(userName);
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
			/*SpcSgTag spcSgTag = new SpcSgTag();
			spcSgTag.setCreatedTime(spcSubGroup.getCreatedTime());
			spcSgTag.setCompanyId(ContextUtils.getCompanyId());
			spcSgTag.setCreator(ContextUtils.getUserName());
			spcSgTag.setModifiedTime(new Date());
			spcSgTag.setModifier(ContextUtils.getUserName());
			spcSgTag.setTagValue(inspectionPointType.toString());
			spcSgTag.setTagName("");
			spcSgTag.setTagCode("");
			spcSubGroup.setInfo1(inspectionPointType.toString());*/
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
		spcSgSample.setCreator(userName);
		spcSgSample.setLastModifiedTime(new Date());
		spcSgSample.setLastModifier(userName);
		spcSgSample.setSampleNo("X" + (sampleSize + 1));
		spcSgSample.setSampleOrderNum(sampleSize + 1+"");
		spcSgSample.setSamValue(paramValue);
		spcSgSample.setSpcSubGroup(spcSubGroup);
		spcSgSampleDao.save(spcSgSample);
		//如果采集的数据够一组时,检查监控条件
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
			abnormalInfoManager.lanchAbnormal(spcSubGroup.getSubGroupOrderNum()+"", feature, list,null);
			BaseUtil.setSpcSgSampleDao(null);
			
			spcSubGroup.setJudgeState(1);
			spcSubGroupDao.save(spcSubGroup);
		}
		return spcSgSample;
	} 
	private Integer getMaxGroupOrderNum(QualityFeature feature,Long companyId){
		List<Object> objs = qualityFeatureDao.find("select max(s.subGroupOrderNum) from SpcSubGroup s where s.qualityFeature = ? and s.companyId = ?",feature,companyId);
		if(objs.get(0)==null){
			return 1;
		}else{
			return Integer.valueOf(objs.get(0).toString())+1;
		}
	}
	
	public void updateSpcSgGroup(SpcSubGroup spcSubGroup,Long companyid,String userName){
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
		spcSubGroup.setRangeDiff(randDiff);
		spcSubGroupDao.save(spcSubGroup);
	}
	/**
	 * 读取检验数据
	 * 
	 * @param file
	 * @throws Exception
	 */
	public Map<String, String> importInspectionFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> valueMap = new HashMap<String, String>();
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		Iterator<Row> rows = sheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();// 标题行
		int i = 1;
		int rowTimes = 1;
		String mapKey = "";
		while (rows.hasNext()) {
			row = sheet.getRow(i-1);
			if(row==null){
				break;
			}
			String cellValueStr = "";
			if(rowTimes==1){
				Cell cell = row.getCell(rowTimes-1);
				mapKey = cell.getStringCellValue();
				rowTimes++;
				continue;
			}
			for (int j=1;j>0;j++) {
				Cell cell = row.getCell(j);
				if(cell!=null){
					if(cellValueStr.length()==0){
						cellValueStr = String.valueOf(cell.getNumericCellValue());
					}else{
						cellValueStr += "," + String.valueOf(cell.getNumericCellValue());
					}
				}else{
					rowTimes=1;
					break;
				}
				
			}
			valueMap.put(mapKey, cellValueStr);
			i++;
		}
		file.delete();
		return valueMap;
	}

	public String getReportIdBySpcSmapleId(String spcSampleId) {
		// TODO Auto-generated method stub
		String reportId = "";
		String sql = "select FK_IQC_IIAR from IQC_CHECK_ITEM t where t.spc_sample_ids like ? ";
		@SuppressWarnings("unchecked")
		List<Object> objs = spcSubGroupDao.getSession().createSQLQuery(sql).setParameter(0,"%"+spcSampleId+"%").list();
		if(objs.size()!=0){
			reportId = objs.get(0).toString();
		}
		return reportId;
	}
	public void hiddenState(String hideId,String type){
		String[] ids = hideId.split(",");
		for(String id : ids){
			IncomingInspectionActionsReport incomingInspectionActionsReport = incomingInspectionActionsReportDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				incomingInspectionActionsReport.setHiddenState("N");
			}else{
				incomingInspectionActionsReport.setHiddenState("Y");
			}
			incomingInspectionActionsReportDao.save(incomingInspectionActionsReport);
		}
	}
	/**
	 * 查询检验报告
	 * @param id
	 * @param name
	 * @return
	 */
	public IncomingInspectionActionsReport searchReport(String supplierCode,String checkBomCode,String type){
		String hql = "select i from IncomingInspectionActionsReport i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode=?   ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(checkBomCode);
		if(type.equals("ort")){
			hql += "  and  i.ordReportId is not null ";
		}
		if(type.equals("hsf")){
			hql += "  and  i.hisReportId is not null ";
		}
		hql += " order by i.inspectionDate desc ";
		List<IncomingInspectionActionsReport> list = incomingInspectionActionsReportDao.find(hql,searchParams.toArray());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
