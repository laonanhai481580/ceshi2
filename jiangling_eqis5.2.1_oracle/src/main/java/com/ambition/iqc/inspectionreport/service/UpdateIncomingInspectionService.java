package com.ambition.iqc.inspectionreport.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.exception.AmbFrameException;

/**
 * 类名:UpdateIncomingInspection.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-4-9 上午11:10:30
 * </p>
 */
@Service
public class UpdateIncomingInspectionService{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private IncomingInspectionActionsReportDao incomingInspectionActionsReportDao;
	
	
	/**
	 * 检验报告回写ERP
	 */
	public void beginIntegration(Long companyId,String userLoginName,String updateDateStr){
		if(integrationFlag){
			log.error("当前正有别的用户在执行更新供应商信息的操作!");
			return;
		}
		integrationFlag = true;
		//导入
		long time = System.currentTimeMillis();
		Session session = null;
		Transaction transaction = null;
		try {
			session = incomingInspectionActionsReportDao.getSession();
			String sql = "from IncomingInspectionActionsReport i where i.inspectionState = ? and i.createdTime > ? ";
			@SuppressWarnings("unchecked")
			List<IncomingInspectionActionsReport> reports = session.createQuery(sql.toString())
															.setParameter(0, "已完成").setParameter(1,DateUtil.parseDate(updateDateStr)).list();
			log.error("size:" + reports.size());
			for(IncomingInspectionActionsReport iiar : reports){
				String inspectionNo=iiar.getInspectionNo();
				String sqlDetails = "select count(*) from iqc_erp_request_record e where collection_name = ? and details like ?";
				SQLQuery query = session.createSQLQuery(sqlDetails.toString());
				query.setParameter(0,"回写进货检验结果");
				query.setParameter(1,"%" + inspectionNo + "%");
				List<Object> details = query.list();
				if(!(Integer.valueOf(details.get(0).toString())>0)){
					transaction = session.beginTransaction();
					
					transaction.commit();
					log.error("write:" + inspectionNo);
				}else{
					log.error("exist:" + inspectionNo);
				}
			}
		}catch (Throwable e) {
			log.error("检验报告回写ERP失败!",e);
			if(transaction != null && transaction.isActive()){
				transaction.rollback();
			}
			throw new AmbFrameException("检验报告回写ERP失败!",e);
		}finally{
			integrationFlag = false;
		}
		log.debug("检验报告回写ERP用时:" + (System.currentTimeMillis()-time)/1000 + "秒");
	}
	
	private volatile boolean integrationFlag = false;
}
