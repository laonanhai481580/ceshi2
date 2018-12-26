package com.ambition.epm.baseinfo.service;

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

import com.ambition.epm.baseinfo.dao.EpmOrtItemDao;
import com.ambition.epm.entity.EpmOrtIndicator;
import com.ambition.epm.entity.EpmOrtItem;
import com.ambition.qsm.entity.CustomerAudit;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class EpmOrtItemManager {
	@Autowired
	private EpmOrtItemDao ortItemDao;
	@Autowired
	private EpmOrtIndicatorManager ortIndicatorManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public EpmOrtItem getOrtItem(Long id){
		return ortItemDao.get(id);
	}
	
	public EpmOrtItem getOrtItemByItemName(String itemName){
		return ortItemDao.getOrtItemByItemName(itemName);
	}
	
	//验证并保存记录
	private boolean isExistOrtItem(Long id, String condition, String itemName, EpmOrtIndicator ortIndicator) {
		String hql = "select count(*) from EpmOrtItem d where d.companyId = ? and d.ortIndicator = ? and (d.condition =? or d.itemName =?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(ortIndicator);
		params.add(condition);
		params.add(itemName);
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = ortItemDao.getSession().createQuery(hql);
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
	public void saveOrtItem(EpmOrtItem ortItem){
		if(StringUtils.isEmpty(ortItem.getItemName())){
			throw new RuntimeException("测试项目名称不为空!");
		}
		if(StringUtils.isEmpty(ortItem.getCondition())){
			throw new RuntimeException("测试条件不为空!");
		}
//		if(isExistOrtItem(ortItem.getId(),ortItem.getCondition(),ortItem.getItemName(),ortItem.getOrtIndicator())){
//			throw new RuntimeException("已存在相同的测试项目!");
//		}
		ortItemDao.save(ortItem);
	}	
	public void saveExcelOrtItem(EpmOrtItem ortItem){
		ortItemDao.save(ortItem);
	}	
	//删除记录
	public void deleteOrtItem(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(ortItemDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", ortItemDao.get(Long.valueOf(id)).toString());
			}
			ortItemDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteOrtItem(EpmOrtItem ortItem){
		logUtilDao.debugLog("删除", ortItem.toString());
		ortItemDao.delete(ortItem);
	}
	
	//返回页面对象列表
	public Page<EpmOrtItem> list(Page<EpmOrtItem> page,EpmOrtIndicator ortIndicator){
		return ortItemDao.list(page, ortIndicator);
	}
	
	public Page<EpmOrtItem> listByParent(Page<EpmOrtItem> page,Long parentId){
		return ortItemDao.listByParent(page, parentId);
	}
	
	public Page<EpmOrtItem> getCodeByParams(Page<EpmOrtItem> page, JSONObject params,EpmOrtIndicator ortIndicator){
		return ortItemDao.getCodeByParams(page, params,ortIndicator);
	}
	
	public Page<EpmOrtItem> list(Page<EpmOrtItem> page,String code){
		return ortItemDao.list(page, code);
	}
	//返回所有对象列表
	public List<EpmOrtItem> listAll(EpmOrtIndicator ortIndicator){
		return ortItemDao.getAllOrtItems(ortIndicator);
	}	
	public List<EpmOrtItem> listOrtItem(String itemName){
		return ortItemDao.getOrtItem(itemName);
	}
	@SuppressWarnings("static-access")
	public String importFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Map<String, String> fieldMap1 = new HashMap<String, String>();
		fieldMap.put("不良类型编码", "ortIndicatorNo");
		fieldMap.put("不良类型名称", "ortIndicatorName");
		fieldMap1.put("不良编码", "ortItemNo");
		fieldMap1.put("不良编码名称", "ortItemName");
		fieldMap1.put("不良名称缩写", "ortItemShort");
		fieldMap1.put("不良类型编码", "");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet ortIndicatorSheet = book.getSheetAt(0);
		Row ortIndicatorRow = ortIndicatorSheet.getRow(0);
		if (ortIndicatorRow == null) {
			throw new RuntimeException("不良类别表格第一行不能为空!");
		}

		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0;; i++) {
			Cell cell = ortIndicatorRow.getCell(i);
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
		Iterator<Row> rows = ortIndicatorSheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();// 标题行
		int i = 1;
		while (rows.hasNext()) {
			ortIndicatorRow = rows.next();
			//判断不良类别编码是否存在
			Cell ortIndicatorcell = ortIndicatorRow.getCell(columnMap.get("客户编号"));
			String customerNo="";
			if(ortIndicatorcell!=null){
				if(ortIndicatorcell.CELL_TYPE_STRING == ortIndicatorcell.getCellType()){
					customerNo=ortIndicatorcell.getStringCellValue();
				}else if(ortIndicatorcell.CELL_TYPE_NUMERIC == ortIndicatorcell.getCellType()){
					customerNo=df.format(ortIndicatorcell.getNumericCellValue());
				}
			}
			ortIndicatorcell = ortIndicatorRow.getCell(columnMap.get("机种"));
			String model="";
			if(ortIndicatorcell!=null){
				if(ortIndicatorcell.CELL_TYPE_STRING == ortIndicatorcell.getCellType()){
					model=ortIndicatorcell.getStringCellValue();
				}else if(ortIndicatorcell.CELL_TYPE_NUMERIC == ortIndicatorcell.getCellType()){
					model=df.format(ortIndicatorcell.getNumericCellValue());
				}
			}
			ortIndicatorcell = ortIndicatorRow.getCell(columnMap.get("样品类型"));
			String samplType="";
			if(ortIndicatorcell!=null){
				if(ortIndicatorcell.CELL_TYPE_STRING == ortIndicatorcell.getCellType()){
					samplType=ortIndicatorcell.getStringCellValue();
				}else if(ortIndicatorcell.CELL_TYPE_NUMERIC == ortIndicatorcell.getCellType()){
					samplType=df.format(ortIndicatorcell.getNumericCellValue());
				}
			}
			EpmOrtIndicator ortIndicator=ortIndicatorManager.getOrtIndicatorBySth(customerNo,model,samplType);
			if(ortIndicator==null){
				ortIndicator = new EpmOrtIndicator();
				ortIndicator.setCreatedTime(new Date());
				ortIndicator.setCompanyId(ContextUtils.getCompanyId());
				ortIndicator.setCreator(ContextUtils.getUserName());
				ortIndicator.setLastModifiedTime(new Date());
				ortIndicator.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : columnMap.keySet()) {
				Cell cell = ortIndicatorRow.getCell(columnMap.get(columnName));
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						if(cell.getStringCellValue().equals("False")){
							setProperty(ortIndicator,fieldMap.get(columnName),false);
						}else if(cell.getStringCellValue().equals("True")){
							setProperty(ortIndicator,fieldMap.get(columnName),true);
						}
						else{
							setProperty(ortIndicator,fieldMap.get(columnName),cell.getStringCellValue());
						}
						
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(ortIndicator,fieldMap.get(columnName), date);
						} else {
							setProperty(ortIndicator,fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(ortIndicator,fieldMap.get(columnName), cell.getCellFormula());
					}
				}
			}
			try {
				ortIndicatorManager.saveOrtIndicator(ortIndicator);
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
			throw new RuntimeException("测试项目模板格式不正确!");
		}
		Iterator<Row> defectionRows = defectionSheet.rowIterator();
		defectionRows.next();// 标题行
		int k = 1;
		while (defectionRows.hasNext()) {
			defectionRow = defectionRows.next();
			//判断测试项目是否存在
			Cell ortItemcell = defectionRow.getCell(defectionColumnMap.get("测试项目名称"));
			String itemName="";
			if(ortItemcell!=null){
			if(ortItemcell.CELL_TYPE_STRING == ortItemcell.getCellType()){
				itemName=ortItemcell.getStringCellValue();
			}else if(ortItemcell.CELL_TYPE_NUMERIC == ortItemcell.getCellType()){
				itemName=df.format(ortItemcell.getNumericCellValue());
			}
			}
			EpmOrtItem d=getOrtItemByItemName(itemName);
			if(d==null){
				d = new EpmOrtItem();
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
				String ortIndicatorNo="";
				if(Cell.CELL_TYPE_STRING == cell.getCellType()){
					ortIndicatorNo=cell.getStringCellValue();
				}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
					ortIndicatorNo=df.format(cell.getNumericCellValue());
				}
				//EpmOrtIndicator ortIndicator=ortIndicatorManager.getOrtIndicatorByCode(ortIndicatorNo);
				//d.setOrtIndicator(ortIndicator);
			}
		}
			try {
				saveExcelOrtItem(d);
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

	public Page<EpmOrtItem> listPageByParams(Page<EpmOrtItem> page,
			String customerNo, String productNo, String sampleType) {
		// TODO Auto-generated method stub
		String hql = " from EpmOrtItem e where e.ortIndicator.customerNo=? and e.ortIndicator.model=? and e.ortIndicator.samplType=?";
		return ortItemDao.searchPageByHql(page, hql, customerNo,productNo,sampleType);
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Long ortIndicatorId = Long.valueOf(Struts2Utils.getParameter("ortIndicatorId"));
		EpmOrtIndicator ortIndicator = ortIndicatorManager.getOrtIndicator(ortIndicatorId);
		fieldMap.put("测试项目名称", "itemName");
		fieldMap.put("测试条件", "condition");
		fieldMap.put("测试数量", "count");
		fieldMap.put("判定标准Green", "standardGreen");
		fieldMap.put("判定标准Yellow", "standardYellow");
		fieldMap.put("判定标准Red", "standardRed");
		fieldMap.put("备注", "remark");
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
		if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new RuntimeException("模板格式不正确!");
		}
		Iterator<Row> rows = sheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();//标题行
		int i = 1;
		while(rows.hasNext()){
			row = rows.next();
			EpmOrtItem item=new EpmOrtItem();
			item.setCreatedTime(new Date());
			item.setOrtIndicator(ortIndicator);
			item.setCompanyId(ContextUtils.getCompanyId());
			item.setCreator(ContextUtils.getLoginName());
			item.setCreatorName(ContextUtils.getUserName());
			item.setLastModifiedTime(new Date());
			item.setLastModifier(ContextUtils.getUserName());
			for(String columnName : columnMap.keySet()){
				Cell cell = row.getCell(columnMap.get(columnName));
				if(cell != null){
					if(Cell.CELL_TYPE_STRING == cell.getCellType()){
						setProperty(item, fieldMap.get(columnName),cell.getStringCellValue());
					}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(item, fieldMap.get(columnName),date);
						}else{
							setProperty(item, fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
						setProperty(item, fieldMap.get(columnName),cell.getCellFormula());
					}
					
				}
				
			}
			try {
				ortItemDao.save(item);
//				ortIndicator.getEpmOrtItems().add(item);
//				ortIndicatorManager.saveOrtIndicator(ortIndicator);
				sb.append("第" + (i+1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("第" + (i+1) + "行保存失败:<font color=red>" + e.getMessage() + "</font><br/>");
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
