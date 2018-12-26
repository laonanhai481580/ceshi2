package com.ambition.supplier.manager.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.supplier.entity.Evaluate;
import com.ambition.supplier.entity.SupplierEvaluateState;
import com.ambition.supplier.entity.SupplierQcds;
import com.ambition.supplier.entity.WarnSign;
import com.ambition.supplier.evaluate.service.EvaluateManager;
import com.ambition.supplier.manager.dao.SupplierQcdsDao;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:供应商QCDS评价
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-6-18 发布
 */
@Service
@Transactional
public class SupplierQcdsManager {
	@Autowired
	private SupplierQcdsDao supplierQcdsDao;

	@Autowired
	private EvaluateManager evaluateManager;
	
	@Autowired
	private WarnSignManager warnSignManager;
	/**
	 * 方法名:供应商QCDS评价汇总 
	  * @param page
	  * @param evaluateYear
	  * @param evaluateMonth
	  * @return
	 */
	public Page<SupplierQcds> search(Page<SupplierQcds> page,Integer evaluateYear,Integer evaluateMonth){
		return supplierQcdsDao.search(page,evaluateYear,evaluateMonth);
	}
	/**
	 * 方法名:获取供应商QCDS 
	  * <p>功能说明：根据供应商编码,评价年份,评价月份获取QCDS</p>
	  * @param supplierCode
	  * @param evaluateYear
	  * @param evaluateMonth
	  * @return
	 */
	public SupplierQcds getQcdsBySupplierAndEvaluate(String supplierName,Integer evaluateYear,Integer evaluateMonth){
		String hql = "from SupplierQcds q where q.name = ? and q.evaluateYear = ? and q.evaluateMonth = ?";
		List<SupplierQcds> qcds = supplierQcdsDao.find(hql,supplierName,evaluateYear,evaluateMonth);
		if(qcds.isEmpty()){
			return null;
		}else{
			return qcds.get(0);
		}
	}
	
	
	public List<IncomingInspectionActionsReport> getIqcIiar(SupplierEvaluateState supplierEvaluateState,Integer evaluateYear,Integer evaluateMonth){
		Calendar cal= Calendar.getInstance();
		cal.set(Calendar.YEAR, evaluateYear);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, evaluateMonth);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date startDate=cal.getTime();
		cal.set(Calendar.MONTH, evaluateMonth);
		cal.set(Calendar.DATE,31);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date endDate=cal.getTime();
		String hql = "from IncomingInspectionActionsReport iiar where iiar.supplierCode=? and iiar.businessUnitCode=? and iiar.enterDate between ? and ? ";
		List<IncomingInspectionActionsReport> iiars = supplierQcdsDao.find(hql,supplierEvaluateState.getCode(),supplierEvaluateState.getBusinessUnitCode(),startDate,endDate);
		return iiars;
	}
	
	
	/**
	 * 方法名:获取供应商QCDS 
	  * <p>功能说明：根据供应商编码,评价年份,评价月份获取QCDS</p>
	  * @param supplierCode
	  * @param evaluateYear
	  * @param evaluateMonth
	  * @return
	 */
	public SupplierQcds getQcdsBySupplierAndEvaluate(String businessUnitCode,String supplierCode,Integer evaluateYear,Integer evaluateMonth){
		String hql = "from SupplierQcds q where q.businessUnitCode = ? and q.code = ? and q.evaluateYear = ? and q.evaluateMonth = ?";
		List<SupplierQcds> qcds = supplierQcdsDao.find(hql,businessUnitCode,supplierCode,evaluateYear,evaluateMonth);
		if(qcds.isEmpty()){
			return null;
		}else{
			return qcds.get(0);
		}
	}
	
	public void saveSupplierQcds(SupplierQcds qcds){
		supplierQcdsDao.save(qcds);
	}
	
	public void delete(SupplierQcds qcds){
		supplierQcdsDao.delete(qcds);
	}
	/**
	  * 方法名: 根据id获取qcds
	  * @param id
	  * @return
	 */
	public SupplierQcds getSupplierQcds(Long id){
		return supplierQcdsDao.get(id);
	}
	/**
	 * 方法名:计算供应商qcds数据 
	  * <p>功能说明：</p>
	 */
	public void updateSupplierQcds(){
		String hql = "from WarnSign w where w.companyId = ?";
		List<WarnSign> warnSigns = supplierQcdsDao.find(hql,ContextUtils.getCompanyId());
		hql = "from Evaluate e";
		List<Evaluate> evaluates = supplierQcdsDao.find(hql);
		Map<String,Boolean> nameMap = new HashMap<String, Boolean>();
		for(Evaluate evaluate : evaluates){
//			String name = evaluate.getSupplierName() + "_" + evaluate.getEvaluateYear() + "_" + evaluate.getEvaluateMonth();
//			if(nameMap.containsKey(name)){
//				continue;
//			}
//			evaluateManager.calculateQcds(evaluate,warnSigns);
		}
	}
	
	/**
	 * 方法名:计算供应商最近三个月的质量评分
	  * <p>功能说明：</p>
	 */
	public Map<String,Object> caculateLastThreeMonths(SupplierQcds qcds,List<WarnSign> warnSigns){
		List<WarnSign> selfWarnSigns = new ArrayList<WarnSign>();
		for(WarnSign warnSign : warnSigns){
			if(qcds.getBusinessUnitCode() != null && qcds.getBusinessUnitCode().equals(warnSign.getBusinessUnitCode())){
				selfWarnSigns.add(warnSign);
			}
		}
		Map<String,Object> gradeMap = new HashMap<String, Object>();
		int startYear = qcds.getEvaluateYear();
		int endYear = startYear;
		int startMonth = qcds.getEvaluateMonth();
		int endMonth = startMonth;
		//提前两个月
		startMonth -= 2;
		if(startMonth<1){
			startYear--;
			startMonth = 12 + startMonth;
		}
		String hql = "from SupplierQcds e where e.companyId = ? and e.code = ? and e.businessUnitCode = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(qcds.getCompanyId());
		searchParams.add(qcds.getCode());
		searchParams.add(qcds.getBusinessUnitCode());
		if(startYear != endYear){
			hql += " and (e.evaluateYear = ? and e.evaluateMonth >= ? or e.evaluateYear = ? and e.evaluateMonth <= ?)";
			searchParams.add(startYear);
			searchParams.add(startMonth);
			searchParams.add(endYear);
			searchParams.add(endMonth);
		}else{
			hql += " and e.evaluateYear = ? and e.evaluateMonth between ? and ?";
			searchParams.add(startYear);
			searchParams.add(startMonth);
			searchParams.add(endMonth);
		}
		for(int i=1;i<=6;i++){
			if(i == 1){
				hql += " and (e.evaluate"+i+"Name = ?";
			}else{
				hql += " or e.evaluate"+i+"Name = ?";
			}
			searchParams.add("质量");
		}
		hql += ")";
		List<SupplierQcds> list = supplierQcdsDao.find(hql,searchParams.toArray());
		for(SupplierQcds q : list){
			for(int i=1;i<=6;i++){
				try {
					Object evaluateName = PropertyUtils.getProperty(q,"evaluate"+i+"Name");
					if("质量".equals(evaluateName)){
						Object evaluateTotal = PropertyUtils.getProperty(q,"evaluate"+i+"Total");
						Object evaluateRealTotal = PropertyUtils.getProperty(q,"evaluate"+i+"RealTotal");
						if(evaluateTotal != null&&evaluateRealTotal!= null){
							double rate = q.getEvaluate1RealTotal()/q.getEvaluate1Total()*100.0;
//							Map<String,String> resultMap = evaluateManager.setEvaluateGrade(rate,selfWarnSigns);
//							gradeMap.put("grade_" + q.getEvaluateMonth(),resultMap.get("grade"));
						}
					}
				} catch (Exception e) {
					throw new AmbFrameException("取值失败!",e);
				}
			}
		}
//		if(total>0){
//			percentageTotal = realTotal/total*100.0;
//		}
//		Map<String,String> resultMap = evaluateManager.setEvaluateGrade(percentageTotal,warnSigns);
//		DecimalFormat df = new DecimalFormat("0.0");
//		gradeMap.put("realTotal",df.format(percentageTotal));
//		gradeMap.put("grade",resultMap.get("grade"));
		return gradeMap;
	}
	/**
	  * 方法名:转换列表为excel格式的文件流
	  * <p>功能说明：</p>
	  * @param qcdss
	  * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public void exportQcdsExcel(List<SupplierQcds> qcdss,int evaluateYear,int evaluateMonth) throws IOException{
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/supplier-qcds.xls");
			Workbook book = WorkbookFactory.create(inputStream);
			List<WarnSign> warnSigns = null;
			if(!qcdss.isEmpty()){
				warnSigns = warnSignManager.list();
			}
			Sheet sheet = book.getSheetAt(0);
			//表头
			Map<String,Integer> gradeIndexMap = new HashMap<String, Integer>();
			for(int i=0;i<3;i++){
				int month = evaluateMonth - i;
				if(month < 1){
					month = 12 + month;
				}
				gradeIndexMap.put("grade_" + month,4+i);
				if(i>0){
					sheet.getRow(1).getCell(4+i).setCellValue(month +  "月等级");
				}
			}
			int rowIndex = 2;
			Map<String,Double> nameMap = new HashMap<String, Double>();
			DecimalFormat df = new DecimalFormat("0.0");
			for(SupplierQcds qcds : qcdss){
				Row row = sheet.createRow(rowIndex);
				row.setHeightInPoints(24f);
				row.createCell(0).setCellValue(qcds.getBusinessUnitName());
				row.createCell(1).setCellValue(qcds.getName());
				row.createCell(2).setCellValue(qcds.getSupplyProducts());
				nameMap.clear();
				if(qcds.getEvaluate1RealTotal() != null){
					nameMap.put(qcds.getEvaluate1Name(),qcds.getEvaluate1RealTotal());
				}
				if(qcds.getEvaluate2RealTotal() != null){
					nameMap.put(qcds.getEvaluate2Name(),qcds.getEvaluate2RealTotal());
				}
				if(qcds.getEvaluate3RealTotal() != null){
					nameMap.put(qcds.getEvaluate3Name(),qcds.getEvaluate3RealTotal());
				}
				if(qcds.getEvaluate4RealTotal() != null){
					nameMap.put(qcds.getEvaluate4Name(),qcds.getEvaluate4RealTotal());
				}
				if(qcds.getEvaluate5RealTotal() != null){
					nameMap.put(qcds.getEvaluate5Name(),qcds.getEvaluate5RealTotal());
				}
				if(qcds.getEvaluate6RealTotal() != null){
					nameMap.put(qcds.getEvaluate6Name(),qcds.getEvaluate6RealTotal());
				}
				for(String name : nameMap.keySet()){
					if(name.indexOf("质量")>-1){
						row.createCell(3).setCellValue(nameMap.get(name));
					}else if(name.indexOf("供货")>-1){
						row.createCell(7).setCellValue(nameMap.get(name));
					}else if(name.indexOf("价格")>-1){
						row.createCell(8).setCellValue(nameMap.get(name));
					}else if(name.indexOf("售后服务")>-1){
						row.createCell(9).setCellValue(nameMap.get(name));
					}else if(name.indexOf("合作")>-1){
						row.createCell(10).setCellValue(nameMap.get(name));
					}
				}
				if(qcds.getPercentageTotal() != null){
					row.createCell(12).setCellValue(df.format(qcds.getPercentageTotal()));
				}
				if(qcds.getTotal() != null){
					row.createCell(11).setCellValue(df.format(qcds.getTotal()));
				}
				row.createCell(13).setCellValue(qcds.getGrade());
				Map<String,Object> lastThreeMonthGradeMap = caculateLastThreeMonths(qcds, warnSigns);
				for(String grade : gradeIndexMap.keySet()){
					if(lastThreeMonthGradeMap.containsKey(grade)){
						row.createCell(gradeIndexMap.get(grade)).setCellValue(lastThreeMonthGradeMap.get(grade)+"");
					}
				}
				rowIndex++;
			}
			String fileName = evaluateYear + "年" + evaluateMonth + "月供应商QCDS.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append("error.xls").append("\"")
					.toString());
			response.getOutputStream().write(("服务器错误:" + e.getMessage()).getBytes());
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
	}
	
	public Page<SupplierEvaluateState> querySupplierEvaluateState(Page<SupplierEvaluateState> page,Integer evaluateYear,Integer evaluateMonth){
		String selectFields = "select s.*,q.percentage_total,q.id as qcds_id,q.code as qcdsCode";
		String sql = " FROM supplier_supplier s left join supplier_qcds q on s.code = q.code and s.business_unit_code = q.business_unit_code and q.evaluate_year=? and q.evaluate_month=? where 1=1";
		List<Object> searchParams = new ArrayList<Object>();
		if(evaluateYear == null||evaluateMonth == null){
			Calendar calendar = Calendar.getInstance();
			evaluateYear=calendar.get(Calendar.YEAR);
			evaluateMonth = calendar.get(Calendar.MONTH)+1;
		}
		searchParams.add(evaluateYear);
		searchParams.add(evaluateMonth);
		
		Boolean hasBusinessUnit = false;
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		if(StringUtils.isNotEmpty(searchParameters)){
			JSONArray array = JSONArray.fromObject(searchParameters);
			for(Object obj : array){
				JSONObject json = (JSONObject)obj;
				String dbName = json.getString("dbName");
				String propValue = json.getString("propValue");
				String optSign = json.getString("optSign");
//				String dataType = json.getString("dataType");
				if("qcds_code".equals(dbName)){
					sql += " and q.id is ";
					if("1".equals(propValue)){
						sql += " not null";
					}else{
						sql += " null";
					}
				}else{
					if("business_unit_code".equals(dbName)){
						hasBusinessUnit = true;
					}
					sql += " and s." + dbName + " " + optSign + " ? ";
					if("like".equals(optSign)){
						searchParams.add("%" + propValue + "%");
					}else{
						searchParams.add(propValue);
					}
				}
			}
		}
    	//总计
		SQLQuery query = supplierQcdsDao.getSession().createSQLQuery("select count(*) " + sql);
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
        int totalCount = Integer.valueOf(query.list().get(0).toString());
        page.setTotalCount(totalCount);
        //分页查询
//        query = supplierQcdsDao.getSession().createSQLQuery(selectFields + sql);
//		for(int i=0;i<searchParams.size();i++){
//			query.setParameter(i,searchParams.get(i));
//		}
//        query.setFirstResult((page.getPageNo()-1)*page.getPageSize());
//        query.setMaxResults(page.getPageSize());
//        query.setResultTransformer(new EscColumnToBean(SupplierEvaluateState.class));
//        query.addScalar("code",Hibernate.STRING)
//        .addScalar("name",Hibernate.STRING)
//        .addScalar("model",Hibernate.STRING)
//        .addScalar("business_unit_code",Hibernate.STRING)
//        .addScalar("has_update",Hibernate.STRING)
//        .addScalar("is_leaf",Hibernate.STRING)
//        .addScalar("assembly_num",Hibernate.DOUBLE);
        @SuppressWarnings("unchecked")
		List<SupplierEvaluateState> results = query.list();
        page.setResult(results);
		return page;
	}
	
	
	  // 封装出货异常结果数据集的JSON格式
    public String getResultJson(Page<SupplierEvaluateState> page,Integer evaluateYear,Integer evaluateMonth) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (SupplierEvaluateState cp : page.getResult()) {
            JSONObject json = JSONObject.fromObject(JsonParser.object2Json(cp));
            List<IncomingInspectionActionsReport> li=getIqcIiar(cp,evaluateYear,evaluateMonth);
            if(!li.isEmpty()){
            	json.put("isNoChange", "是");
            }else{
            	json.put("isNoChange", "否");
            }
            list.add(json);
        }
        // 添加jqGrid所需的页信息
        StringBuilder json = new StringBuilder();
        json.append("{\"page\":\"");
        json.append(page.getPageNo());
        json.append("\",\"total\":");
        json.append(page.getTotalPages());
        json.append(",\"records\":\"");
        json.append(page.getTotalCount());
        json.append("\",\"rows\":");
        json.append(JSONArray.fromObject(list).toString());
        json.append("}");
        return json.toString();
    }  
	
}
