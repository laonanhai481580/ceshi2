package com.ambition.carmfg.inspectionbase.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IndicatorAttach;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:检验标准文件
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-11-17 发布
 */
@Repository
public class IndicatorAttachDao extends HibernateDao<IndicatorAttach,Long> {
	
	/**
	  * 方法名: 根据ID查询检验标准文件
	  * <p>功能说明：</p>
	  * @param id
	  * @return
	 */
	public IndicatorAttach getIndicatorAttachById(Long id){
		String hql = "from IndicatorAttach i where i.id = ?";
		List<IndicatorAttach> attachs = find(hql,id);
		if(attachs.isEmpty()){
			return null;
		}else{
			return attachs.get(0);
		}
	}
}
