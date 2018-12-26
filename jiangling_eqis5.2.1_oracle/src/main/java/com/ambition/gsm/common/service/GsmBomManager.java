package com.ambition.gsm.common.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.common.dao.GsmBomDao;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmEquipment;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 计量器具编码(SERVICE)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class GsmBomManager {
	@Autowired
	private GsmBomDao gsmBomDao;
	
	public Page<GsmEquipment> getPageByParams(Page<GsmEquipment> page, JSONObject params){
		return gsmBomDao.getPageByParams(page, params);
	}
	
	public Page<GsmEquipment> getPageByGsmCodeRules(Page<GsmEquipment>page,GsmCodeRules gsmCodeRules){
		return gsmBomDao.getPageByGsmCodeRules(page, gsmCodeRules);
	}
	
	/**
	 * 国际化查询
	 * @param option
	 */
	public void i18nChange(List<com.norteksoft.product.api.entity.Option> option){
		String language = "中文";
		if("en".equals(Struts2Utils.getRequest().getLocale().getLanguage())){
			language = "英文";
		}
		String code="";
		for(com.norteksoft.product.api.entity.Option op: option){
			code = code + "'" + op.getName() + "',";
		}
		code = code.substring(0, code.length()-1);
		String sql = "select b.code,o.value from bs_internation b  inner join bs_internation_option o "
		  +" on b.id = o.fk_internation_id"
		  +" and b.code in (" + code +")"
		  +" and o.category_name = '" + language + "'";
		List<Object> list = gsmBomDao.findBySql(sql);
		for (Object object : list) {
			Object[] objs = (Object[])object;
			for(com.norteksoft.product.api.entity.Option op:option){
				if(op.getName().equals(objs[0])){
					op.setName(objs[1].toString());
					break;
				}
			}
		}
	}
}
