package com.ambition.cost.composingdetail.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.cost.composingdetail.service.ComposingManager;
import com.ambition.cost.entity.Composing;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/cost/composing-detail")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/cost/composing-detail", type = "redirectAction") })
public class ComposingAction extends CrudActionSupport<Composing> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private String expandIds;//刷新时展开的节点
	private String deleteIds;//删除的BOM编号 
	private JSONObject params;
	private Composing composing;
	private Integer totalYear;
	private Integer beginMonth;
	private Integer endMonth;
 	@Autowired
	private ComposingManager composingManager;
 	
// 	@Autowired
//	private QualityComplainManager qualityComplainManager;
 	
 	/*@Autowired
	private DefectiveGoodsProcessingFormManager defectiveGoodsProcessingFormManager;*/
 	
 	@Autowired
 	private LogUtilDao logUtilDao;

	public Composing getComposing() {
		return composing;
	}

	public void setComposing(Composing composing) {
		this.composing = composing;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public String getExpandIds() {
		return expandIds;
	}
	public void setExpandIds(String expandIds) {
		this.expandIds = expandIds;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public Composing getModel() {
		return composing;
	}
	
	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public Integer getTotalYear() {
		return totalYear;
	}

	public void setTotalYear(Integer totalYear) {
		this.totalYear = totalYear;
	}

	public Integer getBeginMonth() {
		return beginMonth;
	}

	public void setBeginMonth(Integer beginMonth) {
		this.beginMonth = beginMonth;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			composing = new Composing();
			composing.setCreatedTime(new Date());
			composing.setCompanyId(ContextUtils.getCompanyId());
			composing.setCreator(ContextUtils.getUserName());
			composing.setModifiedTime(new Date());
			composing.setModifier(ContextUtils.getUserName());
			if(parentId != null){
				Composing parent = composingManager.getComposing(parentId);
				if(parent != null){
					composing.setParent(parent);
					composing.setDengji(parent.getDengji()+1);
				}
			}
		}else {
			composing = composingManager.getComposing(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		if(id==null&&parentId!=null){
			Composing parent = composingManager.getComposing(parentId);
			if(parent == null){
				addActionMessage("上级科目为空!");
			}else{
				composing.setParent(parent);
			}
		}
		if(3 == composing.getDengji()){
			ActionContext.getContext().put("checkDepartments",new ArrayList<Option>());
			ActionContext.getContext().put("cooperateDepartments",new ArrayList<Option>());
		}
		//单位
		List<Option> units = ApiFactory.getSettingService().getOptionsByGroupCode("cost_composing_unit");
		ActionContext.getContext().put("units",units);
		
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存质量成本")
	@Override
	public String save() throws Exception {
		//单位
		List<Option> units = ApiFactory.getSettingService().getOptionsByGroupCode("cost_composing_unit");
		ActionContext.getContext().put("units",units);
		if(id == null){
			try{
				composingManager.saveComposing(composing);
				logUtilDao.debugLog("保存", composing.toString());
				addActionMessage("保存成功!");
				return "input";
			}catch(Exception e){
				addActionMessage("保存失败：" + e.getMessage());
				return "input";
			}
		}else{
			if(composing != null){
				composing.setModifiedTime(new Date());
				composing.setModifier(ContextUtils.getUserName());
				try{
					composingManager.saveComposing(composing);
					logUtilDao.debugLog("修改", composing.toString());
					addActionMessage("保存成功!");
					return "input";
				}catch(Exception e){
					addActionMessage("保存失败：" + e.getMessage());
					return "input";
				}
			}else{
				addActionMessage("保存物料BOM失败,物料BOM为空!");
				return "input";
			}
		}
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="删除质量成本")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				composingManager.deleteComposing(deleteIds);
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
		return SUCCESS;
	}
	@Action("list-view")
    public String listView() throws Exception {
        return SUCCESS;
    }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("list-datas")
	public String getComposingsByParent() throws Exception {
		Page page = new Page();
		Map<String,Boolean> expandMap = new HashMap<String, Boolean>();
		if(StringUtils.isNotEmpty(expandIds)){
			String[] ids = expandIds.split(",");
			for(String id : ids){
				if(StringUtils.isNotEmpty(id)){
					expandMap.put(id,true);
				}
			}
		}
		List<Composing> composingParents = null;
		if(nodeid == null){
			composingParents = composingManager.getTopComposingList("all");
		}else{
			Composing parent = composingManager.getComposing(nodeid);
			if(parent != null){
				composingParents = parent.getChildren();
			}else{
				composingParents = new ArrayList<Composing>();
			}
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Composing composing : composingParents){
			convertComposing(composing,expandMap,list);
		}
		page.setResult(list);
		renderText(PageUtils.pageToJson(page));
//		String result = composingManager.getResultJson(page);
//		renderText(JSONObject.fromObject(result).toString());
		logUtilDao.debugLog("查询", "质量成本管理：质量成本构成");
		return null;
	}
	@Action("cost-trend")
	public String generalCostTrend() throws Exception {
		Calendar calendar = Calendar.getInstance();
		Integer currentYear = calendar.get(Calendar.YEAR);
		if(totalYear == null){
			totalYear = currentYear;
		}
		List<Option> options = new ArrayList<Option>();
		for(int i=currentYear;i>currentYear-5;i--){
			Option option = new Option();
			option.setName(String.valueOf(i));
			option.setValue(String.valueOf(i));
			options.add(option);
		}
		ActionContext.getContext().put("totalYears",options);
		if(beginMonth == null || endMonth == null){
			beginMonth = 1;
			endMonth = 12;
		}
		options = new ArrayList<Option>();
		for(int i=1;i<13;i++){
			Option option = new Option();
			option.setName(String.valueOf(i));
			option.setValue(String.valueOf(i));
			options.add(option);
		}
		ActionContext.getContext().put("totalMonths",options);
		return SUCCESS;
	}
	
	private Integer getYearAndMonthNumber(Calendar calendar){
		StringBuffer sb = new StringBuffer("");
		sb.append(calendar.get(Calendar.YEAR));
		if(calendar.get(Calendar.MONTH)+1<10){
			sb.append("0" + (calendar.get(Calendar.MONTH)+1));
		}else{
			sb.append(calendar.get(Calendar.MONTH)+1);
		}
		return Integer.valueOf(sb.toString());
	}
	
	/*@Action("cost-trend-datas")
	public String generalCostTrendDatas() throws Exception {
		params = composingManager.convertJsonObject(params);
		int beginyear = 0,beginmonth = 0,endyear = 0,endmonth = 0;
		if(params.get("startDate_ge_date")!=null && params.get("endDate_le_date")!=null){
			beginyear = Integer.valueOf(params.get("startDate_ge_date").toString().substring(0, 4));
			beginmonth = Integer.valueOf(params.get("startDate_ge_date").toString().substring(5, 7));
			endyear = Integer.valueOf(params.get("endDate_le_date").toString().substring(0, 4));
			endmonth = Integer.valueOf(params.get("endDate_le_date").toString().substring(5, 7));
		}
		
		Calendar startCal = Calendar.getInstance();
		startCal.set(Calendar.YEAR,beginyear);
		startCal.set(Calendar.MONTH,beginmonth-1);
		startCal.set(Calendar.DATE,1);
		
		Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.YEAR,endyear);
		endCal.set(Calendar.MONTH,endmonth);
		endCal.set(Calendar.DATE,1);
		endCal.add(Calendar.DATE,-1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String customer = null,productSeries = null,modelSpecification = null;
		
		//获取月数
		int endMonthNumber = getYearAndMonthNumber(endCal);
		
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("title", "外部损失成本统计");
		result.put("subtitle", params.get("startDate_ge_date").toString()+"至"+params.get("endDate_le_date").toString());
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("tableHeaderList", categories);
		result.put("yAxisTitle1","外<br/>部<br/>损<br/>失<br/>成<br/>本");
		result.put("yAxisTitle2","内<br/>部<br/>损<br/>失<br/>成<br/>本");
		//外部损失成本
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "外部损失成本");
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		while(getYearAndMonthNumber(startCal)<=endMonthNumber){
			Double returnamounttotal = 0.0,returnamounttotal1 = 0.0;
			Map<String,Object> map = new HashMap<String, Object>();
			Map<String,Object> map1 = new HashMap<String, Object>();
			//横坐标月份的参数
			categories.add(startCal.get(Calendar.MONTH)+1);
			
			Date startdate1 = startCal.getTime();
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(startdate1);
			endCalendar.add(Calendar.MONTH, 1);
			endCalendar.add(Calendar.DATE, -1);
			Date enddate1 = endCalendar.getTime();
			
			map.put("startdate", sdf.format(startdate1));
			map.put("enddate", sdf.format(enddate1));
			map1.put("startdate", sdf.format(startdate1));
			map1.put("enddate", sdf.format(enddate1));
			
			List<QualityComplain> qualityComplain = qualityComplainManager.getComplainAll(sdf.parse(map.get("startdate").toString()),sdf.parse(map.get("enddate").toString()),customer,productSeries,modelSpecification);
			for(int j=0;j < qualityComplain.size();j++) { 
				QualityComplain qualityComplaincount = qualityComplain.get(j);
				for(int k=0; k < qualityComplaincount.getQualityComplainComposingItems().size();k++){
					Double returnamount = qualityComplaincount.getQualityComplainComposingItems().get(k).getValue();
					returnamounttotal = returnamounttotal + returnamount;
				}
			}
			List<DefectiveGoodsProcessingForm> defectiveGoodsProcessingForms = defectiveGoodsProcessingFormManager.listDgpfAll(sdf.parse(map.get("startdate").toString()),sdf.parse(map.get("enddate").toString()));
			for(int j=0;j < defectiveGoodsProcessingForms.size();j++) { 
				DefectiveGoodsProcessingForm dgpf = defectiveGoodsProcessingForms.get(j);
				for(int k=0; k < dgpf.getDefectiveGoodsComposingItems().size();k++){
					Double returnamount = dgpf.getDefectiveGoodsComposingItems().get(k).getValue();
					returnamounttotal1 = returnamounttotal1 + returnamount;
				}
			}
			map.put("y", returnamounttotal);
			map1.put("y", returnamounttotal1);
			map.put("qualityionPoint", "外部");
			map1.put("qualityionPoint", "内部");
			data.add(map);
			data1.add(map1);
			startCal.add(Calendar.MONTH, 1);
		}
		series1.put("data",data);
		result.put("series1", series1);
		//退货数量
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("data",data1);
		series2.put("name", "内部损失成本");
		result.put("series2", series2);
		result.put("max", 100);
		renderText(JSONObject.fromObject(result).toString());
		return null;
	}*/
	
	/**
	 * 损失成本汇总
	 * @return
	 * @throws Exception
	 */
	@Action("cost-total")
	public String total() throws Exception {
		Calendar calendar = Calendar.getInstance();
		Integer currentYear = calendar.get(Calendar.YEAR);
		if(totalYear == null){
			totalYear = currentYear;
		}
		List<Option> options = new ArrayList<Option>();
		for(int i=currentYear-2;i<currentYear+1;i++){
			Option option = new Option();
			option.setName(i+"年");
			option.setValue(""+i);
			options.add(option);
		}
		ActionContext.getContext().put("totalYears",options);
		return SUCCESS;
	}
	
	/**
	 * 损失成本汇总表格
	 * @return
	 * @throws Exception
	 */
	@Action("cost-total-table")
	public String totalTable() throws Exception {
//		ActionContext.getContext().put("totalYearTBoday", qualityComplainManager.totalComposingByYear(totalYear));
		/*ActionContext.getContext().put("totalYearTBoday1", defectiveGoodsProcessingFormManager.totalComposingByYear(totalYear));*/
		return SUCCESS;
	}	
	
	/**
	 * 转换质量成本至json对象
	 * @param composing
	 * @return
	 */
	private void convertComposing(Composing composing,Map<String,Boolean> expandMap,List<Map<String,Object>> list){
		Boolean isLeaf = composing.getChildren()==null||composing.getChildren().isEmpty()?true:false;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",composing.getId());
		map.put("name",composing.getName());
		map.put("code",composing.getCode());
		map.put("remark",composing.getRemark());
		map.put("checkDepartment",composing.getCheckDepartment());
		map.put("cooperateDepartment",composing.getCooperateDepartment());
		map.put("level",composing.getDengji()-1);
		map.put("parent",composing.getParent()==null?"":composing.getParent().getId());
		map.put("isLeaf",isLeaf);
		list.add(map);
		if(!isLeaf){
//			if(expandMap.containsKey(composing.getId().toString())){
				map.put("expanded",true);
				map.put("loaded",true);
				expandMap.remove(composing.getId().toString());
				for(Composing child : composing.getChildren()){
					convertComposing(child, expandMap,list);
				}
//			}else{
//				map.put("expanded",false);
//				map.put("loaded",false);
//			}
		}else{
			map.put("loaded",true);
		}
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
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
