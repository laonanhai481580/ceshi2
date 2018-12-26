package com.ambition.carmfg.bom.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
@Transactional
public class ProductBomManager {
	@Autowired
	private ProductBomDao productBomDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	private static Map<String,Integer> structureKeyMap = new HashMap<String,Integer>();
	public static Map<String,Integer> getStructureKeyMap(){
		structureKeyMap.clear();
		String[] strs = PropUtils.getProp("product_structure").split(",");
		int i=0;
		for(String str : strs){
			if(StringUtils.isNotEmpty(str)){
				structureKeyMap.put(str,++i);
			}
		}
		return structureKeyMap;
	}
	
	/**
	 * 检查是否存在相同名称的物料BOM
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistProductBom(Long id,String code,String parentIds){
		String hql = "select count(*) from ProductBom p where p.companyId = ? and p.materielCode = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(code);
		if(parentIds != null){
			hql += " and p.parentIds like ?";
			params.add(parentIds + "%");
		}
		if(id != null){
			hql += " and p.id <> ?";
			params.add(id);
		}
		Query query = productBomDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	
	public ProductBom getProductBom(Long id){
		return productBomDao.get(id);
	}
	
	/**
	 * 更新所有的level和parentIds
	 */
	public void upateAllParentIds(){
		String hql = "from ProductBom p where p.bomParent is null and p.companyId = ?";
		List<ProductBom> productBoms = productBomDao.find(hql,ContextUtils.getCompanyId());
		for(ProductBom productBom : productBoms){
			productBom.setParentIds("," + productBom.getId() + ",");
			productBom.setMaterielLevel(1);
			productBomDao.save(productBom);
			updateChildrenLevelAndParentIds(productBom);
		}
	}
	
	private void updateChildrenLevelAndParentIds(ProductBom productBom){
		for(ProductBom child : productBom.getBomChildren()){
			child.setParentIds(productBom.getParentIds() + child.getId() + ",");
			child.setMaterielLevel(productBom.getMaterielLevel()+1);
			productBomDao.save(child);
			updateChildrenLevelAndParentIds(child);
		}
	}
	
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<ProductBom> search(Page<ProductBom> page,Long parentId){
		return productBomDao.search(page,parentId);
	}
	public Page<ProductBom> searchProduct(Page<ProductBom> page,Long parentId,String type){
		return productBomDao.searchProduct(page,parentId,type);
	}
	public Page<ProductBom> searchByParams(Page<ProductBom> page,String materielName,String materielCode){
		return productBomDao.searchByParams(page,materielName,materielCode);
	}	
	public Page<ProductBom> searchByParams(Page<ProductBom> page){
		return productBomDao.searchByParams(page);
	}
	public Page<ProductBom> searchProductByParams(Page<ProductBom> page,String type){
		return productBomDao.searchProductByParams(page,type);
	}
	public Page<ProductBom> searchModelByParams(Page<ProductBom> page){
		return productBomDao.searchModelByParams(page);
	}
	
	public Page<ProductBom> searchModel(Page<ProductBom> page,Long parentId){
		return productBomDao.searchModel(page,parentId);
	}
	
	private void updateProductBomParentIds(ProductBom productBom){
		if(productBom.getBomParent()!=null&&productBom.getBomParent().getParentIds()==null){
			updateProductBomParentIds(productBom.getBomParent());
		}
		if(productBom.getBomParent() != null){
			productBom.setParentIds(productBom.getBomParent().getParentIds() + productBom.getId() + ",");
		}else{
			productBom.setParentIds("," + productBom.getId() + ",");
		}
		productBomDao.save(productBom);
	}
	
