package com.ambition.carmfg.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 产品物料实体
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "MFG_PRODUCT_BOM")
public class ProductBom  extends IdEntity {
	public static String IMPORTANCE_A = "A类";
	public static String IMPORTANCE_B = "B类";
	public static String IMPORTANCE_C = "C类";
	public static String IMPORTANCE_D = "D类";
	private static final long serialVersionUID = 1L;
	private Integer companyISN;//公司内码
	private String bomCode;//物料BOM编码
	private String materielName; //物料名称
    private String materielCode; //物料编码
    private String materielModel;//规格型号
    private String units;//单位
    private Boolean hasChild = false;//是否有子节点,程序维护
    private String isComing="否";//是否报检
    private Integer assemblyNum;//标准配件数
//    private String materialName;//材料名称
    private String baseNo;//基本型编号
    private Float weight;//单重
    private String remark;//备注
    private String materialType;//物料类别
    private String materialTypeCode;//物料类别代码
    private String ascendType;//追溯类型
    private String importance = IMPORTANCE_A;//重要程度,分为普通和重要,如果是重要时,在供应商准入时需要进行供应商考察
    private Integer materielLevel = 1;//标识
    private Integer orderNum=0;//排序
    private String parentIds;//父级的所有ID,以逗号开始,逗号结束
    private Double price=0.00;//wuxuming  采购单价
    private Date lastUpdateDate;//最后更新时间
    private Long businessUnitId;//组织结构id  与物料号加起来判断是否唯一
    
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    @JsonBackReference//避免无限递归
    private ProductBom bomParent;//上级物料
    
    @OneToMany(mappedBy="bomParent")
    @Cascade(value=CascadeType.DELETE)
    @OrderBy("materielCode")
    @JsonBackReference//避免无限递归
    private List<ProductBom> bomChildren;//子级物料
    
    @Embedded
	private ExtendField extendField;
    

	public Integer getCompanyISN() {
		return companyISN;
	}


	public void setCompanyISN(Integer companyISN) {
		this.companyISN = companyISN;
	}


	public String getBomCode() {
		return bomCode;
	}


	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}


	public String getMaterielName() {
		return materielName;
	}


	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}


	public String getMaterielCode() {
		return materielCode;
	}


	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}


	public String getMaterielModel() {
		return materielModel;
	}


	public void setMaterielModel(String materielModel) {
		this.materielModel = materielModel;
	}


	public String getUnits() {
		return units;
	}


	public void setUnits(String units) {
		this.units = units;
	}


	public Boolean getHasChild() {
		return hasChild;
	}


	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}


	public Integer getAssemblyNum() {
		return assemblyNum;
	}


	public void setAssemblyNum(Integer assemblyNum) {
		this.assemblyNum = assemblyNum;
	}


//	public String getMaterialName() {
//		return materialName;
//	}
//
//
//	public void setMaterialName(String materialName) {
//		this.materialName = materialName;
//	}


	public String getBaseNo() {
		return baseNo;
	}


	public void setBaseNo(String baseNo) {
		this.baseNo = baseNo;
	}


	public Float getWeight() {
		return weight;
	}


	public void setWeight(Float weight) {
		this.weight = weight;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getMaterialType() {
		return materialType;
	}


	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}


	public String getAscendType() {
		return ascendType;
	}


	public void setAscendType(String ascendType) {
		this.ascendType = ascendType;
	}


	public String getImportance() {
		return importance;
	}


	public void setImportance(String importance) {
		this.importance = importance;
	}


	public Integer getMaterielLevel() {
		return materielLevel;
	}


	public void setMaterielLevel(Integer materielLevel) {
		this.materielLevel = materielLevel;
	}


	public Integer getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}


	public String getParentIds() {
		return parentIds;
	}


	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public ProductBom getBomParent() {
		return bomParent;
	}


	public void setBomParent(ProductBom bomParent) {
		this.bomParent = bomParent;
	}


	public List<ProductBom> getBomChildren() {
		return bomChildren;
	}


	public void setBomChildren(List<ProductBom> bomChildren) {
		this.bomChildren = bomChildren;
	}


	public ExtendField getExtendField() {
		return extendField;
	}


	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public Long getBusinessUnitId() {
		return businessUnitId;
	}


	public void setBusinessUnitId(Long businessUnitId) {
		this.businessUnitId = businessUnitId;
	}


	public String getMaterialTypeCode() {
		return materialTypeCode;
	}


	public void setMaterialTypeCode(String materialTypeCode) {
		this.materialTypeCode = materialTypeCode;
	}



	public String getIsComing() {
		return isComing;
	}


	public void setIsComing(String isComing) {
		this.isComing = isComing;
	}


	public String toString() {
		return "制造质量管理：产品BOM	ID"+this.getId()+",物料名称"+this.materielName+",物料编码"+this.materielCode;
	}
	
}
