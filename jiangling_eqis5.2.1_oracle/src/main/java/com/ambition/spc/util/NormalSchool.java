package com.ambition.spc.util;


/**    
 * NormalSchool.java
 * @authorBy wanglf
 *
 */

public class NormalSchool
{
  public static double bpnt(int na, int nb, double p)
  {
    double X = 0.0D; double c = 0.0D;
    int f1 = nb; int f2 = na; int n = 0;

    if (nb <= 1) {
      double q = 0.5D * p;
      n = na;
      if (n == 2) {
        double a = Math.pow(1.0D - 2.0D * q, 2.0D);
        X = Math.pow(2.0D * a / (1.0D - a), 0.5D);
      }
      else if (n < 2) {
        X = 3.141592653589793D * (0.5D - q);
        X = Math.sin(X) / Math.cos(X);
      }
      else {
        c = npnt(q);
        double a = n;
        double b = c * c;
        X = c + (b + 1.0D) * c / (4.0D * a) + 
          ((5.0D * b + 16.0D) * b + 3.0D) * c / (96.0D * a * a) + 
          (((3.0D * b + 19.0D) * b + 17.0D) * b - 15.0D) * c / (384.0D * (a * a * a)) + 
          ((((79.0D * b + 776.0D) * b + 1482.0D) * b - 1920.0D) * b - 945.0D) * c / (
          92160.0D * a * a * a * a) + 
          (((((27.0D * b + 339.0D) * b + 930.0D) * b - 1782.0D) * b - 765.0D) * b + 
          17955.0D) * c / (368640.0D * a * a * a * a * a);
      }

      X *= X;
      if ((na == 1) && (nb != 1)) {
        X = 1.0D / X;
      }

    }
    else if (na <= 1) {
      double q = (1.0D - p) * 0.5D;
      n = nb;
      if (n == 2) {
        double a = (1.0D - 2.0D * q) * (1.0D - 2.0D * q);
        X = Math.pow(2.0D * a / (1.0D - a), 0.5D);
      }
      else if (n < 2) {
        X = 3.141592653589793D * (0.5D - q);
        X = Math.sin(X) / Math.cos(X);
      }
      else {
        c = npnt(q);
        double a = n;
        double b = c * c;
        X = c + (b + 1.0D) * c / (4.0D * a) + 
          ((5.0D * b + 16.0D) * b + 3.0D) * c / (96.0D * a * a) + 
          (((3.0D * b + 19.0D) * b + 17.0D) * b - 15.0D) * c / (384.0D * (a * a * a)) + 
          ((((79.0D * b + 776.0D) * b + 1482.0D) * b - 1920.0D) * b - 945.0D) * c / (
          92160.0D * a * a * a * a) + 
          (((((27.0D * b + 339.0D) * b + 930.0D) * b - 1782.0D) * b - 765.0D) * b + 
          17955.0D) * c / (368640.0D * a * a * a * a * a);
      }

      X *= X;
      if ((na == 1) && (nb != 1))
        X = 1.0D / X;
    }
    else
    {
      X = npnt(p);
      double a = 1 - 2 / (9 * f1);
      double b = 1 - 2 / (9 * f2);
      c = b * b - (1.0D - b) * X * X;
      double d = Math.pow(Math.abs(a * a * b * b - c * (a * a - (1.0D - a) * X * X)), 
        0.5D);
      if (p > 0.5D) {
        d = -d;
      }
      X = Math.pow((a * b + d) / c, 3.0D);
    }

    X = f2 / (f2 + f1 * Math.abs(X));
    if ((n <= 2) && (
      (na == 1) || (nb == 1))) {
      return X;
    }

    while (true)
    {
      double[] pd = bta(na, nb, X);
      double a = pd[1] * ((0.5D * f2 - 1.0D) / X + (0.5D * f1 - 1.0D) / (1.0D - X));
      double b = pd[1] * pd[1] - 2.0D * (pd[0] - p);
      double r;
      if (b <= 0.0D) {
        r = -pd[1] / a;
      }
      else {
        r = 2.0D * (pd[0] - p) / (-pd[1] - Math.pow(b, 0.5D));
      }
      X += r;
      if ((Math.abs(r) - 1.E-009D <= 0.0D) || (Double.isNaN(r))) return X;
    }
  }

