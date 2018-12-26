package com.ambition.iqc.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**    
 * 样本量字码 表
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "IQC_SAMPLE_CODE_LETTER")
public class SampleCodeLetter extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Integer batchSize1;//批量下限
	private Integer batchSize2;//批量上限
	private String special1;//特殊检验水平1
	private String special2;//特殊检验水平2
	private String special3;//特殊检验水平3
	private String special4;//特殊检验水平4
	private String ordinary1;//一般检验水平1
	private String ordinary2;//一般检验水平2
	private String ordinary3;//一般检验水平3
	private String validateLevel1;//验证水平1
	private String validateLevel2;//验证水平2
	private String validateLevel3;//验证水平3
	private String validateLevel4;//验证水平4
	private String validateLevel5;//验证水平5
	private String validateLevel6;//验证水平6
	private String validateLevel7;//验证水平7
	private String baseType = GB_TYPE;//方案类型
	public static final String GB_TYPE = "GB/T2828";
	public static final String MIL_TYPE = "MIL-STD-1916";
	public static final String MIL1051_TYPE = "MIL-STD-1051";
	@Embedded
	private ExtendField extendField;
	
	public Integer getBatchSize1() {
		return batchSize1;
	}
	public void setBatchSize1(Integer batchSize1) {
		this.batchSize1 = batchSize1;
	}
	public Integer getBatchSize2() {
		return batchSize2;
	}
	public void setBatchSize2(Integer batchSize2) {
		this.batchSize2 = batchSize2;
	}
	public String getSpecial1() {
		return special1;
	}
	public void setSpecial1(String special1) {
		this.special1 = special1;
	}
	public String getSpecial2() {
		return special2;
	}
	public void setSpecial2(String special2) {
		this.special2 = special2;
	}
	public String getSpecial3() {
		return special3;
	}
	public void setSpecial3(String special3) {
		this.special3 = special3;
	}
	public String getSpecial4() {
		return special4;
	}
	public void setSpecial4(String special4) {
		this.special4 = special4;
	}
	public String getOrdinary1() {
		return ordinary1;
	}
	public void setOrdinary1(String ordinary1) {
		this.ordinary1 = ordinary1;
	}
	public String getOrdinary2() {
		return ordinary2;
	}
	public void setOrdinary2(String ordinary2) {
		this.ordinary2 = ordinary2;
	}
	public String getOrdinary3() {
		return ordinary3;
	}
	public void setOrdinary3(String ordinary3) {
		this.ordinary3 = ordinary3;
	}
	public String getValidateLevel1() {
		return validateLevel1;
	}
	public void setValidateLevel1(String validateLevel1) {
		this.validateLevel1 = validateLevel1;
	}
	public String getValidateLevel2() {
		return validateLevel2;
	}
	public void setValidateLevel2(String validateLevel2) {
		this.validateLevel2 = validateLevel2;
	}
	public String getValidateLevel3() {
		return validateLevel3;
	}
	public void setValidateLevel3(String validateLevel3) {
		this.validateLevel3 = validateLevel3;
	}
	public String getValidateLevel4() {
		return validateLevel4;
	}
	public void setValidateLevel4(String validateLevel4) {
		this.validateLevel4 = validateLevel4;
	}
	public String getValidateLevel5() {
		return validateLevel5;
	}
	public void setValidateLevel5(String validateLevel5) {
		this.validateLevel5 = validateLevel5;
	}
	public String getValidateLevel6() {
		return validateLevel6;
	}
	public void setValidateLevel6(String validateLevel6) {
		this.validateLevel6 = validateLevel6;
	}
	public String getValidateLevel7() {
		return validateLevel7;
	}
	public void setValidateLevel7(String validateLevel7) {
		this.validateLevel7 = validateLevel7;
	}
	public String getBaseType() {
		return baseType;
	}
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	public String toString(){
		return "物料抽样标准维护：样本量字码     上限"+this.batchSize2+" 下限"+this.batchSize1;
	}
}
