package com.ambition.iqc.inspectionreport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.MaterielTypeLevel;
import com.ambition.iqc.inspectionreport.dao.MaterielTypeLevelDao;

/**
 * 类名:MaterielTypeLevelManager.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-3-3 上午11:20:10
 * </p>
 */
@Service
@Transactional
public class MaterielTypeLevelManager {
	@Autowired
	private MaterielTypeLevelDao  materielTypeLevelDao;
	
    /**
     * 方法名: 
     * <p>功能说明：</p>
     * 创建人:wuxuming 日期： 2015-3-3 version 1.0
     * @param 
     * @return
     */
    public List<MaterielTypeLevel> getTopMaterielTypeLevel(){
        return materielTypeLevelDao.getTopMaterielTypeLevel();
    }
    
    public MaterielTypeLevel getMaterielTypeLevel(Long id){
        return materielTypeLevelDao.get(id);
    }
}
