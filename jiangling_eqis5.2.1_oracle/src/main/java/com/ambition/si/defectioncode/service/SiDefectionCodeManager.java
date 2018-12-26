package com.ambition.si.defectioncode.service;

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

import com.ambition.si.defectioncode.dao.SiDefectionCodeDao;
import com.ambition.si.entity.SiDefectionCode;
import com.ambition.si.entity.SiDefectionType;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class SiDefectionCodeManager {
	@Autowired
	private SiDefectionCodeDao defectionCodeDao;
	@Autowired
	private SiDefectionTypeManager defectionTypeManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public SiDefectionCode getDefectionCode(Long id){
		return defectionCodeDao.get(id);
	}
	
	public SiDefectionCode getDefectionByCode(String code){
		return defectionCodeDao.getDefectionByCode(code);
	}
	
	//验证并保存记录
	private boolean isExistDefectionCode(Long id, String no, String name, SiDefectionType defectionType) {
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
	public void saveDefectionCode(SiDefectionCode defectionCode){
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
	public void saveExcelDefectionCode(SiDefectionCode defectionCode){
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
	public void deleteDefectionCode(SiDefectionCode defectionCode){
		logUtilDao.debugLog("删除", defectionCode.toString());
		defectionCodeDao.delete(defectionCode);
	}
	
	//返回页面对象列表
	public Page<SiDefectionCode> list(Page<SiDefectionCode> page,SiDefectionType defectionType){
		return defectionCodeDao.list(page, defectionType);
	}
	
	public Page<SiDefectionCode> listByParent(Page<SiDefectionCode> page,Long parentId){
		return defectionCodeDao.listByParent(page, parentId);
	}
	
	public Page<SiDefectionCode> getCodeByParams(Page<SiDefectionCode> page, JSONObject params,SiDefectionType defectionType){
		return defectionCodeDao.getCodeByParams(page, params,defectionType);
	}
	
	public Page<SiDefectionCode> list(Page<SiDefectionCode> page,String code){
		return defectionCodeDao.list(page, code);
	}
	//返回所有对象列表
	public List<SiDefectionCode> listAll(SiDefectionType defectionType){
		return defectionCodeDao.getAllDefectionCodes(defectionType);
	}	
	public List<SiDefectionCode> listDefectionCode(String code){
		return defectionCodeDao.getDefectionCode(code);
	}
	public String importFile(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Long defectionTypeId = Long.valueOf(Struts2Utils.getParameter("defectionTypeId"));
		SiDefectionType defectionType = defectionTypeManager.getDefectionType(Long.valueOf(defectionTypeId));
		fieldMap.put("不良代码", "defectionCodeNo");
		fieldMap.put("不良项目名称", "defectionCodeName");
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
			SiDefectionCode defectionCode=new SiDefectionCode();
			defectionCode.setCreatedTime(new Date());
			defectionCode.setDefectionType(defectionType);
			defectionCode.setCompanyId(ContextUtils.getCompanyId());
			defectionCode.setCreator(ContextUtils.getLoginName());
			defectionCode.setCreatorName(ContextUtils.getUserName());
			defectionCode.setLastModifiedTime(new Date());
			defectionCode.setLastModifier(ContextUtils.getUserName());
			for(String columnName : columnMap.keySet()){
				Cell cell = row.getCell(columnMap.get(columnName));
				if(cell != null){
					if(Cell.CELL_TYPE_STRING == cell.getCellType()){
						setProperty(defectionCode, fieldMap.get(columnName),cell.getStringCellValue());
					}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(defectionCode, fieldMap.get(columnName),date);
						}else{
							setProperty(defectionCode, fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
						setProperty(defectionCode, fieldMap.get(columnName),cell.getCellFormula());
					}					
				}	
			}
			try {
				SiDefectionCode code=this.getDefectionByCode(defectionCode.getDefectionCodeNo());
				if(code==null){
					if(defectionCode.getDefectionCodeNo()!=null&&!defectionCode.getDefectionCodeNo().equals("")){
						defectionCodeDao.save(defectionCode);
						sb.append("第" + (i+1) + "行保存成功!<br/>");
						i++;
					}
				}else{
					code.setLastModifiedTime(new Date());
					code.setLastModifier(ContextUtils.getUserName());
					code.setDefectionCodeName(defectionCode.getDefectionCodeName());
					sb.append("第" + (i+1) + "行更新成功!<br/>");
					i++;
				}				
			} catch (Exception e) {
				sb.append("第" + (i+1) + "行保存失败:<font color=red>" + e.getMessage() + "</font><br/>");
				i++;
			}
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
