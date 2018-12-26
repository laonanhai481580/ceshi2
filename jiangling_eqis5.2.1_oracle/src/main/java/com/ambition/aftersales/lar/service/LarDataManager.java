package com.ambition.aftersales.lar.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ambition.aftersales.baseinfo.service.DefectionClassManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.DefectionClassDao;
import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.LarDefectiveItem;

import com.ambition.aftersales.lar.dao.LarDataDao;

import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import java.util.Date;

import com.norteksoft.product.util.ContextUtils;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;




/**
 * 
 * 类名:LAR批次合格率Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class LarDataManager {

	@Autowired
	private DefectionClassManager defectionClassManager;
	@Autowired
	private LarDataDao larDataDao;	
	@Autowired
	private DefectionClassDao defectionClassDao;
	public LarData getLarData(Long id){
		return larDataDao.get(id);
	}
	
	public void deleteLarData(LarData larData){
		larDataDao.delete(larData);
	}

	public Page<LarData> search(Page<LarData> page){
		return larDataDao.search(page);
	}

	public List<LarData> listAll(){
		return larDataDao.getAllLarData();
	}
		
	public void deleteLarData(Long id){
		larDataDao.delete(id);
	}
	public void deleteLarData(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			LarData  larData = larDataDao.get(Long.valueOf(id));
			if(larData.getId() != null){
				larDataDao.delete(larData);
			}
		}
	}
	public void saveLarData(LarData larData){
		larDataDao.save(larData);
	}
	
	
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit){
		String sql = "select t.defection_class,c.defection_item_no,c.defection_item_name from  AFS_DEFECTION_CLASS t  " 
				+" inner join AFS_DEFECTION_ITEM c "
				+" on c.FK_DEFECTION_TYPE_NO = t.id and t.business_unit_name=? "
				+" order by t.defection_class,c.defection_item_no ";
		List<?> list = defectionClassDao.getSession()
							.createSQLQuery(sql)
							.setParameter(0,businessUnit)
							.list();
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for(Object obj :  list){
			Object[] objs = (Object[])obj;
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("typeName",objs[0]);
			map.put("itemCode",objs[1]);
			map.put("itemName",objs[2]);
			results.add(map);
		}
		return results;
	}
	
	public Page<LarData> getListByBusinessUnit(Page<LarData> page,String businessUnit){
		return larDataDao.searchByBusinessUnit(page, businessUnit);
}

public LarDataDao getLarDataDao() {
	// TODO Auto-generated method stub
	return larDataDao;
}



//新增
public String importDatas(File file,String businessUnit) throws Exception{
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
			Iterator<Row> rows = sheet.rowIterator();
			Cell itemItitleCell = cellMap.get("日期");
			if(itemItitleCell==null){
				throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为日期的单元格!&nbsp;&nbsp;</br>");
			}
			Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
			int i = 0;
			while(rows.hasNext()){
				row = rows.next();
				//标题以后再执行
				if(row.getRowNum() <= itemTitleRowNum){
					continue;
				}
				Cell cell = row.getCell(itemItitleCell.getColumnIndex());
				if(cell == null){
					continue;
				}
				Cell tempCell=null;
				 String customerName=null,customerFactory = null,ofilmModel = null,customerModel = null,productStructure=null,productionLine=null,workingProcedure=null;
				Date larDate=null,produceDate=null;
				 //客户名称
				tempCell = cellMap.get("客户名称");
				if(tempCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【客户名称】的单元格!&nbsp;&nbsp;</br>");
				}					
				cell = row.getCell(tempCell.getColumnIndex());
				if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
					customerName=(ExcelUtil.getCellValue(cell).toString()).trim();
				}											 
			    if(customerName==null||"".equals(customerName)){
			    	throw new  AmbFrameException("Sheet"+k+"的客户名称值不能为空");
			    }
			    //日期
				tempCell = cellMap.get("日期");
				if(tempCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【日期】的单元格!&nbsp;&nbsp;</br>");
				}					
				cell = row.getCell(tempCell.getColumnIndex());					
				if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
					larDate=(Date)ExcelUtil.getCellValue(cell);	
				}						
			    if(larDate==null||"".equals(larDate)){
			    	throw new  AmbFrameException("Sheet"+k+"的日期值不能为空");
			    }	
			    //客户场地
				tempCell = cellMap.get("客户场地");
				if(tempCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【客户场地】的单元格!&nbsp;&nbsp;</br>");
				}					
				cell = row.getCell(tempCell.getColumnIndex());
				if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
					customerFactory=(ExcelUtil.getCellValue(cell).toString()).trim();
				}	
			    //欧菲机型
				tempCell = cellMap.get("欧菲机型");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						ofilmModel=(ExcelUtil.getCellValue(cell).toString()).trim();
					}
				}	
			    //客户机型
				tempCell = cellMap.get("客户机型");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						customerModel=(ExcelUtil.getCellValue(cell).toString()).trim();
					}
				}
						
				Integer inputCount=0,inputBatchNum=0,qualifiedBatchNum=0,inspectionCount=0,unqualifiedCount=0;
				
				//入料批数
				tempCell = cellMap.get("入料批数");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中投入数【"+value+"】不是有效数字!");
							}
							inputBatchNum = Integer.valueOf(value);
						}
					}
				}
				

				//合格批数
				tempCell = cellMap.get("合格批数");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中投入数【"+value+"】不是有效数字!");
							}
							qualifiedBatchNum = Integer.valueOf(value);
							
						}
					}
				}
				
				
			
				
				//投入数
				tempCell = cellMap.get("入料数");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中投入数【"+value+"】不是有效数字!");
							}
							inputCount = Integer.valueOf(value);
						}
					}
				}
				
				//抽检数
				tempCell = cellMap.get("抽检数");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil1.isDouble(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中投入数【"+value+"】不是有效数字!");
							}
							inspectionCount = Integer.valueOf(value);
						}
					}
				}
				//不良数
/*					tempCell = cellMap.get("不良数");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(cell != null){
						String value = (ExcelUtil.getCellValue(cell)+"").trim();
						if(StringUtils.isNotEmpty(value)){
							if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
								throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中不良数【"+value+"】不是有效数字!");
							}
							unqualifiedCount = Integer.valueOf(value);
						}
					}
				}	
				if(Integer.valueOf(unqualifiedCount)>Integer.valueOf(inputCount)){
					throw new  AmbFrameException("Sheet"+k+"的不良数不能大于投入数");				  
				}	*/
				DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
				String 	unqualifiedRate=null;
				
				/*if(inputCount==0){
					unqualifiedRate="0.00";
				}else{
					unqualifiedRate=decimalFormat.format((float)(unqualifiedCount*100)/(float)inputCount);					
				}	*/				
				//备注
				String remark=null;
				tempCell = cellMap.get("备注");
				if(tempCell!=null){
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						remark=(ExcelUtil.getCellValue(cell).toString()).trim();
					}	
				}					
								
				LarData larData = new LarData();
				larData.setCreatedTime(new Date());
				larData.setCompanyId(ContextUtils.getCompanyId());
				larData.setCreator(ContextUtils.getUserName());
				larData.setLastModifiedTime(new Date());
				larData.setLastModifier(ContextUtils.getUserName());
				larData.setCustomer(customerName);
				larData.setLarDate(larDate);
				larData.setCustomerFactory(customerFactory);
				larData.setCustomerModel(customerModel);
				larData.setOfilmModel(ofilmModel);
				larData.setWorkingProcedure(workingProcedure);
				//入料批数
				larData.setInputBatchNum(inputBatchNum);
				//合格批数
				larData.setQualifiedBatchNum(qualifiedBatchNum);   //
				//抽检数
				larData.setInspectionCount(inspectionCount);
				
				//larData.setUnqualifiedCount(unqualifiedCount);//
				larData.setInputCount(inputCount);
				//larData.setUnqualifiedRate(unqualifiedRate);//
				larData.setBusinessUnitName(businessUnit);
				List<LarDefectiveItem> larDefectiveItems=new ArrayList<LarDefectiveItem>();															
				Map<String, Map<String,String>> map=getFieldMaps(businessUnit);
				//Integer unqualifiedCount=0;
				for (String typeName : map.keySet()) {
					Map<String,String> codeMap=map.get(typeName);
					for (String codeName : codeMap.keySet()) {
						Integer itemValue=null;
						tempCell = cellMap.get(codeName);
						if(tempCell==null||"".equals(tempCell)){
							continue;
						}
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil1.isInteger(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】不良项目中"+codeName+"【"+value+"】不是有效数字!");
								}
								itemValue = Integer.valueOf(value);
								unqualifiedCount+=itemValue;
								LarDefectiveItem defectionItem = new LarDefectiveItem();
								defectionItem.setCreatedTime(new Date());
								defectionItem.setCompanyId(ContextUtils.getCompanyId());
								defectionItem.setCreator(ContextUtils.getLoginName());
								defectionItem.setCreatorName(ContextUtils.getUserName());
								defectionItem.setLastModifiedTime(new Date());
								defectionItem.setLastModifier(ContextUtils.getUserName());
								defectionItem.setDefectionItemName(codeName);
								defectionItem.setDefectionItemNo(codeMap.get(codeName));
								defectionItem.setDefectionItemValue(itemValue);
								defectionItem.setDefectionClass(typeName);
								defectionItem.setLarData(larData);
								larDefectiveItems.add(defectionItem);
							}		
						}												
					}						
				}
				larData.setLarDefectiveItems(larDefectiveItems);					
				larData.setUnqualifiedCount(unqualifiedCount);
				String unDppm=null;
				if(inputCount==0||unqualifiedCount==0){
					unqualifiedRate="0.00";
					unDppm="0";
				}else{
					unqualifiedRate=decimalFormat.format((float)(unqualifiedCount*100)/(float)inputCount);	
					unDppm=(unqualifiedCount*1000000/inputCount)+"";
				}
				String 	batchQualificationRate=null;//合格批次率
				//合格批次率
				if(inputBatchNum==0||qualifiedBatchNum==0){
					batchQualificationRate="0.00";
				}else{
					batchQualificationRate=decimalFormat.format((float)(qualifiedBatchNum*100)/(float)inputBatchNum);	
				}
				larData.setBatchQualificationRate(batchQualificationRate+"%");
				larData.setUnqualifiedRate(unqualifiedRate+"%");
				larData.setUnqualifiedDppm(unDppm);
				this.saveLarData(larData);
				sb.append("第" + (i+1) + "行导入成功!<br/>");
				i++;
			}
		}
		return sb.toString();
	}finally{
		if(inputStream != null){
			inputStream.close();
		}
		file.delete();
	}
}

//新增
private Map<String, Map<String,String>> getFieldMaps(String businessUnit){				
	Map<String, Map<String,String>> map=new HashMap<String, Map<String,String>>();
	List<DefectionClass> lisType=this.defectionClassManager.getDefectionClassByBusinessUnit(businessUnit);
	for (DefectionClass defectionClass : lisType) {
		List<DefectionItem> listCode=defectionClass.getDefectionItems();
		Map<String,String> fieldMap = new HashMap<String, String>();
		for (DefectionItem defectionItem : listCode) {
			fieldMap.put(defectionItem.getDefectionItemName(), defectionItem.getDefectionItemNo());
		}
		map.put(defectionClass.getDefectionClass(), fieldMap);
	}
	return map;
}

}
