package com.ambition.spc.distributechart.model;

import java.io.Serializable;

import com.ambition.spc.entity.PictureBaseElement;

public class DistributeChartParam extends PictureBaseElement implements Serializable {
	private static final long serialVersionUID = 1L;
	private double[][] itemList;
	private String xTitle;
	private String yTitle;
	private int type;
	private double[] coefficient;

	public DistributeChartParam(double[][] itemList, String xTitle,
			String yTitle) {
		this.itemList = itemList;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
	}

	public double[][] getItemList() {
		return this.itemList;
	}

	public void setItemList(double[][] itemList) {
		this.itemList = itemList;
	}

	public String getXTitle() {
		return this.xTitle;
	}

	public void setXTitle(String xTitle) {
		this.xTitle = xTitle;
	}

	public String getYTitle() {
		return this.yTitle;
	}

	public void setYTitle(String yTitle) {
		this.yTitle = yTitle;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double[] getCoefficient() {
		return this.coefficient;
	}

	public void setCoefficient(double[] coefficient) {
		this.coefficient = coefficient;
	}
}