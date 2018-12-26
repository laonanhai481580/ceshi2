package com.ambition.gsm.base.service;

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

import com.ambition.gsm.base.dao.CheckStandardDao;
import com.ambition.gsm.base.dao.CheckStandardDetailDao;
import com.ambition.gsm.base.dao.GsmCheckItemDao;
import com.ambition.gsm.base.dao.GsmStandardAttachDao;
import com.ambition.gsm.entity.CheckStandard;
import com.ambition.gsm.entity.CheckStandardDetail;
import com.ambition.gsm.entity.GsmCheckItem;
import com.ambition.gsm.entity.GsmStandardAttach;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
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
public class CheckStandardManager {
	@Autowired
	private CheckStandardDao checkStandardDao;
	@Autowired
	private GsmCheckItemDao checkItemDao;
	@Autowired
	private GsmStandardAttachDao standardAttachDao;
	@Autowired
	private CheckStandardDetailDao checkStandardDetailDao;
	@Autowired
	private GsmStandardAttachManager  standardAttachManager;
	@Autowired
	private LogUtilDao logUtilDao;
	public CheckStandard getCheckStandard(Long id){
		return checkStandardDao.get(id);
	}
	
	/**
	  * 方法名: 保存设备参数
	  * <p>功能说明：</p>
	  * @param mfgCheckStandard
	 */
	public void saveCheckStandard(CheckStandard checkStandard){
		checkStandardDao.save(checkStandard);
	}
	
	public void deleteCheckStandard(CheckStandard oqcInspection){
		checkStandardDao.delete(oqcInspection);
	}

	public Page<CheckStandard> search(Page<CheckStandard>page){
		return checkStandardDao.search(page);
	}

	public List<CheckStandard> listAll(){
		return checkStandardDao.getAllCheckStandard();
	}
		
