package com.ambition.carmfg.plantparameter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.ambition.carmfg.entity.IndicatorAttach;
import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.PlantAttach;
import com.ambition.carmfg.entity.PlantItem;
import com.ambition.carmfg.entity.PlantParameter;
import com.ambition.carmfg.entity.PlantParameterDetail;
import com.ambition.carmfg.plantparameter.dao.PlantAttachDao;
import com.ambition.carmfg.plantparameter.dao.PlantItemDao;
import com.ambition.carmfg.plantparameter.dao.PlantParameterDao;
import com.ambition.carmfg.plantparameter.dao.PlantParameterDetailDao;
import com.ambition.iqc.entity.ItemIndicator;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:设备参数Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author LPF
 * @version 1.00 2016年9月3日 发布
 */
@Service
@Transactional
public class PlantParameterManager {
	@Autowired
	private PlantParameterDao plantParameterDao;
	@Autowired
	private PlantItemDao plantItemDao;
	@Autowired
	private PlantAttachDao plantAttachDao;
	@Autowired
	private PlantParameterDetailDao plantParameterDetailDao;
	@Autowired
	private PlantAttachManager  plantAttachManager;
	@Autowired
	private LogUtilDao logUtilDao;
	public PlantParameter getPlantParameter(Long id){
		return plantParameterDao.get(id);
	}
	
	/**
	  * 方法名: 保存设备参数
	  * <p>功能说明：</p>
	  * @param mfgPlantParameter
	 */
	public void savePlantParameter(PlantParameter plantParameter){
		plantParameterDao.save(plantParameter);
	}
	
	public void deletePlantParameter(PlantParameter oqcInspection){
		plantParameterDao.delete(oqcInspection);
	}

	public Page<PlantParameter> search(Page<PlantParameter>page){
		return plantParameterDao.search(page);
	}

	public List<PlantParameter> listAll(){
		return plantParameterDao.getAllPlantParameter();
	}
		
