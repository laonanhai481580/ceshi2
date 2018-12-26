package com.ambition.supplier.estimate.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.EvaluatingGradeRule;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:自动评分规则DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：自动评分规则的增,删,改,查</p>
 * @author  赵骏
 * @version 1.00 2013-4-20 发布
 */
@Repository
public class EvaluatingGradeRuleDao extends HibernateDao<EvaluatingGradeRule, Long> {}
