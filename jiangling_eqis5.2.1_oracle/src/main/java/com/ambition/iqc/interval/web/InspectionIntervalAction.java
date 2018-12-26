package com.ambition.iqc.interval.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.InspectionInterval;
import com.ambition.iqc.interval.service.InspectionIntervalManager;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-10-31 发布
 */
@Namespace("/iqc/inspection-interval")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/inspection-interval", type = "redirectAction") })
public class InspectionIntervalAction extends CrudActionSupport<InspectionInterval>{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String deleteIds;
	private InspectionInterval inspectionInterval;
	@Autowired
	private InspectionIntervalManager inspectionIntervalManager;
	
	private Page<InspectionInterval> page;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}
	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	public Page<InspectionInterval> getPage() {
		return page;
	}
	public void setPage(Page<InspectionInterval> page) {
		this.page = page;
	}
	@Override
	public InspectionInterval getModel() {
		return inspectionInterval;
	}
	@Action("delete")
	@Override
	public String delete() throws Exception { 
		inspectionIntervalManager.deleteInspectionInterval(deleteIds);
		JSONObject obj=new JSONObject();
		obj.put("message", "删除成功");
		renderText(obj.toString());
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String listDatas(){
		page=inspectionIntervalManager.search(page);
		renderText(PageUtils.pageToJson(page,"IQC_INSPECTION_INTERVAL"));
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionInterval=new InspectionInterval();
			inspectionInterval.setCreatedTime(new Date());
			inspectionInterval.setCreator(ContextUtils.getUserName());
			inspectionInterval.setDepartmentId(ContextUtils.getDepartmentId());
			inspectionInterval.setLastModifiedTime(new Date());
			inspectionInterval.setLastModifier(ContextUtils.getUserName());
			inspectionInterval.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionInterval.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			inspectionInterval=inspectionIntervalManager.getInspectionInterval(id);
			inspectionInterval.setLastModifiedTime(new Date());
			inspectionInterval.setLastModifier(ContextUtils.getUserName());
		}
		
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		inspectionIntervalManager.saveInspectionInterval(inspectionInterval);
		this.renderText(JsonParser.getRowValue(inspectionInterval));
		return null;
	}

}
