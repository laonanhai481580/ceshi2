package com.ambition.carmfg.inspectionbase.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.ambition.carmfg.entity.PatrolSettings;
import com.ambition.carmfg.inspectionbase.dao.MfgInspectingIndicatorDao;
import com.ambition.iqc.inspectionbase.service.ItemIndicatorManager;
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
public class MfgInspectingIndicatorManager {
	@Autowired
	private MfgInspectingIndicatorDao mfgInspectingIndicatorDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private MfgItemIndicatorManager mfgItemIndicatorManager;
	/**
	  * 方法名: 根据工序和物料编码获取检验标准
	  * <p>功能说明：</p>
	  * @param workcedure
	  * @param materialCode
	  * @return
	 */
	public MfgInspectingIndicator getInspectingIndicator(String workcedure,String materialCode){
		String hql = "from MfgInspectingIndicator m where m.workingProcedure = ? and m.materielCode=?";
		List<MfgInspectingIndicator> indicators = mfgInspectingIndicatorDao.find(hql,workcedure,materialCode);
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
	public Page<MfgInspectingIndicator> listAll(Page<MfgInspectingIndicator> page,String workingProcedure,String materielCode){
		String hql = "from MfgInspectingIndicator i where i.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		//如果查询条件为空时,物料代码不为空,按照物料来查
		String searchParamters = Struts2Utils.getParameter("searchParameters");
		if(searchParamters==null&&StringUtils.isNotEmpty(workingProcedure)&&StringUtils.isNotEmpty(materielCode)){
			hql += " and i.materielCode = ? and i.workingProcedure = ?";
			searchParams.add(materielCode);
			searchParams.add(workingProcedure);
		}
		return mfgInspectingIndicatorDao.searchPageByHql(page,hql,searchParams.toArray());
	}
	
	/**
	  * 方法名: 查询最新版本的检验标准
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<MfgInspectingIndicator> listMaxVersion(Page<MfgInspectingIndicator> page){
		return mfgInspectingIndicatorDao.listMaxVersion(page);
	}
	
	public MfgInspectingIndicator getInspectingIndicator(Long id){
		return mfgInspectingIndicatorDao.get(id);
	}
	
	/**
	  * 方法名: 保存检验标准
	  * <p>功能说明：</p>
	  * @param mfgInspectingIndicator
	 */
	public void saveInspectingIndicator(MfgInspectingIndicator mfgInspectingIndicator){
		if(mfgItemIndicatorManager.isExistInspectingIndicator(mfgInspectingIndicator.getId(), mfgInspectingIndicator.getMaterielCode(),mfgInspectingIndicator.getWorkingProcedure(), mfgInspectingIndicator.getStandardVersion())){
			throw new AmbFrameException("已经存在相同版本的检验物料和工序!");
		}
/*		if(!ItemIndicatorManager.isStandardVersion(mfgInspectingIndicator.getStandardVersion())){
			throw new AmbFrameException("版本【"+mfgInspectingIndicator.getStandardVersion()+"】格式不正确,正确格式如:V1.0!");
		}*/
		mfgInspectingIndicatorDao.save(mfgInspectingIndicator);
		//保存附件,禁用原有的附件
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),mfgInspectingIndicator.getAttachmentFiles());
	}
	
	/**,
	 * 删除物料名称
	 * @param id
	 */
	public String deleteInspectingIndicator(String deleteIds){
		String[] ids = deleteIds.split(",");
		StringBuilder sb = new StringBuilder("");
		Map<String,List<String>> productNameMap = new HashMap<String, List<String>>();
		for(String id : ids){
			MfgInspectingIndicator mfgInspectingIndicator = mfgInspectingIndicatorDao.get(Long.valueOf(id));
			if(mfgInspectingIndicator.getId() != null){
				try {
					mfgInspectingIndicatorDao.delete(mfgInspectingIndicator);
					sb.append(mfgInspectingIndicator.getModel() + ",");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!productNameMap.containsKey(mfgInspectingIndicator.getMaterielCode())){
					productNameMap.put(mfgInspectingIndicator.getMaterielCode(),new ArrayList<String>());
				}
				productNameMap.get(mfgInspectingIndicator.getMaterielCode()).add(mfgInspectingIndicator.getWorkingProcedure());
				//禁用附件
				useFileManager.useAndCancelUseFiles(mfgInspectingIndicator.getAttachmentFiles(),null);
				//删除对应的检验文件
				if(mfgInspectingIndicator.getIndicatorAttachId() != null){
					String hql = "delete from IndicatorAttach i where i.id = ?";
					mfgInspectingIndicatorDao.batchExecute(hql,mfgInspectingIndicator.getIndicatorAttachId());
				}
			}
		}
		//更新最新检验标准的标志
		for(String materialCode : productNameMap.keySet()){
			List<String> workingProcedures = productNameMap.get(materialCode);
			for(String workingProcedure : workingProcedures){
				mfgItemIndicatorManager.updateMaxVersionFlag(materialCode, workingProcedure);
			}
		}
		return sb.toString();
	}
	/**
	 * 根据物料和工序获取 检验项目
	 * @param productBom
	 * @param workProduce
	 * @return
	 */
	public MfgInspectingIndicator getAllInspectingIndicatorsByProductBomAndWorkProduce(String code,String workProduce){
		String hql = "from MfgInspectingIndicator i where i.materielCode = ? and i.workProduce = ? and i.companyId = ?";
		List<MfgInspectingIndicator> list = mfgInspectingIndicatorDao.find(hql,new Object[]{code,workProduce,ContextUtils.getCompanyId()});
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
	public List<MfgInspectingIndicator> getTopInspectingIndicators(){
		return mfgInspectingIndicatorDao.getTopInspectingIndicators();
	}
	
	/**
	  * 方法名: 保存巡检设置
	  * @param settings
	  * @param selFlag
	  * @param page
	 */
	public void savePatrolSettings(PatrolSettings settings,String selFlag,Page<MfgInspectingIndicator> page){
		List<MfgInspectingIndicator> indicators = null;
		if("select".equals(selFlag)){
			indicators = new ArrayList<MfgInspectingIndicator>();
			String[] ids = Struts2Utils.getParameter("ids").split(",");
			for(String id : ids){
				if(StringUtils.isNotEmpty(id)){
					indicators.add(getInspectingIndicator(Long.valueOf(id)));
				}
			}
		}else{
			page.setPageNo(1);
			page.setPageSize(Integer.MAX_VALUE);
			page = listMaxVersion(page);
			indicators = page.getResult();
		}
		for(MfgInspectingIndicator indicator : indicators){
			indicator.setPatrolSettings(settings);
			mfgInspectingIndicatorDao.save(indicator);
		}
	}

	/**
	 * 根据产品结构查询物料BOM
	 * @return
	 */
	public List<MfgInspectingIndicator> searchMachineNo(JSONObject params){
		return mfgInspectingIndicatorDao.searchMachineNo(params);
	}
}
