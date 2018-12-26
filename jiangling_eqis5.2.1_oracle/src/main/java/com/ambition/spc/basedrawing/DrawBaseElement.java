package com.ambition.spc.basedrawing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.ambition.spc.util.StringUtil;

/**    
 * DrawBaseElement.java
 * @authorBy wanglf
 *
 */
public class DrawBaseElement
{
  public static void DrawAreaLine(Graphics2D g, String[] yItemList, int imageHeight, int imageWidth, int bottomHeight, int leftWidth, int topHeight, int rightWidth, boolean showAllLine, boolean showMinAtXAxes, boolean showAlarm)
  {
    DrawAreaLine(g, yItemList, yItemList.length, 1.0D, 1.0D, imageHeight, 
      imageWidth, bottomHeight, leftWidth, topHeight, 
      rightWidth, showAllLine, showMinAtXAxes, showAlarm);
  }

  public static void DrawAreaLine(Graphics2D g, double upLine, double lowLine, int imageHeight, int imageWidth, int bottomHeight, int leftWidth, int topHeight, int rightWidth, boolean showAllLine, boolean showMinAtXAxes, boolean showAlarm)
  {
    double yInterval = StringUtil.dealData(upLine, lowLine);
    DrawAreaLine(g, null, upLine, lowLine, yInterval, imageHeight, 
      imageWidth, bottomHeight, leftWidth, topHeight, 
      rightWidth, showAllLine, showMinAtXAxes, showAlarm);
  }

  private static void DrawAreaLine(Graphics2D g, String[] yItemList, double upLine, double lowLine, double yInterval, int imageHeight, int imageWidth, int bottomHeight, int leftWidth, int topHeight, int rightWidth, boolean showAllLine, boolean showMinAtXAxes, boolean showAlarm)
  {
    int n = ((upLine - lowLine) % yInterval == 0.0D) && (lowLine >= 0.0D) ? 
      (int)((upLine - lowLine) / yInterval) : 
      (int)((upLine - lowLine) / yInterval) + 1;
    double douCentH8_7;
    double douCentH8_1;
    if (showMinAtXAxes) {
       douCentH8_1 = topHeight;
      douCentH8_7 = imageHeight - bottomHeight;
    }
    else {
      douCentH8_1 = (imageHeight - topHeight - bottomHeight) / 8.0D;
      douCentH8_7 = douCentH8_1 * 7.0D + topHeight;
      douCentH8_1 += topHeight;
    }
    double low_LS;
    if (lowLine < 0.0D) {
      low_LS = lowLine % yInterval == 0.0D ? yInterval * (1 + (int)(lowLine / yInterval)) : yInterval * (int)(lowLine / yInterval);
    }
    else {
      low_LS = yInterval * (int)(lowLine / yInterval) + yInterval;
    }
    double dHY = (douCentH8_7 - douCentH8_1) / (upLine - lowLine);

    if (showAlarm)
      g.setColor(Color.red);
    else
      g.setColor(Color.black);
    int lineLength;
    if (showAllLine) {
      lineLength = imageWidth - rightWidth;
    }
    else {
      lineLength = leftWidth + 6;
    }

    if (yItemList == null) {
      g.drawString(StringUtil.formatdouble(upLine, 3), 
        10, (int)douCentH8_1);
      g.drawString(StringUtil.formatdouble(lowLine, 3), 
        10, (int)douCentH8_7);
    }
    else {
      g.drawString(yItemList[(yItemList.length - 1)], 
        10, (int)douCentH8_1);
      g.drawString(yItemList[0], 
        10, (int)douCentH8_7);
    }
    g.drawLine(leftWidth, (int)douCentH8_1, 
      lineLength, (int)douCentH8_1);
    g.drawLine(leftWidth, (int)douCentH8_7, lineLength, (int)douCentH8_7);

    g.setColor(Color.black);
    for (int i = 1; i < n; i++) {
      if (low_LS + (i - 1) * yInterval >= upLine) {
        break;
      }
      if (yItemList == null) {
        g.drawString(StringUtil.formatdouble(low_LS + (i - 1) * yInterval, 3), 
          10, 
          (int)(douCentH8_7 - 
          (low_LS + (i - 1) * yInterval - lowLine) * dHY));
      }
      else {
        g.drawString(yItemList[i], 10, 
          (int)(douCentH8_7 - 
          (low_LS + (i - 1) * yInterval - lowLine) * dHY));
      }
      g.drawLine(leftWidth, 
        (int)(douCentH8_7 - 
        (low_LS + (i - 1) * yInterval - lowLine) * dHY), 
        lineLength, 
        (int)(douCentH8_7 - 
        (low_LS + (i - 1) * yInterval - lowLine) * dHY));
    }
  }

