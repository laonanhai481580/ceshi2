package com.ambition.util.erp.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.supplier.entity.SupplierMaterialEvaluate;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.PropUtils;
@Service
public class SupplierMaterialEvaluateIntegerServices implements IntegrationService,Runnable{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private ProductBomDao bomDao;       
	@Resource(name="ordbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public void run() {
		try{
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("自动更新赠品仓数据失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getSupplierMaterialEvaluateScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
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
		System.out.println("begin");
		try {
			String tableName = "supplierMaterialEvaluate";
			session = bomDao.getSessionFactory().openSession();
			Query query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp = query.list();
			Date updateTime = null;
			Date saveDate = null;
			if(!updateStamp.isEmpty()){
				updateTime = updateStamp.get(0).getUpdateTime();
				saveDate = updateTime;
			}else{
				updateTime = DateUtil.parseDate("2017-04-20");
				saveDate = updateTime;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String companyName =PropUtils.getProp("companyName");
			List<Object> search = new ArrayList<Object>();
			//从接口查询符合条件的销售数据
			String sql = "SELECT transaction_id,TRANS_NUMBER,item,description,primary_uom_code,primary_quantity,transaction_date,trans_type_id,"
					+ "organization_id,ORGANIZATION_CODE,ORGANIZATION_NAME,subinventory_code,sub_desc,header_desc,vendor_id,vendor_name,vendor_code FROM apps.xxqis_gift_warehouse_mtt_v where transaction_date >? ";
			search.add(saveDate);
			if("CCM".equals(companyName)){
				sql += " and (ORGANIZATION_CODe=? or ORGANIZATION_CODE=? or ORGANIZATION_CODE=? ) order by transaction_date asc";
				search.add("216");
				search.add("297");
				search.add("228");
			}else if("SQ".equals(companyName)){
				sql += " and ORGANIZATION_CODE=?  order by transaction_date asc";
				search.add("282");
			}else if("FPM".equals(companyName)){
				sql += " and ORGANIZATION_CODE=?  order by transaction_date asc";
				search.add("296");
			}
			List<?> bomList = jdbcTemplate.queryForList(sql,search.toArray());
			for(Object obj : bomList){
				Map<String,Object> map = (Map<String,Object>)obj;
				Long transactionId = Long.valueOf(map.get("transaction_id").toString());//事物处理id
				String materialCode = (String)map.get("item");//物料编码
				String materialName = (String)map.get("description");//物料说明			
				Double materialAmount = Double.valueOf(map.get("primary_quantity").toString());//交易数量
				String organizationName =(String)map.get("ORGANIZATION_NAME");//组织名称
				String organizationCode =(String)map.get("ORGANIZATION_CODE");//组织代码
				String supplierName =(String)map.get("vendor_name");//供应商名称
				String supplierCode = (String)map.get("vendor_code");//供应商编码
				SupplierMaterialEvaluate report = null;
				String formNo = formCodeGenerated.getSupplierMaterialEvaluateCode(session,companyId);
				System.out.println(formNo);
				String existHql = "from SupplierMaterialEvaluate s where s.transactionId=?";
				List<?> list = session.createQuery(existHql)
				.setParameter(0,transactionId)
				.list();
				if(list.isEmpty()){
					report = new SupplierMaterialEvaluate();
					report.setCreatedTime(new Date());
					report.setCreator(userLoginName);
					report.setSubCompanyId(companyId);
					report.setCompanyId(companyId);
					report.setFormNo(formNo);
				}else{
					report = (SupplierMaterialEvaluate)list.get(0);
				}				
				report.setTransactionId(transactionId);
				report.setModifiedTime(new Date());
				report.setMaterialCode(materialCode);
				report.setMaterialName(materialName);
				report.setMaterialAmount(materialAmount);
				report.setBusinessUnitName(organizationName);
				report.setBusinessUnitCode(organizationCode);
				report.setSupplierCode(supplierCode);
				report.setSupplier(supplierName);
				
				
				//每次一个事务避免长时间锁住这张表影响前台的查询显示
				transaction = session.beginTransaction();
				session.save(report);
				transaction.commit();
			    session.flush();
	            session.clear();
	            Date lastUpdateDate = (Date)map.get("transaction_date");
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
			}else{
				updateTimestamp = (UpdateTimestamp)updateStamp.get(0);
			}
			updateTimestamp.setLastModifier(userLoginName);
			updateTimestamp.setLastModifiedTime(new Date());
			updateTimestamp.setUpdateTime(saveDate);
			transaction = session.beginTransaction();
			session.save(updateTimestamp);
			transaction.commit();
			session.flush();
			session.clear();
		}catch (Exception e) {
			e.printStackTrace();
			integrationFlag = false;
			//回滚事务
			if(transaction != null){
				transaction.rollback();
			}
			throw new AmbFrameException("获取赠品仓数据失败!",e);
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
