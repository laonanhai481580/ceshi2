package com.ambition.spc.jlanalyse.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.entity.PictureBaseElement;
import com.ambition.spc.jlanalyse.util.JLJudgeAbnormity;

/**    
 * JLControlChartParam.java(awt画计量图表参数)
 * @authorBy wanglf
 *
 */

public class JLControlChartParam extends PictureBaseElement implements Serializable
{
  
  private static final long serialVersionUID = 1L;
  private int offside = 1;
  private String titleY;
  private int samplingNum;
  private int chartType;
  private double[] arrayx;
  private double[] arrayy;
  private double ucl1;
  private double lcl1;
  private double cl1;
  private double ucl2;
  private double lcl2;
  private double cl2;
  private int controlsta;
  private boolean[] abnormalPosUp;
  private boolean[] abnormalPosDow;
  private String[] dateTime;
  private Double tu;
  private Double tl;
  private int dotWidth = 20;

  private int xCoorInter = 1;
  private String upTitle;
  private String lowTitle;
  private int expentType = 1;
  private String formula;

  @SuppressWarnings("rawtypes")
public JLControlChartParam(JLOriginalData originalData, String titleY, Abnormity[] abnormity, boolean XCoorIsTime)
  {
    setTopHeight(20);
    this.titleY = (titleY == null ? "" : titleY);
    this.samplingNum = originalData.getSampleQuantity();
    this.chartType = originalData.getChartType();
    this.upTitle = originalData.getUpTitle();
    this.lowTitle = originalData.getLowTitle();
    this.expentType = originalData.getExpentType();
    this.formula = originalData.getFormula();
    this.controlsta = originalData.getControlState().intValue();
    this.tu = originalData.getTu();
    this.tl = originalData.getTl();

    ArrayList dataList = originalData.getDataList();
    int groupTotal = dataList.size();

    this.arrayx = new double[groupTotal];
    if ((originalData.getChartType() == 
      4) && (groupTotal > 0))
      this.arrayy = new double[groupTotal - 1];
    else {
      this.arrayy = new double[groupTotal];
    }
    this.dateTime = new String[groupTotal];

    switch (this.chartType)
    {
    case 1:
      for (int i = 0; i < groupTotal; i++) {
        JLSampleData sampleData = (JLSampleData)dataList.get(i);
        this.arrayx[i] = sampleData.getAverage();
        this.arrayy[i] = sampleData.getR();
        this.dateTime[i] = sampleData.getSamplingTime();
      }

      this.ucl1 = originalData.getXUCL();
      this.lcl1 = originalData.getXLCL();
      this.cl1 = originalData.getXCL();

      this.ucl2 = originalData.getRUCL();
      this.lcl2 = originalData.getRLCL();
      this.cl2 = originalData.getRCL();

      break;
    case 2:
      for (int i = 0; i < groupTotal; i++) {
        JLSampleData sampleData = (JLSampleData)dataList.get(i);
        this.arrayx[i] = sampleData.getAverage();
        this.arrayy[i] = sampleData.getS();
        this.dateTime[i] = sampleData.getSamplingTime();
      }

      this.ucl1 = originalData.getXUCL();
      this.lcl1 = originalData.getXLCL();
      this.cl1 = originalData.getXCL();

      this.ucl2 = originalData.getSUCL();
      this.lcl2 = originalData.getSLCL();
      this.cl2 = originalData.getSCL();

      break;
    case 3:
      for (int i = 0; i < groupTotal; i++) {
        JLSampleData sampleData = (JLSampleData)dataList.get(i);
        this.arrayx[i] = sampleData.getMedian();
        this.arrayy[i] = sampleData.getR();
        this.dateTime[i] = sampleData.getSamplingTime();
      }

      this.ucl1 = originalData.getXUCL();
      this.lcl1 = originalData.getXLCL();
      this.cl1 = originalData.getXCL();

      this.ucl2 = originalData.getRUCL();
      this.lcl2 = originalData.getRLCL();
      this.cl2 = originalData.getRCL();

      break;
    case 4:
      for (int i = 0; i < groupTotal; i++) {
        JLSampleData sampleData = (JLSampleData)dataList.get(i);
        this.arrayx[i] = sampleData.getAverage();

        if (i > 0) {
          this.arrayy[(i - 1)] = sampleData.getR();
        }
        this.dateTime[i] = sampleData.getSamplingTime();
      }

      this.ucl1 = originalData.getXUCL();
      this.lcl1 = originalData.getXLCL();
      this.cl1 = originalData.getXCL();

      this.ucl2 = originalData.getRUCL();
      this.lcl2 = originalData.getRLCL();
      this.cl2 = originalData.getRCL();
    }

    //if ((this.dateTime[0] == null) || (!XCoorIsTime)) this.dateTime = null;
    if ( (!XCoorIsTime)) this.dateTime = null;
    
    Abnormity[] abLS = JLJudgeAbnormity.getAbnormity(abnormity, 1);
    if ((abLS != null) && (abLS.length > 0)) {
      this.abnormalPosUp = 
        JLJudgeAbnormity.JudgeAbnormal(this.arrayx, abLS, 
        this.ucl1, this.lcl1, this.cl1, this.tu, this.tl, 1, null);
    }
    abLS = JLJudgeAbnormity.getAbnormity(abnormity, 2);
    if ((abLS != null) && (abLS.length > 0))
      this.abnormalPosDow = 
        JLJudgeAbnormity.JudgeAbnormal(this.arrayy, abLS, 
        this.ucl2, this.lcl2, this.cl2, this.tu, this.tl, 2, null);
  }

