package com.ambition.iqc.samplestandard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.AcceptanceQualityLimit;
import com.ambition.iqc.entity.SampleC1051CodeLetter;
import com.ambition.iqc.entity.SampleC1051Scheme;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.samplestandard.dao.SampleC1051CodeLetterDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:c1051抽样标准
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-30 发布
 */
@Service
@Transactional
public class SampleC1051CodeLetterManager {
	@Autowired
	private SampleC1051CodeLetterDao sampleC1051CodeLetterDao;
	
	public SampleC1051CodeLetter getSampleC1051CodeLetter(Long id){
		return sampleC1051CodeLetterDao.get(id);
	}
	
	/**
	 * 根据字码和类型获取抽样方案
	 * @param checkAmount 检验数量
	 * @param aql 抽检AQL
	 * @return
	 */
	public AcceptanceQualityLimit queryQualityLimit(Integer checkAmount,String aql){
		return queryQualityLimit(checkAmount, aql,ContextUtils.getCompanyId());
	}
	/**
	 * 根据字码和类型获取抽样方案
	 * @param checkAmount 检验数量
	 * @param aql 抽检AQL
	 * @return
	 */
	public AcceptanceQualityLimit queryQualityLimit(Integer checkAmount,String aql,Long companyId){
		if(StringUtils.isEmpty(aql)||checkAmount==null){
			return null;
		}
		String hql = "select s from SampleC1051Scheme s where s.companyId = ? and s.aql = ? and ? between ifnull(s.sampleC1051CodeLetter.batchSize1,0) and ifnull(s.sampleC1051CodeLetter.batchSize2,"+Integer.MAX_VALUE+")";
		List<SampleC1051Scheme> sampleC1051Schemes = sampleC1051CodeLetterDao.find(hql,companyId,aql,checkAmount);
		if(sampleC1051Schemes.isEmpty()){
			return null;
		}else{
			SampleC1051Scheme scheme = sampleC1051Schemes.get(0);
			AcceptanceQualityLimit limit = new AcceptanceQualityLimit();
			if(scheme.getAmount()==null||scheme.getAmount() > checkAmount){
				limit.setAmount(checkAmount);
			}else{
				limit.setAmount(scheme.getAmount());
			}
			limit.setAc(0);
			limit.setCode(aql);
			limit.setAql(aql);
			limit.setBaseType(SampleCodeLetter.MIL1051_TYPE);
			limit.setRe(1);
			return limit;
		}
	}
	private boolean checkBatchSize(SampleC1051CodeLetter codeLetter) {
		StringBuffer hql = new StringBuffer("from SampleC1051CodeLetter s where s.companyId = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		if(codeLetter.getBatchSize1()==null||codeLetter.getBatchSize2()==null){
			List<Object> tempParams = new ArrayList<Object>();
			String sql = "select count(id) from SampleC1051CodeLetter s where s.companyId = ?";
			tempParams.add(ContextUtils.getCompanyId());
			if(codeLetter.getBatchSize1()==null){
				sql += " and s.batchSize1 is null";
			}else{
				sql += " and s.batchSize2 is null";
			}
			if(codeLetter.getId() != null){
				sql += " and s.id <> ?";
				tempParams.add(codeLetter.getId());
			}
			List<?> list = sampleC1051CodeLetterDao.find(sql,tempParams.toArray());
			if(Integer.valueOf(list.get(0).toString())>0){
				if(codeLetter.getBatchSize1()==null){
					throw new RuntimeException("已经设置了批量下限为最小值！");
				}else{
					throw new RuntimeException("已经设置了批量上限为最大值！");
				}
			}
			if(codeLetter.getBatchSize1()!=null){
				hql.append(" and (? <= s.batchSize2 or ? <= s.batchSize1)");
				params.add(codeLetter.getBatchSize1());
				params.add(codeLetter.getBatchSize1());
			}else{
				hql.append(" and (? >= s.batchSize2 or ? >= s.batchSize1)");
				params.add(codeLetter.getBatchSize2());
				params.add(codeLetter.getBatchSize2());
			}
		}else{
			hql.append(" and ((s.batchSize1 is not null and s.batchSize1 is not null and (? between s.batchSize1 and s.batchSize2 or ? between s.batchSize1 and s.batchSize2)) " +
					"or (s.batchSize1 is null and (? <= s.batchSize2 or ? <= s.batchSize2)) " +
					"or (s.batchSize2 is null and (s.batchSize1 <= ? or s.batchSize1 <= ?)))");
			params.add(codeLetter.getBatchSize1());
			params.add(codeLetter.getBatchSize2());
			params.add(codeLetter.getBatchSize1());
			params.add(codeLetter.getBatchSize2());
			params.add(codeLetter.getBatchSize1());
			params.add(codeLetter.getBatchSize2());
		}
		if(codeLetter.getId() != null){
			hql.append(" and s.id <> ?");
			params.add(codeLetter.getId());
		}
		List<SampleC1051CodeLetter> list = sampleC1051CodeLetterDao.find(hql.toString(),params.toArray());
		if(list.size()>0){
			SampleC1051CodeLetter sampleCode = list.get(0);
			StringBuffer message = new StringBuffer("");
			if(sampleCode.getBatchSize1() == null){
				message.append("下限为无限小");
			}else{
				message.append("下限为" + sampleCode.getBatchSize1());
			}
			if(sampleCode.getBatchSize2() == null){
				message.append("上限为无限大");
			}else{
				message.append("上限为" + sampleCode.getBatchSize2());
			}
			message.append("的批量设置已经包含了当前设置的上下限!");
			throw new RuntimeException(message.toString());
		}else{
			return false;
		}
	}
	
