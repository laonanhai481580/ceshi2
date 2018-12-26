package com.ambition.cost.loadinfo.services;

import java.util.ArrayList;
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

import com.ambition.cost.entity.QisCostView;
import com.ambition.cost.statistical.dao.CostRecordDao;
import com.ambition.product.EscColumnToBean;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
@Transactional
public class QisCostViewManager {
	@Autowired
	private CostRecordDao costRecordDao;
	private static final Map<String,String> fieldMap = new HashMap<String, String>();
    public QisCostViewManager(){
    	fieldMap.put("formNo","form_no");
    	fieldMap.put("occurringMonthStr","occurring_month_str");
    	fieldMap.put("occurringMonth","occurring_month");
    	fieldMap.put("levelTwoCode","level_two_code");
    	fieldMap.put("levelTwoName","level_two_name");
    	fieldMap.put("levelThreeCode","level_three_code");
    	fieldMap.put("levelThreeName","level_three_name");
    	fieldMap.put("code","code");
    	fieldMap.put("name","name");
    	fieldMap.put("dutyDepart","duty_depart");
    	fieldMap.put("value","value");
    	fieldMap.put("sourceType","source_type");
    	fieldMap.put("feeState","fee_state");
    	fieldMap.put("itemGroup","item_group");
    	fieldMap.put("customerName","customer_name");
    	fieldMap.put("project","project");
    	fieldMap.put("companyName","company_name");
    }
	public Page<QisCostView> getCostPage(Page<QisCostView> page) {
		Session session = costRecordDao.getSession();
        String selectFields = "select * ";
        String sql = " FROM QIS_VIEW_COST f where 1=1";
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
        query.setResultTransformer(new EscColumnToBean(QisCostView.class));
        @SuppressWarnings("unchecked")
        List<QisCostView> results = query.list();
        long i=1l;
        for(QisCostView record : results){
            record.setId(i++);
        }
        page.setResult(results);
        return page;
	}
	public Page<QisCostView> getCostPageByParams(Page<QisCostView> page, JSONObject params) {
		Session session = costRecordDao.getSession();
        String selectFields = "select * ";
        String sql = " FROM QIS_VIEW_COST f where 1=1 and occurring_month between ? and ? ";
        Integer startMonth = Integer.valueOf(params.getString("startDate").replaceAll("-",""));
		Integer endMonth = Integer.valueOf(params.getString("endDate").replaceAll("-",""));
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startMonth);
		searchParams.add(endMonth);
        String levelTwoName = null,name=null,itemGroup=null,customerName=null,project=null,dutyDepart=null,feeState=null,companyName=null;
		if(params.containsKey("levelTwoName")){
			levelTwoName = params.getString("levelTwoName");
			if(StringUtils.isNotEmpty(levelTwoName)){
				sql += " and level_two_name = ?";
				searchParams.add( levelTwoName);
			}
		}
		if(params.containsKey("name")){
			name = params.getString("name");
			if(StringUtils.isNotEmpty(name)){
				sql += " and name = ?";
				searchParams.add( name );
			}
		}
		if(params.containsKey("dutyDepart")){
			dutyDepart = params.getString("dutyDepart");
			if(StringUtils.isNotEmpty(dutyDepart)){
				sql += " and duty_Depart = ?";
				searchParams.add(dutyDepart);
			}
		}
		if(params.containsKey("feeState")){
			feeState = params.getString("feeState");
			if(StringUtils.isNotEmpty(feeState)){
				sql += " and fee_State = ?";
				searchParams.add(feeState);
			}
		}
		if(params.containsKey("itemGroup")){
			itemGroup = params.getString("name");
			if(StringUtils.isNotEmpty(itemGroup)){
				sql += " and item_Group = ?";
				searchParams.add(itemGroup);
			}
		}
		if(params.containsKey("customerName")){
			customerName = params.getString("customerName");
			if(StringUtils.isNotEmpty(customerName)){
				sql += " and customer_Name = ?";
				searchParams.add(customerName);
			}
		}
		if(params.containsKey("project")){
			project = params.getString("project");
			if(StringUtils.isNotEmpty(project)){
				sql += " and project = ?";
				searchParams.add(project);
			}
		}
		if(params.containsKey("companyName")){
			companyName = params.getString("companyName");
			if(StringUtils.isNotEmpty(companyName)){
				sql += " and company_Name = ?";
				searchParams.add(companyName);
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
        query.setResultTransformer(new EscColumnToBean(QisCostView.class));
        @SuppressWarnings("unchecked")
        List<QisCostView> results = query.list();
        long i=1l;
        for(QisCostView record : results){
            record.setId(i++);
        }
        page.setResult(results);
        return page;
	}
	
}
