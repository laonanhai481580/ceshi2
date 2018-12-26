package com.ambition.spc.improvemeasure.web;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.ImprovementMeasure;
import com.ambition.spc.improvemeasure.service.MeasureManager;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**    
 * MeasureAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/base-info/improve-measure")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/improve-measure", type = "redirectAction") })
public class MeasureAction extends com.ambition.product.base.CrudActionSupport<ImprovementMeasure> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private ImprovementMeasure improvementMeasure;
	private Page<ImprovementMeasure> page;
	@Autowired
	private MeasureManager measureManager;
	
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
	
	public ImprovementMeasure getImprovementMeasure() {
		return improvementMeasure;
	}
	
	public void setImprovementMeasure(ImprovementMeasure improvementMeasure) {
		this.improvementMeasure = improvementMeasure;
	}
	
	public Page<ImprovementMeasure> getPage() {
		return page;
	}
	
	public void setPage(Page<ImprovementMeasure> page) {
		this.page = page;
	}

	@Override
	public ImprovementMeasure getModel() {
		return improvementMeasure;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			improvementMeasure = new ImprovementMeasure();
			improvementMeasure.setCompanyId(ContextUtils.getCompanyId());
			improvementMeasure.setCreatedTime(new Date());
			improvementMeasure.setCreator(ContextUtils.getUserName());
			improvementMeasure.setModifiedTime(new Date());
			improvementMeasure.setModifier(ContextUtils.getUserName());
		}else{
			improvementMeasure = measureManager.getImprovementMeasure(id);
		}
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				measureManager.deleteImprovementMeasure(deleteIds);
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}

	@Action("save")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			improvementMeasure.setModifiedTime(new Date());
			improvementMeasure.setModifier(ContextUtils.getUserName());
		}
		try {
			measureManager.saveImprovementMeasure(improvementMeasure);
			this.renderText(JsonParser.getRowValue(improvementMeasure));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = measureManager.getPage(page);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("exports")
	public String exprots() throws Exception {
		page = measureManager.getPage(new Page<ImprovementMeasure>(Integer.MAX_VALUE));
		//ExcelExporter.export(mmsUtil.getExportData(page, "SPC_IMPROVEMENT_MEASURE"),"改善措施台账");
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "SPC_IMPROVEMENT_MEASURE"), "改善措施台账"));
		return null;
	}

}
