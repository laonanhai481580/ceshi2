package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Hibernate;

import com.ambition.product.base.IdEntity;

/**
 * 类名:目标表字段名称
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2018-7-12 发布
 */
@Entity
@Table(name = "SPC_FEATURE_TABLE_COLUMN")
public class FeatureTableColumn extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String columnName;//字段名称
	private String columnType=Hibernate.STRING.getName();//字段类型,
	private String defaultValue;//默认值
	private Integer columnLen;//字段长度
	private Boolean isDefault=false;
	private Integer orderNum=0;//排序
	@ManyToOne
	@JoinColumn(name="FK_FEATURE_LAYER_ID")
	private QualityFeature qualityFeature;//质量特性
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getColumnLen() {
		return columnLen;
	}
	public void setColumnLen(Integer columnLen) {
		this.columnLen = columnLen;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}
	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}
