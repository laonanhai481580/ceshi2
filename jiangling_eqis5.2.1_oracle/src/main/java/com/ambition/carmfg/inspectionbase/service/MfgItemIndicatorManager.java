package com.ambition.carmfg.inspectionbase.service;

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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.checkinspection.service.MfgCheckInspectionReportManager;
import com.ambition.carmfg.entity.IndicatorAttach;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgCheckItem;
import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.carmfg.inspectionbase.dao.MfgInspectingIndicatorDao;
import com.ambition.carmfg.inspectionbase.dao.MfgInspectingItemDao;
import com.ambition.carmfg.inspectionbase.dao.MfgItemIndicatorDao;
import com.ambition.iqc.entity.InspectingItem;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.inspectionbase.service.ItemIndicatorManager;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.importutil.service.SpcImportManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ItemIndicatorManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class MfgItemIndicatorManager {
	@Autowired
	private MfgItemIndicatorDao mfgItemIndicatorDao;
	@Autowired
	private MfgInspectingIndicatorManager mfgInspectingIndicatorManager;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private ProductBomDao productBomDao;
	@Autowired
	private SpcImportManager spcImportManager;
	@Autowired
	private MfgInspectingIndicatorDao mfgInspectingIndicatorDao;
	@Autowired
	private MfgInspectingItemDao mfgInspectingItemDao;
	@Autowired
	private MfgCheckInspectionReportManager mfgCheckInspectionReportManager;
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	@Autowired
	private IndicatorAttachManager indicatorAttachManager;
	
	public MfgItemIndicator getItemIndicator(Long id){
		return mfgItemIndicatorDao.get(id);
	}
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteItemIndicator(Long id){
		mfgItemIndicatorDao.delete(id);
	}
	
	public Page<MfgItemIndicator> search(Page<MfgItemIndicator> page){
		return mfgItemIndicatorDao.list(page);
	}
	public Page<MfgItemIndicator> searchFeatures(Page<MfgItemIndicator> page){
		return mfgItemIndicatorDao.listFeatures(page);
	}
	public void clearDeleteBindFeature(){
		String hql = "update MfgItemIndicator set featureId = null where featureId not in (select id from QualityFeature)";
		mfgInspectingIndicatorDao.createQuery(hql).executeUpdate();
	}
	/**
	 * 添加检验项目
	 * @param inspectingIndicatorId
	 * @param inspectingItemIds
	 * @return
	 */
	public int addItemIndicator(Long inspectingIndicatorId,String inspectingItemIds){
		int addCount = 0;
		MfgInspectingIndicator mfgInspectingIndicator = mfgInspectingIndicatorDao.get(inspectingIndicatorId);
		for(String inspectingItemId : inspectingItemIds.split(",")){
			if(StringUtils.isNotEmpty(inspectingItemId)){
				MfgInspectingItem mfgInspectingItem = mfgInspectingItemDao.get(Long.valueOf(inspectingItemId));
				//只能添加最子级为检验项目
				if(mfgInspectingItem.getItemChildren().isEmpty()){
					//检查是否已经添加
					String hql = "select count(i.id) from MfgItemIndicator i where i.mfgInspectingItem = ? and i.mfgInspectingIndicator = ?";
					List<?> list = mfgItemIndicatorDao.find(hql,mfgInspectingItem,mfgInspectingIndicator);
					if(Integer.valueOf(list.get(0).toString())==0){
						MfgItemIndicator mfgItemIndicator = new MfgItemIndicator();
						mfgItemIndicator.setMfgInspectingIndicator(mfgInspectingIndicator);
						mfgItemIndicator.setMfgInspectingItem(mfgInspectingItem);
						mfgItemIndicatorDao.save(mfgItemIndicator);
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
	public Map<String,Integer> copyInspectingIndicators(Long inspectingIndicatorId,String targetBomId,String targetWorkProcedure){
		int addCount = 0,repeatCount=0;
		MfgInspectingIndicator mfgInspectingIndicator = mfgInspectingIndicatorDao.get(inspectingIndicatorId);
		Map<String,List<String>> productNameMap = new HashMap<String, List<String>>();
		if(StringUtils.isNotEmpty(targetBomId)){
			ProductBom productBom = productBomDao.get(Long.valueOf(targetBomId));
			if(!isExistInspectingIndicator(null,productBom.getMaterielCode(),targetWorkProcedure,mfgInspectingIndicator.getStandardVersion())){
				//复制抽样的编码
				MfgInspectingIndicator targetInspectingIndicator = new MfgInspectingIndicator();
				targetInspectingIndicator.setWorkingProcedure(targetWorkProcedure);
				targetInspectingIndicator.setMaterielCode(productBom.getMaterielCode());
				targetInspectingIndicator.setMaterielName(productBom.getMaterielName());
				targetInspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
				targetInspectingIndicator.setCreatedTime(new Date());
				targetInspectingIndicator.setCreator(ContextUtils.getUserName());
				targetInspectingIndicator.setLastModifiedTime(targetInspectingIndicator.getCreatedTime());
				targetInspectingIndicator.setLastModifier(ContextUtils.getUserName());
				targetInspectingIndicator.setRemark(mfgInspectingIndicator.getRemark());
				targetInspectingIndicator.setStandardVersion(mfgInspectingIndicator.getStandardVersion());
				targetInspectingIndicator.setBusinessUnitName(ContextUtils.getSubCompanyName());
				mfgInspectingIndicatorDao.save(targetInspectingIndicator);
				if(!productNameMap.containsKey(productBom.getMaterielCode())){
					productNameMap.put(productBom.getMaterielCode(),new ArrayList<String>());
				}
				productNameMap.get(productBom.getMaterielCode()).add(targetWorkProcedure);
				//复制项目
				for(MfgItemIndicator mfgItemIndicator : mfgInspectingIndicator.getMfgItemIndicators()){
					MfgItemIndicator targetIndicator = new MfgItemIndicator();
					targetIndicator.setCompanyId(ContextUtils.getCompanyId());
					targetIndicator.setCreatedTime(targetInspectingIndicator.getCreatedTime());
					targetIndicator.setCreator(targetInspectingIndicator.getCreator());
					targetIndicator.setLastModifiedTime(targetInspectingIndicator.getCreatedTime());
					targetIndicator.setLastModifier(ContextUtils.getUserName());
					targetIndicator.setAqlStandard(mfgItemIndicator.getAqlStandard());
					targetIndicator.setMfgInspectingIndicator(targetInspectingIndicator);
					targetIndicator.setMfgInspectingItem(mfgItemIndicator.getMfgInspectingItem());
					targetIndicator.setInspectionAmount(mfgItemIndicator.getInspectionAmount());
					targetIndicator.setInspectionLevel(mfgItemIndicator.getInspectionLevel());
					targetIndicator.setLevela(mfgItemIndicator.getLevela());
					targetIndicator.setLevelb(mfgItemIndicator.getLevelb());
					targetIndicator.setSpecifications(mfgItemIndicator.getSpecifications());
					targetIndicator.setTotalPoints(mfgItemIndicator.getTotalPoints());
					targetIndicator.setRemark(mfgItemIndicator.getRemark());
					mfgItemIndicatorDao.save(targetIndicator);
				}
				addCount++;
			}else{
				repeatCount++;
			}
		}
		
		//更新对应的最新标志
		for(String materielCode : productNameMap.keySet()){
			List<String> workingProcedures = new ArrayList<String>();
			for(String workingProcedure : workingProcedures){
				updateMaxVersionFlag(materielCode, workingProcedure);
			}
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
	public void saveItemIndicator(MfgInspectingIndicator mfgInspectingIndicator,MfgInspectingItem mfgInspectingItem,JSONObject params) throws Exception{
		if(!mfgInspectingItem.getItemChildren().isEmpty()){
			throw new RuntimeException("检验项目还有子节点,不能设置!");
		}
		params = convertJsonObject(params);
		List<MfgItemIndicator> mfgItemIndicators = mfgItemIndicatorDao.getItemIndicatorsByInspectingItem(mfgInspectingItem);
		boolean isMySel = false;
		for(MfgItemIndicator mfgItemIndicator : mfgItemIndicators){
			if(mfgInspectingIndicator.getId().toString().equals(mfgItemIndicator.getMfgInspectingIndicator().getId().toString())){
				isMySel = true;
				for(Object key : params.keySet()){
					setProperty(mfgItemIndicator,key,params);
				}
				String baseType = sampleSchemeManager.getUseBaseType().getBaseType();
				if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
					mfgItemIndicator.setAqlStandard(mfgItemIndicator.getInspectionLevel());
				}
				mfgItemIndicator.setLastModifiedTime(new Date());
				mfgItemIndicator.setLastModifier(ContextUtils.getUserName());
				mfgItemIndicatorDao.save(mfgItemIndicator);
				logUtilDao.debugLog("保存", mfgItemIndicator.toString());
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
	public List<MfgItemIndicator> getAllItemIndicators(Long parentItemIndicatorId){
		return mfgItemIndicatorDao.getAllItemIndicators(parentItemIndicatorId);
	}
	
	/**
	 * 获取所有已设置的模型的指标
	 * @return
	 */
	public List<MfgItemIndicator> getAllItemIndicators(){
		return mfgItemIndicatorDao.getAllItemIndicators();
	}
	
	/**
	 * 根据物料获取 检验项目
	 * @param productBom
	 * @return
	 */
	public List<MfgItemIndicator> getAllItemIndicatorsByProductBomAndWorkProcedure(ProductBom productBom,String workProcedure){
		MfgInspectingIndicator mfgInspectingIndicator = new MfgInspectingIndicator();
		mfgInspectingIndicator = mfgInspectingIndicatorManager.getAllInspectingIndicatorsByProductBomAndWorkProduce(productBom.getMaterielCode(),workProcedure);
		String hql = "from MfgItemIndicator i where i.mfgInspectingIndicator = ? and i.companyId = ?";
		return mfgItemIndicatorDao.find(hql,new Object[]{mfgInspectingIndicator,ContextUtils.getCompanyId()});
	}
	
	/**
	 * 设置评价
	 * @throws Exception 
	 */
	public void setItemIndicator(MfgInspectingIndicator mfgInspectingIndicator,MfgInspectingItem mfgInspectingItem,String isSet,JSONObject params) throws Exception{
		if(!mfgInspectingItem.getItemChildren().isEmpty()){
			throw new RuntimeException("项目指标还有子节点,不能设置!");
		}
		
		if("yes".equals(isSet)){
			String hql = "select count(i.id) from MfgItemIndicator i where i.mfgInspectingIndicator = ? and i.mfgInspectingItem = ?";
			List<?> list = mfgItemIndicatorDao.find(hql,mfgInspectingIndicator,mfgInspectingItem);
			if(Integer.valueOf(list.get(0).toString())>0){
				throw new RuntimeException("检验项目指标已在其他位置设置!");
			}
			MfgItemIndicator mfgItemIndicator = new MfgItemIndicator();
			mfgItemIndicator.setCompanyId(ContextUtils.getCompanyId());
			mfgItemIndicator.setCreatedTime(new Date());
			mfgItemIndicator.setCompanyId(ContextUtils.getCompanyId());
			mfgItemIndicator.setCreator(ContextUtils.getUserName());
			mfgItemIndicator.setLastModifiedTime(new Date());
			mfgItemIndicator.setLastModifier(ContextUtils.getUserName());
			mfgItemIndicator.setMfgInspectingItem(mfgInspectingItem);
			mfgItemIndicator.setMfgInspectingIndicator(mfgInspectingIndicator);
			params = convertJsonObject(params);
			for(Object key : params.keySet()){
				setProperty(mfgItemIndicator,key,params);
			}
			mfgItemIndicatorDao.save(mfgItemIndicator);
			logUtilDao.debugLog("保存", mfgItemIndicator.toString());
		}else{
			String hql = "from MfgItemIndicator i where i.mfgInspectingIndicator = ? and i.mfgInspectingItem = ?";
			List<MfgItemIndicator> list = mfgItemIndicatorDao.find(hql,mfgInspectingIndicator,mfgInspectingItem);
			for(MfgItemIndicator mfgItemIndicator : list){
				mfgItemIndicatorDao.delete(mfgItemIndicator);
			}
			logUtilDao.debugLog("删除","取消料号" + mfgInspectingIndicator.getMaterielCode() + "的检验项目" + mfgInspectingItem.getItemName());
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
	 * 检查是否存在相同名称的物料BOM
	 * @param id
	 * @param name
	 * @return
	 */
	private MfgInspectingItem getInspectingItem(String name,String parentName,String method,String countType,String unit){
		String hql = " from MfgInspectingItem i where i.companyId = ? and i.itemName = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(name);
		if(StringUtils.isNotEmpty(parentName)){
			hql += " and i.itemParent.itemName = ?";
			searchParams.add(parentName);
		}
		List<MfgInspectingItem> list=new ArrayList<MfgInspectingItem>();
		try {
			list= mfgInspectingItemDao.find(hql,searchParams.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!list.isEmpty()){
			MfgInspectingItem item = list.get(0);
			return item;
		}else{
			MfgInspectingItem parent = null;
			if(StringUtils.isNotEmpty(parentName)){
				parent = getInspectingItem(parentName,null,null,null,null);
			}
			MfgInspectingItem inspectingItem = new MfgInspectingItem();
			inspectingItem.setCreatedTime(new Date());
			inspectingItem.setCompanyId(ContextUtils.getCompanyId());
			inspectingItem.setCreator(ContextUtils.getUserName());
			inspectingItem.setLastModifiedTime(new Date());
			inspectingItem.setLastModifier(ContextUtils.getUserName());
			inspectingItem.setItemName(name);
			inspectingItem.setItemParent(parent);
			if(parent != null){
				inspectingItem.setItemLevel(parent.getItemLevel()+1);
			}
			inspectingItem.setMethod(method);
			inspectingItem.setCountType(countType);
			inspectingItem.setUnit(unit);
			mfgInspectingItemDao.save(inspectingItem);
			return inspectingItem;
		}
	}
	
	/**
	 * 检查是否存在相同名称的物料抽样标准
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistInspectingIndicator(Long id,String model,String workingProcedure,String standardVersion){
		String hql = "select count(i.id) from MfgInspectingIndicator i where i.companyId = ? and i.model = ? and i.workingProcedure = ? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(model);
		searchParams.add(workingProcedure);
		/*if(standardVersion != null){
			hql += " and i.standardVersion =  ?";
			searchParams.add(standardVersion);
		}*/
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		if(ContextUtils.getSubCompanyName()!=null){
			hql += " and i.businessUnitName = ?";
			searchParams.add(ContextUtils.getSubCompanyName());
		}
		List<?> list = mfgInspectingIndicatorDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
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
			}
			//查询可选的AQL标准
			List<Option> aqlOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_acceptance_quality_limit");
			Map<String,Boolean> aqlMap = new HashMap<String, Boolean>();
			
			//查询可选的工序
			List<Option> workProcedures = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_procedure");
			Map<String,Boolean> workProcedureMap = new HashMap<String, Boolean>();
			for(Option o : workProcedures){
				workProcedureMap.put(o.getValue(),true);
			}
			StringBuffer canSelAqls = new StringBuffer();
			for(Option option : aqlOptions){
				aqlMap.put(option.getValue(),true);
				if(canSelAqls.length()>0){
					canSelAqls.append(",");
				}
				canSelAqls.append(option.getValue());
			}
			
			Map<String,String> fieldMap = getFieldMap();
			Workbook book = WorkbookFactory.create(inputStream);
			int totalSheets = book.getNumberOfSheets();
			Map<String,Cell> cellMap = new HashMap<String, Cell>();
			Map<String,org.apache.poi.ss.util.CellRangeAddress> cellRangeAddressMap = new HashMap<String, org.apache.poi.ss.util.CellRangeAddress>();
			for(int k=0;k<totalSheets;k++){
				Sheet sheet = book.getSheetAt(k);
				Row row = sheet.getRow(0);
				//隐藏的sheet不处理
				if(book.isSheetHidden(k)){
					continue;
				}
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
				//机种
			    Cell modelCell = ExcelUtil.getNextCell(sheet,cellMap.get("机种"), cellRangeAddressMap);
			    String model = null;
			    if(modelCell != null){
			    	model = (ExcelUtil.getCellValue(modelCell)+"").trim();
			    }
			    if(model==null||"".equals(model)){
			    	throw new  AmbFrameException("Sheet"+k+"的机种值不能为空");
			    }
				//机种名称
			    Cell modelNameCell = ExcelUtil.getNextCell(sheet,cellMap.get("机种名称"), cellRangeAddressMap);
			    String modelName = null;
			    if(modelNameCell != null){
			    	modelName = (ExcelUtil.getCellValue(modelNameCell)+"").trim();
			    }
			    if(modelName==null||"".equals(modelName)){
			    	throw new  AmbFrameException("Sheet"+k+"的机种名称值不能为空");
			    }
				//工序
			    Cell workingProcedureCell = ExcelUtil.getNextCell(sheet,cellMap.get("检验工序"), cellRangeAddressMap);
			    String workingProcedure = null;
			    if(workingProcedureCell != null){
			    	workingProcedure = (ExcelUtil.getCellValue(workingProcedureCell)+"").trim();
			    }
			    if(workingProcedure==null||"".equals(workingProcedure)){
			    	throw new  AmbFrameException("Sheet"+k+"的检验工序值不能为空");
			    }
//				workingProcedure = workingProcedure.replaceAll(" ","");
				if(!workProcedureMap.containsKey(workingProcedure)){
					throw new AmbFrameException("SHEET"+k+"<font color=red>工序【"+workingProcedure+"】还没有维护!</font>&nbsp;&nbsp;</br>");
				}
				//版本号
			    Cell standardVersionCell = ExcelUtil.getNextCell(sheet,cellMap.get("版本号"), cellRangeAddressMap);
			    String standardVersion = null;
			    if(standardVersionCell != null){
			    	standardVersion = (ExcelUtil.getCellValue(standardVersionCell)+"").trim();
			    }
				row = sheet.getRow(4);//列头
				Map<String,Integer> columnMap = new HashMap<String,Integer>();
				for(int j=0;;j++){
					Cell cell = row.getCell(j);
					if(cell==null){
						break;
					}
					String value = cell.getStringCellValue();
					if(fieldMap.containsKey(value)){
						columnMap.put(value,j);
					}
				}

				if(isExistInspectingIndicator(null,model, workingProcedure,standardVersion)){
					throw new AmbFrameException("SHEET"+k+"【"+sheet.getSheetName()+"】中工序【"+workingProcedure+"】,机种【"+model+"】的检验标准已经存在,不能导入！");
				}
				MfgInspectingIndicator inspectingIndicator = new MfgInspectingIndicator();
				inspectingIndicator.setCreatedTime(new Date());
				inspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
				inspectingIndicator.setCreator(ContextUtils.getUserName());
				inspectingIndicator.setBusinessUnitName(ContextUtils.getSubCompanyName());
				inspectingIndicator.setLastModifiedTime(new Date());
				inspectingIndicator.setLastModifier(ContextUtils.getUserName());
				inspectingIndicator.setModel(model);
				inspectingIndicator.setModelName(modelName);
				inspectingIndicator.setWorkingProcedure(workingProcedure);
				
				//先保存检验标准文件,保存对应的ID
				IndicatorAttach attach = indicatorAttachManager.addIndicatorAttach(file,fileName,model,modelName,ContextUtils.getCompanyId());
				inspectingIndicator.setIndicatorAttachId(attach.getId());
				mfgInspectingIndicatorDao.save(inspectingIndicator);
				
				//检查相同的父级名称下重复
				Map<String,Boolean> existItemMap = new HashMap<String, Boolean>();
				Iterator<Row> rows = sheet.rowIterator();
				Cell itemItitleCell = cellMap.get("检验项目");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为检验项目的单元格!&nbsp;&nbsp;</br>");
				}
				 itemItitleCell = cellMap.get("检验类别");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为检验类别的单元格!&nbsp;&nbsp;</br>");
				}
				Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
				df = new DecimalFormat("0.00");
				int orderNum = 0;
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
					String parentName=null,itemName = (ExcelUtil.getCellValue(cell)+"").trim(),method=null,countType=null,unit=null,isJnUnit="否",isInEquipment="否";
					Integer inAmountFir=null, inAmountPatrol=null, inAmountEnd=null;
					if(StringUtils.isEmpty(itemName)){
						continue;
					}
					itemName = CommonUtil1.replaceSymbols(itemName);
					//检验类别
					Cell tempCell = cellMap.get("检验类别");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验类别】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					parentName=(ExcelUtil.getCellValue(cell)+"").trim();
					//检验项目
					tempCell = cellMap.get("检验项目");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验项目】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					itemName=(ExcelUtil.getCellValue(cell)+"").trim();
					if(itemName==null){
						throw new AmbFrameException("【检验项目】不能为空！;</br>");
					}
					itemName=itemName.replace("\\", "/");
					itemName=itemName.replaceAll("\"", "");
					itemName=itemName.replaceAll("'", "");
					//统计类型
					tempCell = cellMap.get("统计类型");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【统计类型】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					countType=(ExcelUtil.getCellValue(cell)+"").trim();
					//单位
					tempCell = cellMap.get("单位");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【单位】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					unit=(ExcelUtil.getCellValue(cell)+"").trim();
					//检验方法
					tempCell = cellMap.get("检验方法");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【检验方法】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					method=(ExcelUtil.getCellValue(cell)+"").trim();
					MfgInspectingItem inspectingItem = getInspectingItem(itemName,parentName, method, countType, unit);

					//ItemIndicator
					String specifications = "",remark="",massParameter="";
					//规格
					tempCell = cellMap.get("规格");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【规格】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						specifications = (ExcelUtil.getCellValue(cell)+"").trim();
						specifications = CommonUtil1.replaceSymbols(specifications);
					}
					//首件检验数量
					tempCell = cellMap.get("首件检验数量");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【首件检验数量】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isInteger(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中首件检验数量【"+value+"】不是有效数字!");
							}
							inAmountFir = Integer.valueOf(value);
						}
					}
					//巡检检验数量
					tempCell = cellMap.get("IPQC检验数量");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【IPQC检验数量】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isInteger(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中IPQC检验数量【"+value+"】不是有效数字!");
							}
							inAmountPatrol = Integer.valueOf(value);
						}
					}
					//未件检验数量
					tempCell = cellMap.get("未件检验数量");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【未件检验数量】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isInteger(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中未件检验数量【"+value+"】不是有效数字!");
							}
							inAmountEnd = Integer.valueOf(value);
						}
					}
					//是否IPQC测试项
					tempCell = cellMap.get("是否IPQC测试项");
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						isJnUnit = (ExcelUtil.getCellValue(cell)+"").trim();
					}
					//是否集成设备
					tempCell = cellMap.get("是否集成设备");
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						isInEquipment = (ExcelUtil.getCellValue(cell)+"").trim();
					}
					//值类型
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
								levela = CommonUtil1.degreeStrToNumber(value);
							}else{
								if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中上限【"+value+"】不是有效数字!");
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
								levelb = CommonUtil1.degreeStrToNumber(value);
							}else{
								if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中下限【"+value+"】不是有效数字!");
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
					tempCell = cellMap.get("测试大项");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为测试大项的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						remark = (ExcelUtil.getCellValue(cell)+"").trim();
					}
					
					//判断是否有重复的检验项目
					String existKey = parentName + "_" + itemName+"_";
					if(existItemMap.containsKey(existKey)){
						throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中存在相同的检验项目【"+itemName+"】,不能导入!");
					}else{
						existItemMap.put(existKey, true);
					}
					
					MfgItemIndicator itemIndicator = new MfgItemIndicator();
					itemIndicator.setMfgInspectingIndicator(inspectingIndicator);
					itemIndicator.setCreatedTime(new Date());
					itemIndicator.setCompanyId(ContextUtils.getCompanyId());
					itemIndicator.setCreator(ContextUtils.getUserName());
					itemIndicator.setLastModifiedTime(new Date());
					itemIndicator.setLastModifier(ContextUtils.getUserName());
					itemIndicator.setMfgInspectingItem(inspectingItem);
					itemIndicator.setOrderNum(++orderNum);
					itemIndicator.setUnit(unit);
					itemIndicator.setCountType(countType);
					itemIndicator.setMethod(method);
					itemIndicator.setSpecifications(specifications);
					itemIndicator.setRemark(remark);
					itemIndicator.setLevela(levela);
					itemIndicator.setLevelb(levelb);
					itemIndicator.setMassParameter(massParameter);
					itemIndicator.setInAmountFir(inAmountFir);
					itemIndicator.setInAmountPatrol(inAmountPatrol);
					itemIndicator.setInAmountEnd(inAmountEnd);
					itemIndicator.setIsJnUnit(isJnUnit);
					itemIndicator.setIsInEquipment(isInEquipment);
					mfgItemIndicatorDao.save(itemIndicator);
				}
				if(existItemMap.keySet().isEmpty()){
					throw new AmbFrameException("SHEET"+k+"机种【"+model+"】的检验项目为空,不能导入!");
				}
				sb.append("机种【"+model+"】的检验标准导入成功!<br/>");
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
	  * @param materialCode 物料代码
	  * @param workingProcedure 工序
	 */
	public void updateMaxVersionFlag(String materialCode,String workingProcedure){
		String hql = "select i.standardVersion from MfgInspectingIndicator i where i.materielCode = ? and i.workingProcedure = ?";
		List<String> standardVersions = mfgInspectingIndicatorDao.find(hql,materialCode,workingProcedure);
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
			if(ItemIndicatorManager.isFirstGranterSecond(standardVersion.split("\\."),maxVersion.split("\\."),0)){
				maxVersion = standardVersion;
			}
		}
		if(StringUtils.isNotEmpty(maxVersion)){
			//更新最大版本号的标志为true,方便查询
			hql = "update MfgInspectingIndicator i set i.isMax = true where i.materielCode = ? and i.workingProcedure = ? and i.standardVersion = ?";
			mfgInspectingIndicatorDao.batchExecute(hql,materialCode,workingProcedure,maxVersion);
			
			hql = "update MfgInspectingIndicator i set i.isMax = false where i.materielCode = ? and i.workingProcedure = ? and i.standardVersion <> ?";
			mfgInspectingIndicatorDao.batchExecute(hql,materialCode,workingProcedure,maxVersion);
		}
	}
	
	
	/**
	 * 导入标准
	 * @param file
	 * @throws Exception
	 */
	public String importPatrol(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		try {
			String baseType = sampleSchemeManager.getUseBaseType().getBaseType();
			Workbook book = WorkbookFactory.create(new FileInputStream(file));
			int totalSheets = book.getNumberOfSheets();
			for(int k=0;k<totalSheets;k++){
				Sheet sheet = book.getSheetAt(k);
				Row row = sheet.getRow(0);
				if(row == null){
					continue;
				}
				//检查相同的父级名称下重复
				Map<String,Boolean> existItemMap = new HashMap<String, Boolean>();
				Iterator<Row> rows = sheet.rowIterator();
				rows.next();//标题行
				rows.next();//物料编码行
				rows.next();//订单位号行
				rows.next();//检验结果判定
				rows.next();//检验项目标题
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				while(rows.hasNext()){
					row = rows.next();
					Object parentName = ExcelUtil.getCellValue(row.getCell(0));
					if(parentName == null||parentName.toString().equals("")){
						continue;
					}
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("parentName",parentName);
					map.put("itemName", ExcelUtil.getCellValue(row.getCell(1)));
					map.put("countType", ExcelUtil.getCellValue(row.getCell(2)));
					map.put("method", ExcelUtil.getCellValue(row.getCell(3)));
					map.put("unit", ExcelUtil.getCellValue(row.getCell(4)));
					map.put("inspectionLevel", ExcelUtil.getCellValue(row.getCell(5)));
					map.put("aqlStandard", ExcelUtil.getCellValue(row.getCell(6)));
					map.put("inspectionAmount", ExcelUtil.getCellValue(row.getCell(7)));
					map.put("specifications", ExcelUtil.getCellValue(row.getCell(8)));
					map.put("levela", ExcelUtil.getCellValue(row.getCell(9)));
					map.put("levelb", ExcelUtil.getCellValue(row.getCell(10)));
					list.add(map);
				}
				String hql = "from MfgInspectingIndicator i where i.workingProcedure like ?";
				List<MfgInspectingIndicator> indicators = mfgInspectingIndicatorDao.find(hql,sheet.getSheetName().trim() + "%");
				Map<String,Boolean> existMap = new HashMap<String,Boolean>();
				Map<String,MfgInspectingItem> itemMap = new HashMap<String, MfgInspectingItem>();
				for(MfgInspectingIndicator indicator : indicators){
					existItemMap.clear();
					itemMap.clear();
					int orderNum = 0;
					for(MfgItemIndicator itemIndicator : indicator.getMfgItemIndicators()){
						MfgInspectingItem parent = itemIndicator.getMfgInspectingItem().getItemParent();
						existMap.put((parent==null?"":parent.getItemName()) + "_" + itemIndicator.getMfgInspectingItem().getItemName(),true);
						if(itemIndicator.getOrderNum() != null && itemIndicator.getOrderNum()>orderNum){
							orderNum = itemIndicator.getOrderNum();
						}
					}
					for(Map<String,Object> map : list){
						String parentName = map.get("parentName").toString();
						String itemName = map.get("itemName").toString();
						if(StringUtils.isEmpty(parentName)||StringUtils.isEmpty(itemName)){
							continue;
						}
						String key = parentName + "_" + itemName;
						if(existItemMap.containsKey(key)){
							continue;
						}
						existItemMap.put(key,true);
						String countType = map.get("countType")==null?"":map.get("countType").toString();
						String method = map.get("method")==null?"":map.get("method").toString();
						String unit = map.get("unit")==null?"":map.get("unit").toString();
						String inspectionLevel = map.get("inspectionLevel")==null?"":map.get("inspectionLevel").toString();
						String aqlStandard = map.get("aqlStandard")==null?"":map.get("aqlStandard").toString();
						Integer inspectionAmount = map.get("inspectionAmount")==null||StringUtils.isEmpty(map.get("inspectionAmount").toString())?1:Integer.valueOf(map.get("inspectionAmount").toString());
						String specifications = map.get("specifications")==null?"":map.get("specifications").toString();
						Double levela = map.get("levela")==null||StringUtils.isEmpty(map.get("levela").toString())?null:Double.valueOf(map.get("levela").toString());
						Double levelb = map.get("levelb")==null||StringUtils.isEmpty(map.get("levelb").toString())?null:Double.valueOf(map.get("levelb").toString());
						if(!itemMap.containsKey(key)){
							itemMap.put(key,getInspectingItem(itemName, parentName, method, countType, unit));
						}
						MfgItemIndicator itemIndicator = new MfgItemIndicator();
						itemIndicator.setMfgInspectingIndicator(indicator);
						itemIndicator.setCreatedTime(new Date());
						itemIndicator.setCompanyId(ContextUtils.getCompanyId());
						itemIndicator.setCreator(ContextUtils.getUserName());
						itemIndicator.setLastModifiedTime(new Date());
						itemIndicator.setLastModifier(ContextUtils.getUserName());
						itemIndicator.setMfgInspectingItem(itemMap.get(key));
						itemIndicator.setInspectionLevel(inspectionLevel);
						itemIndicator.setOrderNum(++orderNum);
						if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
							itemIndicator.setAqlStandard(inspectionLevel);
						}else{
							itemIndicator.setAqlStandard(aqlStandard);
						}
						itemIndicator.setSpecifications(specifications);
						itemIndicator.setInspectionAmount(inspectionAmount);
						itemIndicator.setLevela(levela);
						itemIndicator.setLevelb(levelb);
						mfgItemIndicatorDao.save(itemIndicator);
					}
				}
			}
			return sb.toString();
		}finally{
			file.delete();
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
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
		MfgItemIndicator inspectingItemIndicator = this.getItemIndicator(Long.parseLong(id));
		if(inspectingItemIndicator != null && inspectingItemIndicator.getMfgInspectingItem() != null && inspectingItemIndicator.getMfgInspectingIndicator() != null && StringUtils.isNotEmpty(inspectingItemIndicator.getFeatureId())){
			List<MfgCheckInspectionReport> inomIncomingInspectionActionsReports = mfgCheckInspectionReportManager.getListByInspectingItemIndicator(DateUtil.parseDate(startDate, "yyyy-MM-dd"), DateUtil.parseDate(endDate, "yyyy-MM-dd"), inspectingItemIndicator);
			for (MfgCheckInspectionReport mfgCheckInspectionReport : inomIncomingInspectionActionsReports) {
				if(!InspectionPointTypeEnum.FIRSTINSPECTION.equals(mfgCheckInspectionReport.getInspectionPointType())&&"欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
					continue;
				}
				//此二行用于定义推送至SPC
				String allHisPatrolSpcSampleIds = mfgCheckInspectionReport.getSpcSampleIds()==null?"":mfgCheckInspectionReport.getSpcSampleIds();
				String allPatrolSpcSampleIds = ",";
				List<MfgCheckItem> checkItems = mfgCheckInspectionReport.getCheckItems();
				for (MfgCheckItem checkItem : checkItems) {
					if(StringUtils.isEmpty(checkItem.getCheckItemName()) || !checkItem.getCheckItemName().equals(inspectingItemIndicator.getMfgInspectingItem().getItemName())){
						continue;
					}
					/*if(StringUtils.isEmpty(checkItem.getFeatureId()) || !checkItem.getFeatureId().equals(inspectingItemIndicator.getFeatureId())){
						continue;
					}*/
					//处理数据 SPC
					String spcSampleIds = "";
					if(InspectingItem.COUNTTYPE_METERING.equals(checkItem.getCountType())||"计量型".equals(checkItem.getCountType())){
						//获取检验 result
						String values = "";
						List<Double> valueList=new ArrayList<Double>();
						for (int i = 1; i <= 32; i++) {
							Object value = PropertyUtils.getProperty(checkItem, "result"+i);
							if(value != null){
								DecimalFormat df = new DecimalFormat("#,##0.00000");
								values += df.format(Double.valueOf(value.toString())) + ",";
								valueList.add(Double.valueOf(value.toString()));
							}
						}
						if(StringUtils.isNotEmpty(values)){
							QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureById(Long.valueOf(inspectingItemIndicator.getFeatureId()));
							List<String> hisIds=new ArrayList<String>();
							if(qualityFeature != null){
								String hisSampleIds = checkItem.getSpcSampleIds();
								if(hisSampleIds!=null&&hisSampleIds.indexOf(",")>-1){
									String [] deleteId=hisSampleIds.split(",");
	        				        for(String hisId:deleteId){
	        				        	if(!hisId.equals("")&&hisId!=null){
	        				        		hisIds.add(hisId);
	        				        	}   				        	
	        				        }
								}
								//采集层别信息
								Map<String,String> layerMap=new HashMap<String, String>();
	    						for(FeatureLayer layer : qualityFeature.getFeatureLayers()){
	    							if(layer.getDetailName().equals("机台")&&StringUtils.isNotEmpty(checkItem.getEquipmentNo())){
	    								layerMap.put(layer.getDetailCode(), checkItem.getEquipmentNo());
	    							}else if(layer.getDetailName().equals("班组")&&StringUtils.isNotEmpty(mfgCheckInspectionReport.getWorkGroupType())){
	    								layerMap.put(layer.getDetailCode(), mfgCheckInspectionReport.getWorkGroupType());
	    							}else if(layer.getDetailName().equals("检验员")&&StringUtils.isNotEmpty(mfgCheckInspectionReport.getInspector())){
	    								layerMap.put(layer.getDetailCode(), mfgCheckInspectionReport.getInspector());
	    							}else if(layer.getDetailName().equals("检验")){
	    								String inspectionType="";
	    								if(InspectionPointTypeEnum.FIRSTINSPECTION.equals(mfgCheckInspectionReport.getInspectionPointType())){
	    									inspectionType = "首检";
	    								}else if(InspectionPointTypeEnum.PATROLINSPECTION.equals(mfgCheckInspectionReport.getInspectionPointType())){
	    									inspectionType = "巡检";
	    								}else if(InspectionPointTypeEnum.COMPLETEINSPECTION.equals(mfgCheckInspectionReport.getInspectionPointType())){
	    									inspectionType = "末检";
	    								}
	    								layerMap.put(layer.getDetailCode(), inspectionType);
	    							}
	    						} 
								//spcSampleIds = mfgCheckInspectionReportManager.insertSpcByResults(qualityFeature,mfgCheckInspectionReport.getInspectionDate(),hisSampleIds,values,mfgCheckInspectionReport.getInspectionPointType(),mfgCheckInspectionReport.getWorkGroupType(),mfgCheckInspectionReport,checkItem.getEquipmentNo());
								//List<String> list=spcImportManager.backImportValues(qualityFeature.getId(), hisIds, valueList, null);
								List<String> list=spcImportManager.backImportValues(qualityFeature.getId(),mfgCheckInspectionReport.getInspectionDate(),null,null,hisIds,valueList, null);
	        					for (String string : list) {
	        						spcSampleIds+=string+",";
	    						}
							}
							allPatrolSpcSampleIds += spcSampleIds;
						}
					}
					checkItem.setSpcSampleIds(spcSampleIds);
				}
				/*//删除不存在采集数据的子组(异步调用，不涉及报告的修改，不存在删除问题)
				String[] sampleIds = allHisPatrolSpcSampleIds.split(",");
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
				mfgCheckInspectionReport.setSpcSampleIds(allPatrolSpcSampleIds);
				mfgCheckInspectionReportManager.saveIncomingInspectionActionsReport(mfgCheckInspectionReport);
			}
		}
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
	public Date synchroCheckDatasNew(String id,String startDate,String endDate) throws Exception{
		// 根据检验项目ID获取检验项目
		MfgItemIndicator inspectingItemIndicator = this.getItemIndicator(Long.parseLong(id));
		Date inspectionDate = null;
		if(inspectingItemIndicator != null && inspectingItemIndicator.getMfgInspectingItem() != null && inspectingItemIndicator.getMfgInspectingIndicator() != null && StringUtils.isNotEmpty(inspectingItemIndicator.getFeatureId())){
			List<MfgCheckInspectionReport> inomIncomingInspectionActionsReports = mfgCheckInspectionReportManager.getListByInspectingItemIndicator(DateUtil.parseDate(startDate, "yyyy-MM-dd"), DateUtil.parseDate(endDate, "yyyy-MM-dd"), inspectingItemIndicator);
			for (MfgCheckInspectionReport mfgCheckInspectionReport : inomIncomingInspectionActionsReports) {
				//此二行用于定义推送至SPC
				String allHisPatrolSpcSampleIds = mfgCheckInspectionReport.getSpcSampleIds()==null?"":mfgCheckInspectionReport.getSpcSampleIds();
				String allPatrolSpcSampleIds = ",";
				List<MfgCheckItem> checkItems = mfgCheckInspectionReport.getCheckItems();
				for (MfgCheckItem checkItem : checkItems) {
					if(StringUtils.isEmpty(checkItem.getCheckItemName()) || !checkItem.getCheckItemName().equals(inspectingItemIndicator.getMfgInspectingItem().getItemName())){
						continue;
					}
					inspectionDate = mfgCheckInspectionReport.getInspectionDate();
					/*if(StringUtils.isEmpty(checkItem.getFeatureId()) || !checkItem.getFeatureId().equals(inspectingItemIndicator.getFeatureId())){
						continue;
					}*/
					//处理数据 SPC
					String spcSampleIds = "";
					if(InspectingItem.COUNTTYPE_METERING.equals(checkItem.getCountType())||"计量型".equals(checkItem.getCountType())){
						//获取检验 result
						String values = "";
						for (int i = 1; i <= 32; i++) {
							Object value = PropertyUtils.getProperty(checkItem, "result"+i);
							if(value != null){
								DecimalFormat df = new DecimalFormat("#,##0.00000");
								values += df.format(Double.valueOf(value.toString())) + ",";
							}
						}
						if(StringUtils.isNotEmpty(values)){
							QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureById(Long.valueOf(inspectingItemIndicator.getFeatureId()));
							if(qualityFeature != null){
								String hisSampleIds = checkItem.getSpcSampleIds();
								if(StringUtils.isNotEmpty(hisSampleIds)){
									allHisPatrolSpcSampleIds = allHisPatrolSpcSampleIds.replaceAll("," + hisSampleIds,",");
								}
								spcSampleIds = mfgCheckInspectionReportManager.insertSpcByResults(qualityFeature,mfgCheckInspectionReport.getInspectionDate(),hisSampleIds,values,mfgCheckInspectionReport.getInspectionPointType(),mfgCheckInspectionReport.getWorkGroupType(),mfgCheckInspectionReport,checkItem.getEquipmentNo());
							}
							allPatrolSpcSampleIds += spcSampleIds;
						}
					}
					checkItem.setSpcSampleIds(spcSampleIds);
				}
				/*//删除不存在采集数据的子组(异步调用，不涉及报告的修改，不存在删除问题)
				String[] sampleIds = allHisPatrolSpcSampleIds.split(",");
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
				mfgCheckInspectionReport.setSpcSampleIds(allPatrolSpcSampleIds);
				mfgCheckInspectionReportManager.saveIncomingInspectionActionsReport(mfgCheckInspectionReport);
			}
		}
		return inspectionDate;
	}
	
	/**
	  * 方法名:将以往的检验数据同步到SPC
	 * @param session 
	 * @param transaction 
	  * @param settings
	  * @param 检验项目ID,工序，起始日期，结束日期
	  * @param page
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Date synchroCheckDatasNew2(String startDate,String endDate, Session session, Transaction transaction) throws Exception{
		Date inspectionDate = null;
		List<MfgCheckInspectionReport> inomIncomingInspectionActionsReports = mfgCheckInspectionReportManager.getListByEndTime(DateUtil.parseDate(startDate, "yyyy-MM-dd"), DateUtil.parseDate(endDate, "yyyy-MM-dd"),session);
		for (MfgCheckInspectionReport mfgCheckInspectionReport : inomIncomingInspectionActionsReports) {
//			transaction = session.beginTransaction();
			//此二行用于定义推送至SPC
			String allHisPatrolSpcSampleIds = mfgCheckInspectionReport.getSpcSampleIds()==null?"":mfgCheckInspectionReport.getSpcSampleIds();
			String allPatrolSpcSampleIds = ",";
			if(session==null){
				System.out.println("--");
			}
			Date beginDate = new Date();
			List<MfgCheckItem> checkItems = mfgCheckInspectionReport.getCheckItems();
			for (MfgCheckItem checkItem : checkItems) {
				if(StringUtils.isEmpty(checkItem.getCheckItemName()) || StringUtils.isEmpty(checkItem.getFeatureId())){
					continue;
				}
				inspectionDate = mfgCheckInspectionReport.getInspectionDate();
				//处理数据 SPC
				String spcSampleIds = "";
				if(InspectingItem.COUNTTYPE_METERING.equals(checkItem.getCountType())||"计量型".equals(checkItem.getCountType())){
					//获取检验 result
					String values = "";
					for (int i = 1; i <= 16; i++) {
						Object value = PropertyUtils.getProperty(checkItem, "result"+i);
						if(value != null){
							DecimalFormat df = new DecimalFormat("#,##0.00000");
							values += df.format(Double.valueOf(value.toString())) + ",";
						}
					}
					if(StringUtils.isNotEmpty(values)){
						QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureByIdNew(Long.valueOf(checkItem.getFeatureId()),session);
						if(qualityFeature != null){
							String hisSampleIds = checkItem.getSpcSampleIds();
							if(StringUtils.isNotEmpty(hisSampleIds)){
								allHisPatrolSpcSampleIds = allHisPatrolSpcSampleIds.replaceAll("," + hisSampleIds,",");
							}
							spcSampleIds = mfgCheckInspectionReportManager.insertSpcByResultsNew(qualityFeature,mfgCheckInspectionReport.getInspectionDate(),hisSampleIds,values,mfgCheckInspectionReport.getInspectionPointType(),mfgCheckInspectionReport.getWorkGroupType(),mfgCheckInspectionReport,checkItem.getEquipmentNo(),session);
						}
						allPatrolSpcSampleIds += spcSampleIds;
					}
				}
				checkItem.setSpcSampleIds(spcSampleIds);
			}
			//设置最新的采集数据ID
			Date completeDate = new Date();
			Long mins = completeDate.getTime()-beginDate.getTime();
			System.out.println("耗时:"+mins/1000*60+"分钟");
			logUtilDao.debugLog("spc单条数据同步耗时:", mins/1000*60+"分钟");
			mfgCheckInspectionReport.setSpcSampleIds(allPatrolSpcSampleIds);
			mfgCheckInspectionReport.setHasSynced("是");
			try{
				transaction = session.beginTransaction();
				session.save(mfgCheckInspectionReport);
				transaction.commit();
			}catch(Exception e){
				logUtilDao.debugLog("同步到spc失败", e.getMessage());
				e.printStackTrace();
				//回滚事务
				if(transaction != null&&transaction.isActive()){
					transaction.rollback();
				}
			}
			
		}				
		return inspectionDate;
	}	
}
