package com.ambition.spc.entity;

public class ADTestForm
{
  private double P = 0.0D;

  private boolean isNormal = false;

  private double alpha = 0.05D;

  private double AD = 0.0D;

  public boolean getIsNormal() { return this.isNormal; }

  public void setAlpha(double alpha)
  {
    this.alpha = alpha;
  }

  public void setIsNormal(boolean isNormal) {
    this.isNormal = isNormal;
  }

  public double getAlpha() {
    return this.alpha;
  }

  public void setAD(double AD) {
    this.AD = AD;
  }

  public double getAD() {
    return this.AD;
  }

  public void setP(double P) {
    this.P = P;
  }

  public double getP() {
    return this.P;
  }
}