package com.ambition.supplier.estimate.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.EvaluatingGradeRule;
import com.ambition.supplier.estimate.dao.EvaluatingGradeRuleDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.ContextUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 类名:自动评分规则业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：自动评分规则增加,修改,删除</p>
 * @author  赵骏
 * @version 1.00 2013-4-20 发布
 */
@Service
@Transactional
public class EvaluatingGradeRuleManager {
	@Autowired
	private EvaluatingGradeRuleDao gradeRuleDao;
	
	/**
	  * 方法名: 查询评分规则
	  * <p>功能说明：根据评价指标获取对应的所有评分规则</p>
	  * @param indicatorId
	  * @return
	 */
	public List<EvaluatingGradeRule> listAll(Long indicatorId){
		return gradeRuleDao.find("from EvaluatingGradeRule g where g.companyId = ? and g.evaluatingIndicator.id = ?",ContextUtils.getCompanyId(),indicatorId);
	}
	
	/**
	 * 检查是否存在相同的区间
	 * @param id
	 * @param name
	 * @return
	 */
	private void isExistGradeRule(EvaluatingGradeRule gradeRule){
		String hql = "select count(*) from EvaluatingGradeRule e where e.evaluatingIndicator = ? and e.level = ?";
		List<?> list = null;
		if(gradeRule.getId() == null){
			list = gradeRuleDao.find(hql,gradeRule.getEvaluatingIndicator(),gradeRule.getLevel());
		}else{
			list = gradeRuleDao.find(hql + " and e.id <> ?",gradeRule.getEvaluatingIndicator(),gradeRule.getLevel(),gradeRule.getId());
		}
		if(Integer.valueOf(list.get(0).toString())>0){
			throw new AmbFrameException("已经存在相同的级别【"+gradeRule.getLevel()+"】!");
		}
		hql = "from EvaluatingGradeRule e where e.evaluatingIndicator = ? and " +
				"(nvl(e.start,-65535) <= ? and ? < nvl(e.toend,65535) or nvl(e.start,-65535) < ? and ? < nvl(e.toend,65535))";
		List<Object> params = new ArrayList<Object>();
		params.add(gradeRule.getEvaluatingIndicator());
		Double start = gradeRule.getStart()==null?Double.MIN_VALUE:gradeRule.getStart();
		Double end = gradeRule.getToend()==null?Double.MAX_VALUE:gradeRule.getToend();
		params.add(start);
		params.add(start);
		params.add(end);
		params.add(end);
		if(gradeRule.getId() != null){
			hql += " and e.id <> ?";
			params.add(gradeRule.getId());
		}
		list = gradeRuleDao.find(hql,params.toArray());
		if(list.size()>0){
			if(gradeRule.getStart() == null){
				throw new AmbFrameException("已经存在<【"+gradeRule.getToend()+"】的规则!");
			}else if(gradeRule.getToend() == null){
				throw new AmbFrameException("已经存在>=【"+gradeRule.getStart()+"】的规则!");
			}else{
				throw new AmbFrameException("已经存在>=【"+gradeRule.getStart()+"】并且"+"<【"+gradeRule.getToend()+"】的规则!");
			}
		}
	}
	/**
	  * 方法名:根据ID获取评分规则 
	  * @param id
	  * @return
	 */
	public EvaluatingGradeRule getGradeRule(Long id){
		return gradeRuleDao.get(id);
	}
	
	/**
	  * 方法名:保存评分规则 
	  * <p>功能说明：判断必须的条件,检查是否存在相同的评分区间</p>
	  * @param gradeRule
	 */
	public void saveGradeRule(EvaluatingGradeRule gradeRule){
		if(gradeRule.getStart() == null && gradeRule.getToend() == null){
			throw new AmbFrameException("开始和结束不能同时为空!");
		}else if(gradeRule.getStart() != null && gradeRule.getToend() != null
				&& gradeRule.getStart()>gradeRule.getToend()){
			throw new AmbFrameException("范围结束不能大于开始!");
		}else if(gradeRule.getFee() == null){
			throw new AmbFrameException("得分不能为空!");
		}
		isExistGradeRule(gradeRule);
		gradeRuleDao.save(gradeRule);
	}
	
	/**
	  * 方法名:删除评分规则 
	  * <p>功能说明：删除自动评分规则</p>
	  * @param deleteIds
	 */
	public void deleteGradeRule(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			EvaluatingGradeRule gradeRule = gradeRuleDao.get(Long.valueOf(id));
			if(gradeRule.getId() != null){
				gradeRuleDao.delete(gradeRule);
			}
		}
	}
	
	/**
	  * 方法名:获取评分规则 
	  * <p>功能说明：根据指标ID和得分获取得分</p>
	  * @return
	 */
	public EvaluatingGradeRule getGradeRuleByEvaluateIndicatiorAndFee(Long indicatiorId,Double fee){
		String hql = "from EvaluatingGradeRule r where r.evaluatingIndicator.id = ? and (r.start<= ? and r.toend > ?) or (r.start is null and r.toend > fee)" +
				" or (r.start<= ? and r.toend is null)";
		List<EvaluatingGradeRule> gradeRules = gradeRuleDao.find(hql,indicatiorId,fee,fee,fee,fee);
		if(gradeRules.isEmpty()){
			return null;
		}else{
			return gradeRules.get(0);
		}
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
}
