package com.ambition.spc.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.ambition.product.base.IdEntity;

/**    
 * ControlLimit.java
 * @authorBy wanglf
 *
 */
@Entity
@Table(name = "SPC_CONTROL_LIMIT")
public class ControlLimit extends IdEntity {

	/**
	 * 控制限
	 */
	private static final long serialVersionUID = 1L;
	private double xucl;
	private double xcl;
	private double xlcl;
	private double sucl;
	private double scl;
	private double slcl;
	@ManyToOne
	@JoinColumn(name="FK_QUALITY_FEATURE_ID")
	private QualityFeature qualityFeature;
	public double getXucl() {
		return xucl;
	}
	public void setXucl(double xucl) {
		this.xucl = xucl;
	}
	public double getXcl() {
		return xcl;
	}
	public void setXcl(double xcl) {
		this.xcl = xcl;
	}
	public double getXlcl() {
		return xlcl;
	}
	public void setXlcl(double xlcl) {
		this.xlcl = xlcl;
	}
	public double getSucl() {
		return sucl;
	}
	public void setSucl(double sucl) {
		this.sucl = sucl;
	}
	public double getScl() {
		return scl;
	}
	public void setScl(double scl) {
		this.scl = scl;
	}
	public double getSlcl() {
		return slcl;
	}
	public void setSlcl(double slcl) {
		this.slcl = slcl;
	}
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}
	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}
	
	
}
