package com.ambition.spc.jlanalyse.entity;

import java.io.Serializable;

import com.ambition.spc.entity.CPKMoudle;

/**    
 * JLResult.java(计量控制图的结果参数)
 * @authorBy wanglf
 *
 */
public class JLResult implements Serializable
{
	
  private static final long serialVersionUID = 1L;
  private double average;
  private double max;
  private double min;
  private double r;
  private double s;
  private int n;
  private double xUCL;
  private double xLCL;
  private double xCL;
  private double sUCL;
  private double sLCL;
  private double sCL;
  private double rUCL;
  private double rLCL;
  private double rCL;
  private double skewness;
  private double kurtosis;
  private CPKMoudle cpkMoudle;

  public double getAverage()
  {
    return this.average;
  }

  public void setAverage(double average) {
    this.average = average;
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

  public double getR() {
    return this.r;
  }

  public void setR(double r) {
    this.r = r;
  }

  public double getS() {
    return this.s;
  }

  public void setS(double s) {
    this.s = s;
  }

  public int getN() {
    return this.n;
  }

  public void setN(int n) {
    this.n = n;
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

  public CPKMoudle getCpkMoudle() {
    return this.cpkMoudle;
  }

  public void setRCL(double rCL) {
    this.rCL = rCL;
  }

  public void setCpkMoudle(CPKMoudle cpkMoudle) {
    this.cpkMoudle = cpkMoudle;
  }

  public double getKurtosis() {
    return this.kurtosis;
  }

  public double getSkewness() {
    return this.skewness;
  }

  public void setKurtosis(double kurtosis) {
    this.kurtosis = kurtosis;
  }

  public void setSkewness(double skewness) {
    this.skewness = skewness;
  }
}