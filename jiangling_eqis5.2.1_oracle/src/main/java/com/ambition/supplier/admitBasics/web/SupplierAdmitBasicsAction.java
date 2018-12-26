package com.ambition.supplier.admitBasics.web;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.BaseAction;
import com.ambition.supplier.admitBasics.servce.SupplierAdmitBasicsManager;
import com.ambition.supplier.admitBasics.servce.SupplierAdmitClassManager;
import com.ambition.supplier.entity.SupplierAdmitBasics;
import com.ambition.supplier.entity.SupplierAdmitClass;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/supplier/base-info/admit-basics")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/base-info/admit-basics", type = "redirectAction") })
public class SupplierAdmitBasicsAction extends BaseAction<SupplierAdmitBasics>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SupplierAdmitBasics admitBasics;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierAdmitBasicsManager admitBasicsManager;
	private Page<SupplierAdmitBasics> page;
	private List<SupplierAdmitBasics> list;
	private File myFile;
	private Logger log = Logger.getLogger(this.getClass());
 	//不良类别Id
	private Long admitClassId;
	@Autowired
	private SupplierAdmitClassManager admitClassManager;
	private SupplierAdmitClass admitClass;
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

	public SupplierAdmitBasics getSupplierAdmitBasics() {
		return admitBasics;
	}

	public void setSupplierAdmitBasics(SupplierAdmitBasics admitBasics) {
		this.admitBasics = admitBasics;
	}

	public Page<SupplierAdmitBasics> getPage() {
		return page;
	}

	public void setPage(Page<SupplierAdmitBasics> page) {
		this.page = page;
	}

	public List<SupplierAdmitBasics> getList() {
		return list;
	}

	public void setList(List<SupplierAdmitBasics> list) {
		this.list = list;
	}

	public Long getSupplierAdmitClassId() {
		return admitClassId;
	}

	public void setSupplierAdmitClassId(Long admitClassId) {
		this.admitClassId = admitClassId;
	}


	public SupplierAdmitClass getAdmitClass() {
		return admitClass;
	}

	public void setAdmitClass(SupplierAdmitClass admitClass) {
		this.admitClass = admitClass;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	public SupplierAdmitBasics getModel() {
		return admitBasics;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			String myId = Struts2Utils.getParameter("admitClassId");
			
			admitBasics = new SupplierAdmitBasics();
			if(myId != null && !myId.equals("")){
				admitClassId = Long.valueOf(myId);
				admitClass = admitClassManager.getSupplierAdmitClass(admitClassId);
			}else{
				admitClass = null;
			}
			admitBasics.setCompanyId(ContextUtils.getCompanyId());
			admitBasics.setCreatedTime(new Date());
			admitBasics.setCreator(ContextUtils.getUserName());
			admitBasics.setSupplierAdmitClass(admitClass);
			admitBasics.setLastModifiedTime(new Date());
			admitBasics.setLastModifier(ContextUtils.getUserName());
			admitBasics.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			admitBasics = admitBasicsManager.getSupplierAdmitBasics(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存数据")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			admitBasics.setLastModifiedTime(new Date());
			admitBasics.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", admitBasics.toString());
		}else{
			logUtilDao.debugLog("保存", admitBasics.toString());
		}
		try {
			admitBasicsManager.saveSupplierAdmitBasics(admitBasics);
			renderText(JsonParser.object2Json(admitBasics));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="材料文件维护")
	@Override
	public String delete() throws Exception {
		admitBasicsManager.deleteSupplierAdmitBasics(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除",message="材料文件维护")
	public String deleteSubs() throws Exception {
//		String classId=Struts2Utils.getParameter("admitClassId");
//		if(classId!=null){
//			admitClassId=Long.valueOf(classId);
//		}
		if(admitClassId != null && admitClassId != 0){
			admitClass = admitClassManager.getSupplierAdmitClass(admitClassId);
		}else{
			admitClass = null;
		}
		list = admitBasicsManager.listAll(admitClass);
		for(SupplierAdmitBasics admitBasics: list){
			admitBasicsManager.deleteSupplierAdmitBasics(admitBasics);
			//日志消息,方便跟踪删除记录
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除材料文件,【" + admitBasics.getMaterialName() + "】!");
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(admitClassId != null && admitClassId != 0){
			admitClass = admitClassManager.getSupplierAdmitClass(admitClassId);
		}else{
			admitClass = null;
		}
		list = admitBasicsManager.listAll(admitClass);
		if(list.size() >= 1){
			renderText("have");
		}else{
			renderText("no");
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		if(admitClassId != null && admitClassId != 0){
			admitClass = admitClassManager.getSupplierAdmitClass(admitClassId);
		}else{
			admitClass = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("admitClassId");
		if(myId != null && !myId.equals("")){
			admitClassId = Long.valueOf(myId);
			admitClass = admitClassManager.getSupplierAdmitClass(admitClassId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("admitBasics");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = admitBasicsManager.list(page, code);
		}else{
			page = admitBasicsManager.list(page, admitClass);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "供应商质量管理：基础设置-材料文件维护");
		return null;
	}
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String materialSort = Struts2Utils.getParameter("materialSort");
		try {
			if(materialSort!=null){
				admitClass=admitClassManager.getSupplierAdmitClassByCode(materialSort);
				page = admitBasicsManager.getCodeByParams(page, admitClass.getSupplierAdmitClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "供应商质量管理：基础设置-材料文件维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="材料文件维护")
	public String export() throws Exception {
		Page<SupplierAdmitBasics> page = new Page<SupplierAdmitBasics>(100000);
		String myId = Struts2Utils.getParameter("admitClassId");
		if(myId != null && !myId.equals("")){
			admitClassId = Long.valueOf(myId);
			admitClass = admitClassManager.getSupplierAdmitClass(admitClassId);
		}
		page = admitBasicsManager.list(page, admitClass);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_ADMIT_BASICS"),"材料文件维护"));
		logUtilDao.debugLog("导出", "供应商质量管理：基础设置-材料文件维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	
	@Action("import-datas")
	@LogInfo(optType="导入数据",message="材料文件维护")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(admitBasicsManager.importFile(myFile));
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
	@LogInfo(optType="下载",message="下载材料文件模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/epm-ortItem.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "材料文件模板.xls";
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
