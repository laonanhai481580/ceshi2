package com.ambition.spc.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;

/**    
 * AbnormalReasonType.java
 * @authorBy YUKE
 * 异常原因类别
 */
@Entity
@Table(name = "SPC_ABNORMAL_REASON_TYPE")
public class AbnormalReasonType extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String typeNo;//类别代码
	private String typeName;//类别名称
	private Boolean hasChild = false;//是否有子节点,程序维护
	@Column(name="myLevel")
	private Integer level = 1;//标识
    private Integer orderNum = 0;//排序
    private String parentIds;//父级的所有ID,以逗号开始,逗号结束
    
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private AbnormalReasonType parent;//上级类别
    
    @OneToMany(mappedBy="parent")
    @Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
    @OrderBy("typeNo")
    private List<AbnormalReasonType> children;//子级类别
    
	@OneToMany(mappedBy="abnormalReasonType",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<AbnormalReason> abnormalReasons;//异常原因
	
	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public AbnormalReasonType getParent() {
		return parent;
	}

	public void setParent(AbnormalReasonType parent) {
		this.parent = parent;
	}

	public List<AbnormalReasonType> getChildren() {
		return children;
	}

	public void setChildren(List<AbnormalReasonType> children) {
		this.children = children;
	}

	public List<AbnormalReason> getAbnormalReasons() {
		return abnormalReasons;
	}

	public void setAbnormalReasons(List<AbnormalReason> abnormalReasons) {
		this.abnormalReasons = abnormalReasons;
	}
}
