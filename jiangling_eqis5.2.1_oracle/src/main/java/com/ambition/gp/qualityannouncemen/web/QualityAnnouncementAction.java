package com.ambition.gp.qualityannouncemen.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gp.entity.QualityAnnouncement;
import com.ambition.gp.qualityannouncemen.service.QualityAnnouncementManager;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.useFile.entity.UseFile;
import com.ambition.util.useFile.service.UseFileManager;
import com.ibm.icu.text.SimpleDateFormat;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * 质量公告ACTION
 * @authorBy wlongfeng
 *
 */
@Namespace("/gp/quality-announcement")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gp/quality-announcement", type = "redirectAction") })
public class QualityAnnouncementAction extends BaseAction<QualityAnnouncement> {

	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private JSONObject params;
	private QualityAnnouncement qualityAnnouncement;
	@Autowired
	private QualityAnnouncementManager qualityAnnouncementManager;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private AcsUtils acsUtils;
	
	private Page<QualityAnnouncement> page;
	
	private File imgFile;
	
	private String imgFileFileName;
	
	private String imgWidth;
	
	private String imgHeight;
	
	private String align;
	
	private String imgTitle;
	
	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setPage(Page<QualityAnnouncement> page) {
		this.page = page;
	}
	
	public Page<QualityAnnouncement> getPage() {
		return page;
	}
	
	public QualityAnnouncement getModel() {
		return qualityAnnouncement;
	}
	
	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}


	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileFileName() {
		return imgFileFileName;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public String getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getImgTitle() {
		return imgTitle;
	}

	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			qualityAnnouncement=new QualityAnnouncement();
			qualityAnnouncement.setCreatedTime(new Date());
			qualityAnnouncement.setCompanyId(ContextUtils.getCompanyId());
			qualityAnnouncement.setCreator(ContextUtils.getUserName());
			qualityAnnouncement.setLastModifiedTime(new Date());
			qualityAnnouncement.setLastModifier(ContextUtils.getUserName());
		}else {
			qualityAnnouncement=qualityAnnouncementManager.getQualityAnnouncement(id);
		}
