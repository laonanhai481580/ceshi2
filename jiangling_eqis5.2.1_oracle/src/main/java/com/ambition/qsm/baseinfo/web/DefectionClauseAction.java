package com.ambition.qsm.baseinfo.web;

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

import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.qsm.baseinfo.service.DefectionClauseManager;
import com.ambition.qsm.entity.DefectionClause;
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
 * 类名:不符合条款action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Namespace("/qsm/defection-clause")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/defection-clause", type = "redirectAction") })
public class DefectionClauseAction extends BaseAction<DefectionClause>{

	private static final long serialVersionUID = 1L;
	private DefectionClause defectionClause;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<DefectionClause> page;
	@Autowired
	private DefectionClauseManager defectionClauseManager;
	@Override
	public DefectionClause getModel() {
		return defectionClause;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="不符合条款")
	public String delete() throws Exception {
		try {
			defectionClauseManager.deleteDefectionClause(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除不符合条款，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除不符合条款失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建不符合条款")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			defectionClause=new DefectionClause();
			defectionClause.setCompanyId(ContextUtils.getCompanyId());
			defectionClause.setCreatedTime(new Date());
			defectionClause.setCreator(ContextUtils.getUserName());
			defectionClause.setModifiedTime(new Date());
			defectionClause.setModifier(ContextUtils.getLoginName());
			defectionClause.setModifierName(ContextUtils.getUserName());
			defectionClause.setBusinessUnitName(ContextUtils.getSubCompanyName());
			defectionClause.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			defectionClause=defectionClauseManager.getDefectionClause(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存不符合条款")
	public String save() throws Exception {
		if(id != null && id != 0){
			defectionClause.setModifiedTime(new Date());
			defectionClause.setModifier(ContextUtils.getLoginName());
			defectionClause.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", defectionClause.toString());
		}else{
			logUtilDao.debugLog("保存", defectionClause.toString());
		}
		try {
			defectionClauseManager.saveDefectionClause(defectionClause);
			this.renderText(JsonParser.object2Json(defectionClause));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存不符合条款失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = defectionClauseManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "不符合条款");
		} catch (Exception e) {
			log.error("查询不符合条款失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="不符合条款")
	public String export() throws Exception {
		try {
			Page<DefectionClause> page = new Page<DefectionClause>(65535);
			page = defectionClauseManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"不符合条款台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出不符合条款历失败",e);
		}
		return null;
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
	public Page<DefectionClause> getPage() {
		return page;
	}
	public void setPage(Page<DefectionClause> page) {
		this.page = page;
	}
	
}
