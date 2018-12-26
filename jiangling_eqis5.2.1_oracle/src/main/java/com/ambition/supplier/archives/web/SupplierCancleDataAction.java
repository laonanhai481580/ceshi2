package com.ambition.supplier.archives.web;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.archives.service.SupplierCancleDatasManager;
import com.ambition.supplier.entity.SupplierCancleDatas;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/supplier/archives")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/archives", type = "redirectAction") })
public class SupplierCancleDataAction extends CrudActionSupport<SupplierCancleDatas>{

		/**
		  *SupplierCancleDataAction.java2017年7月5日
		 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private SupplierCancleDatas supplierCancleDatas;
	@Autowired
	private SupplierCancleDatasManager supplierCancleDatasManager;
	private Long id;
	private String deleteIds;
	private Page<SupplierCancleDatas> page;
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	
	public SupplierCancleDatas getSupplierCancleDatas() {
		return supplierCancleDatas;
	}

	public void setSupplierCancleDatas(SupplierCancleDatas supplierCancleDatas) {
		this.supplierCancleDatas = supplierCancleDatas;
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

	public Page<SupplierCancleDatas> getPage() {
		return page;
	}

	public void setPage(Page<SupplierCancleDatas> page) {
		this.page = page;
	}

	@Override
	public SupplierCancleDatas getModel() {
		// TODO Auto-generated method stub
		return supplierCancleDatas;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Action("cancle")
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	@Action("cancle-datas")
	@LogInfo(optType="查询",message="可取消供应商数据查询")
	public String getSuppliers() throws Exception {
		try{
			page = supplierCancleDatasManager.searchByPage(page);
			renderText(PageUtils.pageToJson(page,"SUPPLIER_CANCLE_DATAS"));
		}catch (Exception e) {
			log.error(e);
		}
//		logUtilDao.debugLog("查询", "供应商质量管理：供应商台帐");
		return null;
	}
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
