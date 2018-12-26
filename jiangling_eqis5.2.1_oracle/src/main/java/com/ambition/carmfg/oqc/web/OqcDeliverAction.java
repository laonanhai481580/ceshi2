package com.ambition.carmfg.oqc.web;

import java.io.File;
import java.io.InputStream;
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

import com.ambition.carmfg.entity.OqcDeliver;
import com.ambition.carmfg.oqc.service.OqcDeliverManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
/**
 * 
 * 类名:OQC出货报告action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Namespace("/carmfg/oqc")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/oqc", type = "redirectAction") })
public class OqcDeliverAction extends BaseAction<OqcDeliver>{

	private static final long serialVersionUID = 1L;
	private OqcDeliver oqcDeliver;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<OqcDeliver> page;
	private File myFile;
	private String businessUnit;//所属事业部
	@Autowired
	private OqcDeliverManager oqcDeliverManager;
	@Override
	public OqcDeliver getModel() {
		return oqcDeliver;
	}
	@Action("deliver-delete")
	@LogInfo(optType="删除",message="OQC出货报告")
	public String delete() throws Exception {
		try {
			String[] ids = deleteIds.split(",");
			int allCount=ids.length;
			int delCount=oqcDeliverManager.deleteOqcDeliver(deleteIds);
			createMessage("操作成功!共删除了"+delCount + "条数据，"+(allCount-delCount)+"条数据没有权限删除！");
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除OQC出货报告失败",e);
		}
		return null;
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}	
	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("deliver-list")
	public String list() throws Exception {
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			oqcDeliver=new OqcDeliver();
			oqcDeliver.setCompanyId(ContextUtils.getCompanyId());
			oqcDeliver.setCreatedTime(new Date());
			oqcDeliver.setCreator(ContextUtils.getLoginName());
			oqcDeliver.setModifiedTime(new Date());
			oqcDeliver.setModifier(ContextUtils.getLoginName());
			oqcDeliver.setModifierName(ContextUtils.getUserName());
			oqcDeliver.setBusinessUnitName(ContextUtils.getSubCompanyName());
			oqcDeliver.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			oqcDeliver=oqcDeliverManager.getOqcDeliver(id);
		}
		
	}
	
	@Action("deliver-save")
	@LogInfo(optType="保存",message="保存OQC出货报告")
	public String save() throws Exception {
		if(id != null && id != 0){
			oqcDeliver.setModifiedTime(new Date());
			oqcDeliver.setModifier(ContextUtils.getLoginName());
			oqcDeliver.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", oqcDeliver.toString());
		}else{
			logUtilDao.debugLog("保存", oqcDeliver.toString());
		}
		try {
			String zb1 = Struts2Utils.getParameter("attachmentFiles");
			oqcDeliver.setAttachment(zb1);
			oqcDeliver.setBusinessUnitName(businessUnit);
			oqcDeliverManager.saveOqcDeliver(oqcDeliver);
			this.renderText(JsonParser.object2Json(oqcDeliver));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存OQC出货报告失败  ",e);
		}		
		return null;
	}
	@Action("deliver-list-datas")
	public String listDatas() throws Exception {
		try {
			page = oqcDeliverManager.search(page,businessUnit);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "OQC出货报告");
		} catch (Exception e) {
			log.error("查询OQC出货报告失败  ",e);
		}		
		return null;
	}
	@Action("deliver-export")
	public String export() throws Exception {
		try {
			Page<OqcDeliver> page = new Page<OqcDeliver>(65535);
			page = oqcDeliverManager.search(page,businessUnit);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"OQC出货报告台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出OQC出货报告失败",e);
		}
		return null;
	}
	@Action("deliver-import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("deliver-import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				//String businessUnit = Struts2Utils.getParameter("businessUnit");
				renderHtml(oqcDeliverManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	/**
	 * 
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
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
	public Page<OqcDeliver> getPage() {
		return page;
	}
	public void setPage(Page<OqcDeliver> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}		
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-deliver")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-oqc-deliver.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "OQC出货台账导入模板.xls";
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
}
