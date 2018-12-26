package com.ambition.gp.gpSurvey.services;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
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

import com.ambition.gp.entity.GpSurvey;
import com.ambition.gp.gpSurvey.dao.GpSurveyDao;
import com.ambition.gsm.inspectionplan.dao.InspectionPlanDao;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class GpSurveyManager {
	@Autowired
	private GpSurveyDao gpSurveyDao;
	@Autowired
	private InspectionPlanDao inspectionPlanDao;
	
	public GpSurvey getGpSurvey(Long id){
		return gpSurveyDao.get(id);
	}
	public void saveGpSurvey(GpSurvey gpSurvey){
		gpSurveyDao.save(gpSurvey);
	}
	
	public Page<GpSurvey> list(Page<GpSurvey> page){
		return gpSurveyDao.list(page);
	}
	
	public List<GpSurvey> listAll(){
		return gpSurveyDao.getGpSurvey();
	}
	
	
	public void deleteGpSurvey(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
			GpSurvey gpSurvey=gpSurveyDao.get(Long.valueOf(id));
			if(gpSurvey.getId() != null){
				gpSurveyDao.delete(gpSurvey);
			}
		}
	}
	
	public Page<GpSurvey> search(Page<GpSurvey> page){
		return gpSurveyDao.search(page);
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("GP_EXEMPTION");
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
				GpSurvey gpSurvey = new GpSurvey();
				gpSurvey.setCompanyId(ContextUtils.getCompanyId());
				gpSurvey.setCreatedTime(new Date());
				gpSurvey.setCreator(ContextUtils.getUserName());
				gpSurvey.setModifiedTime(new Date());
				gpSurvey.setModifier(ContextUtils.getUserName());
				User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
				String subName=user.getSubCompanyName();
				gpSurvey.setFactoryClassify(subName);
				for(String key : objMap.keySet()){
					CommonUtil1.setProperty(gpSurvey,key, objMap.get(key));
				}
				gpSurveyDao.save(gpSurvey);
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
