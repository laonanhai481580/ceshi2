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
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.entity.UseBaseType;
import com.ambition.iqc.samplestandard.dao.SampleSchemeDao;
import com.ambition.iqc.samplestandard.dao.UseBaseTypeDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * SampleSchemeManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class SampleSchemeManager {
	@Autowired
	private SampleSchemeDao sampleSchemeDao;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private UseBaseTypeDao useBaseTypeDao;
	
	private UseBaseType useBaseType = null;
	
	public void setUseBaseType(UseBaseType useBaseType) {
		this.useBaseType = useBaseType;
	}

	public UseBaseType getUseBaseType(Long companyId) {
		if(useBaseType == null){
			String hql = "from UseBaseType u where u.companyId = ?";
			List<UseBaseType> useBaseTypes = useBaseTypeDao.find(hql,companyId);
			if(useBaseTypes.isEmpty()){
				useBaseType = new UseBaseType();
				useBaseType.setCompanyId(companyId);
			}else{
				useBaseType = useBaseTypes.get(0);
			}
		}
		return useBaseType;
	}
	
	public UseBaseType getUseBaseType() {
		if(useBaseType == null){
			String hql = "from UseBaseType u where u.companyId = ?";
			List<UseBaseType> useBaseTypes = useBaseTypeDao.find(hql,ContextUtils.getCompanyId());
			if(useBaseTypes.isEmpty()){
				useBaseType = new UseBaseType();
			}else{
				useBaseType = useBaseTypes.get(0);
			}
		}
		return useBaseType;
	}

	public void saveUseBaseType(UseBaseType useBaseType){
		useBaseTypeDao.save(useBaseType);
		this.useBaseType = useBaseType;
	}
	public SampleScheme getSampleScheme(Long id){
		return sampleSchemeDao.get(id);
	}
	
	/**
	 * 根据字码和类型获取抽样方案
	 * @param codeLetter
	 * @param type
	 * @return
	 */
	public AcceptanceQualityLimit queryQualityLimit(String codeLetter,String aql,String baseType,String type){
		String hql = "from AcceptanceQualityLimit a where a.baseType = ? and a.type = ? and a.companyId = ? and a.aql = ? and a.code=?";
		List<AcceptanceQualityLimit> acceptanceQualityLimits = sampleSchemeDao.find(hql,baseType,type,ContextUtils.getCompanyId(),aql,codeLetter);
		if(acceptanceQualityLimits.isEmpty()){
			return null;
		}else{
			return acceptanceQualityLimits.get(0);
		}
	}
	/**
	 * 根据字码和类型获取抽样方案
	 * @param codeLetter
	 * @param type
	 * @return
	 */
	public AcceptanceQualityLimit queryQualityLimit(String codeLetter,String aql,String baseType,String type,Long companyId){
		String hql = "from AcceptanceQualityLimit a where a.baseType = ? and a.type = ? and a.companyId = ? and a.aql = ? and a.code=?";
		List<AcceptanceQualityLimit> acceptanceQualityLimits = sampleSchemeDao.find(hql,baseType,type,companyId,aql,codeLetter);
		if(acceptanceQualityLimits.isEmpty()){
			return null;
		}else{
			return acceptanceQualityLimits.get(0);
		}
	}
	/**
	 * 根据字码和类型获取抽样方案
	 * @param codeLetter
	 * @param type
	 * @return
	 */
	public AcceptanceQualityLimit queryQualityLimit(String codeLetter,String aql,String baseType,String type,String countType){
		if(StringUtils.isEmpty(countType)){
			countType = SampleScheme.COUNT_TYPE;
		}
		String hql = "from AcceptanceQualityLimit a where a.baseType = ? and a.type = ? and a.countType = ? and a.companyId = ? and a.aql = ? and a.code=?";
		List<AcceptanceQualityLimit> acceptanceQualityLimits = sampleSchemeDao.find(hql,baseType,type,countType,ContextUtils.getCompanyId(),aql,codeLetter);
		if(acceptanceQualityLimits.isEmpty()){
			return null;
		}else{
			return acceptanceQualityLimits.get(0);
		}
	}
	/**
	 * 根据字码和类型获取抽样方案
	 * @param codeLetter
	 * @param type
	 * @return
	 */
	public AcceptanceQualityLimit queryQualityLimit(String codeLetter,String aql,String baseType,String type,String countType,Long companyId){
		if(StringUtils.isEmpty(countType)){
			countType = SampleScheme.COUNT_TYPE;
		}
		String hql = "from AcceptanceQualityLimit a where a.baseType = ? and a.type = ? and a.countType = ? and a.companyId = ? and a.aql = ? and a.code=?";
		List<AcceptanceQualityLimit> acceptanceQualityLimits = sampleSchemeDao.find(hql,baseType,type,countType,companyId,aql,codeLetter);
		if(acceptanceQualityLimits.isEmpty()){
			return null;
		}else{
			return acceptanceQualityLimits.get(0);
		}
	}
	private boolean existsSampleScheme(SampleScheme sampleScheme){
		String hql = "select count(id) from SampleScheme s where s.companyId = ? and s.baseType = ? and s.type = ? and s.code = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(sampleScheme.getBaseType());
		params.add(sampleScheme.getType());
		params.add(sampleScheme.getCode());
		if(SampleCodeLetter.MIL_TYPE.equals(sampleScheme.getBaseType())){
			hql += " and s.countType = ?";
			params.add(sampleScheme.getCountType());
		}
		if(sampleScheme.getId() != null){
			hql += " and s.id <> ?";
			params.add(sampleScheme.getId());
		}
		List<?> list = sampleSchemeDao.find(hql,params.toArray());
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
		sampleSchemeDao.save(sampleScheme);
	}
	
	/**
	 * 保存方案,1916
	 * @param sampleScheme
	 * @param params
	 * @throws RuntimeException
	 */
	public void saveSampleScheme(SampleScheme sampleScheme,JSONObject params) throws RuntimeException{
		if(existsSampleScheme(sampleScheme)){
			throw new RuntimeException("已经存在相同的代字码!");
		}
		String[] mitAqls = SampleScheme.getMitAQLs();
		List<String> aqls = new ArrayList<String>();
		for(String aql : mitAqls){
			aqls.add(aql.split("\\|")[0]);
		}
		for(Object pro : params.keySet()){
			String field = pro.toString();
			String value = params.getString(field);
			if("tighten".equals(field)){//加严
				List<SampleScheme> sampleSchemes = querySampleScheme(sampleScheme.getBaseType(),SampleScheme.TIGHTEN_TYPE,sampleScheme.getCode(),sampleScheme.getCountType());
				if("".equals(value)&&!sampleSchemes.isEmpty()){
					sampleSchemeDao.delete(sampleSchemes.get(0));
				}else{
					SampleScheme tightenSampleScheme = null;
					if(sampleSchemes.isEmpty()){
						tightenSampleScheme = new SampleScheme();
						tightenSampleScheme.setType(SampleScheme.TIGHTEN_TYPE);
						tightenSampleScheme.setBaseType(sampleScheme.getBaseType());
						tightenSampleScheme.setCountType(sampleScheme.getCountType());
						tightenSampleScheme.setCompanyId(ContextUtils.getCompanyId());
						tightenSampleScheme.setCreatedTime(new Date());
						tightenSampleScheme.setCreator(ContextUtils.getUserName());
						tightenSampleScheme.setLastModifiedTime(new Date());
						tightenSampleScheme.setLastModifier(ContextUtils.getUserName());
						tightenSampleScheme.setAcceptanceQualityLimits(new ArrayList<AcceptanceQualityLimit>());
					}else{
						tightenSampleScheme = sampleSchemes.get(0);
						tightenSampleScheme.setLastModifiedTime(new Date());
						tightenSampleScheme.setLastModifier(ContextUtils.getUserName());
						tightenSampleScheme.getAcceptanceQualityLimits().clear();
					}
					tightenSampleScheme.setCode(sampleScheme.getCode());
					if(!"".equals(value)&&value != null){
						tightenSampleScheme.setAmount(Integer.valueOf(value));
					}
					for(String aql : aqls){
						AcceptanceQualityLimit acceptanceQualityLimit = new AcceptanceQualityLimit();
						acceptanceQualityLimit.setAql(aql);
						acceptanceQualityLimit.setAc(0);
						acceptanceQualityLimit.setRe(1);
						acceptanceQualityLimit.setCompanyId(ContextUtils.getCompanyId());
						acceptanceQualityLimit.setCreatedTime(new Date());
						acceptanceQualityLimit.setCreator(ContextUtils.getUserName());
						acceptanceQualityLimit.setLastModifiedTime(new Date());
						acceptanceQualityLimit.setLastModifier(ContextUtils.getUserName());
						acceptanceQualityLimit.setSampleScheme(tightenSampleScheme);
						acceptanceQualityLimit.setCode(tightenSampleScheme.getCode());
						if(!"".equals(value)&&value != null){
							acceptanceQualityLimit.setAmount(Integer.valueOf(value));
						}
						acceptanceQualityLimit.setCountType(tightenSampleScheme.getCountType());
						acceptanceQualityLimit.setBaseType(tightenSampleScheme.getBaseType());
						acceptanceQualityLimit.setType(tightenSampleScheme.getType());
						tightenSampleScheme.getAcceptanceQualityLimits().add(acceptanceQualityLimit);
					}
					sampleSchemeDao.save(tightenSampleScheme);
				}
			}else if("relax".equals(field)){//减量
				List<SampleScheme> sampleSchemes = querySampleScheme(sampleScheme.getBaseType(),SampleScheme.RELAX_TYPE,sampleScheme.getCode(),sampleScheme.getCountType());
				if("".equals(value)&&!sampleSchemes.isEmpty()){
					sampleSchemeDao.delete(sampleSchemes.get(0));
				}else{
					SampleScheme relaxSampleScheme = null;
					if(sampleSchemes.isEmpty()){
						relaxSampleScheme = new SampleScheme();
						relaxSampleScheme.setType(SampleScheme.RELAX_TYPE);
						relaxSampleScheme.setBaseType(sampleScheme.getBaseType());
						relaxSampleScheme.setCountType(sampleScheme.getCountType());
						relaxSampleScheme.setCompanyId(ContextUtils.getCompanyId());
						relaxSampleScheme.setCreatedTime(new Date());
						relaxSampleScheme.setCreator(ContextUtils.getUserName());
						relaxSampleScheme.setLastModifiedTime(new Date());
						relaxSampleScheme.setLastModifier(ContextUtils.getUserName());
						relaxSampleScheme.setAcceptanceQualityLimits(new ArrayList<AcceptanceQualityLimit>());
					}else{
						relaxSampleScheme = sampleSchemes.get(0);
						relaxSampleScheme.setLastModifiedTime(new Date());
						relaxSampleScheme.setLastModifier(ContextUtils.getUserName());
						relaxSampleScheme.getAcceptanceQualityLimits().clear();
					}
					relaxSampleScheme.setCode(sampleScheme.getCode());
					if(!"".equals(value)&&value != null){
						relaxSampleScheme.setAmount(Integer.valueOf(value));
					}
					for(String aql : aqls){
						AcceptanceQualityLimit acceptanceQualityLimit = new AcceptanceQualityLimit();
						acceptanceQualityLimit.setAql(aql);
						acceptanceQualityLimit.setAc(0);
						acceptanceQualityLimit.setRe(1);
						acceptanceQualityLimit.setCompanyId(ContextUtils.getCompanyId());
						acceptanceQualityLimit.setCreatedTime(new Date());
						acceptanceQualityLimit.setCreator(ContextUtils.getUserName());
						acceptanceQualityLimit.setLastModifiedTime(new Date());
						acceptanceQualityLimit.setLastModifier(ContextUtils.getUserName());
						acceptanceQualityLimit.setSampleScheme(relaxSampleScheme);
						acceptanceQualityLimit.setCode(relaxSampleScheme.getCode());
						if(!"".equals(value)&&value != null){
							acceptanceQualityLimit.setAmount(Integer.valueOf(value));
						}
						acceptanceQualityLimit.setCountType(relaxSampleScheme.getCountType());
						acceptanceQualityLimit.setBaseType(relaxSampleScheme.getBaseType());
						acceptanceQualityLimit.setType(relaxSampleScheme.getType());
						relaxSampleScheme.getAcceptanceQualityLimits().add(acceptanceQualityLimit);
					}
					sampleSchemeDao.save(relaxSampleScheme);
				}
			}else{
				AcceptanceQualityLimit acceptanceQualityLimit = new AcceptanceQualityLimit();
				acceptanceQualityLimit.setAql(field);
				acceptanceQualityLimit.setAc(0);
				acceptanceQualityLimit.setRe(1);
				acceptanceQualityLimit.setCompanyId(ContextUtils.getCompanyId());
				acceptanceQualityLimit.setCreatedTime(new Date());
				acceptanceQualityLimit.setCreator(ContextUtils.getUserName());
				acceptanceQualityLimit.setLastModifiedTime(new Date());
				acceptanceQualityLimit.setLastModifier(ContextUtils.getUserName());
				acceptanceQualityLimit.setSampleScheme(sampleScheme);
				acceptanceQualityLimit.setCode(sampleScheme.getCode());
				if(!"".equals(value)&&value != null){
					acceptanceQualityLimit.setAmount(Integer.valueOf(value));
				}
				acceptanceQualityLimit.setCountType(sampleScheme.getCountType());
				acceptanceQualityLimit.setBaseType(sampleScheme.getBaseType());
				acceptanceQualityLimit.setType(sampleScheme.getType());
				sampleScheme.getAcceptanceQualityLimits().add(acceptanceQualityLimit);
			}
		}
		sampleSchemeDao.save(sampleScheme);
	}
	
	public void deleteSampleScheme(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			logUtilDao.debugLog("删除", sampleSchemeDao.get(Long.valueOf(id)).toString());
			sampleSchemeDao.delete(Long.valueOf(id));
		}
	}
	/**
	 * 删除1916相关的方案
	 * @param deleteIds
	 * @param other
	 */
	public void deleteSampleScheme(String deleteIds,String other){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			SampleScheme sampleScheme = sampleSchemeDao.get(Long.valueOf(id));
			logUtilDao.debugLog("删除", sampleScheme.toString());
			//删除加严
			List<SampleScheme> sampleSchemes = querySampleScheme(sampleScheme.getBaseType(),SampleScheme.TIGHTEN_TYPE,sampleScheme.getCode(),sampleScheme.getCountType());
			for(SampleScheme tightenSampleScheme : sampleSchemes){
				sampleSchemeDao.delete(tightenSampleScheme);
			}
			//删除减量
			sampleSchemes = querySampleScheme(sampleScheme.getBaseType(),SampleScheme.RELAX_TYPE,sampleScheme.getCode(),sampleScheme.getCountType());
			for(SampleScheme relaxSampleScheme : sampleSchemes){
				sampleSchemeDao.delete(relaxSampleScheme);
			}
			sampleSchemeDao.delete(Long.valueOf(id));
		}
	}
	
	public Page<SampleScheme> getListDatas(Page<SampleScheme> page,String baseType,String type){
		String hql = "from SampleScheme s where s.companyId = ? and s.baseType = ? and s.type = ? order by s.code";
		return sampleSchemeDao.findPage(page, hql, new Object[]{ContextUtils.getCompanyId(),baseType,type});
	}
	
	public Page<SampleScheme> getListDatasByCountType(Page<SampleScheme> page,String type,String baseType,String countType){
		String hql = "from SampleScheme s where s.companyId = ? and s.type = ? and s.baseType = ? and s.countType = ? order by s.code";
		return sampleSchemeDao.findPage(page, hql, new Object[]{ContextUtils.getCompanyId(),type,baseType,countType});
	}
	
	public List<SampleScheme> querySampleScheme(String baseType,String type,String code,String countType){
		String hql = "from SampleScheme s where s.companyId = ? and s.type = ? and s.baseType = ? and s.countType = ? and s.code = ?";
		return sampleSchemeDao.find(hql,ContextUtils.getCompanyId(),type,baseType,countType,code);
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
