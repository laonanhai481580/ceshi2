package com.ambition.epm.sample.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.epm.entity.Sample;
import com.ambition.epm.entity.SampleSublist;
import com.ambition.epm.sample.services.SampleManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
@Namespace("/epm/sample")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/epm/sample", type = "redirectAction")})
public class SampleAction extends AmbWorkflowActionBase<Sample>{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SampleManager sampleManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private Sample sample;
	Logger log = Logger.getLogger(this.getClass());
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	@Override
	protected AmbWorkflowManagerBase<Sample> getAmbWorkflowBaseManager() {
		return sampleManager;
	}
	public void initForm(){
		if(getId() == null){
			getReport().setFormNo(formCodeGenerated.generateSampleNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
//			getReport().setSendDate(new Date());
		}
		List<SampleSublist> sampleSublists= getReport().getSampleSublists();
		if(sampleSublists == null){
			sampleSublists = new ArrayList<SampleSublist>();
			SampleSublist item = new SampleSublist();
			sampleSublists.add(item);
		}else if(sampleSublists.size()==0){
			sampleSublists = new ArrayList<SampleSublist>();
			SampleSublist item = new SampleSublist();
			sampleSublists.add(item);
		}
		ActionContext.getContext().put("_sampleSublists", sampleSublists);
		ActionContext.getContext().put("sampleHandlings",ApiFactory.getSettingService().getOptionsByGroupCode("epm_sampleHandlings"));
	}
	/**
	 * 导出表单
	 * */
	@Action("export-form")
	@LogInfo(optType="导出表单",message="实验室样品管理")
	public String exportReport() {
		try{
			sampleManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
	}
    /**
     * 创建返回消息
     * @param error
     * @param message
     * @return
     */
    public void createMessage(String message){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("error",false);
        map.put("message",message);
        renderText(JSONObject.fromObject(map).toString());
    }
    /**
	 * 列表数据
	 */
	@Action("list-type")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		try{
			page = sampleManager.listState(page, type);
			renderText(PageUtils.pageToJson(page));
//			logUtilDao.debugLog("查询","查询数据",5240l);
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			String str=sampleManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
}
