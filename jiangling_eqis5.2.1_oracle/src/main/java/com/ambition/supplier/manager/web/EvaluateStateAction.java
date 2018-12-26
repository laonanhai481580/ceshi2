package com.ambition.supplier.manager.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.entity.SupplierEvaluateState;
import com.ambition.supplier.manager.service.SupplierQcdsManager;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:评价状态一览表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-1-19 发布
 */
@Namespace("/supplier/manager/evaluate-state")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/manager/evaluate-state", type = "redirectAction") })
public class EvaluateStateAction extends CrudActionSupport<SupplierEvaluateState> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Page<SupplierEvaluateState> page;
	private Integer evaluateYear;
	private Integer evaluateMonth;
	
	@Autowired
	private SupplierQcdsManager qcdsManager;
	
	public Integer getEvaluateYear() {
		return evaluateYear;
	}
	public void setEvaluateYear(Integer evaluateYear) {
		this.evaluateYear = evaluateYear;
	}
	public Integer getEvaluateMonth() {
		return evaluateMonth;
	}
	public void setEvaluateMonth(Integer evaluateMonth) {
		this.evaluateMonth = evaluateMonth;
	}
	public Page<SupplierEvaluateState> getPage() {
		return page;
	}
	public void setPage(Page<SupplierEvaluateState> page) {
		this.page = page;
	}
	@Override
	public SupplierEvaluateState getModel() {
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
	}
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		return null;
	}
	@Action("delete")
	@Override
	public String delete() throws Exception {
		return null;
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		List<Option> options = new ArrayList<Option>();
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		for(int i=2013;i<=currentYear;i++){
			Option option = new Option();
			option.setName(i + "年");
			option.setValue(i + "");
			options.add(option);
		}
		ActionContext.getContext().put("evaluateYears",options);
		if(evaluateYear == null){
			evaluateYear = currentYear;
		}
		options = new ArrayList<Option>();
		for(int i=1;i<=12;i++){
			Option option = new Option();
			option.setName(i + "月");
			option.setValue(i+"");
			options.add(option);
		}
		if(evaluateMonth==null){
			evaluateMonth = calendar.get(Calendar.MONTH)+1;
		}
		ActionContext.getContext().put("evaluateMonths",options);
		return SUCCESS;
	}
	/**
	  * 方法名: 查询数据
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try{
			Calendar calendar = Calendar.getInstance();
			if(evaluateYear == null){
				evaluateYear = calendar.get(Calendar.YEAR);
			}
			if(evaluateMonth==null){
				evaluateMonth = calendar.get(Calendar.MONTH)+1;
			}
			page = qcdsManager.querySupplierEvaluateState(page,evaluateYear,evaluateMonth);
			  String result = qcdsManager.getResultJson(page,evaluateYear,evaluateMonth);
	        renderText(result);   
//			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error("查询失败!",e);
		}
		return null;
	}
}
