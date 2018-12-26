package com.ambition.gsm.equipment.dao;

import java.util.List; 
import org.springframework.stereotype.Repository; 
import com.ambition.gsm.entity.GsmMailSettings;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
 
/**
 * 量检具邮件设置(DAO)
 * @author 张顺治
 *
 */
@Repository
public class GsmMailSettingsDao extends HibernateDao<GsmMailSettings, Long> {

	public GsmMailSettings getByBusinessCode(String businessCode){
		List<GsmMailSettings> gsmMailSettingss = this.find("from GsmMailSettings g where g.companyId=? and g.businessCode=?", new Object[]{ContextUtils.getCompanyId(),businessCode});
		return gsmMailSettingss != null && gsmMailSettingss.size() > 0?gsmMailSettingss.get(0):null;
	}
}