	/**
	 * 保存物料BOM
	 * @param productBom
	 */
	public void saveProductBom(ProductBom productBom){
		if(StringUtils.isEmpty(productBom.getMaterielCode())){
			throw new RuntimeException("BOM代号不能为空!");
		}
		if(StringUtils.isEmpty(productBom.getMaterielName())){
			throw new RuntimeException("BOM名称不能为空!");
		}
		Long id = productBom.getId();
		ProductBom modelSpection = null;
		int modelSpectionLevel = getStructureKeyMap().get("产品型号");
		if(productBom.getMaterielLevel()>modelSpectionLevel && productBom.getBomParent() != null){
			modelSpection = getProductBomParentByLevel(productBom.getBomParent(),modelSpectionLevel);
			if(modelSpection.getParentIds()==null){
				updateProductBomParentIds(modelSpection);
			}
		}
		if(isExistProductBom(productBom.getId(),productBom.getMaterielCode(),modelSpection==null?null:modelSpection.getParentIds())){
			throw new RuntimeException("已经相同的代号!");
		}
		productBomDao.save(productBom);
		//更新parentIds
		if(id == null){
			if(productBom.getBomParent()==null){
				productBom.setParentIds("," + productBom.getId() + ",");
			}else{
				productBom.setParentIds(productBom.getBomParent().getParentIds() + productBom.getId() + ",");
			}
			productBomDao.save(productBom);
		}
	}
	
