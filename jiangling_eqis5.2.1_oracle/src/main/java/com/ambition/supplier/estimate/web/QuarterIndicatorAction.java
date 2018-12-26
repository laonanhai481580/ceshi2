package com.ambition.supplier.estimate.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.datasource.service.EvaluateDataSourceManager;
import com.ambition.supplier.entity.EvaluateDataSource;
import com.ambition.supplier.entity.EvaluatingIndicator;
import com.ambition.supplier.estimate.service.EvaluatingIndicatorManager;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 季度考评
 * @author 赵骏
 *
 */
@Namespace("/supplier/estimate/indicator/quarter")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "supplier/estimate/indicator/quarter", type = "redirectAction") })
public class QuarterIndicatorAction  extends CrudActionSupport<EvaluatingIndicator> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private String deleteIds;//删除的编号 
	@Autowired 
	private LogUtilDao logUtilDao;
	
	private EvaluatingIndicator evaluatingIndicator;
	
 	@Autowired
	private EvaluatingIndicatorManager evaluatingIndicatorManager;
 	
 	@Autowired
 	private EvaluateDataSourceManager evaluateDataSourceManager;

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public EvaluatingIndicator getEvaluatingIndicator() {
		return evaluatingIndicator;
	}

	public void setEvaluatingIndicator(EvaluatingIndicator evaluatingIndicator) {
		this.evaluatingIndicator = evaluatingIndicator;
	}

	public EvaluatingIndicator getModel() {
		return evaluatingIndicator;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			evaluatingIndicator = new EvaluatingIndicator();
			evaluatingIndicator.setCreatedTime(new Date());
			evaluatingIndicator.setCompanyId(ContextUtils.getCompanyId());
			evaluatingIndicator.setCreator(ContextUtils.getUserName());
			evaluatingIndicator.setModifiedTime(new Date());
			evaluatingIndicator.setModifier(ContextUtils.getUserName());
			evaluatingIndicator.setBusinessUnitName(ContextUtils.getSubCompanyName());
			evaluatingIndicator.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			if(parentId != null){
				EvaluatingIndicator parent = evaluatingIndicatorManager.getEvaluatingIndicator(parentId);
				if(parent != null){
					evaluatingIndicator.setParent(parent);
					evaluatingIndicator.setLevel(parent.getLevel()+1);
				}
			}
		}else {
			evaluatingIndicator = evaluatingIndicatorManager.getEvaluatingIndicator(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		rendInput();
		if(id==null){
			EvaluatingIndicator  newIndicatro = evaluatingIndicatorManager.selectBigOrderByNum();
			if(newIndicatro!=null){
				evaluatingIndicator.setOrderByNum(newIndicatro.getOrderByNum()+1);
			}
		}
		return SUCCESS;
	}
	
	private void rendInput(){
		List<Option> units = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_indicator_unit");
		evaluatingIndicatorManager.i18nChange(units);
		ActionContext.getContext().put("units",units);
		ActionContext.getContext().put("dataSources", evaluateDataSourceManager.listAll());
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		if(id == null){
			try{
				evaluatingIndicatorManager.saveEvaluatingIndicator(evaluatingIndicator);
				logUtilDao.debugLog("保存", evaluatingIndicator.toString());
				addActionMessage("保存成功！");
				rendInput();
				return "input";
			}catch(Exception e){
				rendInput();
				addActionMessage("保存失败：" + e.getMessage());
				return "input";
			}
		}else{
			if(evaluatingIndicator != null){
				evaluatingIndicator.setModifiedTime(new Date());
				evaluatingIndicator.setModifier(ContextUtils.getUserName());
				try{
					evaluatingIndicatorManager.saveEvaluatingIndicator(evaluatingIndicator);
					logUtilDao.debugLog("修改", evaluatingIndicator.toString());
					List<Option> units = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_indicator_unit");
					evaluatingIndicatorManager.i18nChange(units);
					ActionContext.getContext().put("units",units);
					ActionContext.getContext().put("dataSources", evaluateDataSourceManager.listAll());
					addActionMessage("保存成功！");
					rendInput();
					return "input";
				}catch(Exception e){
					rendInput();
					addActionMessage("保存失败：" + e.getMessage());
					return "input";
				}
			}else{
				rendInput();
				addActionMessage("保存评价指标失败,指标为空!");
				return "input";
			}
		}
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				evaluatingIndicatorManager.deleteEvaluatingIndicator(deleteIds);
				createMessage("删除成功!");
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("list-datas")
	public String getBomListByParent() throws Exception {
		Page page = new Page();
		List<EvaluatingIndicator> parents = null;
		if(nodeid == null){
			parents = evaluatingIndicatorManager.getTopEvaluatingIndicators();
		}else{
			EvaluatingIndicator parent = evaluatingIndicatorManager.getEvaluatingIndicator(nodeid);
			if(parent != null){
				parents = parent.getChildren();
			}else{
				parents = new ArrayList<EvaluatingIndicator>();
			}
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(EvaluatingIndicator evaluatingIndicator : parents){
			convertEvaluatingIndicator(evaluatingIndicator,list);
		}
		page.setResult(list);
//		renderText(PageUtils.pageToJson(page));
		String result = evaluatingIndicatorManager.getResultJson(page);
		renderText(JSONObject.fromObject(result).toString());
		logUtilDao.debugLog("查询", "供应商质量管理：基础维护-评价指标维护");
		return null;
	}
	
	/**
	 * 转换物料结构至json对象
	 * @param ProductBom
	 * @return
	 */
	private void convertEvaluatingIndicator(EvaluatingIndicator evaluatingIndicator,List<Map<String,Object>> list){
		Boolean isLeaf = evaluatingIndicator.getChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",evaluatingIndicator.getId());
		map.put("name",evaluatingIndicator.getName());
		map.put("unit",evaluatingIndicator.getUnit());
		if(StringUtils.isNotEmpty(evaluatingIndicator.getDataSourceCode())){
			EvaluateDataSource dataSource = evaluateDataSourceManager.getEvaluateDataSourceByCode(evaluatingIndicator.getDataSourceCode());
			if(dataSource != null){
				map.put("evaluateDataSourceName",dataSource.getName());
			}
		}
		map.put("remark",evaluatingIndicator.getRemark());
		map.put("level",evaluatingIndicator.getLevel()-1);
		map.put("parent",evaluatingIndicator.getParent()==null?"":evaluatingIndicator.getParent().getId());
		map.put("isLeaf",isLeaf);
		list.add(map);
		if(!isLeaf){
			map.put("expanded",true);
			map.put("loaded",true);
			for(EvaluatingIndicator child : evaluatingIndicator.getChildren()){
				convertEvaluatingIndicator(child,list);
			}
		}else{
			map.put("loaded",true);
		}
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
