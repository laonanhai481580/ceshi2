package com.ambition.util.erp.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.carmfg.madeinspection.dao.MadeInspectionDao;
import com.ambition.carmfg.madeinspection.service.MadeInspectionManager;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.dao.UpdateTimestampDao;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ParameterUtils;
import com.norteksoft.product.util.ThreadParameters;

/**
 * 
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：IPQC自动检测提交</p>
 * @author  林少伟
 * @version 1.00 2017年9月2日 发布
 */
@Service
@Transactional
public class IpqcAutoSubmitIntegrationService implements IntegrationService,Runnable{

private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProductBomDao bomDao;
	@Autowired
	private UpdateTimestampDao updateTimestampDao;
	@Autowired
	private MadeInspectionDao madeInspectionDao;
	@Autowired
	private MadeInspectionManager madeInspectionManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public void run() {
		try{
			beginIntegration(null, null);
		}catch (Exception e) {
			log.error("自动检测发起IPQC失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getIpqcAutoSubmitScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 导入发料记录
	 */
	public void beginIntegration(Long companyId,String userLoginName){
		if(integrationFlag){
			log.error("当前正有别的用户在执行更新动作!"+this.getClass());
			return;
		}
        SessionFactory sessionFactory = null;
        Boolean participate = null;
        try {
        	if(null == companyId){
        		sessionFactory = (SessionFactory)ContextUtils.getBean("sessionFactory");  
            	participate = bindHibernateSessionToThread(sessionFactory);
        	}
        	integration(companyId, userLoginName);
		}catch (Throwable t) {
			log.error("供应商改进信息更新出错了",t);
		} finally{
			integrationFlag = false;
			if(participate!=null && sessionFactory != null){
				closeHibernateSessionFromThread(participate, sessionFactory);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void integration(Long companyId, String userLoginName) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;
		try {
			//生成IPQC数据的时间
			String tableName = "ipqcAutoSubmit";
			session = updateTimestampDao.getSession();
			String hql1 = "from UpdateTimestamp u where u.tableName = ?";
			Query query = session.createQuery(hql1);
			query.setMaxResults(1);
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp = query.list();
			//从接口查询符合条件的数据
			Date updateTime = null;
			Date saveDate = null;
			if(!updateStamp.isEmpty()){
				updateTime = updateStamp.get(0).getUpdateTime();
				saveDate = updateTime;
			}else{
				updateTime = DateUtil.parseDate("2017-06-29");
				saveDate = updateTime;
			}
			String hql = " from MfgCheckInspectionReport m where m.batchNo=? and m.taskState=? and m.createdTime>? order by m.createdTime asc";
			query=session.createQuery(hql);
			query.setParameter(0,"WinForm");
			query.setParameter(1,"否");
			query.setParameter(2,saveDate);
			@SuppressWarnings("unchecked")
			List<MfgCheckInspectionReport> reports = query.list();
			if(companyId==null){
				ThreadParameters threadParameters = new ThreadParameters();
				threadParameters.setLoginName("ofilm.systemAdmin");
				threadParameters.setPageNumber(1);
				threadParameters.setUserName("systemAdmin");
				threadParameters.setUserId(2227L);
				threadParameters.setCompanyId(2224L);
				ParameterUtils.setParameters(threadParameters);
			}
			if(!reports.isEmpty()){
				for(MfgCheckInspectionReport report : reports){
					String inspectionNo = formCodeGenerated.generateMFGode(session,2224L);
					//每次一个事务避免长时间锁住这张表影响前台的查询显示
					transaction = session.beginTransaction();
					report.setInspectionNo(inspectionNo);
					madeInspectionManager.submitProcess(report);
					report.setReportState(MfgCheckInspectionReport.STATE_AUDIT);
					report.setTaskState("是");
					session.save(report);
					
				}
			}
			query = session.createQuery(hql1);
			query.setMaxResults(1);
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
				updateTimestamp = updateStamp.get(0);
			}
			updateTimestamp.setLastModifier(userLoginName);
			updateTimestamp.setLastModifiedTime(new Date());
			updateTimestamp.setUpdateTime(saveDate);
			session.save(updateTimestamp);
			//提交更新事务
			transaction.commit();
		}catch (Exception e) {
			e.printStackTrace();
			integrationFlag = false;
			transaction.rollback();
			throw new AmbFrameException("自动发起IPQC失败!",e);
		}finally{
			transaction = null;
			if(null != session && session.isOpen())
			session.close();
			integrationFlag = false;
		}
	}
	/**
	  * 方法名: 手动绑定Session
	  * <p>功能说明：</p>
	  * @param sessionFactory
	  * @return
	 */
	public static boolean bindHibernateSessionToThread(SessionFactory sessionFactory) {  
	    if (TransactionSynchronizationManager.hasResource(sessionFactory)) {  
	        return true;  
	    } else {  
	        Session session = sessionFactory.openSession();
	        SessionHolder sessionHolder = new SessionHolder(session);
	        TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
	    }
	    return false;  
	}
	/**
	  * 方法名: 手动取消绑定Session
	  * <p>功能说明：</p>
	  * @param participate
	  * @param sessionFactory
	 */
	public static void closeHibernateSessionFromThread(boolean participate, Object sessionFactory) {  
	    if (!participate) {
	        SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.unbindResource(sessionFactory);  
	        SessionFactoryUtils.closeSession(sessionHolder.getSession());
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
