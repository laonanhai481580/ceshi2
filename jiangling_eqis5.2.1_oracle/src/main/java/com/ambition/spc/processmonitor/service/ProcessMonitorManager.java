package com.ambition.spc.processmonitor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.abnormal.dao.AbnormalInfoDao;
import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.ReasonMeasure;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.processmonitor.dao.ProcessMonitorDao;
import com.ambition.spc.util.Calculator;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.util.ContextUtils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * ProcessMonitorManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class ProcessMonitorManager {
	@Autowired
	private ProcessMonitorDao processMonitorDao;
	@Autowired
	private AbnormalInfoDao abnormalInfoDao;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	
	public ReasonMeasure getReasonMeasure(Long id){
		return processMonitorDao.get(id);
	}
	
	public void saveReasonMeasure(ReasonMeasure reasonMeasure){
		String messageId = Struts2Utils.getParameter("messageId");
		if(StringUtils.isNotEmpty(messageId)){
			AbnormalInfo abnormalInfo = abnormalInfoManager.getAbnormalInfo(Long.valueOf(messageId));
			
			reasonMeasure.setAbnormalInfo(abnormalInfo);
			reasonMeasure.setName(abnormalInfo.getName());
			reasonMeasure.setOccurDate(abnormalInfo.getOccurDate());
			if(abnormalInfo.getNum()!=null){
				reasonMeasure.setNum(abnormalInfo.getNum());
			}
			if(abnormalInfo.getQualityFeature()!=null){
				reasonMeasure.setQualityFeature(abnormalInfo.getQualityFeature());
			}
			
			abnormalInfo.setModifiedTime(new Date());
			abnormalInfo.setModifier(ContextUtils.getUserName());
			abnormalInfo.setPriState("1");//修改状态1.已处理
			abnormalInfoDao.save(abnormalInfo);
		}
		processMonitorDao.save(reasonMeasure);
	}
	
	public ReasonMeasure queryReasonMeasureByAbnormalInfo(AbnormalInfo abnormalInfo){
		return processMonitorDao.queryReasonMeasureByAbnormalInfo(abnormalInfo);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getDetailDatas(SpcSubGroup subGroup){
		//封装每组的样本数据
		ArrayList data = new ArrayList();
		List<SpcSgSample> sampleList = subGroup.getSpcSgSamples();
		int effectiveCapacity = 0;
		if(subGroup.getQualityFeature().getEffectiveCapacity() != 0){
			effectiveCapacity = subGroup.getQualityFeature().getEffectiveCapacity();
		}
		double[] a = new double[effectiveCapacity];
		if(sampleList.size() >= effectiveCapacity){
			for(int j=0;j<effectiveCapacity;j++){
				SpcSgSample sample = sampleList.get(j);
				a[j] = sample.getSamValue();
			}
			data.add(a);
		}
		//封装每组样本数据的最大值、最小值、平均值等
		ArrayList<JLSampleData> dataList = new ArrayList<JLSampleData>();
		for(int i=0;i<data.size();i++){
			JLSampleData jl = new JLSampleData();
			double[] b = (double[])data.get(i);
			jl.setData(b);
			jl.setAverage(Calculator.average(b));
			jl.setMax(Calculator.max(b));
			jl.setMin(Calculator.min(b));
			jl.setS(Calculator.calculateS(b));
			jl.setR(Calculator.calculateR(b));
			jl.setMedian(Calculator.calculateMedian(b));
			dataList.add(jl);
		}
		ActionContext.getContext().put("effectiveCapacity", effectiveCapacity);
		ActionContext.getContext().put("groupDatalist", dataList);
		ActionContext.getContext().put("groudNum", subGroup.getSubGroupOrderNum());
		//附属信息
		if(subGroup.getSpcSgTags() != null){
			ActionContext.getContext().put("sgTagDatas", subGroup.getSpcSgTags());
		}
	}
}
