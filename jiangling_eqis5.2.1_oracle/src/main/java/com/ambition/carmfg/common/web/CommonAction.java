package com.ambition.carmfg.common.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.defectioncode.service.DefectionCodeManager;
import com.ambition.carmfg.defectioncode.service.DefectionTypeManager;
import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.DefectionType;
import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.carmfg.inspectionbase.service.MfgInspectingIndicatorManager;
import com.ambition.product.BaseAction;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.entity.UseFile;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/carmfg/common")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/common", type = "redirectAction") })
public class CommonAction extends BaseAction<DefectionCode> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private Long structureId;//结构编号
	private Boolean multiselect = false;//是否多选
	private Long defectionTypeId;//不良类别Id
	private File uploadFile;//上传的文件
	private String uploadFileName;
	private User user;
	private String businessUnitName;//事业部代码
 	@Autowired
	private MfgInspectingIndicatorManager mfgInspectingIndicatorManager;
	@Autowired
	private AcsUtils acsUtil;
	private Long selParentId;
	

 	private DefectionType defectionType;
	
	private DefectionCode defectionCode;
	
	private Page<DefectionCode> page;
	@Resource(name="mesdbDataJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
 	
 	@Autowired
	private ProductBomManager productBomManager;
 	
 	@Autowired
 	private UseFileManager useFileManager;
 	
 	 	
 	@Autowired
 	private DefectionTypeManager defectionTypeManager;
 	
 	@Autowired
 	private DefectionCodeManager defectionCodeManager;

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}


	public Boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
	}

	public Page<DefectionCode> getPage() {
		return page;
	}

	public void setPage(Page<DefectionCode> page) {
		this.page = page;
	}


	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public Long getStructureId() {
		return structureId;
	}
	
	public void setStructureId(Long structureId) {
		this.structureId = structureId;
	}
	
	public Long getDefectionTypeId() {
		return defectionTypeId;
	}
	
	public void setDefectionTypeId(Long defectionTypeId) {
		this.defectionTypeId = defectionTypeId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getSelParentId() {
		return selParentId;
	}

	public void setSelParentId(Long selParentId) {
		this.selParentId = selParentId;
	}
	
	public Long getParentId() {
		return parentId;
	}
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public DefectionCode getModel() {
		return defectionCode;
	}
	
	public DefectionType getDefectionType() {
		return defectionType;
	}
	
	public void setDefectionType(DefectionType defectionType) {
		this.defectionType = defectionType;
	}
	
	public DefectionCode getDefectionCode() {
		return defectionCode;
	}
	
	public void setDefectionCode(DefectionCode defectionCode) {
		this.defectionCode = defectionCode;
	}

	public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    @Override
	protected void prepareModel() throws Exception {
		
	}
    @Action("select-by-process-section")
    public String selectByProcess() throws Exception {
    	JSONObject result = new JSONObject();
        try{
            String process = Struts2Utils.getParameter("process");
            Map<String,String> map = productBomManager.selectFromMes(process,jdbcTemplate);
            result.put("productLine", map.get("productLine"));
            result.put("processName", map.get("processName"));
            result.put("error", false);
        }catch(Exception e){
            log.error("从mes查找信息失败");
            result.put("error", true);
            result.put("message", "从mes查找信息失败");
        }
        this.renderText(JsonParser.object2Json(result));
        return null;
    }
	@Action("upload")
	public String input() throws Exception {
		try {
			UseFile useFile = useFileManager.saveFile(uploadFile,uploadFileName);
			String iqcDatas = Struts2Utils.getParameter("iqcDatas");
			Map<String, String> valueMap = new HashMap<String, String>();
			if(Boolean.valueOf(iqcDatas)){
				StringBuffer sb = new StringBuffer("");
				Workbook book = WorkbookFactory.create(new FileInputStream(uploadFile));
				Sheet sheet = book.getSheetAt(0);
				Row row = sheet.getRow(0);
				Map<String, Integer> columnMap = new HashMap<String, Integer>();
				Iterator<Row> rows = sheet.rowIterator();
				DecimalFormat df = new DecimalFormat("#.##############");
//				rows.next();// 标题行
				int i = 1;
				int rowTimes = 1;
				String mapKey = "";
				while (rows.hasNext()) {
					row = sheet.getRow(i-1);
					if(row==null){
						break;
					}
					String cellValueStr = "";
					if(rowTimes==1){
						Cell cell = row.getCell(rowTimes-1);
						if(cell==null){
							break;
						}else{
							mapKey = cell.getStringCellValue();
							rowTimes++;
							continue;
						}
					}
					for (int j=1;j>0;j++) {
						Cell cell = row.getCell(j);
						if(cell!=null){
							if(cellValueStr.length()==0){
								if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
									cellValueStr = String.valueOf(cell.getNumericCellValue());
								}else{
									cellValueStr = cell.getStringCellValue();
								}
								
							}else{
								if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
									cellValueStr += "," + String.valueOf(cell.getNumericCellValue());
								}else{
									cellValueStr += "," + cell.getStringCellValue();
								}
								
							}
						}else{
							rowTimes=1;
							break;
						}
						
					}
					valueMap.put(mapKey, cellValueStr);
					i++;
				}
			}
			valueMap.put("fileId", useFile.getId().toString());
			valueMap.put("fileName", uploadFileName);
			//ApiFactory.getBussinessLogService().log("上传附件","文件名称:"+uploadFileName,ContextUtils.getSystemId("amb"));
			renderText(JSONObject.fromObject(valueMap).toString());
//			renderText("{\"fileId\":\""+useFile.getId()+"\",\"fileName\":\""+uploadFileName+"\",\"path\":\""+useFile.getPath()+"\"}");
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("上传[" + uploadFileName + "] 失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("download")
	public String download() throws Exception{
		try {
			if(id != null){
				UseFile useFile = useFileManager.getUseFile(id);
				//ApiFactory.getBussinessLogService().log("下载附件","文件名称:"+useFile.getFileName(),ContextUtils.getSystemId("amb"));
				HttpServletResponse response = Struts2Utils.getResponse();
				response.reset();
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", (new StringBuilder(
						"attachment; filename=\"")).append(new String(useFile.getFileName().getBytes("GBK"),"ISO8859_1")).append("\"")
						.toString());
				response.getOutputStream().write(useFile.getBlobValue().getBytes(1,(int)useFile.getBlobValue().length()));
			}else{
				throw new RuntimeException("编号不能为空!"); 
			}
		} catch (Exception e) {
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/text");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append("下载错误.txt").append("\"")
					.toString());
			response.getOutputStream().write(e.getMessage().getBytes());
		}
		return null;
	}
	
	/**
	 * 更新文件名称
	 * @return
	 * @throws Exception
	 */
	@Action("update-file-name")
	public String updateFileName() throws Exception{
		try {
			useFileManager.updateFileName(id,uploadFileName);
			renderText("{}");
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
		}
		return null;
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
		return SUCCESS;
	}
	/**
	 * 根据事业部查询产品成本价格
	 * */
	@Action("product-bom-price-select")
	public String productBomPriceSelect() throws Exception{
	    businessUnitName=Struts2Utils.getParameter("businessUnitName");
	    return SUCCESS;
	}
	
	
	@Action("product-bom-select")
	public String productBomSelect() throws Exception {
//		List<ProductStructure> productStructures = productStructureManager.getProductStructureParents();
//		
//		List<Map<String,Object>> structureMaps = new ArrayList<Map<String,Object>>();
//		for(ProductStructure productStructure : productStructures){
//			structureMaps.add(convertProductStructureToMap(productStructure));
//		}
//		ActionContext.getContext().put("structureMaps",JSONArray.fromObject(structureMaps).toString());
//		
//		//初始选择
//		productStructure = productStructureManager.getFirstProductStructureByLevel(ProductStructureManager.getStructureKeyMap().keySet().size());

//		ActionContext.getContext().put("importances",productBomManager.getImportanceOptions());
		return SUCCESS;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("bom-list-datas")
	public String getBomListByParent() throws Exception {
		Page page = new Page();
		if(structureId == null){
			page.setResult(new ArrayList<Object>());
		}else{
			String searchParams = Struts2Utils.getParameter("searchParams");
			JSONObject params = null;
			if(StringUtils.isNotEmpty(searchParams)){
				params = JSONObject.fromObject(searchParams);
			}
			List<ProductBom> bomParents = null;
			if(nodeid == null){
				bomParents = productBomManager.getProductBomParentsByStructure(structureId,params);
			}else{
				bomParents = productBomManager.searchProductBomByParent(nodeid, params);
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(ProductBom productBom : bomParents){
				convertProductBom(productBom,list);
			}
			page.setResult(list);
		}
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("search-machine-no")
	public String searchMachineNos() throws Exception {
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		List<MfgInspectingIndicator> searchList = mfgInspectingIndicatorManager.searchMachineNo(params);
		StringBuffer sb = new StringBuffer("");
		List<String> list=new ArrayList<String>();
		for(MfgInspectingIndicator obj : searchList){
			if(list.contains(obj.getModel())){
				continue;
			}
			if(sb.length()>0){
				sb.append(",");
			}			
			list.add(obj.getModel());
			JSONObject json = new JSONObject();
			json.put("code",obj.getModel());
			json.put("name",obj.getModelName());
			json.put("value",obj.getModelName());
			json.put("label",obj.getModel());
			sb.append(json.toString());
		}
		sb.insert(0,"[").append("]");
		renderText(PageUtils.disposeSpecialCharacter(sb.toString()));
		return null;
	}		
	@Action("search-product-boms")
	public String seachProductBoms() throws Exception {
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		String label = Struts2Utils.getParameter("label");
		List<ProductBom> searchList = productBomManager.searchProductBoms(params);
		StringBuffer sb = new StringBuffer("");
		for(ProductBom obj : searchList){
			if(sb.length()>0){
				sb.append(",");
			}
			JSONObject json = new JSONObject();
			json.put("code",obj.getMaterielCode());
			json.put("name",obj.getMaterielName());
			json.put("model",obj.getMaterielModel()==null?"":obj.getMaterielModel());
			json.put("materilType",obj.getMaterialType()==null?"":obj.getMaterialType());
			if("code".equals(label)){
				json.put("value",obj.getMaterielName());
				json.put("label",obj.getMaterielCode());
			}else{
				json.put("label",obj.getMaterielName());
				json.put("value",obj.getMaterielCode());
			}
			sb.append(json.toString());
		}
		sb.insert(0,"[").append("]");
		renderText(PageUtils.disposeSpecialCharacter(sb.toString()));
		return null;
	}
	
	@Action("search-materials")
	public String seachMaterials() throws Exception {
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		List<ProductBom> searchList = productBomManager.searchMaterials(params);
		StringBuffer sb = new StringBuffer("");
		String label = Struts2Utils.getParameter("label");
		for(ProductBom obj : searchList){
			if(sb.length()>0){
				sb.append(",");
			}
			String code = obj.getMaterielCode()==null?"":obj.getMaterielCode().replaceAll("\n","");
			String name = obj.getMaterielName()==null?"":obj.getMaterielName().replaceAll("\n","");
			String model = obj.getMaterielModel()==null?"":obj.getMaterielModel().replaceAll("\n","");
			JSONObject json = new JSONObject();
			if("code".equals(label)){
				json.put("value",code);
				json.put("label",code);
			}else if("name".equals(label)){
				json.put("value",name);
				json.put("label",name);
			}else{
				json.put("value",code);
				json.put("label",code);
			}
			json.put("code",code);
			json.put("name",name);
			json.put("model",model);
			sb.append(json.toString());
		}
		sb.insert(0,"[").append("]");
		renderText(sb.toString());
		return null;
	}
	/**
	 * 转换物料结构至json对象
	 * @param CheckGrade
	 * @return
	 */
	private void convertProductBom(ProductBom productBom,List<Map<String,Object>> list){
		Boolean isLeaf = productBom.getBomChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",productBom.getId());
		map.put("name",productBom.getMaterielName());
		map.put("code",productBom.getMaterielCode());
		map.put("importance",productBom.getImportance());
		if(isLeaf){
			map.put("materialType",productBom.getMaterialType());
			map.put("ascendType",productBom.getAscendType());
		}
		map.put("remark",productBom.getRemark());
		map.put("level",productBom.getMaterielLevel()-1);
//		if(productBom.getLevel() == ProductStructureManager.getStructureKeyMap().keySet().size()){
//			map.put("modelSpecification",productBom.getProductStructure().getName());
//		}
		map.put("parent",productBom.getBomParent()==null?"":productBom.getBomParent().getId());
		map.put("parentName",productBom.getBomParent()==null?"":productBom.getBomParent().getMaterielName());
		map.put("isLeaf",isLeaf);
		map.put("expanded",false);
		map.put("loaded",false);
		list.add(map);
	}
	
	/**
	 * 转换产品结构至map
	 * @param productStructure
	 * @return
	 */
	@SuppressWarnings("unused")
	private Map<String,Object> convertProductStructureToMap(ProductBom productStructure){
		Map<String,Object> map = new HashMap<String, Object>();
//		{ 
//			"data" : "产品结构", 
//			"state" : "closed",
//			attr:{
//				id:'root',
//				level : 0,
//				rel:'drive'
//			}
//		}
		map.put("data",productStructure.getMaterielName());
		map.put("state","open");
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",productStructure.getId());
		attrMap.put("name",productStructure.getMaterielName());
		attrMap.put("level",productStructure.getMaterielLevel());
		map.put("attr",attrMap);
		if(!productStructure.getBomChildren().isEmpty()){
			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
			for(ProductBom child : productStructure.getBomChildren()){
				children.add(convertProductStructureToMap(child));
			}
			map.put("children",children);
		}
		return map;
	}
	
	@Action("product-bom-multi-select")
	public String productBomMultiSelect() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 转换不良分类至map
	 * @param defectionType
	 * @return
	 */
	@Action("defection-code-bom")
	public String defectionCodeSelect() throws Exception {
		List<DefectionType> defectionTypes = defectionTypeManager.listAll();
		List<Map<String,Object>> defectionTypeMaps = new ArrayList<Map<String,Object>>();
		for(DefectionType defectionType : defectionTypes){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("data",defectionType.getDefectionTypeName());
//			map.put("state","open");
			map.put("isLeaf",true);
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",defectionType.getId());
			attrMap.put("defectionTypeName",defectionType.getDefectionTypeName());
			attrMap.put("defectionTypeNo",defectionType.getDefectionTypeNo());
			map.put("attr",attrMap);
			defectionTypeMaps.add(map);
		}
		ActionContext.getContext().put("defectionTypeMaps",JSONArray.fromObject(defectionTypeMaps).toString());
	
		return SUCCESS;
	}
	
	@Action("code-bom-select")
	public String codeBomSelect() throws Exception {
		List<DefectionType> defectionTypes = defectionTypeManager.listAll();
		List<Map<String,Object>> defectionTypeMaps = new ArrayList<Map<String,Object>>();
		for(DefectionType defectionType : defectionTypes){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("data",defectionType.getDefectionTypeName());
			map.put("isLeaf",true);
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",defectionType.getId());
			attrMap.put("defectionTypeName",defectionType.getDefectionTypeName());
			attrMap.put("defectionTypeNo",defectionType.getDefectionTypeNo());
			map.put("attr",attrMap);
			defectionTypeMaps.add(map);
		}
		ActionContext.getContext().put("defectionTypeMaps",JSONArray.fromObject(defectionTypeMaps).toString());
	
		return SUCCESS;
	}
	
	@Action("code-list-datas")
	public String getCodeListByParent() throws Exception {
//		if(defectionTypeId != null && defectionTypeId != 0){
//			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
//			page = defectionCodeManager.list(page, defectionType);
//		}else{
//			page = defectionCodeManager.listCode(page);
//		}		
		if(defectionTypeId != null && defectionTypeId != 0){
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}else{
			defectionType = null;
		}
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		if(params!=null){
			page = defectionCodeManager.getCodeByParams(page, params);
		}else{
			page = defectionCodeManager.list(page, defectionType);
		}
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(DefectionCode defectionCode : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",defectionCode.getId());
				map.put("defectionCodeNo",defectionCode.getDefectionCodeNo());
				map.put("defectionCodeName",defectionCode.getDefectionCodeName());
				map.put("defectionTypeName", defectionCode.getDefectionType().getDefectionTypeName());
				map.put("defectionTypeNo", defectionCode.getDefectionType().getDefectionTypeNo());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("code-bom-multi-select")
	public String codeBomMultiSelect() throws Exception {
		List<DefectionType> defectionTypes = defectionTypeManager.listAll();
		List<Map<String,Object>> defectionTypeMaps = new ArrayList<Map<String,Object>>();
		for(DefectionType defectionType : defectionTypes){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("data",defectionType.getDefectionTypeName());
			map.put("isLeaf",true);
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",defectionType.getId());
			attrMap.put("defectionTypeName",defectionType.getDefectionTypeName());
			attrMap.put("defectionTypeNo",defectionType.getDefectionTypeNo());
			map.put("attr",attrMap);
			defectionTypeMaps.add(map);
		}
		ActionContext.getContext().put("defectionTypeMaps",JSONArray.fromObject(defectionTypeMaps).toString());
	
		return SUCCESS;
	}
	
	@Action("direction-bom-select")
	public String directionBomSelect() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-structure")
	public String listStructure() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<DefectionType> defectionTypes = defectionTypeManager.listAll();
		for(DefectionType typeStructure : defectionTypes){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("data",typeStructure.getDefectionTypeName());
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",typeStructure.getId());
			attrMap.put("defectionTypeName",typeStructure.getDefectionTypeName());
			attrMap.put("defectionTypeNo",typeStructure.getDefectionTypeNo());
			map.put("attr",attrMap);
			resultList.add(map);
		}
		renderText(JSONArray.fromObject(resultList).toString());
		return null;
	}
	@Action("structure-list-datas")
	public String getListDatasByParent() throws Exception {
		try {
			if(Struts2Utils.getRequest().getParameter("searchParameters") != null){
				selParentId = null;
			}
			page = defectionCodeManager.listByParent(page, selParentId);
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(DefectionCode defectionCode : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",defectionCode.getId());
				map.put("defectionCodeNo",defectionCode.getDefectionCodeNo());
				map.put("defectionCodeName",defectionCode.getDefectionCodeName());
				map.put("defectionTypeName", defectionCode.getDefectionType().getDefectionTypeName());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//选择产品型号
	@Action("product-bom-model-select")
	public String productBomModelSelect() throws Exception {
		return SUCCESS;
	}

	@Action("send-message")
	public String sendMessage() throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = null;
		File file = null;
		OutputStream outputStream = null;
		try {
			String svg = Struts2Utils.getRequest().getParameter("svg");
			String tos = Struts2Utils.getRequest().getParameter("tos");
			List<User> toUsers = new ArrayList<User>();
			for(String to : tos.split(",")){
				if(StringUtils.isNotEmpty(to)){
					toUsers.add(ApiFactory.getAcsService().getUserByLoginName(to));
				}
			}
			if(toUsers.isEmpty()){
				throw new AmbFrameException("收件人为空!");
			}
			String subject = Struts2Utils.getRequest().getParameter("subject");
			String content = Struts2Utils.getRequest().getParameter("content");
//			String data=Struts2Utils.getRequest().getParameter("data");
//			String label=Struts2Utils.getRequest().getParameter("label");
//			String width=Struts2Utils.getRequest().getParameter("width");
//			String height=Struts2Utils.getRequest().getParameter("height");
			if (StringUtils.isNotEmpty(svg)) {
			    svg = svg.replaceAll(":rect", "rect");
			    Transcoder t = new JPEGTranscoder();
		    	byteArrayOutputStream = new ByteArrayOutputStream();
				TranscoderInput input = new TranscoderInput(new StringReader(svg));
				TranscoderOutput output = new TranscoderOutput(byteArrayOutputStream);
				t.transcode(input, output);
				file = new File(System.currentTimeMillis() + ".jpg");
				outputStream = new FileOutputStream(file);
				outputStream.write(byteArrayOutputStream.toByteArray());
				outputStream.close();
				outputStream = null;
//				SendPictureMailUtils.sendFiles(toUsers,subject,content,file);
				renderText("{}");
			}else{
				throw new AmbFrameException("图片不存在!");
			}
		} catch (Exception e) {
			createErrorMessage("发送邮件失败," + e.getMessage());
			log.error("发送邮件失败!",e);
		}finally{
			if(outputStream != null){
				outputStream.close();
			}
			if(file != null){
				file.delete();
			}
			if(byteArrayOutputStream != null){
				byteArrayOutputStream.close();
			}
		}
		return null;
	}
	//选择产品型号
	@Action("update-user-email")
	public String updateEmail() throws Exception {
		String loginName = ContextUtils.getLoginName();
		com.norteksoft.acs.entity.organization.User user = acsUtil.getUserByLoginName(loginName);
		ActionContext.getContext().put("user",user);
		return SUCCESS;
	}
	//选择产品型号
	@Action("save-user-email")
	public String saveEmail() throws Exception {
		JSONObject result = new JSONObject();
		String loginName = ContextUtils.getLoginName();
		com.norteksoft.acs.entity.organization.User user = acsUtil.getUserByLoginName(loginName);
		String newEmail = Struts2Utils.getParameter("email");
		user.setEmail(newEmail);
		try{
			productBomManager.saveUser(user);
			result.put("error", false);
			result.put("message", "修改成功");
		}catch(Exception e){
			result.put("message", "修改失败");
			e.printStackTrace();
		}
		this.renderText(result.toString());
		return null;
	}
}