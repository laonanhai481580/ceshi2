package com.ambition.epm.sample.web;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entity.SampleSublist;
import com.ambition.epm.sample.services.SampleSublistManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/epm/sample/sub")
@ParentPackage("default")
@Results({@Result(name = CrudActionSupport.RELOAD, location ="/epm/sample/sub", type = "redirectAction") })
public class SampleSublistAction extends CrudActionSupport<SampleSublist>{

		/**
		  *SampleSublistAction.java2017年5月25日
		 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private String deleteIds;
	private Long id;
	private Page<SampleSublist> page;
	private SampleSublist sampleSublist;
	@Autowired
	private SampleSublistManager sampleSublistManager;
	@Override
	public SampleSublist getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Action("delete")
	@LogInfo(optType="删除",message="删除数据")
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			sampleSublistManager.deleteSampleSublist(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据，编号："+deleteIds);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	
	@Action("list-datas")
	@LogInfo(optType="数据",message="查看数据")
	public String listDates(){
		try {
			page = sampleSublistManager.search(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("台账获取例表失败", e);
		}
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

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public SampleSublist getSampleSublist() {
		return sampleSublist;
	}

	public void setSampleSublist(SampleSublist sampleSublist) {
		this.sampleSublist = sampleSublist;
	}

	public Page<SampleSublist> getPage() {
		return page;
	}

	public void setPage(Page<SampleSublist> page) {
		this.page = page;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
	
}
