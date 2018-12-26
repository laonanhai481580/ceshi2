package com.ambition.spc.importutil.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:后台导入工具类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2018-7-15 发布
 */
@Service
public class SpcImportManager {
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	@Autowired
	private SpcMonitorManager spcMonitorManager;
	private Object lockObj = new Object();
	private long lastExecute = 0l;
	private boolean isBackExecute = false;
	private String backFileName;//备份文件地址
//	//默认延迟1分钟执行
//	private long executeJiangge = 1*60*1000;
	//默认延迟1分钟执行
	private long executeJiangge = 60*1000;
	/**
	  * 方法名:后台批量导入,延时2分钟执行
	  * <p>功能说明：缓存值</p>
	  * @return
	 */
	public List<String> backImportValues(Long featureId,String hisId,Double value,Map<String,String> layerMap){
		List<Double> values = new ArrayList<Double>();
		values.add(value);
		List<String> hisIds = null;
		if(StringUtils.isNotEmpty(hisId)){
			hisIds = new ArrayList<String>();
			hisIds.add(hisId);
		}
		return backImportValues(featureId,hisIds,values, layerMap);
	}
	
	private String basePath = null;
	private String getBakFilePath(){
		//不存在时创建文件夹
		if(StringUtils.isEmpty(basePath)){
			String uploadPath = PropUtils.getProp("common.upload.file.path");
			if(!(uploadPath.endsWith("/")||uploadPath.endsWith("\\"))){
				uploadPath += "/";
			}
			basePath = uploadPath + "spctemp";
			File file = new File(basePath);
			if(!file.exists()){
				file.mkdir();
			}
		}
		return basePath;
	}
	
	/**
	  * 方法名:后台批量导入,延时2分钟执行
	  * <p>功能说明：缓存值</p>
	  * @return
	 */
	public List<String> backImportValues(Long featureId,List<String> hisIds,List<Double> values,Map<String,String> layerMap){
		return backImportValues(featureId,new Date(),null,null,hisIds,values, layerMap);
	}
	
