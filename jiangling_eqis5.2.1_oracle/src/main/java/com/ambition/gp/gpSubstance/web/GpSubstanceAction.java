package com.ambition.gp.gpSubstance.web;

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

import com.ambition.gp.averageMaterial.services.GpAverageMaterialManager;
import com.ambition.gp.entity.GpAverageMaterial;
import com.ambition.gp.entity.GpSubstance;
import com.ambition.gp.gpSubstance.services.GpSubstanceManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;


@Namespace("/gp/averageMaterial/sub")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gp/averageMaterial/sub", type = "redirectAction") })
public class GpSubstanceAction extends CrudActionSupport<GpSubstance>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GpSubstance gpSubstance;
	private GpAverageMaterial gpAverageMaterial;
	private Page<GpSubstance> page;
	private String sid;
	private String ratio;//百分比
	@Autowired
	private GpSubstanceManager gpSubstanceManager;
	@Autowired
	private GpAverageMaterialManager gpAverageMaterialManager;
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

	public GpSubstance getGpSubstance() {
		return gpSubstance;
	}

	public void setGpSubstance(GpSubstance gpSubstance) {
		this.gpSubstance = gpSubstance;
	}

	public Page<GpSubstance> getPage() {
		return page;
	}

	public void setPage(Page<GpSubstance> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

		public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public GpAverageMaterial getGpAverageMaterial() {
		return gpAverageMaterial;
	}

	public void setGpAverageMaterial(GpAverageMaterial gpAverageMaterial) {
		this.gpAverageMaterial = gpAverageMaterial;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

		@Override
		public GpSubstance getModel() {
			// TODO Auto-generated method stub
			return gpSubstance;
		}
		
		@Action("delete")
		@LogInfo(optType="删除",message="删除数据")
		@Override
		public String delete() throws Exception {
			try {
				gpSubstanceManager.deleteGpSubstance(deleteIds);
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
			 sid = Struts2Utils.getParameter("sId");
			return SUCCESS;
		}
		
		@Action("list-datas")
		@LogInfo(optType="数据",message="查看数据")
		public String listDates(){
			try {
				String id = Struts2Utils.getParameter("sid");
				page = gpSubstanceManager.search(page,Long.valueOf(id));
				renderText(PageUtils.pageToJson(page));
//				System.out.println(page);
//				System.out.println(renderText(PageUtils.pageToJson(page)));
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
				gpSubstance = new GpSubstance();
				if(sid != null && sid != ""){
					gpAverageMaterial = gpAverageMaterialManager.getGpAverageMaterial(Long.valueOf(sid));
				}else{
					gpAverageMaterial = null;
				}
				gpSubstance.setCompanyId(ContextUtils.getCompanyId());
				gpSubstance.setCreatedTime(new Date());
				gpSubstance.setCreator(ContextUtils.getUserName());
				gpSubstance.setModifiedTime(new Date());
				gpSubstance.setModifier(ContextUtils.getUserName());
				gpSubstance.setBusinessUnitName(ContextUtils.getSubCompanyName());
				gpSubstance.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
				gpSubstance.setGpAverageMaterial(gpAverageMaterial);
			}else {
				gpSubstance = gpSubstanceManager.getGpSubstance(id);
			}
		}

		
		@Action("save")
		@LogInfo(optType="保存",message="保存数据")
		@Override
		public String save() throws Exception {
			try {
				gpSubstanceManager.saveGpSubstance(gpSubstance);
				renderText(JsonParser.getRowValue(gpSubstance));
				logUtilDao.debugLog("保存",gpSubstance.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}
		@Action("saveDate")
		@LogInfo(optType="保存",message="保存数据")
		public String saveDate() throws Exception {
			try {
				String id = Struts2Utils.getParameter("sid");
				
				GpAverageMaterial gpAverageMaterial = gpAverageMaterialManager.getGpAverageMaterial(Long.valueOf(id));
				List<GpSubstance> gpSubstances= gpAverageMaterial.getGpSubstances();
				GpSubstance gpSubstance = new GpSubstance();
				gpSubstance.setSubstanceName("");
				gpSubstances.add(gpSubstance);
//				gpSubstanceManager.saveGpSubstance(gpSubstance);
				gpAverageMaterialManager.saveGpAverageMaterial(gpAverageMaterial);
				renderText(JsonParser.getRowValue(gpSubstance));
				logUtilDao.debugLog("保存",gpSubstance.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}
		@Action("export")
		@LogInfo(optType="导出",message="产品宣告表子台帐")
		public String export() throws Exception {
			Page<GpSubstance> page = new Page<GpSubstance>(100000);
			page = gpSubstanceManager.list(page);
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
