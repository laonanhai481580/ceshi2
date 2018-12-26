package com.ambition.carmfg.inspectionbase.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectingItem;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.inspectionbase.dao.MfgInspectingItemDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * InspectingItemManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class MfgInspectingItemManager {
	@Autowired
	private MfgInspectingItemDao mfgInspectingItemDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<MfgInspectingItem> search(Page<MfgInspectingItem> page,Long parentId){
		return mfgInspectingItemDao.search(page,parentId);
	}
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<MfgInspectingItem> searchByParams(Page<MfgInspectingItem> page){
		return mfgInspectingItemDao.searchByParams(page);
	}
	/**
	 * 检查是否存在相同项目
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistInspectingItem(Long id,String name,MfgInspectingItem parent){
		String hql = "select count(*) from MfgInspectingItem i where i.companyId = ? and i.itemName = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(name);
		if(id != null){
			hql += " and i.id <> ?";
			params.add(id);
		}
		if(parent == null){
			hql += " and i.itemParent is null";
		}else{
			hql += " and i.itemParent = ?";
			params.add(parent);
		}
		Query query = mfgInspectingItemDao.createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	public MfgInspectingItem getInspectingItem(Long id){
		return mfgInspectingItemDao.get(id);
	}
	
	/**
	 * 根据名称和上级查询项目名称
	 * @param name,parent
	 * @return
	 */
	public MfgInspectingItem getInspectingItemByName(String inspectingItemName,MfgInspectingItem parent){
		return mfgInspectingItemDao.getInspectingItemByName(inspectingItemName, parent);
	}
	
	/**
	 * 保存检验项目
	 * @param inspectingIndicator
	 */
	public void saveInspectingItem(MfgInspectingItem mfgInspectingItem){
		if(StringUtils.isEmpty(mfgInspectingItem.getItemName())){
			throw new RuntimeException("检验项目名称不能为空!");
		}
		if(isExistInspectingItem(mfgInspectingItem.getId(),mfgInspectingItem.getItemName(),mfgInspectingItem.getItemParent())){
			throw new RuntimeException("已经存在相同的检验项目名称!");
		}
		mfgInspectingItemDao.save(mfgInspectingItem);
	}
	
	/**
	 * 删除检验项目
	 * @param id
	 */
	public void deleteInspectingItem(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			MfgInspectingItem mfgInspectingItem = mfgInspectingItemDao.get(Long.valueOf(id));
			if(mfgInspectingItem.getId() != null){
//				if(!inspectingItem.getChildren().isEmpty()){
//					throw new RuntimeException("还有子节点不能删除，请先删除子节点!");
//				}
				logUtilDao.debugLog("删除", mfgInspectingItem.toString());
				mfgInspectingItemDao.delete(mfgInspectingItem);
			}
		}
	}
	
	/**
	 * 获取检验项目
	 * @return
	 */
	public List<MfgInspectingItem> getTopInspectingItems(){
		return mfgInspectingItemDao.getTopInspectingItems();
	}
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<MfgInspectingItem> getInspectingItemTypes(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from MfgInspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and i.itemParent is null");
		}else{
			sb.append(" and i.itemParent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by i.orderNum");
		return mfgInspectingItemDao.find(sb.toString(),params.toArray());
	}
	
	public List<Option> getCountTypeOptions(){
		List<Option> options = new ArrayList<Option>();
		Option option = new Option();
		option.setValue(InspectingItem.COUNTTYPE_COUNT);
		option.setName(InspectingItem.COUNTTYPE_COUNT);
		options.add(option);
		
		option = new Option();
		option.setValue(InspectingItem.COUNTTYPE_METERING);
		option.setName(InspectingItem.COUNTTYPE_METERING);
		options.add(option);
		return options;
	}
	private void updateChildrenLevel(MfgInspectingItem mfgInspectingItem,int level){
		if(!mfgInspectingItem.getItemChildren().isEmpty()){
			String hql = "update MfgInspectingItem i set i.level = ? where i.parent = ?";
			Query query = mfgInspectingItemDao.createQuery(hql,level,mfgInspectingItem);
			query.executeUpdate();
			for(MfgInspectingItem child : mfgInspectingItem.getItemChildren()){
				updateChildrenLevel(child,level+1);
			}
		}
	}
	/**
	 * 移动检验项目
	 * @param moveIds
	 * @param parentId
	 */
	public void moveInspectingItems(String moveIds,Long parentId){
		MfgInspectingItem parent = null;
		if(parentId != null){
			parent = mfgInspectingItemDao.get(parentId);
		}
		for(String id : moveIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				MfgInspectingItem mfgInspectingItem = mfgInspectingItemDao.get(Long.valueOf(id));
				mfgInspectingItem.setItemParent(parent);
				int hisLevel = mfgInspectingItem.getItemLevel();
				if(parent==null){
					mfgInspectingItem.setItemLevel(1);
				}else{
					int level = parent.getItemLevel()+1;
					mfgInspectingItem.setItemLevel(level);
				}
				mfgInspectingItemDao.save(mfgInspectingItem);
				
				//更新level
				int newLevel = mfgInspectingItem.getItemLevel();
				if(hisLevel != newLevel){//更新子级的
					updateChildrenLevel(mfgInspectingItem,newLevel+1);
				}
			}
		}
	}
	/**
	 * 转换json
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
}
