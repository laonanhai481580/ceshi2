package com.ambition.spc.entity;

/**    
 * CPKMoudle.java(CPK实体)
 * @authorBy wanglf
 *
 */

public class CPKMoudle
{
  private double cp;
  private double cr;
  private double cpl;
  private double cpu;
  private double cpk;
  private double k;
  private double pp;
  private double ppu;
  private double ppl;
  private double pr;
  private double ppk;
  private double sigma;
  private double p = 1.0D;
  private int cpGrade;
  private String cpGradeCN;
  private double cpm;
  private double ppm;
  private double zu_cap;
  private double zl_cap;
  private double zu_pref;
  private double zl_pref;
  private double fpu_cap = 1.0D;
  private double fpl_cap = 1.0D;
  private double fp_cap = 1.0D;
  private double fpu_pref = 1.0D;
  private double fpl_pref = 1.0D;
  private double fp_pref = 1.0D;

  public double getCp()
  {
    return isNanAndInf(this.cp) ? 0.0D : this.cp;
  }

  public void setCp(double cp) {
    this.cp = cp;
  }

  public double getCpk() {
    return isNanAndInf(this.cpk) ? 0.0D : this.cpk;
  }

  public void setCpk(double cpk) {
    this.cpk = cpk;
  }

  public double getPp() {
    return isNanAndInf(this.pp) ? 0.0D : this.pp;
  }

  public void setPp(double pp) {
    this.pp = pp;
  }

  public double getPpk() {
    return isNanAndInf(this.ppk) ? 0.0D : this.ppk;
  }

  public void setPpk(double ppk) {
    this.ppk = ppk;
  }

  public double getP() {
    return isNanAndInf(this.p) ? 1.0D : this.p;
  }

  public void setP(double p) {
    this.p = p;
  }

  public int getCpGrade() {
    return this.cpGrade;
  }

  public double getCpu() {
    return isNanAndInf(this.cpu) ? 0.0D : this.cpu;
  }

  public double getCpl() {
    return isNanAndInf(this.cpl) ? 0.0D : this.cpl;
  }

  public double getK() {
    return isNanAndInf(this.k) ? 0.0D : this.k;
  }

  public double getCr() {
    return isNanAndInf(this.cr) ? 0.0D : this.cr;
  }

  public double getSigma() {
    return this.sigma;
  }

  public double getPpu() {
    return isNanAndInf(this.ppu) ? 0.0D : this.ppu;
  }

  public double getPr() {
    return isNanAndInf(this.pr) ? 0.0D : this.pr;
  }

  public double getPpl() {
    return isNanAndInf(this.ppl) ? 0.0D : this.ppl;
  }

  public double getFp_pref() {
    return isNanAndInf(this.fp_pref) ? 1.0D : this.fp_pref;
  }

  public double getFpu_cap() {
    return isNanAndInf(this.fpu_cap) ? 1.0D : this.fpu_cap;
  }

  public double getFpu_pref() {
    return isNanAndInf(this.fpu_pref) ? 1.0D : this.fpu_pref;
  }

  public double getZl_pref() {
    return isNanAndInf(this.zl_pref) ? 0.0D : this.zl_pref;
  }

  public double getFpl_pref() {
    return isNanAndInf(this.fpl_pref) ? 1.0D : this.fpl_pref;
  }

  public double getZu_cap() {
    return isNanAndInf(this.zu_cap) ? 0.0D : this.zu_cap;
  }

  public double getFp_cap() {
    return isNanAndInf(this.fp_cap) ? 1.0D : this.fp_cap;
  }

  public double getZu_pref() {
    return isNanAndInf(this.zu_pref) ? 0.0D : this.zu_pref;
  }

  public double getFpl_cap() {
    return isNanAndInf(this.fpl_cap) ? 1.0D : this.fpl_cap;
  }

  public double getCpm() {
    return isNanAndInf(this.cpm) ? 0.0D : this.cpm;
  }

  public double getZl_cap() {
    return isNanAndInf(this.zl_cap) ? 0.0D : this.zl_cap;
  }

  public double getPpm() {
    return isNanAndInf(this.ppm) ? 0.0D : this.ppm;
  }

  public String getCpGradeCN() {
    return this.cpGradeCN;
  }

  public void setCpGrade(int cpGrade) {
    this.cpGrade = cpGrade;
  }

  public void setCpu(double cpu) {
    this.cpu = cpu;
  }

  public void setCpl(double cpl) {
    this.cpl = cpl;
  }

  public void setK(double k) {
    this.k = k;
  }

  public void setCr(double cr) {
    this.cr = cr;
  }

  public void setSigma(double sigma) {
    this.sigma = sigma;
  }

  public void setPpu(double ppu) {
    this.ppu = ppu;
  }

  public void setPr(double pr) {
    this.pr = pr;
  }

  public void setPpl(double ppl) {
    this.ppl = ppl;
  }

  public void setFp_pref(double fp_pref) {
    this.fp_pref = fp_pref;
  }

  public void setFpu_cap(double fpu_cap) {
    this.fpu_cap = fpu_cap;
  }

  public void setFpu_pref(double fpu_pref) {
    this.fpu_pref = fpu_pref;
  }

  public void setZl_pref(double zl_pref) {
    this.zl_pref = zl_pref;
  }

  public void setFpl_pref(double fpl_pref) {
    this.fpl_pref = fpl_pref;
  }

  public void setZu_cap(double zu_cap) {
    this.zu_cap = zu_cap;
  }

  public void setFp_cap(double fp_cap) {
    this.fp_cap = fp_cap;
  }

  public void setZu_pref(double zu_pref) {
    this.zu_pref = zu_pref;
  }

  public void setFpl_cap(double fpl_cap) {
    this.fpl_cap = fpl_cap;
  }

  public void setCpm(double cpm) {
    this.cpm = cpm;
  }

  public void setZl_cap(double zl_cap) {
    this.zl_cap = zl_cap;
  }

  public void setPpm(double ppm) {
    this.ppm = ppm;
  }

  public void setCpGradeCN(String cpGradeCN) {
    this.cpGradeCN = cpGradeCN;
  }

  public boolean isNanAndInf(double data) {
    if (Double.isNaN(data))
      return true;
    if (Double.isInfinite(data)) {
      return true;
    }
    return data < 0.0D;
  }
}