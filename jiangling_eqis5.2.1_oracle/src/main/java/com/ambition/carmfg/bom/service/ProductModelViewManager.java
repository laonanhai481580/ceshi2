package com.ambition.carmfg.bom.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.service.ProductBomManager;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.mms.base.utils.view.ComboxValues;

/**
 * 产品类型
 * @author 赵骏
 *
 */
@Service
@Transactional
public class ProductModelViewManager implements ComboxValues{

	@Autowired
	private ProductBomManager productBomManager;
	
	@Override
	public Map<String, String> getValues(Object entity) {
		StringBuilder result=new StringBuilder();
		Map<String,String> map=new HashMap<String, String>();
		List<Option> options = productBomManager.getProductModelToOptions();
		for(Option option:options){
			if(result.length() > 0){
				result.append(",");
			}
			result.append("'"+option.getValue()+"':'"+option.getName()+"'");
		}
		map.put("productModel", result.toString());
		return map;
	}
}