	public void deletePlantParameter(Long id){
		plantParameterDao.delete(id);
	}
	public void deletePlantParameter(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			PlantParameter  oqcInspection = plantParameterDao.get(Long.valueOf(id));
			if(oqcInspection.getId() != null){
				plantParameterDao.delete(oqcInspection);
			}
		}
	}
	/**
	 * 删除
	 * @param id
	 */
	public void deletePlantDetail(Long id){
		plantParameterDetailDao.delete(id);
	}
	/**
	 * 导入标准
	 * @param file
	 * @throws Exception
	 */
	public String importPlantParameter(File file,String fileName) throws Exception{
		StringBuffer sb = new StringBuffer("");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			
			//查询可选的工序
			List<Option> workProcedures = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_procedure");
			Map<String,Boolean> workProcedureMap = new HashMap<String, Boolean>();
			for(Option o : workProcedures){
				workProcedureMap.put(o.getValue(),true);
			}
			
			Workbook book = WorkbookFactory.create(inputStream);
			int totalSheets = book.getNumberOfSheets();
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
			  /*  if(isExistPlantParameter(null,model, null)){
					throw new AmbFrameException("SHEET"+k+"【"+sheet.getSheetName()+"】中,机种【"+model+"】的设备参数已经存在,不能导入！");
				}*/
				PlantParameter plantParameter = new PlantParameter();
				plantParameter.setCreatedTime(new Date());
				plantParameter.setCompanyId(ContextUtils.getCompanyId());
				plantParameter.setCreator(ContextUtils.getUserName());
				plantParameter.setLastModifiedTime(new Date());
				plantParameter.setLastModifier(ContextUtils.getUserName());
				plantParameter.setModel(model);
				plantParameter.setModelName(modelName);
				plantParameter.setWorkingProcedure(workingProcedure);
				
				//先保存设备参数文件,保存对应的ID
				PlantAttach attach = plantAttachManager.addPlantAttach(file,fileName,model,modelName,ContextUtils.getCompanyId());
				plantParameter.setPlantAttachId(attach.getId());
				plantParameterDao.save(plantParameter);
				Map<String,Boolean> existItemMap = new HashMap<String, Boolean>();
				Cell itemItitleCell = cellMap.get("设备名称");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为设备名称的单元格!&nbsp;&nbsp;</br>");
				}
				Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
				df = new DecimalFormat("0.00");
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
					String plantName=null,parameterName = null,parameterStandard=null,unit=null;
					plantName = CommonUtil1.replaceSymbols(plantName);
					Cell tempCell = cellMap.get("设备名称");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【设备名称】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					plantName=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("参数名称");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【参数名称】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					parameterName=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("参数规格");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【参数规格】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					parameterStandard=(ExcelUtil.getCellValue(cell)+"").trim();
					tempCell = cellMap.get("单位");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【单位】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					unit=(ExcelUtil.getCellValue(cell)+"").trim();
					String remark="";
					tempCell = cellMap.get("备注");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为备注的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						remark = (ExcelUtil.getCellValue(cell)+"").trim();
					}
					PlantItem plantItem = getPlantItem(plantName,parameterName, parameterStandard,unit,remark);
				
					
				
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
								valueType = ItemIndicator.VALUETYPE_DEGREE;
								levelb = CommonUtil1.degreeStrToNumber(value);
							}else{
								if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中下限【"+value+"】不是有效数字!");
								}
								levelb = Double.valueOf(df.format(Double.valueOf(value)));
							}
						}
					}
					//判断是否有重复的检验项目
					String existKey = plantName;
					if(existItemMap.containsKey(existKey)){
						throw new AmbFrameException("SHEET"+k+"机种【"+model+"】中存在相同的设备【"+plantName+"】,不能导入!");
					}else{
						existItemMap.put(existKey, true);
					}
					
					PlantParameterDetail plantDetail = new PlantParameterDetail();
					plantDetail.setPlantParameter(plantParameter);
					plantDetail.setCreatedTime(new Date());
					plantDetail.setCompanyId(ContextUtils.getCompanyId());
					plantDetail.setCreator(ContextUtils.getUserName());
					plantDetail.setLastModifiedTime(new Date());
					plantDetail.setLastModifier(ContextUtils.getUserName());
					plantDetail.setPlantItem(plantItem);
					plantDetail.setUnit(unit);
					plantDetail.setRemark(remark);
					plantDetail.setLevela(levela);
					plantDetail.setLevelb(levelb);
					plantDetail.setPlantName(plantName);
					plantDetail.setParameterName(parameterName);
					plantDetail.setParameterStandard(parameterStandard);
					plantParameterDetailDao.save(plantDetail);
					
				}
				if(existItemMap.keySet().isEmpty()){
					throw new AmbFrameException("SHEET"+k+"机种【"+model+"】的设备名称为空,不能导入!");
				}
				sb.append("机种【"+model+"】的检验标准导入成功!!<br/>");
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
	 * 检查是否存在相同名称的设备
	 * @param id
	 * @param name
	 * @return
	 */
	private PlantItem getPlantItem(String plantName,String parameterName,String parameterStandard,String unit,String remark){
		String hql = " from PlantItem p where p.companyId = ? and p.plantName = ?  ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(plantName);
		List<PlantItem> list=new ArrayList<PlantItem>();
		try {
			list= plantItemDao.find(hql,searchParams.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!list.isEmpty()){
			PlantItem item = list.get(0);
			return item;
		}else{
			PlantItem plantItem = new PlantItem();
			plantItem.setCreatedTime(new Date());
			plantItem.setCompanyId(ContextUtils.getCompanyId());
			plantItem.setCreator(ContextUtils.getUserName());
			plantItem.setLastModifiedTime(new Date());
			plantItem.setLastModifier(ContextUtils.getUserName());
			plantItem.setPlantName(plantName);
			plantItem.setParameterName(parameterName);
			plantItem.setParameterStandard(parameterStandard);
			plantItem.setUnit(unit);
			plantItem.setRemark(remark);
			plantItemDao.save(plantItem);
			return plantItem;
		}
	}
	
	/**
	 * 获取所有已设置的设备
	 * @return
	 */
	public List<PlantParameterDetail> getAllDetail(Long plantParameterId){
		return plantParameterDetailDao.getAllDetail(plantParameterId);
	}
	
	/**
	 * 编辑保存
	 * @throws Exception 
	 */
	public void savePlantDetail(PlantParameter plantParameter,PlantItem plantItem,JSONObject params) throws Exception{		
			params = convertJsonObject(params);
			List<PlantParameterDetail> details = plantParameterDetailDao.getDetailByPlantItem(plantItem);
			for(PlantParameterDetail detail : details){
				if(plantParameter.getId().toString().equals(detail.getPlantParameter().getId().toString())){
					for(Object key : params.keySet()){
						setProperty(detail,key,params);
					}
					detail.setLastModifiedTime(new Date());
					detail.setLastModifier(ContextUtils.getUserName());
					plantParameterDetailDao.save(detail);
					logUtilDao.debugLog("保存", detail.toString());
					break;
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
	 * 添加设备
	 * @param inspectingIndicatorId
	 * @param inspectingItemIds
	 * @return
	 */
	public int addPlantDetail(Long plantParameterId,String plantItemIds){
		int addCount = 0;
		PlantParameter plantParameter = plantParameterDao.get(plantParameterId);
		for(String plantItemId : plantItemIds.split(",")){
			if(StringUtils.isNotEmpty(plantItemId)){
				PlantItem plantItem =plantItemDao.get(Long.valueOf(plantItemId));
					//检查是否已经添加
					String hql = "select count(i.id) from PlantParameterDetail i where i.plantItem = ? and i.plantParameter = ?";
					List<?> list = plantParameterDetailDao.find(hql,plantItem,plantParameter);
					if(Integer.valueOf(list.get(0).toString())==0){
						PlantParameterDetail detail = new PlantParameterDetail();
						detail.setPlantName(plantItem.getPlantName());
						detail.setParameterName(plantItem.getParameterName());
						detail.setParameterStandard(plantItem.getParameterStandard());
						detail.setUnit(plantItem.getUnit());
						detail.setRemark(plantItem.getRemark());
						detail.setPlantParameter(plantParameter);
						detail.setPlantItem(plantItem);
						plantParameterDetailDao.save(detail);
						addCount++;
				}
			}
		}
		return addCount;
	}
	
	public void downloadAttachById(HttpServletResponse response,Long attachId) throws IOException{
		try {
			PlantAttach attach = plantAttachDao.getPlantAttachById(attachId);
			if(attach == null){
				throw new AmbFrameException("设备参数文件不存在!");
			}
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(new String(attach.getFileName().getBytes("GBK"),"ISO8859_1")).append("\"")
					.toString());
			response.getOutputStream().write(attach.getBlobValue().getBytes(1,(int)attach.getBlobValue().length()));
		} catch (Exception e) {
			response.reset();
			response.setContentType("application/text");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append("下载错误.txt").append("\"")
					.toString());
			response.getOutputStream().write((e.getMessage()==null?"出错了!":e.getMessage()).getBytes());
		}
	}
}
