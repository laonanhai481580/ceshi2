package com.ambition.spc.annormalreason.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.annormalreason.service.AbnormalReasonManager;
import com.ambition.spc.annormalreason.service.AbnormalReasonTypeManger;
import com.ambition.spc.entity.AbnormalReason;
import com.ambition.spc.entity.AbnormalReasonType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * AbnormalReasonTypeAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/base-info/abnormal-reason")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/abnormal-reason", type = "redirectAction") })
public class AbnormalReasonTypeAction extends com.ambition.product.base.CrudActionSupport<AbnormalReasonType>{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long structureId;//结构编号
	private Long parentId;
	private String expandIds;//刷新时展开的节点
	private String deleteIds;
	private AbnormalReasonType abnormalReasonType;
	private Page<AbnormalReasonType> page;
	private Page<AbnormalReason> reasonPage;
	@Autowired
	private AbnormalReasonManager abnormalReasonManager;
	@Autowired
	private AbnormalReasonTypeManger abnormalReasonTypeManger;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public Long getStructureId() {
		return structureId;
	}

	public void setStructureId(Long structureId) {
		this.structureId = structureId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getExpandIds() {
		return expandIds;
	}

	public void setExpandIds(String expandIds) {
		this.expandIds = expandIds;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public AbnormalReasonType getAbnormalReasonType() {
		return abnormalReasonType;
	}

	public void setAbnormalReasonType(AbnormalReasonType abnormalReasonType) {
		this.abnormalReasonType = abnormalReasonType;
	}

	public Page<AbnormalReasonType> getPage() {
		return page;
	}

	public void setPage(Page<AbnormalReasonType> page) {
		this.page = page;
	}

	public Page<AbnormalReason> getReasonPage() {
		return reasonPage;
	}

	public void setReasonPage(Page<AbnormalReason> reasonPage) {
		this.reasonPage = reasonPage;
	}

	@Override
	public AbnormalReasonType getModel() {
		return abnormalReasonType;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			abnormalReasonType = new AbnormalReasonType();
			abnormalReasonType.setCreatedTime(new Date());
			abnormalReasonType.setCompanyId(ContextUtils.getCompanyId());
			abnormalReasonType.setCreator(ContextUtils.getUserName());
			abnormalReasonType.setModifiedTime(new Date());
			abnormalReasonType.setModifier(ContextUtils.getUserName());
			if(parentId != null){
				AbnormalReasonType parentType = abnormalReasonTypeManger.getAbnormalReasonType(parentId);
				if(parentType != null){
					abnormalReasonType.setParent(parentType);
					abnormalReasonType.setLevel(parentType.getLevel()+1);
				}
			}
		}else {
			abnormalReasonType = abnormalReasonTypeManger.getAbnormalReasonType(id);
		}
	}
	
	@Action("type-delete")
	@Override
	public String delete() throws Exception {
		if(id == null){
			createErrorMessage("删除的对象不存在!");
		}else{
			abnormalReasonType = abnormalReasonTypeManger.getAbnormalReasonType(id);
			try {
				if(!abnormalReasonType.getChildren().isEmpty()){
					createErrorMessage("还有子节点,不能删除!");
				}else{
					abnormalReasonTypeManger.deleteAbnormalReasonType(String.valueOf(id));
				}
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("type-input")
	@Override
	public String input() throws Exception {
		if(id==null && parentId!=null){
			AbnormalReasonType parentType = abnormalReasonTypeManger.getAbnormalReasonType(parentId);
			if(parentType == null){
				addActionMessage("父级类别为空!");
			}else{
				abnormalReasonType.setParent(parentType);
			}
		}else if(parentId == null){
			try {
				AbnormalReasonType parent = new AbnormalReasonType();
				parent.setTypeName("原因类别");
				abnormalReasonType.setParent(parent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	@Action("type-save")
	@Override
	public String save() throws Exception {
		if(id == null){
			AbnormalReasonType parentType = null;
			if(parentId != null){
				parentType = abnormalReasonTypeManger.getAbnormalReasonType(parentId);
				if(parentType != null){
					abnormalReasonType.setParent(parentType);
					abnormalReasonType.setLevel(parentType.getLevel() + 1);
				}
			}
			try{
				abnormalReasonTypeManger.saveAbnormalReasonType(abnormalReasonType);
				addActionMessage("保存成功!");
				return "type-input";
			}catch(Exception e){
				e.printStackTrace();
				addActionMessage("保存失败:" + e.getMessage());
				return "type-input";
			}
		}else{
			if(abnormalReasonType != null){
				abnormalReasonType.setModifiedTime(new Date());
				abnormalReasonType.setModifier(ContextUtils.getUserName());
				try{
					abnormalReasonTypeManger.saveAbnormalReasonType(abnormalReasonType);
					addActionMessage("保存成功!");
					return "type-input";
				}catch(Exception e){
					e.printStackTrace();
					addActionMessage("保存失败：" + e.getMessage());
					return "type-input";
				}
			}else{
				addActionMessage("保存失败,原因类别为空!");
				return "type-input";
			}
		}
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("reason-type-datas")
	public String reasonTypeDatas() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<AbnormalReasonType> reasonTypes = abnormalReasonTypeManger.getAbnormalReasonTypes(parentId);
		for(AbnormalReasonType reasonType : reasonTypes){
			resultList.add(abnormalReasonTypeManger.convertReasonType(reasonType));
		}
		renderText(JSONArray.fromObject(resultList).toString());
		return null;
	}
	
	@Action("reason-list-datas")
	public String reasonListData() throws Exception {
		AbnormalReasonType reasonType = null;
		try {
			if(parentId != null){
				reasonType = abnormalReasonTypeManger.getAbnormalReasonType(parentId);
			}
			reasonPage = abnormalReasonManager.getPage(reasonPage, reasonType);
			this.renderText(PageUtils.pageToJson(reasonPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Action("exports")
	public String exprots() throws Exception {
		String typeId = Struts2Utils.getParameter("typeId");
		AbnormalReasonType reasonType = null;
		if(typeId != null && !typeId.equals("")){
			reasonType = abnormalReasonTypeManger.getAbnormalReasonType(Long.valueOf(typeId));
		}
		reasonPage = abnormalReasonManager.getPage(new Page<AbnormalReason>(65535),reasonType);
		String result = ExcelExporter.export(ApiFactory.getMmsService().getExportData(reasonPage,Struts2Utils.getParameter("_list_code")),"异常原因台账");
		renderText(result);
		return null;
	}

}
