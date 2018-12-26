package com.ambition.carmfg.ipqc.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.IpqcAuditImprove;
import com.ambition.carmfg.ipqc.dao.IpqcAuditImproveDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 类名:IPQC稽核问题点改善Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月9日 发布
 */
@Service
@Transactional
public class IpqcAuditImproveManager extends AmbWorkflowManagerBase<IpqcAuditImprove>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private IpqcAuditImproveDao ipqcAuditImproveDao;
	@Override
	public Class<IpqcAuditImprove> getEntityInstanceClass() {
		return IpqcAuditImprove.class;
	}

	@Override
	public String getEntityListCode() {
		return IpqcAuditImprove.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<IpqcAuditImprove, Long> getHibernateDao() {
		return ipqcAuditImproveDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "ipqc-audit-improve";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "IPQC稽核问题点改善流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "ort-test-entrust.xls", IpqcAuditImprove.ENTITY_LIST_NAME);
	}

	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		for (String id : deleteIds) {
			IpqcAuditImprove report = getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		return sb.toString();
	}
	
}
