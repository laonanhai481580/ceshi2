package com.ambition.iqc.samplestandard.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.entity.UseInspectionLevel;
import com.ambition.iqc.samplestandard.dao.SampleCodeLetterDao;
import com.ambition.iqc.samplestandard.dao.UseInspectionLevelDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * SampleCodeLetterManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class SampleCodeLetterManager {
	private static String inspectionLevel = null;
	
	@Autowired
	private UseInspectionLevelDao useInspectionLevelDao;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SampleCodeLetterDao sampleCodeLetterDao;
	
	/**
	 * 根据检验级别和来料数量获取字码
	 * @param inspectionLevel
	 * @param amount
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public String queryCodeLetter(String baseType,String inspectionLevel,Integer amount) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(inspectionLevel==null){
			inspectionLevel = "";
		}
		String hql = "from SampleCodeLetter s where s.companyId = ? and s.baseType = ? and (? between s.batchSize1 and s.batchSize2 " +
				"or (s.batchSize1 is null and s.batchSize2 >= ?) " +
				"or (s.batchSize2 is null and s.batchSize1 <= ?))";
		List<SampleCodeLetter> list = sampleCodeLetterDao.find(hql,ContextUtils.getCompanyId(),baseType,amount,amount,amount);
		if(list.isEmpty()){
			return null;
		}else{
			SampleCodeLetter sampleCodeLetter = list.get(0);
			if(PropertyUtils.getPropertyType(sampleCodeLetter,inspectionLevel)==null){
				return sampleCodeLetter.getSpecial2();
			}else{
				Object obj = PropertyUtils.getProperty(sampleCodeLetter,inspectionLevel);
				if(obj == null){
					return null;
				}else{
					return obj.toString();
				}
			}
		}
	}
	
	/**
	 * 根据检验级别和来料数量获取字码
	 * @param inspectionLevel
	 * @param amount
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public String queryCodeLetter(String baseType,String inspectionLevel,Integer amount,Long companyId){
		if(inspectionLevel==null){
			inspectionLevel = "";
		}
		String hql = "from SampleCodeLetter s where s.companyId = ? and s.baseType = ? and (? between s.batchSize1 and s.batchSize2 " +
				"or (s.batchSize1 is null and s.batchSize2 >= ?) " +
				"or (s.batchSize2 is null and s.batchSize1 <= ?))";
		List<SampleCodeLetter> list = sampleCodeLetterDao.find(hql,companyId,baseType,amount,amount,amount);
		if(list.isEmpty()){
			return null;
		}else{
			SampleCodeLetter sampleCodeLetter = list.get(0);
			try {
				inspectionLevel = inspectionLevel.replaceAll("\\.","-");
				if(PropertyUtils.getPropertyType(sampleCodeLetter,inspectionLevel)==null){
					return sampleCodeLetter.getSpecial2();
				}else{
					Object obj = PropertyUtils.getProperty(sampleCodeLetter,inspectionLevel);
					if(obj == null){
						return null;
					}else{
						return obj.toString();
					}
				}
			} catch (Exception e) {
				throw new AmbFrameException("获取字节码失败!",e);
			}
		}
	}
	
	public String getInspectionLevel(String baseType){
		if(inspectionLevel==null){
			UseInspectionLevel useInspectionLevel = useInspectionLevelDao.getUseInspectionLevel();
			Option option = getInspectionLevelOptionByValue(useInspectionLevel==null?"":useInspectionLevel.getInspectionLevel(),baseType);
			inspectionLevel = option.getValue();
		}
		return inspectionLevel;
	}
	
	private boolean checkBatchSize(SampleCodeLetter sampleCodeLetter) {
		StringBuffer hql = new StringBuffer("from SampleCodeLetter s where s.companyId = ? and s.baseType = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(sampleCodeLetter.getBaseType());
		if(sampleCodeLetter.getBatchSize1()==null||sampleCodeLetter.getBatchSize2()==null){
			List<Object> tempParams = new ArrayList<Object>();
			String sql = "select count(id) from SampleCodeLetter s where s.companyId = ? and s.baseType = ?";
			tempParams.add(ContextUtils.getCompanyId());
			tempParams.add(sampleCodeLetter.getBaseType());
			if(sampleCodeLetter.getBatchSize1()==null){
				sql += " and s.batchSize1 is null";
			}else{
				sql += " and s.batchSize2 is null";
			}
			if(sampleCodeLetter.getId() != null){
				sql += " and s.id <> ?";
				tempParams.add(sampleCodeLetter.getId());
			}
			List<?> list = sampleCodeLetterDao.find(sql,tempParams.toArray());
			if(Integer.valueOf(list.get(0).toString())>0){
				if(sampleCodeLetter.getBatchSize1()==null){
					throw new RuntimeException("已经设置了批量下限为最小值！");
				}else{
					throw new RuntimeException("已经设置了批量上限为最大值！");
				}
			}
			if(sampleCodeLetter.getBatchSize1()!=null){
				hql.append(" and (? <= s.batchSize2 or ? <= s.batchSize1)");
				params.add(sampleCodeLetter.getBatchSize1());
				params.add(sampleCodeLetter.getBatchSize1());
			}else{
				hql.append(" and (? >= s.batchSize2 or ? >= s.batchSize1)");
				params.add(sampleCodeLetter.getBatchSize2());
				params.add(sampleCodeLetter.getBatchSize2());
			}
		}else{
			hql.append(" and ((s.batchSize1 is not null and s.batchSize1 is not null and (? between s.batchSize1 and s.batchSize2 or ? between s.batchSize1 and s.batchSize2)) " +
					"or (s.batchSize1 is null and (? <= s.batchSize2 or ? <= s.batchSize2)) " +
					"or (s.batchSize2 is null and (s.batchSize1 <= ? or s.batchSize1 <= ?)))");
			params.add(sampleCodeLetter.getBatchSize1());
			params.add(sampleCodeLetter.getBatchSize2());
			params.add(sampleCodeLetter.getBatchSize1());
			params.add(sampleCodeLetter.getBatchSize2());
			params.add(sampleCodeLetter.getBatchSize1());
			params.add(sampleCodeLetter.getBatchSize2());
		}
		if(sampleCodeLetter.getId() != null){
			hql.append(" and s.id <> ?");
			params.add(sampleCodeLetter.getId());
		}
		List<SampleCodeLetter> list = sampleCodeLetterDao.find(hql.toString(),params.toArray());
		if(list.size()>0){
			SampleCodeLetter sampleCode = list.get(0);
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
	
	public SampleCodeLetter getSampleCodeLetter(Long id){
		return sampleCodeLetterDao.get(id);
	}
	
	public void saveSampleCodeLetter(SampleCodeLetter sampleCodeLetter){
		if(sampleCodeLetter.getBatchSize1()==null&&sampleCodeLetter.getBatchSize2()==null){
			throw new RuntimeException("上限与下限不能同时为空！");
		}
		if(sampleCodeLetter.getBatchSize1()!=null
				&&sampleCodeLetter.getBatchSize2()!=null
				&&sampleCodeLetter.getBatchSize1()>sampleCodeLetter.getBatchSize2()){
			throw new RuntimeException("上限不能小于下限！");
		}else if(checkBatchSize(sampleCodeLetter)){
			throw new RuntimeException("其他批量已经设置的上下限已经包含了当前的上下限！");
		}
		sampleCodeLetterDao.save(sampleCodeLetter);
	}

	public void deleteSampleCodeLetter(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			logUtilDao.debugLog("删除", sampleCodeLetterDao.get(Long.valueOf(id)).toString());
			sampleCodeLetterDao.delete(Long.valueOf(id));
		}
	}
	public Page<SampleCodeLetter> getListDatas(Page<SampleCodeLetter> page, String baseType){
		String hql = "from SampleCodeLetter s where s.companyId = ? and s.baseType = ? order by s.batchSize1";
		return sampleCodeLetterDao.findPage(page, hql, new Object[]{ContextUtils.getCompanyId(),baseType});
	}
	
	public Option saveInspectionLevel(String level,String baseType){
		Option option = getInspectionLevelOptionByValue(level,baseType);
		UseInspectionLevel useInspectionLevel = useInspectionLevelDao.getUseInspectionLevel();
		if(useInspectionLevel==null){
			useInspectionLevel = new UseInspectionLevel();
			useInspectionLevel.setCompanyId(ContextUtils.getCompanyId());
			useInspectionLevel.setCreatedTime(new Date());
			useInspectionLevel.setCreator(ContextUtils.getUserName());
		}
		useInspectionLevel.setInspectionLevel(option.getValue());
		useInspectionLevel.setLastModifiedTime(new Date());
		useInspectionLevel.setLastModifier(ContextUtils.getUserName());
		useInspectionLevelDao.save(useInspectionLevel);
		inspectionLevel = option.getValue();
		return option;
	}
	/**
	 * 获取检验级别
	 * @return
	 */
	public List<Option> getInspectionLevelOptions(String baseType){
		String[] aqls = null;
		if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
			aqls = SampleScheme.getMitAQLs();
		}else{
			aqls = new String[]{"special1|特殊S-1","special2|特殊S-2","special3|特殊S-3","special4|特殊S-4","ordinary1|一般I","ordinary2|一般II","ordinary3|一般III"};
		}
		List<Option> options = new ArrayList<Option>();
		for(String aql : aqls){
			Option option = new Option();
			if(SampleCodeLetter.MIL_TYPE.equals(baseType)){
				option.setName(aql.split("\\|")[1]);
			}else{
				option.setName(aql.split("\\|")[1]);
			}
			option.setValue(aql.split("\\|")[0]);
			options.add(option);
		}
		return options;
	}
	
	
	public Option getInspectionLevelOptionByValue(String level,String baseType){
		List<Option> options = getInspectionLevelOptions(baseType);
		for(Option option : options){
			if(option.getValue().equals(level)){
				return option;
			}
		}
		return options.get(0);
	}
}
