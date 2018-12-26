package com.ambition.spc.jlanalyse.entity;

import java.io.Serializable;

/**
 * JLSampleData.java
 * 
 * @authorBy wanglf
 * 
 */
public class JLSampleData implements Serializable {

	private static final long serialVersionUID = 1L;
	private double max;
	private double min;
	private double average;
	private double r;
	private double sSquare;
	private double s;
	private double median;
	private double[] data;
	private String samplingTime;
	private boolean judgeState;
	private Integer num;

	public double getAverage() {
		return this.average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public double getS() {
		return this.s;
	}

	public void setS(double s) {
		this.s = s;
	}

	public double getR() {
		return this.r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public double getMax() {
		return this.max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return this.min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getSSquare() {
		return this.sSquare;
	}

	public void setSSquare(double sSquare) {
		this.sSquare = sSquare;
	}

	public double[] getData() {
		return this.data;
	}

	public void setData(double[] data) {
		this.data = data;
	}

	public double getMedian() {
		return this.median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public String getSamplingTime() {
		String retTime = null;
		try {
			if(samplingTime!=null){
			retTime = this.samplingTime.substring(0, 16);
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return retTime == null ? "" : retTime;
	}

	public void setSamplingTime(String samplingTime) {
		this.samplingTime = samplingTime;
	}

	public void setJudgeState(boolean judgeState) {
		this.judgeState = judgeState;
	}

	public boolean getJudgeState() {
		return this.judgeState;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
}