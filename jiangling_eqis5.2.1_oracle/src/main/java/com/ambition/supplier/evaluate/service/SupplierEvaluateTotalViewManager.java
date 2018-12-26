package com.ambition.supplier.evaluate.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.EscColumnToBean;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.baseInfo.dao.SupplierLevelScoreDao;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierEvaluateTotalView;
import com.ambition.supplier.entity.SupplierLevelScore;
import com.ambition.supplier.evaluate.dao.EvaluateDao;
import com.ambition.supplier.evaluate.dao.SupplierEvaluateTotalViewDao;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
@Transactional
public class SupplierEvaluateTotalViewManager {
	@Autowired
	private EvaluateDao evaluateDao;
	@Autowired
	private SupplierDao supplierDao;
	@Autowired
	private SupplierLevelScoreDao supplierLevelScoreDao;
	@Autowired
	private SupplierEvaluateTotalViewDao supplierEvaluateTotalViewDao;
	private static final Map<String,String> fieldMap = new HashMap<String, String>();
	 public SupplierEvaluateTotalViewManager(){
	    	fieldMap.put("allName","all_name");
	    	fieldMap.put("supplierName","supplier_name");
	    	fieldMap.put("supplierId","supplier_id");
	    	fieldMap.put("materialType","material_type");
	    	fieldMap.put("evaluateYear","evaluate_year");
	    	fieldMap.put("evaluateMonth","evaluate_month");
	    	fieldMap.put("quality","quality");
	    	fieldMap.put("develop","develop");
	    	fieldMap.put("purche","purche");
	    	fieldMap.put("console","console");
	    	fieldMap.put("supplierCode","supplier_code");
	    	fieldMap.put("grade","grate");
	    	fieldMap.put("total","total");
	    }
	public Page<SupplierEvaluateTotalView> getViewPage(
			Page<SupplierEvaluateTotalView> page) {
		Session session = evaluateDao.getSession();
        String selectFields = "select * ";
        String sql = " FROM supplier_evaluate_total_view f where 1=1 ";
        List<Object> searchParams = new ArrayList<Object>();
        String searchParameters = Struts2Utils.getParameter("searchParameters");
//        if(StringUtils.isEmpty(searchParameters)){
//            return page;
//        }
        if(StringUtils.isNotEmpty(searchParameters)){
            JSONArray array = JSONArray.fromObject(searchParameters);
            for(int i=0;i<array.size();i++){
                JSONObject json = array.getJSONObject(i);
                String propName = json.getString("propName");
                if(!fieldMap.containsKey(propName)){
                    throw new AmbFrameException("不包含查询条件["+propName+"]");
                }
                Object propValue = json.getString("propValue");
                String optSign = json.getString("optSign");
                String dataType = json.getString("dataType");
                if("DATE".equals(dataType)){
                    propValue = DateUtil.parseDate(propValue+"","yyyy-MM-dd HH:mm:ss");
                }
                if("like".equals(optSign)){
                    sql += " and f." + fieldMap.get(propName) + " like ?";
                    searchParams.add("%" + propValue + "%");
                }else if(">=".equals(optSign)){
                    sql += " and f." + fieldMap.get(propName) + " >= ?";
                    searchParams.add(propValue);
                }else if("<=".equals(optSign)){
                    sql += " and f." + fieldMap.get(propName) + " <= ?";
                    searchParams.add(propValue);
                }else{
                    sql += " and f." + fieldMap.get(propName) + " = ?";
                    searchParams.add(propValue);
                }
            }
        }
        //总计
        SQLQuery query = session.createSQLQuery("select count(*) " + sql);
        for(int i=0;i<searchParams.size();i++){
            query.setParameter(i,searchParams.get(i));
        }
        int totalCount = Integer.valueOf(query.list().get(0).toString());
        page.setTotalCount(totalCount);
        //分页查询
        query = session.createSQLQuery(selectFields + sql);
        for(int i=0;i<searchParams.size();i++){
            query.setParameter(i,searchParams.get(i));
        }
        query.setFirstResult((page.getPageNo()-1)*page.getPageSize());
        query.setMaxResults(page.getPageSize());
        query.setResultTransformer(new EscColumnToBean(SupplierEvaluateTotalView.class));
        @SuppressWarnings("unchecked")
        List<SupplierEvaluateTotalView> results = query.list();
        long i=1l;
        for(SupplierEvaluateTotalView record : results){
            record.setId(i++);
        }
        page.setResult(results);
        return page;
	}
	public void rendHistoryDatas() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		Integer evaluateYear = calendar.get(Calendar.YEAR);
		Session session = evaluateDao.getSession();
		String sqlDel = "delete from SUPPLIER_EVALUATE_TOTAL_VIEW where creator='"+ ContextUtils.getLoginName()+"'";
		/*if(evaluateYear!=null){
			sqlDel+=" and evaluate_year='"+evaluateYear+"'";
		}*/
		session.createSQLQuery(sqlDel)
			.executeUpdate();
		String sql = "select t.supplier_Id||t.supplier_Name||t.material_Type||t.evaluate_year||t.evaluate_Month, t.supplier_Id,t.SUPPLIER_NAME,t.material_Type,t.evaluate_year,t.evaluate_Month,t.estimate_model_name,t.real_total_points from SUPPLIER_EVALUATE t";
		/*if(evaluateYear!=null){
			sql+=" where t.evaluate_year='"+evaluateYear+"'";
		}*/
		SQLQuery query = session.createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<Object> list = query.list();
		Map<String,List<Object>> map = new HashMap<String,List<Object>>();
		for(Object obj : list ){
			Object[] objs = (Object[])obj;
			if(!map.containsKey(objs[0].toString())){
				List<Object> objArray = new ArrayList<Object>();
				objArray.add(objs);
				map.put(objs[0].toString(), objArray);
			}else{
				map.get(objs[0].toString()).add(objs);
			}
		}
		for(String key : map.keySet()){
			List<Object> objArray = map.get(key);
			SupplierEvaluateTotalView totalView = null;
			Double total = 0.0;
			for(int i=0;i<objArray.size();i++){
				Object[] objs = (Object[])objArray.get(i);
				if(i==0){
					totalView = new SupplierEvaluateTotalView();
					totalView.setCreatedTime(new Date());
					totalView.setCompanyId(ContextUtils.getCompanyId());
					totalView.setCreator(ContextUtils.getLoginName());
					totalView.setCreatorName(ContextUtils.getUserName());
					totalView.setAllName(objs[0].toString());
					totalView.setSupplierId(Long.valueOf(objs[1].toString()));
					try{
						Supplier su = supplierDao.get(Long.valueOf(objs[1].toString()));
						totalView.setSupplierCode(su.getCode());
					}catch(Exception e){
						e.printStackTrace();
						totalView.setSupplierCode("已删除");
					}
					totalView.setSupplierName(objs[2].toString());
					totalView.setMaterialType(objs[3].toString());
					totalView.setEvaluateYear(Integer.valueOf(objs[4].toString()));
					totalView.setEvaluateMonth(Integer.valueOf(objs[5].toString()));
				}
				if("质量".equals(objs[6].toString())){
					totalView.setQuality(objs[7]==null?0.0:Double.valueOf(objs[7].toString()));
					total += objs[7]==null?0.0:Double.valueOf(objs[7].toString());
				}
				if("研发".equals(objs[6].toString())){
					totalView.setDevelop(objs[7]==null?0.0:Double.valueOf(objs[7].toString()));
					total += objs[7]==null?0.0:Double.valueOf(objs[7].toString());
				}
				if("采购".equals(objs[6].toString())){
					totalView.setPurche(objs[7]==null?0.0:Double.valueOf(objs[7].toString()));
					total += objs[7]==null?0.0:Double.valueOf(objs[7].toString());
				}
				if("物控调达".equals(objs[6].toString())){
					totalView.setConsole(objs[7]==null?0.0:Double.valueOf(objs[7].toString()));
					total += objs[7]==null?0.0:Double.valueOf(objs[7].toString());
				}
				totalView.setTotal(total);
				//判断供应商级别
				String hqlBase = " from SupplierLevelScore s where s.companyId=? and s.scoreStart<=? and s.scoreEnd>=?";
				List<SupplierLevelScore> scores =  supplierLevelScoreDao.find(hqlBase,ContextUtils.getCompanyId(),total,total);
				String grade = "";
				if(scores.size()!=0){
					SupplierLevelScore score = scores.get(0);
					grade = score.getAuditLevel();
				}
				totalView.setGrade(grade);
			}
			session.save(totalView);
		}
	}
	public Page<SupplierEvaluateTotalView> getPage(
			Page<SupplierEvaluateTotalView> page) {
		// TODO Auto-generated method stub
		String hql = " from SupplierEvaluateTotalView t where t.creator=?";
		return supplierEvaluateTotalViewDao.searchPageByHql(page, hql,ContextUtils.getLoginName());
	}
	public List<SupplierEvaluateTotalView> getResults() {
		// TODO Auto-generated method stub
		String hql = " from SupplierEvaluateTotalView t where 1=1 ";
		  List<Object> searchParams = new ArrayList<Object>();
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		 if(StringUtils.isNotEmpty(searchParameters)){
	            JSONArray array = JSONArray.fromObject(searchParameters);
	            for(int i=0;i<array.size();i++){
	                JSONObject json = array.getJSONObject(i);
	                String propName = json.getString("propName");
	                if(!fieldMap.containsKey(propName)){
	                    throw new AmbFrameException("不包含查询条件["+propName+"]");
	                }
	                Object propValue = json.getString("propValue");
	                String optSign = json.getString("optSign");
	                String dataType = json.getString("dataType");
	                
	                if("like".equals(optSign)){
	                    hql += " and t." + propName + " like '%"+propValue+"%'";
	                }else{
	                	hql += " and t." + propName + " = ?";
	                	if("evaluateYear".equals(propName)||"evaluateMonth".equals(propName)){
	                		searchParams.add(Integer.valueOf(propValue.toString()));
	                	}else {
	                		searchParams.add(propValue);
	                	}
	                    
	                }
	            }
	        }
		 hql += " and t.creator=?";
		 searchParams.add(ContextUtils.getLoginName());
		return supplierEvaluateTotalViewDao.find(hql,searchParams.toArray());
	}
}
