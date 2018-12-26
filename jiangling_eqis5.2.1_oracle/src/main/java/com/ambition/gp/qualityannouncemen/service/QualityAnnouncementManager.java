package com.ambition.gp.qualityannouncemen.service;


import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gp.entity.QualityAnnouncement;
import com.ambition.gp.qualityannouncemen.dao.QualityAnnouncementDao;
import com.ambition.util.useFile.entity.UseFile;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

/**    
* 质量公告SERVICE
* @authorBy wlongfeng
*
*/

@Service
@Transactional
public class QualityAnnouncementManager {
	@Autowired
	private QualityAnnouncementDao qualityAnnouncementDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public QualityAnnouncement getQualityAnnouncement(Long id){
		return qualityAnnouncementDao.get(id);
	}
	
	public void saveQualityAnnouncement(QualityAnnouncement qualityAnnouncement) throws Exception{
		if(qualityAnnouncement.getIsRelease() == "0"){
			qualityAnnouncement.setReleaseStatus("未发布");
		}else if(qualityAnnouncement.getIsRelease() == "1"){
			qualityAnnouncement.setReleaseStatus("已发布");
		}
		/*
		//TODO:删除原来保存的文件
		if(qualityAnnouncement.getContentHtmlFileId()!=null){
			UseFile useFile = useFileManager.findById(qualityAnnouncement.getContentHtmlFileId());
			if(useFile != null){
				useFileManager.delete(useFile);
			}
			qualityAnnouncement.setContentHtmlFileId(null);
		}
		//保存内容到硬盘,以处理文件过大导致内容超出长度限制
		if(StringUtils.isNotEmpty(qualityAnnouncement.getShowContentHtml())){
			File file = null;
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				String tempName = "质量公告-" + UUID.randomUUID().toString()+".dat";
				file = new File(tempName);
				outputStream = new FileOutputStream(file);
				outputStream.write(qualityAnnouncement.getShowContentHtml().getBytes("UTF-8"));
				outputStream.close();
				outputStream = null;
				
//				inputStream = new FileInputStream(file);
				UseFile useFile = useFileManager.saveFile(file,tempName);
				qualityAnnouncement.setContentHtmlFileId(useFile.getId());
			} finally{
				if(outputStream != null){
					outputStream.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
				if(file != null && file.exists()){
					file.delete();
				}
			}
		}*/
		qualityAnnouncement.setModifiedTime(new Date());
		qualityAnnouncement.setModifier(ContextUtils.getUserName());
		qualityAnnouncement.setModifierName(ContextUtils.getUserName());
		qualityAnnouncementDao.save(qualityAnnouncement);
		if(qualityAnnouncement.getIsRelease() == "1"){
			String emailContent = "您好! 在QIS系统中您有一条公告信息需要尽快查阅,请知悉!  "
					+"  标题: ["+qualityAnnouncement.getTitle()+"],"
					+"  质量公告内容:["+ qualityAnnouncement.getShowContentHtml()+"],"
					+"  发布组织: ["+qualityAnnouncement.getTerritorial()+"],"
					+"  发布人:  ["+qualityAnnouncement.getPublisher()+"],"
					+"  发布日期: ["+qualityAnnouncement.getReleaseTime()+"]";
			Set<String> names=new HashSet<String>();
			if (StringUtils.isNotEmpty(qualityAnnouncement.getSendMail())) {
				String[] sendMails = qualityAnnouncement.getSendMail().split(",");
				for (int i = 0; i < sendMails.length; i++) {
	//				int begin=sendMails[i].indexOf("(");
	//				names.add(sendMails[i].substring(0,begin));
					names.add(sendMails[i]);
				}
			}
			for(Iterator<String> nameIte = names.iterator(); nameIte.hasNext();) {
				String name= nameIte.next().toString();
				List<User> user=ApiFactory.getAcsService().getUsersByName(name);
				if(user.size()!=0){
					String email = user.get(0).getEmail();
					if(StringUtils.isNotEmpty(email)){
						AsyncMailUtils.sendMail(email,"质量公告！",emailContent.toString());
					}
				}
				
			}
		}
	}
	/**
	  * 方法名:获取文件内容
	  * <p>功能说明：
	  * 如果是保存到硬盘,从硬盘中读取
	  * 否则返回内容HTML
	  * </p>
	  * @return
	 * @throws Exception 
	 */
	public String getContentHtml(QualityAnnouncement qualityAnnouncement) throws Exception{
		if(qualityAnnouncement.getContentHtmlFileId()!=null){
			UseFile useFile = useFileManager.findById(qualityAnnouncement.getContentHtmlFileId());
			if(useFile != null){
				ByteOutputStream byteOutputStream = null;
				try {
					byteOutputStream = new ByteOutputStream();
//					useFileManager.writeFromUseFile(useFile, byteOutputStream);
					return new String(byteOutputStream.getBytes(),"UTF-8");
				}catch(Exception ex){
					Logger.getLogger(this.getClass()).error("读取文件出错!",ex);
					return qualityAnnouncement.getContentHtml();
				} finally{
					if(byteOutputStream != null){
						byteOutputStream.close();
					}
				}
			}
		}
		//默认返回字段的内容
		return qualityAnnouncement.getContentHtml();
	}
	
