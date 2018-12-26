package com.ambition.spc.jlanalyse.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.commons.lang.xwork.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ambition.spc.basedrawing.DrawBaseControlChart;
import com.ambition.spc.basedrawing.DrawBaseElement;
import com.ambition.spc.basedrawing.DrawDotLine;
import com.ambition.spc.jlanalyse.entity.JLControlChartParam;
import com.ambition.spc.util.StringUtil;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**    
 * DrawJLControlChart.java(awt画计量图标类)
 * @authorBy wanglf
 *
 */
public class CopyOfDrawJLControlChart
{
  public static BufferedImage drawJLChart(JLControlChartParam jLControlChartData)
  {
    int length = jLControlChartData.getArrayx().length;

    int n = (jLControlChartData.getImageWidth() - 
      jLControlChartData.getLeftWidth() - 
      jLControlChartData.getRightWidth()) / 
      jLControlChartData.getDotWidth();

    if (n < 1) {
      n = 1;
    }

    int offside = jLControlChartData.getOffside();
    int startside = 0;
    //修改前台翻页显示的问题
    String currentPageStr = Struts2Utils.getParameter("currentPage");
    if(StringUtils.isNotEmpty(currentPageStr)){
    	int currentPage = Integer.valueOf(currentPageStr);
    	startside = currentPage * n;
    	if(startside<0){
    		startside = 0;
    	}else if(startside > length-1){
    		currentPage = length/n-1;
    		if(length%n>0){
    			currentPage++;
    		}
    		startside = currentPage*n;
    	}
    }else{
    	if (offside < n) {
	      offside = n;
	    }

	    if (offside > length) {
	      offside = length;
	    }

	    startside = offside > n ? offside - n : 0;
    }

    return drawDynJLChart(startside, jLControlChartData.getTitleY(), 
      jLControlChartData.getSamplingNum(), 
      jLControlChartData.getChartType(), 
      jLControlChartData.getArrayx(), 
      jLControlChartData.getArrayy(), 
      jLControlChartData.getUcl1(), 
      jLControlChartData.getLcl1(), 
      jLControlChartData.getCl1(), 
      jLControlChartData.getUcl2(), 
      jLControlChartData.getLcl2(), 
      jLControlChartData.getCl2(), 
      jLControlChartData.getControlsta(), 
      jLControlChartData.getAbnormalPosUp(), 
      jLControlChartData.getAbnormalPosDow(), 
      jLControlChartData.getDateTime(), 
      jLControlChartData.getTu(), jLControlChartData.getTl(), 
      jLControlChartData.getImageHeight(), 
      jLControlChartData.getImageWidth(), 
      jLControlChartData.getTopHeight(), 
      jLControlChartData.getBottomHeight(), 
      jLControlChartData.getLeftWidth(), 
      jLControlChartData.getRightWidth(), 
      jLControlChartData.getDotWidth(), n, 
      jLControlChartData.getXCoorInter(), 
      jLControlChartData.getDataPrecision(), 
      jLControlChartData.getUpTitle(), 
      jLControlChartData.getLowTitle());
  }

