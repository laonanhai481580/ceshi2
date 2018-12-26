package com.ambition.supplier.admittance.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.admittance.dao.AdmittanceFlowConfigureDao;
import com.ambition.supplier.entity.AdmittanceFlowConfigure;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;

/**
 * 类名:准入流程配置业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：准入流程配置增加,修改,删除</p>
 * @author  赵骏
 * @version 1.00 2013-4-20 发布
 */
@Service
@Transactional
public class AdmittanceFlowConfigureManager {
	@Autowired
	private AdmittanceFlowConfigureDao flowConfigureDao;
	
	/**
	  * 方法名: 查询准入配置
	  * <p>功能说明：分页查询准入配置</p>
	  * @param page
	  * @return
	 */
	public Page<AdmittanceFlowConfigure> search(Page<AdmittanceFlowConfigure> page){
		return flowConfigureDao.searchPageByHql(page,"from AdmittanceFlowConfigure a");
	}
	
	/**
	  * 方法名: 获取流程配置规则
	  * <p>功能说明：根据是否新供应商和物料类别查询对应的准入规则</p>
	  * @param isNewSupplier
	  * @param marialType
	  * @return
	 */
	public AdmittanceFlowConfigure getAdmittanceFlowConfigure(boolean isNewSupplier,String materialType){
		String hql = "from AdmittanceFlowConfigure a where a.isNewSupplier = ? and a.materialType = ?";
		List<AdmittanceFlowConfigure> admittanceFlowConfigures = flowConfigureDao.find(hql,new Object[]{isNewSupplier,materialType});
		//如果为空,返回默认的配置规则
		if(admittanceFlowConfigures.isEmpty()){
			AdmittanceFlowConfigure flowConfigure = new AdmittanceFlowConfigure();
			flowConfigure.setIsNewSupplier(isNewSupplier);
			flowConfigure.setMaterialType(materialType);
			return flowConfigure;
		}else{
			return admittanceFlowConfigures.get(0);
		}
	}
	
	/**
	 * 检查是否存在相同的准入规则
	 * @param id
	 * @param name
	 * @return
	 */
	private void isExistFlowConfigure(AdmittanceFlowConfigure configure){
		String hql = "select count(*) from AdmittanceFlowConfigure a where a.isNewSupplier = ? and a.materialType = ?";
		List<?> list = null;
		if(configure.getId() == null){
			list = flowConfigureDao.find(hql,configure.getIsNewSupplier(),configure.getMaterialType());
		}else{
			list = flowConfigureDao.find(hql + " and a.id <> ?",configure.getIsNewSupplier(),configure.getMaterialType(),configure.getId());
		}
		if(Integer.valueOf(list.get(0).toString())>0){
			throw new AmbFrameException("已经存在相同的配置:是否新供应商为【"+configure.getIsNewSupplier()+"】,物料类别为【"+configure.getMaterialType()+"】!");
		}
	}
	/**
	  * 方法名:根据ID获取流程配置
	  * @param id
	  * @return
	 */
	public AdmittanceFlowConfigure getFlowConfigure(Long id){
		return flowConfigureDao.get(id);
	}
	
	/**
	  * 方法名:保存准入流程设置
	  * <p>功能说明：是否新供应商和物料类别不能为空,检查是否新供应商和物料类别是否重复</p>
	  * @param gradeRule
	 */
	public void saveAdmittanceFlowConfigure(AdmittanceFlowConfigure configure){
		if(configure.getIsNewSupplier()==null){
			throw new AmbFrameException("是否新供应商不能同时为空!");
		}else if(StringUtils.isEmpty(configure.getMaterialType())){
			throw new AmbFrameException("物料类别不能为空!");
		}else if(!configure.getNeedSampleAppraisal()&&!configure.getNeedSublotsAppraisal()){
			throw new AmbFrameException("样件鉴定和小批鉴定必须选择一项!");
		}else if(configure.getNeedSampleAppraisal()){
			if(configure.getMinSampleAppraisal()!=null
					&&configure.getMaxSampleAppraisal()!=null
					&&configure.getMinSampleAppraisal()>configure.getMaxSampleAppraisal()){
				throw new AmbFrameException("样件鉴定的保底次数不能大于样件鉴定的上限次数!");
			}
		}else if(configure.getNeedSublotsAppraisal()){
			if(configure.getMinSublotsAppraisal()!=null
					&&configure.getMaxSublotsAppraisal()!=null
					&&configure.getMinSublotsAppraisal()<configure.getMaxSublotsAppraisal()){
				throw new AmbFrameException("小批鉴定的保底次数不能大于小批鉴定的上限次数!");
			}
		}
		isExistFlowConfigure(configure);
		flowConfigureDao.save(configure);
	}
	
	/**
	  * 方法名:删除准入流程配置
	  * <p>功能说明：删除准入流程配置</p>
	  * @param deleteIds
	 */
	public void deleteAdmittanceFlowConfigure(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			AdmittanceFlowConfigure configure = flowConfigureDao.get(Long.valueOf(id));
			if(configure.getId() != null){
				flowConfigureDao.delete(configure);
			}
		}
	}
}
