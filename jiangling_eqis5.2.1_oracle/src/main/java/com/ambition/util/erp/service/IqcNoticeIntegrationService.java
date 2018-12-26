package com.ambition.util.erp.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.InspectionFromOverdueEmail;
import com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.product.util.AsyncMailUtils;

/**
 * 类名:来料检验单
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：处理QIS与ERP系统来料检验单信息的整合</p>
 * @author  LPF
 * @version 1.00 2013-8-19 发布
 */
@Service
public class IqcNoticeIntegrationService implements IntegrationService,Runnable {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private IncomingInspectionActionsReportDao incomingInspectionActionsReportDao;       
	@Override
	public void run() {
		try{
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("检验任务超期通知失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getIqcNoticeScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 检验任务超期通知
	 */
	@SuppressWarnings("unchecked")
	public void beginIntegration(Long companyId,String userLoginName){
		if(integrationFlag){
			return;
		}
		integrationFlag = true;
		Session session = null;
		Transaction transaction = null;
		try {
			String tableName = "iqcNotice";
			session = incomingInspectionActionsReportDao.getSessionFactory().openSession();
			Query query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp = query.list();
			
			String noticeHql = " from InspectionFromOverdueEmail i where i.overdue is not null ";
			query = session.createQuery(noticeHql);
			List<InspectionFromOverdueEmail> inspectionFromOverdueEmails = (List<InspectionFromOverdueEmail>) query.list();
			for (InspectionFromOverdueEmail inspectionFromOverdueEmail : inspectionFromOverdueEmails) {
				Integer overDay = inspectionFromOverdueEmail.getOverdue().intValue();
				Date nowDate = new Date(); 
		        Calendar cal = Calendar.getInstance();    
		        cal.setTime(nowDate);  
		        long time1 = cal.getTimeInMillis(); 
				
				String hql = " from IncomingInspectionActionsReport i where i.inspectionState=? and i.checkBomCode=? ";
				query = session.createQuery(hql);
				query.setParameter(0,IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT);
				query.setParameter(1,inspectionFromOverdueEmail.getMaterielCode());
				List<IncomingInspectionActionsReport> reports = query.list();
				for (IncomingInspectionActionsReport report : reports) {
				 	Date createdTime = report.getCreatedTime();  
					cal.setTime(createdTime);   
					String isDueMail=report.getIsDueMail();
				    long time2 = cal.getTimeInMillis();  
				    long days=(time1-time2)/(1000*3600); 
				    if(days>overDay&&!("是").equals(isDueMail)){
				    	String message = "进料检验单号:"+report.getInspectionNo()+",物料编号为:"+report.getCheckBomCode()+",物料名称为:"+ report.getCheckBomName()+"的物料检验时间已超期,请尽快检验";
					    String warmingManLogin=inspectionFromOverdueEmail.getReveiveLoginMan();
					    if(warmingManLogin!=null){
					    	String a[]=warmingManLogin.split(",");
					    	for (int i = 0; i < a.length; i++) {
								String userId=a[i].toString();
								String email = "";
								String sql = "select email from acs_user where login_name=? ";
								Query query2 = session.createSQLQuery(sql).setParameter(0, userId);
								List<Object> emails = query2.list();
								if(emails.size()>0){
									email = (String) emails.get(0);
								}
								if(StringUtils.isNotEmpty(email)){
									AsyncMailUtils.sendMail(email,"检验任务超期",message);
							    }
								report.setIsDueMail("是");
							}
					    }
				    }
				}								
			}
			
			//待检验
			/*String hql = " from IncomingInspectionActionsReport i where i.inspectionState=? and i.checkBomCode in(select materielCode from InspectionFromOverdueEmail)";
			query = session.createQuery(hql);
			query.setParameter(0,IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT);
			List<IncomingInspectionActionsReport> reports = query.list();
			for (IncomingInspectionActionsReport report : reports) {
				String noticeHql = " from InspectionFromOverdueEmail i where i.overdue is not null  and i.materielCode=? ";
				query = session.createQuery(noticeHql);
				query.setParameter(0,report.getCheckBomCode());
				List<InspectionFromOverdueEmail> inspectionFromOverdueEmails = (List<InspectionFromOverdueEmail>) query.list();
				if(inspectionFromOverdueEmails.size()==0){
					continue;
				}
				InspectionFromOverdueEmail inspectionFromOverdueEmail = (InspectionFromOverdueEmail) query.list().get(0);
				Integer overDay = inspectionFromOverdueEmail.getOverdue().intValue();
				Date nowDate = new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
				nowDate=sdf.parse(sdf.format(nowDate));  
		        Calendar cal = Calendar.getInstance();    
		        cal.setTime(nowDate);  
		        long time1 = cal.getTimeInMillis();   
		        Date createdTime = sdf.parse(sdf.format(report.getCreatedTime()));  
				cal.setTime(createdTime);   
				String isDueMail=report.getIsDueMail();
			    long time2 = cal.getTimeInMillis();  
			    long days=(time1-time2)/(1000*3600); 						
			    if(days>overDay&&isDueMail!=null&&!("是").equals(isDueMail)){
			    	String message = "进料检验单号:"+report.getInspectionNo()+",物料编号为:"+report.getCheckBomCode()+",物料名称为:"+ report.getCheckBomName()+"的物料检验时间已超期,请尽快检验";
				    String warmingManLogin=inspectionFromOverdueEmail.getReveiveLoginMan();
				    if(warmingManLogin!=null){
				    	String a[]=warmingManLogin.split(",");
				    	for (int i = 0; i < a.length; i++) {
							String userId=a[i].toString();
							String email = "";
							String sql = "select email from acs_user where login_name=";
							Query query2 = session.createSQLQuery(sql).setParameter(0, userId);
							List<Object> emails = query2.list();
							if(emails.size()>0){
								email = (String) emails.get(0);
							}
							if(StringUtils.isNotEmpty(email)){
								AsyncMailUtils.sendMail(email,"检验任务超期",message);
						    }
							report.setIsDueMail("是");
						}
				    }
			    }
			}*/
			//重检
			/*String recheckHql = " from IncomingInspectionActionsReport i where i.inspectionState=?  ";
			query = session.createQuery(recheckHql);
			query.setParameter(0,IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK);
			List<IncomingInspectionActionsReport> reckeckLists = query.list();
			for (IncomingInspectionActionsReport report : reckeckLists) {
				String noticeHql = " from InspectionFromOverdueEmail i where i.overdue is not null  and i.materielCode=? ";
				query = session.createQuery(noticeHql);
				query.setParameter(0,report.getCheckBomCode());
				List<InspectionFromOverdueEmail> inspectionFromOverdueEmails = query.list();
				if(inspectionFromOverdueEmails.size()==0){
					continue;
				}
				InspectionFromOverdueEmail inspectionFromOverdueEmail = inspectionFromOverdueEmails.get(0);
				Integer overDay = inspectionFromOverdueEmail.getOverdue().intValue();
				Date nowDate = new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
				nowDate=sdf.parse(sdf.format(nowDate));  
		        Calendar cal = Calendar.getInstance();    
		        cal.setTime(nowDate);  
		        long time1 = cal.getTimeInMillis();   
		        Date recheckTime = sdf.parse(sdf.format(report.getRecheckTime()));  
				cal.setTime(recheckTime);   
			    long time2 = cal.getTimeInMillis();  
			    long days=(time1-time2)/(1000*3600*24); 						
			    if(days>overDay){
			    	String message = "进料检验单号:"+report.getInspectionNo()+",物料编号为:"+report.getCheckBomCode()+",物料名称为:"+ report.getCheckBomName()+"的物料重检时间已超期,请尽快检验";
				    String warmingManLogin=inspectionFromOverdueEmail.getReveiveLoginMan();
				    if(warmingManLogin!=null){
				    	String a[]=warmingManLogin.split(",");
				    	for (int i = 0; i < a.length; i++) {
							String userId=a[i].toString();
							String email = ApiFactory.getAcsService().getUserByLoginName(userId).getEmail();
							if(StringUtils.isNotEmpty(email)){
								AsyncMailUtils.sendMail(email,"检验任务超期",message);
						    }
						}
				    }
			    }
			}  		*/	 			
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
			updateTimestamp.setUpdateTime(new Date());
			//保存变更信息
			transaction = session.beginTransaction();
			session.save(updateTimestamp);
			transaction.commit();
			session.flush();
			session.clear();
		}catch (Exception e) {
			e.printStackTrace();
			integrationFlag = false;
			throw new AmbFrameException("检验任务超期通知失败!",e);
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
