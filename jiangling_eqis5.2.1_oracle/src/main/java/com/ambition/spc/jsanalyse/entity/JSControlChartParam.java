package com.ambition.spc.jsanalyse.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.entity.PictureBaseElement;
import com.ambition.spc.jsanalyse.util.JSJudgeAbnormity;

/**    
 * JSControlChartParam.java(awt画计数图表参数)
 * @authorBy wanglf
 *
 */
public class JSControlChartParam extends PictureBaseElement implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int offside = 1;
  private int chartType;
  private int samplingNum;
  private double[] dataArray;
  private double ucl;
  private double lcl;
  private double[] uclArray;
  private double[] lclArray;
  private double cl;
  private boolean[] abnormalPos;
  private String[] dateTime;
  private int dotWidth = 20;

  private int xCoorInter = 1;
  private String title;
  private boolean caseSelect = false;
  private String formula;

  @SuppressWarnings("rawtypes")
public JSControlChartParam(JSOriginalData originalData, Abnormity[] abnormity, boolean XCoorIsTime)
  {
    setTopHeight(20);
    this.chartType = originalData.getChartType();

    ArrayList dataList = originalData.getDataList();

    int groupTotal = dataList.size();

    this.dataArray = new double[groupTotal];
    this.dateTime = new String[groupTotal];
    this.caseSelect = originalData.getCaseSelect();
    this.formula = originalData.getFormula();
    this.title = originalData.getTitle();

    if ((this.caseSelect) || (this.chartType == 5) || 
      (this.chartType == 7))
    {
      for (int i = 0; i < groupTotal; i++) {
        JSSampleData sampleData = (JSSampleData)dataList.get(i);
        this.dataArray[i] = sampleData.getRejectNumber();
        this.dateTime[i] = sampleData.getSamplingTime();
      }

      JSSampleData sampleData_LS = (JSSampleData)dataList.get(0);
      this.samplingNum = (int)sampleData_LS.getSampleQuantity();

      this.ucl = originalData.getUCL();
      this.lcl = originalData.getLCL();
      this.cl = originalData.getCL();
    }
    else
    {
      this.uclArray = new double[groupTotal];
      this.lclArray = new double[groupTotal];

      for (int i = 0; i < groupTotal; i++) {
        JSSampleData sampleData = (JSSampleData)dataList.get(i);
        this.dataArray[i] = sampleData.getUnitRejectNumber();
        this.dateTime[i] = sampleData.getSamplingTime();
        this.uclArray[i] = sampleData.getUpLine();
        this.lclArray[i] = sampleData.getLowLine();
      }

      this.cl = originalData.getCL();
    }

    if ((this.dateTime[0] == null) || (!XCoorIsTime)) this.dateTime = null;

    if ((this.caseSelect) || (this.chartType == 5) || 
      (this.chartType == 7)) {
      this.abnormalPos = 
        JSJudgeAbnormity.JudgeAbnormal(this.dataArray, 
        abnormity, this.ucl, this.lcl, this.cl);
    }
    else
      this.abnormalPos = 
        JSJudgeAbnormity.JudgeAbnormal(this.dataArray, 
        abnormity, this.uclArray, this.lclArray, this.cl);
  }

  public boolean[] getAbnormalPos()
  {
    return this.abnormalPos;
  }

  public int getChartType() {
    return this.chartType;
  }

  public double getCl() {
    return this.cl;
  }

  public double[] getDataArray() {
    return this.dataArray;
  }

  public String[] getDateTime() {
    return this.dateTime;
  }

  public int getDotWidth() {
    return this.dotWidth;
  }

  public double getLcl() {
    return this.lcl;
  }

  public double[] getLclArray() {
    return this.lclArray;
  }

  public int getOffside() {
    return this.offside;
  }

  public double getUcl() {
    return this.ucl;
  }

  public double[] getUclArray() {
    return this.uclArray;
  }

  public void setUclArray(double[] uclArray) {
    this.uclArray = uclArray;
  }

  public void setUcl(double ucl) {
    this.ucl = ucl;
  }

  public void setOffside(int offside) {
    this.offside = offside;
  }

  public void setLclArray(double[] lclArray) {
    this.lclArray = lclArray;
  }

  public void setLcl(double lcl) {
    this.lcl = lcl;
  }

  public void setDotWidth(int dotWidth) {
    this.dotWidth = dotWidth;
  }

  public void setDateTime(String[] dateTime) {
    this.dateTime = dateTime;
  }

  public void setDataArray(double[] dataArray) {
    this.dataArray = dataArray;
  }

  public void setCl(double cl) {
    this.cl = cl;
  }

  public void setChartType(int chartType) {
    this.chartType = chartType;
  }

  public void setAbnormalPos(boolean[] abnormalPos) {
    this.abnormalPos = abnormalPos;
  }

  public int getSamplingNum() {
    return this.samplingNum;
  }

  public void setSamplingNum(int samplingNum) {
    this.samplingNum = samplingNum;
  }

  public int getXCoorInter() {
    return this.xCoorInter;
  }

  public void setXCoorInter(int xCoorInter) {
    this.xCoorInter = xCoorInter;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
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
}