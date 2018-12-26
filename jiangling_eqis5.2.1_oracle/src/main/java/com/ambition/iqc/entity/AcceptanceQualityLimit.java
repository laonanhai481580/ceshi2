package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * 接收质量限AQL
 * @authorBy YUKE
 *
 */
@Entity
@Table(name="IQC_ACCEPTANCE_QUALITY_LIMIT")
public class AcceptanceQualityLimit extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String code;//样本量字码
	private Integer amount;//样本量
	private String type;//检验类型
	private String countType;//计值类型
	private String baseType;//抽样标准类型
	private String aql;//接收质量数
	private Integer ac;//接收数
	private Integer re;//拒收数
	@ManyToOne
	@JoinColumn(name="FK_SAMPLE_SCHEME_ID")
	private SampleScheme sampleScheme;//抽样方案
	
	public String getAql() {
		return aql;
	}
	public void setAql(String aql) {
		this.aql = aql;
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
	public SampleScheme getSampleScheme() {
		return sampleScheme;
	}
	public void setSampleScheme(SampleScheme sampleScheme) {
		this.sampleScheme = sampleScheme;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCountType() {
		return countType;
	}
	public void setCountType(String countType) {
		this.countType = countType;
	}
	public String getBaseType() {
		return baseType;
	}
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
}
