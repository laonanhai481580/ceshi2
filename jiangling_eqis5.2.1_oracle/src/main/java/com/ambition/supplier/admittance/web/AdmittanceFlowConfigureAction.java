package com.ambition.supplier.admittance.web;

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

import com.ambition.supplier.admittance.service.AdmittanceFlowConfigureManager;
import com.ambition.supplier.entity.AdmittanceFlowConfigure;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:流程设置Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：流程配置的增删改查</p>
 * @author  赵骏
 * @version 1.00 2013-4-20 发布
 */
@Namespace("/supplier/admittance/flow-configure")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "supplier/admittance/flow-configure", type = "redirectAction") })
public class AdmittanceFlowConfigureAction  extends CrudActionSupport<AdmittanceFlowConfigure> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long indicatorId;//指标ID
	private String deleteIds;//删除的编号 
	
	private AdmittanceFlowConfigure admittanceFlowConfigure;
	
 	@Autowired
	private AdmittanceFlowConfigureManager flowConfigureManager;
 	
 	private Page<AdmittanceFlowConfigure> page;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
	public String getDeleteIds() {
		return deleteIds;
	}
	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	public AdmittanceFlowConfigure getAdmittanceFlowConfigure() {
		return admittanceFlowConfigure;
	}
	public void setAdmittanceFlowConfigure(
			AdmittanceFlowConfigure admittanceFlowConfigure) {
		this.admittanceFlowConfigure = admittanceFlowConfigure;
	}
	
	public Page<AdmittanceFlowConfigure> getPage() {
		return page;
	}
	public void setPage(Page<AdmittanceFlowConfigure> page) {
		this.page = page;
	}
	@Override
	public AdmittanceFlowConfigure getModel() {
		return admittanceFlowConfigure;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			admittanceFlowConfigure = new AdmittanceFlowConfigure();
			admittanceFlowConfigure.setCreatedTime(new Date());
			admittanceFlowConfigure.setCompanyId(ContextUtils.getCompanyId());
			admittanceFlowConfigure.setCreator(ContextUtils.getUserName());
			admittanceFlowConfigure.setLastModifiedTime(new Date());
			admittanceFlowConfigure.setBusinessUnitName(ContextUtils.getSubCompanyName());
			admittanceFlowConfigure.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			admittanceFlowConfigure = flowConfigureManager.getFlowConfigure(id);
		}
	}
	
	@Action("save")
	@Override
	@LogInfo(optType="保存",message="保存准入流程配置")
	public String save() throws Exception {
		try {
			flowConfigureManager.saveAdmittanceFlowConfigure(admittanceFlowConfigure);
			this.renderText(JsonParser.getRowValue(admittanceFlowConfigure));
		} catch (Exception e) {
			if(e instanceof AmbFrameException){
				logger.debug("保存准入流程配置出错",e);
			}else{
				logger.error("保存准入流程配置出错",e);
			}
			createErrorMessage("保存准入流程配置失败!" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	@LogInfo(optType="删除",message="删除准入流程配置")
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				flowConfigureManager.deleteAdmittanceFlowConfigure(deleteIds);
				renderText("删除成功!");
			} catch (Exception e) {
				renderText("删除失败:" + e.getMessage());
				logger.error("删除准入流程配置失败!",e);
			}
		}
		return null;
	}
	
	@Action("list-datas")
	@LogInfo(optType="查询",message="查询准入流程配置")
	public String list() throws Exception {
		page = flowConfigureManager.search(page);
		renderText(PageUtils.pageToJson(page,"SUPPLIER_ADMITTANCE_FLOW_CONFIGURE"));
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unused")
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
