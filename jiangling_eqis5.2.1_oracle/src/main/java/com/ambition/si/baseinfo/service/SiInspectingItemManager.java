package com.ambition.si.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectingItem;
import com.ambition.si.baseinfo.dao.SiInspectingItemDao;
import com.ambition.si.entity.SiInspectingItem;
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
public class SiInspectingItemManager {
	@Autowired
	private SiInspectingItemDao siInspectingItemDao;
	@Autowired
	private LogUtilDao logUtilDao;
	
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<SiInspectingItem> search(Page<SiInspectingItem> page,Long parentId){
		return siInspectingItemDao.search(page,parentId);
	}
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<SiInspectingItem> searchByParams(Page<SiInspectingItem> page){
		return siInspectingItemDao.searchByParams(page);
	}
	/**
	 * 检查是否存在相同项目
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistInspectingItem(Long id,String name,SiInspectingItem parent){
		String hql = "select count(*) from SiInspectingItem i where i.companyId = ? and i.itemName = ?";
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
		Query query = siInspectingItemDao.createQuery(hql);
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
	public SiInspectingItem getInspectingItem(Long id){
		return siInspectingItemDao.get(id);
	}
	
	/**
	 * 根据名称和上级查询项目名称
	 * @param name,parent
	 * @return
	 */
	public SiInspectingItem getInspectingItemByName(String inspectingItemName,SiInspectingItem parent){
		return siInspectingItemDao.getInspectingItemByName(inspectingItemName, parent);
	}
	
	/**
	 * 保存检验项目
	 * @param inspectingIndicator
	 */
	public void saveInspectingItem(SiInspectingItem siInspectingItem){
		if(StringUtils.isEmpty(siInspectingItem.getItemName())){
			throw new RuntimeException("检验项目名称不能为空!");
		}
		if(isExistInspectingItem(siInspectingItem.getId(),siInspectingItem.getItemName(),siInspectingItem.getItemParent())){
			throw new RuntimeException("已经存在相同的检验项目名称!");
		}
		siInspectingItemDao.save(siInspectingItem);
	}
	
	/**
	 * 删除检验项目
	 * @param id
	 */
	public void deleteInspectingItem(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			SiInspectingItem siInspectingItem = siInspectingItemDao.get(Long.valueOf(id));
			if(siInspectingItem.getId() != null){
//				if(!inspectingItem.getChildren().isEmpty()){
//					throw new RuntimeException("还有子节点不能删除，请先删除子节点!");
//				}
				logUtilDao.debugLog("删除", siInspectingItem.toString());
				siInspectingItemDao.delete(siInspectingItem);
			}
		}
	}
	
	/**
	 * 获取检验项目
	 * @return
	 */
	public List<SiInspectingItem> getTopInspectingItems(){
		return siInspectingItemDao.getTopInspectingItems();
	}
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<SiInspectingItem> getInspectingItemTypes(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from SiInspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and i.itemParent is null");
		}else{
			sb.append(" and i.itemParent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by i.orderNum");
		return siInspectingItemDao.find(sb.toString(),params.toArray());
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
	private void updateChildrenLevel(SiInspectingItem siInspectingItem,int level){
		if(!siInspectingItem.getItemChildren().isEmpty()){
			String hql = "update SiInspectingItem i set i.level = ? where i.parent = ?";
			Query query = siInspectingItemDao.createQuery(hql,level,siInspectingItem);
			query.executeUpdate();
			for(SiInspectingItem child : siInspectingItem.getItemChildren()){
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
		SiInspectingItem parent = null;
		if(parentId != null){
			parent = siInspectingItemDao.get(parentId);
		}
		for(String id : moveIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				SiInspectingItem siInspectingItem = siInspectingItemDao.get(Long.valueOf(id));
				siInspectingItem.setItemParent(parent);
				int hisLevel = siInspectingItem.getItemLevel();
				if(parent==null){
					siInspectingItem.setItemLevel(1);
				}else{
					int level = parent.getItemLevel()+1;
					siInspectingItem.setItemLevel(level);
				}
				siInspectingItemDao.save(siInspectingItem);
				
				//更新level
				int newLevel = siInspectingItem.getItemLevel();
				if(hisLevel != newLevel){//更新子级的
					updateChildrenLevel(siInspectingItem,newLevel+1);
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
