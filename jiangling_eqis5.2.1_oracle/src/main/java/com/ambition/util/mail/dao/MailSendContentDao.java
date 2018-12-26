/**   
 * @Title: MailSendContentDao.java 
 * @Package com.ambition.util.mail.dao 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2015-3-17 上午9:45:43 
 * @version V1.0   
 */ 
package com.ambition.util.mail.dao;

import org.springframework.stereotype.Repository;

import com.ambition.util.mail.entity.MailSendContent;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名: MailSendContentDao 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：获得邮件发送内容的Dao</p>
 * @author  刘承斌
 * @version 1.00  2015-3-17 上午9:45:43  发布
 */
@Repository
public class MailSendContentDao extends HibernateDao<MailSendContent, Long> {

}
