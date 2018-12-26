package com.ambition.cost.innerproductloss.web;

import java.io.File;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import com.ambition.cost.entity.InnerProductLoss;
import com.ambition.cost.innerproductloss.service.InnerProductLossManager;
import com.ambition.cost.partsloss.service.PartsLossManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
@Namespace("/cost/innerproductloss")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "cost/innerproductloss", type = "redirectAction") })
public class InnerProductLossAction extends CrudActionSupport<InnerProductLoss> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private InnerProductLoss innerProductLoss;
	private Page<InnerProductLoss> page;
	private File myFile;
	
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

	public Page<InnerProductLoss> getPage() {
		return page;
	}

	public void setPage(Page<InnerProductLoss> page) {
		this.page = page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	@Autowired
	private InnerProductLossManager innerProductLossManager;
	

	@Override
	public InnerProductLoss getModel() {
		 return innerProductLoss;
	}

	@Action("delete")
	@LogInfo(optType="删除",message="质量成本内部成品损失")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
            renderText("删除的对象不存在!");
        }else{
            try {
            	innerProductLossManager.deleteInnerProductLoss(deleteIds);
            } catch (Exception e) {
                e.printStackTrace();
                renderText("删除失败:" + e.getMessage());
            }
        }
        return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Action("innerProductLoss-list")
	public String list() throws Exception {
		return SUCCESS;
 	}
	@Action("list-datas")
	public String getListDatas() throws Exception {
		page = innerProductLossManager.search(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			innerProductLoss=new InnerProductLoss();
		}else{
			innerProductLoss=innerProductLossManager.getInnerProductLoss(id);
		}
		
	}
	/**
	 * 保存
	 */
	@Override
	@Action("save")
	@LogInfo(optType="保存",message="质量成本内部成品损失")
	public String save() throws Exception {
		if(id != null){
			innerProductLoss.setModifiedTime(new Date());
			innerProductLoss.setModifier(ContextUtils.getUserName());
		}
		try {
			innerProductLossManager.saveInnerProductLoss(innerProductLoss);
			renderText(JsonParser.getRowValue(innerProductLoss));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 导出
	 */
	@Action("export")
	@LogInfo(optType="导出",message="质量成本内部成品损失")
	public String export() throws Exception {
		Page<InnerProductLoss> page = new Page<InnerProductLoss>(100000);
		page = innerProductLossManager.search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"COST_INNER_PRODUCT_LOSS"),"内部成品损失维护台帐"));
		return null;
	}
	/**
	  * 方法名: 导入台账数据
	  * @return
	  * @throws Exception
	 */
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(innerProductLossManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	 * 台账模板导出
	 */
	@Action("template-export")
	@LogInfo(optType="导出",message="内部成品损失维护模板导出")
	public String templateExport() throws Exception {
		try {
			PartsLossManager.createImportTemplate("COST_INNER_PRODUCT_LOSS","内部成品损失维护导入模板.xls");
		} catch (Exception e) {
			log.error("内部成品损失维护导入模板导出失败!",e);
			renderText("导出失败:" + e.getMessage());
		}
		return null;
	}
	
	}
