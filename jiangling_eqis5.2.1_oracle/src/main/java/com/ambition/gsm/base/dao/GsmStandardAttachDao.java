package com.ambition.gsm.base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmStandardAttach;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:检验标准文件Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-25 发布
 */
@Repository
public class GsmStandardAttachDao extends HibernateDao<GsmStandardAttach,Long> {
	
	/**
	  * 方法名: 根据ID检验标准文件
	  * <p>功能说明：</p>
	  * @param id
	  * @return
	 */
	public GsmStandardAttach getStandardAttachById(Long id){
		String hql = "from GsmStandardAttach i where i.id = ?";
		List<GsmStandardAttach> attachs = find(hql,id);
		if(attachs.isEmpty()){
			return null;
		}else{
			return attachs.get(0);
		}
	}
}
