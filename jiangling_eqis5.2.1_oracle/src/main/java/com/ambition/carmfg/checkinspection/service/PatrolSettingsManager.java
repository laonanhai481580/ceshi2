package com.ambition.carmfg.checkinspection.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.checkinspection.dao.MfgCheckInspectionReportDao;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgPatrolItem;
import com.ambition.carmfg.entity.PatrolMonitor;
import com.ambition.carmfg.entity.PatrolSettings;
import com.ambition.supplier.utils.DateUtil;

/**
 * 类名:巡检到期未巡检提醒消息
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：设定下次巡检时间,生成监控数据</p>
 * @author  赵骏
 * @version 1.00 2013-9-5 发布
 */
@Service
@Transactional
public class PatrolSettingsManager{
	@Autowired
	private  MfgCheckInspectionReportDao mfgCheckInspectionReportDao;
	
	/**
	  * 方法名: 生成监控预警数据
	  * <p>功能说明：</p>
	  * @param report
	  * @return 下次巡检时间
	 */
	public Date monitorPatrolRecord(MfgCheckInspectionReport report){
		Date nextPatrolDate = null;
		//如果已经完成或者不需要监控,取消预警监控
		if(MfgCheckInspectionReport.PATROL_STATE_COMPLETE.equals(report.getPatrolState())
				||report.getPatrolSettings()==null
				||!report.getPatrolSettings().getRemindSwitch()){
			String hql = "delete from PatrolMonitor p where p.patrolInspectionReportId = ?";
			mfgCheckInspectionReportDao.createQuery(hql,report.getId()).executeUpdate();
		}
		
		if(report.getPatrolSettings() != null 
				&& StringUtils.isNotEmpty(report.getPatrolSettings().getTimeIntervalType())
				&& StringUtils.isNotEmpty(report.getPatrolSettings().getTimeIntervalValue())){
			//计算下次巡检的时间
			PatrolSettings settings = report.getPatrolSettings();
			List<MfgPatrolItem> patrolItems = report.getPatrolItems();
			Collections.sort(patrolItems,new Comparator<MfgPatrolItem>() {
				public int compare(MfgPatrolItem item1,MfgPatrolItem item2) {
					if(item1.getInspectionDate().getTime()<item2.getInspectionDate().getTime()){
						return -1;
					}else if(item1.getInspectionDate().getTime()>item2.getInspectionDate().getTime()){
						return 1;
					}else{
						return 0;
					}
				}
			});
			MfgPatrolItem lastItem = patrolItems.get(patrolItems.size()-1);
			Calendar calendar = Calendar.getInstance();
			if("fixed".equals(settings.getTimeIntervalType())){
				MfgPatrolItem firstItem = patrolItems.get(0);
				calendar.setTime(firstItem.getInspectionDate());
				int interval = (int)(Double.valueOf(settings.getTimeIntervalValue())*60);
				while(calendar.getTimeInMillis()<=lastItem.getInspectionDate().getTime()){
					calendar.add(Calendar.MINUTE,interval);
				}
			}else{
				String lastDateStr = DateUtil.formateDateStr(lastItem.getInspectionDate());
				String[] timeStrs = settings.getTimeIntervalValue().split(",");
				List<Date> timeList = new ArrayList<Date>();
				for(String timeStr : timeStrs){
					if(StringUtils.isNotEmpty(timeStr)){
						timeList.add(DateUtil.parseDate(lastDateStr + " " + timeStr,"yyyy-MM-dd HH:mm"));
					}
				}
				Collections.sort(timeList,new Comparator<Date>() {
					public int compare(Date item1,Date item2) {
						if(item1.getTime()<item2.getTime()){
							return -1;
						}else if(item1.getTime()>item2.getTime()){
							return 1;
						}else{
							return 0;
						}
					}
				});
				
				//设置最后一次的检验日期
				calendar.setTime(lastItem.getInspectionDate());
				boolean hasSet = false;
				for(Date time : timeList){
					if(calendar.getTimeInMillis()<=time.getTime()){
						calendar.setTime(time);
						hasSet = true;
						break;
					}
				}
				if(!hasSet){
					calendar.setTime(timeList.get(0));
					calendar.add(Calendar.DATE,1);
				}
			}
			nextPatrolDate = calendar.getTime();
		}
		
		if(report.getPatrolSettings() != null 
				&& report.getPatrolSettings().getRemindSwitch()){
			String hql = "from PatrolMonitor p where p.patrolInspectionReportId = ?";
			List<PatrolMonitor> patrolMonitors = mfgCheckInspectionReportDao.find(hql,report.getId());
			PatrolMonitor monitor = null;
			if(patrolMonitors.isEmpty()){
				monitor = new PatrolMonitor();
				monitor.setCompanyId(report.getCompanyId());
				monitor.setCreatedTime(new Date());
				monitor.setCreator(report.getLastModifier());
				monitor.setPatrolInspectionReportId(report.getId());
			}else{
				monitor = patrolMonitors.get(0);
			}
			monitor.setBomCode(report.getCheckBomCode());
			monitor.setBomName(report.getCheckBomName());
			monitor.setWorkProcedure(report.getWorkProcedure());
			monitor.setWorkShop(report.getWorkshop());
			monitor.setChecker(report.getInspector());
			monitor.setInspectionNo(report.getInspectionNo());
			monitor.setLastModifiedTime(new Date());
			monitor.setLastModifier(report.getLastModifier());
			
			PatrolSettings settings = report.getPatrolSettings();
			monitor.setPatrolSettings(settings);
			//计算提醒时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(nextPatrolDate);
			if("fixed".equals(settings.getRemindTimeType())){//固定时间间隔
				calendar.add(Calendar.MINUTE,(int)(Double.valueOf(settings.getRemindTimeValue())*60));
			}
			monitor.setShouldPatrolDate(calendar.getTime());
			mfgCheckInspectionReportDao.getSession().save(monitor);
		}
		return nextPatrolDate;
	}
	
	/**
	  * 方法名: 根据巡检报告ID删除监控数据
	  * <p>功能说明：</p>
	  * @param mfgCheckInspectionReportId
	 */
	public void deleteMonitor(Long mfgCheckInspectionReportId){
		String hql = "delete from PatrolMonitor p where p.patrolInspectionReportId = ?";
		mfgCheckInspectionReportDao.batchExecute(hql,mfgCheckInspectionReportId);
	}
}
