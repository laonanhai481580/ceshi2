package com.ambition.carmfg.bom.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
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

import com.ambition.carmfg.bom.service.InspectionBomManager;
import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.carmfg.entity.ProductBomInspection;
import com.ambition.product.BaseAction;
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


@Namespace("/iqc/inspection-base/inspection-bom")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/inspection-base/inspection-bom", type = "redirectAction") })
public class InspectionBomAction extends BaseAction<ProductBomInspection> {

	private static final long serialVersionUID = 1L;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;//删除的BOM编号
	private Page<ProductBomInspection> page;
	private Page<ProductBom> pageBom;
 	@Autowired
	private ProductBomManager productBomManager;			
	private File myFile;
	
	private ProductBomInspection inspectionBom;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
 	@Autowired
	private InspectionBomManager inspectionBomManager;
 	
 	
	public Page<ProductBomInspection> getPage() {
		return page;
	}

	public void setPage(Page<ProductBomInspection> page) {
		this.page = page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
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
	public ProductBomInspection getProductBomInspection() {
		return inspectionBom;
	}
	public void setProductBomInspection(ProductBomInspection inspectionBom) {
		this.inspectionBom = inspectionBom;
	}
	public ProductBomInspection getModel() {
		return inspectionBom;
	}
	

    public Page<ProductBom> getPageBom() {
		return pageBom;
	}

	public void setPageBom(Page<ProductBom> pageBom) {
		this.pageBom = pageBom;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionBom = new ProductBomInspection();
			inspectionBom.setCreatedTime(new Date());
			inspectionBom.setCompanyId(ContextUtils.getCompanyId());
			inspectionBom.setCreator(ContextUtils.getUserName());
			inspectionBom.setLastModifiedTime(new Date());
			inspectionBom.setLastModifier(ContextUtils.getUserName());
			inspectionBom.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionBom.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			inspectionBom = inspectionBomManager.getInspectionBom(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@LogInfo(optType="保存",message="报检物料")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			inspectionBom.setModifiedTime(new Date());
			inspectionBom.setModifier(ContextUtils.getLoginName());
			inspectionBom.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", inspectionBom.toString());
		}else{
			logUtilDao.debugLog("保存", inspectionBom.toString());
		}
		try {
			inspectionBomManager.saveInspectionBom(inspectionBom);
			this.renderText(JsonParser.object2Json(inspectionBom));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存报检物料信息失败  ",e);
		}		
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="报检物料")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				inspectionBomManager.deleteInspectionBom(deleteIds);				
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}		
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = inspectionBomManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "报检物料信息");
		} catch (Exception e) {
			log.error("查询报检物料信息失败  ",e);
		}		
		return null;
	}

	@Action("export")
	@LogInfo(optType="导出",message="报检物料")
	public String exports() throws Exception {
		try {
			Page<ProductBomInspection> page = new Page<ProductBomInspection>(65535);
			page = inspectionBomManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"报检物料台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出报检物料信息失败",e);
		}
		return null;
	}
	@Action("check-item-select")
	public String checkItemSelect() throws Exception {
		return SUCCESS;
	}	
	/**
	 * 添加检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("add-bom")
	public String addItem() throws Exception {
		try{
			int addCount = inspectionBomManager.addBom(deleteIds);
			createMessage("操作成功!共添加了"+addCount + "个物料!");
		}catch (Exception e) {
			log.error("添加物料失败!",e);
			createErrorMessage("操作失败:" + e.getMessage());
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
			if(myFile != null){
				renderHtml(inspectionBomManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载报检物料导入模板
	  * <p>功能说明：下载报检物料导入模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载报检物料导入模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/iqc-inspection-bom-template.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "报检物料导入模板.xls";
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
}
