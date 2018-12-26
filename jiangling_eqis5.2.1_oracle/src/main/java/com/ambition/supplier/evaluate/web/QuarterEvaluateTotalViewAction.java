package com.ambition.supplier.evaluate.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.SupplierEvaluateTotalView;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.evaluate.service.SupplierEvaluateTotalViewManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:供应商评价汇总
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年12月9日 发布
 */
@Namespace("/supplier/evaluate/quarter")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/evaluate/quarter", type = "redirectAction") })
public class QuarterEvaluateTotalViewAction  extends CrudActionSupport<SupplierEvaluateTotalView>{
	private static final long serialVersionUID = 1L;
    private Long id;
    private SupplierEvaluateTotalView supplierEvaluateTotalView;
    @Autowired
    private SupplierEvaluateTotalViewManager supplierEvaluateTotalViewManager;
    private Page<SupplierEvaluateTotalView> page;
    private JSONObject params;
    @Autowired
	private EstimateModelManager estimateModelManager;
    private Logger log=Logger.getLogger(this.getClass());
    @Autowired
    private LogUtilDao logUtilDao;
    public SupplierEvaluateTotalView getSupplierEvaluateTotalView() {
		return supplierEvaluateTotalView;
	}

	public void setSupplierEvaluateTotalView(SupplierEvaluateTotalView supplierEvaluateTotalView) {
		this.supplierEvaluateTotalView = supplierEvaluateTotalView;
	}

	public Page<SupplierEvaluateTotalView> getPage() {
		return page;
	}

	public void setPage(Page<SupplierEvaluateTotalView> page) {
		this.page = page;
	}

