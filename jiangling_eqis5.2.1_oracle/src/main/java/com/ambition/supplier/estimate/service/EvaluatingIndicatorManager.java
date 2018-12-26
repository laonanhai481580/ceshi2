package com.ambition.supplier.estimate.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.EvaluatingIndicator;
import com.ambition.supplier.estimate.dao.EvaluatingIndicatorDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

@Service
@Transactional
public class EvaluatingIndicatorManager {
	@Autowired
	private EvaluatingIndicatorDao evaluatingIndicatorDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	/**
	 * 国际化查询
	 * @param option
	 */
	public void i18nChange(List<com.norteksoft.product.api.entity.Option> option){
		String language = "中文";
		if("en".equals(Struts2Utils.getRequest().getLocale().getLanguage())){
			language = "英文";
		}
		String code="";
		for(com.norteksoft.product.api.entity.Option op: option){
			code = code + "'" + op.getName() + "',";
		}
		code = code.substring(0, code.length()-1);
		String sql = "select b.code,o.value from bs_internation b  inner join bs_internation_option o "
		  +" on b.id = o.fk_internation_id"
		  +" and b.code in (" + code +")"
		  +" and o.category_name = '" + language + "'";
		List<Object> list = evaluatingIndicatorDao.findBySql(sql);
		for (Object object : list) {
			Object[] objs = (Object[])object;
			for(com.norteksoft.product.api.entity.Option op:option){
				if(op.getName().equals(objs[0])){
					op.setName(objs[1].toString());
					break;
				}
			}
		}
	}
	
	/**
	 * 检查是否存在相同名称的指标
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistEvaluatingIndicator(Long id,String name,EvaluatingIndicator parent){
		String hql = "select count(*) from EvaluatingIndicator e where e.companyId = ? and e.name = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(name);
		if(id != null){
			hql += " and e.id <> ?";
			params.add(id);
		}
		if(parent == null){
			hql += " and e.parent is null";
		}else{
			hql += " and e.parent = ?";
			params.add(parent);
		}
		Query query = evaluatingIndicatorDao.getSession().createQuery(hql);
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
	public EvaluatingIndicator getEvaluatingIndicator(Long id){
		return evaluatingIndicatorDao.get(id);
	}
	
	public EvaluatingIndicator getEvaluatingIndicatorByModelIndicatorId(Long modelIndicatorId){
		String hql = "select m.evaluatingIndicator from ModelIndicator m where m.id = ?";
		List<EvaluatingIndicator> evaluatingIndicators = evaluatingIndicatorDao.find(hql,modelIndicatorId);
		if(evaluatingIndicators.isEmpty()){
			return null;
		}else{
			return evaluatingIndicators.get(0);
		}
	}
	/**
	 * 保存指标
	 * @param evaluatingIndicator
	 */
	public void saveEvaluatingIndicator(EvaluatingIndicator evaluatingIndicator){
		if(StringUtils.isEmpty(evaluatingIndicator.getName())){
			throw new RuntimeException("指标名称不能为空");
		}
		if(isExistEvaluatingIndicator(evaluatingIndicator.getId(),evaluatingIndicator.getName(),evaluatingIndicator.getParent())){
			throw new RuntimeException("已在存相同的名称");
		}
		evaluatingIndicatorDao.save(evaluatingIndicator);
	}
	
	/**
	 * 删除评价指标
	 * @param id
	 */
	public void deleteEvaluatingIndicator(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			EvaluatingIndicator evaluatingIndicator = evaluatingIndicatorDao.get(Long.valueOf(id));
			String sql = "select * from SUPPLIER_EVALUATE_DETAIL where evaluating_Indicator_Id=?";
			@SuppressWarnings("unchecked")
			List<Object> lists = evaluatingIndicatorDao.getSession().createSQLQuery(sql).setParameter(0, Long.valueOf(id)).list();
			if(lists.size()>0){
				throw new AmbFrameException("该指标已有对应的评价数据，不能删除");
			}
			if(evaluatingIndicator.getId() != null){
//				if(!evaluatingIndicator.getChildren().isEmpty()){
//					throw new RuntimeException("还有子节点不能删除，请先删除子节点!");
//				}
				logUtilDao.debugLog("删除", evaluatingIndicator.toString());
				evaluatingIndicatorDao.delete(evaluatingIndicator);
			}
		}
	}
	
	/**
	 * 获取评价指标顶级
	 * @return
	 */
	public List<EvaluatingIndicator> getTopEvaluatingIndicators(){
		return evaluatingIndicatorDao.getTopEvaluatingIndicators();
	}
	
	// 封装不良细项结果数据集的JSON格式
	public String getResultJson(Page<Object> page) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (Object o : page.getResult()) {
			HashMap<String, Object> hs = new HashMap<String, Object>();
			StringBuffer sb = new StringBuffer();
			sb.append(JsonParser.object2Json(o));
			sb.delete(sb.length() - 1, sb.length());
			sb.append(",");
			sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
			JSONObject jObject = JSONObject.fromObject(sb.toString());
			list.add(jObject);
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

	public EvaluatingIndicator selectBigOrderByNum() {
		// TODO Auto-generated method stub
		String hql = " from EvaluatingIndicator e order by e.orderByNum desc";
		List<EvaluatingIndicator> lists = evaluatingIndicatorDao.find(hql);
		if(lists.size()==0){
			return null;
		}else{
			return lists.get(0);
		}
	}
	
}