  public static void DrawYAxesCoor(Graphics2D g, double upLine, double lowLine, int imageHeight, int imageWidth, int bottomHeight, int leftWidth, int topHeight, int rightWidth, boolean showAllLine)
  {
    if (upLine < 0.0D) upLine = 0.0D;
    if (lowLine > 0.0D) lowLine = 0.0D;
    double yInterval = StringUtil.dealData(upLine, lowLine);
    int nUp = 0;
    if ((upLine > 0.0D) && (lowLine < 0.0D)) {
      nUp = -lowLine % yInterval == 0.0D ? 
        (int)(-lowLine / yInterval) : 
        (int)(-lowLine / yInterval) + 1;
    }
    double mxH = upLine - lowLine;
    int n = mxH % yInterval == 0.0D ? 
      (int)(mxH / yInterval) : 
      (int)(mxH / yInterval) + 1;
    if (upLine == 0.0D) nUp = n;

    double douCentH8_7 = imageHeight - bottomHeight;
    double dHY = (douCentH8_7 - topHeight) / mxH;
    if (lowLine < 0.0D)
      douCentH8_7 += lowLine * dHY;
    int lineLength;
    if (showAllLine) {
      lineLength = imageWidth - rightWidth;
    }
    else {
      lineLength = leftWidth + 6;
    }

    g.setColor(Color.black);
    for (int i = 1; i <= n; i++) {
      g.drawString(StringUtil.formatdouble((i - nUp) * yInterval, 3), 
        10, (int)(douCentH8_7 - (i - nUp) * yInterval * dHY));
      g.drawLine(leftWidth, (int)(douCentH8_7 - (i - nUp) * yInterval * dHY), 
        lineLength, (int)(douCentH8_7 - (i - nUp) * yInterval * dHY));
    }
  }

  public static void DrawAxes(Graphics2D g, int imageHeight, int imageWidth, int bottomHeight, int leftWidth)
  {
    int lineX = imageHeight - bottomHeight;

    g.drawLine(leftWidth, 20, leftWidth, lineX);
    g.drawLine(leftWidth - 5, 26, leftWidth, 20);
    g.drawLine(leftWidth + 5, 26, leftWidth, 20);

    g.drawLine(leftWidth, lineX, imageWidth - 10, lineX);
    g.drawLine(imageWidth - 16, lineX - 5, 
      imageWidth - 10, lineX);
    g.drawLine(imageWidth - 16, lineX + 5, 
      imageWidth - 10, lineX);
  }

  public static void DrawAxesXCent(Graphics2D g, int imageHeight, int imageWidth, int bottomHeight, int leftWidth, int topHeight)
  {
    int lineX = imageHeight / 2;

    if (topHeight <= 0) {
      g.drawLine(leftWidth, 0, leftWidth, imageHeight - bottomHeight);
    }
    else {
      g.drawLine(leftWidth, 20, leftWidth, imageHeight - bottomHeight);
      g.drawLine(leftWidth - 5, 26, leftWidth, 20);
      g.drawLine(leftWidth + 5, 26, leftWidth, 20);
    }

    g.drawLine(leftWidth, lineX, imageWidth - 30, lineX);
    g.drawLine(imageWidth - 36, lineX - 5, 
      imageWidth - 30, lineX);
    g.drawLine(imageWidth - 36, lineX + 5, 
      imageWidth - 30, lineX);
  }

  public static BufferedImage getImage(int imageHeight, int imageWidth, boolean showFrame)
  {
    BufferedImage image = new BufferedImage(imageWidth, imageHeight, 
      1);
    Graphics2D g = (Graphics2D)image.getGraphics();
    setBackgroundCloor(g, imageHeight);

    if (showFrame) {
      g.fillRect(1, 1, imageWidth - 2, imageHeight - 2);
    }
    else {
      g.fillRect(0, 0, imageWidth, imageHeight);
    }
    return image;
  }

  public static void drawXAxesCoor(Graphics2D g, int imageHeight, int imageWidth, int X, int Y, String XTitle)
  {
    g.rotate(Math.toRadians(90.0D), imageHeight / 2, imageHeight / 2);
    g.drawString(XTitle, Y, imageHeight - X);
    g.rotate(Math.toRadians(-90.0D), imageHeight / 2, imageHeight / 2);
  }

  public static void setBackgroundCloor(Graphics2D g, int imageHeight)
  {
    RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, 
    RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHints(hints);

    g.setPaint(
      new GradientPaint(0.0F, imageHeight, new Color(208, 226, 228), 0.0F, 0.0F, 
      new Color(255, 255, 255), true));
  }
}