package com.ambition.si.baseinfo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.si.baseinfo.dao.SiInspectingIndicatorDao;
import com.ambition.si.entity.SiInspectingIndicator;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * InspectionIndicatorManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class SiInspectingIndicatorManager {
	@Autowired
	private SiInspectingIndicatorDao siInspectingIndicatorDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private SiItemIndicatorManager siItemIndicatorManager;
	/**
	  * 方法名: 根据工序和物料编码获取检验标准
	  * <p>功能说明：</p>
	  * @param workcedure
	  * @param materialCode
	  * @return
	 */
	public SiInspectingIndicator getInspectingIndicator(String workcedure,String materialCode){
		String hql = "from SiInspectingIndicator m where m.workingProcedure = ? and m.materielCode=?";
		List<SiInspectingIndicator> indicators = siInspectingIndicatorDao.find(hql,workcedure,materialCode);
		if(indicators.isEmpty()){
			return null;
		}else{
			return indicators.get(0);
		}
	}
	
	/**
	  * 方法名: 查询所有版本的检验标准
	  * <p>功能说明：</p>
	  * @param page
	  * @param workingProcedure 工序
	  * @param materielCode 物料编码
	  * @return
	 */
	public Page<SiInspectingIndicator> listAll(Page<SiInspectingIndicator> page,String workingProcedure,String materielCode){
		String hql = "from SiInspectingIndicator i where i.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		//如果查询条件为空时,物料代码不为空,按照物料来查
		String searchParamters = Struts2Utils.getParameter("searchParameters");
		if(searchParamters==null&&StringUtils.isNotEmpty(workingProcedure)&&StringUtils.isNotEmpty(materielCode)){
			hql += " and i.materielCode = ? and i.workingProcedure = ?";
			searchParams.add(materielCode);
			searchParams.add(workingProcedure);
		}
		return siInspectingIndicatorDao.searchPageByHql(page,hql,searchParams.toArray());
	}
	
	/**
	  * 方法名: 查询最新版本的检验标准
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<SiInspectingIndicator> listMaxVersion(Page<SiInspectingIndicator> page){
		return siInspectingIndicatorDao.listMaxVersion(page);
	}
	
	public SiInspectingIndicator getInspectingIndicator(Long id){
		return siInspectingIndicatorDao.get(id);
	}
	
	/**
	  * 方法名: 保存检验标准
	  * <p>功能说明：</p>
	  * @param siInspectingIndicator
	 */
	public void saveInspectingIndicator(SiInspectingIndicator siInspectingIndicator){
		if(siItemIndicatorManager.isExistInspectingIndicator(siInspectingIndicator.getId(), siInspectingIndicator.getMaterielCode(),siInspectingIndicator.getWorkingProcedure(), siInspectingIndicator.getStandardVersion())){
			throw new AmbFrameException("已经存在相同版本的检验物料和工序!");
		}
/*		if(!ItemIndicatorManager.isStandardVersion(siInspectingIndicator.getStandardVersion())){
			throw new AmbFrameException("版本【"+siInspectingIndicator.getStandardVersion()+"】格式不正确,正确格式如:V1.0!");
		}*/
		siInspectingIndicatorDao.save(siInspectingIndicator);
		//保存附件,禁用原有的附件
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),siInspectingIndicator.getAttachmentFiles());
	}
	
	/**,
	 * 删除物料名称
	 * @param id
	 */
	public void deleteInspectingIndicator(String deleteIds){
		String[] ids = deleteIds.split(",");
		Map<String,List<String>> productNameMap = new HashMap<String, List<String>>();
		for(String id : ids){
			SiInspectingIndicator siInspectingIndicator = siInspectingIndicatorDao.get(Long.valueOf(id));
			if(siInspectingIndicator.getId() != null){
				try {
					siInspectingIndicatorDao.delete(siInspectingIndicator);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!productNameMap.containsKey(siInspectingIndicator.getMaterielCode())){
					productNameMap.put(siInspectingIndicator.getMaterielCode(),new ArrayList<String>());
				}
				productNameMap.get(siInspectingIndicator.getMaterielCode()).add(siInspectingIndicator.getWorkingProcedure());
				//禁用附件
				useFileManager.useAndCancelUseFiles(siInspectingIndicator.getAttachmentFiles(),null);
				//删除对应的检验文件
				if(siInspectingIndicator.getIndicatorAttachId() != null){
					String hql = "delete from IndicatorAttach i where i.id = ?";
					siInspectingIndicatorDao.batchExecute(hql,siInspectingIndicator.getIndicatorAttachId());
				}
			}
		}
		//更新最新检验标准的标志
		for(String materialCode : productNameMap.keySet()){
			List<String> workingProcedures = productNameMap.get(materialCode);
			for(String workingProcedure : workingProcedures){
				siItemIndicatorManager.updateMaxVersionFlag(materialCode, workingProcedure);
			}
		}
	}
	/**
	 * 根据物料和工序获取 检验项目
	 * @param productBom
	 * @param workProduce
	 * @return
	 */
	public SiInspectingIndicator getAllInspectingIndicatorsByProductBomAndWorkProduce(String code,String workProduce){
		String hql = "from SiInspectingIndicator i where i.materielCode = ? and i.workProduce = ? and i.companyId = ?";
		List<SiInspectingIndicator> list = siInspectingIndicatorDao.find(hql,new Object[]{code,workProduce,ContextUtils.getCompanyId()});
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
	public List<SiInspectingIndicator> getTopInspectingIndicators(){
		return siInspectingIndicatorDao.getTopInspectingIndicators();
	}
}
