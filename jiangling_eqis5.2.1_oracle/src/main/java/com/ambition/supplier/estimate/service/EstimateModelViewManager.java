package com.ambition.supplier.estimate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.EstimateModel;
import com.norteksoft.mms.base.utils.view.ComboxValues;

/**
 * 季度考评
 * @author 赵骏
 *
 */
@Service
@Transactional
public class EstimateModelViewManager implements ComboxValues{

	@Autowired
	private EstimateModelManager estimateModelManager;
	
	@Override
	public Map<String, String> getValues(Object entity) {
		StringBuilder result=new StringBuilder();
		Map<String,String> map=new HashMap<String, String>();
		List<EstimateModel> estimateModels = estimateModelManager.getTopEstimateModels();
		for(EstimateModel estimateModel : estimateModels){
			if(result.length() > 0){
				result.append(",");
			}
			result.append("'" + estimateModel.getId() + "':'" + estimateModel.getName() + "'");
		}
		map.put("estimateModelId", result.toString());
		return map;
	}
}
