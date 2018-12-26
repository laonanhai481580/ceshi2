package com.ambition.epm.dataAcquisition.web;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entrustOrt.services.EntrustOrtSublistManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/epm/data-acquisition")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/epm/data-acquisition", type = "redirectAction")})
public class EntrustOrtMapAction extends CrudActionSupport<EntrustOrtSublist> {

		/**
		  *EntrustOrtMapAction.java2018年1月27日
		 */
	private static final long serialVersionUID = 1L;
	private JSONObject params;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private EntrustOrtSublist entrustOrtSublist;
	private Page<EntrustOrtSublist> page;
	@Autowired
	private EntrustOrtSublistManager entrustOrtSublistManager;//

	@Override
	public EntrustOrtSublist getModel() {
		// TODO Auto-generated method stub
		return null;
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
	public String list() throws Exception {
		// TODO Auto-generated method stub
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
	/**
	 * 实验统计分析页面
	 */
	@Action("show-detail")
	@LogInfo(optType="页面",message="查看详情")
	public String showDetail() throws Exception {
		return SUCCESS;
	}
	/**
	 * 数据
	 */
	@Action("size-spread-chart-datas")
	@LogInfo(optType="数据",message="尺寸分布趋势图数据")
	public String sizeSpreadchartDatas() throws Exception {
		String type = Struts2Utils.getParameter("type");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		try {
			if(type==null){
				type="N";
			}
			Map<String,Object> list=null;
			if(weight==3){
				 list=entrustOrtSublistManager.listStatistics(type, null,params);
			}else{
				 list=entrustOrtSublistManager.listStatistics(type, subName,params);
			}
			this.renderText(JSONObject.fromObject(list).toString());
		} catch (Exception e) {
			log.error("查询出错!",e);
		}
		return null;	
	}
	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntrustOrtSublist getEntrustOrtSublist() {
		return entrustOrtSublist;
	}

	public void setEntrustOrtSublist(EntrustOrtSublist entrustOrtSublist) {
		this.entrustOrtSublist = entrustOrtSublist;
	}

	public Page<EntrustOrtSublist> getPage() {
		return page;
	}

	public void setPage(Page<EntrustOrtSublist> page) {
		this.page = page;
	}

}
