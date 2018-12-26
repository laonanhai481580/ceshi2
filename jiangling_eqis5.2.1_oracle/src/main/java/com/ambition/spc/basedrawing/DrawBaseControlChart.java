package com.ambition.spc.basedrawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.ambition.spc.util.StringUtil;
/**    
 * DrawBaseControlChart.java
 * @authorBy wanglf
 *
 */


public class DrawBaseControlChart
{
  public static BufferedImage DrawBaseChart(int startside, double[] itemList, double lcl, boolean[] abnormalPos, String[] dateTime, int imageHeight, int imageWidth, int topHeight, int bottomHeight, int leftWidth, int rightWidth, int dotWidth, int showDot, int xCoor, double coorH10_8, int corIntH10_9, double dh)
  {
    int length = itemList.length;

    int endDot = length < showDot ? length : showDot;

    BufferedImage image = new BufferedImage(imageWidth, imageHeight, 
      1);
    Graphics2D g = (Graphics2D)image.getGraphics();
    DrawBaseElement.setBackgroundCloor(g, imageHeight);

    g.fillRect(1, 1, imageWidth - 2, imageHeight - 2);

    g.setColor(Color.black);

    DrawBaseElement.DrawAxes(g, imageHeight, imageWidth, bottomHeight, 
      leftWidth);

    double leftInterval = dotWidth > 30 ? leftWidth + dotWidth / 2 : 
      leftWidth + dotWidth;
    for (int i = 0; i < endDot; i++) {
      if (abnormalPos[(startside + i)]) {
        g.setColor(Color.red);
        g.fillOval((int)(leftInterval + dotWidth * i - 2.0D), 
          (int)(coorH10_8 - dh * (itemList[(startside + i)] - lcl) - 
          2.0D), 5, 5);
      }
      else {
        g.setColor(Color.black);
        g.fillOval((int)(leftInterval + dotWidth * i - 1.0D), 
          (int)(coorH10_8 - dh * (itemList[(startside + i)] - lcl) - 
          1.0D), 3, 3);
      }
    }

    g.setColor(Color.black);

    if (dateTime == null) {
      g.drawString("序号", imageWidth - rightWidth, 
        corIntH10_9 + 35);
      for (int i = 0; i < endDot; i++)
        if (i % xCoor == 0) {
          g.drawLine((int)(leftInterval + dotWidth * i), 
            corIntH10_9, 
            (int)(leftInterval + dotWidth * i), 
            corIntH10_9 - 6);
          g.drawString(Integer.toString(startside + i + 1), 
            (int)(leftInterval + dotWidth * i - 5.0D), 
            corIntH10_9 + 11);
        }
        else {
          g.drawLine((int)(leftInterval + dotWidth * i), corIntH10_9, 
            (int)(leftInterval + dotWidth * i), corIntH10_9 - 3);
        }
    }
    else
    {
      g.drawString("时间", imageWidth - rightWidth, 
        corIntH10_9 + 35);
      for (int i = 0; i < endDot; i++) {
        if (i % xCoor == 0) {
          g.drawLine((int)(leftInterval + dotWidth * i), corIntH10_9, 
            (int)(leftInterval + dotWidth * i), corIntH10_9 - 6);

          int len = dateTime[(startside + i)].length();
          if (len < 10) {
            g.drawString(dateTime[(startside + i)], 
              (int)(leftInterval + dotWidth * i - 5.0D), 
              corIntH10_9 + 11);
          }
          else {
            g.drawString(dateTime[(startside + i)].substring(5, 10), 
              (int)(leftInterval + dotWidth * i - 5.0D), 
              corIntH10_9 + 11);
            if (len > 15) {
              g.drawString(dateTime[(startside + i)].substring(11, 16), 
                (int)(leftInterval + dotWidth * i - 5.0D), 
                corIntH10_9 + 22);
            }
            else if (len > 10) {
              g.drawString(dateTime[(startside + i)].substring(10), 
                (int)(leftInterval + dotWidth * i - 5.0D), 
                corIntH10_9 + 22);
            }

          }

        }
        else
        {
          g.drawLine((int)(leftInterval + dotWidth * i), 
            corIntH10_9, 
            (int)(leftInterval + dotWidth * i), 
            corIntH10_9 - 3);
        }
      }
    }

    g.setColor(Color.blue);
    for (int i = 1; i < endDot; i++) {
      g.drawLine((int)(leftInterval + dotWidth * (i - 1)), 
        (int)(coorH10_8 - dh * (itemList[(startside + i - 1)] - lcl)), 
        (int)(leftInterval + dotWidth * i), 
        (int)(coorH10_8 - dh * (itemList[(startside + i)] - lcl)));
    }
    return image;
  }

