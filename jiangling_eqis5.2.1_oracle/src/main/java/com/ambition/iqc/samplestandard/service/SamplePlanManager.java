package com.ambition.iqc.samplestandard.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.AcceptanceQualityLimit;
import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.samplestandard.dao.SamplePlanDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * SamplePlanManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class SamplePlanManager {
	@Autowired
	private SamplePlanDao samplePlanDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public SampleScheme getSampleScheme(Long id){
		return samplePlanDao.get(id);
	}
	/**
	 * 根据字码和类型获取抽样方案
	 * @param codeLetter
	 * @param type
	 * @return
	 */
	public AcceptanceQualityLimit queryQualityLimit(String codeLetter,String aql,String type){
		String hql = "from AcceptanceQualityLimit a where a.type = ? and a.companyId = ? and a.aql = ? and a.code=?";
		List<AcceptanceQualityLimit> acceptanceQualityLimits = samplePlanDao.find(hql,type,ContextUtils.getCompanyId(),aql,codeLetter);
		if(acceptanceQualityLimits.isEmpty()){
			return null;
		}else{
			return acceptanceQualityLimits.get(0);
		}
	}
	private boolean existsSampleScheme(SampleScheme sampleScheme){
		String hql = "select count(id) from SampleScheme s where s.companyId = ? and s.type = ? and s.code = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(sampleScheme.getType());
		params.add(sampleScheme.getCode());
		if(sampleScheme.getId() != null){
			hql += " and s.id <> ?";
			params.add(sampleScheme.getId());
		}
		List<?> list = samplePlanDao.find(hql,params.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	public void saveSampleScheme(SampleScheme sampleScheme) throws RuntimeException{
		if(existsSampleScheme(sampleScheme)){
			throw new RuntimeException("已经存在相同的编码!");
		}
		samplePlanDao.save(sampleScheme);
	}
	public void deleteSampleScheme(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			logUtilDao.debugLog("删除", samplePlanDao.get(Long.valueOf(id)).toString());
			samplePlanDao.delete(Long.valueOf(id));
		}
	}
	public Page<SampleScheme> getListDatas(Page<SampleScheme> page,String type){
		String hql = "from SampleScheme s where s.companyId = ? and s.type = ? order by s.code";
		return samplePlanDao.findPage(page, hql, new Object[]{ContextUtils.getCompanyId(),type});
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
		}else{
			for(Object key : params.keySet()){
				resultJson.put(key,params.getJSONArray(key.toString()).get(0));
			}
			return resultJson;			
		}
	}
}
