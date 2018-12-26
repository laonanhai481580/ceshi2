package com.ambition.aftersales.baseinfo.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.OfilmModelDao;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.OfilmModel;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class OfilmModelManager {
	@Autowired
	private OfilmModelDao ofilmModelDao;
	@Autowired
	private CustomerListManager customerListManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public OfilmModel getOfilmModel(Long id){
		return ofilmModelDao.get(id);
	}
	
	public OfilmModel getDefectionByCode(String code){
		return ofilmModelDao.getDefectionByCode(code);
	}
	public void saveOfilmModel(OfilmModel ofilmModel){
		if(StringUtils.isEmpty(ofilmModel.getOfilmModel())){
			throw new RuntimeException("欧菲机种不为空!");
		}
		if(StringUtils.isEmpty(ofilmModel.getCustomerModel())){
			throw new RuntimeException("客户端机种不为空!");
		}
		ofilmModelDao.save(ofilmModel);
	}	
	public void saveExcelOfilmModel(OfilmModel ofilmModel){
		ofilmModelDao.save(ofilmModel);
	}	
	//删除记录
	public void deleteOfilmModel(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(ofilmModelDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", ofilmModelDao.get(Long.valueOf(id)).toString());
			}
			ofilmModelDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteOfilmModel(OfilmModel ofilmModel){
		logUtilDao.debugLog("删除", ofilmModel.toString());
		ofilmModelDao.delete(ofilmModel);
	}
	
	//返回页面对象列表
	public Page<OfilmModel> list(Page<OfilmModel> page,CustomerList customerList){
		return ofilmModelDao.list(page, customerList);
	}
	
	public Page<OfilmModel> listByParent(Page<OfilmModel> page,Long parentId){
		return ofilmModelDao.listByParent(page, parentId);
	}
	
	public Page<OfilmModel> getCodeByParams(Page<OfilmModel> page, JSONObject params){
		return ofilmModelDao.getCodeByParams(page, params);
	}
	
	public Page<OfilmModel> list(Page<OfilmModel> page,String code){
		return ofilmModelDao.list(page, code);
	}
	//返回所有对象列表
	public List<OfilmModel> listAll(CustomerList customerList){
		return ofilmModelDao.getAllOfilmModels(customerList);
	}	
//	返回页面对象列表
	public Page<OfilmModel> listByParams(Page<OfilmModel> page ,String customerName){
		return ofilmModelDao.listByParams(page,customerName);
	}
	public List<OfilmModel> listOfilmModel(String code){
		return ofilmModelDao.getOfilmModel(code);
	}
	public String importFile(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Long defectionTypeId = Long.valueOf(Struts2Utils.getParameter("customerListId"));
		CustomerList customerList = customerListManager.getCustomerList(Long.valueOf(defectionTypeId));
		fieldMap.put("欧菲机种", "ofilmModel");
		fieldMap.put("客户端机种", "customerModel");
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
			throw new RuntimeException("模板格式不正确!");
		}*/
		Iterator<Row> rows = sheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();//标题行
		int i = 0;
		while(rows.hasNext()){
			row = rows.next();
			OfilmModel ofilmModel=new OfilmModel();
			ofilmModel.setCreatedTime(new Date());
			ofilmModel.setCustomerList(customerList);
			ofilmModel.setCompanyId(ContextUtils.getCompanyId());
			ofilmModel.setCreator(ContextUtils.getLoginName());
			ofilmModel.setCreatorName(ContextUtils.getUserName());
			ofilmModel.setLastModifiedTime(new Date());
			ofilmModel.setLastModifier(ContextUtils.getUserName());
			for(String columnName : columnMap.keySet()){
				Cell cell = row.getCell(columnMap.get(columnName));
				if(cell != null){
					if(Cell.CELL_TYPE_STRING == cell.getCellType()){
						setProperty(ofilmModel, fieldMap.get(columnName),cell.getStringCellValue());
					}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(ofilmModel, fieldMap.get(columnName),date);
						}else{
							setProperty(ofilmModel, fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
						setProperty(ofilmModel, fieldMap.get(columnName),cell.getCellFormula());
					}					
				}	
			}
			try {
				List<OfilmModel> list=this.listOfilmModel(ofilmModel.getOfilmModel());
				if(list.size()==0){
					if(ofilmModel.getOfilmModel()!=null&&!ofilmModel.getOfilmModel().equals("")){
						ofilmModelDao.save(ofilmModel);
						sb.append("第" + (i+1) + "行保存成功!<br/>");
						i++;
					}
				}else{
					OfilmModel model=list.get(0);
					model.setLastModifiedTime(new Date());
					model.setLastModifier(ContextUtils.getUserName());
					model.setCustomerModel(ofilmModel.getCustomerModel());
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
