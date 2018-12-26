/*package com.ambition.util.erp.schedule;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.dao.ProcedureDefectionDefinitionDao;
import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.ProcedureDefectionDefinition;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.supplier.entity.Supplier;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
//import com.norteksoft.acs.service.organization.AcsService;
import com.norteksoft.product.util.ContextUtils;

*//**
 * 类名:导入初始化数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-12-26 发布
 *//*
@Service
@Transactional
public class ImportFromHisDatas {
	@Autowired
	private ProductBomDao productBomDao;
	
//	@Autowired
//	private BusinessUnitManager businessUnitManager;
	
//	@Autowired
//	private AcsService acsService;
	
	@Autowired
	private ProcedureDefectionDefinitionDao procedureDefectionDefinitionDao;
	
	*//**
	  * 方法名: 导入物料(成品)
	  * <p>功能说明：</p>
	 *//*
	public void importProductBom(){
		String hql = "delete from ProductBom where companyId = ?";
		Query query = productBomDao.getSession().createQuery(hql);
		query.setParameter(0, ScheduleJob.getScheduleCompanyId());
		query.executeUpdate();
		
		String sql = "select cpdm as code,cpmc as name,cpxh as model,jldw as units from a_1_cpxx";
		query = productBomDao.getSession().createSQLQuery(sql);
		List<?> list = query.list();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||"".equals(objs[0].toString())||objs[1]==null||"".equals(objs[1].toString())){
				continue;
			}
			ProductBom bom = new ProductBom();
			bom.setCompanyId(ScheduleJob.getScheduleCompanyId());
			bom.setCreatedTime(new Date());
			bom.setCreator(ScheduleJob.getScheduleUserName());
			bom.setCode(objs[0]+"");
			bom.setName(objs[1]+"");
//			bom.setModel(objs[2]+"");
//			bom.setUnits(objs[3]+"");
			bom.setLastModifiedTime(new Date());
			bom.setLastModifier(ScheduleJob.getScheduleUserName());
			productBomDao.save(bom);
		}
	}
	
	*//**
	  * 方法名:导入历史的 事业部档案
	  * <p>功能说明：</p>
	 *//*
	@SuppressWarnings("unused")
	public void importBusinessUnit(){
		String hql = "delete from BusinessUnit where companyId = ?";
		productBomDao.batchExecute(hql,ScheduleJob.getScheduleCompanyId());
		String sql = "select tjgs,tjgssm from a_3_tjgda";
		SQLQuery query = productBomDao.getSession().createSQLQuery(sql);
		List<?> list = query.list();
		for(Object obj : list){
//			Object[] objs = (Object[])obj;
//			businessUnitManager.saveBusinessUnit(objs[0]+"",objs[1]+"",ScheduleJob.getScheduleCompanyId());
		}
	}
	
	*//**
	  * 方法名: 导入供应商和经销商
	  * <p>功能说明：</p>
	 *//*
	@SuppressWarnings("unused")
	public void importSupplierAndCustomer(){
		String hql = "delete from Supplier where companyId = ?";
		Query query = productBomDao.getSession().createQuery(hql);
		query.setParameter(0, ScheduleJob.getScheduleCompanyId());
		query.executeUpdate();
		
		hql = "delete from Customer where companyId = ?";
		query = productBomDao.getSession().createQuery(hql);
		query.setParameter(0, ScheduleJob.getScheduleCompanyId());
		query.executeUpdate();
		//缓存基础信息
		Map<String,String> baseInfoMap = new HashMap<String, String>();
		String sql = "select khdm as code,khmc as name from a_2_khda";
		query = productBomDao.getSession().createSQLQuery(sql);
		List<?> list = query.list();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			baseInfoMap.put(objs[0]+"",objs[1]+"");
		}
		
		//查询供应商
		sql = "select khdm as supplier_code,tjgs as unit_code from a_2_khda_gyzt";
		query = productBomDao.getSession().createSQLQuery(sql);
		list = query.list();
		Map<String,String> supplierMap = new HashMap<String, String>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			String supplierCode = objs[0]+"";
			String unitCode = objs[1]+"";
			if(supplierMap.containsKey(supplierCode)){
				supplierMap.put(supplierCode,supplierMap.get(supplierCode) + "," + unitCode);
			}else{
				supplierMap.put(supplierCode,unitCode);
			}
		}
		
		//查询经销商
		sql = "select khdm as customer_code,tjgs as unit_code from a_2_khda_jxzt";
		query = productBomDao.getSession().createSQLQuery(sql);
		list = query.list();
		Map<String,String> customerMap = new HashMap<String, String>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			String customerCode = objs[0]+"";
			String unitCode = objs[1]+"";
			if(customerMap.containsKey(customerCode)){
				customerMap.put(customerCode,customerMap.get(customerCode) + "," + unitCode);
			}else{
				customerMap.put(customerCode,unitCode);
			}
		}
//		Map<String,BusinessUnit> unitMap = new HashMap<String, BusinessUnit>();
		//保存供应商和经销商
//		for(String supplierCode : baseInfoMap.keySet()){
		for(Map.Entry<String, String> entry:baseInfoMap.entrySet()){
			String supplierCode= entry.getKey();
			String supplierName = entry.getValue();
			String supplierUnitCodes = supplierMap.get(supplierCode);
			String customerUnitCodes = customerMap.get(supplierCode);
			//供应商
			if(supplierUnitCodes != null){
				String[] unitCodes = supplierUnitCodes.split(",");
				for(String unitCode : unitCodes){
					Supplier supplier = new Supplier();
					supplier.setCompanyId(ScheduleJob.getScheduleCompanyId());
					supplier.setCreatedTime(new Date());
					supplier.setCreator(ScheduleJob.getScheduleUserName());
//					supplier.setBusinessUnitCode(unitCode);
//					if(!unitMap.containsKey(unitCode)){
//						unitMap.put(unitCode, businessUnitManager.getBusinessUnitByCode(unitCode,ScheduleJob.getScheduleCompanyId()));
//					}
//					BusinessUnit unit = unitMap.get(unitCode);
//					if(unit != null){
//						supplier.setBusinessUnitName(unit.getBusinessUnitName());
//					}
					supplier.setCode(supplierCode);
					supplier.setName(supplierName);
					productBomDao.getSession().save(supplier);
				}
			}
			
			//经销商
			if(customerUnitCodes != null){
				String[] unitCodes = customerUnitCodes.split(",");
				for(String unitCode : unitCodes){
//					Customer customer =new Customer();
//					customer.setCompanyId(ScheduleJob.getScheduleCompanyId());
//					customer.setCreatedTime(new Date());
//					customer.setCreator(ScheduleJob.getScheduleUserName());
//					customer.setBusinessUnitCode(unitCode);
//					if(!unitMap.containsKey(unitCode)){
//						unitMap.put(unitCode, businessUnitManager.getBusinessUnitByCode(unitCode,ScheduleJob.getScheduleCompanyId()));
//					}
//					BusinessUnit unit = unitMap.get(unitCode);
//					if(unit != null){
//						customer.setBusinessUnitName(unit.getBusinessUnitName());
//					}
//					customer.setCode(supplierCode);
//					customer.setName(supplierName);
//					productBomDao.getSession().save(customer);
				}
			}
		}
	}
	
	*//**
	  * 方法名: 导入物料成本价
	  * <p>功能说明：</p>
	 *//*
	public void importCostPrice(){
		String hql = "delete from MaterialCostPrice";
		productBomDao.batchExecute(hql);
//		String sql = "select tjgs,tjgssm from a_3_tjgda";
//		SQLQuery query = productBomDao.getSession().createSQLQuery(sql);
//		List<?> list = query.list();
//		for(Object obj : list){
//			Object[] objs = (Object[])obj;
//			businessUnitManager.saveBusinessUnit(objs[0]+"",objs[1]+"",ContextUtils.getCompanyId());
//		}
	}
	
	*//**
	  * 方法名:递归获取父级部门 
	  * <p>功能说明：</p>
	  * @param code
	  * @return
	 *//*
	private Map<String,String> getParentByCode(String level,List<Map<String,String>> deptList){
		if(StringUtils.isEmpty(level)||level.length()==1){
			return null;
		}
		String parentLevel = level.substring(0,level.length()-1); 
		for(Map<String,String> map : deptList){
			if(parentLevel.equals(map.get("level"))){
				return map;
			}
		}
		return getParentByCode(parentLevel,deptList);
	}
	//导入部门
	public void deleteDepartments(){
		String sql = "select cjgx as code,bmmc as name,cjgx as level from a_3_bmda";
		Query query = productBomDao.getSession().createSQLQuery(sql);
		List<?> list = query.list();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null){
				continue;
			}
			String code = objs[0]+"";
//			acsService.deleteDepartment(code);
		}
	}
		
	//导入部门
	public void importDepartment(){
		List<Map<String,String>> deptList = new ArrayList<Map<String,String>>();
		//查询经销商
		String sql = "select bmbm as code,bmmc as name,cjgx as level from a_3_bmda";
		Query query = productBomDao.getSession().createSQLQuery(sql);
		List<?> list = query.list();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null){
				continue;
			}
			String code = objs[0]+"";
			String name = objs[1]+"";
			String level = objs[2]==null?"":objs[2].toString();
			Map<String,String> deptMap = new HashMap<String, String>();
			deptMap.put("code",code);
			deptMap.put("name",name);
			deptMap.put("level",level);
			deptList.add(deptMap);
			Map<String,String> parentMap = getParentByCode(level,deptList);
//			acsService.saveDepartment(parentMap==null?null:parentMap.get("code"),code,name);
		}
	}
	
	//导入用户
	@SuppressWarnings("unused")
	public void importUserInfos(){
		String sql = "select ygbm as code,ygxm as name,oamm as password,bmbm as dept,yxdz as email,tel as phone from a_3_ygda";
		Query query = productBomDao.getSession().createSQLQuery(sql);
		List<?> list = query.list();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[0]==null||objs[1]==null){
				continue;
			}
			String code = (objs[0]+"").trim();
			String name = (objs[1]+"").trim();
			String password = objs[2]==null?"abc":objs[2].toString();
			String dept = objs[3]==null?"":objs[3].toString().trim();
			String mail = objs[4]==null?"":objs[4].toString().trim();
			String tel = objs[5] == null?"":objs[5].toString().trim();
			try {
//				acsService.saveUser(code,name, password,dept,mail,tel,null);
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void importBadRecord() throws InvalidFormatException, FileNotFoundException, IOException{
		Workbook book = WorkbookFactory.create(new FileInputStream("C:/Users/zhaojun/Desktop/各工序不良代码表1.xls"));
		int totalSheets = book.getNumberOfSheets();
		for(int k=0;k<totalSheets;k++){
			Sheet sheet = book.getSheetAt(k);
			String[] strs = sheet.getSheetName().split("\\+");
			//生产线+工序
			String productLine = strs[0],workProcedure = strs[1];
			
			//检查相同的父级名称下重复
			Iterator<Row> rows = sheet.rowIterator();
			rows.next();//标题行
			int rowIndex = 1;
			while(rows.hasNext()){
				Row row = rows.next();
				String badCode = ExcelUtil.getCellValue(row.getCell(1))+"";
				if(StringUtils.isEmpty(badCode)){
					continue;
				}
//				String badName = ExcelUtil.getCellValue(row.getCell(2))+"";
				String hql = "from DefectionCode d where d.defectionCodeNo = ?";
				List<DefectionCode> defectionCodes = productBomDao.createQuery(hql,badCode).list();
				if(defectionCodes.isEmpty()){
					throw new AmbFrameException("sheet["+sheet.getSheetName()+"]中不良代码["+badCode+"]不存在!");
				}
				DefectionCode defectionCode = defectionCodes.get(0);
				//判断是否存在
				hql = "from ProcedureDefectionDefinition  p where p.productionLine = ? and p.workProcedure = ? and p.defectiveItemCode = ?";
				List<ProcedureDefectionDefinition> list = productBomDao.createQuery(hql,productLine,workProcedure,defectionCode.getDefectionCodeNo()).list();
				if(!list.isEmpty()){
					ProcedureDefectionDefinition pd = list.get(0);
//					pd.setOrderNum(rowIndex);
					procedureDefectionDefinitionDao.save(pd);
				}else{
					ProcedureDefectionDefinition pd = new ProcedureDefectionDefinition();
					pd.setCompanyId(ScheduleJob.getScheduleCompanyId());
					pd.setCreatedTime(new Date());
					pd.setCreator(ContextUtils.getUserName());
					pd.setProductionLine(productLine);
					pd.setWorkProcedure(workProcedure);
					pd.setDefectiveItemCode(badCode);
					pd.setDefectiveItem(defectionCode.getDefectionCodeName());
//					pd.setDefectionTypeNo(defectionCode.getDefectionType()==null?"":defectionCode.getDefectionType().getDefectionTypeNo());
//					pd.setDefectionTypeName(defectionCode.getDefectionType()==null?"":defectionCode.getDefectionType().getDefectionTypeName());
//					pd.setOrderNum(rowIndex);
					procedureDefectionDefinitionDao.save(pd);
				}
				
				rowIndex += 4;
			}
		}
//		System.out.println("aa");
	}
	
	public static void main(String[] args) {
		String code = "12";
		String parentCode = code.substring(0,code.length()-1);
		System.out.println(parentCode);
	}
}
*/