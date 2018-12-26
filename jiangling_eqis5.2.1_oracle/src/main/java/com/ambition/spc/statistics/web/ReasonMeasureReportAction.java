package com.ambition.spc.statistics.web;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.ReasonMeasure;
import com.ambition.spc.statistics.service.ReasonMeasureReportManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**    
 * ReasonMeasureReportAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/statistics-analysis")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/statistics-analysis", type = "redirectAction") })
public class ReasonMeasureReportAction extends CrudActionSupport<ReasonMeasure> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private ReasonMeasure reasonMeasure;
	private Page<ReasonMeasure> page;
	private JSONObject params;//查询参数
	@Autowired
	private ReasonMeasureReportManager reasonMeasureReportManager;

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

	public ReasonMeasure getReasonMeasure() {
		return reasonMeasure;
	}

	public void setReasonMeasure(ReasonMeasure reasonMeasure) {
		this.reasonMeasure = reasonMeasure;
	}

	public Page<ReasonMeasure> getPage() {
		return page;
	}

	public void setPage(Page<ReasonMeasure> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	public ReasonMeasure getModel() {
		return reasonMeasure;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Action("reason-measure")
	public String reasonMeasure() throws Exception{
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String listDatas() throws Exception{
		try{
			page = reasonMeasureReportManager.searchByPage(page,params);
			this.renderText(PageUtils.pageToJson(page,"SPC_REASON_MEASURE"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
