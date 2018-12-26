package com.ambition.carmfg.ipqc.web;

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

import com.ambition.carmfg.entity.IpqcAuditWarming;
import com.ambition.carmfg.ipqc.service.IpqcAuditWarmingManager;
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
 * 类名:IPQC稽核预警action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月20日 发布
 */
@Namespace("/carmfg/ipqc/ipqc-warming")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/ipqc/ipqc-warming", type = "redirectAction") })
public class IpqcAuditWarmingAction extends BaseAction<IpqcAuditWarming>{

	private static final long serialVersionUID = 1L;
	private IpqcAuditWarming ipqcAuditWarming;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<IpqcAuditWarming> page;
	@Autowired
	private IpqcAuditWarmingManager ipqcAuditWarmingManager;
	@Override
	public IpqcAuditWarming getModel() {
		return ipqcAuditWarming;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="IPQC稽核预警信息")
	public String delete() throws Exception {
		try {
			ipqcAuditWarmingManager.deleteIpqcAuditWarming(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除IPQC稽核预警，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除IPQC稽核预警信息失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			ipqcAuditWarming=new IpqcAuditWarming();
			ipqcAuditWarming.setCompanyId(ContextUtils.getCompanyId());
			ipqcAuditWarming.setCreatedTime(new Date());
			ipqcAuditWarming.setCreator(ContextUtils.getUserName());
			ipqcAuditWarming.setModifiedTime(new Date());
			ipqcAuditWarming.setModifier(ContextUtils.getLoginName());
			ipqcAuditWarming.setModifierName(ContextUtils.getUserName());
			ipqcAuditWarming.setBusinessUnitName(ContextUtils.getSubCompanyName());
			ipqcAuditWarming.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ipqcAuditWarming=ipqcAuditWarmingManager.getIpqcAuditWarming(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存IPQC稽核预警信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			ipqcAuditWarming.setModifiedTime(new Date());
			ipqcAuditWarming.setModifier(ContextUtils.getLoginName());
			ipqcAuditWarming.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", ipqcAuditWarming.toString());
		}else{
			logUtilDao.debugLog("保存", ipqcAuditWarming.toString());
		}
		try {
			ipqcAuditWarmingManager.saveIpqcAuditWarming(ipqcAuditWarming);
			this.renderText(JsonParser.object2Json(ipqcAuditWarming));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存IPQC稽核预警信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = ipqcAuditWarmingManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "IPQC稽核预警信息");
		} catch (Exception e) {
			log.error("查询IPQC稽核预警信息失败  ",e);
		}		
		return null;
	}
	@Action("export")
	public String export() throws Exception {
		try {
			Page<IpqcAuditWarming> page = new Page<IpqcAuditWarming>(65535);
			page = ipqcAuditWarmingManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"IPQC稽核预警台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出IPQC稽核预警信息失败",e);
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
	public Page<IpqcAuditWarming> getPage() {
		return page;
	}
	public void setPage(Page<IpqcAuditWarming> page) {
		this.page = page;
	}
	
}