  public static double[] bta(int ka, int kb, double X)
  {
    int di = 0;
    double w = Math.sqrt(1.0D - X);
    double q = Math.sqrt(X);
    int m = kb / 2 * 2 - kb + 2;
    int n = ka / 2 * 2 - ka + 2;
    double[] pd = new double[2];
    if (m > 1) {
      if (n <= 1) {
        pd[0] = q;
        pd[1] = ((1.0D - X) * pd[0] / 2.0D);
      }
      else {
        pd[0] = X;
        pd[1] = (X * (1.0D - X));
      }

    }
    else if (n <= 1) {
      pd[0] = (1.0D - 0.6366197724D * Math.atan(w / q));
      pd[1] = (0.3183098862D * w * q);
    }
    else {
      pd[0] = (1.0D - w);
      pd[1] = (w * X / 2.0D);
    }

    int dm = m;
    int i = n;
    while (i <= ka) {
      di = i;
      if (ka <= i) {
        break;
      }
      pd[0] -= 2.0D * pd[1] / di;
      pd[1] = (pd[1] * X * (dm + di) / di);
      i += 2;
    }
    i = m;
    while (i <= kb) {
      if (kb <= i) break;
      dm = i;
      pd[0] += 2.0D * pd[1] / dm;
      pd[1] = (pd[1] * (1.0D - X) * (dm + di) / dm);
      i += 2;
    }
    if (X * (1.0D - X) == 0.0D) {
      pd[1] = 0.0D;
    }
    else {
      pd[1] /= X * (1.0D - X);
    }
    return pd;
  }

  public static double fq(int n1, int n2, double f)
  {
    f = n2 / (n2 + f * n1);
    double[] pd = bta(n2, n1, f);
    return pd[0];
  }

  public static double fpnt(int n1, int n2, double q)
  {
    double F = bpnt(n2, n1, q);
    return (1.0D - F) * n2 / (n1 * F);
  }

  public static double npnt(double q)
  {
    double p = q;
    double u;
    if (q - 0.5D == 0.0D) {
      u = 0.0D;
    }
    else {
      if (q - 0.5D > 0.0D) {
        p = 1.0D - q;
      }
      double Y = -Math.log(p * (1.0D - p) * 4.0D) / Math.log(2.718281828459045D);
      u = Math.sqrt(Y * (2.0611786D - 5.7262704D / (Y + 11.640594999999999D)));
      while (true) {
        double q1 = nq(u);
        double dens = Math.exp(-0.5D * u * u) * 0.3989422804D;
        double ddens = -u * dens;
        double root = dens * dens - (p - q1) * ddens * 2.0D;
        double r;
        if (root <= 0.0D) {
          r = -dens / ddens;
        }
        else {
          r = (p - q1) * 2.0D / (-dens - Math.sqrt(root));
        }
        u += r;
        if (Math.abs(r) <= 1.E-009D) break; if (Double.isNaN(r)) {
          break;
        }
      }
    }
    if (q > 0.5D) {
      u = -u;
    }
    return u;
  }

  public static double nq(double u)
  {
    double Y = Math.abs(u);
    double e = 0.3989422804D * Math.exp(-0.5D * Y * Y);
    double F = 28.0D;
    double q = 0.0D;
    if (Y >= 3.0D) {
      for (int i = 0; i < 28; i++) {
        q = F / (Y + q);
        F -= 1.0D;
      }
      q = e / (Y + q);
    }
    else {
      for (int i = 1; i < 29; i++) {
        q *= Math.pow(-1.0D, i);
        q = F * Y * Y / (2.0D * F + 1.0D + q);
        F -= 1.0D;
      }
      q = 0.5D - e * Y / (1.0D - q);
    }
    if (u < 0.0D) {
      q = 1.0D - q;
    }
    return q;
  }

  public static double zt(double u)
  {
    return 1.0D - nq(u);
  }

