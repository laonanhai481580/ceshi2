package com.ambition.util.erp.service;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.carmfg.inspectionbase.dao.MfgItemIndicatorDao;
import com.ambition.carmfg.inspectionbase.service.MfgItemIndicatorManager;
import com.ambition.util.erp.dao.UpdateTimestampDao;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ParameterUtils;
import com.norteksoft.product.util.ThreadParameters;
/**
 * 同步检验数据到spc
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2018年3月31日 发布
 */
@Service
@Transactional
public class SpcSynchroDatasService implements IntegrationService,Runnable{
	private Logger log = Logger.getLogger(this.getClass());
	private volatile boolean integrationFlag = false;
	@Autowired
	private UpdateTimestampDao updateTimestampDao;
	@Autowired
	private MfgItemIndicatorManager mfgItemIndicatorManager;//检验项目指标
	@Autowired
	private MfgItemIndicatorDao mfgItemIndicatorDao;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			if(ContextUtils.getCompanyId()==null){
				ThreadParameters threadParameters = new ThreadParameters();
				threadParameters.setLoginName("ofilm.systemAdmin");
				threadParameters.setPageNumber(1);
				threadParameters.setUserName("systemAdmin");
				threadParameters.setUserId(2227L);
				threadParameters.setCompanyId(2224L);
				ParameterUtils.setParameters(threadParameters);

			}
			beginIntegration(null, null);
		}catch (Exception e) {
			log.error("检验数据同步到spc失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getSynchroSpcDatasScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean isIntegration() {
		// TODO Auto-generated method stub
		return false;
	}

	public void beginIntegration(Long companyId,String userLoginName){
		// TODO Auto-generated method stub
		if(integrationFlag){
			log.error("当前正有别的用户在执行更新动作!"+this.getClass());
			return;
		}
		integrationFlag=true;
        SessionFactory sessionFactory = null;
        Boolean participate = null;
        try {
        	if(null == companyId){
        		sessionFactory = (SessionFactory)ContextUtils.getBean("sessionFactory");  
            	participate = bindHibernateSessionToThread(sessionFactory);
        	}
        	beginIntegration(companyId, userLoginName,true);
		}catch (Throwable t) {
			log.error("检验数据同步到spc失败",t);
		} finally{
			integrationFlag = false;
			if(participate!=null && sessionFactory != null){
				closeHibernateSessionFromThread(participate, sessionFactory);
			}
		}
	}
	@SuppressWarnings({ "unchecked", "deprecation", "null" })
	public void beginIntegration(Long companyId, String userLoginName,
			boolean isAuto) {
		Session session = null;
		PreparedStatement ps = null;
		/*Statement statement = null;*/
		Transaction transaction = null;
		try {
			String tableName = "spc";
			session = mfgItemIndicatorDao.getSessionFactory().openSession();
//			Query query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
//			query.setParameter(0,tableName);
			String hql = " from UpdateTimestamp u where u.tableName = ? ";
			List<UpdateTimestamp> updateStamp = updateTimestampDao.find(hql,tableName);
			Date updateTime = null;
			Date saveDate = null;
			String startDate = null;
			String endDate = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			if(!updateStamp.isEmpty()){
				updateTime = updateStamp.get(0).getUpdateTime();
				Calendar cal = Calendar.getInstance();
				cal.setTime(updateTime);
				cal.add(Calendar.HOUR, 2);
				startDate = sdf1.format(updateTime);
				endDate = sdf1.format(cal.getTime());
				saveDate = cal.getTime();
			}else{
				updateTime = new Date();
				saveDate = updateTime;
				String nowDateStr = sdf1.format(updateTime);
				startDate = nowDateStr + " 00:00:00";
				endDate = nowDateStr + " 23:59:59";
				saveDate = sdf.parse(endDate);
			}
			/*String hqlItem = " from MfgItemIndicator m where m.featureId is not null";
			List<MfgItemIndicator> items = mfgItemIndicatorDao.find(hqlItem);
			
			for(MfgItemIndicator item : items){
				saveDate = mfgItemIndicatorManager.synchroCheckDatasNew(String.valueOf(item.getId()),startDate,endDate);
			}*/
			saveDate = mfgItemIndicatorManager.synchroCheckDatasNew2(startDate,endDate,session,transaction);
			if(saveDate==null){
				
				saveDate = updateTime;
			}
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
			//保存变更信息
//			updateTimestampDao.save(updateTimestamp);
			transaction = session.beginTransaction();
			session.save(updateTimestamp);
			transaction.commit();
		}catch (Exception e) {
			log.error("检验数据同步到spc失败：",e);
			throw new AmbFrameException("检验数据同步到spc失败：",e);
		}
		
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
	@Override
	public int getProgressbar() {
		// TODO Auto-generated method stub
		return 0;
	}

}
