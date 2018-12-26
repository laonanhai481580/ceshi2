package com.ambition.carmfg.baseinfo.web;

import java.util.Date;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.InspectionPointManager;
import com.ambition.carmfg.baseinfo.service.InspectorManager;
import com.ambition.carmfg.entity.InspectionPoint;
import com.ambition.carmfg.entity.Inspector;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/carmfg/base-info/inspection-point")
@ParentPackage("default")
public class InspectorAction extends CrudActionSupport<Inspector>{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long inspectorPointId;//检查点ID
	private String deleteIds;
	private Inspector inspector;
	
	private String params;
	@Autowired
	private InspectorManager inspectorManager;
	@Autowired
	private InspectionPointManager inspectionPointManager;
	private Page<Inspector> page;
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	
	public void setInspectorPointId(Long inspectorPointId) {
		this.inspectorPointId = inspectorPointId;
	}
	public Long getInspectorPointId() {
		return inspectorPointId;
	}
	public void setPage(Page<Inspector> page) {
		this.page = page;
	}
	public Page<Inspector> getPage() {
		return page;
	}
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	@Override
	public Inspector getModel() {
		return inspector;
	}
	
	@Override
	protected void prepareModel() throws Exception {

	}
	
	@Action("delete-inspector")
	@LogInfo(optType="删除",message="检验员维护")
	@Override
	public String delete() throws Exception {
		inspectorManager.deleteInspector(deleteIds);
		return null;
	}
	
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Action("list-inspector")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	
	@Action("save-inspector")
	@LogInfo(optType="保存",message="检验员维护")
	@Override
	public String save() throws Exception{
		InspectionPoint inspectionPoint = inspectionPointManager.getInspectionPoint(inspectorPointId);
		inspectionPoint.getInspectors().clear();
		
		JSONArray js = JSONArray.fromObject(params);
		for(int i=0;i<js.size();i++){
			Inspector inspector = new Inspector();
			inspector.setCompanyId(ContextUtils.getCompanyId());
			inspector.setCreatedTime(new Date());
			inspector.setCreator(ContextUtils.getUserName());
			inspector.setName(js.getJSONObject(i).get("name").toString());
			inspector.setUserId(Long.valueOf(js.getJSONObject(i).get("userId").toString()));
			inspector.setInspectionPoint(inspectionPoint);
			inspectionPoint.getInspectors().add(inspector);
			
		}
		inspectionPointManager.saveInspectionPoint(inspectionPoint);
		return null;
	}
	
	@Action("list-inspector-datas")
	public String getListDatas() throws Exception {
		page = inspectorManager.list(page,inspectorPointId);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	
}
