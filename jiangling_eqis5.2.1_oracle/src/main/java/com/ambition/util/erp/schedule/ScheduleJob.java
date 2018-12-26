package com.ambition.util.erp.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.inspectionreport.service.DureEmailService;
import com.ambition.iqc.inspectionreport.service.TaskEmailService;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ambition.util.erp.service.DcrnReportIntegerationServices;
import com.ambition.util.erp.service.EcnReportIntegerationServices;
import com.ambition.util.erp.service.GpAverageMaterialService;
import com.ambition.util.erp.service.GsmWarmingService;
import com.ambition.util.erp.service.IpqcAutoSubmitIntegrationService;
import com.ambition.util.erp.service.IqcIntegrationService;
import com.ambition.util.erp.service.IqcNoticeIntegrationService;
import com.ambition.util.erp.service.MaterialIntegrationService;
import com.ambition.util.erp.service.SentOutRecordIntegrationService;
import com.ambition.util.erp.service.SpcSynchroDatasService;
import com.ambition.util.erp.service.SupplierIntegrationService;
import com.ambition.util.erp.service.SupplierMaterialEvaluateIntegerServices;
import com.ambition.util.erp.service.UseFileIntegrationService;
import com.norteksoft.product.util.PropUtils;

 /**
  * 自动执行定时任务
  * @author 赵骏
  *
  */ 
public class ScheduleJob{
	static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
	@Autowired
	private SupplierIntegrationService supplierIntegrationService;
	@Autowired
	private MaterialIntegrationService materialIntegrationService;
	@Autowired
	private SentOutRecordIntegrationService sentOutRecordIntegrationService;//发料记录
	@Autowired
	private IqcIntegrationService iqcIntegrationService;//来料检验
	@Autowired
	private UseFileIntegrationService useFileIntegrationService;//文件
	@Autowired
	private TaskEmailService taskEmailService;
	@Autowired
	private SupplierMaterialEvaluateIntegerServices supplierMaterialEvaluateIntegerServices;
	@Autowired
	private DureEmailService dureEmailService;
	@Autowired
	private DcrnReportIntegerationServices dcrnReportIntegerationServices;
	@Autowired
	private IqcNoticeIntegrationService iqcNoticeIntegrationService;
	@Autowired
	private EcnReportIntegerationServices ecnReportIntegerationServices;
	@Autowired
	private GsmWarmingService gsmWarmingService;
	@Autowired
	private SpcSynchroDatasService spcSynchroDatasService;
	@Autowired
	private GpAverageMaterialService gpAverageMaterialService;
	@Autowired
	private IpqcAutoSubmitIntegrationService ipqcAutoSubmitIntegrationService;
	private static Map<String,ScheduledFuture<?>> scheduleFutureMap = new HashMap<String, ScheduledFuture<?>>();
	
