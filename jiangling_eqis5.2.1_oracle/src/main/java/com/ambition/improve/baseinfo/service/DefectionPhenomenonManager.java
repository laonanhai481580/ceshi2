package com.ambition.improve.baseinfo.service;

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

import com.ambition.improve.baseinfo.dao.DefectionPhenomenonDao;
import com.ambition.improve.entity.DefectionPhenomenon;
import com.ambition.improve.entity.ProblemDescrible;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class DefectionPhenomenonManager {
	@Autowired
	private DefectionPhenomenonDao defectionPhenomenonDao;
	@Autowired
	private ProblemDescribleManager problemDescribleManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public DefectionPhenomenon getDefectionPhenomenon(Long id){
		return defectionPhenomenonDao.get(id);
	}
	
	public DefectionPhenomenon getDefectionByCode(String code){
		return defectionPhenomenonDao.getDefectionByCode(code);
	}
//	返回页面对象列表
	public Page<DefectionPhenomenon> listByParams(Page<DefectionPhenomenon> page ,String businessUnitName,String problemType,String model){
		return defectionPhenomenonDao.listByParams(page,businessUnitName,problemType,model);
	}
	//验证并保存记录
	private boolean isExistDefectionPhenomenon(Long id, String name, ProblemDescrible problemDescrible) {
		String hql = "select count(*) from DefectionPhenomenon d where d.companyId = ? and d.problemDescrible = ? and  d.defectionPhenomenonName =? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(problemDescrible);
		params.add(name);
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = defectionPhenomenonDao.getSession().createQuery(hql);
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
	public void saveDefectionPhenomenon(DefectionPhenomenon defectionPhenomenon){
		if(StringUtils.isEmpty(defectionPhenomenon.getDefectionPhenomenonName())){
			throw new RuntimeException("不良现象名称不为空!");
		}
		if(isExistDefectionPhenomenon(defectionPhenomenon.getId(),defectionPhenomenon.getDefectionPhenomenonName(),defectionPhenomenon.getProblemDescrible())){
			throw new RuntimeException("已存在相同的不良现象名称!");
		}
		defectionPhenomenonDao.save(defectionPhenomenon);
	}	
	public void saveExcelDefectionPhenomenon(DefectionPhenomenon defectionPhenomenon){
		defectionPhenomenonDao.save(defectionPhenomenon);
	}	
	//删除记录
	public void deleteDefectionPhenomenon(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(defectionPhenomenonDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", defectionPhenomenonDao.get(Long.valueOf(id)).toString());
			}
			defectionPhenomenonDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteDefectionPhenomenon(DefectionPhenomenon defectionPhenomenon){
		logUtilDao.debugLog("删除", defectionPhenomenon.toString());
		defectionPhenomenonDao.delete(defectionPhenomenon);
	}
	
	//返回页面对象列表
	public Page<DefectionPhenomenon> list(Page<DefectionPhenomenon> page,ProblemDescrible problemDescrible){
		return defectionPhenomenonDao.list(page, problemDescrible);
	}
	
	public Page<DefectionPhenomenon> listByParent(Page<DefectionPhenomenon> page,Long parentId){
		return defectionPhenomenonDao.listByParent(page, parentId);
	}
	
	public Page<DefectionPhenomenon> getCodeByParams(Page<DefectionPhenomenon> page, JSONObject params){
		return defectionPhenomenonDao.getCodeByParams(page, params);
	}
	
	public Page<DefectionPhenomenon> list(Page<DefectionPhenomenon> page,String code){
		return defectionPhenomenonDao.list(page, code);
	}
	//返回所有对象列表
	public List<DefectionPhenomenon> listAll(ProblemDescrible problemDescrible){
		return defectionPhenomenonDao.getAllDefectionPhenomenons(problemDescrible);
	}	
	public List<DefectionPhenomenon> listDefectionPhenomenon(String code){
		return defectionPhenomenonDao.getDefectionPhenomenon(code);
	}
	@SuppressWarnings("static-access")
	public String importFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Map<String, String> fieldMap1 = new HashMap<String, String>();
		fieldMap.put("不良类型", "defectionType");
		fieldMap1.put("不良现象名称", "defectionPhenomenonName");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet problemDescribleSheet = book.getSheetAt(0);
		Row problemDescribleRow = problemDescribleSheet.getRow(0);
		if (problemDescribleRow == null) {
			throw new RuntimeException("问题描述表格第一行不能为空!");
		}

		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0;; i++) {
			Cell cell = problemDescribleRow.getCell(i);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap.containsKey(value)) {
				columnMap.put(value, i);
			}
		}
		if (columnMap.keySet().size() != fieldMap.keySet().size()) {
			throw new RuntimeException("问题描述表格模板格式不正确!");
		}
		Iterator<Row> rows = problemDescribleSheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();// 标题行
		int i = 1;
		while (rows.hasNext()) {
			problemDescribleRow = rows.next();
			//判断不良类别编码是否存在
			Cell defectionTypecell = problemDescribleRow.getCell(columnMap.get("不良类型"));
			String defectionType="";
			if(defectionTypecell!=null){
			if(defectionTypecell.CELL_TYPE_STRING == defectionTypecell.getCellType()){
				defectionType=defectionTypecell.getStringCellValue();
			}else if(defectionTypecell.CELL_TYPE_NUMERIC == defectionTypecell.getCellType()){
				defectionType=df.format(defectionTypecell.getNumericCellValue());
			}
			}
			ProblemDescrible problemDescrible=problemDescribleManager.getProblemDescribleByCode(defectionType);
			if(problemDescrible==null){
				problemDescrible = new ProblemDescrible();
				problemDescrible.setCreatedTime(new Date());
				problemDescrible.setCompanyId(ContextUtils.getCompanyId());
				problemDescrible.setCreator(ContextUtils.getUserName());
				problemDescrible.setLastModifiedTime(new Date());
				problemDescrible.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : columnMap.keySet()) {
				Cell cell = problemDescribleRow.getCell(columnMap.get(columnName));
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						if(cell.getStringCellValue().equals("False")){
							setProperty(problemDescrible,fieldMap.get(columnName),false);
						}else if(cell.getStringCellValue().equals("True")){
							setProperty(problemDescrible,fieldMap.get(columnName),true);
						}
						else{
							setProperty(problemDescrible,fieldMap.get(columnName),cell.getStringCellValue());
						}
						
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(problemDescrible,fieldMap.get(columnName), date);
						} else {
							setProperty(problemDescrible,fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(problemDescrible,fieldMap.get(columnName), cell.getCellFormula());
					}
				}
			}
			try {
				problemDescribleManager.saveExcelProblemDescrible(problemDescrible);
				sb.append("问题描述第" + (i + 1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("问题描述第" + (i + 1) + "行保存失败:<font color=red>"
						+ e.getMessage() + "</font><br/>");
			}
			i++;
		}
		
		Sheet defectionSheet = book.getSheetAt(1);
		Row defectionRow = defectionSheet.getRow(0);
		if (defectionRow == null) {
			throw new RuntimeException("问题描述第一行不能为空!");
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
			throw new RuntimeException("问题描述表格模板格式不正确!");
		}
		Iterator<Row> defectionRows = defectionSheet.rowIterator();
		defectionRows.next();// 标题行
		int k = 1;
		while (defectionRows.hasNext()) {
			defectionRow = defectionRows.next();
			//判断不良是否存在
			Cell defectionPhenomenoncell = defectionRow.getCell(defectionColumnMap.get("不良现象名称"));
			String defectionPhenomenon="";
			if(defectionPhenomenoncell!=null){
			if(defectionPhenomenoncell.CELL_TYPE_STRING == defectionPhenomenoncell.getCellType()){
				defectionPhenomenon=defectionPhenomenoncell.getStringCellValue();
			}else if(defectionPhenomenoncell.CELL_TYPE_NUMERIC == defectionPhenomenoncell.getCellType()){
				defectionPhenomenon=df.format(defectionPhenomenoncell.getNumericCellValue());
			}
			}
			DefectionPhenomenon d=getDefectionByCode(defectionPhenomenon);
			if(d==null){
				d = new DefectionPhenomenon();
				d.setCreatedTime(new Date());
				d.setCompanyId(ContextUtils.getCompanyId());
				d.setCreator(ContextUtils.getUserName());
				d.setLastModifiedTime(new Date());
				d.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : defectionColumnMap.keySet()) {
				Cell cell = defectionRow.getCell(defectionColumnMap.get(columnName));
				if(!columnName.equals("不良现象名称")){
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
				String problemDescribleNo="";
				if(Cell.CELL_TYPE_STRING == cell.getCellType()){
					problemDescribleNo=cell.getStringCellValue();
				}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
					problemDescribleNo=df.format(cell.getNumericCellValue());
				}
				ProblemDescrible problemDescrible=problemDescribleManager.getProblemDescribleByCode(problemDescribleNo);
				d.setProblemDescrible(problemDescrible);
			}
		}
			try {
				saveExcelDefectionPhenomenon(d);
				sb.append("不良现象第" + (k + 1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("不良现象第" + (k + 1) + "行保存失败:<font color=red>"
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
