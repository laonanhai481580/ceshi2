package com.ambition.supplier.audit.services;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.ambition.supplier.audit.dao.SupplierYearCheckDao;
import com.ambition.supplier.entity.SupplierYearCheckView;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:供应商年度稽核视图
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月27日 发布
 */
@Service
@Transactional
public class SupplierYearCheckViewManager {
	@Autowired
	private SupplierYearCheckDao supplierYearCheckDao;
	private static final Map<String,String> fieldMap = new HashMap<String, String>();
    public SupplierYearCheckViewManager(){
    	fieldMap.put("year","year");
    	fieldMap.put("name","name");
    	fieldMap.put("code","code");
    	fieldMap.put("supplyFactory","supply_factory");
    	fieldMap.put("supplyMaterial","supply_material");
    	fieldMap.put("firstCheckDate","first_check_date");
    	fieldMap.put("secondCheckDate","second_check_date");
    	fieldMap.put("finalCheckResult","final_check_result");
    	fieldMap.put("checker","checker");
    }
	public Page<SupplierYearCheckView> getSupplierYearCheckViewPage(Page<SupplierYearCheckView> page) {
		Session session = supplierYearCheckDao.getSession();
        String selectFields = "select * ";
        String auditYear = Struts2Utils.getParameter("auditYear");
        String sql = " FROM SUPPLIER_YEAR_CHECK_VIEW f where 1=1 and year = ?";
        List<Object> searchParams = new ArrayList<Object>();
        String searchParameters = Struts2Utils.getParameter("searchParameters");
        searchParams.add(Integer.valueOf(auditYear));
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
        query.setResultTransformer(new EscColumnToBean(SupplierYearCheckView.class));
        @SuppressWarnings("unchecked")
        List<SupplierYearCheckView> results = query.list();
        long i=1l;
        for(SupplierYearCheckView record : results){
            record.setId(i++);
        }
        page.setResult(results);
        return page;
	}
}
