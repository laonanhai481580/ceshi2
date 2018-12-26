package com.ambition.cost.statistical.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 内外部损失推移图DAO
 * 
 * @author zhongjianhang
 * 
 */
@SuppressWarnings("rawtypes")
@Repository
public class LossRateDao extends HibernateDao {
	// 获取内部损失中不合格处理报告单中的成本金额总计
	public void getInteriolLossTotal(Date startDate, Date endDate, String statas) {
	}

	// 根据发货单获取销售总额
	public void getGrossSalesTotal(Date startDate, Date endDate) {
		// String sqlString3 =
		// "select  SUM(msgr.amount_of_money)  from  mfg_send_goods_record  msgr";
		// String sqlString3 ="";
		// Double grossSalesTotal = (Double) this.getSession()
		// .createSQLQuery(sqlString3)
		// .addScalar("grossSalesTotal", Hibernate.DOUBLE).uniqueResult();
		// return grossSalesTotal;
	}

	// 根据维修单获取材料损失费用(外部损失费用)
	public void getRepairLossTotal(Date startDate, Date endDate) {
		// String sqlString4 =
		// "select SUM(mrrr.repair_cost_total) from mfg_repair_record_report mrrr";
		// String sqlString4="";
		// Double repairLossTotal =
		// (Double)this.getSession().createSQLQuery(sqlString4).addScalar("repairLossTotal",
		// Hibernate.DOUBLE).uniqueResult();
		// return repairLossTotal;

	}
	/*//获取内部损失明细
    @SuppressWarnings("unused")
	public String queryInteriorLossDetail(
			Page<DefectiveGoodsComposingItem> page, JSONObject params)
			throws ParseException {
		String myType = (String) params.get("myType");
		Date startDate = new Date();
		Date endDate = new Date();
		if (myType != null) {
			int startyear = Integer.valueOf(params.get("startDate_ge_date").toString().substring(0, 4));
			int startmonth = Integer.valueOf(params.get("startDate_ge_date").toString().substring(5, 7));	
			int  endyear = Integer.valueOf(params.get("endDate_le_date").toString().substring(0, 4));
	        int  endmonth = Integer.valueOf(params.get("endDate_le_date").toString().substring(5, 7));
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar startCal = Calendar.getInstance();
			System.out.println(startCal.toString());
			Calendar endCal = Calendar.getInstance();
			
			if ("month".equals(myType)) {
				int year = 0, month = 0;
				startCal.set(Calendar.YEAR, startyear);
				System.out.println(startmonth);
				startCal.set(Calendar.MONTH, startmonth-1);
				System.out.println(startmonth-1);
				startCal.set(Calendar.DATE, 1);
				startCal.set(Calendar.HOUR_OF_DAY, 0);
				startCal.set(Calendar.MINUTE, 0);
				startCal.set(Calendar.SECOND, 0);
				endCal.set(Calendar.YEAR, endyear);
				System.out.println(startmonth);
				endCal.set(Calendar.MONTH, endmonth);
				endCal.set(Calendar.DATE, 1);
				endCal.add(Calendar.DATE, -1);
				endCal.set(Calendar.HOUR_OF_DAY, 23);
				endCal.set(Calendar.MINUTE, 59);
				endCal.set(Calendar.SECOND, 59);
				startDate = startCal.getTime();
				endDate = endCal.getTime();
			}
		}
		String sql = "select company_id,form_number,code,name,department_name,occurring_date,value,form_type"
				+ ",form_id from QIS_VIEW_INNER_COST c where c.company_id = ? and c.occurring_date between ? and ?";
		Query query = this.getSession().createSQLQuery(sql);
		query.setParameter(0, ContextUtils.getCompanyId());
		System.out.println(startDate);
		System.out.println(endDate);
		query.setParameter(1, startDate);
		query.setParameter(2, endDate);
		int size = query.list().size();
		System.out.println(size);
	    page.setTotalCount(size);
		query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		List<?> list = query.list();
		JSONArray array = new JSONArray();
		int id = 1;
		for (Object obj : list) {
				Object[] objs = (Object[]) obj;
				String formNumber = (String) objs[1];
				System.out.println(formNumber);
				String code = (String) objs[2];
		        String name = (String) objs[3];
		      
	            String departName = (String) objs[4];
	            Date date = (Date) objs[5];
	            Double value = (Double) objs[6];
				String formType = (String) objs[7];
				System.out.println(formType);
				Long formId = Long.valueOf(objs[8].toString());
				JSONObject json = new JSONObject();
			    json.put("id", id++);
				json.put("formType", formType == null ? "" : formType);
			    json.put("formNumber", formNumber == null ? "" : formNumber);
			    json.put("code", code);
				json.put("name", name == null ? "" : name);
				json.put("departName", departName == null ? "" : departName);
				json.put("occurringDate", DateUtil.formateDateStr(date));
				json.put("value", value == null ? "" : value);
                json.put("formId", formId == null ? "" : formId);
				array.add(json);
			}
			JSONObject jsonPage = new JSONObject();
			jsonPage.put("records", page.getTotalCount());
			jsonPage.put("total", page.getTotalPages());
			jsonPage.put("page", page.getPageNo());
			jsonPage.put("rows", array);
			// page=
			// findPage(page,"from Cargoinfor cargoinfor where companyId=? and date>=? and date<? ",
			// ContextUtils.getCompanyId(),otherstartDate,otherendDate);
	
			return jsonPage.toString();
	}*/
}
