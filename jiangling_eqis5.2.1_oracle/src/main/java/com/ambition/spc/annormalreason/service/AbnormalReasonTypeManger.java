package com.ambition.spc.annormalreason.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.annormalreason.dao.AbnormalReasonTypeDao;
import com.ambition.spc.entity.AbnormalReason;
import com.ambition.spc.entity.AbnormalReasonType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;

/**    
 * AbnormalReasonTypeManger.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class AbnormalReasonTypeManger {
	@Autowired
	private AbnormalReasonTypeDao abnormalReasonTypeDao;
	
	public AbnormalReasonType getAbnormalReasonType(Long id){
		return abnormalReasonTypeDao.get(id);
	}
	
	public void deleteAbnormalReasonType(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			AbnormalReasonType reasonType = abnormalReasonTypeDao.get(Long.valueOf(id));
			if(reasonType.getId() != null){
				if(!reasonType.getChildren().isEmpty()){
					throw new RuntimeException(reasonType.getTypeName() + "还有子节点不能删除，请先删除子节点!");
				}
				abnormalReasonTypeDao.delete(reasonType);
			}
		}
	}
	
	public void saveAbnormalReasonType(AbnormalReasonType reasonType){
		abnormalReasonTypeDao.save(reasonType);
	}
	
	/**
	 * 根据父类查询结构
	 * @param parentId
	 * @return
	 */
	public List<AbnormalReasonType> getAbnormalReasonTypes(Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("from AbnormalReasonType a where a.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
			sb.append(" and a.parent is null");
		}else{
			sb.append(" and a.parent.id = ?");
			params.add(parentId);
		}
		sb.append(" order by a.orderNum");
		return abnormalReasonTypeDao.find(sb.toString(),params.toArray());
	}
	
	public String getReasonResultJson(Page<AbnormalReasonType> page,AbnormalReasonType reasonType){
		List<JSONObject> list = new ArrayList<JSONObject>();
		List<AbnormalReason> reasons = reasonType.getAbnormalReasons();
		for(AbnormalReason reason : reasons){
			HashMap<String,Object> hs = new HashMap<String,Object>();
			hs.put("id", reason.getId());
			hs.put("reasonNo", reason.getReasonNo());
			hs.put("reasonName", reason.getReasonName());
			hs.put("reasonType", reasonType.getTypeName());
			hs.put("reason", reason.getReason());
			hs.put("measures", reason.getMeasures());
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
			JSONObject jObject = JSONObject.fromObject(sb.toString());
			list.add(jObject);
		}
		//添加jqGrid所需的页信息
		StringBuilder json = new StringBuilder();
		json.append("{\"page\":\"");
		json.append(page.getPageNo());
		json.append("\",\"total\":");
		json.append(reasons.size());
		json.append(",\"records\":\"");
		json.append(reasons.size());
		json.append("\",\"rows\":");
		json.append(JSONArray.fromObject(list).toString());
		json.append("}");
		return json.toString();
	}
	
	/**
	 * 转换结构至json对象
	 * @param abnormalReasonType
	 * @return
	 */
	public Map<String,Object> convertReasonType(AbnormalReasonType reasonType){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",reasonType.getTypeName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",reasonType.getId());
		attrMap.put("level",reasonType.getLevel());
		attrMap.put("name",reasonType.getTypeName());
		map.put("attr", attrMap);
		if(!reasonType.getChildren().isEmpty()){
			map.put("state","open");
			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
			for(AbnormalReasonType child : reasonType.getChildren()){
				children.add(convertReasonType(child));
			}
			map.put("children",children);
		}
		return map;
	}
}
