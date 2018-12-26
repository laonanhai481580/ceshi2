package com.ambition.gsm.base.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.base.service.CheckStandardManager;
import com.ambition.gsm.base.service.GsmCheckItemManager;
import com.ambition.gsm.entity.CheckStandard;
import com.ambition.gsm.entity.CheckStandardDetail;
import com.ambition.gsm.entity.GsmCheckItem;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:检验标准Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年11月24日 发布
 */

@Namespace("/gsm/base/check-standard")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gsm/base/check-standard", type = "redirectAction") })
public class CheckStandardAction extends BaseAction<CheckStandard>{
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long indicatorId;
	private String deleteIds;//删除的编号 
	private JSONObject params;//检验项目指标对象
	private File myFile;
	private String myFileFileName;//导入文件名称
	private String isSet;//是否设置
	private Page<CheckStandard> page;
	private Page<CheckStandardDetail> detailPage;
	private CheckStandard checkStandard;
	private String measurementSpecification;
	private String measurementName;
	private String manufacturer;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmCheckItemManager checkItemManager;
	@Autowired
	private CheckStandardManager checkStandardManager;
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
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getMyFileFileName() {
		return myFileFileName;
	}
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
	public Page<CheckStandard> getPage() {
		return page;
	}
	public void setPage(Page<CheckStandard> page) {
		this.page = page;
	}
	public CheckStandard getCheckStandard() {
		return checkStandard;
	}
	public void setCheckStandard(CheckStandard checkStandard) {
		this.checkStandard = checkStandard;
	}
	
	public Page<CheckStandardDetail> getDetailPage() {
		return detailPage;
	}
	public void setDetailPage(Page<CheckStandardDetail> detailPage) {
		this.detailPage = detailPage;
	}
	public Long getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
		
