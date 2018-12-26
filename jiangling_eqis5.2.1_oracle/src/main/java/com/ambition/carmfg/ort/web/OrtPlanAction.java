package com.ambition.carmfg.ort.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OrtPlan;
import com.ambition.carmfg.ort.service.OrtPlanManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:ORT计划action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Namespace("/carmfg/ort/ort-plan")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/ort/ort-plan", type = "redirectAction") })
public class OrtPlanAction extends BaseAction<OrtPlan>{

	private static final long serialVersionUID = 1L;
	private OrtPlan ortPlan;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private String userName;
	private String loginName;
	private Page<OrtPlan> page;
	@Autowired
	private OrtPlanManager ortPlanManager;
	@Override
	public OrtPlan getModel() {
		return ortPlan;
	}
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			ortPlanManager.deleteOrtPlan(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除ORT计划，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除ORT计划失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建ORT计划")
	public String list() throws Exception {
		userName=ContextUtils.getUserName();
		loginName=ContextUtils.getLoginName();
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			ortPlan=new OrtPlan();
			ortPlan.setCompanyId(ContextUtils.getCompanyId());
			ortPlan.setCreatedTime(new Date());
			ortPlan.setCreator(ContextUtils.getUserName());
			ortPlan.setModifiedTime(new Date());
			ortPlan.setModifier(ContextUtils.getLoginName());
			ortPlan.setModifierName(ContextUtils.getUserName());
			ortPlan.setBusinessUnitName(ContextUtils.getSubCompanyName());
			ortPlan.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ortPlan=ortPlanManager.getOrtPlan(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存ORT计划")
	public String save() throws Exception {
		if(id != null && id != 0){
			ortPlan.setModifiedTime(new Date());
			ortPlan.setModifier(ContextUtils.getLoginName());
			ortPlan.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", ortPlan.toString());
		}else{
			logUtilDao.debugLog("保存", ortPlan.toString());
		}
		try {
			ortPlanManager.saveOrtPlan(ortPlan);
			this.renderText(JsonParser.object2Json(ortPlan));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存ORT计划失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = ortPlanManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "ORT计划");
		} catch (Exception e) {
			log.error("查询ORT计划失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="ORT计划")
	public String export() throws Exception {
		try {
			Page<OrtPlan> page = new Page<OrtPlan>(65535);
			page = ortPlanManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"ORT计划台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出ORT计划失败",e);
		}
		return null;
	}
	
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	/**
	 * 
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
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
	public Page<OrtPlan> getPage() {
		return page;
	}
	public void setPage(Page<OrtPlan> page) {
		this.page = page;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}
