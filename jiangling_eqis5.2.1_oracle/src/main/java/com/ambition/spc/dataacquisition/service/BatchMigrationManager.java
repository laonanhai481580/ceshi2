package com.ambition.spc.dataacquisition.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.importutil.service.SpcImportManager;
import com.ambition.spc.processdefine.service.FeatureTableColumnManager;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;

/**
 * 类名:批量迁移旧数据的拆分表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2018-7-15 发布
 */
@Service
public class BatchMigrationManager {
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;
	@Autowired
	private FeatureTableColumnManager tableColumnManager;
	@Autowired
	private SpcImportManager importManager;
	private Object lockObj = new Object();
	private boolean isDoing = false;
	private Integer needDo=0;
	private String currentName;
	private Long currentCount=0l;
	private Long totalCount=0l;
	private String errorMessage=null;
	private String executeUUID=null;
	public String beginMigration(String groupIds){
		synchronized (lockObj) {
			if(isDoing){
				String message = null;
				if(needDo>0){
					message = "正在准备迁移数据!";
				}else{
					message = "正在迁移数据,计划迁移【"+needDo+"】个特性数据,当前迁移特性为【"+currentName+"】,总计【"+totalCount+"】个数据需要迁移,当前正在第【"+currentCount+"】个数据!";
				}
				throw new AmbFrameException(message);
			}
			isDoing = true;
			errorMessage = null;
			executeUUID = UUID.randomUUID().toString();
		}
		try {
			final List<Long> qualityFeatureIds = new ArrayList<Long>();
			if(StringUtils.isNotEmpty(groupIds)){
				for(String groupId : groupIds.split(",")){
					qualityFeatureIds.add(Long.valueOf(groupId));
				}
			}else{
				String hql = "select q.id from QualityFeature q";
				List<?> list = spcSubGroupDao.createQuery(hql).list();
				for(Object obj : list){
					qualityFeatureIds.add(Long.valueOf(obj.toString()));
				}
			}
			needDo = qualityFeatureIds.size();
			//后台自动运行
			Thread thread = new Thread(new Runnable() {
				public void run() {
					Session session = spcSubGroupDao.getSessionFactory().openSession();
					Transaction transaction = null;
					try {
						for(Long featureId : qualityFeatureIds){
							QualityFeature qualityFeature = (QualityFeature)session.get(QualityFeature.class,featureId);
							if(StringUtils.isEmpty(qualityFeature.getTargetTableName())){
								transaction = session.beginTransaction();
								//自动建表
								String tableName = tableColumnManager.generateTable(qualityFeature,session);
								qualityFeature.setTargetTableName(tableName);
								session.save(qualityFeature);
								transaction.commit();
							}
							currentName = qualityFeature.getName();
							currentCount=0l;
							totalCount=0l;
							
							String targetTableName = qualityFeature.getTargetTableName();
							//查询最大的历史数据ID
							String sql = "select max(HIS_ID) from " + targetTableName;
							List<?> list = session.createSQLQuery(sql).list();
							Long maxHisId = 0l;
							if(list.size()>0&&list.get(0)!=null){
								maxHisId = Long.valueOf(list.get(0).toString());
							}
							final StringBuffer insertSql = new StringBuffer("insert into " + targetTableName + "(id,his_id,company_id,created_time,CREATOR_NAME,inspection_date,data_value");
							String valueSql = "?,?,?,?,?,?,?";
							final List<String> layerFields = new ArrayList<String>();
							for(FeatureLayer layer : qualityFeature.getFeatureLayers()){
								if(layerFields.contains(layer.getDetailCode())){
									continue;
								}
								insertSql.append("," + layer.getDetailCode());
								valueSql += ",?";
								layerFields.add(layer.getDetailCode());
							}
							insertSql.append(")" + " values(" + valueSql + ")");
							//从原表中查询
							String selectSql = "select s.id,s.created_Time,s.sam_Value ";
							if(layerFields.size()>0){
								for(String layerField : layerFields){
									selectSql += ",g." + layerField;
								}
								
							}
							selectSql += " from SPC_SG_SAMPLE s left join SPC_SUB_GROUP g on s.FK_SUB_GROUP_ID = g.id";
							selectSql += "  where s.id > ? and g.FK_QUALITY_FEATURE_ID = ? order by s.id";
							
							final Map<String,Long> statusMap = new HashMap<String, Long>();
							statusMap.put("maxHisId",maxHisId);
							while(true){
								Query query = session.createSQLQuery(selectSql)
										.setParameter(0,statusMap.get("maxHisId"))
										.setParameter(1,qualityFeature.getId());
								query.setMaxResults(10000);//一次处理一万条
								
								final List<?> values = query.list();
								if(values.isEmpty()){
									break;
								}
								totalCount+=values.size();
								List<JSONObject> valueMaps = new ArrayList<JSONObject>();
								Long tempMaxId = null;
								for(int j=0;j<values.size();j++){
									Object[] objs = (Object[])values.get(j);
									tempMaxId = Long.valueOf(objs[0].toString());
									Object id = tempMaxId;
									Object inspection_date = objs[1];
									Object dataValue = objs[2];
									
									JSONObject valueMap = new JSONObject();
									valueMap.put("id", UUID.randomUUID().toString());
									if(inspection_date != null){
										valueMap.put("inspectionDate",DateUtil.formateDateStr((Date)inspection_date,"yyyyMMddHHmmssSSS"));
									}
									valueMap.put("hisId",id);
									valueMap.put("value",dataValue);
									
									//层别信息
									for(int k=0;k<layerFields.size();k++){
										Object layerValue = objs[3+k];
										if(layerValue==null){
											continue;
										}
										valueMap.put(layerFields.get(k),layerValue);
									}
									valueMaps.add(valueMap);
									currentCount++;
								}
								
								importManager.executeImport(session, featureId, valueMaps);
								
								statusMap.put("maxHisId",tempMaxId);
							}
							//迁移标志
							transaction = session.beginTransaction();
							qualityFeature.setIsMigration("是");
							session.save(qualityFeature);
							transaction.commit();
						}
					}catch (Exception e) {
						if(transaction != null && transaction.isActive()){
							transaction.rollback();
						}
						Logger.getLogger(BatchMigrationManager.class).error("批量迁移数据失败!",e);
						
						errorMessage = e.getMessage();
					} finally{
						synchronized (lockObj) {
							isDoing = false;
						}
						if(session != null){
							session.close();
						}
					}
				}
			});
			thread.start();
			return executeUUID;
		} catch (Throwable e) {
			synchronized (lockObj) {
				isDoing = false;
			}
			throw new AmbFrameException("批量转移失败!",e);
		}
	}
	
	/**
	  * 方法名:查询执行状态 
	  * <p>功能说明：</p>
	  * @return
	 */
	public JSONObject queryCurrentStatus(String executeFlag){
		JSONObject result = new JSONObject();
		synchronized (lockObj) {
			if(executeFlag.equalsIgnoreCase(executeUUID)){
				if(StringUtils.isNotEmpty(errorMessage)){
					result.put("error",true);
					result.put("message",errorMessage);
				}else{
					String message = "正在迁移数据,计划迁移【"+needDo+"】个特性数据,当前迁移特性为【"+currentName+"】,总计【"+totalCount+"】个数据需要迁移,当前正在第【"+currentCount+"】个数据!";
					if(!isDoing){
						message = "执行完成,迁移了【"+needDo+"】个特性数据";
						result.put("complete",true);
					}
					result.put("message",message);
				}
			}else{
				result.put("message","执行完成!");
			}
		}
		return result;
	}
}
