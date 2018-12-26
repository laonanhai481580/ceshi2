package com.ambition.supplier.delivery.web;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.delivery.service.SupplierDeliveryManager;
import com.ambition.supplier.entity.SupplierDelivery;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**    
 * SupplierDeliveryAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/supplier/stat")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/stat", type = "redirectAction") })
public class SupplierDeliveryAction extends CrudActionSupport<SupplierDelivery> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private SupplierDelivery supplierDelivery;
	private Page<SupplierDelivery> page;

	@Autowired
	private SupplierDeliveryManager supplierDeliveryManager;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SupplierDelivery getSupplierDelivery() {
		return supplierDelivery;
	}

	public void setSupplierDelivery(SupplierDelivery supplierDelivery) {
		this.supplierDelivery = supplierDelivery;
	}

	public Page<SupplierDelivery> getPage() {
		return page;
	}

	public void setPage(Page<SupplierDelivery> page) {
		this.page = page;
	}

	@Override
	public SupplierDelivery getModel() {
		return null;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("delivery-rate")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("delivery-rate-datas")
	public String listDatas() throws Exception {
		try{
			page = supplierDeliveryManager.search(page);
			this.renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			e.printStackTrace();
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
