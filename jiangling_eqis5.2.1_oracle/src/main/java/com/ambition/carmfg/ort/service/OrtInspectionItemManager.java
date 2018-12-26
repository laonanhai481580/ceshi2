package com.ambition.carmfg.ort.service;

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

import com.ambition.carmfg.entity.OrtCustomer;
import com.ambition.carmfg.entity.OrtInspectionItem;
import com.ambition.carmfg.ort.dao.OrtInspectionItemDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;
/**
 * 
 * 类名:ORT计划Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPFs
 * @version 1.00 2016年8月31日 发布
 */
@Service
@Transactional
public class OrtInspectionItemManager {
	@Autowired
	private OrtInspectionItemDao inspectionItemDao;
	@Autowired
	private OrtCustomerManager customerManager;
	public OrtInspectionItem getDefectionByItem(String testItem){
		return inspectionItemDao.getDefectionByItem(testItem);
	}		
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public OrtInspectionItem getInspectionItem(Long id){
		return inspectionItemDao.get(id);
	}

	//验证并保存记录
	private boolean isExistInspectionItem(Long id, String testItem, OrtCustomer customer) {
		String hql = "select count(*) from OrtInspectionItem d where d.companyId = ? and d.ortCustomer = ? and d.testItem =? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(customer);
		params.add(testItem);
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = inspectionItemDao.getSession().createQuery(hql);
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
	public void saveInspectionItem(OrtInspectionItem inspectionItem){
		if(StringUtils.isEmpty(inspectionItem.getTestItem())){
			throw new RuntimeException("测试项目不为空!");
		}

		if(isExistInspectionItem(inspectionItem.getId(),inspectionItem.getTestItem(),inspectionItem.getOrtCustomer())){
			throw new RuntimeException("已存在相同的测试项目!");
		}
		inspectionItemDao.save(inspectionItem);
	}	
	public void saveExcelInspectionItem(OrtInspectionItem inspectionItem){
		inspectionItemDao.save(inspectionItem);
	}	
	//删除记录
	public void deleteInspectionItem(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(inspectionItemDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", inspectionItemDao.get(Long.valueOf(id)).toString());
			}
			inspectionItemDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteInspectionItem(OrtInspectionItem inspectionItem){
		logUtilDao.debugLog("删除", inspectionItem.toString());
		inspectionItemDao.delete(inspectionItem);
	}
	
	//返回页面对象列表
	public Page<OrtInspectionItem> list(Page<OrtInspectionItem> page,OrtCustomer customer){
		return inspectionItemDao.list(page, customer);
	}
	
	public Page<OrtInspectionItem> listByParent(Page<OrtInspectionItem> page,Long parentId){
		return inspectionItemDao.listByParent(page, parentId);
	}
	
	public Page<OrtInspectionItem> getCodeByParams(Page<OrtInspectionItem> page, JSONObject params,OrtCustomer customer){
		return inspectionItemDao.getCodeByParams(page, params,customer);
	}
//	返回页面对象列表
	public Page<OrtInspectionItem> listByParams(Page<OrtInspectionItem> page ,String customerNo){
		return inspectionItemDao.listByParams(page,customerNo);
	}	
	public Page<OrtInspectionItem> list(Page<OrtInspectionItem> page,String code){
		return inspectionItemDao.list(page, code);
	}
	//返回所有对象列表
	public List<OrtInspectionItem> listAll(OrtCustomer customer){
		return inspectionItemDao.getAllInspectionItems(customer);
	}	

	@SuppressWarnings("static-access")
	public String importFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Map<String, String> fieldMap1 = new HashMap<String, String>();
		fieldMap.put("客户编码", "customerNo");
		fieldMap.put("客户名称", "customerName");
		fieldMap1.put("测试项目", "testItem");
		fieldMap1.put("测试条件", "testCondition");
		fieldMap1.put("测试数量", "value");
		fieldMap1.put("判定标准", "judgeStandard");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet customerSheet = book.getSheetAt(0);
		Row customerRow = customerSheet.getRow(0);
		if (customerRow == null) {
			throw new RuntimeException("测试项目表格第一行不能为空!");
		}

		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0;; i++) {
			Cell cell = customerRow.getCell(i);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap.containsKey(value)) {
				columnMap.put(value, i);
			}
		}
		if (columnMap.keySet().size() != fieldMap.keySet().size()) {
			throw new RuntimeException("测试项目表格模板格式不正确!");
		}
		Iterator<Row> rows = customerSheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();// 标题行
		int i = 1;
		while (rows.hasNext()) {
			customerRow = rows.next();
			//判断客户编码是否存在
			Cell customercell = customerRow.getCell(columnMap.get("客户编码"));
			String customerNo="";
			if(customercell!=null){
			if(customercell.CELL_TYPE_STRING == customercell.getCellType()){
				customerNo=customercell.getStringCellValue();
			}else if(customercell.CELL_TYPE_NUMERIC == customercell.getCellType()){
				customerNo=df.format(customercell.getNumericCellValue());
			}
			}
			OrtCustomer customer=customerManager.getOrtCustomerByCode(customerNo);
			if(customer==null){
				customer = new OrtCustomer();
				customer.setCreatedTime(new Date());
				customer.setCompanyId(ContextUtils.getCompanyId());
				customer.setCreator(ContextUtils.getUserName());
				customer.setLastModifiedTime(new Date());
				customer.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : columnMap.keySet()) {
				Cell cell = customerRow.getCell(columnMap.get(columnName));
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						if(cell.getStringCellValue().equals("False")){
							setProperty(customer,fieldMap.get(columnName),false);
						}else if(cell.getStringCellValue().equals("True")){
							setProperty(customer,fieldMap.get(columnName),true);
						}
						else{
							setProperty(customer,fieldMap.get(columnName),cell.getStringCellValue());
						}
						
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(customer,fieldMap.get(columnName), date);
						} else {
							setProperty(customer,fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(customer,fieldMap.get(columnName), cell.getCellFormula());
					}
				}
			}
			try {
				customerManager.saveExcelOrtCustomer(customer);
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
			Cell inspectionItemcell = defectionRow.getCell(defectionColumnMap.get("测试项目"));
			String testItem="";
			if(inspectionItemcell!=null){
			if(inspectionItemcell.CELL_TYPE_STRING == inspectionItemcell.getCellType()){
				testItem=inspectionItemcell.getStringCellValue();
			}else if(inspectionItemcell.CELL_TYPE_NUMERIC == inspectionItemcell.getCellType()){
				testItem=df.format(inspectionItemcell.getNumericCellValue());
			}
			}
			OrtInspectionItem d=getDefectionByItem(testItem);
			if(d==null){
				d = new OrtInspectionItem();
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
				String customerNo="";
				if(Cell.CELL_TYPE_STRING == cell.getCellType()){
					customerNo=cell.getStringCellValue();
				}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
					customerNo=df.format(cell.getNumericCellValue());
				}
				OrtCustomer customer=customerManager.getOrtCustomerByCode(customerNo);
				d.setOrtCustomer(customer);
			}
		}
			try {
				saveExcelInspectionItem(d);
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