  private static BufferedImage drawDynJLChart(int startside, String titleY, int samplingNum, int chartType, double[] arrayx, double[] arrayy, double ucl1, double lcl1, double cl1, double ucl2, double lcl2, double cl2, int controlSta, boolean[] abnormalPosUp, boolean[] abnormalPosDow, String[] dateTime, Double tu, Double tl, int imageHeight, int imageWidth, int topHeight, int bottomHeight, int leftWidth, int rightWidth, int dotWid, int showDot, int xCoor, int dataPrecision, String upTitle, String lowTitle)
  {
    try
    {
      int endDot = arrayx.length < showDot ? arrayx.length : showDot;

      BufferedImage image = new BufferedImage(imageWidth, imageHeight, 
        1);
      Graphics2D g = (Graphics2D)image.getGraphics();
      DrawBaseElement.setBackgroundCloor(g, imageHeight);

      
      g.fillRect(1, 1, imageWidth - 2, imageHeight - 2);

      
      int centHeight = imageHeight - topHeight - bottomHeight;
      double mLowValue = lcl2 <= 0.0D ? centHeight / 18.0D : 0.0D;

      g.setColor(Color.black);

      int bottomLine = imageHeight - bottomHeight;
      double coorH18_1 = centHeight / 18.0D;

      //画区域背景色
    	Color c1=g.getColor();  
		g.setColor(new Color(255,255,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 2.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c1);
		
		Color c2=g.getColor();  
		g.setColor(new Color(181,251,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 3.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c2);
		
		Color c3=g.getColor();  
		g.setColor(new Color(8,248,32));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 4.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c3);
		
		Color c4=g.getColor();  
		g.setColor(new Color(8,248,32));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 5.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c4);
		
		Color c5=g.getColor();  
		g.setColor(new Color(181,251,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 6.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c5);
		
		Color c6=g.getColor();  
		g.setColor(new Color(255,255,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 7.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c6);
		
		
		
    	Color c7=g.getColor();  
		g.setColor(new Color(255,255,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 12.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c7);
		
		Color c8=g.getColor();  
		g.setColor(new Color(181,251,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 13.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c8);
		
		Color c9=g.getColor();  
		g.setColor(new Color(8,248,32));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 14.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c9);
		
		Color c10=g.getColor();  
		g.setColor(new Color(8,248,32));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 15.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c10);
		
		Color c11=g.getColor();  
		g.setColor(new Color(181,251,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 16.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c11);
		
		Color c12=g.getColor();  
		g.setColor(new Color(255,255,0));
		g.fillRect(leftWidth, (int) (topHeight + coorH18_1 * 17.0D),
				imageWidth - rightWidth-60,
				(int) (topHeight + coorH18_1 * 3.0D)
						- (int) (topHeight + coorH18_1 * 2.0D));
		g.setColor(c12);
		
      
		
      DrawBaseElement.DrawAxes(g, imageHeight, imageWidth, bottomHeight, 
        leftWidth);

      g.drawString(titleY, 30, topHeight / 2 + 5);
      if (chartType < 4) {
        g.drawString("n=" + samplingNum, leftWidth + 25, 
          (int)(topHeight + coorH18_1 * 2.0D - 2.0D));
      }

      g.drawString(upTitle, imageWidth / 2, 
        (int)(topHeight + centHeight / 2 + coorH18_1 * 2.0D));
      g.drawString(lowTitle, imageWidth / 2, 
        bottomLine + 33);

      g.drawLine(leftWidth, (int)(topHeight + coorH18_1 * 5.0D), 
        imageWidth - rightWidth, (int)(topHeight + coorH18_1 * 5.0D));
      g.drawLine(leftWidth, (int)(topHeight + coorH18_1 * 14.0D + mLowValue), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 14.0D + mLowValue));

      if (controlSta == 2) {
        DrawDotLine.drawDashDotLine(g, leftWidth, 
          (int)(topHeight + coorH18_1 * 2.0D), 
          imageWidth - rightWidth, 
          (int)(topHeight + coorH18_1 * 2.0D));
        DrawDotLine.drawDashDotLine(g, leftWidth, 
          (int)(topHeight + coorH18_1 * 8.0D), 
          imageWidth - rightWidth, 
          (int)(topHeight + coorH18_1 * 8.0D));

        DrawDotLine.drawDashDotLine(g, leftWidth, 
          (int)(topHeight + coorH18_1 * 11.0D + mLowValue), 
          imageWidth - rightWidth, 
          (int)(topHeight + coorH18_1 * 11.0D + mLowValue));
        if (lcl2 > 0.0D)
          DrawDotLine.drawDashDotLine(g, leftWidth, 
            (int)(topHeight + coorH18_1 * 17.0D + 
            mLowValue), 
            imageWidth - rightWidth, 
            (int)(topHeight + coorH18_1 * 17.0D + 
            mLowValue));
      }
      else
      {
    	  DrawDotLine.drawBreakLine(g, leftWidth, (int)(topHeight + coorH18_1 * 2.0D), 
          imageWidth - rightWidth, 
          (int)(topHeight + coorH18_1 * 2.0D));
    	  
         DrawDotLine.drawBreakLine(g, leftWidth, (int)(topHeight + coorH18_1 * 8.0D), 
          imageWidth - rightWidth, 
          (int)(topHeight + coorH18_1 * 8.0D));

        DrawDotLine.drawBreakLine(g, leftWidth, 
          (int)(topHeight + coorH18_1 * 11.0D + mLowValue), 
          imageWidth - rightWidth, 
          (int)(topHeight + coorH18_1 * 11.0D + mLowValue));
        if (lcl2 > 0.0D) {
          DrawDotLine.drawBreakLine(g, leftWidth, 
            (int)(topHeight + coorH18_1 * 17.0D + mLowValue), 
            imageWidth - rightWidth, 
            (int)(topHeight + coorH18_1 * 17.0D + mLowValue));
        }

      }

      DrawDotLine.drawBreakLine(g, leftWidth, (int)(topHeight + coorH18_1 * 3.0D), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 3.0D));
      
        DrawDotLine.drawBreakLine(g, leftWidth, (int)(topHeight + coorH18_1 * 4.0D), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 4.0D));
      DrawDotLine.drawBreakLine(g, leftWidth, (int)(topHeight + coorH18_1 * 6.0D), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 6.0D));
      DrawDotLine.drawBreakLine(g, leftWidth, (int)(topHeight + coorH18_1 * 7.0D), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 7.0D));

