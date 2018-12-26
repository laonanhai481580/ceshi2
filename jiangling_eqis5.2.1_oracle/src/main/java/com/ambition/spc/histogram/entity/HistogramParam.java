package com.ambition.spc.histogram.entity;

import java.io.Serializable;

import com.ambition.spc.entity.PictureBaseElement;

/**    
 * HistogramParam.java(画直通图参数)
 * @authorBy wanglf
 *
 */

public class HistogramParam extends PictureBaseElement implements Serializable
{
  private static final long serialVersionUID = 1L;
  private double[] itemList;
  private int fz = 0;

  private int sigma = 3;

  private boolean kzx = true;

  private boolean ztx = true;
  private Double tu;
  private Double tl;
  private String xTitle;

  public HistogramParam(double[] itemList, String xTitle, Double tu, Double tl)
  {
    this.itemList = itemList;
    this.tu = tu;
    this.tl = tl;
    this.xTitle = xTitle;
  }

  public int getFz() {
    return this.fz;
  }

  public double[] getItemList() {
    return this.itemList;
  }

  public boolean getKzx() {
    return this.kzx;
  }

  public int getSigma() {
    return this.sigma;
  }

  public Double getTl() {
    return this.tl;
  }

  public Double getTu() {
    return this.tu;
  }

  public boolean getZtx() {
    return this.ztx;
  }

  public void setZtx(boolean ztx) {
    this.ztx = ztx;
  }

  public void setTu(Double tu) {
    this.tu = tu;
  }

  public void setTl(Double tl) {
    this.tl = tl;
  }

  public void setSigma(int sigma) {
    this.sigma = sigma;
  }

  public void setKzx(boolean kzx) {
    this.kzx = kzx;
  }

  public void setItemList(double[] itemList) {
    this.itemList = itemList;
  }

  public void setFz(int fz) {
    this.fz = fz;
  }

  public String getXTitle() {
    return this.xTitle;
  }

  public void setXTitle(String xTitle) {
    this.xTitle = xTitle;
  }
}