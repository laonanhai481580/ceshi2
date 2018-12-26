package com.ambition.carmfg.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.norteksoft.product.api.entity.Option;

/**    
 * CommonManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class ComponentCommonManager {
	/**
	 * 转换map对象到options
	 * @param map
	 * @return
	 */
	public List<Option> convertMapToOptions(Map<String,Object> map){
		List<Option> options = new ArrayList<Option>();
		for(String key : map.keySet()){
			Option option = new Option();
			option.setName(key);
			option.setValue(map.get(key).toString());
			options.add(option);
		}
		return options;
	}
}