  public int getOffside() {
    return this.offside;
  }

  public String getTitleY() {
    return this.titleY;
  }

  public int getSamplingNum() {
    return this.samplingNum;
  }

  public int getChartType() {
    return this.chartType;
  }

  public double[] getArrayx() {
    return this.arrayx;
  }

  public double[] getArrayy() {
    return this.arrayy;
  }

  public double getUcl1() {
    return this.ucl1;
  }

  public double getLcl1() {
    return this.lcl1;
  }

  public double getCl1() {
    return this.cl1;
  }

  public double getUcl2() {
    return this.ucl2;
  }

  public double getLcl2() {
    return this.lcl2;
  }

  public double getCl2() {
    return this.cl2;
  }

  public int getControlsta() {
    return this.controlsta;
  }

  public boolean[] getAbnormalPosUp() {
    return this.abnormalPosUp;
  }

  public boolean[] getAbnormalPosDow() {
    return this.abnormalPosDow;
  }

  public String[] getDateTime() {
    return this.dateTime;
  }

  public Double getTu() {
    return this.tu;
  }

  public Double getTl() {
    return this.tl;
  }

  public int getDotWidth()
  {
    return this.dotWidth;
  }

  public void setAbnormalPosDow(boolean[] abnormalPosDow) {
    this.abnormalPosDow = abnormalPosDow;
  }

  public void setAbnormalPosUp(boolean[] abnormalPosUp) {
    this.abnormalPosUp = abnormalPosUp;
  }

  public void setArrayx(double[] arrayx) {
    this.arrayx = arrayx;
  }

  public void setArrayy(double[] arrayy) {
    this.arrayy = arrayy;
  }

  public void setChartType(int chartType) {
    this.chartType = chartType;
  }

  public void setCl1(double cl1) {
    this.cl1 = cl1;
  }

  public void setCl2(double cl2) {
    this.cl2 = cl2;
  }

  public void setControlsta(int controlsta) {
    this.controlsta = controlsta;
  }

  public void setDateTime(String[] dateTime) {
    this.dateTime = dateTime;
  }

  public void setDotWidth(int dotWidth) {
    this.dotWidth = dotWidth;
  }

  public void setLcl1(double lcl1)
  {
    this.lcl1 = lcl1;
  }

  public void setLcl2(double lcl2) {
    this.lcl2 = lcl2;
  }

  public void setOffside(int offside) {
    this.offside = offside;
  }

  public void setSamplingNum(int samplingNum) {
    this.samplingNum = samplingNum;
  }

  public void setTitleY(String titleY) {
    this.titleY = (titleY == null ? "" : titleY);
  }

  public void setTl(Double tl) {
    this.tl = tl;
  }

  public void setTu(Double tu) {
    this.tu = tu;
  }

  public void setUcl1(double ucl1) {
    this.ucl1 = ucl1;
  }

  public void setUcl2(double ucl2) {
    this.ucl2 = ucl2;
  }

  public int getXCoorInter() {
    return this.xCoorInter;
  }

  public void setXCoorInter(int xCoorInter) {
    this.xCoorInter = xCoorInter;
  }

  public String getUpTitle()
  {
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

  public int getExpentType() {
    return this.expentType;
  }

  public void setFormula(String formula) {
    this.formula = formula;
  }

  public void setExpentType(int expentType) {
    this.expentType = expentType;
  }
}