package com.ambition.carmfg.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.wf.engine.client.ExtendField;

@Entity
@Table(name = "MFG_INSPECTION_POINT")
public class InspectionPoint extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String inspectionPointName;//检查点名称
	private String factory; // 工厂
	private String workshop; // 车间
	private String productionLine; // 生产线
	private String section; // 工段,工段可表示几个工序的组合,如一个工段名叫机加工,工序可能包括精加工,粗加工
	private String workGroup; // 班组
	private String workGroupType; // 班别
	private String workProcedure; // 工序
	private String workingStatus;//开工状态
	private String processBusNode;//工艺节点
	
	@Enumerated(EnumType.ORDINAL)
	private InspectionType inspectionType;
	
	private Long listId;// 关联的自定义列表
	private String listCode;//关联的列表编号
	
	@Enumerated(EnumType.ORDINAL)
	private InspectionPointTypeEnum listType;//检查点类型
	
	@Embedded
	private ExtendField extendField;
	
	@OneToMany(mappedBy = "inspectionPoint",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore()
	private List<Inspector> inspectors;
	
	
	public String getInspectionPointName() {
		return inspectionPointName;
	}

	public void setInspectionPointName(String inspectionPointName) {
		this.inspectionPointName = inspectionPointName;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getWorkshop() {
		return workshop;
	}

	public void setWorkshop(String workshop) {
		this.workshop = workshop;
	}

	public String getProductionLine() {
		return productionLine;
	}

	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getWorkGroup() {
		return workGroup;
	}

	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}

	public String getWorkGroupType() {
		return workGroupType;
	}

	public void setWorkGroupType(String workGroupType) {
		this.workGroupType = workGroupType;
	}

	public String getWorkProcedure() {
		return workProcedure;
	}

	public void setWorkProcedure(String workProcedure) {
		this.workProcedure = workProcedure;
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
	}
	public String getListCode() {
		return listCode;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
	}

	
	public InspectionPointTypeEnum getListType() {
		return listType;
	}

	public void setListType(InspectionPointTypeEnum listType) {
		this.listType = listType;
	}

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public List<Inspector> getInspectors() {
		return inspectors;
	}

	public void setInspectors(List<Inspector> inspectors) {
		this.inspectors = inspectors;
	}

	public String getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(String workingStatus) {
		this.workingStatus = workingStatus;
	}

	public String getProcessBusNode() {
		return processBusNode;
	}

	public void setProcessBusNode(String processBusNode) {
		this.processBusNode = processBusNode;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public String toString() {
		return "制造质量管理：采集点维护    ID"+this.getId()+",检查点名称"+this.inspectionPointName+",生产线"+this.productionLine;
	}
}
