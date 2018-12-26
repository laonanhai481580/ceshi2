package com.ambition.spc.annormalreason.dao;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.AbnormalReason;
import com.ambition.spc.entity.AbnormalReasonType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * AbnormalReasonDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class AbnormalReasonDao extends HibernateDao<AbnormalReason, Long> {
	public Page<AbnormalReason> getPage(Page<AbnormalReason> page,AbnormalReasonType type){
		if(type!=null && !type.equals("")){
			return searchPageByHql(page, "from AbnormalReason a where a.companyId = ? and a.abnormalReasonType = ?", ContextUtils.getCompanyId(),type);
		}else{
			return searchPageByHql(page, "from AbnormalReason a where a.companyId = ?", ContextUtils.getCompanyId());
		}
	}
}
