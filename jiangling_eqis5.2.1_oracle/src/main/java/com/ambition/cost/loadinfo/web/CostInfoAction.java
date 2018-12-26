package com.ambition.cost.loadinfo.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.cost.entity.QisCostView;
import com.ambition.cost.loadinfo.services.QisCostViewManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  林少伟
 * @version 1.00 2014-6-6 发布
 */
@Namespace("/cost/loadinfo")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/cost/loadinfo", type = "redirectAction") })
public class CostInfoAction extends CrudActionSupport<QisCostView>{
    private static final long serialVersionUID = 1L;
    private Long id;
    private QisCostView qisCostView;
    @Autowired
    private QisCostViewManager qisCostViewManager;
    private Page<QisCostView> page;
    private JSONObject params;
    @Autowired
    private LogUtilDao logUtilDao;
    
    public QisCostView getQisCostView() {
		return qisCostView;
	}

	public void setQisCostView(QisCostView qisCostView) {
		this.qisCostView = qisCostView;
	}

	public Page<QisCostView> getPage() {
		return page;
	}

	public void setPage(Page<QisCostView> page) {
		this.page = page;
	}

	public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public Map<String,String> getParameters(){
        String formNo = Struts2Utils.getParameter("formNo");
        String occurringMonthStr = Struts2Utils.getParameter("occurringMonthStr");
        String occurringMonth = Struts2Utils.getParameter("occurringMonth");
        String levelTwoCode = Struts2Utils.getParameter("levelTwoCode");
        String levelTwoName = Struts2Utils.getParameter("levelTwoName");
        String levelThreeCode = Struts2Utils.getParameter("levelThreeCode");
        String levelThreeName = Struts2Utils.getParameter("levelThreeName");
        String code = Struts2Utils.getParameter("code");
        String name = Struts2Utils.getParameter("name");
        String dutyDepart = Struts2Utils.getParameter("dutyDepart");
        String value = Struts2Utils.getParameter("value");
        String sourceType = Struts2Utils.getParameter("sourceType");
        String feeState = Struts2Utils.getParameter("feeState");
        String itemGroup = Struts2Utils.getParameter("itemGroup");
        String customerName = Struts2Utils.getParameter("customerName");
        String project = Struts2Utils.getParameter("project");
        String companyName = Struts2Utils.getParameter("companyName");
        Map<String,String> map = new HashMap<String,String>();
        map.put("formNo",formNo);
	    map.put("occurringMonthStr",occurringMonthStr);
	    map.put("occurringMonth",occurringMonth);
	    map.put("levelTwoCode",levelTwoCode);
	    map.put("levelTwoName",levelTwoName);
	    map.put("levelThreeCode",levelThreeCode);
	    map.put("levelThreeName",levelThreeName);
	    map.put("code",code);
	    map.put("name",name);
	    map.put("dutyDepart",dutyDepart);
	    map.put("value",value);
	    map.put("sourceType",sourceType);
	    map.put("feeState",feeState);
	    map.put("itemGroup",itemGroup);
	    map.put("customerName",customerName);
	    map.put("project",project);
	    map.put("companyName",companyName);
	    return map;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    @Override
    public QisCostView getModel() {
        return qisCostView;
    }

    @Override
    public String delete() throws Exception {
        return null;
    }

    @Override
    public String input() throws Exception {
        return null;
    }

    @Action("list")
    @Override
    public String list() throws Exception {
        return SUCCESS;
    }
    
    @Action("list-datas")
    public String listdatas() throws Exception{
        try {
            page = qisCostViewManager.getCostPage(page);
            renderText(PageUtils.pageToJson(page));
//            String result = ascendManager.getResultJson(page);
//            renderText(JSONObject.fromObject(result).toString());
            return null;
        } catch (Exception e) {
            logUtilDao.debugLog("查询失败", "查询失败"+e.getMessage());
        }
        return null;
    }
    @Action("export")
    @LogInfo(optType="导出",message="质量成本视图")   
    public String export() throws Exception {
        Page<QisCostView> page = new Page<QisCostView>(100000);
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        params =  (JSONObject) request.getSession().getAttribute("searchParmas");
        params= CommonUtil1.convertJsonObject(params);
        if(params.isEmpty()){
        	this.page =  page = qisCostViewManager.getCostPage(page);
        }else{
        	this.page = qisCostViewManager.getCostPageByParams(page,params);
        }
        this.renderText(ExcelExporter.export(ApiFactory.getMmsService()
                .getExportData(page, "QIS_COST_VIEW"), "质量损失明细台帐"));
        logUtilDao.debugLog("查询", "质量损失明细");
        return null;
    }
    /**
	 * 台账
	 * @return
	 * @throws Exception
	 */
	@Action("show-detail")
	public String showDetail() throws Exception {
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.getSession().setAttribute("searchParmas", params);
		return SUCCESS;
	}	
	/**
	 * 台账数据
	 * @return
	 * @throws Exception
	 */
	@Action("show-detail-datas")
	public String showDetailDatas() throws Exception {
	    HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        params =  (JSONObject) request.getSession().getAttribute("searchParmas");
		params= CommonUtil1.convertJsonObject(params);
		try {
            page = qisCostViewManager.getCostPageByParams(page,params);
            renderText(PageUtils.pageToJson(page));
//            String result = ascendManager.getResultJson(page);
//            renderText(JSONObject.fromObject(result).toString());
            return null;
        } catch (Exception e) {
            logUtilDao.debugLog("查询失败", "查询失败"+e.getMessage());
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
