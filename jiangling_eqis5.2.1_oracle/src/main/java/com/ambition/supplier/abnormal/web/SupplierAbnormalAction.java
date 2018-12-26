package com.ambition.supplier.abnormal.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.abnormal.services.SupplierAbnormalManager;
import com.ambition.supplier.entity.SupplierAbnormal;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:上线异常数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月5日 发布
 */
@Namespace("/supplier/abnormal")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/abnormal", type = "redirectAction") })
public class SupplierAbnormalAction extends CrudActionSupport<SupplierAbnormal>{

		/**
		  *SupplierAbnormalAction.java2016年11月5日
		 */
	private static final long serialVersionUID = 5364456594768902530L;
	private Long id;
	private String deleteIds;
	private Page<SupplierAbnormal> page;
	private JSONObject params;//查询参数
	private SupplierAbnormal supplierAbnormal;
	private String saveSucc;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierAbnormalManager supplierAbnormalManager;
	@Override
	public SupplierAbnormal getModel() {
		// TODO Auto-generated method stub
		return supplierAbnormal;
	}

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

	public Page<SupplierAbnormal> getPage() {
		return page;
	}

	public void setPage(Page<SupplierAbnormal> page) {
		this.page = page;
	}

	public SupplierAbnormal getSupplierAbnormal() {
		return supplierAbnormal;
	}

	public void setSupplierAbnormal(SupplierAbnormal supplierAbnormal) {
		this.supplierAbnormal = supplierAbnormal;
	}

	public String getSaveSucc() {
		return saveSucc;
	}

	public void setSaveSucc(String saveSucc) {
		this.saveSucc = saveSucc;
	}

	@Override
	@Action("list")
	public String list() throws Exception {
		return "SUCCESS";
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		// TODO Auto-generated method stub
		page = supplierAbnormalManager.listDatas(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		Page<SupplierAbnormal> page = new Page<SupplierAbnormal>(65535);
		page = supplierAbnormalManager.listDatas(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_ABNORMAL"),"上线异常数据"));
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id==null){
			supplierAbnormal = new SupplierAbnormal();
			supplierAbnormal.setCreatedTime(new Date());
			supplierAbnormal.setCompanyId(ContextUtils.getCompanyId());
			supplierAbnormal.setCreatorName(ContextUtils.getUserName());
			supplierAbnormal.setCreator(ContextUtils.getLoginName());
			supplierAbnormal.setLastModifiedTime(new Date());
			supplierAbnormal.setLastModifier(ContextUtils.getUserName());
			supplierAbnormal.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierAbnormal.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
	/*		String companyName = ContextUtils.getCompanyName();
			String companyCode = "282";
			if("欧菲科技-神奇工场".equals(companyName)){
				companyCode = "282";
			}
			supplierAbnormal.setBusinessUnitCode(companyCode);*/
//			supplierAbnormal.setBusinessUnitName("");//事业部
		}else {
			supplierAbnormal = supplierAbnormalManager.getSupplierAbnormal(id);
		}
	}

	@Override
	@Action("save")
	@LogInfo(optType="修改/保存",message="修改/保存数据")
	public String save() throws Exception {
		JSONObject result = new JSONObject();
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		if(id != null){
			supplierAbnormal.setLastModifiedTime(new Date());
			supplierAbnormal.setLastModifier(ContextUtils.getUserName());
		}
		try {
			supplierAbnormalManager.saveSupplierAbnormal(supplierAbnormal);
			renderText(JsonParser.getRowValue(supplierAbnormal));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除",message="删除数据")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		supplierAbnormalManager.delete(deleteIds);
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
