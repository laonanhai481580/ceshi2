package com.ambition.supplier.archives.web;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.archives.service.SupplyProductManager;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 类名:供应商供应产品
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author linshaowei
 * @version 1.00 2016年10月25日 发布
 */
@Namespace("/supplier/archives/products")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/archives/products", type = "redirectAction") })
public class SupplyProductAction extends CrudActionSupport<SupplyProduct>{
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<SupplyProduct> page;
	private SupplyProduct supplyProduct;
	@Autowired
	private SupplyProductManager supplyProductManager;
	
	
	
	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
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

	public Page<SupplyProduct> getPage() {
		return page;
	}

	public void setPage(Page<SupplyProduct> page) {
		this.page = page;
	}

	public SupplyProduct getSupplyProduct() {
		return supplyProduct;
	}

	public void setSupplyProduct(SupplyProduct supplyProduct) {
		this.supplyProduct = supplyProduct;
	}

	@Override
	public SupplyProduct getModel() {
		// TODO Auto-generated method stub
		return supplyProduct;
	}

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
	@Action("list-product-datas")
	public String productDatas() throws Exception {
		try{
			String parentId = Struts2Utils.getParameter("parentId");
			page = supplyProductManager.searchByPage(page,Long.valueOf(parentId));
			renderText(PageUtils.pageToJson(page,"SUPPLIER_SUPPLY_PRODUCT"));
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id==null){
			supplyProduct = new SupplyProduct();
			supplyProduct.setCreatedTime(new Date());
			supplyProduct.setCompanyId(ContextUtils.getCompanyId());
			supplyProduct.setCreatorName(ContextUtils.getUserName());
			supplyProduct.setCreator(ContextUtils.getLoginName());
			supplyProduct.setLastModifiedTime(new Date());
			supplyProduct.setLastModifier(ContextUtils.getUserName());
			supplyProduct.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplyProduct.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			supplyProduct = supplyProductManager.getSupplyProduct(id);
		}
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
