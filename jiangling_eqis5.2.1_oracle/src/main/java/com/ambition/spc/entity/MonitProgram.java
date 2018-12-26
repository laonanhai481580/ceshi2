package com.ambition.spc.entity;

import java.sql.Blob;
import java.util.Date;
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
 * MonitProgram.java
 * @authorBy wanglf
 *
 */
@Entity
@Table(name = "SPC_MONIT_PROGRAM")
public class MonitProgram extends IdEntity {

	/**
	 * 监控方案定义
	 */
	private static final long serialVersionUID = 1L;
	private String code;//方案编号
	private String name;//方案名称
	private String editer;//编辑人
	private Date editDate;//编辑日期
	private String categoryDescription;//类别描述
	private Boolean hasChild = false;//是否有子节点,程序维护
	@Column(name="myLevel")
	private Integer level = 1;//标识
    private Integer orderNum = 0;//排序
    private String parentIds;//父级的所有ID,以逗号开始,逗号结束
    
    private Integer imageWidth;//图片宽度
	private Integer imageHeight;//图片高度
	private String imageUrl;//图表URL
	private Blob attach;//附件
	private String attachName;//附件名字
	private String attachUrl;//附件URL
    
    
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private MonitProgram parent;//上级物料
    
    @OneToMany(mappedBy="parent")
    @Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
    @OrderBy("code")
    private List<MonitProgram> children;//子级物料
    
    @OneToMany(mappedBy="monitProgram",cascade=CascadeType.ALL)
    @Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<MonitPoint> MonitPoints;//子级物料
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEditer() {
		return editer;
	}

	public void setEditer(String editer) {
		this.editer = editer;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
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

	public MonitProgram getParent() {
		return parent;
	}

	public void setParent(MonitProgram parent) {
		this.parent = parent;
	}

	public List<MonitProgram> getChildren() {
		return children;
	}

	public void setChildren(List<MonitProgram> children) {
		this.children = children;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public List<MonitPoint> getMonitPoints() {
		return MonitPoints;
	}

	public void setMonitPoints(List<MonitPoint> monitPoints) {
		MonitPoints = monitPoints;
	}

	public Integer getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Blob getAttach() {
		return attach;
	}

	public void setAttach(Blob attach) {
		this.attach = attach;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}

	public String toString() {
		return "SPC：监控方案    ID"+this.getId()+",监控方案编号"+this.code;
	}
	
}
