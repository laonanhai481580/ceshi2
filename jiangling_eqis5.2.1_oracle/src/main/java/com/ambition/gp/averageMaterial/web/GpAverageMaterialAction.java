package com.ambition.gp.averageMaterial.web;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gp.averageMaterial.services.GpAverageMaterialManager;
import com.ambition.gp.entity.GpAverageMaterial;
import com.ambition.gp.entity.GpSubstance;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;



@Namespace("/gp/averageMaterial")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gp/averageMaterial", type = "redirectAction") })
public class GpAverageMaterialAction extends CrudActionSupport<GpAverageMaterial>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GpAverageMaterial gpAverageMaterial;
	private Page<GpAverageMaterial> page;
	private File myFile;
	private String isHarmful;
	private String factorySupply;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private GpAverageMaterialManager gpAverageMaterialManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	private GridColumnInfo gpSubstanceGridColumnInfo;

 	private List<DynamicColumnDefinition> dynamicColumnDefinitions = new ArrayList<DynamicColumnDefinition>();
	
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

	public GpAverageMaterial getGpAverageMaterial() {
		return gpAverageMaterial;
	}

	public void setGpAverageMaterial(GpAverageMaterial gpAverageMaterial) {
		this.gpAverageMaterial = gpAverageMaterial;
	}

	public Page<GpAverageMaterial> getPage() {
		return page;
	}

	public void setPage(Page<GpAverageMaterial> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	

	public GridColumnInfo getGpSubstanceGridColumnInfo() {
		return gpSubstanceGridColumnInfo;
	}

	public void setGpSubstanceGridColumnInfo(
			GridColumnInfo gpSubstanceGridColumnInfo) {
		this.gpSubstanceGridColumnInfo = gpSubstanceGridColumnInfo;
	}

	public List<DynamicColumnDefinition> getDynamicColumnDefinitions() {
		return dynamicColumnDefinitions;
	}

	public void setDynamicColumnDefinitions(
			List<DynamicColumnDefinition> dynamicColumnDefinitions) {
		this.dynamicColumnDefinitions = dynamicColumnDefinitions;
	}
	
	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getIsHarmful() {
		return isHarmful;
	}

	public void setIsHarmful(String isHarmful) {
		this.isHarmful = isHarmful;
	}
	
		public String getFactorySupply() {
		return factorySupply;
	}

	public void setFactorySupply(String factorySupply) {
		this.factorySupply = factorySupply;
	}

		@Override
		public GpAverageMaterial getModel() {
			// TODO Auto-generated method stub
			return gpAverageMaterial;
		}
		
		@Action("delete")
		@LogInfo(optType="删除",message="删除均质材料数据")
		@Override
		public String delete() throws Exception {
			try {
				gpAverageMaterialManager.deleteGpAverageMaterial(deleteIds);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据，编号："+deleteIds);
			} catch (Exception e) {
				// TODO: handle exception
				renderText("删除失败:" + e.getMessage());
				log.error("删除数据信息失败",e);
			}
			return null;
		}
		@Action("input")
		@Override
		public String input() throws Exception {
			// TODO Auto-generated method stub
			try {
				String eid = Struts2Utils.getParameter("id");
				GpAverageMaterial gpAverageMaterial = gpAverageMaterialManager.getGpAverageMaterial(Long.valueOf(eid));
				List<GpSubstance> gpSubstances= gpAverageMaterial.getGpSubstances();
				if(gpSubstances == null||gpSubstances.size()==0){
					gpSubstances = new ArrayList<GpSubstance>();
					GpSubstance item = new GpSubstance();
					gpSubstances.add(item);
				}
				ActionContext.getContext().put("_gpSubstances", gpSubstances);
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e);
			}
			return SUCCESS;
		}
		@Action("list")
		@Override
		public String list() throws Exception {
			// TODO Auto-generated method stub
			return SUCCESS;
		}
		@Action("list-pending")
		public String listPending() throws Exception {
			// TODO Auto-generated method stub
			factorySupply = Struts2Utils.getParameter("factorySupply");
			List<Option> factorySupplys = ApiFactory.getSettingService().getOptionsByGroupCode("gp_factorySupply");
			if(StringUtils.isEmpty(factorySupply)){
				if(factorySupplys.size()>0){
					factorySupply = factorySupplys.get(0).getValue();
				}
			}
			ActionContext.getContext().put("factorySupplys",factorySupplys);
			return SUCCESS;
		}
		@Action("list-ok")
		public String listOk() throws Exception {
			// TODO Auto-generated method stub
			return SUCCESS;
		}
		@Action("list-qualified")
		public String listQualified() throws Exception {
			// TODO Auto-generated method stub
			return SUCCESS;
		}
		@Action("list-datas")
		@LogInfo(optType="查询",message="查询数据")
		public String listDates(){
			String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
			String userName = ContextUtils.getCompanyName();
			String code = ContextUtils.getLoginName();
			String type = Struts2Utils.getParameter("type");
			factorySupply = Struts2Utils.getParameter("factorySupply");
//			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
//			String subName=user.getSubCompanyName();
			try {
//				if("开发部".equals(dept)){
//					page = gpAverageMaterialManager.listState(page,type,null,null,null);
//					renderText(PageUtils.pageToJson(page));
//				}else
					if("供应商".equals(dept)){
					page = gpAverageMaterialManager.listState(page,type,code,null,null);
					renderText(PageUtils.pageToJson(page));
				}else{
					page = gpAverageMaterialManager.listState(page,type,null,null,factorySupply);
					renderText(PageUtils.pageToJson(page));
				}
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
				gpAverageMaterial = new GpAverageMaterial();
				gpAverageMaterial.setCompanyId(ContextUtils.getCompanyId());
				gpAverageMaterial.setCreatedTime(new Date());
				gpAverageMaterial.setCreator(ContextUtils.getUserName());
				gpAverageMaterial.setModifiedTime(new Date());
				gpAverageMaterial.setModifier(ContextUtils.getUserName());
				gpAverageMaterial.setBusinessUnitName(ContextUtils.getCompanyName());
				gpAverageMaterial.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
				
			}else {
				gpAverageMaterial = gpAverageMaterialManager.getGpAverageMaterial(id);
			}
		}
		
		@Action("save")
		@LogInfo(optType="保存",message="保存均质材料数据")
		@Override
		public String save() throws Exception {
			try {
				String fj = Struts2Utils.getParameter("attachmentFiles");
				String fj1 = Struts2Utils.getParameter("attachmentFiles2");
				String fj2 = Struts2Utils.getParameter("attachmentFiles3");
				String supplierName = ContextUtils.getUserName();
				String supplierCode = ContextUtils.getLoginName();
				String supplierEmail = ContextUtils.getEmail();
				if(fj!=null){
					gpAverageMaterial.setTestReportFile(fj);
				}
				if(fj1!=null){
					gpAverageMaterial.setMsdsFile(fj1);
				}
				if(fj2!=null){
					gpAverageMaterial.setSubstancesFile(fj2);
				}
				Date testDate = gpAverageMaterial.getTestReportDate();
				if(testDate!=null){
					gpAverageMaterial.setTestReportExpire(getTestReportDate(testDate));
				}
				gpAverageMaterial.setAverageMaterialName(gpAverageMaterial.getAverageMaterialName().trim());
				gpAverageMaterial.setAverageMaterialModel(gpAverageMaterial.getAverageMaterialModel().trim());
				gpAverageMaterial.setManufacturer(gpAverageMaterial.getManufacturer().trim());
				if(gpAverageMaterial.getSupplierCode()==null){
					if(gpAverageMaterialManager.findMaterial(gpAverageMaterial)){
						createErrorMessage("保存失败：已有相同数据");
						return null;
					}
					gpAverageMaterial.setSupplierName(supplierName);
					gpAverageMaterial.setSupplierCode(supplierCode);
					if(gpAverageMaterial.getSupplierEmail()==null){
						gpAverageMaterial.setSupplierEmail(supplierEmail);
					}
					gpAverageMaterial.setTaskProgress(GpAverageMaterial.STATE_SUBMIT);
				}
				gpAverageMaterialManager.saveGpAverageMaterial(gpAverageMaterial);
				renderText(JsonParser.getRowValue(gpAverageMaterial));
				logUtilDao.debugLog("保存",gpAverageMaterial.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}

		@Action("isHarmful")
		@LogInfo(optType="变更")
		public String harmful(){
			String eid = Struts2Utils.getParameter("id");
			String type = Struts2Utils.getParameter("type");
			try {
				gpAverageMaterialManager.isHarmfulDate(eid);
				gpAverageMaterialManager.harmful(eid,type);
				if("Y".equals(type)){
					gpAverageMaterial=gpAverageMaterialManager.getGpAverageMaterial(id);
					gpAverageMaterialManager.setUpdateStatus(gpAverageMaterial);
				}
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "变更数据状态，编号："+eid);
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("提交失败："+e.getMessage());
				return null;
			}
			return renderText("操作成功");
		}
		@Action("export")
		@LogInfo(optType="导出",message="均值材料台帐")
		public String export() throws Exception {
			Page<GpAverageMaterial> page = new Page<GpAverageMaterial>(100000);
			String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
			String code = ContextUtils.getLoginName();
			String type = Struts2Utils.getParameter("type");
			factorySupply = Struts2Utils.getParameter("factorySupply");
			if("开发部".equals(dept)){
				page = gpAverageMaterialManager.listState(page,type,null,null,null);
			}else if("供应商".equals(dept)){
				page = gpAverageMaterialManager.listState(page,type,code,null,null);
			}else{
				page = gpAverageMaterialManager.listState(page,type,null,null,null);
			}
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GP_AVERAGE_MATERIAL"),"均值材料台帐"));
			logUtilDao.debugLog("导出", "均值材料台帐");
			return null;
		}
		@Action("input-s")
		public String inputsub() throws Exception {
			getCertificateDatas();
			return SUCCESS;
		}
		/**
		 * 管控物质
		 * @return
		 * @throws Exception
		 */
		@Action("gpSubstance-datas")
		public void getCertificateDatas() throws Exception {
			String eid = Struts2Utils.getParameter("id");
			GpAverageMaterial gpAverageMaterial = gpAverageMaterialManager.getGpAverageMaterial(Long.valueOf(eid));
			List<GpSubstance> gpSubstances= gpAverageMaterial.getGpSubstances();
			if(gpSubstances == null||gpSubstances.size()==0){
				gpSubstances = new ArrayList<GpSubstance>();
				GpSubstance item = new GpSubstance();
				gpSubstances.add(item);
			}
			ActionContext.getContext().put("_gpSubstances", gpSubstances);
		}
		
		@Action("save-sub")
		public String saveSub() throws Exception{
			try {
				String eid = Struts2Utils.getParameter("id");
				String zb = Struts2Utils.getParameter("zibiao");
				GpAverageMaterial gpAverageMaterial = gpAverageMaterialManager.getGpAverageMaterial(Long.valueOf(eid));
				gpAverageMaterialManager.saveEntity(gpAverageMaterial, zb);
				renderText("操作成功");
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
				renderText("操作失败");
			}
			input();
			return "input";
		}
		@Action("import")
		public String imports() throws Exception {
			return SUCCESS;
		}
		
		@Action("import-datas")
		public String importDatas() throws Exception {
			try {
				if(myFile != null){
					renderHtml(gpAverageMaterialManager.importDatas(myFile));
				}
			} catch (Exception e) {
				renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
			}
			return null;
		}
		/**
		  * 方法名: 下载检设备参数模板
		  * <p>功能说明：下载检验标准的模板</p>
		  * @return
		  * @throws Exception
		 */
		@Action("download-template")
		@LogInfo(optType="下载",message="下载均值材料模板")
		public String downloadTemplate() throws Exception {
			InputStream inputStream = null;
			try {
				inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/gp-average.xlsx");
				Workbook book = WorkbookFactory.create(inputStream);
				String fileName = "均值材料模板.xls";
				byte byname[] = fileName.getBytes("gbk");
				fileName = new String(byname, "8859_1");
				HttpServletResponse response = Struts2Utils.getResponse();
				response.reset();
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", (new StringBuilder(
						"attachment; filename=\"")).append(fileName).append("\"")
						.toString());
				book.write(response.getOutputStream());
			}catch (Exception e) {
				log.error("导出失败!",e);
			} finally{
				if(inputStream != null){
					inputStream.close();
				}
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
		public Date getTestReportDate(Date testReportDate){
			Calendar c = Calendar.getInstance();
			c.setTime(testReportDate);
			c.add(Calendar.DAY_OF_MONTH, 365);
			Date tomorrow = c.getTime();
			return tomorrow;
		}
		@Action("select-average-update")
		@LogInfo(optType="更新",message="更新均质材料过期状态")
		public String selectAverageUpdate(){
			JSONObject result = new JSONObject();
			String eid = Struts2Utils.getParameter("id");
			String[] ids = eid.split(",");
			for(String id : ids){
				GpAverageMaterial gpAverage=gpAverageMaterialManager.getGpAverageMaterial(Long.valueOf(id));
				gpAverageMaterialManager.setUpdateStatus(gpAverage);
			}
			try{			
				result.put("message", "更新成功!");
				result.put("error", false);
			}catch(Exception e){
				result.put("error", true);
				result.put("message","更新失败"+ e.getMessage());
				e.printStackTrace();
			}
			renderText(result.toString());
			return null;
		}
}
