/**   
 * @Title: UpdateTimestampDao.java 
 * @Package com.ambition.util.erp.dao 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2015-3-17 上午11:51:19 
 * @version V1.0   
 */ 
package com.ambition.util.erp.dao;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名: UpdateTimestampDao 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  刘承斌
 * @version 1.00  2015-3-17 上午11:51:19  发布
 */
@Repository
public class UpdateTimestampDao extends HibernateDao<UpdateTimestamp, Long> {

}
