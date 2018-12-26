package com.ambition.carmfg.madeinspection.dao;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.MfgManufactureMessage;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016-9-3 发布
 */
@Repository
public class MfgManufactureMessageDao extends HibernateDao<MfgManufactureMessage, Long> {
	
	public Page<MfgManufactureMessage> searchMfgManufactureMessage(Page<MfgManufactureMessage> page){
		String hql =" from MfgManufactureMessage sm";
		return this.searchPageByHql(page, hql);
	}
}
