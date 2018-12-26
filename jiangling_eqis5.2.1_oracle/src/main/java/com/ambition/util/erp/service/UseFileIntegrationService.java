package com.ambition.util.erp.service;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.util.useFile.dao.UseFileDao;

/**
 * 类名:定时同步数据变更记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：每天一次定时同步数据变更记录</p>
 * @author  赵骏
 * @version 1.00 2013-8-21 发布
 */
@Service
public class UseFileIntegrationService implements Runnable {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private UseFileDao useFileDao;
	
	public void run() {
		Session session = null;
		try{
			session = useFileDao.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			//删除1个小时以前创建的没有使用的文件
			String hql = "delete from UseFile u where u.isUse = ? and u.createdTime > ?";
			org.hibernate.Query query = session.createQuery(hql);
			query.setParameter(0,false);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR,-1);
			query.setParameter(1,calendar.getTime());
			query.executeUpdate();
			transaction.commit();
		}catch (Exception e) {
			log.error("删除过期文件失败!",e);
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}
}