	/**
	  * 方法名:后台批量导入,延时2分钟执行
	  * <p>功能说明：缓存值</p>
	  * @return
	 */
	public List<String> backImportValues(Long featureId,Date inspectionDate,String delDataFlag,String newDataFlag,
			List<String> hisIds,List<Double> values,Map<String,String> layerMap){
		List<String> tempIds = new ArrayList<String>();
		synchronized (lockObj) {
			if(StringUtils.isNotEmpty(backFileName)){
				//存在时,检查文件长度是否超过5M,超过时另存为新文件
				String fullName = getBakFilePath() + "/" + backFileName;
				File file = new File(fullName);
				if(file.length()>5*1024*1024*1024){
					backFileName = null;
				}
			}
			if(StringUtils.isEmpty(backFileName)){
				backFileName = DateUtil.formateDateStr(new Date(),"yyyyMMddHHmmssSSS")+".spcbak";
			}
			String fullName = getBakFilePath() + "/" + backFileName;
			
			OutputStream out = null;
			BufferedOutputStream bufferedOutputStream = null;
			try {
				out = new FileOutputStream(fullName,true);
				bufferedOutputStream = new BufferedOutputStream(out);
				JSONObject json = new JSONObject();
				inspectionDate = inspectionDate == null?new Date():inspectionDate;
				for(Double value : values){
					json.clear();
					
					String tempId = UUID.randomUUID().toString();
					json.put("id", tempId);
					
					json.put("inspectionDate",DateUtil.formateDateStr(inspectionDate,"yyyyMMddHHmmssSSS"));
					String userName = ContextUtils.getUserName();
					if(userName==null){
						userName = "system";
					}
					json.put("creatorName",userName);
					json.put("value", value);
					json.put("featureId", featureId);
					if(StringUtils.isNotEmpty(newDataFlag)){
						json.put("dataFlag",newDataFlag);
					}
					json.put("flag","new");
					if(layerMap != null){
						for(String detailCode : layerMap.keySet()){
							String detailValue = layerMap.get(detailCode);
							if(detailValue==null){
								continue;
							}
							json.put(detailCode, detailValue);
						}
					}
					bufferedOutputStream.write((json.toString()+"\r\n").getBytes());
					tempIds.add(tempId);
				}
				if(hisIds != null){
					for(String hisId : hisIds){
						json.clear();
						json.put("featureId", featureId);
						json.put("deleteId", hisId);
						json.put("flag","delete");
						bufferedOutputStream.write((json.toString()+"\r\n").getBytes());
					}
				}
				if(StringUtils.isNotEmpty(delDataFlag)){
					json.clear();
					json.put("delDataFlag", delDataFlag);
					json.put("featureId", featureId);
					json.put("flag","delDataFlag");
					bufferedOutputStream.write((json.toString()+"\r\n").getBytes());
				}
			} catch (Throwable e) {
				throw new AmbFrameException("缓存文件失败!",e);
			}finally{
				if(bufferedOutputStream != null){
					try {
						bufferedOutputStream.close();
					} catch (IOException e) {
						Logger.getLogger(SpcImportManager.class).error("备份SPC数据时关闭流出错!",e);
					}
				}
				if(out != null){
					try {
						out.close();
					} catch (IOException e) {
						Logger.getLogger(SpcImportManager.class).error("备份SPC数据时关闭流出错!",e);
					}
				}
			}
			
			boolean neecBack = false;
			if(!isBackExecute){
				neecBack = true;
			}
			if(neecBack){
				isBackExecute = true;
				startBackService();
			}
		}
		return tempIds;
	}
	/**
	  * 方法名:启动后台服务
	  * <p>功能说明：</p>
	  * @return
	 */
	private void startBackService(){
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Session session = null;
				try {
					while(true){
						long between = System.currentTimeMillis() - lastExecute;
						long sleep = executeJiangge - between;
						if(sleep>0){
							Thread.sleep(sleep);
						}
						Map<String,List<JSONObject>> executeMap = new HashMap<String,List<JSONObject>>();
						Map<String,List<String>> deleteMap = new HashMap<String,List<String>>();
						Map<String,List<String>> deleteFlagMap = new HashMap<String,List<String>>();
						List<File> readFiles = new ArrayList<File>();
						synchronized (lockObj) {
							//当前的备份文件设置为空
							backFileName = null;
							String basePath = getBakFilePath();
							File baseFile = new File(basePath);
							File[] files = baseFile.listFiles();
							long maxExecute = 0;
							for(File spcFile : files){
								String fileName = spcFile.getName();
								if(!fileName.endsWith(".spcbak")){
									continue;
								}
								boolean isFirst = true;
								if(fileName.endsWith("-read.spcbak")){
									isFirst = false;
								}else{
									//重命名,表示已读
									String newFileName = getBakFilePath() + "/" +  fileName.split("\\.")[0] + "-read.spcbak";
									File newFile = new File(newFileName);
									spcFile.renameTo(newFile);
									spcFile = newFile;
								}
								FileReader fileReader = null;
								BufferedReader bufferedReader = null;
								try {
									fileReader = new FileReader(spcFile);
									bufferedReader = new BufferedReader(fileReader);
									String readline = null;
									while((readline=bufferedReader.readLine())!=null){
										if(StringUtils.isEmpty(readline)){
											continue;
										}
										JSONObject json = JSONObject.fromObject(readline);
										String featureId = json.getString("featureId");
										String flag = json.getString("flag");
										if("delete".equals(flag)){
											if(!deleteMap.containsKey(featureId)){
												deleteMap.put(featureId,new ArrayList<String>());
											}
											deleteMap.get(featureId).add(json.getString("deleteId"));
										}else if("delDataFlag".equals(flag)){
											if(!deleteFlagMap.containsKey(featureId)){
												deleteFlagMap.put(featureId,new ArrayList<String>());
											}
											deleteFlagMap.get(featureId).add(json.getString("delDataFlag"));
										}else{
											if(!executeMap.containsKey(featureId)){
												executeMap.put(featureId,new ArrayList<JSONObject>());
											}
											executeMap.get(featureId).add(json);
											
											//不是第一次读时,先删除一遍
											if(!isFirst){
												if(!deleteMap.containsKey(featureId)){
													deleteMap.put(featureId,new ArrayList<String>());
												}
												deleteMap.get(featureId).add(json.getString("id"));
											}
											maxExecute++;
										}
									}
								} catch (Throwable e) {
									throw new AmbFrameException("读取SPC缓存文件失败!",e);
								}finally{
									if(bufferedReader != null){
										try {
											bufferedReader.close();
										} catch (IOException e) {
											Logger.getLogger(SpcImportManager.class).error("读取SPC缓存数据时关闭流出错!",e);
										}
									}
									if(fileReader != null){
										try {
											fileReader.close();
										} catch (IOException e) {
											Logger.getLogger(SpcImportManager.class).error("读取SPC缓存数据时关闭流出错!",e);
										}
									}
								}
								readFiles.add(spcFile);
								//超过10万条时，下次再执行
								if(maxExecute>=100000){
									break;
								}
							}
						}
						session = qualityFeatureDao.getSessionFactory().openSession();
						//根据标记删除
						for(String featureId : deleteFlagMap.keySet()){
							executeDeleteFlags(session,Long.valueOf(featureId), deleteFlagMap.get(featureId));
							//清除监控缓存的数据
							spcMonitorManager.clearMonitorCaches(featureId);
						}
						//删除
						for(String featureId : deleteMap.keySet()){
							executeDelete(session,Long.valueOf(featureId), deleteMap.get(featureId));
							//清除监控缓存的数据
							spcMonitorManager.clearMonitorCaches(featureId);
						}
						deleteMap.clear();
						//导入新数据
						for(String featureId : executeMap.keySet()){
							List<JSONObject> newValues = executeMap.get(featureId);
							executeImport(session,Long.valueOf(featureId),newValues);
							newValues.clear();
						}
						executeMap.clear();
						
						session.close();
						session = null;
						//删除历史文件
						for(File spcFile : readFiles){
							spcFile.delete();
						}
						//检查是否还有文件未处理完成 ,需要继续循环
						String basePath = getBakFilePath();
						File baseFile = new File(basePath);
						File[] files = baseFile.listFiles();
						if(files.length==0){
							break;
						}else{
							//停留5秒再次执行
							Thread.sleep(5000l);
						}
					}
				} catch (Throwable e) {
					Logger.getLogger(SpcImportManager.class).error("后台导入出错",e);
				}finally{
					synchronized (lockObj) {
						isBackExecute = false;
						lastExecute = System.currentTimeMillis();
					}
				}
			}
		});
		thread.start();
	}
	/**
	  * 方法名: 
	  * <p>功能说明：</p>
	  * @return
	 */
	public void executeImport(Session session,Long featureId,final List<JSONObject> valueMaps){
		final QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(featureId,session);
		final StringBuffer insertSql = new StringBuffer("insert into " + qualityFeature.getTargetTableName() + "(id,company_id,created_time,CREATOR_NAME,inspection_date,data_value,his_id,data_flag");
		String valueSql = "?,?,?,?,?,?,?,?";
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
		
		//每次2000条
		final int total = valueMaps.size();
		final int groupCount= 2000;
		int times = total/groupCount;
		if(total%groupCount>0){
			times++;
		}
		long startTimeMillis = System.currentTimeMillis();
		for(int i=0;i<times;i++){
			final int start = i*groupCount;
			final int end = (i+1)*groupCount;
			session.doWork(new Work() {
				@Override
				public void execute(Connection conn) throws SQLException {
					conn.setAutoCommit(false);
					PreparedStatement ps = conn.prepareStatement(insertSql.toString());
					try {
						for(int j=start;j<end&j<total;j++){
							JSONObject valueMap = valueMaps.get(j);
							
							Object inspection_date = null;
							if(valueMap.containsKey("inspectionDate")){
								inspection_date = valueMap.get("inspectionDate");
							}
							if(valueMap.containsKey("inspectionDate")){
								inspection_date = DateUtil.parseDate(valueMap.getString("inspectionDate"),"yyyyMMddHHmmssSSS");
							}
							if(inspection_date==null){
								inspection_date = new Date();
							}
							inspection_date = new Timestamp(((Date)inspection_date).getTime());
							
							Object dataValue = valueMap.get("value");
							
							
//							id,company_id,created_time,CREATOR_NAME,inspection_date,data_value,hisId
							ps.setObject(1,valueMap.get("id"));
							ps.setObject(2,qualityFeature.getCompanyId());
							ps.setObject(3,inspection_date);
							String creatorName = "system";;
							if(valueMap.containsKey("creatorName")){
								creatorName = valueMap.getString("creatorName");
							}
							ps.setObject(4,creatorName);
							ps.setObject(5,inspection_date);
							ps.setObject(6,dataValue);
							Long hisId = null;
							if(valueMap.containsKey("hisId")){
								hisId = valueMap.getLong("hisId");
							}
							ps.setObject(7,hisId);
							
							String dataFlag = null;
							if(valueMap.containsKey("dataFlag")){
								dataFlag = valueMap.getString("dataFlag");
							}
							ps.setObject(8,dataFlag);
							
							//层别信息
							for(int k=0;k<layerFields.size();k++){
								String layerCode = layerFields.get(k);
								String layerValue = null;
								if(valueMap.containsKey(layerCode)){
									layerValue = valueMap.getString(layerCode);
								}
								ps.setObject(9+k,layerValue);
							}
							ps.addBatch();
						}
						ps.executeBatch();
						conn.commit();
						ps.clearBatch();
					} finally{
						if(ps != null){
							ps.close();
						}
					}
				}
			});
		}
		//添加到监控数据
		spcMonitorManager.addMonitors(valueMaps);
		
		System.out.println(Thread.currentThread().getId() + ":insert:" + featureId + ",times:" + (System.currentTimeMillis() - startTimeMillis)/1000 + ",import success:" + total);
	}
	
	/**
	  * 方法名:执行删除操作
	  * <p>功能说明：</p>
	  * @return
	 */
	public void executeDelete(Session session,Long featureId,final List<String> deleteIds){
		final QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(featureId,session);
		final StringBuffer deleteSql = new StringBuffer("delete from " + qualityFeature.getTargetTableName() + " where id = ?");
		
		//每次2000条
		int total = deleteIds.size();
		final int groupCount= 2000;
		int times = total/groupCount;
		if(deleteIds.size()%groupCount>0){
			times++;
		}
		long startTimeMillis = System.currentTimeMillis();
		for(int i=0;i<times;i++){
			session.doWork(new Work() {
				@Override
				public void execute(Connection conn) throws SQLException {
					conn.setAutoCommit(false);
					PreparedStatement ps = conn.prepareStatement(deleteSql.toString());
					try {
						for(int j=0;j<groupCount&&j<deleteIds.size();j++){
							String deleteId = deleteIds.get(j);
							ps.setObject(1,deleteId);
							ps.addBatch();
						}
						ps.executeBatch();
						conn.commit();
						ps.clearBatch();
						
						//移除执行完成的数据
						for(int j=0;j<groupCount&&deleteIds.size()>0;j++){
							deleteIds.remove(0);
						}
					} finally{
						if(ps != null){
							ps.close();
						}
					}
				}
			});
		}
		System.out.println(Thread.currentThread().getId() + ":delete:" + featureId + ",times:" + (System.currentTimeMillis() - startTimeMillis)/1000 + ",import success:" + total);
	}
	
	/**
	  * 方法名:执行根据标记删除操作
	  * <p>功能说明：</p>
	  * @return
	 */
	public void executeDeleteFlags(Session session,Long featureId,final List<String> deleteFlags){
		final QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(featureId,session);
		final StringBuffer deleteSql = new StringBuffer("delete from " + qualityFeature.getTargetTableName() + " where data_flag = ?");
		
		//每次2000条
		int total = deleteFlags.size();
		final int groupCount= 2000;
		int times = total/groupCount;
		if(deleteFlags.size()%groupCount>0){
			times++;
		}
		long startTimeMillis = System.currentTimeMillis();
		for(int i=0;i<times;i++){
			session.doWork(new Work() {
				@Override
				public void execute(Connection conn) throws SQLException {
					conn.setAutoCommit(false);
					PreparedStatement ps = conn.prepareStatement(deleteSql.toString());
					try {
						for(int j=0;j<groupCount&&j<deleteFlags.size();j++){
							String deleteFlag = deleteFlags.get(j);
							ps.setObject(1,deleteFlag);
							ps.addBatch();
						}
						ps.executeBatch();
						conn.commit();
						ps.clearBatch();
						
						//移除执行完成的数据
						for(int j=0;j<groupCount&&deleteFlags.size()>0;j++){
							deleteFlags.remove(0);
						}
					} finally{
						if(ps != null){
							ps.close();
						}
					}
				}
			});
		}
		System.out.println(Thread.currentThread().getId() + ":executeDeleteFlags:" + featureId + ",times:" + (System.currentTimeMillis() - startTimeMillis)/1000 + ",import success:" + total);
	}
}
