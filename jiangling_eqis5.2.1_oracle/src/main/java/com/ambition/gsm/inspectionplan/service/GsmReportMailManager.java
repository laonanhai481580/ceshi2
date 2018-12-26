package com.ambition.gsm.inspectionplan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.ambition.gsm.inspectionplan.dao.GsmInnerCheckReportDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
@Service
public class GsmReportMailManager implements AfterTaskCompleted{
	
	@Autowired
	private GsmInnerCheckReportDao gsmInnerCheckReportDao;
	@Override
	public void execute(Long dataId, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		GsmInnerCheckReport report= gsmInnerCheckReportDao.get(dataId);
		if(transact.toString().equals("approve")){
			String message = "单号:"+report.getFormNo()+"仪器名称:"+report.getMeasurementName()+" 校验完成，请放心使用，谢谢！"+PropUtils.getProp("gsmEmailDress2");
			String dutyManEmail = ApiFactory.getAcsService().getUserByLoginName(report.getDutyLoginMan()).getEmail();
			String copyManEmails = report.getCopyLoginMan();
			if(dutyManEmail!=null){
				AsyncMailUtils.sendMail(dutyManEmail,"内校报告",message);
			}
			if(copyManEmails!=null){
				String a[]=copyManEmails.split(",");
				String copyManEmail=null;
				for (int i = 0; i < a.length; i++) {
					String userMan=a[i].toString();
					copyManEmail=ApiFactory.getAcsService().getUserByLoginName(userMan).getEmail();
					if(copyManEmail!=null){
						AsyncMailUtils.sendMail(copyManEmail,"内校报告",message);
					}
				}
			}
		}
	}

}
