package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:C1051抽样标准
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-30 发布
 */
@Entity
@Table(name="IQC_SAMPLE_C1051_SCHEME")
public class SampleC1051Scheme extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String aql;//aql标准
	private Integer amount;//样本量
	private Integer ac;
	private Integer re;
	
	@ManyToOne
	@JoinColumn(name="FK_SAMPLE_C1051_CODE_LETTER_ID")
	private SampleC1051CodeLetter sampleC1051CodeLetter;//c1051抽样标准

	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public SampleC1051CodeLetter getSampleC1051CodeLetter() {
		return sampleC1051CodeLetter;
	}

	public void setSampleC1051CodeLetter(SampleC1051CodeLetter sampleC1051CodeLetter) {
		this.sampleC1051CodeLetter = sampleC1051CodeLetter;
	}

	public Integer getAc() {
		return ac;
	}

	public void setAc(Integer ac) {
		this.ac = ac;
	}

	public Integer getRe() {
		return re;
	}

	public void setRe(Integer re) {
		this.re = re;
	}
	
}
