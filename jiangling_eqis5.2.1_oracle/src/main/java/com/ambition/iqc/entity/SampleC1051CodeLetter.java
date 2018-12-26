package com.ambition.iqc.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;

/**
 * 类名:MIL-STD-1051标准抽样
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-30 发布
 */
@Entity
@Table(name = "IQC_SAMPLE_C1051_CODE_LETTER")
public class SampleC1051CodeLetter extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Integer batchSize1;//批量下限
	private Integer batchSize2;//批量上限

	@OneToMany(mappedBy="sampleC1051CodeLetter",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
    private List<SampleC1051Scheme> sampleC1051Schemes;//接收质量限AQL
	
	public Integer getBatchSize1() {
		return batchSize1;
	}

	public void setBatchSize1(Integer batchSize1) {
		this.batchSize1 = batchSize1;
	}

	public Integer getBatchSize2() {
		return batchSize2;
	}

	public void setBatchSize2(Integer batchSize2) {
		this.batchSize2 = batchSize2;
	}

	public List<SampleC1051Scheme> getSampleC1051Schemes() {
		return sampleC1051Schemes;
	}

	public void setSampleC1051Schemes(List<SampleC1051Scheme> sampleC1051Schemes) {
		this.sampleC1051Schemes = sampleC1051Schemes;
	}

	public String toString(){
		return "物料抽样标准维护：样本量字码     上限"+this.batchSize2+" 下限"+this.batchSize1;
	}
}
