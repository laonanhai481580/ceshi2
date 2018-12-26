package com.ambition.spc.processdefine.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Settings;
import org.hibernate.impl.SessionImpl;
import org.springframework.stereotype.Service;

import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.FeatureTableColumn;
import com.ambition.spc.entity.QualityFeature;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:质量特性表字段管理
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2018-7-12 发布
 */
@Service
public class FeatureTableColumnManager {
//	@Autowired
//	private FeatureTableColumnDao tableColumnDao;
	
	/**
	  * 方法名:建表语句
	  * <p>功能说明：</p>
	  * @return
	 * @throws SQLException 
	 * @throws HibernateException 
	 */
	public String generateTable(QualityFeature qualityFeature,Session session) throws HibernateException, SQLException{
		Settings settings = ((SessionImpl)session).getFactory().getSettings();
		String schemaName = settings.getDefaultSchemaName();
		String dialect = settings.getDialect().toString();
		if(dialect.indexOf(".Oracle")>-1){
			return generateTableForOracle(qualityFeature,session);
		}else if(dialect.indexOf(".SQLServer")>-1){
			return generateTableForSQLServer(qualityFeature,session);
		}else{
			return null;
		}
	}
//	String sql = "select "+field+" from sysobjects where type = ?";
//	if(ChartDatabaseSetting.TYPE_MYSQL.equals(setting.getType())){
//		sql = "select table_name as NAME from INFORMATION_SCHEMA.TABLES where table_schema = ?";
//		field = "table_name";
//		searchParams.add(setting.getDatabaseName());
//	}if(ChartDatabaseSetting.TYPE_ORACLE.equals(setting.getType())){
//		sql = "select table_name as NAME from user_tables where 1=1 ";
//		field = "table_name";
//	}else{
//		searchParams.add("u");
//	}
	/**
	  * 方法名:获取SQLServer的表
	  * <p>功能说明：</p>
	  * @return
	 */
	private String generateTableForSQLServer(QualityFeature qualityFeature,Session session){
		//查询已存在的表
		String tableName = qualityFeature.getTargetTableName();
		if(StringUtils.isEmpty(tableName)){
			tableName = "spc_data_" + qualityFeature.getId();
		}
		tableName = tableName.toUpperCase();
		String sql = "select a.name columnname from sys.columns a,sys.objects b,sys.types c"
				+ " where a.object_id=b.object_id and b.name=? and a.system_type_id=c.system_type_id order by a.column_id";
		List<?> list = session.createSQLQuery(sql).addScalar("columnname", Hibernate.STRING).setParameter(0,tableName).list();
		Map<String,Boolean> existColumnMap = new HashMap<String,Boolean>();
		for(Object obj : list){
			existColumnMap.put(obj.toString().toUpperCase(),true);
		}
		//为空时建新表,创建默认字段
		if(existColumnMap.isEmpty()){
			final StringBuffer createTableSql = new StringBuffer("create table "+tableName);
			createTableSql.append(" (");
			createTableSql.append("id VARCHAR(100) PRIMARY KEY,");//主键，UUID自增
			createTableSql.append("company_id numeric(19, 0),");
			createTableSql.append("quality_feature_id numeric(19, 0),");//质量特性ID
			createTableSql.append("his_id numeric(19, 0),");//历史数据ID
			createTableSql.append("data_flag VARCHAR(255),");//历史数据标记,后续根据此字段批量更新和删除
			createTableSql.append("CREATED_TIME datetime,");
			createTableSql.append("CREATOR VARCHAR(100),");
			createTableSql.append("CREATOR_NAME VARCHAR(255),");
			createTableSql.append("MODIFIED_TIME datetime,");
			createTableSql.append("MODIFIER VARCHAR(100),");
			createTableSql.append("MODIFIER_NAME VARCHAR(255),");
			createTableSql.append("INSPECTION_DATE datetime,");//采集时间
			createTableSql.append("DATA_VALUE float");
			createTableSql.append(")");
			session.createSQLQuery(createTableSql.toString()).executeUpdate();
			
			//保存默认的表字段
			addDefaultColumns(qualityFeature,session);
		}
		//删除非默认的映射字段
		String delHql = "delete from FeatureTableColumn where isDefault = ? and qualityFeature = ?";
		session.createQuery(delHql).setParameter(0,false).setParameter(1,qualityFeature).executeUpdate();
		if(qualityFeature.getFeatureLayers() != null){
			Map<String,Boolean> layerExistMap = new HashMap<String, Boolean>();
			for(FeatureLayer featureLayer : qualityFeature.getFeatureLayers()){
				//添加表字段
				String columnName = featureLayer.getDetailCode().toUpperCase();
				if(layerExistMap.containsKey(columnName)){
					continue;
				}
				layerExistMap.put(columnName, true);
				if(!existColumnMap.containsKey(columnName)){
					session.createSQLQuery("alter table "+tableName+" add "+columnName+" VARCHAR(255)").executeUpdate();
					existColumnMap.put(columnName,true);
				}
				addTableColumn(session,qualityFeature,columnName,Hibernate.STRING.getName(),false);
			}
		}
		return tableName;
	}
	/**
	  * 方法名:获取oracle的表
	  * <p>功能说明：</p>
	  * @return
	 */
	private String generateTableForOracle(QualityFeature qualityFeature,Session session){
		//查询已存在的表
		String tableName = qualityFeature.getTargetTableName();
		if(StringUtils.isEmpty(tableName)){
			tableName = "spc_data_" + qualityFeature.getId();
		}
		tableName = tableName.toUpperCase();
		String sql = "select column_name from user_tab_columns where table_name = ?";
		List<?> list = session.createSQLQuery(sql).setParameter(0,tableName).list();
		Map<String,Boolean> existColumnMap = new HashMap<String,Boolean>();
		for(Object obj : list){
			existColumnMap.put(obj.toString().toUpperCase(),true);
		}
		//为空时建新表,创建默认字段
		if(existColumnMap.isEmpty()){
			final StringBuffer createTableSql = new StringBuffer("create table "+tableName);
			createTableSql.append(" (");
			createTableSql.append("id varchar2(100) PRIMARY KEY,");//主键，UUID自增
			createTableSql.append("company_id NUMBER(19),");
			createTableSql.append("quality_feature_id NUMBER(19),");//质量特性ID
			createTableSql.append("his_id NUMBER(19),");//历史数据ID
			createTableSql.append("data_flag VARCHAR2(255 CHAR),");//历史数据标记,后续根据此字段批量更新和删除
			createTableSql.append("CREATED_TIME TIMESTAMP(6),");
			createTableSql.append("CREATOR VARCHAR2(100 CHAR),");
			createTableSql.append("CREATOR_NAME VARCHAR2(255 CHAR),");
			createTableSql.append("MODIFIED_TIME TIMESTAMP(6),");
			createTableSql.append("MODIFIER VARCHAR2(100 CHAR),");
			createTableSql.append("MODIFIER_NAME VARCHAR2(255 CHAR),");
			createTableSql.append("INSPECTION_DATE TIMESTAMP(6),");//采集时间
			createTableSql.append("DATA_VALUE float");
			createTableSql.append(")");
			session.createSQLQuery(createTableSql.toString()).executeUpdate();
			
			//保存默认的表字段
			addDefaultColumns(qualityFeature,session);
		}
		
		//删除非默认的映射字段
		String delHql = "delete from FeatureTableColumn where isDefault = ? and qualityFeature = ?";
		session.createQuery(delHql).setParameter(0,false).setParameter(1,qualityFeature).executeUpdate();
		if(qualityFeature.getFeatureLayers() != null){
			Map<String,Boolean> layerExistMap = new HashMap<String, Boolean>();
			for(FeatureLayer featureLayer : qualityFeature.getFeatureLayers()){
				//添加表字段
				String columnName = featureLayer.getDetailCode().toUpperCase();
				if(layerExistMap.containsKey(columnName)){
					continue;
				}
				layerExistMap.put(columnName, true);
				if(!existColumnMap.containsKey(columnName)){
					session.createSQLQuery("alter table "+tableName+" add "+columnName+" VARCHAR2(255)").executeUpdate();
				}
				addTableColumn(session,qualityFeature,columnName,Hibernate.STRING.getName(),false);
			}
		}
		return tableName;
	}
	
