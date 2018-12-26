package com.ambition.spc.statistics.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.histogram.draw.DrawHistogram;
import com.ambition.spc.histogram.entity.HistogramParam;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 类名:统计分析数据-直方图
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-7-1 发布
 */
@Service
public class DrawHistogramDrawManager {
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcDataManager spcDataManager;
	/**
	 * 画直方图核心算法
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 */
	public void drawHistogramDraw(String featureId,String startDateStr,String endDateStr,String lastAmoutStr,JSONObject layerParams){
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),null);
		Integer sampleCapacity = qualityFeature.getSampleCapacity();
		if(sampleCapacity == null){
			sampleCapacity = 5;
		}
		Integer lastAmount = null;
		if(CommonUtil1.isInteger(lastAmoutStr)){
			lastAmount = Integer.valueOf(lastAmoutStr);
		}
		Map<String,Object> resultMap = spcDataManager.querySpcDataValues(qualityFeature, startDateStr, endDateStr,
				layerParams, 1, null, lastAmount);
		@SuppressWarnings("unchecked")
		List<double[]> values = (List<double[]>)resultMap.get("values");
		
		double[] itemList = new double[values.size() * sampleCapacity];
		//封装总的样本数据
		for(int i=0;i<values.size();i++){
			double[] sampleList = values.get(i);
			for(int j=0;j<sampleList.length;j++){
				itemList[i * sampleCapacity + j]=sampleList[j];
			}
		}
		BufferedImage image = null;
		HistogramParam param = new HistogramParam(itemList, "X", qualityFeature.getUpperTarge(), qualityFeature.getLowerTarge());
		if(qualityFeature.getPrecs()!=null){
			param.setDataPrecision(Integer.parseInt(qualityFeature.getPrecs()));
		}
		param.setImageHeight(400);
		param.setImageWidth(800);
		HttpServletResponse response=Struts2Utils.getResponse();
		image = DrawHistogram.drawHistogram(param);
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
}
