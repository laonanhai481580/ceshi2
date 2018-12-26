package com.ambition.supplier.businessunitmap.web;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.businessunitmap.service.SupplierMappingBusinessUnitManger;
import com.ambition.supplier.entity.SupplierMappingBusinessUnit;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.annotations.Conversion;

/**
 * 类名:SupplierMappingBusinessUnitAction.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2016-7-6 下午2:01:47
 * </p>
 */
@Namespace("/supplier/business-unit-map")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "supplier/business-unit-map", type = "redirectAction") })
@Conversion
public class SupplierMappingBusinessUnitAction   extends CrudActionSupport<SupplierMappingBusinessUnit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	
	private Long id;
	
	private String deleteIds;//删除的编号 
	
	private SupplierMappingBusinessUnit supplierMappingBusinessUnit;
	
	private Page<SupplierMappingBusinessUnit> page;
	
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
	public Page<SupplierMappingBusinessUnit> getPage() {
		return page;
	}
	public void setPage(Page<SupplierMappingBusinessUnit> page) {
		this.page = page;
	}
	@Autowired
	private SupplierMappingBusinessUnitManger supplierMappingBusinessUnitManger;
	
 	@Override
	public SupplierMappingBusinessUnit getModel() {
		return supplierMappingBusinessUnit;
	}
 	@Action("delete")
	@Override
	public String delete() throws Exception {
 	     String deleteIds = Struts2Utils.getParameter("deleteIds");
         String ids[]=deleteIds.split(",");
         for(String deleteid:ids){
        	 supplierMappingBusinessUnitManger.deleteMappingBusinessUnt(Long.valueOf(deleteid));
         }
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
	public String listDatas() throws Exception {
		try {
	        page = supplierMappingBusinessUnitManger.search(page);
            renderText(PageUtils.pageToJson(page));            
        } catch (Exception e) {
            addActionError(e.getMessage());
            log.error("数据获取失败",e);
        }
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplierMappingBusinessUnit=new SupplierMappingBusinessUnit();
			supplierMappingBusinessUnit.setCompanyId(ContextUtils.getCompanyId());
			supplierMappingBusinessUnit.setCreatedTime(new Date());
			supplierMappingBusinessUnit.setCreator(ContextUtils.getUserName());
			supplierMappingBusinessUnit.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierMappingBusinessUnit.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			supplierMappingBusinessUnit=supplierMappingBusinessUnitManger.getSupplierMappingBusinessUnit(id);
		}
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try{
			supplierMappingBusinessUnit.setLastModifiedTime(new Date());
			supplierMappingBusinessUnit.setLastModifier(ContextUtils.getUserName());
			supplierMappingBusinessUnitManger.save(supplierMappingBusinessUnit);
            this.renderText(JsonParser.getRowValue(supplierMappingBusinessUnit));
        }catch(Exception e){                
            log.error("保存失败",e);
            renderText("{error:true,message:\""+"保存失败:" + e.getMessage()+"\"}");
        }
		return null;
	}

}