	public String getMeasurementSpecification() {
		return measurementSpecification;
	}
	public void setMeasurementSpecification(String measurementSpecification) {
		this.measurementSpecification = measurementSpecification;
	}
	public String getMeasurementName() {
		return measurementName;
	}
	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getIsSet() {
		return isSet;
	}
	public void setIsSet(String isSet) {
		this.isSet = isSet;
	}
	@Override
	public CheckStandard getModel() {
		return checkStandard;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			checkStandard = new CheckStandard();
			checkStandard.setCreatedTime(new Date());
			checkStandard.setCompanyId(ContextUtils.getCompanyId());
			checkStandard.setCreator(ContextUtils.getUserName());
			checkStandard.setLastModifiedTime(new Date());
			checkStandard.setLastModifier(ContextUtils.getUserName());
			checkStandard.setBusinessUnitName(ContextUtils.getSubCompanyName());
			checkStandard.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			checkStandard = checkStandardManager.getCheckStandard(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				checkStandardManager.saveCheckStandard(checkStandard);
				logUtilDao.debugLog("保存", checkStandard.toString());
			}else{
				checkStandard.setLastModifiedTime(new Date());
				checkStandard.setLastModifier(ContextUtils.getUserName());
				checkStandardManager.saveCheckStandard(checkStandard);
				logUtilDao.debugLog("修改", checkStandard.toString());
			}
			this.renderText(JsonParser.getRowValue(checkStandard));
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				checkStandardManager.deleteCheckStandard(deleteIds);
				createMessage("删除成功!");
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
	public String getBomListByParent() throws Exception {
		page = checkStandardManager.search(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("import-form")
	public String importForm() throws Exception {
		return SUCCESS;
	}
	
	@Action("imports")
	@LogInfo(optType="导入",message="导入检验标准")
	public String imports() throws Exception {
		try {
			if(myFile != null){
				renderHtml(checkStandardManager.importCheckStandard(myFile,myFileFileName));
			}
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("导入检验标准失败",e);
			}
			renderHtml("<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	@Action("export")
	public String export() throws Exception {
		try {
			Page<CheckStandard> page = new Page<CheckStandard>(Integer.MAX_VALUE);
			page = checkStandardManager.search(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GSM_CHECK_STANDARD"),"检验标准"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 编辑检验标准
	 * @return
	 * @throws Exception
	 */
	@Action("edit-indicator")
	public String editIndicator() throws Exception {
		return SUCCESS;
	}
	@Action("inspection-bom-select")
	public String inspectionBomSelect() throws Exception {
		return SUCCESS;
	}
	/**
	 * 编辑检验标准的数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("edit-indicator-datas")
	public String editIndicatorDatas() throws Exception {
		checkStandard = checkStandardManager.getCheckStandard(indicatorId);
		Page page = new Page();
		if(checkStandard == null){
			renderText(PageUtils.pageToJson(page));
		}else{
			//所有已设置的标准
			List<CheckStandardDetail> details = checkStandardManager.getAllDetail(checkStandard.getId());
			Map<Long,CheckStandardDetail> selfPlantDetailMap = new HashMap<Long, CheckStandardDetail>();
			List<GsmCheckItem> items = new ArrayList<GsmCheckItem>();
			for(CheckStandardDetail detail : details){
				GsmCheckItem item = detail.getCheckItem();
				selfPlantDetailMap.put(item.getId(),detail);
				items.add(item);
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(GsmCheckItem item : items){
				convertGsmCheckItem(item,list,selfPlantDetailMap);
			}
			page.setResult(list);
			renderText(PageUtils.pageToJson(page));
		}
		return null;
	}
	/**
	 * 转换检验项目结构至json对象
	 * @param inspectingItem
	 * @return
	 */
	private void convertGsmCheckItem(GsmCheckItem item,List<Map<String,Object>> list,Map<Long,CheckStandardDetail
			> selfPlantDetailMap){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",item.getId());
		CheckStandardDetail detail = selfPlantDetailMap.get(item.getId());
		if(detail != null){
			map.put("itemIndicatorId",detail.getId());
			map.put("itemName",detail.getItemName());
			map.put("standardValue",detail.getStandardValue());
			map.put("allowableError",detail.getAllowableError());
			map.put("canUse","yes");
			map.put("isSet","yes");
		}
		list.add(map);
	}
	/**
	  * 方法名: 下载检验标准的模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	public String downloadTemplate() throws Exception {
		String fileName = "检验标准模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/gsm-check-standard.xls");
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes,0 ,bytes.length);
		response.getOutputStream().write(bytes);
		inputStream.close();
		return null;
	}
	@Action("download-attach")
	@LogInfo(optType="下载",message="下载检验标准文件")
	public String downloadAttach() throws IOException{
		checkStandardManager.downloadAttachById(Struts2Utils.getResponse(),id);
		return null;
	}
	/**
	 * 保存编辑的检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("save-item")
	public String saveItem() throws Exception {
		try{
			CheckStandard checkStandard =checkStandardManager.getCheckStandard(indicatorId);
			GsmCheckItem checkItem =checkItemManager.getGsmCheckItem(id);
				checkStandardManager.savePlantDetail(checkStandard, checkItem,params);
				createMessage("操作成功!");
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	/**
	 * 删除检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("delete-item")
	public String deleteItem() throws Exception {
		try{
			checkStandardManager.deletePlantDetail(indicatorId);
			createMessage("删除成功!");
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("删除失败:" + e.getMessage());
		}
		return null;
	}
	/**
	 * 添加检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("add-item")
	public String addItem() throws Exception {
		try{
			int addCount = checkStandardManager.addPlantDetail(indicatorId, deleteIds);
			createMessage("操作成功!共添加了"+addCount + "个项目!");
		}catch (Exception e) {
			log.error("添加检验项目失败!",e);
			createErrorMessage("操作失败:" + e.getMessage());
		}
		return null;
	}	
	
	/**
	 * 问题点弹框选择
	 * @param component
	 * @return
	 */
	@Action("check-detail-select")
	public String postionBomSelect() throws Exception {
		String measurementName=Struts2Utils.getParameter("measurementName");
		String measurementSpecification=Struts2Utils.getParameter("measurementSpecification");
		String manufacturer=Struts2Utils.getParameter("manufacturer");
		this.setMeasurementName(measurementName);
		this.setMeasurementSpecification(measurementSpecification);
		this.setManufacturer(manufacturer);
		return SUCCESS;
	}	
	@Action("select-list-datas")
	public String getPositionCodeByParent() throws Exception {
		String measurementName=Struts2Utils.getParameter("measurementName");
		String measurementSpecification=Struts2Utils.getParameter("measurementSpecification");
		String manufacturer=Struts2Utils.getParameter("manufacturer");
		detailPage = checkStandardManager.searchByParams(detailPage,measurementName,measurementSpecification,manufacturer);
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(detailPage.getOrder());
			resultPage.setOrderBy(detailPage.getOrderBy());
			resultPage.setPageNo(detailPage.getPageNo());
			resultPage.setPageSize(detailPage.getPageSize());
			resultPage.setTotalCount(detailPage.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(CheckStandardDetail checkStandardDetail : detailPage.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",checkStandardDetail.getId());
				map.put("itemName",checkStandardDetail.getItemName());
				map.put("standardValue",checkStandardDetail.getStandardValue());
				map.put("allowableError",checkStandardDetail.getAllowableError());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
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
