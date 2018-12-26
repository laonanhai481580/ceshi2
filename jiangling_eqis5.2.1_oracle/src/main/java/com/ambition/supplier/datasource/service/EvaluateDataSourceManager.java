package com.ambition.supplier.datasource.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.datasource.dao.EvaluateDataSourceDao;
import com.ambition.supplier.entity.EvaluateDataSource;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.wf.engine.client.ExtendField;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
/**
 * 类名:数据来源业务管理类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：管理数据来源增删改查,缓存数据来源执行的对象</p>
 * @author  赵骏
 * @version 1.00 2013-4-19 发布
 */
@Service
@Transactional(rollbackFor=Throwable.class)
public class EvaluateDataSourceManager {
	@Autowired
	private EvaluateDataSourceDao evaluateDataSourceDao;
	@Autowired
	private EvaluateDataSourceExecuteUtil dataSourceGenerateUtil;
	//规则执行对象的缓存
	private Map<String,EvaluateDataSourceExecuteBase> dataSourceExecuteMap = new ConcurrentHashMap<String, EvaluateDataSourceExecuteBase>();
	
	/**
	  * 方法名:查询数据来源 
	  * <p>功能说明：</p>
	  * @param page
	  * @return Page
	 * @throws CloneNotSupportedException 
	 */
	public Page<EvaluateDataSource> search(Page<EvaluateDataSource> page){
		Page<EvaluateDataSource> p = evaluateDataSourceDao.search(page);
		List<EvaluateDataSource> gradeRules = new ArrayList<EvaluateDataSource>();
		for(EvaluateDataSource gradeRule : p.getResult()){
			EvaluateDataSource cloneRule = (EvaluateDataSource) gradeRule.clone();
			cloneRule.setExecuteCode("");
			gradeRules.add(cloneRule);
		}
		p.setResult(gradeRules);
		return p;
	}
	