//		String _releaseDate=Struts2Utils.getParameter("_releaseDate");
//		if(StringUtils.isNotEmpty(_releaseDate)){
//			qualityAnnouncement.setReleaseDate(DateUtil.parseDate(_releaseDate));
//		}
	}
	
	
	@Action("save")
	@Override
	@LogInfo(optType="保存",message="保存公告")
	public String save() throws Exception {
		JSONObject result = new JSONObject();
		String cacheKey = "gp_quality_content";
		try {
			String isStart = Struts2Utils.getParameter("isStart");
			String showContentHtml = null;
			String releaseTime = Struts2Utils.getParameter("releaseTime");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if("true".equals(isStart)){
				showContentHtml = "";
			}else{
				showContentHtml = (String)Struts2Utils.getSession().getAttribute(cacheKey);
			}
			if(showContentHtml==null){
				showContentHtml="";
			}
			showContentHtml += Struts2Utils.getParameter("showContentHtml");
			if(Struts2Utils.getParameter("showContentHtml")==null){
				throw new RuntimeException("缺少填写公告内容!请刷新后重试!");
			}
			String isEnd = Struts2Utils.getParameter("isEnd");
			if("true".equals(isEnd)){
				qualityAnnouncement.setReleaseTime(sdf.parse(releaseTime));
				qualityAnnouncement.setShowContentHtml(showContentHtml);
				qualityAnnouncement.setContent(showContentHtml);
				qualityAnnouncementManager.saveQualityAnnouncement(qualityAnnouncement);
				result.put("success",true);
				result.put("id",qualityAnnouncement.getId());
				//移除缓存
				Struts2Utils.getSession().removeAttribute(cacheKey);
			}else{
				Struts2Utils.getSession().setAttribute(cacheKey,showContentHtml);
			}
		} catch (Exception e) {
			log.error("保存失败!",e);
			result.put("error",true);
			result.put("message","保存失败," + e.getMessage());
			//移除缓存
			Struts2Utils.getSession().removeAttribute(cacheKey);
		}
		renderText(result.toString());
		return null;
	}
	
	//发布
	@Action("release")
	@LogInfo(optType="保存",message="发布公告")
	public String release() throws Exception {
		try {
			if (id != null) {
//				qualityAnnouncement.setLastModifiedTime(new Date());
//				qualityAnnouncement.setLastModifier(ContextUtils.getUserName());
				prepareModel();
				qualityAnnouncement.setIsRelease("1");
				qualityAnnouncement.setReleaseTime(new Date());
				logUtilDao.debugLog("修改", qualityAnnouncement.toString());
			} 
			qualityAnnouncementManager.saveQualityAnnouncement(qualityAnnouncement);
			
			//同时置顶
			if("1".equals(Struts2Utils.getParameter("topFlag"))){
				qualityAnnouncementManager.goTop(id+"");
			}
			
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("message","发布成功！");
			result.put("id",qualityAnnouncement.getId());
			renderText(JSONObject.fromObject(result).toString());
		} catch (Exception e) {
			e.printStackTrace();
			addActionMessage("发布失败:" + e.getMessage());
		}
		return null;
	}

	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		qualityAnnouncementManager.deleteQualityAnnouncement(deleteIds);
		return null;
	}
	

	@Action("input")
	@Override
	public String input() throws Exception {
		if(id!=null){
			QualityAnnouncement qualityAnnouncement=qualityAnnouncementManager.getQualityAnnouncement(id);
			ActionContext.getContext().put("qualityAnnouncement", qualityAnnouncement);
		}
		//属地
		List<Option> territorials = ApiFactory.getSettingService().getOptionsByGroupCode("qms_release_department");
		if(territorials==null){
			territorials = new ArrayList<Option>();
		}
		ActionContext.getContext().put("territorials",territorials);
		//公告类型
		List<Option> announcementTypes = ApiFactory.getSettingService().getOptionsByGroupCode("qms_realease_type");
		if(announcementTypes==null){
			announcementTypes = new ArrayList<Option>();
		}
		ActionContext.getContext().put("announcementTypes",announcementTypes);
		//内容分类
		List<Option> contentTypes = ApiFactory.getSettingService().getOptionsByGroupCode("qms_quality_content_type");
		if(contentTypes==null){
			contentTypes = new ArrayList<Option>();
		}
		ActionContext.getContext().put("contentTypes",contentTypes);
		//显示内容
		try {
			qualityAnnouncement.setShowContentHtml(qualityAnnouncementManager.getContentHtml(qualityAnnouncement));
		} catch (Exception e) {
			log.error("显示内容处理失败!",e);
		}
		return SUCCESS;
	}
	
	
	@Action("view-input")
	public String viewInput() throws Exception {
		if(id!=null){
		QualityAnnouncement qualityAnnouncement=qualityAnnouncementManager.getQualityAnnouncement(id);
		ActionContext.getContext().put("qualityAnnouncement", qualityAnnouncement);
		}
		return SUCCESS;
	}
	
	@Action("view")
	public String view() throws Exception {
		if(id!=null){
			QualityAnnouncement qualityAnnouncement=qualityAnnouncementManager.getQualityAnnouncement(id);
			try {
				String showContentHtml = qualityAnnouncementManager.getContentHtml(qualityAnnouncement);
				qualityAnnouncement.setShowContentHtml(showContentHtml);
			} catch (Exception e) {
				qualityAnnouncement.setShowContentHtml(qualityAnnouncement.getContentHtml());
				log.error("读取内容失败!",e);
			}
			ActionContext.getContext().getValueStack().push(qualityAnnouncement);
		}
		return SUCCESS;
	}
	
	
	@Action("preview-view")
	public String previewView() throws Exception {
		String releaseTime=Struts2Utils.getParameter("releaseTime");
		String publishOrganization=Struts2Utils.getParameter("publishOrganization");
		String publisher=Struts2Utils.getParameter("publisher");
		String contentHtml=Struts2Utils.getParameter("showContentHtml");
		String attachFile=Struts2Utils.getParameter("attachFile");
		String title=Struts2Utils.getParameter("title");
		QualityAnnouncement qualityAnnouncement=new QualityAnnouncement();
		qualityAnnouncement.setTitle(title);
		qualityAnnouncement.setPublisher(publisher);
		qualityAnnouncement.setPublishOrganization(publishOrganization);
		qualityAnnouncement.setContentHtml(contentHtml);
		qualityAnnouncement.setAttachFile(attachFile);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		qualityAnnouncement.setReleaseTime(sdf.parse(releaseTime));
		ActionContext.getContext().getValueStack().push(qualityAnnouncement);
		return SUCCESS;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	@Action("list-zsk-view")
	public String listZsk() throws Exception {
		return SUCCESS;
	}
	@Action("list-zjk-view")
	public String listZjk() throws Exception {
		return SUCCESS;
	}
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try{
			String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
			page = qualityAnnouncementManager.searchByState(page,dept);
			renderText(PageUtils.pageToJson(page,"GP_QUALITY_ANNOUNCEMENT"));
		}catch (Exception e) {
			log.error("查询失败!",e);
		}
		return null;
		//发布页手动分页---miao
//		String pageNo = Struts2Utils.getParameter("pageNo");
//		page = new Page<QualityAnnouncement>(15);
//		if(pageNo!=null){
//			page.setPageNo(Integer.parseInt(pageNo));
//		}
//		page = qualityAnnouncementManager.searchByState(page);
//		List<QualityAnnouncement> list = page.getResult();
//		ActionContext.getContext().put("list",list);
//		return SUCCESS;
	}
	@Action("list-view")
	public String listView() throws Exception {
		return SUCCESS;
	}
	@Action("list-view-datas")
	public String getListView() throws Exception {
		String pageNoStr = Struts2Utils.getParameter("pageNo");
		Integer pageNo = 1;
		if(CommonUtil1.isInteger(pageNoStr)){
			pageNo = Integer.valueOf(pageNoStr);
		}
		String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
		//供应商端不查询
//		if("供应商".equals(dept)){
			String search = Struts2Utils.getParameter("search");
			String announcementType = Struts2Utils.getParameter("announcementType");
			if(search!=null&&!"".equals(search)){
				page = new Page<QualityAnnouncement>(100000);
				page = qualityAnnouncementManager.searchByRelease(page,search,announcementType);
			}else{
				page = new Page<QualityAnnouncement>(100000);
				page = qualityAnnouncementManager.searchByRelease(page,announcementType);
			}
//		}else{
//			page = new Page<QualityAnnouncement>(65535);
//			page.setResult(new ArrayList<QualityAnnouncement>());
//		}
		List<QualityAnnouncement> lists = page.getResult();
		Integer pages = (lists.size()/15);
		if(lists.size()>0&&pages==0){
			pages=1;
		}else if(lists.size()> (pages*15-1)){
			pages=pages+1;
		}
		List<QualityAnnouncement> list = new ArrayList<QualityAnnouncement>();
		for(int i=((pageNo-1)*15);i<lists.size()&&i<pageNo*15;i++){
			list.add(lists.get(i));
		}
		ActionContext.getContext().put("list",lists);
		ActionContext.getContext().put("pages",pages);
		return SUCCESS;
	}
	
