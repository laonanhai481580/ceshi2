package com.ambition.spc.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;

/**    
 * MonitPoint.java
 * @authorBy wanglf
 *
 */
@Entity
@Table(name = "SPC_MONIT_POINT")
public class MonitPoint extends IdEntity {

	/**
	 * 监控点设置
	 */
	private static final long serialVersionUID = 1L;
	//基本信息
	private String pointName;//监控点名称
	private String pointCode;//监控点编号
	private String pointEditer;//监控点编辑人
	private Date pointEditDate;//监控点编辑日期
	private Integer myLeft;//相对位置x值
	private Integer myTop;//相对位置y值
	private Integer imageWidth;//图片宽度
	private Integer imageHeight;//图片高度
	private String remark;
	private String isCpk;//Y:是显示 N:不显示
	@ManyToOne
	@JoinColumn(name="FK_MONIT_PROGRAM_ID")
	private MonitProgram monitProgram;//监控方案
	
	
	@OneToMany(mappedBy="monitPoint",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<MonitQualityFeature> monitQualityFeature;
	
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	public String getPointCode() {
		return pointCode;
	}
	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
	}
	public String getPointEditer() {
		return pointEditer;
	}
	public void setPointEditer(String pointEditer) {
		this.pointEditer = pointEditer;
	}
	public Date getPointEditDate() {
		return pointEditDate;
	}
	public void setPointEditDate(Date pointEditDate) {
		this.pointEditDate = pointEditDate;
	}
	public MonitProgram getMonitProgram() {
		return monitProgram;
	}
	public void setMonitProgram(MonitProgram monitProgram) {
		this.monitProgram = monitProgram;
	}
	public List<MonitQualityFeature> getMonitQualityFeature() {
		return monitQualityFeature;
	}
	public void setMonitQualityFeature(List<MonitQualityFeature> monitQualityFeature) {
		this.monitQualityFeature = monitQualityFeature;
	}
	public Integer getMyLeft() {
		return myLeft;
	}
	public void setMyLeft(Integer myLeft) {
		this.myLeft = myLeft;
	}
	public Integer getMyTop() {
		return myTop;
	}
	public void setMyTop(Integer myTop) {
		this.myTop = myTop;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}
	public Integer getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}
	public String getIsCpk() {
		return isCpk;
	}
	public void setIsCpk(String isCpk) {
		this.isCpk = isCpk;
	}
	
}
