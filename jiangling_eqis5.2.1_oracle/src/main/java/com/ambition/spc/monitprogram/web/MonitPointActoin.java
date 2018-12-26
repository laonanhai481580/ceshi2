package com.ambition.spc.monitprogram.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.MonitPoint;
import com.ambition.spc.entity.MonitProgram;
import com.ambition.spc.entity.MonitQualityFeature;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.monitprogram.service.MonitPointManager;
import com.ambition.spc.monitprogram.service.MonitProgramManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * MonitProgramAction.java
 * @authorBy wanglf
 *
 */
@Namespace("/spc/base-info/monitor-point")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/monitor-point", type = "redirectAction") })
public class MonitPointActoin extends CrudActionSupport<MonitPoint> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private Long structureId;//结构编号
	private String expandIds;//刷新时展开的节点
	private String deleteIds;
	private Page<MonitPoint> page;
	private MonitPoint monitPoint;
	private JSONObject params;
	@Autowired
	private MonitPointManager monitPointManager;
 	@Autowired
	private MonitProgramManager monitProgramManager;
 	@Autowired
	private QualityFeatureManager qualityFeatureManager;
 	@Autowired
	private AbnormalInfoManager abnormalInfoManager; 
 	@Autowired
	private JlanalyseDrawManager jlanalyseDrawManager;
 	@Autowired
	private LogUtilDao logUtilDao;
	
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

	public Page<MonitPoint> getPage() {
		return page;
	}

	public void setPage(Page<MonitPoint> page) {
		this.page = page;
	}


	public MonitPoint getModel() {
		return monitPoint;
	}
	
	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			monitPoint = new MonitPoint();
			monitPoint.setCreatedTime(new Date());
			monitPoint.setCompanyId(ContextUtils.getCompanyId());
			monitPoint.setCreator(ContextUtils.getUserName());
			monitPoint.setModifiedTime(new Date());
			monitPoint.setModifier(ContextUtils.getUserName());
		}else {
			monitPoint = monitPointManager.getMonitPoint(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	
	@SuppressWarnings("unused")
	@Action("save")
	@Override
	public String save() throws Exception {
		JSONObject result = null;
		try {
			params = convertJsonObject(params);
			String isCpk="N";
			if(params!=null){
				if(params.containsKey("isCpk")){
					isCpk=params.getString("isCpk");
				}
			}
			String monitorProgramId=Struts2Utils.getParameter("monitorProgramId");
			String qualityFeatureIds=Struts2Utils.getParameter("qualityFeatureIds");
			MonitProgram monitProgram =monitProgramManager.getMonitProgram(Long.parseLong(monitorProgramId));
			String type=Struts2Utils.getParameter("type");
			monitPoint.setMonitProgram(monitProgram);
			
			monitPoint.setIsCpk(isCpk);
			monitPointManager.saveMonitPoint(monitPoint);
			//保存质量参数
			Boolean isCpkModel=false;
			if(StringUtils.isNotEmpty(qualityFeatureIds)){
				String[] idss=qualityFeatureIds.split(",");
				List<String> qualityFeatureIdList=new ArrayList<String>();
				for(int i=0;i<idss.length;i++){
					qualityFeatureIdList.add(idss[i]);
				}
				List<MonitQualityFeature> monitQualityFeatures=monitPointManager.listMonitQualityFeature(monitPoint.getId());
				for(int i=0;i<monitQualityFeatures.size();i++){
					for(int j=0;j<qualityFeatureIdList.size();j++){
						String qfId=qualityFeatureIdList.get(j);
						if(qfId.equals(String.valueOf(monitQualityFeatures.get(i).getQualityFeatureId()))){
							qualityFeatureIdList.remove(j);
						}
					}
				}
				if(qualityFeatureIdList.size()>1){
					if(monitPoint.getIsCpk()!=null){
						if(monitPoint.getIsCpk().equals("Y")){
							addActionMessage("显示CPK只能保存一个质量参数");
							isCpkModel=true;
						}
					}
				}
				if(!isCpkModel){
					for(int i=0;i<qualityFeatureIdList.size();i++){
						String id=qualityFeatureIdList.get(i);
						MonitQualityFeature	m=new MonitQualityFeature();
						m.setCreatedTime(new Date());
						m.setCompanyId(ContextUtils.getCompanyId());
						m.setCreator(ContextUtils.getUserName());
						m.setModifiedTime(new Date());
						m.setModifier(ContextUtils.getUserName());
						m.setMonitPoint(monitPoint);
						m.setQualityFeatureId(Long.parseLong(id));
						monitPointManager.saveMonitQualityFeature(m);
					}
				}
			}
			result = monitProgramManager.convertToJson(monitPoint);
		} catch (Exception e) {
			logger.error("监控灯移动失败!",e);
			result.put("error",true);
			result.put("message",e.getMessage());
		}
		this.renderText(result.toString());
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		JSONObject result = new JSONObject();
		try {
			monitPointManager.deleteMonitPoint(deleteIds);
		} catch (Exception e) {
			logger.error("删除失败!",e);
			result.put("error",true);
			result.put("message",e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return null;
	}
	@Action("list-datas")
	public String getListDatas() throws Exception {
		MonitProgram monitProgram =monitProgramManager.getMonitProgram(parentId);
		try {
			page=monitPointManager.listByMonitProgram(page, monitProgram);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "SPC:监控点");
		return null;
	}
	
	@Action("save-qualityfeature")
	public String monitPointId() throws Exception {
		String monitPointId=Struts2Utils.getParameter("monitPointId");
		MonitPoint monitPoint=monitPointManager.getMonitPoint(Long.parseLong(monitPointId));
		String ids=Struts2Utils.getParameter("ids");
		String[] idss=ids.split(",");
		for(int i=0;i<idss.length;i++){
			String id=idss[i];
			MonitQualityFeature	m=new MonitQualityFeature();
			m.setCreatedTime(new Date());
			m.setCompanyId(ContextUtils.getCompanyId());
			m.setCreator(ContextUtils.getUserName());
			m.setModifiedTime(new Date());
			m.setModifier(ContextUtils.getUserName());
			m.setMonitPoint(monitPoint);
			m.setQualityFeatureId(Long.parseLong(id));
			monitPointManager.saveMonitQualityFeature(m);
		}
		return null;
	}
	
	@Action("list-monit-qualityfeature-datas")
	public String getListQualityfeatureDatas() throws Exception {
		String monitPointId =Struts2Utils.getParameter("monitPointId");
		String type=Struts2Utils.getParameter("type");
		if(StringUtils.isEmpty(type)){
			List<MonitQualityFeature> monitQualityFeatures=monitPointManager.listMonitQualityFeature(Long.parseLong(monitPointId));
			List<QualityFeature> qualityFeatures=new ArrayList<QualityFeature>();
			List<Long> monitQualityFeatureIds=new ArrayList<Long>();
			for(int i=0;i<monitQualityFeatures.size();i++){
				QualityFeature qualityFeature=qualityFeatureManager.getQualityFeatureById(monitQualityFeatures.get(i).getQualityFeatureId());
				if(qualityFeature!=null){
				qualityFeatures.add(qualityFeature);
				monitQualityFeatureIds.add(monitQualityFeatures.get(i).getId());
				}
			}
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(qualityFeatures.size());
			List<Object> list = new ArrayList<Object>();
			for(int i=0;i<qualityFeatures.size();i++){
				QualityFeature q=qualityFeatures.get(i);
				Long monitQualityFeatureId=monitQualityFeatureIds.get(i);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",monitQualityFeatureId);
				map.put("name",q.getName());
				map.put("code",q.getCode());
				map.put("paramType", q.getParamType());
				map.put("unit", q.getUnit());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		}else{
		String qualityFeatureIds =Struts2Utils.getParameter("qualityFeatureIds");
		if(StringUtils.isNotEmpty(qualityFeatureIds)){
		String[] qualityFeatureId=qualityFeatureIds.split(",");
		Set<String> qualityFeatureIdList=new HashSet<String>();
		for(int i=0;i<qualityFeatureId.length;i++){
			qualityFeatureIdList.add(qualityFeatureId[i]);
		}
		List<MonitQualityFeature> monitQualityFeatures=new ArrayList<MonitQualityFeature>();
		if(StringUtils.isNotEmpty(monitPointId)){
		 monitQualityFeatures=monitPointManager.listMonitQualityFeature(Long.parseLong(monitPointId));
		}
		for(int i=0;i<monitQualityFeatures.size();i++){
			qualityFeatureIdList.add(String.valueOf(monitQualityFeatures.get(i).getQualityFeatureId()));
		}
		Page<Object> resultPage = new Page<Object>();
		resultPage.setOrder(page.getOrder());
		resultPage.setOrderBy(page.getOrderBy());
		resultPage.setPageNo(page.getPageNo());
		resultPage.setPageSize(page.getPageSize());
		resultPage.setTotalCount(qualityFeatureId.length);
		List<Object> list = new ArrayList<Object>();
		for(int i=0;i<qualityFeatureIdList.size();i++){
			QualityFeature q=qualityFeatureManager.getQualityFeatureById(Long.parseLong((String) qualityFeatureIdList.toArray()[i]));
			if(q!=null){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id",q.getId());
			map.put("name",q.getName());
			map.put("code",q.getCode());
			map.put("paramType", q.getParamType());
			map.put("unit", q.getUnit());
			list.add(map);
			}
		}
		resultPage.setResult(list);
		renderText(PageUtils.pageToJson(resultPage));
		}
		}
		/*try {
			page= qualityFeatureManager.getPageByMoint(page, monitPointId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText(PageUtils.pageToJson(page));*/
		return null;
	}
	
	@Action("delete-monit-quality-feature")
	public String deleteMonitQualityFeature() throws Exception {
		monitPointManager.deleteMonitQualityFeature(deleteIds);
		return null;
	}
	
	@Action("add-monit-point")
	public String addMonitPoint() throws Exception {
		if(id!=null){
			monitPoint = monitPointManager.getMonitPoint(id);
//			//截取日期
//			DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			String value=df1.format(monitPoint.getPointEditDate());
//			monitPoint.setPointEditDate(df.parse(value));
			
			ActionContext.getContext().put("monitorProgramId", monitPoint.getMonitProgram().getId());
			ActionContext.getContext().put("monitPoint", monitPoint);
			ActionContext.getContext().put("pointEditDate", monitPoint.getPointEditDate());//编辑日期格化
			ActionContext.getContext().put("imageWidth", monitPoint.getImageWidth());
			ActionContext.getContext().put("imageHeight", monitPoint.getImageHeight());
			ActionContext.getContext().put("myLeft", monitPoint.getMyLeft());
			ActionContext.getContext().put("myTop", monitPoint.getMyTop());
		}else{
			String monitorProgramId=Struts2Utils.getParameter("monitorProgramId");
			String type=Struts2Utils.getParameter("type");
			String imageWidth=Struts2Utils.getParameter("imageWidth");
			String imageHeight=Struts2Utils.getParameter("imageHeight");
			String myLeft=Struts2Utils.getParameter("myLeft");
			String myTop=Struts2Utils.getParameter("myTop");
			ActionContext.getContext().put("monitorProgramId", monitorProgramId);
			ActionContext.getContext().put("imageWidth", imageWidth);
			ActionContext.getContext().put("imageHeight", imageHeight);
			ActionContext.getContext().put("myLeft", myLeft);
			ActionContext.getContext().put("type", type);
			ActionContext.getContext().put("myTop", myTop);
			
			monitPoint = new MonitPoint();
			monitPoint.setPointEditer(ContextUtils.getUserName());
			monitPoint.setPointEditDate(new Date());
		}
		ActionContext.getContext().getValueStack().push(monitPoint);
		return SUCCESS;
	}
	
	@Action("quality-feature-table")
	public String qualityFeatureTable() throws Exception {
		createQualityFeatureTable();
		return SUCCESS;
	}
	
	@Action("look-quality-feature-table")
	public String lookQualityFeatureTable() throws Exception {
		createQualityFeatureTable();
		return SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	private void createQualityFeatureTable(){
		monitPoint = monitPointManager.getMonitPoint(id);
		List<MonitQualityFeature> monitQualityFeatures=monitPoint.getMonitQualityFeature();
		Calendar startCal = Calendar.getInstance();
		startCal.add(Calendar.HOUR,-24);
		Date startDate=startCal.getTime();
		
		Calendar endCal = Calendar.getInstance();
		Date endDate=endCal.getTime();
		String isCpkModel="N";
		if(monitPoint.getIsCpk()!=null){
			if(monitPoint.getIsCpk().equals("Y")){
				isCpkModel="Y";
			}
		}
		if(isCpkModel.equals("N")){
		//通过异常信息判断灯的颜色
		List<QualityFeature> qualityFeatures=new ArrayList<QualityFeature>();
		List<String> colors=new ArrayList<String>();
		for(int i=0;i<monitQualityFeatures.size();i++){
			MonitQualityFeature m=monitQualityFeatures.get(i);
			QualityFeature qualityFeature=qualityFeatureManager.getQualityFeatureById(m.getQualityFeatureId());
			if(qualityFeature!=null){
				qualityFeatures.add(qualityFeature);
				List<AbnormalInfo> abnormalInfo=abnormalInfoManager.getAbnormalInfo(qualityFeature.getId(), startDate, endDate);//异常信息
				List<AbnormalInfo> handleAbnormalInfo=abnormalInfoManager.getHandleAbnormalInfo(qualityFeature.getId(), startDate, endDate, "2");//产生了异常而且还有异常未处理
//				List<AbnormalInfo> notHandleAbnormalInfo=abnormalInfoManager.getHandleAbnormalInfo(qualityFeature.getId(), startDate, endDate, "1");//产生了异常而且异常已全部处理
//				if(abnormalInfo.size()==0){
//					colors.add("green");
//				}
//				if(notHandleAbnormalInfo.size()>0){
//					colors.add("red");
//				}
//				if(handleAbnormalInfo.size()>0&&notHandleAbnormalInfo.size()==0){
//					colors.add("blue");
//				}
				
				if(abnormalInfo.size()==0){
					colors.add("green");
				}else{
					if(handleAbnormalInfo.size()==0){//有故障但是全部已处理时，为蓝色
						colors.add("blue");
					}else{
						colors.add("red");//未处理时，为红色
					}
				}
			}
		}
		ActionContext.getContext().put("qualityFeatures", qualityFeatures);
		ActionContext.getContext().put("colors", colors);
		ActionContext.getContext().put("isCpkModel", isCpkModel);
		}else{
			for(int i=0;i<monitQualityFeatures.size();i++){
				MonitQualityFeature m=monitQualityFeatures.get(i);
				QualityFeature qualityFeature=qualityFeatureManager.getQualityFeatureById(m.getQualityFeatureId());
				if(qualityFeature!=null){
				JLcalculator jLcalculator=new JLcalculator();
				JLOriginalData originalData=new JLOriginalData();
				//封装originalDate
				jlanalyseDrawManager.calculatJl(jLcalculator, String.valueOf(qualityFeature.getId()),startDate,startDate,originalData,"analysis","",new JSONArray(),null);
				ActionContext.getContext().put("jLResult", jLcalculator.getjLResult());
				ActionContext.getContext().put("cpkMoudle", jLcalculator.getjLResult().getCpkMoudle());
				ActionContext.getContext().put("isCpkModel", isCpkModel);
				}
			}
		}
	}
	
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	
}