	/**
	  * 方法名:添加默认字段值
	  * <p>功能说明：</p>
	  * @return
	 */
	private void addDefaultColumns(QualityFeature feature,Session session){
		addTableColumn(session,feature,"id",Hibernate.STRING.getName(),true);
		addTableColumn(session,feature,"company_id",Hibernate.LONG.getName(),true);
		addTableColumn(session,feature,"quality_feature_id",Hibernate.LONG.getName(),true);
		addTableColumn(session,feature,"his_id",Hibernate.LONG.getName(),true);
		addTableColumn(session,feature,"CREATED_TIME",Hibernate.TIMESTAMP.getName(),true);
		addTableColumn(session,feature,"CREATOR",Hibernate.STRING.getName(),true);
		addTableColumn(session,feature,"CREATOR_NAME",Hibernate.STRING.getName(),true);
		addTableColumn(session,feature,"MODIFIED_TIME",Hibernate.TIMESTAMP.getName(),true);
		addTableColumn(session,feature,"MODIFIER",Hibernate.STRING.getName(),true);
		addTableColumn(session,feature,"MODIFIER_NAME",Hibernate.STRING.getName(),true);
		addTableColumn(session,feature,"INSPECTION_DATE",Hibernate.TIMESTAMP.getName(),true);
		addTableColumn(session,feature,"DATA_VALUE",Hibernate.FLOAT.getName(),true);
	}
	
	private void addTableColumn(Session session,QualityFeature feature,String columnName,String columnType,Boolean isDefault){
		FeatureTableColumn tableColumn = new FeatureTableColumn();
		tableColumn.setQualityFeature(feature);
		tableColumn.setCompanyId(ContextUtils.getCompanyId());
		tableColumn.setCreatedTime(new Date());
		tableColumn.setCreator(ContextUtils.getLoginName());
		tableColumn.setCreatorName(ContextUtils.getUserName());
		tableColumn.setColumnName(columnName.toUpperCase());
		tableColumn.setColumnType(columnType);
		tableColumn.setIsDefault(isDefault);
		session.save(tableColumn);
	}
}
