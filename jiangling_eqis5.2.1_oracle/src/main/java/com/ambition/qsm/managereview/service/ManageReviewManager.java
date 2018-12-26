package com.ambition.qsm.managereview.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.entity.ManageReview;
import com.ambition.qsm.managereview.dao.ManageReviewDao;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:管理评审表Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月27日 发布
 */
@Service
@Transactional
public class ManageReviewManager extends AmbWorkflowManagerBase<ManageReview>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ManageReviewDao manageReviewDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public Class<ManageReview> getEntityInstanceClass() {
		return ManageReview.class;
	}

	@Override
	public String getEntityListCode() {
		return ManageReview.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<ManageReview, Long> getHibernateDao() {
		return manageReviewDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "manage-review";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "管理评审表流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "manage-review.xlsx", ManageReview.ENTITY_LIST_NAME);
	}
	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps 子表对象
	  * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	public void saveEntity(ManageReview report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("QSM_MANAGE_REVIEW");
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
				
				ManageReview manageReviews = new ManageReview();
				manageReviews.setCompanyId(ContextUtils.getCompanyId());
				manageReviews.setCreatedTime(new Date());
				manageReviews.setCreator(ContextUtils.getUserName());
				manageReviews.setModifiedTime(new Date());
				manageReviews.setModifier(ContextUtils.getUserName());
				
				String inspectionNo = formCodeGenerated.generateManageReviewNo();
				manageReviews.setFormNo(inspectionNo);
				for(String key : objMap.keySet()){
					CommonUtil1.setProperty(manageReviews,key, objMap.get(key));
				}
				manageReviewDao.getSession().save(manageReviews);
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
