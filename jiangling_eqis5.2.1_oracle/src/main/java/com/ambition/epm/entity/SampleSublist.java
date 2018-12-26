package com.ambition.epm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * 类名:样品借还
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  XJN
 * @version 1.00 2017年5月24日 发布
 */
@Entity
@Table(name="EPM_SAMPLE_SUBLIST")
public class SampleSublist extends IdEntity{

		/**
		  *SampleSublist.java2017年5月24日
		 */
	private static final long serialVersionUID = 1L;
	private Date returnDate;//归还日期
	private int returnQuantity;//归还数量
	private String remark;//备注
	
	@ManyToOne
	@JoinColumn(name="EPM_SUBLIST_ID")
    @JsonIgnore()
	private Sample sample;

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public int getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(int returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
	
}
