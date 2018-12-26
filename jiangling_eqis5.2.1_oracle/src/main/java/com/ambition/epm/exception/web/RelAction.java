package com.ambition.epm.exception.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.epm.entity.Rel;
import com.ambition.epm.exception.service.RelManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
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
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:体系维护action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Namespace("/epm/rel")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/epm/rel", type = "redirectAction") })
public class RelAction extends BaseAction<Rel>{
	private static final long serialVersionUID = 1L;
	private Rel rel;
	@Autowired
	private LogUtilDao logUtilDao;
	private File myFile;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	@Autowired
	private AcsUtils acsUtils;
	private Page<Rel> page;
	@Autowired
	private RelManager relManager;
	@Override
	public Rel getModel() {
		return rel;
	}
	@Action("isHarmful")
	@LogInfo(optType="变更")
	public String harmful(){
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		try {
			relManager.isHarmfulDate(eid);
			relManager.harmful(eid,type);
			if("Y".equals(type)){
				rel=relManager.getRel(id);
			}
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "变更数据状态，编号："+eid);
		} catch (Exception e) {
			createErrorMessage("提交失败："+e.getMessage());
			return null;
		}
		return renderText("操作成功");
	}
	@Action("delete")
	@LogInfo(optType="删除",message="REL数据")
	public String delete() throws Exception {
		try {
			relManager.deleteRel(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除体系维护，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除REL数据失败",e);
		}
		return null;
	}
	@Override
	public String input() throws Exception {
		return null;
	}
	
	
	@Action("list")
	@LogInfo(optType="新建",message="新建REL数据")
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			rel=new Rel();
			rel.setCompanyId(ContextUtils.getCompanyId());
			rel.setCreatedTime(new Date());
			rel.setCreator(ContextUtils.getUserName());
			rel.setModifiedTime(new Date());
			rel.setModifier(ContextUtils.getLoginName());
			rel.setModifierName(ContextUtils.getUserName());
			rel.setBusinessUnitName(ContextUtils.getSubCompanyName());
			rel.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			rel=relManager.getRel(id);
		}
		
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存REL数据")
	public String save() throws Exception {
		if(id != null && id != 0){
			rel.setModifiedTime(new Date());
			rel.setModifier(ContextUtils.getLoginName());
			rel.setModifierName(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", rel.toString());
		}else{
			logUtilDao.debugLog("保存", rel.toString());
		}
		try {
			String zb1 = Struts2Utils.getParameter("attachmentFiles");
			rel.setAttachment(zb1);
			relManager.saveRel(rel);
			this.renderText(JsonParser.object2Json(rel));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("REL数据护失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	@LogInfo(optType="查询",message="查询数据")
	public String listDates(){
		String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
		//String userName = ContextUtils.getCompanyName();
		String code = ContextUtils.getLoginName();
		String type = Struts2Utils.getParameter("type");
	//	factorySupply = Struts2Utils.getParameter("factorySupply");
   //	User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
  //	String subName=user.getSubCompanyName();
		try {
//			if("开发部".equals(dept)){
//				page = gpAverageMaterialManager.listState(page,type,null,null,null);
//				renderText(PageUtils.pageToJson(page));
//			}else
				if("供应商".equals(dept)){
				page = relManager.listState(page,type,code,null);
				renderText(PageUtils.pageToJson(page));
			}else{
				page = relManager.listState(page,type,null,null);//,factorySupply
				renderText(PageUtils.pageToJson(page));
			}
		} catch (Exception e) {
			log.error("台账获取例表失败", e);
		}
		return null;
	}
	
	@Action("export")
	@LogInfo(optType="导出", message="REL报告台账")
	public String export() throws Exception {
		try {
			Page<Rel> page = new Page<Rel>(65535);
			page = relManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"REL台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("REL台账",e);
		}
		return null;
	}
	
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(this.myFile != null){
				renderHtml(relManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	  @Action("download-template")
	  @LogInfo(optType="下载", message="下载REL数据导入模板")
	  public String downloadTemplate()
	    throws Exception
	  {
	    InputStream inputStream = null;
	    try {
	      inputStream = getClass().getClassLoader().getResourceAsStream("template/report/rel-template.xlsx");
	      Workbook book = WorkbookFactory.create(inputStream);
	      String fileName = "REL数据导入模板.xls";
	      byte[] byname = fileName.getBytes("gbk");
	      fileName = new String(byname, "8859_1");
	      HttpServletResponse response = Struts2Utils.getResponse();
	      response.reset();
	      response.setContentType("application/vnd.ms-excel");
	      response.setHeader("Content-Disposition", 
	        "attachment; filename=\"" + fileName + "\"");
	      book.write(response.getOutputStream());
	    } catch (Exception e) {
	      this.log.error("下载REL数据导入模板失败!", e);
	    } finally {
	      if (inputStream != null) {
	        inputStream.close();
	      }
	    }
	    return null;
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
	public Page<Rel> getPage() {
		return page;
	}
	public void setPage(Page<Rel> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
}
