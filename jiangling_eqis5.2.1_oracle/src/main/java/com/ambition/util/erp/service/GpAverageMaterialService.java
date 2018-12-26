package com.ambition.util.erp.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.gp.averageMaterial.dao.GpAverageMaterialDao;
import com.ambition.gp.entity.GpAverageMaterial;
import com.ambition.util.erp.schedule.ScheduleJob;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.AsyncMailUtils;
/**
 * 
 * 类名:均值材料过期提醒
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2018年3月30日 发布
 */
@Service
public class GpAverageMaterialService implements IntegrationService,Runnable {
	
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GpAverageMaterialDao gpAverageMaterialDao;
	@Override
	public void run() {
		try{
			beginIntegration(ScheduleJob.getScheduleCompanyId(),ScheduleJob.getScheduleUserName());
		}catch (Exception e) {
			log.error("均值材料过期提醒失败!",e);
		}
		ScheduleJob
		.addSchedule(this,ScheduleJob.getGpAverageMaterialScheduleDate().getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	private volatile boolean integrationFlag = false;
	@Override
	public boolean isIntegration() {
		// TODO Auto-generated method stub
		return integrationFlag;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void beginIntegration(Long companyId, String userLoginName) {
		// TODO Auto-generated method stub
		if(integrationFlag){
			return;
		}
		integrationFlag = true;
		Session session = null;
		Transaction transaction = null;
		try {
			String tableName = "gpAverageMaterial";
			session = gpAverageMaterialDao.getSessionFactory().openSession();
			Query query=null;
			String hql = " from GpAverageMaterial s  where s.isHarmful=?";
			query = session.createQuery(hql).setParameter(0, GpAverageMaterial.STATE_QUALIFIED);
			List<GpAverageMaterial> reports = query.list();
			Long nowDates=new Date().getTime();//当前时间
			Set<String> list=new HashSet<String>();
			for (GpAverageMaterial report : reports) {
				Date reportDate=report.getTestReportExpire();
				if(reportDate==null){
					continue;
				}
				list.add(report.getSupplierCode());
				int dates=(int) ((reportDate.getTime()-nowDates)/(24 * 60 * 60 * 1000));
				if(dates<=0){
					report.setIsHarmful(GpAverageMaterial.STATE_OVERDUE);
					report.setTaskProgress(GpAverageMaterial.STATE_OVERDUE);
				}
				session.save(report);
				if(dates<7){
					String message="贵司提供的以下均质材料的测试报告即将于"+reportDate+"过期，请提醒原厂重新安排测试并于"+reportDate+"之前登录O-film QIS系统绿色环保模块更新报告"
							+ "拆解部件名称:"+report.getPartName()+"均值材料名称:"+report.getAverageMaterialName()+"均值材料型号"+report.getAverageMaterialModel();
					AsyncMailUtils.sendMail(report.getSupplierEmail(),"均质材料报告过期提醒!",message);
					Thread.currentThread().sleep(10000);//毫秒
				}
			}
			for(String code:list){
				String hql1 = " from GpAverageMaterial s  where s.isHarmful=? and supplierCode=?";
				query = session.createQuery(hql1).setParameter(0, GpAverageMaterial.STATE_QUALIFIED).setParameter(1, code);
				List<GpAverageMaterial> reports1 = query.list();
				String paName=null;
				String agName=null;
				String agMode=null;
				String email=reports1.get(0).getSupplierEmail();
				for(GpAverageMaterial reports2:reports1){
					Date reportDate=reports2.getTestReportExpire();
					if(reportDate==null){
						continue;
					}
					int dates=(int) ((reportDate.getTime()-nowDates)/(24 * 60 * 60 * 1000));
			        if(dates<=45&&dates>7){
			        	paName += reports2.getPartName()+",";
						agName += reports2.getAverageMaterialName()+",";
						agMode += reports2.getAverageMaterialModel()+",";
			        }
				}
				String message=null;
				if("星期一".equals(getWeekOfDate(new Date()))&&paName!=null){
					if(agName==null){
						agName="";
					}
					if(agMode==null){
						agMode="";
					}
					message="贵司提供的以下均质材料的测试报告即将于过期，请提醒原厂重新安排测试并于期间登录O-film QIS系统绿色环保模块更新报告"
							+ "拆解部件名称:"+paName+"均值材料名称:"+agName+"均值材料型号"+agMode;
					AsyncMailUtils.sendMail(email,"均质材料报告过期提醒!",message);
					Thread.currentThread().sleep(5000);//毫秒
				}
				
			}
			query = session.createQuery("from UpdateTimestamp u where u.tableName = ?");
			query.setParameter(0,tableName);
			List<UpdateTimestamp> updateStamp=query.list();
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
			System.err.println();
			integrationFlag = false;
			throw new AmbFrameException("均值材料过期提醒失败!",e);
		}finally{
			integrationFlag = false;
			if(session != null){
				session.close();
			}
		}
	}

	@Override
	public int getProgressbar() {
		// TODO Auto-generated method stub
		return 0;
	}
	public boolean isDate(){
		// 获得一个日历对象
		Calendar c = Calendar.getInstance();
		// 得到本月的那一天
		int today = c.get(Calendar.DAY_OF_MONTH);
		// 然后判断是不是本月的第一天
		if(today ==1||today==7||today==13||today==19||today==25||today==31){
			return true;
		}else{
			return false;
		}
	}
	public Date formatMonth(Date date){
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
        Date d = c.getTime();
		return d;
	}
	public Date formatDate(Date date){
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -7);
        Date d = c.getTime();
		return d;
	}
	/**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
}
