package com.ambition.carmfg.madeinspection.web;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.checkinspection.service.MfgCheckInspectionReportManager;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:检验台帐
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  吴旭明
 * @version 1.00 2016-9-8 发布
 */
@Namespace("/carmfg/inspection/termination-inspection-record")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/inspection/termination-inspection-record", type = "redirectAction") })
public class TerminationMadeInspectionAction extends CrudActionSupport<MfgCheckInspectionReport>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private LogUtilDao logUtilDao;
	private Long id;
	private String deleteIds;
	private MfgCheckInspectionReport mfgCheckInspectionReport;
	@Autowired
	private MfgCheckInspectionReportManager mfgCheckInspectionReportManager;
	private Page<MfgCheckInspectionReport> page;
	
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

	public Page<MfgCheckInspectionReport> getPage() {
		return page;
	}

	public void setPage(Page<MfgCheckInspectionReport> page) {
		this.page = page;
	}
	
	
	/**
	  * 方法名: 获取首检完成单据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("first-list-all")
	public String getTerminationFirList(){
		return SUCCESS;
	}
	
	@Action("patrol-list-all")
	public String getTerminationPatList(){
		return SUCCESS;
	}
	
	@Action("end-list-all")
	public String getTerminationEndList(){
		return SUCCESS;
	}
	
	@Action("un-list")
	public String getTerminationUnList(){
		return SUCCESS;
	}
	@Action("select-product")
	public String selectproduct(){
		return SUCCESS;
	}
	@Action("select-product-datas")
	public String selectproductDatas(){
		try {
			page = mfgCheckInspectionReportManager.listAllProduct(page);
			List<MfgCheckInspectionReport> list=page.getResult();
			Long i=1L;
			for (MfgCheckInspectionReport mfgCheckInspectionReport : list) {
				mfgCheckInspectionReport.setId(i++);
			}
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	  * 方法名: 首检结束完成数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("first-list-all-datas")
	public String getTerminationFirListData(){
		page = mfgCheckInspectionReportManager.listAll(page,InspectionPointTypeEnum.FIRSTINSPECTION);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	/**
	  * 方法名: 巡检结束完成数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("patrol-list-all-datas")
	public String getTerminationPatrolListData(){
		try {
			page = mfgCheckInspectionReportManager.listAll(page,InspectionPointTypeEnum.PATROLINSPECTION);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	  * 方法名: 末检结束完成数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("end-list-all-datas")
	public String getTerminationEndListData(){
		page = mfgCheckInspectionReportManager.listAll(page,InspectionPointTypeEnum.COMPLETEINSPECTION);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	/**
	  * 方法名: 不合格数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("un-list-datas")
	public String getTerminationUnListData(){
		try {
			page = mfgCheckInspectionReportManager.listUnqualifieds(page);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	/**
	  * 方法名: 首检导出
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("export-first-list-all")
	@LogInfo(optType="导出",message="首检导出")
	public String exportFirstList(){
		try{
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.listAll(page,InspectionPointTypeEnum.FIRSTINSPECTION);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_FIRST_INSPECTION_ALL"),"检验报告"));
		}catch(Exception e){
			logUtilDao.debugLog("首检导出失败", e.getMessage());
		}
		return null;
	}
	/**
	  * 方法名: 巡检导出
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("export-patrol-list-all")
	@LogInfo(optType="导出",message="巡检导出")
	public String exportPatrolList(){
		try{
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.listAll(page,InspectionPointTypeEnum.PATROLINSPECTION);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PATROL_INSPECTION_REPORT"),"检验报告"));
		}catch(Exception e){
			logUtilDao.debugLog("首检导出失败", e.getMessage());
		}
		return null;
	}
	/**
	  * 方法名: 末检导出
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("export-end-list-all")
	@LogInfo(optType="导出",message="末检导出")
	public String exportEndList(){
		try{
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.listAll(page,InspectionPointTypeEnum.COMPLETEINSPECTION);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_FIRST_INSPECTION_ALL"),"检验报告"));
		}catch(Exception e){
			logUtilDao.debugLog("首检导出失败", e.getMessage());
		}
		return null;
	}
	
	
	/**
	  * 方法名: 不合格品导出
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("export-un-list-datas")
	@LogInfo(optType="导出",message="不合格品导出")
	public String exportUnList(){
		try{
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.listUnqualifieds(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_INSPECTION_UNQUALIFIED"),"检验报告"));
		}catch(Exception e){
			logUtilDao.debugLog("首检导出失败", e.getMessage());
		}
		return null;
	}
	
	
	@Override
	public MfgCheckInspectionReport getModel() {
		return mfgCheckInspectionReport;
	}
	

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
