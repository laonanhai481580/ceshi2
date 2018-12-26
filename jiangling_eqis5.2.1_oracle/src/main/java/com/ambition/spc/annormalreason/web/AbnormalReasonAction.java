package com.ambition.spc.annormalreason.web;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.annormalreason.service.AbnormalReasonManager;
import com.ambition.spc.annormalreason.service.AbnormalReasonTypeManger;
import com.ambition.spc.entity.AbnormalReason;
import com.ambition.spc.entity.AbnormalReasonType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * AbnormalReasonAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/base-info/abnormal-reason")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/abnormal-reason", type = "redirectAction") })
public class AbnormalReasonAction extends com.ambition.product.base.CrudActionSupport<AbnormalReason>{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private AbnormalReason abnormalReason;
	private Page<AbnormalReason> page;
	@Autowired
	private AbnormalReasonManager abnormalReasonManager;
	@Autowired
	private AbnormalReasonTypeManger abnormalReasonTypeManger;
	
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
	
	public AbnormalReason getAbnormalReason() {
		return abnormalReason;
	}
	
	public void setAbnormalReason(AbnormalReason abnormalReason) {
		this.abnormalReason = abnormalReason;
	}
	
	public Page<AbnormalReason> getPage() {
		return page;
	}
	
	public void setPage(Page<AbnormalReason> page) {
		this.page = page;
	}

	@Override
	public AbnormalReason getModel() {
		return abnormalReason;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			abnormalReason = new AbnormalReason();
			abnormalReason.setCompanyId(ContextUtils.getCompanyId());
			abnormalReason.setCreatedTime(new Date());
			abnormalReason.setCreator(ContextUtils.getUserName());
			abnormalReason.setModifiedTime(new Date());
			abnormalReason.setModifier(ContextUtils.getUserName());
		}else{
			abnormalReason = abnormalReasonManager.getAbnormalReason(id);
		}
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				abnormalReasonManager.deleteAbnormalReason(deleteIds);
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		String typeId = Struts2Utils.getParameter("typeId");
		if(typeId != null && !typeId.equals("")){
			AbnormalReasonType reasonType = abnormalReasonTypeManger.getAbnormalReasonType(Long.valueOf(typeId));
			ActionContext.getContext().put("reasonType",reasonType.getTypeName());
		}
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			abnormalReason.setModifiedTime(new Date());
			abnormalReason.setModifier(ContextUtils.getUserName());
		}
		String typeId = Struts2Utils.getParameter("typeId");
		AbnormalReasonType reasonType = null;
		if(typeId != null && !typeId.equals("")){
			reasonType = abnormalReasonTypeManger.getAbnormalReasonType(Long.valueOf(typeId));
			abnormalReason.setAbnormalReasonType(reasonType);
			abnormalReason.setReasonType(reasonType.getTypeName());
		}
		try {
			abnormalReasonManager.saveAbnormalReason(abnormalReason);
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText(JsonParser.getRowValue(abnormalReason));
				return null;
			}else{
				addActionMessage("保存成功!");
				return "input";
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText("{'error':true,'message':'保存失败:"+e.getMessage()+"'}");
				return null;
			}else{
				abnormalReason.setId(id);
				addActionMessage("保存失败:" + e.getMessage());
				return "input";
			}
		}
	}
	
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = abnormalReasonManager.getPage(page,null);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
