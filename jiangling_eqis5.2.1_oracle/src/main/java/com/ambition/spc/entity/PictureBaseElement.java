package com.ambition.spc.entity;

import java.io.Serializable;

/**    
 * PictureBaseElement.java(图片的基本类)
 * @authorBy wanglf
 *
 */

public class PictureBaseElement implements Serializable
{
  private static final long serialVersionUID = 1L;

  private int imageHeight = 400;

  private int imageWidth = 670;

  private int topHeight = 40;

  private int bottomHeight = 40;

  private int leftWidth = 67;

  private int rightWidth = 67;

  private int dataPrecision = 3;

  public int getBottomHeight()
  {
    return this.bottomHeight;
  }

  public void setBottomHeight(int bottomHeight) {
    this.bottomHeight = bottomHeight;
  }

  public int getImageHeight() {
    return this.imageHeight;
  }

  public void setImageHeight(int imageHeight) {
    this.imageHeight = (imageHeight < 50 ? 50 : imageHeight);
  }

  public int getImageWidth() {
    return this.imageWidth;
  }

  public void setImageWidth(int imageWidth) {
    this.imageWidth = (imageWidth < 50 ? 50 : imageWidth);
  }

  public int getLeftWidth() {
    return this.leftWidth;
  }

  public void setLeftWidth(int leftWidth) {
    this.leftWidth = leftWidth;
  }

  public int getRightWidth() {
    return this.rightWidth;
  }

  public void setRightWidth(int rightWidth) {
    this.rightWidth = rightWidth;
  }

  public int getTopHeight() {
    return this.topHeight;
  }

  public void setTopHeight(int topHeight) {
    this.topHeight = topHeight;
  }

  public int getDataPrecision() {
    return this.dataPrecision;
  }

  public void setDataPrecision(int dataPrecision) {
    this.dataPrecision = dataPrecision;
  }
}