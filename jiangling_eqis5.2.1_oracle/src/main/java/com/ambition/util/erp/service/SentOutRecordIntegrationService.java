package com.ambition.util.erp.service;

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
import org.springframework.stereotype.Service;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.SentOutRecord;
import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:发料记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年11月14日 发布
 */
@Service
public class SentOutRecordIntegrationService implements IntegrationService,Runnable{

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
		.addSchedule(this,ScheduleJob.getSentOutRecordScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 导入发料记录
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
			//生成发料记录
			String tableName = "sentOutRecord";
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
				updateTime = DateUtil.parseDate("2016-10-1");
				saveDate = updateTime;
			}
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String companyName = ContextUtils.getCompanyName();
			List<Object> search = new ArrayList<Object>();
			//从接口查询符合条件的销售数据
			String sql = "SELECT transaction_id,organization_id,ORGANIZATION_CODE,transaction_date,inventory_item_id,item_number,item_description,uom,primary_quantity,subinventory_code,transaction_type_id" 
						+ ",trx_type_name,cateogry_code,category_description,organization_name FROM  apps.xxqis_wip_trx_v where transaction_date>? ";
			
			if("欧菲科技-CCM".equals(companyName)){
				sql += " and (ORGANIZATION_CODE=? or ORGANIZATION_CODE=?) order by transaction_date asc";
				search.add(updateTime);
				search.add("216");
				search.add("297");
			}else if("欧菲科技-神奇工场".equals(companyName)){
				sql += " and ORGANIZATION_CODE=?  order by transaction_date asc";
				search.add(updateTime);
				search.add("282");
			}else if("欧菲科技-FPM".equals(companyName)){
				sql += " and ORGANIZATION_CODE=?  order by transaction_date asc";
				search.add(updateTime);
				search.add("296");
			}
			List<?> bomList = jdbcTemplate.queryForList(sql,search.toArray());
			//List<?> bomList = jdbcTemplate.queryForList(sql);
			int record = 0;
			int inewRecord=0;
			int rsize=bomList.size();
			for(Object obj : bomList){
				//System.out.println(formatter.format(updateTime)+"到"+formatter.format(new Date())+":"+record+"/"+rsize+"----------------");
				Map<String,Object> map = (Map<String,Object>)obj;
				Long transactionId = Long.valueOf(map.get("transaction_id").toString());//交易记录 id 
				Long organizationId =  Long.valueOf(map.get("organization_id").toString());//组织 id
				String businessUnitCode =(String)map.get("ORGANIZATION_CODE");//组织编码
				Date transactionDate = (Date)map.get("transaction_date");//交易日期
				Long itemId = Long.valueOf(map.get("inventory_item_id").toString());//物料 id
				String itemNumber = (String)map.get("item_number");//物料编号
				String itemDescription = (String)map.get("item_description");//物料描述
				String units = (String)map.get("uom");//单位
				Double primaryQuantity = Double.valueOf(map.get("primary_quantity").toString());//事务处理数量
				String subinventoryCode = (String)map.get("subinventory_code");//交易子库
				Long transactionTypeId = Long.valueOf(map.get("transaction_type_id").toString());//交易类型id
				String trxTypeName = (String)map.get("trx_type_name");//交易类型名称
				String cateogryCode = (String)map.get("cateogry_code");//物料分类代码
				String categoryDescription = (String)map.get("category_description");//物料分类描述
				String businessUnitName = (String)map.get("organization_name");
				SentOutRecord sentOutRecord = null;
				
				String existHql = "from SentOutRecord s where s.transactionId=? and s.organizationId=?";
				List<?> list = session.createQuery(existHql)
				.setParameter(0,transactionId)
				.setParameter(1,organizationId)
				.list();
				if(list.isEmpty()){
					inewRecord++;
					sentOutRecord = new SentOutRecord();
					sentOutRecord.setCreatedTime(new Date());
					sentOutRecord.setCreator(userLoginName);
					sentOutRecord.setTransactionId(transactionId);
					sentOutRecord.setSubCompanyId(organizationId);
					sentOutRecord.setBusinessUnitName(businessUnitName);
					sentOutRecord.setBusinessUnitCode(businessUnitCode);
					sentOutRecord.setOrganizationId(organizationId);
					sentOutRecord.setCompanyId(companyId);
					sentOutRecord.setUom(units);
					sentOutRecord.setTransactionDate(transactionDate);
					sentOutRecord.setItemId(itemId);
					sentOutRecord.setItemNumber(itemNumber);
					sentOutRecord.setItemDescription(itemDescription);
					sentOutRecord.setPrimaryQuantity(primaryQuantity);
					sentOutRecord.setSubinventoryCode(subinventoryCode);
					sentOutRecord.setTransactionTypeId(transactionTypeId);
					sentOutRecord.setTrxTypeName(trxTypeName);
					sentOutRecord.setCateogryCode(cateogryCode);
					sentOutRecord.setCategoryDescription(categoryDescription);
					sentOutRecord.setCategoryDescription(categoryDescription);
				}else{
					sentOutRecord = (SentOutRecord) list.get(0);
					sentOutRecord.setUom(units);
					sentOutRecord.setTransactionDate(transactionDate);
					sentOutRecord.setItemId(itemId);
					sentOutRecord.setItemNumber(itemNumber);
					sentOutRecord.setItemDescription(itemDescription);
					sentOutRecord.setPrimaryQuantity(primaryQuantity);
					sentOutRecord.setSubinventoryCode(subinventoryCode);
					sentOutRecord.setTransactionTypeId(transactionTypeId);
					sentOutRecord.setTrxTypeName(trxTypeName);
					sentOutRecord.setCateogryCode(cateogryCode);
					sentOutRecord.setCategoryDescription(categoryDescription);
					sentOutRecord.setCategoryDescription(categoryDescription);
				}
				
				//每次一个事务避免长时间锁住这张表影响前台的查询显示
				transaction = session.beginTransaction();
				session.save(sentOutRecord);
				transaction.commit();
			    session.flush();
	            session.clear();
	            Date lastUpdateDate = transactionDate;
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
				updateTimestamp.setCreator(userLoginName);
				updateTimestamp.setCreatedTime(new Date());
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
			e.printStackTrace();
			integrationFlag = false;
			//回滚事务
			if(transaction != null&&transaction.isActive()){
				transaction.rollback();
			}
			throw new AmbFrameException("获取发料数据失败!",e);
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
