package com.ambition.carmfg.baseinfo.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.InspectionPointManager;
import com.ambition.carmfg.entity.InspectionPoint;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.ContextService;
import com.norteksoft.mms.base.utils.ParseJsonUtil;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;


@SuppressWarnings("deprecation")
@Namespace("/carmfg/base-info/inspection-point")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/base-info/inspection-point", type = "redirectAction") })
public class InspectionPointAction extends BaseAction<InspectionPoint> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private InspectionPoint inspectionPoint;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private InspectionPointManager inspectionPointManager;
	@Autowired
	private ContextService contextService;
	
	private Page<InspectionPoint> page;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public void setPage(Page<InspectionPoint> page) {
		this.page = page;
	}
	
	public Page<InspectionPoint> getPage() {
		return page;
	}
	
	public InspectionPoint getModel() {
		return inspectionPoint;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionPoint=new InspectionPoint();
			inspectionPoint.setCompanyId(contextService.getCompanyId());
			inspectionPoint.setCreatedTime(new Date());
			inspectionPoint.setCreator(contextService.getUserName());
			inspectionPoint.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionPoint.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			
		}else{
			inspectionPoint = inspectionPointManager.getInspectionPoint(id);
		}
			
		/**
		 * TODO检验类别、输入台帐、检验人员的绑定
		 */
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="采集点维护")
	@Override
	public String save() throws Exception {
		try{
			if(id != null && id != 0){
				logUtilDao.debugLog("修改", inspectionPoint.toString());
			}else{
				logUtilDao.debugLog("保存", inspectionPoint.toString());
			}
			if(inspectionPointManager.isSingleInspectionPoint(inspectionPoint.getInspectionPointName(),id)){
				inspectionPointManager.saveInspectionPoint(inspectionPoint);
				this.renderText(ParseJsonUtil.getRowValue(inspectionPoint));
			}else{
				createErrorMessage("检查点名称不允许重复！");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="采集点维护")
	@Override
	public String delete() throws Exception {
		inspectionPointManager.deleteInspectionPoint(deleteIds);
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();//生成菜单
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		page = inspectionPointManager.list(page);
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-采集点维护");
		return null;
	}
	
	
	@Action("export")
	@LogInfo(optType="导出",message="采集点维护")
	public String export() throws Exception {
		Page<InspectionPoint> page = new Page<InspectionPoint>(100000);
		page = inspectionPointManager.list(page);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_INSPECTION_POINT"),"检验数据采集点"));
		logUtilDao.debugLog("导出", "制造质量管理：基础设置-采集点维护");
		return null;
	}
}
