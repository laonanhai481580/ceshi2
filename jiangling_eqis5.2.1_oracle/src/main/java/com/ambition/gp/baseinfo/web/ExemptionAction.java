package com.ambition.gp.baseinfo.web;

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

import com.ambition.gp.baseinfo.service.ExemptionManager;
import com.ambition.gp.entity.Exemption;
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


@Namespace("/gp/base-info/exemption")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gp/base-info/exemption", type = "redirectAction") })
public class ExemptionAction extends CrudActionSupport<Exemption>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private Exemption exemption;
	private File myFile;
	private Page<Exemption> page;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private ExemptionManager exemptionManager;
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

	public Exemption getExemption() {
		return exemption;
	}

	public void setExemption(Exemption exemption) {
		this.exemption = exemption;
	}

	public Page<Exemption> getPage() {
		return page;
	}

	public void setPage(Page<Exemption> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

		@Override
		public Exemption getModel() {
			// TODO Auto-generated method stub
			return exemption;
		}
		
		@Action("delete")
		@LogInfo(optType="删除",message="删除豁免数据")
		@Override
		public String delete() throws Exception {
			try {
				exemptionManager.deleteExemption(deleteIds);
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
		@LogInfo(optType="查询",message="查询数据")
		public String listDates(){
			try {
					page = exemptionManager.search(page);
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
				exemption = new Exemption();
				exemption.setCompanyId(ContextUtils.getCompanyId());
				exemption.setCreatedTime(new Date());
				exemption.setCreator(ContextUtils.getUserName());
				exemption.setModifiedTime(new Date());
				exemption.setModifier(ContextUtils.getUserName());
				exemption.setBusinessUnitName(ContextUtils.getSubCompanyName());
				exemption.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			}else {
				exemption = exemptionManager.getExemption(id);
			}
		}

		
		@Action("save")
		@LogInfo(optType="保存",message="保存豁免数据")
		@Override
		public String save() throws Exception {
			try {
				exemptionManager.saveExemption(exemption);
				renderText(JsonParser.getRowValue(exemption));
				logUtilDao.debugLog("保存",exemption.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}

		@Action("export")
		@LogInfo(optType="导出",message="豁免清单")
		public String export() throws Exception {
			Page<Exemption> page = new Page<Exemption>(100000);
			page = exemptionManager.list(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GP_EXEMPTION"),"豁免清单"));
			logUtilDao.debugLog("导出", "豁免清单");
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
					renderHtml(exemptionManager.importDatas(myFile));
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
		@LogInfo(optType="下载",message="下载豁免条款模版")
		public String downloadTemplate() throws Exception {
			InputStream inputStream = null;
			try {
				inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/gp-exemption.xls");
				Workbook book = WorkbookFactory.create(inputStream);
				String fileName = "豁免条款模板.xls";
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
}
