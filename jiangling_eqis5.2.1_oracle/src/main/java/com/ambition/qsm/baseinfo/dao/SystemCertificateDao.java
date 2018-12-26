package com.ambition.qsm.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.qsm.entity.SystemCertificate;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:公司证书DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Repository
public class SystemCertificateDao extends HibernateDao<SystemCertificate,Long>{
	public Page<SystemCertificate> list(Page<SystemCertificate> page){
		return findPage(page, "from SystemCertificate d");
	}
	
	public List<SystemCertificate> getAllSystemCertificate(){
		return find("from SystemCertificate d");
	}

    public Page<SystemCertificate> search(Page<SystemCertificate> page) {
        return searchPageByHql(page, "from SystemCertificate d ");
    }
}
