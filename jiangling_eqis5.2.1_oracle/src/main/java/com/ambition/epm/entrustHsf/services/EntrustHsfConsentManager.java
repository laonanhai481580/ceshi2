package com.ambition.epm.entrustHsf.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.epm.entity.EntrustHsf;
import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entity.Sample;
import com.ambition.epm.entrustHsf.dao.EntrustHsfDao;
import com.ambition.epm.entrustHsf.dao.EntrustHsfSublistDao;
import com.ambition.epm.sample.dao.SampleDao;
import com.ambition.epm.sample.services.SampleManager;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
@Service
public class EntrustHsfConsentManager implements AfterTaskCompleted {
	@Autowired
	private EntrustHsfDao entrustHsfDao;
	@Autowired
	private EntrustHsfSublistDao entrustHsfSublistDao;
	@Autowired
	private SampleManager sampleManager;
	@Autowired
	private SampleDao sampleDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;

	@Override
	public void execute(Long arg0, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		EntrustHsf report = entrustHsfDao.get(arg0);
		
//		EntrustHsfSublist entrustHsfSublist = entrustHsfSublistDao.get(a);
		List<EntrustHsfSublist> entrustHsfSublists = report.getEntrustHsfSublists();
		
		for(int i=0;entrustHsfSublists.size()>i;i++){
			EntrustHsfSublist entrustHsfSublist = entrustHsfSublistDao.get(report.getEntrustHsfSublists().get(i).getId());
			if(transact.toString().equals("submit")){
				Sample sample = new Sample();
				sample.setCompanyId(ContextUtils.getCompanyId());//公司ID
				sample.setCompleteDate(new Date());
				sample.setCreator(ContextUtils.getLoginName());
				sample.setCreatorName(ContextUtils.getUserName());
				sample.setModifiedTime(new Date());
				sample.setModifier(ContextUtils.getUserName());
				sample.setFormNo(formCodeGenerated.generateSampleNo());//表单单号
//				sample.setReportNo(report.getFormNo());
//				sample.setSendDate(report.getCompleteDate());
				
				sample.setReportNo(entrustHsfSublist.getEntrustHsf().getFormNo());//委托编号
				sample.setSendDate(entrustHsfSublist.getEntrustHsf().getConsignableDate());//申请日期
				sample.setInspectionPerson(entrustHsfSublist.getEntrustHsf().getConsignor());//申请人
				sample.setInspectionPersonLogin(entrustHsfSublist.getEntrustHsf().getTransactor());//申请人登录名
				sample.setCustomerNo(entrustHsfSublist.getClient());//客户
				sample.setSampleName(entrustHsfSublist.getSampleName());//样品名称
				sample.setQuantity(entrustHsfSublist.getAmount());//样品数量
				sample.setInspectionDapt(entrustHsfSublist.getEntrustHsf().getConsignorDept());//部门
				sample.setSampleHandling(entrustHsfSublist.getEntrustHsf().getSampleHandling());//样品处理方式
				sampleDao.getSession().save(sample);
			}
		}
		/*
		if(transact.toString().equals("submit")){
			Sample sample = new Sample();
			sample.setCompanyId(ContextUtils.getCompanyId());//公司ID
			sample.setCompleteDate(new Date());
			sample.setCreator(ContextUtils.getLoginName());
			sample.setCreatorName(ContextUtils.getUserName());
			sample.setModifiedTime(new Date());
			sample.setModifier(ContextUtils.getUserName());
			sample.setFormNo(formCodeGenerated.generateSampleNo());//表单单号
//			sample.setReportNo(report.getFormNo());
//			sample.setSendDate(report.getCompleteDate());
			
			sample.setReportNo(entrustHsfSublist.getEntrustHsf().getFormNo());//委托编号
			sample.setSendDate(entrustHsfSublist.getEntrustHsf().getConsignableDate());//申请日期
			sample.setInspectionPerson(entrustHsfSublist.getEntrustHsf().getConsignor());//申请人
			sample.setInspectionPersonLogin(entrustHsfSublist.getEntrustHsf().getTransactor());//申请人登录名
			sample.setCustomerNo(entrustHsfSublist.getClient());//客户
			sample.setQuantity(entrustHsfSublist.getAmount());//样品数量
			sampleDao.getSession().save(sample);
		} */
	}

}
