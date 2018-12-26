package com.ambition.gsm.useRecord.service;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.GsmUseRecord;
import com.ambition.gsm.equipment.dao.GsmMailSettingsDao;
import com.ambition.gsm.useRecord.dao.GsmUseRecordDao;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;

/**
 * 计量器具用户使用记录(SERVICE)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class GsmUseRecordManager {
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmUseRecordDao gsmUseRecordDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private GsmMailSettingsDao gsmMailSettingsDao;
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public GsmUseRecord getGsmUseRecord(Long id){
		return gsmUseRecordDao.get(id);
	}
	
	/**
	 * 一个量具多次被借
	 * @param no
	 * @return
	 */
	public GsmUseRecord getGsmUseRecordByNo(String no){
		return gsmUseRecordDao.getGsmUseRecordByNo(no);
	}
	
	/**
	 * 保存对象
	 * @param gsmUseRecord
	 */
	public void saveGsmUseRecord(GsmUseRecord gsmUseRecord){
		gsmUseRecordDao.save(gsmUseRecord);
	}
	
	/**
	 * 删除对象
	 * @param deleteIds
	 */
	public void deleteGsmUseRecord(String deleteIds){
		for(String id:deleteIds.split(",")){
			gsmUseRecordDao.delete(Long.valueOf(id));
		}
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmUseRecord> getPage(Page<GsmUseRecord> page){
		return gsmUseRecordDao.getPage(page);
	}
	
	/**
	 * 转化JSONObject
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;	
	}
	
	public GsmMailSettings getGsmMailSettingsByBusinessCode(String businessCode) {
		return gsmMailSettingsDao.getByBusinessCode(businessCode);
	}
}
