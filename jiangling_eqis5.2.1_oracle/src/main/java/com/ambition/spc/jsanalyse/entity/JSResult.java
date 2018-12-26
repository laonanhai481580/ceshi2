package com.ambition.spc.jsanalyse.entity;

/**    
 * JSResult.java(计数控制图的结果参数)
 * @authorBy wanglf
 *
 */
public class JSResult
{
  private double average;
  private double max;
  private double min;
  private double r;
  private double s;
  private double rate;

  public double getMax()
  {
    return this.max;
  }
  public void setMax(double max) {
    this.max = max;
  }
  public double getAverage() {
    return this.average;
  }
  public void setAverage(double average) {
    this.average = average;
  }
  public double getMin() {
    return this.min;
  }
  public void setMin(double min) {
    this.min = min;
  }
  public double getR() {
    return this.r;
  }
  public void setR(double r) {
    this.r = r;
  }
  public double getRate() {
    return this.rate;
  }
  public void setRate(double rate) {
    this.rate = rate;
  }
  public double getS() {
    return this.s;
  }
  public void setS(double s) {
    this.s = s;
  }
}