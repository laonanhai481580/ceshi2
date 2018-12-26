package com.ambition.epm.entrustOrt.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entrustOrt.dao.EntrustOrtSublistDao;
import com.ambition.supplier.utils.DateUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class EntrustOrtSublistManager {
	@Autowired
	private EntrustOrtSublistDao entrustOrtSublistDao;
	public EntrustOrtSublist getEntrustOrtSublist(Long id){
		return entrustOrtSublistDao.get(id);
	}
	public List<EntrustOrtSublist> getByOrtId(Long id){
		return entrustOrtSublistDao.getByOrtId(id);
	}
	public void saveEntrustOrtSublist(EntrustOrtSublist entrustOrtSublist){
		entrustOrtSublistDao.save(entrustOrtSublist);
	}
	public Page<EntrustOrtSublist> list(Page<EntrustOrtSublist> page){
		return entrustOrtSublistDao.list(page);
	}
	
	public List<EntrustOrtSublist> listAll(){
		return entrustOrtSublistDao.getEntrustOrtSublist();
	}
	
	public void deleteEntrustOrtSublist(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
 			EntrustOrtSublist entrustOrtSublist=entrustOrtSublistDao.get(Long.valueOf(id));
			if(entrustOrtSublist.getId() != null){
				entrustOrtSublistDao.delete(entrustOrtSublist);
			}
		}
	}
	
	public Page<EntrustOrtSublist> search(Page<EntrustOrtSublist> page){
		return entrustOrtSublistDao.search(page);
	}
	public void alterEntrustOrtSublist(Long id){
		EntrustOrtSublist entrustOrtSublist =entrustOrtSublistDao.get(Long.valueOf(id));
		entrustOrtSublist.setTestAfter("OK");
		entrustOrtSublistDao.save(entrustOrtSublist);
	} 
	public Page<EntrustOrtSublist> listState(Page<EntrustOrtSublist> page,String state ,String str){
		String hql = " from EntrustOrtSublist e where e.hiddenState=? and e.companyId=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		searchParams.add(ContextUtils.getCompanyId());
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		return entrustOrtSublistDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public Map<String,Object> listStatistics(String state ,String str,JSONObject params){
		params = convertJsonObject(params);
		JSONObject resultJson = new JSONObject();
		Date startDate = DateUtil.parseDate(params.getString("startDate"));
		Date endDate = DateUtil.parseDate(params.getString("endDate"));
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Integer> data = new ArrayList<Integer>();
		//最大值
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "投入数");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		
		//最小值
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "不良数");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
		
		//平均值
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "无效数");
		List<Map<String,Object>> data3 = new ArrayList<Map<String,Object>>();	
		//查询数据
		String hql = " select e.testNumber,e.defectNumber,e.invalidNumber,e.id from EntrustOrtSublist e where e.hiddenState=?  and e.companyId=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		searchParams.add(ContextUtils.getCompanyId());
		if(startDate==null){
			hql=hql+" and createdTime between ? and ?";
			searchParams.add(startDate);
			searchParams.add(endDate);
		}
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		Query query=null; 
		try {
			query=entrustOrtSublistDao.getSession().createQuery(hql.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		List<Integer> categories = new ArrayList<Integer>();
		result.put("title", "可靠性统计图");
		result.put("categories", categories);
		result.put("yAxisTitle1","数<br/>据");
		result.put("max", 100);
		List<?> list = query.list();
		int i=0;
		for(Object obj : list){
			i++;
			categories.add(i);
			Object objs[] = (Object[])obj;
			Map<String,Object> map = new HashMap<String, Object>();
			//投入数
			String id=null;
			if(objs[3]!=null){
				id=objs[3].toString();
			}
			String testNumber=null;
			if(objs[0]!=null){
				testNumber=objs[0].toString();
			}
			map.put("name","投入数");
			map.put("y", testNumber);
			map.put("id", id);
			data1.add(map);
			
			//不良数
			if(objs[3]!=null){
				id=objs[3].toString();
			}
			String defectNumber=null;
			if(objs[1]!=null){
				defectNumber=objs[1].toString();
			}
			map=new HashMap<String, Object>();
			map.put("name","不良数");
			map.put("y", defectNumber);
			map.put("id", id);
			data2.add(map);
			
			//不良数
			if(objs[3]!=null){
				id=objs[3].toString();
			}
			String invalidNumber=null;
			if(objs[2]!=null){
				invalidNumber=objs[2].toString();
			}
			map=new HashMap<String, Object>();
			map.put("name","无效数");
			map.put("y", invalidNumber);
			map.put("id", id);
			data3.add(map);
			
			result.put("tableHeaderList", categories);
			result.put("firstColName","数据");
			result.put("yAxisTitle1","");
			result.put("yAxisTitle2","");
			
			series1.put("data",data1);
			result.put("series1", series1);
			
			series2.put("data",data2);
			result.put("series2", series2);
			
			series3.put("data",data3);
			result.put("series3", series3);
		}
		
		return result;
	}
	public List<EntrustOrtSublist>listOutOrIn(String subName){
		return entrustOrtSublistDao.listOutOrIn(subName);
	}
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
}
