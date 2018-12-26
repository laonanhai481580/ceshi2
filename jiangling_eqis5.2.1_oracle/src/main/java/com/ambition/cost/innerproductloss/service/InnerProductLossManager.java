package com.ambition.cost.innerproductloss.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.cost.entity.InnerProductLoss;
import com.ambition.cost.innerproductloss.dao.InnerProductLossDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
@Service
@Transactional
public class InnerProductLossManager {
	@Autowired
	private InnerProductLossDao innerProductLossDao;
	public InnerProductLoss getInnerProductLoss(Long id){
		return innerProductLossDao.get(id);
	}

	public void saveInnerProductLoss(InnerProductLoss innerProductLoss){
		innerProductLossDao.save(innerProductLoss);
	}
	
	public void deleteInnerProductLoss(InnerProductLoss innerProductLoss){
		innerProductLossDao.delete(innerProductLoss);
	}

	public Page<InnerProductLoss> search(Page<InnerProductLoss>page){
		return innerProductLossDao.search(page);
	}

	public List<InnerProductLoss> listAll(){
		return innerProductLossDao.getAllInnerProductLoss();
	}
		
	public void deleteInnerProductLoss(Long id){
		innerProductLossDao.delete(id);
	}
	
	/**
	 * 删除
	 * @param ids
	 */
	public String deleteInnerProductLoss(String ids) {
		String[] deleteIds = ids.split(",");
		int deleteNum=0;
		int failNum=0;
		for (String id : deleteIds) {
			InnerProductLoss  innerProductLoss = innerProductLossDao.get(Long.valueOf(id));
			innerProductLossDao.delete(innerProductLoss);
				deleteNum++;
		}
		return deleteNum+" 删除成功，"+failNum+" 删除失败！";
	}
	
	/**
	 * 导入台帐数据
	 * @param file
	 * @param parent
	 * @throws Exception
	 */
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getDataFieldMap("COST_INNER_PRODUCT_LOSS");
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
			throw new AmbFrameException("Excel格式不正确!请重新导出台账数据模板!");
		}
		
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 1;
		while(rows.hasNext()){
			row = rows.next();
			Cell myCell = row.getCell(columnMap.get("维护日期"));
			if(myCell == null){
				continue;
			}
//			String dateStr = (CommonUtil.getCellValue(myCell)+"").trim();
//			if(StringUtils.isEmpty(dateStr)){
//				continue;
//			}
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
				InnerProductLoss loss = null;
				String code = (String)objMap.get("code");
				List<InnerProductLoss> list = innerProductLossDao.getAllInnerProductLossByCode(code);//判断是否已经存在
				boolean isNew = false;
				if(list.isEmpty()){
					loss = new InnerProductLoss();
					loss.setCompanyId(ContextUtils.getCompanyId());
					loss.setCreatedTime(new Date());
					loss.setCreator(ContextUtils.getUserName());
					isNew = true;
				}else{
					loss = (InnerProductLoss)list.get(0);
				}
				loss.setModifiedTime(new Date());
				loss.setModifier(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					setProperty(loss,key, objMap.get(key));
				}
				//如果客户代码不为空，客户名称为空,则根据客户代码获取客户名称
				String customerCode = loss.getCustomerCode();
				customerCode=customerCode.replace(" ","");
			/*	if(StringUtils.isNotEmpty(customerCode)){
				   CustomerList customer= customerDao.getCustomerByCode(customerCode);
				   if(null!=customer){
			    	    loss.setCustomerName(customer.getCustomerName());
			       }
			   }*/
			   this.saveInnerProductLoss(loss);
			   if(isNew){
				   sb.append("第" + (i+1) + "行保存成功!<br/>");
			   }else{
				   sb.append("第" + (i+1) + "行更新成功!<br/>");
			   }
			} catch (Exception e) {
				sb.append("第" + (i+1) + "行保存失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}
	
	private void setProperty(Object obj,String property,Object value) throws Exception{
		Class<?> type = PropertyUtils.getPropertyType(obj,property);
		if(type != null){
			if(value==null||StringUtils.isEmpty(value.toString())){
				PropertyUtils.setProperty(obj,property,null);
			}else{
				if(Date.class.getName().equals(type.getName())){
					if(null!=value&&""!=value){
						PropertyUtils.setProperty(obj,property,value);
						}
				}else if(String.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,value.toString());
				}else if(Integer.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
				}else if(Double.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
				}else if(Float.class.getName().equals(type.getName())){
					PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
				}
			}
		}
	}
	
	private Map<String,String> getDataFieldMap(String listCode){
		Map<String,String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(listCode);
		for(ListColumn column: columns.getColumns()){
			if(column.getVisible()){
				fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
			}
		}
		return fieldMap;
	}
	/**
	 * 导出内部成品损失台账模板
	 * @param qualityComplain
	 * @throws Exception
	 */
	public void createReport() throws Exception{
		InputStream inputStream = getClass().getResourceAsStream("/template/report/innerproductloss.xls");
		Workbook workbook = WorkbookFactory.create(inputStream);
		inputStream.close();
		String fileName = "内部成品损失台账模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		workbook.write(response.getOutputStream());
	}
	
	

}
