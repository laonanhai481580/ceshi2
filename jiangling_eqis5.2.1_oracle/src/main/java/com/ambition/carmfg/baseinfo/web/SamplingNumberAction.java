package com.ambition.carmfg.baseinfo.web;

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

import com.ambition.carmfg.baseinfo.service.SamplingNumberManager;
import com.ambition.carmfg.entity.SamplingNumber;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:抽检数量维护action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月8日 发布
 */
@Namespace("/carmfg/sampling-number")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/sampling-number", type = "redirectAction") })
public class SamplingNumberAction extends BaseAction<SamplingNumber>{

	private static final long serialVersionUID = 1L;
	private SamplingNumber samplingNumber;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private GridColumnInfo personGridColumnInfo;
	private Page<SamplingNumber> page;
	@Autowired
	private SamplingNumberManager samplingNumberManager;
	@Override
	public SamplingNumber getModel() {
		return samplingNumber;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="删除抽检数量维护信息")
	public String delete() throws Exception {
		try {
			samplingNumberManager.deleteSamplingNumber(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除抽检数量维护信息，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除抽检数量维护信息失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建抽检数量维护信息")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			samplingNumber=new SamplingNumber();
			samplingNumber.setCompanyId(ContextUtils.getCompanyId());
			samplingNumber.setCreatedTime(new Date());
			samplingNumber.setCreator(ContextUtils.getUserName());
			samplingNumber.setModifiedTime(new Date());
			samplingNumber.setModifier(ContextUtils.getLoginName());
			samplingNumber.setModifierName(ContextUtils.getUserName());
			samplingNumber.setBusinessUnitName(ContextUtils.getSubCompanyName());
			samplingNumber.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			samplingNumber=samplingNumberManager.getSamplingNumber(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存抽检数量维护信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			samplingNumber.setModifiedTime(new Date());
			samplingNumber.setModifier(ContextUtils.getLoginName());
			samplingNumber.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", samplingNumber.toString());
		}else{
			logUtilDao.debugLog("保存", samplingNumber.toString());
		}
		try {
			samplingNumberManager.saveSamplingNumber(samplingNumber);
			this.renderText(JsonParser.object2Json(samplingNumber));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存抽检数量维护信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = samplingNumberManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "抽检数量维护信息");
		} catch (Exception e) {
			log.error("查询抽检数量维护信息失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="抽检数量维护信息")
	public String export() throws Exception {
		try {
			Page<SamplingNumber> page = new Page<SamplingNumber>(65535);
			page = samplingNumberManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"抽检数量维护信息台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出抽检数量维护信息失败",e);
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
	public Page<SamplingNumber> getPage() {
		return page;
	}
	public void setPage(Page<SamplingNumber> page) {
		this.page = page;
	}
	public GridColumnInfo getPersonGridColumnInfo() {
		return personGridColumnInfo;
	}
	public void setPersonGridColumnInfo(GridColumnInfo personGridColumnInfo) {
		this.personGridColumnInfo = personGridColumnInfo;
	}
	
}
