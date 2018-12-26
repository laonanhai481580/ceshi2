package com.ambition.doc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.doc.dao.DocumentDao;
import com.ambition.doc.entity.DocumentFiles;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  林少伟
 * @version 1.00 2014-6-6 发布
 */
@Service
@Transactional
public class DocumentManager {

    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private UseFileManager useFileManager;
    /**
          * 方法名: 
          * <p>功能说明：</p>
          * @param calendar
          * @return
         */
    public void deleteDocument(String deleteIds) {
        String[] ids = deleteIds.split(",");
        for(String id:ids){
            DocumentFiles document = documentDao.get(Long.valueOf(id));
            documentDao.delete(document);
            useFileManager.useAndCancelUseFiles(document.getFileSystem(), null);
        }
    }
    /**
          * 方法名: 
          * <p>功能说明：</p>
          * @param calendar
          * @return
         */
    public DocumentFiles deleteDocument(Long id) {
        // TODO Auto-generated method stub
        return documentDao.get(id);
    }
    /**
          * 方法名: 
          * <p>功能说明：</p>
          * @param calendar
          * @return
         */
    public void saveDocument(DocumentFiles document) {
        documentDao.save(document);
        useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFilesG"),document.getFileSystem());
    }
    /**
          * 方法名: 
          * <p>功能说明：</p>
          * @param calendar
          * @return
         */
    public Page<DocumentFiles> getDocument(Page<DocumentFiles> page) {
        // TODO Auto-generated method stub
        return documentDao.searchPageByHql(page," from DocumentFiles m");
    }

}
