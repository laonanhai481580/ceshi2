package com.ambition.doc.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.doc.entity.DocumentFiles;
import com.ambition.doc.service.DocumentManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  林少伟
 * @version 1.00 2014-6-6 发布
 */
@Namespace("/help")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/help", type = "redirectAction") })
public class DocumentAction extends CrudActionSupport<DocumentFiles>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DocumentFiles document;
    private String deleteIds;
    private Long id;
    @Autowired
    private DocumentManager documentManager;
    @Autowired
    private LogUtilDao logUtilDao;
    private JSONObject params;
    private Page<DocumentFiles> page;
    
    
    public Page<DocumentFiles> getPage() {
        return page;
    }

    public void setPage(Page<DocumentFiles> page) {
        this.page = page;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentFiles getDocument() {
        return document;
    }

    public void setDocument(DocumentFiles document) {
        this.document = document;
    }

    public String getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(String deleteIds) {
        this.deleteIds = deleteIds;
    }

    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    /* (non-Javadoc)
     * @see com.opensymphony.xwork2.ModelDriven#getModel()
     */
    @Override
    public DocumentFiles getModel() {
        // TODO Auto-generated method stub
        return document;
    }

    /* (non-Javadoc)
     * @see com.norteksoft.product.web.struts2.CrudActionSupport#save()
     */
    @Override
    @Action("save")
    @LogInfo(optType="保存",message="系统使用帮助")
    public String save() throws Exception {
        try {
            
            documentManager.saveDocument(document);
            logUtilDao.debugLog("修改", document.toString());
            this.renderText(JsonParser.getRowValue(document));
                
            
        } catch (Exception e) {
            e.printStackTrace();
            createErrorMessage("保存失败：" + e.getMessage());
        }
        return null;
    }
    private void createErrorMessage(String message){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("error",true);
        map.put("message",message);
        renderText(JSONObject.fromObject(map).toString());
    }
    /* (non-Javadoc)
     * @see com.norteksoft.product.web.struts2.CrudActionSupport#delete()
     */
    @Override
    @Action("delete")
    @LogInfo(optType="删除",message="系统使用帮助")
    public String delete() throws Exception {
        if(StringUtils.isEmpty(deleteIds)){
            renderText("删除的对象不存在!");
        }else{
            try {
                documentManager.deleteDocument(deleteIds);
            } catch (Exception e) {
                e.printStackTrace();
                renderText("删除失败:" + e.getMessage());
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.norteksoft.product.web.struts2.CrudActionSupport#list()
     */
    @Override
    @Action("list")
    public String list() throws Exception {
        // TODO Auto-generated method stub
        return SUCCESS;
    }

    @Action("list-datas")
    public String listDatas(){
        try{
//          String dept =acsUtils.getDepartmentsByUser(ContextUtils.getCompanyId(), ContextUtils.getUserId()).get(0).getName();//用户名带出该部门名称
            page = documentManager.getDocument(page);
            this.renderText(PageUtils.pageToJson(page));
        }catch (Exception e) {
            e.printStackTrace();
        }
        logUtilDao.debugLog("查询", "系统使用帮助");
        return null;
    }
    /* (non-Javadoc)
     * @see com.norteksoft.product.web.struts2.CrudActionSupport#input()
     */
    @Override
    public String input() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.norteksoft.product.web.struts2.CrudActionSupport#prepareModel()
     */
    @Override
    protected void prepareModel() throws Exception {
        if(id==null){
            document = new DocumentFiles();
            document.setCreatedTime(new Date());
            document.setCompanyId(ContextUtils.getCompanyId());
            document.setCreator(ContextUtils.getUserName());
            document.setLastModifiedTime(new Date());
            document.setLastModifier(ContextUtils.getUserName());
        }else{
            document = documentManager.deleteDocument(id);
        }
    }

}
