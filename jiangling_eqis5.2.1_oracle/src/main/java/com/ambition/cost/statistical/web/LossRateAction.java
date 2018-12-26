package com.ambition.cost.statistical.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.cost.composingdetail.service.ComposingManager;
import com.ambition.cost.entity.Composing;
import com.ambition.cost.entity.CostRecord;
import com.ambition.cost.statistical.service.LossRateManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 内外部损失率ACTION
 * 
 * @author LPF
 * 
 */
@Namespace("/cost/statistical-analysis")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/cost/statistical-analysis", type = "redirectAction") })
public class LossRateAction extends CrudActionSupport<CostRecord> {
	private Logger log = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Page<CostRecord> page;
	private Page<Composing> page3;
	private CostRecord costRecord;
	@Autowired
	private ComposingManager composingManager;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	
	public Page<Composing> getPage3() {
		return page3;
	}

	public void setPage3(Page<Composing> page3) {
		this.page3 = page3;
	}



	public Page<CostRecord> getPage() {
		return page;
	}

	public void setPage(Page<CostRecord> page) {
		this.page = page;
	}

	public CostRecord getCostRecord() {
		return costRecord;
	}

	public void setCostRecord(CostRecord costRecord) {
		this.costRecord = costRecord;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public LossRateManager getLossRateManager() {
		return lossRateManager;
	}

	public void setLossRateManager(LossRateManager lossRateManager) {
		this.lossRateManager = lossRateManager;
	}

	public LogUtilDao getLogUtilDao() {
		return logUtilDao;
	}

	public void setLogUtilDao(LogUtilDao logUtilDao) {
		this.logUtilDao = logUtilDao;
	}

	private JSONObject params;
	@Autowired
	private LossRateManager lossRateManager;

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
	
	/**
	 * 损失成本汇总
	 * @return
	 * @throws Exception
	 */
	@Action("cost-total")
	public String total() throws Exception {
//		UpdateTimestamp updateTimestamp = updateTimestampManager.getUpdateTimestamp(CacheCostIntegrationService.CACHE_COST_UPDATE_TABLE_NAME);
//		if(updateTimestamp!=null){
//			ActionContext.getContext().put("updateDate",DateUtil.formateTimeStr(updateTimestamp.getLastModifiedTime()));
//		}
//		params= convertJsonObject(params);
//		int startyear = Integer.valueOf(params.get("startDate").toString().substring(0, 4));
//		int startmonth = Integer.valueOf(params.get("startDate").toString().substring(5, 7));
//	    int startdate =Integer.valueOf(params.get("startDate").toString().substring(8, 10));
//		int endyear = Integer.valueOf(params.get("endDate").toString().substring(0, 4));
//        int  endmonth = Integer.valueOf(params.get("endDate").toString().substring(5, 7));
//        int enddate = Integer.valueOf(params.get("endDate").toString().substring(8,10));
//        startTime = startyear+"-"+startmonth+"-"+startdate;
//        endTime = endyear+"-"+endmonth+"-"+enddate;
//       System.out.println(startTime);
//       System.out.println(endTime);
//       ActionContext.getContext().put("startTime",startTime);
//       ActionContext.getContext().put("endTime",endTime);
        
		
		
//		Calendar calendar = Calendar.getInstance(); 
//		Integer currentYear = calendar.get(Calendar.YEAR);
//		if(totalYear == null){
//			totalYear = currentYear;
//		}
//		List<Option> options = new ArrayList<Option>();
//		for(int i=currentYear-2;i<currentYear+1;i++){
//			Option option = new Option();
//			option.setName(i+"年");
//			option.setValue(""+i);
//			options.add(option);
//		}
//		ActionContext.getContext().put("totalYears",options);
		ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		List<Option> companyNames = ApiFactory.getSettingService().getOptionsByGroupCode("subcompanys");
	    ActionContext.getContext().put("companyNames",companyNames);
	    ActionContext.getContext().put("dutyUnits",ApiFactory.getSettingService().getOptionsByGroupCode("complain_duty_department"));
		ActionContext.getContext().put("costStatuss",ApiFactory.getSettingService().getOptionsByGroupCode("complain_cost_status"));
		return SUCCESS;
	}
	
	/**
	 * 损失成本汇总表格
	 * @return
	 * @throws Exception
	 */
	@Action("cost-total-table")
	public String totalTable() throws Exception {
		params= CommonUtil1.convertJsonObject(params);
		List<Map<String,Object>> composings = composingManager.getComposingsForTotal(params);
		ActionContext.getContext().put("composings",composings);
		return SUCCESS;
	}	
	@Action("cost-all")
	public String costAll() throws Exception {
		ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		List<Option> companyNames = ApiFactory.getSettingService().getOptionsByGroupCode("subcompanys");
	    ActionContext.getContext().put("companyNames",companyNames);
	    ActionContext.getContext().put("dutyUnits",ApiFactory.getSettingService().getOptionsByGroupCode("complain_duty_department"));
		ActionContext.getContext().put("costStatuss",ApiFactory.getSettingService().getOptionsByGroupCode("complain_cost_status"));
		return SUCCESS;
	}
	@Action("cost-all-table")
	public String allTable() throws Exception {
		params= CommonUtil1.convertJsonObject(params);
		/*Map<st, List<Map<String,Object>>> composings = composingManager.getComposingsForAll(params);*/
		List<Map<String,Object>> composings = composingManager.getComposingsForAll(params);
		Integer startMonth = Integer.valueOf(params.getString("startDate").replaceAll("-",""));
		Integer endMonth = Integer.valueOf(params.getString("endDate").replaceAll("-",""));	
		Map<Integer, Integer> map=new HashMap<Integer, Integer>();
		int o=0;
		for (int k = startMonth; k <= endMonth; k++) {
			Integer year=(k/100);
			String s = String.valueOf(k);	 
			map.put(o, k);
			Integer month=Integer.parseInt(s.substring(s.length() - 2, s.length()));
			if(month==12){
				year++;
				month=1;
				k=Integer.valueOf((year.toString()+"00"));
			}
			o++;
		}
		ActionContext.getContext().put("composings",composings);
		ActionContext.getContext().put("map",map);
		return SUCCESS;
	}		
	/**
	 * 外部损失分析柏拉图
	 * @return
	 * @throws Exception
	 */
	@Action("exterior-loss-plato-list")
	public String exteriorLossPlatoList() throws Exception {
		 List<Option> productName = ApiFactory.getSettingService().getOptionsByGroupCode("ams_product_type");
	      ActionContext.getContext().put("productName",productName);//产业选项组
	      return SUCCESS;
	}	
	
	/**
     * 内部损失成本柏拉图表数据
     */
    @Action("interior-report-datas")
    @LogInfo(optType="统计",message="内部损失成本柏拉图")
    public String getInteriorReportDatas()
        throws Exception
    {
        try
        {
            renderText(JSONObject.fromObject(lossRateManager.queryInteriorReport(params)).toString());
        }
        catch(Exception e)
        {
        	log.error("内部损失成本统计失败!",e);
            createErrorMessage("统计失败:"+e.getMessage());
        }
        return null;
    }
    
    /**
     * 外部损失成本柏拉图表数据
     */
    @Action("exterior-report-datas")
    @LogInfo(optType="统计",message="外部损失成本柏拉图")
    public String getExteriorReportDatas()
        throws Exception
    {
        try
        {
            renderText(JSONObject.fromObject(lossRateManager.queryExteriorReport(params)).toString());
        }
        catch(Exception e)
        {
        	log.error("内部损失成本统计失败!",e);
            createErrorMessage("统计失败:"+e.getMessage());
        }
        return null;
    }
	
    /**
	 * 质量损失成本
	 * @return
	 * @throws Exception
	 */
	@Action("loss-plato-detail")
	public String lossPlatoDetail() throws Exception {
		params = CommonUtil1.convertJsonObject(params);
	     return SUCCESS;
	}
	
	@Override
	public CostRecord getModel() {
		return costRecord;
	}	
	
	/**
	  * 方法名: 手动更新质量成本统计
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("update-cost-loss-count")
	public String updateCostCount() throws Exception {
//		JSONObject obj = new JSONObject();
//		try {
//			//更新外部成本
//			cacheExteriorCostIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName(),false);
//			obj.put("updateDate",DateUtil.formateTimeStr(new Date()));
//			obj.put("message","操作成功!");
//		} catch (Exception e) {
//			log.error("更新失败!",e);
//			obj.put("error",true);
//			if(e instanceof AmbFrameException){
//				obj.put("message",e.getMessage());
//			}else{
//				obj.put("message","操作失败," + e.getMessage());
//			}
//		}
//		renderText(obj.toString());
		return null;
	}
	
	
    
	// 质量成本内部损失率推移图页面
    @Action("interior-loss-rate-trend-list")
    @LogInfo(optType = "页面", message = "质量成本内外部损失率推移图页面")
    public String interiorLossRateTrendList() throws Exception {
    	List<Option> companyNames = ApiFactory.getSettingService().getOptionsByGroupCode("subcompanys");
	    ActionContext.getContext().put("companyNames",companyNames);
	    ActionContext.getContext().put("dutyUnits",ApiFactory.getSettingService().getOptionsByGroupCode("complain_duty_department"));
		ActionContext.getContext().put("costStatuss",ApiFactory.getSettingService().getOptionsByGroupCode("complain_cost_status"));
        return SUCCESS;
    }

    // 质量成本内部损失率推移图数据
    @Action("interior-loss-rate-trend-list-datas")
    @LogInfo(optType = "数据", message = "质量成本内外部损失率推移图数据")
    public String interiorLossRateTrendListDatas() throws Exception {
        params = CommonUtil1.convertJsonObject(params);
        try {
            if (params.get("myType") != null
                    && params.get("myType").toString().equals("month")) {
                this.renderText(JSONObject.fromObject(
                        lossRateManager.getInteriorLossRateProcessDatasByMonths(params))
                        .toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	@Action("cost-analysis")
	public String analysisList() throws Exception {	
		 ActionContext.getContext().put("threeLevels",ApiFactory.getSettingService().getOptionsByGroupCode("cost_three_level"));
		 ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		 ActionContext.getContext().put("cost_topComposings",ApiFactory.getSettingService().getOptionsByGroupCode("cost_topComposing"));
		return SUCCESS;
	}
	
	@Action("cost-analysis-datas")
	public String analysisDatas() throws Exception{
		try {
			JSONObject result = lossRateManager.queryAnalysisDdata(convertJsonObject(params));
			renderText(result.toString());
		} catch (Exception e) {
			log.error("统计失败!",e);
			JSONObject result = new JSONObject();
			result.put("error",true);
			result.put("message","统计失败," + e.getMessage());
			renderText(result.toString());
		}
		return null;
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
