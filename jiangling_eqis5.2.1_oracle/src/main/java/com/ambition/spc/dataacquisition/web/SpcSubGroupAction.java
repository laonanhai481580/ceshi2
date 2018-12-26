package com.ambition.spc.dataacquisition.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.dataacquisition.utils.CharacterConverter;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.LayerDetail;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSgTag;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.layertype.service.LayerTypeManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * SpcSubGroupAction.java
 * @param <WritableWorkbook>
 * @authorBy YUKE
 *
 */
@Namespace("/spc/data-acquisition")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/data-acquisition", type = "redirectAction") })
public class SpcSubGroupAction extends com.ambition.product.base.CrudActionSupport<SpcSubGroup> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long productId;//产品定义ID
	private String productName;//产品名称
	private SpcSubGroup spcSubGroup;
	private SpcSgSample spcSgSample;
	private SpcSgTag spcSgTag;
	private Page<SpcSubGroup> page;
	private JSONObject params;
	private File myFile;
	private String featureId;
	private String excelType;//导入的excel模板类型

	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
 	private LayerTypeManager layerTypeManager;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public SpcSubGroup getSpcSubGroup() {
		return spcSubGroup;
	}
	
	public void setSpcSubGroup(SpcSubGroup spcSubGroup) {
		this.spcSubGroup = spcSubGroup;
	}
	
	public SpcSgSample getSpcSgSample() {
		return spcSgSample;
	}
	
	public void setSpcSgSample(SpcSgSample spcSgSample) {
		this.spcSgSample = spcSgSample;
	}
	
	public SpcSgTag getSpcSgTag() {
		return spcSgTag;
	}
	
	public void setSpcSgTag(SpcSgTag spcSgTag) {
		this.spcSgTag = spcSgTag;
	}
	
	public Page<SpcSubGroup> getPage() {
		return page;
	}
	
	public void setPage(Page<SpcSubGroup> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	@Override
	public SpcSubGroup getModel() {
		return spcSubGroup;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		List<FeatureLayer> layerItems = new ArrayList<FeatureLayer>();
		Struts2Utils.getRequest().setAttribute("layerItems",layerItems);
		List<Object> layerLists = new ArrayList<Object>();
		Struts2Utils.getRequest().setAttribute("layerLists",layerLists);
		return SUCCESS;
	}
	
	@Action("sg-sample-datas")
	public String getSgSampleDatas() throws Exception {
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("sg-tag-datas")
	public String getSgTagDatas() throws Exception {
		String featureId = null;
		if(Struts2Utils.getParameter("featureId") != null){
			featureId = Struts2Utils.getParameter("featureId");
		}
		try {
			if(featureId != null && !featureId.isEmpty()){
				QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
				List list = new ArrayList();
				for(FeatureLayer layer:qualityFeature.getFeatureLayers()){
					Map<String,Object> result = new HashMap<String,Object>();
					result.put("id","layer_" + layer.getId());
					result.put("sampleMethod", layer.getSampleMethod());
					result.put("tagName", layer.getDetailName());
					result.put("tagCode", layer.getDetailCode());
					result.put("tagValue", "");
					result.put("isInputValue", layer.getIsInputValue());
					list.add(result);
				}
				Page<FeatureLayer> page = new Page<FeatureLayer>();
				page.setResult(list);
				page.setTotalCount(1);
				page.setPageSize(15);
				renderText(PageUtils.pageToJson(page));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			spcSubGroupManager.saveForInput();
			renderText("{}");
		} catch (Exception e) {
			log.error("保存失败!",e);
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	/**
	  * 方法名: 查询未完成采集的数据
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("find-last-spc-sub-group")
	public String findLastSpcSubGroup() throws Exception {
		try {
			JSONObject result = spcSubGroupManager.findLastSubGroup(Long.valueOf(Struts2Utils.getParameter("featureId")));
			renderText(result.toString());
		} catch (Exception e) {
			log.error("查询失败!",e);
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("save-tag")
	public String saveTag() throws Exception {
		try {
			spcSgTag = spcSubGroupManager.saveTag();
			String result = "{\"id\":\""+spcSgTag.getId()+"\",\"tagName\":\""+spcSgTag.getTagName()+"\"," +
					"\"tagCode\":\""+spcSgTag.getTagCode()+"\"," +
					"\"tagValue\":\""+spcSgTag.getTagValue()+"\"," +
					"\"mainId\":\""+spcSgTag.getSpcSubGroup().getId()+"\"," +
					"\"sampleMethod\":\""+spcSgTag.getMethod()+"\"}";
			renderText(result);
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("layer-items")
	public String getLayerItems() throws Exception {
		try {
			String featureId = Struts2Utils.getParameter("featureId");
			List<FeatureLayer> layerItems = new ArrayList<FeatureLayer>();
			if(featureId != null && !featureId.isEmpty()){
				QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
				if(qualityFeature.getFeatureLayers() != null){
					layerItems = qualityFeature.getFeatureLayers();
				}
			}
			Struts2Utils.getRequest().setAttribute("layerItems",layerItems);
			List<Object> layerLists = new ArrayList<Object>();
			List<Object> list = new ArrayList<Object>();
			for(FeatureLayer layer:layerItems){
				Map<Object,List<Object>> map = new HashMap<Object,List<Object>>();
				if("2".equals(layer.getSampleMethod())){
					LayerType layerType = layerTypeManager.getLayerTypeByName(layer.getDetailName());
					if(layerType.getLayerDetails().size() != 0){
						for(LayerDetail layerDetail : layerType.getLayerDetails()){
							Map<String,Object> map1 = new HashMap<String,Object>();
							map1.put("name",layerDetail.getDetailName());
							map1.put("value",layerDetail.getDetailCode());
							list.add(map1);
						}
					}
				}
				map.put(layer, list);
				layerLists.add(map);
			}
			Struts2Utils.getRequest().setAttribute("layerLists",layerLists);
		} catch (Exception e) {
			log.error("获取层别失败!",e);
		}
		return SUCCESS;
	}
	
	/**    
	 * 离线采集
	 */
	@Action("off-line-list")
	public String offLine() throws Exception {
		return SUCCESS;
	}
	
	/**    
	 * 生成离线数据采集的Excel模板
	 */
	@Action("general-model")
	public String generalModel() throws Exception {
		String featureId = Struts2Utils.getParameter("featureId");
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
		//样本容量
		int size = 0;
		size = qualityFeature.getSampleCapacity().intValue();
		size = size == 0 ? 5 : size;
		String filename = null;
		String[] chars={"\\\\","\"","/",":","\\*","\\?",">","<","\\|"};
		if(qualityFeature.getName() != null){
			filename = qualityFeature.getName();
			for(int i=0;i<chars.length;i++){
				if(i<2){
					filename = filename.replaceAll(chars[i],"_");
				}else{
					filename = filename.replaceAll(chars[i],"_");
				}
			}
		}
		
		//设置响应头和下载保存的文件名
		Struts2Utils.getResponse().reset();
		Struts2Utils.getResponse().setContentType("application/msexcel;charset=UTF-8");
		Struts2Utils.getResponse().setHeader("Content-Disposition", "attachment;" + " filename="+ CharacterConverter.encode(Struts2Utils.getRequest(),filename)+".xls");
		
		//附属信息的列数
		int dependCols = 0;
		List<FeatureLayer> dependInfos = qualityFeature.getFeatureLayers();
		if (dependInfos != null) {
			dependCols = dependInfos.size();
		}
		dependCols = dependCols == 0 ? 4 : dependCols;
		int[] depCols = new int[dependCols];
		
		OutputStream os = null;
	    WritableWorkbook wwb = null;
	    try {
	         os = Struts2Utils.getResponse().getOutputStream();
	         wwb = Workbook.createWorkbook(os);
	    }catch (Exception e) {
	      e.printStackTrace();
	    }
	    WritableSheet ws = wwb.createSheet(filename, 0);
	    WritableFont wf = new WritableFont(WritableFont.TIMES,10, WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
	    WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
	    WritableFont font1 = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.ROSE);
	    
	    WritableCellFormat wcfF = new WritableCellFormat(wf);
	    WritableCellFormat wcfF1 = new WritableCellFormat(font);
	    WritableCellFormat wcfF2 = new WritableCellFormat(font1);
	    try {
			wcfF.setAlignment(Alignment.CENTRE);
			wcfF1.setAlignment(Alignment.CENTRE);
			wcfF2.setAlignment(Alignment.CENTRE);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	    
    	String[] baseName = new String[]{"批次号","位置","时间","录入人","备注"};
	    int[] baseN = new int[]{1,0,1,0,0};
	    int baseInfoSize = 5;//基本信息有五列
	    
	    //-----------第一行--------------------
	    for(int i=0;i<baseInfoSize;i++){
	    	if(baseN[i]==1){
	    		setLabel(i, 0,"基本信息", wcfF1,ws);
	    	}else{
	    		setLabel(i, 0,"基本信息", wcfF,ws);
	    	}
	    }
	    int dataSize = size;
	    for (int i = 0; i < dataSize; i++) {
	    	setLabel(i+baseInfoSize, 0,"数据", wcfF1,ws);
	    } 
	    
	    //-----------第二行--------------------
	    for(int i=0;i<baseInfoSize;i++){
	    	setLabel(i, 1,baseName[i], wcfF,ws);
	    }
	    int num = 1;
		int col = 0;
		for (int i = 0; i < size; i++,num++) {
			setLabel(col+baseInfoSize, 1,"X"+ num, wcfF,ws);
			col++;
		}
		int index = 0;
		if(qualityFeature.getFeatureLayers() != null && qualityFeature.getFeatureLayers().size() != 0){
			for(FeatureLayer layer:qualityFeature.getFeatureLayers()){
				if(layer != null){
					setLabel(index+baseInfoSize+dataSize, 1,layer.getDetailName(),wcfF,ws);
				}
				depCols[index]=0;
				index++;
			}
		}else{
			for(index=0;index < dependCols;index++){
				if(index==0){
					setLabel(index+baseInfoSize+dataSize, 1,"机器",wcfF,ws);
				}
				if(index==1){
					setLabel(index+baseInfoSize+dataSize, 1,"生产线",wcfF,ws);
				}
				if(index==2){
					setLabel(index+baseInfoSize+dataSize, 1,"班组",wcfF,ws);
				}
				if(index==3){
					setLabel(index+baseInfoSize+dataSize, 1,"批次",wcfF,ws);
				}
				depCols[index]=0;
			}
		}
		
		//---------补充第一行的附属信息-------------
		for (int i = 0; i < dependCols; i++) {
			if(depCols[i] == 0){
				setLabel(i+baseInfoSize+dataSize, 0,"附属信息", wcfF,ws);
			}
		}

	    //合并单元格
	    try {
			ws.mergeCells(0, 0, (baseInfoSize-1),0 );//合并单元格，参数格式（开始列，开始行，结束列，结束行）
			ws.mergeCells(baseInfoSize,0,(baseInfoSize+dataSize-1),0);
			ws.mergeCells((baseInfoSize+dataSize),0,(baseInfoSize+dataSize+dependCols-1),0);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	    
		try {
			wwb.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭Excel工作薄对象
			try {
				try {
					wwb.close();
				} catch (WriteException e) {
					e.printStackTrace();
				}
				os.close();
			} catch (IOException ex1) {
				ex1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 设置单元格内容
	 *
	 */
	private void setLabel(int col, int row, String colName, WritableCellFormat wcfF, WritableSheet ws) {
		Label labelC0x = new jxl.write.Label(col, row, colName, wcfF);
		try {
			ws.addCell(labelC0x);
		} catch (RowsExceededException ex) {
			ex.printStackTrace();
		} catch (WriteException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 以excel方式保存数据
	 * 
	 */
	@Action("saveParamExcel")
	public String saveParamExcel() throws Exception {
		try {
			String str = excelJxlParse(myFile,featureId);
			addActionMessage(str);
		}catch (Exception e) {
			log.error("导入失败",e);
			addActionMessage("导入失败：" + e.getMessage());
		}
		return "off-line-list";
	}
	
	public String excelJxlParse(File myFile,String paramID) throws Exception{
		String title = "对不起，导入失败，原因：";
	    
		QualityFeature param = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(paramID),null);
		if (param == null) {
			return title+"参数为空";
		}
		ActionContext.getContext().put("qualityFeature",param);
		if(StringUtils.isEmpty(param.getTargetTableName())){
			return title+"保存的数据不存在!";
		}
		//数据位置
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(myFile);
			org.apache.poi.ss.usermodel.Workbook book = WorkbookFactory.create(inputStream);
			org.apache.poi.ss.usermodel.Sheet sheet = book.getSheetAt(0);
			Row groupRow = sheet.getRow(0);
			//统计合并列
			int sheetMergeCount = sheet.getNumMergedRegions(); 
			Map<String,CellRangeAddress> cellRangeAddressMap = new HashMap<String, CellRangeAddress>();
			for(int i = 0 ; i < sheetMergeCount ; i++ ){  
		        CellRangeAddress ca = sheet.getMergedRegion(i);
		        int firstRow = ca.getFirstRow(),lastRow = ca.getLastRow();
		        int firstColumn = ca.getFirstColumn(),lastColumn = ca.getLastColumn();
		        for(int rowIndex = firstRow;rowIndex<=lastRow;rowIndex++){
		        	for(int columnIndex = firstColumn;columnIndex<=lastColumn;columnIndex++){
		        		String key = rowIndex + "_" + columnIndex;
		        		cellRangeAddressMap.put(key,ca);
		        	}
		        }
		    }
			
			Integer[] valuePos = null;//值的位置
			Integer[] layerPos = null;//层别信息的位置
			Iterator<Cell> cellIterator = groupRow.cellIterator();
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				Object cellValue = ExcelUtil.getCellValue(cell);
				if(cellValue==null){
					continue;
				}
				String value = cellValue.toString().trim();
				if("数据".equals(value)){
					valuePos = new Integer[2];
					String key = cell.getRowIndex() + "_" + cell.getColumnIndex();
					if(cellRangeAddressMap.containsKey(key)){
						CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(key);
						valuePos[0] = cellRangeAddress.getFirstColumn();
						valuePos[1] = cellRangeAddress.getLastColumn();
					}else{
						valuePos[0] = cell.getColumnIndex();
						valuePos[1] = cell.getColumnIndex();
					}
				}else if("附属信息".equals(value)){
					layerPos = new Integer[2];
					String key = cell.getRowIndex() + "_" + cell.getColumnIndex();
					if(cellRangeAddressMap.containsKey(key)){
						CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(key);
						layerPos[0] = cellRangeAddress.getFirstColumn();
						layerPos[1] = cellRangeAddress.getLastColumn();
					}else{
						layerPos[0] = cell.getColumnIndex();
						layerPos[1] = cell.getColumnIndex();
					}
				}
			}
			if(valuePos==null){
				return title+"模板格式不正确,找不到数据列";
			}
			Row headerRow = sheet.getRow(1);
			cellIterator = headerRow.cellIterator();
			Map<String,Integer> columnIndexMap = new HashMap<String, Integer>();
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				Object cellValue = ExcelUtil.getCellValue(cell);
				if(cellValue==null){
					continue;
				}
				String value = cellValue.toString().trim();
				if("时间".equalsIgnoreCase(value)){
					columnIndexMap.put("inspectionDate",cell.getColumnIndex());
					break;
				}
			}
			if(columnIndexMap.containsKey("")){
				return title+"模板格式不正确,没有时间列.";
			}
			//层别信息
			Map<String,FeatureLayer> layerNameMap = new HashMap<String, FeatureLayer>();
			if(param.getFeatureLayers() != null){
				for(FeatureLayer layer:param.getFeatureLayers()){
					layerNameMap.put(layer.getDetailName(), layer);
				}
			}
			Map<String,Integer> layerIndexMap = new HashMap<String, Integer>();
			for(int i=layerPos[0];i<=layerPos[1];i++){
				Cell cell = headerRow.getCell(i);
				Object cellValue = ExcelUtil.getCellValue(cell);
				if(cellValue==null){
					continue;
				}
				String value = cellValue.toString().trim();
				if(layerNameMap.containsKey(value)){
					FeatureLayer layer = layerNameMap.get(value);
					layerIndexMap.put(layer.getDetailCode(),cell.getColumnIndex());
				}
			}
			Iterator<Row> rowIterator = sheet.rowIterator();
			Map<String,String> layerValueMap = new HashMap<String, String>();
			List<JSONObject> values = new ArrayList<JSONObject>();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				if(row.getRowNum()<2){
					continue;
				}
				Object dateValue = ExcelUtil.getCellValue(row.getCell(columnIndexMap.get("inspectionDate")));
				Date insepctionDate = null;
				if(dateValue != null){
					if(dateValue instanceof Date){
						insepctionDate = (Date)dateValue;
					}else{
						String str = dateValue.toString().trim().replace("/","-");
						if(str.length()==10){
							str += " 00:00:00";
						}
						insepctionDate = DateUtil.parseDate(str,"yyyy-MM-dd HH:mm");
					}
				}
				if(insepctionDate == null){
					insepctionDate = new Date();
				}
				
				//层别信息
				layerValueMap.clear();
				for(String detailCode : layerIndexMap.keySet()){
					Cell cell = row.getCell(layerIndexMap.get(detailCode));
					Object cellValue = ExcelUtil.getCellValue(cell);
					if(cellValue==null){
						continue;
					}
					layerValueMap.put(detailCode,cellValue.toString().trim());
				}
				//数据
				for(int i=valuePos[0];i<=valuePos[1];i++){
					Cell cell = row.getCell(i);
					Object cellValue = ExcelUtil.getCellValue(cell);
					if(cellValue==null){
						continue;
					}
					String value = cellValue.toString().trim();
					if(CommonUtil1.isDouble(value)){
						JSONObject valueMap = new JSONObject();
						valueMap.put("id",UUID.randomUUID().toString());
						valueMap.put("inspectionDate",DateUtil.formateDateStr(insepctionDate,"yyyyMMddHHmmssSSS"));
						valueMap.put("creatorName",ContextUtils.getUserName());
						valueMap.put("value",Double.valueOf(value));
						valueMap.put("featureId", param.getId().toString());
						for(String detailCode : layerValueMap.keySet()){
							String layerValue = layerValueMap.get(detailCode);
							if(StringUtils.isEmpty(layerValue)){
								continue;
							}
							valueMap.put(detailCode,layerValue);
						}
						values.add(valueMap);
					}
				}
			}
			if(values.isEmpty()){
				return title+"导入数据为空.";
			}
			spcSubGroupManager.saveImportItems(param, values);
			return "导入成功.";
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
			myFile.delete();
		}
	}
	
	public void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	
	@Action("special-excel-spc-import")
	public String specialExcelSpcImport(){
		List<Option> options = new ArrayList<Option>();
//		Option option = new Option();
//		option.setName("BCCP1");
//		option.setValue("16997799");
//		options.add(option);
//		option = new Option();
//		option.setName("BCCP2");
//		option.setValue("16997857");
//		options.add(option);
//		
//		option = new Option();
//		option.setName("X7左后门间隙1");
//		option.setValue("17000676");
//		options.add(option);
//		
//		option = new Option();
//		option.setName("X7左后门间隙2");
//		option.setValue("17000679");
//		options.add(option);
//		
//		option = new Option();
//		option.setName("X7左后门间隙3");
//		option.setValue("17000680");
//		options.add(option);
//		
//		option = new Option();
//		option.setName("X7左后门间隙4");
//		option.setValue("17000681");
//		options.add(option);
		ActionContext.getContext().put("options", options);
		return SUCCESS;
	}

	public String getExcelType() {
		return excelType;
	}

	public void setExcelType(String excelType) {
		this.excelType = excelType;
	}
	

	@Action("import-excel-spc-datas")
	public String importExcelSpcDatas(){
		List<Option> options = new ArrayList<Option>();
		try {
			options = spcSubGroupManager.importSpcDatas(myFile, excelType, productId);
//			if("冲压SPC".equals(excelType)){
//				options = spcSubGroupManager.importExcelSpcDatas_1(myFile,productId);
//			}else if("焊装三坐标".equals(excelType)){
//				options = spcSubGroupManager.importExcelSpcDatas_2(myFile);
//			}else if("焊装SPC".equals(excelType)){
//				options = spcSubGroupManager.importExcelSpcDatas_3(myFile);
//			}else if("涂装SPC".equals(excelType)){
//				options = spcSubGroupManager.importExcelSpcDatas_4(myFile);
//			}else if("总装SPC".equals(excelType)){
//				options = spcSubGroupManager.importExcelSpcDatas_5(myFile);
//			}
			addActionMessage("导入成功！");
		} catch (Exception e) {
			addActionMessage("导入失败：" + e.getMessage());
			log.error("导入EXCEL模板失败!",e);
		}
		ActionContext.getContext().put("options", options);
		return "special-excel-spc-import";
	}
	
	@Action("download")
	public String download(){
		try {
			String name_1 = "";
			String name_2 = "";
			if("冲压SPC".equals(excelType)){
				name_1 = "importExcel_1.xlsx";
				name_2 = "冲压SPC导入模板.xlsx";
			}else if("焊装三坐标".equals(excelType)){
				name_1 = "importExcel_2.xlsx";
				name_2 = "焊装三坐标导入模板.xlsx";
			}else if("焊装SPC".equals(excelType)){
				name_1 = "importExcel_3.xlsx";
				name_2 = "焊装SPC导入模板.xlsx";
			}else if("涂装SPC".equals(excelType)){
				name_1 = "importExcel_4.xlsx";
				name_2 = "涂装SPC导入模板.xlsx";
			}else if("总装SPC".equals(excelType)){
				name_1 = "importExcel_5.xlsx";
				name_2 = "总装SPC导入模板.xlsx";
			}
			CommonUtil1.downloadTemplate(name_1,name_2);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("下载EXCEL模板失败!",e);
		}
		return null;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
