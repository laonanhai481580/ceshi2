package com.ambition.carmfg.oqc.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.SizeInspection;
import com.ambition.carmfg.oqc.dao.SizeInspectionDao;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:尺寸检验数据Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年7月7日 发布
 */
@Service
@Transactional
public class SizeInspectionManager {
	@Autowired
	private SizeInspectionDao sizeInspectionDao;	
	public SizeInspection getSizeInspection(Long id){
		return sizeInspectionDao.get(id);
	}
	
	public void deleteSizeInspection(SizeInspection sizeInspection){
		sizeInspectionDao.delete(sizeInspection);
	}

	public Page<SizeInspection> search(Page<SizeInspection>page,String businessUnit){
		return sizeInspectionDao.search(page, businessUnit);
	}

	public List<SizeInspection> listAll(){
		return sizeInspectionDao.getAllSizeInspection();
	}
		
	public void deleteSizeInspection(Long id){
		sizeInspectionDao.delete(id);
	}
	public void deleteSizeInspection(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			if(id!=null){
				SizeInspection  sizeInspection = sizeInspectionDao.get(Long.valueOf(id));
					sizeInspectionDao.delete(sizeInspection);
			}			
		}
	}
	public void saveSizeInspection(SizeInspection sizeInspection){
		sizeInspectionDao.save(sizeInspection);
	}
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit){
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		Map<String,Boolean> existMap = new HashMap<String, Boolean>();
		for(Object obj :  results){
			Object[] objs = (Object[])obj;
			String itemCode = objs[1]+"";
			if(existMap.containsKey(itemCode)){
				continue;
			}
			existMap.put(itemCode,true);
		}
		return results;
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("MFG_OQC_DELIVER");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if(row == null){
			throw new RuntimeException("第一行不能为空!");
		}
		
		Map<String,Integer> columnMap = new HashMap<String,Integer>();
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
		/*if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new AmbFrameException("Excel格式不正确!请重新导出台账数据模板!");
		}*/
		
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 0;
		while(rows.hasNext()){
			row = rows.next();
			try {
				Map<String,Object> objMap = new HashMap<String, Object>();
				for(String columnName : columnMap.keySet()){
					Cell cell = row.getCell(columnMap.get(columnName));
					if(cell != null){
						Object value = null;
						if(Cell.CELL_TYPE_STRING == cell.getCellType()){
							value = cell.getStringCellValue();
						}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = cell.getDateCellValue();
							} else {
								value = df.format(cell.getNumericCellValue());
							}
						}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
							value = cell.getCellFormula();
						}
						objMap.put(fieldMap.get(columnName),value);
					}
				}
				SizeInspection sizeInspection = new SizeInspection();
				sizeInspection.setCompanyId(ContextUtils.getCompanyId());
				sizeInspection.setCreatedTime(new Date());
				sizeInspection.setCreator(ContextUtils.getUserName());
				sizeInspection.setModifiedTime(new Date());
				sizeInspection.setModifier(ContextUtils.getLoginName());
				sizeInspection.setModifierName(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					CommonUtil1.setProperty(sizeInspection,key, objMap.get(key));
				}
			   sizeInspectionDao.save(sizeInspection);
			   sb.append("第" + (i+1) + "行导入成功!<br/>");
			} catch (Exception e) {
				e.printStackTrace();
				sb.append("第" + (i+1) + "行导入失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}
	
	/**
	  * 方法名:获取字段映射 
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,String> getFieldMap(String listCode){
		Map<String,String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(listCode);
		for(ListColumn column: columns.getColumns()){
			if(column.getVisible()){
				fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
			}
		}
		return fieldMap;
	}
}
