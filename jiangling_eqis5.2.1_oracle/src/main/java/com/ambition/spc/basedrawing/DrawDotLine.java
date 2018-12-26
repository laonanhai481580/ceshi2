package com.ambition.spc.basedrawing;

import java.awt.Graphics2D;

/**    
 * DrawDotLine.java
 * @authorBy wanglf
 *
 */
public class DrawDotLine
{
  public static void drawBreakLine(Graphics2D g, int x1, int y1, int x2, int y2)
  {
    int xL = x2 - x1;
    int yL = y2 - y1;
    double hL = Math.sqrt(xL * xL + yL * yL);
    if (hL <= 20.0D) {
      g.drawLine(x1, y1, x2, y2);
    }
    else {
      int m = (int)hL;
      m = m / 20 * 20 == m ? m / 20 : m / 20 + 1;
      double sinX = 1.0D * xL / hL;
      double sinY = 1.0D * yL / hL;

      for (int i = 0; i < m - 1; i++) {
        int dhCur = i * 20;
        g.drawLine((int)(x1 + dhCur * sinX), (int)(y1 + dhCur * sinY), 
          (int)(x1 + (dhCur + 15) * sinX), 
          (int)(y1 + (dhCur + 15) * sinY));
      }
      int dhCur = (m - 1) * 20;
      g.drawLine((int)(x1 + dhCur * sinX), (int)(y1 + dhCur * sinY), x2, y2);
    }
  }

  public static void drawDashDotLine(Graphics2D g, int x1, int y1, int x2, int y2)
  {
    int xL = x2 - x1;
    int yL = y2 - y1;
    double hL = Math.sqrt(xL * xL + yL * yL);
    if (hL <= 15.0D) {
      g.drawLine(x1, y1, x2, y2);
    }
    else {
      int m = (int)hL;
      m = m / 15 * 15 == m ? m / 15 : m / 15 + 1;
      double sinX = 1.0D * xL / hL;
      double sinY = 1.0D * yL / hL;

      for (int i = 0; i < m - 1; i++) {
        int dhCur = i * 15;
        g.drawLine((int)(x1 + dhCur * sinX), (int)(y1 + dhCur * sinY), 
          (int)(x1 + (dhCur + 7) * sinX), 
          (int)(y1 + (dhCur + 7) * sinY));
        g.drawLine((int)(x1 + (dhCur + 10) * sinX), 
          (int)(y1 + (dhCur + 10) * sinY), 
          (int)(x1 + (dhCur + 12) * sinX), 
          (int)(y1 + (dhCur + 12) * sinY));
      }
      int dhCur = (m - 1) * 15;
      g.drawLine((int)(x1 + dhCur * sinX), (int)(y1 + dhCur * sinY), x2, 
        y2);
    }
  }
}