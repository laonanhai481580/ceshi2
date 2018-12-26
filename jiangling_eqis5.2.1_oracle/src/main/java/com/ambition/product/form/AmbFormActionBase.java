package com.ambition.product.form;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.IdEntity;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

import flex.messaging.util.URLDecoder;
/**
 * 类名:普通表单、台帐管理Action基类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-5-16 发布
 */
public abstract class AmbFormActionBase<T extends IdEntity> extends CrudActionSupport<T> {
	protected Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	protected Long id;
	protected String deleteIds;
	protected String formNo;//表单编号
	protected T report;//对象
	private Page<T> page;
	private JSONObject params;
	
	/**
	  * 方法名: 获取Service类
	  * <p>功能说明：</p>
	  * @return
	 */
	protected abstract AmbFormManagerBase<T> getAmbFormBaseManager();
	
	/**
	  * 方法名: 初始化表单元素需要的值
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		
	}
	@Override
	public T getModel() {
		return report;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id!=null){
	    	report = getAmbFormBaseManager().getEntity(id);
	    }else if(id==null){
	    	report = getAmbFormBaseManager().getEntityInstanceClass().newInstance();
	    	report.setCreatedTime(new Date());
	    	report.setCreator(ContextUtils.getLoginName());
	    	report.setCreatorName(ContextUtils.getUserName());
	    	report.setCompanyId(ContextUtils.getCompanyId());
	    	report.setSubCompanyId(ContextUtils.getSubCompanyId());
	    	report.setDepartmentId(ContextUtils.getDepartmentId());
	    }
	}
	
	/**
	 * 删除
	 */
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			getAmbFormBaseManager().deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据成功!");
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			getLog().error("删除失败!", e);
		}
		return null;
	}
	
	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	@LogInfo(optType="新建页面")
	public String input() throws Exception {
		initForm();
		return SUCCESS;
	}
	
	/**
	  * 方法名: 查看详情
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("view-info")
    public String viewInfo()
        throws Exception
    {
		if(id != null){
			report = getAmbFormBaseManager().getEntity(id);
		}else if(StringUtils.isNotEmpty(formNo)){
			report = getAmbFormBaseManager().findReportByFormNo(formNo);
			if(report == null){
	        	addActionMessage("表单编号为["+formNo+"]的单据不存在!");
	        }else{
	        	id = report.getId();
	        }
		}else{
			addActionMessage("表单不存在!");
		}
		initForm();
        ActionContext.getContext().getValueStack().push(report);
        return SUCCESS;
    }
	
	/**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try{
			page = getAmbFormBaseManager().search(page);
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			getLog().error("查询失败!",e);
		}
		return null;
	}
	
	/**
	  * 方法名: 初始化表单元素需要的值
	  * <p>功能说明：</p>
	 */
	public void beforeSaveCallback(){}
	
	/**
	  * 方法名: 保存表单的值
	  * <p>功能说明：</p>
	 */
	@Action("save")
	public String save() throws Exception {
		//设置
		try {
			beforeSaveCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			getAmbFormBaseManager().saveEntity(report,childMaps);
			addActionMessage("保存成功!");
		} catch (Exception e) {
			getLog().error("保存失败!",e);
			addActionMessage("保存失败," + e.getMessage());
			if(id != null){
				report = getAmbFormBaseManager().getEntity(id);
			}
		}
		initForm();
//		String returnurl = Struts2Utils.getParameter("inputformortaskform").equals("inputform")?"input":"process-task";
		return "input";
	}
	
	/**
	  * 方法名: 获取子表信息
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,List<JSONObject>> getChildrenInfos(){
		String _childrenInfos = Struts2Utils.getParameter("_childrenInfos");
		if(StringUtils.isEmpty(_childrenInfos)){
			return null;
		}
		_childrenInfos = URLDecoder.decode(_childrenInfos);
		JSONArray childrenInfos = JSONArray.fromObject(_childrenInfos);
		Map<String,List<JSONObject>> childMaps = new HashMap<String, List<JSONObject>>();
		String flagKey = "__flag";
		for(Object obj : childrenInfos){
			JSONObject childInfo = (JSONObject)obj;
			String flags = childInfo.getString("flags");
			List<JSONObject> items = new ArrayList<JSONObject>();
			String[] flagStrs = flags.split(",");
			Map<String,JSONObject> itemMap = new HashMap<String, JSONObject>();
			for(String flag : flagStrs){
				if(StringUtils.isNotEmpty(flag)&&!itemMap.containsKey(flag)){
					JSONObject json = new JSONObject();
					json.put(flagKey,flag);
					json.put("entityClass",childInfo.getString("entityClass"));
					itemMap.put(flag,json);
					items.add(json);
				}
			}
			@SuppressWarnings("unchecked")
			Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
			for(String key : paramMap.keySet()){
				if(key.indexOf("_")>0){
					String name = key.substring(0,key.indexOf("_"));
					if(itemMap.containsKey(name)){
						String fieldName = key.substring(key.indexOf("_")+1,key.length());
						itemMap.get(name).put(fieldName,Struts2Utils.getParameter(key));
					}
				}
			}
			int size = items.size();
			for(int i=0;i<size;i++){
				JSONObject his = items.get(i);
				items.set(i,itemMap.get(his.getString(flagKey)));
				his.remove(flagKey);
			}
			String childrenFieldName = childInfo.getString("fieldName");//子集的字段名称
			childMaps.put(childrenFieldName,items);
		}
		return childMaps;
	}
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	public String export() throws Exception {
		Page<T> page = new Page<T>(100000);
		page = getAmbFormBaseManager().search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbFormBaseManager().getEntityListCode()),getAmbFormBaseManager().getEntityListName()));
		return null;
	}

	/**
	  * 方法名: 导出
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("export-excel-report")
	@LogInfo(optType="导出",message="导出报告")
	public String exportReportForExcel() throws Exception{
		try {
			getAmbFormBaseManager().exportReport(id);
	    } catch (Exception e) {
	   	   getLog().error("导出失败!",e);
	       renderText("导出失败:" + e.getMessage());
	    }
		return null;
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
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public T getReport() {
		return report;
	}
	public void setReport(T report) {
		this.report = report;
	}
	public Page<T> getPage() {
		return page;
	}
	public void setPage(Page<T> page) {
		this.page = page;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
}

