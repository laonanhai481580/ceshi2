package com.ambition.gp.gpmaterial.web;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.ambition.gp.entity.GpMaterialSub;
import com.ambition.gp.gpmaterial.services.GpMaterialSubManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;


@Namespace("/gp/gpmaterial/sub")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gp/gpmaterial/sub", type = "redirectAction") })
public class GpMaterialSubAction extends CrudActionSupport<GpMaterialSub>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GpMaterialSub gpMaterialSub;
	private Page<GpMaterialSub> page;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private GpMaterialSubManager gpMaterialSubManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public GpMaterialSub getGpMaterialSub() {
		return gpMaterialSub;
	}

	public void setGpMaterialSub(GpMaterialSub gpMaterialSub) {
		this.gpMaterialSub = gpMaterialSub;
	}

	public Page<GpMaterialSub> getPage() {
		return page;
	}

	public void setPage(Page<GpMaterialSub> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

		@Override
		public GpMaterialSub getModel() {
			// TODO Auto-generated method stub
			return gpMaterialSub;
		}
		
		@Action("delete")
		@LogInfo(optType="删除",message="删除数据")
		@Override
		public String delete() throws Exception {
			try {
				gpMaterialSubManager.deleteGpMaterialSub(deleteIds);
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
		@LogInfo(optType="数据",message="查看数据")
		public String listDates(){
			try {
				String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
				String code = ContextUtils.getLoginName();
				if("供应商".equals(dept)){
					page = gpMaterialSubManager.listState(page,code);
					renderText(PageUtils.pageToJson(page));
				}else{
					page = gpMaterialSubManager.search(page);
					renderText(PageUtils.pageToJson(page));
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error("台账获取例表失败", e);
			}
			return null;
		}
		@Action("list-product")
		@LogInfo(optType="数据",message="查看数据")
		public String listProducts(){
			try {
				page = gpMaterialSubManager.listPageByParams(page);
				renderText(PageUtils.pageToJson(page));
			} catch (Exception e) {
				// TODO: handle exception
				log.error("台账获取例表失败", e);
			}
			return null;
		}
		
		@Override
		protected void prepareModel() throws Exception {
			// TODO Auto-generated method stub
			if(id == null){
				gpMaterialSub = new GpMaterialSub();
				gpMaterialSub.setCompanyId(ContextUtils.getCompanyId());
				gpMaterialSub.setCreatedTime(new Date());
				gpMaterialSub.setCreator(ContextUtils.getUserName());
				gpMaterialSub.setModifiedTime(new Date());
				gpMaterialSub.setModifier(ContextUtils.getUserName());
				gpMaterialSub.setBusinessUnitName(ContextUtils.getSubCompanyName());
				gpMaterialSub.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			}else {
				gpMaterialSub = gpMaterialSubManager.getGpMaterialSub(id);
			}
		}

		
		@Action("save")
		@LogInfo(optType="保存",message="保存数据")
		@Override
		public String save() throws Exception {
			try {
				String fj = Struts2Utils.getParameter("attachmentFiles");
				String fj1 = Struts2Utils.getParameter("attachmentFiles2");
				if(fj!=null){
					gpMaterialSub.setTestReportFile(fj);
				}
				if(fj1!=null){
					gpMaterialSub.setMsdsFile(fj1);
				}
				Date testDate = gpMaterialSub.getTestReportDate();
				if(testDate!=null){
					gpMaterialSub.setTestReportExpire(getTestReportDate(testDate));
				}
				gpMaterialSubManager.saveGpMaterialSub(gpMaterialSub);
				renderText(JsonParser.getRowValue(gpMaterialSub));
				logUtilDao.debugLog("保存",gpMaterialSub.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}
		@Action("select-input")
		public String averageInput(){
			String isId=Struts2Utils.getParameter("id");
			try {
				List<GpMaterialSub> gpMaterialSubs=null;
				if(isId!=null){
					gpMaterialSubs=gpMaterialSubManager.selectAverageInput(isId);
				}
				if(gpMaterialSubs==null||gpMaterialSubs.size()==0){
					gpMaterialSubs = new ArrayList<GpMaterialSub>();
					gpMaterialSubs.add(new GpMaterialSub());
				}
				ActionContext.getContext().put("_gpMaterialSubs",gpMaterialSubs);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return SUCCESS;
		}
		@Action("isHarmful")
		public String harmful(){
			String eid = Struts2Utils.getParameter("id");
			String type = Struts2Utils.getParameter("type");
			try {
				gpMaterialSubManager.harmful(eid,type);
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("提交失败："+e.getMessage());
				return null;
			}
			return renderText("操作成功");
		}
		@Action("export")
		@LogInfo(optType="导出",message="产品宣告表子台帐")
		public String export() throws Exception {
			Page<GpMaterialSub> page = new Page<GpMaterialSub>(100000);
			page = gpMaterialSubManager.list(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GP_MATERIAL_SUB"),"产品宣告表子台帐"));
			logUtilDao.debugLog("导出", "产品宣告表子台帐");
			return null;
		}
		//创建返回消息
		public void createErrorMessage(String message){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("error",true);
			map.put("message",message);
			renderText(JSONObject.fromObject(map).toString());
		}
		public Date getTestReportDate(Date testReportDate){
			Calendar c = Calendar.getInstance();
			c.setTime(testReportDate);
			c.add(Calendar.DAY_OF_MONTH, 365);
			Date tomorrow = c.getTime();
			return tomorrow;
		}
}
