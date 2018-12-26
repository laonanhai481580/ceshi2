package com.ambition.gp.qualityannouncemen.dao;


import java.util.List;

import javax.persistence.OrderBy;

import org.apache.commons.lang.xwork.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.QualityAnnouncement;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**    
* 质量公告DAO
* @authorBy wlongfeng
*
*/
@Repository
public class QualityAnnouncementDao extends HibernateDao<QualityAnnouncement, Long> {
		
	
	public List<QualityAnnouncement> getAllQualityAnnouncement(String rows){
		String hql="from QualityAnnouncement p where p.state=? order by p.releaseDate desc";
		Query query = this.createQuery(hql);
		query.setParameter(0,"审核通过");
		query.setMaxResults(Integer.parseInt(rows));
		@SuppressWarnings("unchecked")
		List<QualityAnnouncement> list = query.list();
		return list;
	}
	public List<QualityAnnouncement> getAllQualityAnnouncementByState(String state,Long id){
		return find("from QualityAnnouncement p where p.state=? and p.projectPlan.id=? order by releaseDate desc",state,id);
	}
	public Page<QualityAnnouncement> search(Page<QualityAnnouncement> page) {
        return searchPageByHql(page, "from QualityAnnouncement p where p.companyId=?",ContextUtils.getCompanyId());
    }
	public Page<QualityAnnouncement> searchByState(Page<QualityAnnouncement> page) {
		String hql = "from QualityAnnouncement p where p.companyId=?";
		if(StringUtils.isEmpty(page.getOrderBy())){
			hql += " order by p.topFlag desc,p.releaseTime desc";
		}
		return searchPageByHql(page,hql,ContextUtils.getCompanyId());
	}
	public Page<QualityAnnouncement> searchByRelease(Page<QualityAnnouncement> page,String search,String announcementType) {
		String sql = "from QualityAnnouncement p where p.announcementType = ? and title like ('%"+search+"%') or territorial like ('%"+search+"%')"
				+ " or content like ('%"+search+"%') or publisher like ('%"+search+"%') and isRelease='1'";
		sql += " and p.companyId=?";
		if(StringUtils.isEmpty(page.getOrderBy())){
			sql += " order by p.topFlag desc,p.releaseTime desc";
		}
		return searchPageByHql(page,sql,announcementType,ContextUtils.getCompanyId());
	}
	public Page<QualityAnnouncement> searchByRelease(Page<QualityAnnouncement> page,String announcementType) {
		String hql = "from QualityAnnouncement p where p.announcementType = ? and isRelease='1'";
		if(StringUtils.isEmpty(page.getOrderBy())){
			hql += " order by p.topFlag desc,p.releaseTime desc";
		}
		return searchPageByHql(page,hql,announcementType);
	}
	
}
