package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.AdmittanceFlowConfigure;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:准入流程配置DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：准入流程配置的增,删,改,查</p>
 * @author  赵骏
 * @version 1.00 2013-4-20 发布
 */
@Repository
public class AdmittanceFlowConfigureDao extends HibernateDao<AdmittanceFlowConfigure, Long> {}
