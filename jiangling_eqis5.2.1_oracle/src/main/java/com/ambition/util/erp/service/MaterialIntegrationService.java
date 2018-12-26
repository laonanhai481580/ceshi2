package com.ambition.util.erp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:物料与ERP整合的服务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：处理QIS与ERP系统物料的整合</p>
 * @author  赵骏
 * @version 1.00 2013-8-19 发布
 */
@Service
public class MaterialIntegrationService implements IntegrationService,Runnable {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProductBomDao bomDao;
	@Resource(name="ordbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public void run() {
		try{
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("自动更新物料失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getBomScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 导入物料
	 */
	@SuppressWarnings("unchecked")
	public void beginIntegration(Long companyId,String userLoginName){
		if(integrationFlag){
			return;
		}
		integrationFlag = true;
		Session session = null;
		Transaction transaction = null;
		//导入
		try {
			//生成销售数据
			String tableName = "bom";
			session = bomDao.getSessionFactory().openSession();
			
			Query query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp = query.list();
			Date updateTime = null;
			Date saveDate = null;
			if(!updateStamp.isEmpty()){
				updateTime = updateStamp.get(0).getUpdateTime();
				saveDate = updateTime;
//				//提前一周
//				Calendar calendar = Calendar.getInstance();
//				calendar.setTime(updateTime);
//				calendar.add(Calendar.DATE,-3);
//				updateTime = calendar.getTime();
			}else{
				updateTime = DateUtil.parseDate("2017-01-01");
				saveDate = updateTime;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String companyName =PropUtils.getProp("companyName");
			List<Object> search = new ArrayList<Object>();
			//从接口查询符合条件的销售数据
			String sql = "SELECT SEGMENT1 as F_CODE,description as F_NAME,PRIMARY_UOM_CODE as F_UNIT,cate_desc as F_ITEM_TYPE ,cate_code as F_ITEM_TYPE_CODE,organization_id as F_BUSINESS_UNIT_ID,organization_name as F_business_unit_name ,organization_code as F_organization_code," 
						+ "LAST_UPDATE_DATE as LAST_UPDATE_DATE FROM apps.XXQIS_ITEM_V where LAST_UPDATE_DATE>=? ";
			search.add(saveDate);
			if("CCM".equals(companyName)){
				sql += " and (organization_code=? or organization_code=? or organization_code=?) order by LAST_UPDATE_DATE asc";
				search.add("216");
				search.add("297");
				search.add("228");
			}else if("SQ".equals(companyName)){
				sql += " and organization_code=?  order by LAST_UPDATE_DATE asc";
				search.add("282");
			}else if("FPM".equals(companyName)){
				sql += " and (organization_code=? or organization_code=? or organization_code=? or organization_code=? or organization_code=? or organization_code=? or organization_code=? )  order by LAST_UPDATE_DATE asc";
				search.add("296");
				search.add("211");
				search.add("298");
				search.add("209");
				search.add("581");
				search.add("582");
				search.add("299");
			}
			List<?> bomList = jdbcTemplate.queryForList(sql,search.toArray());
			//List<?> bomList = jdbcTemplate.queryForList(sql);
			for(Object obj : bomList){
				//System.out.println(formatter.format(updateTime)+"到"+formatter.format(new Date())+":"+record+"/"+rsize+"----------------");
				Map<String,Object> map = (Map<String,Object>)obj;
				String code = (String)map.get("F_CODE");//物料编号
				String name = (String)map.get("F_NAME");//物料说明
				String unit =(String)map.get("F_UNIT");//单位
				String itemType = (String)map.get("F_ITEM_TYPE");//物料类别
				String materialTypeCode = (String)map.get("F_ITEM_TYPE_CODE");//物料类别代码
				Long businessUnitId = Long.valueOf(map.get("F_BUSINESS_UNIT_ID").toString());//组织结构id
				String businessUnitName = (String)map.get("F_business_unit_name");//组织名称(事业部)
				String businessUnitCode = (String)map.get("F_organization_code");//组织名称(事业部)
				ProductBom productBom = null;
				
				String existHql = "from ProductBom s where s.materielCode=? ";
				List<?> list = session.createQuery(existHql)
				.setParameter(0,code)
				.list();
				if(list.isEmpty()){
					productBom = new ProductBom();
					productBom.setCreatedTime(new Date());
					productBom.setCreator(userLoginName);
					productBom.setMaterielCode(code);
					productBom.setMaterielName(name);
					productBom.setSubCompanyId(companyId);
					productBom.setCompanyId(companyId);
				}else{
					productBom = (ProductBom)list.get(0);
				}
				productBom.setModifiedTime(new Date());
				productBom.setModifier(userLoginName);
				productBom.setMaterielName(name);
				productBom.setMaterialType(itemType);
				productBom.setUnits(unit);
				productBom.setBusinessUnitId(businessUnitId);
				productBom.setBusinessUnitName(businessUnitName);
				productBom.setBusinessUnitCode(businessUnitCode);
				productBom.setLastUpdateDate((Date)map.get("LAST_UPDATE_DATE"));
			    productBom.setMaterialTypeCode(materialTypeCode);
				//每次一个事务避免长时间锁住这张表影响前台的查询显示
				transaction = session.beginTransaction();
				session.save(productBom);
				transaction.commit();
			    session.flush();
	            session.clear();
	            Date lastUpdateDate = productBom.getLastUpdateDate();
				if(lastUpdateDate != null 
						&& lastUpdateDate.getTime()>saveDate.getTime()){
					saveDate = lastUpdateDate;
				}
			}
			query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			updateStamp = query.list();
			UpdateTimestamp updateTimestamp = null;
			if(updateStamp.isEmpty()){
				updateTimestamp = new UpdateTimestamp();
				updateTimestamp.setTableName(tableName);
				updateTimestamp.setCompanyId(companyId);
				updateTimestamp.setCreatedTime(new Date());
				updateTimestamp.setCreator(userLoginName);
			}else{
				updateTimestamp = (UpdateTimestamp)updateStamp.get(0);
			}
			updateTimestamp.setLastModifier(userLoginName);
			updateTimestamp.setLastModifiedTime(new Date());
			updateTimestamp.setUpdateTime(saveDate);
			//保存变更信息
			transaction = session.beginTransaction();
			session.save(updateTimestamp);
			transaction.commit();
			session.flush();
			session.clear();
		}catch (Exception e) {
			//回滚事务
			if(transaction != null){
				transaction.rollback();
			}
			throw new AmbFrameException("获取物料信息数据失败!",e);
		}finally{
			integrationFlag = false;
			if(session != null){
				session.close();
			}
		}
	}
	private volatile boolean integrationFlag = false;
	@Override
	public boolean isIntegration() {
		return integrationFlag;
	}
	public int getProgressbar() {
		// TODO Auto-generated method stub
		return 0;
	}

}
