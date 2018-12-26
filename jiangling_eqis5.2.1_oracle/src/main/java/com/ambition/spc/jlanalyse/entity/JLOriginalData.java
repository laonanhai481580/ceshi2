package com.ambition.spc.jlanalyse.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * JLOriginalData.java
 * 
 * @authorBy wanglf
 * 
 */
public class JLOriginalData implements Serializable {

	private static final long serialVersionUID = 1L;
	private int chartType;
	private Integer controlState;
	private int sampleQuantity = 1;
	private double xUCL;
	private double xLCL;
	private double xCL;
	private double sUCL;
	private double sLCL;
	private double sCL;
	private double rUCL;
	private double rLCL;
	private double rCL;
	private Double tu;
	private Double tl;
	private Double m;
	@SuppressWarnings("rawtypes")
	private ArrayList dataList;
	private String upTitle;
	private String lowTitle;
	private String formula;
	private int expentType = 1;
	private boolean sixSigma = false;
	private double alldataaverage;

	public int getChartType() {
		return this.chartType;
	}

	public void setChartType(int chartType) {
		this.chartType = chartType;
	}

	public Integer getControlState() {
		return this.controlState;
	}

	public void setControlState(Integer controlState) {
		this.controlState = controlState;
	}

	public int getSampleQuantity() {
		return this.sampleQuantity;
	}

	public void setSampleQuantity(int sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}

	public double getXUCL() {
		return this.xUCL;
	}

	public void setXUCL(double xUCL) {
		this.xUCL = xUCL;
	}

	public double getXLCL() {
		return this.xLCL;
	}

	public void setXLCL(double xLCL) {
		this.xLCL = xLCL;
	}

	public double getXCL() {
		return this.xCL;
	}

	public void setXCL(double xCL) {
		this.xCL = xCL;
	}

	public Double getTu() {
		return this.tu;
	}

	public void setTu(Double tu) {
		this.tu = tu;
	}

	public Double getTl() {
		return this.tl;
	}

	public void setTl(Double tl) {
		this.tl = tl;
	}

	public Double getM() {
		return this.m;
	}

	public void setM(Double m) {
		this.m = m;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getDataList() {
		if(dataList==null){
			return new ArrayList();
		}
		return this.dataList;
	}

	@SuppressWarnings("rawtypes")
	public void setDataList(ArrayList dataList) {
		this.dataList = dataList;
	}

	public double getSUCL() {
		return this.sUCL;
	}

	public void setSUCL(double sUCL) {
		this.sUCL = sUCL;
	}

	public double getSLCL() {
		return this.sLCL;
	}

	public void setSLCL(double sLCL) {
		this.sLCL = sLCL;
	}

	public double getSCL() {
		return this.sCL;
	}

	public void setSCL(double sCL) {
		this.sCL = sCL;
	}

	public double getRUCL() {
		return this.rUCL;
	}

	public void setRUCL(double rUCL) {
		this.rUCL = rUCL;
	}

	public double getRLCL() {
		return this.rLCL;
	}

	public void setRLCL(double rLCL) {
		this.rLCL = rLCL;
	}

	public double getRCL() {
		return this.rCL;
	}

	public void setRCL(double rCL) {
		this.rCL = rCL;
	}

	public String getUpTitle() {
		return this.upTitle;
	}

	public void setUpTitle(String upTitle) {
		this.upTitle = upTitle;
	}

	public String getLowTitle() {
		return this.lowTitle;
	}

	public void setLowTitle(String lowTitle) {
		this.lowTitle = lowTitle;
	}

	public String getFormula() {
		return this.formula;
	}

	public boolean isSixSigma() {
		return this.sixSigma;
	}

	public int getExpentType() {
		return this.expentType;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public void setSixSigma(boolean sixSigma) {
		this.sixSigma = sixSigma;
	}

	public void setExpentType(int expentType) {
		this.expentType = expentType;
	}

	public double getAlldataaverage() {
		return alldataaverage;
	}

	public void setAlldataaverage(double alldataaverage) {
		this.alldataaverage = alldataaverage;
	}
	
}