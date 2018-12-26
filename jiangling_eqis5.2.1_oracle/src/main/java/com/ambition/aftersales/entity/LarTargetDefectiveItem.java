
package com.ambition.aftersales.entity;
import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="AFS_LAR_TARGET_DEFECTIVE_ITEMS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "fieldHandler"})
public class LarTargetDefectiveItem extends IdEntity
{
  private static final long serialVersionUID = 1L;
  private String defectionClass;
  private String defectionItemNo;
  private String defectionItemName;
  private Integer defectionItemValue;

  @ManyToOne
  @JoinColumn(name="AFS_LAR_TARGET_ID")
  @JsonIgnore
  private LarTarget larTarget;

  public String getDefectionClass()
  {
    return this.defectionClass;
  }
  public void setDefectionClass(String defectionClass) {
    this.defectionClass = defectionClass;
  }
  public String getDefectionItemNo() {
    return this.defectionItemNo;
  }
  public void setDefectionItemNo(String defectionItemNo) {
    this.defectionItemNo = defectionItemNo;
  }
  public String getDefectionItemName() {
    return this.defectionItemName;
  }
  public void setDefectionItemName(String defectionItemName) {
    this.defectionItemName = defectionItemName;
  }
  public Integer getDefectionItemValue() {
    return this.defectionItemValue;
  }
  public void setDefectionItemValue(Integer defectionItemValue) {
    this.defectionItemValue = defectionItemValue;
  }
  public LarTarget getLarTarget() {
	return larTarget;
 }
  public void setLarTarget(LarTarget larTarget) {
	this.larTarget = larTarget;
}



}