  public static void addControlLine(Graphics2D g, double ucl, Double lowLine, Double clLine, int conStat, int imageWidth, int topHeight, int bottomHeight, int leftWidth, int rightWidth, int dataPrecision, double coorH10_1, double mLowValue, double coorH10_8, double dh)
  {
    double lcl = 0.0D;
    if (lowLine != null) {
      lcl = lowLine.doubleValue();
    }

    if (clLine != null) {
      double cl = clLine.doubleValue();
      g.drawLine(leftWidth, (int)(coorH10_8 - dh * (cl - lcl)), 
        imageWidth - rightWidth, 
        (int)(coorH10_8 - dh * (cl - lcl)));
      g.drawString("CL", imageWidth - rightWidth, 
        (int)(coorH10_8 - dh * (cl - lcl)));
      g.drawString(StringUtil.formatdouble(cl, dataPrecision), 10, 
        (int)(coorH10_8 - dh * (cl - lcl)));
    }

    g.drawString("UCL", imageWidth - rightWidth, 
      (int)(coorH10_8 - dh * (ucl - lcl)));
    g.drawString(StringUtil.formatdouble(ucl, dataPrecision), 10, 
      (int)(coorH10_8 - dh * (ucl - lcl)));

    if (lowLine != null) {
      g.drawString("LCL", imageWidth - rightWidth, 
        (int)(coorH10_8 - 1.0D));
      g.drawString(StringUtil.formatdouble(lcl, dataPrecision), 10, 
        (int)(coorH10_8 - 1.0D));
    }

    if (conStat == 2) {
      DrawDotLine.drawDashDotLine(g, leftWidth, 
        (int)(topHeight + coorH10_1 + mLowValue), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH10_1 + mLowValue));
      if ((mLowValue == 0.0D) && (lowLine != null))
        DrawDotLine.drawDashDotLine(g, leftWidth, (int)coorH10_8, 
          imageWidth - rightWidth, (int)coorH10_8);
    }
    else
    {
      DrawDotLine.drawBreakLine(g, leftWidth, 
        (int)(topHeight + coorH10_1 + mLowValue), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH10_1 + mLowValue));
      if ((mLowValue == 0.0D) && (lowLine != null))
        DrawDotLine.drawBreakLine(g, leftWidth, (int)coorH10_8, 
          imageWidth - rightWidth, (int)coorH10_8);
    }
  }

  public static BufferedImage DrawErrorChart(int imageWidth, int imageHeight)
  {
    BufferedImage image = new BufferedImage(imageWidth, imageHeight, 1);
    Graphics2D g = (Graphics2D)image.getGraphics();
    DrawBaseElement.setBackgroundCloor(g, imageHeight);

    g.fillRect(1, 1, imageWidth - 2, imageHeight - 2);
    Font font = g.getFont();
    Font font1 = new Font("ZFTFont", 1, 20);
    g.setFont(font1);
    g.setColor(Color.red);
    g.drawString("数据有问题,请检查数据！", 
      imageWidth / 10 + 40, 
      imageHeight / 2);
    g.setFont(font);
    return image;
  }
}