package com.ambition.improve.baseinfo.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.improve.baseinfo.service.ProblemDescribleManager;
import com.ambition.improve.entity.ProblemDescrible;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/improve/base-info/problem-describle")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/improve/base-info/problem-describle", type = "redirectAction") })
public class ProblemDescribleAction extends BaseAction<ProblemDescrible>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String operate;
	private String businessUnit;//所属事业部
	private ProblemDescrible problemDescrible;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private ProblemDescribleManager problemDescribleManager;
	private Page<ProblemDescrible> page;
	
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
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public ProblemDescrible getProblemDescrible() {
		return problemDescrible;
	}
	public void setProblemDescrible(ProblemDescrible problemDescrible) {
		this.problemDescrible = problemDescrible;
	}
	public Page<ProblemDescrible> getPage() {
		return page;
	}
	public void setPage(Page<ProblemDescrible> page) {
		this.page = page;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	@Override
	public ProblemDescrible getModel() {
		return problemDescrible;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			problemDescrible = new ProblemDescrible();
			problemDescrible.setCompanyId(ContextUtils.getCompanyId());
			problemDescrible.setCreatedTime(new Date());
			problemDescrible.setCreator(ContextUtils.getUserName());
			problemDescrible.setLastModifiedTime(new Date());
			problemDescrible.setLastModifier(ContextUtils.getUserName());
			problemDescrible.setBusinessUnitName(ContextUtils.getSubCompanyName());
			problemDescrible.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			problemDescrible = problemDescribleManager.getProblemDescrible(id);
		}
	}
	@Action("problem-describle-delete")
	@LogInfo(optType="删除",message="问题描述维护")
	@Override
	public String delete() throws Exception {
		try{
			problemDescribleManager.deleteProblemDescrible(deleteIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("problem-describle-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("problem-describle-list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}
	@Action("problem-describle-save")
	@LogInfo(optType="保存",message="问题描述维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			problemDescrible.setLastModifiedTime(new Date());
			problemDescrible.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", problemDescrible.toString());
		}else{
			logUtilDao.debugLog("保存", problemDescrible.toString());
		}
		try{
			problemDescribleManager.saveProblemDescrible(problemDescrible);
			this.renderText(JsonParser.object2Json(problemDescrible));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("problem-describle-list-datas")
	public String getListDatas() throws Exception {
		page = problemDescribleManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "质量改进管理：基础设置-问题描述维护");
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="问题描述维护")
	public String export() throws Exception {
		Page<ProblemDescrible> page = new Page<ProblemDescrible>(100000);
		page = problemDescribleManager.list(page);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IMP_PROBLEM_DESCRIBLE"),"problemDescrible"));
		logUtilDao.debugLog("导出", "质量改进管理：基础设置-问题描述维护");
		return null;
	}
}
