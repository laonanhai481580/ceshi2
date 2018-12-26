package com.ambition.spc.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.LayerDetail;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.layertype.service.LayerTypeManager;
import com.ambition.spc.processdefine.service.ProcessDefineManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**    
 * CommonAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/common")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/common", type = "redirectAction") })
public class CommonAction extends com.ambition.product.base.CrudActionSupport<QualityFeature> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private Long processId;//结构编号
	private Long layerId;
	private Boolean multiselect = false;//是否多选
	private Long selParentId;
 	private ProcessPoint processPoint;
	private QualityFeature qualityFeature;
	private Page<QualityFeature> page;
	private Page<LayerDetail> layerPage;
	private LayerType layerType;
	
 	@Autowired
 	private ProcessDefineManager processDefineManager;
 	 	
 	@Autowired
 	private QualityFeatureManager qualityFeatureManager;
 	
 	@Autowired
 	private LayerTypeManager layerTypeManager;
 	
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Long getLayerId() {
		return layerId;
	}

	public void setLayerId(Long layerId) {
		this.layerId = layerId;
	}

	public Boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
	}

	public Long getSelParentId() {
		return selParentId;
	}

	public void setSelParentId(Long selParentId) {
		this.selParentId = selParentId;
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
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

	public Page<LayerDetail> getLayerPage() {
		return layerPage;
	}

	public void setLayerPage(Page<LayerDetail> layerPage) {
		this.layerPage = layerPage;
	}

	public LayerType getLayerType() {
		return layerType;
	}

	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	@Override
	public QualityFeature getModel() {
		return qualityFeature;
	}

	@Override
	protected void prepareModel() throws Exception {
		super.prepareModel();
	}
 	
	@Action("feature-bom-select")
	public String featureBomSelect() throws Exception {
		return SUCCESS;
	}
	
	@Action("feature-bom-multi-select")
	public String featureBomMultiSelect() throws Exception {
		return SUCCESS;
	}
	
	@Action("feature-list-datas")
	public String getCodeListByParent() throws Exception {
		if(Struts2Utils.getParameter("processId") != null){
			processId = Long.valueOf(Struts2Utils.getParameter("processId"));
		}
		if(processId != null && processId != 0){
			processPoint = processDefineManager.getProcessPoint(processId);
		}
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		try {
			page = qualityFeatureManager.getPage(page, processPoint,params);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		if(params!=null){
//			page = defectionCodeManager.getCodeByParams(page, params);
//		}else{
//			page = defectionCodeManager.list(page, defectionType);
//		}
//		try {
//			Page<Object> resultPage = new Page<Object>();
//			resultPage.setOrder(page.getOrder());
//			resultPage.setOrderBy(page.getOrderBy());
//			resultPage.setPageNo(page.getPageNo());
//			resultPage.setPageSize(page.getPageSize());
//			resultPage.setTotalCount(page.getTotalCount());
//			List<Object> list = new ArrayList<Object>();
//			for(DefectionCode defectionCode : page.getResult()){
//				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("id",defectionCode.getId());
//				map.put("defectionCodeNo",defectionCode.getDefectionCodeNo());
//				map.put("defectionCodeName",defectionCode.getDefectionCodeName());
//				map.put("defectionTypeName", defectionCode.getDefectionType().getDefectionTypeName());
//				list.add(map);
//			}
//			resultPage.setResult(list);
//			renderText(PageUtils.pageToJson(resultPage));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}
	
	@Action("layer-info-select")
	public String layerInfoSelect() throws Exception {
		return SUCCESS;
	}
	
	@Action("layer-info-datas")
	public String getLayerInfoDatas() throws Exception {
		if(Struts2Utils.getParameter("layerId") != null){
			layerId = Long.valueOf(Struts2Utils.getParameter("layerId"));
		}
		if(layerId != null && layerId != 0){
			FeatureLayer layer = qualityFeatureManager.getFeatureLayer(layerId);
			layerType = layerTypeManager.getLayerType(layer.getTargetId());
		}
		try {
			Page<LayerDetail> page = new Page<LayerDetail>(Integer.MAX_VALUE);
			page.setResult(layerType.getLayerDetails());
			page.setPageNo(1);
			page.setTotalCount(page.getResult().size());
			page.setPageSize(Integer.MAX_VALUE);
			renderText(PageUtils.pageToJson(page,"SPC_SELECT_LAYER_DETAIL"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("set-layer-datas")
	public String setLayerInfoDatas() throws Exception {
		if(Struts2Utils.getParameter("layerName") != null && !Struts2Utils.getParameter("layerName").isEmpty()){
			layerType = layerTypeManager.getLayerTypeByName(Struts2Utils.getParameter("layerName"));
		}
		List<Object> list = new ArrayList<Object>();
		if(layerType.getLayerDetails().size() != 0){
			for(LayerDetail layerDetail : layerType.getLayerDetails()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name",layerDetail.getDetailName());
				map.put("value",layerDetail.getDetailCode());
				list.add(map);
			}
		}else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name","");
			map.put("value","");
			list.add(map);
		}
		renderText(JSONArray.fromObject(list).toString());
//		renderText("[{\"name\":\"aa\",\"value\":\"12\"},{\"name\":\"aa\",\"value\":\"12\"}]");
		return null;
	}
}