  public static double tq(int n, double t)
  {
    double x = n / (t * t + n);
    double[] pd = bta(n, 1, x);

    if (t < 0.0D) {
      pd[0] = (1.0D - pd[0]);
    }
    return pd[0];
  }

  public static double tpnt(int n, double q)
  {
    double t = bpnt(n, 1, q);
    t = Math.sqrt((1.0D - t) * n / t);
    if (q > 0.5D) {
      t = -t;
    }
    return t;
  }

  public static double[] x2q(int n, double x2)
  {
    double[] qd = new double[2];
    if (x2 > 0.0D) {
      double dd = 0.0D;
      int k = 0;
      if (n % 2 == 0) {
        qd[0] = Math.exp(-x2 / 2.0D);
        dd = qd[0];
        k = 2;
      }
      else {
        double f = Math.sqrt(x2);
        qd[0] = (2.0D * nq(f));
        dd = 0.7978845609D * Math.exp(-x2 / 2.0D) / f;
        k = 1;
      }
      int m = n - 2;
      if (m > 0) {
        for (int i = k; i <= m; i += 2) {
          dd = dd * x2 / i;
          qd[0] += dd;
          if (dd <= -50.0D) {
            break;
          }
        }
      }
      qd[1] = (dd / 2.0D);
    }
    else {
      qd[0] = 1.0D;
      qd[1] = 0.0D;
    }
    return qd;
  }

  public static double x2pnt(int n, double q)
  {
    double[] p = new double[9];
    double x = 0.0D;
    double x2 = 0.0D;

    if (n - 2 < 0) {
      x = npnt(0.5D * q);
      x2 = x * x;
    }
    else if (n - 2 == 0) {
      x2 = -Math.log(q) / Math.log(2.718281828459045D) * 2.0D;
    }
    else {
      x = npnt(q);
      double z = x * x;
      p[0] = ((z - 7.0D) * x / 36.0D);
      p[1] = (((-3.0D * z - 7.0D) * z + 16.0D) / 810.0D);
      p[2] = (((9.0D * z + 256.0D) * z - 433.0D) * x / 38880.0D);
      p[3] = ((((12.0D * z - 243.0D) * z - 923.0D) * z + 1472.0D) / 204120.0D);
      p[4] = 
        ((((-3753.0D * z - 4353.0D) * z + 289517.0D) * z + 289717.0D) * x / 
        146966400.0D);
      p[5] = 
        (((((270.0D * z + 4614.0D) * z - 9513.0D) * z - 104989.0D) * z + 
        35968.0D) / 55112400.0D);
      p[6] = 
        (((((-5139.0D * z - 547848.0D) * z - 2742210.0D) * z + 7016224.0D) * 
        z + 37501325.0D) * x / 358400.0D / 59049.0D);
      p[7] = 
        ((((((-364176.0D * z + 6208146.0D) * z + 125735778.0D) * z + 
        303753831.0D) * z - 672186949.0D) * z - 2432820224.0D) / 
        17537553.0D / 112000.0D);
      p[8] = 
        ((((((199112985.0D * z + 1885396761.0D) * z - 31857434154.0D) * z - 
        287542736226.0D) * z - 556030221167.0D) * z + 487855454729.0D) * 
        x / 2087956777984000.0D);
      x2 = n + 1.1421356237D * Math.sqrt(n) * x + (z - 1.0D) / 3.0D * 2.0D;
      for (int i = 0; i < 9; i++)
        x2 += Math.sqrt(Math.pow(Math.pow(2.0D, i + 2) * n, -i)) * p[i];
      while (true)
      {
        double[] pd = x2q(n, x2);
        double dxdens = pd[1] * ((n * 0.5D - 1.0D) / x2 - 0.5D);
        double root = pd[1] * pd[1] - (q - pd[0]) * dxdens * 2.0D;
        double r;
        if (root <= 0.0D) {
          r = -pd[1] / dxdens;
        }
        else {
          r = (q - pd[0]) * 2.0D / (-pd[1] - Math.sqrt(root));
        }
        x2 += r;
        if (Math.abs(r) - 1.E-009D <= 0.0D) break; if (Double.isNaN(r)) {
          break;
        }
      }
    }
    return x2;
  }
}