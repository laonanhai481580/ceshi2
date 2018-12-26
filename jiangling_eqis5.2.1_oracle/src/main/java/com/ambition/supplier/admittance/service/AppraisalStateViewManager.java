package com.ambition.supplier.admittance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.norteksoft.product.api.entity.Option;
import com.norteksoft.mms.base.utils.view.ComboxValues;

/**
 * 鉴定报告的状态
 * @author 赵骏
 *
 */
@Service
public class AppraisalStateViewManager implements ComboxValues{
	@Autowired
	private SampleAppraisalReportManager sampleAppraisalReportManager;
	@Override
	public Map<String, String> getValues(Object entity) {
		StringBuilder result=new StringBuilder();
		Map<String,String> map=new HashMap<String, String>();
		List<Option> options = sampleAppraisalReportManager.getAppraisStateOptions();
		for(Option option:options){
			if(result.length() > 0){
				result.append(",");
			}
			result.append("'"+option.getValue()+"':'"+option.getName()+"'");
		}
		map.put("appraisalState", result.toString());
		return map;
	}
}
