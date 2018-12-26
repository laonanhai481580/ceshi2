package com.ambition.iqc.inspectionreport.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.MaterielTypeLevel;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:MaterielTypeLevelDao.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-3-3 上午11:24:36
 * </p>
 */
@Repository
public class MaterielTypeLevelDao extends HibernateDao<MaterielTypeLevel, Long>{
	
    /**
     * 方法名: 
     * <p>功能说明：</p>
     * 创建人:wuxuming 日期： 2015-3-3 version 1.0
     * @param 
     * @return
     */
    public List<MaterielTypeLevel> getTopMaterielTypeLevel(){
        String hql = "from MaterielTypeLevel c where c.materielTyepParent is null and c.companyId=? order by c.materielLevel";
        return find(hql,ContextUtils.getCompanyId());
    }
}
