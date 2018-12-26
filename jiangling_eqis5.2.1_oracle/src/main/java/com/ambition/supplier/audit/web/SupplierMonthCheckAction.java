package com.ambition.supplier.audit.web;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.audit.services.SupplierMonthCheckManager;
import com.ambition.supplier.entity.SupplierMonthCheck;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
/**
 * 类名:供应商月度稽核
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月27日 发布
 */
@Namespace("/supplier/audit/month")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/audit/month", type = "redirectAction") })
public class SupplierMonthCheckAction extends CrudActionSupport<SupplierMonthCheck>{
		/**
		  *SupplierMonthCheckAction.java2016年10月27日
		 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private Long supplierId;
    private SupplierMonthCheck supplierMonthCheck;
    private String saveSucc;
    @Autowired
    private SupplierMonthCheckManager supplierMonthCheckManager;
    private Page<SupplierMonthCheck> page;
    private JSONObject params;
    @Autowired
    private SupplierManager supplierManager;
    @Autowired
    private LogUtilDao logUtilDao;
	@Override
	public SupplierMonthCheck getModel() {
		// TODO Auto-generated method stub
		return supplierMonthCheck;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public SupplierMonthCheck getSupplierMonthCheck() {
		return supplierMonthCheck;
	}

	public void setSupplierMonthCheck(SupplierMonthCheck supplierMonthCheck) {
		this.supplierMonthCheck = supplierMonthCheck;
	}

	public String getSaveSucc() {
		return saveSucc;
	}

	public void setSaveSucc(String saveSucc) {
		this.saveSucc = saveSucc;
	}

	public Page<SupplierMonthCheck> getPage() {
		return page;
	}

	public void setPage(Page<SupplierMonthCheck> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Action("input")
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}

	@Override
	@Action("list")
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	@Action("list-datas")
    public String listdatas() throws Exception{
        try {
            page = supplierMonthCheckManager.getSupplierMonthCheckPage(page);
            renderText(PageUtils.pageToJson(page));
        } catch (Exception e) {
            logUtilDao.debugLog("查询失败", "查询失败"+e.getMessage());
        }
        return null;
    }
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
