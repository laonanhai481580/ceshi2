package com.ambition.util.erp.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.ecm.dcrn.dao.DcrnReportDao;
import com.ambition.ecm.entity.DcrnReport;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.AsyncMailUtils;
@Service
public class DcrnReportIntegerationServices implements  IntegrationService,Runnable  {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private DcrnReportDao dcrnReportDao;
	
	public void run() {
		try{
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("自动DCR/N变更邮件提醒失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getDcrnReportScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	 private volatile boolean integrationFlag = false;
	@Override
	public void beginIntegration(Long companyId, String userLoginName) {
		// TODO Auto-generated method stub
		if(integrationFlag){
			log.error("当前正有别的用户在执行更新操作!");
			return;
		}
		integrationFlag = true;
		Transaction transaction = null;
		Session session = null;
		try{
			session = dcrnReportDao.getSessionFactory().openSession();
			String tableName = "dcrnEmail";
			String hql = "from DcrnReport e where e.completeDate is null";
			Query query = session.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<DcrnReport> lists = query.list();
			Date date = new Date();
			for(DcrnReport report : lists){
				long day=0;
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");    
		        Date beginDate;
		        Date endDate;
		        endDate = format.parse(format.format(date));
	            beginDate= format.parse(format.format(report.getCreatedTime()));    
	            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
	            if(report.getNuclearSignPeroid()!=null&&report.getNuclearSignPeroid().contains("急件")&&day>=3){
	            	String email = ApiFactory.getAcsService().getUserByLoginName(report.getCreator()).getEmail();
					if(StringUtils.isNotEmpty(email)){
						AsyncMailUtils.sendMail(email,"DCR/N变更单急件到期提醒","单号:"+report.getDcrnNo()+",机种编号:"+report.getMachineNo()+",流程发起天数:"+day+1);
					}
	            }
	            if(report.getNuclearSignPeroid()!=null&&report.getNuclearSignPeroid().contains("一般件")&&day>=13){
	            	String email = ApiFactory.getAcsService().getUserByLoginName(report.getCreator()).getEmail();
					if(StringUtils.isNotEmpty(email)){
						AsyncMailUtils.sendMail(email,"DCR/N变更单急件到期提醒","单号:"+report.getDcrnNo()+",机种编号:"+report.getMachineNo()+",流程发起天数:"+day+1);
					}
	            }
			}
			query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp = query.list();
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
			updateTimestamp.setUpdateTime(new Date());
			//保存变更信息
			transaction = session.beginTransaction();
			session.save(updateTimestamp);
			transaction.commit();
			session.flush();
			session.clear();
		}catch (Exception e) {
			log.error("DCR/N变更邮件提醒失败!",e);
		}finally{
			integrationFlag = false;
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}
	@Override
	public int getProgressbar() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean isIntegration() {
		// TODO Auto-generated method stub
		return false;
	}
}
