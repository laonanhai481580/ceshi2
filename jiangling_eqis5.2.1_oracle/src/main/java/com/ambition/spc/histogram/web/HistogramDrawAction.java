package com.ambition.spc.histogram.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.BsRules;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.histogram.draw.DrawHistogram;
import com.ambition.spc.histogram.entity.HistogramParam;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**    
 * HistogramDrawAction.java(画直通图ACTION)
 * @authorBy wanglf
 *
 */


@SuppressWarnings("deprecation")
@Namespace("/spc/histogram")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/histogram", type = "redirectAction") })
public class HistogramDrawAction extends CrudActionSupport<BsRules> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private BsRules bsRules;
	private Page<BsRules> page = new Page<BsRules>(Page.EACH_PAGE_TEN, true);
	private String deleteIds;
	private JSONObject params;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setPage(Page<BsRules> page) {
		this.page = page;
	}
	
	public Page<BsRules> getPage() {
		return page;
	}
	
	public BsRules getModel() {
		return bsRules;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	protected void prepareModel() throws Exception {
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}
	
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		return null;
	}
	
	/**
	 * 画直通图核心方法
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@Action("histogram-draw")
	public String histogramDraw() throws Exception {
		String featureId=Struts2Utils.getParameter("featureId");
		String date=Struts2Utils.getParameter("date");
		String batch=Struts2Utils.getParameter("batch");
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		Date startDate=null;
		Date endDate=null;
		int n=0;
		if(StringUtils.isNotEmpty(date)){
			n=Integer.parseInt(date)+1;
			endCal.add(Calendar.DATE, 1);
			endDate=endCal.getTime();
			endDate.setMinutes(0);
			endDate.setHours(0);
			endDate.setSeconds(0);
			startCal.setTime(endDate);
			startCal.add(Calendar.DATE, -n);
			startDate=startCal.getTime();
		}else{
			endCal.add(Calendar.DATE, 1);
			endDate=endCal.getTime();
			endDate.setMinutes(0);
			endDate.setHours(0);
			endDate.setSeconds(0);
			startCal.setTime(endDate);
			startCal.add(Calendar.DATE, -1);
			startDate=startCal.getTime();
		}
		//计算时间end
		QualityFeature qualityFeature=new QualityFeature();
		int effectiveCapacity=0;		
		if(featureId!=null){
			qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
			effectiveCapacity=qualityFeature.getSampleCapacity();
		}
		List<SpcSubGroup> spcSubGroupList=new ArrayList<SpcSubGroup>();;
		if(featureId!=null){
			spcSubGroupList=spcSubGroupManager.getSpcSubGroupList(Long.parseLong(featureId),startDate,endDate);
		}
		double[] itemList = new double[spcSubGroupList.size() * effectiveCapacity];
		if(qualityFeature.getId()!=null){
		//封装总的样本数据
		for(int i=0;i<spcSubGroupList.size();i++){
			SpcSubGroup group=spcSubGroupList.get(i);
			List<SpcSgSample> sampleList=group.getSpcSgSamples();
			for(int j=0;j<sampleList.size();j++){
				SpcSgSample sample=sampleList.get(j);
				itemList[i * sampleList.size() + j]=sample.getSamValue();
			}
		}
		BufferedImage image = null;
		HistogramParam param = new HistogramParam(itemList, "X", null, null);
		param.setImageHeight(400);
		param.setImageWidth(800);
		HttpServletResponse response=Struts2Utils.getResponse();
		image = DrawHistogram.drawHistogram(param);
		if (image == null) {
			return null;
		}
		OutputStream out = null;
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Pragma", "No-cache");
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException ex3) {
					ex3.printStackTrace();
				}
			}
		}
		return null;
	}

}
