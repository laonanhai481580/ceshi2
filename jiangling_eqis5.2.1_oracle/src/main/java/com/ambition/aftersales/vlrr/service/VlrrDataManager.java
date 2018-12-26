package com.ambition.aftersales.vlrr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.DefectionClassDao;
import com.ambition.aftersales.entity.VlrrData;
import com.ambition.aftersales.vlrr.dao.VlrrDataDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 
 * 类名:VLRR数据Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class VlrrDataManager {
	@Autowired
	private VlrrDataDao vlrrDataDao;
	@Autowired
	private DefectionClassDao defectionClassDao;	
	public VlrrData getVlrrData(Long id){
		return vlrrDataDao.get(id);
	}
	
	public void deleteVlrrData(VlrrData vlrrData){
		vlrrDataDao.delete(vlrrData);
	}

	public Page<VlrrData> search(Page<VlrrData>page){
		return vlrrDataDao.search(page);
	}

	public List<VlrrData> listAll(){
		return vlrrDataDao.getAllVlrrData();
	}
		
	public void deleteVlrrData(Long id){
		vlrrDataDao.delete(id);
	}
	public void deleteVlrrData(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			VlrrData  vlrrData = vlrrDataDao.get(Long.valueOf(id));
			if(vlrrData.getId() != null){
				vlrrDataDao.delete(vlrrData);
			}
		}
	}
	public void saveVlrrData(VlrrData vlrrData){
		vlrrDataDao.save(vlrrData);
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
				+" order by c.id ";
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
	
	public Page<VlrrData> getListByBusinessUnit(Page<VlrrData> page,String businessUnit){
			return vlrrDataDao.searchByBusinessUnit(page, businessUnit);
	}

	public VlrrDataDao getVlrrDataDao() {
		// TODO Auto-generated method stub
		return vlrrDataDao;
	}
	
}
