package com.ambition.cost.partsloss.web;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.cost.entity.PartsLoss;
import com.ambition.cost.partsloss.service.PartsLossManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/cost/partsloss")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "cost/partsloss", type = "redirectAction")})
public class PartsLossAction extends CrudActionSupport<PartsLoss> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private PartsLoss partsLoss;
	private Page<PartsLoss> page;
	private File myFile;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	
	
	@Autowired
	private PartsLossManager partsLossManager;
	
	/**
	 * 删除
	 */
	@Override
	@LogInfo(optType="删除",message="删除内部零件损失表")
	@Action("partsLoss-delete")
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
            renderText("删除的对象不存在!");
        }else{
            try {
            	partsLossManager.deletePartsLoss(deleteIds);
            } catch (Exception e) {
                e.printStackTrace();
                renderText("删除失败:" + e.getMessage());
            }
        }
        return null;
	}

	/**
	 * 输入页面
	 */
	@Override
	@Action("partsLoss-input")
	public String input() throws Exception {
		return SUCCESS;
	}

	/**
	 * 列表
	 */
	@Override
	@Action("partsLoss-list")
	public String list() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("partsLoss-listDatas")
	public String getListDatas() throws Exception {
		try{
		page = partsLossManager.search(page);
//		String str =PageUtils.pageToJson(page);
//		renderText(str.replaceAll("\\\\n",""));
		renderText(PageUtils.pageToJson(page));
		}
		catch(Exception e){
			logUtilDao.debugLog("零件数据列表错误信息", e.getMessage());
		}
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			partsLoss=new PartsLoss();
		}else{
			partsLoss=partsLossManager.getPartsLoss(id);
		}
	}

	/**
	 * 保存
	 */
	@Override
	@Action("partsLoss-save")
	@LogInfo(optType="保存",message="保存内部零件损失表")
	public String save() throws Exception {
		if(id != null){
			partsLoss.setModifiedTime(new Date());
			partsLoss.setModifier(ContextUtils.getUserName());
		}
		try {
			partsLossManager.savePartsLoss(partsLoss);
			renderText(JsonParser.getRowValue(partsLoss));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PartsLoss getModel() {
		return partsLoss;
	}
	
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	
	/**
	 * 编辑-保存
	 */
	@Action("partsLoss-editSave")
	@LogInfo(optType="partsLoss-editSave",message="内部零件损失表")
	public String editSave() throws Exception {
		partsLossManager.savePartsLoss(partsLoss);
			this.renderText(JsonParser.getRowValue(partsLoss));
		return null;
	}
	
	/**
	 * 编辑-删除
	 */
	@Action("partsLoss-editDelete")
	@LogInfo(optType="编辑-删除",message="内部零件损失表")
	public String editDelete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
            renderText("删除的对象不存在!");
        }else{
            try {
            	partsLossManager.deletePartsLoss(deleteIds);
            } catch (Exception e) {
                e.printStackTrace();
                renderText("删除失败:" + e.getMessage());
            }
        }
        return null;
	}
	/**
	 * 生成零件图号
	 * */
	@Action("create-part-image-no")
	public String createPartImageNo()throws Exception{
		try{
			List<PartsLoss> li =partsLossManager.getPartsLoss();
			for(PartsLoss pl:li){
				String mainCode=pl.getMainCode();
				if(mainCode!=null&&!"".equals(mainCode)){
					String [] code=mainCode.split("\\.");
	//				for(int i=2;i<=code.length;i++){
						if(code[code.length-1].length()<6){
							pl.setPartImageNo(code[code.length-2]+"."+code[code.length-1]);
						}else{
							pl.setPartImageNo(code[code.length-2]+"-"+code[code.length-1].substring(0, 4));
						}
	//				}
				}
				partsLossManager.savePartsLoss(pl);
			}
			renderText("{\"message\":\"生成成功!\"}");
		}catch(Exception e){
			log.error("质量成本生产零件图号失败:!",e);
			createErrorMessage("更新客户信息失败:" + e.getMessage());
		}
		return null;
	}
	/**
	  * 方法名: 导入台账数据
	  * @return
	  * @throws Exception
	 */
	@Action("import-datas")
	@LogInfo(optType="导入",message="内部零件损失表")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(partsLossManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	/**
	 * 导出
	 */
	@Action("partsLoss-export")
	@LogInfo(optType="导出",message="内部零件损失维护台帐导出")
	public String export() throws Exception {
		Page<PartsLoss> page = new Page<PartsLoss>(100000);
		page = partsLossManager.search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"COST_PARTS_LOSS"),"内部零件损失维护台帐"));
		return null;
	}
	/**
	 * 台账模板导出
	 */
	@Action("template-export")
	@LogInfo(optType="导出",message="台账模板导出")
	public String templateExport() throws Exception {
		try {
			PartsLossManager.createImportTemplate("COST_PARTS_LOSS","内部零件损失维护导入模板.xls");
		} catch (Exception e) {
			log.error("内部零件损失维护导入模板导出失败!",e);
			renderText("导出失败:" + e.getMessage());
		}
		return null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public PartsLoss getPartsLoss() {
		return partsLoss;
	}

	public void setPartsLoss(PartsLoss partsLoss) {
		this.partsLoss = partsLoss;
	}

	public Page<PartsLoss> getPage() {
		return page;
	}

	public void setPage(Page<PartsLoss> page) {
		this.page = page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}