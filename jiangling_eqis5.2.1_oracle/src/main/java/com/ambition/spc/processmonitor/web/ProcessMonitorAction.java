package com.ambition.spc.processmonitor.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.ReasonMeasure;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.processmonitor.service.ProcessMonitorManager;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**    
 * ProcessMonitorAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/process-monitor")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/process-monitor", type = "redirectAction") })
public class ProcessMonitorAction extends CrudActionSupport<ReasonMeasure>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private ReasonMeasure reasonMeasure;
	private Page<ReasonMeasure> page;
	private AbnormalInfo abnormalInfo;
	private SpcSubGroup subGroup;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	@Autowired
	private ProcessMonitorManager processMonitorManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	
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

	public ReasonMeasure getReasonMeasure() {
		return reasonMeasure;
	}

	public void setReasonMeasure(ReasonMeasure reasonMeasure) {
		this.reasonMeasure = reasonMeasure;
	}

	public Page<ReasonMeasure> getPage() {
		return page;
	}

	public void setPage(Page<ReasonMeasure> page) {
		this.page = page;
	}

	public AbnormalInfo getAbnormalInfo() {
		return abnormalInfo;
	}

	public void setAbnormalInfo(AbnormalInfo abnormalInfo) {
		this.abnormalInfo = abnormalInfo;
	}

	public SpcSubGroup getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(SpcSubGroup subGroup) {
		this.subGroup = subGroup;
	}

	@Override
	public ReasonMeasure getModel() {
		return reasonMeasure;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			reasonMeasure = new ReasonMeasure();
			reasonMeasure.setCompanyId(ContextUtils.getCompanyId());
			reasonMeasure.setCreatedTime(new Date()); 
			reasonMeasure.setCreator(ContextUtils.getUserName()); 
			reasonMeasure.setModifiedTime(new Date());
			reasonMeasure.setModifier(ContextUtils.getUserName());
		}else{
			reasonMeasure = processMonitorManager.getReasonMeasure(id);
		}
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		return SUCCESS;
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("save")
	@Override
	public String save() throws Exception {
		if(id!=null && id!=0){
			reasonMeasure.setModifiedTime(new Date());
			reasonMeasure.setModifier(ContextUtils.getUserName());
		}
		try {
			processMonitorManager.saveReasonMeasure(reasonMeasure);
			addActionMessage("保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			reasonMeasure.setId(id);
			addActionMessage("保存失败:" + e.getMessage());
		}
		return "reason-measure-input";
	}
	
	/**
	 * 异常消息子组数据
	 * @return
	 * @throws Exception
	 */
	@Action("subgroup-detail")
	public String subGroupDetail() throws Exception{
		String messageId = Struts2Utils.getParameter("messageId");
		if(messageId != null && !messageId.equals("null") && !messageId.equals("undefined")){
			abnormalInfo = abnormalInfoManager.getAbnormalInfo(Long.valueOf(messageId));
			if(abnormalInfo.getSpcSubGroup() == null){
				subGroup = spcSubGroupManager.queryGroupByNumAndFeature(abnormalInfo.getQualityFeature(), abnormalInfo.getNum()==null?0:Integer.valueOf(abnormalInfo.getNum()),null);
			}else{
				subGroup = abnormalInfo.getSpcSubGroup();
			}
			if(subGroup != null){
				processMonitorManager.getDetailDatas(subGroup);
				ActionContext.getContext().put("error",false);
			}else{
				ActionContext.getContext().put("error",true);
			}
		}else{
			ActionContext.getContext().put("error",true);
		}
		return SUCCESS;
	}
	
	@Action("reason-measure-input")
	public String reasonMeasureInput() throws Exception {
		String messageId = Struts2Utils.getParameter("messageId");
		if(messageId != null){
			abnormalInfo = abnormalInfoManager.getAbnormalInfo(Long.valueOf(messageId));
			reasonMeasure = processMonitorManager.queryReasonMeasureByAbnormalInfo(abnormalInfo);
		}
		ActionContext.getContext().put("reasonMeasure",reasonMeasure);
		return SUCCESS;
	}
	
}
