package com.ambition.gp.averageMaterial.services;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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

import com.ambition.gp.averageMaterial.dao.GpAverageMaterialDao;
import com.ambition.gp.entity.GpAverageMaterial;
import com.ambition.gp.entity.GpSubstance;
import com.ambition.gsm.inspectionplan.dao.InspectionPlanDao;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

@Service
@Transactional
public class GpAverageMaterialManager {
	@Autowired
	private GpAverageMaterialDao gpAverageMaterialDao;
	@Autowired
	private InspectionPlanDao inspectionPlanDao;
	
	public GpAverageMaterial getGpAverageMaterial(Long id){
		return gpAverageMaterialDao.get(id);
	}
	public void saveGpAverageMaterial(GpAverageMaterial gpAverageMaterial){
		gpAverageMaterialDao.save(gpAverageMaterial);
	}
	
	public Page<GpAverageMaterial> list(Page<GpAverageMaterial> page){
		return gpAverageMaterialDao.list(page);
	}
	
	public List<GpAverageMaterial> listAll(){
		return gpAverageMaterialDao.getGpAverageMaterial();
	}
	
	
	public void deleteGpAverageMaterial(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
			GpAverageMaterial gpAverageMaterial=gpAverageMaterialDao.get(Long.valueOf(id));
			if(gpAverageMaterial.getId() != null){
				gpAverageMaterialDao.delete(gpAverageMaterial);
			}
		}
	}
	
	public Page<GpAverageMaterial> search(Page<GpAverageMaterial> page){
		return gpAverageMaterialDao.search(page);
	}
	public Page<GpAverageMaterial> listState(Page<GpAverageMaterial> page,String type,String code,String subName ,String factorySupply) {
		// TODO Auto-generated method stub
		String hql = " from GpAverageMaterial s where hiddenState='N' ";
		List<Object> searchParams = new ArrayList<Object>();
//		searchParams.add(state);
		if(type!=null ){
			if("0".equals(type)){
				type=GpAverageMaterial.STATE_SUBMIT;
			}
			if("1".equals(type)){
				type=GpAverageMaterial.STATE_PENDING;
			}
			if("2".equals(type)){
				type=GpAverageMaterial.STATE_QUALIFIED;
			}
			if("3".equals(type)){
				type=GpAverageMaterial.STATE_OVERDUE;
			}
			hql=hql+" and s.isHarmful=?";
			searchParams.add(type);
		}
		if(code!=null ){
			hql=hql+" and s.supplierCode=?";
			searchParams.add(code);
		}
		if(subName!=null ){
			hql=hql+" and s.factoryClassify=?";
			searchParams.add(subName);
		}
		if("南昌".equals(factorySupply)||"苏州".equals(factorySupply)||"深圳".equals(factorySupply)){
			hql=hql+" and s.factorySupply=?";
			searchParams.add(factorySupply);
		}
		return gpAverageMaterialDao.searchPageByHql(page, hql, searchParams.toArray());
		
	}
	public void harmful(String eid,String type){
		String[] ids = eid.split(",");
		for(String id : ids){
			GpAverageMaterial gpAverageMaterial = gpAverageMaterialDao.get(Long.valueOf(id));
			if("U".equals(type)){
				gpAverageMaterial.setIsHarmful(GpAverageMaterial.STATE_PENDING);
				gpAverageMaterial.setTaskProgress(GpAverageMaterial.STATE_PENDING);
			}else if("Y".equals(type)){
				gpAverageMaterial.setIsHarmful(GpAverageMaterial.STATE_QUALIFIED);
				gpAverageMaterial.setTaskProgress(GpAverageMaterial.STATE_QUALIFIED);
			}else if("N".equals(type)){
				gpAverageMaterial.setIsHarmful(GpAverageMaterial.STATE_SUBMIT);
				gpAverageMaterial.setTaskProgress(GpAverageMaterial.STATE_SUBMIT);
			}else if("O".equals(type)){
				gpAverageMaterial.setIsHarmful(GpAverageMaterial.STATE_OVERDUE);
				gpAverageMaterial.setTaskProgress(GpAverageMaterial.STATE_OVERDUE);
			}
			if(!"未更新".equals(gpAverageMaterial.getUpdateStatus())){
				gpAverageMaterial.setUpdateStatus("未更新");
			}
			gpAverageMaterialDao.save(gpAverageMaterial);
		}
	}
	public GpAverageMaterial saveEntity(GpAverageMaterial report,String zb){
		if(StringUtils.isNotEmpty(zb)){
			JSONArray childrenInfo = JSONArray.fromObject(zb);
			if(report.getGpSubstances()==null){
				report.setGpSubstances(new ArrayList<GpSubstance>());
			}else{
				report.getGpSubstances().clear();
			}
			for (int i = 0; i < childrenInfo.size(); i++) {
				JSONObject js = childrenInfo.getJSONObject(i);
				GpSubstance tr = new GpSubstance();
				tr.setCompanyId(ContextUtils.getCompanyId());
				tr.setCreatedTime(new Date());
				tr.setCreator(ContextUtils.getLoginName());
				tr.setCreatorName(ContextUtils.getUserName());
				tr.setModifiedTime(new Date());
				tr.setModifier(ContextUtils.getLoginName());
				tr.setModifierName(ContextUtils.getUserName());
				tr.setGpAverageMaterial(report);
				for(Object key : js.keySet()){
					if(key != null && js.get(key) != null){
						try{
							CommonUtil1.setProperty(tr, key.toString(), js.get(key));
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				report.getGpSubstances().add(tr);
			}
		}
		return report;
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("GP_AVERAGE_MATERIAL");
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
				GpAverageMaterial gpAverageMaterial = new GpAverageMaterial();
				gpAverageMaterial.setCompanyId(ContextUtils.getCompanyId());
				gpAverageMaterial.setCreatedTime(new Date());
				gpAverageMaterial.setCreator(ContextUtils.getUserName());
				gpAverageMaterial.setModifiedTime(new Date());
				gpAverageMaterial.setModifier(ContextUtils.getUserName());
				gpAverageMaterial.setHiddenState("N");
				gpAverageMaterial.setIsHarmful(GpAverageMaterial.STATE_SUBMIT);
				String supplierName = ContextUtils.getUserName();
				String supplierCode = ContextUtils.getLoginName();
				String supplierEmail = ContextUtils.getEmail();
				gpAverageMaterial.setSupplierName(supplierName);
				gpAverageMaterial.setSupplierCode(supplierCode);
				Date testDate = gpAverageMaterial.getTestReportDate();
				if(testDate!=null){
					gpAverageMaterial.setTestReportExpire(getTestReportDate(testDate));
				}
				if(gpAverageMaterial.getSupplierEmail()==null){
					gpAverageMaterial.setSupplierEmail(supplierEmail);
				}
				
				User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
				String subName=user.getSubCompanyName();
				gpAverageMaterial.setFactoryClassify(subName);
				for(String key : objMap.keySet()){
					CommonUtil1.setProperty(gpAverageMaterial,key, objMap.get(key));
				}
				if(findMaterial(gpAverageMaterial)){
					throw new RuntimeException("已有相同数据!");
				}
//				setUpdateStatus(gpAverageMaterial);
				gpAverageMaterialDao.save(gpAverageMaterial);
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
				if(column.getHeaderName().contains("$")){
					String headName=LocalizedTextUtil.findDefaultText(column.getHeaderName().replace("${","").replace("}",""), ActionContext.getContext().getLocale());
					if(headName!=null){
						fieldMap.put(headName, column.getTableColumn().getName());
					}
				}else{
					fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
				}
			}
		}
		return fieldMap;
	}
	@SuppressWarnings("unchecked")
	public Boolean findMaterial(GpAverageMaterial gpAverageMaterial){
		String supplierCode = ContextUtils.getLoginName();
		String name = gpAverageMaterial.getAverageMaterialName();
		if("".equals(name)||name==null){
			throw new RuntimeException("均质材料名称不能为空!");
		}
		String model = gpAverageMaterial.getAverageMaterialModel();
		if("".equals(model)||model==null){
			throw new RuntimeException("均质材料型号不能为空!");
		}
		String manufacturer = gpAverageMaterial.getManufacturer();
		if("".equals(manufacturer)||manufacturer==null){
			throw new RuntimeException("制造商不能为空!");
		}
		String hql = " from GpAverageMaterial s  where s.isHarmful in ( ?, ?) and s.supplierCode=? "
				+ "and s.averageMaterialName=? and s.averageMaterialModel=? and s.manufacturer=?";
		Query query = gpAverageMaterialDao.createQuery(hql)
				.setParameter(0, GpAverageMaterial.STATE_SUBMIT)
				.setParameter(1, GpAverageMaterial.STATE_PENDING)
				.setParameter(2, supplierCode)
				.setParameter(3, name)
				.setParameter(4, model)
				.setParameter(5, manufacturer);
		List<GpAverageMaterial> reports = query.list();
		if(reports.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	@SuppressWarnings("unchecked")
	public void setUpdateStatus(GpAverageMaterial gpAverageMaterial){
		String supplierCode = gpAverageMaterial.getSupplierCode().trim();//编码
		String name = gpAverageMaterial.getAverageMaterialName().trim();//名称
		String model = gpAverageMaterial.getAverageMaterialModel().trim();//型号
		String manufacturer = gpAverageMaterial.getManufacturer().trim();//制造商
		String hql = " from GpAverageMaterial s  where s.isHarmful in ( ?, ?) and s.updateStatus=? and s.supplierCode=? "
				+ "and s.averageMaterialName=? and s.averageMaterialModel=? and s.manufacturer=? and s.id <>? order by s.testReportExpire ASC";
		Query query = gpAverageMaterialDao.createQuery(hql)
				.setParameter(0, GpAverageMaterial.STATE_OVERDUE).setParameter(1, GpAverageMaterial.STATE_QUALIFIED)
				.setParameter(2, "未更新")
				.setParameter(3, supplierCode).setParameter(4, name)
				.setParameter(5, model).setParameter(6, manufacturer).setParameter(7, gpAverageMaterial.getId());
		
		List<GpAverageMaterial> reports = query.list();
		if(!reports.isEmpty()){
				GpAverageMaterial gpAverageMaterial1=reports.get(0);
				gpAverageMaterial1.setUpdateStatus("已更新");
				if(gpAverageMaterial1.getIsHarmful().equals(GpAverageMaterial.STATE_QUALIFIED)){
					gpAverageMaterial1.setIsHarmful(GpAverageMaterial.STATE_OVERDUE);
					gpAverageMaterial1.setTaskProgress(GpAverageMaterial.STATE_OVERDUE);
				}
				gpAverageMaterialDao.save(gpAverageMaterial1);
		}
		
	}
	public void isHarmfulDate(String eid){
		String[] ids = eid.split(",");
		for(String id : ids){
			GpAverageMaterial gpAverageMaterial = gpAverageMaterialDao.get(Long.valueOf(id));
			String text1 =gpAverageMaterial.getText1();
			String text2 =gpAverageMaterial.getText2();
			String text3 =gpAverageMaterial.getText3();
			String text4 =gpAverageMaterial.getText4();
			String text5 =gpAverageMaterial.getText5();
			String text6 =gpAverageMaterial.getText6();
			String text7 =gpAverageMaterial.getText7();
			String text8 =gpAverageMaterial.getText8();
			String text9 =gpAverageMaterial.getText9();
			String text10 =gpAverageMaterial.getText10();
			String text11 =gpAverageMaterial.getText11();
			String text12 =gpAverageMaterial.getText12();
			String text13 =gpAverageMaterial.getText13();
			String text14 =gpAverageMaterial.getText14();
			String text15 =gpAverageMaterial.getText15();
			String text16 =gpAverageMaterial.getText16();
			String text17 =gpAverageMaterial.getText17();
			String name = gpAverageMaterial.getAverageMaterialName();
			String model = gpAverageMaterial.getAverageMaterialModel();
			String manufacturer = gpAverageMaterial.getManufacturer();
			String testReportFile = gpAverageMaterial.getTestReportFile();
			String msdsFile = gpAverageMaterial.getMsdsFile();
			Date testReportDate = gpAverageMaterial.getTestReportDate();
			String testReportNo = gpAverageMaterial.getTestReportNo();
			String factorySupply=gpAverageMaterial.getFactorySupply();
			String companyName =PropUtils.getProp("companyName");
			if("".equals(text1)||text1==null){
				throw new RuntimeException("Cd不能为空!");
			}
			if("".equals(text2)||text2==null){
				throw new RuntimeException("Pb不能为空!");
			}
			if("".equals(text3)||text3==null){
				throw new RuntimeException("Hg不能为空!");
			}
			if("".equals(text4)||text4==null){
				throw new RuntimeException("Cr+6不能为空!");
			}
			if("".equals(text5)||text5==null){
				throw new RuntimeException("PBBs不能为空!");
			}
			if("".equals(text6)||text6==null){
				throw new RuntimeException("PBDEs不能为空!");
			}
			if("".equals(text7)||text7==null){
				throw new RuntimeException("DEHP不能为空!");
			}
			if("".equals(text8)||text8==null){
				throw new RuntimeException("BBP不能为空!");
			}
			if("".equals(text9)||text9==null){
				throw new RuntimeException("DBP不能为空!");
			}
			if("".equals(text10)||text10==null){
				throw new RuntimeException("DIBP不能为空!");
			}
			if("".equals(text11)||text11==null){
				throw new RuntimeException("Br不能为空!");
			}
			if("".equals(text12)||text12==null){
				throw new RuntimeException("Cl不能为空!");
			}
			if("".equals(text13)||text13==null){
				throw new RuntimeException("Be不能为空!");
			}
			if("".equals(text14)||text14==null){
				throw new RuntimeException("Be不能为空!");
			}
			if("".equals(text15)||text15==null){
				throw new RuntimeException("As不能为空!");
			}
			if("".equals(text16)||text16==null){
				throw new RuntimeException("PFOS不能为空!");
			}
			if("".equals(text17)||text17==null){
				throw new RuntimeException("PFOA不能为空!");
			}
			if("".equals(name)||name==null){
				throw new RuntimeException("均质材料名称不能为空!");
			}
			if("".equals(model)||model==null){
				throw new RuntimeException("均质材料型号不能为空!");
			}
			if("".equals(manufacturer)||manufacturer==null){
				throw new RuntimeException("制造商不能为空!");
			}
			if(testReportFile==null||"".equals(testReportFile)||testReportFile.indexOf("|~|")<0){
				throw new RuntimeException("测试报告不能为空!");
			}
			if("".equals(msdsFile)||msdsFile==null||msdsFile.indexOf("|~|")<0){
				throw new RuntimeException("MSDS不能为空!");
			}
			if("".equals(testReportDate)||testReportDate==null){
				throw new RuntimeException("测试报告日期不能为空!");
			}
			if("".equals(testReportNo)||testReportNo==null){
				throw new RuntimeException("测试报告编号不能为空!");
			}
			if(companyName!=null){
				if(factorySupply==null){
					if("TP".equals(companyName)){
						throw new RuntimeException("请填写供应厂区!");
					}
				}
			}
		}
	}
	public Date getTestReportDate(Date testReportDate){
		Calendar c = Calendar.getInstance();
		c.setTime(testReportDate);
		c.add(Calendar.DAY_OF_MONTH, 365);
		Date tomorrow = c.getTime();
		return tomorrow;
	}
}
