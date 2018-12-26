package com.ambition.doc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  林少伟
 * @version 1.00 2014-6-6 发布
 */
@Entity
@Table(name="DOCUMENT_DOCUMENT")
public class DocumentFiles extends IdEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String systemname;
    private String fileSystem;
    public String getSystemname() {
        return systemname;
    }
    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }
    public String getFileSystem() {
        return fileSystem;
    }
    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }
    
}
