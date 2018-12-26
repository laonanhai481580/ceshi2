package com.ambition.epm.entrustHsf.web;

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

import com.ambition.epm.entity.EntrustHsf;
import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entrustHsf.services.EntrustHsfManager;
import com.ambition.epm.entrustHsf.services.EntrustHsfSublistManager;
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

@Namespace("/epm/entrust-hsf/sub")
@ParentPackage("default")
@Results({@Result(name = CrudActionSupport.RELOAD, location ="/epm/entrust-hsf/sub", type = "redirectAction") })
public class EntrustHsfSublistAction extends CrudActionSupport<EntrustHsfSublist>{

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private String ids;
	private String deleteIds;
	private EntrustHsf entrustHsf;
	private Long id;
	private EntrustHsfSublist entrustHsfSublist;
	private Page<EntrustHsfSublist> page;
	@Autowired
	private EntrustHsfSublistManager entrustHsfSublistManager;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	EntrustHsfManager entrustHsfManager;
	
	public EntrustHsf getEntrustHsf() {
		return entrustHsf;
	}

	public void setEntrustHsf(EntrustHsf entrustHsf) {
		this.entrustHsf = entrustHsf;
	}
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Logger getLog() {
		return log;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public EntrustHsfSublist getEntrustHsfSublist() {
		return entrustHsfSublist;
	}

	public void setEntrustHsfSublist(EntrustHsfSublist entrustHsfSublist) {
		this.entrustHsfSublist = entrustHsfSublist;
	}

	public Page<EntrustHsfSublist> getPage() {
		return page;
	}

	public void setPage(Page<EntrustHsfSublist> page) {
		this.page = page;
	}

	@Override
	public EntrustHsfSublist getModel() {
		// TODO Auto-generated method stub
		return entrustHsfSublist;
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			entrustHsfSublistManager.deleteEntrustHsfSublist(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除HSF子表数据，编号："+deleteIds);
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
	@LogInfo(optType="查询",message="查询HSF子表数据")
	public String listDates(){
		try {
			page = entrustHsfSublistManager.search(page);
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
	@LogInfo(optType="查询",message="查询数据")
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
				page = entrustHsfSublistManager.listState(page, type,null);
			}else{
				page = entrustHsfSublistManager.listState(page, type,subName);
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
			entrustHsfSublist = new EntrustHsfSublist();
			entrustHsfSublist.setCompanyId(ContextUtils.getCompanyId());
			entrustHsfSublist.setCreatedTime(new Date());
			entrustHsfSublist.setCreator(ContextUtils.getUserName());
			entrustHsfSublist.setModifiedTime(new Date());
			entrustHsfSublist.setModifier(ContextUtils.getUserName());
			entrustHsfSublist.setBusinessUnitName(ContextUtils.getSubCompanyName());
			entrustHsfSublist.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			entrustHsfSublist.setFactoryClassify(subName);
		}else {
			entrustHsfSublist = entrustHsfSublistManager.getEntrustHsfSublist(id);
		}
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存HSF子表数据")
	@Override
	public String save() throws Exception {
		try {
			entrustHsfSublistManager.saveEntrustHsfSublist(entrustHsfSublist);
			renderText(JsonParser.getRowValue(entrustHsfSublist));
			logUtilDao.debugLog("保存",entrustHsfSublist.toString());
		} catch (Exception e) {
			// TODO: handle exception
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	@Action("epmHide")
	public void addFactoryClassify(){
		String fcId =Struts2Utils.getParameter("id");
		String[] fcIds=fcId.split(",");
		for(String id : fcIds){
			EntrustHsfSublist entrustHsfSublist=entrustHsfSublistManager.getEntrustHsfSublist(Long.decode(id));
			String fc = entrustHsfSublist.getEntrustHsf().getFactoryClassify();
			entrustHsfSublist.setFactoryClassify(fc);
			entrustHsfSublistManager.saveEntrustHsfSublist(entrustHsfSublist);
		}
	}
	//创建返回消息
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		String companyName =PropUtils.getProp("companyName");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		Page<EntrustHsfSublist> page = new Page<EntrustHsfSublist>(100000);
		if(weight==3||!"TP".equals(companyName)){
			page = entrustHsfSublistManager.listState(page, "N",null);
		}else{
			page = entrustHsfSublistManager.listState(page, "N",subName);
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService()
				.getExportData(page, "EPM_ENTRUST_HSF_SUBLIST"), "hsf实验委托台帐"));
		return null;
	}
	@Action("update-all")
	@LogInfo(optType="修改")
	public String updateSupplierAll() throws Exception {
		JSONObject result = new JSONObject();
		String type=Struts2Utils.getParameter("type");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		List<EntrustHsfSublist> lists=entrustHsfSublistManager.listOutOrIn(subName);
		for(EntrustHsfSublist list:lists){
			if(type.equals("in")){
				if(list.getTestAfterIn()!=null){
					list.setTestAfter(list.getTestAfterIn());
				}
				if(list.getRemarkIn()!=null){
					list.setRemark(list.getRemarkIn());
				}
				if(list.getSupplierIn()!=null){
					list.setSupplier(list.getSupplierIn());
					list.setSupplierCode(list.getSupplierCodeIn());
				}
			}else{
				if(list.getTestAfterIn()==null||"".equals(list.getTestAfterIn())){
					list.setTestAfterIn(list.getTestAfter());
				}
				if(list.getRemarkIn()==null||"".equals(list.getRemarkIn())){
					list.setRemarkIn(list.getRemark());
				}
				if(list.getTestAfterOut()!=null){
					list.setTestAfter(list.getTestAfterOut());
				}
				if(list.getRemarkOut()!=null){
					list.setRemark(list.getRemarkOut());
				}
				if(list.getSupplierOut()!=null){
					list.setSupplier(list.getSupplierOut());
					list.setSupplierCode(list.getSupplierCodeOut());
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
	public String updateSupplierInput(){
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
		String remark=Struts2Utils.getParameter("remark");
		String supplierNameTarget=Struts2Utils.getParameter("supplierNameTarget");
		String supplierCodeTarget=Struts2Utils.getParameter("supplierCodeTarget");
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			if(id!=null){
				EntrustHsfSublist entrustHsfSublist=entrustHsfSublistManager.getEntrustHsfSublist(Long.valueOf(id));
				if(state.equals("in")&&state!=null&&!"".equals(state)){
					entrustHsfSublist.setTestAfter(testAfter);
					entrustHsfSublist.setTestAfterIn(testAfter);
					entrustHsfSublist.setRemark(remark);
					entrustHsfSublist.setRemarkIn(remark);
					entrustHsfSublist.setSupplier(supplierNameTarget);
					entrustHsfSublist.setSupplierCode(supplierCodeTarget);
					entrustHsfSublist.setSupplierIn(supplierNameTarget);
					entrustHsfSublist.setSupplierCodeIn(supplierCodeTarget);
				}else{
					if(entrustHsfSublist.getTestAfterIn()==null||"".equals(entrustHsfSublist.getTestAfterIn())){
						entrustHsfSublist.setTestAfterIn(entrustHsfSublist.getTestAfter());
					}
					if(entrustHsfSublist.getRemarkIn()==null||"".equals(entrustHsfSublist.getRemarkIn())){
						entrustHsfSublist.setRemarkIn(entrustHsfSublist.getRemark());
					}
					if(entrustHsfSublist.getSupplierIn()==null||"".equals(entrustHsfSublist.getSupplierIn())){
						entrustHsfSublist.setSupplierIn(entrustHsfSublist.getSupplier());
						entrustHsfSublist.setSupplierCodeIn(entrustHsfSublist.getSupplierCode());
					}
					entrustHsfSublist.setTestAfter(testAfter);
					entrustHsfSublist.setTestAfterOut(testAfter);
					entrustHsfSublist.setRemark(remark);
					entrustHsfSublist.setRemarkOut(remark);
					entrustHsfSublist.setSupplier(supplierNameTarget);
					entrustHsfSublist.setSupplierOut(supplierNameTarget);
					entrustHsfSublist.setSupplierCode(supplierCodeTarget);
					entrustHsfSublist.setSupplierCodeOut(supplierCodeTarget);
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
