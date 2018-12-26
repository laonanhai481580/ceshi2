package com.ambition.spc.entity;

public class JohnsonTransferForm
{
  private double P;
  private double Z;
  private int type;
  private double par1;
  private double par2;
  private double par3;
  private double par4;

  public void setType(int type)
  {
    this.type = type;
  }

  public void setPar4(double par4) {
    this.par4 = par4;
  }

  public void setPar3(double par3) {
    this.par3 = par3;
  }

  public void setPar2(double par2) {
    this.par2 = par2;
  }

  public void setPar1(double par1) {
    this.par1 = par1;
  }

  public int getType() {
    return this.type;
  }

  public double getPar4() {
    return this.par4;
  }

  public double getPar3() {
    return this.par3;
  }

  public double getPar2() {
    return this.par2;
  }

  public double getPar1() {
    return this.par1;
  }

  public void setZ(double Z) {
    this.Z = Z;
  }

  public double getZ() {
    return this.Z;
  }

  public void setP(double P) {
    this.P = P;
  }

  public double getP() {
    return this.P;
  }
}