	/**
	 * 保存方案,1916
	 * @param sampleScheme
	 * @param params
	 * @throws RuntimeException
	 */
	public void saveSampleC1051CodeLetter(SampleC1051CodeLetter codeLetter,JSONObject params) throws RuntimeException{
		if(codeLetter.getBatchSize1()==null&&codeLetter.getBatchSize2()==null){
			throw new AmbFrameException("上限与下限不能同时为空！");
		}
		if(codeLetter.getBatchSize1()!=null
				&&codeLetter.getBatchSize2()!=null
				&&codeLetter.getBatchSize1()>codeLetter.getBatchSize2()){
			throw new RuntimeException("上限不能小于下限！");
		}else if(checkBatchSize(codeLetter)){
			throw new AmbFrameException("其他批量已经设置的上下限已经包含了当前的上下限！");
		}
		if(codeLetter.getSampleC1051Schemes() != null){
			codeLetter.getSampleC1051Schemes().clear();
		}else{
			codeLetter.setSampleC1051Schemes(new ArrayList<SampleC1051Scheme>());
		}
		for(Object pro : params.keySet()){
			String field = pro.toString();
			String value = params.getString(field);
			SampleC1051Scheme sampleC1051Scheme = new SampleC1051Scheme();
			sampleC1051Scheme.setAql(field);
			sampleC1051Scheme.setAc(0);
			sampleC1051Scheme.setRe(1);
			sampleC1051Scheme.setCompanyId(ContextUtils.getCompanyId());
			sampleC1051Scheme.setCreatedTime(new Date());
			sampleC1051Scheme.setCreator(ContextUtils.getUserName());
			sampleC1051Scheme.setLastModifiedTime(new Date());
			sampleC1051Scheme.setLastModifier(ContextUtils.getUserName());
			sampleC1051Scheme.setSampleC1051CodeLetter(codeLetter);
			if(!"".equals(value)&&value != null){
				sampleC1051Scheme.setAmount(Integer.valueOf(value));
			}
			codeLetter.getSampleC1051Schemes().add(sampleC1051Scheme);
		}
		sampleC1051CodeLetterDao.save(codeLetter);
	}
	
	public void deleteSampleC1051CodeLetter(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			sampleC1051CodeLetterDao.delete(Long.valueOf(id));
		}
	}
	
	public Page<SampleC1051CodeLetter> getListDatas(Page<SampleC1051CodeLetter> page){
		String hql = "from SampleC1051CodeLetter s where s.companyId = ? order by s.batchSize1";
		return sampleC1051CodeLetterDao.findPage(page, hql, new Object[]{ContextUtils.getCompanyId()});
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
