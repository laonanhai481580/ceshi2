package com.ambition.spc.jsanalyse.entity;

import java.io.Serializable;

/**    
 * JSSampleData.java
 * @authorBy wanglf
 *
 */
public class JSSampleData implements Serializable
{
  
  private static final long serialVersionUID = 1L;
  private double sampleQuantity;
  private double rejectNumber;
  private double unitRejectNumber;
  private double upLine;
  private double lowLine;
  private String samplingTime;
  private boolean judgeState;

  public double getSampleQuantity()
  {
    return this.sampleQuantity;
  }
  public void setSampleQuantity(double sampleQuantity) {
    this.sampleQuantity = sampleQuantity;
  }
  public double getRejectNumber() {
    return this.rejectNumber;
  }
  public void setRejectNumber(double rejectNumber) {
    this.rejectNumber = rejectNumber;
  }
  public double getUnitRejectNumber() {
    return this.unitRejectNumber;
  }
  public void setUnitRejectNumber(double unitRejectNumber) {
    this.unitRejectNumber = unitRejectNumber;
  }
  public double getUpLine() {
    return this.upLine;
  }
  public void setUpLine(double upLine) {
    this.upLine = upLine;
  }
  public double getLowLine() {
    return this.lowLine;
  }
  public void setLowLine(double lowLine) {
    this.lowLine = lowLine;
  }
  public boolean getJudgeState() {
    return this.judgeState;
  }
  public void setJudgeState(boolean judgeState) {
    this.judgeState = judgeState;
  }
  public void setSamplingTime(String samplingTime) {
    this.samplingTime = samplingTime;
  }
  public String getSamplingTime() {
    return this.samplingTime;
  }
}