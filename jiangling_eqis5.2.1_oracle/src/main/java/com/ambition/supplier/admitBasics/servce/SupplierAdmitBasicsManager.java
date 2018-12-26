package com.ambition.supplier.admitBasics.servce;
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

import com.ambition.epm.entity.EpmOrtIndicator;
import com.ambition.epm.entity.EpmOrtItem;
import com.ambition.supplier.admitBasics.dao.SupplierAdmitBasicsDao;
import com.ambition.supplier.entity.SupplierAdmitBasics;
import com.ambition.supplier.entity.SupplierAdmitClass;
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
public class SupplierAdmitBasicsManager {
	@Autowired
	private SupplierAdmitBasicsDao admitBasicsDao;
	@Autowired
	private SupplierAdmitClassManager admitClassManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public SupplierAdmitBasics getSupplierAdmitBasics(Long id){
		return admitBasicsDao.get(id);
	}
	
	//验证并保存记录
	private boolean isExistSupplierAdmitBasics(Long id, String name, SupplierAdmitClass admitClass) {
		String hql = "select count(*) from SupplierAdmitBasics d where d.companyId = ?  and d.materialName =?   and d.supplierAdmitClass.businessUnitName=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(name);
		params.add(admitClass.getBusinessUnitName());
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = admitBasicsDao.getSession().createQuery(hql);
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
	public void saveSupplierAdmitBasics(SupplierAdmitBasics admitBasics){
		if(StringUtils.isEmpty(admitBasics.getMaterialName())){
			throw new RuntimeException("材料名称不为空!");
		}
//		if(isExistSupplierAdmitBasics(admitBasics.getId(),admitBasics.getMaterialName(),admitBasics.getSupplierAdmitClass())){
//			throw new RuntimeException(admitBasics.getSupplierAdmitClass()+"已存在相同的材料代码或名称!");
//		}
		admitBasicsDao.save(admitBasics);
	}	
	public void saveExcelSupplierAdmitBasics(SupplierAdmitBasics admitBasics){
		admitBasicsDao.save(admitBasics);
	}	
	//删除记录
	public void deleteSupplierAdmitBasics(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(admitBasicsDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", admitBasicsDao.get(Long.valueOf(id)).toString());
			}
			admitBasicsDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteSupplierAdmitBasics(SupplierAdmitBasics admitBasics){
		logUtilDao.debugLog("删除", admitBasics.toString());
		admitBasicsDao.delete(admitBasics);
	}
	
	//返回页面对象列表
	public Page<SupplierAdmitBasics> list(Page<SupplierAdmitBasics> page,SupplierAdmitClass admitBasics){
		return admitBasicsDao.list(page, admitBasics);
	}
	
	public Page<SupplierAdmitBasics> getCodeByParams(Page<SupplierAdmitBasics> page, String materialSort){
		return admitBasicsDao.getCodeByParams(page, materialSort);
	}
	
	public Page<SupplierAdmitBasics> list(Page<SupplierAdmitBasics> page,String code){
		return admitBasicsDao.list(page, code);
	}
	//返回所有对象列表
	public List<SupplierAdmitBasics> listAll(SupplierAdmitClass admitBasics){
		return admitBasicsDao.getAllSupplierAdmitBasicss(admitBasics);
	}	
	public String importFile(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Long admitClassId = Long.valueOf(Struts2Utils.getParameter("admitClassId"));
		SupplierAdmitClass supplierAdmitClass = admitClassManager.getSupplierAdmitClass(admitClassId);
		fieldMap.put("审核部门", "auditDepartment");
		fieldMap.put("资料名称", "materialName");
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
			SupplierAdmitBasics item=new SupplierAdmitBasics();
			item.setCreatedTime(new Date());
			item.setSupplierAdmitClass(supplierAdmitClass);
			item.setCompanyId(ContextUtils.getCompanyId());
			item.setCreator(ContextUtils.getLoginName());
			item.setCreatorName(ContextUtils.getUserName());
			item.setLastModifiedTime(new Date());
			item.setLastModifier(ContextUtils.getUserName());
			item.setMaterialSort(supplierAdmitClass.getSupplierAdmitClass());
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
				admitBasicsDao.save(item);
				sb.append("第" + (i) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("第" + (i) + "行保存失败:<font color=red>" + e.getMessage() + "</font><br/>");
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
