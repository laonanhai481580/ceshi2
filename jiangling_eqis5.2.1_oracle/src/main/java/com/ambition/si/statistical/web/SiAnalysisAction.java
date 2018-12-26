package com.ambition.si.statistical.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.si.entity.SiCheckInspectionReport;
import com.ambition.si.statistical.service.SiAnalysisManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**
 * 内外部损失率ACTION
 * 
 * @author LPF
 * 
 */
@Namespace("/si/data-acquisition")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/si/data-acquisition", type = "redirectAction") })
public class SiAnalysisAction extends CrudActionSupport<SiCheckInspectionReport> {
	private Logger log = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Page<SiCheckInspectionReport> page;
	private SiCheckInspectionReport siCheckInspectionReport;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Page<SiCheckInspectionReport> getPage() {
		return page;
	}

	public void setPage(Page<SiCheckInspectionReport> page) {
		this.page = page;
	}

	public SiCheckInspectionReport getSiCheckInspectionReport() {
		return siCheckInspectionReport;
	}

	public void setSiCheckInspectionReport(SiCheckInspectionReport siCheckInspectionReport) {
		this.siCheckInspectionReport = siCheckInspectionReport;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public SiAnalysisManager getSiAnalysisManager() {
		return siAnalysisManager;
	}

	public void setSiAnalysisManager(SiAnalysisManager siAnalysisManager) {
		this.siAnalysisManager = siAnalysisManager;
	}

	public LogUtilDao getLogUtilDao() {
		return logUtilDao;
	}

	public void setLogUtilDao(LogUtilDao logUtilDao) {
		this.logUtilDao = logUtilDao;
	}

	private JSONObject params;
	@Autowired
	private SiAnalysisManager siAnalysisManager;

	@Autowired
	private LogUtilDao logUtilDao;



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
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
	}
	@Action("total")
	public String costAll() throws Exception {
		List<Option> processSections = ApiFactory.getSettingService().getOptionsByGroupCode("processSections");
		ActionContext.getContext().put("processSections", processSections);
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		ActionContext.getContext().put("businessUnits", businessUnits);
		return SUCCESS;
	}
	@Action("total-table")
	public String allTable() throws Exception {
		params= CommonUtil1.convertJsonObject(params);
		Map<String,Map<String,Double>> map = siAnalysisManager.getAmountsForAll(params);
		Double stockAmountTotal=0.0,appearanceAmountTotal=0.0,functionAmountTotal=0.0,sizeAmountTotal=0.0;
		Double stockLotAmountTotal=0.0,inspectionLotAmountTotal=0.0,passLotAmountTotal=0.0,rejectLotAmountTotal=0.0,lrrRateTotal=0.0;
		Double appearanceUnAmountTotal=0.0,sizeUnAmountTotal=0.0,functionUnAmountTotal=0.0;
		Double appearanceUnRateTotal=0.0,functionUnRateTotal=0.0,sizeUnRateTotal=0.0;
		for (Map<String,Double> map1 : map.values()) {
			stockAmountTotal+=map1.get("stockAmount");
			appearanceAmountTotal+=map1.get("appearanceAmount");
			functionAmountTotal+=map1.get("functionAmount");
			sizeAmountTotal+=map1.get("sizeAmount");
			stockLotAmountTotal+=map1.get("stockLotAmount");
			inspectionLotAmountTotal+=map1.get("inspectionLotAmount");
			passLotAmountTotal+=map1.get("passLotAmount");
			rejectLotAmountTotal+=map1.get("rejectLotAmount");
			appearanceUnAmountTotal+=map1.get("appearanceUnAmount");
			sizeUnAmountTotal+=map1.get("sizeUnAmount");
			functionUnAmountTotal+=map1.get("functionUnAmount");
		}  
		if(appearanceAmountTotal>0){
			appearanceUnRateTotal=appearanceUnAmountTotal*100/appearanceAmountTotal;
		}
		if(functionAmountTotal>0){
			functionUnRateTotal=functionUnAmountTotal*100/functionAmountTotal;
		}
		if(sizeAmountTotal>0){
			sizeUnRateTotal=sizeUnAmountTotal*100/sizeAmountTotal;
		}
		if(inspectionLotAmountTotal>0){
			lrrRateTotal=rejectLotAmountTotal*100/inspectionLotAmountTotal;
		}
		if(appearanceUnRateTotal>0){
			BigDecimal  a=new  BigDecimal(appearanceUnRateTotal);  
			appearanceUnRateTotal=a.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		}
		if(functionUnRateTotal>0){
			BigDecimal  a=new  BigDecimal(functionUnRateTotal);  
			functionUnRateTotal=a.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		}
		if(sizeUnRateTotal>0){
			BigDecimal  a=new  BigDecimal(sizeUnRateTotal);  
			sizeUnRateTotal=a.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		}
		if(lrrRateTotal>0){
			BigDecimal  b=new  BigDecimal(lrrRateTotal);  
			lrrRateTotal=b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
		}
		Map<String,Double> totalMap=new HashMap<String, Double>();
		totalMap.put("stockAmountTotal", stockAmountTotal);
		totalMap.put("appearanceAmountTotal", appearanceAmountTotal);
		totalMap.put("sizeAmountTotal", sizeAmountTotal);
		totalMap.put("functionAmountTotal", functionAmountTotal);
		totalMap.put("stockLotAmountTotal", stockLotAmountTotal);
		totalMap.put("inspectionLotAmountTotal", inspectionLotAmountTotal);
		totalMap.put("passLotAmountTotal", passLotAmountTotal);
		totalMap.put("rejectLotAmountTotal", rejectLotAmountTotal);
		totalMap.put("lrrRateTotal", lrrRateTotal);
		totalMap.put("appearanceUnAmountTotal", appearanceUnAmountTotal);
		totalMap.put("sizeUnAmountTotal", sizeUnAmountTotal);
		totalMap.put("functionUnAmountTotal", functionUnAmountTotal);
		totalMap.put("appearanceUnRateTotal", appearanceUnRateTotal);
		totalMap.put("functionUnRateTotal", functionUnRateTotal);
		totalMap.put("sizeUnRateTotal", sizeUnRateTotal);
		
		//获取具体不良项目数量
		Map<String,Object> itemValueMap=siAnalysisManager.getItemsForAll(params);
		@SuppressWarnings("unchecked")
		Map<String,Set<String>> itemMap=(Map<String, Set<String>>) itemValueMap.get("itemMap");
		itemValueMap.remove("itemMap");
		ActionContext.getContext().put("dateValueMap",map);
		ActionContext.getContext().put("totalMap",totalMap);
		ActionContext.getContext().put("itemMap",itemMap);
		ActionContext.getContext().put("itemValueMap",itemValueMap);
		return SUCCESS;
	}			
	@Override
	public SiCheckInspectionReport getModel() {
		return siCheckInspectionReport;
	}	

	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}

}