      DrawDotLine.drawBreakLine(g, leftWidth, 
        (int)(topHeight + coorH18_1 * 12.0D + mLowValue), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 12.0D + mLowValue));
      DrawDotLine.drawBreakLine(g, leftWidth, 
        (int)(topHeight + coorH18_1 * 13.0D + mLowValue), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 13.0D + mLowValue));
      DrawDotLine.drawBreakLine(g, leftWidth, 
        (int)(topHeight + coorH18_1 * 15.0D + mLowValue), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 15.0D + mLowValue));
      DrawDotLine.drawBreakLine(g, leftWidth, 
        (int)(topHeight + coorH18_1 * 16.0D + mLowValue), 
        imageWidth - rightWidth, 
        (int)(topHeight + coorH18_1 * 16.0D + mLowValue));

      double yTu = 3.0D * coorH18_1 / (ucl1 - cl1);
      double yTl = 3.0D * coorH18_1 / (cl1 - lcl1);
      double yTuOld = yTu;
      double yTlOld = yTl;
      double yTuOld2 = 3.0D * coorH18_1 / (ucl2 - cl2);
      double yTlOld2 = 3.0D * coorH18_1 / (lcl2 - cl2);
      if ((tu != null) || (tl != null)) {
        g.setColor(Color.red);
        double tu_ls = 0.0D;
        double tl_ls = 0.0D;
        double ydhu = 0.0D;
        if ((tu != null) && (ucl1 != cl1)) {
          tu_ls = tu.doubleValue();
          if (tu_ls > cl1 + (ucl1 - cl1) / 3.0D * 5.0D) {
            yTu = 2.0D * coorH18_1 / (tu_ls - ucl1);
            ydhu = topHeight;
          }
          else if (tu_ls > cl1) {
            ydhu = topHeight + coorH18_1 * 5.0D - yTu * (tu_ls - cl1);
          }
          else if (tu_ls > lcl1) {
            ydhu = topHeight + coorH18_1 * 5.0D - (tu_ls - lcl1) * yTl;
          }

        }

        if ((tl != null) && (lcl1 != cl1))
        {
          tl_ls = tl.doubleValue();
          double ydh;
          if (tl_ls < cl1 - (cl1 - lcl1) / 3.0D * 5.0D) {
            yTl = 2.0D * coorH18_1 / (lcl1 - tl_ls);
            ydh = topHeight + coorH18_1 * 10.0D;
          }
          else
          {
            if (tl_ls < cl1) {
              ydh = topHeight + coorH18_1 * 5.0D - yTl * (tl_ls - cl1);
            }
            else
            {
              if (tl_ls < ucl1) {
                ydh = topHeight + coorH18_1 * 5.0D - (tl_ls - cl1) * yTu;
              }
              else {
                ydh = topHeight + coorH18_1 * 2.0D - (tl_ls - ucl1) * yTu;
              }
            }
          }
          DrawDotLine.drawBreakLine(g, leftWidth, (int)ydh, 
            imageWidth - rightWidth, (int)ydh);
          g.drawString("TL=" + StringUtil.formatdouble(tl_ls, dataPrecision), 
            imageWidth - 65, (int)ydh);
        }

        if ((tu != null) && (ucl1 != cl1)) {
          tu_ls = tu.doubleValue();
          if (tu_ls <= lcl1) {
            ydhu = topHeight + coorH18_1 * 8.0D - (tu_ls - cl1) * yTl;
          }
          DrawDotLine.drawBreakLine(g, leftWidth, (int)ydhu, 
            imageWidth - rightWidth, (int)ydhu);
          g.drawString("TU=" + StringUtil.formatdouble(tu_ls, dataPrecision), 
            imageWidth - 65, (int)ydhu);
        }

      }

      g.setColor(Color.black);

      g.drawString("UCL="+ucl1, imageWidth - 80, 
        (int)(topHeight + coorH18_1 * 2.0D));
      g.drawString("CL="+cl1, imageWidth - 65, 
        (int)(topHeight + coorH18_1 * 5.0D));
      g.drawString("LCL="+lcl1, imageWidth - 65, 
        (int)(topHeight + coorH18_1 * 8.0D));
      g.drawString(StringUtil.formatdouble(ucl1, dataPrecision), 10, 
        (int)(topHeight + coorH18_1 * 2.0D + 5.0D));
      g.drawString(StringUtil.formatdouble(cl1, dataPrecision), 10, 
        (int)(topHeight + coorH18_1 * 5.0D + 5.0D));
      g.drawString(StringUtil.formatdouble(lcl1, dataPrecision), 10, 
        (int)(topHeight + coorH18_1 * 8.0D + 5.0D));

      g.drawString("UCL="+ucl2, imageWidth - 65, 
        (int)(topHeight + coorH18_1 * 11.0D + mLowValue));
      g.drawString("CL="+cl2, imageWidth - 65, 
        (int)(topHeight + coorH18_1 * 14.0D + mLowValue));

      g.drawString("LCL="+lcl2, imageWidth - 65, 
        (int)(topHeight + coorH18_1 * 17.0D + mLowValue - 1.0D));

      g.drawString(StringUtil.formatdouble(ucl2, dataPrecision), 10, 
        (int)(topHeight + coorH18_1 * 11.0D + mLowValue + 5.0D));
      g.drawString(StringUtil.formatdouble(cl2, dataPrecision), 10, 
        (int)(topHeight + coorH18_1 * 14.0D + mLowValue + 5.0D));
      g.drawString(StringUtil.formatdouble(lcl2, dataPrecision), 10, 
        (int)(topHeight + coorH18_1 * 17.0D + mLowValue + 5.0D));
      
      g.drawString("A", leftWidth + 6, (int)(topHeight + coorH18_1 * 3.0D - 1.0D));
      g.drawString("B", leftWidth + 6, (int)(topHeight + coorH18_1 * 4.0D - 1.0D));
      g.drawString("C", leftWidth + 6, (int)(topHeight + coorH18_1 * 5.0D - 1.0D));
      g.drawString("C", leftWidth + 6, (int)(topHeight + coorH18_1 * 5.0D + 11.0D));
      g.drawString("B", leftWidth + 6, (int)(topHeight + coorH18_1 * 6.0D + 11.0D));
      g.drawString("A", leftWidth + 6, (int)(topHeight + coorH18_1 * 7.0D + 11.0D));

      
      
      
      g.drawString("A", leftWidth + 6, 
        (int)(topHeight + coorH18_1 * 12.0D - 1.0D + mLowValue));
      g.drawString("B", leftWidth + 6, 
        (int)(topHeight + coorH18_1 * 13.0D - 1.0D + mLowValue));
      g.drawString("C", leftWidth + 6, 
        (int)(topHeight + coorH18_1 * 14.0D - 1.0D + mLowValue));
      g.drawString("C", leftWidth + 6, 
        (int)(topHeight + coorH18_1 * 14.0D + 11.0D + mLowValue));
      g.drawString("B", leftWidth + 6, 
        (int)(topHeight + coorH18_1 * 15.0D + 11.0D + mLowValue));
      g.drawString("A", leftWidth + 6, 
        (int)(topHeight + coorH18_1 * 16.0D + 11.0D + mLowValue));
      //缓存坐标集合
      JSONArray points = new JSONArray();
      
      if (showDot > 1)
      {
        double leftInterval = 
          leftWidth + dotWid;
        if (endDot > 0)
        {
          if (dateTime == null) {
            g.drawString("序号", imageWidth - rightWidth, 
              bottomLine + 35);
            for (int i = 0; i < endDot; i++)
              if (i % xCoor == 0) {
            	g.drawString(i + startside + 1 + "", 
                        (int)(leftInterval + dotWid * i), 
                        bottomLine + 11);
                g.drawLine((int)(leftInterval + dotWid * i), 
                  bottomLine, 
                  (int)(leftInterval + dotWid * i), 
                  bottomLine - 6);
              }
              else {
                g.drawLine((int)(leftInterval + dotWid * i), 
                  bottomLine, 
                  (int)(leftInterval + dotWid * i), 
                  bottomLine - 3);
              }
          }
          else
          {
            g.drawString("时间", imageWidth - rightWidth, 
              bottomLine + 35);
            for (int i = 0; i < endDot; i++) {
              if (i % xCoor == 0)
              {
                int len = dateTime[(startside + i)].length();
                if (len < 10) {
                  g.drawString(dateTime[(i + startside)], 
                    (int)(leftInterval + dotWid * i), 
                    bottomLine + 11);
                }
                else {
                  g.drawString(dateTime[(i + startside)].substring(5, 10), 
                    (int)(leftInterval + dotWid * i), 
                    bottomLine + 11);
                  if (len > 15) {
                    g.drawString(dateTime[(i + startside)].substring(11, 16), 
                      (int)(leftInterval + dotWid * i), 
                      bottomLine + 22);
                  }
                  else if (len > 10) {
                    g.drawString(dateTime[(i + startside)].substring(10), 
                      (int)(leftInterval + dotWid * i), 
                      bottomLine + 22);
                  }

                }

                g.drawLine((int)(leftInterval + dotWid * i), 
                  bottomLine, 
                  (int)(leftInterval + dotWid * i), 
                  bottomLine - 6);
              }
              else {
                g.drawLine((int)(leftInterval + dotWid * i), 
                  bottomLine, 
                  (int)(leftInterval + dotWid * i), 
                  bottomLine - 3);
              }

            }

          }

          double mx1 = 0.0D; double my1 = 0.0D;
          JSONArray datas = (JSONArray) Struts2Utils.getRequest().getAttribute("datas");
          if(datas == null){
        	  datas = new JSONArray();
          }
          int dataSize = datas.size();
          for (int i = 0; i < endDot; i++) {
        	if(startside+i>arrayx.length-1){
        		break;
        	}
            double mx = leftInterval + dotWid * i;
            double my;
            if (arrayx[(startside + i)] > cl1)
            {
              if (ucl1 == cl1) {
                my = topHeight + coorH18_1 * 5.0D;
              }
              else
              {
                if (arrayx[(startside + i)] > ucl1) {
                  my = topHeight + coorH18_1 * 2.0D - 
                    (arrayx[(startside + i)] - ucl1) * yTu;
                }
                else
                  my = topHeight + coorH18_1 * 5.0D - 
                    (arrayx[(startside + i)] - cl1) * yTuOld;
              }
            }
            else
            {
              if (lcl1 == cl1) {
                my = topHeight + coorH18_1 * 5.0D;
              }
              else
              {
                if (arrayx[(startside + i)] < lcl1) {
                  my = topHeight + coorH18_1 * 8.0D - 
                    (arrayx[(startside + i)] - lcl1) * yTl;
                }
                else {
                  my = topHeight + coorH18_1 * 5.0D - 
                    (arrayx[(startside + i)] - cl1) * yTlOld;
                }
              }
            }
            JSONObject point = new JSONObject();
            if(dataSize>0){
            	JSONObject d = datas.getJSONObject(startside+ i);
            	point.put("id",d.get("id"));
            	point.put("date",d.get("date"));
            }
            if ((abnormalPosUp != null) && (abnormalPosUp[(startside + i)])) {
              g.setColor(Color.red);
              g.fillOval((int)mx - 3, (int)my - 3, 7, 7);
              //记录位置
              point.put("x", (int)mx - 3);
              point.put("y", (int)my - 3);
              point.put("width",7);
              point.put("height",7);
            }
            else {
              g.setColor(Color.black);
              g.fillOval((int)mx - 2, (int)my - 2, 5, 5);
              //记录位置
              point.put("x", (int)mx - 2);
              point.put("y", (int)my - 2);
              point.put("width",5);
              point.put("height",5);
            }
            if (i > 0)
            {
              g.setColor(Color.blue);
              g.drawLine((int)mx1, (int)my1, (int)mx, (int)my);
            }
            mx1 = mx;
            my1 = my;
            //添加到坐标集中
            points.add(point);
          }
          int mk = endDot;
          if (chartType == 4) {
            mk = endDot - 1;
          }

          for (int i = 0; i < mk; i++)
          {
        	  if(startside+i>arrayy.length-1){
        		  break;
          	  }
            double mx;
            if (chartType == 4)
              mx = leftInterval + dotWid * (1 + i);
            else
              mx = leftInterval + dotWid * i;
            double my;
            if (arrayy[(startside + i)] > cl2)
            {
              if (ucl2 == cl2) {
                my = topHeight + coorH18_1 * 14.0D + mLowValue;
              }
              else
                my = topHeight + coorH18_1 * 14.0D - 
                  (arrayy[(startside + i)] - cl2) * yTuOld2 + mLowValue;
            }
            else
            {
              if (lcl2 == cl2) {
                my = topHeight + coorH18_1 * 14.0D + mLowValue;
              }
              else {
                my = topHeight + coorH18_1 * 14.0D + 
                  (arrayy[(startside + i)] - cl2) * yTlOld2 + mLowValue;
              }
            }

            JSONObject point = new JSONObject();
            if(dataSize>0){
            	JSONObject d = datas.getJSONObject(startside + i);
            	point.put("id",d.get("id"));
            	point.put("date",d.get("date"));
            }
            if ((abnormalPosDow != null) && (abnormalPosDow[(startside + i)] )) {
              g.setColor(Color.red);
              g.fillOval((int)mx - 3, (int)my - 3, 7, 7);
              //记录位置
              point.put("x", (int)mx - 3);
              point.put("y", (int)my - 3);
              point.put("width",7);
              point.put("height",7);
            }
            else {
              g.setColor(Color.black);
              g.fillOval((int)mx - 2, (int)my - 2, 5, 5);
              //记录位置
              point.put("x", (int)mx - 2);
              point.put("y", (int)my - 2);
              point.put("width",5);
              point.put("height",5);
            }
            if (i > 0)
            {
              g.setColor(Color.blue);
              g.drawLine((int)mx1, (int)my1, (int)mx, (int)my);
            }
            mx1 = mx;
            my1 = my;
            //添加到坐标集中
            points.add(point);
          }
        }
      }
      Struts2Utils.getSession().setAttribute("featurePoints",points);
      return image; } catch (Exception e) {
    	  Struts2Utils.getSession().setAttribute("featurePoints",new JSONArray());
    }
    return DrawBaseControlChart.DrawErrorChart(imageWidth, imageHeight);
  }
}