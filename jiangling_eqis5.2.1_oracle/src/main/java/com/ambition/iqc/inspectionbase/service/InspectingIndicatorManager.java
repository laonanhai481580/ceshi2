package com.ambition.iqc.inspectionbase.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectingIndicator;
import com.ambition.iqc.inspectionbase.dao.InspectingIndicatorDao;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * InspectionIndicatorManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class InspectingIndicatorManager {
	@Autowired
	private InspectingIndicatorDao inspectingIndicatorDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private ItemIndicatorManager indicatorManager;
	
	/**
	  * 方法名: 查询所有版本的检验标准
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<InspectingIndicator> listAll(Page<InspectingIndicator> page,String materielCode){
		String hql = "from InspectingIndicator i where i.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		//如果查询条件为空时,物料代码不为空,按照物料来查
		String searchParamters = Struts2Utils.getParameter("searchParameters");
		if(searchParamters==null&&StringUtils.isNotEmpty(materielCode)){
			hql += " and i.materielCode = ?";
			searchParams.add(materielCode);
		}
		return inspectingIndicatorDao.searchPageByHql(page,hql,searchParams.toArray());
	}
	
	/**
	  * 方法名: 查询最新版本的检验标准
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<InspectingIndicator> listMaxVersion(Page<InspectingIndicator> page){
		return inspectingIndicatorDao.listMaxVersion(page);
	}
	
	/**
	 * 检查是否存在相同名称的物料名称
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistInspectingIndicator(Long id,String code,String standardVersion){
		String hql = "select count(i.id) from InspectingIndicator i where i.companyId = ? and i.materielCode = ? and i.standardVersion = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(code);
		searchParams.add(standardVersion);
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = inspectingIndicatorDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	public InspectingIndicator getInspectingIndicator(Long id){
		return inspectingIndicatorDao.get(id);
	}
	
	/**
	  * 方法名:根据物料编码获取检验标准 
	  * <p>功能说明：</p>
	  * @param materialCode
	  * @return
	 */
	public InspectingIndicator getInspectingIndicator(String materialCode){
		String hql = "from InspectingIndicator i where i.materielCode = ?";
		List<InspectingIndicator> indicators = inspectingIndicatorDao.find(hql,materialCode);
		if(indicators.isEmpty()){
			return null;
		}else{
			return indicators.get(0);
		}
	}
	/**
	 * 保存物料名称
	 * @param 
	 */
	public void saveInspectingIndicator(InspectingIndicator inspectingIndicator){
		if(isExistInspectingIndicator(inspectingIndicator.getId(),inspectingIndicator.getMaterielCode(),inspectingIndicator.getStandardVersion())){
			throw new AmbFrameException("已经相同的检验物料!");
		}
		if(!ItemIndicatorManager.isStandardVersion(inspectingIndicator.getStandardVersion())){
			throw new AmbFrameException("版本号【"+inspectingIndicator.getStandardVersion()+"】格式不正确,正确格式如:V1.0!");
		}
		inspectingIndicatorDao.save(inspectingIndicator);
		//保存附件,禁用原有的附件
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),inspectingIndicator.getAttachmentFiles());
	}
	
	/**
	 * 修改物料名称
	 * @param 
	 */
	public void updateInspectingIndicator(InspectingIndicator inspectingIndicator){
		inspectingIndicatorDao.save(inspectingIndicator);
	}
	
	/**
	 * 删除物料名称
	 * @param id
	 */
	public void deleteInspectingIndicator(String deleteIds){
		String[] ids = deleteIds.split(",");
		Map<String,String> productNameMap = new HashMap<String, String>();
		for(String id : ids){
			InspectingIndicator inspectingIndicator = inspectingIndicatorDao.get(Long.valueOf(id));
			if(inspectingIndicator.getId() != null){
				inspectingIndicatorDao.delete(inspectingIndicator);
				//保存更记录记录,修改标准的最新标志
				productNameMap.put(inspectingIndicator.getMaterielCode(), inspectingIndicator.getMaterielCode());
				//取消附件的使用
				useFileManager.useAndCancelUseFiles(inspectingIndicator.getAttachmentFiles(),null);
				//删除对应的检验文件
				if(inspectingIndicator.getIndicatorAttachId() != null){
					String hql = "delete from IndicatorAttach i where i.id = ?";
					inspectingIndicatorDao.batchExecute(hql,inspectingIndicator.getIndicatorAttachId());
				}
			}
		}
		for(String materialCode:productNameMap.keySet()){
			indicatorManager.updateMaxVersionFlag(materialCode);
		}
	}
	/**
	 * 根据物料获取 检验项目
	 * @param productBom
	 * @return
	 */
	public InspectingIndicator getAllInspectingIndicatorsByProductBom(String code){
		String hql = "from InspectingIndicator i where i.materielCode = ? and i.companyId = ?";
		List<InspectingIndicator> list = inspectingIndicatorDao.find(hql,new Object[]{code,ContextUtils.getCompanyId()});
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	/**
	 * 获取顶级检验项目-物料名称
	 * @return
	 */
	public List<InspectingIndicator> getTopInspectingIndicators(){
		return inspectingIndicatorDao.getTopInspectingIndicators();
	}
	// 封装不良细项结果数据集的JSON格式
	public String getResultJson(Page<Object> page) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (Object o : page.getResult()) {
			HashMap<String, Object> hs = new HashMap<String, Object>();
			StringBuffer sb = new StringBuffer();
			sb.append(JsonParser.object2Json(o));
			sb.delete(sb.length() - 1, sb.length());
			sb.append(",");
			sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
			JSONObject jObject = JSONObject.fromObject(sb.toString());
			list.add(jObject);
		}
		// 添加jqGrid所需的页信息
		StringBuilder json = new StringBuilder();
		json.append("{\"page\":\"");
		json.append(page.getPageNo());
		json.append("\",\"total\":");
		json.append(page.getTotalPages());
		json.append(",\"records\":\"");
		json.append(page.getTotalCount());
		json.append("\",\"rows\":");
		json.append(JSONArray.fromObject(list).toString());
		json.append("}");
		return json.toString();
	}
}