	public void deleteCheckStandard(Long id){
		checkStandardDao.delete(id);
	}
	public void deleteCheckStandard(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			CheckStandard  oqcInspection = checkStandardDao.get(Long.valueOf(id));
			if(oqcInspection.getId() != null){
				checkStandardDao.delete(oqcInspection);
			}
		}
	}
	/**
	 * 删除
	 * @param id
	 */
	public void deletePlantDetail(Long id){
		checkStandardDetailDao.delete(id);
	}

	public Page<CheckStandardDetail> searchByParams(Page<CheckStandardDetail> page, String measurementName,String measurementSpecification,String manufacturer){
		return checkStandardDetailDao.searchByParams(page,measurementName,measurementSpecification,manufacturer);
	}		
	/**
	 * 检查是否存在相同名称的仪器检验标准
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistCheckStandard(String measurementName,String measurementSpecification,String manufacturer){
		String hql = "select count(i.id) from CheckStandard i where i.companyId = ? and i.measurementName = ? and i.measurementSpecification = ? and i.manufacturer=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(measurementName);
		searchParams.add(measurementSpecification);
		searchParams.add(manufacturer);
		List<?> list = checkStandardDao.find(hql,searchParams.toArray());
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
	public String importCheckStandard(File file,String fileName) throws Exception{
		StringBuffer sb = new StringBuffer("");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
						
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
			  //仪器名称
			    Cell measurementNameCell = ExcelUtil.getNextCell(sheet,cellMap.get("仪器名称"), cellRangeAddressMap);
			    String measurementName = null;
			    if(measurementNameCell != null){
			    	measurementName = (ExcelUtil.getCellValue(measurementNameCell)+"").trim();
			    }
			    if(measurementName==null||"".equals(measurementName)){
			    	throw new  AmbFrameException("Sheet"+k+"的仪器名称不能为空");
			    }
				//规格型号
			    Cell measurementSpecificationCell = ExcelUtil.getNextCell(sheet,cellMap.get("规格型号"), cellRangeAddressMap);
			    String measurementSpecification = null;
			    if(measurementSpecificationCell != null){
			    	measurementSpecification = (ExcelUtil.getCellValue(measurementSpecificationCell)+"").trim();
			    }
			    if(measurementSpecification==null||"".equals(measurementSpecification)){
			    	throw new  AmbFrameException("Sheet"+k+"的规格型号不能为空");
			    }
				//制造厂商 
			    Cell manufacturerCell = ExcelUtil.getNextCell(sheet,cellMap.get("制造厂商"), cellRangeAddressMap);
			    String manufacturer = null;
			    if(manufacturerCell != null){
			    	manufacturer = (ExcelUtil.getCellValue(manufacturerCell)+"").trim();
			    }
			    if(manufacturer==null||"".equals(manufacturer)){
			    	throw new  AmbFrameException("Sheet"+k+"的制造厂商不能为空");
			    }
			    
			    if(isExistCheckStandard(measurementName,measurementSpecification, manufacturer)){
			    	throw new AmbFrameException("SHEET"+k+"【"+sheet.getSheetName()+"】中,仪器名称【"+measurementName+"】，规格型号【"+measurementSpecification+"】，制造厂商【"+manufacturer+"】的检验标准已经存在,不能导入！");
			    }
				CheckStandard checkStandard = new CheckStandard();
				checkStandard.setCreatedTime(new Date());
				checkStandard.setCompanyId(ContextUtils.getCompanyId());
				checkStandard.setCreator(ContextUtils.getUserName());
				checkStandard.setLastModifiedTime(new Date());
				checkStandard.setLastModifier(ContextUtils.getUserName());
				checkStandard.setManufacturer(manufacturer);
				checkStandard.setMeasurementName(measurementName);
				checkStandard.setMeasurementSpecification(measurementSpecification);
				
				//先保存设备参数文件,保存对应的ID
				GsmStandardAttach attach = standardAttachManager.addStandardAttach(file,fileName,measurementName,measurementSpecification,manufacturer,ContextUtils.getCompanyId());
				checkStandard.setStandardAttachId(attach.getId());
				checkStandardDao.save(checkStandard);
				Map<String,Boolean> existItemMap = new HashMap<String, Boolean>();
				//指定从哪一行之后开始是导入的项目
				Cell itemItitleCell = cellMap.get("项目名称");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为仪器名称的单元格!&nbsp;&nbsp;</br>");
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
					String itemName = (ExcelUtil.getCellValue(cell)+"").trim(),standardValue = null,allowableError=null;
					if(StringUtils.isEmpty(itemName)){
						continue;
					}
					itemName = CommonUtil1.replaceSymbols(itemName);
					Cell tempCell = cellMap.get("项目名称");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【项目名称】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());
					itemName=(ExcelUtil.getCellValue(cell)+"").trim();
					
					tempCell = cellMap.get("标准值");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【标准值】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());					
					standardValue=(ExcelUtil.getCellValue(cell)+"").trim();
					
					tempCell = cellMap.get("允许误差");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【允许误差】的单元格!&nbsp;&nbsp;</br>");
					}
					cell = row.getCell(tempCell.getColumnIndex());					
					allowableError=(ExcelUtil.getCellValue(cell)+"").trim();
					
					GsmCheckItem checkItem = getGsmCheckItem(itemName,standardValue, allowableError);
				
					//判断是否有重复的检验项目
					String existKey = itemName;
					if(existItemMap.containsKey(existKey)){
						throw new AmbFrameException("SHEET"+k+"仪器名称【"+measurementName+"】中存在相同的项目【"+itemName+"】,不能导入!");
					}else{
						existItemMap.put(existKey, true);
					}
					
					CheckStandardDetail checkDetail = new CheckStandardDetail();
					checkDetail.setCheckStandard(checkStandard);
					checkDetail.setCreatedTime(new Date());
					checkDetail.setCompanyId(ContextUtils.getCompanyId());
					checkDetail.setCreator(ContextUtils.getUserName());
					checkDetail.setLastModifiedTime(new Date());
					checkDetail.setLastModifier(ContextUtils.getUserName());
					checkDetail.setItemName(itemName);
					checkDetail.setAllowableError(allowableError);
					checkDetail.setStandardValue(standardValue);
					checkDetail.setCheckItem(checkItem);
					checkStandardDetailDao.save(checkDetail);
					
				}
				if(existItemMap.keySet().isEmpty()){
					throw new AmbFrameException("SHEET"+k+"仪器名称【"+measurementName+"】的项目名称为空,不能导入!");
				}
				sb.append("仪器名称【"+measurementName+"】的检验标准导入成功!!<br/>");
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
	 * 检查是否存在相同名称的项目
	 * @param id
	 * @param name
	 * @return
	 */
	private GsmCheckItem getGsmCheckItem(String itemName,String standardValue,String allowableError){
		String hql = " from GsmCheckItem p where p.companyId = ? and p.itemName = ?  ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(itemName);
		List<GsmCheckItem> list=new ArrayList<GsmCheckItem>();
		try {
			list= checkItemDao.find(hql,searchParams.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!list.isEmpty()){
			GsmCheckItem item = list.get(0);
			return item;
		}else{
			GsmCheckItem checkItem = new GsmCheckItem();
			checkItem.setCreatedTime(new Date());
			checkItem.setCompanyId(ContextUtils.getCompanyId());
			checkItem.setCreator(ContextUtils.getUserName());
			checkItem.setLastModifiedTime(new Date());
			checkItem.setLastModifier(ContextUtils.getUserName());
			checkItem.setItemName(itemName);
			checkItem.setStandardValue(standardValue);
			checkItem.setAllowableError(allowableError);
			checkItemDao.save(checkItem);
			return checkItem;
		}
	}
	
	/**
	 * 获取所有已设置的项目
	 * @return
	 */
	public List<CheckStandardDetail> getAllDetail(Long checkStandardId){
		return checkStandardDetailDao.getAllDetail(checkStandardId);
	}
	
	/**
	 * 编辑保存
	 * @throws Exception 
	 */
	public void savePlantDetail(CheckStandard checkStandard,GsmCheckItem checkItem,JSONObject params) throws Exception{		
			params = convertJsonObject(params);
			List<CheckStandardDetail> details = checkStandardDetailDao.getDetailByCheckItem(checkItem);
			for(CheckStandardDetail detail : details){
				if(checkStandard.getId().toString().equals(detail.getCheckStandard().getId().toString())){
					for(Object key : params.keySet()){
						setProperty(detail,key,params);
					}
					detail.setLastModifiedTime(new Date());
					detail.setLastModifier(ContextUtils.getUserName());
					checkStandardDetailDao.save(detail);
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
	public int addPlantDetail(Long checkStandardId,String checkItemIds){
		int addCount = 0;
		CheckStandard checkStandard = checkStandardDao.get(checkStandardId);
		for(String checkItemId : checkItemIds.split(",")){
			if(StringUtils.isNotEmpty(checkItemId)){
				GsmCheckItem checkItem =checkItemDao.get(Long.valueOf(checkItemId));
					//检查是否已经添加
					String hql = "select count(i.id) from CheckStandardDetail i where i.checkItem = ? and i.checkStandard = ?";
					List<?> list = checkStandardDetailDao.find(hql,checkItem,checkStandard);
					if(Integer.valueOf(list.get(0).toString())==0){
						CheckStandardDetail detail = new CheckStandardDetail();
						detail.setItemName(checkItem.getItemName());
						detail.setStandardValue(checkItem.getStandardValue());
						detail.setAllowableError(checkItem.getAllowableError());
						detail.setCheckStandard(checkStandard);
						detail.setCheckItem(checkItem);
						checkStandardDetailDao.save(detail);
						addCount++;
				}
			}
		}
		return addCount;
	}
	
	public void downloadAttachById(HttpServletResponse response,Long attachId) throws IOException{
		try {
			GsmStandardAttach attach = standardAttachDao.getStandardAttachById(attachId);
			if(attach == null){
				throw new AmbFrameException("检验标准文件不存在!");
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

	public CheckStandard getCheckStandard(String measurementName, String measurementSpecification,String manufacturer) {
		String hql = " from CheckStandard i where i.companyId = ? and i.measurementName = ? and i.measurementSpecification = ? and i.manufacturer=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(measurementName);
		searchParams.add(measurementSpecification);
		searchParams.add(manufacturer);
		List<CheckStandard> list = checkStandardDao.find(hql,searchParams.toArray());		
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