	public void init(){
		//是否可以初始化同步集成
		if(!CommonUtil1.canInitializeSynchronization()){
			return;
		}
		
		//删除过期文件,每天 00 点执行一次
		/*executor.schedule(useFileIntegrationService,0,TimeUnit.MILLISECONDS);
		int hours = 23 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hours < 0){
			hours = 0;
		}
		executor.scheduleAtFixedRate(useFileIntegrationService,hours,24,TimeUnit.HOURS);*/
		Date date = null;
		//供应商自动评价
//		Date ddate=getSupplierAutoEvaluateScheduleDate();
//		if(1==1){
//			return;
//		}
		//触发售后质量管理预警 每隔15分执行一次
//		Date date = getAfterSalesScheduleDate();
//		addSchedule(originateAfterSalesWarn,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//ERP未连接不执行
//		if(!CommonUtil.getErpConnectState()){
//			return;
//		}
//		
		//物料BOM信息,每晚23点30分执行一次
		date = getBomScheduleDate();
		addSchedule(materialIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		
		date = getDcrnReportScheduleDate();
		addSchedule(dcrnReportIntegerationServices,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		date = getEcnReportScheduleDate();
		addSchedule(ecnReportIntegerationServices,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		date = getIqcNoticeScheduleDate();
		addSchedule(iqcNoticeIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		date = getGsmWarmingScheduleDate();
		addSchedule(gsmWarmingService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//更新车身号,每晚23点45分执行一次
//		date = getUpdateCarBodyCodeScheduleDate();
//		addSchedule(updateCarBodyTypeService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
		//供应商信息,每晚0点执行一次
		date = getSupplierScheduleDate();
//		addSchedule(supplierIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		date = getSynchroSpcDatasScheduleDate();
		addSchedule(spcSynchroDatasService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		
		date = getSupplierMaterialEvaluateScheduleDate();
		addSchedule(supplierMaterialEvaluateIntegerServices,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		
		date = getSentOutRecordScheduleDate();
		addSchedule(sentOutRecordIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		//来料信息,每隔5分钟执行,同步一次
		date = getIqcScheduleDate();
		addSchedule(iqcIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		//ipqc,每隔5分钟执行,同步一次
		//date = getIpqcAutoSubmitScheduleDate();
		//addSchedule(ipqcAutoSubmitIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		//检验任务超期通知
		//date = getIqcNoticeScheduleDate();
		//addSchedule(iqcNoticeIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		//均质材料
		date = getGpAverageMaterialScheduleDate();
		addSchedule(gpAverageMaterialService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//供应商物料信息,每晚4点执行一次
//		date = getSupplierMaterialScheduleDate();
//		addSchedule(supplierMaterialIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//生产工单,每隔15分执行一次
//		date = getTaskScheduleDate();
//		addSchedule(taskIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//车身信息,每隔15分执行一次
//		date = getCarBodyScheduleDate();
//		addSchedule(carBodyIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//车型信息,每晚3点15分执行一次
//		date = getCarTypeScheduleDate();
//		addSchedule(carTypeIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//维修站信息,每晚3点执行一次
//		date = getStationScheduleDate();
//		addSchedule(maintenanceStationIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//代理商出库信息,每晚0点15分执行一次
//		date = getAgentScheduleDate();
//		addSchedule(agentDeliveryIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//工厂出库表,每晚2点30分执行一次
//		date = getFactoryScheduleDate();
//		addSchedule(factoryDeliveryIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//基础参数表信息,每晚2点执行一次
//		date = getSysParamScheduleDate();
//		addSchedule(sysParamIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//业务活动明细信息,每晚0点25分执行一次
//		date = getActivityScheduleDate();
//		addSchedule(businessActivityIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//业务类型明细信息,每晚0点20分执行一次
//		date = getTypeScheduleDate();
//		addSchedule(businessTypeIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//工时信息,每晚1点执行一次
//		date = getHourScheduleDate();
//		addSchedule(hourIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//颜色信息,每晚1点三十五分执行一次
//		date = getColorScheduleDate();
//		addSchedule(colorIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//售后理赔三包头表,每隔15分执行一次
//		date=getFaultInfoScheduleDate();
//		addSchedule(afterSalesClaimListService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//售后理赔更换零件行表,每隔15分执行一次
//		date=getReplacePartsScheduleDate();
//		addSchedule(afterSalesReplacePartsService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//售后理赔使用工时,每隔15分执行一次
//		date=getClaimManhourScheduleDate();
//		addSchedule(afterSalesClaimManhourService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//售后理赔外出服务费,每隔15分执行一次
//		date=getOutgoFeeScheduleDate();
//		addSchedule(afterSalesOutgoFeeService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//售后理赔其他项目,每隔15分执行一次
//		date=getClaimOtherItemScheduleDate();
//		addSchedule(afterSalesClaimOtherItemService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//售后理赔三包扣款单,每隔15分执行一次
//		date=getClaimWrDebitScheduleDate();
//		addSchedule(afterSalesClaimWrDebitService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		//售后理赔旧件回收,每隔15分执行一次
//		date=getClaimRoutineptItemScheduleDate();
//		addSchedule(afterSalesClaimRoutineptItemService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//品质报告,每隔15分执行一次
//		date=getApplyReportScheduleDate();
//		addSchedule(afterSalesApplyReportService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		
//		中强保,每隔15分执行一次:中强保在三包台帐中已全部包含,取消单独的接口
//		date=getMfmaintRecordScheduleDate();
//		addSchedule(afterSalesMfmaintRecordService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		
		//获的中联接口数据,每隔15分执行一次
//		date=getChnlScheduleDate();
//		addSchedule(incomingInspectionGetChnlService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
		
		//中联接口数据生产送检单,每隔15分执行一次
//		date=getChnlToIqcScheduleDate();
//		addSchedule(chnlToIQCService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//	
//		
//		//目标计算实销数,每晚23:45点执行一次
//		date = getUpdateGoalAftersalesScheduleDate();
//		addSchedule(updateGoalAftersalesService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//目标计算故障金额故障数,每晚23:35点执行一次
//		date = getUpdateGoalAftersalesFaultPaymentScheduleDate();
//		addSchedule(updateGoalAftersalesFaultPaymentService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//	
//		date=getChnlToIqcScheduleDate();
//		addSchedule(interfaceService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		
//		//供应商自动评价
//		date=getSupplierAutoEvaluateScheduleDate();
//		addSchedule(supplierAutoEvaluateService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		//供应商免检物料
//		date = getIncomingNoInspectionScheduleDate();
//		addSchedule(incomingNoInspectionService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		//PDI问题集成
//		date =getPDIProblemIntegrationScheduleDate();
//		addSchedule(pdiProblemIntegrationService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
//		//触发质量问题关闭率计算,默认周四早上计算
//		date = getQualityProblemCloseRateScheduleDate();
//		addSchedule(problemCloseRateService,date.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 方法名:获取调度的公司编号 
	  * @return
	 */
	public static Long getScheduleCompanyId(){
		return 2224l;
	}
	
	/**
	 * 方法名:获取调度的公司编号 
	  * @return
	 */
	public static String getScheduleUserName(){
		return "system";
	}
	public static ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor(){
		return executor;
	}
	
	/**
	  * 方法名: 添加调度
	  * <p>功能说明：</p>
	  * @param command
	  * @param delay
	  * @param timeUnit
	 */
	public static void addSchedule(Runnable command,long delay,TimeUnit timeUnit){
		ScheduledFuture<?> future = 
				getScheduledThreadPoolExecutor()
				.schedule(command,delay,timeUnit);
		scheduleFutureMap.put(command.getClass().getName(),future);
	}
	
	/**
	  * 方法名: 数据更新记录下次执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getUpdateRecordDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.record.interval");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,(int)(Double.valueOf(scheduleTimeStr)*60));
		return calendar.getTime();
	}
	
	/**
	  * 方法名: 供应商的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getSupplierScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","supplier.time");
		return getScheduleDate(scheduleTimeStr);
	}
	/**
	  * 方法名: 检验任务超期通知
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getIqcNoticeScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","iqcNotice.interval");
		return getScheduleDateByHours(scheduleTimeStr);
	}
	/**
	  * 方法名: 计量器具校验提前预警
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getGsmWarmingScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","gsmWarming.time");
		return getScheduleDate(scheduleTimeStr);
	}
	/**
	  * 方法名: 均值材料过期提醒
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getGpAverageMaterialScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","gpAverageMaterial.time");
		return getScheduleDate(scheduleTimeStr);
	}
	/**
	  * 方法名: 赠品仓
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getSupplierMaterialEvaluateScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","supplierEvaluate.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 供应商物料关系的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getSupplierMaterialScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","supplierMaterial.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 售后理赔信息三包头的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getFaultInfoScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.faultInfo.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 售后理赔更换零件的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getReplacePartsScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.replaceParts.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 售后理赔使用工时的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getClaimManhourScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.claimManhour.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	 * 方法名: getApplyReportScheduleDate 
	 * <p>功能说明：品质报告的调度执行时间</p>
	 * @return Date
	 * @throws
	 */
	public static Date getApplyReportScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.applyReport.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	/**
	  * 方法名: 售后理赔外出服务费用的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getOutgoFeeScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.outgoFee.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 售后理赔*的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getClaimOtherItemScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.claimOtherItem.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 售后理赔三包扣款单的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getClaimWrDebitScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.claimWrDebit.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 售后理赔旧件回收单的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getClaimRoutineptItemScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.claimRoutineptItem.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	 * 方法名: getMfmaintRecordScheduleDate 
	 * <p>功能说明：中强保</p>
	 * @return Date
	 * @throws
	 */
	public static Date getMfmaintRecordScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.mfmaintRecord.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 物料BOM的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getBomScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","bom.time");
		return getScheduleDate(scheduleTimeStr);
	}
	/**
	  * 方法名: dcrn邮件提醒
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getDcrnReportScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","dcrn.time");
		return getScheduleDate(scheduleTimeStr);
	}
	/**
	  * 方法名: ecn邮件提醒
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getEcnReportScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","ecn.time");
		return getScheduleDate(scheduleTimeStr);
	}
	/**
	  * 方法名: 发料记录的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getSentOutRecordScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","sentOutRecord.time");
		return getScheduleDate(scheduleTimeStr);
	}
	/**
	  * 方法名: 来料检验单的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getIqcIntegrationDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","iqc.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	/**
	  * 方法名: 更新车身号的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getUpdateCarBodyCodeScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","updateCarBodyCode.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 更新目标售后计算数据(实销数)
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getUpdateGoalAftersalesScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","updateGoalAftersales.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 更新目标售后计算数据（故障金额故障数）
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getUpdateGoalAftersalesFaultPaymentScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","updateGoalAftersalesFaultPayment.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 供应商自动评价
	  * <p>功能说明：每个月1号3点10分</p>
	  * @return
	 */
	public static Date getSupplierAutoEvaluateScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","calDefectiveGoodsProcessing.time");
		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DATE,IncomingInspectionReportManager.TOTAL_MONTH_DATE);
//		String evaluateDateStr = DateUtil.formateDateStr(calendar);
//		Date date = DateUtil.parseDateTime(evaluateDateStr + " " + scheduleTimeStr);
//		if(date.getTime()<System.currentTimeMillis()){
//			calendar.setTime(date);
//			calendar.add(Calendar.MONTH,1);
			return calendar.getTime();
//		}else{
//			return date;
//		}
	}
	
	/**
	  * 方法名: 生产工单的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getTaskScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.task.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 车身信息的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getCarBodyScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.carBody.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 车型信息的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getCarTypeScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","carType.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 维修站的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getStationScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","maintenance.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 代理商出库信息的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getAgentScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","agent.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 工厂出库表的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getFactoryScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","factory.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 基础参数表的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getSysParamScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","sysParam.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 业务活动明细的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getActivityScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","activity.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 业务类型明细的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getTypeScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","type.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 工时的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getHourScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","hour.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 颜色的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getColorScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","color.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 售后预警的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getAfterSalesScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.aftersales.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	 * 方法名: getMfmaintRecordScheduleDate 
	 * <p>功能说明：中联推送送检单</p>
	 * @return Date
	 * @throws
	 */
	public static Date getChnlScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.chnlRecord.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	 * 方法名: getChnlToIqcScheduleDate 
	 * <p>功能说明：生成送检单</p>
	 * @return Date
	 * @throws
	 */
	public static Date  getChnlToIqcScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.chnltoiqc.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名:免检物料集成周期
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date  getIncomingNoInspectionScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","update.noinspectiontoiqc.interval");
		if(StringUtils.isEmpty(scheduleTimeStr)){
			scheduleTimeStr = "30";//默认30分钟一次
		}
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	

	/**
	  * 方法名: 数据更新记录下次执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getPDIProblemIntegrationScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","PDIProblemIntegration.interval");
		if(StringUtils.isEmpty(scheduleTimeStr)){
			scheduleTimeStr = "30";//默认30分钟一次
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,(int)(Double.valueOf(scheduleTimeStr)*60));
		return calendar.getTime();
	}
	
	/**
	  * 方法名: 质量问题关闭率计算的调度执行时间
	  * <p>功能说明：</p>
	  * @return
	 */
	public static Date getQualityProblemCloseRateScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","problemCloseRate.time");
		return getScheduleDate(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 根据时间串获取执行调度的准确时间
	  * <p>功能说明：
	  * 	如果传入的时间串小于当前的时间,时间自动加一天
	  * </p>
	  * @param timeStr
	  * @return
	 */
	private static Date getScheduleDate(String timeStr){
		String currentDateStr = DateUtil.formateDateStr(new Date());
		Date date = DateUtil.parseDateTime(currentDateStr + " " + timeStr);
		if(date.getTime()<System.currentTimeMillis()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE,1);
			return calendar.getTime();
		}else{
			return date;
		}
	}
	
	/**
	  * 方法名: 根据时间串获取执行调度的准确时间
	  * <p>功能说明：
	  * 	如果传入的时间串小于当前的时间,时间自动加一个月，每个月1号执行
	  * </p>
	  * @param timeStr
	  * @return
	 */
	private static Date getScheduleMonth(String timeStr){
		String currentDateStr = DateUtil.formateDateStr(new Date());
		Date date = DateUtil.parseDateTime(currentDateStr + " " + timeStr);
		if(date.getTime()<System.currentTimeMillis()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH,1);
			calendar.set(Calendar.DAY_OF_MONTH,1);
			return calendar.getTime();
		}else{
			return date;
		}
	}
	 /**
     * 方法名: 
     * <p>功能说明：</p>
     * @param calendar
     * @return
    */
	public static Date getIqcScheduleDate() {
	   // TODO Auto-generated method stub
	   String scheduleTimeStr = PropUtils.getProp("schedule.properties","iqc.interval");
	   return getScheduleDateByMinutes(scheduleTimeStr);
	}
	/**
		  * 方法名: 
		  * <p>功能说明：IPQC调度定时执行时间</p>
	      *
		  * @return
	      * @version 1.00 2017年9月2日 发布
	 */
	public static Date getIpqcAutoSubmitScheduleDate() {
		// TODO Auto-generated method stub
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","ipqc.interval");
	   return getScheduleDateByMinutes(scheduleTimeStr);
	}
	/**
	 * 方法名: getMailScheduleDate 
	 * <p>功能说明：发送邮件调度定时执行时间</p> 
	 * @return Date
	 * @throws
	 */
	public static Date getMailScheduleDate(){
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","mail.interval");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}
	
	/**
	  * 方法名: 根据分钟间隔获取执行调度的准确时间
	  * <p>功能说明：
	  * </p>
	  * @param minuteStr
	  * @return
	 */
	private static Date getScheduleDateByMinutes(String minuteStr){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,(int)(Double.valueOf(minuteStr)*60));
		return calendar.getTime();
	}
	private static Date getScheduleDateByHours(String minuteStr){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,(int)(Double.valueOf(minuteStr)*60*60));
		return calendar.getTime();
	}	
	public static void main(String[] args) {
		String ss = "12342342314中国";
		System.out.println(ss.length());
	}
	public static Date getSynchroSpcDatasScheduleDate() {
		String scheduleTimeStr = PropUtils.getProp("schedule.properties","spc.time");
		return getScheduleDateByMinutes(scheduleTimeStr);
	}

}
