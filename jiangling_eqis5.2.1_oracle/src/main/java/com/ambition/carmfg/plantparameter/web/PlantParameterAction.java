package com.ambition.carmfg.plantparameter.web;

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

import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.PlantItem;
import com.ambition.carmfg.entity.PlantParameter;
import com.ambition.carmfg.entity.PlantParameterDetail;
import com.ambition.carmfg.plantparameter.service.PlantItemManager;
import com.ambition.carmfg.plantparameter.service.PlantParameterManager;
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
 * 类名:设备参数Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月3日 发布
 */

@Namespace("/carmfg/plant-parameter")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/plant-parameter", type = "redirectAction") })
public class PlantParameterAction extends BaseAction<PlantParameter>{
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long indicatorId;
	private String deleteIds;//删除的编号 
	private JSONObject params;//检验项目指标对象
	private File myFile;
	private String myFileFileName;//导入文件名称
	private String isSet;//是否设置
	private Page<PlantParameter> page;
	private PlantParameter plantParameter;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private PlantItemManager plantItemManager;
	@Autowired
	private PlantParameterManager plantParameterManager;
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
	public Page<PlantParameter> getPage() {
		return page;
	}
	public void setPage(Page<PlantParameter> page) {
		this.page = page;
	}
	public PlantParameter getPlantParameter() {
		return plantParameter;
	}
	public void setPlantParameter(PlantParameter plantParameter) {
		this.plantParameter = plantParameter;
	}
	
	public Long getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
	
	
	public String getIsSet() {
		return isSet;
	}
	public void setIsSet(String isSet) {
		this.isSet = isSet;
	}
	@Override
	public PlantParameter getModel() {
		return plantParameter;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			plantParameter = new PlantParameter();
			plantParameter.setCreatedTime(new Date());
			plantParameter.setCompanyId(ContextUtils.getCompanyId());
			plantParameter.setCreator(ContextUtils.getUserName());
			plantParameter.setLastModifiedTime(new Date());
			plantParameter.setLastModifier(ContextUtils.getUserName());
			plantParameter.setBusinessUnitName(ContextUtils.getSubCompanyName());
			plantParameter.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			plantParameter = plantParameterManager.getPlantParameter(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@LogInfo(optType="保存",message="设备参数维护")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				plantParameterManager.savePlantParameter(plantParameter);
				logUtilDao.debugLog("保存", plantParameter.toString());
			}else{
				plantParameter.setLastModifiedTime(new Date());
				plantParameter.setLastModifier(ContextUtils.getUserName());
				plantParameterManager.savePlantParameter(plantParameter);
				logUtilDao.debugLog("修改", plantParameter.toString());
			}
			this.renderText(JsonParser.getRowValue(plantParameter));
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="设备参数维护")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				plantParameterManager.deletePlantParameter(deleteIds);
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
		page = plantParameterManager.search(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("import-form")
	public String importForm() throws Exception {
		return SUCCESS;
	}
	
	@Action("imports")
	@LogInfo(optType="导入",message="导入设备参数")
	public String imports() throws Exception {
		try {
			if(myFile != null){
				renderHtml(plantParameterManager.importPlantParameter(myFile,myFileFileName));
			}
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("导入设备参数失败",e);
			}
			renderHtml("<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出",message="导出设备参数")
	public String export() throws Exception {
		try {
			Page<PlantParameter> page = new Page<PlantParameter>(Integer.MAX_VALUE);
			page = plantParameterManager.search(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PLANT_PARAMETER"),"设备参数"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 编辑设备参数
	 * @return
	 * @throws Exception
	 */
	@Action("edit-indicator")
	public String editIndicator() throws Exception {
		return SUCCESS;
	}
	@Action("plant-item-select")
	public String plantItemSelect() throws Exception {
		return SUCCESS;
	}
	/**
	 * 编辑设备参数的数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("edit-indicator-datas")
	public String editIndicatorDatas() throws Exception {
		plantParameter = plantParameterManager.getPlantParameter(indicatorId);
		Page page = new Page();
		if(plantParameter == null){
			renderText(PageUtils.pageToJson(page));
		}else{
			//所有已设置的设备
			List<PlantParameterDetail> details = plantParameterManager.getAllDetail(plantParameter.getId());
			Map<Long,PlantParameterDetail> selfPlantDetailMap = new HashMap<Long, PlantParameterDetail>();
			List<PlantItem> items = new ArrayList<PlantItem>();
			for(PlantParameterDetail detail : details){
				PlantItem item = detail.getPlantItem();
				selfPlantDetailMap.put(item.getId(),detail);
				items.add(item);
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(PlantItem item : items){
				convertPlantItem(item,list,selfPlantDetailMap);
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
	private void convertPlantItem(PlantItem item,List<Map<String,Object>> list,Map<Long,PlantParameterDetail
			> selfPlantDetailMap){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",item.getId());
		PlantParameterDetail detail = selfPlantDetailMap.get(item.getId());
		if(detail != null){
			map.put("itemIndicatorId",detail.getId());
			map.put("params.unit",detail.getUnit());
			map.put("params.plantName",detail.getPlantName());
			map.put("params.parameterName",detail.getParameterName());
			map.put("params.parameterStandard",detail.getParameterStandard());
			map.put("params.levela",detail.getLevela());
			map.put("params.levelb",detail.getLevelb());
			map.put("params.remark",detail.getRemark());
			map.put("canUse","yes");
			map.put("isSet","yes");
		}
		list.add(map);
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载设备参数模板")
	public String downloadTemplate() throws Exception {
		String fileName = "设备参数模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-plant-parameter.xls");
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes,0 ,bytes.length);
		response.getOutputStream().write(bytes);
		inputStream.close();
		return null;
	}
	@Action("download-attach")
	@LogInfo(optType="下载",message="下载设备参数文件")
	public String downloadAttach() throws IOException{
		plantParameterManager.downloadAttachById(Struts2Utils.getResponse(),id);
		return null;
	}
	/**
	 * 保存编辑的设备参数
	 * @return
	 * @throws Exception
	 */
	@Action("save-item")
	@LogInfo(optType="保存",message="保存编辑的设备参数")
	public String saveItem() throws Exception {
		try{
			PlantParameter plantParameter =plantParameterManager.getPlantParameter(indicatorId);
			PlantItem plantItem =plantItemManager.getPlantItem(id);
				plantParameterManager.savePlantDetail(plantParameter, plantItem,params);
				createMessage("操作成功!");
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	/**
	 * 删除的设备信息
	 * @return
	 * @throws Exception
	 */
	@Action("delete-item")
	@LogInfo(optType="删除",message="删除设备信息")
	public String deleteItem() throws Exception {
		try{
			plantParameterManager.deletePlantDetail(indicatorId);
			createMessage("删除成功!");
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("删除失败:" + e.getMessage());
		}
		return null;
	}
	/**
	 * 添加的设备信息
	 * @return
	 * @throws Exception
	 */
	@Action("add-item")
	@LogInfo(optType="添加",message="添加设备信息")
	public String addItem() throws Exception {
		try{
			int addCount = plantParameterManager.addPlantDetail(indicatorId, deleteIds);
			createMessage("操作成功!共添加了"+addCount + "个设备!");
		}catch (Exception e) {
			log.error("添加设备信息失败!",e);
			createErrorMessage("操作失败:" + e.getMessage());
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
