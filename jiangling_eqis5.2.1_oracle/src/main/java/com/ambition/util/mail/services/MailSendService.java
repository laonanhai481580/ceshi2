package com.ambition.util.mail.services;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.erp.service.IntegrationService;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.mail.dao.MailSendContentDao;
import com.ambition.util.mail.entity.MailSendContent;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ParameterUtils;
import com.norteksoft.product.util.ThreadParameters;

@Service
public class MailSendService implements IntegrationService,Runnable{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MailSendContentDao mailSendContentDao;
	@Override
	public void run() {
		try{
			ThreadParameters threadParameters = new ThreadParameters();
			threadParameters.setLoginName("amb.systemAdmin");
			threadParameters.setPageNumber(1);
			threadParameters.setUserName("systemAdmin");
			threadParameters.setUserId(1L);
			threadParameters.setCompanyId(1L);
			ParameterUtils.setParameters(threadParameters);
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("发送邮件失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getMailScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}

	

	@Override
	public void beginIntegration(Long companyId, String userLoginName) {
		if(integrationFlag){
			log.error("当前正有别的用户在执行发送邮件的操作!");
			return;
		}
		integrationFlag = true;
		//导入
		long time = System.currentTimeMillis();
		Session session = null;
		Transaction transaction = null;
		try {
			session = mailSendContentDao.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			@SuppressWarnings("unchecked")
			List<MailSendContent> mailSendContents = session.createQuery("from MailSendContent where deleted = ? and sendDate < ?")
				.setParameter(0, "0")
				.setParameter(1, new Date())
				.list();
			
			for(MailSendContent mailSendContent : mailSendContents){
				if(mailSendContent.getTos()==null || mailSendContent.getTos().equals(""))
					continue;
				String[] users = mailSendContent.getTos().split(",");
				for(String user:users){
					String email = ApiFactory.getAcsService().getUserByLoginName(user).getEmail();
					if(StringUtils.isNotEmpty(email)){
			            AsyncMailUtils.sendMail(email,mailSendContent.getSubject(),mailSendContent.getContents().toString());
			        }
				}
				mailSendContent.setModifier(userLoginName);
				mailSendContent.setModifiedTime(new Date());
				mailSendContent.setDeleted("1");
				session.update(mailSendContent);
			}
			
			//更新操作时间
			UpdateTimestamp updateTimestamp = null;
			@SuppressWarnings("unchecked")
			List<UpdateTimestamp> datas = session.createQuery("from UpdateTimestamp u where u.tableName = ?")
					.setParameter(0,"mailSend").list();
			if(datas.isEmpty()){
				updateTimestamp = new UpdateTimestamp();
				updateTimestamp.setTableName("mailSend");
				updateTimestamp.setCompanyId(companyId);
				updateTimestamp.setCreator(userLoginName);
				updateTimestamp.setCreatedTime(new Date());
				updateTimestamp.setModifier(userLoginName);
				updateTimestamp.setModifiedTime(new Date());
				session.save(updateTimestamp);
			}else{
				updateTimestamp = datas.get(0);
				updateTimestamp.setModifier(userLoginName);
				updateTimestamp.setModifiedTime(new Date());
				session.update(updateTimestamp);
			}
			//提交事务
			transaction.commit();
		}catch (Throwable e) {
			if(transaction!=null && transaction.isActive()){
				transaction.rollback();
			}
			throw new AmbFrameException("发送邮件失败!",e);
		}finally{
			integrationFlag = false;
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		log.debug("发送邮件用时:" + (System.currentTimeMillis()-time)/1000 + "秒"); 
	}

	
	private volatile boolean integrationFlag = false;
	@Override
	public boolean isIntegration() {
		return integrationFlag;
	}
	@Override
	public int getProgressbar() {
		return 0;
	}
}