	/**
	  * 方法名:查询数据来源 
	  * <p>功能说明：根据查询条件查询自动数据来源</p>
	  * @param page
	  * @param params
	  * @return
	 */
	public Page<EvaluateDataSource> search(Page<EvaluateDataSource> page,JSONObject params){
		params = convertJsonObject(params);
		StringBuffer hqlBuffer = new StringBuffer("from EvaluateDataSource d where d.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		
		//绑定查询条件
		for(Object key : params.keySet()){
			String[] properties = key.toString().split("_");
			if(properties.length<3){
				continue;
			}
			Object value = params.get(key);
			if(value==null){
				continue;
			}
			if(properties.length > 3){
				value = convertObject(properties[3],value);
			}
			if("like".equals(properties[2])){
				hqlBuffer.append(" and " + properties[0] + "." + properties[1] + " like ? ");
				searchParams.add("%" + value + "%");
			}else{
				hqlBuffer.append(" and " + properties[0] + "." + properties[1] + " = ? ");
				searchParams.add(value);
			}
		}
		return evaluateDataSourceDao.findPage(page,hqlBuffer.toString(),searchParams);
	}
	

	/**
	  * 方法名: 查询所有的数据来源
	  * <p>功能说明：查询所有自动数据来源</p>
	  * @return
	 */
	public List<EvaluateDataSource> listAll() {
		return evaluateDataSourceDao.find("from EvaluateDataSource d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	  * 方法名: 检查是否存在相同名称的数据来源
	  * <p>功能说明：根据规则ID,编码和名称判断是否存在相同的</p>
	  * @param gradeRule
	  * @return
	 */
	private Boolean isExistDataSource(EvaluateDataSource dataSource){
		String hql = "select count(d.id) from EvaluateDataSource d where d.companyId = ? and (d.code = ? or d.name=?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(dataSource.getCode());
		params.add(dataSource.getName());
		if(dataSource.getId() != null){
			hql += " and d.id  <> ?";
			params.add(dataSource.getId());
		}
		int count = Integer.valueOf(evaluateDataSourceDao.find(hql,params.toArray()).get(0).toString());
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	  * 方法名: 获取数据来源对象
	  * <p>功能说明：根据ID查询自动数据来源 </p>
	  * @param id
	  * @return
	 */
	public EvaluateDataSource getGradeRule(Long id){
		return evaluateDataSourceDao.get(id);
	}
	
	/**
	  * 方法名:获取数据来源 
	  * <p>功能说明：根据规则编码获取数据来源对象</p>
	  * @param ruleCode
	  * @return EvaluateDataSource
	 */
	public EvaluateDataSource getEvaluateDataSourceByCode(String dataSourceCode){
		return evaluateDataSourceDao.getEvaluateDataSourceByCode(dataSourceCode);
	}
	
	/**
	  * 方法名: 保存数据来源
	  * <p>功能说明：保存数据来源,编码,名称,计算代码不能为空</p>
	  * @param gradeRule
	  * @throws Exception
	 */
	public void saveEvaluateDataSource(EvaluateDataSource dataSource) throws Exception{
		if(StringUtils.isEmpty(dataSource.getCode())){
			throw new AmbFrameException("编码不能为空！");
		}
		if(StringUtils.isEmpty(dataSource.getName())){
			throw new AmbFrameException("名称为能为空！");
		}
		if(StringUtils.isEmpty(dataSource.getExecuteCode())){
			throw new AmbFrameException("执行代码不能为空");
		}
		if(isExistDataSource(dataSource)){
			throw new AmbFrameException("已存在相同的编码与名称！");
		}
		//测试生成的代码是否正确
		try {
			EvaluateDataSourceExecuteBase dataSourceExecute = dataSourceGenerateUtil.generateObject(System.currentTimeMillis(),dataSource.getExecuteCode());
			dataSourceExecute.execute("1",new Date(),new Date(),"FPCA");
			dataSourceExecuteMap.put(dataSource.getCode(),dataSourceExecute);
		} catch (Throwable e) {
			throw new AmbFrameException("测试执行代码错误," + e.getMessage());
		}
		evaluateDataSourceDao.save(dataSource);
	}
	
	/**
	  * 方法名:删除数据来源 
	  * <p>功能说明：删除自动数据来源,同时删除对应缓存的执行结构</p>
	  * @param deleteIds
	 */
	public void deleteEvaluateDataSource(String deleteIds){
		String[] ids = deleteIds.split(",");
		List<String> codes = new ArrayList<String>();
		for(String id : ids){
			EvaluateDataSource dataSource = evaluateDataSourceDao.get(Long.valueOf(id));
			if(dataSource != null){
				evaluateDataSourceDao.delete(dataSource);
				codes.add(dataSource.getCode());
			}
		}
		//移除缓存的规则执行对象
		for(String code : codes){
			dataSourceExecuteMap.remove(code);
		}
	}
	
	/**
	  * 方法名: 获取数据来源
	  * <p>功能说明：根据数据来源对象计算数据来源结果</p>
	  * @param dataSourceCode
	  * @param supplierId
	  * @param startDate
	  * @param endDate
	  * @return
	  * @throws Exception
	 */
	public Map<String,Object> executeEvaluateDataSource(String dataSourceCode,String supplierCode,Date startDate,Date endDate,String materialType) throws Exception{
		EvaluateDataSourceExecuteBase execute = dataSourceExecuteMap.get(dataSourceCode);
		if(execute == null){
			EvaluateDataSource dataSource = getEvaluateDataSourceByCode(dataSourceCode);
			if(dataSource == null){
				dataSourceExecuteMap.put(dataSourceCode, null);
			}else{
				execute = dataSourceGenerateUtil.generateObject(dataSource.getId(),dataSource.getExecuteCode());
				dataSourceExecuteMap.put(dataSourceCode, execute);
			}
		}
		if(execute == null){
			throw new AmbFrameException("编码为【"+dataSourceCode+"】的数据来源不存在!");
		}else{
			Map<String,Object> soucre = execute.execute(supplierCode, startDate, endDate,materialType);
			return soucre;
		}
	}
	/**
	  * 方法名: 执行sql语句
	  * <p>功能说明：执行sql语句</p>
	  * @param sql
	  * @param values
	  * @return
	 */
	public List<Object> executeSql(String sql,Object... values){
		return evaluateDataSourceDao.findBySql(sql, values);
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
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
	
	/**
	 * 转换对象
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertObject(String type,Object value){
		if(value == null){
			return null;
		}else if("date".equals(type)){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			try {
				return new Date(sdf.parse(value.toString()).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}else if("long".equals(type)){
			return Long.valueOf(value.toString());
		}else if("double".equals(type)){
			return Double.valueOf(value.toString());
		}else{
			return value;
		}
	}
	
	/**
	 * 转换对象
	 * @param className
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private Object convertObject(Class<?> className,JSONObject jsonObject,JSONObject createPropertyJson,JSONObject updatePropertyJson) throws Exception{
		try {
			Long id = jsonObject.getLong("id");
			Object obj = null;
			if(id < 0){
				obj = className.newInstance();
				for(Object key : createPropertyJson.keySet()){
					setProperty(obj, key,createPropertyJson);
				}
				PropertyUtils.setProperty(obj,"id",id);
			}else{
//				obj = supplierDao.getSession().get(className, id);
				if(obj == null){
					return obj;
				}
				for(Object key : updatePropertyJson.keySet()){
					setProperty(obj, key,updatePropertyJson);
				}
			}
			jsonObject.remove("id");
			ExtendField extendField = null;
			for(Object key : jsonObject.keySet()){
				if(key.toString().startsWith("extendField.")){
					if(extendField == null){
						extendField = new ExtendField();
					}
					String fieldName = key.toString().split("\\.")[1];
					setProperty(extendField, fieldName,jsonObject.get(key.toString()));
				}else{
					setProperty(obj, key,jsonObject);
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private void setProperty(Object obj,Object key,JSONObject json) throws Exception{
		String[] args = key.toString().split("_");
		if(args.length == 1){
			Class<?> type = PropertyUtils.getPropertyType(obj,key.toString()); 
			if(type != null){
				if(StringUtils.isEmpty(json.get(key).toString())){
					PropertyUtils.setProperty(obj,key.toString(),null);
				}else{
					if(Date.class.equals(type)){
						PropertyUtils.setProperty(obj,key.toString(),DateUtil.parseDate(json.get(key).toString()));
					}else{
						PropertyUtils.setProperty(obj,key.toString(),json.get(key));
					}
				}
			}
		}else if(args.length == 2){
			if("long".equals(args[1])){
				if(PropertyUtils.getPropertyType(obj,args[0]) != null){
					PropertyUtils.setProperty(obj,args[0],json.getLong(key.toString()));
				}
			}else if("utildate".equals(args[1])){
				if(PropertyUtils.getPropertyType(obj,args[0]) != null){
					JSONObject dateJson = json.getJSONObject(key.toString());
					PropertyUtils.setProperty(obj,args[0],new Date(dateJson.getLong("time")));
				}
			}
		}
	}

	private void setProperty(Object obj,String fieldName,Object val) throws Exception{
		Class<?> classType = PropertyUtils.getPropertyType(obj, fieldName);
		if(classType != null){
			if(val==null||StringUtils.isEmpty(val.toString())){
				PropertyUtils.setProperty(obj,fieldName,null);
			}else{
				if(Date.class.getName().equals(classType.getName())){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					PropertyUtils.setProperty(obj,fieldName,sdf.parse(val.toString()));
				}else if(Double.class.getName().equals(classType.getName())){
					PropertyUtils.setProperty(obj,fieldName,Double.valueOf(val.toString()));
				}else{
					PropertyUtils.setProperty(obj,fieldName,val);
				}
			}
		}
	}
	
}
