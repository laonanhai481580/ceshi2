package com.ambition.aftersales.oba.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.DefectionClassDao;
import com.ambition.aftersales.entity.ObaData;
import com.ambition.aftersales.oba.dao.ObaDataDao;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 
 * 类名:OBA数据Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class ObaDataManager {
	@Autowired
	private ObaDataDao obaDataDao;
	@Autowired
	private DefectionClassDao defectionClassDao;	
	 private static final Map<String,String> fieldMap = new HashMap<String, String>();
	    public ObaDataManager(){
	        fieldMap.put("obaDate", "oba_date");
	        fieldMap.put("place", "place");
	        fieldMap.put("customerName", "customer_name");
	        fieldMap.put("produceDate", "produce_date");
	    }	
	public ObaData getObaData(Long id){
		return obaDataDao.get(id);
	}
	public ObaDataDao getObaDataDao(){
		return obaDataDao;
	}	
	public void deleteObaData(ObaData obaData){
		obaDataDao.delete(obaData);
	}

	public Page<ObaData> search(Page<ObaData>page){
		return obaDataDao.search(page);
	}

	public List<ObaData> listAll(){
		return obaDataDao.getAllObaData();
	}
		
	public void deleteObaData(Long id){
		obaDataDao.delete(id);
	}
	public void deleteObaData(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			ObaData  obaData = obaDataDao.get(Long.valueOf(id));
			if(obaData.getId() != null){
				obaDataDao.delete(obaData);
			}
		}
	}
	public void saveObaData(ObaData obaData){
		obaDataDao.save(obaData);
	}
	
	/**
	 * 根据事业部查询不良项目
	 * @param productLine
	 * @return
	 */
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit){
		String sql = "select t.defection_class,c.defection_item_no,c.defection_item_name from  AFS_DEFECTION_CLASS t  " 
				+" inner join AFS_DEFECTION_ITEM c "
				+" on c.FK_DEFECTION_TYPE_NO = t.id and t.business_unit_name=? "
				+" order by c.id";
		List<?> list = defectionClassDao.getSession()
							.createSQLQuery(sql)
							.setParameter(0,businessUnit)
							.list();
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for(Object obj :  list){
			Object[] objs = (Object[])obj;
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("typeName",objs[0]);
			map.put("itemCode",objs[1]);
			map.put("itemName",objs[2]);
			results.add(map);
		}
		return results;
	}
	/**
	  * 方法名: 计算对应的不良值
	  * <p>功能说明：</p>
	  * @param ids
	  * @param map
	 */
	public void setDefectiveValuesForExport(Map<String,String> map){
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		String sql = "select  t.FK_OBA_DATA_ID,t.defection_Item_No,t.defection_Item_Value from AFS_OBA_DEFECTIVE_ITEMS t where t.FK_OBA_DATA_ID in "
				+ "(select s.id from AFS_OBA_DATA s where  1=1 and s.business_Unit_Name=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(Struts2Utils.getParameter("businessUnit"));
		if (StringUtils.isNotEmpty(searchParameters)) {
			JSONArray array = JSONArray.fromObject(searchParameters);
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				String propName = json.getString("propName");
				if (!fieldMap.containsKey(propName)) {
					throw new AmbFrameException("不包含查询条件[" + propName + "]");
				}
				Object propValue = json.getString("propValue");
				String optSign = json.getString("optSign");
				String dataType = json.getString("dataType");
				if ("DATE".equals(dataType)) {
					propValue = DateUtil.parseDate(propValue + "",
							"yyyy-MM-dd HH:mm:ss");
				}
				if ("like".equals(optSign)) {
					sql += " and s." + fieldMap.get(propName) + " like ?";
					searchParams.add("%" + propValue + "%");
				} else if (">=".equals(optSign)) {
					sql += " and s." + fieldMap.get(propName) + " >= ?";
					searchParams.add(propValue);
				} else if ("<=".equals(optSign)) {
					sql += " and s." + fieldMap.get(propName) + " <= ?";
					searchParams.add(propValue);
				} else {
					sql += " and s." + fieldMap.get(propName) + " = ?";
					searchParams.add(propValue);
				}
			}
		}
		sql += ") and t.defection_Item_Value is not null ";
		SQLQuery query = obaDataDao.getSession().createSQLQuery(sql);
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		List<?> items = query.list();
		//Date af = new Date();
		for (Object obj : items) {
			Object[] objs = (Object[]) obj;
			String key = objs[0] + "";
			String codeValue = "a" + objs[1] + ":" + objs[2];
			if (!map.containsKey(key)) {
				map.put(key, codeValue);
			} else {
				map.put(key, map.get(key) + "," + codeValue);
			}
		}
	}	
	public Page<ObaData> getListByBusinessUnit(Page<ObaData> page,String businessUnit){
			return obaDataDao.searchByBusinessUnit(page, businessUnit);
	}
	
}
