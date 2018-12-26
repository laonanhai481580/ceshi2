package com.ambition.iqc.inspectionbase.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.IndicatorAttach;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgCheckItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.carmfg.inspectionbase.service.IndicatorAttachManager;
import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.InspectingIndicator;
import com.ambition.iqc.entity.InspectingItem;
import com.ambition.iqc.entity.ItemIndicator;
import com.ambition.iqc.entity.MaterielTypeLevel;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.inspectionbase.dao.InspectingIndicatorDao;
import com.ambition.iqc.inspectionbase.dao.InspectingItemDao;
import com.ambition.iqc.inspectionbase.dao.ItemIndicatorDao;
import com.ambition.iqc.inspectionreport.dao.MaterielTypeLevelDao;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.spc.dataacquisition.dao.SpcSgSampleDao;
import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ItemIndicatorManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class ItemIndicatorManager {
	@Autowired
	private ItemIndicatorDao itemIndicatorDao;
	@Autowired
	private InspectingIndicatorManager inspectingIndicatorManager;
	@Autowired
	private SpcSgSampleDao spcSgSampleDao;
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private ProductBomDao productBomDao;
	@Autowired
	private InspectingIndicatorDao inspectingIndicatorDao;
	@Autowired
	private InspectingItemDao inspectingItemDao;
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	@Autowired
	private MaterielTypeLevelDao materielTypeLevelDao;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	@Autowired
	private IncomingInspectionActionsReportManager incomingInspectionActionsReportManager;
	@Autowired
	private IndicatorAttachManager indicatorAttachManager;
	
	public ItemIndicator getItemIndicator(Long id){
		return itemIndicatorDao.get(id);
	}
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteItemIndicator(Long id){
		itemIndicatorDao.delete(id);
	}
	
	/**
	 * 添加检验项目
	 * @param inspectingIndicatorId
	 * @param inspectingItemIds
	 * @return
	 */
	public int addItemIndicator(Long inspectingIndicatorId,String inspectingItemIds){
		int addCount = 0;
		InspectingIndicator inspectingIndicator = inspectingIndicatorDao.get(inspectingIndicatorId);
		for(String inspectingItemId : inspectingItemIds.split(",")){
			if(StringUtils.isNotEmpty(inspectingItemId)){
				InspectingItem inspectingItem = inspectingItemDao.get(Long.valueOf(inspectingItemId));
				//只能添加最子级为检验项目
				if(inspectingItem.getItemChildren().isEmpty()){
					//检查是否已经添加
					String hql = "select count(i.id) from ItemIndicator i where i.inspectingItem = ? and i.inspectingIndicator = ?";
					List<?> list = itemIndicatorDao.find(hql,inspectingItem,inspectingIndicator);
					if(Integer.valueOf(list.get(0).toString())==0){
						ItemIndicator itemIndicator = new ItemIndicator();
						itemIndicator.setInspectingIndicator(inspectingIndicator);
						itemIndicator.setInspectingItem(inspectingItem);
						itemIndicatorDao.save(itemIndicator);
						addCount++;
					}
				}
			}
		}
		return addCount;
	}
	
	/**
	 * 复制抽样标准
	 * @param inspectingIndicatorId
	 * @param targetBomCodes
	 * @return
	 */
	public Map<String,Integer> copyInspectingIndicators(Long inspectingIndicatorId,String targetBomIds){
		int addCount = 0,repeatCount=0;
		Map<String,String> productNameMap = new HashMap<String, String>();
		InspectingIndicator inspectingIndicator = inspectingIndicatorDao.get(inspectingIndicatorId);
		for(String bomId : targetBomIds.split(",")){
			if(StringUtils.isNotEmpty(bomId)){
				ProductBom productBom = productBomDao.get(Long.valueOf(bomId));
				if(!isExistInspectingIndicator(null,productBom.getMaterielCode(),inspectingIndicator.getStandardVersion())){
					//复制抽样的编码
					InspectingIndicator targetInspectingIndicator = new InspectingIndicator();
					targetInspectingIndicator.setMaterielCode(productBom.getMaterielCode());
					targetInspectingIndicator.setMaterielName(productBom.getMaterielName());
					targetInspectingIndicator.setIs3C(inspectingIndicator.getIs3C());
					targetInspectingIndicator.setIsStandard(inspectingIndicator.getIsStandard());
					targetInspectingIndicator.setIskeyComponent(inspectingIndicator.getIskeyComponent());
					targetInspectingIndicator.setStandardVersion(inspectingIndicator.getStandardVersion());
					targetInspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
					targetInspectingIndicator.setCreatedTime(new Date());
					targetInspectingIndicator.setCreator(ContextUtils.getUserName());
					targetInspectingIndicator.setLastModifiedTime(targetInspectingIndicator.getCreatedTime());
					targetInspectingIndicator.setLastModifier(ContextUtils.getUserName());
					targetInspectingIndicator.setRemark(inspectingIndicator.getRemark());
//					IndicatorAttach attach = indicatorAttachManager.copyIndicatorAttach(inspectingIndicator.getIndicatorAttachId());
//					if(attach != null){
//						targetInspectingIndicator.setIndicatorAttachId(attach.getId());
//					}
					inspectingIndicatorDao.save(targetInspectingIndicator);
					//保存检验标准,最后一起更新最新版本的标志
					productNameMap.put(targetInspectingIndicator.getMaterielCode(),targetInspectingIndicator.getMaterielCode());
					//复制项目
					for(ItemIndicator itemIndicator : inspectingIndicator.getItemIndicators()){
						ItemIndicator targetIndicator = new ItemIndicator();
						targetIndicator.setCompanyId(ContextUtils.getCompanyId());
						targetIndicator.setCreatedTime(targetInspectingIndicator.getCreatedTime());
						targetIndicator.setCreator(targetInspectingIndicator.getCreator());
						targetIndicator.setLastModifiedTime(targetInspectingIndicator.getCreatedTime());
						targetIndicator.setLastModifier(ContextUtils.getUserName());
						targetIndicator.setAqlStandard(itemIndicator.getAqlStandard());
						targetIndicator.setInspectingIndicator(targetInspectingIndicator);
						targetIndicator.setInspectingItem(itemIndicator.getInspectingItem());
						targetIndicator.setInspectionAmount(itemIndicator.getInspectionAmount());
						targetIndicator.setInspectionLevel(itemIndicator.getInspectionLevel());
						targetIndicator.setLevela(itemIndicator.getLevela());
						targetIndicator.setLevelb(itemIndicator.getLevelb());
						targetIndicator.setSpecifications(itemIndicator.getSpecifications());
						targetIndicator.setTotalPoints(itemIndicator.getTotalPoints());
						targetIndicator.setRemark(itemIndicator.getRemark());
						itemIndicatorDao.save(targetIndicator);
					}
					addCount++;
				}else{
					repeatCount++;
				}
			}
		}
		//更新是否最新标验标准的标志
		for(String materialCode : productNameMap.values()){
			updateMaxVersionFlag(materialCode);
		}
		Map<String,Integer> result = new HashMap<String, Integer>();
		result.put("add",addCount);
		result.put("repeat", repeatCount);
		return result;
	}
	
	/**
	 * 保存检验项目指标
	 * @throws Exception 
	 * 
	 */
	public void saveItemIndicator(InspectingIndicator inspectingIndicator,InspectingItem inspectingItem,JSONObject params) throws Exception{
		if(!inspectingItem.getItemChildren().isEmpty()){
			throw new RuntimeException("检验项目还有子节点,不能设置!");
		}
		params = convertJsonObject(params);
		List<ItemIndicator> itemIndicators = itemIndicatorDao.getItemIndicatorsByInspectingItem(inspectingItem);
		boolean isMySel = false;
		for(ItemIndicator itemIndicator : itemIndicators){
			if(inspectingIndicator.getId().toString().equals(itemIndicator.getInspectingIndicator().getId().toString())){
				isMySel = true;
				for(Object key : params.keySet()){
					setProperty(itemIndicator,key,params);
				}
				String baseType = sampleSchemeManager.getUseBaseType().getBaseType();
				if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
					itemIndicator.setAqlStandard(itemIndicator.getInspectionLevel());
				}
				itemIndicator.setLastModifiedTime(new Date());
				itemIndicator.setLastModifier(ContextUtils.getUserName());
				itemIndicatorDao.save(itemIndicator);
				logUtilDao.debugLog("保存", itemIndicator.toString());
				break;
			}
		}
		if(!isMySel){
			throw new RuntimeException("请先勾选是否设置!");
		}
	}
	
	
	/**
	 * 获取所有已设置的模型的指标
	 * @return
	 */
	public List<ItemIndicator> getAllItemIndicators(Long parentItemIndicatorId){
		return itemIndicatorDao.getAllItemIndicators(parentItemIndicatorId);
	}
	
	/**
	 * 获取所有已设置的模型的指标
	 * @return
	 */
	public List<ItemIndicator> getAllItemIndicators(){
		return itemIndicatorDao.getAllItemIndicators();
	}
	
	/**
	 * 根据物料获取 检验项目
	 * @param productBom
	 * @return
	 */
	public List<ItemIndicator> getAllItemIndicatorsByProductBom(ProductBom productBom){
		InspectingIndicator inspectingIndicator = new InspectingIndicator();
		inspectingIndicator = inspectingIndicatorManager.getAllInspectingIndicatorsByProductBom(productBom.getMaterielCode());
		String hql = "from ItemIndicator i where i.inspectingIndicator = ? and i.companyId = ?";
		return itemIndicatorDao.find(hql,new Object[]{inspectingIndicator,ContextUtils.getCompanyId()});
	}
	
	/**
	 * 设置评价
	 * @throws Exception 
	 */
	public void setItemIndicator(InspectingIndicator inspectingIndicator,InspectingItem inspectingItem,String isSet,JSONObject params) throws Exception{
		if(!inspectingItem.getItemChildren().isEmpty()){
			throw new RuntimeException("项目指标还有子节点,不能设置!");
		}
		
		if("yes".equals(isSet)){
			String hql = "select count(i.id) from ItemIndicator i where i.inspectingIndicator = ? and i.inspectingItem = ?";
			List<?> list = itemIndicatorDao.find(hql,inspectingIndicator,inspectingItem);
			if(Integer.valueOf(list.get(0).toString())>0){
				throw new RuntimeException("检验项目指标已在其他位置设置!");
			}
			ItemIndicator itemIndicator = new ItemIndicator();
			itemIndicator.setCompanyId(ContextUtils.getCompanyId());
			itemIndicator.setCreatedTime(new Date());
			itemIndicator.setCompanyId(ContextUtils.getCompanyId());
			itemIndicator.setCreator(ContextUtils.getUserName());
			itemIndicator.setLastModifiedTime(new Date());
			itemIndicator.setLastModifier(ContextUtils.getUserName());
			itemIndicator.setInspectingItem(inspectingItem);
			itemIndicator.setInspectingIndicator(inspectingIndicator);
			params = convertJsonObject(params);
			for(Object key : params.keySet()){
				setProperty(itemIndicator,key,params);
			}
			itemIndicatorDao.save(itemIndicator);
			logUtilDao.debugLog("保存", itemIndicator.toString());
		}else{
			String hql = "from ItemIndicator i where i.inspectingIndicator = ? and i.inspectingItem = ?";
			List<ItemIndicator> list = itemIndicatorDao.find(hql,inspectingIndicator,inspectingItem);
			for(ItemIndicator itemIndicator : list){
				itemIndicatorDao.delete(itemIndicator);
			}
			logUtilDao.debugLog("删除","取消料号" + inspectingIndicator.getMaterielCode() + "的检验项目" + inspectingItem.getItemName());
		}
	}
	
	private Map<String,String> getFieldMap(){
		Map<String,String> fieldMap = new HashMap<String, String>();
		fieldMap.put("检验项目分类","parentName_inspectingItem");
		fieldMap.put("检验项目","itemName_inspectingItem");
		fieldMap.put("统计类型","countType_inspectingItem");
		fieldMap.put("检验方法","method_inspectingItem");
		fieldMap.put("单位","unit_inspectingItem");
		fieldMap.put("检验类别","inspectionLevel_itemIndicator");
		fieldMap.put("AQL","aqlStandard_itemIndicator");
		fieldMap.put("检验数量","inspectionAmount_itemIndicator");
		fieldMap.put("规格","specifications_itemIndicator");
		fieldMap.put("上限","levela_itemIndicator");
		fieldMap.put("下限","levelb_itemIndicator");
		fieldMap.put("备注","remark_itemIndicator");
		return fieldMap;
	}
	/**
	 * 方法名: 保存物料大类数据
	 * <p>功能说明：</p>
	 * 创建人:wuxuming 日期： 2015-3-2 version 1.0
	 * @param 
	 * @return
	 */
	private MaterielTypeLevel saveMaterielTypeLevel(MaterielTypeLevel parent,String typeName,int level ){
		List<Object> searchParams = new ArrayList<Object>();
		String hql = "from MaterielTypeLevel l where l.materielTypeName = ?";
		searchParams.add(typeName);
		if(parent == null){
			hql += " and l.materielTyepParent is null";
		}else{
			hql += " and l.materielTyepParent  = ?";
			searchParams.add(parent);
		}
		List<MaterielTypeLevel> list = materielTypeLevelDao.find(hql,searchParams.toArray());
		if(!list.isEmpty()){
			return list.get(0);
		}else{
			MaterielTypeLevel materielTypeLevel=new MaterielTypeLevel();
			materielTypeLevel.setMaterielTypeName(typeName);
			materielTypeLevel.setMaterielTyepParent(parent);
			materielTypeLevel.setCreator(ContextUtils.getUserName());
			materielTypeLevel.setCreatedTime(new Date());
			materielTypeLevel.setMaterielLevel(level);
//			materielTypeLevel.setChildMaterielType(null);
			materielTypeLevel.setLastModifiedTime(new Date());
			materielTypeLevel.setLastModifier(ContextUtils.getUserName());
			materielTypeLevel.setCompanyId(ContextUtils.getCompanyId());
			materielTypeLevelDao.save(materielTypeLevel);
			return materielTypeLevel;
		}
	}
	/**
	 * 方法名: 
	 * <p>功能说明：保存物料大类父子数据结构</p>
	 * 创建人:wuxuming 日期： 2015-3-2 version 1.0
	 * @param 
	 * @return
	 */
	private void getMaterielTypeLevel(InspectingIndicator inspectingIndicator) throws Exception{
		MaterielTypeLevel firstLevel = saveMaterielTypeLevel(null,inspectingIndicator.getMaterielLevelOne(),1);
		MaterielTypeLevel secondLevel = saveMaterielTypeLevel(firstLevel,inspectingIndicator.getMaterielLevelSec(),2);
		saveMaterielTypeLevel(secondLevel,inspectingIndicator.getMaterielLevelTree(),3);
	}
	/**
	 * 检查是否存在相同名称的物料BOM
	 * @param id
	 * @param name
	 * @return
	 */
	private InspectingItem getInspectingItem(String name,String parentName,String method,String countType,String unit,String
			standards){
		String hql = "from InspectingItem i where i.companyId = ? and i.itemName = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(name);
		if(StringUtils.isNotEmpty(parentName)){
			hql += " and i.itemParent.itemName = ?";
			searchParams.add(parentName);
		}
		List<InspectingItem> list = inspectingItemDao.find(hql,searchParams.toArray());
		if(!list.isEmpty()){
			InspectingItem item = list.get(0);
			return item;
//			if(item.getParent() != null){
//				String hisMethod = item.getMethod()==null?"":item.getMethod();
//				String hisCountType = item.getCountType()==null?"":item.getCountType();
//				String hisUnit = item.getUnit()==null?"":item.getUnit();
//				if(!hisMethod.equals(item.getMethod())
//						||!hisCountType.equals(item.getCountType())
//						||!hisUnit.equals(item.getUnit())){
//					item.setMethod(method);
//					item.setCountType(countType);
//					item.setUnit(unit);
//					inspectingItemDao.save(item);
//				}
//			}
		}else{
			InspectingItem parent = null;
			if(StringUtils.isNotEmpty(parentName)){
				parent = getInspectingItem(parentName,null,null,null,null,null);
			}
			InspectingItem inspectingItem = new InspectingItem();
			inspectingItem.setCreatedTime(new Date());
			inspectingItem.setCompanyId(ContextUtils.getCompanyId());
			inspectingItem.setCreator(ContextUtils.getUserName());
			inspectingItem.setLastModifiedTime(new Date());
			inspectingItem.setLastModifier(ContextUtils.getUserName());
			inspectingItem.setItemName(name);
			inspectingItem.setStandards(standards);
			inspectingItem.setItemParent(parent);
			if(parent != null){
			inspectingItem.setItemLevel(parent.getItemLevel()+1);
			}
			inspectingItem.setItemMethod(method);
			inspectingItem.setCountType(countType);
			inspectingItem.setUnit(unit);
			inspectingItemDao.save(inspectingItem);
			return inspectingItem;
		}
	}
	
	/**
	 * 检查是否存在相同名称的物料抽样标准
	 * @param id  对象的ID
	 * @param code 产品代码
	 * @param standardVersion 版本号
	 * @return
	 */
	private Boolean isExistInspectingIndicator(Long id,String code,String standardVersion){
		String hql = "select count(i.id) from InspectingIndicator i where i.companyId = ? and i.materielCode = ? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(code);
//		searchParams.add(standardVersion);
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = inspectingIndicatorDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 检查是否存在相同类别的物料抽样标准
	 * @param id  对象的ID
	 * @param code 产品代码
	 * @param standardVersion 版本号
	 * @return
	 */
	private Boolean isExistInspectingIndicatorCCM(Long id,String materielType,String standardVersion){
		String hql = "select count(i.id) from InspectingIndicator i where i.companyId = ? and i.materielType = ? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(materielType);
//		searchParams.add(standardVersion);
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = inspectingIndicatorDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	  * 方法名:判断字符串是否版本号的格式
	  *      正确的格式如:V1.0
	  * @param str
	  * @return
	 */
	public static boolean isStandardVersion(String str){
		return StringUtils.isNotEmpty(str) && str.matches("^[V|v](\\d{1,}\\.){0,}\\d{1,}$");
	}
	/**
	 * 导入标准
	 * @param file
	 * @throws Exception
	 */
	public String importIndicator(File file,String fileName) throws Exception{
		StringBuffer sb = new StringBuffer("");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			String baseType = sampleSchemeManager.getUseBaseType().getBaseType();
			List<Option> options = sampleCodeLetterManager.getInspectionLevelOptions(baseType);
			Map<String,String> levelMap = new HashMap<String, String>();
			String firstLevel = null;
			StringBuffer canSelLevels = new StringBuffer();//可选的检验类别
			String defaultInspectionLevel = null;
			for(Option option : options){
				if(firstLevel == null){
					firstLevel = option.getValue();
				}
				String name = option.getName();
				if(SampleCodeLetter.GB_TYPE.equals(baseType)){
					name = name.substring(2);
				}
				levelMap.put(name,option.getValue());
				if(canSelLevels.length()>0){
					canSelLevels.append(",");
				}
				canSelLevels.append(name);
				if(defaultInspectionLevel==null){
					defaultInspectionLevel = option.getValue();
				}
			}
			//查询可选的AQL标准
			List<Option> aqlOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_acceptance_quality_limit");
			Map<String,Boolean> aqlMap = new HashMap<String, Boolean>();
			StringBuffer canSelAqls = new StringBuffer();
			String defaultAql = null;
			for(Option option : aqlOptions){
				aqlMap.put(option.getValue(),true);
				if(canSelAqls.length()>0){
					canSelAqls.append(",");
				}
				canSelAqls.append(option.getValue());
				if(defaultAql==null){
					defaultAql = option.getValue();
				}
			}
			Workbook book = WorkbookFactory.create(inputStream);
			int totalSheets = book.getNumberOfSheets();
			Map<String,InspectingIndicator> productNameMap = new HashMap<String,InspectingIndicator>();
			Map<String,Cell> cellMap = new HashMap<String, Cell>();
			Map<String,org.apache.poi.ss.util.CellRangeAddress> cellRangeAddressMap = new HashMap<String, org.apache.poi.ss.util.CellRangeAddress>();
			for(int k=0;k<totalSheets;k++){
				Sheet sheet = book.getSheetAt(k);
				//隐藏的sheet不处理
				if(book.isSheetHidden(k)){
					continue;
				}
				Row row = sheet.getRow(0);
				if(row == null){
					continue;
				}
				//缓存每个单元格值的
				cellMap.clear();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while(rowIterator.hasNext()){
					row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						Object value = ExcelUtil.getCellValue(cell);
						if(value != null){
							String key = value.toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("　","");
							if(!cellMap.containsKey(key)){
								cellMap.put(key,cell);
							}
						}
					}
				}
				//缓存所有的合并单元格
				cellRangeAddressMap.clear();
				int sheetMergeCount = sheet.getNumMergedRegions();  
				DecimalFormat df = new DecimalFormat("0.00######");
			    for(int i = 0 ; i < sheetMergeCount ; i++ ){  
			        CellRangeAddress ca = sheet.getMergedRegion(i);
			        int firstRow = ca.getFirstRow(),lastRow = ca.getLastRow();
			        int firstColumn = ca.getFirstColumn(),lastColumn = ca.getLastColumn();
			        for(int rowIndex = firstRow;rowIndex<=lastRow;rowIndex++){
			        	for(int columnIndex = firstColumn;columnIndex<=lastColumn;columnIndex++){
			        		String key = rowIndex + "_" + columnIndex;
			        		cellRangeAddressMap.put(key,ca);
			        	}
			        }
			    }
			    //物料编码
			    Cell codeCell = ExcelUtil.getNextCell(sheet,cellMap.get("物料编码"), cellRangeAddressMap);
			    String materielCode = null;
			    if(codeCell != null){
			    	materielCode = (ExcelUtil.getCellValue(codeCell)+"").trim();
			    }
			    if(materielCode==null||"".equals(materielCode)){
			    	throw new  AmbFrameException("Sheet"+k+"的物料编码值不能为空");
			    }
			    //物料名称
			    Cell materielNameCell = ExcelUtil.getNextCell(sheet,cellMap.get("物料名称"), cellRangeAddressMap);
			    String materielName = null;
			    if(codeCell != null){
			    	materielName = (ExcelUtil.getCellValue(materielNameCell)+"").trim();
			    }
			    if(materielName==null){
			    	throw new  AmbFrameException("Sheet"+k+"的物料编码值不能为空");
			    }
			    //物料类别
			    Cell materielTypeCell = ExcelUtil.getNextCell(sheet,cellMap.get("物料类别"), cellRangeAddressMap);
			    String materielType = null;
			    if(codeCell != null){
			    	materielType = (ExcelUtil.getCellValue(materielTypeCell)+"").trim();
			    }
			    Cell figureNumberCell = ExcelUtil.getNextCell(sheet,cellMap.get("图纸版本"), cellRangeAddressMap);
			    String figureNumber = null;
			    if(codeCell != null){
			    	figureNumber = (ExcelUtil.getCellValue(figureNumberCell)+"").trim();
			    }
			    if(isExistInspectingIndicator(null,materielCode, null)){
					throw new AmbFrameException("SHEET"+k+"【"+sheet.getSheetName()+"】中,编码【"+materielCode+"】的检验标准已经存在,不能导入！");
				}
				InspectingIndicator inspectingIndicator = new InspectingIndicator();
				inspectingIndicator.setCreatedTime(new Date());
				inspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
				inspectingIndicator.setCreator(ContextUtils.getUserName());
				inspectingIndicator.setLastModifiedTime(new Date());
				inspectingIndicator.setLastModifier(ContextUtils.getUserName());
				inspectingIndicator.setMaterielCode(materielCode);
				inspectingIndicator.setMaterielName(materielName);
				inspectingIndicator.setFigureNumber(figureNumber);
				inspectingIndicator.setMaterielType(materielType);
				String key = figureNumber + materielCode;
				productNameMap.put(key, inspectingIndicator);
				//先保存检验标准文件,保存对应的ID
				IndicatorAttach attach = indicatorAttachManager.addIndicatorAttach(file,fileName,materielCode,materielName,ContextUtils.getCompanyId());
				inspectingIndicator.setIndicatorAttachId(attach.getId());
				inspectingIndicatorDao.save(inspectingIndicator);
				Map<String,Boolean> existItemMap = new HashMap<String, Boolean>();
				Cell itemItitleCell = cellMap.get("检验项目");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为检验项目的单元格!&nbsp;&nbsp;</br>");
				}
				Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
				df = new DecimalFormat("0.00000");
				boolean isGaoYi = false;//
				int orderNum = 0;
				Iterator<Row> rows = sheet.rowIterator();
				while(rows.hasNext()){
					row = rows.next();
					//标题以后再执行
					if(row.getRowNum() <= itemTitleRowNum){
						continue;
					}
					//itemName:检验指标
					Cell cell = row.getCell(itemItitleCell.getColumnIndex());
					if(cell == null){
						continue;
					}
					String parentName=null,itemName = (ExcelUtil.getCellValue(cell)+"").trim(),method=null,countType=null,unit=null;
					if(StringUtils.isEmpty(itemName)){
						continue;
					}
					itemName = CommonUtil1.replaceSymbols(itemName);
					Cell tempCell = cellMap.get("检验类别");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验类别】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					parentName=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("检验项目");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验项目】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					itemName=(ExcelUtil.getCellValue(cell)+"").trim();
					if(itemName==null){
						throw new AmbFrameException("【检验项目】不能为空！;</br>");
					}
					if(itemName.indexOf("\\")>-1){
						itemName=itemName.replace("\\", "/");
					}
					itemName=itemName.replaceAll("\"", "");
					itemName=itemName.replaceAll("'", "");
					tempCell = cellMap.get("统计类型");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【统计类型】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					countType=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("单位");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【单位】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					unit=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("检验方法");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验方法】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					method=(ExcelUtil.getCellValue(cell)+"").trim();					
				
					String inspectionLevel = null,aqlStandard = null,standards = "",remark="",massParameter="";
					String isInEquipment="否";//是否集成设备
					tempCell = cellMap.get("是否集成设备");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【是否集成设备】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						isInEquipment = (ExcelUtil.getCellValue(cell)+"");
					}
					tempCell = cellMap.get("检验水平");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验水平】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						inspectionLevel = (ExcelUtil.getCellValue(cell)+"").trim().toUpperCase();
						if(StringUtils.isNotEmpty(inspectionLevel)&&!isGaoYi){
							if(levelMap.containsKey(inspectionLevel)){
								inspectionLevel = levelMap.get(inspectionLevel);
							}
						}
					}
					tempCell = cellMap.get("AQL");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【AQL】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						aqlStandard = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(aqlStandard)
								&&!aqlMap.containsKey(aqlStandard.toUpperCase())){
							throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【AQL】的单元格!&nbsp;&nbsp;</br>");
						}
						aqlStandard = aqlStandard.toUpperCase();
					}
					if(StringUtils.isEmpty(aqlStandard)){
						aqlStandard = defaultAql;
					}
				
					Integer inspectionAmount = null;
					tempCell = cellMap.get("抽样数量");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【抽样数量】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isDouble(value)){ 
								throw new AmbFrameException("SHEET"+k+"物料【"+materielName+"】中检验数量【"+value+"】不是有效数字!");
							}
						inspectionAmount = Double.valueOf(value).intValue();
					}
					}
					
					tempCell = cellMap.get("规格");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【规格】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						standards = (ExcelUtil.getCellValue(cell)+"").trim();
						standards = CommonUtil1.replaceSymbols(standards);
					}
					//值类型
					String valueType = ItemIndicator.VALUETYPE_DEFAULT;
					Double levela = null,levelb = null;
					tempCell = cellMap.get("上限");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为上限的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							//是角度值时
							if(CommonUtil1.isDegree(value)){
								valueType = ItemIndicator.VALUETYPE_DEGREE;
								levela = CommonUtil1.degreeStrToNumber(value);
							}else{
								if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"物料【"+materielName+"】中上限【"+value+"】不是有效数字!");
								}
								levela = Double.valueOf(df.format(Double.valueOf(value)));
							}
						}
					}
					tempCell = cellMap.get("下限");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为下限的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							//是角度值时
							if(CommonUtil1.isDegree(value)){
								valueType = ItemIndicator.VALUETYPE_DEGREE;
								levelb = CommonUtil1.degreeStrToNumber(value);
							}else{
								if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"物料【"+materielName+"】中下限【"+value+"】不是有效数字!");
								}
								levelb = Double.valueOf(df.format(Double.valueOf(value)));
							}
						}
					}
					tempCell = cellMap.get("质量参数");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【质量参数】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell !=null){
						massParameter= (ExcelUtil.getCellValue(cell)+"").trim();
					}
					tempCell = cellMap.get("备注");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为备注的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						remark = (ExcelUtil.getCellValue(cell)+"").trim();
					}
					
					InspectingItem inspectingItem = getInspectingItem(itemName,parentName, method, countType, unit,standards);
					//判断是否有重复的检验项目
					String existKey = parentName + "_" + itemName;
					if(existItemMap.containsKey(existKey)){
						throw new AmbFrameException("SHEET"+k+"物料【"+materielName+"】中存在相同的检验项目【"+itemName+"】,不能导入!");
					}else{
						existItemMap.put(existKey, true);
					}
					
					ItemIndicator itemIndicator = new ItemIndicator();
					itemIndicator.setInspectingIndicator(inspectingIndicator);
					itemIndicator.setCreatedTime(new Date());
					itemIndicator.setCompanyId(ContextUtils.getCompanyId());
					itemIndicator.setCreator(ContextUtils.getUserName());
					itemIndicator.setLastModifiedTime(new Date());
					itemIndicator.setLastModifier(ContextUtils.getUserName());
					itemIndicator.setInspectingItem(inspectingItem);
					itemIndicator.setInspectionLevel(inspectionLevel);
					itemIndicator.setOrderNum(++orderNum);
					itemIndicator.setIndicatorUnit(unit);
					itemIndicator.setCountType(countType);
					itemIndicator.setIndicatorMethod(method);
					itemIndicator.setIsInEquipment(isInEquipment);
					if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
						itemIndicator.setAqlStandard(inspectionLevel);
					}else{
						itemIndicator.setAqlStandard(aqlStandard);
					}
					itemIndicator.setSpecifications(standards);
					itemIndicator.setRemark(remark);
					itemIndicator.setInspectionAmount(inspectionAmount==null?"":inspectionAmount.toString());
					itemIndicator.setLevela(levela);
					itemIndicator.setLevelb(levelb);
					itemIndicator.setMassParameter(massParameter);
					itemIndicator.setValueType(valueType);
					itemIndicatorDao.save(itemIndicator);
				}
				if(existItemMap.keySet().isEmpty()){
					throw new AmbFrameException("SHEET"+k+"物料【"+materielName+"】的检验项目为空,不能导入!");
				}
				sb.append("物料【"+materielName+"】的检验标准导入成功!!<br/>");
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
	 * 导入标准
	 * @param file
	 * @throws Exception
	 */
	public String importIndicatorCCM(File file,String fileName) throws Exception{
		StringBuffer sb = new StringBuffer("");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			String baseType = sampleSchemeManager.getUseBaseType().getBaseType();
			List<Option> options = sampleCodeLetterManager.getInspectionLevelOptions(baseType);
			Map<String,String> levelMap = new HashMap<String, String>();
			String firstLevel = null;
			StringBuffer canSelLevels = new StringBuffer();//可选的检验类别
			String defaultInspectionLevel = null;
			for(Option option : options){
				if(firstLevel == null){
					firstLevel = option.getValue();
				}
				String name = option.getName();
				if(SampleCodeLetter.GB_TYPE.equals(baseType)){
					name = name.substring(2);
				}
				levelMap.put(name,option.getValue());
				if(canSelLevels.length()>0){
					canSelLevels.append(",");
				}
				canSelLevels.append(name);
				if(defaultInspectionLevel==null){
					defaultInspectionLevel = option.getValue();
				}
			}
			//查询可选的AQL标准
			List<Option> aqlOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_acceptance_quality_limit");
			Map<String,Boolean> aqlMap = new HashMap<String, Boolean>();
			StringBuffer canSelAqls = new StringBuffer();
			String defaultAql = null;
			for(Option option : aqlOptions){
				aqlMap.put(option.getValue(),true);
				if(canSelAqls.length()>0){
					canSelAqls.append(",");
				}
				canSelAqls.append(option.getValue());
				if(defaultAql==null){
					defaultAql = option.getValue();
				}
			}
			Workbook book = WorkbookFactory.create(inputStream);
			int totalSheets = book.getNumberOfSheets();
			Map<String,InspectingIndicator> productNameMap = new HashMap<String,InspectingIndicator>();
			Map<String,Cell> cellMap = new HashMap<String, Cell>();
			Map<String,org.apache.poi.ss.util.CellRangeAddress> cellRangeAddressMap = new HashMap<String, org.apache.poi.ss.util.CellRangeAddress>();
			for(int k=0;k<totalSheets;k++){
				Sheet sheet = book.getSheetAt(k);
				//隐藏的sheet不处理
				if(book.isSheetHidden(k)){
					continue;
				}
				Row row = sheet.getRow(0);
				if(row == null){
					continue;
				}
				//缓存每个单元格值的
				cellMap.clear();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while(rowIterator.hasNext()){
					row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						Object value = ExcelUtil.getCellValue(cell);
						if(value != null){
							String key = value.toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("　","");
							if(!cellMap.containsKey(key)){
								cellMap.put(key,cell);
							}
						}
					}
				}
				//缓存所有的合并单元格
				cellRangeAddressMap.clear();
				int sheetMergeCount = sheet.getNumMergedRegions();  
				DecimalFormat df = new DecimalFormat("0.00######");
			    for(int i = 0 ; i < sheetMergeCount ; i++ ){  
			        CellRangeAddress ca = sheet.getMergedRegion(i);
			        int firstRow = ca.getFirstRow(),lastRow = ca.getLastRow();
			        int firstColumn = ca.getFirstColumn(),lastColumn = ca.getLastColumn();
			        for(int rowIndex = firstRow;rowIndex<=lastRow;rowIndex++){
			        	for(int columnIndex = firstColumn;columnIndex<=lastColumn;columnIndex++){
			        		String key = rowIndex + "_" + columnIndex;
			        		cellRangeAddressMap.put(key,ca);
			        	}
			        }
			    }
			    Cell codeCell = null;
			    //物料编码
			   /* Cell codeCell = ExcelUtil.getNextCell(sheet,cellMap.get("物料编码"), cellRangeAddressMap);
			    String materielCode = null;
			    if(codeCell != null){
			    	materielCode = (ExcelUtil.getCellValue(codeCell)+"").trim();
			    }
			    if(materielCode==null||"".equals(materielCode)){
			    	throw new  AmbFrameException("Sheet"+k+"的物料编码值不能为空");
			    }
			    //物料名称
			    Cell materielNameCell = ExcelUtil.getNextCell(sheet,cellMap.get("物料名称"), cellRangeAddressMap);
			    String materielName = null;
			    if(codeCell != null){
			    	materielName = (ExcelUtil.getCellValue(materielNameCell)+"").trim();
			    }
			    if(materielName==null){
			    	throw new  AmbFrameException("Sheet"+k+"的物料编码值不能为空");
			    }*/
			    //物料类别
			    Cell materielTypeCell = ExcelUtil.getNextCell(sheet,cellMap.get("物料类别"), cellRangeAddressMap);
			    String materielType = null;
			    if(materielTypeCell != null){
			    	materielType = (ExcelUtil.getCellValue(materielTypeCell)+"").trim();
			    }
			    Cell figureNumberCell = ExcelUtil.getNextCell(sheet,cellMap.get("图纸版本"), cellRangeAddressMap);
			    String figureNumber = null;
			    if(figureNumberCell != null){
			    	figureNumber = (ExcelUtil.getCellValue(figureNumberCell)+"").trim();
			    }
			    if(isExistInspectingIndicatorCCM(null,materielType, null)){
					throw new AmbFrameException("SHEET"+k+"【"+sheet.getSheetName()+"】中,物料类别【"+materielType+"】的检验标准已经存在,不能导入！");
				}
				InspectingIndicator inspectingIndicator = new InspectingIndicator();
				inspectingIndicator.setCreatedTime(new Date());
				inspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
				inspectingIndicator.setCreator(ContextUtils.getUserName());
				inspectingIndicator.setLastModifiedTime(new Date());
				inspectingIndicator.setLastModifier(ContextUtils.getUserName());
//				inspectingIndicator.setMaterielCode(materielCode);
//				inspectingIndicator.setMaterielName(materielName);
				inspectingIndicator.setFigureNumber(figureNumber);
				inspectingIndicator.setMaterielType(materielType);
				String key = figureNumber + materielType;
				productNameMap.put(key, inspectingIndicator);
				//先保存检验标准文件,保存对应的ID
				IndicatorAttach attach = indicatorAttachManager.addIndicatorAttachCCM(file,fileName,materielType,ContextUtils.getCompanyId());
				inspectingIndicator.setIndicatorAttachId(attach.getId());
				inspectingIndicatorDao.save(inspectingIndicator);
				Map<String,Boolean> existItemMap = new HashMap<String, Boolean>();
				Cell itemItitleCell = cellMap.get("检验项目");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为检验项目的单元格!&nbsp;&nbsp;</br>");
				}
				Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
				df = new DecimalFormat("0.00000");
				boolean isGaoYi = false;//
				int orderNum = 0;
				Iterator<Row> rows = sheet.rowIterator();
				while(rows.hasNext()){
					row = rows.next();
					//标题以后再执行
					if(row.getRowNum() <= itemTitleRowNum){
						continue;
					}
					//itemName:检验指标
					Cell cell = row.getCell(itemItitleCell.getColumnIndex());
					if(cell == null){
						continue;
					}
					String parentName=null,itemName = (ExcelUtil.getCellValue(cell)+"").trim(),method=null,countType=null,unit=null;
					if(StringUtils.isEmpty(itemName)){
						continue;
					}
					itemName = CommonUtil1.replaceSymbols(itemName);
					Cell tempCell = cellMap.get("检验类别");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验类别】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					parentName=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("检验项目");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验项目】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					itemName=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("统计类型");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【统计类型】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					countType=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("单位");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【单位】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					unit=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("检验方法");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验方法】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					method=(ExcelUtil.getCellValue(cell)+"").trim();					
				
					String inspectionLevel = null,aqlStandard = null,standards = "",remark="",massParameter="";
					String isInEquipment="否";//是否集成设备
					tempCell = cellMap.get("是否集成设备");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【是否集成设备】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						isInEquipment = (ExcelUtil.getCellValue(cell)+"");
					}
					tempCell = cellMap.get("检验水平");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验水平】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						inspectionLevel = (ExcelUtil.getCellValue(cell)+"").trim().toUpperCase();
						if(StringUtils.isNotEmpty(inspectionLevel)&&!isGaoYi){
							if(levelMap.containsKey(inspectionLevel)){
								inspectionLevel = levelMap.get(inspectionLevel);
							}
						}
					}
					tempCell = cellMap.get("AQL");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【AQL】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						aqlStandard = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(aqlStandard)
								&&!aqlMap.containsKey(aqlStandard.toUpperCase())){
							throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【AQL】的单元格!&nbsp;&nbsp;</br>");
						}
						aqlStandard = aqlStandard.toUpperCase();
					}
					if(StringUtils.isEmpty(aqlStandard)){
						aqlStandard = defaultAql;
					}
				
					String inspectionAmount = null;
					tempCell = cellMap.get("抽样数量");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【抽样数量】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						inspectionAmount= (ExcelUtil.getCellValue(cell)+"").trim();
	//					if(StringUtils.isNotEmpty(value)){
	//						if(!CommonUtil.isDouble(value)){ 
	//							throw new AmbFrameException("SHEET"+k+"物料【"+materielName+"】中检验数量【"+value+"】不是有效数字!");
	//						}
	//						inspectionAmount = Double.valueOf(value).intValue();
	//					}
					}
					
					tempCell = cellMap.get("规格");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【规格】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						standards = (ExcelUtil.getCellValue(cell)+"").trim();
						standards = CommonUtil1.replaceSymbols(standards);
					}
					//值类型
					String valueType = ItemIndicator.VALUETYPE_DEFAULT;
					Double levela = null,levelb = null;
					tempCell = cellMap.get("上限");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为上限的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							//是角度值时
							if(CommonUtil1.isDegree(value)){
								valueType = ItemIndicator.VALUETYPE_DEGREE;
								levela = CommonUtil1.degreeStrToNumber(value);
							}else{
								if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"物料【"+materielType+"】中上限【"+value+"】不是有效数字!");
								}
								levela = Double.valueOf(df.format(Double.valueOf(value)));
							}
						}
					}
					tempCell = cellMap.get("下限");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为下限的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							//是角度值时
							if(CommonUtil1.isDegree(value)){
								valueType = ItemIndicator.VALUETYPE_DEGREE;
								levelb = CommonUtil1.degreeStrToNumber(value);
							}else{
								if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"物料类别【"+materielType+"】中下限【"+value+"】不是有效数字!");
								}
								levelb = Double.valueOf(df.format(Double.valueOf(value)));
							}
						}
					}
					tempCell = cellMap.get("质量参数");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【质量参数】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell !=null){
						massParameter= (ExcelUtil.getCellValue(cell)+"").trim();
					}
					tempCell = cellMap.get("备注");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为备注的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						
						remark = (ExcelUtil.getCellValue(cell)+"").trim();
					}
					
					InspectingItem inspectingItem = getInspectingItem(itemName,parentName, method, countType, unit,standards);
					//判断是否有重复的检验项目
					String existKey = parentName + "_" + itemName;
					if(existItemMap.containsKey(existKey)){
						throw new AmbFrameException("SHEET"+k+"物料类别【"+materielType+"】中存在相同的检验项目【"+itemName+"】,不能导入!");
					}else{
						existItemMap.put(existKey, true);
					}
					
					ItemIndicator itemIndicator = new ItemIndicator();
					itemIndicator.setInspectingIndicator(inspectingIndicator);
					itemIndicator.setCreatedTime(new Date());
					itemIndicator.setCompanyId(ContextUtils.getCompanyId());
					itemIndicator.setCreator(ContextUtils.getUserName());
					itemIndicator.setLastModifiedTime(new Date());
					itemIndicator.setLastModifier(ContextUtils.getUserName());
					itemIndicator.setInspectingItem(inspectingItem);
					itemIndicator.setInspectionLevel(inspectionLevel);
					itemIndicator.setOrderNum(++orderNum);
					itemIndicator.setIndicatorUnit(unit);
					itemIndicator.setCountType(countType);
					itemIndicator.setIndicatorMethod(method);
					itemIndicator.setIsInEquipment(isInEquipment);
					if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
						itemIndicator.setAqlStandard(inspectionLevel);
					}else{
						itemIndicator.setAqlStandard(aqlStandard);
					}
					itemIndicator.setSpecifications(standards);
					itemIndicator.setRemark(remark);
					itemIndicator.setInspectionAmount(inspectionAmount);
					itemIndicator.setLevela(levela);
					itemIndicator.setLevelb(levelb);
					itemIndicator.setMassParameter(massParameter);
					itemIndicator.setValueType(valueType);
					itemIndicatorDao.save(itemIndicator);
				}
				if(existItemMap.keySet().isEmpty()){
					throw new AmbFrameException("SHEET"+k+"物料类别【"+materielType+"】的检验项目为空,不能导入!");
				}
				sb.append("物料类别【"+materielType+"】的检验标准导入成功!!<br/>");
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
	  * 方法名: 更新产品的最新标志
	  * <p>功能说明：</p>
	  * @param indicator
	 */
	public void updateMaxVersionFlag(List<String> materialCodes){
		for(String materialCode : materialCodes){
			updateMaxVersionFlag(materialCode);
		}
	}
	
	/**
	  * 方法名: 更新产品的最新标志
	  * <p>功能说明：</p>
	  * @param indicator
	 */
	public void updateMaxVersionFlag(String materialCode){
		String hql = "select i.standardVersion from InspectingIndicator i where i.materielCode = ?";
		List<String> standardVersions = inspectingIndicatorDao.find(hql,materialCode);
		//判断最大的值
		String maxVersion = "";
		for(String standardVersion : standardVersions){
			if(StringUtils.isEmpty(maxVersion)){
				maxVersion = standardVersion;
				continue;
			}
			if(StringUtils.isEmpty(standardVersion)){
				continue;
			}
			if(isFirstGranterSecond(standardVersion.split("\\."),maxVersion.split("\\."),0)){
				maxVersion = standardVersion;
			}
		}
		if(StringUtils.isNotEmpty(maxVersion)){
			//更新最大版本号的标志为true,方便查询
			hql = "update InspectingIndicator i set i.isMax = true where i.materielCode = ? and i.standardVersion = ?";
			inspectingIndicatorDao.batchExecute(hql,materialCode,maxVersion);
			
			hql = "update InspectingIndicator i set i.isMax = false where i.materielCode = ? and i.standardVersion <> ?";
			inspectingIndicatorDao.batchExecute(hql,materialCode,maxVersion);
		}
	}
	
	/**
	  * 方法名:判断版本号格式的第一个字符数组是否大于第二字符数组 
	  * <p>功能说明：</p>
	  * @param firstStr
	  * @param secondStr
	  * @param index
	  * @return
	 */
	public static boolean isFirstGranterSecond(String[] firstStrs,String[] secondStrs,int index){
		if(index>=firstStrs.length
				||index>=secondStrs.length){
			return false;
		}else{
			if(CommonUtil1.isInteger(firstStrs[index])
					&&CommonUtil1.isInteger(secondStrs[index])){
				int firstNum = Integer.valueOf(firstStrs[index]);
				int secNum = Integer.valueOf(secondStrs[index]);
				if(firstNum>secNum){
					return true;
				}else if(firstNum < secNum){
					return false;
				}
			}else{
				int num = firstStrs[index].compareTo(secondStrs[index]);
				if(num > 0){
					return true;
				}else if(num < 0){
					return false;
				}
			}
		}
		//如果一样,继续比对
		if(index+1 < firstStrs.length 
			&& index+1  < secondStrs.length){
			return isFirstGranterSecond(firstStrs,secondStrs,index+1);
		}else{
			if(firstStrs.length>secondStrs.length){
				return true;
			}else{
				return false;
			}
		}
	}
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
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
						SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
						try {
							PropertyUtils.setProperty(obj,key.toString(),df.parse(json.get(key).toString()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}else if(Double.class.equals(type)){
						PropertyUtils.setProperty(obj,key.toString(),Double.valueOf(json.get(key).toString()));
					}else if(Integer.class.equals(type)){
						PropertyUtils.setProperty(obj,key.toString(),Integer.valueOf(json.get(key).toString()));
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
	
	public static void main(String[] args) {
		String ss = "55%";
	}
	
	
	/**
	  * 方法名:将以往的检验数据同步到SPC
	  * @param settings
	  * @param 检验项目ID,工序，起始日期，结束日期
	  * @param page
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void synchroCheckDatas(String id,String startDate,String endDate) throws Exception{
		// 根据检验项目ID获取检验项目
		ItemIndicator inspectingItemIndicator = this.getItemIndicator(Long.parseLong(id));
		if(inspectingItemIndicator != null && inspectingItemIndicator.getInspectingItem() != null && inspectingItemIndicator.getInspectingIndicator() != null && StringUtils.isNotEmpty(inspectingItemIndicator.getFeatureId())){
			List<IncomingInspectionActionsReport> inomIncomingInspectionActionsReports = incomingInspectionActionsReportManager.getListByInspectingItemIndicator(DateUtil.parseDate(startDate, "yyyy-MM-dd"), DateUtil.parseDate(endDate, "yyyy-MM-dd"), inspectingItemIndicator);
			for (IncomingInspectionActionsReport incomingInspectionActionsReport : inomIncomingInspectionActionsReports) {
				//此二行用于定义推送至SPC
				String allHisPatrolSpcSampleIds = incomingInspectionActionsReport.getSpcSampleIds()==null?"":incomingInspectionActionsReport.getSpcSampleIds();
				String allPatrolSpcSampleIds = ",";
				
				List<CheckItem> checkItems = incomingInspectionActionsReport.getCheckItems();
				for (CheckItem checkItem : checkItems) {
					if(StringUtils.isEmpty(checkItem.getCheckItemName()) || !checkItem.getCheckItemName().equals(inspectingItemIndicator.getInspectingItem().getItemName())){
						continue;
					}
					//处理数据 SPC
					String spcSampleIds = "";
					if(InspectingItem.COUNTTYPE_METERING.equals(checkItem.getCountType())){
						//获取检验 result
						String values = "";
						for (int i = 1; i <= 32; i++) {
							Object value = PropertyUtils.getProperty(checkItem, "result"+i);
							if(value != null){
								values += Double.valueOf(value.toString()) + ",";
							}
						}
						if(StringUtils.isNotEmpty(values)){
							QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureById(Long.valueOf(inspectingItemIndicator.getFeatureId()));
							if(qualityFeature != null){
								String hisSampleIds = checkItem.getSpcSampleIds();
								if(StringUtils.isNotEmpty(hisSampleIds)){
									allHisPatrolSpcSampleIds = allHisPatrolSpcSampleIds.replaceAll("," + hisSampleIds,",");
								}
								spcSampleIds = incomingInspectionActionsReportManager.insertSpcByResults(qualityFeature,incomingInspectionActionsReport.getInspectionDate(),hisSampleIds,values);
							}
							allPatrolSpcSampleIds += spcSampleIds;
						}
					}
					checkItem.setSpcSampleIds(spcSampleIds);
				}
				//删除不存在采集数据的子组(异步调用，不涉及报告的修改，不存在删除问题)
				/*String[] sampleIds = allHisPatrolSpcSampleIds.split(",");
				for(String sampleId : sampleIds){
					sampleId = sampleId.trim();
					if(StringUtils.isNotEmpty(sampleId)){
						List<SpcSgSample> list = spcSgSampleDao.find("from SpcSgSample s where s.id = ?",Long.valueOf(sampleId));
						if(list != null && list.size() > 0){
							SpcSgSample sample = list.get(0);
							spcSubGroupDao.delete(sample.getSpcSubGroup());
						}
					}
				}*/
				//设置最新的采集数据ID
				incomingInspectionActionsReport.setSpcSampleIds(allPatrolSpcSampleIds);
				incomingInspectionActionsReportManager.saveIncomingInspectionActionsReport(incomingInspectionActionsReport);
			}
		}
	}
}


