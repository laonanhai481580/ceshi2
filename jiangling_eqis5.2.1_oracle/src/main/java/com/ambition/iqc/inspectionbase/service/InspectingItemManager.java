package com.ambition.iqc.inspectionbase.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectingItem;
import com.ambition.iqc.inspectionbase.dao.InspectingItemDao;
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
public class InspectingItemManager {
	@Autowired
	private InspectingItemDao inspectingItemDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<InspectingItem> search(Page<InspectingItem> page,Long parentId){
		return inspectingItemDao.search(page,parentId);
	}
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<InspectingItem> searchByParams(Page<InspectingItem> page){
		return inspectingItemDao.searchByParams(page);
	}
	
	/**
	 * 检查是否存在相同项目
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistInspectingItem(Long id,String name,InspectingItem parent){
		String hql = "select count(*) from InspectingItem i where i.companyId = ? and i.itemName = ?";
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
		Query query = inspectingItemDao.getSession().createQuery(hql);
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
	public InspectingItem getInspectingItem(Long id){
		return inspectingItemDao.get(id);
	}
	
	/**
	 * 根据名称和上级查询项目名称
	 * @param name,parent
	 * @return
	 */
	public InspectingItem getInspectingItemByName(String inspectingItemName,InspectingItem parent){
		return inspectingItemDao.getInspectingItemByName(inspectingItemName, parent);
	}
	
	/**
	 * 保存检验项目
	 * @param inspectingIndicator
	 */
	public void saveInspectingItem(InspectingItem inspectingItem){
		if(StringUtils.isEmpty(inspectingItem.getItemName())){
			throw new RuntimeException("检验项目名称不能为空!");
		}
		if(isExistInspectingItem(inspectingItem.getId(),inspectingItem.getItemName(),inspectingItem.getItemParent())){
			throw new RuntimeException("已经存在相同的检验项目名称!");
		}
		inspectingItemDao.save(inspectingItem);
	}
	
	/**
	 * 删除检验项目
	 * @param id
	 */
	public void deleteInspectingItem(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			InspectingItem inspectingItem = inspectingItemDao.get(Long.valueOf(id));
			if(inspectingItem.getId() != null){
//				if(!inspectingItem.getChildren().isEmpty()){
//					throw new RuntimeException("还有子节点不能删除，请先删除子节点!");
//				}
				logUtilDao.debugLog("删除", inspectingItem.toString());
				inspectingItemDao.delete(inspectingItem);
			}
		}
	}
	
	/**
	 * 获取检验项目
	 * @return
	 */
	public List<InspectingItem> getTopInspectingItems(){
		return inspectingItemDao.getTopInspectingItems();
	}
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<InspectingItem> getInspectingItemTypes(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from InspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and i.itemParent is null");
		}else{
			sb.append(" and i.itemParent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by i.orderNum");
		return inspectingItemDao.find(sb.toString(),params.toArray());
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
	private void updateChildrenLevel(InspectingItem inspectingItem,int level){
		if(!inspectingItem.getItemChildren().isEmpty()){
			String hql = "update InspectingItem i set i.itemLevel = ? where i.itemParent = ?";
			Query query = inspectingItemDao.createQuery(hql,level,inspectingItem);
			query.executeUpdate();
			for(InspectingItem child : inspectingItem.getItemChildren()){
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
		InspectingItem parent = null;
		if(parentId != null){
			parent = inspectingItemDao.get(parentId);
		}
		for(String id : moveIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				InspectingItem inspectingItem = inspectingItemDao.get(Long.valueOf(id));
				inspectingItem.setItemParent(parent);
				int hisLevel = inspectingItem.getItemLevel();
				if(parent==null){
					inspectingItem.setItemLevel(1);
				}else{
					int level = parent.getItemLevel()+1;
					inspectingItem.setItemLevel(level);
				}
				inspectingItemDao.save(inspectingItem);
				//更新level
				int newLevel = inspectingItem.getItemLevel();
				if(hisLevel != newLevel){//更新子级的
					updateChildrenLevel(inspectingItem,newLevel+1);
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
