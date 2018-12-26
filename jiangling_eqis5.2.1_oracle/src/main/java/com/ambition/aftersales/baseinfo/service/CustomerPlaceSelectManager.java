package com.ambition.aftersales.baseinfo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.CustomerPlace;
import com.norteksoft.mms.base.utils.view.ComboxValues;
import com.norteksoft.product.api.entity.Option;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2014-12-26 发布
 */
@Service
@Transactional
public class CustomerPlaceSelectManager implements ComboxValues{
    @Autowired
    private CustomerPlaceManager customerPlaceManager;

    @Override
    public Map<String, String> getValues(Object entity) {
        StringBuilder result=new StringBuilder();
        Map<String,String> map=new HashMap<String, String>();
        List<CustomerPlace> list = customerPlaceManager.getPlaces();
        List<Option> options = customerPlaceManager.converThirdLevelToList(list);
        result.append("'':''");
        for(Option option:options){
            if(result.length() > 0){
                result.append(",");
            }
            result.append("'"+option.getValue()+"':'"+option.getName()+"'");
        }
        map.put("place", result.toString());
        map.put("customerFactory", result.toString());
        return map;
    } 
}
