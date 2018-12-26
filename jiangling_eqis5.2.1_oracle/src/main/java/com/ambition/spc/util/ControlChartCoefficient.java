package com.ambition.spc.util;

/**    
 * ControlChartCoefficient.java(样本容量n是多少对应的常数)
 * @authorBy wanglf
 *
 */
public class ControlChartCoefficient
{
  private double a1;
  private double a2;
  private double b3;
  private double b4;
  private double c4;
  private double d2;
  private double d3;
  private double d4;
  private double m3A2;

  public ControlChartCoefficient(int n)
  {
    if (n < 2) n = 2;
    switch (n) {
    case 2:
      this.c4 = 0.7979000000000001D;
      this.a1 = 2.659D;
      this.a2 = 1.88D;
      this.b3 = 0.0D;
      this.b4 = 3.267D;
      this.d2 = 1.128D;
      this.d3 = 0.0D;
      this.d4 = 3.267D;
      this.m3A2 = 1.88D;
      break;
    case 3:
      this.c4 = 0.8862D;
      this.a1 = 1.954D;
      this.a2 = 1.023D;
      this.b3 = 0.0D;
      this.b4 = 2.568D;
      this.d2 = 1.693D;
      this.d3 = 0.0D;
      this.d4 = 2.574D;
      this.m3A2 = 1.187D;
      break;
    case 4:
      this.c4 = 0.9213D;
      this.a1 = 1.628D;
      this.a2 = 0.729D;
      this.b3 = 0.0D;
      this.b4 = 2.266D;
      this.d2 = 2.059D;
      this.d3 = 0.0D;
      this.d4 = 2.282D;
      this.m3A2 = 0.796D;
      break;
    case 5:
      this.c4 = 0.94D;
      this.a1 = 1.427D;
      this.a2 = 0.577D;
      this.b3 = 0.0D;
      this.b4 = 2.089D;
      this.d2 = 2.326D;
      this.d3 = 0.0D;
      this.d4 = 2.114D;
      this.m3A2 = 0.691D;
      break;
    case 6:
      this.c4 = 0.9515D;
      this.a1 = 1.287D;
      this.a2 = 0.483D;
      this.b3 = 0.03D;
      this.b4 = 1.97D;
      this.d2 = 2.534D;
      this.d3 = 0.0D;
      this.d4 = 2.004D;
      this.m3A2 = 0.549D;
      break;
    case 7:
      this.c4 = 0.9594D;
      this.a1 = 1.182D;
      this.a2 = 0.419D;
      this.b3 = 0.118D;
      this.b4 = 1.882D;
      this.d2 = 2.704D;
      this.d3 = 0.076D;
      this.d4 = 1.924D;
      this.m3A2 = 0.509D;
      break;
    case 8:
      this.c4 = 0.965D;
      this.a1 = 1.099D;
      this.a2 = 0.373D;
      this.b3 = 0.185D;
      this.b4 = 1.815D;
      this.d2 = 2.847D;
      this.d3 = 0.136D;
      this.d4 = 1.864D;
      this.m3A2 = 0.432D;
      break;
    case 9:
      this.c4 = 0.9693000000000001D;
      this.a1 = 1.032D;
      this.a2 = 0.337D;
      this.b3 = 0.239D;
      this.b4 = 1.761D;
      this.d2 = 2.97D;
      this.d3 = 0.184D;
      this.d4 = 1.816D;
      this.m3A2 = 0.412D;
      break;
    case 10:
      this.c4 = 0.9727D;
      this.a1 = 0.975D;
      this.a2 = 0.308D;
      this.b3 = 0.284D;
      this.b4 = 1.716D;
      this.d2 = 3.078D;
      this.d3 = 0.223D;
      this.d4 = 1.777D;
      this.m3A2 = 0.363D;
      break;
    case 11:
      this.c4 = 0.9754D;
      this.a1 = 0.9270000000000001D;
      this.a2 = 0.285D;
      this.b3 = 0.321D;
      this.b4 = 1.679D;
      this.d2 = 3.173D;
      this.d3 = 0.256D;
      this.d4 = 1.744D;
      break;
    case 12:
      this.c4 = 0.9776D;
      this.a1 = 0.886D;
      this.a2 = 0.266D;
      this.b3 = 0.354D;
      this.b4 = 1.646D;
      this.d2 = 3.258D;
      this.d3 = 0.283D;
      this.d4 = 1.717D;
      break;
    case 13:
      this.c4 = 0.9794000000000001D;
      this.a1 = 0.85D;
      this.a2 = 0.249D;
      this.b3 = 0.382D;
      this.b4 = 1.618D;
      this.d2 = 3.336D;
      this.d3 = 0.307D;
      this.d4 = 1.693D;
      break;
    case 14:
      this.c4 = 0.981D;
      this.a1 = 0.817D;
      this.a2 = 0.235D;
      this.b3 = 0.406D;
      this.b4 = 1.594D;
      this.d2 = 3.407D;
      this.d3 = 0.328D;
      this.d4 = 1.672D;
      break;
    case 15:
      this.c4 = 0.9823D;
      this.a1 = 0.789D;
      this.a2 = 0.223D;
      this.b3 = 0.428D;
      this.b4 = 1.572D;
      this.d2 = 3.472D;
      this.d3 = 0.347D;
      this.d4 = 1.653D;
      break;
    case 16:
      this.c4 = 0.9835D;
      this.a1 = 0.763D;
      this.a2 = 0.212D;
      this.b3 = 0.448D;
      this.b4 = 1.552D;
      this.d2 = 3.532D;
      this.d3 = 0.363D;
      this.d4 = 1.637D;
      break;
    case 17:
      this.c4 = 0.9845D;
      this.a1 = 0.739D;
      this.a2 = 0.203D;
      this.b3 = 0.466D;
      this.b4 = 1.534D;
      this.d2 = 3.588D;
      this.d3 = 0.378D;
      this.d4 = 1.622D;
      break;
    case 18:
      this.c4 = 0.9854000000000001D;
      this.a1 = 0.718D;
      this.a2 = 0.194D;
      this.b3 = 0.482D;
      this.b4 = 1.518D;
      this.d2 = 3.64D;
      this.d3 = 0.391D;
      this.d4 = 1.608D;
      break;
    case 19:
      this.c4 = 0.9862D;
      this.a1 = 0.698D;
      this.a2 = 0.187D;
      this.b3 = 0.497D;
      this.b4 = 1.503D;
      this.d2 = 3.689D;
      this.d3 = 0.403D;
      this.d4 = 1.597D;
      break;
    case 20:
      this.c4 = 0.9869D;
      this.a1 = 0.6800000000000001D;
      this.a2 = 0.18D;
      this.b3 = 0.51D;
      this.b4 = 1.49D;
      this.d2 = 3.735D;
      this.d3 = 0.415D;
      this.d4 = 1.585D;
      break;
    case 21:
      this.c4 = 0.9876D;
      this.a1 = 0.663D;
      this.a2 = 0.173D;
      this.b3 = 0.523D;
      this.b4 = 1.477D;
      this.d2 = 3.778D;
      this.d3 = 0.425D;
      this.d4 = 1.575D;
      break;
    case 22:
      this.c4 = 0.9882D;
      this.a1 = 0.647D;
      this.a2 = 0.167D;
      this.b3 = 0.534D;
      this.b4 = 1.466D;
      this.d2 = 3.819D;
      this.d3 = 0.434D;
      this.d4 = 1.566D;
      break;
    case 23:
      this.c4 = 0.9887D;
      this.a1 = 0.633D;
      this.a2 = 0.162D;
      this.b3 = 0.545D;
      this.b4 = 1.455D;
      this.d2 = 3.858D;
      this.d3 = 0.443D;
      this.d4 = 1.557D;
      break;
    case 24:
      this.c4 = 0.9892D;
      this.a1 = 0.619D;
      this.a2 = 0.157D;
      this.b3 = 0.5550000000000001D;
      this.b4 = 1.445D;
      this.d2 = 3.859D;
      this.d3 = 0.451D;
      this.d4 = 1.548D;
      break;
    case 25:
      this.c4 = 0.9896D;
      this.a1 = 0.606D;
      this.a2 = 0.153D;
      this.b3 = 0.565D;
      this.b4 = 1.435D;
      this.d2 = 3.931D;
      this.d3 = 0.459D;
      this.d4 = 1.541D;
    }

    if (n > 25) {
      this.c4 = (4.0D * (n - 1) / (4.0D * n - 3.0D));
      this.a1 = (3.0D / Math.sqrt(n) / this.c4);
      this.a2 = 0.153D;
      this.b3 = (1.0D - 3.0D / Math.sqrt(2.0D * n - 2.0D) / this.c4);
      this.b4 = (1.0D + 3.0D / Math.sqrt(2.0D * n - 2.0D) / this.c4);
      this.d3 = 0.223D;
      this.d4 = 1.777D;
    }

    if (n > 10)
      this.m3A2 = 0.363D;
  }

  public double getA1()
  {
    return this.a1;
  }

  public void setA1(double a1) {
    this.a1 = a1;
  }

  public double getA2() {
    return this.a2;
  }

  public void setA2(double a2) {
    this.a2 = a2;
  }

  public double getB3() {
    return this.b3;
  }

  public void setB3(double b3) {
    this.b3 = b3;
  }

  public double getB4() {
    return this.b4;
  }

  public void setB4(double b4) {
    this.b4 = b4;
  }

  public double getC4() {
    return this.c4;
  }

  public void setC4(double c4) {
    this.c4 = c4;
  }

  public double getD3() {
    return this.d3;
  }

  public void setD3(double d3) {
    this.d3 = d3;
  }

  public double getD4() {
    return this.d4;
  }

  public void setD4(double d4) {
    this.d4 = d4;
  }

  public double getM3A2() {
    return this.m3A2;
  }

  public void setM3A2(double m3A2) {
    this.m3A2 = m3A2;
  }
  public double getD2() {
    return this.d2;
  }
  public void setD2(double d2) {
    this.d2 = d2;
  }
}