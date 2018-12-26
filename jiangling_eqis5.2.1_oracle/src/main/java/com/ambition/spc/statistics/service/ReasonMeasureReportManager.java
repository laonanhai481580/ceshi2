package com.ambition.spc.statistics.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.entity.ReasonMeasure;
import com.ambition.spc.statistics.dao.ReasonMeasureReportDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ReasonMeasureReportManager.java
 * @authorBy YUKE
 *
 */
@Service
public class ReasonMeasureReportManager {
	@Autowired
	private ReasonMeasureReportDao reasonMeasureReportDao;
	
	public Page<ReasonMeasure> getPage(Page<ReasonMeasure> page) {
		return reasonMeasureReportDao.search(page);
	}
	
	@SuppressWarnings({"unchecked" })
	public Page<ReasonMeasure> searchByPage(Page<ReasonMeasure> page,JSONObject params){
		StringBuffer sb = new StringBuffer("from ReasonMeasure r where r.companyId = ? ");
		params = convertJsonObject(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		//查询条件
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params!=null){
			if(params.containsKey("startDate_ge_date") && params.containsKey("endDate_le_date")){
				if(params.getString("startDate_ge_date") != null && params.getString("endDate_le_date") != null){
					try {
						startDate = sdf.parse(params.getString("startDate_ge_date"));
						endDate = sdf.parse(params.getString("endDate_le_date"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				sb.append(" and r.occurDate between ? and ?");
				searchParams.add(startDate);
				searchParams.add(endDate);
			}
		}
		
		Query query = reasonMeasureReportDao.getSession().createQuery(sb.toString());
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		Page<ReasonMeasure> reasonMeasurePage = new Page<ReasonMeasure>();
		reasonMeasurePage.setPageNo(page.getPageNo());
		List<ReasonMeasure> list = query.list();
		if(list!=null && list.size()>0){
			reasonMeasurePage.setTotalCount(list.size());
		}
		List<ReasonMeasure> reasonMeasures = new ArrayList<ReasonMeasure>();
		int h=-1;
		int j = (page.getPageNo()-1)*page.getPageSize();
		for(ReasonMeasure obj : list){
			h++;
			if(h==j){
				j++;
			}else{
				continue;
			}
			if(j>page.getPageNo()*page.getPageSize()){
				continue;
			}
			reasonMeasures.add(obj);
		}
		
		reasonMeasurePage.setPageSize(page.getPageSize());
		reasonMeasurePage.setResult(new ArrayList<ReasonMeasure>());
		reasonMeasurePage.getResult().addAll(reasonMeasures);
		return reasonMeasurePage;
	}
	
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}else{
			for(Object key : params.keySet()){
				resultJson.put(key,params.getJSONArray(key.toString()).get(0));
			}
			return resultJson;
		}
	}
	
}
