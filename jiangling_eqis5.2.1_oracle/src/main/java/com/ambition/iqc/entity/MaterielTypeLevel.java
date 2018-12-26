package com.ambition.iqc.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;

/**
 * 类名:MaterielParent.java
 * 中文类名:进货物料大类关系表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：物料大类关系表，用于统计分析展示效果</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-2-28 下午5:40:59
 * </p>
 */
@Entity
@Table(name= "IQC_MATERIEL_TYPE_LEVEL")
public class MaterielTypeLevel extends IdEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String materielTypeName;//物料大类名称
	private Integer materielLevel;//物料大类级别
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
	private MaterielTypeLevel materielTyepParent;//父级
	
    @OneToMany(mappedBy="materielTyepParent",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<MaterielTypeLevel> childMaterielType;//子级


	public String getMaterielTypeName() {
		return materielTypeName;
	}

	public void setMaterielTypeName(String materielTypeName) {
		this.materielTypeName = materielTypeName;
	}


	public Integer getMaterielLevel() {
		return materielLevel;
	}

	public void setMaterielLevel(Integer materielLevel) {
		this.materielLevel = materielLevel;
	}

	public MaterielTypeLevel getMaterielTyepParent() {
		return materielTyepParent;
	}

	public void setMaterielTyepParent(MaterielTypeLevel materielTyepParent) {
		this.materielTyepParent = materielTyepParent;
	}

	public List<MaterielTypeLevel> getChildMaterielType() {
		return childMaterielType;
	}

	public void setChildMaterielType(List<MaterielTypeLevel> childMaterielType) {
		this.childMaterielType = childMaterielType;
	}
    
    
}