	/**
	 * 删除物料BOM
	 * @param id
	 */
	public void deleteProductBom(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			ProductBom productBom = productBomDao.get(Long.valueOf(id));
			if(productBom.getId() != null){
				if(!productBom.getBomChildren().isEmpty()){
					throw new RuntimeException(productBom.getMaterielName() + "还有子节点不能删除，请先删除子节点!");
				}
				logUtilDao.debugLog("删除", productBom.toString());
				productBomDao.delete(productBom);
			}
		}
	}
	
	/**
	 * 移动物料BOM
	 * @param moveIds
	 * @param parentId
	 */
	public void moveProductBoms(String moveIds,Long parentId){
		ProductBom parent = null;
		if(parentId != null){
			parent = productBomDao.get(parentId);
		}
		String parentIdStr = parentId==null?"":parentId.toString();
		for(String id : moveIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				ProductBom productBom = productBomDao.get(Long.valueOf(id));
				String hisParentIdStr = productBom.getBomParent() == null?"":productBom.getBomParent().getId().toString();
				if(!parentIdStr.equals(hisParentIdStr)){
					productBom.setBomParent(parent);
					if(parent==null){
						productBom.setMaterielLevel(1);
						productBom.setParentIds("," + productBom.getId() + ",");
					}else{
						productBom.setMaterielLevel(parent.getMaterielLevel()+1);
						productBom.setParentIds(parent.getParentIds() + productBom.getId() + ",");
					}
					productBomDao.save(productBom);
					//更新paerntIds和level
					updateProductBomParentIds(productBom);
				}
			}
		}
	}
	
	/**
	 * 根据产品结构查询物料BOM的顶级
	 * @param productStructureId
	 * @return
	 */
	public List<ProductBom> getProductBomParentsByStructure(Long productStructureId,JSONObject params){
		return productBomDao.getProductBomParentsByStructure(productStructureId,params);
	}
	/**
	 * 根据产品结构查询物料BOM
	 * @return
	 */
	public List<ProductBom> searchProductBoms(JSONObject params){
		return productBomDao.searchProductBoms(params);
	}
	
	/**
	 * 根据产品结构查询物料BOM
	 * @return
	 */
	public List<ProductBom> searchMaterials(JSONObject params){
		return productBomDao.searchMaterials(params);
	}
	/**
	 * 根据父级名称查询物料BOM
	 * @return
	 */
	public List<ProductBom> searchProductBomByParent(Long parentId,JSONObject params){
		return productBomDao.searchProductBomByParent(parentId, params);
	}
	
	/**
	 * 根据产品型号和物料名称获取物料BOM
	 * @param productStructureId
	 * @param bomName
	 * @return
	 */
	public ProductBom getProductBomByStructureAndBomName(Long productStructureId,String bomName){
		String hql = "from ProductBom p where p.productStructure.id = ? and p.materielName = ? and p.companyId=?";
		List<ProductBom> list = productBomDao.find(hql,new Object[]{productStructureId,bomName,ContextUtils.getCompanyId()});
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	/**
	 * 根据物料代码获取物料BOM
	 * @param bomName
	 * @return
	 */
	public ProductBom getProductBomByBomCode(String bomCode){
		String hql = "from ProductBom p where p.materielCode = ? and p.companyId = ?";
		List<ProductBom> list = productBomDao.find(hql,new Object[]{bomCode,ContextUtils.getCompanyId()});
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	private ProductBom getProductBomParentByLevel(ProductBom bom,int level){
		if(bom.getMaterielLevel()==level){
			return bom;
		}else if(bom.getBomParent()==null){
			return null;
		}else{
			return getProductBomParentByLevel(bom.getBomParent(),level);
		}
	}
	
	/**
	 * 根据产品代码获取产品型号
	 * @param bomCode
	 * @return
	 */
	public ProductBom getModelSpecificationByBomCode(String bomCode){
		String hql = "from ProductBom p where p.materielCode = ? and p.companyId = ?";
		List<ProductBom> list = productBomDao.find(hql,new Object[]{bomCode,ContextUtils.getCompanyId()});
		if(list.isEmpty()){
			return null;
		}else{
			ProductBom bom = list.get(0);
			int level = getStructureKeyMap().get("产品型号");
			if(level > bom.getMaterielLevel()){
				return null;
			}else{
				return getProductBomParentByLevel(bom,level);
			}
		}
	}
	/**
	 * 根据产品代码获取所有产品型号
	 * @param bomCode
	 * @return
	 */
	public List<ProductBom> getModelSpecificationsByBomCode(String bomCode){
		List<ProductBom> result = new ArrayList<ProductBom>();
		String hql = "from ProductBom p where p.materielCode = ? and p.companyId = ?";
		List<ProductBom> list = productBomDao.find(hql,new Object[]{bomCode,ContextUtils.getCompanyId()});
		if(list.isEmpty()){
			return result;
		}else{
			int level = getStructureKeyMap().get("产品型号");
			for(ProductBom bom : list){
				if(level <= bom.getMaterielLevel()){
					result.add(getProductBomParentByLevel(bom,level));
				}
			}
			return result;
		}
	}
	
	/**
	 * 根据产品型号获取产品类型
	 * @param modelSpecification
	 * @return
	 */
	public String getTypeByModel(String modelSpecification){
		String result = null;
		String hql = "from ProductBom p where p.materielName = ? and p.companyId = ?";
		List<ProductBom> list = productBomDao.find(hql,new Object[]{modelSpecification,ContextUtils.getCompanyId()});
		if(!list.isEmpty() && list.get(0) != null){
			//产品类型
			if(list.get(0).getBomParent()!=null){
				if(list.get(0).getBomParent().getBomParent()!=null){
					result = list.get(0).getBomParent().getBomParent().getMaterielName();
				}
			}
		}
		return result;
	}
	
	/**
	 * 根据产品型号获取产品系列
	 * @param modelSpecification
	 * @return
	 */
	public String getSerialByModel(String modelSpecification){
		String result = null;
		String hql = "from ProductBom p where p.materielName = ? and p.companyId = ?";
		List<ProductBom> list = productBomDao.find(hql,new Object[]{modelSpecification,ContextUtils.getCompanyId()});
		if(!list.isEmpty() && list.get(0) != null){
			//产品系列
			if(list.get(0).getBomParent()!=null){
				result = list.get(0).getBomParent().getMaterielName();
			}
		}
		return result;
	}
	
	/**
	 * 查询产品类型
	 * @return
	 */
	public Map<String,Object> getProductModels(){
//		Integer level = getStructureKeyMap().get("产品类型");
		return new HashMap<String, Object>();
//		List<ProductBom> productStructures = getProductStructureByLevel(level);
//		return convertProductStructures(productStructures);
	}
	
	/**
	 * 根据等级查询产品结构
	 * @param level
	 * @return
	 */
	public List<ProductBom> getProductStructureByLevel(Integer level){
		String hql = "from ProductBom p where p.companyId = ? and p.materielLevel = ? order by p.orderNum";
		return productBomDao.find(hql,ContextUtils.getCompanyId(),level);
	}
	
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<ProductBom> getProductStructures(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and p.bomParent is null");
		}else{
			sb.append(" and p.bomParent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by p.orderNum");
		return productBomDao.find(sb.toString(),params.toArray());
	}
	/**
	 * 根据父类查询Bom
	 * @param parentId
	 * @return
	 */
	public List<ProductBom> getProductBoms(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from ProductBom p where p.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and p.bomParent is null");
		}else{
			sb.append(" and p.bomParent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by p.hasChild desc,p.orderNum asc");
		return productBomDao.find(sb.toString(),params.toArray());
	}
	/**
	 * 查询产品类型
	 * @return
	 */
	public List<Option> getProductModelToOptions(){
		Integer level = getStructureKeyMap().get("产品类型");
		List<ProductBom> productStructures = getProductStructureByLevel(level);
		return convertProductStructureToList(productStructures);
	}
	
	/**
	 * 查询产品系列
	 * @return
	 */
	public Map<String,Object> getProductSeries(){
		Integer level = getStructureKeyMap().get("产品系列");
		List<ProductBom> productStructures = getProductStructureByLevel(level);
		return convertProductStructures(productStructures);
	}
	
	/**
	 * 查询产品系列
	 * @return
	 */
	public List<Option> getProductSerieToOptions(){
		Integer level = getStructureKeyMap().get("产品系列");
		List<ProductBom> productStructures = getProductStructureByLevel(level);
		return convertProductStructureToList(productStructures);
	}
	
	/**
	 * 查询产品型号
	 * @return
	 */
	public Map<String,Object> getModelSpecifications(){
		Integer level = getStructureKeyMap().get("产品型号");
		List<ProductBom> productStructures = getProductStructureByLevel(level);
		return convertProductStructures(productStructures);
	}
	
	/**
	 * 查询产品型号
	 * @return
	 */
	public List<Option> getModelSpecificationToOptions(){
		Integer level = getStructureKeyMap().get("产品型号");
		List<ProductBom> productStructures = getProductStructureByLevel(level);
		return convertProductStructureToList(productStructures);
	}
	
	/**
	 * 根据物料名称查询产品型号
	 * @return
	 */
	public List<ProductBom> getModelSpecificationByBomName(String bomName){
		String hql = "from ProductBom p where p.materielName = ? and p.companyId =?";
		return productBomDao.find(hql,new Object[]{bomName,ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据物料名称查询产品型号
	 * @return
	 */
	public List<Option> getModelSpecificationByBomNameToOptions(String bomName){
		return convertProductStructureToList(getModelSpecificationByBomName(bomName));
	}
	
	/**
	 * 转换产品结构
	 * @param productStructures
	 * @return
	 */
	private Map<String,Object> convertProductStructures(List<ProductBom> productStructures){
		Map<String,Object> map = new HashMap<String, Object>();
		for(ProductBom productStructure : productStructures){
			map.put(productStructure.getMaterielName(),productStructure.getMaterielName());
		}
		return map;
	}
	
	/**
	 * 转换产品结构To Options
	 * @param productStructures
	 * @return
	 */
	private List<Option> convertProductStructureToList(List<ProductBom> productStructures){
		List<Option> options = new ArrayList<Option>();
		for(ProductBom productStructure : productStructures){
			Option option = new Option();
			String name = productStructure.getMaterielName();
			option.setName(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
			option.setValue(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
			options.add(option);
		}
		return options;
	}
	
	public List<Option> getImportanceOptions(){
		List<Option> options = new ArrayList<Option>();
		Option option = new Option();
		option.setName(ProductBom.IMPORTANCE_A);
		option.setValue(ProductBom.IMPORTANCE_A);
		options.add(option);
		
		option = new Option();
		option.setName(ProductBom.IMPORTANCE_B);
		option.setValue(ProductBom.IMPORTANCE_B);
		options.add(option);

		option = new Option();
		option.setName(ProductBom.IMPORTANCE_C);
		option.setValue(ProductBom.IMPORTANCE_C);
		options.add(option);

		option = new Option();
		option.setName(ProductBom.IMPORTANCE_D);
		option.setValue(ProductBom.IMPORTANCE_D);
		options.add(option);
		return options;
	}
	/**
	 * 导入产品BOM
	 * @param file
	 * @param parent
	 * @param productStructure
	 * @throws Exception
	 */
	public String importBom(File file,ProductBom parent) throws Exception{
		StringBuffer sb = new StringBuffer("");
		Map<String,String> fieldMap = getFieldMap();
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
			String url = Struts2Utils.getRequest().getContextPath();
			throw new RuntimeException("资料格式不正确!&nbsp;&nbsp;<br/><a href='"+url+"/carmfg/base-info/bom/download-template.htm'>下载物料BOM资料模板>></a>");
		}
		DecimalFormat df = new DecimalFormat("#.##############");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 1;
		while(rows.hasNext()){
			row = rows.next();
			ProductBom productBom = new ProductBom();
			productBom.setCreatedTime(new Date());
			productBom.setCompanyId(ContextUtils.getCompanyId());
			productBom.setCreator(ContextUtils.getUserName());
			productBom.setLastModifiedTime(new Date());
			productBom.setLastModifier(ContextUtils.getUserName());
			if(parent != null){
				productBom.setBomParent(parent);
				productBom.setMaterielLevel(parent.getMaterielLevel() + 1);
			}
			for(String columnName : columnMap.keySet()){
				Cell cell = row.getCell(columnMap.get(columnName));
				if(cell != null){
					if(Cell.CELL_TYPE_STRING == cell.getCellType()){
						setProperty(productBom, fieldMap.get(columnName),cell.getStringCellValue());
					}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
						setProperty(productBom, fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
					}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
						setProperty(productBom, fieldMap.get(columnName),cell.getCellFormula());
					}
				}
			}
			try {
				saveProductBom(productBom);
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
	 * 导出
	 * @param productStructure
	 * @param parent
	 * @throws Exception
	 */
	public void exports(ProductBom parent) throws Exception {
		Workbook wb = new HSSFWorkbook();
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setColor(IndexedColors.RED.getIndex());
		font.setBoldweight((short) 700);
		style.setFont(font);
		style.setAlignment((short) 2);
		style.setVerticalAlignment((short) 0);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern((short) 1);
		style.setBorderTop((short) 1);
		style.setTopBorderColor(IndexedColors.BLUE.getIndex());
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		Sheet sheet = wb.createSheet("sheet1");
		Row row = sheet.createRow(0);
		Map<String,String> fieldMap = getFieldMap();
		Map<Integer,String> columnMap = new HashMap<Integer, String>();
		int index=0;
		for(String name : fieldMap.keySet()){
			Cell cell = row.createCell(index);
			cell.setCellStyle(style);
			cell.setCellValue(name);
			columnMap.put(index,fieldMap.get(name));
			index++;
		}
		
		String hql = "from ProductBom p where p.companyId=?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		if(parent != null){
			hql += " and p.bomParent = ?";
			params.add(parent);
		}else{
			 hql += " and p.bomParent is null";
		}
		List<ProductBom> bomList = productBomDao.find(hql,params.toArray());
		int rowIndex = 1;
		for(ProductBom bom : bomList){
			row = sheet.createRow(rowIndex++);
			for(Integer columnIndex : columnMap.keySet()){
				Cell cell = row.createCell(columnIndex);
				String key = columnMap.get(columnIndex);
				Object valObj = PropertyUtils.getProperty(bom,key);
				cell.setCellValue((valObj==null?"":valObj)+"");
			}
		}
		
		String fileName = (new StringBuilder(String.valueOf("物料Bom"))).append(
				".xls").toString();
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		wb.write(response.getOutputStream());
	}
	
	private Map<String,String> getFieldMap(){
		Map<String,String> fieldMap = new HashMap<String, String>();
		fieldMap.put("物料编码","materielCode");
		fieldMap.put("物料名称","materielName");
		fieldMap.put("单位","units");
		fieldMap.put("备注","remark");
		return fieldMap;
	}
	
	/**
	 * 创建资料模板
	 * @throws Exception 
	 */
	public void createTemplate() throws Exception{
		Workbook wb = new HSSFWorkbook();
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setColor(IndexedColors.RED.getIndex());
		font.setBoldweight((short) 700);
		style.setFont(font);
		style.setAlignment((short) 2);
		style.setVerticalAlignment((short) 0);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern((short) 1);
		style.setBorderTop((short) 1);
		style.setTopBorderColor(IndexedColors.BLUE.getIndex());
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		Sheet sheet = wb.createSheet("Sheet1");
		Row row = sheet.createRow(0);
		Cell cell = null;
		int colIndex = 0;
		Map<String,String> fieldMap = getFieldMap();
		for(String columnName : fieldMap.keySet()){
			cell = row.createCell(colIndex++);
			cell.setCellStyle(style);
			cell.setCellValue(columnName);
		}
		String fileName = "物料BOM资料模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		wb.write(response.getOutputStream());
	}
	
	private void setProperty(Object obj,String property,Object value) throws Exception{
		Class<?> type = PropertyUtils.getPropertyType(obj,property);
		if(type != null){
			if(value==null||StringUtils.isEmpty(value.toString())){
				PropertyUtils.setProperty(obj,property,null);
			}else{
				if(Date.class.getName().equals(type.getName())){
					
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
	
	/**
	  * 方法名: 保存物料(成品)
	  * <p>功能说明：</p>
	  * @param code
	  * @param name
	  * @param model
	  * @param unit
	  * @param companyId
	 */
	public void saveMaterial(String code,String name,String model,String unit,Long companyId){
		if(StringUtils.isEmpty(code)){
			throw new AmbFrameException("编码不能为空!");
		}
		if(StringUtils.isEmpty(name)){
			throw new AmbFrameException("名称不能为空!");
		}
//		if(StringUtils.isEmpty(model)){
//			throw new AmbFrameException("型号不能为空!");
//		}
		String hql = "from ProductBom b where b.companyId = ? and b.materielCode = ?";
		List<ProductBom> boms = productBomDao.find(hql,companyId,code);
		ProductBom bom = null;
		if(boms.isEmpty()){
			bom = new ProductBom();
			bom.setCompanyId(companyId);
			bom.setCreatedTime(new Date());
			bom.setCreator(ScheduleJob.getScheduleUserName());
			bom.setMaterielCode(code);
		}else{
			bom = boms.get(0);
		}
		bom.setMaterielName(name);
		bom.setMaterielModel(model);
		bom.setUnits(unit);
		bom.setLastModifiedTime(new Date());
		bom.setLastModifier(ScheduleJob.getScheduleUserName());
		productBomDao.save(bom);
	}
	
	/**
	  * 方法名: 删除物料(成品)
	  * <p>功能说明：</p>
	  * @param code
	  * @param name
	  * @param model
	  * @param unit
	  * @param companyId
	 */
	public void deleteMaterial(String code,Long companyId){
		if(StringUtils.isEmpty(code)){
			throw new AmbFrameException("编码不能为空!");
		}
		String hql = "delete from ProductBom b where b.companyId = ? and b.materielCode = ?";
		productBomDao.createQuery(hql,companyId,code).executeUpdate();
	}
	
	public ProductBom getProductBomPrice(String productCode){
	    return productBomDao.getProductBomPrice(productCode);
	}

	public Map<String,String> selectFromMes(String process, JdbcTemplate jdbcTemplate) {
		// TODO Auto-generated method stub
		Long stageId = Long.valueOf(process);
		String productLine = "";
		String processName = "";
		Map<String,String> map = new HashMap<String,String>();
		if(stageId!=null){
			String sql2 = "select distinct(l.pdline_name) from sys_terminal t,sys_pdline l where t.pdline_id = l.pdline_id and t.stage_id = ? and t.enabled='Y'";
			SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet(sql2 ,new Object[]{stageId});
			while(rowSet2.next()){
				if(productLine.length()==0){
					productLine = rowSet2.getString("pdline_name");
				}else{
					productLine += "," + rowSet2.getString("pdline_name");
				}
			}
			map.put("productLine", productLine);
			String sql3 = "select distinct( p.process_name) from sys_terminal t,sys_process p where t.process_id = p.process_id and t.stage_id=? and t.enabled='Y'";
			SqlRowSet rowSet3 = jdbcTemplate.queryForRowSet(sql3 ,new Object[]{stageId});
			while(rowSet3.next()){
				if(processName.length()==0){
					processName = rowSet3.getString("process_name");
				}else{
					processName += "," + rowSet3.getString("process_name");
				}
			}
			map.put("processName", processName);
		}
		
		return map;
	}

	public void saveUser(com.norteksoft.acs.entity.organization.User user) {
		// TODO Auto-generated method stub
		productBomDao.getSession().save(user);
	}
}
