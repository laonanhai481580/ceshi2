package com.ambition.spc.entity;

import java.io.Serializable;

/**    
 * MointCpkMoudle.java(数据传输类)
 * @authorBy wanglf
 *
 */
public class MointCpkMoudle implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer myLeft;//相对位置x值
	private Integer myTop;//相对位置y值
	private double  cpk;
	private double max;
	private double min;
	private double avg;
	private String color;
	private Integer imageWidth;//图片宽度
	private Integer imageHeight;//图片高度
	private Long qualityFeatureId;//质量参数ID
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
	public double getCpk() {
		return cpk;
	}
	public void setCpk(double cpk) {
		this.cpk = cpk;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getQualityFeatureId() {
		return qualityFeatureId;
	}
	public void setQualityFeatureId(Long qualityFeatureId) {
		this.qualityFeatureId = qualityFeatureId;
	}


}
