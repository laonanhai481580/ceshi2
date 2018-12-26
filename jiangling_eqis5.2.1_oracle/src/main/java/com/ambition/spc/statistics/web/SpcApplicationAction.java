package com.ambition.spc.statistics.web;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.layertype.service.LayerTypeManager;
import com.ambition.spc.statistics.service.SpcApplicationDatasManager;
import com.ambition.spc.statistics.service.SpcApplicationManager;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**    
 * SpcApplicationAction.java
 * @authorBy YUKE
 * SPC应用状况报表
 */
@Namespace("/spc/statistics-analysis")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/statistics-analysis", type = "redirectAction") })
public class SpcApplicationAction extends com.ambition.product.base.CrudActionSupport<QualityFeature> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private QualityFeature qualityFeature;
	private Page<QualityFeature> page = new Page<QualityFeature>(Page.EACH_PAGE_TEN, true);
	private String deleteIds;
	private JSONObject params;
	@Autowired
	private SpcApplicationManager spcApplicationManager;
	@Autowired
	private SpcApplicationDatasManager spcApplicationDatasManager;
	@Autowired
	private LayerTypeManager layerTypeManager;
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}
	
	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}
	
	public Page<QualityFeature> getPage() {
		return page;
	}
	
	public void setPage(Page<QualityFeature> page) {
		this.page = page;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}
	
	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	public JSONObject getParams() {
		return params;
	}
	
	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	@Action("spc-application")
	public String spcApplication() throws Exception {
		List<LayerType> layerTypes=layerTypeManager.getLayerTypes();
		ActionContext.getContext().put("layerTypes", layerTypes);
		return SUCCESS;
	}
	
	//SPC应用状况报表数据
	@Action("spc-application-datas")
	public String spcApplicationDatas() throws Exception {
		try {
//			this.renderText(JSONObject.fromObject(spcApplicationManager.getSpcApplicationDatas(params)).toString());
			this.renderText(JSONObject.fromObject(spcApplicationDatasManager.getSpcApplicationDatas(params)).toString());
		} catch (Exception e) {
			createErrorMessage("统计失败:" + e.getMessage());
			log.error("数据查询失败!",e);
		}
		return null;
	}
	
	@Action("spc-application-detail")
	public String spcApplicationDetail() throws Exception {
		params = CommonUtil1.convertJsonObject(params);
		return SUCCESS;
	}
	
	@Action("spc-application-detail-datas")
	public String spcApplicationDetailDatas() throws Exception {
		try {
			page = spcApplicationManager.searchByPage(page,params);
			renderText(PageUtils.pageToJson(page,"SPC_QUALITY_FEATURE_DETAIL"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
