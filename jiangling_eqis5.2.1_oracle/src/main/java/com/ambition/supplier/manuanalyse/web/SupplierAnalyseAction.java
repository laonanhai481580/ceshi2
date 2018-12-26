package com.ambition.supplier.manuanalyse.web;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.ambition.product.BaseAction;
import com.ambition.supplier.entity.CheckPlan;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.ApiFactory;
import com.opensymphony.xwork2.ActionContext;

/**
 * 供应商质量分析
 * @author 王龙峰
 *
 */
@Namespace("/supplier/stat")
@ParentPackage("default")
public class SupplierAnalyseAction  extends BaseAction<CheckPlan> {
	private static final long serialVersionUID = 1L;
	
	@Action("defective-goods-chart")
	public String defectiveGoodsChart() throws Exception {
		List<Option> classifications = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_Classification");
		ActionContext.getContext().put("classifications",classifications);
		return SUCCESS;
	}
	
	@Action("inspection-chart")
	public String inspectionChart() throws Exception {
		List<Option> importance = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_importance");
		ActionContext.getContext().put("importances", importance);
		List<Option> materialType = ApiFactory.getSettingService().getOptionsByGroupCode("supply_materialType");
		ActionContext.getContext().put("materialTypes", materialType);
		return SUCCESS;
	}
	
	@Action("inspection-one-chart")
	public String inspectionOneChart() throws Exception {
		return SUCCESS;
	}
	
	@Action("material-rate-chart")
	public String materialRateChart() throws Exception {
		return SUCCESS;
	}
	
	@Action("supplier-rate-chart")
	public String supplierRateChart() throws Exception {
		return SUCCESS;
	}
}
