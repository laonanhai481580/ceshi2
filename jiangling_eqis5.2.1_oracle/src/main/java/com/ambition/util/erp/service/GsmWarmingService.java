package com.ambition.util.erp.service;

import java.util.Calendar;
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
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.equipment.dao.GsmEquipmentDao;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.inspectionplan.service.InspectionPlanManager;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.util.ParameterUtils;
import com.norteksoft.product.util.ThreadParameters;


/**
 * 类名:计量红警灯
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：处理QIS与ERP系统来料检验单信息的整合</p>
 * @author  LPF
 * @version 1.00 2017-6-13 发布
 */
@Service
public class GsmWarmingService implements IntegrationService,Runnable {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GsmEquipmentDao gsmEquipmentDao;  
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;
	@Autowired
	private InspectionPlanManager inspectionPlanManager;
	@Override
	public void run() {
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
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("计量器具校验提前预警失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getGsmWarmingScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 计量器具校验提前预警
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
		
			String tableName = "gsmWarming";
			session = gsmEquipmentDao.getSessionFactory().openSession();
			Query query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp = query.list();
			String hql = " from GsmEquipment i   ";
			query = session.createQuery(hql);
			List<GsmEquipment> reports = query.list();
			for (GsmEquipment report : reports) {
				boolean checks = false;
				Date nextProofTime=report.getNextProofTime();
				String checkMethod=report.getCheckMethod();	
//				int wDay = report.getWarnDates();
				Long nowDates=new Date().getTime();//当前时间
				if(nextProofTime!=null&&checkMethod!=null&&"内校".equals(checkMethod)&& !GsmEquipment.STATE_BLOCKUP.equals(report.getMeasurementState())){
				   Long warnDates=nextProofTime.getTime();
				   Long intervalMilli = warnDates - nowDates;
				   int 	days=(int)(intervalMilli/(24 * 60 * 60 * 1000));
				   if(nowDates>warnDates){
					   report.setIsWarm(1);
					   sendEmail(report,days,session);
					   checks = check(report);
				   }else{
					   if(days<=10){
						   report.setIsWarm(1); 
						   sendEmail(report,days,session);
						   checks = check(report);
					   }else{
						   report.setIsWarm(0);
					   }
				   }if(checks==true){
					   report.setMeasurementState(GsmEquipment.STATE_CHECK);
				   }
				}else if(nextProofTime!=null&&checkMethod!=null&&"外校".equals(checkMethod)&& !GsmEquipment.STATE_BLOCKUP.equals(report.getMeasurementState())){
				   Long warnDates=nextProofTime.getTime();
				   Long intervalMilli = warnDates - nowDates;
				   int 	days=(int)(intervalMilli/(24 * 60 * 60 * 1000));
				   if(nowDates>warnDates){
					   report.setIsWarm(1);
					   sendEmail(report,days,session);
					   checks = check(report);
				   }else{
					   if(days<=30){
						   report.setIsWarm(1); 
						   sendEmail(report,days,session);
						   checks = check(report);
					   }else{
						   report.setIsWarm(0);
					   }
				   }if(checks==true){
					   report.setMeasurementState(GsmEquipment.STATE_CHECK);
				   }
				}else if(nextProofTime!=null&&GsmEquipment.STATE_BLOCKUP.equals(report.getMeasurementState())){
					 Long warnDates=nextProofTime.getTime();
					 Long intervalMilli = warnDates - nowDates;
					 int days=(int)(intervalMilli/(24 * 60 * 60 * 1000));
					 report.setIsWarm(1);
					 if(isDate()==true){
						 sendEmail(report,days,session);
					 }
				}else{
					report.setIsWarm(0);
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
			throw new AmbFrameException("计量器具校验提前预警失败!",e);
		}finally{
			integrationFlag = false;
			if(session != null){
				session.close();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void sendEmail(GsmEquipment gsmEquipment,int day, Session session){
		String dutyMan=gsmEquipment.getDutyLoginMan();//责任人
		String copyMan=gsmEquipment.getCopyLoginMan();//抄送人
		String director=null;
		if(!GsmEquipment.STATE_BLOCKUP.equals(gsmEquipment.getMeasurementState())){
			director = gsmEquipment.getDirectorLogin();//主管
		}
		Query query = null;
		String message=null;
		if(day<0){
			message = "*计量器具最新预警信息:设备【"+gsmEquipment.getEquipmentName()+"】、规格型号【"+gsmEquipment.getEquipmentModel()+"】、管理编号【"+gsmEquipment.getManagerAssets()+"】校验预警，仪器已过期，请及时送校！地址："+PropUtils.getProp("gsmEmailDress");
		}else{
			message = "*计量器具最新预警信息:设备【"+gsmEquipment.getEquipmentName()+"】、规格型号【"+gsmEquipment.getEquipmentModel()+"】、管理编号【"+gsmEquipment.getManagerAssets()+"】校验预警，仪器"+day+"天后即将过期，请及时送校！地址："+PropUtils.getProp("gsmEmailDress");
		}
		String SQL="select email from acs_user where login_name = ? ";
		
		if(dutyMan!=null && !"".equals(dutyMan)){	
			query = session.createSQLQuery(SQL).setParameter(0,dutyMan);
			List<Object> emails = query.list();
			String dutyManEmail = emails.get(0).toString();
			if(StringUtils.isNotEmpty(dutyManEmail)){
				AsyncMailUtils.sendMail(dutyManEmail,"计量器具校验预警",message);
			}
		}
		if(copyMan!=null && !"".equals(copyMan)){	
			String a[]=copyMan.split(",");
			for (int i = 0; i < a.length; i++) {
				String userMan=a[i].toString();
				query = session.createSQLQuery(SQL).setParameter(0,userMan);
				List<Object> emails = query.list();
				String copyManEmail = emails.get(0).toString();
				if(StringUtils.isNotEmpty(copyManEmail)){
					AsyncMailUtils.sendMail(copyManEmail,"计量器具校验预警",message);
				}
			}
		}
		if(day<=7){
			if(director!=null && !"".equals(director)){	
				query = session.createSQLQuery(SQL).setParameter(0,director);
				List<Object> emails = query.list();
				String directorEmail = emails.get(0).toString();
				if(StringUtils.isNotEmpty(directorEmail)){
					AsyncMailUtils.sendMail(directorEmail,"计量器具校验预警",message);
				}
			}
		}
		
	}
	private boolean check(GsmEquipment gsmEquipment){
		try {
			InspectionPlan inspectionPlan=gsmEquipmentManager.createEquipmentPlanZD(gsmEquipment);
			if(inspectionPlan!=null){
				inspectionPlanManager.testZd(inspectionPlan, gsmEquipment.getCheckMethod());
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private volatile boolean integrationFlag = false;
	@Override
	public boolean isIntegration() {
		return integrationFlag;
	}
	public int getProgressbar() {
		return 0;
	}
	public boolean isDate(){
		// 获得一个日历对象
		Calendar c = Calendar.getInstance();
		// 得到本月的那一天
		int today = c.get(Calendar.DAY_OF_MONTH);
		// 然后判断是不是本月的第一天
		if(today ==1){
			return true;
		}else{
			return false;
		}
	}
}
