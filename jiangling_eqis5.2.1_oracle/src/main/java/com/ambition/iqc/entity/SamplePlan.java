package com.ambition.iqc.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**    
 * SamplePlan.java
 * @authorBy YUKE
 * 抽样计划
 */
@Entity
@Table(name="IQC_SAMPLE_PLAN")
public class SamplePlan extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String code;//样本代字
	private Integer tighten;//加严
	private Integer vl1;//验证水平1
	private Integer vl2;//验证水平2
	private Integer vl3;//验证水平3
	private Integer vl4;//验证水平4
	private Integer vl5;//验证水平5
	private Integer vl6;//验证水平6
	private Integer vl7;//验证水平7
	private Integer relax;//减量
	private String type;//类型
	public static final String MEASURE_TYPE="计量";
	public static final String COUNT_TYPE="计数";
	@Embedded
	private ExtendField extendField;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getTighten() {
		return tighten;
	}
	public void setTighten(Integer tighten) {
		this.tighten = tighten;
	}
	public Integer getVl1() {
		return vl1;
	}
	public void setVl1(Integer vl1) {
		this.vl1 = vl1;
	}
	public Integer getVl2() {
		return vl2;
	}
	public void setVl2(Integer vl2) {
		this.vl2 = vl2;
	}
	public Integer getVl3() {
		return vl3;
	}
	public void setVl3(Integer vl3) {
		this.vl3 = vl3;
	}
	public Integer getVl4() {
		return vl4;
	}
	public void setVl4(Integer vl4) {
		this.vl4 = vl4;
	}
	public Integer getVl5() {
		return vl5;
	}
	public void setVl5(Integer vl5) {
		this.vl5 = vl5;
	}
	public Integer getVl6() {
		return vl6;
	}
	public void setVl6(Integer vl6) {
		this.vl6 = vl6;
	}
	public Integer getVl7() {
		return vl7;
	}
	public void setVl7(Integer vl7) {
		this.vl7 = vl7;
	}
	public Integer getRelax() {
		return relax;
	}
	public void setRelax(Integer relax) {
		this.relax = relax;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	
}
