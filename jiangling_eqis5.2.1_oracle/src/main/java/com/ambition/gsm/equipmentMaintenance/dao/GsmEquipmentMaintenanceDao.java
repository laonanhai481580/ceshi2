package com.ambition.gsm.equipmentMaintenance.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmEquipmentMaintenance;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 量检具维修管理(DAO)
 * @author 张顺治
 *
 */
@Repository
public class GsmEquipmentMaintenanceDao  extends HibernateDao<GsmEquipmentMaintenance, Long> {
	
	/**
	 * 分页对象
	 * @param page
	 * @param maintenanceState
	 * @param operator
	 * @return
	 */
	public Page<GsmEquipmentMaintenance> getPage(Page<GsmEquipmentMaintenance> page,String maintenanceState,String operator){
		StringBuilder sbHql = new StringBuilder("from GsmEquipmentMaintenance g where g.companyId = ?");
		List<Object> searchParames = new ArrayList<Object>();
		searchParames.add(ContextUtils.getCompanyId());
		if(StringUtils.isNotEmpty(maintenanceState) && StringUtils.isNotEmpty(operator)){
			if("IN".equals(operator.toUpperCase())){
				sbHql.append(" and g.maintenanceState in('"+(maintenanceState.contains(",")?maintenanceState.replaceAll(",", "','"):maintenanceState)+"') ");
			}else if("NOTIN".equals(operator.toUpperCase())){
				sbHql.append(" and g.maintenanceState not in('"+(maintenanceState.contains(",")?maintenanceState.replaceAll(",", "','"):maintenanceState)+"') ");
			}
		}
		return searchPageByHql(page, sbHql.toString(), searchParames.toArray());
	}
	
	/**
	 * 上次维修时间
	 * @param measurementNo
	 * @return
	 */
	public GsmEquipmentMaintenance getmaintenanceMaxDate( String measurementNo){
		String sql = "from GsmEquipmentMaintenance t where t.companyId = ? and gsmEquipment.measurementNo = ? and id in (select max(id) from GsmEquipmentMaintenance where t.companyId = ? and gsmEquipment.measurementNo = ?)";
		List<GsmEquipmentMaintenance> list = this.find(sql, ContextUtils.getCompanyId(), measurementNo,ContextUtils.getCompanyId(), measurementNo); 
		return list != null && list.size() > 0?list.get(0):null;
	}
	
	/**
	 * 已维修的次数
	 * @param measurementNo
	 * @return
	 */
	public long countMaintenance(String measurementNo){
		String sql = "select count(*) from GsmEquipmentMaintenance t where t.companyId = ? and gsmEquipment.measurementNo = ?";
		return (this.countHqlResult(sql, ContextUtils.getCompanyId(), measurementNo))+1;
	}
}
