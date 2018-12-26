package com.ambition.epm.entrustOrt.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.epm.entity.EntrustOrt;
import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entity.Sample;
import com.ambition.epm.entrustOrt.dao.EntrustOrtDao;
import com.ambition.epm.entrustOrt.dao.EntrustOrtSublistDao;
import com.ambition.epm.sample.dao.SampleDao;
import com.ambition.epm.sample.services.SampleManager;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
@Service
public class EntrustOrtConsentManager implements AfterTaskCompleted {
	@Autowired
	private EntrustOrtDao entrustOrtDao;
	@Autowired
	private SampleManager sampleManager;
	@Autowired
	private EntrustOrtSublistDao entrustOrtSublistDao;
	@Autowired
	private SampleDao sampleDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public void execute(Long arg0, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		EntrustOrt report = entrustOrtDao.get(arg0);
//		List<EntrustOrtSublist> entrustOrtSublists = report.getEntrustOrtSublists();
//		for(int i=0;entrustOrtSublists.size()>i;i++){
//			EntrustOrtSublist entrustOrtSublist = entrustOrtSublistDao.get(report.getEntrustOrtSublists().get(i).getId());
			if(transact.toString().equals("submit")){
				Sample sample = new Sample();
				sample.setCompanyId(ContextUtils.getCompanyId());//公司ID
				sample.setCompleteDate(new Date());
				sample.setCreator(ContextUtils.getLoginName());
				sample.setCreatorName(ContextUtils.getUserName());
				sample.setModifiedTime(new Date());
				sample.setModifier(ContextUtils.getUserName());
				sample.setFormNo(formCodeGenerated.generateSampleNo());//表单单号
				
				sample.setReportNo(report.getFormNo());//表单编号
				sample.setSendDate(report.getConsignableDate());
				sample.setInspectionPerson(report.getConsignor());
				sample.setInspectionPersonLogin(report.getTransactor());
				sample.setCustomerNo(report.getCustomerNo());//客户
				sample.setProductNo(report.getProductNo());//机种
				sample.setQuantity(report.getQuantity());//样品数量
				sample.setInspectionDapt(report.getConsignorDept());//部门
				sample.setSampleHandling(report.getSampleHandling());//样品处理方式
				sampleDao.getSession().save(sample);
			}
	}
	
}
