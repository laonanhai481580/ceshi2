package com.ambition.si.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 供应商检验检验标准维护
 * @author lpf
 *
 */
@Entity
@Table(name="SI_INSPECTING_INDICATOR")
public class SiInspectingIndicator extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String workingProcedure;//工序
	private String model;//机种		
	private String modelName;//机种名称
    @Column(length=50)
    private String standardVersion;//版本号
	private String materielCode;//产品代码
	private String materielName;//产品名称
	private String materielType;//物料大类
	private String materielLevelOne;//物料大类一级
	private String materielLevelSec;//物料大类二级
	private String materielLevelTree;//物料大类三级
	private String is3C;//是否3C产品
	private String isStandard;//是否标准件
	private String iskeyComponent;//是否关键件

    private Boolean isMax=true;//是否最新版本
    private String remark;//备注
    private Long indicatorAttachId;//检验文件的ID
    private String imgFileName;//图片文件名称
    private String attachmentFiles;//附件

    
    @OneToMany(mappedBy="siInspectingIndicator",cascade=javax.persistence.CascadeType.REMOVE)
    private List<SiItemIndicator> siItemIndicators;//已经设置的项目指标
    
	public String toString(){
		return "供应商检验检标准维护：检验标准维护    检验物料名称"+this.materielName;
	}

	public String getWorkingProcedure() {
		return workingProcedure;
	}

	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getStandardVersion() {
		return standardVersion;
	}

	public void setStandardVersion(String standardVersion) {
		this.standardVersion = standardVersion;
	}

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getMaterielName() {
		return materielName;
	}

	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}

	public String getMaterielType() {
		return materielType;
	}

	public void setMaterielType(String materielType) {
		this.materielType = materielType;
	}

	public String getMaterielLevelOne() {
		return materielLevelOne;
	}

	public void setMaterielLevelOne(String materielLevelOne) {
		this.materielLevelOne = materielLevelOne;
	}

	public String getMaterielLevelSec() {
		return materielLevelSec;
	}

	public void setMaterielLevelSec(String materielLevelSec) {
		this.materielLevelSec = materielLevelSec;
	}

	public String getMaterielLevelTree() {
		return materielLevelTree;
	}

	public void setMaterielLevelTree(String materielLevelTree) {
		this.materielLevelTree = materielLevelTree;
	}

	public String getIs3C() {
		return is3C;
	}

	public void setIs3C(String is3c) {
		is3C = is3c;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	public String getIskeyComponent() {
		return iskeyComponent;
	}

	public void setIskeyComponent(String iskeyComponent) {
		this.iskeyComponent = iskeyComponent;
	}

	public Boolean getIsMax() {
		return isMax;
	}

	public void setIsMax(Boolean isMax) {
		this.isMax = isMax;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getIndicatorAttachId() {
		return indicatorAttachId;
	}

	public void setIndicatorAttachId(Long indicatorAttachId) {
		this.indicatorAttachId = indicatorAttachId;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public String getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public List<SiItemIndicator> getSiItemIndicators() {
		return siItemIndicators;
	}

	public void setSiItemIndicators(List<SiItemIndicator> siItemIndicators) {
		this.siItemIndicators = siItemIndicators;
	}
	
}
