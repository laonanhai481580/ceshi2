package com.ambition.spc.distributechart.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.ambition.spc.basedrawing.DrawBaseElement;
import com.ambition.spc.distributechart.model.DistributeChartParam;
import com.ambition.spc.util.StringUtil;

public class DrawDistributeChart
{
  public static BufferedImage drawDistributeChart(DistributeChartParam distributeChartParam)
  {
    BufferedImage image = DrawBaseElement.getImage(distributeChartParam.getImageHeight(), distributeChartParam.getImageWidth(), true);
    addDistributeChart(image, distributeChartParam);
    return image;
  }

  private static void addDistributeChart(BufferedImage image, DistributeChartParam distributeChartParam)
  {
    double[][] itemList = distributeChartParam.getItemList();
    String xTitle = distributeChartParam.getXTitle();
    String yTitle = distributeChartParam.getYTitle();
    int imageHeight = distributeChartParam.getImageHeight();
    int imageWidth = distributeChartParam.getImageWidth();
    int topHeight = distributeChartParam.getTopHeight();
    int bottomHeight = distributeChartParam.getBottomHeight();
    int leftWidth = distributeChartParam.getLeftWidth();
    int rightWidth = distributeChartParam.getRightWidth();
    int dataPrecision = distributeChartParam.getDataPrecision();
    int length = itemList.length;

    Graphics2D g = (Graphics2D)image.getGraphics();

    if (length > 2)
    {
      double xmax = itemList[0][0];
      double xmin = itemList[0][0];
      double ymax = itemList[0][1];
      double ymin = itemList[0][1];
      for (int i = 1; i < length; i++) {
        if (itemList[i][0] > xmax) {
          xmax = itemList[i][0];
        }
        if (itemList[i][0] < xmin) {
          xmin = itemList[i][0];
        }
        if (itemList[i][1] > ymax) {
          ymax = itemList[i][1];
        }
        if (itemList[i][1] < ymin) {
          ymin = itemList[i][1];
        }
      }

      double xInterval = StringUtil.dealData(xmax, xmin);
      double yInterval = StringUtil.dealData(ymax, ymin);
      xmin = xInterval * (int)(xmin / xInterval);
      xmax = xInterval * ((int)(xmax / xInterval) + 1);
      ymin = yInterval * (int)(ymin / yInterval);
      ymax = yInterval * ((int)(ymax / yInterval) + 1);
      int dotXNum = (int)((xmax - xmin) / xInterval);
      int dotYNum = (int)((ymax - ymin) / yInterval);

      double imageW = imageWidth - leftWidth - rightWidth;
      double imageH = imageHeight - topHeight - bottomHeight;
      double xh = 1.0D * imageW / dotXNum;
      double yh = 1.0D * imageH / dotYNum;
      int leftSide = leftWidth + 10;
      int downSide = imageHeight - bottomHeight - 6;

      g.setColor(Color.black);

      DrawBaseElement.DrawAxes(g, imageHeight, imageWidth, bottomHeight, 
        leftWidth);

      g.drawString(yTitle, leftWidth + 10, 20);
      g.drawString(xTitle, imageWidth - rightWidth, imageHeight - 6);

      g.drawString("n=" + length, leftWidth + 40, 35);

      for (int i = 0; i < dotXNum + 1; i++) {
        g.drawLine((int)(leftSide + xh * i), imageHeight - bottomHeight, 
          (int)(leftSide + xh * i), downSide);
        g.drawString(
          StringUtil.formatdouble(xmin + xInterval * i, 
          dataPrecision), 
          (int)(leftSide + xh * i - 5.0D), downSide + 18);
      }

      for (int i = 0; i < dotYNum + 1; i++) {
        g.drawLine(leftWidth, (int)(downSide - yh * i), 
          leftWidth + 6, (int)(downSide - yh * i));
        g.drawString(
          StringUtil.formatdouble(ymin + yInterval * i, 
          dataPrecision), 
          10, (int)(downSide - yh * i));
      }

      g.setColor(Color.red);
      for (int i = 0; i < length; i++) {
        g.fillOval(
          (int)(leftSide + (itemList[i][0] - xmin) / (xmax - xmin) * 
          imageW - 2.0D), 
          (int)(downSide - (itemList[i][1] - ymin) / (ymax - ymin) * 
          imageH - 2.0D), 5, 5);
      }

      double[] coefficient = distributeChartParam.getCoefficient();
      int type = distributeChartParam.getType();
      if ((type < 7) && (type > 0) && (coefficient != null) && (coefficient.length > 1) && (
        (type != 5) || (coefficient.length >= 3))) {
        g.setColor(Color.blue);
        double xR = xmin;
        double yR = 0.0D;
        double xO = 0.0D;
        double yO = 0.0D;
        double dxR = (xmax - xmin) / (imageW * 4.0D);
        for (int i = 0; i < imageW * 4.0D; i++) {
          switch (type) {
          case 1:
            yR = coefficient[0] + coefficient[1] * xR;
            break;
          case 2:
            yR = coefficient[0] + coefficient[1] * Math.log(xR);
            break;
          case 3:
            yR = coefficient[0] * Math.pow(xR, coefficient[1]);
            break;
          case 4:
            yR = coefficient[0] + coefficient[1] / xR;
            break;
          case 5:
            yR = coefficient[0] + coefficient[1] * xR + 
              coefficient[2] * xR * xR;
            break;
          case 6:
            yR = coefficient[0] * Math.pow(coefficient[1], xR);
          }

          if (i > 0) {
            g.drawLine((int)(leftSide + (xO - xmin) / (xmax - xmin) * imageW), 
              (int)(downSide - (yO - ymin) / (ymax - ymin) * imageH), 
              (int)(leftSide + (xR - xmin) / (xmax - xmin) * imageW), 
              (int)(downSide - (yR - ymin) / (ymax - ymin) * imageH));
          }
          xO = xR;
          yO = yR;
          xR += dxR;
        }
      }
    }
    else
    {
      Font font = g.getFont();
      Font font1 = new Font("ZFTFont", 1, 20);
      g.setFont(font1);
      g.setColor(Color.red);
      g.drawString("数据不够3对，不能绘制散布图。", 
        imageWidth / 10 + 40, 
        imageHeight / 2);
      g.setFont(font);
      return;
    }
  }
}