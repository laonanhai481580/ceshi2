package com.ambition.carmfg.defectioncode.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.defectioncode.dao.DefectionCodeDao;
import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.DefectionType;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class DefectionCodeManager {
	@Autowired
	private DefectionCodeDao defectionCodeDao;
	@Autowired
	private DefectionTypeManager defectionTypeManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public DefectionCode getDefectionCode(Long id){
		return defectionCodeDao.get(id);
	}
	
	public DefectionCode getDefectionByCode(String code){
		return defectionCodeDao.getDefectionByCode(code);
	}
	
	//验证并保存记录
	private boolean isExistDefectionCode(Long id, String no, String name, DefectionType defectionType) {
		String hql = "select count(*) from DefectionCode d where d.companyId = ? and d.defectionType = ? and (d.defectionCodeNo =? or d.defectionCodeName =?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(defectionType);
		params.add(no);
		params.add(name);
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = defectionCodeDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size();i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString()) > 0){
			return true;
		}else{
			return false;
		}
	}
	public void saveDefectionCode(DefectionCode defectionCode){
		if(StringUtils.isEmpty(defectionCode.getDefectionCodeNo())){
			throw new RuntimeException("不良代码不为空!");
		}
		if(StringUtils.isEmpty(defectionCode.getDefectionCodeName())){
			throw new RuntimeException("不良代码名称不为空!");
		}
		if(isExistDefectionCode(defectionCode.getId(),defectionCode.getDefectionCodeNo(),defectionCode.getDefectionCodeName(),defectionCode.getDefectionType())){
			throw new RuntimeException("已存在相同的不良代码或名称!");
		}
		defectionCodeDao.save(defectionCode);
	}	
	public void saveExcelDefectionCode(DefectionCode defectionCode){
		defectionCodeDao.save(defectionCode);
	}	
	//删除记录
	public void deleteDefectionCode(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(defectionCodeDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", defectionCodeDao.get(Long.valueOf(id)).toString());
			}
			defectionCodeDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteDefectionCode(DefectionCode defectionCode){
		logUtilDao.debugLog("删除", defectionCode.toString());
		defectionCodeDao.delete(defectionCode);
	}
	
	//返回页面对象列表
	public Page<DefectionCode> list(Page<DefectionCode> page,DefectionType defectionType){
		return defectionCodeDao.list(page, defectionType);
	}
	
	public Page<DefectionCode> listByParent(Page<DefectionCode> page,Long parentId){
		return defectionCodeDao.listByParent(page, parentId);
	}
	
	public Page<DefectionCode> getCodeByParams(Page<DefectionCode> page, JSONObject params){
		return defectionCodeDao.getCodeByParams(page, params);
	}
	
	public Page<DefectionCode> list(Page<DefectionCode> page,String code){
		return defectionCodeDao.list(page, code);
	}
	
	//返回所有对象列表
	public List<DefectionCode> listAll(DefectionType defectionType){
		return defectionCodeDao.getAllDefectionCodes(defectionType);
	}	
	public List<DefectionCode> listDefectionCode(String code){
		return defectionCodeDao.getDefectionCode(code);
	}
	public String listDefectionCodeByName(String name,String typeName,String businessUnit){
		return defectionCodeDao.getDefectionCodeByName(name,typeName,businessUnit);
	}	
	@SuppressWarnings("static-access")
	public String importFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Map<String, String> fieldMap1 = new HashMap<String, String>();
		fieldMap.put("不良类型编码", "defectionTypeNo");
		fieldMap.put("不良类型名称", "defectionTypeName");
		fieldMap1.put("不良编码", "defectionCodeNo");
		fieldMap1.put("不良编码名称", "defectionCodeName");
		fieldMap1.put("不良名称缩写", "defectionCodeShort");
		fieldMap1.put("不良类型编码", "");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet defectionTypeSheet = book.getSheetAt(0);
		Row defectionTypeRow = defectionTypeSheet.getRow(0);
		if (defectionTypeRow == null) {
			throw new RuntimeException("不良类别表格第一行不能为空!");
		}

		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0;; i++) {
			Cell cell = defectionTypeRow.getCell(i);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap.containsKey(value)) {
				columnMap.put(value, i);
			}
		}
		if (columnMap.keySet().size() != fieldMap.keySet().size()) {
			throw new RuntimeException("不良类别表格模板格式不正确!");
		}
		Iterator<Row> rows = defectionTypeSheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();// 标题行
		int i = 1;
		while (rows.hasNext()) {
			defectionTypeRow = rows.next();
			//判断不良类别编码是否存在
			Cell defectionTypecell = defectionTypeRow.getCell(columnMap.get("不良类型编码"));
			String defectionTypeCode="";
			if(defectionTypecell!=null){
			if(defectionTypecell.CELL_TYPE_STRING == defectionTypecell.getCellType()){
				defectionTypeCode=defectionTypecell.getStringCellValue();
			}else if(defectionTypecell.CELL_TYPE_NUMERIC == defectionTypecell.getCellType()){
				defectionTypeCode=df.format(defectionTypecell.getNumericCellValue());
			}
			}
			DefectionType defectionType=defectionTypeManager.getDefectionTypeByCode(defectionTypeCode);
			if(defectionType==null){
				defectionType = new DefectionType();
				defectionType.setCreatedTime(new Date());
				defectionType.setCompanyId(ContextUtils.getCompanyId());
				defectionType.setCreator(ContextUtils.getUserName());
				defectionType.setLastModifiedTime(new Date());
				defectionType.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : columnMap.keySet()) {
				Cell cell = defectionTypeRow.getCell(columnMap.get(columnName));
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						if(cell.getStringCellValue().equals("False")){
							setProperty(defectionType,fieldMap.get(columnName),false);
						}else if(cell.getStringCellValue().equals("True")){
							setProperty(defectionType,fieldMap.get(columnName),true);
						}
						else{
							setProperty(defectionType,fieldMap.get(columnName),cell.getStringCellValue());
						}
						
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(defectionType,fieldMap.get(columnName), date);
						} else {
							setProperty(defectionType,fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(defectionType,fieldMap.get(columnName), cell.getCellFormula());
					}
				}
			}
			try {
				defectionTypeManager.saveExcelDefectionType(defectionType);
				sb.append("不良类别第" + (i + 1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("不良类别第" + (i + 1) + "行保存失败:<font color=red>"
						+ e.getMessage() + "</font><br/>");
			}
			i++;
		}
		
		Sheet defectionSheet = book.getSheetAt(1);
		Row defectionRow = defectionSheet.getRow(0);
		if (defectionRow == null) {
			throw new RuntimeException("不良第一行不能为空!");
		}

		Map<String, Integer> defectionColumnMap = new HashMap<String, Integer>();
		for (int j = 0;; j++) {
			Cell cell = defectionRow.getCell(j);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap1.containsKey(value)) {
				defectionColumnMap.put(value, j);
			}
		}
		if (defectionColumnMap.keySet().size() != fieldMap1.keySet().size()) {
			throw new RuntimeException("不良表格模板格式不正确!");
		}
		Iterator<Row> defectionRows = defectionSheet.rowIterator();
		defectionRows.next();// 标题行
		int k = 1;
		while (defectionRows.hasNext()) {
			defectionRow = defectionRows.next();
			//判断不良是否存在
			Cell defectionCodecell = defectionRow.getCell(defectionColumnMap.get("不良编码"));
			String defectionCode="";
			if(defectionCodecell!=null){
			if(defectionCodecell.CELL_TYPE_STRING == defectionCodecell.getCellType()){
				defectionCode=defectionCodecell.getStringCellValue();
			}else if(defectionCodecell.CELL_TYPE_NUMERIC == defectionCodecell.getCellType()){
				defectionCode=df.format(defectionCodecell.getNumericCellValue());
			}
			}
			DefectionCode d=getDefectionByCode(defectionCode);
			if(d==null){
				d = new DefectionCode();
				d.setCreatedTime(new Date());
				d.setCompanyId(ContextUtils.getCompanyId());
				d.setCreator(ContextUtils.getUserName());
				d.setLastModifiedTime(new Date());
				d.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : defectionColumnMap.keySet()) {
				Cell cell = defectionRow.getCell(defectionColumnMap.get(columnName));
				if(!columnName.equals("不良类型编码")){
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						if(cell.getStringCellValue().equals("False")){
							setProperty(d,fieldMap1.get(columnName),false);
						}else if(cell.getStringCellValue().equals("True")){
							setProperty(d,fieldMap1.get(columnName),true);
						}
						else{
							setProperty(d,fieldMap1.get(columnName),cell.getStringCellValue());
						}
						
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(d,fieldMap1.get(columnName), date);
						} else {
							setProperty(d,fieldMap1.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(d,fieldMap1.get(columnName), cell.getCellFormula());
					}
					
				}
			}else{
				String defectionTypeNo="";
				if(Cell.CELL_TYPE_STRING == cell.getCellType()){
					defectionTypeNo=cell.getStringCellValue();
				}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
					defectionTypeNo=df.format(cell.getNumericCellValue());
				}
				DefectionType defectionType=defectionTypeManager.getDefectionTypeByCode(defectionTypeNo);
				d.setDefectionType(defectionType);
			}
		}
			try {
				saveExcelDefectionCode(d);
				sb.append("不良第" + (k + 1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("不良第" + (k + 1) + "行保存失败:<font color=red>"
						+ e.getMessage() + "</font><br/>");
			}
			k++;
		}
		
		file.delete();
		return sb.toString();
	}
	
	private void setProperty(Object obj, String property, Object value) throws Exception {
		Class<?> type = PropertyUtils.getPropertyType(obj, property);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, property, null);
			} else {
				if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Boolean.valueOf(value.toString()));
				} else {
					PropertyUtils.setProperty(obj, property, value);
				}
			}
		}
	}
}
