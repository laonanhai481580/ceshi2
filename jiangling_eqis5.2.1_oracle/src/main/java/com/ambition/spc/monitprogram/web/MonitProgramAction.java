package com.ambition.spc.monitprogram.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.CPKMoudle;
import com.ambition.spc.entity.MointCpkMoudle;
import com.ambition.spc.entity.MonitPoint;
import com.ambition.spc.entity.MonitProgram;
import com.ambition.spc.entity.MonitQualityFeature;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLResult;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.monitprogram.service.MonitProgramManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * MonitProgramAction.java
 * @authorBy wanglf
 *
 */
@Namespace("/spc/base-info/monitor-program")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/monitor-program", type = "redirectAction") })
public class MonitProgramAction extends com.ambition.product.base.CrudActionSupport<MonitProgram> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private Long structureId;//结构编号
	private String expandIds;//刷新时展开的节点
	private String deleteIds;
	private Page<ProcessPoint> page;
	private MonitProgram monitProgram;
 	@Autowired
	private MonitProgramManager monitProgramManager;
 	@Autowired
	private JlanalyseDrawManager jlanalyseDrawManager;
 	@Autowired
	private QualityFeatureManager qualityFeatureManager;
 	
	@Autowired
	private LogUtilDao logUtilDao;
	
	private File file;
	private String fileFileName;   
	private String fileContentType; 
	private String attachUrl; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getStructureId() {
		return structureId;
	}

	public void setStructureId(Long structureId) {
		this.structureId = structureId;
	}

	public String getExpandIds() {
		return expandIds;
	}

	public void setExpandIds(String expandIds) {
		this.expandIds = expandIds;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Page<ProcessPoint> getPage() {
		return page;
	}

	public void setPage(Page<ProcessPoint> page) {
		this.page = page;
	}

	public MonitProgram getMonitProgram() {
		return monitProgram;
	}

	public void setMonitProgram(MonitProgram monitProgram) {
		this.monitProgram = monitProgram;
	}
	


	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}

	public MonitProgram getModel() {
		return monitProgram;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			monitProgram = new MonitProgram();
			monitProgram.setCreatedTime(new Date());
			monitProgram.setCompanyId(ContextUtils.getCompanyId());
			monitProgram.setCreator(ContextUtils.getUserName());
			monitProgram.setModifiedTime(new Date());
			monitProgram.setModifier(ContextUtils.getUserName());
			if(parentId != null){
				MonitProgram parentmonitProgram = monitProgramManager.getMonitProgram(parentId);
				if(parentmonitProgram != null){
					monitProgram.setParent(parentmonitProgram);
					monitProgram.setLevel(parentmonitProgram.getLevel()+1);
				}
			}
		}else {
			monitProgram = monitProgramManager.getMonitProgram(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		if(id==null&&parentId!=null){
			MonitProgram parentmonitProgram =monitProgramManager.getMonitProgram(parentId);
			if(parentmonitProgram == null){
				addActionMessage("父级物料为空!");
			}else{
				monitProgram.setParent(parentmonitProgram);
			}
		}else if(parentId==null){
			MonitProgram parent = new MonitProgram();
			parent.setName("监控方案");
			monitProgram.setParent(parent);
		}
		if(monitProgram.getId()==null){
			monitProgram.setEditer(ContextUtils.getUserName());
			monitProgram.setEditDate(new Date());
		}
		return SUCCESS;
	}
	
	@Action("program-save")
	@Override
	public String save() throws Exception {
		JSONObject result = new JSONObject();
		try {
			if(id == null){
				MonitProgram parentmonitProgram = null;
				if(parentId != null){
					parentmonitProgram = monitProgramManager.getMonitProgram(parentId);
					monitProgram.setParent(parentmonitProgram);
				}
				if(parentmonitProgram != null){
					monitProgram.setLevel(parentmonitProgram.getLevel() + 1);
				}
				logUtilDao.debugLog("保存", monitProgram.toString());
				monitProgramManager.saveMonitProgram(monitProgram);
			}else{
				if(monitProgram != null){
					monitProgram.setModifiedTime(new Date());
					monitProgram.setModifier(ContextUtils.getUserName());
					logUtilDao.debugLog("修改", monitProgram.toString());
					monitProgramManager.saveMonitProgram(monitProgram);
				}else{
					throw new AmbFrameException("保存监控方案失败,监控方案为空!");
				}
			}	
			result.put("id",monitProgram.getId());
		} catch (Exception e) {
			logger.error("保存失败!",e);
			result.put("error",true);
			result.put("message",e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(id == null){
			createErrorMessage("删除的对象不存在!");
		}else{
			monitProgram = monitProgramManager.getMonitProgram(id);
			try {
				if(!monitProgram.getChildren().isEmpty()){
					createErrorMessage("还有子节点,不能删除!");
				}else{
					monitProgramManager.deleteMonitProgram(String.valueOf(id));
					createMessage("删除成功!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}
	@Action("monit-program-list")
	public String monitProgramList() throws Exception {
		try{
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			List<MonitProgram> monitPrograms = monitProgramManager.getMonitPrograms(parentId);
			if(null != monitPrograms && monitPrograms.size() > 0){
				for(MonitProgram monitProgram : monitPrograms){
					resultList.add(monitProgramManager.convertMonitProgram(monitProgram));
				}
			}
			renderText(JSONArray.fromObject(resultList).toString());
			logUtilDao.debugLog("查询", "SPC:监控方案-监控方案列表");
		}catch(Exception e){
		    e.printStackTrace();	
		}
		return null;
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		listMonitor();
		return SUCCESS;
	}
	
	@Action("monitor-list")
	public String monitorList() throws Exception {
		listMonitor();
		return SUCCESS;
	}
	
	public void listMonitor(){
		monitProgram = monitProgramManager.getFirstLevelMonitProgram();
		List<MonitPoint> monitPoints=new ArrayList<MonitPoint>();
		if(monitProgram!=null){
		 monitPoints = monitProgram.getMonitPoints();
		}
		List<JSONObject> lights = new ArrayList<JSONObject>();
		for(MonitPoint monitPoint : monitPoints){
			JSONObject json = monitProgramManager.convertToJson(monitPoint);
			lights.add(json);
		}
		ActionContext.getContext().put("lights",JSONArray.fromObject(lights).toString());
		ActionContext.getContext().put("monitProgram", monitProgram);
	}
	
	@Action("monitor-program-pic")
	public String monitorProgramPic() throws Exception {
		createMointorPic();
		return SUCCESS;
	}
	
	@Action("look-monitor-program-pic")
	public String lookMonitorProgramPic() throws Exception {
		createMointorPic();
		return SUCCESS;
	}
	
	public void createMointorPic(){
		String monitorProgramId=Struts2Utils.getParameter("monitorProgramId");
		monitProgram = monitProgramManager.getMonitProgram(Long.parseLong(monitorProgramId));
		List<MonitPoint> monitPoints = monitProgram.getMonitPoints();
		List<JSONObject> lights = new ArrayList<JSONObject>();
		for(MonitPoint monitPoint : monitPoints){
			JSONObject json = monitProgramManager.convertToJson(monitPoint);
			lights.add(json);
		}
		ActionContext.getContext().put("lights",JSONArray.fromObject(lights).toString());
		ActionContext.getContext().put("monitProgram", monitProgram);
	}
	
	
	@Action("cpk-table")
	public String cpkTable() throws Exception {
		createCpkTable();
		return SUCCESS;
	}
	
	@Action("look-cpk-table")
	public String lookCpkTable() throws Exception {
		createCpkTable();
		return SUCCESS;
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	public void createCpkTable(){
		String monitorProgramId=Struts2Utils.getParameter("monitorProgramId");
		monitProgram = monitProgramManager.getMonitProgram(Long.parseLong(monitorProgramId));
		List<MonitPoint> monitPoints = monitProgram.getMonitPoints();
		List<JSONObject> lights = new ArrayList<JSONObject>();
		List<MointCpkMoudle> mointCpkMoudles=new ArrayList<MointCpkMoudle>();
		List<JLResult> jLResults=new ArrayList<JLResult>();
		List<CPKMoudle> cpkMoudles=new ArrayList<CPKMoudle>();
		for(MonitPoint monitPoint : monitPoints){
			JSONObject json = monitProgramManager.convertToJson(monitPoint);
			lights.add(json);
			String iscpk=monitPoint.getIsCpk();
			if(iscpk!=null){
			if(iscpk.equals("Y")){
				Calendar startCal = Calendar.getInstance();
				Calendar endCal = Calendar.getInstance();
				Date startDate=startCal.getTime();
				startDate.setHours(0);startDate.setMinutes(0);startDate.setSeconds(0);
				MointCpkMoudle m=new MointCpkMoudle();
				m.setId(monitPoint.getId());
				m.setMyLeft(monitPoint.getMyLeft()+15);
				m.setMyTop(monitPoint.getMyTop());
				m.setImageHeight(monitPoint.getImageHeight());
				m.setImageWidth(monitPoint.getImageWidth());
				for(int i=0;i<monitPoint.getMonitQualityFeature().size();i++){
					MonitQualityFeature monitQualityFeature=monitPoint.getMonitQualityFeature().get(i);
					QualityFeature qualityFeature=qualityFeatureManager.getQualityFeatureById(monitQualityFeature.getQualityFeatureId());
					if(qualityFeature!=null){
					JLcalculator jLcalculator=new JLcalculator();
					JLOriginalData originalData=new JLOriginalData();
					//封装originalDate
					jlanalyseDrawManager.calculatJl(jLcalculator, String.valueOf(qualityFeature.getId()),startDate,startDate,originalData,"analysis","",new JSONArray(),null);
					jLResults.add(jLcalculator.getjLResult());
					cpkMoudles.add(jLcalculator.getjLResult().getCpkMoudle());
					m.setAvg(jLcalculator.getjLResult().getAverage());
					m.setMax(jLcalculator.getjLResult().getMax());
					m.setMin(jLcalculator.getjLResult().getMin());
					m.setCpk(jLcalculator.getjLResult().getCpkMoudle().getCpk());
					m.setQualityFeatureId(qualityFeature.getId());
					mointCpkMoudles.add(m);
					}
				}
				
			}
			}
		}
		ActionContext.getContext().put("lights",JSONArray.fromObject(lights).toString());
		ActionContext.getContext().put("monitProgram", monitProgram);
		ActionContext.getContext().put("mointCpkMoudles", mointCpkMoudles);
		ActionContext.getContext().put("jLResults", jLResults);
		ActionContext.getContext().put("cpkMoudles", cpkMoudles);
	}
	
	
	@Action("list-attachurl")
	public String listAttachUrl() throws Exception {
		monitProgram = monitProgramManager.getMonitProgram(id);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("attachUrl", monitProgram.getAttachUrl());
		renderText(JSONObject.fromObject(result).toString());
		return null;
	}
	@Action("list-datas")
	public String getListDatas() throws Exception {
		return null;
	}
	
	@Action("list-qualityfeature")
	public String listQualityFeature() throws Exception {
		String monitPointId=Struts2Utils.getParameter("monitPointId");
		ActionContext.getContext().put("monitPointId", monitPointId);
		return SUCCESS;
	}
	
	@Action("save-attach")
	public String saveAttach() throws Exception {
		JSONObject result = new JSONObject();
		try {
			attachUrl = Struts2Utils.getParameter("attachUrl");
			monitProgram = monitProgramManager.getMonitProgram(id);
			monitProgram.setCompanyId(ContextUtils.getCompanyId());
			monitProgram.setCreatedTime(new Date());
			monitProgram.setCreator(ContextUtils.getUserName());
			monitProgram.setModifiedTime(new Date());
			monitProgram.setModifier(ContextUtils.getUserName());
			monitProgram.setAttachName(attachUrl);
			monitProgram.setAttachUrl(attachUrl);
			monitProgramManager.saveOnlyMonitProgram(monitProgram);
		} catch (Exception e) {
			logger.error("保存失败!",e);
			result.put("error",true);
			result.put("message",e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	/*private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}*/
	
	
}
