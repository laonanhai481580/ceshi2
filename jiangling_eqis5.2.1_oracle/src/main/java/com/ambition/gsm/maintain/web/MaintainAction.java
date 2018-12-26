package com.ambition.gsm.maintain.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.epm.entity.EpmOrtItem;
import com.ambition.gsm.entity.Maintain;
import com.ambition.gsm.maintain.service.MaintainManager;
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


@Namespace("/gsm/maintain")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gsm/maintain", type = "redirectAction") })
public class MaintainAction extends CrudActionSupport<Maintain>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private Maintain maintain;
	private Page<Maintain> page;
	@Autowired
	private MaintainManager maintainManager;
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

	public Maintain getMaintain() {
		return maintain;
	}

	public void setMaintain(Maintain maintain) {
		this.maintain = maintain;
	}

	public Page<Maintain> getPage() {
		return page;
	}

	public void setPage(Page<Maintain> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

		@Override
		public Maintain getModel() {
			// TODO Auto-generated method stub
			return maintain;
		}
		
		@Action("delete")
		@LogInfo(optType="删除",message="删除成本数据")
		@Override
		public String delete() throws Exception {
			try {
				maintainManager.deleteMaintain(deleteIds);
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
		@LogInfo(optType="数据",message="成本数据")
		public String listDates(){
			try {
				page = maintainManager.search(page);
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
				maintain = new Maintain();
				maintain.setCompanyId(ContextUtils.getCompanyId());
				maintain.setCreatedTime(new Date());
				maintain.setCreator(ContextUtils.getUserName());
				maintain.setModifiedTime(new Date());
				maintain.setModifier(ContextUtils.getUserName());
				maintain.setBusinessUnitName(ContextUtils.getSubCompanyName());
				maintain.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			}else {
				maintain = maintainManager.getMaintain(id);
			}
		}
		/**
		 * 生成校验计划
		 * @return
		 * @throws Exception
		 */
		@Action("create-man-plan")
		@LogInfo(optType="生成计划",message="计量器具生成校验计划")
		public String createMan() throws Exception {
			try {
				maintainManager.createMan(ids);
	 			createMessage("生成校验成功");
			} catch (Exception e) {
				log.error("生成校验失败：", e);
				addActionError("生成校验失败");
				createErrorMessage("生成校验失败："+e.getMessage()); 
			}
			return null;
		}

		
		@Action("save")
		@LogInfo(optType="保存",message="保存成本数据")
		@Override
		public String save() throws Exception {
			try {
				maintainManager.saveMaintain(maintain);
				renderText(JsonParser.getRowValue(maintain));
				logUtilDao.debugLog("保存",maintain.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}
		@Action("export")
		@LogInfo(optType="导出",message="维修管理台帐")
		public String export() throws Exception {
			Page<Maintain> page = new Page<Maintain>(100000);
			page = maintainManager.list(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GSM_MAINTAIN"),"维修管理台帐"));
			logUtilDao.debugLog("导出", "维修管理台帐");
			return null;
		}
		//创建返回消息
		public void createErrorMessage(String message){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("error",true);
			map.put("message",message);
			renderText(JSONObject.fromObject(map).toString());
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
}
