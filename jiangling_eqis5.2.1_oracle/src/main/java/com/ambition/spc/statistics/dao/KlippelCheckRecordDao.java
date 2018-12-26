package com.ambition.spc.statistics.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.KlippelCheckRecord;
import com.ambition.util.common.DateUtil;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 类名:Klippel数据采集合格
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-7-3 发布
 */
@Repository
public class KlippelCheckRecordDao extends HibernateDao<KlippelCheckRecord, Long> {
	/**
	  * 方法名:Klippel数据采集转换
	  * <p>功能说明：从Klippel的数据转换为产品合格率 </p>
	  * @param rs 
	  * 字段格式:id,product_no,machine_no,detect_date,param_name,param_value
	 * @throws SQLException 
	 */
	public void addKlippelCheckRecords(ResultSet rs,Long companyId,String loginUserName) throws SQLException{
		Map<String,KlippelCheckRecord> recMap = new HashMap<String, KlippelCheckRecord>();
		while(rs.next()){
			String productNo = rs.getString(2);
			String machineNo = rs.getString(3);
			Date date = rs.getDate(4);
			String value = rs.getString(6);
			String key = productNo + "-" + machineNo + "-" + DateUtil.formateDateStr(date) + "-" + value;
			if(!recMap.containsKey(key)){
				boolean isPass = "0".equals(value)?false:true;
				String hql = "from KlippelCheckRecord k where k.companyId = ? and k.productNo = ? " +
						"and k.machineNo = ? and k.detectDate = ? and k.isPass = ?";
				List<KlippelCheckRecord> records = find(hql,companyId,productNo,machineNo,date,isPass);
				if(records.isEmpty()){
					KlippelCheckRecord record = new KlippelCheckRecord();
					record.setCompanyId(companyId);
					record.setCreatedTime(date);
					record.setCompanyId(companyId);
					record.setCreator(loginUserName);
					record.setMachineNo(machineNo);
					record.setProductNo(productNo);
					record.setDetectDate(date);
					record.setIsPass(isPass);
					recMap.put(key,record);
				}else{
					recMap.put(key,records.get(0));
				}
			}
			KlippelCheckRecord record = recMap.get(key);
			record.setModifiedTime(new Date());
			record.setModifier(loginUserName);
			record.setAmount(record.getAmount()+1);
		}
		for(KlippelCheckRecord record : recMap.values()){
			this.save(record);
		}
	}
}
