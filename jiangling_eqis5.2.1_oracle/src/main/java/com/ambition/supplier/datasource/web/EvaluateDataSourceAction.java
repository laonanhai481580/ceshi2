package com.ambition.supplier.datasource.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.datasource.service.EvaluateDataSourceManager;
import com.ambition.supplier.entity.EvaluateDataSource;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.conversion.annotations.Conversion;

@Namespace("/supplier/datasource")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/datasource", type = "redirectAction")})
@Conversion
public class EvaluateDataSourceAction extends CrudActionSupport<EvaluateDataSource> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<EvaluateDataSource> page;
	private EvaluateDataSource evaluateDataSource;
	
	private Boolean multiselect = false;
	
	private JSONObject params;//查询参数
	
 	@Autowired
	private EvaluateDataSourceManager dataSourceManager;

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

	public Page<EvaluateDataSource> getPage() {
		return page;
	}

	public void setPage(Page<EvaluateDataSource> page) {
		this.page = page;
	}

	public EvaluateDataSource getEvaluateDataSource() {
		return evaluateDataSource;
	}

	public void setEvaluateDataSource(EvaluateDataSource evaluateDataSource) {
		this.evaluateDataSource = evaluateDataSource;
	}

	public Boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public EvaluateDataSourceManager getGradeRuleManager() {
		return dataSourceManager;
	}

	public void setGradeRuleManager(EvaluateDataSourceManager gradeRuleManager) {
		this.dataSourceManager = gradeRuleManager;
	}

	@Override
	public EvaluateDataSource getModel() {
		return evaluateDataSource;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			evaluateDataSource = new EvaluateDataSource();
			evaluateDataSource.setCreatedTime(new Date());
			evaluateDataSource.setCompanyId(ContextUtils.getCompanyId());
			evaluateDataSource.setCreator(ContextUtils.getUserName());
			evaluateDataSource.setLastModifiedTime(new Date());
			evaluateDataSource.setLastModifier(ContextUtils.getUserName());
			evaluateDataSource.setBusinessUnitName(ContextUtils.getSubCompanyName());
			evaluateDataSource.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			evaluateDataSource = dataSourceManager.getGradeRule(id);
		}
	}
	
	@Action("input")
	@Override
	@LogInfo(optType="数据来源维护")
	public String input() throws Exception {
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,evaluateDataSource.toString());
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	@LogInfo(optType="保存评分数据来源")
	public String save() throws Exception {
		try {
			if(id == null){
				dataSourceManager.saveEvaluateDataSource(evaluateDataSource);
			}else{
				if(evaluateDataSource != null){
					evaluateDataSource.setLastModifiedTime(new Date());
					evaluateDataSource.setLastModifier(ContextUtils.getUserName());
					dataSourceManager.saveEvaluateDataSource(evaluateDataSource);
				}else{
					throw new AmbFrameException("ID为【"+id+"】的数据来源不存在!");
				}
			}
			this.renderText("{\"id\":"+evaluateDataSource.getId()+",\"code\":\""+evaluateDataSource.getCode()+"\"}");
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,evaluateDataSource.toString());
		} catch (Exception e) {
			createErrorMessage("保存失败：" + e.getMessage());
			if(!(e instanceof AmbFrameException)){
				logger.error("保存评分数据来源失败",e);
			}else{
				logger.debug("保存评分数据来源失败",e);
			}
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"保存失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	@LogInfo(optType="删除评分数据来源")
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除失败,删除的IDS为空!");
		}else{
			try {
				dataSourceManager.deleteEvaluateDataSource(deleteIds);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"deleteIds:" + deleteIds);
			} catch (Exception e) {
				createErrorMessage("删除失败：" + e.getMessage());
				if(!(e instanceof AmbFrameException)){
					logger.error("删除数据来源失败",e);
				}else{
					logger.debug("删除数据来源失败",e);
				}
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除失败," + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	@LogInfo(optType="数据来源页面")
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	@LogInfo(optType="查询数据来源")
	public String getGradeRules() throws Exception {
		try{
			page = dataSourceManager.search(page);
			renderText(PageUtils.pageToJson(page,"SUPPLIER_EVALUATE_DATA_SOURCE"));
		}catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				logger.error("查询数据来源失败",e);
			}else{
				logger.debug("查询数据来源失败",e);
			}
		}
		return null;
	}
	
	/*
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
