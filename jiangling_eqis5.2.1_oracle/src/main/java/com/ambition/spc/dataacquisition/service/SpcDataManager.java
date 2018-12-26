package com.ambition.spc.dataacquisition.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.util.common.DateUtil;

/**
 * 类名:SPC数据查询
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2018-7-16 发布
 */
@Service
public class SpcDataManager {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	private Session session;
	private boolean needCache = true;//开启查询缓存，默认开启
	private static Map<String,Map<String,Object>> queryCacheMap = new HashMap<String, Map<String,Object>>();//缓存值
	//设定缓存失效时间是10秒
	private static long expireTimes = 10*1000;
	public SpcDataManager(){}
	public SpcDataManager(boolean needCache){
		this.needCache = needCache;
	}
	public SpcDataManager(Session session){
		this.session = session;
	}
	public SpcDataManager(Session session,boolean needCache){
		this.session = session;
		this.needCache = needCache;
	}
	private Session getSession(){
		if(session==null){
			return qualityFeatureDao.getSession();
		}else{
			return session;
		}
	}
	/**
	  * 方法名: 根据查询条件查询数据
	  * 返回值参数有:
	  * values  采集数据
	  * posDatas 对应的位置信息   
	  * @return
	 */
	public Map<String,Object> querySpcDataValues(QualityFeature qualityFeature,String startDateStr,String endDateStr,
			JSONObject layerParams,
			Integer pageNo,Integer pageSize,Integer lastAmount){
		Date startDate = null,endDate=null;
		if(StringUtils.isNotEmpty(startDateStr)){
			startDate = DateUtil.parseDate(startDateStr.replace("/","-"));
		}
		if(StringUtils.isNotEmpty(endDateStr)){
			if(endDateStr.length()==10){
				endDateStr += " 23:59:59.999";
			}
			endDate = DateUtil.parseDate(endDateStr.replace("/","-"),"yyyy-MM-dd HH:mm:ss.SSS");
		}
		return querySpcDataValues(qualityFeature, startDate, endDate, layerParams, pageNo, pageSize, lastAmount);
	}
	/**
	  * 方法名: 根据查询条件查询数据
	  * 返回值参数有:
	  * values  采集数据
	  * posDatas 对应的位置信息   
	  * @return
	 */
	public Map<String,Object> querySpcDataValues(QualityFeature qualityFeature,
			Date startDate,Date endDate,
			JSONObject layerParams,
			Integer pageNo,
			Integer pageSize,
			Integer lastAmount){
		Integer sampleCapacity = qualityFeature.getSampleCapacity();
		if(sampleCapacity == null){
			sampleCapacity = 5;
		}
		Integer effectiveCapacity = qualityFeature.getEffectiveCapacity();
		if(effectiveCapacity == null){
			effectiveCapacity = sampleCapacity;
		}
		if(pageNo == null){
			pageNo = 1;
		}
		Integer firstResults = null;
		Integer maxResults = null;
		if(pageSize != null){
			firstResults = (pageNo-1)*sampleCapacity;
			maxResults = pageSize*sampleCapacity;
		}
		return querySpcDataValueByRange(qualityFeature, startDate, endDate, layerParams, firstResults, maxResults, lastAmount);
	}
	
	
	/**
	  * 方法名: 根据查询条件查询数据
	  * 返回值参数有:
	  * values  采集数据
	  * posDatas 对应的位置信息   
	  * @return
	 */
	public Map<String,Object> querySpcDataValueByRange(QualityFeature qualityFeature,
			Date startDate,Date endDate,
			JSONObject layerParams,
			Integer firstResults,
			Integer maxResults,
			Integer lastAmount){
		Integer sampleCapacity = qualityFeature.getSampleCapacity();
		Integer effectiveCapacity = qualityFeature.getEffectiveCapacity();
		List<String> layerFields = new ArrayList<String>();
		String layerSql = "";
		if(qualityFeature.getFeatureLayers() != null){
			for(FeatureLayer layer : qualityFeature.getFeatureLayers()){
				layerFields.add(layer.getDetailCode());
				layerSql += "," + layer.getDetailCode();
			}
		}
		List<Object> searchParams = new ArrayList<Object>();
		StringBuffer whereSql = new StringBuffer(" where 1=1 ");
		StringBuilder queryCacheStr = new StringBuilder(qualityFeature.getId().toString());
		if(startDate != null){
			whereSql.append(" and inspection_date >= ?");
			searchParams.add(new java.sql.Timestamp(startDate.getTime()));
			queryCacheStr.append("start:"+DateUtil.formateDateStr(startDate));
		}
		if(endDate != null){
			whereSql.append(" and inspection_date <= ?");
			searchParams.add(new java.sql.Timestamp(endDate.getTime()));
			queryCacheStr.append("end:"+DateUtil.formateDateStr(endDate));
		}
		if(layerParams != null){
			for(String layerField : layerFields){
				if(layerParams.containsKey(layerField)){
					String value = layerParams.getString(layerField);
					value = flex.messaging.util.URLDecoder.decode(value);
					if(StringUtils.isNotEmpty(value)){
						whereSql.append(" and "+layerField+" like ?");
						searchParams.add("%"+value.trim()+"%");
						queryCacheStr.append(layerField+":"+value.trim());
					}
				}
			}
		}
		if(lastAmount!=null&&lastAmount>0){
			queryCacheStr.append("lastAmount:"+lastAmount);
		}else{
			queryCacheStr.append("lastAmount:"+lastAmount);
			if(firstResults != null){
				queryCacheStr.append("first:"+firstResults);
			}
			if(maxResults != null){
				queryCacheStr.append("max:"+maxResults);
			}
		}
		//需要缓存时,直接从缓存中取值
		if(needCache){
			synchronized (queryCacheMap) {
				Map<String,Object> resultMap = queryCacheMap.get(queryCacheStr.toString());
				if(resultMap != null){
					Long lastTimes = (Long)resultMap.get("lastCacheTime");
					long between = System.currentTimeMillis() - lastTimes;
					//失效时间范围内时,直接返回缓存值
					if(between<expireTimes){
						Map<String,Object> newMap = new HashMap<String, Object>();
						newMap.put("values", resultMap.get("values"));
						newMap.put("posDatas", resultMap.get("posDatas"));
						logger.info("从缓存中返回值");
						
						//清除过期的对象
						List<String> removeKeys = new ArrayList<String>();
						for(String str : queryCacheMap.keySet()){
							Map<String,Object> map = queryCacheMap.get(str);
							long hisTimes = (Long)map.get("lastCacheTime");
							long hisBetween = System.currentTimeMillis() - hisTimes;
							if(hisBetween>expireTimes){
								removeKeys.add(str);
							}
						}
						for(String str : removeKeys){
							queryCacheMap.remove(str);
						}
						return newMap;
					}
				}
			}
		}
		//后30位数据
		List<?> list = null;
		if(lastAmount!=null&&lastAmount>0){//只查最后30位数据
			String sql = "from " + qualityFeature.getTargetTableName();
			Query query = getSession().createSQLQuery("select count(*) " + sql + whereSql);
			for(int i=0;i<searchParams.size();i++){
				query.setParameter(i,searchParams.get(i));
			}
			Integer total = Integer.valueOf(query.list().get(0).toString());
			Integer groupCount = total/sampleCapacity;
			//判断是否符合计算样本数,符合时才累加
			if(total%sampleCapacity>=effectiveCapacity){
				groupCount++;
			}
			Integer startGroup = 1;
			if(groupCount>lastAmount){
				startGroup = groupCount - lastAmount;
			}
			query = getSession().createSQLQuery("select id,inspection_date,data_value" + layerSql + " " + sql + whereSql + " order by inspection_date");
			for(int i=0;i<searchParams.size();i++){
				query.setParameter(i,searchParams.get(i));
			}
			query.setFirstResult((startGroup-1)*sampleCapacity);
			list = query.list();
		}else{
			String sql = "select id,inspection_date,data_value" + layerSql + " from " + qualityFeature.getTargetTableName() + whereSql + " order by inspection_date";
			Query query = getSession().createSQLQuery(sql);
			for(int i=0;i<searchParams.size();i++){
				query.setParameter(i,searchParams.get(i));
			}
			//分页条件为空时,查询全部
			if(firstResults != null){
				query.setFirstResult(firstResults);
			}
			if(maxResults != null){
				query.setMaxResults(maxResults);
			}
			list = query.list();
		}
		Integer groupCount = list.size()/sampleCapacity;
		if(list.size()%sampleCapacity>=effectiveCapacity){
			groupCount++;
		}
		List<double[]> values = new ArrayList<double[]>();
		JSONArray posDatas = new JSONArray();
		for(int i=0;i<list.size();i+=sampleCapacity){
			String ids= "";
			Integer num = 0;
			
			double[] a = new double[sampleCapacity];
			for(int j=0;j<sampleCapacity&&(j+i)<list.size();j++){
				Object[] objs = (Object[])list.get(j+i);
				String id = objs[0].toString();
				if(ids.length()>0){
					ids += ",";
				}
				ids += id;
				
				
				
				Double value = Double.valueOf(objs[2].toString());
				a[j]=value;
				
				num++;
			}
			//有效容量时添加
			if(num>=effectiveCapacity){
				values.add(a);
				
				//缓存坐标数据
				JSONObject d = new JSONObject();
				d.put("id",ids);
				
				Object[] objs = (Object[])list.get(i);
				Date inspectionDate = (Date)objs[1];
				d.put("date",DateUtil.formateDateStr(inspectionDate));
				d.put("inspectionDateTime",DateUtil.formateDateStr(inspectionDate,"yyyyMMddHHmmss.SSS"));//精确到毫秒,方便查询
				d.put("inspectionDate",d.getString("date"));
				for(int k=0;k<layerFields.size();k++){
					Object layerValue = objs[3+k];
					if(layerValue != null){
						d.put(layerFields.get(k),layerValue);
					}
				}
				posDatas.add(d);
			}
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("values",values);
		resultMap.put("posDatas",posDatas);
		resultMap.put("lastCacheTime",System.currentTimeMillis());
		//添加到缓存对象
		synchronized (queryCacheMap) {
			queryCacheMap.put(queryCacheStr.toString(),resultMap);
		}
		return resultMap;
	}
	
	/**
	  * 方法名:批量查询数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,Map<String,Object>> multiQuerySpcDataValues(List<QualityFeature> qualityFeatures,
			final String startDateStr,final String endDateStr,
			final JSONObject layerParams,
			final Integer pageNo,
			final Integer pageSize,
			final Integer lastAmount) throws InterruptedException{
		int batchNum = 5;
		int times = qualityFeatures.size()/batchNum;
		if(qualityFeatures.size()%batchNum>0){
			times++;
		}
		final Map<String,Map<String,Object>> dataMap = new HashMap<String,Map<String,Object>>();
		for(int i=0;i<times;i++){
			List<QualityFeature> executeFeatures = new ArrayList<QualityFeature>();
			int start = i*batchNum;
			int end = (i+1) * batchNum-1;
			if(end>=qualityFeatures.size()){
				end = qualityFeatures.size()-1;
			}
			for(int k=start;k<=end;k++){
				executeFeatures.add(qualityFeatures.get(k));
			}
			final Map<String,Boolean> flagMap = new HashMap<String, Boolean>();
			for(QualityFeature qualityFeature : executeFeatures){
				final QualityFeature tempFeature = qualityFeature;
				synchronized (flagMap) {
					flagMap.put(qualityFeature.getId().toString(),true);
				}
				Thread thread = new Thread(new Runnable() {
					public void run() {
						try {
							Map<String,Object> resultMap = querySpcDataValues(tempFeature, startDateStr, endDateStr, layerParams,1,null,null);
							dataMap.put(tempFeature.getId().toString(),resultMap);
						}catch(Throwable e){
							Logger.getLogger(this.getClass()).error("批量查询失败!",e);
						}finally{
							synchronized (flagMap) {
								flagMap.remove(tempFeature.getId().toString());
							}
						}
					}
				});
				thread.start();
			}
			while(true){
				synchronized (flagMap) {
					int size = flagMap.size();
					if(size==0){
						break;
					}
				}
				Thread.sleep(100l);
			}
		}
		return dataMap;
	}
	
	/**
	  * 方法名: 查询最后一部分数据为监控计算
	  * <p>功能说明：</p>
	  * @return
	 */
	public List<JSONObject> queryLastGroupValuesForCache(QualityFeature qualityFeature,Integer groupCount){
		Integer sampleCapacity = qualityFeature.getSampleCapacity();
		Integer effectiveCapacity = qualityFeature.getEffectiveCapacity();
		Query query = getSession().createSQLQuery("select count(*) from " + qualityFeature.getTargetTableName());
		Integer total = Integer.valueOf(query.list().get(0).toString());
		Integer totalGroupCount = total/sampleCapacity;
		//判断是否符合计算样本数,符合时才累加
		if(total%sampleCapacity>=effectiveCapacity){
			totalGroupCount++;
		}
		Integer startGroup = 1;
		if(groupCount>groupCount){
			startGroup = groupCount - groupCount;
		}
		query = getSession().createSQLQuery("select id,inspection_date,data_value from " + qualityFeature.getTargetTableName() + " order by inspection_date");
		query.setFirstResult((startGroup-1)*sampleCapacity);
		List<?> list = query.list();
		List<JSONObject> datas = new ArrayList<JSONObject>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			//缓存坐标数据
			JSONObject data = new JSONObject();
			data.put("id",objs[0]);
			data.put("inspectionDate",DateUtil.formateDateStr((Date)objs[1],"yyyyMMddHHmmssSSS"));//精确到毫秒,方便查询
			data.put("value",Double.valueOf(objs[2].toString()));
			datas.add(data);
		}
		return datas;
	}
	
	private final static Map<String,DecimalFormat> formatMap = new HashMap<String, DecimalFormat>();
	
	/**
	  * 方法名: 格式化数字
	  * <p>功能说明：精度默认为两位</p>
	  * @param value 需格式化的内容
	  * @return
	 */
	public static Double formatValue(Double value){
		return formatValue(value,2);
	}
	
	/**
	  * 方法名: 格式化数字
	  * <p>功能说明：</p>
	  * @param value 需格式化的内容
	  * @param precs 数字精度
	  * @return
	 */
	public static Double formatValue(Double value,Integer precs){
		if(value==null){
			return value;
		}
		if(precs==null){
			precs = 2;
		}
		DecimalFormat df = null;
		synchronized (formatMap) {
			df = formatMap.get(precs+"");
			if(df == null){
				String formatStr = "0.";
				for(int i=0;i<precs;i++){
					formatStr += "#";
				}
				df = new DecimalFormat(formatStr);
				formatMap.put(precs.toString(), df);
			}
		}
		return Double.valueOf(df.format(value));
	}
}