	public void deleteQualityAnnouncement(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			logUtilDao.debugLog("删除", qualityAnnouncementDao.get(Long.valueOf(id)).toString());
			qualityAnnouncementDao.delete(Long.valueOf(id));
		}
	}
	
	public void deleteQualityAnnouncement(QualityAnnouncement qualityAnnouncement){
		logUtilDao.debugLog("删除", qualityAnnouncementDao.toString());
		qualityAnnouncementDao.delete(qualityAnnouncement);
	}
	
	
	public List<QualityAnnouncement> listAll(String rows){
		return qualityAnnouncementDao.getAllQualityAnnouncement(rows);
	}
	public List<QualityAnnouncement> getAllQualityAnnouncementByState(String state,Long id){
		return qualityAnnouncementDao.getAllQualityAnnouncementByState(state,id);
	}
	
	public Page<QualityAnnouncement> search(Page<QualityAnnouncement> page) {
		return qualityAnnouncementDao.search(page);
	}

	public Page<QualityAnnouncement> searchByState(Page<QualityAnnouncement> page,String dept) {
		if("供应商".equals(dept)){
			return page;
		}
		return qualityAnnouncementDao.searchByState(page);
	}
	public Page<QualityAnnouncement> searchByRelease(Page<QualityAnnouncement> page,String search,String announcementType) {
		return qualityAnnouncementDao.searchByRelease(page,search,announcementType);
	}
	public Page<QualityAnnouncement> searchByRelease(Page<QualityAnnouncement> page,String announcementType) {
		return qualityAnnouncementDao.searchByRelease(page,announcementType);
	}
	/*public void sendNewQualityAnnouncementMail(ProjectPlan projectPlan,QualityAnnouncement qualityAnnouncement) {
		Set<String> names=new HashSet<String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotEmpty(projectPlan.getProjectManager())) {
			String[] projectManagers = projectPlan.getProjectManager().split(",");
			for (int i = 0; i < projectManagers.length; i++) {
				names.add(projectManagers[i]);
			}
		}
		if (StringUtils.isNotEmpty(projectPlan.getProjectCreator())) {
			names.add(projectPlan.getProjectCreator());
		}
		if (StringUtils.isNotEmpty(projectPlan.getProjectMember())) {
			String[] projectMembers = projectPlan.getProjectMember().split(",");
			for (int i = 0; i < projectMembers.length; i++) {
				names.add(projectMembers[i]);
			}
		}
		if (StringUtils.isNotEmpty(projectPlan.getController())) {
			String[] controllers = projectPlan.getController().split(",");
			for (int i = 0; i < controllers.length; i++) {
				names.add(controllers[i]);
			}
		}
		if(projectPlan.getProjectPlanTasks()!=null){
			for(int i=0;i<projectPlan.getProjectPlanTasks().size();i++){
				ProjectPlanTask projectPlanTask=projectPlan.getProjectPlanTasks().get(i);
				if(projectPlanTask.getProjectPlanTaskResources()!=null){
					for(int j=0;j<projectPlanTask.getProjectPlanTaskResources().size();j++){
						ProjectPlanTaskResource ProjectPlanTaskResource=projectPlanTask.getProjectPlanTaskResources().get(j);
						names.add(ProjectPlanTaskResource.getName());
					}
				}
			}
		}
		String releaseDateStr="";
		if(qualityAnnouncement.getReleaseDate()!=null){
			releaseDateStr=sdf.format(qualityAnnouncement.getReleaseDate());
		}
		String emailContent = "发布人:"+qualityAnnouncement.getPublisher() + "\n了项目编号:"
				+ projectPlan.getProjectNo() + "\n批次号:"+qualityAnnouncement.getBatchNo()+"\n项目名称:"
				+ projectPlan.getProjectName() + " \n发布时间:"
				+ releaseDateStr + "\n公告内容:"
				+ qualityAnnouncement.getAnnouncementContent();
 			for(Iterator<String> nameIte = names.iterator(); nameIte.hasNext();) { 
				String name= nameIte.next().toString();
				List<User> user=ApiFactory.getAcsService().getUsersByName(name);
				if(user!=null&&user.size()>0){
					String email = user.get(0).getEmail();
					if(StringUtils.isNotEmpty(email)){
						AsyncMailUtils.sendMail(email,"项目公告！",emailContent.toString());
					}
				}
				
			}
	}*/

	/**
	  * 方法名:置顶标识
	  * <p>功能说明：</p>
	  * @return
	 */
	public void goTop(String ids){
		//查询最大的topFlag
		String hql = "select max(topFlag) from QualityAnnouncement s";
		List<?> list = qualityAnnouncementDao.createQuery(hql).list();
		long max = 0;
		if(list.size()>0&&list.get(0)!=null){
			max = Long.valueOf(list.get(0).toString());
		}
		max++;
		
		String updateHql = "update QualityAnnouncement set topFlag = ? where id in (" + ids + ")";
		qualityAnnouncementDao.createQuery(updateHql,max).executeUpdate();
	}
}			
				
	
