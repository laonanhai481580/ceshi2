package com.ambition.iqc.inspectionreport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectionTaskEmail;
import com.ambition.iqc.entity.WaitTaskEmailNotice;
import com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ParameterUtils;
import com.norteksoft.product.util.ThreadParameters;
/**
 * 类名:检验任务通知.java
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016-11-19 发布
 */
@Service
@Transactional
public class TaskEmailService implements Runnable{
	
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private  IncomingInspectionActionsReportDao iqcDao;
	private boolean isRun=true;
	@Override
	public void run() {
		try{
			if(isRun){
				return;
			}
			isRun=false;
			//模拟用户登入，设置上下文
			ThreadParameters threadParameters = new ThreadParameters();
			threadParameters.setLoginName("amb.systemAdmin");
			threadParameters.setPageNumber(1);
			threadParameters.setUserName("systemAdmin");
			threadParameters.setUserId(54L);
			threadParameters.setCompanyId(1L);
			ParameterUtils.setParameters(threadParameters);
			String hql1=" from InspectionTaskEmail email";
			List<InspectionTaskEmail> taskEmails=iqcDao.getSession().createQuery(hql1).list();
			String hql2=" from WaitTaskEmailNotice wten where wetn.isTaskEmail=?";
			List<WaitTaskEmailNotice> notices=iqcDao.getSession().createQuery(hql2).setParameter(0, "否").list();
			Map<String,List<WaitTaskEmailNotice>> mapNotice= new HashMap<String,List<WaitTaskEmailNotice>>();
			for(WaitTaskEmailNotice notice:notices){
				if(mapNotice.containsKey(notice.getBomCategory())){
					List<WaitTaskEmailNotice> noticeArray=mapNotice.get(notice.getBomCategory());
					noticeArray.add(notice);
					mapNotice.put(notice.getBomCategory(), noticeArray);
				}else{
					List<WaitTaskEmailNotice> noticeArray= new ArrayList<WaitTaskEmailNotice>();
					noticeArray.add(notice);
					mapNotice.put(notice.getBomCategory(), noticeArray);
				}
			}
			for(InspectionTaskEmail taskEmail:taskEmails){
				if(mapNotice.containsKey(taskEmail.getMaterielCategory())){
					List<WaitTaskEmailNotice> taskNotices=mapNotice.get(taskEmail.getMaterielCategory());
					String mailDeploy="",receivedUserName="";//邮箱配置,接收人
					for(WaitTaskEmailNotice notice:taskNotices){
						String[] userIds = taskEmail.getUserIds().split(",");
						for(String userId : userIds){
							if(StringUtils.isEmpty(userId)){
								continue;
							}
							String content="新增待检验通知，报告单号为【"+notice.getIqcReportNo()+"】,物料号为【"+notice.getBomCode()+"】";
							mailDeploy+=ApiFactory.getAcsService().getUserById(Long.valueOf(userId)).getMailboxDeploy().getCode()+",";//邮箱配置
							receivedUserName+=ApiFactory.getAcsService().getUserById(Long.valueOf(userId)).getName()+",";//姓名
							String emailAddress=ApiFactory.getAcsService().getUserById(Long.valueOf(userId)).getEmail();
							if(emailAddress!=null&&!"".equals(emailAddress)){
								sendMail(emailAddress,content);
							}
						}
						notice.setEmailTaskUserId(taskEmail.getUserIds());
						notice.setEmailTaskName(mailDeploy);
						notice.setEmailTaskTcpType(receivedUserName);
						notice.setIsTaskEmail("是");
						notice.setEmailTaskTime(new Date());
						iqcDao.getSession().save(notice);
					}
				}
			}
		}catch(Exception e){
			log.error("新增进货待检验提醒失败!",e);
		}finally{
			isRun=true;
		}
		
	}
	private void sendMail(String toMail,String content){
		try {
			AsyncMailUtils.sendMail(toMail,"新增进货待检验单提醒",content);
			log.debug("新增进货待检验单邮件【"+content+"】发送成功,接收地址【"+toMail+"】!");
		} catch (Exception e) {
			log.error("邮件发送到新增进货待检验单提醒失败!",e);
		}
	}
}
