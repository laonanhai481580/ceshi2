package com.ambition.spc.histogram.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.ambition.spc.basedrawing.DrawBaseElement;
import com.ambition.spc.basedrawing.DrawDotLine;
import com.ambition.spc.histogram.entity.HistogramParam;
import com.ambition.spc.util.Calculator;
import com.ambition.spc.util.StringUtil;

/**    
 * DrawHistogram.java(AWT画直通图)
 * @authorBy wanglf
 *
 */

public class DrawHistogram
{
  public static BufferedImage drawHistogram(HistogramParam HistogramParam)
  {
    BufferedImage image = DrawBaseElement.getImage(HistogramParam
      .getImageHeight(), HistogramParam.getImageWidth(), true);
    addHistogram(image, HistogramParam);
    return image;
  }

  private static void addHistogram(BufferedImage image, HistogramParam HistogramParam)
  {
    int imageHeight = HistogramParam.getImageHeight();
    int imageWidth = HistogramParam.getImageWidth();
    double[] itemList = HistogramParam.getItemList();
    int fz = HistogramParam.getFz();
    int sigma = HistogramParam.getSigma();
    boolean kzx = HistogramParam.getKzx();
    boolean ztx = HistogramParam.getZtx();
    Double tu = HistogramParam.getTu();
    Double tl = HistogramParam.getTl();
    String xTitle = HistogramParam.getXTitle();
    int n = HistogramParam.getDataPrecision();
    int topHeight = HistogramParam.getTopHeight();
    int bottomHeight = HistogramParam.getBottomHeight();
    int leftWidth = HistogramParam.getLeftWidth();
    int rightWidth = HistogramParam.getRightWidth();

    int length = itemList.length;

    Graphics2D g = (Graphics2D)image.getGraphics();

    g.setColor(Color.black);
    if (length >= 10)
    {
      DrawBaseElement.DrawAxes(g, imageHeight, imageWidth, bottomHeight, leftWidth);

      g.drawString(xTitle, imageWidth - rightWidth, 
        imageHeight - 6);

      double pmax = Calculator.max(itemList);
      double pmin = Calculator.min(itemList);
      double x_pj = Calculator.average(itemList);
      double s = Calculator.calculateS(itemList);

      if (fz == 0) {
        fz = (int)(2.5D + Math.log(length) / Math.log(2.0D));
      }

      double h = (pmax - pmin) / fz;
      double HL = Math.pow(10.0D, -n);
      h = (int)((h + HL + 1.E-015D) / HL) * HL;
      HL /= 2.0D;

      double[][] stat = new double[fz][3];

      int f_max = 0;

      for (int i = 0; i < fz; i++) {
        stat[i][0] = (i > 0 ? stat[(i - 1)][0] + h : pmin - HL);
        stat[i][1] = (stat[i][0] + h);
        stat[i][2] = 0.0D;
        for (int j = 0; j < length; j++) {
          if ((itemList[j] > stat[i][0]) && (itemList[j] < stat[i][1])) {
            stat[i][2] += 1.0D;
          }
        }
        if (stat[i][2] > f_max) {
          f_max = (int)stat[i][2];
        }
      }

      imageWidth = imageWidth - leftWidth - rightWidth;
      imageHeight = imageHeight - topHeight - bottomHeight;

      double leftW = imageWidth / 4.0D; double rightW = imageWidth * 3.0D / 4.0D;
      double centW = rightW - leftW;

      double ff = (imageHeight - 40.0D) / f_max;

      double pminW = stat[0][0];
      double pmaxW = stat[(fz - 1)][1];

      double dh = centW / (pmaxW - pminW);

      double tu_ls = tu == null ? 0.0D : tu.doubleValue();
      double tl_ls = tl == null ? 0.0D : tl.doubleValue();

      if ((kzx) && (tu_ls > tl_ls)) {
        if ((tu != null) && 
          (leftW + (tu_ls - pmin + HL) * dh > 
          imageWidth)) {
          dh = imageWidth / 2.0D / (tu_ls - (pmaxW + pminW) / 2.0D);
          leftW = imageWidth - dh * (tu_ls - pminW);
          rightW = imageWidth - dh * (tu_ls - pmaxW);
          centW = rightW - leftW;
        }
        if ((tl != null) && (leftW + (tl_ls - pmin + HL) * dh <= 0.0D)) {
          dh = imageWidth / 2.0D / ((pmaxW + pminW) / 2.0D - tl_ls);
          leftW = dh * (pminW - tl_ls);
          rightW = dh * (pmaxW - tl_ls);
          centW = rightW - leftW;
        }

      }

      if ((sigma > 2) && (s > 0.0D)) {
        if (leftW + dh * (x_pj - s * sigma - pminW) <= 0.0D) {
          dh = imageWidth / 2.0D / ((pmaxW + pminW) / 2.0D - (x_pj - s * sigma));
          leftW = dh * (pminW - (x_pj - s * sigma));
          rightW = dh * (pmaxW - (x_pj - s * sigma));
          centW = rightW - leftW;
        }
        if (leftW + dh * (x_pj + s * sigma - pminW) > imageWidth) {
          dh = imageWidth / 2.0D / (
            x_pj + s * sigma - (pmaxW + pminW) / 2.0D);
          leftW = imageWidth - dh * (x_pj + s * sigma - pminW);
          rightW = imageWidth - dh * (x_pj + s * sigma - pmaxW);
          centW = rightW - leftW;
        }
      }

      double hh = centW / fz;

      leftW += leftWidth;
      imageHeight += topHeight;

      int[][] cols = { { 255, 150, 0, 255, 64, 255, 64, 255, 0, 150, 255, 255, 150, 
        120, 255, 0, 255, 210, 200, 173, 255, 255, 128, 128, 128, 128, 200, 133, 
        173, 210, 235, 191, 200, 255, 174, 245, 167, 189 }, 
        { 150, 150, 128, 128, 0, 0, 0, 68, 128, 255, 255, 150, 255, 50, 200, 
        200, 160, 166, 191, 173, 230, 255, 255, 255, 0, 128, 133, 200, 245, 
        185, 198, 128, 125, 210, 218, 164, 200, 245 }, 
        { 150, 255, 192, 64, 128, 0, 0, 103, 255, 255, 150, 255, 150, 120, 
        150, 220, 255, 166, 255, 230, 134, 0, 255, 128, 128, 192, 121, 166, 
        175, 110, 125, 155, 245, 128, 255, 200, 189, 200 } };

      g.drawString(StringUtil.formatdouble(stat[0][0], n + 1), (int)(leftW - 10.0D), 
        imageHeight + 11);

      int fontlenth = 
        Math.max(StringUtil.formatdoubleAddZero(stat[0][0], n + 1).length(), 
        StringUtil.formatdoubleAddZero(stat[(fz - 1)][1], n + 1).length());
      fontlenth = (int)(centW / 5.0D / fontlenth);
      if (fontlenth == 0) fontlenth = 1;

      if (fz / fontlenth * fontlenth == fz) {
        fontlenth = fz / fontlenth;
      }
      else {
        fontlenth = fz / fontlenth + 1;
      }

      for (int i = 0; i < fz; i++) {
        Color col = new Color(cols[0][i], cols[1][i], cols[2][i]);
        g.setColor(col);
        g.fillRect((int)(leftW + hh * i), 
          (int)(imageHeight - stat[i][2] * ff), 
          (int)(hh + 1.0D), (int)(stat[i][2] * ff + 1.0D));
        g.setColor(Color.black);

        int m_ls = (int)stat[i][2];

        g.drawString(Integer.toString(m_ls), (int)(leftW + hh * (i + 0.3D)), 
          (int)(imageHeight - stat[i][2] * ff) - 1);
        g.drawString(StringUtil.formatdouble(100.0D / length * m_ls, 1), 
          (int)(leftW + hh * (i + 0.3D) - 8.0D), 
          (int)(imageHeight - stat[i][2] * ff) - 12);
        if ((i + 1) % fontlenth == 0) {
          if ((i + 1) % (fontlenth * 2) == 0) {
            g.drawString(StringUtil.formatdouble(stat[i][1], n + 1), 
              (int)(leftW + hh * (i + 1) - 10.0D), 
              imageHeight + 11);
          }
          else {
            g.drawString(StringUtil.formatdouble(stat[i][1], n + 1), 
              (int)(leftW + hh * (i + 1) - 10.0D), 
              imageHeight + 22);
          }
        }
      }

      g.setColor(Color.red);
      DrawDotLine.drawBreakLine(g, (int)(leftW + dh * (x_pj - pminW)), 
        20, (int)(leftW + dh * (x_pj - pminW)), 
        imageHeight);
      g.setColor(Color.black);
      g.drawString("_", (int)(leftW + dh * (x_pj - pminW) - 10.0D), 10);
      g.drawString("x=" + StringUtil.formatdouble(x_pj, n), 
        (int)(leftW + dh * (x_pj - pminW) - 10.0D), 20);

      if (kzx)
      {
        if ((tu != null) && (tl != null)) {
          g.setColor(Color.red);
          g.drawLine((int)(leftW + dh * (tu_ls - pminW)), 
            35, (int)(leftW + dh * (tu_ls - pminW)), 
            imageHeight);
          g.drawLine((int)(leftW + dh * (tl_ls - pminW)), 
            35, (int)(leftW + dh * (tl_ls - pminW)), 
            imageHeight);

          DrawDotLine.drawBreakLine(g, (int)(leftW + dh * ((tu_ls + tl_ls) / 2.0D - pminW)), 
            50, (int)(leftW + dh * ((tu_ls + tl_ls) / 2.0D - pminW)), 
            imageHeight);

          g.setColor(Color.black);

          g.drawLine((int)(leftW + dh * (tl_ls - pminW)), 35, 
            (int)(leftW + dh * (tu_ls - pminW)), 35);

          g.drawString("USL=" + StringUtil.formatdouble(tu_ls, n), 
            (int)(leftW + dh * (tu_ls - pminW) - 10.0D), 35);

          g.drawString("LSL=" + StringUtil.formatdouble(tl_ls, n), 
            (int)(leftW + dh * (tl_ls - pminW) - 10.0D), 35);

          g.drawString("M=" + StringUtil.formatdouble((tu_ls + tl_ls) / 2.0D, n), 
            (int)(leftW + dh * ((tu_ls + tl_ls) / 2.0D - pminW) - 
            10.0D), 50);
        } else if (tu != null) {
          g.setColor(Color.red);
          g.drawLine((int)(leftW + dh * (tu_ls - pminW)), 
            35, (int)(leftW + dh * (tu_ls - pminW)), 
            imageHeight);
          g.setColor(Color.black);
          g.drawString("USL=" + StringUtil.formatdouble(tu_ls, n), 
            (int)(leftW + dh * (tu_ls - pminW) - 10.0D), 35);
        } else if (tl != null) {
          g.setColor(Color.red);
          g.drawLine((int)(leftW + dh * (tl_ls - pminW)), 
            35, (int)(leftW + dh * (tl_ls - pminW)), 
            imageHeight);
          g.setColor(Color.black);
          g.drawString("LSL=" + StringUtil.formatdouble(tl_ls, n), 
            (int)(leftW + dh * (tl_ls - pminW) - 10.0D), 35);
        }
      }

      if ((sigma > 2) && (s > 0.0D)) {
        g.setColor(Color.red);
        DrawDotLine.drawBreakLine(g, (int)(leftW + dh * (x_pj - s * sigma - pminW)), 
          65, (int)(leftW + dh * (x_pj - s * sigma - pminW)), 
          imageHeight);
        DrawDotLine.drawBreakLine(g, 
          (int)(leftW + dh * (x_pj + s * sigma - pminW)), 
          65, (int)(leftW + dh * (x_pj + s * sigma - pminW)), 
          imageHeight);

        g.drawString("-" + sigma + "σ", 
          (int)(leftW + dh * (x_pj - s * sigma - pminW) - 10.0D), 65);
        g.drawString(sigma + "σ", 
          (int)(leftW + dh * (x_pj + s * sigma - pminW) - 10.0D), 65);
        g.setColor(Color.black);
      }

      boolean ztYN = false;
      if ((length >= 30) && (s > 0.0D)) {
        double u3 = 0.0D; double u4 = 0.0D;
        for (int i = 0; i < length; i++) {
          u3 += Math.pow(itemList[i] - x_pj, 3.0D);
          u4 += Math.pow(itemList[i] - x_pj, 4.0D);
        }

        u3 /= length;
        u4 /= length;
        if ((u3 / Math.pow(s, 3.0D) > -2.0D * Math.sqrt(6.0D / length)) && 
          (u3 / Math.pow(s, 3.0D) < 2.0D * Math.sqrt(6.0D / length)) && 
          (u4 / Math.pow(s, 4.0D) - 3.0D > -2.0D * Math.sqrt(24.0D / length)) && 
          (u4 / Math.pow(s, 4.0D) - 3.0D < 2.0D * Math.sqrt(24.0D / length))) {
          ztYN = true;
        }
      }

      g.drawString("频数/频率(%)", leftWidth - 24, 20);

      double dhZT = s > 0.0D ? 
        (imageHeight - 30 - topHeight) * s * Math.sqrt(6.283185307179586D) : 0.0D;

      if ((ztx) && (s > 0.0D)) {
        double ztxX = (pmaxW - pminW + 2.0D * h) / 1000.0D;

        if (ztYN) {
          g.setColor(Color.black);
        }
        else if (length < 30) {
          g.setColor(Color.green);
        }
        else {
          g.setColor(Color.red);
        }

        for (int i = 0; i < 1000; i++) {
          double ztxY = Math.pow(2.718281828459045D, -Math.pow(pminW - h + i * ztxX - x_pj, 2.0D) / 
            2.0D / s / s) / s / Math.sqrt(6.283185307179586D);
          g.drawLine((int)(leftW + dh * (ztxX * i - h)), 
            (int)(imageHeight - ztxY * dhZT), 
            (int)(leftW + dh * (ztxX * i - h)), 
            (int)(imageHeight - ztxY * dhZT));
        }
      }

      g.setColor(Color.black);

      int k1 = f_max / 10;
      int nh = k1 * 10 == f_max ? k1 : k1 + 1;
      for (int i = 0; i < 10; i++) {
        if (nh * i > f_max) break;
        g.drawString(Integer.toString(nh * (i + 1)), 20, 
          (int)(imageHeight - ff * nh * (i + 1) + 5.0D));
        g.drawLine(leftWidth, (int)(imageHeight - ff * nh * (i + 1)), 
          leftWidth + 5, (int)(imageHeight - ff * nh * (i + 1)));

        g.drawString(StringUtil.formatdouble(100.0D / length * (nh * (i + 1)), 1), 
          leftWidth + 20, (int)(imageHeight - ff * nh * (i + 1) + 5.0D));
      }
    }
    else {
      Font font = g.getFont();
      Font font1 = new Font("ZFTFont", 1, 20);
      g.setFont(font1);
      g.setColor(Color.red);
      g.drawString("数据总个数少于10个不能绘制直方图！", 
        imageWidth / 10 + 20, 
        imageHeight / 2);
      g.setFont(font);
    }
  }
}