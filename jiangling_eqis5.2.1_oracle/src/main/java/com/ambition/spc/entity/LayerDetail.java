package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.ambition.spc.entity.LayerType;

import com.ambition.product.base.IdEntity;

/**    
 * LayerDetail.java
 * @authorBy wanglf
 *
 */
@Entity
@Table(name = "SPC_LAYER_DETAIL")
public class LayerDetail extends IdEntity {

	/**
	 * 层别详情
	 */
	private static final long serialVersionUID = 1L;
	//基本信息
	private String detailName;//名称
	private String detailCode;//简码
	@ManyToOne
	@JoinColumn(name="FK_LAYTYPE_TYPE_ID")
	private LayerType layerType;//层别类别
	
	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getDetailCode() {
		return detailCode;
	}

	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}

	public LayerType getLayerType() {
		return layerType;
	}

	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	public String toString() {
		return "SPC：层别详情  ID"+this.getId()+",质量特性编码"+this.detailCode;
	}
}
