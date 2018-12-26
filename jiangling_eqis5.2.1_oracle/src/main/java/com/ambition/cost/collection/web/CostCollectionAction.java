package com.ambition.cost.collection.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.cost.collection.service.CostCollectionManager;
import com.ambition.cost.entity.CostRecord;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:成本数据采集
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-16 发布
 */
@Namespace("/cost/collection")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "cost/collection", type = "redirectAction") })
public class CostCollectionAction extends CrudActionSupport<CostRecord> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<CostRecord> page;
	private CostRecord costRecord;
	private String costType;//成本类型
	@Autowired
	CostCollectionManager costCollectionManager;
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



	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	@Override
	public CostRecord getModel() {
		return costRecord;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			costRecord = new CostRecord();
			costRecord.setCompanyId(ContextUtils.getCompanyId());
			costRecord.setCreatedTime(new Date());
			costRecord.setCreator(ContextUtils.getUserName());
			costRecord.setSubCompanyId(ContextUtils.getSubCompanyId());
			costRecord.setDepartmentId(ContextUtils.getDepartmentId());			
			costRecord.setBusinessUnitName(ContextUtils.getSubCompanyName());
			costRecord.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			costRecord = costCollectionManager.getCostRecord(id);
		}
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="删除成本数据")
	public String delete() throws Exception {
		costCollectionManager.deleteCostRecord(deleteIds);
		return null;
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 
		* 方法名: 重检、拆解损失费用统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("reinspection-list")
	public String reinspectionList() throws Exception {
		ActionContext.getContext().put("costType","reinspection");
		return SUCCESS;
	}
	
	@Action("reinspection-list-datas")
	@LogInfo(optType="数据",message="成本数据-重检、拆解损失")
	public String  reinspectionListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}	
	
	/**
	 * 
		* 方法名: 材料损失金额统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("material-loss-list")
	public String materialLossList() throws Exception {
		ActionContext.getContext().put("costType","materialLoss");
		return SUCCESS;
	}
	
	@Action("material-loss-list-datas")
	@LogInfo(optType="数据",message="成本数据-材料损失")
	public String  materialLossListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	/**
	 * 
		* 方法名: 客户索赔统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("customer-claim-list")
	public String customerClaimList() throws Exception {
		ActionContext.getContext().put("costType","customerClaim");
		return SUCCESS;
	}
	
	@Action("customer-claim-list-datas")
	@LogInfo(optType="数据",message="成本数据-客户索赔")
	public String  customerClaimListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}	
	
	
	/**
	 * 
		* 方法名: 质量处理费用统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("quality-cost-list")
	public String qualityCostList() throws Exception {
		ActionContext.getContext().put("costType","qualityCost");
		return SUCCESS;
	}
	
	@Action("quality-cost-list-datas")
	@LogInfo(optType="数据",message="成本数据-质量处理费用")
	public String  qualityCostListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}	
		
	/**
	 * 
		* 方法名: 退货损失统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("return-loss-list")
	public String returnLossList() throws Exception {
		ActionContext.getContext().put("costType","returnLoss");
		return SUCCESS;
	}
	
	@Action("return-loss-list-datas")
	@LogInfo(optType="数据",message="成本数据-退货损失")
	public String  returnLossListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}	
	
	/**
	 * 
		* 方法名: 检测设备折旧金额统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("equipment-loss-list")
	public String equipmentLossList() throws Exception {
		ActionContext.getContext().put("costType","equipmentLoss");
		return SUCCESS;
	}
	
	@Action("equipment-loss-list-datas")
	@LogInfo(optType="数据",message="成本数据-退货损失-检测设备折旧")
	public String  equipmentLossListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}	
		
	/**
	 * 
		* 方法名: 品质故障统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("quality-failure-list")
	public String qualityFailureList() throws Exception {
		ActionContext.getContext().put("costType","qualityFailure");
		return SUCCESS;
	}
	
	@Action("quality-failure-list-datas")
	@LogInfo(optType="数据",message="成本数据-退货损失-品质故障")
	public String qualityFailureListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}	
		
	/**
	 * 
		* 方法名: 品保部薪酬统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("qa-pay-list")
	public String qaPayList() throws Exception {
		ActionContext.getContext().put("costType","qaPay");
		return SUCCESS;
	}
	
	@Action("qa-pay-list-datas")
	@LogInfo(optType="数据",message="成本数据-退货损失-品保部薪酬")
	public String qaPayListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
		
	/**
	 * 
		* 方法名: 部门费用统计页面
		* <p>功能说明：</p>
		* @return
		* @throws Exception
	 */
	@Action("department-cost-list")
	public String departmentCostList() throws Exception {
		ActionContext.getContext().put("costType","departmentCost");
		return SUCCESS;
	}
	
	@Action("department-cost-list-datas")
	@LogInfo(optType="数据",message="成本数据-退货损失-部门费用")
	public String departmentCostListDatas() throws Exception {
		page = costCollectionManager.list(page,costType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存成本数据")
	public String save() throws Exception {
		try {
			String costType=Struts2Utils.getParameter("costType");
			costRecord.setCostType(costType);
			Date date=costRecord.getRefDate();
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM" );
			String str=sdf.format(date);
			Integer occurringMonth=Integer.valueOf(str.replaceAll("-",""));
			costRecord.setOccurringMonth(occurringMonth);
			costCollectionManager.saveCostRecord(costRecord);
			this.renderText(JsonParser.getRowValue(costRecord));
		} catch (Exception e) {
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	
	@Action("export")
	@LogInfo(optType="导出",message="导出成本数据")
	public String exportInputs() throws Exception {
		Page<CostRecord> page = new Page<CostRecord>(100000);
		String costType=Struts2Utils.getParameter("costType");
		String listCode=Struts2Utils.getParameter("listCode");
		String listName=Struts2Utils.getParameter("listName");
		page = costCollectionManager.list(page,costType);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, listCode),listName));
		return null;
	}

	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		this.renderText(JSONObject.fromObject(map).toString());
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
