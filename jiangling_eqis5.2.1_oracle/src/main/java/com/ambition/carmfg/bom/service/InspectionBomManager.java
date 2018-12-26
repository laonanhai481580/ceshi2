package com.ambition.carmfg.bom.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.ambition.carmfg.bom.dao.InspectionBomDao;
import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.carmfg.entity.ProductBomInspection;
import com.ambition.si.entity.SiDefectionCode;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class InspectionBomManager {
	@Autowired
	private InspectionBomDao inspectionBomDao;	
	@Autowired
	private ProductBomDao productBomDao;
	public ProductBomInspection getInspectionBom(Long id){
		return inspectionBomDao.get(id);
	}
	
	public void deleteProductBomInspection(ProductBomInspection inspectionBom){
		inspectionBomDao.delete(inspectionBom);
	}

	public Page<ProductBomInspection> search(Page<ProductBomInspection>page){
		return inspectionBomDao.search(page);
	}

	public List<ProductBomInspection> listAll(){
		return inspectionBomDao.getAllInspectionBom();
	}
		
	public void deleteInspectionBom(Long id){
		inspectionBomDao.delete(id);
	}
	public void deleteInspectionBom(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			ProductBomInspection  inspectionBom = inspectionBomDao.get(Long.valueOf(id));
			if(inspectionBom.getId() != null){
				inspectionBomDao.delete(inspectionBom);
			}
		}
	}
	public void saveInspectionBom(ProductBomInspection inspectionBom){
		inspectionBomDao.save(inspectionBom);
	}
	public ProductBomInspection getProductBomInspection(String code){
		return inspectionBomDao.getProductBomInspection(code);
	}
	/**
	 * 添加物料
	 * @param inspectingIndicatorId
	 * @param inspectingItemIds
	 * @return
	 */
	public int addBom(String bomIds){
		int addCount = 0;
		for(String bomId : bomIds.split(",")){
			if(StringUtils.isNotEmpty(bomId)){
				ProductBom productBom =productBomDao.get(Long.valueOf(bomId));
					//检查是否已经添加
					String hql = "select count(i.id) from ProductBomInspection i where i.materielName = ? and i.materielCode = ?";
					List<?> list = inspectionBomDao.find(hql,productBom.getMaterielName(),productBom.getMaterielCode());
					if(Integer.valueOf(list.get(0).toString())==0){
						ProductBomInspection bom = new ProductBomInspection();
						bom.setMaterielName(productBom.getMaterielName());
						bom.setMaterielCode(productBom.getMaterielCode());
						bom.setMaterialType(productBom.getMaterialType());
						bom.setMaterielModel(productBom.getMaterielModel());
						bom.setUnits(productBom.getUnits());
						bom.setMaterialTypeCode(productBom.getMaterialTypeCode());
						bom.setRemark(productBom.getRemark());
						inspectionBomDao.save(bom);
						addCount++;
				}
			}
		}
		return addCount;
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
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("MFG_PRODUCT_BOM_INSPECTION");
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
				ProductBomInspection inspectionBom = new ProductBomInspection();
				inspectionBom.setCompanyId(ContextUtils.getCompanyId());
				inspectionBom.setCreatedTime(new Date());
				inspectionBom.setCreator(ContextUtils.getUserName());
				inspectionBom.setModifiedTime(new Date());
				inspectionBom.setModifier(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					CommonUtil1.setProperty(inspectionBom,key, objMap.get(key));
				}
				ProductBomInspection bom=this.getProductBomInspection(inspectionBom.getMaterielCode());
				if(bom==null&&inspectionBom.getMaterielCode()!=null){
					inspectionBomDao.save(inspectionBom);
					sb.append("第" + (i+1) + "行保存成功!<br/>");
					i++;
				}else{
					bom.setLastModifiedTime(new Date());
					bom.setLastModifier(ContextUtils.getUserName());
					bom.setMaterielName(inspectionBom.getMaterielName());
					bom.setMaterialType(inspectionBom.getMaterialType());
					sb.append("第" + (i+1) + "行更新成功!<br/>");
					i++;
				}				
			} catch (Exception e) {
				e.printStackTrace();
				sb.append("第" + (i+1) + "行导入失败:<font color=red>" + e.getMessage() + "</font><br/>");
				i++;
			}
		}
		file.delete();
		return sb.toString();
	}	
}
