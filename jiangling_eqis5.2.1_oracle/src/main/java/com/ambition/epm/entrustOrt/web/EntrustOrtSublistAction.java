package com.ambition.epm.entrustOrt.web;

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

import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entrustOrt.services.EntrustOrtSublistManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
@Namespace("/epm/entrust-ort/sub")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/epm/entrust-ort/sub", type = "redirectAction")})
public class EntrustOrtSublistAction extends CrudActionSupport<EntrustOrtSublist>{

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private EntrustOrtSublist entrustOrtSublist;
	private Page<EntrustOrtSublist> page;
	@Autowired
	private EntrustOrtSublistManager entrustOrtSublistManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public EntrustOrtSublist getEntrustOrtSublist() {
		return entrustOrtSublist;
	}
	public void setEntrustOrtSublist(EntrustOrtSublist entrustOrtSublist) {
		this.entrustOrtSublist = entrustOrtSublist;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
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
	public Page<EntrustOrtSublist> getPage() {
		return page;
	}
	public void setPage(Page<EntrustOrtSublist> page) {
		this.page = page;
	}
	@Override
	public EntrustOrtSublist getModel() {
		return entrustOrtSublist;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="删除ORT子表数据")
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			entrustOrtSublistManager.deleteEntrustOrtSublist(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据，编号："+deleteIds);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		
		return null;
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	
	@Action("list-datas")
	@LogInfo(optType="查询",message="查询ORT子表数据")
	public String listDates(){
		try {
			page = entrustOrtSublistManager.search(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("台账获取例表失败", e);
		}
		return null;
	}
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询ORT子表数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
//		String processSection = Struts2Utils.getParameter("processSection");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		try{
			if(type==null){
				type="N";
			}
			if(weight==3){
				page = entrustOrtSublistManager.listState(page, type,null);
			}else{
				page = entrustOrtSublistManager.listState(page, type,subName);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
			entrustOrtSublist = new EntrustOrtSublist();
			entrustOrtSublist.setCompanyId(ContextUtils.getCompanyId());
			entrustOrtSublist.setCreatedTime(new Date());
			entrustOrtSublist.setCreator(ContextUtils.getUserName());
			entrustOrtSublist.setModifiedTime(new Date());
			entrustOrtSublist.setModifier(ContextUtils.getUserName());
			entrustOrtSublist.setBusinessUnitName(ContextUtils.getSubCompanyName());
			entrustOrtSublist.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			entrustOrtSublist.setFactoryClassify(subName);
		}else {
			entrustOrtSublist = entrustOrtSublistManager.getEntrustOrtSublist(id);
		}
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存ORT子表数据")
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		try {
			entrustOrtSublistManager.saveEntrustOrtSublist(entrustOrtSublist);
			renderText(JsonParser.getRowValue(entrustOrtSublist));
			logUtilDao.debugLog("保存",entrustOrtSublist.toString());
		} catch (Exception e) {
			// TODO: handle exception
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	//创建返回消息
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	@Action("epmHide")
	public String addFactoryClassify(){
		String fcId =Struts2Utils.getParameter("id");
		String[] fcIds=fcId.split(",");
		for(String id : fcIds){
			EntrustOrtSublist entrustOrtSublist=entrustOrtSublistManager.getEntrustOrtSublist(Long.decode(id));
			String fc = entrustOrtSublist.getEntrustOrt().getBusinessUnitName();
			entrustOrtSublist.setFactoryClassify(fc);
			entrustOrtSublistManager.saveEntrustOrtSublist(entrustOrtSublist);
		}
		return null;
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出ORT子表数据")
	public String export() throws Exception {
		String companyName =PropUtils.getProp("companyName");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		Page<EntrustOrtSublist> page = new Page<EntrustOrtSublist>(100000);
		if(weight==3||!"TP".equals(companyName)){
			page = entrustOrtSublistManager.listState(page, "N",null);
		}else{
			page = entrustOrtSublistManager.listState(page, "N",subName);
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService()
				.getExportData(page, "EPM_ENTRUST_ORT_SUBLIST"), "Ort试验验委托台帐"));
		return null;
	}
	@Action("update-all")
	@LogInfo(optType="修改")
	public String updateSupplierAll() throws Exception {
		JSONObject result = new JSONObject();
		String type=Struts2Utils.getParameter("type");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		List<EntrustOrtSublist> lists=entrustOrtSublistManager.listOutOrIn(subName);
		for(EntrustOrtSublist list:lists){
			if(type.equals("in")){
				if(list.getTestAfterIn()!=null){
					list.setTestAfter(list.getTestAfterIn());
				}
				if(list.getDefectNumberIn()!=null){
					list.setDefectNumber(list.getDefectNumberIn());
				}
				if(list.getRemarkIn()!=null){
					list.setRemark(list.getRemarkIn());
				}
				if(list.getDefectRateIn()!=null){
					list.setDefectRate(list.getDefectRateIn());
				}
			}else{
				if(list.getTestAfterIn()==null||"".equals(list.getTestAfterIn())){
					list.setTestAfterIn(list.getTestAfter());
				}
				if(list.getDefectNumberIn()==null||"".equals(list.getDefectNumberIn())){
					list.setDefectNumberIn(list.getDefectNumber());
				}
				if(list.getRemarkIn()==null||"".equals(list.getRemarkIn())){
					list.setRemarkIn(list.getRemark());
				}
				if(list.getDefectRateIn()==null||"".equals(list.getDefectRateIn())){
					list.setDefectRateIn(list.getDefectRate());
				}
				if(list.getTestAfterOut()!=null){
					list.setTestAfter(list.getTestAfterOut());
				}
				if(list.getDefectNumberOut()!=null){
					list.setDefectNumber(list.getDefectNumberOut());
				}
				if(list.getRemarkOut()!=null){
					list.setRemark(list.getRemarkOut());
				}
				if(list.getDefectRateOut()!=null){
					list.setDefectRate(list.getDefectRateOut());
				}
			}
		}
		try{
			result.put("message", "修改成功!");
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", e.getMessage());
			e.printStackTrace();
		}
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"修改对内对外状态为:"+type);
		renderText(result.toString());
		return null;
	}
	@Action("update-input")
	public String updateInput(){
		String eid = Struts2Utils.getParameter("id");
		Struts2Utils.getRequest().setAttribute("id",eid);
		ActionContext.getContext().put("updateStates",ApiFactory.getSettingService().getOptionsByGroupCode("iqc-update-state"));
		ActionContext.getContext().put("testResults",ApiFactory.getSettingService().getOptionsByGroupCode("epm_testResults"));
		return SUCCESS;
	}
	@Action("update-after")
	@LogInfo(optType="修改")
	public String updateAfter() throws Exception {
		JSONObject result = new JSONObject();
		String deleteIds=Struts2Utils.getParameter("id");
		String state=Struts2Utils.getParameter("state");
		String testAfter=Struts2Utils.getParameter("testAfter");
		String defect=Struts2Utils.getParameter("defectNumber");
		String remark=Struts2Utils.getParameter("remark");
		String defectRate=Struts2Utils.getParameter("defectRate"); 
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			if(id!=null){
				EntrustOrtSublist entrustOrtSublist=entrustOrtSublistManager.getEntrustOrtSublist(Long.valueOf(id));
				if(state.equals("in")&&state!=null&&!"".equals(state)){
					entrustOrtSublist.setTestAfter(testAfter);
					entrustOrtSublist.setTestAfterIn(testAfter);
					entrustOrtSublist.setDefectNumber(Integer.valueOf(defect));
					entrustOrtSublist.setDefectNumberIn(Integer.valueOf(defect));
					entrustOrtSublist.setRemark(remark);
					entrustOrtSublist.setRemarkIn(remark);
					entrustOrtSublist.setDefectRate(defectRate);
					entrustOrtSublist.setDefectRateIn(defectRate);
				}else{
					if(entrustOrtSublist.getTestAfterIn()==null||"".equals(entrustOrtSublist.getTestAfterIn())){
						entrustOrtSublist.setTestAfterIn(entrustOrtSublist.getTestAfter());
					}
					if(entrustOrtSublist.getDefectNumberIn()==null||"".equals(entrustOrtSublist.getDefectNumberIn())){
						entrustOrtSublist.setDefectNumberIn(entrustOrtSublist.getDefectNumber());
					}
					if(entrustOrtSublist.getRemarkIn()==null||"".equals(entrustOrtSublist.getRemarkIn())){
						entrustOrtSublist.setRemarkIn(entrustOrtSublist.getRemark());
					}
					if(entrustOrtSublist.getDefectRateIn()==null||"".equals(entrustOrtSublist.getDefectRateIn())){
						entrustOrtSublist.setDefectRateIn(entrustOrtSublist.getDefectRate());
					}
					entrustOrtSublist.setTestAfter(testAfter);
					entrustOrtSublist.setDefectNumber(Integer.valueOf(defect));
					entrustOrtSublist.setRemark(remark);
					entrustOrtSublist.setDefectRate(defectRate);
					entrustOrtSublist.setTestAfterOut(testAfter);
					entrustOrtSublist.setDefectNumberOut(Integer.valueOf(defect));
					entrustOrtSublist.setRemarkOut(remark);
					entrustOrtSublist.setDefectRateOut(defectRate);
				}
			}
		}
		try{			
			result.put("message", "保存成功!");
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", e.getMessage());
			e.printStackTrace();
		}
		renderText(result.toString());
		return null;
	}
}