//	@Action("upload-image")
//	public String uploadImage() throws Exception {
//		HttpServletRequest request = Struts2Utils.getRequest();
//		HttpServletResponse response = Struts2Utils.getResponse();
//		//文件保存目录路径
//		String savePath = request.getSession().getServletContext().getRealPath("/") + "/attached/";
//
//		//文件保存目录URL
//		String saveUrl  = request.getContextPath() + "/attached/";
//
//         // 定义允许上传的文件扩展名
//          String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
//         // 最大文件大小
//         long maxSize = 1000000;
//        PrintWriter out = null;
//         try {
//             out = response.getWriter();
//         } catch (IOException e1) {
//             log.error(e1);
//         }
// 
//         if (imgFile == null) {
//             out.println(getError("请选择文件。"));
//             return null;
//         }
// 
//         // 检查目录
//         File uploadDir = new File(savePath);
//         if (!uploadDir.isDirectory()) {
//             out.println(getError("上传目录不存在。"));
//             return null;
//         }
//         // 检查目录写权限
//         if (!uploadDir.canWrite()) {
//             out.println(getError("上传目录没有写权限。"));
//             return null;
//         }
//         // 创建文件夹
//         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//         String ymd = sdf.format(new Date());
//         savePath += ymd + "/";
//         saveUrl += ymd + "/";
//         File dirFile = new File(savePath);
//         if (!dirFile.exists()) {
//             dirFile.mkdirs();
//         }
//         String fileExt = imgFileFileName.substring(imgFileFileName.lastIndexOf(".") + 1).toLowerCase();
//         if (!Arrays.<String> asList(fileTypes).contains(fileExt)) {
//             out.println(getError("上传文件扩展名[" + fileExt + "]是不允许的扩展名。"));
//             return null;
//         }
//         if (imgFile.length() > maxSize) {
//             out.println(getError("[ " + imgFileFileName + " ]超过单个文件大小限制，文件大小[ " + imgFile.length() + " ]，限制为[ " + maxSize + " ] "));
//             return null;
//         }
//         SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//         String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
//         File uploadedFile = new File(savePath, newFileName);
//         try {
//             FileUtil.copyFile(imgFile, uploadedFile);
//             JSONObject obj = new JSONObject();
//             obj.put("error", 0);
//             obj.put("url", saveUrl + newFileName);
//             log.debug(obj);
//             out.println(obj.toString());
//             log.debug("上传图片:[" + uploadedFile.getName() + "]" + ">>>[" + newFileName + "]成功");
//         } catch (IOException e) {
//             log.error("图片上传失败:" + e);
//         }
//         return null;
//	}
	
	@Action("upload-image")
	public String uploadImage() throws Exception {
		HttpServletResponse response = Struts2Utils.getResponse();
        // 定义允许上传的文件扩展名
        String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
        // 最大文件大小
        long maxSize = 1000000;
        PrintWriter out = response.getWriter();
        if (imgFile == null) {
           out.println(getError("请选择文件。"));
             return null;
        }
        String fileExt = imgFileFileName.substring(imgFileFileName.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.<String> asList(fileTypes).contains(fileExt)) {
        	out.println(getError("上传文件扩展名[" + fileExt + "]是不允许的扩展名。"));
        	return null;
        }
        if (imgFile.length() > maxSize) {
        	out.println(getError("[ " + imgFileFileName + " ]超过单个文件大小限制，文件大小[ " + imgFile.length() + " ]，限制为[ " + maxSize + " ] "));
        	return null;
       	}
        try {
        	 UseFile useFile = useFileManager.saveFile(imgFile,imgFileFileName);
             JSONObject obj = new JSONObject();
             obj.put("error", 0);
             String url = PropUtils.getProp("host.app") + "/carmfg/common/download.htm?id=" + useFile.getId();
             obj.put("url",url);
             log.debug(obj);
             out.println(obj.toString());
             log.debug("上传图片:[" + imgFileFileName + "]成功");
         } catch (IOException e) {
             log.error("图片上传失败:"+e.getMessage(),e);
         }
         return null;
	}
	
	
	private String getError(String message) {
         JSONObject obj = new JSONObject();
         obj.put("error", 1);
         obj.put("message", message);
         log.debug(obj);
         return obj.toString();
	}
	
	
	/**
	  * 方法名:置顶
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("go-top")
	public String goTop() throws Exception {
		try {
			qualityAnnouncementManager.goTop(Struts2Utils.getParameter("ids"));
			JSONObject json = new JSONObject();
			json.put("message","置顶成功！");
			renderText(json.toString());
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put("error",true);
			json.put("message","置顶失败," + e.getMessage());
			renderText(json.toString());
			log.error("置顶失败",e);
		}
		return null;
	}
	
}
