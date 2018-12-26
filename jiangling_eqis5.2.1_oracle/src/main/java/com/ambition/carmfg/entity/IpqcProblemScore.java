package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:IPQC问题严重度分数维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年7月17日 发布
 */
@Entity
@Table(name = "MFG_IPQC_PROBLEM_SCORE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class IpqcProblemScore extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_IPQC_PROBLEM_SCORE";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "IPQC问题严重度分数维护";//实体_列表_名称
	private String problemDegree;//问题严重度
	private Integer problemScore;//问题分数
	public String getProblemDegree() {
		return problemDegree;
	}
	public void setProblemDegree(String problemDegree) {
		this.problemDegree = problemDegree;
	}
	public Integer getProblemScore() {
		return problemScore;
	}
	public void setProblemScore(Integer problemScore) {
		this.problemScore = problemScore;
	}

	
	
}
