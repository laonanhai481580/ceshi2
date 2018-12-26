package com.ambition.spc.entity;

import java.io.Serializable;

public class Abnormity implements Serializable {

	private static final long serialVersionUID = 1L;
	private int chartType;//控制图类型1、均值极差 2、均值标准差 4、单值移动极差
	private int abnormity;//规则号
	private int param1;//总点数
	private int param2;//总点数中的点数
	private String abnormityCN;//规则名称
	private int abnormityNum;
	private int upLow = 3;

	public Abnormity(int chartType) {
		this.chartType = chartType;
	}

	public int getAbnormity() {
		return this.abnormity;
	}

	public void setAbnormity(int abnormity) {
		this.abnormity = abnormity;
	}

	public int getParam1() {
		return this.param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public int getParam2() {
		return this.param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}

	public String getAbnormityCN() {
		StringBuffer abnormityBuffer = new StringBuffer();
		switch (this.chartType) {
		case 1:
			switch (this.abnormity) {
			case 1:
				abnormityBuffer.append("连续").append(this.param1).append("点上升");
				break;
			case 2:
				abnormityBuffer.append("连续").append(this.param1).append("点下降");
				break;
			case 3:
				abnormityBuffer.append("连续").append(this.param1).append("点不变");
				break;
			case 4:
				abnormityBuffer.append("连续").append(this.param1).append("点交替上下");
				break;
			case 5:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在A区");
				break;
			case 6:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在B区");
				break;
			case 7:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在C区");
				break;
			case 8:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在控制线以外");
				break;
			case 9:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在规格线以外");
				break;
			case 10:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在中心线同一侧的B区以外");
				break;
			case 11:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在中心线同一侧的C区以外");
				break;
			case 12:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在中心线同一侧");
				break;
			case 13:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在中心线两侧的B区以外");
				break;
			case 14:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在中心线两侧的C区以外");
			}
			break;
		case 2:
			switch (this.abnormity) {
			case 1:
				abnormityBuffer.append("连续").append(this.param1).append("点上升");
				break;
			case 2:
				abnormityBuffer.append("连续").append(this.param1).append("点下降");
				break;
			case 3:
				abnormityBuffer.append("连续").append(this.param1).append("点不变");
				break;
			case 4:
				abnormityBuffer.append("连续").append(this.param1).append("点交替上下");
				break;
			case 8:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在控制线以外");
				break;
			case 12:
				abnormityBuffer.append("连续").append(this.param1).append("点中有").append(this.param2).append("点在中心线同一侧");
			}
			break;
		case 4:
			switch (this.abnormity) {
			case 1:
				abnormityBuffer.append("连续").append(this.param1).append("点上升");
				break;
			case 2:
				abnormityBuffer.append("连续").append(this.param1).append("点下降");
				break;
			case 3:
				abnormityBuffer.append("连续").append(this.param1).append("点不变");
				break;
			case 4:
				abnormityBuffer.append("连续").append(this.param1).append("点交替上下");
				break;
			case 5:
//				abnormityBuffer.append("连续").append(this.param1).append("点在红带");
				break;
			case 6:
//				abnormityBuffer.append("连续").append(this.param1).append("点在黄带");
				break;
			case 7:
//				abnormityBuffer.append("连续").append(this.param1).append("点在同侧的红带");
				break;
			case 8:
//				abnormityBuffer.append("连续").append(this.param1).append("点在同侧的黄带");
			}
		}
		this.abnormityCN = abnormityBuffer.toString();
		return this.abnormityCN;
	}

	public int getAbnormityNum() {
		return this.abnormityNum;
	}

	public void setAbnormityNum(int abnormityNum) {
		this.abnormityNum = abnormityNum;
	}

	public int getChartType() {
		return this.chartType;
	}

	public void setChartType(int chartType) {
		this.chartType = chartType;
	}

	public int getUpLow() {
		return this.upLow;
	}

	public void setUpLow(int upLow) {
		this.upLow = upLow;
	}
}