	public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public Map<String,Object> getParameters(){
        String allName = Struts2Utils.getParameter("allName");
        String supplierName = Struts2Utils.getParameter("supplierName");
        String materialType = Struts2Utils.getParameter("materialType");
        Integer evaluateMonth = Integer.valueOf(Struts2Utils.getParameter("evaluateMonth"));
        Integer evaluateYear = Integer.valueOf(Struts2Utils.getParameter("evaluateYear"));
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("allName",allName);
	    map.put("supplierName",supplierName);
	    map.put("materialType",materialType);
	    map.put("evaluateYear",evaluateYear);
	    map.put("evaluateMonth",evaluateMonth);
	    return map;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    @Override
    public SupplierEvaluateTotalView getModel() {
        return supplierEvaluateTotalView;
    }

    @Override
    public String delete() throws Exception {
        return null;
    }

    @Override
    public String input() throws Exception {
        return null;
    }
    public Map<String,Integer> getCycleMap(){
		List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_estimate_model_cycle");
		Map<String,Integer> cycleMap = new HashMap<String, Integer>();
		for(Option option : options){
			if(StringUtils.isNumeric(option.getValue())){
				cycleMap.put(option.getName(),Integer.parseInt(option.getValue()));
			}
		}
		return cycleMap;
	}
    public List<Option> createMonhts(int cycle){
		List<Option> months = new ArrayList<Option>();
		for (int i = 0; i <= (12/cycle)-1; i++) {
			Option option = new Option();
			option.setName(i*cycle+1 + "");
			option.setValue(i*cycle+1 + "");
			months.add(option);
		}
		return months;
	}
    @Action("list")
    @Override
    public String list() throws Exception {
//    	ActionContext.getContext().put("estimateModelMaps","[]");
    	List<EstimateModel> estimateModel = estimateModelManager.getParentEstimateModel();
		List<Option> estimateModels = new ArrayList<Option>();
		for (int i = 0; i < estimateModel.size(); i++) {
			Option option = new Option();
			option.setName(estimateModel.get(i).getName());
			option.setValue(estimateModel.get(i).getId().toString());
			estimateModels.add(option);
		}
		ActionContext.getContext().put("estimateModels", estimateModels);
		String parentModelId = Struts2Utils.getParameter("parentModelId");
		if(StringUtils.isNotEmpty(parentModelId)){
			EstimateModel model = estimateModelManager.getEstimateModel(Long.parseLong(parentModelId));
			Map<String,Integer> cycleMap = getCycleMap();
			ActionContext.getContext().put("months", createMonhts(cycleMap.get(model.getCycle())));
		}else if(estimateModel.isEmpty()){
			ActionContext.getContext().put("months", createMonhts(1));
		}else{
			Map<String,Integer> cycleMap = getCycleMap();
			ActionContext.getContext().put("months", createMonhts(cycleMap.get(estimateModel.get(0).getCycle())));
		}
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		String year = Struts2Utils.getParameter("year");
		String month = Struts2Utils.getParameter("month");
		ActionContext.getContext().put("supplierName", Struts2Utils.getParameter("supplierName"));
		if(StringUtils.isEmpty(year)){
			ActionContext.getContext().put("year", current.get(Calendar.YEAR));
		}else{
			ActionContext.getContext().put("year", Integer.parseInt(year));
		}
		if(StringUtils.isEmpty(month)){
			ActionContext.getContext().put("month", current.get(Calendar.MONTH));
		}else{
			ActionContext.getContext().put("month", Integer.parseInt(month));
		}
		if(StringUtils.isEmpty(parentModelId)){
			ActionContext.getContext().put("parentModelId", estimateModel.get(0).getId());
		}else{
			ActionContext.getContext().put("parentModelId", Integer.parseInt(parentModelId));
		}
		List<Option> years = new ArrayList<Option>();
		for(int i=current.get(Calendar.YEAR);i>current.get(Calendar.YEAR)-2;i--){
			Option option = new Option();
			option.setName(i + "年");
			option.setValue(i + "");
			years.add(option);
		}
		ActionContext.getContext().put("years", years);
		supplierEvaluateTotalViewManager.rendHistoryDatas();
        return SUCCESS;
    }
    
    @Action("list-datas")
    public String listdatas() throws Exception{
        try {
//            page = supplierEvaluateTotalViewManager.getViewPage(page);
            page = supplierEvaluateTotalViewManager.getPage(page);
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        	request.getSession().setAttribute("page",page);
            renderText(PageUtils.pageToJson(page));
            return null;
        } catch (Exception e) {
            logUtilDao.debugLog("查询失败", "查询失败"+e.getMessage());
        }
        return null;
    }
    @SuppressWarnings("unchecked")
	@Action("export-all")
    public String exportAll() throws Exception {
//    	supplierEvaluateTotalViewManager.rendHistoryDatas();
    	List<SupplierEvaluateTotalView> results = supplierEvaluateTotalViewManager.getResults();
    	Page<SupplierEvaluateTotalView> page = new Page<SupplierEvaluateTotalView>();
    	page.setResult(results);;
//    	 page = supplierEvaluateTotalViewManager.getPage(page);
        this.renderText(ExcelExporter.export(ApiFactory.getMmsService()
                .getExportData(page, "SUPPLIER_EVALUATE_TOTAL_VIEW"), "供应商评价汇总台帐"));
        logUtilDao.debugLog("查询", "供应商评价汇总");
        return null;
    }
    /**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("export-excel")
	@LogInfo(optType="导出",message="导出excel")
	public String downloadTemplate() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();  
			HttpServletRequest  request = ServletActionContext.getRequest(); 
			request.setCharacterEncoding("UTF-8");// 设置request的编码方式，防止中文乱码
			String fileName = "导出数据.xlsx";// 设置导出的文件名称
			StringBuffer sb = new StringBuffer(Struts2Utils.getParameter("tableInfo"));// 将表格信息放入内存
			String contentType = "application/vnd.ms-excel";// 定义导出文件的格式的字符串
			String recommendedName = new String(fileName.getBytes(), "iso_8859_1");// 设置文件名称的编码格式
			response.setContentType(contentType);// 设置导出文件格式
			response.setHeader("Content-Disposition", "attachment; filename="
					+ recommendedName );
			response.resetBuffer();
			// 利用输出输入流导出文件
			ServletOutputStream sos = response.getOutputStream();
			sos.write(sb.toString().getBytes());
			sos.flush();
			sos.close();
		}catch (Exception e) {
			log.error("导出失败!",e);
		} 
		return null;
	}
    @Override
    protected void prepareModel() throws Exception {
        
    }
    
    @Override
    public String save() throws Exception {
        return null;
    }
    
}
