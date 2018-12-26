package com.ambition.spc.jsanalyse.entity;

import java.io.Serializable;
import java.util.ArrayList;
/**    
 * JSOriginalData.java
 * @authorBy wanglf
 *
 */
public class JSOriginalData implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int chartType;
  private double UCL;
  private double LCL;
  private double CL;
  @SuppressWarnings("rawtypes")
  private ArrayList dataList;
  private String title;
  private boolean caseSelect = false;
  private String formula;

  public double getUCL()
  {
    return this.UCL;
  }

  public void setUCL(double UCL) {
    this.UCL = UCL;
  }

  public double getLCL() {
    return this.LCL;
  }

  public void setLCL(double LCL) {
    this.LCL = LCL;
  }

  public double getCL() {
    return this.CL;
  }

  public void setCL(double CL) {
    this.CL = CL;
  }

  public int getChartType() {
    return this.chartType;
  }

  public void setChartType(int chartType) {
    this.chartType = chartType;
  }

  @SuppressWarnings("rawtypes")
public ArrayList getDataList() {
    return this.dataList;
  }

  @SuppressWarnings("rawtypes")
public void setDataList(ArrayList dataList) {
    this.dataList = dataList;
  }

  public boolean getCaseSelect() {
    return this.caseSelect;
  }

  public void setCaseSelect(boolean caseSelect) {
    this.caseSelect = caseSelect;
  }

  public String getFormula() {
    return this.formula;
  }

  public void setFormula(String formula) {
    this.formula = formula;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}